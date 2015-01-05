package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

public class Client {

	public static void main(String[] args) throws Exception {
//		for (int i = 0; i < 10; i++) {
			new Client().start();
//		}
	}
	
	public void start() throws IOException {
		Socket socket = new Socket("localhost", 1234);
		final OutputStream out = socket.getOutputStream();
		final InputStream in = socket.getInputStream();
		
		new Thread(new Runnable() {
			public void run() {
				Random r = new Random();
				while (true) {
					try {
						int op = 1 + r.nextInt(4);
						int n1 = r.nextInt(100);
						int n2 = 1 + r.nextInt(100);
						out.write((op + " " + n1 + " " + n2).getBytes());
						out.flush();
						Thread.sleep(1000L);
					} catch (Exception e) {
						e.printStackTrace();
						break;
					}
				}
			}
		}).start();

		new Thread(new Runnable() {
			public void run() {
				byte[] buffer = new byte[1024];
				int length;
				while (true) {
					try {
						while(in.available() > 0) {
							if((length = in.read(buffer)) > 0) {
								System.out.println(new String(buffer, 0, length));
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
						break;
					}
				}
			}
		}).start();
	}

}