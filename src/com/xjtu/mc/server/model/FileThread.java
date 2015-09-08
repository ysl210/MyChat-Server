package com.xjtu.mc.server.model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.xjtu.mc.common.MCMessage;

public class FileThread extends Thread{
	private Socket s;
	private MCMessage m;
	public FileThread(Socket s, MCMessage m){
		this.s=s;
		this.m=m;
	}
	@Override
	public void run() {
		try {
			ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(m);
		} catch (IOException e) {
			try {
				s.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
