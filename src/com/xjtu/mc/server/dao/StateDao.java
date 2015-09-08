package com.xjtu.mc.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.xjtu.mc.server.view.Add;
import com.xjtu.mc.server.view.Notice;

public class StateDao {
	private Connection conn=null;
	private PreparedStatement ps=null;
	private ResultSet rs=null;
	private String sql="";
	public StateDao(){
		conn = DBUtil.getDBUtil().getConnection();
	}
	public String getOnLineUsersInfo(){
		String res="";
		try {
			//sql = "select * from mc_state where isonline=1";
			sql = "select * from mc_state";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				res+=rs.getString("account")+"   "+String.valueOf(rs.getInt("isonline"))+"   "+rs.getString("peer")
						+"   "+String.valueOf(rs.getInt("mask"))+"\n\r";	
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	public String getOnLineUsers(String account){
		String res="";
		try {
			sql = "select account from mc_state where isonline=1 and account<>?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, account);
			rs = ps.executeQuery();
			while(rs.next()){
				res+=rs.getString("account")+" ";	
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	public void delete(String account){
		try {
			sql = "delete from mc_state where account=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, account);
			ps.executeQuery();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Object getUserStateInfo(String account,int type){
		Object res = null;
		try {
			if(type==1){
				sql = "select isonline from mc_state where account=?";//×´Ì¬²éÑ¯
			}
			else if(type==2){
				sql = "select peer from mc_state where account=?";//¶Ô¶Ë²éÑ¯
			}
			else if(type==3){
				sql = "select mask from mc_state where account=?";//Ëæ»úÂë²éÑ¯
			}
			else{
				
			}
			ps = conn.prepareStatement(sql);
			ps.setString(1, account);
			rs = ps.executeQuery();
			while(rs.next()){
				if(type==1){
					res=rs.getInt("isonline");
				}
				if(type==2){
					res=rs.getString("peer");
				}
				if(type==3){
					res=rs.getInt("mask");
				}
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	public boolean setIsOnline(String account, int i,int mask) {
		try {
			sql = "update mc_state set isonline=?,peer=?,mask=? where account=?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, i);
			ps.setString(2, "000");
			ps.setInt(3, mask);
			ps.setString(4, account);
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
	public boolean setStateObject(String account,int type,Object obj){
		try {
			if(type==1){
				sql = "update mc_state set isonline=? where account=?";
			}
			if(type==2){
				sql = "update mc_state set peer=? where account=?";
			}
			if(type==3){
				sql = "update mc_state set mask=? where account=?";
			}
			ps = conn.prepareStatement(sql);
			ps.setObject(1, obj);
			ps.setString(2, account);
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
	public void insertState(String account){
		try {
			sql = "insert into mc_state values(?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, account);
			ps.setInt(2, 0);
			ps.setString(3, "000");
			ps.setInt(4, 0);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void reset(){
		try {
			sql = "update mc_state set isonline=0,peer='000',mask=0";
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
