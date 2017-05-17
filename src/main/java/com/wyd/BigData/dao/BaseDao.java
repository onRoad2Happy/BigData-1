package com.wyd.BigData.dao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.wyd.BigData.JDBC.ExecuteCallBack;
import com.wyd.BigData.JDBC.JDBCWrapper;
import com.wyd.BigData.bean.AccountInfo;
import com.wyd.BigData.bean.AccountNewCount;
import com.wyd.BigData.bean.DeviceInfo;
import com.wyd.BigData.bean.DeviceNewCount;
import com.wyd.BigData.bean.LoginSumInfo;
import com.wyd.BigData.bean.PlayerInfo;
import com.wyd.BigData.bean.PlayerNewCount;
public class BaseDao implements Serializable {
    /**
     * 
     */
    private static final long  serialVersionUID = 125201040947818309L;
    public static final String TAB_DIRECTORY    = "";
    private static BaseDao     instance         = null;
    JDBCWrapper                jdbcw            = null;
    Map<Integer, AccountInfo>  accountInfoMap   = null;
    Map<String, DeviceInfo>    deviceInfoMap    = null;
    Map<Integer, PlayerInfo>    playerInfoMap    = null;

    public BaseDao() {
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
                        accountInfo.setCreateTime(rs.getDate(7));
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
        jdbcw.doQuery("select `id`,`player_id`,`service_id`,`channel_id`,`account_id`,`device_mac`,`create_time`,`player_name`,`player_sex`,`player_level`,`upgrade_time`,`player_fighting`,`vip_level`,`sports_level`,`ranking_level`,`login_time`,`is_two`,`is_third`,`is_four`,`is_five`,`is_six`,`is_seven`,`is_fourteen`,`is_thirty`,`is_sixty`,`total_money`,`recharge_num`,`first_channel`,`first_money`,`first_level`,`first_recharge`,`first_cost_time`,`first_cost_level`,`first_cost_num`,`first_cost_item`,`wltv`,`mltv`,`diamond`,`gold`,`login_num`,`seven_num`,`total_online`,`guild_id`,`coures_id`,`coures_step`,`tiro_time`,`mate_id`,`top_singlemap`,`top_dare_singlemap`,`top_singlemap_time`,`top_elite_singlemap`,`top_dare_elite_singlemap`,`top_elite_singlemap_time`,`vigor`,`battle_win_marry`,`battle_win_guild`,`gag_time`,`gag_reason`,`is_eight`,`is_nine`,`is_ten`,`is_eleven`,`is_twelve`,`is_thirteen`,`login_days`,`friend_count`,`weapon_id`,`weapon_item_id`,`weapon_level`,`necklace_id`,`necklace_item_id`,`necklace_level`,`ring_id`,`ring_item_id`,`ring_level`,`bracelet_id`,`bracelet_item_id`,`bracelet_level`,`talisman_id`,`talisman_item_id`,`talisman_level`,`medal_id`,`medal_item_id`,`medal_level`,`prop_fury_level`,`prop_hidesingle_level`,`prop_hidegroup_level`,`prop_reflect_level`,`prop_treatsingle_level`,`prop_treatgroup_level`,`prop_guardian_level`,`prop_dice_level`,`pet_id`,`pet_item_id`,`pet_level`,`rank_match_num`,`contact_num`,`top_towermap`,`towermap_num`,`teammap_num` from tab_player_info where player_id=?", new Object[] { playerId}, new ExecuteCallBack() {
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
                        info.setCreateTime(rs.getDate(7));
                        info.setPlayerName(rs.getString(8));
                        info.setPlayerSex(rs.getString(9));
                        info.setPlayerLevel(rs.getInt(10));
                        info.setUpgradeTime(rs.getInt(11));
                        info.setPlayerFighting(rs.getInt(12));
                        info.setVipLevel(rs.getInt(13));
                        info.setSportsLevel(rs.getInt(14));
                        info.setRankingLevel(rs.getInt(15));
                        info.setLoginTime(rs.getDate(16));
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
                        info.setFirstRecharge(rs.getDate(31));
                        info.setFirstCostTime(rs.getDate(32));
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
                        deviceInfo.setCreateTime(rs.getDate(5));
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
        createAccountNewCountSql(today);
        List<Object[]> paramsList = new ArrayList<>();
        for (AccountNewCount info : accountList) {
            paramsList.add(new Object[] { info.getServiceId(), info.getChannelId(), info.getAccountId(), info.getCreateTime()});
        }
        String tableName = today + "_tab_account_new_count";
        jdbcw.doBatch("insert into " + tableName + " (service_id,channel_id,account_id,create_time) values (?,?,?,?)", paramsList);
    }

    public void savePlayerInfoBatch(List<PlayerInfo> playerInfoList){
        List<Object[]> paramsList = new ArrayList<>();
        for (PlayerInfo info : playerInfoList) {
            paramsList.add(new Object[] {info.getPlayerId(),info.getServiceId(),info.getChannelId(),info.getAccountId(),info.getDeviceMac(),info.getCreateTime(),info.getPlayerName(),info.getPlayerSex(),info.getPlayerLevel(),info.getUpgradeTime(),info.getPlayerFighting(),info.getVipLevel(),info.getSportsLevel(),info.getRankingLevel(),info.getLoginTime(),info.isTwo(),info.isThird(),info.isFour(),info.isFive(),info.isSix(),info.isSeven(),info.isFourteen(),info.isThirty(),info.isSixty(),info.getTotalMoney(),info.getRechargeNum(),info.getFirstChannel(),info.getFirstMoney(),info.getFirstLevel(),info.getFirstRecharge(),info.getFirstCostTime(),info.getFirstCostLevel(),info.getFirstCostNum(),info.getFirstCostItem(),info.getWltv(),info.getMltv(),info.getDiamond(),info.getGold(),info.getLoginNum(),info.getSevenNum(),info.getTotalOnline(),info.getGuildId(),info.getCouresId(),info.getCouresStep(),info.getTiroTime(),info.getMateId(),info.getTopSinglemap(),info.getTopDareSinglemap(),info.getTopSinglemapTime(),info.getTopEliteSinglemap(),info.getTopDareEliteSinglemap(),info.getTopEliteSinglemapTime(),info.getVigor(),info.getBattleWinMarry(),info.getBattleWinGuild(),info.getGagTime(),info.getGagReason(),info.isEight(),info.isNine(),info.isTen(),info.isEleven(),info.isTwelve(),info.isThirteen(),info.getLoginDays(),info.getFriendCount(),info.getWeaponId(),info.getWeaponItemId(),info.getWeaponLevel(),info.getNecklaceId(),info.getNecklaceItemId(),info.getNecklaceLevel(),info.getRingId(),info.getRingItemId(),info.getRingLevel(),info.getBraceletId(),info.getBraceletItemId(),info.getBraceletLevel(),info.getTalismanId(),info.getTalismanItemId(),info.getTalismanLevel(),info.getMedalId(),info.getMedalItemId(),info.getMedalLevel(),info.getPropFuryLevel(),info.getPropHidesingleLevel(),info.getPropHidegroupLevel(),info.getPropReflectLevel(),info.getPropTreatsingleLevel(),info.getPropTreatgroupLevel(),info.getPropGuardianLevel(),info.getPropDiceLevel(),info.getPetId(),info.getPetItemId(),info.getPetLevel(),info.getRankMatchNum(),info.getContactNum(),info.getTopTowermap(),info.getTowermapNum(),info.getTeammapNum()});
        }        
        jdbcw.doBatch("insert into tab_player_info (`player_id`,`service_id`,`channel_id`,`account_id`,`device_mac`,`create_time`,`player_name`,`player_sex`,`player_level`,`upgrade_time`,`player_fighting`,`vip_level`,`sports_level`,`ranking_level`,`login_time`,`is_two`,`is_third`,`is_four`,`is_five`,`is_six`,`is_seven`,`is_fourteen`,`is_thirty`,`is_sixty`,`total_money`,`recharge_num`,`first_channel`,`first_money`,`first_level`,`first_recharge`,`first_cost_time`,`first_cost_level`,`first_cost_num`,`first_cost_item`,`wltv`,`mltv`,`diamond`,`gold`,`login_num`,`seven_num`,`total_online`,`guild_id`,`coures_id`,`coures_step`,`tiro_time`,`mate_id`,`top_singlemap`,`top_dare_singlemap`,`top_singlemap_time`,`top_elite_singlemap`,`top_dare_elite_singlemap`,`top_elite_singlemap_time`,`vigor`,`battle_win_marry`,`battle_win_guild`,`gag_time`,`gag_reason`,`is_eight`,`is_nine`,`is_ten`,`is_eleven`,`is_twelve`,`is_thirteen`,`login_days`,`friend_count`,`weapon_id`,`weapon_item_id`,`weapon_level`,`necklace_id`,`necklace_item_id`,`necklace_level`,`ring_id`,`ring_item_id`,`ring_level`,`bracelet_id`,`bracelet_item_id`,`bracelet_level`,`talisman_id`,`talisman_item_id`,`talisman_level`,`medal_id`,`medal_item_id`,`medal_level`,`prop_fury_level`,`prop_hidesingle_level`,`prop_hidegroup_level`,`prop_reflect_level`,`prop_treatsingle_level`,`prop_treatgroup_level`,`prop_guardian_level`,`prop_dice_level`,`pet_id`,`pet_item_id`,`pet_level`,`rank_match_num`,`contact_num`,`top_towermap`,`towermap_num`,`teammap_num`) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", paramsList);    
    }

    public void savePlayerNewCountBatch(String today, List<PlayerNewCount> accountList) {
        createPlayerNewCountSql(today);
        List<Object[]> paramsList = new ArrayList<>();
        for (PlayerNewCount info : accountList) {
            paramsList.add(new Object[] { info.getServiceId(), info.getChannelId(), info.getCreateTime()});
        }
        String tableName = today + "_tab_player_new_count";
        jdbcw.doBatch("insert into " + tableName + " (service_id,channel_id,player_id,create_time) values (?,?,?,?)", paramsList);
    }

    public void saveDeviceNewCountBatch(String today, List<DeviceNewCount> accountList) {
        createDeviceNewCountSql(today);
        List<Object[]> paramsList = new ArrayList<>();
        for (DeviceNewCount info : accountList) {
            paramsList.add(new Object[] { info.getServiceId(), info.getChannelId(), info.getDeviceMac(), info.getCreateTime()});
        }
        String tableName = today + "_tab_device_new_count";
        jdbcw.doBatch("insert into " + tableName + " (service_id,channel_id,device_mac,create_time) values (?,?,?,?)", paramsList);
    }

    public void saveAccountInfoBatch(List<AccountInfo> accountList) {
        List<Object[]> paramsList = new ArrayList<>();
        for (AccountInfo info : accountList) {
            paramsList.add(new Object[] { info.getAccountId(), info.getServiceId(), info.getChannelId(), info.getAccountName(), info.getAccountPwd(), info.getCreateTime(), info.getDeviceMac(), info.getSystemVersion(), info.getSystemType()});
        }
        String tableName = "tab_account_info";
        jdbcw.doBatch("insert into " + tableName + " (`account_id`,`service_id`,`channel_id`,`account_name`,`account_pwd`,`create_time`,`device_mac`,`system_version`,`system_type`) values (?,?,?,?,?,?,?,?,?)", paramsList);
    }
    public void updatePlayerInfoBatch(List<PlayerInfo> playerInfoList) {
        List<Object[]> paramsList = new ArrayList<>();
        for (PlayerInfo info : playerInfoList) {
            paramsList.add(new Object[] { info.getPlayerLevel(),info.getLoginTime(),info.getLoginNum(),info.getDiamond(),info.getGold(),info.getVigor(),info.getPlayerId()});
            playerInfoMap.put(info.getPlayerId(), info);
        }        
        jdbcw.doBatch("update tab_player_info set player_level=?,login_time=?,login_num=?,diamond=?,gold=?,vigor=? where player_id=?", paramsList);
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

    private void createAccountNewCountSql(String today) {
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

    private void createPlayerNewCountSql(String today) {
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

    private void createDeviceNewCountSql(String today) {
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
}
