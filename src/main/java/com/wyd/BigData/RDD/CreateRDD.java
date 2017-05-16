package com.wyd.BigData.RDD;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.log4j.helpers.LogLog;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.flume.SparkFlumeEvent;
import com.wyd.BigData.bean.AccountInfo;
import com.wyd.BigData.bean.AccountNewCount;
import com.wyd.BigData.dao.BaseDao;
import com.wyd.BigData.util.StringUtil;
public class CreateRDD implements Serializable {
    /**
     * 
     */
    private static final long    serialVersionUID = -758442520627154431L;
    static SimpleDateFormat      sf               = new SimpleDateFormat("yyyy_MM_dd");
    private static final Pattern SPACE            = Pattern.compile("\t");

    @SuppressWarnings("serial")
    public void call(JavaRDD<SparkFlumeEvent> rdd, SparkSession spark) {
        final String today = sf.format(Calendar.getInstance().getTime());
        JavaRDD<SparkFlumeEvent> createRDD = filter(rdd);
        if (createRDD.count() == 0) return;
        LogLog.error("createRDD count:"+createRDD.count());
        createRDD.foreachPartition(new VoidFunction<Iterator<SparkFlumeEvent>>() {
            BaseDao               dao         = BaseDao.getInstance();
            List<AccountNewCount> accountNewList = new ArrayList<>();
            List<AccountInfo> accountInfoList = new ArrayList<>();

            @Override
            public void call(Iterator<SparkFlumeEvent> t) throws Exception {
                while (t.hasNext()) {
                    String line = new String(t.next().event().getBody().array());
                    String[] parts = SPACE.split(line);
                    int accountId = Integer.parseInt(parts[4]);
                    AccountInfo accountInfo = dao.getAccountInfo(accountId);
                    Date dataTime = new Date(Long.parseLong(parts[1]));
                    LogLog.error("accountInfo("+accountId+") is null?"+(accountInfo==null));
                    if (accountInfo == null) {
                        accountInfo = new AccountInfo();
                        accountInfo.setAccountId(accountId);
                        accountInfo.setServiceId(Integer.parseInt(parts[2]));
                        accountInfo.setChannelId(Integer.parseInt(parts[3]));
                        accountInfo.setAccountName(parts[10]);
                        accountInfo.setAccountPwd(parts[11]);
                        accountInfo.setCreateTime(dataTime);
                        accountInfo.setDeviceMac(StringUtil.filterOffUtf8Mb4(parts[5]));
                        accountInfo.setSystemType(StringUtil.filterOffUtf8Mb4(parts[7]));
                        accountInfo.setSystemVersion(parts[8]);
                        accountInfoList.add(accountInfo);
                        AccountNewCount accountNewCount = new AccountNewCount();
                        accountNewCount.setServiceId(Integer.parseInt(parts[2]));
                        accountNewCount.setChannelId(Integer.parseInt(parts[3]));
                        accountNewCount.setAccountId(accountId);
                        accountNewCount.setCreateTime(dataTime);
                        accountNewList.add(accountNewCount);
                    }
                }
                dao.saveAccountNewCountBatch(today, accountNewList);
                dao.saveAccountInfoBatch(accountInfoList);
            }
        });
    }

    @SuppressWarnings("serial")
    private JavaRDD<SparkFlumeEvent> filter(JavaRDD<SparkFlumeEvent> rdd) {
        return rdd.filter(new Function<SparkFlumeEvent, Boolean>() {
            @Override
            public Boolean call(SparkFlumeEvent flume) throws Exception {
                String line = new String(flume.event().getBody().array());
                String[] parts = SPACE.split(line);
                return (parts.length >= 2 && "1".equals(parts[0]));
            }
        });
    }
}
