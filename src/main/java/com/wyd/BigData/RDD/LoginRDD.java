package com.wyd.BigData.RDD;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.log4j.helpers.LogLog;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.streaming.flume.SparkFlumeEvent;
import com.wyd.BigData.bean.AccountInfo;
import com.wyd.BigData.bean.LoginInfo;
import com.wyd.BigData.bean.LoginSumInfo;
import com.wyd.BigData.bean.PlayerInfo;
import com.wyd.BigData.dao.BaseDao;
import com.wyd.BigData.util.StringUtil;
import scala.Tuple2;
public class LoginRDD implements Serializable {
    /**
     * 
     */
    private static final long    serialVersionUID = -758442520627154431L;
    static SimpleDateFormat      sf               = new SimpleDateFormat("yyyy_MM_dd");
    private static final Pattern SPACE            = Pattern.compile("\t");

    @SuppressWarnings("serial")
    public  void call(JavaRDD<SparkFlumeEvent> rdd, SparkSession spark) {
        if (rdd.count() == 0) return;
        JavaRDD<SparkFlumeEvent> loingRDD = filter(rdd);
        LogLog.debug("loingRDD=====" + loingRDD.count());
        // 数据源去重
        JavaRDD<Row> loingRowRDD = loingRDD.map(new Function<SparkFlumeEvent, Row>() {
            @Override
            public Row call(SparkFlumeEvent flume) throws Exception {
                String line = new String(flume.event().getBody().array());
                String[] parts = SPACE.split(line);
                return RowFactory.create(Integer.valueOf(parts[2]), Integer.valueOf(parts[3]), Integer.valueOf(parts[5]));
            }
        }).distinct();
        LogLog.debug("count2=====" + loingRowRDD.count());
        final String today = sf.format(Calendar.getInstance().getTime());
        // 查询数据库
        BaseDao dao = BaseDao.getInstance();
        List<LoginSumInfo> loginInfoFromDBList = dao.getAllLoginSumInfo(today);
        if (loginInfoFromDBList.size() > 0) {
            // 把登陆表里的所有数据查出变成RDD，格式是KEY：playerId,Value:true
            JavaSparkContext jsc = new JavaSparkContext(rdd.context());
            List<Tuple2<Integer, Boolean>> userFromDBList = new ArrayList<>();
            for (LoginSumInfo info : loginInfoFromDBList) {
                userFromDBList.add(new Tuple2<Integer, Boolean>(info.getPlayerId(), true));
            }
            JavaPairRDD<Integer, Boolean> userFromDbRDD = jsc.parallelizePairs(userFromDBList);
            // 把数据来源RDD变成KEY：playerId,Value: 数据本身
            JavaPairRDD<Integer, Row> rdd2Pair = loingRowRDD.mapToPair(new PairFunction<Row, Integer, Row>() {
                @Override
                public Tuple2<Integer, Row> call(Row r) throws Exception {
                    return new Tuple2<Integer, Row>(r.getInt(2), r);
                }
            });
            JavaPairRDD<Integer, Tuple2<Row, Optional<Boolean>>> joined = rdd2Pair.leftOuterJoin(userFromDbRDD);
           
            JavaPairRDD<Integer, Tuple2<Row, Optional<Boolean>>> filtered = joined.filter(new Function<Tuple2<Integer, Tuple2<Row, Optional<Boolean>>>, Boolean>() {
                @Override
                public Boolean call(Tuple2<Integer, Tuple2<Row, Optional<Boolean>>> v1) throws Exception {
                    Optional<Boolean> optional = v1._2._2;
                    return  !optional.isPresent() || !optional.get();
                }
            });
            LogLog.debug("count3=====" + filtered.count());
            
            
            JavaPairRDD<Integer, Row> rdd2filtered = filtered.mapToPair(new PairFunction<Tuple2<Integer, Tuple2<Row, Optional<Boolean>>>, Integer, Row>() {
                @Override
                public Tuple2<Integer, Row> call(Tuple2<Integer, Tuple2<Row, Optional<Boolean>>> t) throws Exception {
                    return new Tuple2<Integer, Row>(t._1, t._2._1);
                }
            });
            rdd2filtered.foreachPartition(new VoidFunction<Iterator<Tuple2<Integer, Row>>>() {
                @Override
                public void call(Iterator<Tuple2<Integer, Row>> t) throws Exception {
                    BaseDao dao = BaseDao.getInstance();
                    Row data;
                    List<LoginSumInfo> loginList = new ArrayList<>();
                    while (t.hasNext()) {
                        data = t.next()._2;                        
                        loginList.add(new LoginSumInfo(data.getInt(0), data.getInt(1), data.getInt(1), data.getInt(2)));
                    }
                    dao.saveLoginSumInfoBatch(today, loginList);
                }
            });
        } else {
            loingRowRDD.foreachPartition(new VoidFunction<Iterator<Row>>() {
                @Override
                public void call(Iterator<Row> r) throws Exception {
                    BaseDao dao = BaseDao.getInstance();
                    Row data;
                    List<LoginSumInfo> loginList = new ArrayList<>();
                    while (r.hasNext()) {
                        data = r.next();                        
                        loginList.add(new LoginSumInfo(data.getInt(0), data.getInt(1), data.getInt(1), data.getInt(2)));
                    }
                    dao.saveLoginSumInfoBatch(today, loginList);
                }
            });
        }
        //更新playerInfo,loginInfo
        loingRDD.foreachPartition(new VoidFunction<Iterator<SparkFlumeEvent>>() {
            @Override
            public void call(Iterator<SparkFlumeEvent> t) throws Exception {
                BaseDao dao = BaseDao.getInstance();
                List<PlayerInfo> playerInfoList = new ArrayList<>();
                List<LoginInfo> loginInfoList = new ArrayList<>();
                while (t.hasNext()) {                    
                    String line = new String(t.next().event().getBody().array());
                    String[] datas = SPACE.split(line);
                    int playerId = Integer.parseInt(datas[5]);
                    PlayerInfo playerInfo = dao.getPlayerInfo(playerId);
                    if(playerInfo==null){
                        continue;
                    }
                    Date dataTime = new Date(Long.parseLong(datas[1]));
                    playerInfo.setLoginTime(dataTime);                    
                    playerInfo.setLoginNum(playerInfo.getLoginNum() + 1);
                    LoginInfo login = new LoginInfo();
                    login.setServiceId(Integer.parseInt(datas[2]));
                    login.setChannelId(Integer.parseInt(datas[3]));
                    login.setAccountId(Integer.parseInt(datas[4]));
                    login.setPlayerId(Integer.parseInt(datas[5]));
                    login.setDeviceMac(StringUtil.filterOffUtf8Mb4(datas[6]));
                    login.setDeviceName(StringUtil.filterOffUtf8Mb4(datas[7]));
                    login.setSystemName(StringUtil.filterOffUtf8Mb4(datas[8]));
                    login.setPlayerChannel(playerInfo.getChannelId());
                    if (datas.length > 9) {
                        login.setSystemVersion(datas[9]);
                        login.setAppVersion(datas[10]);
                    }
                    login.setLoginTime(dataTime);
                    if (datas.length > 11) {
                        login.setLoginIp(datas[11]);
                    }
                    if (datas.length > 12) {
                        int diamond=Integer.parseInt(datas[12]),gold=Integer.parseInt(datas[13]),vigor=Integer.parseInt(datas[14]);
                        login.setDiamond(diamond);
                        login.setGold(gold);
                        login.setVigor(vigor);                    
                        playerInfo.setDiamond(diamond);
                        playerInfo.setGold(gold);
                        playerInfo.setVigor(vigor);
                    }
                    login.setPlayerLevel(playerInfo.getPlayerLevel());
                    login.setPlayerName(playerInfo.getPlayerName());
                    AccountInfo accountInfo = dao.getAccountInfo(Integer.parseInt(datas[4]));
                    if (accountInfo != null) {
                        login.setAccountName(accountInfo.getAccountName());
                    }
                    playerInfoList.add(playerInfo);
                    loginInfoList.add(login);
                }
                dao.updatePlayerInfoBatch(playerInfoList);
                dao.saveLoginInfoBatch(today, loginInfoList);
                
            }
        });
    }
    

    
    

    @SuppressWarnings("serial")
    private JavaRDD<SparkFlumeEvent> filter(JavaRDD<SparkFlumeEvent> rdd) {
        return rdd.filter(new Function<SparkFlumeEvent, Boolean>() {
            @Override
            public Boolean call(SparkFlumeEvent flume) throws Exception {
                String line = new String(flume.event().getBody().array());
                String[] parts = SPACE.split(line);
                return (parts.length >= 2 && "2".equals(parts[0]));
            }
        });
    }

}
