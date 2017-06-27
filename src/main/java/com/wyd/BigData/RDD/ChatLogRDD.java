package com.wyd.BigData.RDD;
import com.wyd.BigData.bean.ChatLog;
import com.wyd.BigData.bean.PlayerInfo;
import com.wyd.BigData.dao.BaseDao;
import com.wyd.BigData.util.DataType;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.Serializable;
public class ChatLogRDD implements Serializable {
    private static final String DATATYPE         = String.valueOf(DataType.MARKNUM_CHATLOG);
    private static final long serialVersionUID = 1773415141825433318L;

    public void call(JavaRDD<String[]> rdd) {
        JavaRDD<String[]> chatRDD = rdd.filter(parts -> parts.length > 5 && DATATYPE.equals(parts[0]));
        if (chatRDD.count() == 0)
            return;
        SparkSession spark = SparkSession.builder().enableHiveSupport().config(rdd.context().getConf()).getOrCreate();
        JavaRDD<ChatLog> chatLogRDD = chatRDD.map(datas -> {
            ChatLog chatLog= new ChatLog();
            int dataTime =(int) (Long.parseLong(datas[1])/1000);
            int sendId = Integer.parseInt(datas[2]);
            int reveId = Integer.parseInt(datas[3]);
            int channel = Integer.parseInt(datas[4]);
            String message = datas[5];
            PlayerInfo playerInfo = BaseDao.getInstance().getPlayerInfo(sendId);
            if (null != playerInfo) {
                chatLog.setName(playerInfo.getPlayerName());
                chatLog.setLevel(playerInfo.getPlayerLevel());
                chatLog.setVipLevel(playerInfo.getVipLevel());
            }
            chatLog.setSendId(sendId);
            chatLog.setReveId(reveId);
            chatLog.setChannel(channel);
            chatLog.setSendTime(dataTime);
            chatLog.setMessage(message);
            return chatLog;
        });
        // 创建一个DataFrame
        Dataset<Row> dataFrame = spark.createDataFrame(chatLogRDD, ChatLog.class);
        if (dataFrame.count() > 0) {
            String tableName = "tab_chatLog",tmpName="tmp_chatLog";
            dataFrame.createOrReplaceTempView(tmpName);
            createTable(spark, tableName);
            spark.sql("INSERT INTO " + tableName + " select sendTime,sendId,reveId,channel, name, level, vipLevel, message from "+tmpName);
        }

    }
    private void createTable(SparkSession spark, String tableName) {
        spark.sql("CREATE TABLE IF NOT EXISTS " + tableName + " (sendTime int,sendId int,reveId int,channel int, name string, level int, vipLevel int, message string)");
    }

}
