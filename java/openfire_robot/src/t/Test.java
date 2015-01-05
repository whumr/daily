package t;

public class Test {
	
	static String HOST = "192.168.1.88:7070";
	static String DOMAIN = "mr.cn";
	
	public static void main(String[] args) throws Exception {
		HttpUtil httpUtil = HttpUtil.getInstance();
		httpUtil.setDomain(DOMAIN);
		httpUtil.setHost(HOST);
		
//		HttpClient client = httpUtil.connect("mr", "mr");
//		httpUtil.sendMessage(client, "mr2", "sssssssssss");
		
		TalkThread talk1 = new TalkThread("mr", "mr", httpUtil);
		talk1.setTo("mr2");
		talk1.setMsg("test from mr");
		
		TalkThread talk2 = new TalkThread("mr1", "mr", httpUtil);
		talk2.setTo("mr2");
		talk2.setMsg("test from mr1");
		
		talk1.start();
		talk2.start();
	}

}
