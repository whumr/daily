package test;

public class Test_finally {

	public static void main(String[] args) {
		/**	结果
t11111
ttttt
aa
		 */
		System.out.println(t());
	}

	static String t() {
		try {
			return t1();
		} finally {
			System.out.println("ttttt");
		}
	}
	
	static String t1() {
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("t11111");
		return "aa";
	}

}
