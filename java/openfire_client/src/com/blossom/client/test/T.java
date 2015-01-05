package com.blossom.client.test;

import java.util.ArrayList;
import java.util.List;

public class T {

//	static String s = "aa";
//	static String s = "aadaaaaaaa";
//	static String s = "abcdefghijklmn";
	static String s = "geqadjlksajlkfjsslfkjwklej";
	
	
	public static void main(String[] args) {
		Point result = getBestPoint(s);
		System.out.println("------------");
		if (result != null)
			System.out.println("best : " + s.substring(result.start_index, result.end_index + 1) + "\t" + result);
	}

	private static Point getBestPoint(String s) {
		List<Point> list = new ArrayList<Point>();
		for (int i = 0; i < s.length() - 1; i ++) {
			if (i > 0 && s.charAt(i) > s.charAt(i - 1))
				continue;
			Point p = getBestPoint(s, i);
			if (p != null)
				list.add(p);
		}
		return getBestPoint(s, list);
	}
	
	private static Point getBestPoint(String s, int start) {
		List<Point> list = new ArrayList<Point>();
		
		char c = s.charAt(start);
		
		for (int i = start + 1; i < s.length(); i++) {
			char end = s.charAt(i);
			if (end >= c) {
				while (++i < s.length() && end < s.charAt(i)) {
					end = s.charAt(i);
				}
				i --;
				Point p = new Point();
				p.start_char = c;
				p.start_index = start;
				p.end_char = end;
				p.end_index = i;
				process(p, s, c, end);
				list.add(p);
			}
		}
		return getBestPoint(s, list);
	}
	
	private static Point getBestPoint(String s, List<Point> list) {
		//取最少要删除多少个字母
		Point result = null;
		int to_delete = -1;
		for (Point p : list) {
			int delete = s.length() - (p.end_index + 1) + (p.un_ordered_chars.size());
			if (delete < to_delete || to_delete == -1 ) {
				result = p;
				to_delete = delete;
			}
			
//			System.out.println(s.substring(p.start_index, p.end_index + 1) + "\t" + p);
		}
//		System.out.println("------------");
		if (result != null)
			System.out.println("best : " + s.substring(result.start_index, result.end_index + 1) + "\t" + result);
		return result;
	}
	
	private static void process(Point p, String s, char min_char, char max_char) {
		int start = p.start_index, end = p.end_index;
		String sbus = s.substring(start + 1, end);
		//前面需要删除的
		for (int i = 0; i < start; i++) {
			p.un_ordered_chars.add(s.charAt(i));
		}
		//中间需要删除的		
		for (int i = 0; i < sbus.length(); i++) {
			char c = sbus.charAt(i);
			if (c <= min_char || c >= max_char) {
				p.un_ordered_chars.add(c);
			}
		}
		if (min_char == max_char)
			p.un_ordered_chars.add(max_char);
		//后面需要删除的
		for (int i = end + 1; i < s.length(); i++) {
			p.un_ordered_chars.add(s.charAt(i));
		}
	}
}

class Point {
	public int start_index, end_index;
	public char start_char, end_char;
	List<Character> un_ordered_chars;
	
	public Point() {
		un_ordered_chars = new ArrayList<Character>();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("需要删除:");
		for (char c : un_ordered_chars) {
			sb.append(c).append(" ");
		}
		return sb.toString();
	}
}

