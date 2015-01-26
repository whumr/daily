package test.hdfs;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class TestHdfs {
	
	static String uri = "hdfs://192.168.9.11:9001/";
	static Configuration conf = new Configuration();
	static FileSystem fs;
	static {
		try {
			fs = FileSystem.get(URI.create(uri), conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		String path = "/test/test.txt", content = "this is a test txt";
		write(path, content);
		read(path);
	}
	
	static void read(String path) throws IOException {
        FSDataInputStream in = fs.open(new Path(path));
        StringBuilder sb = new StringBuilder();
        byte[] bs = new byte[10240];
        int length;
        while ((length = in.read(bs)) > 0)
        	sb.append(new String(bs, 0, length));
        in.close();
        
        System.out.println(sb.toString());
	}

	static void write(String path, String content) throws IOException {
		byte[] buff = content.getBytes();  
        Path p = new Path(path);  
        FSDataOutputStream out = fs.create(p);  
        //控制复本数量-wt  
        fs.setReplication(p, (short) 1);  
        out.write(buff);  
        out.flush();
        out.close();  
	}
}
