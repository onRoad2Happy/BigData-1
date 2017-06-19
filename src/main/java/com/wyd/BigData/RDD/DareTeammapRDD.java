package com.wyd.BigData.RDD;
import com.wyd.BigData.bean.PlayerTeammap;
import com.wyd.BigData.bean.ServiceInfo;
import com.wyd.BigData.bean.TeammapInfo;
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
        //格式playerId sectionId difficulty isWin
        JavaRDD<String[]> playerTeammapRDD = dareTeammapRDD.flatMap(datas -> {
            String sectionId = datas[2];
            String difficulty = datas[3];
            String isWin = datas[4];
            String[] playerIds = datas[5].split(",");
            List<String[]> list = new ArrayList<>();
            for (String playerId : playerIds) {
                list.add(new String[] { playerId, sectionId, difficulty, isWin });
            }
            return list.iterator();
        });
        //KEY:playerId_sectionId VAL:PlayerTeammap
        JavaPairRDD<String,PlayerTeammap> playerTeammapCounts=playerTeammapRDD.mapToPair(datas -> {
            PlayerTeammap playerTeammap = new PlayerTeammap();
            String playerId = datas[0];
            String sectionId =datas[1];
            int difficulty = Integer.parseInt(datas[2]);
            int isWin = Integer.parseInt(datas[3]);
            switch (difficulty) {
            case 1:
                playerTeammap.setsDareCount(1);
                if (isWin == 1) {
                    playerTeammap.setsPassCount(1);
                }
                break;
            case 2:
                playerTeammap.setdDareCount(1);
                if (isWin == 1) {
                    playerTeammap.setdPassCount(1);
                }
                break;
            case 3:
                playerTeammap.sethDareCount(1);
                if (isWin == 1) {
                    playerTeammap.sethPassCount(1);
                }
                break;
            }
            String key = playerId+ "_" +sectionId;
            return new Tuple2<>(key, playerTeammap);
        }).reduceByKey((x, y) -> {
            PlayerTeammap playerTeammap = new PlayerTeammap();
            playerTeammap.setsDareCount(x.getsDareCount()+y.getsDareCount());
            playerTeammap.setsPassCount(x.getsPassCount()+y.getsPassCount());
            playerTeammap.setdDareCount(x.getdDareCount()+y.getdDareCount());
            playerTeammap.setdPassCount(x.getdPassCount()+y.getdPassCount());
            playerTeammap.sethDareCount(x.gethDareCount()+y.gethDareCount());
            playerTeammap.sethPassCount(x.gethPassCount()+y.gethPassCount());
            return playerTeammap;
        });
        playerTeammapCounts.foreachPartition(it->{
            BaseDao dao = BaseDao.getInstance();
            List<PlayerTeammap> saveList = new ArrayList<>();
            List<PlayerTeammap> updateList = new ArrayList<>();
            while (it.hasNext()){
                Tuple2<String,PlayerTeammap> t=it.next();
                String [] params= t._1().split("_");
                PlayerTeammap info = t._2();
                int serviceId=-1;
                int playerId= Integer.parseInt(params[0]);
                int sectionId= Integer.parseInt(params[1]);
                PlayerTeammap playerTeammap =  dao.getPlayerTeammap(playerId,sectionId);
                if(playerTeammap==null){
                    info.setPlayerId(playerId);
                    info.setSectionId(sectionId);
                    ServiceInfo serviceInfo = dao.getServiceInfo(playerId);
                    info.setServiceId(serviceInfo.getServiceId());
                    saveList.add(info);
                }else{
                    playerTeammap.setsDareCount(playerTeammap.getsDareCount()+info.getsDareCount());
                    playerTeammap.setsPassCount(playerTeammap.getsPassCount()+info.getsPassCount());
                    playerTeammap.setdDareCount(playerTeammap.getdDareCount()+info.getdDareCount());
                    playerTeammap.setdPassCount(playerTeammap.getdPassCount()+info.getdPassCount());
                    playerTeammap.sethDareCount(playerTeammap.gethDareCount()+info.gethDareCount());
                    playerTeammap.sethPassCount(playerTeammap.gethPassCount()+info.gethPassCount());
                    updateList.add(playerTeammap);
                }
            }
            dao.savePlayerTeammapBatch(saveList);
            dao.updatePlayerTeammapBath(updateList);

        });
    }
}
