package com.wyd.BigData;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.helpers.LogLog;
import org.apache.spark.SparkConf;


import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;

import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.flume.FlumeUtils;
import org.apache.spark.streaming.flume.SparkFlumeEvent;
import com.wyd.BigData.RDD.CreateRDD;
import com.wyd.BigData.RDD.LoginRDD;
import com.wyd.BigData.RDD.LogoutRDD;
import com.wyd.BigData.RDD.OnlineRDD;
import com.wyd.BigData.RDD.RechargeRDD;
public class App {



    /**
     * 描述：启动服务
     * 注意awaitTermination
     */
    @SuppressWarnings("serial")
    private void launch() throws Exception {
        PropertiesConfiguration config = Global.getInstance().config;
        SparkConf sparkConf = new SparkConf().setAppName("FlumeData");
        // sparkConf.set("spark.streaming.stopGracefullyOnShutdown", "true");
        String host = config.getString("flume_host");
        int port = config.getInt("flume_port");
        int durations = config.getInt("durations");
        // System.out.println("=========host:"+host);
        JavaStreamingContext ssc = new JavaStreamingContext(sparkConf, Durations.seconds(durations));
        Global.getInstance().setSsc(ssc);
        JavaReceiverInputDStream<SparkFlumeEvent> lines = FlumeUtils.createPollingStream(ssc, host, port);
        lines.foreachRDD(rdd->{
            long count = rdd.count();
            LogLog.warn("接收到数据:"+count);
            if ( count == 0) return;

            SparkSession spark = SparkSession.builder().enableHiveSupport().config(rdd.context().getConf()).getOrCreate();
            // 创建用户
            new CreateRDD().call(rdd);
            // 登陆数据入库
            new LoginRDD().call(rdd);
            // 充值数据
            new RechargeRDD().call(rdd);
            // 登出
            new LogoutRDD().call(rdd);
            // 在线人数
            new OnlineRDD().call(rdd);
        });
        ssc.start();
        ssc.awaitTermination();
    }

    public static void main(String[] args) {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(()->{
                JavaStreamingContext ssc = Global.getInstance().getSsc();
                if (ssc != null) {
                    ssc.stop(true, true);
                }
            }) );
            App instance = new App();
            instance.launch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
