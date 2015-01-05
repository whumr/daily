<%@page import="java.io.FileWriter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="java.io.*" 
    pageEncoding="UTF-8"%>
<%
/**
协议头四行内容
45 -----------------------------7de231211204c4 
80 Content-Disposition: form-data; name="file"; filename="xx.txt" 
26 Content-Type: text/plain 
2 
标记文件结尾
-----------------------------7de231211204c4--
**/
	ServletInputStream sin = request.getInputStream();
	byte[] buffer = new byte[1024 * 8];
	int length = 0, row = 0;
	String mark = "";
	String filename = "";
	while ((length = sin.readLine(buffer, 0, buffer.length)) > 0) {
		out.println(length + "    " + new String(buffer, 0, length, "UTF-8") + "<br>");
		String s = new String(buffer, 0, length, "UTF-8");
		if (row == 0)
			mark = s.trim();
		else if (s.indexOf("filename=") > 0) {
			int end = s.lastIndexOf("\"");
			int start = s.substring(0, end).lastIndexOf("\"");
			filename = s.substring(start + 1, end);
		} else if ("".equals(s.trim()))
			break;
		row ++;
	}
	
	out.println("filename:    " + filename + "<br>");
	filename = request.getRealPath("/") + "../" + filename;
	FileOutputStream fout = new FileOutputStream(filename);
	while ((length = sin.readLine(buffer, 0, buffer.length)) > 0) {
		String s = new String(buffer, 0, length);
		if (s.startsWith(mark))
			break;
		fout.write(buffer, 0, length);
	}
	//写的文件会多出一个换行，2字节
	fout.flush();
	fout.close();
	File f = new File(filename);
	out.println(f.exists());
	out.println(f.getAbsolutePath());
%>