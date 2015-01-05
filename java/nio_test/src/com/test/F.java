package com.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class F {

	static String text = "1:100:胡锦涛:胡锦涛:32:你吃了没";

	static String onLine = "1_lbt4_0#128#001EC958856C#0#0#0:1309887311:aaaaa:aaa:6291459:dsds.ads.";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			
			
			InetAddress local = InetAddress.getLocalHost();
//			InetAddress local = InetAddress.getByName("10.32.193.134");
			DatagramSocket socket = new DatagramSocket();
			byte[] a = text.getBytes("GBK");
			DatagramPacket data = new DatagramPacket(a, a.length, local, 2425);
			socket.send(data);
			
			
//			F f = new F();
//			
//			f.new L().start();
//			MulticastSocket ms = new MulticastSocket();
//			byte[] a = onLine.getBytes("GBK");
//			DatagramPacket data = new DatagramPacket(a, a.length, InetAddress.getByName("255.255.255.255"), 2425);
//			ms.send(data);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	class L extends Thread {
		public void run() {
			try {
				DatagramSocket server = new DatagramSocket(2425);
				while(true) {
					byte[] buffer = new byte[8192];
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
					server.receive(packet);
					if(packet != null)
						System.out.println(packet.getAddress().getHostAddress()
							+ "\t" + packet.getAddress().getHostName()
							+ "\t" + new String(packet.getData()));
					try {
						Thread.sleep(100);
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
	
	class SocketReader extends Thread {
		private Socket socket;
		public SocketReader(Socket socket) {
			super();
			this.socket = socket;
		}
		public void run() {
			try {
				System.out.println(socket.getInetAddress().getHostAddress() + "\t" 
						+ socket.getInetAddress().getHostName());
				InputStream in = socket.getInputStream();
				byte[] buff = new byte[8192];
				int len = 0;
				StringBuilder sb = new StringBuilder();
				while((len = in.read(buff)) > 0) {
					sb.append(new String(buff, 0, len));
				}
				in.close();
				socket.close();
				System.out.println(sb.toString());
				System.out.println("---------------------------------");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
