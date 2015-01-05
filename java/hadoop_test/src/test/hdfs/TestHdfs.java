package test.hdfs;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;


public class TestHdfs {

	public static void main(String[] args) throws IOException {
		
		if (args.length == 1)
			read(args[0]);
		else if (args.length > 1) {
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				sb.append(args[i]).append("\n");
			}
			write(args[0], sb.toString());
		}
		
		
	}
	
	static void read(String path) throws IOException {
		String hadoop_path = "hdfs://hadoop:9001/" + path;  
        
        Configuration conf = new Configuration();  
        FileSystem fs = FileSystem.get(URI.create(hadoop_path), conf);  
        Path p = new Path(hadoop_path);  
        FSDataInputStream in = fs.open(p);
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
        String hadoop_path = "hdfs://hadoop:9001/" + path;  
          
        Configuration conf = new Configuration();  
        FileSystem fs = FileSystem.get(URI.create(hadoop_path), conf);  
        Path p = new Path(hadoop_path);  
        FSDataOutputStream out = fs.create(p);  
        //控制复本数量-wt  
        fs.setReplication(p, (short) 1);  
        out.write(buff);  
        out.close();  
	}
}
