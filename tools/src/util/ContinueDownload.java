package util;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 断点续传,下载时根据当前文件大小，向下载服务器发送参数，从下一个字节开始读取
 * 
 * @author Administrator
 *
 */
public class ContinueDownload {

public static void main(String[] args) throws Exception {
		
		int buffer_size = 4096;
		
		URL url = new URL("http://download.taobaocdn.com/wangwang/AliIM2011_taobao(7.00.07C).exe");
		File file = new File("D:/test.exe.tmp");
		int start = 0;
		RandomAccessFile fos = new RandomAccessFile(file, "rw");
		if(file.exists()) {
			start = (int)file.length();
			fos.seek(start);
			System.out.println(start);
		}
		HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
		
		int max = urlConn.getContentLength();
		System.out.println("max:" + max);
		urlConn.disconnect();
		urlConn = (HttpURLConnection)url.openConnection();
		urlConn.setRequestProperty("Range", "bytes=" + start + "-"+ max);
		
		System.out.println("contentLength:" + urlConn.getContentLength());
		
		if(urlConn.getContentLength() > 0) {
			InputStream in = urlConn.getInputStream();
			byte[] buffer = new byte[buffer_size];
			int length = 0;
			while((length = in.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
			}
			in.close();
			fos.close();
			file.renameTo(new File("D:/test.exe"));
		} else {
			fos.close();
			file.renameTo(new File("D:/test.exe"));
		}
		urlConn.disconnect();
	}
}
