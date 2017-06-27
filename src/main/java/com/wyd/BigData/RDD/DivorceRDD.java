package com.wyd.BigData.RDD;
import com.wyd.BigData.bean.MarryInfo;
import com.wyd.BigData.bean.PlayerInfo;
import com.wyd.BigData.bean.ServiceInfo;
import com.wyd.BigData.dao.BaseDao;
import com.wyd.BigData.util.DataType;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import scala.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class DivorceRDD implements Serializable {
    private static final String DATATYPE         = String.valueOf(DataType.MARKNUM_DIVORCE);
    private static final long   serialVersionUID = 6420862317163914790L;

    public void call(JavaRDD<String[]> rdd) {
        JavaRDD<String[]> divorceRDD = rdd.filter(parts -> parts.length > 2 && DATATYPE.equals(parts[0]));
        if (divorceRDD.count() == 0)
            return;
        List<String[]> divorceList = divorceRDD.collect();
        BaseDao dao = BaseDao.getInstance();
        List<PlayerInfo> playerInfoList = new ArrayList<>();
        for (String[] datas : divorceList) {
            int groomId = Integer.parseInt(datas[2]);
            int brideId = Integer.parseInt(datas[3]);
            int marryMark = Integer.parseInt(datas[4]);
            PlayerInfo groomInfo = dao.getPlayerInfo(groomId);
            if (groomInfo != null) {
                groomInfo.setMateId(0);
                playerInfoList.add(groomInfo);
            }
            PlayerInfo brideInfo = dao.getPlayerInfo(brideId);
            if (brideInfo != null) {
                brideInfo.setMateId(0);
                playerInfoList.add(brideInfo);
            }
        }
        dao.updatePlayerMarryInfoBatch(playerInfoList);
        JavaPairRDD<String, Integer> counts = divorceRDD.mapToPair(datas -> {
            BaseDao dao2 = BaseDao.getInstance();
            int groomId = Integer.parseInt(datas[2]);
            ServiceInfo serviceInfo = dao2.getServiceInfo(groomId);
            int marryMark = Integer.parseInt(datas[4]);
            int serviceId = serviceInfo == null ? -1 : serviceInfo.getServiceId();
            String key = serviceId + "_" + marryMark;
            return new Tuple2<>(key, 1);
        }).reduceByKey((a, b) -> a + b);
        counts.foreachPartition(it -> {
            BaseDao dao3 = BaseDao.getInstance();
            List<MarryInfo> list1 = new ArrayList<>();
            while (it.hasNext()) {
                Tuple2<String, Integer> t = it.next();
                String[] params = t._1().split("_");
                int serviceId = Integer.parseInt(params[0]);
                int marryMark = Integer.parseInt(params[1]);
                int count = t._2();
                if (marryMark == 2) {
                    MarryInfo marryInfo = dao3.getMarryInfo(serviceId);
                    if (marryInfo == null) {
                        marryInfo = new MarryInfo();
                        marryInfo.setServiceId(serviceId);
                        dao3.saveMarryInfo(marryInfo);
                    }
                    marryInfo.setDivorceNum(marryInfo.getDivorceNum() + count);
                    list1.add(marryInfo);
                }
            }
            dao3.updateMarryInfoBath(list1, -1);
        });
    }
}
