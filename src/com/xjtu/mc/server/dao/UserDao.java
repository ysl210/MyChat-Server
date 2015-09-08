package com.xjtu.mc.server.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xjtu.mc.common.User;

public class UserDao {
	private Connection conn=null;
	private PreparedStatement ps=null;
	private ResultSet rs=null;
	private String sql="";
	public UserDao(){
		conn = DBUtil.getDBUtil().getConnection();
	}
	
	public void delete(String account){
		try {
			sql = "delete from mc_user where account=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, account);
			ps.executeQuery();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean login(String account, String password) {
		try {
			sql = "select * from mc_user where account=? and pwd=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, account);
			ps.setString(2, password);
			rs = ps.executeQuery();
			if (rs != null && rs.next() == true) {
				return true;
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean register(User u) {
		try {
			sql = "insert into mc_user values(?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, u.getAccount());
			ps.setString(2, u.getPwd());
			int r = ps.executeUpdate();
			if (r > 0) {
				return true;
			}
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public void setPwd(User u){
		try {
			sql = "update mc_user set pwd=? where account=?";
			ps = conn.prepareStatement(sql);
			ps.setString(2, u.getAccount());
			ps.setString(1, u.getPwd());
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public User getUser(String account) {
		User user=new User();
		try {
			sql = "select * FROM mc_user where account=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, account);
			rs = ps.executeQuery();
			while(rs.next()){
				user.setAccount(rs.getString("account"));
				user.setPwd(rs.getString("pwd"));
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	public List<User> getAllUsers(){
		List<User> l = new ArrayList<User>();
		try {
			sql = "select * FROM mc_user";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				User user=new User();
				user.setAccount(rs.getString("account"));
				user.setPwd(rs.getString("pwd"));
				l.add(user);
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return l;
	}
	public void close(){
		try {
			if(rs!=null){
				rs.close();
			}
			if(ps!=null){
				ps.close();
			}
			if(conn!=null){
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}