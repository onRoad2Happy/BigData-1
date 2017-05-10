package com.wyd.BigData.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class JDBCWrapper {
	private static JDBCWrapper instance = null;
	private static LinkedBlockingQueue<Connection> connPool = new LinkedBlockingQueue<>();

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public JDBCWrapper() {
		for (int i = 0; i < 10; i++) {
			Connection conn;
			try {
				conn = DriverManager.getConnection("jdbc:mysql://192.168.3.30/test?user=root&password=123456&useUnicode=true&characterEncoding=UTF8");
				connPool.put(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	public static JDBCWrapper getInstance() {
		if (instance == null) {
			synchronized (JDBCWrapper.class) {
				if (instance == null) {
					instance = new JDBCWrapper();
				}
			}

		}
		return instance;
	}

	public Connection getConnction() throws Exception {
		int count = 0;
		Connection conn = connPool.poll();
		while (conn == null && count < 10) {
			try {
				Thread.sleep(200l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			conn = connPool.poll();
			count++;
		}
		if (conn == null) {
			throw new Exception("cant't get any connection!");
		}
		return conn;
	}
	/**
	 * 批量提交
	 */
	public int[] doBatch(String sql, List<Object[]> paramsList) {
		Connection conn = null;
		int[] result = null;
		PreparedStatement pst = null;
		try {
			conn = getConnction();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			for (Object[] params : paramsList) {
				for (int i = 0; i < params.length; i++) {
					pst.setObject(i + 1, params[i]);
				}
				pst.addBatch();
			}
			result = pst.executeBatch();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					connPool.put(conn);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}
	/**
	 * 执行查询
	 * @param sql
	 * @param params
	 * @param callBack
	 */
	public void doQuery(String sql, Object[] params, ExecuteCallBack callBack) {
		Connection conn = null;		
		PreparedStatement pst = null;
		try {
			conn = getConnction();
			pst = conn.prepareStatement(sql);

			for (int i = 0; i < params.length; i++) {
				pst.setObject(i + 1, params[i]);
			}
			callBack.call(pst.executeQuery());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					connPool.put(conn);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}		
	}
	public static void main(String[] args) {
		JDBCWrapper jdbcw= JDBCWrapper.getInstance();
		List<String> names = new ArrayList<String>();
		List<Object[]> paramsList = new ArrayList<>();
		paramsList.add(new Object[]{"spark"});
		paramsList.add(new Object[]{"scala"});
		jdbcw.doBatch("INSERT INTO tab_user(name) VALUES(?)", paramsList);
		paramsList.clear();
		paramsList.add(new Object[]{"java",1});
		jdbcw.doBatch("UPDATE tab_user set name=? where id=?", paramsList);
		jdbcw.doQuery("select * from tab_user", new Object[]{}, new ExecuteCallBack() {
			@Override
			public void call(ResultSet rs) {				
				try {
					while(rs.next()){
						names.add(rs.getString(2));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		for(String n:names){
			System.out.println(n);
		}
	}
}
