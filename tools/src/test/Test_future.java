package test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class Test_future {

	public static void main(String[] args) {
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		ExecutorService executor = Executors.newSingleThreadExecutor();
		FutureTask<String> future = new FutureTask<String>(
				new Callable<String>() {// 使用Callable接口作为构造参数
					public String call() {
						// 真正的任务在这里执行，这里的返回值类型为String，可以为任意类型
						try {
							System.out.println("1111before sleep.........");
							Thread.sleep(5000);
							System.out.println("111end sleep.........");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						return new SimpleDateFormat("111 yyyy-MM-dd HH:mm:ss").format(new Date());
					}
				});
		System.out.println("step 1.........");
		executor.execute(future);
		System.out.println("step 2.........");
		try {
			System.out.println(future.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println("step 3.........");
		Future<String> f = executor.submit(
				new Callable<String>() {

					@Override
					public String call() throws Exception {
						// 真正的任务在这里执行，这里的返回值类型为String，可以为任意类型
						try {
							System.out.println("222before sleep.........");
							Thread.sleep(5000);
							System.out.println("222end sleep.........");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						return new SimpleDateFormat("222 yyyy-MM-dd HH:mm:ss").format(new Date());
					}
					
				}
		);
		System.out.println("step 4.........");
		try {
			System.out.println(f.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println("step 5.........");
		executor.shutdown();
	}
}