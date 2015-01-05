package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 将汉字转化为 \\uxxxx格式
 * @author Administrator
 *
 */
public class UnicodeStringUtil {

	public static void main(String[] args) {
		/**结果
\u4e2d\u0063\u56fd
4e2d
63
56fd
		 */
		String s = "中c国";
		System.out.println(getUncode(s));
		
		for (int i = 0; i < s.length(); i++) {
			System.out.println(Integer.toHexString(s.charAt(i)));
		}
		
		/**结果
&#20013;&#99;&#22269;
中国
\u4e2dc\u56fd
中国\\uqqqq
		 */
		System.out.println(StringToWebUnicode(s));
		System.out.println(WebUnicodeToString("&#20013;&#22269;"));
		System.out.println(StringToUnicode(s));
		System.out.println(UnicodeToString("\\u4e2d\\u56fd\\\\uqqqq"));
	}
	
	/*
	 * 普通类型的unicode转string
	 */
	public static String UnicodeToString(String input) {
		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(input);
		char ch;
		while (matcher.find()) {
			ch = (char) Integer.parseInt(matcher.group(2), 16);
			input = input.replace(matcher.group(1), ch + "");
		}
		return input;
	}
	/*
	 * string转普通类型的unicode
	 */
	public static String StringToUnicode(String input) {
		String str = "";
		for (char c : input.toCharArray()) {
			if ((int) c > 128)
				str += "\\u" + Integer.toHexString((int) c);
			else
				str += c;
		}
		return str;

	}
	/*
	 * string转web类型的unicode
	 */
	public static String StringToWebUnicode(String input) {
		String str = "";
		for (char c : input.toCharArray()) {
			str += "&#" + (int) c + ";";
		}
		return str;
	}
	/*
	 * web类型的unicode转string
	 */
	public static String WebUnicodeToString(String input) {
		String str = "";
		String[] y1 = input.split(";");
		for (String c : y1) {
			if (c.length() > 2) {
				str += (char) Integer.parseInt(c.substring(2));
			}
		}
		return str;
	}
	
	
	public static String getUncode(String str) {
		if (str == null)
			return "";
		String hs = "";

		try {
			byte b[] = str.getBytes("UTF-16");
			for (int n = 0; n < b.length; n++) {
				str = (Integer.toHexString(b[n] & 0xFF));
				if (str.length() == 1)
					hs = hs + "0" + str;
				else
					hs = hs + str;
				if (n < b.length - 1)
					hs = hs + "";
			}
			// 去除第一个标记字符
			str = hs.toLowerCase().substring(4);
			char[] chs = str.toCharArray();
			str = "";
			for (int i = 0; i < chs.length; i = i + 4) {
				str += "\\u" + chs[i] + chs[i + 1] + chs[i + 2] + chs[i + 3];
			}
			return str;
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
		return str;
	}

}
