package com.ming.server.listener.imp;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.ming.server.common.domain.Constants.NET;
import com.ming.server.common.domain.Protocol;
import com.ming.server.common.domain.Request;
import com.ming.server.listener.AbstractMessageListener;
import com.ming.server.listener.MessageQueue;
import com.ming.server.util.ProtocolFactory;
import com.ming.server.util.ProtocolUtil;

public class TcpListener extends AbstractMessageListener implements Runnable {

	private static Logger logger = Logger.getLogger(TcpListener.class);
	
	private ServerSocket serverSocket;

	@Override
	public void init() throws IOException {
		// TODO Auto-generated method stub
		//listen the broadcast port
		serverSocket = new ServerSocket(NET.TCP_PORT);
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
		if(serverSocket != null) {
			if(serverSocket.isClosed())
				serverSocket.close();
			serverSocket = null;
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				new SocketReader(socket).start();
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
				DataInputStream in = new DataInputStream(socket.getInputStream());
				ProtocolUtil util = ProtocolFactory.getProtocolUtil(in);
				if(util == null) {
					logger.error("no ProtocolUtil matched...");
				} else {
					Protocol protocol = util.readProtocol(in);
					if(protocol instanceof Request)
						((Request)protocol).setSocket(socket);
					MessageQueue.putMessage(protocol);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}