package com.test.nio;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class BroadCast extends Thread {
	String s = "天气预报,最高温度32度,最低温度25度 ";
	int port = 5858; // 组播的端口.
	InetAddress group = null; // 组播组的地址.
	MulticastSocket socket = null; // 多点广播套接字.

	BroadCast() {
		try {
//			group = InetAddress.getLocalHost(); // 设置广播组的地址
			group = InetAddress.getByName("10.32.193.155"); // 设置广播组的地址
			socket = new MulticastSocket(port); // 多点广播套接字将在port端口广播。
			socket.setTimeToLive(1); // 多点广播套接字发送数据报范围为本地网络。
			socket.joinGroup(group); // 加入广播组,加入group后,socket发送的数据报，
										// 可以被加入到group中的成员接收到。
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error:   " + e);
		}
	}

	public void run() {
		while (true) {
			try {
				sleep(5000);
				DatagramPacket packet = null; // 待广播的数据包。
				byte data[] = s.getBytes();
				packet = new DatagramPacket(data, data.length, group, port);
				System.out.println(new String(data));
				socket.send(packet); // 广播数据包。
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error:   " + e);
			}
		}
	}

	public static void main(String args[]) {
		new BroadCast().start();
	}
}
