package com.wyd.BigData.bean;
import org.apache.spark.sql.execution.columnar.INT;
/**
 * Created by root on 6/10/17.
 */
public class ServiceInfo implements java.io.Serializable {
    private static final long serialVersionUID = -4179150293713966049L;
    private int serviceId;
    private int channelId;
    private int accountId;



    public ServiceInfo(int serviceId, int channelId,int accountId) {
        this.channelId = channelId;
        this.serviceId = serviceId;
        this.accountId =  accountId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
