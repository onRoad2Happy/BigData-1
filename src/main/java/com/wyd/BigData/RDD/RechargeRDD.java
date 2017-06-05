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
import com.wyd.BigData.bean.PlayerInfo;
import com.wyd.BigData.bean.RechargeInfo;
import com.wyd.BigData.dao.BaseDao;
import com.wyd.BigData.util.DateUtil;
public class RechargeRDD implements Serializable {
    /**
     * 
     */
    private static final long    serialVersionUID = -758442520627154431L;
    private static SimpleDateFormat      sf               = new SimpleDateFormat("yyyy_MM_dd");
    private static final Pattern SPACE            = Pattern.compile("\t");

    @SuppressWarnings("serial")
    public void call(JavaRDD<SparkFlumeEvent> rdd) {
        JavaRDD<SparkFlumeEvent> rechargeRDD = filter(rdd);
        if (rechargeRDD.count() == 0) return;
        LogLog.debug("rechargeRDD count:" + rechargeRDD.count());
        rechargeRDD.foreachPartition(new VoidFunction<Iterator<SparkFlumeEvent>>() {
            BaseDao dao = BaseDao.getInstance();
            List<PlayerInfo> playerInfoList = new ArrayList<>();
            List<RechargeInfo> rechargeInfoList = new ArrayList<>();
            @Override
            public void call(Iterator<SparkFlumeEvent> t) throws Exception {
                while (t.hasNext()) {
                    String line = new String(t.next().event().getBody().array(),"UTF-8");
                    String[] datas = SPACE.split(line);
                    int playerId = Integer.parseInt(datas[2]);
                    PlayerInfo playerInfo = dao.getPlayerInfo(playerId);
                    if (null == playerInfo) {
                        playerId = Integer.parseInt(datas[7].split("-")[0]);
                        playerInfo = dao.getPlayerInfo(playerId);
                    }
                    double rechargeMoney = Double.parseDouble(datas[6]);
                    int payChannel = Integer.parseInt(datas[3]);
                    if (null != playerInfo) {
                        Date rechargeTime = new Date(Long.parseLong(datas[1]));
                        if (null == playerInfo.getFirstRecharge()) {
                            playerInfo.setFirstChannel(payChannel);
                            playerInfo.setFirstMoney(rechargeMoney);
                            playerInfo.setFirstRecharge(rechargeTime);
                            playerInfo.setFirstLevel(playerInfo.getPlayerLevel());
                        }
                        playerInfo.setTotalMoney(playerInfo.getTotalMoney() + rechargeMoney);
                        playerInfo.setRechargeNum(playerInfo.getRechargeNum() + 1);
                        int ld = DateUtil.compareDateOnDay(playerInfo.getCreateTime(), rechargeTime);
                        if (ld < 7) {
                            playerInfo.setWltv(playerInfo.getWltv() + rechargeMoney);
                        }
                        if (ld < 31) {
                            playerInfo.setMltv(playerInfo.getMltv() + rechargeMoney);
                        }
                        RechargeInfo info = new RechargeInfo();
                        info.setServiceId(playerInfo.getServiceId());
                        info.setPayChannel(payChannel);
                        info.setPlayerChannel(Integer.parseInt(datas[4]));
                        info.setPlayerId(playerId);
                        info.setProductId(Integer.parseInt(datas[5]));
                        info.setRechargeTime(rechargeTime);
                        info.setMoney(rechargeMoney);
                        info.setOrderNum(datas[7]);
                        if (datas.length > 7) {
                            info.setCount(Integer.parseInt(datas[8]));
                            info.setCountAll(Integer.parseInt(datas[9]));
                        }     
                        playerInfoList.add(playerInfo);
                        rechargeInfoList.add(info);
                    }
                    
                }
                dao.saveRechargeInfoBatch(rechargeInfoList);
                dao.updatePlayerInfoBatch(playerInfoList);
            }
        });
    }

    @SuppressWarnings("serial")
    private JavaRDD<SparkFlumeEvent> filter(JavaRDD<SparkFlumeEvent> rdd) {
        return rdd.filter(flume -> {
                String line = new String(flume.event().getBody().array(),"UTF-8");
                String[] parts = SPACE.split(line);
                return (parts.length >= 2 && "4".equals(parts[0]));
        });
    }
}
