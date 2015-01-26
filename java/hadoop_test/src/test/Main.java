package test;

import org.apache.hadoop.util.ProgramDriver;

import test.yarn.YarnTest;

public class Main {

	public static void main(String[] args) {
		int exitCode = -1;
	    ProgramDriver pgd = new ProgramDriver();
	    try {
	      pgd.addClass("test", YarnTest.class, 
	                   "A map/reduce program that counts the words in the input files.");
	      exitCode = pgd.run(args);
	    }
	    catch(Throwable e){
	      e.printStackTrace();
	    }
	    
	    System.exit(exitCode);
	}

}
