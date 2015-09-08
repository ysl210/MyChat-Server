/**
 * ��������ĳ���ͻ��˵�ͨ���߳�
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
				//Start.q.offer("���ͷ���"+m.getSender()+"  ��Ϣ���ͣ�"+m.getType());
				//�Դӿͻ���ȡ�õ���Ϣ���������жϣ�����Ӧ�Ĵ���
				if(m.getType().equals(MCMessageType.COM_MES)){//�������ͨ��Ϣ��
					DoWhatAndSendMes.sendMes(m);
				}else if(m.getType().equals(MCMessageType.GET_ONLINE_FRIENDS)){//��������б�
					DoWhatAndSendMes.sendUserList(m);
				}else if(m.getType().equals(MCMessageType.HELLO)){//����
					DoWhatAndSendMes.sendHello(m);
				}
				else if(m.getType().equals(MCMessageType.REQ_CONNECT)){//��������
					DoWhatAndSendMes.reqConnect(m);
				}
				else if(m.getType().equals(MCMessageType.REQ_DISCONNECT)){//�Ͽ�����
					DoWhatAndSendMes.reqDisconnect(m);
				}
				else if(m.getType().equals(MCMessageType.ALLOW_CONNECT)){//��������
					DoWhatAndSendMes.allowConnect(m);
				}
				else if(m.getType().equals(MCMessageType.REFUSE_CONNECT)){//�ܾ�����
					DoWhatAndSendMes.refuseConnect(m);
				}
				else if(m.getType().equals(MCMessageType.SEND_FILE)){//�����ļ�
					DoWhatAndSendMes.sendFile(m);
				}
				else if(m.getType().equals(MCMessageType.SEND_PIC)){//����ͼƬ
					DoWhatAndSendMes.sendPic(m);
				}
				else if(m.getType().equals(MCMessageType.SEND_REC)){//��������
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
