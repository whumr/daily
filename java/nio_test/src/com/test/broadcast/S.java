package com.test.broadcast;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class S {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(C.sp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(ss != null) {
			while(true) {
				try {
					Socket socket = ss.accept();
					new SocketReader(socket).start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
