package t;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class PClient {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws  
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Socket s = new Socket("127.0.0.1", 2555);
		s.setKeepAlive(true);
		InputStream in = s.getInputStream();
		OutputStream out = s.getOutputStream();
		String content = "吃了吗";
//		int version, long code, long fUserId, long tUserId, int length, String content
		MyProtocol p = new MyProtocol(0x101, 0x1001, 476892651L, 5648611L, 
				content.getBytes("UTF-8").length, content);
		ProtocolUtil.write(out, p);
		System.out.println(p);
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] b = new byte[1024];
		int read = 0;
		while((read = in.read(b)) > 0)
			System.out.println(new String(b, 0, read, "GBK"));
		System.out.println("--------------------------------");
	}
}
