package test.demob;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Mc {

	public static void main(String[] args) throws Exception {
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
		String s = null;
		Client c = new Client(Integer.parseInt(args[0]));
//		Client c = null;
//		if((s = r.readLine()) != null)
//			c = new Client(Integer.parseInt(s));
		while((s = r.readLine()) != null) {
			String[] ss = createClient(s);
			if(ss != null)
				c.send(new Msg(0, c.getId(), Integer.parseInt(ss[0]), ss[1]));
		}
	}
	
	public static String[] createClient(String s) {
		for(int i = 0; i < s.length(); i ++) {
			if(s.charAt(i) == ' ')
				return new String[]{s.substring(0, i), s.substring(i)};
		}
		return null;
	}
}
