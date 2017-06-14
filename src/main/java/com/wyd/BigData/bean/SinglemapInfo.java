package com.wyd.BigData.bean;
/**
 * Created by root on 6/12/17.
 */
public class SinglemapInfo implements java.io.Serializable {
    private static final long serialVersionUID = 5654242604970441002L;
    private int id;
    private int               serviceId;
    private int               mapId;
    private long              totalTime;
    private int               dareCount;			//	累计挑战次数
    private int               passCount;			//	累计通关次数
    private int               star1Count;			//	一星通关次数
    private int               star2Count;			//	二星通关次数
    private int               star3Count;			//	三星通关次数

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

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public int getDareCount() {
        return dareCount;
    }

    public void setDareCount(int dareCount) {
        this.dareCount = dareCount;
    }

    public int getPassCount() {
        return passCount;
    }

    public void setPassCount(int passCount) {
        this.passCount = passCount;
    }

    public int getStar1Count() {
        return star1Count;
    }

    public void setStar1Count(int star1Count) {
        this.star1Count = star1Count;
    }

    public int getStar2Count() {
        return star2Count;
    }

    public void setStar2Count(int star2Count) {
        this.star2Count = star2Count;
    }

    public int getStar3Count() {
        return star3Count;
    }

    public void setStar3Count(int star3Count) {
        this.star3Count = star3Count;
    }
}
