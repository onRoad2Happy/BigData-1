package com.wyd.BigData.bean;
import java.io.Serializable;
/**
 * Created by root on 6/12/17.
 */
public class SinglemapItem  implements Serializable {
    private static final long serialVersionUID = 5568121042574104087L;
    private int id;
    private int               serviceId;
    private int               playerId;
    private int               mapId;
    private int               startTime;
    private int               finishTime;
    private int				  passStar; 	//通关星级
    private int               dataTime;//数据时间

    public int getDataTime() {
        return dataTime;
    }

    public void setDataTime(int dataTime) {
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

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public int getPassStar() {
        return passStar;
    }

    public void setPassStar(int passStar) {
        this.passStar = passStar;
    }
}
