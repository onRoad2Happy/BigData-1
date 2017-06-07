package com.wyd.BigData.bean;
/**
 * Created by root on 6/7/17.
 */
public class GuildInfo implements java.io.Serializable {
    private static final long serialVersionUID = 4213656003132465956L;
    private int id;
    private int				  guildId;
    private int               serviceId;
    private int               guildLevel;
    private int               guildNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGuildId() {
        return guildId;
    }

    public void setGuildId(int guildId) {
        this.guildId = guildId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getGuildLevel() {
        return guildLevel;
    }

    public void setGuildLevel(int guildLevel) {
        this.guildLevel = guildLevel;
    }

    public int getGuildNum() {
        return guildNum;
    }

    public void setGuildNum(int guildNum) {
        this.guildNum = guildNum;
    }
}
