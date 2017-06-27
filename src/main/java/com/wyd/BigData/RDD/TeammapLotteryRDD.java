package com.wyd.BigData.RDD;
import com.wyd.BigData.bean.PlayerTeammap;
import com.wyd.BigData.bean.ServiceInfo;
import com.wyd.BigData.bean.TeammapInfo;
import com.wyd.BigData.bean.TeammapLottery;
import com.wyd.BigData.dao.BaseDao;
import com.wyd.BigData.util.DataType;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.execution.columnar.LONG;
import scala.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class TeammapLotteryRDD implements Serializable {
    private static final long   serialVersionUID = -6322169442660445600L;
    private static final String DATATYPE         = String.valueOf(DataType.MARKNUM_TEAMMAPLOTTERY_ITEM);

    public void call(JavaRDD<String[]> rdd) {
        JavaRDD<String[]> teammapLotteryRDD = rdd.filter(parts -> parts.length > 2 && DATATYPE.equals(parts[0]));
        if (teammapLotteryRDD.count() == 0)
            return;
        //KEY:serviceId_sectionId VAL:TeammapInfo
        JavaPairRDD<String, TeammapInfo> counts = teammapLotteryRDD.mapToPair(datas -> {
            BaseDao dao = BaseDao.getInstance();
            int playerId = Integer.parseInt(datas[2]);
            int sectionId = Integer.parseInt(datas[3]);
            int difficulty = Integer.parseInt(datas[4]);
            int deplete = Integer.parseInt(datas[5]);
            ServiceInfo serviceInfo = dao.getServiceInfo(playerId);
            TeammapInfo teammapInfo = new TeammapInfo();
            switch (difficulty) {
            case 1:
                teammapInfo.setSLotteryCount(1);
                teammapInfo.setSLotteryDeplete(deplete);
                break;
            case 2:
                teammapInfo.setDLotteryCount(1);
                teammapInfo.setDLotteryDeplete(deplete);
                break;
            case 3:
                teammapInfo.setHLotteryCount(1);
                teammapInfo.setHLotteryDeplete(deplete);
                break;
            }
            int serviceId = serviceInfo == null ? -1 : serviceInfo.getServiceId();
            String key = serviceId + "_" + sectionId;
            return new Tuple2<>(key, teammapInfo);
        }).reduceByKey((x, y) -> {
            TeammapInfo teammapInfo = new TeammapInfo();
            teammapInfo.setSLotteryCount(x.getSLotteryCount() + y.getSLotteryCount());
            teammapInfo.setSLotteryDeplete(x.getSLotteryDeplete() + y.getSLotteryDeplete());
            teammapInfo.setDLotteryCount(x.getDLotteryCount() + y.getDLotteryCount());
            teammapInfo.setDLotteryDeplete(x.getDLotteryDeplete() + y.getDLotteryDeplete());
            teammapInfo.setHLotteryCount(x.getHLotteryCount() + y.getHLotteryCount());
            teammapInfo.setHLotteryDeplete(x.getHLotteryDeplete() + y.getHLotteryDeplete());
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
                    teammapInfo.setSLotteryCount(teammapInfo.getSLotteryCount() + info.getSLotteryCount());
                    teammapInfo.setDLotteryCount(teammapInfo.getDLotteryCount() + info.getDLotteryCount());
                    teammapInfo.setHLotteryCount(teammapInfo.getHLotteryCount() + info.getHLotteryCount());
                    teammapInfo.setSLotteryDeplete(teammapInfo.getSLotteryDeplete() + info.getSLotteryDeplete());
                    teammapInfo.setDLotteryDeplete(teammapInfo.getDLotteryDeplete() + info.getDLotteryDeplete());
                    teammapInfo.setHLotteryDeplete(teammapInfo.getHLotteryDeplete() + info.getHLotteryDeplete());
                    updatelist.add(teammapInfo);
                }
            }
            dao.saveTeammapInfoBatch(savelist);
            dao.updateTeammapInfoBath2(updatelist);
        });
        List<String[]> teammapLogList = teammapLotteryRDD.collect();
        BaseDao dao = BaseDao.getInstance();
        List<TeammapLottery> teammapLotteryList = new ArrayList<>();
        for (String[] datas : teammapLogList) {
            int serviceId = -1;
            long dataTime = Long.parseLong(datas[1]);
            int playerId = Integer.parseInt(datas[2]);
            int sectionId = Integer.parseInt(datas[3]);
            int difficulty = Integer.parseInt(datas[4]);
            int deplete = Integer.parseInt(datas[5]);
            ServiceInfo serviceInfo = dao.getServiceInfo(playerId);
            serviceId = serviceInfo != null ? serviceInfo.getServiceId() : -1;
            TeammapLottery lottery = new TeammapLottery();
            lottery.setServiceId(serviceId);
            lottery.setPlayerId(playerId);
            lottery.setSectionId(sectionId);
            lottery.setDifficulty(difficulty);
            lottery.setDeplete(deplete);
            lottery.setDataTime(dataTime);
            teammapLotteryList.add(lottery);
        }
        dao.saveTeammapLotteryBatch(teammapLotteryList);
    }
}
