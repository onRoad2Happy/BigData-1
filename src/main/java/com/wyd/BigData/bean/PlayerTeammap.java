package com.wyd.BigData.bean;
/**
 * Created by root on 6/17/17.
 */
public class PlayerTeammap implements java.io.Serializable {
    private static final long serialVersionUID = -910601445384630591L;
    private int id;
    private int               serviceId;
    private int               playerId;
    private int               sectionId;		//	章节id
    private int               sDareCount;		//	简单累计挑战次数
    private int               sPassCount;		//	简单累计通关次数
    private int               dDareCount;		//	困难累计挑战次数
    private int               dPassCount;		//	困难累计通关次数
    private int               hDareCount;		//	地狱累计挑战次数
    private int               hPassCount;		//	地狱累计通关次数

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

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public int getsDareCount() {
        return sDareCount;
    }

    public void setsDareCount(int sDareCount) {
        this.sDareCount = sDareCount;
    }

    public int getsPassCount() {
        return sPassCount;
    }

    public void setsPassCount(int sPassCount) {
        this.sPassCount = sPassCount;
    }

    public int getdDareCount() {
        return dDareCount;
    }

    public void setdDareCount(int dDareCount) {
        this.dDareCount = dDareCount;
    }

    public int getdPassCount() {
        return dPassCount;
    }

    public void setdPassCount(int dPassCount) {
        this.dPassCount = dPassCount;
    }

    public int gethDareCount() {
        return hDareCount;
    }

    public void sethDareCount(int hDareCount) {
        this.hDareCount = hDareCount;
    }

    public int gethPassCount() {
        return hPassCount;
    }

    public void sethPassCount(int hPassCount) {
        this.hPassCount = hPassCount;
    }
}
