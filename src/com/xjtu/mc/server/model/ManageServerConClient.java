/**
 * ����ͻ������ӵ���
 */
package com.xjtu.mc.server.model;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.xjtu.mc.common.MCMessage;
import com.xjtu.mc.common.MCMessageType;
import com.xjtu.mc.server.dao.DBUtil;
import com.xjtu.mc.server.dao.StateDao;
import com.xjtu.mc.server.view.Start;


public class ManageServerConClient {
	public static HashMap<String, ServerConClientThread> hm=new HashMap<String,ServerConClientThread>();
	public static HashMap<String, Long> ctime=new HashMap<String,Long>();
	public static String users="";
	//���һ���ͻ���ͨ���߳�
	public static void addClientThread(String account, ServerConClientThread cc){
		hm.put(account,cc);
	}
	//�õ�һ���ͻ���ͨ���߳�
	public static ServerConClientThread getClientThread(String i){
		return (ServerConClientThread)hm.get(i);
	}
	public static void delClientThread(String i,int type){
		if(hm.get(i)!=null){
			hm.get(i).flag=false;
			hm.remove(i);
			if(type==1){
				Start.q.offer("["+i+"]���ߣ�at  "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			}
			else{
				Start.q.offer("["+i+"]�ѵ��ߣ�at  "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			}
		}
	}
	//���ص�ǰ�����˵����
	public static List getAllOnLineUserid(){
		List list=new ArrayList();
		//ʹ�õ��������
		Iterator it=hm.keySet().iterator();
		while(it.hasNext()){
			list.add((String) it.next());
		}
		return list;
	}
	
	public static boolean isOnline(String a){
		List list=new ArrayList();
		//ʹ�õ��������
		Iterator it=hm.keySet().iterator();
		while(it.hasNext()){
			String account=(String) it.next();
			if(a.equals(a)){
				return true;
			}
		}
		return false;
	}
	//���һ���ͻ��˼�ʱ
		public static void addClientTime(String account, long ct){
			ctime.put(account,ct);
		}
		//�õ�һ���ͻ���ͨ���߳�
		public static long getClientTime(String i){
			return (long)ctime.get(i);
		}
		public static void delClientTime(String i){
			ctime.remove(i);
		}
		public static void checkTime() {
			List list=new ArrayList();
			List delList = new ArrayList();//����װ��Ҫɾ����Ԫ��
			//ʹ�õ��������
			Iterator it=ctime.keySet().iterator();
			while(it.hasNext()){
				
				String account=(String) it.next();
				
				long l = new Date().getTime()-(long)ctime.get(account);
				if(l>=40000||"".equals(DoWhatAndSendMes.udao.getUser(account).getAccount())){
					
					delList.add(account);
				}
			}
			for(Object account:delList){
				
				DoWhatAndSendMes.sdao.setIsOnline((String)account, 0, 0);
				ctime.remove(account);
				delClientThread((String)account,2);
			}
			List l = getAllOnLineUserid();
			String s="";
			for(Object i:l){
				if((!"000".equals(s=(String)DoWhatAndSendMes.sdao.getUserStateInfo((String)i, 2)))){
						if((int)DoWhatAndSendMes.sdao.getUserStateInfo(s, 1)==0){
							DoWhatAndSendMes.sdao.setStateObject((String)i, 2, "000");
							ServerConClientThread scc=ManageServerConClient.getClientThread((String)i);
							try {
								ObjectOutputStream oos=new ObjectOutputStream(scc.s.getOutputStream());
								MCMessage ms=new MCMessage();
								ms.setType(MCMessageType.REQ_DISCONNECT);
								oos.writeObject(ms);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
				}
				//System.out.println((String)i);
			}
		}
}
