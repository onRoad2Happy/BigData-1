package com.wyd.BigData.RDD;
import com.wyd.BigData.bean.GuildInfo;
import com.wyd.BigData.bean.PlayerInfo;
import com.wyd.BigData.dao.BaseDao;
import com.wyd.BigData.util.DataType;
import org.apache.spark.api.java.JavaRDD;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class CreateGuildRDD implements Serializable {
    private static final long   serialVersionUID = -1329380821588558546L;
    private static final String DATATYPE         = String.valueOf(DataType.MARKNUM_CREATEGUILD);
    //BaseRDD
    public void call(JavaRDD<String[]> rdd) {
        JavaRDD<String[]> guildRDD = rdd.filter(parts -> parts.length > 2 && DATATYPE.equals(parts[0]));
        if (guildRDD.count() == 0)
            return;
        List<String[]> guildLogList = guildRDD.collect();
        BaseDao dao = BaseDao.getInstance();
        List<PlayerInfo> playerInfoList = new ArrayList<>();
        for (String[] datas : guildLogList) {
            int playerId = Integer.parseInt(datas[2]);
            int guildId = Integer.parseInt(datas[3]);
            PlayerInfo playerInfo = dao.getPlayerInfo(playerId);
            if (null != playerInfo) {
                playerInfo.setGuildId(guildId);
                playerInfoList.add(playerInfo);
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
        dao.updatePlayerGuildInfoBatch(playerInfoList);
    }
}
