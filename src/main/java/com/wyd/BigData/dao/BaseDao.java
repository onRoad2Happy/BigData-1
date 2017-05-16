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

    public BaseDao() {
        accountInfoMap = new LinkedHashMap<Integer, AccountInfo>() {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean removeEldestEntry(java.util.Map.Entry<Integer, AccountInfo> pEldest) {
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
        jdbcw.doQuery("select `id` ,`account_id`,`service_id`,`channel_id`,`account_name`," + "`account_pwd`,`create_time`,`device_mac`,`system_version`,`system_type` from tab_account_info where id=?", new Object[] { id}, new ExecuteCallBack() {
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
