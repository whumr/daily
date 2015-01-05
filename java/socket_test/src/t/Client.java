package t;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws  
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Socket s = new Socket("127.0.0.1", 2555);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String st;
		while(true) {
			if((st = br.readLine()) != null) {
				try{
					s.sendUrgentData(0xFF);
				}catch(IOException ex){
				      s = new Socket("127.0.0.1", 2555);
				      System.out.println("---------------------reconnected------------------");
				}
				s.getOutputStream().write(st.getBytes());
			}
		}
	}
}
