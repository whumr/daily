package test.download;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class MinaClient {

	/**
	 * @param args
	 *            zxp
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IoConnector connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(5000);
		connector.setHandler(new MinaClient().new client());
		// config.setConnectTimeout(1);
		// DefaultIoFilterChainBuilder d= config.getFilterChain();
		// d.addLast("codec", new ProtocolCodecFilter(new
		// TextLineCodecFactory(Charset.forName("UTF-8"))));
		InetSocketAddress address = new InetSocketAddress("127.0.0.1", MinaServer.PORT);
//		connector.getFilterChain().addLast("a", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		connector.getFilterChain().addLast("a", new ProtocolCodecFilter(new TestProtocolCodecFactory()));
		connector.connect(address);
	}

	public class client implements IoHandler {

		public void exceptionCaught(IoSession arg0, Throwable arg1)
				throws Exception {
			// TODO Auto-generated method stub
			System.out.println("客户端异常:" + arg1);
			arg0.close(true);
		}

		public void messageReceived(IoSession arg0, Object arg1)
				throws Exception {
			// TODO Auto-generated method stub
			SendMessages send = (SendMessages) arg1;
			System.out.println("client receive message..." + send.getMsg());
//			if (send.msg != null) {
//				send.setMsg("0");
//				this.sendMessages(arg0, send);
//			}
		}

		public void messageSent(IoSession arg0, Object arg1) throws Exception {
			// TODO Auto-generated method stub
			// 不能用这个方法发送信息，要不死循环
			// arg0.getw
		}

		public void sessionClosed(IoSession arg0) throws Exception {
			// TODO Auto-generated method stub
			System.out.println("client closed...");
			arg0.close(true);
		}

		public void sessionCreated(IoSession arg0) throws Exception {
			// TODO Auto-generated method stub
			System.out.println("client created...");
			// arg0.getFilterChain()
		}

		public void sessionIdle(IoSession arg0, IdleStatus arg1)
				throws Exception {
			// TODO Auto-generated method stub
			System.out.println("client idle...");
//			if (arg1 == IdleStatus.BOTH_IDLE) {
//				this.messageSent(arg0, "空闲测试");
//				SendMessages send = new SendMessages();
//				send.setMsg("aaaaa");
//				this.sendMessages(arg0, send);
//			}
		}

		public void sessionOpened(IoSession arg0) throws Exception {
			// TODO Auto-generated method stub
			System.out.println("client open...");
//			System.out.println("客户端打开");
			// 10秒内没有读写就设置为空闲通道
			arg0.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

			// 自定义包解析
			// ProtocolCodecFactory codec=new TestProtocolCodecFactory();
			// arg0.getFilterChain().addFirst("test", new
			// ProtocolCodecFilter(codec));
		}

		@SuppressWarnings("unused")
		private void sendMessages(IoSession arg0, Object arg1) {
			System.out.println("sendMessages:" + ((SendMessages)arg1).msg);
			arg0.write(arg1);
		}

	}
}
