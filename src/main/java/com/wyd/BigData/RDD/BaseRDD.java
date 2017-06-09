package com.wyd.BigData.RDD;
import com.wyd.BigData.bean.GuildInfo;
import com.wyd.BigData.bean.PlayerInfo;
import com.wyd.BigData.dao.BaseDao;
import com.wyd.BigData.util.DataType;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.flume.SparkFlumeEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
/**
 * Created by root on 6/7/17.
 */
public class BaseRDD implements Serializable {
    private static final Pattern SPACE            = Pattern.compile("\t");
    private static final long    serialVersionUID = 7889887567808742443L;

    public void call(JavaRDD<SparkFlumeEvent> rdd) {
        rdd.foreachPartition(new VoidFunction<Iterator<SparkFlumeEvent>>() {
            BaseDao dao = BaseDao.getInstance();
            List<PlayerInfo> playerInfoList = new ArrayList<>();

            @Override public void call(Iterator<SparkFlumeEvent> t) throws Exception {
                while (t.hasNext()) {
                    String line = new String(t.next().event().getBody().array(), "UTF-8");
                    String[] datas = SPACE.split(line);
                    int type = Integer.parseInt(datas[0]);
                    if (datas.length >= 2) {
                        switch (type) {
                        case DataType.MARKNUM_LOGOUT:
                            playerLogout(datas);
                            break;
                        case DataType.MARKNUM_RECHARGE:
                            recharge(datas);
                            break;
                        case DataType.MARKNUM_ONLINENUM:
                            onlineNum(datas);
                            break;
                        case DataType.MARKNUM_PLAYERUPGRADE:
                            playerUpgrade(datas);
                            break;
                        case DataType.MARKNUM_VIPUPGRADE:
                            vipUpgrade(datas,playerInfoList);
                            break;
                        case DataType.MARKNUM_CREATEGUILD:
                            createGuild(datas,playerInfoList);
                            break;
                        }
                    }
                }
                dao.updatePlayerInfoBatch(playerInfoList);
            }
        });
    }
    private void recharge(String[] datas){

    }
    private void onlineNum(String[] datas){

    }
    private void playerUpgrade(String[] datas){

    }
    private void vipUpgrade(String[] datas, List<PlayerInfo> playerInfoList){
        BaseDao dao = BaseDao.getInstance();
        int playerId = Integer.parseInt(datas[2]);
        int vipLevel = Integer.parseInt(datas[3]);
        PlayerInfo playerInfo = dao.getPlayerInfo(playerId,false);
        if (null != playerInfo) {
            playerInfo.setVipLevel(vipLevel);
            playerInfoList.add(playerInfo);
        }
    }
    private void playerLogout(String[] datas){

    }
    /**
     * 创建公会
     *
     * @param datas 数据源
     * @param playerInfoList 玩家List
     */
    private void createGuild(String[] datas, List<PlayerInfo> playerInfoList) {
        BaseDao dao = BaseDao.getInstance();
        int playerId = Integer.parseInt(datas[2]);
        int guildId = Integer.parseInt(datas[3]);
        PlayerInfo playerInfo = dao.getPlayerInfo(playerId);
        if (null != playerInfo) {
            playerInfo.setGuildId(guildId);
            playerInfoList.add(playerInfo);
        }
        GuildInfo guildInfo = dao.getGuildInfo(guildId);
        if (guildInfo == null) {
            guildInfo = new GuildInfo();
            guildInfo.setGuildId(guildId);
            guildInfo.setServiceId(playerInfo.getServiceId());
            guildInfo.setGuildLevel(1);
            guildInfo.setGuildNum(1);
            dao.saveGuildInfo(guildInfo);
        }
    }
}
