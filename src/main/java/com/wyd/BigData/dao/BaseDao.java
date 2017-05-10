package com.wyd.BigData.dao;

import com.wyd.BigData.JDBC.JDBCWrapper;

public class BaseDao {
	JDBCWrapper jdbcw= JDBCWrapper.getInstance();
	public void delet(String tableName){		
		jdbcw.executeSQL("delete from "+tableName);
	}
	
	public void createTabLogin(String tableName){
		jdbcw.executeSQL("CREATE TABLE IF NOT EXISTS " + tableName + " (server_id INT,channel_id INT, player_id INT)");
	}
}
