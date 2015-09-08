package com.xjtu.mc.server.model;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

import com.xjtu.mc.common.MCMessage;
import com.xjtu.mc.common.MCMessageType;
import com.xjtu.mc.server.dao.DBUtil;
import com.xjtu.mc.server.dao.StateDao;
import com.xjtu.mc.server.dao.UserDao;

public class DoWhatAndSendMes {
	
	public static StateDao sdao=new StateDao();
	public static UserDao udao=new UserDao();
	public static void sendMes(MCMessage m){
		try{
			//取得接收人的通信线程
			ServerConClientThread scc=ManageServerConClient.getClientThread(m.getReceiver());
			ObjectOutputStream oos=new ObjectOutputStream(scc.s.getOutputStream());
			//向接收人发送消息
			oos.writeObject(m);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void sendUserList(MCMessage m){
		try{
			//操作数据库，返回好友列表
			String res=sdao.getOnLineUsers(m.getSender());
			//发送好友列表到客户端
			ServerConClientThread scc=ManageServerConClient.getClientThread(m.getSender());
			ObjectOutputStream oos=new ObjectOutputStream(scc.s.getOutputStream());
			MCMessage ms=new MCMessage();
			ms.setType(MCMessageType.RET_ONLINE_FRIENDS);
			ms.setContent(res.getBytes());
			oos.writeObject(ms);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void sendHello(MCMessage m) {
		ManageServerConClient.addClientTime(m.getSender(), new Date().getTime());
		ServerConClientThread scc=ManageServerConClient.getClientThread(m.getSender());
		ObjectOutputStream oos = null;
		try {
			//发送好友列表到客户端
			oos=new ObjectOutputStream(scc.s.getOutputStream());
			MCMessage ms=new MCMessage();
			ms.setType(MCMessageType.HELLO_ONLINE_FRIENDS);
			ms.setContent(ManageServerConClient.users.getBytes());
			oos.writeObject(ms);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void reqConnect(MCMessage m) {
		String peer=(String)sdao.getUserStateInfo(m.getReceiver(), 2);
		int state=Integer.parseInt(String.valueOf(sdao.getUserStateInfo(m.getReceiver(), 1)));
		ServerConClientThread scc=null;
		ObjectOutputStream oos = null;
		MCMessage ms=new MCMessage();
		if("000".equals(peer)&&state==1){
			
			scc=ManageServerConClient.getClientThread(m.getReceiver());
		try {
			
			oos=new ObjectOutputStream(scc.s.getOutputStream());
			/*ms.setType(MCMessageType.REQ_CONNECT);
			ms.setContent(m.getContent());
			ms.setSender(m.getSender());
			ms.setReceiver(m.getReceiver());*/
			oos.writeObject(m);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
		else{
			try {
				scc=ManageServerConClient.getClientThread(m.getSender());
				oos=new ObjectOutputStream(scc.s.getOutputStream());
				ms.setType(MCMessageType.REFUSE_CONNECT);
				oos.writeObject(ms);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void reqDisconnect(MCMessage m) {
		ServerConClientThread scc1=ManageServerConClient.getClientThread(m.getReceiver());
		ServerConClientThread scc2=ManageServerConClient.getClientThread(m.getSender());
		ObjectOutputStream oos1 = null;
		ObjectOutputStream oos2 = null;
		try {
			oos1=new ObjectOutputStream(scc1.s.getOutputStream());
			MCMessage ms1=new MCMessage();
			ms1.setType(MCMessageType.REQ_DISCONNECT);
			oos1.writeObject(ms1);
			
			oos2=new ObjectOutputStream(scc2.s.getOutputStream());
			MCMessage ms2=new MCMessage();
			ms2.setType(MCMessageType.REQ_DISCONNECT);
			oos2.writeObject(ms2);
			sdao.setStateObject(m.getSender(), 2, "000");
			sdao.setStateObject(m.getReceiver(), 2, "000");
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void allowConnect(MCMessage m) {
		String peer1=(String)sdao.getUserStateInfo(m.getReceiver(), 2);
		int state1=Integer.parseInt(String.valueOf(sdao.getUserStateInfo(m.getReceiver(), 1)));
		String peer2=(String)sdao.getUserStateInfo(m.getSender(), 2);
		int state2=Integer.parseInt(String.valueOf(sdao.getUserStateInfo(m.getSender(), 1)));
		ServerConClientThread scc1=ManageServerConClient.getClientThread(m.getSender());
		ServerConClientThread scc2=ManageServerConClient.getClientThread(m.getReceiver());
		ObjectOutputStream oos1 = null;
		MCMessage ms1=new MCMessage();
		ObjectOutputStream oos2 = null;
		MCMessage ms2=new MCMessage();
		if("000".equals(peer1)&&state1==1&&"000".equals(peer2)&&state2==1){
			
		sdao.setStateObject(m.getSender(), 2, m.getReceiver());
		sdao.setStateObject(m.getReceiver(), 2, m.getSender());
		//int mask1=(int) sdao.getUserStateInfo(m.getReceiver(), 3);
		//int mask2=(int) sdao.getUserStateInfo(m.getSender(), 3);
		try {
			oos1=new ObjectOutputStream(scc1.s.getOutputStream());
			ms1.setType(MCMessageType.CONFIRM_CONNECT);
			//ms1.setContent(String.valueOf(mask1).getBytes());
			ms1.setReceiver(m.getSender());
			ms1.setSender(m.getReceiver());
			oos1.writeObject(ms1);
			oos2=new ObjectOutputStream(scc2.s.getOutputStream());
			ms2.setType(MCMessageType.CONFIRM_CONNECT);
			//ms2.setContent(String.valueOf(mask2).getBytes());
			ms2.setReceiver(m.getReceiver());
			ms2.setSender(m.getSender());
			ms2.setContent(m.getContent());
			ms2.setDeviceId(m.getDeviceId());
			ms2.setContent(m.getContent());
			oos2.writeObject(ms2);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
		else{
			try {
				oos1=new ObjectOutputStream(scc1.s.getOutputStream());
				ms1.setType(MCMessageType.REFUSE_CONNECT);
				oos1.writeObject(ms1);
				oos2=new ObjectOutputStream(scc2.s.getOutputStream());
				ms2.setType(MCMessageType.REFUSE_CONNECT);
				oos2.writeObject(ms2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void refuseConnect(MCMessage m) {
		try {
			ServerConClientThread scc=ManageServerConClient.getClientThread(m.getReceiver());
			ObjectOutputStream oos=new ObjectOutputStream(scc.s.getOutputStream());
			MCMessage ms=new MCMessage();
			ms.setType(MCMessageType.REFUSE_CONNECT);
			oos.writeObject(ms);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void sendFile(MCMessage m) {
		new FileThread(ManageServerConClient.getClientThread(m.getReceiver()).s,m).start();
	}

	public static void sendPic(MCMessage m) {
		new FileThread(ManageServerConClient.getClientThread(m.getReceiver()).s,m).start();
	}

	public static void sendRec(MCMessage m) {
		new FileThread(ManageServerConClient.getClientThread(m.getReceiver()).s,m).start();
	}
}
