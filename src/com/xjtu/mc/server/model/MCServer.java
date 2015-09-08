package com.xjtu.mc.server.model;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.xjtu.mc.common.MCMessage;
import com.xjtu.mc.common.MCMessageType;
import com.xjtu.mc.common.User;
import com.xjtu.mc.server.dao.StateDao;
import com.xjtu.mc.server.dao.UserDao;
import com.xjtu.mc.server.view.Start;

public class MCServer{
	
	private ServerSocket ss = null;
	private Socket s=null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	private UserDao udao;
	private StateDao sdao;
	private int port=5469;
	
	public void getServerSocket(){
		try {
			ss=new ServerSocket(port);
		} catch (IOException e1) {
			e1.printStackTrace();
			Start.q.offer("端口"+port+"被占用！");
		}
	}
	public void start(){
		udao=new UserDao();
		sdao=new StateDao();
		Start.q.offer("服务器已启动 at "+new Date());
		ManageServerConClient.users = sdao.getOnLineUsers("no");
		String[] accounts = ManageServerConClient.users.split(" ");
		for(int i=0;i<accounts.length;i++){
			//ManageServerConClient.addClientTime(accounts[i], new Date().getTime());
			sdao.setIsOnline(accounts[i], 0, 0);
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				StateDao sdao = new StateDao();
				while(Start.systemstate==1){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					ManageServerConClient.checkTime();
					ManageServerConClient.users = sdao.getOnLineUsers("no");
					Start.state=sdao.getOnLineUsersInfo();
				}
			}
		}).start();
		while(Start.systemstate==1){
			try {
				s=ss.accept();
			//接受客户端发来的信息
			ois=new ObjectInputStream(s.getInputStream());
			Object o = ois.readObject();
			User u=null;
			if(o instanceof User){
				u=(User)o;
			}
			else{
				u=new User();
				u.setOperation("");
			}
			//User u = (User)ois.readObject();
			MCMessage m=new MCMessage();
			oos=new ObjectOutputStream(s.getOutputStream());
	        if("login".equals(u.getOperation())){ //登录
	        	String account=u.getAccount();
	        	boolean b=udao.login(account, u.getPwd());//连接数据库验证用户
				if(b){
					if(Integer.parseInt(String.valueOf(sdao.getUserStateInfo(u.getAccount(),1)))!=1){
						Start.q.offer("["+account+"]上线！at  "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						//更改数据库用户状态
						int mask = ((int)(Math.random()*899999))+100000;
						sdao.setIsOnline(account, 1, mask);
						//返回一个成功登陆的信息包，并附带个人信息
						m.setType(MCMessageType.SUCCESS);
						m.setContent((mask+"_"+sdao.getOnLineUsers(u.getAccount())).getBytes());
						oos.writeObject(m);
						ServerConClientThread cct=new ServerConClientThread(s);//单开一个线程，让该线程与该客户端保持连接
						ManageServerConClient.addClientThread(u.getAccount(),cct);
						ManageServerConClient.addClientTime(u.getAccount(), new Date().getTime());
						cct.start();//启动与该客户端通信的线程
					}
					else{
						m.setType(MCMessageType.ALREADY_LOGIN);
						oos.writeObject(m);
					}
				}else{
					m.setType(MCMessageType.LOGIN_FAIL);
					oos.writeObject(m);
				}
	        }else if(u.getOperation().equals("logout")){
	        	
	        	/*m.setType(MCMessageType.SUCCESS);
				oos.writeObject(m);*/
				sdao.setIsOnline(u.getAccount(), 0, 0);
				ManageServerConClient.delClientThread(u.getAccount(),1);
	        	ManageServerConClient.delClientTime(u.getAccount());
	        }else if(u.getOperation().equals("register")){
	        	if(udao.register(u)){
	        		//返回一个注册成功的信息包
					m.setType(MCMessageType.SUCCESS);
					oos.writeObject(m);
	        	}
	        	else{
					m.setType(MCMessageType.FAIL);
					oos.writeObject(m);
				}
	        }
	        else{
	        }
		}catch (Exception e) {
			e.printStackTrace();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//Start.systemstate=0;
		}	
	} 
	//Start.q.offer("服务器已停止 at "+new Date());
	udao.close();
    sdao.close();
   try {
    	ois.close();
    	oos.close();
    	s.close();
		//ss.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}
