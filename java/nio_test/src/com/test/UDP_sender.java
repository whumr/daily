package com.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class UDP_sender {
	
	public static void main(String[] args) throws IOException {
		MulticastSocket ms = new MulticastSocket();
		for(int i=0;i<100;i++) {
			
			new Runner(ms, i).start();
		}
	}
}
class Runner extends Thread {
	static String text = "sdsada";

	MulticastSocket ms;
	int index;
	
	public Runner(MulticastSocket ms, int index) {
		super();
		this.ms = ms;
		this.index = index;
	}

	public void run(){
		try {
			byte[] a = ("Thread--" + index + "\t" + System.currentTimeMillis() + "\t" + text).getBytes("GBK");
			DatagramPacket data = new DatagramPacket(a, a.length, InetAddress.getLocalHost(), 2425);
			ms.send(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}