package com.xjtu.mc.server.dao;
import java.sql.*;

public class DBUtil {
	private static DBUtil dbutil=null;
	private Connection conn=null;
	private String driver = "oracle.jdbc.driver.OracleDriver";
	
	public static String user = "";
	public static String pwd = "";
	public static String port="1521";
	public static String ip="localhost";
	private String url = "jdbc:oracle:thin:@"+ip+":"+port+":orcl";
	private PreparedStatement ps=null;
	private ResultSet rs=null;
	private String sql="";
	
	private DBUtil(){
		getConnection();
		createTable();
	}
	public void createTable(){
		sql = "SELECT * FROM USER_TABLES where table_name='MC_USER'";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (!rs.next()) {
				sql="create table mc_user(account varchar2(32) primary key,	pwd varchar2(32))";
				try {
					rs.close();
					ps.close();
					ps = conn.prepareStatement(sql);
					ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		sql = "SELECT * FROM USER_TABLES where table_name='MC_STATE'";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (!rs.next()) {
				sql="create table mc_state(account varchar2(32) primary key,isonline number,peer varchar2(32),mask number,foreign key(account) references mc_user(account))";
				try {
					rs.close();
					ps.close();
					ps = conn.prepareStatement(sql);
					ps.executeUpdate();
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/*try {
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
	public synchronized static DBUtil getDBUtil(){
		if(dbutil==null){
			dbutil=new DBUtil();
		}
		
		return dbutil;
	}
	
	public Connection getConnection(){
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pwd);
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
