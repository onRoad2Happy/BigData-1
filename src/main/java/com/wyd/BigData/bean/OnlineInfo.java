package com.wyd.BigData.bean;

public class OnlineInfo implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int               id;
    private int               serviceId;
    private int               channelId;
    private int               dateMinute;// 记录时间(分钟)
    private int               onlineNum;
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
    public int getDateMinute() {
        return dateMinute;
    }
    public void setDateMinute(int dateMinute) {
        this.dateMinute = dateMinute;
    }
    public int getOnlineNum() {
        return onlineNum;
    }
    public void setOnlineNum(int onlineNum) {
        this.onlineNum = onlineNum;
    }
    
}
