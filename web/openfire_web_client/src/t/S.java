package t;

import javax.swing.JFrame;

public class S extends JFrame{

	private static final long serialVersionUID = 5296140847237574282L;

	public S() {
		super();
	}



	public static void main(String[] args) {
		String s = "127.0.0.1 chat#.352.cn";
		for (int i = 1; i <= 50; i++) {
			System.out.println(s.replaceFirst("#", i + ""));
		}
	}

}