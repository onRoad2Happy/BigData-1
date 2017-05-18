package com.wyd.BigData.dao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.wyd.BigData.JDBC.ExecuteCallBack;
import com.wyd.BigData.JDBC.JDBCWrapper;
import com.wyd.BigData.bean.AccountInfo;
import com.wyd.BigData.bean.AccountNewCount;
import com.wyd.BigData.bean.DeviceInfo;
import com.wyd.BigData.bean.DeviceNewCount;
import com.wyd.BigData.bean.LoginInfo;
import com.wyd.BigData.bean.LoginSumInfo;
import com.wyd.BigData.bean.OnlineInfo;
import com.wyd.BigData.bean.PlayerInfo;
import com.wyd.BigData.bean.PlayerNewCount;
import com.wyd.BigData.bean.RechargeInfo;
public class BaseDao implements Serializable {
    /**
     * 
     */
    private static final long       serialVersionUID = 125201040947818309L;
    private static SimpleDateFormat sf               = new SimpleDateFormat("yyyy_MM_dd");
    private static final String     TAB_DIRECTORY    = "";
    private static BaseDao          instance         = null;
    JDBCWrapper                     jdbcw            = null;
    Map<Integer, AccountInfo>       accountInfoMap   = null;
    Map<String, DeviceInfo>         deviceInfoMap    = null;
    Map<Integer, PlayerInfo>        playerInfoMap    = null;
    Map<Integer, LoginInfo>         loginInfoMap     = null;

