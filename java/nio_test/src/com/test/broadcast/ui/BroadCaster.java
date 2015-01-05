package com.test.broadcast.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class BroadCaster extends JFrame {

	private static final long serialVersionUID = 4163293377118885774L;
	private JTextArea textArea;
	private JTextField textField;
	private JButton sendButton;

	public void init() {
		this.setTitle("Reciver");
		this.setSize(300, 400);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(((int)dimension.getWidth() - 200) / 2, ((int)dimension.getHeight() - 400) / 2);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new FlowLayout());
		textArea = new JTextArea(10, 25);
		textArea.setEditable(false);
		this.add(textArea);
		textField = new JTextField(25);
		this.add(textField);
		sendButton = new JButton("send");
		this.add(sendButton);
		this.setVisible(true);
		addListener();
	}
	
	private void addListener() {
		sendButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(textField.getText() != null && !textField.getText().trim().equals("")) {
					String text = textField.getText() + "\t" + C.sdf.format(new Date());
					new Runner(text).start();
					textArea.append(text + "\n");
				}
			}
		});
	}
	
	public static void main(String[] args) {
		BroadCaster b = new BroadCaster();
		b.init();
	}
	
	class Runner extends Thread {
		
		private String text;
		
		public Runner(String text) {
			super();
			this.text = text;
		}

		public void run() {
			try {
				MulticastSocket socket = new MulticastSocket(C.port);
				socket.setBroadcast(true);
				InetAddress group = InetAddress.getByName(C.ip);
				socket.joinGroup(group);
				DatagramPacket packet = new DatagramPacket(text.getBytes(), text.length(), group, C.port);
				socket.send(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

