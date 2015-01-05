package com.test.broadcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class T_B {
	
	public static String msg = "1_1bt4_0#128#001EC958856C#0#0#0:1309516650:aaa:AAA:0:.";
	public static int port = 2425;
	public static String ip = "255.255.255.255";
	
	public static void main(String[] args) {
		MulticastSocket socket = null;
		InetAddress group = null;
		try {
			group = InetAddress.getByName("255.255.255.255");
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(group != null) {
			try {
				socket = new MulticastSocket(port);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("new MulticastSocket error by port " + port);
			}
			
			if(socket != null) {
				try {
					socket.joinGroup(group);
					DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(), group, port);
					socket.send(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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