package com.wyd.BigData.bean;
/**
 * Created by root on 6/6/17.
 */
public class PlayerLevelInfo implements java.io.Serializable {
    private static final long serialVersionUID = 1870896559032168059L;
    private long id;
    private int  serviceId;
    private int  channelId;
    private int  level;
    private int  playerCount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }
}
