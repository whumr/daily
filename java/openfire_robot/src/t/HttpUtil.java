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
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

public class HttpUtil {

	private static HttpUtil httpUtil;
	
	private String host, server, domain;
	
	private HttpUtil() {
	}
	
	public static HttpUtil getInstance() {
		if (httpUtil == null)
			httpUtil = new HttpUtil();
		return httpUtil;
	}
	
	public void setHost(String host) {
		this.host = host;
		this.server = "http://" + host + "/http-bind/";;
	}

	public void setDomain(String domain) {
		this.domain = domain;
		s1 = s1.replaceFirst("DOMAIN", this.domain);
		s4 = s4.replaceFirst("DOMAIN", this.domain);
		MESSAGE = MESSAGE.replaceFirst("DOMAIN", this.domain);
	}

	public void connect(HttpClient client, String name, String passwd, String sid, long rid) throws Exception {
		String reply = parse(client, name, passwd, sid, rid++);
		if (login(client, reply, sid, rid++)) {
			sendPost(client, s4.replaceFirst("\\?", rid++ + "").replaceFirst("\\?", sid));
			sendPost(client, s5.replaceFirst("\\?", rid++ + "").replaceFirst("\\?", sid));
			sendPost(client, s6.replaceFirst("\\?", rid++ + "").replaceFirst("\\?", sid));
		}
	}
	
	public void sendMessage(HttpClient client, String to, String msg, String sid, long rid) throws Exception {
		sendPost(client, MESSAGE.replaceFirst("\\?", rid + "").replaceFirst("\\?", sid)
				.replaceFirst("\\?", to).replaceFirst("\\?", msg), false);
	}
	
	private boolean login(HttpClient client, String x, String sid, long rid) throws Exception {
		String response = sendPost(client, s3.replaceFirst("\\?", rid + "").replaceFirst("\\?", sid).replaceFirst("\\?", x));
		Document doc = DocumentHelper.parseText(response);
		return doc.getRootElement().element("success") != null;
	}
	
	private String parse(HttpClient client, String name, String passwd, String sid, long rid) throws Exception {
		String reply = sendPost(client, s2.replaceFirst("\\?", rid + "").replaceFirst("\\?", sid));
		Document doc = DocumentHelper.parseText(reply);
		String challenge = doc.getRootElement().element("challenge").getText();
		ScriptEngineManager mgr = new ScriptEngineManager();    
		ScriptEngine engine = mgr.getEngineByExtension("js");
		engine.eval(new FileReader("md5.js"));
		Invocable inv = (Invocable) engine;     
		String value = String.valueOf(inv.invokeFunction("x", challenge, domain, name, passwd)); 
		return value;
	}
	
	private String sendPost(HttpClient client, String s) throws Exception {
		return sendPost(client, s, true);
	}
	
	private String sendPost(HttpClient client, String s, boolean wait) throws Exception {
		HttpPost post = getPost();
		post.setEntity(new StringEntity(s, "UTF-8"));
		if (wait) {
			HttpResponse response = client.execute(post);
			String reply = readResponse(response);
			post.completed();
			return reply;
		} else {
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
	
	public String getSid(HttpClient client, long rid) throws Exception {
		HttpPost post = getPost();
		post.setEntity(new StringEntity(s1.replaceFirst("\\?", rid + ""), "UTF-8"));
		HttpResponse response = client.execute(post);
		String s = readResponse(response);
		post.completed();
		Document doc = DocumentHelper.parseText(s);
		return doc.getRootElement().attributeValue("authid");
	}
	
	private HttpPost getPost() {
		HttpPost post = new HttpPost(server); 
		post.setHeader("Host", host);
		post.setHeader("Content-Type", "text/xml; charset=utf-8");
		return post;
	}
	
	private String readResponse(HttpResponse response) throws Exception {
		InputStreamReader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
		char[] chars = new char[4096];
		StringBuilder sb = new StringBuilder();
		int length = -1;
		while ((length = reader.read(chars)) > 0)
			sb.append(chars, 0, length);
		return sb.toString();
	}
	
	private String  s1 = "<body xmlns:xmpp='urn:xmpp:xbosh' rid='?' to='DOMAIN' xml:lang='en' wait='60' " +
			"hold='1' content='text/xml; charset=utf-8' ver='1.6' xmpp:version='1.0'></body>";
	private String  s2 = "<body rid='?' sid='?'><auth xmlns='urn:ietf:params:xml:ns:xmpp-sasl' mechanism='DIGEST-MD5'/></body>";
	private String  s3 = "<body rid='?' sid='?'><response xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>?</response></body>";
	private String  s4 = "<body xmlns:xmpp='urn:xmpp:xbosh' rid='?' sid='?' to='DOMAIN' xml:lang='en' xmpp:restart='true'></body>";
	private String  s5 = "<body rid='?' sid='?'><iq type='set' id='_bind_auth_2'><bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'/>" +
			"</iq></body>";
	private String  s6 = "<body rid='?' sid='?'><iq type='set' id='_session_auth_2'><session xmlns='urn:ietf:params:xml:" +
			"ns:xmpp-session'/></iq></body>";
	
	private String MESSAGE = "<body rid=\"?\" sid=\"?\"><message to=\"?@DOMAIN\" type=\"chat\"><body>?</body></message></body>";
}