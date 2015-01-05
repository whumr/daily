package buy_352;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.InputTag;
import org.htmlparser.util.NodeList;

public class Caifu {

	static int id = 448, money = 10000, sleep = 100;
	
	static HttpClient client = new HttpClient();
	
	//投标完成后去页面支付
//	https://www.96caifu.com/user/payAjax
	
	static String BASE_URL = "https://www.96caifu.com", HOST = "www.96caifu.com", 
			Index_url = BASE_URL + "/index.html",
			Login_url = BASE_URL + "/account/dologin?format=json",
			Info_url = BASE_URL + "/loan/detail/" + id + ".html",
			Buy_url = BASE_URL + "/loan/sends",
			Pay_url = BASE_URL + "/user/payAjax",
			Return_url = BASE_URL + "/PayReturn/InitiativeTender";

	static String PAY_BASE_URL = "https://lab.chinapnr.com", PAY_HOST = "lab.chinapnr.com", 
			Pay_redirct_url = PAY_BASE_URL + "/muser/publicRequests",
			Pay_submit_url = PAY_BASE_URL + "/muser/publicRequests/activeBidConfirm";
	
	public static void main(String[] args) throws Exception {
		//登陆
		System.out.println(post(Login_url, Index_url, 
				new String[][]{
					{"account", account},
					{"password", password}
				}, false, HOST));
		//详情
		get(Info_url, Index_url, false, HOST);
		
		String result = "";
		//申购
		while(true) {
			result = post(Buy_url, Info_url, 
					new String[][]{
					{"lid", id + ""},
					{"unit", money + ""}
				}, true, HOST);
			System.out.println(result);
			if("1".equals(result))
				break;
			Thread.sleep(sleep);
		}
		
		StringBuilder sb = new StringBuilder();
		
		//取跳转参数
		String redirect_html = get(Pay_url, Info_url, false, HOST);
		sb.append(redirect_html);
		
		Map<String, String> map = readInput(redirect_html);
		//跳转到输密码页
		redirect_html = post(Pay_redirct_url, Pay_url, getParams(map), false, PAY_HOST);
		sb.append("\n\n\n").append(redirect_html);
		//付款提交
		map = readInput(redirect_html);
		map.put("TransPwd", TransPwd);
		redirect_html = post(Pay_submit_url, Pay_redirct_url, getParams(map), false, PAY_HOST);
		sb.append("\n\n\n").append(redirect_html);
		//返回96caifu
		map = readInput(redirect_html);
		redirect_html = post(Return_url, Pay_submit_url, getParams(map), false, HOST);
		System.out.println(redirect_html);
		sb.append("\n\n\n").append(redirect_html);
		
		//记录日志
		FileWriter writer = new FileWriter("log");
		writer.write(sb.toString());
		writer.flush();
		writer.close();
	}
	
	static String get(String url, String refer, boolean ajax, String host) throws Exception {
		GetMethod get = new GetMethod(url);
		get.addRequestHeader("Host", host);
		get.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		get.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
		if (refer != null)
			get.addRequestHeader("Referer", refer);
		//是否ajax
		if (ajax)
			get.addRequestHeader("X-Requested-With", "XMLHttpRequest");
		client.executeMethod(get);
		return readResponse(get.getResponseBodyAsStream());
	}
	
	static String post(String url, String refer, String[][] params, boolean ajax, String host) throws Exception {
		PostMethod post = new PostMethod(url);
		post.addRequestHeader("Host", host);
		post.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
		if (refer != null)
			post.addRequestHeader("Referer", refer);
		//是否ajax
		if (ajax)
			post.addRequestHeader("X-Requested-With", "XMLHttpRequest");
		else
			post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		//是否有参数
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				post.addParameter(params[i][0], params[i][1]);
			}
		}
		client.executeMethod(post);
		return readResponse(post.getResponseBodyAsStream());
	}
	
	static String readResponse(InputStream in) throws Exception {
		InputStreamReader reader = new InputStreamReader(in, "UTF-8");
		char[] chars = new char[4096];
		StringBuilder sb = new StringBuilder();
		int length = -1;
		while ((length = reader.read(chars)) > 0)
			sb.append(chars, 0, length);
		return sb.toString();
	}
	
	static Map<String, String> readInput(String src) throws Exception {
		Parser p = new Parser();
		p.setEncoding("UTF-8");
		p.setInputHTML(src);
		
		Map<String, String> map = new HashMap<String, String>();
		
		NodeList list = p.extractAllNodesThatMatch(new TagNameFilter("INPUT"));
		for (int i = 0; i < list.size(); i++) {
			InputTag input = (InputTag)list.elementAt(i);
			System.out.println(input.getAttribute("id") + "\t" + input.getAttribute("value"));
			map.put(input.getAttribute("id"), input.getAttribute("value"));
		}
//		TransPwd
		return map;
	}
	
	static String[][] getParams(Map<String, String> map) {
		String[][] result = new String[map.size()][2];
		int i = 0;
		for (String key : map.keySet()) {
			result[i++] = new String[]{key, map.get(key)};
		}
		return result;
	}

	static String account = "", password = "", TransPwd = "";
}
