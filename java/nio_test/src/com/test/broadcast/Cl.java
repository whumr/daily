package com.test.broadcast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Cl {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Socket socket = new Socket(C.sip, C.sp);
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String s = null;
			while((s = reader.readLine()) != null) {
				if(socket.isClosed()) {
					socket = new Socket(C.sip, C.sp);
				}
				socket.getOutputStream().write(s.getBytes());
				socket.getOutputStream().flush();
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}