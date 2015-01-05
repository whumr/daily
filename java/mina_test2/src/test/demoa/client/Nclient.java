package test.demoa.client;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import test.demoa.code.ACodeFactory;
import test.demoa.server.MinaTimeServer;

public class Nclient extends IoHandlerAdapter {

	public static void main(String[] args) {
		Nclient nc = new Nclient();
		SocketConnector connector = new NioSocketConnector();   
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ACodeFactory("client")));   
        connector.setHandler(nc);   
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", MinaTimeServer.PORT);
        connector.connect(address);
	}
	
	public void messageReceived(IoSession session, Object message) throws Exception {   
        System.out.println("messageReceived:" + message.toString());
	}
	
	public void sessionCreated(IoSession session) throws Exception {
		System.out.println("sessionCreated");
		session.write("sessionCreated");
    }
	
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("sessionOpened");
		session.write("sessionOpened");
    }
}
