package com.wyd.BigData.RDD;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.log4j.helpers.LogLog;
import org.apache.spark.api.java.JavaRDD;

import org.apache.spark.api.java.function.VoidFunction;

import org.apache.spark.streaming.flume.SparkFlumeEvent;
import com.wyd.BigData.bean.OnlineInfo;
import com.wyd.BigData.dao.BaseDao;
public class OnlineRDD implements Serializable {
    /**
     * 
     */
    private static final long    serialVersionUID = -758442520627154431L;
    private static SimpleDateFormat      sf               = new SimpleDateFormat("yyyy_MM_dd");
    private static final Pattern SPACE            = Pattern.compile("\t");

    @SuppressWarnings("serial")
    public void call(JavaRDD<SparkFlumeEvent> rdd) {
        JavaRDD<SparkFlumeEvent> onlineRDD = filter(rdd);
        if (onlineRDD.count() == 0) return;
        LogLog.debug("logoutRDD count:" + onlineRDD.count());
        onlineRDD.foreachPartition(new VoidFunction<Iterator<SparkFlumeEvent>>() {
            BaseDao dao = BaseDao.getInstance();
            List<OnlineInfo> onlineList = new ArrayList<>();            
            @Override
            public void call(Iterator<SparkFlumeEvent> t) throws Exception {
                while (t.hasNext()) {
                    String line = new String(t.next().event().getBody().array(),"UTF-8");
                    String[] datas = SPACE.split(line);
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
            
        });
    }

    @SuppressWarnings("serial")
    private JavaRDD<SparkFlumeEvent> filter(JavaRDD<SparkFlumeEvent> rdd) {
        return rdd.filter(flume->{
                String line = new String(flume.event().getBody().array(),"UTF-8");
                String[] parts = SPACE.split(line);
                return (parts.length >= 2 && "3".equals(parts[0]));
        });
    }
}