    public BaseDao() {
        loginInfoMap = new LinkedHashMap<Integer, LoginInfo>() {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean removeEldestEntry(java.util.Map.Entry<Integer, LoginInfo> pEldest) {
                return size() > 1000;
            }
        };
        accountInfoMap = new LinkedHashMap<Integer, AccountInfo>() {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean removeEldestEntry(java.util.Map.Entry<Integer, AccountInfo> pEldest) {
                return size() > 1000;
            }
        };
        playerInfoMap = new LinkedHashMap<Integer, PlayerInfo>() {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean removeEldestEntry(java.util.Map.Entry<Integer, PlayerInfo> pEldest) {
                return size() > 1000;
            }
        };
        deviceInfoMap = new LinkedHashMap<String, DeviceInfo>() {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean removeEldestEntry(java.util.Map.Entry<String, DeviceInfo> pEldest) {
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

    public AccountInfo getAccountInfo(int id) {
        if (accountInfoMap.containsKey(id)) return accountInfoMap.get(id);
        AccountInfo accountInfo = new AccountInfo();
        jdbcw.doQuery("select `id` ,`account_id`,`service_id`,`channel_id`,`account_name`,`account_pwd`,`create_time`,`device_mac`,`system_version`,`system_type` from tab_account_info where id=?", new Object[] { id}, new ExecuteCallBack() {
            @Override
            public void call(ResultSet rs) {
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
            }
        });
        if (accountInfo.getId() != 0) {
            accountInfoMap.put(id, accountInfo);
            return accountInfo;
        }
        return null;
    }

    public PlayerInfo getPlayerInfo(int playerId) {
        if (playerInfoMap.containsKey(playerId)) return playerInfoMap.get(playerId);
        PlayerInfo info = new PlayerInfo();
        jdbcw.doQuery(
                "select `id`,`player_id`,`service_id`,`channel_id`,`account_id`,`device_mac`,`create_time`,`player_name`,`player_sex`,`player_level`,`upgrade_time`,`player_fighting`,`vip_level`,`sports_level`,`ranking_level`,`login_time`,`is_two`,`is_third`,`is_four`,`is_five`,`is_six`,`is_seven`,`is_fourteen`,`is_thirty`,`is_sixty`,`total_money`,`recharge_num`,`first_channel`,`first_money`,`first_level`,`first_recharge`,`first_cost_time`,`first_cost_level`,`first_cost_num`,`first_cost_item`,`wltv`,`mltv`,`diamond`,`gold`,`login_num`,`seven_num`,`total_online`,`guild_id`,`coures_id`,`coures_step`,`tiro_time`,`mate_id`,`top_singlemap`,`top_dare_singlemap`,`top_singlemap_time`,`top_elite_singlemap`,`top_dare_elite_singlemap`,`top_elite_singlemap_time`,`vigor`,`battle_win_marry`,`battle_win_guild`,`gag_time`,`gag_reason`,`is_eight`,`is_nine`,`is_ten`,`is_eleven`,`is_twelve`,`is_thirteen`,`login_days`,`friend_count`,`weapon_id`,`weapon_item_id`,`weapon_level`,`necklace_id`,`necklace_item_id`,`necklace_level`,`ring_id`,`ring_item_id`,`ring_level`,`bracelet_id`,`bracelet_item_id`,`bracelet_level`,`talisman_id`,`talisman_item_id`,`talisman_level`,`medal_id`,`medal_item_id`,`medal_level`,`prop_fury_level`,`prop_hidesingle_level`,`prop_hidegroup_level`,`prop_reflect_level`,`prop_treatsingle_level`,`prop_treatgroup_level`,`prop_guardian_level`,`prop_dice_level`,`pet_id`,`pet_item_id`,`pet_level`,`rank_match_num`,`contact_num`,`top_towermap`,`towermap_num`,`teammap_num` from tab_player_info where player_id=?",
                new Object[] { playerId}, new ExecuteCallBack() {
                    @Override
                    public void call(ResultSet rs) {
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
                    }
                });
        if (info.getId() != 0) {
            playerInfoMap.put(playerId, info);
            return info;
        }
        return null;
    }

    public DeviceInfo getDeviceInfo(String mac) {
        if (deviceInfoMap.containsKey(mac)) return deviceInfoMap.get(mac);
        DeviceInfo deviceInfo = new DeviceInfo();
        jdbcw.doQuery("select `id` ,`service_id` ,`channel_id`,`device_mac`,`create_time`, `device_name`,`system_name`,`system_version`,`app_version`  from tab_device_info where device_mac=?", new Object[] { mac}, new ExecuteCallBack() {
            @Override
            public void call(ResultSet rs) {
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
            paramsList.add(new Object[] { info.getServiceId(), info.getChannelId(), info.getDeviceMac(), info.getCreateTime(), info.getDeviceName(), info.getSystemName(), info.getSystemVersion(), info.getAppVersion()});
        }
        String tableName = "tab_device_info";
        jdbcw.doBatch("insert into " + tableName + " (`service_id` ,`channel_id`,`device_mac`,`create_time`, `device_name`,`system_name`,`system_version`,`app_version`) values (?,?,?,?,?,?,?,?)", paramsList);
    }

    public void delete(String tableName) {
        jdbcw.executeSQL("delete from " + tableName);
    }

    public void createTabLogin(String tableName) {
        jdbcw.executeSQL("CREATE TABLE IF NOT EXISTS " + tableName + " (server_id INT,channel_id INT, player_id INT)");
    }

    public List<LoginSumInfo> getAllLoginSumInfo(String today) {
        List<LoginSumInfo> infoList = new ArrayList<>();
        createLoginSumInfo(today);
        jdbcw.doQuery("select id,service_id,channel_id,player_channel,player_id from  " + today + "_tab_login_sum_info", null, new ExecuteCallBack() {
            @Override
            public void call(ResultSet rs) {
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
            }
        });
        return infoList;
    }

    public void saveLoginSumInfoBatch(String today, List<LoginSumInfo> loginList) {
        List<Object[]> paramsList = new ArrayList<>();
        for (LoginSumInfo info : loginList) {
            paramsList.add(new Object[] { info.getServiceId(), info.getChannelId(), info.getPlayerChannel(), info.getPlayerId()});
        }
        jdbcw.doBatch("INSERT INTO " + today + "_tab_login_sum_info(service_id,channel_id,player_channel,player_id) VALUES(?,?,?,?)", paramsList);
    }

    public void saveAccountNewCountBatch(String today, List<AccountNewCount> accountList) {
        createAccountNewCount(today);
        List<Object[]> paramsList = new ArrayList<>();
        for (AccountNewCount info : accountList) {
            paramsList.add(new Object[] { info.getServiceId(), info.getChannelId(), info.getAccountId(), info.getCreateTime()});
        }
        String tableName = today + "_tab_account_new_count";
        jdbcw.doBatch("insert into " + tableName + " (service_id,channel_id,account_id,create_time) values (?,?,?,?)", paramsList);
    }

    public void savePlayerInfoBatch(List<PlayerInfo> playerInfoList) {
        List<Object[]> paramsList = new ArrayList<>();
        for (PlayerInfo info : playerInfoList) {
            paramsList.add(new Object[] { info.getPlayerId(), info.getServiceId(), info.getChannelId(), info.getAccountId(), info.getDeviceMac(), info.getCreateTime(), info.getPlayerName(), info.getPlayerSex(), info.getPlayerLevel(), info.getUpgradeTime(), info.getPlayerFighting(), info.getVipLevel(), info.getSportsLevel(), info.getRankingLevel(), info.getLoginTime(), info.isTwo(), info.isThird(), info.isFour(), info.isFive(), info.isSix(), info.isSeven(), info.isFourteen(), info.isThirty(), info.isSixty(), info.getTotalMoney(), info.getRechargeNum(), info.getFirstChannel(), info.getFirstMoney(), info.getFirstLevel(), info.getFirstRecharge(), info.getFirstCostTime(), info.getFirstCostLevel(), info.getFirstCostNum(), info.getFirstCostItem(), info.getWltv(), info.getMltv(),
                    info.getDiamond(), info.getGold(), info.getLoginNum(), info.getSevenNum(), info.getTotalOnline(), info.getGuildId(), info.getCouresId(), info.getCouresStep(), info.getTiroTime(), info.getMateId(), info.getTopSinglemap(), info.getTopDareSinglemap(), info.getTopSinglemapTime(), info.getTopEliteSinglemap(), info.getTopDareEliteSinglemap(), info.getTopEliteSinglemapTime(), info.getVigor(), info.getBattleWinMarry(), info.getBattleWinGuild(), info.getGagTime(), info.getGagReason(), info.isEight(), info.isNine(), info.isTen(), info.isEleven(), info.isTwelve(), info.isThirteen(), info.getLoginDays(), info.getFriendCount(), info.getWeaponId(), info.getWeaponItemId(), info.getWeaponLevel(), info.getNecklaceId(), info.getNecklaceItemId(), info.getNecklaceLevel(), info.getRingId(),
                    info.getRingItemId(), info.getRingLevel(), info.getBraceletId(), info.getBraceletItemId(), info.getBraceletLevel(), info.getTalismanId(), info.getTalismanItemId(), info.getTalismanLevel(), info.getMedalId(), info.getMedalItemId(), info.getMedalLevel(), info.getPropFuryLevel(), info.getPropHidesingleLevel(), info.getPropHidegroupLevel(), info.getPropReflectLevel(), info.getPropTreatsingleLevel(), info.getPropTreatgroupLevel(), info.getPropGuardianLevel(), info.getPropDiceLevel(), info.getPetId(), info.getPetItemId(), info.getPetLevel(), info.getRankMatchNum(), info.getContactNum(), info.getTopTowermap(), info.getTowermapNum(), info.getTeammapNum()});
        }
        jdbcw.doBatch(
                "insert into tab_player_info (`player_id`,`service_id`,`channel_id`,`account_id`,`device_mac`,`create_time`,`player_name`,`player_sex`,`player_level`,`upgrade_time`,`player_fighting`,`vip_level`,`sports_level`,`ranking_level`,`login_time`,`is_two`,`is_third`,`is_four`,`is_five`,`is_six`,`is_seven`,`is_fourteen`,`is_thirty`,`is_sixty`,`total_money`,`recharge_num`,`first_channel`,`first_money`,`first_level`,`first_recharge`,`first_cost_time`,`first_cost_level`,`first_cost_num`,`first_cost_item`,`wltv`,`mltv`,`diamond`,`gold`,`login_num`,`seven_num`,`total_online`,`guild_id`,`coures_id`,`coures_step`,`tiro_time`,`mate_id`,`top_singlemap`,`top_dare_singlemap`,`top_singlemap_time`,`top_elite_singlemap`,`top_dare_elite_singlemap`,`top_elite_singlemap_time`,`vigor`,`battle_win_marry`,`battle_win_guild`,`gag_time`,`gag_reason`,`is_eight`,`is_nine`,`is_ten`,`is_eleven`,`is_twelve`,`is_thirteen`,`login_days`,`friend_count`,`weapon_id`,`weapon_item_id`,`weapon_level`,`necklace_id`,`necklace_item_id`,`necklace_level`,`ring_id`,`ring_item_id`,`ring_level`,`bracelet_id`,`bracelet_item_id`,`bracelet_level`,`talisman_id`,`talisman_item_id`,`talisman_level`,`medal_id`,`medal_item_id`,`medal_level`,`prop_fury_level`,`prop_hidesingle_level`,`prop_hidegroup_level`,`prop_reflect_level`,`prop_treatsingle_level`,`prop_treatgroup_level`,`prop_guardian_level`,`prop_dice_level`,`pet_id`,`pet_item_id`,`pet_level`,`rank_match_num`,`contact_num`,`top_towermap`,`towermap_num`,`teammap_num`) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                paramsList);
    }

    public void savePlayerNewCountBatch(String today, List<PlayerNewCount> accountList) {
        createPlayerNewCount(today);
        List<Object[]> paramsList = new ArrayList<>();
        for (PlayerNewCount info : accountList) {
            paramsList.add(new Object[] { info.getServiceId(), info.getChannelId(), info.getCreateTime()});
        }
        String tableName = today + "_tab_player_new_count";
        jdbcw.doBatch("insert into " + tableName + " (service_id,channel_id,player_id,create_time) values (?,?,?,?)", paramsList);
    }

    public void saveRechargeInfoBatch(String today, List<RechargeInfo> accountList) {
        createRechargeInfo(today);
        List<Object[]> paramsList = new ArrayList<>();
        for (RechargeInfo info : accountList) {
            paramsList.add(new Object[] { info.getServiceId(), info.getPayChannel(), info.getPlayerChannel(), info.getPlayerId(), info.getProductId(), info.getRechargeTime(), info.getMoney(), info.getOrderNum(), info.getCount(), info.getCountAll()});
        }
        String tableName = today + "_tab_recharge_info";
        jdbcw.doBatch("insert into " + tableName + " (`service_id`,`pay_channel`,`player_channel`,`player_id`,`product_id`,`recharge_time`,`money`,`order_num`,`count`,`count_all`) values (?,?,?,?,?,?,?,?,?,?)", paramsList);
    }
    private Date getRsDate(java.sql.Timestamp timestamp){
       return timestamp==null?null: new Date(timestamp.getTime());
    }
    public LoginInfo getLoginInfo(String today, int playerId) {
        if (loginInfoMap.containsKey(playerId)) return loginInfoMap.get(playerId);
        LoginInfo info = new LoginInfo();
        String sql = "select `id`,`service_id`,`channel_id`,`account_id`,`player_id`,`device_mac`,`device_name`,`system_name`,`system_version`,`app_version`,`login_time`,`logout_time`,`online_time`,`login_ip`,`diamond`,`gold`,`vigor`,`player_level`,`account_name`,`player_name` from " + today + "_tab_login_info where player_id=?";
        jdbcw.doQuery(sql, new Object[] { playerId}, new ExecuteCallBack() {
            @Override
            public void call(ResultSet rs) {
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
            }
        });
        if (info.getId() != 0) {
            loginInfoMap.put(info.getPlayerId(), info);
            return info;
        }
        return null;
    }

    public void saveLoginInfoBatch(String today, List<LoginInfo> loginList) {
        createLoginInfo(today);
        List<Object[]> paramsList = new ArrayList<>();
        for (LoginInfo info : loginList) {
            paramsList.add(new Object[] { info.getServiceId(), info.getChannelId(), info.getAccountId(), info.getPlayerId(), info.getDeviceMac(), info.getDeviceName(), info.getSystemName(), info.getSystemVersion(), info.getAppVersion(), info.getLoginTime(), info.getLogoutTime(), info.getOnlineTime(), info.getLoginIp(), info.getDiamond(), info.getGold(), info.getVigor(), info.getPlayerLevel(), info.getAccountName(), info.getPlayerName()});
        }
        String tableName = today + "_tab_login_info";
        jdbcw.doBatch("insert into " + tableName + " (`service_id`,`channel_id`,`account_id`,`player_id`,`device_mac`,`device_name`,`system_name`,`system_version`,`app_version`,`login_time`,`logout_time`,`online_time`,`login_ip`,`diamond`,`gold`,`vigor`,`player_level`,`account_name`,`player_name`) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", paramsList);
    }

    public void saveDeviceNewCountBatch(String today, List<DeviceNewCount> accountList) {
        createDeviceNewCount(today);
        List<Object[]> paramsList = new ArrayList<>();
        for (DeviceNewCount info : accountList) {
            paramsList.add(new Object[] { info.getServiceId(), info.getChannelId(), info.getDeviceMac(), info.getCreateTime()});
        }
        String tableName = today + "_tab_device_new_count";
        jdbcw.doBatch("insert into " + tableName + " (service_id,channel_id,device_mac,create_time) values (?,?,?,?)", paramsList);
    }

    public void saveOnlineInfoBatch(String today, List<OnlineInfo> onlineInfoList) {
        createOnlineInfo(today);
        List<Object[]> paramsList = new ArrayList<>();
        for (OnlineInfo info : onlineInfoList) {
            paramsList.add(new Object[] { info.getServiceId(), info.getChannelId(), info.getDateMinute(), info.getOnlineNum()});
        }
        String tableName = today + "_tab_online_info";
        jdbcw.doBatch("insert into " + tableName + " (service_id,channel_id,date_minute,online_num) values (?,?,?,?)", paramsList);
    }

    public void saveAccountInfoBatch(List<AccountInfo> accountList) {
        List<Object[]> paramsList = new ArrayList<>();
        for (AccountInfo info : accountList) {
            paramsList.add(new Object[] { info.getAccountId(), info.getServiceId(), info.getChannelId(), info.getAccountName(), info.getAccountPwd(), info.getCreateTime(), info.getDeviceMac(), info.getSystemVersion(), info.getSystemType()});
        }
        String tableName = "tab_account_info";
        jdbcw.doBatch("insert into " + tableName + " (`account_id`,`service_id`,`channel_id`,`account_name`,`account_pwd`,`create_time`,`device_mac`,`system_version`,`system_type`) values (?,?,?,?,?,?,?,?,?)", paramsList);
    }
/**
 * 当登出时使用logout  ture
 * @param loginInfoList
 * @param logout
 */
    public void updateLoginInfoBatch(List<LoginInfo> loginInfoList,boolean logout) {
        Map<String, List<Object[]>> dayParamMap = new HashMap<>();
        for (LoginInfo info : loginInfoList) {
            String today = sf.format(info.getLoginTime());
            List<Object[]> paramsList = dayParamMap.get(today);
            if (paramsList == null) {
                paramsList = new ArrayList<>();
                dayParamMap.put(today, paramsList);
            }
            paramsList.add(new Object[] {info.getLogoutTime(),info.getOnlineTime(),info.getPlayerLevel(), info.getPlayerId()});
            loginInfoMap.put(info.getPlayerId(), info);
        }
        for (String today : dayParamMap.keySet()) {
            if(logout){
                jdbcw.doBatch("update " + today + "_tab_login_info set logout_time=?,online_time=?,player_level=?  where player_id=? and logout_time is null", dayParamMap.get(today));
            }else{
                jdbcw.doBatch("update " + today + "_tab_login_info set logout_time=?,online_time=?,player_level=?  where player_id=?", dayParamMap.get(today));
            }
        }
    }

    public void updatePlayerInfoBatch(List<PlayerInfo> playerInfoList) {
        List<Object[]> paramsList = new ArrayList<>();
        for (PlayerInfo info : playerInfoList) {
            paramsList.add(new Object[] { info.getPlayerLevel(), info.getLoginTime(), info.getLoginNum(), info.getDiamond(), info.getGold(), info.getVigor(), info.getFirstChannel(), info.getFirstMoney(), info.getFirstRecharge(), info.getFirstLevel(), info.getTotalMoney(), info.getRechargeNum(), info.getWltv(), info.getMltv(), info.getPlayerId()});
            playerInfoMap.put(info.getPlayerId(), info);
        }
        jdbcw.doBatch("update tab_player_info set player_level=?,login_time=?,login_num=?,diamond=?,gold=?,vigor=?,first_channel=?,first_money=?,first_recharge=?,first_level=?,total_money=?,recharge_num=?,wltv=?,mltv=? where player_id=?", paramsList);
    }

    private void createLoginInfo(String today) {
        StringBuffer createSql = new StringBuffer();
        createSql.append("CREATE TABLE IF NOT EXISTS `").append(today).append("_tab_login_info`(")//
                .append("`id` bigint(20) NOT NULL AUTO_INCREMENT,")//
                .append("`service_id` int(11) DEFAULT NULL,")//
                .append("`channel_id` int(11) DEFAULT NULL,")//
                .append("`player_channel` int(11) DEFAULT NULL,")//
                .append("`account_id` int(11) DEFAULT NULL,")//
                .append("`player_id` int(11) DEFAULT NULL,")//
                .append("`device_mac` varchar(255) DEFAULT NULL,")//
                .append("`device_name` varchar(255) DEFAULT NULL,")//
                .append("`system_name` varchar(255) DEFAULT NULL,")//
                .append("`system_version` varchar(255) DEFAULT NULL,")//
                .append("`app_version` varchar(255) DEFAULT NULL,")//
                .append("`login_time` datetime DEFAULT NULL,")//
                .append("`logout_time` datetime DEFAULT NULL,")//
                .append("`online_time` int(11) DEFAULT NULL,")//
                .append("`login_ip` varchar(32) DEFAULT NULL,")//
                .append("`diamond` int(11) DEFAULT '0',")//
                .append("`gold` int(11) DEFAULT '0',")//
                .append("`vigor` int(11) DEFAULT '0',")//
                .append("`player_level` int(11) DEFAULT '0',")//
                .append("`account_name` varchar(255) DEFAULT NULL,")//
                .append("`player_name` varchar(255) DEFAULT NULL,")//
                .append("PRIMARY KEY (`id`),")//
                .append("KEY `service_id` (`service_id`),")//
                .append("KEY `channel_id` (`channel_id`),")//
                .append("KEY `player_channel` (`player_channel`),")//
                .append("KEY `account_id` (`account_id`),")//
                .append("KEY `player_id` (`player_id`),")//
                .append("KEY `device_mac` (`device_mac`),")//
                .append("KEY `login_time` (`login_time`),")//
                .append("KEY `logout_time` (`logout_time`),")//
                .append("KEY `online_time` (`online_time`)")//
                .append(") ENGINE=InnoDB DEFAULT CHARSET=utf8" + TAB_DIRECTORY + ";");//
        jdbcw.executeSQL(createSql.toString());
    }

    /**
    * 创建登陆汇总日志sql
    * 
    * @param today
    */
    private void createLoginSumInfo(String today) {
        StringBuffer createSql = new StringBuffer();
        createSql.append("CREATE TABLE IF NOT EXISTS `").append(today).append("_tab_login_sum_info`(")//
                .append("`id` bigint(20) NOT NULL AUTO_INCREMENT,")//
                .append("`service_id` int(11) DEFAULT NULL,")//
                .append("`channel_id` int(11) DEFAULT NULL,")//
                .append("`player_channel` int(11) DEFAULT NULL,")//
                .append("`player_id` int(11) DEFAULT NULL,")//
                .append("PRIMARY KEY (`id`),")//
                .append("KEY `service_id` (`service_id`),")//
                .append("KEY `channel_id` (`channel_id`),")//
                .append("KEY `player_channel` (`player_channel`),")//
                .append("KEY `player_id` (`player_id`)")//
                .append(") ENGINE=InnoDB DEFAULT CHARSET=utf8" + TAB_DIRECTORY + ";");//
        jdbcw.executeSQL(createSql.toString());
    }

    private void createAccountNewCount(String today) {
        StringBuffer createSql = new StringBuffer();
        createSql.append("CREATE TABLE IF NOT EXISTS `").append(today).append("_tab_account_new_count`(")//
                .append("`id` bigint(20) NOT NULL AUTO_INCREMENT,")//
                .append("`service_id` int(11) DEFAULT NULL,")//
                .append("`channel_id` int(11) DEFAULT NULL,")//
                .append("`account_id` int(11) DEFAULT NULL,")//
                .append("`create_time` datetime DEFAULT NULL,")//
                .append("PRIMARY KEY (`id`),")//
                .append("KEY `anc_service_id` (`service_id`),")//
                .append("KEY `anc_channel_id` (`channel_id`)")//
                .append(") ENGINE=InnoDB DEFAULT CHARSET=utf8" + TAB_DIRECTORY + ";");//
        jdbcw.executeSQL(createSql.toString());
    }

    private void createPlayerNewCount(String today) {
        StringBuffer createSql = new StringBuffer();
        createSql.append("CREATE TABLE IF NOT EXISTS `").append(today).append("_tab_player_new_count`(")//
                .append("`id` bigint(20) NOT NULL AUTO_INCREMENT,")//
                .append("`service_id` int(11) DEFAULT NULL,")//
                .append("`channel_id` int(11) DEFAULT NULL,")//
                .append("`player_id` int(11) DEFAULT NULL,")//
                .append("`create_time` datetime DEFAULT NULL,")//
                .append("PRIMARY KEY (`id`),")//
                .append("KEY `pnc_service_id` (`service_id`),")//
                .append("KEY `pnc_channel_id` (`channel_id`)")//
                .append(") ENGINE=InnoDB DEFAULT CHARSET=utf8" + TAB_DIRECTORY + ";");//
        jdbcw.executeSQL(createSql.toString());
    }

    private void createDeviceNewCount(String today) {
        StringBuffer createSql = new StringBuffer();
        createSql.append("CREATE TABLE IF NOT EXISTS `").append(today).append("_tab_device_new_count`(")//
                .append("`id` bigint(20) NOT NULL AUTO_INCREMENT,")//
                .append("`service_id` int(11) DEFAULT NULL,")//
                .append("`channel_id` int(11) DEFAULT NULL,")//
                .append("`device_mac` varchar(255) DEFAULT NULL,")//
                .append("`create_time` datetime DEFAULT NULL,")//
                .append("PRIMARY KEY (`id`),")//
                .append("KEY `dnc_service_id` (`service_id`),")//
                .append("KEY `dnc_channel_id` (`channel_id`)")//
                .append(") ENGINE=InnoDB DEFAULT CHARSET=utf8" + TAB_DIRECTORY + ";");//
        jdbcw.executeSQL(createSql.toString());
    }

    private void createOnlineInfo(String today) {
        StringBuffer createSql = new StringBuffer();
        createSql.append("CREATE TABLE IF NOT EXISTS `").append(today).append("_tab_online_info`(")//
                .append("`id` bigint(11) NOT NULL AUTO_INCREMENT,")//
                .append("`service_id` int(11) DEFAULT NULL,")//
                .append("`channel_id` int(11) DEFAULT NULL,")//
                .append("`date_minute` int(11) DEFAULT NULL,")// 记录时间(分钟)
                .append("`online_num` int(11) DEFAULT NULL,")//
                .append("PRIMARY KEY (`id`),")//
                .append("KEY `service_id` (`service_id`),")//
                .append("KEY `date_minute` (`date_minute`)")//
                .append(") ENGINE=InnoDB DEFAULT CHARSET=utf8" + TAB_DIRECTORY + ";");//
        jdbcw.executeSQL(createSql.toString());
    }

    private void createRechargeInfo(String today) {
        StringBuffer createSql = new StringBuffer();
        createSql.append("CREATE TABLE IF NOT EXISTS `").append(today).append("_tab_recharge_info`(")//
                .append("`id` int(11) NOT NULL AUTO_INCREMENT,") //
                .append("`service_id` int(11) DEFAULT NULL,") // 服务器id
                .append("`pay_channel` int(11) DEFAULT NULL,") //
                .append("`player_channel` int(11) DEFAULT NULL,") //
                .append("`player_id` int(11) DEFAULT NULL,") // 玩家id
                .append("`product_id` int(11) DEFAULT NULL,") //
                .append("`recharge_time` datetime DEFAULT NULL,") // 充值时间
                .append("`money` double(100,2) DEFAULT '0.00',") //
                .append("`order_num` varchar(255) DEFAULT NULL,") //
                .append("`count` int(11) DEFAULT '0',") // 充值得钻
                .append("`count_all` int(11) DEFAULT '0',") // 充值后玩家钻石总额
                .append("PRIMARY KEY (`id`),") //
                .append("KEY `service_id` (`service_id`),") //
                .append("KEY `channel_id` (`pay_channel`),") //
                .append("KEY `account_id` (`player_channel`),") //
                .append("KEY `player_id` (`player_id`),") //
                .append("KEY `product_id` (`product_id`),") //
                .append("KEY `recharge_time` (`recharge_time`),") //
                .append("KEY `order_num` (`order_num`)") //
                .append(") DEFAULT CHARSET=utf8" + TAB_DIRECTORY + ";"); //
        jdbcw.executeSQL(createSql.toString());
    }
}
