package com.wyd.BigData.bean;

import java.util.Date;

public class PlayerInfo implements java.io.Serializable {
    private static final long serialVersionUID = 2950486540693172760L;
    private int id;
    private int playerId;
    private int serviceId;
    private int channelId;
    private int accountId;
    private String deviceMac;
    private Date createTime;
    private String playerName;
    private String playerSex;
    private int playerLevel;
    private int upgradeTime;
    private int playerFighting;
    private int vipLevel;
    private int sportsLevel;
    private int rankingLevel;
    private Date loginTime;
    private boolean isTwo;// 是否次留
    private boolean isThird;// 
    private boolean isFour;// 
    private boolean isFive;// 是否5留
    private boolean isSix;// 是否6留
    private boolean isSeven;// 是否7留
    private boolean isEight;// 是否8留
    private boolean isNine;// 是否9留
    private boolean isTen;// 是否10留
    private boolean isEleven;// 是否11留
    private boolean isTwelve;// 是否12留
    private boolean isThirteen;// 是否13留
    private boolean isFourteen;// 是否双周留
    private boolean isThirty;// 是否30留
    private boolean isSixty;// 是否60留
    private double totalMoney;
    private int rechargeNum;// 充值次数
    private int firstChannel;// 首冲渠道
    private int firstLevel;// 首冲玩家等级
    private double firstMoney;
    private Date firstRecharge;
    private Date firstCostTime;// 首次消耗钻石时间
    private int firstCostNum;// 首次消耗钻石数量
    private int firstCostLevel;// 首次消耗钻石等级
    private int firstCostItem;// 首次消耗钻石购买物品
    private double wltv;// 周ltv
    private double mltv;// 月ltv
    private int diamond;
    private int gold;
    private int loginNum;// 登录次数
    private int sevenNum;// 7天内登录次数
    private int totalOnline;// 总计在线时长
    private int guildId;
    private int couresId;// 新手id
    private int couresStep;// 新手步骤
    private int tiroTime;// 新手触发时间
    private int mateId;// 配偶id
    private int topSinglemap;// 最高通关副本
    private int topDareSinglemap;// 最高挑战副本
    private int topSinglemapTime;// 最高副本通关时长
    private int topEliteSinglemap;// 最高通关精英副本
    private int topDareEliteSinglemap;// 最高挑战精英副本
    private int topEliteSinglemapTime;// 最高精英副本通关时长
    private int vigor;// 体力
    private int battleWinMarry;// 战斗胜利场数
    private int battleWinGuild;// 公会胜利场数
    private String gagTime;// 禁言时间
    private String gagReason;// 禁言原因
    
