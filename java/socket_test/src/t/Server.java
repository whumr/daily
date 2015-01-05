package t;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

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
			System.out.println("---------------------accept " + s.getInetAddress().getHostAddress());
			InputStream in = s.getInputStream();
			byte[] b = new byte[16];
			int read = 0;
			while((read = in.read(b)) > 0) {
				String sss = new String(b, 0, read);
				if(sss.equals("bye"))
					break;
				System.out.println(sss);
			}
			s.close();
			System.out.println("---------------------accept end------------------");
		}
	}

}
