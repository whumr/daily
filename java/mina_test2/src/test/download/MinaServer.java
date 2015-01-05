package test.download;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class MinaServer {
	
	public static final int PORT = 9656;

	/**
	 * @param args
	 * @throws Exception
	 *             zxp
	 * 
	 */
	public static void main(String[] args) throws Exception {
		IoAcceptor acceptor = new NioSocketAcceptor();
//		IoAcceptorConfig config = new SocketAcceptorConfig();
		// config.setDisconnectOnUnbind(true);
//		DefaultIoFilterChainBuilder d = config.getFilterChain();
		// d.addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(
		// Charset.forName("UTF-8"))));
		// d.addFirst("a", );
		InetSocketAddress address = new InetSocketAddress(PORT);
		acceptor.setHandler(new MinaServer().new server());
		acceptor.getFilterChain().addFirst("a", new ProtocolCodecFilter(new TestProtocolCodecFactory()));
//		acceptor.getFilterChain().addFirst("a", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
//		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.bind(address);
	}

	public class server implements IoHandler {

		public void exceptionCaught(IoSession arg0, Throwable arg1)
				throws Exception {
			// TODO Auto-generated method stub
			System.out.println("异常:" + arg1);
			arg1.fillInStackTrace();
		}

		public void messageReceived(IoSession arg0, Object arg1)
				throws Exception {
			// TODO Auto-generated method stub
			SendMessages send = (SendMessages) arg1;
			System.out.println("收到信息:" + send.msg);
		}

		// 信息发送成功后触发的事件
		public void messageSent(IoSession arg0, Object arg1) throws Exception {
			// TODO Auto-generated method stub
			System.out.println("发送信息:" + ((SendMessages)arg1).msg);
		}

		public void sessionClosed(IoSession arg0) throws Exception {
			// TODO Auto-generated method stub
			System.out.println("session 关闭");
			arg0.close(true);
		}

		public void sessionCreated(IoSession arg0) throws Exception {
			// TODO Auto-generated method stub
			System.out.println("session 建立");
		}

		public void sessionIdle(IoSession arg0, IdleStatus arg1)
				throws Exception {
			// TODO Auto-generated method stub
			System.out.println("session 空闲");
			if (arg1 == IdleStatus.BOTH_IDLE) {
				// //自定义发送类
				// SendMessages send= new SendMessages();
				// send.setMsg("Hi!客户端你空闲咯！");
				// this.sendMessage(arg0, send);
			}
		}

		public void sessionOpened(IoSession arg0) throws Exception {
			// TODO Auto-generated method stub
			System.out.println("session 打开");
			// arg0.setIdleTime(IdleStatus.BOTH_IDLE, 10);

			// 发送条测试消息
			SendMessages send = new SendMessages();
			send.setMsg("Hi!客户端你好!");
			this.sendMessage(arg0, send);

		}

		private void sendMessage(IoSession arg0, Object arg1) {
			arg0.write(arg1);
		}
	}

}