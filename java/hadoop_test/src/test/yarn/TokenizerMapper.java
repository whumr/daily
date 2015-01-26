package test.yarn;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * mapper
 * <1> 读取输入文件内容，解析成key、value对。对输入文件的每一行，解析成key、value对。每一个键值对调用一次map函数。 
 * <2> 写自己的逻辑，对输入的key、value处理，转换成新的key、value输出。
 * <3> 对输出的key、value进行分区。
 * <4> 对不同分区的数据，按照key进行排序、分组。相同key的value放到一个集合中。
 * <5> (可选)分组后的数据进行归约。
 *
 */
public class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

	private final static IntWritable one = new IntWritable(1);
	private Text word = new Text();

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		/**
		 * 对每个单词进行计数、输出
		 */
		StringTokenizer itr = new StringTokenizer(value.toString());
		while (itr.hasMoreTokens()) {
			word.set(itr.nextToken());
			context.write(word, one);
		}
	}
}