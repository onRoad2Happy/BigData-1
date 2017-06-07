package com.wyd.BigData.bean;
public class LoginSumInfo  implements java.io.Serializable {
    private static final long serialVersionUID = -1830443850353667946L;
    private int id;
    private int               serviceId;
    private int               channelId;
    private int               playerId;
    private int               playerChannel;

    public LoginSumInfo() {
    }

    public LoginSumInfo(int serviceId, int channelId, int playerChannel, int playerId) {
        this.serviceId = serviceId;
        this.channelId = channelId;
        this.channelId = channelId;
        this.playerId = playerId;
        this.playerChannel = playerChannel;
    }

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

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerChannel() {
        return playerChannel;
    }

    public void setPlayerChannel(int playerChannel) {
        this.playerChannel = playerChannel;
    }
}
