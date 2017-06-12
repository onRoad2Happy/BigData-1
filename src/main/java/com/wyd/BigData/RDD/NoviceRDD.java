package com.wyd.BigData.RDD;
import com.wyd.BigData.bean.NoviceInfo;
import com.wyd.BigData.bean.PlayerInfo;
import com.wyd.BigData.dao.BaseDao;
import com.wyd.BigData.util.DataType;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import scala.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class NoviceRDD implements Serializable {
    private static final long serialVersionUID = -8397029531035009187L;

    private static final String DATATYPE         = String.valueOf(DataType.MARKNUM_NOVICE);
    //no map
    public void call(JavaRDD<String[]> rdd) {
        JavaRDD<String[]> noviceRDD = rdd.filter(parts -> parts.length > 2 && DATATYPE.equals(parts[0]));

        if (noviceRDD.count() == 0)
            return;

        noviceRDD.foreachPartition(it -> {
            BaseDao dao = BaseDao.getInstance();
            List<PlayerInfo> playerInfoList = new ArrayList<>();
            List<NoviceInfo> noviceList = new ArrayList<>();
            while (it.hasNext()) {
                String[] datas=it.next();
                int time = (int)(Long.parseLong(datas[1])/1000);
                int playerId = Integer.parseInt(datas[2]);
                int couresId = Integer.parseInt(datas[3]);
                int stepId = Integer.parseInt(datas[4]);

                PlayerInfo info = dao.getPlayerInfo(playerId);
                if(info !=null){
                    info.setCouresId(couresId);
                    info.setCouresStep(stepId);
                    info.setTiroTime(time);
                    playerInfoList.add(info);
                    int accountId = info.getAccountId();
                    int serviceId =  info.getServiceId();
                    NoviceInfo noviceInfo = new NoviceInfo();
                    noviceInfo.setCouresId(couresId);
                    noviceInfo.setCouresStep(stepId);
                    noviceInfo.setPlayerId(playerId);
                    noviceInfo.setServiceId(serviceId);
                    noviceInfo.setTime(time);
                    noviceInfo.setAccountId(accountId);
                    noviceList.add(noviceInfo);
                }
            }
            dao.updatePlayerNoviceInfoBatch(playerInfoList);
            dao.saveNoviceInfoBatch(noviceList);
        });
    }

}
