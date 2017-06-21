package com.wyd.BigData.bean;
import java.util.Date;
/**
 * Created by root on 6/21/17.
 */
public class BattleInfo implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int     id;
    private int     serviceId;
    private Date    createTime;
    private int     battleMode;// 战斗模式 1竞技模式2复活模式
    private int     battleChannel;// 战斗频道1积分2练习3世界boss4组队5排位6公会7弹王
    private boolean isBattleMull;// 是否混战
    private boolean isRobot;// 是否对战机器人
    private int     playerNum;
    private long    totalTime;// 总计耗时
    private int     battleCount;// 战斗次数统计

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getBattleMode() {
        return battleMode;
    }

    public void setBattleMode(int battleMode) {
        this.battleMode = battleMode;
    }

    public int getBattleChannel() {
        return battleChannel;
    }

    public void setBattleChannel(int battleChannel) {
        this.battleChannel = battleChannel;
    }

    public boolean isBattleMull() {
        return isBattleMull;
    }

    public void setBattleMull(boolean battleMull) {
        isBattleMull = battleMull;
    }

    public boolean isRobot() {
        return isRobot;
    }

    public void setRobot(boolean robot) {
        isRobot = robot;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public int getBattleCount() {
        return battleCount;
    }

    public void setBattleCount(int battleCount) {
        this.battleCount = battleCount;
    }
}
