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
public class DareTeammapRDD implements Serializable {
    private static final long   serialVersionUID = 3917069543244737757L;
    private static final String DATATYPE         = String.valueOf(DataType.MARKNUM_DARETEAMMAP);

    public void call(JavaRDD<String[]> rdd) {
        JavaRDD<String[]> dareTeammapRDD = rdd.filter(parts -> parts.length > 2 && DATATYPE.equals(parts[0]));
        if (dareTeammapRDD.count() == 0)
            return;
        //KEY:serviceId_sectionId VAL:TeammapInfo
        JavaPairRDD<String, TeammapInfo> counts = dareTeammapRDD.mapToPair(datas -> {
            BaseDao dao = BaseDao.getInstance();
            int sectionId = Integer.parseInt(datas[2]);
            int difficulty = Integer.parseInt(datas[3]);
            int isWin = Integer.parseInt(datas[4]);
            String[] playerIds = datas[5].split(",");
            int pileTime = (int) (Long.parseLong(datas[6]) / 1000);
            int playerId = Integer.parseInt(playerIds[0]);
            ServiceInfo serviceInfo = dao.getServiceInfo(playerId);
            TeammapInfo teammapInfo = new TeammapInfo();
            switch (difficulty) {
            case 1:
                teammapInfo.setSDareCount(1);
                if (isWin == 1) {
                    teammapInfo.setSTotalTime(pileTime);
                    teammapInfo.setSPassCount(1);
                    switch (playerIds.length) {
                    case 1:
                        teammapInfo.setSMember1Count(1);
                        break;
                    case 2:
                        teammapInfo.setSMember2Count(1);
                        break;
                    case 3:
                        teammapInfo.setSMember3Count(1);
                        break;
                    }
                }
                break;
            case 2:
                teammapInfo.setDDareCount(1);
                if (isWin == 1) {
                    teammapInfo.setDTotalTime(pileTime);
                    teammapInfo.setDPassCount(1);
                    switch (playerIds.length) {
                    case 1:
                        teammapInfo.setDMember1Count(1);
                        break;
                    case 2:
                        teammapInfo.setDMember2Count(1);
                        break;
                    case 3:
                        teammapInfo.setDMember3Count(1);
                        break;
                    }
                }
                break;
            case 3:
                teammapInfo.setHDareCount(1);
                if (isWin == 1) {
                    teammapInfo.setHTotalTime(pileTime);
                    teammapInfo.setHPassCount(1);
                    switch (playerIds.length) {
                    case 1:
                        teammapInfo.setHMember1Count(1);
                        break;
                    case 2:
                        teammapInfo.setHMember2Count(1);
                        break;
                    case 3:
                        teammapInfo.setHMember3Count(1);
                        break;
                    }
                }
                break;
            }
            int serviceId = serviceInfo == null ? -1 : serviceInfo.getServiceId();
            String key = serviceId + "_" + sectionId;
            return new Tuple2<>(key, teammapInfo);
        }).reduceByKey((x, y) -> {
            TeammapInfo teammapInfo = new TeammapInfo();
            teammapInfo.setSDareCount(x.getSDareCount() + y.getSDareCount());
            teammapInfo.setDDareCount(x.getDDareCount() + y.getDDareCount());
            teammapInfo.setHDareCount(x.getHDareCount() + y.getHDareCount());
            teammapInfo.setSTotalTime(x.getSTotalTime() + y.getSTotalTime());
            teammapInfo.setDTotalTime(x.getDTotalTime() + y.getDTotalTime());
            teammapInfo.setHTotalTime(x.getHTotalTime() + y.getHTotalTime());
            teammapInfo.setSPassCount(x.getSPassCount() + y.getSPassCount());
            teammapInfo.setDPassCount(x.getDPassCount() + y.getDPassCount());
            teammapInfo.setHPassCount(x.getHPassCount() + y.getHPassCount());
            teammapInfo.setSMember1Count(x.getSMember1Count() + y.getSMember1Count());
            teammapInfo.setDMember1Count(x.getDMember1Count() + y.getDMember1Count());
            teammapInfo.setHMember1Count(x.getHMember1Count() + y.getHMember1Count());
            teammapInfo.setSMember2Count(x.getSMember2Count() + y.getSMember2Count());
            teammapInfo.setDMember2Count(x.getDMember2Count() + y.getDMember2Count());
            teammapInfo.setHMember2Count(x.getHMember2Count() + y.getHMember2Count());
            teammapInfo.setSMember3Count(x.getSMember3Count() + y.getSMember3Count());
            teammapInfo.setDMember3Count(x.getDMember3Count() + y.getDMember3Count());
            teammapInfo.setHMember3Count(x.getHMember3Count() + y.getHMember3Count());
            return teammapInfo;
        });
        counts.foreachPartition(it -> {
            BaseDao dao = BaseDao.getInstance();
            List<TeammapInfo> savelist = new ArrayList<>();
            List<TeammapInfo> updatelist = new ArrayList<>();
            while (it.hasNext()) {
                Tuple2<String, TeammapInfo> t = it.next();
                String[] params = t._1().split("_");
                int serviceId = Integer.parseInt(params[0]);
                int sectionId = Integer.parseInt(params[1]);
                TeammapInfo info = t._2();
                TeammapInfo teammapInfo = dao.getTeammapInfo(serviceId, sectionId);
                if (teammapInfo == null) {
                    info.setServiceId(serviceId);
                    info.setSectionId(sectionId);
                    savelist.add(info);
                } else {
                    teammapInfo.setSDareCount(teammapInfo.getSDareCount() + info.getSDareCount());
                    teammapInfo.setDDareCount(teammapInfo.getDDareCount() + info.getDDareCount());
                    teammapInfo.setHDareCount(teammapInfo.getHDareCount() + info.getHDareCount());
                    teammapInfo.setSTotalTime(teammapInfo.getSTotalTime() + info.getSTotalTime());
                    teammapInfo.setDTotalTime(teammapInfo.getDTotalTime() + info.getDTotalTime());
                    teammapInfo.setHTotalTime(teammapInfo.getHTotalTime() + info.getHTotalTime());
                    teammapInfo.setSPassCount(teammapInfo.getSPassCount() + info.getSPassCount());
                    teammapInfo.setDPassCount(teammapInfo.getDPassCount() + info.getDPassCount());
                    teammapInfo.setHPassCount(teammapInfo.getHPassCount() + info.getHPassCount());
                    teammapInfo.setSMember1Count(teammapInfo.getSMember1Count() + info.getSMember1Count());
                    teammapInfo.setDMember1Count(teammapInfo.getDMember1Count() + info.getDMember1Count());
                    teammapInfo.setHMember1Count(teammapInfo.getHMember1Count() + info.getHMember1Count());
                    teammapInfo.setSMember2Count(teammapInfo.getSMember2Count() + info.getSMember2Count());
                    teammapInfo.setDMember2Count(teammapInfo.getDMember2Count() + info.getDMember2Count());
                    teammapInfo.setHMember2Count(teammapInfo.getHMember2Count() + info.getHMember2Count());
                    teammapInfo.setSMember3Count(teammapInfo.getSMember3Count() + info.getSMember3Count());
                    teammapInfo.setDMember3Count(teammapInfo.getDMember3Count() + info.getDMember3Count());
                    teammapInfo.setHMember3Count(teammapInfo.getHMember3Count() + info.getHMember3Count());
                    updatelist.add(teammapInfo);
                }
            }
            dao.saveTeammapInfoBatch(savelist);
            dao.updateTeammapInfoBath(updatelist);
        });
        //按玩家拆分数据.如把a 1,2拆成a 1和a 2
        //格式playerId sectionId difficulty isWin pileTime dataTime playerSize
        JavaRDD<String[]> playerTeammapRDD = dareTeammapRDD.flatMap(datas -> {
            String dataTime = datas[1];
            String sectionId = datas[2];
            String difficulty = datas[3];
            String isWin = datas[4];
            String[] playerIds = datas[5].split(",");
            String pileTime = datas[6];
            List<String[]> list = new ArrayList<>();
            for (String playerId : playerIds) {
                list.add(new String[] { playerId, sectionId, difficulty, isWin, pileTime, dataTime, String.valueOf(playerIds.length) });
            }
            return list.iterator();
        });

        //KEY:playerId_sectionId VAL:PlayerTeammap
        JavaPairRDD<String, PlayerTeammap> playerTeammapCounts = playerTeammapRDD.mapToPair(datas -> {
            PlayerTeammap playerTeammap = new PlayerTeammap();
            String playerId = datas[0];
            String sectionId = datas[1];
            int difficulty = Integer.parseInt(datas[2]);
            int isWin = Integer.parseInt(datas[3]);
            switch (difficulty) {
            case 1:
                playerTeammap.setSDareCount(1);
                if (isWin == 1) {
                    playerTeammap.setSPassCount(1);
                }
                break;
            case 2:
                playerTeammap.setDDareCount(1);
                if (isWin == 1) {
                    playerTeammap.setDPassCount(1);
                }
                break;
            case 3:
                playerTeammap.setHDareCount(1);
                if (isWin == 1) {
                    playerTeammap.setHPassCount(1);
                }
                break;
            }
            String key = playerId + "_" + sectionId;
            return new Tuple2<>(key, playerTeammap);
        }).reduceByKey((x, y) -> {
            PlayerTeammap playerTeammap = new PlayerTeammap();
            playerTeammap.setSDareCount(x.getSDareCount() + y.getSDareCount());
            playerTeammap.setSPassCount(x.getSPassCount() + y.getSPassCount());
            playerTeammap.setDDareCount(x.getDDareCount() + y.getDDareCount());
            playerTeammap.setDPassCount(x.getDPassCount() + y.getDPassCount());
            playerTeammap.setHDareCount(x.getHDareCount() + y.getHDareCount());
            playerTeammap.setHPassCount(x.getHPassCount() + y.getHPassCount());
            return playerTeammap;
        });
        playerTeammapCounts.foreachPartition(it -> {
            BaseDao dao = BaseDao.getInstance();
            List<PlayerTeammap> saveList = new ArrayList<>();
            List<PlayerTeammap> updateList = new ArrayList<>();
            while (it.hasNext()) {
                Tuple2<String, PlayerTeammap> t = it.next();
                String[] params = t._1().split("_");
                PlayerTeammap info = t._2();
                int playerId = Integer.parseInt(params[0]);
                int sectionId = Integer.parseInt(params[1]);
                PlayerTeammap playerTeammap = dao.getPlayerTeammap(playerId, sectionId);
                if (playerTeammap == null) {
                    info.setPlayerId(playerId);
                    info.setSectionId(sectionId);
                    ServiceInfo serviceInfo = dao.getServiceInfo(playerId);
                    info.setServiceId(serviceInfo.getServiceId());
                    saveList.add(info);
                } else {
                    playerTeammap.setSDareCount(playerTeammap.getSDareCount() + info.getSDareCount());
                    playerTeammap.setSPassCount(playerTeammap.getSPassCount() + info.getSPassCount());
                    playerTeammap.setDDareCount(playerTeammap.getDDareCount() + info.getDDareCount());
                    playerTeammap.setDPassCount(playerTeammap.getDPassCount() + info.getDPassCount());
                    playerTeammap.setHDareCount(playerTeammap.getHDareCount() + info.getHDareCount());
                    playerTeammap.setHPassCount(playerTeammap.getHPassCount() + info.getHPassCount());
                    updateList.add(playerTeammap);
                }
            }
            dao.savePlayerTeammapBatch(saveList);
            dao.updatePlayerTeammapBath(updateList);
        });
        //保存玩家战斗数据
        List<String[]> playerTeammapLogList =playerTeammapRDD.collect();
        BaseDao dao = BaseDao.getInstance();
        List<DareMapInfo> dareMapInfoList = new ArrayList<>();
        List<TeammapItem> teammapItemList = new ArrayList<>();
        for(String[] datas:playerTeammapLogList){
            int playerId = Integer.parseInt(datas[0]);
            int sectionId = Integer.parseInt(datas[1]);
            int difficulty = Integer.parseInt(datas[2]);
            int isWin = Integer.parseInt(datas[3]);
            int pileTime = Integer.parseInt(datas[4]);
            long dataTime = Long.parseLong(datas[5]);
            int playerSize = Integer.parseInt(datas[6]);
            PlayerInfo playerInfo = dao.getPlayerInfo(playerId,false);
            if (playerInfo == null)
                continue;
            DareMapInfo dareMapInfo = new DareMapInfo();
            dareMapInfo.setAction(isWin == 1 ? DareMapInfo.COME_OUT : DareMapInfo.COME_IN);
            dareMapInfo.setMapId(sectionId);
            dareMapInfo.setPlayerId(playerId);
            dareMapInfo.setDifficult(difficulty);
            dareMapInfo.setServiceId(playerInfo.getServiceId());
            dareMapInfo.setTime(1);
            dareMapInfo.setRecordTime((int) (dataTime / 1000));
            dareMapInfo.setType(DareMapInfo.TEAM_MAP);
            dareMapInfo.setAccountId(playerInfo.getAccountId());
            dareMapInfo.setChallengeType(0);
            dareMapInfoList.add(dareMapInfo);
            TeammapItem item = new TeammapItem();
            item.setServiceId(playerInfo.getServiceId());
            item.setPlayerId(playerId);
            item.setPlayerNum(playerSize);
            item.setMapId(sectionId);
            item.setDifficulty(difficulty);
            item.setStartTime((int) (dataTime / 1000));
            item.setFinishTime(pileTime);
            item.setIsWin(isWin);
            teammapItemList.add(item);
            playerInfo.setTeammapNum(playerInfo.getTeammapNum() + 1);
            dao.updatePlayerTeammap(playerInfo);
        }
        dao.saveTeammapItemBatch(teammapItemList);
        dao.saveDareMapInfoBatch(dareMapInfoList);
    }
}
