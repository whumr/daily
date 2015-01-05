package util;

import java.io.File;

public class Make_cocos_cpp {

	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder();
		File f = new File("D:\\co\\star\\Classes");
		for (String s : f.list()) {
			if (s.endsWith("cpp"))
				sb.append("                   ../../Classes/")
				.append(s).append(" \\\n");
		}
		System.out.println(sb.toString());
	}
}
