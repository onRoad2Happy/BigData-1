package com.wyd.BigData.RDD;
import com.wyd.BigData.bean.PlayerInfo;
import com.wyd.BigData.bean.PlayerLevelInfo;
import com.wyd.BigData.bean.UpgradeInfo;
import com.wyd.BigData.dao.BaseDao;
import org.apache.log4j.helpers.LogLog;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.flume.SparkFlumeEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
public class UpgradeRDD implements Serializable {
    /**
     *
     */
    private static final long    serialVersionUID = -758442520627154431L;
    private static final Pattern SPACE            = Pattern.compile("\t");

    @SuppressWarnings("serial")
    public void call(JavaRDD<SparkFlumeEvent> rdd) {
        JavaRDD<SparkFlumeEvent> rechargeRDD = filter(rdd);
        if (rechargeRDD.count() == 0)
            return;
        //LogLog.debug("rechargeRDD count:" + rechargeRDD.count());
        rechargeRDD.foreachPartition(new VoidFunction<Iterator<SparkFlumeEvent>>() {
            BaseDao dao = BaseDao.getInstance();
            List<PlayerInfo> playerInfoList = new ArrayList<>();
            List<UpgradeInfo> upgradeInfoList = new ArrayList<>();
            List<PlayerLevelInfo> playerLevelInfo = new ArrayList<>();

            @Override public void call(Iterator<SparkFlumeEvent> t) throws Exception {
                while (t.hasNext()) {
                    String line = new String(t.next().event().getBody().array(), "UTF-8");
                    String[] datas = SPACE.split(line);
                    long dataTime = Long.parseLong(datas[1]);
                    int playerId = Integer.parseInt(datas[2]);
                    int playerLevel = Integer.parseInt(datas[3]);
                    int pileTime = (int) (Long.parseLong(datas[4]) / 1000);
                    PlayerInfo playerInfo = dao.getPlayerInfo(playerId);
                    if (null != playerInfo) {
                        playerInfo.setPlayerLevel(playerLevel + 1);
                        playerInfo.setUpgradeTime((int) (dataTime / 1000));
                        //玩家等级预处理
                        PlayerLevelInfo levelInfo1 = dao.getPlayerLevel(playerInfo.getServiceId(), playerInfo.getChannelId(), playerLevel);
                        if (levelInfo1 != null) {
                            levelInfo1.setPlayerCount(levelInfo1.getPlayerCount() > 0 ? levelInfo1.getPlayerCount() - 1 : 0);
                            playerLevelInfo.add(levelInfo1);
                        } else {
                            dao.savePlayerLevelInfo(levelInfo1);
                        }
                        PlayerLevelInfo levelInfo2 = dao.getPlayerLevel(playerInfo.getServiceId(), playerInfo.getChannelId(), playerLevel + 1);
                        if (levelInfo2 != null) {
                            levelInfo2.setPlayerCount(levelInfo2.getPlayerCount() + 1);
                        } else {
                            dao.savePlayerLevelInfo(levelInfo2);
                        }
                        UpgradeInfo upgradeInfo = dao.getUpgradeInfo(playerInfo.getServiceId(), playerLevel);
                        if (upgradeInfo != null) {
                            upgradeInfo.setTotalTime(upgradeInfo.getTotalTime() + pileTime);
                            upgradeInfo.setTotalCount(upgradeInfo.getTotalCount() + 1);
                            upgradeInfoList.add(upgradeInfo);
                        } else {
                            upgradeInfo = new UpgradeInfo();
                            upgradeInfo.setServiceId(playerInfo.getServiceId());
                            upgradeInfo.setPlayerLevel(playerLevel);
                            upgradeInfo.setTotalTime(upgradeInfo.getTotalTime() + pileTime);
                            upgradeInfo.setTotalCount(upgradeInfo.getTotalCount() + 1);
                            dao.saveUpgradeInfo(upgradeInfo);
                        }
                        playerInfoList.add(playerInfo);
                    }
                }
                dao.updateUpgradeInfoBatch(upgradeInfoList);
                dao.updatePlayerLevelInfoBatch(playerLevelInfo);
                dao.updatePlayerInfoBatch(playerInfoList);
            }
        });
    }

    @SuppressWarnings("serial") private JavaRDD<SparkFlumeEvent> filter(JavaRDD<SparkFlumeEvent> rdd) {
        return rdd.filter(flume -> {
            String line = new String(flume.event().getBody().array(), "UTF-8");
            String[] parts = SPACE.split(line);
            return (parts.length >= 2 && "6".equals(parts[0]));
        });
    }
}
