package com.wyd.BigData.RDD;
import com.wyd.BigData.bean.GuildInfo;
import com.wyd.BigData.bean.PlayerInfo;
import com.wyd.BigData.dao.BaseDao;
import org.apache.spark.api.java.JavaRDD;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class CreateGuildRDD implements Serializable {
    private static final long    serialVersionUID = -6343114767248386683L;

    @SuppressWarnings("serial") public void call(JavaRDD<String[]> rdd) {
        JavaRDD<String[]> rechargeRDD = filter(rdd);
        if (rechargeRDD.count() == 0)
            return;
        rechargeRDD.foreachPartition(t -> {
            BaseDao dao = BaseDao.getInstance();
            List<PlayerInfo> playerInfoList = new ArrayList<>();
            while (t.hasNext()) {
                String[] datas = t.next();
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
            dao.updatePlayerInfoBatch(playerInfoList);
        });
    }

    @SuppressWarnings("serial") private JavaRDD<String[]> filter(JavaRDD<String[]> rdd) {
        return rdd.filter(parts -> (parts.length >= 2 && "8".equals(parts[0])));
    }
}
