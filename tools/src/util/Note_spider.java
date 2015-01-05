package util;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.util.NodeList;

/**
 * 爬取小说网站，生成txt文件
 * @author Administrator
 *
 */
public class Note_spider {
	
	static HttpClient client = new HttpClient();
	
	public static void main(String[] args) throws Exception {
//		http://www.dxsxs.com/waiwen/627/17159.html
//		http://www.dxsxs.com/waiwen/627/17204.html
		
		StringBuilder buffer = new StringBuilder();
		int j = 1;
		for (int i = 17159; i <= 17204; i++, j++) {
			buffer.append("第 " + j + "章\n");
			buffer.append(parse(get("http://www.dxsxs.com/waiwen/627/" + i + ".html", "http://www.dxsxs.com/waiwen/627/")));
		}
		FileWriter w = new FileWriter("教父.txt");
		w.write(buffer.toString());
		w.flush();
		w.close();
	}

	static String get(String url, String refer) throws Exception {
		GetMethod get = new GetMethod(url);
		get.addRequestHeader("Host", "www.dxsxs.com");
		get.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		get.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
		if (refer != null)
			get.addRequestHeader("Referer", refer);
		client.executeMethod(get);
		return readResponse(get.getResponseBodyAsStream());
	}
	
	static String parse(String s) throws Exception {
		String ss = null;
		Parser p = new Parser();
		p.setEncoding("UTF-8");
		p.setInputHTML(s);
		
		NodeList list = p.extractAllNodesThatMatch(new CssSelectorNodeFilter("div .zw"));
		for (int i = 0; i < list.size(); i++) {
			Node n = list.elementAt(i);
			if (n instanceof Div) {
				ss = ((Div)n).getStringText();
			}
		}
		
		
		BufferedReader r = new BufferedReader(new StringReader(ss));
		StringBuilder sb = new StringBuilder();
		String x = null;
		while((x = r.readLine()) != null) {
			x = x.trim();
			if (x.startsWith("&nbsp;"))
				sb.append(x.replaceAll("&nbsp;", " ").replaceAll("<br />", "\n"));
		}
		r.close();
		sb.append("\r\n");
		
		return sb.toString();
	}
	
	static String readResponse(InputStream in) throws Exception {
		InputStreamReader reader = new InputStreamReader(in, "UTF-8");
		char[] chars = new char[4096];
		StringBuilder sb = new StringBuilder();
		int length = -1;
		while ((length = reader.read(chars)) > 0)
			sb.append(chars, 0, length);
		in.close();
		return sb.toString();
	}
}
