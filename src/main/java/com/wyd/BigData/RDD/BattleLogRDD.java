package com.wyd.BigData.RDD;
import com.wyd.BigData.bean.BattleInfo;
import com.wyd.BigData.bean.GuildInfo;
import com.wyd.BigData.bean.PlayerInfo;
import com.wyd.BigData.dao.BaseDao;
import com.wyd.BigData.util.DataType;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import scala.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class BattleLogRDD implements Serializable {
    private static final long   serialVersionUID = -1045304438114667292L;
    private static final String DATATYPE         = String.valueOf(DataType.MARKNUM_BATTLELOG);

    public void call(JavaRDD<String[]> rdd) {
        JavaRDD<String[]> battleRDD = rdd.filter(parts -> parts.length > 5 && DATATYPE.equals(parts[0]));
        if (battleRDD.count() == 0)
            return;
        //KEY:battleMode_battleChannel_isBattleMull_isRobot_playerNum VAL:[1,pileTime]
        JavaPairRDD<String, Long[]> couts = battleRDD.mapToPair(datas -> new Tuple2<>(datas[2] + "_" + datas[3] + "_" + datas[4] + "_" + datas[5] + "_" + datas[6], new Long[] { 1L, Long.parseLong(datas[7]) })).reduceByKey((x, y) -> new Long[] { x[0] + y[0], x[1] + y[1] });
        couts.foreachPartition(it -> {
            BaseDao dao = BaseDao.getInstance();
            List<BattleInfo> saveList = new ArrayList<>();
            List<BattleInfo> updateList = new ArrayList<>();
            while (it.hasNext()){
                Tuple2<String,Long[]> t=it.next();
                String[] params= t._1().split("_");
                int battleMode=Integer.parseInt(params[0]);
                int battleChannel=Integer.parseInt(params[1]);
                boolean isBattleMull=Boolean.parseBoolean(params[2]);
                boolean isRobot=Boolean.parseBoolean(params[3]);
                int playerNum=Integer.parseInt(params[4]);
                int battleCount = Integer.parseInt(String.valueOf(t._2()[0]));
                long totalTime = t._2()[1];
                //TODO serverID is default value
                int serviceId=-1;
                BattleInfo info =dao.getBattleInfo(serviceId,battleMode, battleChannel, isBattleMull, isRobot, playerNum);
                if(info==null){
                    info=new BattleInfo();
                    info.setServiceId(serviceId);
                    info.setBattleMode(battleMode);
                    info.setBattleChannel(battleChannel);
                    info.setBattleMull(isBattleMull);
                    info.setRobot(isRobot);
                    info.setPlayerNum(playerNum);
                    info.setTotalTime(totalTime);
                    info.setBattleCount(battleCount);
                    saveList.add(info);
                }else{
                    info.setTotalTime(totalTime);
                    info.setBattleCount(battleCount);
                    updateList.add(info);
                }
            }
            dao.saveBattleInfoBatch(saveList);
            dao.updateBattleInfoBatch(updateList);
        });
    }
}
