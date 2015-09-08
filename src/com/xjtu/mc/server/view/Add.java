package com.xjtu.mc.server.view;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.xjtu.mc.common.User;
import com.xjtu.mc.server.dao.DBUtil;
import com.xjtu.mc.server.model.DoWhatAndSendMes;


public class Add extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private JLabel jl1;
	private JLabel jl2;
	private TextField tf1;
	private TextField tf2;
	private JButton jb1;
	private JButton jb2;
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		this.dispose();
	}
	public Add(){
		GridLayout gl=new GridLayout();
		gl.setColumns(2);
		gl.setRows(3);
		this.setLayout(gl);
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.setTitle("�ֻ���ʱͨ�ŷ����");//���ô������
		this.setVisible(true);//���ô���Ŀɼ���
		this.setLocation(860, 130);
		jl1=new JLabel("�û�����");
		jl2=new JLabel("���룺");
		tf1=new TextField();
		this.setSize(240, 120);
		Font f = new Font("����", 0, 20);
		tf1.setFont(f);
		tf2=new TextField();
		tf2.setFont(f);
		jb1=new JButton("ȷ��");
		jb2=new JButton("ȡ��");
		this.add(jl1);
		this.add(tf1);
		this.add(jl2);
		this.add(tf2);
		this.add(jb1);
		this.add(jb2);
		jb1.addActionListener(new ConfirmListener());
		jb2.addActionListener(this);
		this.setTitle("�û����");
		
	}
	private class ConfirmListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String user = tf1.getText().trim();
			String pwd = tf2.getText().trim();
			if("".equals(DBUtil.user)||"".equals(DBUtil.pwd)){
				Notice.display(Add.this.getContentPane(), "DB�˺�ȱʧ��");
			}
			else if("".equals(user)||"".equals(pwd)){
				Notice.display(Add.this.getContentPane(),"�˺Ż����벻��Ϊ�գ�");
			}
			else if(user.length()<4||pwd.length()<4){
				Notice.display(Add.this.getContentPane(),"�˺Ż���������4���ַ���");
			}
			else if(user.length()>12||pwd.length()>12){
				Notice.display(Add.this.getContentPane(),"�˺Ż��������С��12���ַ���");
			}
			else if(user.equals(DoWhatAndSendMes.udao.getUser(user).getAccount())){
				Notice.display(Add.this.getContentPane(),"�˺��Ѵ��ڣ�");
			}
			else{
				User u = new User();
				u.setAccount(user);
				u.setPwd(pwd);
				DoWhatAndSendMes.udao.register(u);
				DoWhatAndSendMes.sdao.insertState(user);
				Notice.display(Add.this.getContentPane(),"�û���ӳɹ���");
				Add.this.dispose();
			}
		}
		
	}
}
