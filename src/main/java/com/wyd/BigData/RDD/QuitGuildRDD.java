package com.wyd.BigData.RDD;
import com.wyd.BigData.bean.GuildInfo;
import com.wyd.BigData.bean.PlayerInfo;
import com.wyd.BigData.dao.BaseDao;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import scala.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class QuitGuildRDD implements Serializable {
    private static final long serialVersionUID = 5309677673498328780L;

    public void call(JavaRDD<String[]> rdd) {
        JavaRDD<String[]> quitRDD = filter(rdd);
        if (quitRDD.count() == 0)
            return;
        //更 新玩家信息
        quitRDD.foreachPartition(it -> {
            BaseDao dao = BaseDao.getInstance();
            List<PlayerInfo> playerInfoList = new ArrayList<>();
            while (it.hasNext()) {
                String[] datas = it.next();
                int playerId = Integer.parseInt(datas[2]);
                int guildId = Integer.parseInt(datas[3]);
                PlayerInfo playerInfo = dao.getPlayerInfo(playerId);
                if (playerInfo != null && playerInfo.getGuildId()==guildId) {
                    playerInfo.setGuildId(0);
                    playerInfoList.add(playerInfo);
                }
            }
            dao.updatePlayerGuildInfoBatch(playerInfoList);
        });
        //更新公会人数
        JavaPairRDD<String,Integer> counts=quitRDD.mapToPair(datas->new Tuple2<>(datas[3],1)).reduceByKey((a,b)->a+b);
        counts.foreachPartition(it->{
            BaseDao dao = BaseDao.getInstance();
            List<GuildInfo> guildInfoList= new ArrayList<>();
            while (it.hasNext()){
                Tuple2<String,Integer> t = it.next();
                int guildId = Integer.parseInt(t._1());
                int count = t._2();
                GuildInfo guildInfo=dao.getGuildInfo(guildId);
                if(guildInfo!=null){
                    guildInfo.setGuildNum(guildInfo.getGuildNum()-count);
                    guildInfoList.add(guildInfo);
                }
            }
            dao.updateGuildInfoBatch(guildInfoList);
        });
    }

    @SuppressWarnings("serial") private JavaRDD<String[]> filter(JavaRDD<String[]> rdd) {
        return rdd.filter(parts -> (parts.length >= 2 && "11".equals(parts[0])));
    }
}
