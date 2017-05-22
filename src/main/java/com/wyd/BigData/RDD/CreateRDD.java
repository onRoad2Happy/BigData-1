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

import org.apache.spark.api.java.function.VoidFunction;

import org.apache.spark.streaming.flume.SparkFlumeEvent;
import com.wyd.BigData.bean.AccountInfo;
import com.wyd.BigData.bean.AccountNewCount;
import com.wyd.BigData.bean.DeviceInfo;
import com.wyd.BigData.bean.DeviceNewCount;
import com.wyd.BigData.bean.PlayerInfo;
import com.wyd.BigData.bean.PlayerNewCount;
import com.wyd.BigData.dao.BaseDao;
import com.wyd.BigData.util.StringUtil;
public class CreateRDD implements Serializable {
    /**
     * 
     */
    private static final long    serialVersionUID = -758442520627154431L;
    private static SimpleDateFormat      sf               = new SimpleDateFormat("yyyy_MM_dd");
    private static final Pattern SPACE            = Pattern.compile("\t");

    @SuppressWarnings("serial")
    public void call(JavaRDD<SparkFlumeEvent> rdd) {
        final String today = sf.format(Calendar.getInstance().getTime());
        JavaRDD<SparkFlumeEvent> createRDD = filter(rdd);
        if (createRDD.count() == 0) return;
        LogLog.debug("createRDD count:" + createRDD.count());
        createRDD.foreachPartition(new VoidFunction<Iterator<SparkFlumeEvent>>() {
            BaseDao               dao             = BaseDao.getInstance();
            List<AccountNewCount> accountNewList  = new ArrayList<>();
            List<AccountInfo>     accountInfoList = new ArrayList<>();
            List<DeviceNewCount>  deviceNewList   = new ArrayList<>();
            List<DeviceInfo>      deviceInfoList  = new ArrayList<>();
            List<PlayerNewCount>  playerNewList   = new ArrayList<>();
            List<PlayerInfo>      playerInfoList  = new ArrayList<>();

            @Override
            public void call(Iterator<SparkFlumeEvent> t) throws Exception {
                while (t.hasNext()) {
                    String line = new String(t.next().event().getBody().array());
                    String[] datas = SPACE.split(line);
                    Date dataTime = new Date(Long.parseLong(datas[1]));
                    int serviceId = Integer.parseInt(datas[2]);
                    int channelId = Integer.parseInt(datas[3]);
                    int accountId = Integer.parseInt(datas[4]);
                    String mac = StringUtil.filterOffUtf8Mb4(datas[5]);
                    AccountInfo accountInfo = dao.getAccountInfo(accountId);
                    PlayerNewCount playerNewCount = new PlayerNewCount();
                    playerNewCount.setServiceId(serviceId);
                    playerNewCount.setChannelId(channelId);
                    playerNewCount.setPlayerId(Integer.parseInt(datas[12]));
                    playerNewCount.setCreateTime(dataTime);
                    playerNewList.add(playerNewCount);
                    // plyaerInfo
                    PlayerInfo playerInfo = new PlayerInfo();
                    playerInfo.setPlayerId(Integer.parseInt(datas[12]));
                    playerInfo.setServiceId(serviceId);
                    playerInfo.setChannelId(channelId);
                    playerInfo.setAccountId(accountId);
                    playerInfo.setCreateTime(dataTime);
                    playerInfo.setPlayerName(datas[13]);
                    playerInfo.setDeviceMac(mac);
                    playerInfo.setPlayerSex("0".equals(datas[14]) ? "男" : "女");
                    playerInfo.setLoginTime(playerInfo.getCreateTime());
                    playerInfo.setPlayerLevel(1);
                    playerInfo.setUpgradeTime((int) (dataTime.getTime() / 1000));
                    playerInfoList.add(playerInfo);
                    // LogLog.error("accountInfo("+accountId+") is null?"+(accountInfo==null));
                    if (accountInfo == null) {
                        accountInfo = new AccountInfo();
                        accountInfo.setAccountId(accountId);
                        accountInfo.setServiceId(serviceId);
                        accountInfo.setChannelId(channelId);
                        accountInfo.setAccountName(datas[10]);
                        accountInfo.setAccountPwd(datas[11]);
                        accountInfo.setCreateTime(dataTime);
                        accountInfo.setDeviceMac(mac);
                        accountInfo.setSystemType(StringUtil.filterOffUtf8Mb4(datas[7]));
                        accountInfo.setSystemVersion(datas[8]);
                        accountInfoList.add(accountInfo);
                        AccountNewCount accountNewCount = new AccountNewCount();
                        accountNewCount.setServiceId(serviceId);
                        accountNewCount.setChannelId(channelId);
                        accountNewCount.setAccountId(accountId);
                        accountNewCount.setCreateTime(dataTime);
                        accountNewList.add(accountNewCount);
                    }
                    DeviceInfo deviceInfo = dao.getDeviceInfo(mac);
                    if (null == deviceInfo) {
                        deviceInfo = new DeviceInfo();
                        deviceInfo.setServiceId(serviceId);
                        deviceInfo.setChannelId(channelId);
                        deviceInfo.setDeviceMac(mac);
                        deviceInfo.setCreateTime(dataTime);
                        deviceInfo.setDeviceName(StringUtil.filterOffUtf8Mb4(datas[6]));
                        deviceInfo.setSystemName(StringUtil.filterOffUtf8Mb4(datas[7]));
                        deviceInfo.setSystemVersion(datas[8]);
                        deviceInfo.setAppVersion(datas[9]);
                        deviceInfoList.add(deviceInfo);
                        DeviceNewCount deviceNewCount = new DeviceNewCount();
                        deviceNewCount.setServiceId(serviceId);
                        deviceNewCount.setChannelId(channelId);
                        deviceNewCount.setDeviceMac(mac);
                        deviceNewCount.setCreateTime(dataTime);
                        deviceNewList.add(deviceNewCount);
                    }
                }
                dao.saveAccountNewCountBatch(today, accountNewList);
                dao.saveAccountInfoBatch(accountInfoList);
                dao.saveDeviceNewCountBatch(today, deviceNewList);
                dao.saveDeviceInfoBatch(deviceInfoList);
                dao.savePlayerNewCountBatch(today, playerNewList);
                dao.savePlayerInfoBatch(playerInfoList);
            }
        });
    }

    @SuppressWarnings("serial")
    private JavaRDD<SparkFlumeEvent> filter(JavaRDD<SparkFlumeEvent> rdd) {
        return rdd.filter(flume->{
                String line = new String(flume.event().getBody().array());
                String[] parts = SPACE.split(line);
                return (parts.length >= 2 && "1".equals(parts[0]));
            });
    }
}
