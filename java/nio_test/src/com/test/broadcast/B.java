package com.test.broadcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class B {
	
	public static String msg = "aaaaaaaaaaaa";
	public static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	
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
			while(true) {
				for(int i = 0; i < C.b_p.length; i ++) {
					try {
						socket = new MulticastSocket(C.b_p[i]);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.err.println("new MulticastSocket error by port " + C.b_p[i]);
					}
					
					if(socket != null) {
						try {
							socket.joinGroup(group);
							String s = msg + C.b_p[i] + " " + sdf.format(new Date());
							DatagramPacket packet = new DatagramPacket(s.getBytes(), s.length(), group, C.b_p[i]);
							socket.send(packet);
							System.out.println("broadcast msg on port " + C.b_p[i] + " " + sdf.format(new Date()));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}