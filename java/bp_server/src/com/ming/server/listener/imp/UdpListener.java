package com.ming.server.listener.imp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.ming.server.common.domain.Constants.NET;
import com.ming.server.listener.AbstractMessageListener;
import com.ming.server.listener.MessageQueue;

public class UdpListener extends AbstractMessageListener implements Runnable {

	private DatagramSocket datagramSocket;
	
	private InetAddress group;

	@Override
	public void init() throws IOException {
		// TODO Auto-generated method stub
		//listen the broadcast port
		datagramSocket = new DatagramSocket(NET.UDP_PORT);
		inited = true;
	}

	@Override
	public void start() throws IOException {
		// TODO Auto-generated method stub
		if(!inited)
			init();
		new Thread(this).start();
	}

	@Override
	public void destory() throws IOException {
		// TODO Auto-generated method stub
		if(datagramSocket != null) {
			if(datagramSocket.isClosed())
				datagramSocket.close();
			datagramSocket = null;
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			byte data[] = new byte[NET.BUFFER_SIZE];
			DatagramPacket packet = new DatagramPacket(data, data.length, group, NET.UDP_PORT);
			try {
				datagramSocket.receive(packet);
				new PacketReader(packet).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class PacketReader extends Thread {
		
		private DatagramPacket packet;
		
		public PacketReader(DatagramPacket packet) {
			super();
			this.packet = packet;
		}

		public void run() {
			packet.getData();
			MessageQueue.putMessage(null);
		}
	}
}
