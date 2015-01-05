package com.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.test.broadcast.ui.C;

public class T {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Date date = new Date(1309516650);
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
		System.out.println(System.currentTimeMillis());
		System.out.println(long2ip(1309516650));
		System.out.println(ip2long("10.32.193.132"));
		
		
		try {
			InetAddress local = InetAddress.getLocalHost();
			System.out.println(local.getHostAddress());
			System.out.println("---------------");
			InetAddress is[] = InetAddress.getAllByName("localhost");
			for(InetAddress i : is)
				System.out.println(i.getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("---------------");
		try {
//			Enumeration<NetworkInterface> e1 = NetworkInterface.getNetworkInterfaces ();
//			while (e1.hasMoreElements ())  
//			{  
//				NetworkInterface ni = e1.nextElement ();  
//				Enumeration<InetAddress> e2 = ni.getInetAddresses ();  
//				while (e2.hasMoreElements ())  
//				{  
//					InetAddress ia = e2.nextElement ();  
//					System.out.println(ia.getHostAddress());  
//				}  
//			}  
			
			NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
			List<InterfaceAddress> list = ni.getInterfaceAddresses();
			InetAddress i = null;
			for(InterfaceAddress ia : list)
				if(ia.getBroadcast() != null) {
					System.out.println(ia.getBroadcast().getHostAddress());
					i = ia.getBroadcast();
				}

			String text = "xxxxxxxxxx";
			try {
				MulticastSocket socket = new MulticastSocket(C.port);
				socket.setBroadcast(true);
				socket.joinGroup(i);
				DatagramPacket packet = new DatagramPacket(text.getBytes(), text.length(), i, C.port);
				socket.send(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

	}

	public static String long2ip(long ipLong) {
		// long ipLong = 1037591503;
		long mask[] = { 0x000000FF, 0x0000FF00, 0x00FF0000, 0xFF000000 };
		long num = 0;
		StringBuffer ipInfo = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			num = (ipLong & mask[i]) >> (i * 8);
			if (i > 0)
				ipInfo.insert(0, ".");
			ipInfo.insert(0, Long.toString(num, 10));
		}
		return ipInfo.toString();
	}
	
	public static long ip2long(String ip) {  
		String[] ips = ip.split("[.]");  
		long num =  16777216L*Long.parseLong(ips[0]) + 65536L*Long.parseLong(ips[1]) + 256*Long.parseLong(ips[2]) + Long.parseLong(ips[3]);  
		return num;  
	}
}
