package test;

import java.net.InetSocketAddress;
import java.util.Random;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class MinaClient {

	public static void main(String[] args) {
		//Create TCP/IP connection     
        NioSocketConnector connector = new NioSocketConnector();     
             
        //设定这个过滤器将一行一行(/r/n)的读取数据     
        connector.getFilterChain().addLast("myChin", new ProtocolCodecFilter(new TextLineCodecFactory()));     
             
        //客户端的消息处理器：一个SamplMinaServerHander对象     
        connector.setHandler(new MinaClientHandler());     
             
        //set connect timeout     
        connector.setConnectTimeoutMillis(3000L);     
             
        //连接到服务器：     
        ConnectFuture cf = connector.connect(new InetSocketAddress("localhost", 1234));     
             
        //Wait for the connection attempt to be finished.     
        cf.awaitUninterruptibly();     
             
        cf.getSession().getCloseFuture().awaitUninterruptibly();     
             
        connector.dispose();     
	}

}

class MinaClientHandler extends IoHandlerAdapter {  
    // 当客户端连接进入时  
    @Override  
    public void sessionOpened(final IoSession session) throws Exception {  
        System.out.println("sessionOpened");  
        new Thread(new Runnable() {
			public void run() {
				Random r = new Random();
				while (true) {
					try {
						int op = 1 + r.nextInt(4);
						int n1 = r.nextInt(100);
						int n2 = 1 + r.nextInt(100);
						session.write(op + " " + n1 + " " + n2);
						Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
        System.out.println("thread start");
    }  
  
    @Override  
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {  
        System.out.println("客户端发送信息异常....");  
        cause.printStackTrace();
    }  
  
    // 当客户端发送消息到达时  
    @Override  
    public void messageReceived(IoSession session, Object message) throws Exception {  
        System.out.println("服务器返回的数据：" + message.toString());  
    }  
  
    @Override  
    public void sessionClosed(IoSession session) throws Exception {  
        System.out.println("客户端与服务端断开连接.....");  
    }  
  
    @Override  
    public void sessionCreated(IoSession session) throws Exception {  
        System.out.println("sessionCreated");  
    }  
  
}  
