package test;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import com.dbay.apns4j.IApnsService;
import com.dbay.apns4j.impl.ApnsServiceImpl;
import com.dbay.apns4j.model.ApnsConfig;
import com.dbay.apns4j.model.Feedback;
import com.dbay.apns4j.model.Payload;

/**
 * 苹果消息推送工具
 * 依赖apns4j.jar、json-simple.jar
 * 需要有苹果的证书
 * 
 * 项目地址
 * https://github.com/RamosLi/dbay-apns-for-java
 * 
 * @author Administrator
 *
 */
public class Test_apns {

	static String password = "5211643", keyStoreFile = "push_app.p12", 
	token = "468f52d69db332fa742f4624830101e3ae39a7664d97d677f722b841808091f4";
	
	static IApnsService service;
	
	static {
		ApnsConfig config = new ApnsConfig();
		InputStream is = Test_apns.class.getClassLoader().getResourceAsStream(keyStoreFile);
		config.setKeyStore(is);
		//开发环境
		config.setDevEnv(true);
//		config.setDevEnv(false);
		config.setPassword(password);
		config.setPoolSize(1);
		config.setTimeout(10 * 1000);
		service = ApnsServiceImpl.createInstance(config);
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		String x = null;
		while(true) {
			x = s.nextLine();
			Payload payload = new Payload();
			//通知文字
			payload.setAlert(x);
			//app图标上显示数字
			payload.setBadge(1);
			//通知声音
			payload.setSound("default");
			service.sendNotification(token, payload);
			
			List<Feedback> list = service.getFeedbacks();
			if (list != null && list.size() > 0) {
				for (Feedback feedback : list) {
					System.out.println(feedback.getDate() + " " + feedback.getToken());
				}
			}
			System.out.println("-------------");
		}
		
	}
}
