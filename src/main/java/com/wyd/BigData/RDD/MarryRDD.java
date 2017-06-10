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
public class MarryRDD implements Serializable {
    private static final long   serialVersionUID = -3611622090732914055L;
    private static final String DATATYPE         = String.valueOf(DataType.MARKNUM_MARRY);

    public void call(JavaRDD<String[]> rdd) {
        JavaRDD<String[]> marryRDD = rdd.filter(parts -> parts.length > 2 && DATATYPE.equals(parts[0]));
        if (marryRDD.count() == 0)
            return;
        marryRDD.foreachPartition(it -> {
            BaseDao dao = BaseDao.getInstance();
            List<PlayerInfo> playerInfoList = new ArrayList<>();
            while (it.hasNext()) {
                String[] datas = it.next();
                int groomId = Integer.parseInt(datas[2]);
                int brideId = Integer.parseInt(datas[3]);
                PlayerInfo groomInfo = dao.getPlayerInfo(groomId);
                if (groomInfo != null) {
                    groomInfo.setMateId(brideId);
                    playerInfoList.add(groomInfo);
                }
                PlayerInfo brideInfo = dao.getPlayerInfo(brideId);
                if (brideInfo != null) {
                    brideInfo.setMateId(groomId);
                    playerInfoList.add(brideInfo);
                }
            }
            dao.updatePlayerMarryInfoBatch(playerInfoList);
        });
        JavaPairRDD<String, Integer> counts = marryRDD.mapToPair(datas -> {
            BaseDao dao = BaseDao.getInstance();
            int groomId = Integer.parseInt(datas[2]);
            ServiceInfo serviceInfo = dao.getServiceInfo(groomId);
            int marryMark = Integer.parseInt(datas[4]);
            int marryType = Integer.parseInt(datas[5]);
            int serviceId = serviceInfo == null ? -1 : serviceInfo.getServiceId();
            String key = serviceId + "_" + marryMark + "_" + marryType;
            return new Tuple2<>(key, 1);
        }).reduceByKey((a, b) -> a + b);
        counts.foreachPartition(it -> {
            BaseDao dao = BaseDao.getInstance();
            List<MarryInfo> list1 = new ArrayList<>();
            List<MarryInfo> list2 = new ArrayList<>();
            List<MarryInfo> list3 = new ArrayList<>();
            List<MarryInfo> list4 = new ArrayList<>();
            while (it.hasNext()) {
                Tuple2<String, Integer> t = it.next();
                String[] params = t._1().split("_");
                int serviceId = Integer.parseInt(params[0]);
                int marryMark = Integer.parseInt(params[1]);
                int marryType = Integer.parseInt(params[2]);
                int count = t._2();
                if (marryMark == 2) {
                    MarryInfo marryInfo = dao.getMarryInfo(serviceId);
                    if (marryInfo == null) {
                        marryInfo = new MarryInfo();
                        marryInfo.setServiceId(serviceId);
                        dao.saveMarryInfo(marryInfo);
                    }
                    switch (marryType) {
                    case 1:
                        marryInfo.setLuxuriousNum(marryInfo.getLuxuriousNum() + count);
                        list1.add(marryInfo);
                        break;
                    case 2:
                        marryInfo.setLuxuryNum(marryInfo.getLuxuryNum() + count);
                        list2.add(marryInfo);
                        break;
                    case 3:
                        marryInfo.setRomanticNum(marryInfo.getRomanticNum() + count);
                        list3.add(marryInfo);
                        break;
                    case 4:
                        marryInfo.setGeneralNum(marryInfo.getGeneralNum() + count);
                        list4.add(marryInfo);
                        break;
                    }
                }
            }
            //不要更新未修改的值，未修改的值有可能在其它excutor里更改了，此时更新会把旧的值增覆盖上去
            dao.updateMarryInfoBath(list1, 1);
            dao.updateMarryInfoBath(list2, 2);
            dao.updateMarryInfoBath(list3, 3);
            dao.updateMarryInfoBath(list4, 4);
        });
    }
}
