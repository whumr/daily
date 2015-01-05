package test.demob;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Vector;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class Client extends IoHandlerAdapter {

	private int id;
	private SocketConnector connector;
	private InetSocketAddress address;
	private List<Msg> list;
	
	public Client(int id) {
		super();
		this.id = id;
		list = new Vector<Msg>();
		connector = new NioSocketConnector();   
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new CFactory("client_" + id)));   
        connector.setHandler(this);   
        address = new InetSocketAddress("127.0.0.1", Constants.PORT);
        list.add(new Msg(0,id, 0, ""));
        connector.connect(address);
	}
	
	public void send(Msg msg) throws Exception {
		list.add(msg);
		connector.connect(address);
	}
	
	public void messageReceived(IoSession session, Object message) throws Exception {   
        System.out.println("messageReceived:" + message.toString());
	}
	
	public void sessionCreated(IoSession session) throws Exception {
		System.out.println("sessionCreated");
		synchronized(list) {
			if(list != null && !list.isEmpty()) {
				session.write(list.get(0));
				list.remove(0);
			}
		}
    }
	
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("sessionOpened");
    }

	public int getId() {
		return id;
	}
}
