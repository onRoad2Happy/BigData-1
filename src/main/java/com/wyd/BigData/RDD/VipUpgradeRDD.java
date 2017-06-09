package com.wyd.BigData.RDD;
import com.wyd.BigData.bean.PlayerInfo;
import com.wyd.BigData.bean.PlayerLevelInfo;
import com.wyd.BigData.bean.UpgradeInfo;
import com.wyd.BigData.dao.BaseDao;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.flume.SparkFlumeEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
public class VipUpgradeRDD implements Serializable {
    private static final Pattern SPACE            = Pattern.compile("\t");
    private static final long    serialVersionUID = -8397029531035009187L;

    @SuppressWarnings("serial") public void call(JavaRDD<String[]> rdd) {
        JavaRDD<String[]> rechargeRDD = filter(rdd);
        if (rechargeRDD.count() == 0)
            return;
        //LogLog.debug("rechargeRDD count:" + rechargeRDD.count());
        rechargeRDD.foreachPartition(t -> {
            BaseDao dao = BaseDao.getInstance();
            List<PlayerInfo> playerInfoList = new ArrayList<>();
            while (t.hasNext()) {
                String[] datas = t.next();
                int playerId = Integer.parseInt(datas[2]);
                int vipLevel = Integer.parseInt(datas[3]);
                PlayerInfo playerInfo = dao.getPlayerInfo(playerId);
                if (null != playerInfo) {
                    playerInfo.setVipLevel(vipLevel);
                    playerInfoList.add(playerInfo);
                }
            }
            dao.updateVipLevelBatch(playerInfoList);
        });
    }

    @SuppressWarnings("serial") private JavaRDD<String[]> filter(JavaRDD<String[]> rdd) {
        return rdd.filter(parts -> (parts.length >= 2 && "7".equals(parts[0])));
    }
}