    private int loginDays;      //登录天数
    private int friendCount;    //好友数
    private int weaponId;       //已装备武器ID
    private int weaponItemId;   //武器物品ID
    private int weaponLevel;    //武器等级
    private int necklaceId;     //已装备项链ID
    private int necklaceItemId; //项链物品ID
    private int necklaceLevel;  //项链等级
    private int ringId;         //已装备戒指ID
    private int ringItemId;     //戒指物品ID
    private int ringLevel;      //戒指等级
    private int braceletId;     //已装备护腕ID
    private int braceletItemId; //护腕物品ID
    private int braceletLevel;  //护腕等级
    private int talismanId;     //已装备徽章ID
    private int talismanItemId; //徽章物品ID
    private int talismanLevel;  //徽章等级
    private int medalId;        //已装备勋章ID
    private int medalItemId;    //勋章物品ID
    private int medalLevel;     //勋章等级
    private int propFuryLevel;          //怒火等级
    private int propHidesingleLevel;    //单体隐身等级
    private int propHidegroupLevel;     //群体隐身等级
    private int propReflectLevel;       //反射盾等级
    private int propTreatsingleLevel;   //单体治疗等级
    private int propTreatgroupLevel;    //群体治疗等级
    private int propGuardianLevel;      //守护盾等级
    private int propDiceLevel;          //命运骰蛊等级
    private int petId;      //出战宝宝ID
    private int petItemId;  //宝宝物品ID
    private int petLevel;   //宝宝等级
    private int rankMatchNum;   //排位赛次数
    private int contactNum;     //联习赛次数
    private int topTowermap;    //最高爬塔副本
    private int towermapNum;    //爬塔副本重置次数
    private int teammapNum;     //组队副本次数
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getPlayerId() {
        return playerId;
    }
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
    public int getServiceId() {
        return serviceId;
    }
    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }
    public int getChannelId() {
        return channelId;
    }
    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }
    public int getAccountId() {
        return accountId;
    }
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }   
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public String getPlayerSex() {
        return playerSex;
    }
    public void setPlayerSex(String playerSex) {
        this.playerSex = playerSex;
    }
    public Date getLoginTime() {
        return loginTime;
    }
    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }
    public String getDeviceMac() {
        return deviceMac;
    }
    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }
    public int getPlayerLevel() {
        return playerLevel;
    }
    public void setPlayerLevel(int playerLevel) {
        this.playerLevel = playerLevel;
    }
    public int getUpgradeTime() {
        return upgradeTime;
    }
    public void setUpgradeTime(int upgradeTime) {
        this.upgradeTime = upgradeTime;
    }
    public int getPlayerFighting() {
        return playerFighting;
    }
    public void setPlayerFighting(int playerFighting) {
        this.playerFighting = playerFighting;
    }
    public int getVipLevel() {
        return vipLevel;
    }
    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }
    public int getSportsLevel() {
        return sportsLevel;
    }
    public void setSportsLevel(int sportsLevel) {
        this.sportsLevel = sportsLevel;
    }
    public int getRankingLevel() {
        return rankingLevel;
    }
    public void setRankingLevel(int rankingLevel) {
        this.rankingLevel = rankingLevel;
    }
    public boolean isTwo() {
        return isTwo;
    }
    public void setTwo(boolean isTwo) {
        this.isTwo = isTwo;
    }
    public boolean isThird() {
        return isThird;
    }
    public void setThird(boolean isThird) {
        this.isThird = isThird;
    }
    public boolean isFour() {
        return isFour;
    }
    public void setFour(boolean isFour) {
        this.isFour = isFour;
    }
    public boolean isFive() {
        return isFive;
    }
    public void setFive(boolean isFive) {
        this.isFive = isFive;
    }
    public boolean isSix() {
        return isSix;
    }
    public void setSix(boolean isSix) {
        this.isSix = isSix;
    }
    public boolean isSeven() {
        return isSeven;
    }
    public void setSeven(boolean isSeven) {
        this.isSeven = isSeven;
    }
    public boolean isEight() {
        return isEight;
    }
    public void setEight(boolean isEight) {
        this.isEight = isEight;
    }
    public boolean isNine() {
        return isNine;
    }
    public void setNine(boolean isNine) {
        this.isNine = isNine;
    }
    public boolean isTen() {
        return isTen;
    }
    public void setTen(boolean isTen) {
        this.isTen = isTen;
    }
    public boolean isEleven() {
        return isEleven;
    }
    public void setEleven(boolean isEleven) {
        this.isEleven = isEleven;
    }
    public boolean isTwelve() {
        return isTwelve;
    }
    public void setTwelve(boolean isTwelve) {
        this.isTwelve = isTwelve;
    }
    public boolean isThirteen() {
        return isThirteen;
    }
    public void setThirteen(boolean isThirteen) {
        this.isThirteen = isThirteen;
    }
    public boolean isFourteen() {
        return isFourteen;
    }
    public void setFourteen(boolean isFourteen) {
        this.isFourteen = isFourteen;
    }
    public boolean isThirty() {
        return isThirty;
    }
    public void setThirty(boolean isThirty) {
        this.isThirty = isThirty;
    }
    public boolean isSixty() {
        return isSixty;
    }
    public void setSixty(boolean isSixty) {
        this.isSixty = isSixty;
    }
    public double getTotalMoney() {
        return totalMoney;
    }
    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }
    public int getRechargeNum() {
        return rechargeNum;
    }
    public void setRechargeNum(int rechargeNum) {
        this.rechargeNum = rechargeNum;
    }
    public int getFirstChannel() {
        return firstChannel;
    }
    public void setFirstChannel(int firstChannel) {
        this.firstChannel = firstChannel;
    }
    public int getFirstLevel() {
        return firstLevel;
    }
    public void setFirstLevel(int firstLevel) {
        this.firstLevel = firstLevel;
    }
    public double getFirstMoney() {
        return firstMoney;
    }
    public void setFirstMoney(double firstMoney) {
        this.firstMoney = firstMoney;
    }
    public Date getFirstRecharge() {
        return firstRecharge;
    }
    public void setFirstRecharge(Date firstRecharge) {
        this.firstRecharge = firstRecharge;
    }
    public Date getFirstCostTime() {
        return firstCostTime;
    }
    public void setFirstCostTime(Date firstCostTime) {
        this.firstCostTime = firstCostTime;
    }
    public int getFirstCostNum() {
        return firstCostNum;
    }
    public void setFirstCostNum(int firstCostNum) {
        this.firstCostNum = firstCostNum;
    }
    public int getFirstCostLevel() {
        return firstCostLevel;
    }
    public void setFirstCostLevel(int firstCostLevel) {
        this.firstCostLevel = firstCostLevel;
    }
    public int getFirstCostItem() {
        return firstCostItem;
    }
    public void setFirstCostItem(int firstCostItem) {
        this.firstCostItem = firstCostItem;
    }
    public double getWltv() {
        return wltv;
    }
    public void setWltv(double wltv) {
        this.wltv = wltv;
    }
    public double getMltv() {
        return mltv;
    }
    public void setMltv(double mltv) {
        this.mltv = mltv;
    }
    public int getDiamond() {
        return diamond;
    }
    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }
    public int getGold() {
        return gold;
    }
    public void setGold(int gold) {
        this.gold = gold;
    }
    public int getLoginNum() {
        return loginNum;
    }
    public void setLoginNum(int loginNum) {
        this.loginNum = loginNum;
    }
    public int getSevenNum() {
        return sevenNum;
    }
    public void setSevenNum(int sevenNum) {
        this.sevenNum = sevenNum;
    }
    public int getTotalOnline() {
        return totalOnline;
    }
    public void setTotalOnline(int totalOnline) {
        this.totalOnline = totalOnline;
    }
    public int getGuildId() {
        return guildId;
    }
    public void setGuildId(int guildId) {
        this.guildId = guildId;
    }
    public int getCouresId() {
        return couresId;
    }
    public void setCouresId(int couresId) {
        this.couresId = couresId;
    }
    public int getCouresStep() {
        return couresStep;
    }
    public void setCouresStep(int couresStep) {
        this.couresStep = couresStep;
    }
    public int getTiroTime() {
        return tiroTime;
    }
    public void setTiroTime(int tiroTime) {
        this.tiroTime = tiroTime;
    }
    public int getMateId() {
        return mateId;
    }
    public void setMateId(int mateId) {
        this.mateId = mateId;
    }
    public int getTopSinglemap() {
        return topSinglemap;
    }
    public void setTopSinglemap(int topSinglemap) {
        this.topSinglemap = topSinglemap;
    }
    public int getTopDareSinglemap() {
        return topDareSinglemap;
    }
    public void setTopDareSinglemap(int topDareSinglemap) {
        this.topDareSinglemap = topDareSinglemap;
    }
    public int getTopSinglemapTime() {
        return topSinglemapTime;
    }
    public void setTopSinglemapTime(int topSinglemapTime) {
        this.topSinglemapTime = topSinglemapTime;
    }
    public int getTopEliteSinglemap() {
        return topEliteSinglemap;
    }
    public void setTopEliteSinglemap(int topEliteSinglemap) {
        this.topEliteSinglemap = topEliteSinglemap;
    }
    public int getTopDareEliteSinglemap() {
        return topDareEliteSinglemap;
    }
    public void setTopDareEliteSinglemap(int topDareEliteSinglemap) {
        this.topDareEliteSinglemap = topDareEliteSinglemap;
    }
    public int getTopEliteSinglemapTime() {
        return topEliteSinglemapTime;
    }
    public void setTopEliteSinglemapTime(int topEliteSinglemapTime) {
        this.topEliteSinglemapTime = topEliteSinglemapTime;
    }
    public int getVigor() {
        return vigor;
    }
    public void setVigor(int vigor) {
        this.vigor = vigor;
    }
    public int getBattleWinMarry() {
        return battleWinMarry;
    }
    public void setBattleWinMarry(int battleWinMarry) {
        this.battleWinMarry = battleWinMarry;
    }
    public int getBattleWinGuild() {
        return battleWinGuild;
    }
    public void setBattleWinGuild(int battleWinGuild) {
        this.battleWinGuild = battleWinGuild;
    }
    public String getGagTime() {
        return gagTime;
    }
    public void setGagTime(String gagTime) {
        this.gagTime = gagTime;
    }
   
    public String getGagReason() {
        return gagReason;
    }
    public void setGagReason(String gagReason) {
        this.gagReason = gagReason;
    }
    public int getLoginDays() {
        return loginDays;
    }
    public void setLoginDays(int loginDays) {
        this.loginDays = loginDays;
    }
    public int getFriendCount() {
        return friendCount;
    }
    public void setFriendCount(int friendCount) {
        this.friendCount = friendCount;
    }
    public int getWeaponId() {
        return weaponId;
    }
    public void setWeaponId(int weaponId) {
        this.weaponId = weaponId;
    }
    public int getWeaponItemId() {
        return weaponItemId;
    }
    public void setWeaponItemId(int weaponItemId) {
        this.weaponItemId = weaponItemId;
    }
    public int getWeaponLevel() {
        return weaponLevel;
    }
    public void setWeaponLevel(int weaponLevel) {
        this.weaponLevel = weaponLevel;
    }
    public int getNecklaceId() {
        return necklaceId;
    }
    public void setNecklaceId(int necklaceId) {
        this.necklaceId = necklaceId;
    }
    public int getNecklaceItemId() {
        return necklaceItemId;
    }
    public void setNecklaceItemId(int necklaceItemId) {
        this.necklaceItemId = necklaceItemId;
    }
    public int getNecklaceLevel() {
        return necklaceLevel;
    }
    public void setNecklaceLevel(int necklaceLevel) {
        this.necklaceLevel = necklaceLevel;
    }
    public int getRingId() {
        return ringId;
    }
    public void setRingId(int ringId) {
        this.ringId = ringId;
    }
    public int getRingItemId() {
        return ringItemId;
    }
    public void setRingItemId(int ringItemId) {
        this.ringItemId = ringItemId;
    }
    public int getRingLevel() {
        return ringLevel;
    }
    public void setRingLevel(int ringLevel) {
        this.ringLevel = ringLevel;
    }
    public int getBraceletId() {
        return braceletId;
    }
    public void setBraceletId(int braceletId) {
        this.braceletId = braceletId;
    }
    public int getBraceletItemId() {
        return braceletItemId;
    }
    public void setBraceletItemId(int braceletItemId) {
        this.braceletItemId = braceletItemId;
    }
    public int getBraceletLevel() {
        return braceletLevel;
    }
    public void setBraceletLevel(int braceletLevel) {
        this.braceletLevel = braceletLevel;
    }
    public int getTalismanId() {
        return talismanId;
    }
    public void setTalismanId(int talismanId) {
        this.talismanId = talismanId;
    }
    public int getTalismanItemId() {
        return talismanItemId;
    }
    public void setTalismanItemId(int talismanItemId) {
        this.talismanItemId = talismanItemId;
    }
    public int getTalismanLevel() {
        return talismanLevel;
    }
    public void setTalismanLevel(int talismanLevel) {
        this.talismanLevel = talismanLevel;
    }
    public int getMedalId() {
        return medalId;
    }
    public void setMedalId(int medalId) {
        this.medalId = medalId;
    }
    public int getMedalItemId() {
        return medalItemId;
    }
    public void setMedalItemId(int medalItemId) {
        this.medalItemId = medalItemId;
    }
    public int getMedalLevel() {
        return medalLevel;
    }
    public void setMedalLevel(int medalLevel) {
        this.medalLevel = medalLevel;
    }
    public int getPropFuryLevel() {
        return propFuryLevel;
    }
    public void setPropFuryLevel(int propFuryLevel) {
        this.propFuryLevel = propFuryLevel;
    }
    
    public int getPropHidesingleLevel() {
        return propHidesingleLevel;
    }
    public void setPropHidesingleLevel(int propHidesingleLevel) {
        this.propHidesingleLevel = propHidesingleLevel;
    }
   
    public int getPropHidegroupLevel() {
        return propHidegroupLevel;
    }
    public void setPropHidegroupLevel(int propHidegroupLevel) {
        this.propHidegroupLevel = propHidegroupLevel;
    }
    public int getPropReflectLevel() {
        return propReflectLevel;
    }
    public void setPropReflectLevel(int propReflectLevel) {
        this.propReflectLevel = propReflectLevel;
    }
    
    
    public int getPropTreatsingleLevel() {
        return propTreatsingleLevel;
    }
    public void setPropTreatsingleLevel(int propTreatsingleLevel) {
        this.propTreatsingleLevel = propTreatsingleLevel;
    }
    public int getPropTreatgroupLevel() {
        return propTreatgroupLevel;
    }
    public void setPropTreatgroupLevel(int propTreatgroupLevel) {
        this.propTreatgroupLevel = propTreatgroupLevel;
    }
    public int getPropGuardianLevel() {
        return propGuardianLevel;
    }
    public void setPropGuardianLevel(int propGuardianLevel) {
        this.propGuardianLevel = propGuardianLevel;
    }
    public int getPropDiceLevel() {
        return propDiceLevel;
    }
    public void setPropDiceLevel(int propDiceLevel) {
        this.propDiceLevel = propDiceLevel;
    }
    public int getPetId() {
        return petId;
    }
    public void setPetId(int petId) {
        this.petId = petId;
    }
    public int getPetItemId() {
        return petItemId;
    }
    public void setPetItemId(int petItemId) {
        this.petItemId = petItemId;
    }
    public int getPetLevel() {
        return petLevel;
    }
    public void setPetLevel(int petLevel) {
        this.petLevel = petLevel;
    }
    public int getRankMatchNum() {
        return rankMatchNum;
    }
    public void setRankMatchNum(int rankMatchNum) {
        this.rankMatchNum = rankMatchNum;
    }
    public int getContactNum() {
        return contactNum;
    }
    public void setContactNum(int contactNum) {
        this.contactNum = contactNum;
    }
    public int getTopTowermap() {
        return topTowermap;
    }
    public void setTopTowermap(int topTowermap) {
        this.topTowermap = topTowermap;
    }
    public int getTowermapNum() {
        return towermapNum;
    }
    public void setTowermapNum(int towermapNum) {
        this.towermapNum = towermapNum;
    }
    public int getTeammapNum() {
        return teammapNum;
    }
    public void setTeammapNum(int teammapNum) {
        this.teammapNum = teammapNum;
    }    
    
}
