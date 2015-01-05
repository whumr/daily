package com.blossom.test;

public class Xx {

	public static void main(String[] args) {
		C c = new C();
		System.out.println(c.getX());
	}

}

class P {
	protected String x = "p xx";
	
	public String getX() {
		return x;
	}
}

class C extends P {
	protected String x = "C xx";
	public String getX() {
		return super.x;
	}
}