package com.wyd.BigData.util;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.wyd.BigData.JDBC.ExecuteCallBack;
import com.wyd.BigData.JDBC.JDBCWrapper;
import com.wyd.BigData.bean.OnlineInfo;
import com.wyd.BigData.bean.PlayerInfo;
import com.wyd.BigData.bean.RechargeInfo;
import com.wyd.BigData.dao.BaseDao;
public class SqlUtil {
    /**
     * 根据表字段名生成select、insert语句
     * @param tableName
     * @param dbName
     */
    public static void getSQLStr(String tableName, String dbName) {
        JDBCWrapper jdbcw = JDBCWrapper.getInstance();
        jdbcw.doQuery("SELECT column_name,data_type from information_schema.columns  WHERE  table_name = ? and table_schema=?", new Object[] { tableName, dbName}, new ExecuteCallBack() {
            @Override
            public void call(ResultSet rs) {
                try {
                    String s = "", columns = "", params = "", getBean = "", setBean = "";
                    boolean first = true;
                    int c = 1;
                    while (rs.next()) {
                        String column = rs.getString(1);
                        s = first ? "" : ",";
                        columns += s + "`" + column + "`";
                        params += s + "?";
                        String method = "", firsChar = column.substring(0, 1).toUpperCase();
                        method = firsChar + column.substring(1, column.length());
                        int index = method.indexOf("_");
                        if (index != -1) {
                            String[] methodSplit = method.split("_");
                            String other = "", otherFisterChar = "";
                            method = methodSplit[0];
                            for (int i = 1; i < methodSplit.length; i++) {
                                other = methodSplit[i];
                                otherFisterChar = other.substring(0, 1).toUpperCase();
                                other = otherFisterChar + other.substring(1, other.length());
                                method += other;
                            }
                        }
                        if (!column.equals("id")) {
                            getBean += s + "info.get" + method + "()";
                        }
                        String datatype = rs.getString(2).toLowerCase();
                        if (method.indexOf("Is") != -1 && datatype.equals("tinyint")) {
                            setBean += "info.set" + method + "(rs.getBoolean(" + c + "));\n";
                        } else if (datatype.equals("int")) {
                            setBean += "info.set" + method + "(rs.getInt(" + c + "));\n";
                        } else if (datatype.equals("datetime")) {
                            setBean += "info.set" + method + "(rs.getDate(" + c + "));\n";
                        } else if (datatype.equals("double")) {
                            setBean += "info.set" + method + "(rs.getDate(" + c + "));\n";
                        } else {
                            setBean += "info.set" + method + "(rs.getString(" + c + "));\n";
                        }
                        setBean = setBean.replace("setIs", "set");
                        c++;
                        first = false;
                    }
                    String selectSql = "select " + columns + " from " + tableName;
                    columns = columns.replace("`id`,", "");
                    String insertSql = "insert into " + tableName + " (" + columns + ") values (" + params + ")";
                    System.out.println(selectSql);
                    System.out.println(insertSql);
                    System.out.println(getBean);
                    System.out.println(setBean);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        SqlUtil.getSQLStr("2016_11_15_tab_login_info", "test");
        List<PlayerInfo> playerInfoList = new ArrayList<>();
        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.setPlayerId(1);
        playerInfo.setServiceId(2);
        playerInfo.setChannelId(3);
        playerInfo.setAccountId(4);
        playerInfo.setPlayerLevel(10);
        playerInfo.setCreateTime(new Date());
        playerInfo.setPlayerName("test");
        playerInfo.setPlayerSex("0".equals(1) ? "男" : "女");
        playerInfo.setLoginTime(playerInfo.getCreateTime());
        playerInfo.setVigor(11);
        playerInfo.setMltv(1);
        playerInfo.setWltv(2);
        playerInfoList.add(playerInfo);
        BaseDao dao = BaseDao.getInstance();
        // dao.savePlayerInfoBatch(playerInfoList);
        // dao.updatePlayerInfoBatch(playerInfoList);
        // playerInfo=dao.getPlayerInfo(1);
        // System.out.println(playerInfo.getPlayerName()+" "+playerInfo.getMltv()+" "+playerInfo.getWltv());
        List<OnlineInfo> onlineInfoList = new ArrayList<>();
        OnlineInfo info = new OnlineInfo();
        info.setServiceId(1);
        info.setDateMinute(2);
        info.setOnlineNum(3);
        info.setChannelId(5);
        onlineInfoList.add(info);
        // dao.saveOnlineInfoBatch("2017_05_18", onlineInfoList);
    }
}
