package t;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class PServer {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ServerSocket ss = new ServerSocket(2555);
		System.out.println("---------------------server start------------------");
		while (true) {
			Socket s = ss.accept();
			s.setKeepAlive(true);
			System.out.println("---------------------accept " + s.getInetAddress().getHostAddress());
			InputStream in = s.getInputStream();
			OutputStream out = s.getOutputStream();
			MyProtocol p = ProtocolUtil.read(in);
			System.out.println(p);
			
			out.write("哈哈哈".getBytes("GBK"));
			System.out.println("---------------------accept end------------------");
		}
	}

}
