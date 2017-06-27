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
        //KEY:serviceId_marryMark VAL:[1,marryType1,marryType2,marryType3,marryType4]
        JavaPairRDD<String, Integer[]> counts = marryRDD.mapToPair(datas -> {
            BaseDao dao = BaseDao.getInstance();
            int groomId = Integer.parseInt(datas[2]);
            ServiceInfo serviceInfo = dao.getServiceInfo(groomId);
            int marryMark = Integer.parseInt(datas[4]);
            int marryType = Integer.parseInt(datas[5]);
            int marryType1 = 0, marryType2 = 0, marryType3 = 0, marryType4 = 0;
            switch (marryType) {
            case 1:
                marryType1 = 1;
                break;
            case 2:
                marryType2 = 1;
                break;
            case 3:
                marryType3 = 1;
                break;
            default:
                marryType4 = 1;
                break;
            }
            int serviceId = serviceInfo == null ? -1 : serviceInfo.getServiceId();
            String key = serviceId + "_" + marryMark;
            return new Tuple2<>(key, new Integer[] { 1, marryType1, marryType2, marryType3, marryType4 });
        }).reduceByKey((x, y) -> new Integer[] { x[0] + y[0], x[1] + y[1], x[2] + y[2], x[3] + y[3], x[4] + y[4] });
        counts.foreachPartition(it -> {
            BaseDao dao = BaseDao.getInstance();
            List<MarryInfo> list = new ArrayList<>();
            while (it.hasNext()) {
                Tuple2<String, Integer[]> t = it.next();
                String[] params = t._1().split("_");
                int serviceId = Integer.parseInt(params[0]);
                int marryMark = Integer.parseInt(params[1]);
                int count = t._2()[0];
                int marryType1 = t._2()[1];
                int marryType2 = t._2()[2];
                int marryType3 = t._2()[3];
                int marryType4 = t._2()[4];
                if (marryMark == 2) {
                    MarryInfo marryInfo = dao.getMarryInfo(serviceId);
                    if (marryInfo == null) {
                        marryInfo = new MarryInfo();
                        marryInfo.setServiceId(serviceId);
                        marryInfo.setMarryNum(count);
                        marryInfo.setLuxuriousNum(marryType1);
                        marryInfo.setLuxuryNum(marryType2);
                        marryInfo.setRomanticNum(marryType3);
                        marryInfo.setGeneralNum(marryType4);
                        dao.saveMarryInfo(marryInfo);
                    }
                    marryInfo.setMarryNum(marryInfo.getMarryNum() + count);
                    marryInfo.setLuxuriousNum(marryInfo.getLuxuriousNum() + marryType1);
                    marryInfo.setLuxuryNum(marryInfo.getLuxuryNum() + marryType2);
                    marryInfo.setRomanticNum(marryInfo.getRomanticNum() + marryType3);
                    marryInfo.setGeneralNum(marryInfo.getGeneralNum() + marryType4);
                    list.add(marryInfo);
                }
            }
            dao.updateMarryInfoBath(list, 0);
        });
        List<String[]> marryLogList = marryRDD.collect();
        BaseDao dao = BaseDao.getInstance();
        List<PlayerInfo> playerInfoList = new ArrayList<>();
        for (String[] datas : marryLogList) {
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
    }
}
