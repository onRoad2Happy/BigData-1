package com.wyd.BigData.bean;
/**
 * Created by root on 6/12/17.
 */
public class NoviceInfo implements java.io.Serializable {
    private static final long serialVersionUID = 8681424958997433767L;
    private int id;
    private int serviceId;
    private int time;
    private int playerId;
    private int couresId;// 新手id
    private int couresStep;// 新手步骤
    private int accountId;

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

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getCouresId() {
        return couresId;
    }

    public void setCouresId(int couresId) {
        this.couresId = couresId;
    }

    public int getCouresStep() {
        return couresStep;
    }

    public void setCouresStep(int couresStep) {
        this.couresStep = couresStep;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
