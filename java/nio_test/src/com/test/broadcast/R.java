package com.test.broadcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Date;

public class R {

	public static void main(String[] args) {
		MulticastSocket socket = null;
		InetAddress group = null;
		try {
			group = InetAddress.getByName(C.b_a);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(group != null) {
			for(int i = 0; i < C.b_p.length; i ++) {
				try {
					socket = new MulticastSocket(C.b_p[i]);
					if(socket != null) {
						try {
							socket.joinGroup(group);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						new Runner(socket, group, C.b_p[i]).start();
						break;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}

class Runner extends Thread {
	private MulticastSocket socket;
	private InetAddress group;
	private int port;
	public Runner(MulticastSocket socket, InetAddress group, int port) {
		super();
		this.socket = socket;
		this.group = group;
		this.port = port;
	}
	public void run() {
		while(true) {
			byte data[] = new byte[8192];
			DatagramPacket packet = null;
			packet = new DatagramPacket(data, data.length, group, port);
			try {
				socket.receive(packet);
				System.out.println("received msg from port " + port 
						+ new String(packet.getData(), 0, packet.getLength()));
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
			System.out.println("listening on port " + port + B.sdf.format(new Date()));
			System.out.println("*****************************\n");
		}
	}
	public MulticastSocket getSocket() {
		return socket;
	}
	public void setSocket(MulticastSocket socket) {
		this.socket = socket;
	}
	public InetAddress getGroup() {
		return group;
	}
	public void setGroup(InetAddress group) {
		this.group = group;
	}
}