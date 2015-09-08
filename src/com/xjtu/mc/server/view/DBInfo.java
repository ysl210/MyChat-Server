package com.xjtu.mc.server.view;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.xjtu.mc.server.dao.DBUtil;


public class DBInfo extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private JLabel jl1;
	private JLabel jl2;
	private JLabel jl3;
	private JLabel jl4;
	private TextField tf1;
	private TextField tf2;
	private TextField tf3;
	private TextField tf4;
	private JButton jb1;
	private JButton jb2;
	private String user = "";
	private String pwd = "";
	private String port = "";
	private String ip="";
	private File file=null; 
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		this.dispose();
	}
	public DBInfo(){
		file = new File("db.ini"); 
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			try {
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				user=br.readLine();
				pwd=br.readLine();
				ip=br.readLine();
				port=br.readLine();
				if(user==null){
					user="";
				}
				if(pwd==null){
					pwd="";
				}
				if(ip==null||"".equals(ip)){
					ip="localhost";
				}
				if(port==null||"".equals(port)){
					port="1521";
				}
				DBUtil.user=user;
				DBUtil.pwd = pwd;
				DBUtil.ip = ip;
				DBUtil.port = port;
				br.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		GridLayout gl=new GridLayout();
		gl.setColumns(2);
		gl.setRows(5);
		this.setLayout(gl);
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.setTitle("手机即时通信服务端");//设置窗体标题
		this.setVisible(true);//设置窗体的可见性
		this.setLocation(860, 130);
		this.setSize(240, 200);
		Font f = new Font("宋体", 0, 20);
		jl1=new JLabel("数据库账号：");
		jl2=new JLabel("数据库密码：");
		tf1=new TextField();
		tf1.setText(user);
		tf1.setFont(f);
		tf2=new TextField();
		tf2.setText(pwd);
		tf2.setFont(f);
		jl3=new JLabel("数据库IP：");
		jl4=new JLabel("数据库端口：");
		tf3=new TextField();
		tf3.setText(ip);
		tf3.setFont(f);
		tf4=new TextField();
		tf4.setText(port);
		tf4.setFont(f);
		jb1=new JButton("确定");
		jb2=new JButton("取消");
		this.add(jl1);
		this.add(tf1);
		this.add(jl2);
		this.add(tf2);
		this.add(jl3);
		this.add(tf3);
		this.add(jl4);
		this.add(tf4);
		this.add(jb1);
		this.add(jb2);
		jb1.addActionListener(new ConfirmListener());
		jb2.addActionListener(this);
		this.setTitle("数据库账号");
	}
	private class ConfirmListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			user = tf1.getText().trim();
			pwd = tf2.getText().trim();
			ip = tf3.getText().trim();
			port = tf4.getText().trim();
			if("".equals(user)||"".equals(pwd)){
				Notice.display(DBInfo.this.getContentPane(), "账号或密码不能为空！");
			}
			else if("".equals(ip)||"".equals(port)){
				Notice.display(DBInfo.this.getContentPane(), "IP或端口不能为空！");
			}
			else if(Start.systemstate==0){
				
				DBUtil.user=user;
				DBUtil.pwd=pwd;
				DBUtil.ip=ip;
				DBUtil.port=port;
				try {
					FileWriter fw = new FileWriter(file);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(user+"\r\n");
					bw.append(pwd+"\r\n");
					bw.append(ip+"\r\n");
					bw.append(port);
					bw.close();
					fw.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				Notice.display(DBInfo.this.getContentPane(), "数据库账号设置成功!\n\r注：本软件只支持oracle数据库");
				DBInfo.this.dispose();
			}
			else{
				Notice.display(DBInfo.this.getContentPane(), "请先停止服务器！");
			}
		}
		
	}
}
