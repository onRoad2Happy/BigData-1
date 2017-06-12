package com.wyd.BigData.RDD;
import com.wyd.BigData.bean.PlayerInfo;
import com.wyd.BigData.dao.BaseDao;
import com.wyd.BigData.util.DataType;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import scala.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class VipUpgradeRDD implements Serializable {
    private static final long serialVersionUID = -8397029531035009187L;

    private static final String DATATYPE         = String.valueOf(DataType.MARKNUM_VIPUPGRADE);

    public void call(JavaRDD<String[]> rdd) {
        JavaRDD<String[]> vipUpgradeRDD = rdd.filter(parts -> parts.length > 2 && DATATYPE.equals(parts[0]));

        if (vipUpgradeRDD.count() == 0)
            return;
        JavaPairRDD<String, Integer> maxLevelRDD = vipUpgradeRDD.mapToPair(datas -> new Tuple2<>(datas[2], Integer.parseInt(datas[3]))).reduceByKey(Math::max);
        maxLevelRDD.foreachPartition(it -> {
            BaseDao dao = BaseDao.getInstance();
            List<PlayerInfo> playerInfoList = new ArrayList<>();
            while (it.hasNext()) {
                Tuple2<String,Integer> t=it.next();
                int playerId = Integer.parseInt(t._1());
                int vipLevel = t._2();
                PlayerInfo info = dao.getPlayerInfo(playerId);
                if(info !=null){
                    info.setVipLevel(vipLevel);
                    playerInfoList.add(info);
                }
            }
            dao.updateVipLevelBatch(playerInfoList);
        });
    }

}
