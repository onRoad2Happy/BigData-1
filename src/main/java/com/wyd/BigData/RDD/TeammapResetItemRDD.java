package com.wyd.BigData.RDD;
import com.wyd.BigData.bean.ServiceInfo;
import com.wyd.BigData.bean.TeammapInfo;
import com.wyd.BigData.bean.TeammapReset;
import com.wyd.BigData.dao.BaseDao;
import com.wyd.BigData.util.DataType;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import scala.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class TeammapResetItemRDD implements Serializable {
    private static final String DATATYPE         = String.valueOf(DataType.MARKNUM_TEAMMAPRESET_ITEM);
    private static final long   serialVersionUID = -3244599034488592543L;

    public void call(JavaRDD<String[]> rdd) {
        JavaRDD<String[]> teammapRDD = rdd.filter(parts -> parts.length > 2 && DATATYPE.equals(parts[0]));
        if (teammapRDD.count() == 0)
            return;
        //KEY:playerId_sectionId VAL:[1,deplete]
        JavaPairRDD<String, Integer[]> counts = teammapRDD.mapToPair(datas -> new Tuple2<>(datas[2] + "_" + datas[3], new Integer[] { 1, Integer.parseInt(datas[4]) })).reduceByKey((x, y) -> new Integer[] { x[0] + y[0], x[1] + y[1] });
        counts.foreachPartition(it -> {
            BaseDao dao = BaseDao.getInstance();
            List<TeammapInfo> teammapInfoList = new ArrayList<>();
            while (it.hasNext()) {
                Tuple2<String, Integer[]> t = it.next();
                String[] params = t._1().split("_");
                int playerId = Integer.parseInt(params[0]);
                int sectionId = Integer.parseInt(params[1]);
                Integer[] vals = t._2();
                ServiceInfo serviceInfo = dao.getServiceInfo(playerId);
                if (serviceInfo != null) {
                    TeammapInfo teammapInfo = dao.getTeammapInfo(serviceInfo.getServiceId(), sectionId);
                    if (teammapInfo != null) {
                        teammapInfo.setResetCount(teammapInfo.getResetCount() + vals[0]);
                        teammapInfo.setResetDeplete(teammapInfo.getResetDeplete() + vals[1]);
                        teammapInfoList.add(teammapInfo);
                    }
                }
            }
            dao.updateTeammapInfoBath3(teammapInfoList);
        });
        List<String[]> teammapLogList = teammapRDD.collect();
        BaseDao dao = BaseDao.getInstance();
        List<TeammapReset> teammapResetList = new ArrayList<>();
        for (String[] datas : teammapLogList) {
            int playerId = Integer.parseInt(datas[2]);
            int sectionId = Integer.parseInt(datas[3]);
            int deplete = Integer.parseInt(datas[4]);
            ServiceInfo serviceInfo = dao.getServiceInfo(playerId);
            if (serviceInfo == null)
                continue;
            TeammapReset reset = new TeammapReset();
            reset.setPlayerId(playerId);
            reset.setSectionId(sectionId);
            reset.setServiceId(serviceInfo.getServiceId());
            reset.setDeplete(deplete);
            reset.setDataTime(Long.parseLong(datas[1]));
            teammapResetList.add(reset);
        }
        dao.saveTeammapResetBatch(teammapResetList);
    }
}
