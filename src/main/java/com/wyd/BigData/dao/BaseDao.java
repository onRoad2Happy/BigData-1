package com.wyd.BigData.dao;
import com.wyd.BigData.JDBC.JDBCWrapper;
import com.wyd.BigData.bean.*;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
public class BaseDao implements Serializable {
    /**
     *
     */
    private static final long                      serialVersionUID = 125201040947818309L;
    private static       SimpleDateFormat          sf               = new SimpleDateFormat("yyyy_MM_dd");
    private static final String                    TAB_DIRECTORY    = "";
    private static       BaseDao                   instance         = null;
    private              JDBCWrapper               jdbcw            = null;
    private              Map<Integer, AccountInfo> accountInfoMap   = null;
    private              Map<String, DeviceInfo>   deviceInfoMap    = null;
    private              Map<Integer, PlayerInfo>  playerInfoMap    = null;
    private              Map<Integer, LoginInfo>   loginInfoMap     = null;

    private BaseDao() {
        loginInfoMap = new LinkedHashMap<Integer, LoginInfo>() {
            private static final long serialVersionUID = 1L;

            @Override protected boolean removeEldestEntry(java.util.Map.Entry<Integer, LoginInfo> pEldest) {
                return size() > 1000;
            }
        };
        accountInfoMap = new LinkedHashMap<Integer, AccountInfo>() {
            private static final long serialVersionUID = 1L;

            @Override protected boolean removeEldestEntry(java.util.Map.Entry<Integer, AccountInfo> pEldest) {
                return size() > 1000;
            }
        };
        playerInfoMap = new LinkedHashMap<Integer, PlayerInfo>() {
            private static final long serialVersionUID = 1L;

            @Override protected boolean removeEldestEntry(java.util.Map.Entry<Integer, PlayerInfo> pEldest) {
                return size() > 1000;
            }
        };
        deviceInfoMap = new LinkedHashMap<String, DeviceInfo>() {
            private static final long serialVersionUID = 1L;

            @Override protected boolean removeEldestEntry(java.util.Map.Entry<String, DeviceInfo> pEldest) {
                return size() > 1000;
            }
        };
        jdbcw = JDBCWrapper.getInstance();
    }

    public static BaseDao getInstance() {
        if (instance == null) {
            synchronized (JDBCWrapper.class) {
                if (instance == null) {
                    instance = new BaseDao();
                }
            }
        }
        return instance;
    }

