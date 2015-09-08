/**
 * 服务器和某个客户端的通信线程
 */
package com.xjtu.mc.server.model;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import com.xjtu.mc.common.MCMessage;
import com.xjtu.mc.common.MCMessageType;
import com.xjtu.mc.server.view.Start;

public class ServerConClientThread extends Thread {
	Socket s;
	boolean flag=true; 
	public ServerConClientThread(Socket s){
		this.s=s;
	}

	public void run() {	
		while(Start.systemstate==1&&flag){
			ObjectInputStream ois = null;
			MCMessage m = null;
			try {
				ois=new ObjectInputStream(s.getInputStream());
				m=(MCMessage) ois.readObject();
				//Start.q.offer("发送方："+m.getSender()+"  消息类型："+m.getType());
				//对从客户端取得的消息进行类型判断，做相应的处理
				if(m.getType().equals(MCMessageType.COM_MES)){//如果是普通消息包
					DoWhatAndSendMes.sendMes(m);
				}else if(m.getType().equals(MCMessageType.GET_ONLINE_FRIENDS)){//请求好友列表
					DoWhatAndSendMes.sendUserList(m);
				}else if(m.getType().equals(MCMessageType.HELLO)){//心跳
					DoWhatAndSendMes.sendHello(m);
				}
				else if(m.getType().equals(MCMessageType.REQ_CONNECT)){//请求连接
					DoWhatAndSendMes.reqConnect(m);
				}
				else if(m.getType().equals(MCMessageType.REQ_DISCONNECT)){//断开连接
					DoWhatAndSendMes.reqDisconnect(m);
				}
				else if(m.getType().equals(MCMessageType.ALLOW_CONNECT)){//允许连接
					DoWhatAndSendMes.allowConnect(m);
				}
				else if(m.getType().equals(MCMessageType.REFUSE_CONNECT)){//拒绝连接
					DoWhatAndSendMes.refuseConnect(m);
				}
				else if(m.getType().equals(MCMessageType.SEND_FILE)){//发送文件
					DoWhatAndSendMes.sendFile(m);
				}
				else if(m.getType().equals(MCMessageType.SEND_PIC)){//发送图片
					DoWhatAndSendMes.sendPic(m);
				}
				else if(m.getType().equals(MCMessageType.SEND_REC)){//发送语音
					DoWhatAndSendMes.sendRec(m);
				}
			
			}catch(EOFException e){
				//e.printStackTrace();
				break;
			}catch(NullPointerException e){
				//break;
				e.printStackTrace();
			}catch(SocketException e){
				
				e.printStackTrace();
				//break;
			}catch (Exception e) {
				try {
					//if(ois!=null){
						ois.close();
					//}
					//if(s!=null){
						s.close();
					//}
				} catch (Exception e1) {	
					e.printStackTrace();
					//System.out.println("abc");
				}
				e.printStackTrace();
			}
		}
		try {
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
