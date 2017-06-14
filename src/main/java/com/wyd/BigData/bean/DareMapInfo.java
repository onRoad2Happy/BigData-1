package com.wyd.BigData.bean;
/**
 * Created by root on 6/12/17.
 */
public class DareMapInfo implements java.io.Serializable {
    private static final long serialVersionUID = -4443889398916782901L;
    /** 进入副本   */
    public static final int COME_IN = 1;
    /** 挑战副本成功   */
    public static final int COME_OUT = 2;
    /** 单人副本   */
    public static final int SINGLE_MAP = 1;
    /** 组队副本   */
    public static final int TEAM_MAP = 2;

    private int               id;
    private int               serviceId;
    private int               time; //挑战次数
    private int               playerId;
    private int               mapId;// 副本ID
    private int               type;// 副本类型(1单人副本，2组队副本)
    private int               accountId;// 玩家账号
    private int               difficult;// 副本难度
    private int               action;// 1,进入副本，2,挑战成功
    private int               recordTime;
    private int               challengeType;//0 挑战  1 扫荡


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

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getDifficult() {
        return difficult;
    }

    public void setDifficult(int difficult) {
        this.difficult = difficult;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(int recordTime) {
        this.recordTime = recordTime;
    }

    public int getChallengeType() {
        return challengeType;
    }

    public void setChallengeType(int challengeType) {
        this.challengeType = challengeType;
    }
}
