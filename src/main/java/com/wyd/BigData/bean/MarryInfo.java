package com.wyd.BigData.bean;
/**
 * Created by root on 6/9/17.
 */
public class MarryInfo implements java.io.Serializable {
    private static final long serialVersionUID = -2972655633727904948L;
    private int id;
    private int serviceId;
    private int marryNum;// 结婚次数
    private int divorceNum;// 离婚次数
    private int luxuriousNum;//豪华的;
    private int luxuryNum;//    奢侈品;
    private int romanticNum;//    浪漫;
    private int generalNum;//一般;

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

    public int getMarryNum() {
        return marryNum;
    }

    public void setMarryNum(int marryNum) {
        this.marryNum = marryNum;
    }

    public int getDivorceNum() {
        return divorceNum;
    }

    public void setDivorceNum(int divorceNum) {
        this.divorceNum = divorceNum;
    }

    public int getLuxuriousNum() {
        return luxuriousNum;
    }

    public void setLuxuriousNum(int luxuriousNum) {
        this.luxuriousNum = luxuriousNum;
    }

    public int getLuxuryNum() {
        return luxuryNum;
    }

    public void setLuxuryNum(int luxuryNum) {
        this.luxuryNum = luxuryNum;
    }

    public int getRomanticNum() {
        return romanticNum;
    }

    public void setRomanticNum(int romanticNum) {
        this.romanticNum = romanticNum;
    }

    public int getGeneralNum() {
        return generalNum;
    }

    public void setGeneralNum(int generalNum) {
        this.generalNum = generalNum;
    }
}
