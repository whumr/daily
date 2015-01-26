package test.yarn;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class YarnTest {
	
	static final String INPUT_PATH = "hdfs://192.168.9.11:9001/test/test.txt";
	static final String OUT_PATH = "hdfs://192.168.9.11:9001/test/out";
	
	public static void main(String[] args) throws Exception {
		System.out.println(System.getenv().get("HADOOP_HOME"));
		Configuration conf = new Configuration();
		FileSystem fileSystem = FileSystem.get(new URI(INPUT_PATH), conf);
		Path outPath = new Path(OUT_PATH);
		if (fileSystem.exists(outPath)) {
			fileSystem.delete(outPath, true);
		}
		
		Job job = Job.getInstance(conf, "test job");
		job.setJarByClass(YarnTest.class);
		FileInputFormat.setInputPaths(job, INPUT_PATH);// 1.1指定读取的文件位于哪里

		job.setInputFormatClass(TextInputFormat.class);// 指定如何对输入文件进行格式化，把输入文件每一行解析成键值对
		job.setMapperClass(TokenizerMapper.class);// 1.2 指定自定义的map类

		job.setMapOutputKeyClass(Text.class);// map输出的<k,v>类型。如果<k3,v3>的类型与<k2,v2>类型一致，则可以省略
		job.setMapOutputValueClass(IntWritable.class);

//		job.setPartitionerClass(HashPartitioner.class);// 1.3 分区
		
		job.setNumReduceTasks(1);// 有一个reduce任务运行
		// 1.4  排序、分组
		// 1.5  规约
		job.setReducerClass(IntSumReducer.class);// 2.2 指定自定义reduce类

		// 指定reduce的输出类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		// 2.3 指定写出到哪里
		FileOutputFormat.setOutputPath(job, outPath);
		
		// 指定输出文件的格式化类
		job.setOutputFormatClass(TextOutputFormat.class);

		job.waitForCompletion(true);// 把job提交给JobTracker运行
	}
}