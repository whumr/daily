package test;

public class Test_partnet {

	public static void main(String[] args) {
		Child c = new Child();
		c.print();
	}
	
}

class Parent {
	protected String name = "parent";
	
	public void print() {
		System.out.println(this.name);
	}
}

class Child extends Parent {
	
	public Child() {
		this.name = "child";
	}

//	protected String name = "child";
}
