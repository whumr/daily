package t;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class TalkThread extends Thread {

	private String name, passwd, to, msg;
	private HttpUtil httpUtil;
	
	private boolean flag = true;
	private int count = 1000;
	private long rid = 4133813207L;
	
	public TalkThread() {
		super();
	}

	public TalkThread(String name, String passwd, HttpUtil httpUtil) {
		super();
		this.name = name;
		this.passwd = passwd;
		this.httpUtil = httpUtil;
	}

	@Override
	public void run() {
		try {
			HttpClient client = HttpClientBuilder.create().build();
			String sid = httpUtil.getSid(client, rid++);
			httpUtil.connect(client, name, passwd, sid, rid);
			rid = rid + 5;
			synchronized (client) {
				if (to == null || msg == null)
					client.wait(10);
			}
			while (flag) {
				if (count > 0) {
					httpUtil.sendMessage(client, to, msg + "\t" + System.currentTimeMillis(), sid, rid++);
					count--;
					Thread.sleep(100);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setTo(String to) {
		this.to = to;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
