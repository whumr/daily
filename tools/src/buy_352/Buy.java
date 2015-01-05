package buy_352;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class Buy {

	static int check_id = 1443, id = 2288, money = 50;

	static String account = "", password = "", a_count = "", transPwd = "";

	static String BUY_TIME = "2014-11-19 14:00:00";
	
	static String BASE_URL = "http://www.352.com", HOST = "www.352.com", 
			Login_url = "https://login.352.com/login/loginCommitJson.do?time=",
			Detail_url = BASE_URL + "/trading3/webTradingRzbDetail.do?id=" + check_id,
			Check_url = BASE_URL + "/trading3/tradingCanBuy.do?ztbPackage.id=" + check_id + "&m=" 
				+ money + "&rangeId=" + id,
				
			Check_url_2 = BASE_URL + "/trading3/webBuyCheckInfo.do?pid=" + check_id +
					"&m=" + money + "&rid=" + id + "&p=" + money + "&rangeId=" + id,
				
			Buy_url = BASE_URL + "/trading3/webBuyCheckInfo.jsp?rid=" + id,
			Submit_url = BASE_URL + "/trading3/webBuyCheckAndDone.do?rid=" + id,
			
			Time_url = BASE_URL + "/trading3/getSysDate.do";
			
	static HttpClient client = new HttpClient();
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static void main(String[] args) throws Exception {
		
		/**
		 * login 
		 */
		System.out.println(post(Login_url + System.currentTimeMillis(), 
				"https://login.352.com/logon", 
			new String[][]{
				{"j", BASE_URL},
				{"account", account},
				{"password", password}
		}, true));
		
		Thread.sleep(200L);
		
		/**
		 * detail
		 */
		get(Detail_url, BASE_URL, false);
		
		Thread.sleep(200L);
		
		/**
		 * check
		 */
		System.out.println(post(Check_url, Detail_url, null, true));
		
		Thread.sleep(200L);
		
		/**
		 * check2
		 */
		get(Check_url_2, Detail_url, false);
		
		Thread.sleep(200L);

		/**
		 * redirect get
		 */
		get(Buy_url, Detail_url, false);
		
		Thread.sleep(200L);
		
		/**
		 * get time
		 */
		long time = getTime();
		System.out.println(sdf.format(new Date(time)));
		
		long buy_time = sdf.parse(BUY_TIME).getTime();
		
		System.out.println(buy_time);
		
		long delay = buy_time - time;
				
		long x = delay / 1000;
		System.out.println(x / 3600 + "小时" + x % 3600 / 60 + "分" + x % 3600 % 60 + "秒");
		
		//wait
//		if (delay > 0)
//			Thread.sleep(delay - 100);
		
		/**
		 * buy
		 */
		long t = System.currentTimeMillis();
		while (true) {
			String s = post(Submit_url, Buy_url, 
					new String[][]{
					{"checkBlossomId", a_count},
					{"transPwd", transPwd}
			}, false);
			s = URLDecoder.decode(s, "UTF-8");
			System.out.println(s);
			System.out.println("posted\t" + (System.currentTimeMillis() - t));
			t = System.currentTimeMillis();
			if (s.contains("waitfordeal.jsp")) {
				get(s, Submit_url, false);
				break;
			}
		}
	}
	
	/**
	 * http get
	 * 
	 * @param url
	 * @param refer
	 * @param ajax
	 * @return
	 * @throws Exception
	 */
	static String get(String url, String refer, boolean ajax) throws Exception {
		GetMethod get = new GetMethod(url);
		get.addRequestHeader("Host", HOST);
//		get.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		get.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
		if (refer != null)
			get.addRequestHeader("Referer", refer);
		//is ajax
		if (ajax)
			get.addRequestHeader("X-Requested-With", "XMLHttpRequest");
		client.executeMethod(get);
		return get.getResponseBodyAsString();
	}
	
	/**
	 * get time
	 * @return
	 * @throws Exception
	 */
	static long getTime() throws Exception {
		String time = get(Time_url, null, true);
		return Long.parseLong(time.split(":")[1].trim().substring(0, 13));
	}
	
	/**
	 * http post
	 * 
	 * @param url
	 * @param refer
	 * @param params
	 * @param ajax
	 * @return
	 * @throws Exception
	 */
	static String post(String url, String refer, String[][] params, boolean ajax) throws Exception {
		PostMethod post = new PostMethod(url);
		post.addRequestHeader("Host", HOST);
		post.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
		if (refer != null)
			post.addRequestHeader("Referer", refer);
		//is ajax
		if (ajax)
			post.addRequestHeader("X-Requested-With", "XMLHttpRequest");
		else
			post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		//post data
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				post.addParameter(params[i][0], params[i][1]);
			}
		}
		client.executeMethod(post);
		Header locationHeader = post.getResponseHeader("location");  
//		if (locationHeader != null && !ajax)
//			return get(locationHeader.getValue(), url, false);
		if (locationHeader != null && !ajax)
			return locationHeader.getValue();
		return post.getResponseBodyAsString();
	}

}

