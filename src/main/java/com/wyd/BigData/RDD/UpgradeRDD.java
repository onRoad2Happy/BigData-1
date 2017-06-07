package com.wyd.BigData.RDD;
import com.wyd.BigData.bean.PlayerInfo;
import com.wyd.BigData.bean.PlayerLevelInfo;
import com.wyd.BigData.bean.UpgradeInfo;
import com.wyd.BigData.dao.BaseDao;
import org.apache.log4j.helpers.LogLog;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.streaming.flume.SparkFlumeEvent;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
public class UpgradeRDD implements Serializable {
    private static final Pattern          SPACE            = Pattern.compile("\t");
    private static final long             serialVersionUID = 35472250916594214L;
    private static           SimpleDateFormat sf               = new SimpleDateFormat("yyyy_MM_dd");
    @SuppressWarnings("serial")
    public void call(JavaRDD<String[]> rdd) {
        JavaRDD<String[]> upgradeRDD = filter(rdd);
        if (upgradeRDD.count() == 0)
            return;
        upgradeRDD.map(parts->{
            String day = sf.format(new Date(Long.parseLong(parts[1])));
            return RowFactory.create(Integer.valueOf(parts[2]), Integer.valueOf(parts[3]), Integer.valueOf(parts[5]), day);
        }).distinct();

    }

    @SuppressWarnings("serial") private JavaRDD<String[]> filter(JavaRDD<String[]> rdd) {
        return rdd.filter(parts -> (parts.length >= 2 && "6".equals(parts[0])));
    }
}
