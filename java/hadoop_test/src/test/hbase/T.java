package test.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.util.Bytes;

public class T {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws  
	 * @throws  
	 */
	public static void main(String[] args) throws IOException {
//		Configuration conf = HBaseConfiguration.create();
		
		Configuration conf = new Configuration();       

		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.zookeeper.quorum", "192.168.147.130");
        
        Configuration configuration = HBaseConfiguration.create(conf);     
		
		
//		conf.set("hbase.zookeeper.quorum", "hadoop");
		
		
//		conf.set("hbase.cluster.distributed", "false");
//		conf.set("hbase.master", "192.168.147.129:51536");
		
		
		HBaseAdmin admin = new HBaseAdmin(configuration);
		System.out.println(admin.tableExists("test"));
//		HTableDescriptor tableDescriptor = admin.getTableDescriptor(Bytes.toBytes("database"));
//		byte[] name = tableDescriptor.getName();
//		System.out.println(new String(name));
//		HColumnDescriptor[] columnFamilies = tableDescriptor
//				.getColumnFamilies();
//		for (HColumnDescriptor d : columnFamilies) {
//			System.out.println(d.getNameAsString());
//		}

	}

}
