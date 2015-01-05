package tem;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import tem.domain.Protocol;
import tem.util.ProtocolUtil;

public class Server extends Thread {

	private ServerSocket serverSocket;

	private CommonService commonService;
	
	public Server() {
		super();
		try {
			serverSocket = new ServerSocket(Constants.port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(true) {
			try {
				Socket socket = serverSocket.accept();
				new ProtocolRunner(socket).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	class ProtocolRunner extends Thread {
		private Socket socket;
		
		public ProtocolRunner(Socket socket) {
			super();
			this.socket = socket;
		}

		public void run() {
			try {
				Protocol protocol = ProtocolUtil.read(socket.getInputStream());
				commonService.parseCommand(protocol);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
