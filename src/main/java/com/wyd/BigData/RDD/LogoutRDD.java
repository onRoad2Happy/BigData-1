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
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.flume.SparkFlumeEvent;
import com.wyd.BigData.bean.LoginInfo;
import com.wyd.BigData.bean.PlayerInfo;
import com.wyd.BigData.bean.RechargeInfo;
import com.wyd.BigData.dao.BaseDao;
import com.wyd.BigData.util.DateUtil;
public class LogoutRDD implements Serializable {
    /**
     * 
     */
    private static final long    serialVersionUID = -758442520627154431L;
    static SimpleDateFormat      sf               = new SimpleDateFormat("yyyy_MM_dd");
    private static final Pattern SPACE            = Pattern.compile("\t");

    @SuppressWarnings("serial")
    public void call(JavaRDD<SparkFlumeEvent> rdd, SparkSession spark) {
        final String today = sf.format(Calendar.getInstance().getTime());
        JavaRDD<SparkFlumeEvent> logoutRDD = filter(rdd);
        if (logoutRDD.count() == 0) return;
        LogLog.debug("logoutRDD count:" + logoutRDD.count());
        logoutRDD.foreachPartition(new VoidFunction<Iterator<SparkFlumeEvent>>() {
            BaseDao            dao              = BaseDao.getInstance();
            List<PlayerInfo>   playerInfoList   = new ArrayList<>();
            List<LoginInfo> loginInfoList = new ArrayList<>();

            @Override
            public void call(Iterator<SparkFlumeEvent> t) throws Exception {
                while (t.hasNext()) {
                    String line = new String(t.next().event().getBody().array());
                    String[] datas = SPACE.split(line);
                    Date dataTime = new Date(Long.parseLong(datas[1]));
                    int playerId = Integer.parseInt(datas[2]);
                    PlayerInfo playerInfo = dao.getPlayerInfo(playerId);
                    if (null != playerInfo) {
                        String lastLoginDay = sf.format(playerInfo.getLoginTime());
                        LoginInfo loginInfo = dao.getLoginInfo(lastLoginDay, playerId);
                        if (null != loginInfo) {
                            loginInfo.setLogoutTime(dataTime);
                            loginInfo.setOnlineTime((int) (Long.parseLong(datas[3]) / 1000));
                            loginInfo.setPlayerLevel(playerInfo.getPlayerLevel());
                            playerInfo.setTotalOnline(playerInfo.getTotalOnline() + loginInfo.getOnlineTime());
                            if (datas.length > 4) {
                                playerInfo.setVigor(Integer.parseInt(datas[4]));
                            }
                            playerInfoList.add(playerInfo);
                            loginInfoList.add(loginInfo);                            
                        }
                    }
                }
                dao.updatePlayerInfoBatch(playerInfoList);
                dao.updateLoginInfoBatch(loginInfoList,true);
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
                return (parts.length >= 2 && "3".equals(parts[0]));
            }
        });
    }
}
