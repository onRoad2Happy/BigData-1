package com.wyd.BigData.bean;

import java.util.Date;

public class DeviceNewCount implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int               id;
    private int               serviceId;
    private int               channelId;
    private String            deviceMac;
    private Date              createTime;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
    public String getDeviceMac() {
        return deviceMac;
    }
    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
