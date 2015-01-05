package test;

//import java.io.ByteArrayOutputStream;
//import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

//import sun.tools.attach.HotSpotVirtualMachine;
//import sun.tools.attach.WindowsAttachProvider;

public class Test_vm {
	
	/**
	 * 需要  jdk\lib\tools.jar, jdk\jre\bin\attach.dll
	 * 
	 * @param args
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();  
		String name = bean.getName();  
		System.out.println(name);
		int index = name.indexOf('@');  
		String pid = name.substring(0, index);  
		//这里要区分操作系统  
//		HotSpotVirtualMachine machine = (HotSpotVirtualMachine)new WindowsAttachProvider().attachVirtualMachine(pid);  
//		InputStream is = machine.heapHisto();  
//		  
//		
//		ByteArrayOutputStream os = new ByteArrayOutputStream();  
//		int readed;  
//		byte[] buff = new byte[1024];  
//		while((readed = is.read(buff)) > 0)  
//		    os.write(buff, 0, readed);  
//		is.close();  
//		  
//		machine.detach();  
//		System.out.println(os);  
	}
}
