package com.wyd.BigData.RDD;
import com.wyd.BigData.bean.PlayerInfo;
import com.wyd.BigData.bean.PlayerLevelInfo;
import com.wyd.BigData.bean.ServiceInfo;
import com.wyd.BigData.bean.UpgradeInfo;
import com.wyd.BigData.dao.BaseDao;
import com.wyd.BigData.util.DataType;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import scala.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class UpgradeRDD implements Serializable {
    private static final long             serialVersionUID = 35472250916594214L;

    private static final String DATATYPE         = String.valueOf(DataType.MARKNUM_PLAYERUPGRADE);

    public void call(JavaRDD<String[]> rdd) {
        JavaRDD<String[]> upgradeRDD = rdd.filter(parts -> parts.length > 2 && DATATYPE.equals(parts[0]));

        if (upgradeRDD.count() == 0)
            return;
        //KEY: 玩家ID VAL: [升级时间,玩家升级前等级]
        JavaPairRDD<String, String[]> playerLevelRDD = upgradeRDD.mapToPair(parts -> new Tuple2<>(parts[2], new String[] { parts[1], parts[3] }));
        // 取玩家最大等级
        JavaPairRDD<String, String[]> playerMaxLevelRDD = playerLevelRDD.reduceByKey((i1, i2) -> Integer.parseInt(i1[1]) > Integer.parseInt(i2[1]) ? i1 : i2);
        playerMaxLevelRDD.foreachPartition(it -> {
            BaseDao dao = BaseDao.getInstance();
            List<PlayerInfo> playerInfoList = new ArrayList<>();
            while (it.hasNext()) {
                Tuple2<String, String[]> t = it.next();
                PlayerInfo playerInfo = dao.getPlayerInfo(Integer.parseInt(t._1()));
                if (playerInfo != null) {
                    int playerLevel = Integer.parseInt(t._2[1]) + 1;
                    int time = (int) (Long.parseLong(t._2[0]) / 1000);
                    playerInfo.setPlayerLevel(playerLevel);
                    playerInfo.setUpgradeTime(time);
                    playerInfoList.add(playerInfo);
                }
            }
            dao.updatePlayerUpgradeBatch(playerInfoList);
        });
        // 统计各等级增加和减少的玩家个数
        // KEY：服务器ID_渠道ID_玩家升级前等级 VAL:1
        JavaPairRDD<String, Integer> levelsRDD = upgradeRDD.mapToPair(parts -> {
            BaseDao dao2 = BaseDao.getInstance();
            int playerId = Integer.parseInt(parts[2]);
            int playerLevel = Integer.parseInt(parts[3]);
            int channelId = -1;
            int serviceId = -1;
            ServiceInfo serviceInfo = dao2.getServiceInfo(playerId);
            if (serviceInfo != null) {
                channelId = serviceInfo.getChannelId();
                serviceId = serviceInfo.getServiceId();
            }
            String key = serviceId + "_" + channelId + "_" + playerLevel;
            return new Tuple2<>(key, 1);
        });
        BaseDao dao = BaseDao.getInstance();
        JavaPairRDD<String, Integer> levelCountRDD = levelsRDD.reduceByKey((i1, i2) -> i1 + i2);
        List<Tuple2<String, Integer>> levelCountList = levelCountRDD.collect();
        for (Tuple2<String, Integer> levelCount : levelCountList) {
            String[] params = levelCount._1().split("_");
            int serviceId = Integer.parseInt(params[0]);
            int channelId = Integer.parseInt(params[1]);
            int playerLevel = Integer.parseInt(params[2]);
            int count = levelCount._2;
            PlayerLevelInfo beforeLevel = dao.getPlayerLevel(serviceId, channelId, playerLevel);
            if (beforeLevel == null) {
                beforeLevel = new PlayerLevelInfo();
                beforeLevel.setChannelId(channelId);
                beforeLevel.setServiceId(serviceId);
                beforeLevel.setLevel(playerLevel);
                beforeLevel.setPlayerCount(-count);
                dao.savePlayerLevelInfo(beforeLevel);
            } else {
                beforeLevel.setPlayerCount(beforeLevel.getPlayerCount() - count);
                dao.updatePlayerLevelInfo(beforeLevel);
            }
            PlayerLevelInfo afterLevel = dao.getPlayerLevel(serviceId, channelId, playerLevel + 1);
            if (afterLevel == null) {
                afterLevel = new PlayerLevelInfo();
                afterLevel.setChannelId(channelId);
                afterLevel.setServiceId(serviceId);
                afterLevel.setLevel(playerLevel + 1);
                afterLevel.setPlayerCount(count);
                dao.savePlayerLevelInfo(afterLevel);
            } else {
                afterLevel.setPlayerCount(afterLevel.getPlayerCount() + count);
                dao.updatePlayerLevelInfo(afterLevel);
            }
        }
        //累计peileTime
        // KEY  服务器ID_等级， VAL [升级耗时,计数1]
        JavaPairRDD<String, Integer[]> pileTimeCountRDD = upgradeRDD.mapToPair(parts -> {
            BaseDao dao2 = BaseDao.getInstance();
            int playerId = Integer.parseInt(parts[2]);
            int level = Integer.parseInt(parts[3]);
            int pileTime = (int) (Long.parseLong(parts[4]) / 1000);
            int serviceId = -1;
            ServiceInfo serviceInfo = dao2.getServiceInfo(playerId);
            if (serviceInfo != null) {
                serviceId = serviceInfo.getServiceId();
            }
            String key = serviceId + "_" + level;
            return new Tuple2<>(key, new Integer[] { pileTime, 1 });
        }).reduceByKey((i1, i2) -> new Integer[] { i1[0] + i2[0], i1[1] + i2[1] });
        List<UpgradeInfo> upgradeInfoList = new ArrayList<>();
        List<Tuple2<String, Integer[]>> pileTimeCountList = pileTimeCountRDD.collect();
        for (Tuple2<String, Integer[]> pileTimeCount : pileTimeCountList) {
            String[] params =  pileTimeCount._1().split("_");
            int serviceId = Integer.parseInt(params[0]);
            int level = Integer.parseInt(params[1]);
            int pileTime = pileTimeCount._2()[0];
            int count = pileTimeCount._2()[1];
            UpgradeInfo upgradeInfo=dao.getUpgradeInfo(serviceId,level);
            if(upgradeInfo==null){
                upgradeInfo= new UpgradeInfo();
                upgradeInfo.setServiceId(serviceId);
                upgradeInfo.setPlayerLevel(level);
                upgradeInfo.setTotalCount(count);
                upgradeInfo.setTotalTime(pileTime);
                dao.saveUpgradeInfo(upgradeInfo);
            }else{
                upgradeInfo.setTotalCount(count);
                upgradeInfo.setTotalTime(pileTime);
                upgradeInfoList.add(upgradeInfo);
            }

        }
        dao.updateUpgradeInfoBatch(upgradeInfoList);
    }

}
