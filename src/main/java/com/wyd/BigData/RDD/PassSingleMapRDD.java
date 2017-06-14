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
public class PassSingleMapRDD implements Serializable {
    private static final long   serialVersionUID = 7028175316210533264L;
    private static final String DATATYPE         = String.valueOf(DataType.MARKNUM_PASSSINGLE);

    public void call(JavaRDD<String[]> rdd) {
        JavaRDD<String[]> passSingleRDD = rdd.filter(parts -> parts.length > 2 && DATATYPE.equals(parts[0]));
        if (passSingleRDD.count() == 0)
            return;
        //=================累计通关时间和次数=====================
        //KEY:serviceId_mapId VAL [1,pileTime,star1,star2,star3]
        JavaPairRDD<String, Integer[]> counts = passSingleRDD.mapToPair(datas -> {
            BaseDao dao = BaseDao.getInstance();
            int serviceId = -1;
            int playerId = Integer.parseInt(datas[2]);
            ServiceInfo serviceInfo = dao.getServiceInfo(playerId);
            if (serviceInfo != null) {
                serviceId = serviceInfo.getServiceId();
            }
            String mapId = datas[3];
            int star = Integer.parseInt(datas[4]);
            int pileTime = Integer.parseInt(datas[5]);
            int star1 = 0, star2 = 0, star3 = 0;
            switch (star) {
            case 1:
                star1 = 1;
                break;
            case 2:
                star2 = 1;
                break;
            case 3:
                star3 = 1;
                break;
            }
            return new Tuple2<>(serviceId + "_" + mapId, new Integer[] { 1, pileTime, star1, star2, star3 });
        }).reduceByKey((x, y) -> new Integer[] { x[0] + y[0], x[1] + y[1], x[2] + y[2], x[3] + y[3], x[4] + y[4] });
        counts.foreachPartition(it -> {
            List<SinglemapInfo> singlemapInfoList = new ArrayList<>();
            BaseDao dao = BaseDao.getInstance();
            while (it.hasNext()) {
                Tuple2<String, Integer[]> t = it.next();
                String[] params = t._1().split("_");
                int serviceId = Integer.parseInt(params[0]);
                int mapId = Integer.parseInt(params[1]);
                int passCount = t._2()[0];
                int pileTime = t._2()[1];
                int star1 = t._2()[2];
                int star2 = t._2()[3];
                int star3 = t._2()[4];
                SinglemapInfo info = dao.getSinglemapInfo(serviceId, mapId);
                if (info == null) {
                    info = new SinglemapInfo();
                    info.setMapId(mapId);
                    info.setServiceId(serviceId);
                    info.setTotalTime(pileTime);
                    info.setPassCount(passCount);
                    info.setStar1Count(star1);
                    info.setStar2Count(star2);
                    info.setStar3Count(star3);
                    dao.saveSinglemapInfo(info);
                } else {
                    info.setTotalTime(pileTime + info.getTotalTime());
                    info.setPassCount(passCount + info.getPassCount());
                    info.setStar1Count(star1 + info.getStar1Count());
                    info.setStar2Count(star2 + info.getStar2Count());
                    info.setStar3Count(star3 + info.getStar3Count());
                    singlemapInfoList.add(info);
                }
            }
            dao.updateSinglemapTotalTimeBatch(singlemapInfoList);
            dao.updateSinglemapPassCountBatch(singlemapInfoList);
        });
        //=================end累计通关时间和次数end=====================
        //=================玩家通关====================
        // KEY player_maptype VAL [mapId,datatime]
        JavaPairRDD<String, Integer[]> maxMapRDD = passSingleRDD.mapToPair(datas -> {
            String mapId = datas[3];
            String playerId = datas[2];
            String mapType = mapId.startsWith("1") ? "1" : "2";
            int dataTime = (int) (Long.parseLong(datas[1]) / 1000);
            return new Tuple2<>(playerId + "_" + mapType, new Integer[] { Integer.parseInt(mapId), dataTime });
        }).reduceByKey((x, y) -> x[0] > y[0] ? x : y);
        maxMapRDD.foreachPartition(it -> {
            BaseDao dao = BaseDao.getInstance();
            List<PlayerInfo> playerInfoList1 = new ArrayList<>();
            List<PlayerInfo> playerInfoList2 = new ArrayList<>();
            while (it.hasNext()) {
                Tuple2<String, Integer[]> t = it.next();
                String[] params = t._1().split("_");
                int playerId = Integer.parseInt(params[0]);
                int mapType = Integer.parseInt(params[1]);
                int mapId = t._2()[0];
                int dataTime = t._2()[1];
                PlayerInfo playerInfo = dao.getPlayerInfo(playerId);
                if (playerInfo != null) {
                    if (mapType == 1 && (playerInfo.getTopDareSinglemap() > 20000 || playerInfo.getTopDareSinglemap() < mapId)) {
                        playerInfo.setTopSinglemap(mapId);
                        playerInfo.setTopSinglemapTime(dataTime);
                        playerInfoList1.add(playerInfo);
                    } else if (mapType == 2 && playerInfo.getTopDareEliteSinglemap() < mapId) {
                        playerInfo.setTopEliteSinglemap(mapId);
                        playerInfo.setTopEliteSinglemapTime(dataTime);
                        playerInfoList2.add(playerInfo);
                    }
                }
            }
            dao.updatePlayerTopSinglemapBatch(playerInfoList1);
            dao.updatePlayerTopEliteSinglemapBatch(playerInfoList2);
        });
        //=================end玩家通关end====================

        passSingleRDD.foreachPartition(it -> {
            BaseDao dao = BaseDao.getInstance();
            List<DareMapInfo> dareMapInfoList = new ArrayList<>();
            List<DareMapInfo> actionInfoList = new ArrayList<>();
            List<SinglemapItem> singlemapItemList = new ArrayList<>();
            while (it.hasNext()) {
                String[] datas = it.next();
                long dataTime = Long.parseLong(datas[1]);
                int playerId = Integer.parseInt(datas[2]);
                int mapId = Integer.parseInt(datas[3]);
                int star = Integer.parseInt(datas[4]);
                ServiceInfo serviceInfo = dao.getServiceInfo(playerId);
                DareMapInfo dareMapInfo = dao.getDareMapInfo(serviceInfo.getServiceId(), playerId, mapId, DareMapInfo.COME_IN, dataTime);
                if (dareMapInfo != null) {
                    dareMapInfo.setAction(DareMapInfo.COME_OUT);
                    dareMapInfo.setRecordTime((int) (dataTime / 1000));
                    actionInfoList.add(dareMapInfo);
                } else {
                    dareMapInfo = new DareMapInfo();
                    dareMapInfo.setMapId(mapId);
                    dareMapInfo.setPlayerId(playerId);
                    dareMapInfo.setServiceId(serviceInfo.getServiceId());
                    dareMapInfo.setTime(1);
                    dareMapInfo.setRecordTime((int) (dataTime / 1000));
                    dareMapInfo.setAction(DareMapInfo.COME_OUT);
                    dareMapInfo.setType(DareMapInfo.SINGLE_MAP);
                    dareMapInfo.setAccountId(serviceInfo.getAccountId());
                    dareMapInfo.setChallengeType(0);
                    dareMapInfoList.add(dareMapInfo);
                }
                SinglemapItem item= dao.getLastSinglemapItem(playerId,dataTime);
                if(item!=null) {
                    int finishTime = (int) (dataTime / 1000);
                    if (finishTime >= item.getStartTime()) {
                        item.setFinishTime(finishTime - item.getStartTime());
                    }
                    item.setPassStar(star);
                    item.setDataTime((int) (dataTime / 1000));
                    singlemapItemList.add(item);
                }
            }
            dao.saveDareMapInfoBatch(dareMapInfoList);
            dao.updateSinglemapItemBatch(singlemapItemList);
            dao.updateDareMapActionBath(actionInfoList);
        });
    }
}
