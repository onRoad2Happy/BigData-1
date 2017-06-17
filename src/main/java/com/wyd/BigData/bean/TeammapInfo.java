package com.wyd.BigData.bean;
/**
 * Created by root on 6/16/17.
 */
public class TeammapInfo  implements java.io.Serializable {
    private static final long serialVersionUID = 6631649480175553130L;
    private int id;
    private int               serviceId;
    private int               sectionId;			//	章节Id
    private long              sTotalTime;			//	简单累计耗时
    private int               sDareCount;			//	简单累计挑战次数

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

    public long getSTotalTime() {
        return sTotalTime;
    }

    public void setSTotalTime(long sTotalTime) {
        this.sTotalTime = sTotalTime;
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

    public int getSMember1Count() {
        return sMember1Count;
    }

    public void setSMember1Count(int sMember1Count) {
        this.sMember1Count = sMember1Count;
    }

    public int getSMember2Count() {
        return sMember2Count;
    }

    public void setSMember2Count(int sMember2Count) {
        this.sMember2Count = sMember2Count;
    }

    public int getSMember3Count() {
        return sMember3Count;
    }

    public void setSMember3Count(int sMember3Count) {
        this.sMember3Count = sMember3Count;
    }

    public int getSLotteryCount() {
        return sLotteryCount;
    }

    public void setSLotteryCount(int sLotteryCount) {
        this.sLotteryCount = sLotteryCount;
    }

    public int getSLotteryDeplete() {
        return sLotteryDeplete;
    }

    public void setSLotteryDeplete(int sLotteryDeplete) {
        this.sLotteryDeplete = sLotteryDeplete;
    }

    public long getDTotalTime() {
        return dTotalTime;
    }

    public void setDTotalTime(long dTotalTime) {
        this.dTotalTime = dTotalTime;
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

    public int getDMember1Count() {
        return dMember1Count;
    }

    public void setDMember1Count(int dMember1Count) {
        this.dMember1Count = dMember1Count;
    }

    public int getDMember2Count() {
        return dMember2Count;
    }

    public void setDMember2Count(int dMember2Count) {
        this.dMember2Count = dMember2Count;
    }

    public int getDMember3Count() {
        return dMember3Count;
    }

    public void setDMember3Count(int dMember3Count) {
        this.dMember3Count = dMember3Count;
    }

    public int getDLotteryCount() {
        return dLotteryCount;
    }

    public void setDLotteryCount(int dLotteryCount) {
        this.dLotteryCount = dLotteryCount;
    }

    public int getDLotteryDeplete() {
        return dLotteryDeplete;
    }

    public void setDLotteryDeplete(int dLotteryDeplete) {
        this.dLotteryDeplete = dLotteryDeplete;
    }

    public long getHTotalTime() {
        return hTotalTime;
    }

    public void setHTotalTime(long hTotalTime) {
        this.hTotalTime = hTotalTime;
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

    public int getHMember1Count() {
        return hMember1Count;
    }

    public void setHMember1Count(int hMember1Count) {
        this.hMember1Count = hMember1Count;
    }

    public int getHMember2Count() {
        return hMember2Count;
    }

    public void setHMember2Count(int hMember2Count) {
        this.hMember2Count = hMember2Count;
    }

    public int getHMember3Count() {
        return hMember3Count;
    }

    public void setHMember3Count(int hMember3Count) {
        this.hMember3Count = hMember3Count;
    }

    public int getHLotteryCount() {
        return hLotteryCount;
    }

    public void setHLotteryCount(int hLotteryCount) {
        this.hLotteryCount = hLotteryCount;
    }

    public int getHLotteryDeplete() {
        return hLotteryDeplete;
    }

    public void setHLotteryDeplete(int hLotteryDeplete) {
        this.hLotteryDeplete = hLotteryDeplete;
    }

    public int getResetCount() {
        return resetCount;
    }

    public void setResetCount(int resetCount) {
        this.resetCount = resetCount;
    }

    public int getResetDeplete() {
        return resetDeplete;
    }

    public void setResetDeplete(int resetDeplete) {
        this.resetDeplete = resetDeplete;
    }

    private int  sPassCount;			//	简单累计通关次数
    private int  sMember1Count;			//	简单一人通关次数
    private int  sMember2Count;			//	简单二人通关次数
    private int  sMember3Count;			//	简单三人通关次数
    private int  sLotteryCount;			//	简单钻石翻牌次数
    private int  sLotteryDeplete;			//	简单钻石翻牌消耗
    private long dTotalTime;			//	困难累计耗时
    private int  dDareCount;			//	困难累计挑战次数
    private int  dPassCount;			//	困难累计通关次数
    private int               dMember1Count;			//	困难一人通关次数
    private int               dMember2Count;			//	困难二人通关次数
    private int               dMember3Count;			//	困难三人通关次数
    private int               dLotteryCount;			//	困难钻石翻牌次数
    private int               dLotteryDeplete;			//	困难钻石翻牌消耗
    private long              hTotalTime;			//	地狱累计耗时
    private int               hDareCount;			//	地狱累计挑战次数
    private int               hPassCount;			//	地狱累计通关次数
    private int               hMember1Count;			//	地狱一人通关次数
    private int               hMember2Count;			//	地狱二人通关次数
    private int               hMember3Count;			//	地狱三人通关次数
    private int               hLotteryCount;			//	地狱钻石翻牌次数
    private int               hLotteryDeplete;			//	地狱钻石翻牌消耗
    private int               resetCount;			//	重置副本次数
    private int               resetDeplete;			//	重置消耗
}
