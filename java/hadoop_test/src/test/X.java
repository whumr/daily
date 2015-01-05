package test;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class X {

	public static void main(String[] args) throws IOException {
		Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "master");
        HBaseAdmin hbaseAdmin = new HBaseAdmin(conf);
        System.out.println(hbaseAdmin.tableExists("test"));
        
        HTable table = new HTable(conf, "user_info");    

        ResultScanner resultScanner = table.getScanner(new Scan());   
        Iterator<Result> results = resultScanner.iterator();
        while(results.hasNext()) {
            Result result = results.next();
            List<Cell> cells = result.listCells();
            for(Cell cell : cells) {
                System.out.println("column:" + Bytes.toString(CellUtil.cloneFamily(cell)));
                System.out.println("column name:" + Bytes.toString(CellUtil.cloneQualifier(cell)));
                System.out.println("column value:" + Bytes.toString(CellUtil.cloneValue(cell)));
                System.out.println("timestamp:" + cell.getTimestamp() + "\n------------------");
            }
        }
	}
}
