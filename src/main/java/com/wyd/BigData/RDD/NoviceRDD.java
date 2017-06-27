package com.wyd.BigData.RDD;
import com.wyd.BigData.bean.NoviceInfo;
import com.wyd.BigData.bean.PlayerInfo;
import com.wyd.BigData.bean.ServiceInfo;
import com.wyd.BigData.dao.BaseDao;
import com.wyd.BigData.util.DataType;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import scala.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class NoviceRDD implements Serializable {
    private static final long   serialVersionUID = -8397029531035009187L;
    private static final String DATATYPE         = String.valueOf(DataType.MARKNUM_NOVICE);

    public void call(JavaRDD<String[]> rdd) {
        JavaRDD<String[]> noviceRDD = rdd.filter(parts -> parts.length > 2 && DATATYPE.equals(parts[0]));
        if (noviceRDD.count() == 0)
            return;
        //取时间最大的。时间最大的数据是最新数据.KEY playerId VAL datatime,couresId,stepId
        JavaPairRDD<String, Integer[]> maxStepRDD = noviceRDD.mapToPair(datas -> new Tuple2<>(datas[2], new Integer[] { (int) (Long.parseLong(datas[1]) / 1000), Integer.parseInt(datas[3]), Integer.parseInt(datas[4]) })).reduceByKey((x, y) -> x[0] > y[0] ? x : y);
        maxStepRDD.foreachPartition(it -> {
            BaseDao dao = BaseDao.getInstance();
            List<PlayerInfo> playerInfoList = new ArrayList<>();
            while (it.hasNext()) {
                Tuple2<String, Integer[]> t = it.next();
                int playerId = Integer.parseInt(t._1());
                Integer[] datas = t._2();
                int time = datas[0];
                int couresId = datas[1];
                int stepId = datas[2];
                PlayerInfo info = dao.getPlayerInfo(playerId);
                if (info != null) {
                    info.setCouresId(couresId);
                    info.setCouresStep(stepId);
                    info.setTiroTime(time);
                    playerInfoList.add(info);
                }
            }
            dao.updatePlayerNoviceInfoBatch(playerInfoList);
        });
        List<String[]> noviceLogList = noviceRDD.collect();
        BaseDao dao = BaseDao.getInstance();
        List<NoviceInfo> noviceList = new ArrayList<>();
        for (String[] datas : noviceLogList) {
            int time = (int) (Long.parseLong(datas[1]) / 1000);
            int playerId = Integer.parseInt(datas[2]);
            int couresId = Integer.parseInt(datas[3]);
            int stepId = Integer.parseInt(datas[4]);
            ServiceInfo info = dao.getServiceInfo(playerId);
            if (info != null) {
                int accountId = info.getAccountId();
                int serviceId = info.getServiceId();
                NoviceInfo noviceInfo = new NoviceInfo();
                noviceInfo.setCouresId(couresId);
                noviceInfo.setCouresStep(stepId);
                noviceInfo.setPlayerId(playerId);
                noviceInfo.setServiceId(serviceId);
                noviceInfo.setTime(time);
                noviceInfo.setAccountId(accountId);
                noviceList.add(noviceInfo);
            }
        }
        dao.saveNoviceInfoBatch(noviceList);
    }
}
