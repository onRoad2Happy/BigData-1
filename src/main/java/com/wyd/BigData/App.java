package com.wyd.BigData;
import com.wyd.BigData.RDD.*;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.helpers.LogLog;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.catalyst.plans.logical.Join;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.flume.FlumeUtils;
import org.apache.spark.streaming.flume.SparkFlumeEvent;

import java.util.regex.Pattern;
public class App {

    private static final Pattern SPACE = Pattern.compile("\t");

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
        ssc.checkpoint("/checkpointofspark");
        Global.getInstance().setSsc(ssc);
        JavaReceiverInputDStream<SparkFlumeEvent> lines = FlumeUtils.createPollingStream(ssc, host, port);
        lines.foreachRDD(rdd->{
            long count = rdd.count();
            LogLog.warn("接收到数据:"+count);
            if ( count == 0) return;
             JavaRDD<String[]> strsRDD= rdd.map(flume->{
                String line = new String(flume.event().getBody().array(),"UTF-8");
                return SPACE.split(line);
            });
            // 创建用户
            new CreateRDD().call(strsRDD);
            // 登陆数据入库
            new LoginRDD().call(strsRDD);
            // 登出
            new LogoutRDD().call(strsRDD);
            // 充值数据
            new RechargeRDD().call(strsRDD);
            // 在线人数
            new OnlineRDD().call(strsRDD);
            // 角色升级
            new UpgradeRDD().call(strsRDD);
            // VIP升级
            new VipUpgradeRDD().call(strsRDD);
            // 创建公会
            new CreateGuildRDD().call(strsRDD);
            // 公会升级
            new GuildUpgradeRDD().call(strsRDD);
            // 加入公会
            new JoinGuildRDD().call(strsRDD);
            // 退出公会
            new QuitGuildRDD().call(strsRDD);
            // 结婚
            new MarryRDD().call(strsRDD);
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
