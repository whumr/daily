package test.demoa.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import test.demoa.server.MinaTimeServer;

public class Tclient {

	
	public static void main(String[] args) throws IOException {
		Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), MinaTimeServer.PORT);
		DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
		dout.writeInt(1);
		dout.writeUTF("撒地方环境按时开放后 ");
		dout.flush();
		socket.close();
	}
}
