package com.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDP_receiver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int count = 0;
		try {
			DatagramSocket server = new DatagramSocket(2425);
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			while(true) {
				byte[] buffer = new byte[8192];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				server.receive(packet);
				if(packet != null && !packet.getAddress().getHostAddress().equals("10.32.193.151")
						&& !packet.getAddress().getHostName().equals("yaffleshi")
						&& !packet.getAddress().getHostAddress().equals("10.32.193.149")
						&& !packet.getAddress().getHostName().equals("Yaffleshilap"))
					System.out.println(packet.getAddress().getHostAddress()
						+ "\t" + packet.getAddress().getHostName()
						+ "\t" + new String(packet.getData(), 0, packet.getLength())
						+ "\t" + ++count + "\t" + sdf.format(new Date()));
				try {
					Thread.sleep(10);
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
