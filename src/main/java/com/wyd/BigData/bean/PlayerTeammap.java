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

    public int getSDareCount() {
        return sDareCount;
    }

    public void setSDareCount(int sDareCount) {
        this.sDareCount = sDareCount;
    }

    public int getSPassCount() {
        return sPassCount;
    }

    public void setSPassCount(int sPassCount) {
        this.sPassCount = sPassCount;
    }

    public int getDDareCount() {
        return dDareCount;
    }

    public void setDDareCount(int dDareCount) {
        this.dDareCount = dDareCount;
    }

    public int getDPassCount() {
        return dPassCount;
    }

    public void setDPassCount(int dPassCount) {
        this.dPassCount = dPassCount;
    }

    public int getHDareCount() {
        return hDareCount;
    }

    public void setHDareCount(int hDareCount) {
        this.hDareCount = hDareCount;
    }

    public int getHPassCount() {
        return hPassCount;
    }

    public void setHPassCount(int hPassCount) {
        this.hPassCount = hPassCount;
    }
}
