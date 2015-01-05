package t;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

public class X {
	
	static String HOST = "192.168.1.20:7070";
	static String SERVER = "http://" + HOST + "/http-bind/";
	static String DOMAIN = "chat.352.cn";
	
	static String name = "mr1";
	static String pass = "mr";

	
	static long RID = 4133813207L;
	static String sid;
	
	static HttpClient client = HttpClientBuilder.create().build();
	
	public static void main(String[] args) throws Exception {
		
//		Packet p = new Packet("cmVhbG09Im1yLmNuIixub25jZT0iejZvZHhPWHRVbDBMVFdvbmxDNStGYmdDQzNpM3BET2dPQ3MvMkJmdSIscW9wPSJhdXRoIixjaGFyc2V0PXV0Zi04LGFsZ29yaXRobT1tZDUtc2Vzcw==");
//		String x = p.response(name, pass);
//		System.out.println(x);
		
		sid = getSid();
//		System.out.println(sid);
		String reply = parse();
		if (login(reply)) {
			sendPost(s4.replaceFirst("\\?", RID++ + "").replaceFirst("\\?", sid));
			sendPost(s5.replaceFirst("\\?", RID++ + "").replaceFirst("\\?", sid));
			sendPost(s6.replaceFirst("\\?", RID++ + "").replaceFirst("\\?", sid));
			
			for (int i = 0; i < 1000; i++) {
				sendMessage("mr@" + DOMAIN, "testfsdffffwefwefwetestfsdffffwefwefwetestfsdffffwefwefwetestfsdffffwefwefwetestfsd" +
						"ffffwefwefwetestfsdffffwefwefwetestfsdffffwefwefwetestfsdffffwefwefwetestfsdffffwefwefwetestfsdff" +
						"ffwefwefwetestfsdffffwefwefwetestfsdffffwefwefwetestfsdffffwefwefwe " + i);
				Thread.sleep(50);
			}
		}
		
	}
	
	static void sendMessage(String to, String msg) throws Exception {
		sendPost(MESSAGE.replaceFirst("\\?", RID++ + "").replaceFirst("\\?", sid)
				.replaceFirst("\\?", to).replaceFirst("\\?", msg), false);
	}
	
	static boolean login(String x) throws Exception {
		String response = sendPost(s3.replaceFirst("\\?", RID++ + "").replaceFirst("\\?", sid).replaceFirst("\\?", x));
		Document doc = DocumentHelper.parseText(response);
		return doc.getRootElement().element("success") != null;
	}
	
	static String parse() throws Exception {
		String reply = sendPost(s2.replaceFirst("\\?", RID++ + "").replaceFirst("\\?", sid));
		Document doc = DocumentHelper.parseText(reply);
		String challenge = doc.getRootElement().element("challenge").getText();
		
		ScriptEngineManager mgr = new ScriptEngineManager();    
		ScriptEngine engine = mgr.getEngineByExtension("js");
		engine.eval(new FileReader("md5.js"));
		Invocable inv = (Invocable) engine;     
		String value = String.valueOf(inv.invokeFunction("x", challenge, DOMAIN, name, pass)); 
//		System.out.println(value);
		
		return value;
	}
	
	static String getSid() throws Exception {
		HttpPost post = getPost();
		post.setEntity(new StringEntity(s1.replaceFirst("\\?", RID++ + ""), "UTF-8"));
		HttpResponse response = client.execute(post);
		String s = readResponse(response);
		post.completed();
		Document doc = DocumentHelper.parseText(s);
		return doc.getRootElement().attributeValue("authid");
	}
	
	static String sendPost(String s) throws Exception {
//		HttpPost post = getPost();
//		post.setEntity(new StringEntity(s, "UTF-8"));
//		HttpResponse response = client.execute(post);
//		String reply = readResponse(response);
//		post.completed();
////		System.out.println(reply);
//		return reply;
		return sendPost(s, true);
	}
	
	static String sendPost(String s, boolean wait) throws Exception {
		HttpPost post = getPost();
		post.setEntity(new StringEntity(s, "UTF-8"));
		if (wait) {
			HttpResponse response = client.execute(post);
			String reply = readResponse(response);
			post.completed();
//		System.out.println(reply);
			return reply;
		} else{
			Builder builder = RequestConfig.custom();
			builder.setSocketTimeout(1);
			post.setConfig(builder.build());
			try {
				client.execute(post);
			} catch (SocketTimeoutException e) {
			}
			post.completed();
			return null;
		}
	}
	
	static HttpPost getPost() {
		HttpPost post = new HttpPost(SERVER); 
		post.setHeader("Host", HOST);
		post.setHeader("Content-Type", "text/xml; charset=utf-8");
		return post;
	}
	
	static String readResponse(HttpResponse response) throws Exception {
		InputStreamReader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
		char[] chars = new char[4096];
		StringBuilder sb = new StringBuilder();
		int length = -1;
		while ((length = reader.read(chars)) > 0)
			sb.append(chars, 0, length);
		return sb.toString();
	}
	
	static String  s1 = "<body xmlns:xmpp='urn:xmpp:xbosh' rid='?' to='" + DOMAIN + "' xml:lang='en' wait='60' " +
			"hold='1' content='text/xml; charset=utf-8' ver='1.6' xmpp:version='1.0'></body>";
	static String  s2 = "<body rid='?' sid='?'><auth xmlns='urn:ietf:params:xml:ns:xmpp-sasl' mechanism='DIGEST-MD5'/></body>";
	static String  s3 = "<body rid='?' sid='?'><response xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>?</response></body>";
	static String  s4 = "<body xmlns:xmpp='urn:xmpp:xbosh' rid='?' sid='?' to='" + DOMAIN + "' xml:lang='en' xmpp:restart='true'></body>";
	static String  s5 = "<body rid='?' sid='?'><iq type='set' id='_bind_auth_2'><bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'/>" +
			"</iq></body>";
	static String  s6 = "<body rid='?' sid='?'><iq type='set' id='_session_auth_2'><session xmlns='urn:ietf:params:xml:" +
			"ns:xmpp-session'/></iq></body>";
	
	static String MESSAGE = "<body rid=\"?\" sid=\"?\"><message to=\"?\" type=\"chat\"><body>?</body></message></body>";
}

