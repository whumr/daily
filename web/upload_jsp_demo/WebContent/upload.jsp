<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>

<%
	out.println(request.getParameter("file"));
	

	ServletInputStream in = request.getInputStream();
	// out.println("文件信息:<br>");
	long begin = System.currentTimeMillis();// 传送时间计时开始
	int l = 0;
	byte[] b = new byte[1024];
	while ((l = in.read(b)) > 0) {
		
	}
	l = in.readLine(b, 0, b.length);
	String sign = new String(b, 0, l);// eg.-----------------------------7d9dd29630a34
	l = in.readLine(b, 0, b.length);
	String info = new String(b, 0, l);// eg.Content-Disposition:form-data;
	// name="file";
	l = in.readLine(b, 0, b.length);
	// String type=new String(b,0,l);
	//eg.Content-Type:application/octet-stream(程序文件)
	l = in.readLine(b, 0, b.length);
	// String nulll=new String(b,0,l);//此值应为空
	int nIndex = info.toLowerCase().indexOf("filename=\"");
	int nLastIndex = info.toLowerCase().indexOf("\"", nIndex + 10);
	String filepath = info.substring(nIndex + 10, nLastIndex);
	int na = filepath.lastIndexOf("\\");
	String filename = filepath.substring(na + 1);
	// out.println("文件绝对路径："+filepath+"<br>");
	// out.println("文件名："+filename+"<br><br>");
	String path = "../up" + filename;
	File fi = new File(path);// 建立目标文件
	BufferedOutputStream f = new BufferedOutputStream(new FileOutputStream(fi));
	while ((l = in.readLine(b, 0, b.length)) > 0) {
		if (l == sign.length()) {
			String sign1 = new String(b, 0, sign.length());
			// out.println(sign1+"<br>");
			if (sign1.startsWith(sign))// 比对是否文件已传完
				break;
		}
		f.write(b, 0, l);
		f.flush();
	}
	f.flush();
	f.close();
	long end = System.currentTimeMillis();// 传送时间计时结束
	out.println("上传文件用时："+(end-begin)+"毫秒<br>");
%>