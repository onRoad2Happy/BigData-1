package com.wyd.BigData.RDD;
import com.wyd.BigData.bean.ItemLog;
import com.wyd.BigData.bean.PlayerInfo;
import com.wyd.BigData.dao.BaseDao;
import com.wyd.BigData.util.DataType;
import com.wyd.BigData.util.StringUtil;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class ItemLogRDD implements Serializable {
    /**
     *
     */
    private static final long   serialVersionUID = 3946488323487087158L;
    private static final String DATATYPE         = String.valueOf(DataType.MARKNUM_ITEMLOG);

    public void call(JavaRDD<String[]> rdd) {
        JavaRDD<String[]> itemRDD = rdd.filter(parts -> parts.length > 2 && DATATYPE.equals(parts[0]));
        SparkSession spark = SparkSession.builder().enableHiveSupport().config(rdd.context().getConf()).getOrCreate();
        if (rdd.count() == 0)
            return;
        JavaRDD<ItemLog> itemLogRDD = itemRDD.map(datas -> {
            ItemLog itemLog = new ItemLog();
            long dataTime = Long.valueOf(datas[1]);
            int playerId = Integer.parseInt(datas[2]);
            int itemId = Integer.parseInt(datas[3]);
            int changeOrigin = Integer.parseInt(datas[4]);
            int changeType = Integer.parseInt(datas[5]);
            int changeNum = Integer.parseInt(datas[6]);
            int mainType = Integer.parseInt(datas[7]);
            int subType = Integer.parseInt(datas[8]);
            int useType = Integer.parseInt(datas[9]);
            int getItemId = datas[10] != null && !datas[10].equals("") && StringUtil.isNumeric(datas[10]) ? Integer.parseInt(datas[10]) : -1;
            String name = datas[11];
            int accountId = 0;
            int beforeNum = 0;
            int afterNum = 0;
            if (datas.length > 12) {
                accountId = Integer.parseInt(datas[12]);
                beforeNum = Integer.parseInt(datas[13]);
                afterNum = Integer.parseInt(datas[14]);
            }
            itemLog.setTime(dataTime);
            itemLog.setPlayerId(playerId);
            itemLog.setItemId(itemId);
            itemLog.setChangeOrigin(changeOrigin);
            itemLog.setChangeType(changeType);
            itemLog.setChangeNum(changeNum);
            itemLog.setMainType(mainType);
            itemLog.setSubType(subType);
            itemLog.setUseType(useType);
            itemLog.setGetItemId(getItemId);
            itemLog.setName(name);
            itemLog.setAccountId(accountId);
            itemLog.setBeforeNum(beforeNum);
            itemLog.setAfterNum(afterNum);
            return itemLog;
        });
        // 创建一个DataFrame
        Dataset<Row> dataFrame = spark.createDataFrame(itemLogRDD, ItemLog.class);
        if (dataFrame.count() > 0) {
            dataFrame.createOrReplaceTempView("tmp_itemLog");
            String tableName = "tab_itemLog";
            createTable(spark, tableName);
            spark.sql("INSERT INTO " + tableName + " select * from tmp_itemLog");
        }
        //KEY: playerId_itemId  VAL:changeNum
        JavaPairRDD<String, Integer> itemMergeChangeNumRDD = itemRDD.mapToPair(datas -> {
            String playerId = datas[2];
            String itemId = datas[3];
            String changeType = datas[5];
            String key = playerId + "_" + itemId;
            int changeNum = Integer.parseInt(datas[6]);
            changeNum = "0".equals(changeType) ? changeNum : -changeNum;//changeType为0时是增加，其它为减少
            return new Tuple2<>(key, changeNum);
        }).reduceByKey((x, y) -> x + y);
        // update player gold and diamond count
        itemMergeChangeNumRDD.foreachPartition(it -> {
            BaseDao dao = BaseDao.getInstance();
            List<PlayerInfo> diamondInfoList = new ArrayList<>();
            List<PlayerInfo> goldInfoList = new ArrayList<>();
            while (it.hasNext()) {
                Tuple2<String, Integer> t = it.next();
                String[] params = t._1().split("_");
                int count = t._2();
                int playerId = Integer.parseInt(params[0]);
                String itemId = params[1];
                PlayerInfo playerInfo = dao.getPlayerInfo(playerId);
                if (null != playerInfo) {
                    if (itemId.equals("1")) {
                        playerInfo.setDiamond(playerInfo.getDiamond() + count);
                        diamondInfoList.add(playerInfo);
                    } else if (itemId.equals("2")) {
                        playerInfo.setGold(playerInfo.getGold() + count);
                        goldInfoList.add(playerInfo);
                    }
                }
            }
            dao.updatePlayerGoldInfoBatch(goldInfoList);
            dao.updatePlayerDiamondInfoBatch(diamondInfoList);
        });
        //update first cost info
        itemRDD.foreachPartition(it -> {
            BaseDao dao = BaseDao.getInstance();
            while (it.hasNext()) {
                String[] datas = it.next();
                String itemId = datas[3];
                if (itemId.equals("1")) {
                    int playerId = Integer.parseInt(datas[2]);
                    PlayerInfo playerInfo = dao.getPlayerInfo(playerId);
                    if (playerInfo != null && playerInfo.getFirstCostTime() == null) {
                        playerInfo = dao.getPlayerInfo(playerId, false);//从数据库再查一次保证数据准确性
                        if (playerInfo.getFirstCostTime() == null) {
                            long dataTime = Long.valueOf(datas[1]);
                            int changeNum = Integer.parseInt(datas[6]);
                            int getItemId = datas[10] != null && !datas[10].equals("") && StringUtil.isNumeric(datas[10]) ? Integer.parseInt(datas[10]) : -1;
                            playerInfo.setFirstCostTime(new Date(dataTime));
                            playerInfo.setFirstCostLevel(playerInfo.getPlayerLevel());
                            playerInfo.setFirstCostNum(changeNum);
                            playerInfo.setFirstCostItem(getItemId);
                            dao.updatePlayerFirstCostInfo(playerInfo);
                        }
                    }
                }
            }
        });
    }

    private void createTable(SparkSession spark, String tableName) {
        spark.sql("CREATE TABLE IF NOT EXISTS " + tableName + " (time bigint,playerId int,itemId int , changeOrigin int, changeType int, changeNum int, mainType int, subType int, useType int,getItemId int,name string, accountId int, beforeNum int, afterNum int)");
    }
}

