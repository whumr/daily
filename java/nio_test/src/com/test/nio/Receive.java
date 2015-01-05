package com.test.nio;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Receive extends Frame implements Runnable, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8309055988212778880L;
	int port; // 组播的端口.
	InetAddress group = null; // 组播组的地址.
	MulticastSocket socket = null; // 多点广播套接字.
	Button 开始接收, 停止接收;
	TextArea 显示正在接收内容, 显示已接收的内容;
	Thread thread; // 负责接收信息的线程.
	boolean 停止 = false;

	public Receive() {
		super("定时接收信息 ");
		thread = new Thread(this);
		开始接收 = new Button("开始接收 ");
		停止接收 = new Button("停止接收 ");
		停止接收.addActionListener(this);
		开始接收.addActionListener(this);
		显示正在接收内容 = new TextArea(10, 10);
		显示正在接收内容.setForeground(Color.blue);
		显示已接收的内容 = new TextArea(10, 10);
		Panel north = new Panel();
		north.add(开始接收);
		north.add(停止接收);
		add(north, BorderLayout.NORTH);
		Panel center = new Panel();
		center.setLayout(new GridLayout(1, 2));
		center.add(显示正在接收内容);
		center.add(显示已接收的内容);
		add(center, BorderLayout.CENTER);
		validate();
		port = 5858; // 设置组播组的监听端口。
		try {
			group = InetAddress.getLocalHost(); // 设置广播组的地址
			socket = new MulticastSocket(port); // 多点广播套接字将在port端口广播。
			socket.joinGroup(group); // 加入广播组,加入group后,socket发送的数据报,
										// 可以被加入到group中的成员接收到。
		} catch (Exception e) {
		}
		setBounds(100, 50, 360, 380);
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == 开始接收) {
			开始接收.setBackground(Color.blue);
			停止接收.setBackground(Color.gray);
			if (!(thread.isAlive())) {
				thread = new Thread(this);
			}
			try {
				thread.start();
				停止 = false;
			} catch (Exception ee) {
			}
		}
		if (e.getSource() == 停止接收) {
			开始接收.setBackground(Color.gray);
			停止接收.setBackground(Color.blue);
			thread.interrupt();
			停止 = true;
		}
	}

	public void run() {
		while (true) {
			byte data[] = new byte[8192];
			DatagramPacket packet = null;
			packet = new DatagramPacket(data, data.length, group, port); // 待接收的数据包。
			try {
				socket.receive(packet);
				String message = new String(packet.getData(), 0,
						packet.getLength());
				显示正在接收内容.setText("正在接收的内容:\n " + message);
				显示已接收的内容.append(message + "\n ");
			} catch (Exception e) {
			}
			if (停止 == true) {
				break;
			}
		}
	}

	public static void main(String args[]) {
		new Receive();
	}
}