    public AccountInfo getAccountInfo(int accountId) {
        if (accountInfoMap.containsKey(accountId))
            return accountInfoMap.get(accountId);
        AccountInfo accountInfo = new AccountInfo();
        jdbcw.doQuery("select `id` ,`account_id`,`service_id`,`channel_id`,`account_name`,`account_pwd`,`create_time`,`device_mac`,`system_version`,`system_type` from tab_account_info where account_id=? ORDER BY id DESC LIMIT 1", new Object[] { accountId }, rs -> {
            try {
                while (rs.next()) {
                    accountInfo.setId(rs.getInt(1));
                    accountInfo.setAccountId(rs.getInt(2));
                    accountInfo.setServiceId(rs.getInt(3));
                    accountInfo.setChannelId(rs.getInt(4));
                    accountInfo.setAccountName(rs.getString(5));
                    accountInfo.setAccountPwd(rs.getString(6));
                    accountInfo.setCreateTime(getRsDate(rs.getTimestamp(7)));
                    accountInfo.setDeviceMac(rs.getString(8));
                    accountInfo.setSystemVersion(rs.getString(9));
                    accountInfo.setSystemType(rs.getString(10));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        if (accountInfo.getId() != 0) {
            accountInfoMap.put(accountId, accountInfo);
            return accountInfo;
        }
        return null;
    }

    public PlayerLevelInfo getPlayerLevel(int serviceId, int channelId, int level) {
        PlayerLevelInfo playerLevelInfo = new PlayerLevelInfo();
        jdbcw.doQuery("select `id`,`service_id`,`channel_id`,`level`,`player_count` from tab_player_level_info where `service_id`=? and `channel_id`=? and `level`=?", new Object[] { serviceId, channelId, level }, rs -> {
            try {
                while (rs.next()) {
                    playerLevelInfo.setId(rs.getInt(1));
                    playerLevelInfo.setServiceId(rs.getInt(2));
                    playerLevelInfo.setChannelId(rs.getInt(3));
                    playerLevelInfo.setLevel(rs.getInt(4));
                    playerLevelInfo.setPlayerCount(rs.getInt(5));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        if (playerLevelInfo.getId() == 0) {
            return null;
        }
        return playerLevelInfo;
    }
    public void updateMarryInfoBath(List<MarryInfo> marryInfoList){
        List<Object[]> paramsList = new ArrayList<>();
        for(MarryInfo info:marryInfoList) {
            paramsList.add(new Object[] { info.getMarryNum(), info.getDivorceNum(), info.getLuxuriousNum(), info.getLuxuryNum(), info.getRomanticNum(), info.getGeneralNum(),info.getServiceId()});
        }
        jdbcw.doBatch("update tab_marry_info set `marry_num`=?,`divorce_num`=?,`luxurious_num`=?,`luxury_num`=?,`romantic_num`=?,`general_num`=? where service_id=?",paramsList);
    }
    public void saveMarryInfo(MarryInfo info){
        List<Object[]> paramsList = new ArrayList<>();
        paramsList.add(new Object[] {info.getServiceId(),info.getMarryNum(),info.getDivorceNum(),info.getLuxuriousNum(),info.getLuxuryNum(),info.getRomanticNum(),info.getGeneralNum() });
        jdbcw.doBatch("insert into tab_marry_info (`service_id`,`marry_num`,`divorce_num`,`luxurious_num`,`luxury_num`,`romantic_num`,`general_num`) values (?,?,?,?,?,?,?)",paramsList);
    }
    public MarryInfo getMarryInfo(int serviceId) {
        MarryInfo marryInfo = new MarryInfo();
        jdbcw.doQuery("select `id`,`service_id`,`marry_num`,`divorce_num`,`luxurious_num`,`luxury_num`,`romantic_num`,`general_num` from tab_marry_info where service_id=?", new Object[] { serviceId }, rs -> {
            try {
                while (rs.next()) {
                    marryInfo.setId(rs.getInt(1));
                    marryInfo.setServiceId(rs.getInt(2));
                    marryInfo.setMarryNum(rs.getInt(3));
                    marryInfo.setDivorceNum(rs.getInt(4));
                    marryInfo.setLuxuriousNum(rs.getInt(5));
                    marryInfo.setLuxuryNum(rs.getInt(6));
                    marryInfo.setRomanticNum(rs.getInt(7));
                    marryInfo.setGeneralNum(rs.getInt(8));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        if (marryInfo.getId() == 0) {
            return null;
        }
        return marryInfo;
    }
    public UpgradeInfo getUpgradeInfo(int serviceId, int playerLevel) {
        UpgradeInfo upgradeInfo = new UpgradeInfo();
        jdbcw.doQuery("select `id`,`service_id`,`player_level`,`total_time`,`total_count` from tab_upgrade_info where `service_id`=? and `player_level`=?", new Object[] { serviceId, playerLevel }, rs -> {
            try {
                while (rs.next()) {
                    upgradeInfo.setId(rs.getInt(1));
                    upgradeInfo.setServiceId(rs.getInt(2));
                    upgradeInfo.setPlayerLevel(rs.getInt(3));
                    upgradeInfo.setTotalTime(rs.getInt(4));
                    upgradeInfo.setTotalCount(rs.getInt(5));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        if (upgradeInfo.getId() == 0) {
            return null;
        }
        return upgradeInfo;
    }

    public GuildInfo getGuildInfo(int guildId) {
        GuildInfo guildInfo = new GuildInfo();
        jdbcw.doQuery("select `id`,`guild_id`,`service_id`,`guild_level`,`guild_num` from tab_guild_info where `guild_id`=?", new Object[] { guildId }, rs -> {
            try {
                while (rs.next()) {
                    guildInfo.setId(rs.getInt(1));
                    guildInfo.setGuildId(rs.getInt(2));
                    guildInfo.setServiceId(rs.getInt(3));
                    guildInfo.setGuildLevel(rs.getInt(4));
                    guildInfo.setGuildNum(rs.getInt(5));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        if (guildInfo.getId() == 0) {
            return null;
        }
        return guildInfo;
    }

    public PlayerInfo getPlayerInfo(int playerId) {
        return getPlayerInfo(playerId, true);
    }

    public PlayerInfo getPlayerInfo(int playerId, boolean useCache) {
        if (playerInfoMap.containsKey(playerId) && useCache)
            return playerInfoMap.get(playerId);
        PlayerInfo info = new PlayerInfo();
        jdbcw.doQuery(
                "select `id`,`player_id`,`service_id`,`channel_id`,`account_id`,`device_mac`,`create_time`,`player_name`,`player_sex`,`player_level`,`upgrade_time`,`player_fighting`,`vip_level`,`sports_level`,`ranking_level`,`login_time`,`is_two`,`is_third`,`is_four`,`is_five`,`is_six`,`is_seven`,`is_fourteen`,`is_thirty`,`is_sixty`,`total_money`,`recharge_num`,`first_channel`,`first_money`,`first_level`,`first_recharge`,`first_cost_time`,`first_cost_level`,`first_cost_num`,`first_cost_item`,`wltv`,`mltv`,`diamond`,`gold`,`login_num`,`seven_num`,`total_online`,`guild_id`,`coures_id`,`coures_step`,`tiro_time`,`mate_id`,`top_singlemap`,`top_dare_singlemap`,`top_singlemap_time`,`top_elite_singlemap`,`top_dare_elite_singlemap`,`top_elite_singlemap_time`,`vigor`,`battle_win_marry`,`battle_win_guild`,`gag_time`,`gag_reason`,`is_eight`,`is_nine`,`is_ten`,`is_eleven`,`is_twelve`,`is_thirteen`,`login_days`,`friend_count`,`weapon_id`,`weapon_item_id`,`weapon_level`,`necklace_id`,`necklace_item_id`,`necklace_level`,`ring_id`,`ring_item_id`,`ring_level`,`bracelet_id`,`bracelet_item_id`,`bracelet_level`,`talisman_id`,`talisman_item_id`,`talisman_level`,`medal_id`,`medal_item_id`,`medal_level`,`prop_fury_level`,`prop_hidesingle_level`,`prop_hidegroup_level`,`prop_reflect_level`,`prop_treatsingle_level`,`prop_treatgroup_level`,`prop_guardian_level`,`prop_dice_level`,`pet_id`,`pet_item_id`,`pet_level`,`rank_match_num`,`contact_num`,`top_towermap`,`towermap_num`,`teammap_num` from tab_player_info where player_id=? ORDER BY id DESC LIMIT 1",
                new Object[] { playerId }, rs -> {
                    try {
                        while (rs.next()) {
                            info.setId(rs.getInt(1));
                            info.setPlayerId(rs.getInt(2));
                            info.setServiceId(rs.getInt(3));
                            info.setChannelId(rs.getInt(4));
                            info.setAccountId(rs.getInt(5));
                            info.setDeviceMac(rs.getString(6));
                            info.setCreateTime(getRsDate(rs.getTimestamp(7)));
                            info.setPlayerName(rs.getString(8));
                            info.setPlayerSex(rs.getString(9));
                            info.setPlayerLevel(rs.getInt(10));
                            info.setUpgradeTime(rs.getInt(11));
                            info.setPlayerFighting(rs.getInt(12));
                            info.setVipLevel(rs.getInt(13));
                            info.setSportsLevel(rs.getInt(14));
                            info.setRankingLevel(rs.getInt(15));
                            info.setLoginTime(getRsDate(rs.getTimestamp(16)));
                            info.setTwo(rs.getBoolean(17));
                            info.setThird(rs.getBoolean(18));
                            info.setFour(rs.getBoolean(19));
                            info.setFive(rs.getBoolean(20));
                            info.setSix(rs.getBoolean(21));
                            info.setSeven(rs.getBoolean(22));
                            info.setFourteen(rs.getBoolean(23));
                            info.setThirty(rs.getBoolean(24));
                            info.setSixty(rs.getBoolean(25));
                            info.setTotalMoney(rs.getDouble(26));
                            info.setRechargeNum(rs.getInt(27));
                            info.setFirstChannel(rs.getInt(28));
                            info.setFirstMoney(rs.getDouble(29));
                            info.setFirstLevel(rs.getInt(30));
                            info.setFirstRecharge(getRsDate(rs.getTimestamp(31)));
                            info.setFirstCostTime(getRsDate(rs.getTimestamp(32)));
                            info.setFirstCostLevel(rs.getInt(33));
                            info.setFirstCostNum(rs.getInt(34));
                            info.setFirstCostItem(rs.getInt(35));
                            info.setWltv(rs.getDouble(36));
                            info.setMltv(rs.getDouble(37));
                            info.setDiamond(rs.getInt(38));
                            info.setGold(rs.getInt(39));
                            info.setLoginNum(rs.getInt(40));
                            info.setSevenNum(rs.getInt(41));
                            info.setTotalOnline(rs.getInt(42));
                            info.setGuildId(rs.getInt(43));
                            info.setCouresId(rs.getInt(44));
                            info.setCouresStep(rs.getInt(45));
                            info.setTiroTime(rs.getInt(46));
                            info.setMateId(rs.getInt(47));
                            info.setTopSinglemap(rs.getInt(48));
                            info.setTopDareSinglemap(rs.getInt(49));
                            info.setTopSinglemapTime(rs.getInt(50));
                            info.setTopEliteSinglemap(rs.getInt(51));
                            info.setTopDareEliteSinglemap(rs.getInt(52));
                            info.setTopEliteSinglemapTime(rs.getInt(53));
                            info.setVigor(rs.getInt(54));
                            info.setBattleWinMarry(rs.getInt(55));
                            info.setBattleWinGuild(rs.getInt(56));
                            info.setGagTime(rs.getString(57));
                            info.setGagReason(rs.getString(58));
                            info.setEight(rs.getBoolean(59));
                            info.setNine(rs.getBoolean(60));
                            info.setTen(rs.getBoolean(61));
                            info.setEleven(rs.getBoolean(62));
                            info.setTwelve(rs.getBoolean(63));
                            info.setThirteen(rs.getBoolean(64));
                            info.setLoginDays(rs.getInt(65));
                            info.setFriendCount(rs.getInt(66));
                            info.setWeaponId(rs.getInt(67));
                            info.setWeaponItemId(rs.getInt(68));
                            info.setWeaponLevel(rs.getInt(69));
                            info.setNecklaceId(rs.getInt(70));
                            info.setNecklaceItemId(rs.getInt(71));
                            info.setNecklaceLevel(rs.getInt(72));
                            info.setRingId(rs.getInt(73));
                            info.setRingItemId(rs.getInt(74));
                            info.setRingLevel(rs.getInt(75));
                            info.setBraceletId(rs.getInt(76));
                            info.setBraceletItemId(rs.getInt(77));
                            info.setBraceletLevel(rs.getInt(78));
                            info.setTalismanId(rs.getInt(79));
                            info.setTalismanItemId(rs.getInt(80));
                            info.setTalismanLevel(rs.getInt(81));
                            info.setMedalId(rs.getInt(82));
                            info.setMedalItemId(rs.getInt(83));
                            info.setMedalLevel(rs.getInt(84));
                            info.setPropFuryLevel(rs.getInt(85));
                            info.setPropHidesingleLevel(rs.getInt(86));
                            info.setPropHidegroupLevel(rs.getInt(87));
                            info.setPropReflectLevel(rs.getInt(88));
                            info.setPropTreatsingleLevel(rs.getInt(89));
                            info.setPropTreatgroupLevel(rs.getInt(90));
                            info.setPropGuardianLevel(rs.getInt(91));
                            info.setPropDiceLevel(rs.getInt(92));
                            info.setPetId(rs.getInt(93));
                            info.setPetItemId(rs.getInt(94));
                            info.setPetLevel(rs.getInt(95));
                            info.setRankMatchNum(rs.getInt(96));
                            info.setContactNum(rs.getInt(97));
                            info.setTopTowermap(rs.getInt(98));
                            info.setTowermapNum(rs.getInt(99));
                            info.setTeammapNum(rs.getInt(100));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
        if (info.getId() != 0) {
            playerInfoMap.put(playerId, info);
            return info;
        }
        return null;
    }

    public DeviceInfo getDeviceInfo(String mac) {
        if (deviceInfoMap.containsKey(mac))
            return deviceInfoMap.get(mac);
        DeviceInfo deviceInfo = new DeviceInfo();
        jdbcw.doQuery("select `id` ,`service_id` ,`channel_id`,`device_mac`,`create_time`, `device_name`,`system_name`,`system_version`,`app_version`  from tab_device_info where device_mac=? ORDER BY id DESC LIMIT 1", new Object[] { mac }, rs -> {
            try {
                while (rs.next()) {
                    deviceInfo.setId(rs.getInt(1));
                    deviceInfo.setServiceId(rs.getInt(2));
                    deviceInfo.setChannelId(rs.getInt(3));
                    deviceInfo.setDeviceMac(rs.getString(4));
                    deviceInfo.setCreateTime(getRsDate(rs.getTimestamp(5)));
                    deviceInfo.setDeviceName(rs.getString(6));
                    deviceInfo.setSystemName(rs.getString(7));
                    deviceInfo.setSystemVersion(rs.getString(8));
                    deviceInfo.setAppVersion(rs.getString(9));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        if (deviceInfo.getId() != 0) {
            deviceInfoMap.put(mac, deviceInfo);
            return deviceInfo;
        }
        return null;
    }

    public void saveDeviceInfoBatch(List<DeviceInfo> accountList) {
        List<Object[]> paramsList = new ArrayList<>();
        for (DeviceInfo info : accountList) {
            paramsList.add(new Object[] { info.getServiceId(), info.getChannelId(), info.getDeviceMac(), info.getCreateTime(), info.getDeviceName(), info.getSystemName(), info.getSystemVersion(), info.getAppVersion() });
        }
        String tableName = "tab_device_info";
        jdbcw.doBatch("insert into " + tableName + " (`service_id` ,`channel_id`,`device_mac`,`create_time`, `device_name`,`system_name`,`system_version`,`app_version`) values (?,?,?,?,?,?,?,?)", paramsList);
    }

    public List<LoginSumInfo> getAllLoginSumInfo(String today) {
        List<LoginSumInfo> infoList = new ArrayList<>();
        createLoginSumInfo(today);
        jdbcw.doQuery("select id,service_id,channel_id,player_channel,player_id from  " + today + "_tab_login_sum_info", null, rs -> {
            try {
                while (rs.next()) {
                    LoginSumInfo info = new LoginSumInfo();
                    info.setId(rs.getInt(1));
                    info.setServiceId(rs.getInt(2));
                    info.setChannelId(rs.getInt(3));
                    info.setPlayerChannel(rs.getInt(4));
                    info.setPlayerId(rs.getInt(5));
                    infoList.add(info);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return infoList;
    }

    /**
     * 保存每天登陆玩家
     * @param today today
     * @param loginList loginList
     */
    public void saveLoginSumInfoBatch(String today, List<LoginSumInfo> loginList) {
        List<Object[]> paramsList = new ArrayList<>();
        for (LoginSumInfo info : loginList) {
            paramsList.add(new Object[] { info.getServiceId(), info.getChannelId(), info.getPlayerChannel(), info.getPlayerId() });
        }
        jdbcw.doBatch("INSERT INTO " + today + "_tab_login_sum_info(service_id,channel_id,player_channel,player_id) VALUES(?,?,?,?)", paramsList);
    }

    public void saveAccountNewCountBatch(List<AccountNewCount> accountList) {
        Map<String, List<AccountNewCount>> accountMap = new HashMap<>();
        for (AccountNewCount account : accountList) {
            String today = sf.format(account.getCreateTime());
            accountMap.computeIfAbsent(today, x -> new ArrayList<>()).add(account);
        }
        for (String today : accountMap.keySet()) {
            saveAccountNewCountBatch(today, accountMap.get(today));
        }
    }

    private void saveAccountNewCountBatch(String today, List<AccountNewCount> accountList) {
        createAccountNewCount(today);
        List<Object[]> paramsList = new ArrayList<>();
        for (AccountNewCount info : accountList) {
            paramsList.add(new Object[] { info.getServiceId(), info.getChannelId(), info.getAccountId(), info.getCreateTime() });
        }
        String tableName = today + "_tab_account_new_count";
        jdbcw.doBatch("insert into " + tableName + " (service_id,channel_id,account_id,create_time) values (?,?,?,?)", paramsList);
    }

    public void savePlayerInfoBatch(List<PlayerInfo> playerInfoList) {
        List<Object[]> paramsList = new ArrayList<>();
        for (PlayerInfo info : playerInfoList) {
            paramsList
                    .add(new Object[] { info.getPlayerId(), info.getServiceId(), info.getChannelId(), info.getAccountId(), info.getDeviceMac(), info.getCreateTime(), info.getPlayerName(), info.getPlayerSex(), info.getPlayerLevel(), info.getUpgradeTime(), info.getPlayerFighting(), info.getVipLevel(), info.getSportsLevel(), info.getRankingLevel(), info.getLoginTime(), info.isTwo(), info.isThird(), info.isFour(), info.isFive(), info.isSix(), info.isSeven(), info.isFourteen(), info.isThirty(), info.isSixty(), info.getTotalMoney(), info.getRechargeNum(), info.getFirstChannel(), info.getFirstMoney(), info.getFirstLevel(), info.getFirstRecharge(), info.getFirstCostTime(), info.getFirstCostLevel(), info.getFirstCostNum(), info.getFirstCostItem(), info.getWltv(), info.getMltv(), info.getDiamond(),
                            info.getGold(), info.getLoginNum(), info.getSevenNum(), info.getTotalOnline(), info.getGuildId(), info.getCouresId(), info.getCouresStep(), info.getTiroTime(), info.getMateId(), info.getTopSinglemap(), info.getTopDareSinglemap(), info.getTopSinglemapTime(), info.getTopEliteSinglemap(), info.getTopDareEliteSinglemap(), info.getTopEliteSinglemapTime(), info.getVigor(), info.getBattleWinMarry(), info.getBattleWinGuild(), info.getGagTime(), info.getGagReason(), info.isEight(), info.isNine(), info.isTen(), info.isEleven(), info.isTwelve(), info.isThirteen(), info.getLoginDays(), info.getFriendCount(), info.getWeaponId(), info.getWeaponItemId(), info.getWeaponLevel(), info.getNecklaceId(), info.getNecklaceItemId(), info.getNecklaceLevel(), info.getRingId(),
                            info.getRingItemId(), info.getRingLevel(), info.getBraceletId(), info.getBraceletItemId(), info.getBraceletLevel(), info.getTalismanId(), info.getTalismanItemId(), info.getTalismanLevel(), info.getMedalId(), info.getMedalItemId(), info.getMedalLevel(), info.getPropFuryLevel(), info.getPropHidesingleLevel(), info.getPropHidegroupLevel(), info.getPropReflectLevel(), info.getPropTreatsingleLevel(), info.getPropTreatgroupLevel(), info.getPropGuardianLevel(), info.getPropDiceLevel(), info.getPetId(), info.getPetItemId(), info.getPetLevel(), info.getRankMatchNum(), info.getContactNum(), info.getTopTowermap(), info.getTowermapNum(), info.getTeammapNum() });
        }
        jdbcw.doBatch(
                "insert into tab_player_info (`player_id`,`service_id`,`channel_id`,`account_id`,`device_mac`,`create_time`,`player_name`,`player_sex`,`player_level`,`upgrade_time`,`player_fighting`,`vip_level`,`sports_level`,`ranking_level`,`login_time`,`is_two`,`is_third`,`is_four`,`is_five`,`is_six`,`is_seven`,`is_fourteen`,`is_thirty`,`is_sixty`,`total_money`,`recharge_num`,`first_channel`,`first_money`,`first_level`,`first_recharge`,`first_cost_time`,`first_cost_level`,`first_cost_num`,`first_cost_item`,`wltv`,`mltv`,`diamond`,`gold`,`login_num`,`seven_num`,`total_online`,`guild_id`,`coures_id`,`coures_step`,`tiro_time`,`mate_id`,`top_singlemap`,`top_dare_singlemap`,`top_singlemap_time`,`top_elite_singlemap`,`top_dare_elite_singlemap`,`top_elite_singlemap_time`,`vigor`,`battle_win_marry`,`battle_win_guild`,`gag_time`,`gag_reason`,`is_eight`,`is_nine`,`is_ten`,`is_eleven`,`is_twelve`,`is_thirteen`,`login_days`,`friend_count`,`weapon_id`,`weapon_item_id`,`weapon_level`,`necklace_id`,`necklace_item_id`,`necklace_level`,`ring_id`,`ring_item_id`,`ring_level`,`bracelet_id`,`bracelet_item_id`,`bracelet_level`,`talisman_id`,`talisman_item_id`,`talisman_level`,`medal_id`,`medal_item_id`,`medal_level`,`prop_fury_level`,`prop_hidesingle_level`,`prop_hidegroup_level`,`prop_reflect_level`,`prop_treatsingle_level`,`prop_treatgroup_level`,`prop_guardian_level`,`prop_dice_level`,`pet_id`,`pet_item_id`,`pet_level`,`rank_match_num`,`contact_num`,`top_towermap`,`towermap_num`,`teammap_num`) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                paramsList);
    }

    public void savePlayerNewCountBatch(List<PlayerNewCount> accountList) {
        Map<String, List<PlayerNewCount>> accountMap = new HashMap<>();
        for (PlayerNewCount account : accountList) {
            String today = sf.format(account.getCreateTime());
            accountMap.computeIfAbsent(today, x -> new ArrayList<>()).add(account);
        }
        for (String today : accountMap.keySet()) {
            savePlayerNewCountBatch(today, accountMap.get(today));
        }
    }

    private void savePlayerNewCountBatch(String today, List<PlayerNewCount> accountList) {
        createPlayerNewCount(today);
        List<Object[]> paramsList = new ArrayList<>();
        for (PlayerNewCount info : accountList) {
            paramsList.add(new Object[] { info.getServiceId(), info.getChannelId(), info.getPlayerId(), info.getCreateTime() });
        }
        String tableName = today + "_tab_player_new_count";
        jdbcw.doBatch("insert into " + tableName + " (service_id,channel_id,player_id,create_time) values (?,?,?,?)", paramsList);
    }

    public void saveRechargeInfoBatch(List<RechargeInfo> rechargeInfoList) {
        Map<String, List<RechargeInfo>> rechargeMap = new HashMap<>();
        for (RechargeInfo recharge : rechargeInfoList) {
            String today = sf.format(recharge.getRechargeTime());
            rechargeMap.computeIfAbsent(today, x -> new ArrayList<>()).add(recharge);
        }
        for (String today : rechargeMap.keySet()) {
            saveRechargeInfoBatch(today, rechargeMap.get(today));
        }
    }

    private void saveRechargeInfoBatch(String today, List<RechargeInfo> accountList) {
        createRechargeInfo(today);
        List<Object[]> paramsList = new ArrayList<>();
        for (RechargeInfo info : accountList) {
            paramsList.add(new Object[] { info.getServiceId(), info.getPayChannel(), info.getPlayerChannel(), info.getPlayerId(), info.getProductId(), info.getRechargeTime(), info.getMoney(), info.getOrderNum(), info.getCount(), info.getCountAll() });
        }
        String tableName = today + "_tab_recharge_info";
        jdbcw.doBatch("insert into " + tableName + " (`service_id`,`pay_channel`,`player_channel`,`player_id`,`product_id`,`recharge_time`,`money`,`order_num`,`count`,`count_all`) values (?,?,?,?,?,?,?,?,?,?)", paramsList);
    }

    private Date getRsDate(java.sql.Timestamp timestamp) {
        return timestamp == null ? null : new Date(timestamp.getTime());
    }

    public LoginInfo getLoginInfo(String today, int playerId) {
        if (loginInfoMap.containsKey(playerId))
            return loginInfoMap.get(playerId);
        LoginInfo info = new LoginInfo();
        String sql = "select `id`,`service_id`,`channel_id`,`account_id`,`player_id`,`device_mac`,`device_name`,`system_name`,`system_version`,`app_version`,`login_time`,`logout_time`,`online_time`,`login_ip`,`diamond`,`gold`,`vigor`,`player_level`,`account_name`,`player_name` from " + today + "_tab_login_info where player_id=? ORDER BY id DESC LIMIT 1";
        jdbcw.doQuery(sql, new Object[] { playerId }, rs -> {
            try {
                while (rs.next()) {
                    info.setId(rs.getInt(1));
                    info.setServiceId(rs.getInt(2));
                    info.setChannelId(rs.getInt(3));
                    info.setAccountId(rs.getInt(4));
                    info.setPlayerId(rs.getInt(5));
                    info.setDeviceMac(rs.getString(6));
                    info.setDeviceName(rs.getString(7));
                    info.setSystemName(rs.getString(8));
                    info.setSystemVersion(rs.getString(9));
                    info.setAppVersion(rs.getString(10));
                    info.setLoginTime(getRsDate(rs.getTimestamp(11)));
                    info.setLogoutTime(getRsDate(rs.getTimestamp(12)));
                    info.setOnlineTime(rs.getInt(13));
                    info.setLoginIp(rs.getString(14));
                    info.setDiamond(rs.getInt(15));
                    info.setGold(rs.getInt(16));
                    info.setVigor(rs.getInt(17));
                    info.setPlayerLevel(rs.getInt(18));
                    info.setAccountName(rs.getString(19));
                    info.setPlayerName(rs.getString(20));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        if (info.getId() != 0) {
            loginInfoMap.put(info.getPlayerId(), info);
            return info;
        }
        return null;
    }

    public void saveLoginInfoBatch(List<LoginInfo> accountList) {
        Map<String, List<LoginInfo>> accountMap = new HashMap<>();
        for (LoginInfo account : accountList) {
            String today = sf.format(account.getLoginTime());
            accountMap.computeIfAbsent(today, x -> new ArrayList<>()).add(account);
        }
        for (String today : accountMap.keySet()) {
            saveLoginInfoBatch(today, accountMap.get(today));
        }
    }

    private void saveLoginInfoBatch(String today, List<LoginInfo> loginList) {
        createLoginInfo(today);
        List<Object[]> paramsList = new ArrayList<>();
        for (LoginInfo info : loginList) {
            paramsList.add(new Object[] { info.getServiceId(), info.getChannelId(), info.getPlayerChannel(), info.getAccountId(), info.getPlayerId(), info.getDeviceMac(), info.getDeviceName(), info.getSystemName(), info.getSystemVersion(), info.getAppVersion(), info.getLoginTime(), info.getLogoutTime(), info.getOnlineTime(), info.getLoginIp(), info.getDiamond(), info.getGold(), info.getVigor(), info.getPlayerLevel(), info.getAccountName(), info.getPlayerName() });
        }
        String tableName = today + "_tab_login_info";
        jdbcw.doBatch("insert into " + tableName + " (`service_id`,`channel_id`,`player_channel`,`account_id`,`player_id`,`device_mac`,`device_name`,`system_name`,`system_version`,`app_version`,`login_time`,`logout_time`,`online_time`,`login_ip`,`diamond`,`gold`,`vigor`,`player_level`,`account_name`,`player_name`) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", paramsList);
    }

    public void saveDeviceNewCountBatch(List<DeviceNewCount> accountList) {
        Map<String, List<DeviceNewCount>> accountMap = new HashMap<>();
        for (DeviceNewCount account : accountList) {
            String today = sf.format(account.getCreateTime());
            accountMap.computeIfAbsent(today, x -> new ArrayList<>()).add(account);
        }
        for (String today : accountMap.keySet()) {
            saveDeviceNewCountBatch(today, accountMap.get(today));
        }
    }

    private void saveDeviceNewCountBatch(String today, List<DeviceNewCount> accountList) {
        createDeviceNewCount(today);
        List<Object[]> paramsList = new ArrayList<>();
        for (DeviceNewCount info : accountList) {
            paramsList.add(new Object[] { info.getServiceId(), info.getChannelId(), info.getDeviceMac(), info.getCreateTime() });
        }
        String tableName = today + "_tab_device_new_count";
        jdbcw.doBatch("insert into " + tableName + " (service_id,channel_id,device_mac,create_time) values (?,?,?,?)", paramsList);
    }

    public void saveOnlineInfoBatch(List<OnlineInfo> accountList) {
        Map<String, List<OnlineInfo>> accountMap = new HashMap<>();
        for (OnlineInfo account : accountList) {
            String today = sf.format(new Date(account.getDateMinute() * 60000L));
            accountMap.computeIfAbsent(today, x -> new ArrayList<>()).add(account);
        }
        for (String today : accountMap.keySet()) {
            saveOnlineInfoBatch(today, accountMap.get(today));
        }
    }

    private void saveOnlineInfoBatch(String today, List<OnlineInfo> onlineInfoList) {
        createOnlineInfo(today);
        List<Object[]> paramsList = new ArrayList<>();
        for (OnlineInfo info : onlineInfoList) {
            paramsList.add(new Object[] { info.getServiceId(), info.getChannelId(), info.getDateMinute(), info.getOnlineNum() });
        }
        String tableName = today + "_tab_online_info";
        jdbcw.doBatch("insert into " + tableName + " (service_id,channel_id,date_minute,online_num) values (?,?,?,?)", paramsList);
    }

    public void saveAccountInfoBatch(List<AccountInfo> accountList) {
        List<Object[]> paramsList = new ArrayList<>();
        for (AccountInfo info : accountList) {
            paramsList.add(new Object[] { info.getAccountId(), info.getServiceId(), info.getChannelId(), info.getAccountName(), info.getAccountPwd(), info.getCreateTime(), info.getDeviceMac(), info.getSystemVersion(), info.getSystemType() });
        }
        String tableName = "tab_account_info";
        jdbcw.doBatch("insert into " + tableName + " (`account_id`,`service_id`,`channel_id`,`account_name`,`account_pwd`,`create_time`,`device_mac`,`system_version`,`system_type`) values (?,?,?,?,?,?,?,?,?)", paramsList);
    }

    public void saveUpgradeInfo(UpgradeInfo info) {
        List<Object[]> paramsList = new ArrayList<>();
        paramsList.add(new Object[] { info.getServiceId(), info.getPlayerLevel(), info.getTotalTime(), info.getTotalCount() });
        jdbcw.doBatch("insert into tab_upgrade_info (`service_id`,`player_level`,`total_time`,`total_count`) values (?,?,?,?)", paramsList);
    }

    public void savePlayerLevelInfo(PlayerLevelInfo info) {
        List<Object[]> paramsList = new ArrayList<>();
        paramsList.add(new Object[] { info.getServiceId(), info.getChannelId(), info.getLevel(), info.getPlayerCount() });
        jdbcw.doBatch("insert into tab_player_level_info (`service_id`,`channel_id`,`level`,`player_count`) values (?,?,?,?)", paramsList);
    }

    /**
     *
     * @param loginInfoList  登陆实体 *
     */
    public void updateLoginInfoBatch(List<LoginInfo> loginInfoList) {
        Map<String, List<Object[]>> dayParamMap = new HashMap<>();
        for (LoginInfo info : loginInfoList) {
            String today = sf.format(info.getLoginTime());
            List<Object[]> paramsList = dayParamMap.computeIfAbsent(today, x -> new ArrayList<>());
            paramsList.add(new Object[] { info.getLogoutTime(), info.getOnlineTime(), info.getPlayerLevel(), info.getPlayerId() });
            loginInfoMap.put(info.getPlayerId(), info);
        }
        for (String today : dayParamMap.keySet()) {
            jdbcw.doBatch("update " + today + "_tab_login_info set logout_time=?,online_time=?,player_level=?  where player_id=? and logout_time is null", dayParamMap.get(today));
        }
    }

    public void updateGuildInfoBatch(List<GuildInfo> guildInfoList) {
        List<Object[]> paramsList = new ArrayList<>();
        for (GuildInfo info : guildInfoList) {
            paramsList.add(new Object[] { info.getGuildLevel(), info.getGuildNum(), info.getId() });
        }
        jdbcw.doBatch("update tab_guild_info set guild_level=?,guild_num=? where id=?", paramsList);
    }

    public void saveGuildInfo(GuildInfo info) {
        List<Object[]> paramsList = new ArrayList<>();
        paramsList.add(new Object[] { info.getGuildId(), info.getServiceId(), info.getGuildLevel(), info.getGuildNum() });
        jdbcw.doBatch("insert into tab_guild_info (`guild_id`,`service_id`,`guild_level`,`guild_num`) values (?,?,?,?)", paramsList);
    }

    public void updateVipLevelBatch(List<PlayerInfo> playerInfoList) {
        if(playerInfoList.size()==0)return;
        List<Object[]> paramsList = new ArrayList<>();
        for (PlayerInfo info : playerInfoList) {
            paramsList.add(new Object[] { info.getVipLevel(), info.getPlayerId() });
            playerInfoMap.put(info.getPlayerId(), info);
        }
        jdbcw.doBatch("update tab_player_info set vip_level=? where player_id=?", paramsList);
    }

    public void updatePlayerUpgradeBatch(List<PlayerInfo> playerInfoList) {
        if(playerInfoList.size()==0)return;
        List<Object[]> paramsList = new ArrayList<>();
        for (PlayerInfo info : playerInfoList) {
            paramsList.add(new Object[] { info.getPlayerLevel(), info.getUpgradeTime(), info.getPlayerId() });
            playerInfoMap.put(info.getPlayerId(), info);
        }
        jdbcw.doBatch("update tab_player_info set player_level=?,upgrade_time=? where player_id=?", paramsList);
    }

    public void updatePlayerLoginInfoBatch(List<PlayerInfo> playerInfoList) {
        if(playerInfoList.size()==0)return;
        List<Object[]> paramsList = new ArrayList<>();
        for (PlayerInfo info : playerInfoList) {
            paramsList.add(new Object[] { info.getLoginTime(), info.getLoginNum(), info.getDiamond(), info.getGold(), info.getVigor(), info.getPlayerId() });
            playerInfoMap.put(info.getPlayerId(), info);
        }
        jdbcw.doBatch("update tab_player_info set login_time=?,login_num=?,diamond=?,gold=?,vigor=? where player_id=?", paramsList);
    }

    public void updateTotalOnlineBatch(List<PlayerInfo> playerInfoList) {
        if(playerInfoList.size()==0)return;
        List<Object[]> paramsList = new ArrayList<>();
        for (PlayerInfo info : playerInfoList) {
            paramsList.add(new Object[] { info.getTotalOnline(), info.getVigor(), info.getPlayerId() });
            playerInfoMap.put(info.getPlayerId(), info);
        }
        jdbcw.doBatch("update tab_player_info set total_online=?,vigor=? where player_id=?", paramsList);
    }

    public void updatePlayerGuildInfoBatch(List<PlayerInfo> playerInfoList) {
        if(playerInfoList.size()==0)return;
        List<Object[]> paramsList = new ArrayList<>();
        for (PlayerInfo info : playerInfoList) {
            paramsList.add(new Object[] { info.getGuildId(), info.getPlayerId() });
            playerInfoMap.put(info.getPlayerId(), info);
        }
        jdbcw.doBatch("update tab_player_info set guild_id=? where player_id=?", paramsList);
    }
    public void updatePlayerMarryInfoBatch(List<PlayerInfo> playerInfoList) {
        if(playerInfoList.size()==0)return;
        List<Object[]> paramsList = new ArrayList<>();
        for (PlayerInfo info : playerInfoList) {
            paramsList.add(new Object[] { info.getMateId(), info.getPlayerId() });
            playerInfoMap.put(info.getPlayerId(), info);
        }
        jdbcw.doBatch("update tab_player_info set mate_id=? where player_id=?", paramsList);
    }

    public void updateRechargeBatch(List<PlayerInfo> playerInfoList) {
        if(playerInfoList.size()==0)return;
        List<Object[]> paramsList = new ArrayList<>();
        for (PlayerInfo info : playerInfoList) {
            paramsList.add(new Object[] { info.getFirstChannel(), info.getFirstMoney(), info.getFirstRecharge(), info.getFirstLevel(), info.getTotalMoney(), info.getRechargeNum(), info.getWltv(), info.getMltv(), info.getPlayerId() });
            playerInfoMap.put(info.getPlayerId(), info);
        }
        jdbcw.doBatch("update tab_player_info set first_channel=?,first_money=?,first_recharge=?,first_level=?,total_money=?,recharge_num=?,wltv=?,mltv=? where player_id=?", paramsList);
    }

    public void updateUpgradeInfoBatch(List<UpgradeInfo> upgradeInfoList) {
        if(upgradeInfoList.size()==0)return;
        List<Object[]> paramsList = new ArrayList<>();
        for (UpgradeInfo info : upgradeInfoList) {
            paramsList.add(new Object[] { info.getTotalTime(), info.getTotalCount(), info.getId() });
        }
        jdbcw.doBatch("update tab_upgrade_info set total_time=?,total_count=? where id=?", paramsList);
    }

    public void updatePlayerLevelInfo(PlayerLevelInfo info) {
        List<Object[]> paramsList = new ArrayList<>();
        paramsList.add(new Object[] { info.getPlayerCount(), info.getId() });
        jdbcw.doBatch("update tab_player_level_info set `player_count`=? where id=?", paramsList);
    }

    private void createLoginInfo(String today) {
        String createSql = "CREATE TABLE IF NOT EXISTS `" + today + "_tab_login_info`(" + "`id` bigint(20) NOT NULL AUTO_INCREMENT,"//
                + "`service_id` int(11) DEFAULT NULL,"//
                + "`channel_id` int(11) DEFAULT NULL,"//
                + "`player_channel` int(11) DEFAULT NULL,"//
                + "`account_id` int(11) DEFAULT NULL,"//
                + "`player_id` int(11) DEFAULT NULL,"//
                + "`device_mac` varchar(255) DEFAULT NULL,"//
                + "`device_name` varchar(255) DEFAULT NULL,"//
                + "`system_name` varchar(255) DEFAULT NULL,"//
                + "`system_version` varchar(255) DEFAULT NULL,"//
                + "`app_version` varchar(255) DEFAULT NULL,"//
                + "`login_time` datetime DEFAULT NULL,"//
                + "`logout_time` datetime DEFAULT NULL,"//
                + "`online_time` int(11) DEFAULT NULL,"//
                + "`login_ip` varchar(32) DEFAULT NULL,"//
                + "`diamond` int(11) DEFAULT '0',"//
                + "`gold` int(11) DEFAULT '0',"//
                + "`vigor` int(11) DEFAULT '0',"//
                + "`player_level` int(11) DEFAULT '0',"//
                + "`account_name` varchar(255) DEFAULT NULL,"//
                + "`player_name` varchar(255) DEFAULT NULL,"//
                + "PRIMARY KEY (`id`),"//
                + "KEY `service_id` (`service_id`),"//
                + "KEY `channel_id` (`channel_id`),"//
                + "KEY `player_channel` (`player_channel`),"//
                + "KEY `account_id` (`account_id`),"//
                + "KEY `player_id` (`player_id`),"//
                + "KEY `device_mac` (`device_mac`),"//
                + "KEY `login_time` (`login_time`),"//
                + "KEY `logout_time` (`logout_time`),"//
                + "KEY `online_time` (`online_time`)"//
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8" + TAB_DIRECTORY + ";";//
        jdbcw.executeSQL(createSql);
    }

    /**
     * 创建登陆汇总日志sql
     *
     * @param today 当前时间格式为yyyy_MM_dd
     */
    private void createLoginSumInfo(String today) {
        String createSql = "CREATE TABLE IF NOT EXISTS `" + today + "_tab_login_sum_info`("//
                + "`id` bigint(20) NOT NULL AUTO_INCREMENT,"//
                + "`service_id` int(11) DEFAULT NULL,"//
                + "`channel_id` int(11) DEFAULT NULL,"//
                + "`player_channel` int(11) DEFAULT NULL,"//
                + "`player_id` int(11) DEFAULT NULL,"//
                + "PRIMARY KEY (`id`)," + "KEY `service_id` (`service_id`)," + "KEY `channel_id` (`channel_id`)," + "KEY `player_channel` (`player_channel`)," + "KEY `player_id` (`player_id`)" + ") ENGINE=InnoDB DEFAULT CHARSET=utf8" + TAB_DIRECTORY + ";";//
        jdbcw.executeSQL(createSql);
    }

    private void createAccountNewCount(String today) {
        String createSql = "CREATE TABLE IF NOT EXISTS `" + today + "_tab_account_new_count`(" + "`id` bigint(20) NOT NULL AUTO_INCREMENT," + "`service_id` int(11) DEFAULT NULL," + "`channel_id` int(11) DEFAULT NULL," + "`account_id` int(11) DEFAULT NULL," + "`create_time` datetime DEFAULT NULL," + "PRIMARY KEY (`id`)," + "KEY `anc_service_id` (`service_id`)," + "KEY `anc_channel_id` (`channel_id`)" + ") ENGINE=InnoDB DEFAULT CHARSET=utf8" + TAB_DIRECTORY + ";";//
        jdbcw.executeSQL(createSql);
    }

    private void createPlayerNewCount(String today) {
        String createSql = "CREATE TABLE IF NOT EXISTS `" + today + "_tab_player_new_count`(" + "`id` bigint(20) NOT NULL AUTO_INCREMENT," + "`service_id` int(11) DEFAULT NULL," + "`channel_id` int(11) DEFAULT NULL," + "`player_id` int(11) DEFAULT NULL," + "`create_time` datetime DEFAULT NULL," + "PRIMARY KEY (`id`)," + "KEY `pnc_service_id` (`service_id`)," + "KEY `pnc_channel_id` (`channel_id`)" + ") ENGINE=InnoDB DEFAULT CHARSET=utf8" + TAB_DIRECTORY + ";";//
        jdbcw.executeSQL(createSql);
    }

    private void createDeviceNewCount(String today) {
        String createSql = "CREATE TABLE IF NOT EXISTS `" + today + "_tab_device_new_count`(" + "`id` bigint(20) NOT NULL AUTO_INCREMENT," + "`service_id` int(11) DEFAULT NULL," + "`channel_id` int(11) DEFAULT NULL," + "`device_mac` varchar(255) DEFAULT NULL," + "`create_time` datetime DEFAULT NULL," + "PRIMARY KEY (`id`)," + "KEY `dnc_service_id` (`service_id`)," + "KEY `dnc_channel_id` (`channel_id`)" + ") ENGINE=InnoDB DEFAULT CHARSET=utf8" + TAB_DIRECTORY + ";";//
        jdbcw.executeSQL(createSql);
    }

    private void createOnlineInfo(String today) {
        String createSql = "CREATE TABLE IF NOT EXISTS `" + today + "_tab_online_info`(" + "`id` bigint(11) NOT NULL AUTO_INCREMENT," + "`service_id` int(11) DEFAULT NULL," + "`channel_id` int(11) DEFAULT NULL," + "`date_minute` int(11) DEFAULT NULL,"// 记录时间(分钟)
                + "`online_num` int(11) DEFAULT NULL," + "PRIMARY KEY (`id`)," + "KEY `service_id` (`service_id`)," + "KEY `date_minute` (`date_minute`)" + ") ENGINE=InnoDB DEFAULT CHARSET=utf8" + TAB_DIRECTORY + ";";//
        jdbcw.executeSQL(createSql);
    }

    private void createRechargeInfo(String today) {
        String createSql = "CREATE TABLE IF NOT EXISTS `" + today + "_tab_recharge_info`(" + "`id` int(11) NOT NULL AUTO_INCREMENT,"// //
                + "`service_id` int(11) DEFAULT NULL,"// // 服务器id
                + "`pay_channel` int(11) DEFAULT NULL,"// //
                + "`player_channel` int(11) DEFAULT NULL,"// //
                + "`player_id` int(11) DEFAULT NULL,"// // 玩家id
                + "`product_id` int(11) DEFAULT NULL,"// //
                + "`recharge_time` datetime DEFAULT NULL,"// // 充值时间
                + "`money` double(100,2) DEFAULT '0.00',"// //
                + "`order_num` varchar(255) DEFAULT NULL,"// //
                + "`count` int(11) DEFAULT '0',"// // 充值得钻
                + "`count_all` int(11) DEFAULT '0',"// // 充值后玩家钻石总额
                + "PRIMARY KEY (`id`),"// //
                + "KEY `service_id` (`service_id`),"// //
                + "KEY `channel_id` (`pay_channel`),"// //
                + "KEY `account_id` (`player_channel`),"// //
                + "KEY `player_id` (`player_id`),"// //
                + "KEY `product_id` (`product_id`),"// //
                + "KEY `recharge_time` (`recharge_time`),"// //
                + "KEY `order_num` (`order_num`)"// //
                + ") DEFAULT CHARSET=utf8" + TAB_DIRECTORY + ";"; //
        jdbcw.executeSQL(createSql);
    }
}
