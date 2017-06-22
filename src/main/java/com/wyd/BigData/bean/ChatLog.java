package com.wyd.BigData.bean;
/**
 * Created by root on 6/21/17.
 */
public class ChatLog implements java.io.Serializable {
    private static final long serialVersionUID = 132773662251768303L;
    private int sendId;
    private int reveId;
    private int channel;
    private String name;
    private int level;
    private int vipLevel;
    private String message;
    private int sendTime;

    public int getSendTime() {
        return sendTime;
    }

    public void setSendTime(int sendTime) {
        this.sendTime = sendTime;
    }

    public int getSendId() {
        return sendId;
    }

    public void setSendId(int sendId) {
        this.sendId = sendId;
    }

    public int getReveId() {
        return reveId;
    }

    public void setReveId(int reveId) {
        this.reveId = reveId;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
