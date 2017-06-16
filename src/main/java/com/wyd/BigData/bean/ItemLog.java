package com.wyd.BigData.bean;
/**
 * Created by root on 6/14/17.
 */
public class ItemLog  implements java.io.Serializable {
    private static final long serialVersionUID = 5844661744302780196L;
    private long time;
    private int playerId;
    private int itemId;
    private int changeOrigin;
    private int changeType;
    private int changeNum;
    private int mainType;
    private int subType;
    private int useType;
    private int getItemId;
    private String name;
    private int accountId;
    private int beforeNum;
    private int afterNum;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getChangeOrigin() {
        return changeOrigin;
    }

    public void setChangeOrigin(int changeOrigin) {
        this.changeOrigin = changeOrigin;
    }

    public int getChangeType() {
        return changeType;
    }

    public void setChangeType(int changeType) {
        this.changeType = changeType;
    }

    public int getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(int changeNum) {
        this.changeNum = changeNum;
    }

    public int getMainType() {
        return mainType;
    }

    public void setMainType(int mainType) {
        this.mainType = mainType;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public int getUseType() {
        return useType;
    }

    public void setUseType(int useType) {
        this.useType = useType;
    }

    public int getGetItemId() {
        return getItemId;
    }

    public void setGetItemId(int getItemId) {
        this.getItemId = getItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getBeforeNum() {
        return beforeNum;
    }

    public void setBeforeNum(int beforeNum) {
        this.beforeNum = beforeNum;
    }

    public int getAfterNum() {
        return afterNum;
    }

    public void setAfterNum(int afterNum) {
        this.afterNum = afterNum;
    }
}
