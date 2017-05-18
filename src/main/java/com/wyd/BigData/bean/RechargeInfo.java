package com.wyd.BigData.bean;

import java.util.Date;

public class RechargeInfo implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int               id;
    private int               serviceId;
    private int               payChannel;
    private int               playerChannel;
    private int               playerId;
    private int               productId;
    private Date              rechargeTime;
    private double            money;
    private String            orderNum;
    private int               count;// 充值的钻
    private int               countAll;// 充值后玩家钻石数
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
    public int getPayChannel() {
        return payChannel;
    }
    public void setPayChannel(int payChannel) {
        this.payChannel = payChannel;
    }
    public int getPlayerChannel() {
        return playerChannel;
    }
    public void setPlayerChannel(int playerChannel) {
        this.playerChannel = playerChannel;
    }
    public int getPlayerId() {
        return playerId;
    }
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
    public int getProductId() {
        return productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }
    public Date getRechargeTime() {
        return rechargeTime;
    }
    public void setRechargeTime(Date rechargeTime) {
        this.rechargeTime = rechargeTime;
    }
    public double getMoney() {
        return money;
    }
    public void setMoney(double money) {
        this.money = money;
    }
    public String getOrderNum() {
        return orderNum;
    }
    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public int getCountAll() {
        return countAll;
    }
    public void setCountAll(int countAll) {
        this.countAll = countAll;
    }
    
}
