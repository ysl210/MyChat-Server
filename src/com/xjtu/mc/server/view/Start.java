package com.xjtu.mc.server.view;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.xjtu.mc.common.User;
import com.xjtu.mc.server.dao.DBUtil;
import com.xjtu.mc.server.model.DoWhatAndSendMes;
import com.xjtu.mc.server.model.MCServer;


public class Start extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton b1,b2,b3,b4,b5,b6,b7,b8,b9;
	private JScrollPane jp1,jp2,jp3;
	private JScrollBar jb1,jb2,jb3;
	private JTextArea t1,t2,t3;
	private Add add=null;
	private Delete del=null;
	private Modify mo=null;
	private MCServer mcs = null;
	private DBInfo di=null;
	public static int systemstate=0; 
	public static String state="";
	private int count;
	public static Queue<String> q=new LinkedList<String>();
	public Start() {
		File f= new File("db.ini");
		if(f.exists()){
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String dbUser="";
			String dbPwd = "";
			String dbIP = "";
			String dbPort = "";
			dbUser=br.readLine();
			dbPwd=br.readLine();
			dbIP=br.readLine();
			dbPort=br.readLine();
			if(dbUser!=null){
				DBUtil.user=dbUser;
			}
			if(dbPwd!=null){
				DBUtil.pwd=dbPwd;
			}
			if(dbIP!=null){
				DBUtil.ip=dbIP;
			}
			if(dbPort!=null){
				DBUtil.port=dbPort;
			}
			br.close();
			fr.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}}
		else{
			try {
				f.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		this.setLayout(null);
		b1=new JButton("DB设置");
		b1.setSize(100, 30);
		b1.setLocation(40, 30);
		b2=new JButton("DB复位");
		b2.setSize(100, 30);
		b2.setLocation(250, 30);
		b3=new JButton("用户查询");
		b3.setSize(100, 30);
		b3.setLocation(440, 30);
		b4=new JButton("增加用户");
		b4.setSize(100, 30);
		b4.setLocation(440, 110);
		b5=new JButton("修改密码");
		b5.setSize(100, 30);
		b5.setLocation(440, 190);
		b6=new JButton("删除用户");
		b6.setSize(100, 30);
		b6.setLocation(440, 270);
		b7=new JButton("退出");
		b7.setSize(100, 30);
		b7.setLocation(440, 350);
		b8=new JButton("启动");
		b8.setSize(100, 30);
		b8.setLocation(40, 350);
		b9=new JButton("停止");
		b9.setSize(100, 30);
		b9.setLocation(250, 350);
		t1 = new JTextArea();// 创建输入文本框，最多显示10个字符
		t1.setEditable(false);
		//t1.setSize(229, 169);
		//t1.setLocation(40, 80);
		t1.setBackground(Color.WHITE);
		t1.setAutoscrolls(true);
		//t1.setLineWrap(true);
		jp1 = new JScrollPane(t1);
		jb1 = jp1.getVerticalScrollBar();
		jp1.getHorizontalScrollBar();
		jp1.setSize(239, 149);
		jp1.setLocation(40, 80);
		t2 = new JTextArea();// 创建输入文本框，最多显示10个字符
		t2.setEditable(false);
		//t2.setSize(229, 90);
		//t2.setLocation(40, 250);
		t2.setBackground(Color.WHITE);
		t2.setAutoscrolls(true);
		t2.setLineWrap(true);
		jp2 = new JScrollPane(t2);
		jb2 = jp2.getVerticalScrollBar();
		jp2.getHorizontalScrollBar();
		jp2.setSize(239, 110);
		jp2.setLocation(40, 230);
		t3 = new JTextArea();// 创建输入文本框，最多显示10个字符
		t3.setEditable(false);
		//t3.setSize(150, 260);
		//t3.setLocation(270, 80);
		t3.setBackground(Color.WHITE);
		t3.setAutoscrolls(true);
		t3.setLineWrap(true);
		jp3 = new JScrollPane(t3);
		jb3 = jp3.getVerticalScrollBar();
		jp3.getHorizontalScrollBar();
		jp3.setSize(140, 260);
		jp3.setLocation(280, 80);
		//t1.set
		this.add(b1); // 将组件添加到窗口上
		//this.add(b2);
		this.add(b3);
		this.add(b4);
		this.add(b5);
		this.add(b6);
		this.add(b7);
		this.add(b8);
		this.add(b9);
		this.add(jp1);
		this.add(jp2);
		this.add(jp3);
		b1.addActionListener(new DBListener());// 为文本框注册ActionEvent事件监听器
		b2.addActionListener(new ResetListener());
		b3.addActionListener(new QueryListener());
		b4.addActionListener(new AddListener());
		b5.addActionListener(new ModifyListener());
		b6.addActionListener(new DelListener());
		b7.addActionListener(this);
		b8.addActionListener(new StartListener());
		b9.addActionListener(new StopListener());
		// 为窗口注册窗口事件监听程序，监听器以匿名类的形式进行
		this.addWindowListener(new WindowAdapter() {// 匿名类开始
					public void windowClosing(WindowEvent e){
						systemstate=0;
						System.exit(0);
					} // 窗口关闭
				});// 匿名类结束
		this.setTitle("手机即时保密通信服务端");//设置窗体标题
		this.setSize(600, 480);//设置窗口大小
		this.setLocation(250,120);
		this.setVisible(true);//设置窗体的可见性
		this.setResizable(false);
		t1.setText("用户              状态  对端           随机码\n\r");
		t2.setText("点击启动服务器！\n\r");
		mcs = new MCServer();
		mcs.getServerSocket();
		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	}
	
	private class DBListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(di==null){
				di=new DBInfo();
			}
			else if(di.isShowing()){
				di.dispose();
			}
			else{
				di.show();
			}
		}
		
	} 
	private class QueryListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if("".equals(DBUtil.user)||"".equals(DBUtil.pwd)){
				Notice.display(getContentPane(), "DB账号缺失！");
			}
			else{
				String s="";
				List<User> l = DoWhatAndSendMes.udao.getAllUsers();
				for(User u:l){
					s=s+"账号："+u.getAccount()+"\n\r密码："+u.getPwd()+"\n\r\n\r";
				}
				t3.setText(s);
				//jb3.setValue(jb3.getMinimum());
			}
		}
	}
	private class ResetListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(systemstate==1){
				Notice.display(getContentPane(), "请先停止系统！");
			}
			else{
				reset();
			}
		}
	}
	private class AddListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(add==null){
				add=new Add();
			}
			else if(add.isShowing()){
				add.dispose();
			}
			else{
				add.show();
			}
		}
	}
	private class ModifyListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(mo==null){
				mo=new Modify();
			}
			else if(mo.isShowing()){
				mo.dispose();
			}
			else{
				mo.show();
			}
		}
	}
	private class DelListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(del==null){
				del=new Delete();
			}
			else if(del.isShowing()){
				del.dispose();
			}
			else{
				del.show();
			}
		}
	}
	private class StartListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if("".equals(DBUtil.user)||"".equals(DBUtil.pwd)){
				Notice.display(getContentPane(), "DB账号缺失！");
			}
			else if(systemstate==0){
				Start.this.setTitle("手机即时保密通信服务端(正在运行)");
				systemstate=1;
				new Thread(new Runnable() {
				
					@Override
					public void run() {
						mcs.start();
					}
				}).start();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						while(systemstate==1){
							t1.setText("用户              状态  对端           随机码\n\r"+state);
							if(!q.isEmpty()){
								if(t2.getText().length()>200){
									String s = t2.getText().substring(100);
									t2.setText(s+"\n\r"+q.poll()+"\n\r");
								}
								else{
									t2.append(q.poll()+"\n\r");
								}
								jb2.setValue(jb2.getMaximum());
								jb2.setValue(jb2.getMaximum());
								jb2.setValue(jb2.getMaximum());
								//jb2.setValue(jb2.getMaximum());
							}
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}).start();
			}
			else{
				Notice.display(getContentPane(), "服务器已启动!");
			}
			
			
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(systemstate==1){
			Notice.display(getContentPane(), "请先停止服务器！");
		}
		else{
		if(add!=null){
			add.dispose();
		}
		if(del!=null){
			del.dispose();
		}
		if(mo!=null){
			mo.dispose();
		}
		this.dispose();
		System.exit(0);
		}
		
	}
	private void reset() {
		if("".equals(DBUtil.user)||"".equals(DBUtil.pwd)){
			Notice.display(getContentPane(), "DB账号缺失！");
		}
		else{
			DoWhatAndSendMes.sdao.reset();
		}
	}
	private class StopListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(systemstate==1){
				Start.this.setTitle("手机即时保密通信服务端(已停止)");
				t2.append("服务器已停止 at "+new Date()+"\n\r重启间隔20s！\n\r");
				count=20;
				systemstate=0;
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						b8.setEnabled(false);
						while(count>=0){
							try {
								b8.setText("启动("+count+")");
								Thread.sleep(1000);
								count=count-1;
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						b8.setText("启动");
						b8.setEnabled(true);
					}
				}).start();
			}
			else{
				Notice.display(getContentPane(), "服务器已停止！");
			}
		}
		
	}
	
}
