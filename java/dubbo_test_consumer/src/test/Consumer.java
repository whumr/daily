package test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.test.dubbo.TestService;

public class Consumer {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("consumer.xml");
        context.start();
 
        TestService demoService = (TestService)context.getBean("demoService");
        String hello = demoService.sayHello("world");
 
        System.out.println( hello );
        
        context.close();
	}

}
