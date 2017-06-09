package com.wyd.BigData.RDD;
import com.wyd.BigData.bean.GuildInfo;
import com.wyd.BigData.dao.BaseDao;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import scala.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class GuildUpgradeRDD implements Serializable {
    private static final long serialVersionUID = 1902046021195565690L;
   @SuppressWarnings("serial") public void call(JavaRDD<String[]> rdd) {
        JavaRDD<String[]> upgradeRDD = filter(rdd);
        if (upgradeRDD.count() == 0)
            return;
        JavaPairRDD<String, Integer> maxLevelRDD = upgradeRDD.mapToPair(datas -> new Tuple2<>(datas[2], Integer.parseInt(datas[3]))).reduceByKey(Math::max);
        maxLevelRDD.foreachPartition(it -> {
            BaseDao dao = BaseDao.getInstance();
            List<GuildInfo> guildInfoList = new ArrayList<>();
            while (it.hasNext()) {
                Tuple2<String,Integer> t=it.next();
                int guildId = Integer.parseInt(t._1());
                int level = t._2();
                GuildInfo info = dao.getGuildInfo(guildId);
                if(info !=null){
                    info.setGuildLevel(level);
                    guildInfoList.add(info);
                }
            }
            dao.updateGuildInfoBatch(guildInfoList);
        });
    }

    @SuppressWarnings("serial") private JavaRDD<String[]> filter(JavaRDD<String[]> rdd) {
        return rdd.filter(parts -> (parts.length >= 2 && "9".equals(parts[0])));
    }
}
