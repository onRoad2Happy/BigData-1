package com.wyd.BigData.bean;
import java.io.Serializable;
/**
 * Created by root on 6/19/17.
 */
public class TeammapLottery implements Serializable {
    private static final long serialVersionUID = 1548192114608397510L;
    private int id;
    private int serviceId;
    private int sectionId;
    private int difficulty;	//难度
    private int deplete;		//钻石翻牌消耗
    private int playerId;
    private long dataTime;

    public long getDataTime() {
        return dataTime;
    }

    public void setDataTime(long dataTime) {
        this.dataTime = dataTime;
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

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDeplete() {
        return deplete;
    }

    public void setDeplete(int deplete) {
        this.deplete = deplete;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }


}
