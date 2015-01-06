package test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import test.ext.A;

/**
 * 测试spring的aop，依赖两个aspect,spring,cglib,
 * @author Administrator
 *
 */
public class Test_spring_aop {

	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext("config\\test.xml");
		A a = (A)context.getBean("a");
		a.x();
	}
}
