package com.wyd.BigData.RDD;
import com.wyd.BigData.bean.*;
import com.wyd.BigData.dao.BaseDao;
import com.wyd.BigData.util.DataType;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import scala.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class MopSingleMapRDD implements Serializable {
    private static final long   serialVersionUID = 1253496812089250210L;
    private static final String DATATYPE         = String.valueOf(DataType.MARKNUM_SAODANG_SINGLEMAP);

    public void call(JavaRDD<String[]> rdd) {
        JavaRDD<String[]> mopsingleRDD = rdd.filter(parts -> parts.length > 2 && DATATYPE.equals(parts[0]));
        if (mopsingleRDD.count() == 0) {
            return;
        }
        JavaPairRDD<String, Integer> counts = mopsingleRDD.mapToPair(datas -> {
            BaseDao dao = BaseDao.getInstance();
            int serviceId = -1;
            int playerId = Integer.parseInt(datas[2]);
            int challengeTimes = Integer.parseInt(datas[4]);// 挑战次数
            ServiceInfo serviceInfo = dao.getServiceInfo(playerId);
            if (serviceInfo != null) {
                serviceId = serviceInfo.getServiceId();
            }
            return new Tuple2<>(serviceId + "_" + datas[3], challengeTimes);
        }).reduceByKey((x, y) -> x + y);
        counts.foreachPartition(it -> {
            List<SinglemapInfo> singlemapInfoList = new ArrayList<>();
            BaseDao dao = BaseDao.getInstance();
            while (it.hasNext()) {
                Tuple2<String, Integer> t = it.next();
                String[] params = t._1().split("_");
                int serviceId = Integer.parseInt(params[0]);
                int mapId = Integer.parseInt(params[1]);
                int count = t._2();
                SinglemapInfo info = dao.getSinglemapInfo(serviceId, mapId);
                if (info == null) {
                    info = new SinglemapInfo();
                    info.setMapId(mapId);
                    info.setServiceId(serviceId);
                    info.setDareCount(count);
                    info.setPassCount(count);
                    dao.saveSinglemapInfo(info);
                } else {
                    info.setDareCount(count + info.getDareCount());
                    info.setPassCount(count + info.getPassCount());
                    singlemapInfoList.add(info);
                }
            }
            dao.updateSinglemapDareCountBatch(singlemapInfoList);
            dao.updateSinglemapPassCountBatch(singlemapInfoList);
        });
        List<String[]> mopsingleLogList = mopsingleRDD.collect();
        BaseDao dao = BaseDao.getInstance();
        List<DareMapInfo> dareMapInfoList = new ArrayList<>();
        for (String[] datas : mopsingleLogList) {
            long dataTime = Long.parseLong(datas[1]);
            int playerId = Integer.parseInt(datas[2]);
            int mapId = Integer.parseInt(datas[3]);
            int challengeTimes = Integer.parseInt(datas[4]);// 挑战次数
            int serviceId = -1, accountId = -1;
            ServiceInfo serviceInfo = dao.getServiceInfo(playerId);
            if (serviceInfo != null) {
                serviceId = serviceInfo.getServiceId();
                accountId = serviceInfo.getAccountId();
            }
            DareMapInfo dareMapInfo = new DareMapInfo();
            dareMapInfo.setMapId(mapId);
            dareMapInfo.setPlayerId(playerId);
            dareMapInfo.setServiceId(serviceId);
            dareMapInfo.setTime(challengeTimes);
            dareMapInfo.setRecordTime((int) (dataTime / 1000));
            dareMapInfo.setAction(DareMapInfo.COME_OUT);
            dareMapInfo.setType(DareMapInfo.SINGLE_MAP);
            dareMapInfo.setAccountId(accountId);
            dareMapInfo.setChallengeType(1);
            dareMapInfoList.add(dareMapInfo);
        }
        dao.saveDareMapInfoBatch(dareMapInfoList);
    }
}
