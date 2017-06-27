package com.wyd.BigData.RDD;
import com.wyd.BigData.bean.OnlineInfo;
import com.wyd.BigData.dao.BaseDao;
import com.wyd.BigData.util.DataType;
import org.apache.spark.api.java.JavaRDD;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class OnlineRDD implements Serializable {
    /**
     *
     */
    private static final long   serialVersionUID = -758442520627154431L;
    private static final String DATATYPE         = String.valueOf(DataType.MARKNUM_ONLINENUM);

    //BaseRDD
    public void call(JavaRDD<String[]> rdd) {
        JavaRDD<String[]> onlineRDD = rdd.filter(parts -> parts.length > 2 && DATATYPE.equals(parts[0]));
        if (onlineRDD.count() == 0)
            return;
        List<String[]> onlineLogList = onlineRDD.collect();
        BaseDao dao = BaseDao.getInstance();
        List<OnlineInfo> onlineList = new ArrayList<>();
        for (String[] datas : onlineLogList) {
            long dataTime = Long.parseLong(datas[1]);
            OnlineInfo info = new OnlineInfo();
            onlineList.add(info);
            info.setServiceId(Integer.parseInt(datas[2]));
            info.setOnlineNum(Integer.parseInt(datas[3]));
            if (datas.length > 4) {
                info.setChannelId(Integer.parseInt(datas[4]));
            }
            int minute = (int) (dataTime / 60000);
            info.setDateMinute(minute);
        }
        dao.saveOnlineInfoBatch(onlineList);
    }
}
