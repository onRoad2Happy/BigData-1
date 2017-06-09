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
public class MarryRDD implements Serializable {
    private static final long serialVersionUID = -3611622090732914055L;
    private static final String DATATYPE= String.valueOf(DataType.MARKNUM_MARRY);
    public void call(JavaRDD<String[]> rdd) {
        JavaRDD<String[]> marryRDD = rdd.filter(parts -> parts.length>2 && DATATYPE.equals(parts[0]));
        if (marryRDD.count() == 0)
            return;
        //更 新玩家信息
        marryRDD.foreachPartition(it -> {
            BaseDao dao = BaseDao.getInstance();
            List<PlayerInfo> playerInfoList = new ArrayList<>();
            while (it.hasNext()) {
                String[] datas = it.next();
                int groomId = Integer.parseInt(datas[2]);
                int brideId = Integer.parseInt(datas[3]);
                PlayerInfo groomInfo = dao.getPlayerInfo(groomId);
                if (groomInfo != null ) {
                    groomInfo.setMateId(brideId);
                    playerInfoList.add(groomInfo);
                }
                PlayerInfo brideInfo = dao.getPlayerInfo(brideId);
                if (brideInfo != null ) {
                    brideInfo.setMateId(groomId);
                    playerInfoList.add(brideInfo);
                }
            }
            dao.updatePlayerMarryInfoBatch(playerInfoList);
        });
        //更新公会人数
        JavaPairRDD<String,Integer> counts=marryRDD.mapToPair(datas->new Tuple2<>(datas[3],1)).reduceByKey((a,b)->a+b);

    }

}
