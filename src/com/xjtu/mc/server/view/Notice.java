package com.xjtu.mc.server.view;

import java.awt.Component;

import javax.swing.JOptionPane;

public class Notice {
	public static void display(Component c,String message){
		JOptionPane.showMessageDialog(c,message, "系统消息",JOptionPane.INFORMATION_MESSAGE);
	}
}
