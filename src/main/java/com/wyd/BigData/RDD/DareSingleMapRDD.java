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
public class DareSingleMapRDD implements Serializable {
    private static final long   serialVersionUID = -5872213225188155289L;
    private static final String DATATYPE         = String.valueOf(DataType.MARKNUM_DARESINGLE);

    public void call(JavaRDD<String[]> rdd) {
        JavaRDD<String[]> daresingleRDD = rdd.filter(parts -> parts.length > 2 && DATATYPE.equals(parts[0]));
        if (daresingleRDD.count() == 0)
            return;
        JavaPairRDD<String, Integer> counts = daresingleRDD.mapToPair(datas -> {
            BaseDao dao = BaseDao.getInstance();
            int serviceId = -1;
            int playerId = Integer.parseInt(datas[2]);
            ServiceInfo serviceInfo = dao.getServiceInfo(playerId);
            if (serviceInfo != null) {
                serviceId = serviceInfo.getServiceId();
            }
            return new Tuple2<>(serviceId + "_" + datas[3], 1);
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
                    dao.saveSinglemapInfo(info);
                } else {
                    info.setDareCount(count + info.getDareCount());
                    singlemapInfoList.add(info);
                }
            }
            dao.updateSinglemapDareCountBatch(singlemapInfoList);
        });
        // KEY player_maptype VAL mapId
        JavaPairRDD<String, Integer> maxMapRDD = daresingleRDD.mapToPair(datas -> new Tuple2<>(datas[2] + "_" + (datas[3].startsWith("1") ? "1" : "2"), Integer.parseInt(datas[3]))).reduceByKey(Math::max);
        maxMapRDD.foreachPartition(it -> {
            BaseDao dao = BaseDao.getInstance();
            List<PlayerInfo> playerInfoList1 = new ArrayList<>();
            List<PlayerInfo> playerInfoList2 = new ArrayList<>();
            while (it.hasNext()) {
                Tuple2<String, Integer> t = it.next();
                String[] params = t._1().split("_");
                int playerId = Integer.parseInt(params[0]);
                int mapType = Integer.parseInt(params[1]);
                int mapId = t._2();
                PlayerInfo playerInfo = dao.getPlayerInfo(playerId);
                if (playerInfo != null) {
                    if (mapType == 1 && (playerInfo.getTopDareSinglemap() > 20000 || playerInfo.getTopDareSinglemap() < mapId)) {
                        playerInfo.setTopDareSinglemap(mapId);
                        playerInfoList1.add(playerInfo);
                    } else if (mapType == 2 && playerInfo.getTopDareEliteSinglemap() < mapId) {
                        playerInfo.setTopDareEliteSinglemap(mapId);
                        playerInfoList2.add(playerInfo);
                    }
                }
            }
            dao.updatePlayerTopDareSinglemapBatch(playerInfoList1);
            dao.updatePlayerTopDareEliteSinglemapBatch(playerInfoList2);
        });
        daresingleRDD.foreachPartition(it -> {
            BaseDao dao = BaseDao.getInstance();
            List<DareMapInfo> dareMapInfoList = new ArrayList<>();
            List<SinglemapItem> singlemapItemList = new ArrayList<>();
            while (it.hasNext()) {
                String[] datas = it.next();
                long dataTime = Long.parseLong(datas[1]);
                int playerId = Integer.parseInt(datas[2]);
                int mapId = Integer.parseInt(datas[3]);
                ServiceInfo serviceInfo = dao.getServiceInfo(playerId);
                DareMapInfo dareMapInfo = new DareMapInfo();
                dareMapInfo.setMapId(mapId);
                dareMapInfo.setPlayerId(playerId);
                dareMapInfo.setServiceId(serviceInfo.getServiceId());
                dareMapInfo.setTime(1);
                dareMapInfo.setRecordTime((int) (dataTime / 1000));
                dareMapInfo.setAction(DareMapInfo.COME_IN);
                dareMapInfo.setType(DareMapInfo.SINGLE_MAP);
                dareMapInfo.setAccountId(serviceInfo.getAccountId());
                dareMapInfo.setChallengeType(0);
                dareMapInfoList.add(dareMapInfo);
                SinglemapItem item = new SinglemapItem();
                item.setServiceId(serviceInfo.getServiceId());
                item.setPlayerId(playerId);
                item.setMapId(mapId);
                item.setStartTime((int) (dataTime / 1000));
                item.setFinishTime(0);
                item.setPassStar(0);
                singlemapItemList.add(item);
            }
            dao.saveDareMapInfoBatch(dareMapInfoList);
            dao.saveSingleMapItemBatch(singlemapItemList);
        });
    }
}
