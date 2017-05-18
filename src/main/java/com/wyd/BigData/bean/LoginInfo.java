package com.wyd.BigData.bean;

import java.util.Date;

public class LoginInfo implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int               id;
    private int               serviceId;
    private int               channelId;
    private int               accountId;
    private int               playerId;
    private String            deviceMac;
    private String            deviceName;
    private String            systemName;
    private String            systemVersion;
    private String            appVersion;
    private Date              loginTime;
    private Date              logoutTime;
    private int               onlineTime;// 在线时长
    private String            loginIp;
    private int               playerLevel;// 玩家等级
    private int               diamond;
    private int               gold;
    private int               vigor;// 玩家体力
    private String            accountName;
    private String            playerName;
    private int               playerChannel;
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
    public int getAccountId() {
        return accountId;
    }
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
    public int getPlayerId() {
        return playerId;
    }
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
    public String getDeviceMac() {
        return deviceMac;
    }
    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }
    public String getDeviceName() {
        return deviceName;
    }
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    public String getSystemName() {
        return systemName;
    }
    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
    public String getSystemVersion() {
        return systemVersion;
    }
    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }
    public String getAppVersion() {
        return appVersion;
    }
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
    public Date getLoginTime() {
        return loginTime;
    }
    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }
    public Date getLogoutTime() {
        return logoutTime;
    }
    public void setLogoutTime(Date logoutTime) {
        this.logoutTime = logoutTime;
    }
    public int getOnlineTime() {
        return onlineTime;
    }
    public void setOnlineTime(int onlineTime) {
        this.onlineTime = onlineTime;
    }
    public String getLoginIp() {
        return loginIp;
    }
    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }
    public int getPlayerLevel() {
        return playerLevel;
    }
    public void setPlayerLevel(int playerLevel) {
        this.playerLevel = playerLevel;
    }
    public int getDiamond() {
        return diamond;
    }
    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }
    public int getGold() {
        return gold;
    }
    public void setGold(int gold) {
        this.gold = gold;
    }
    public int getVigor() {
        return vigor;
    }
    public void setVigor(int vigor) {
        this.vigor = vigor;
    }
    public String getAccountName() {
        return accountName;
    }
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public int getPlayerChannel() {
        return playerChannel;
    }
    public void setPlayerChannel(int playerChannel) {
        this.playerChannel = playerChannel;
    }
    
}
