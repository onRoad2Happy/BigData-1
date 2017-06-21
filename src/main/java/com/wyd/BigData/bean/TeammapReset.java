package com.wyd.BigData.bean;
import java.io.Serializable;
/**
 * Created by root on 6/21/17.
 */
public class TeammapReset  implements Serializable {
    private static final long serialVersionUID = 1L;
    private int               id;
    private int               serviceId;
    private int               sectionId;
    private int				  playerId;
    private int				  deplete;		//钻石翻牌消耗
    private long              dataTime;

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

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getDeplete() {
        return deplete;
    }

    public void setDeplete(int deplete) {
        this.deplete = deplete;
    }
}
