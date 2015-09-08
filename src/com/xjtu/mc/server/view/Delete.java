package com.xjtu.mc.server.view;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.xjtu.mc.server.dao.DBUtil;
import com.xjtu.mc.server.model.DoWhatAndSendMes;


public class Delete extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel jl1;
	private TextField tf1;
	private JButton jb1;
	private JButton jb2;
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		this.dispose();
	}
	public Delete(){
		GridLayout gl=new GridLayout();
		gl.setColumns(2);
		gl.setRows(2);
		this.setLayout(gl);
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.setTitle("�ֻ���ʱͨ�ŷ����");//���ô������
		this.setSize(300, 100);
		this.setVisible(true);//���ô���Ŀɼ���
		this.setLocation(860, 350);
		jl1=new JLabel("�û�����");
		tf1=new TextField();
		this.setSize(240, 80);
		Font f = new Font("����", 0, 20);
		tf1.setFont(f);
		jb1=new JButton("ȷ��");
		jb2=new JButton("ȡ��");
		this.add(jl1);
		this.add(tf1);
		this.add(jb1);
		this.add(jb2);
		jb1.addActionListener(new ConfirmListener());
		jb2.addActionListener(this);
		this.setTitle("�û�ɾ��");
	}
	private class ConfirmListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String user = tf1.getText().trim();
			if("".equals(DBUtil.user)||"".equals(DBUtil.pwd)){
				Notice.display(Delete.this.getContentPane(), "DB�˺�ȱʧ��");
			}
			else if("".equals(DoWhatAndSendMes.udao.getUser(user).getAccount())){
				Notice.display(Delete.this.getContentPane(), "�û������ڣ�");
			}
			else{
				DoWhatAndSendMes.sdao.delete(user);
				DoWhatAndSendMes.udao.delete(user);
				Notice.display(Delete.this.getContentPane(), "�û���ɾ����");
				//Delete.this.dispose();
			}
		}
		
	}
}
