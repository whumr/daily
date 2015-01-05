package com.test.broadcast.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Reciver extends JFrame {

	private static final long serialVersionUID = 4163293377118885774L;
	private JTextArea textArea;

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
		this.setVisible(true);
		new Runner(textArea).start();
	}
	
	public static void main(String[] args) {
		Reciver r = new Reciver();
		r.init();
	}
	
	class Runner extends Thread {
		
		private JTextArea textArea;
		
		public Runner(JTextArea textArea) {
			super();
			this.textArea = textArea;
		}

		public void run() {
			try {
				MulticastSocket socket = new MulticastSocket(C.port);
				InetAddress group = InetAddress.getByName(C.ip);
				socket.joinGroup(group);
				while(true) {
					byte data[] = new byte[8192];
					DatagramPacket packet = null;
					packet = new DatagramPacket(data, data.length, group, C.port);
					try {
						socket.receive(packet);
						StringBuilder sb = new StringBuilder();
						sb.append(packet.getAddress().getHostAddress()).append("\t")
						.append(packet.getAddress().getHostName()).append("\t")
						.append(new String(packet.getData(), 0, packet.getLength()));
						textArea.append(sb.toString() + "\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

