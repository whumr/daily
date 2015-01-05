package test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class Server {

	static int PORT = 1234;
	
	public static void main(String[] args) throws IOException {
		NioSocketAcceptor acceptor = new NioSocketAcceptor();
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();


        chain.addLast("codec", new ProtocolCodecFilter(new CodecFactory()));

        // Bind
        acceptor.setHandler(new Handler());
        acceptor.bind(new InetSocketAddress(PORT));

        System.out.println("Listening on port " + PORT);
	}
}

class Handler extends IoHandlerAdapter {
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		System.out.println("messageReceived : " + message.toString());
		session.write(message);
	}
	
	@Override
    public void exceptionCaught(IoSession session, Throwable e) {
        if (e instanceof IOException)
        	session.close(true);
        else
        	e.printStackTrace();
    }
}

class CodecFactory implements ProtocolCodecFactory {

	private Encoder encoder = new Encoder();
	private Decoder decoder = new Decoder();
	
	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return decoder;
	}

	class Decoder extends ProtocolDecoderAdapter {
		@Override
		public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
			byte[] buffer = new byte[1024];
			StringBuilder sb = new StringBuilder();
			while(in.hasRemaining()) {
				int length = Math.min(in.remaining(), 1024);
				in.get(buffer, 0, length);
				sb.append(new String(buffer, 0, length));
			}
			String[] ss = sb.toString().trim().split(" ");
			Entity e = new Entity(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]), Integer.parseInt(ss[2]));
			out.write(e);
		}
	}
	
	class Encoder extends ProtocolEncoderAdapter {
		@Override
		public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
			if (message instanceof Entity) {
				String s = ((Entity)message).toFullString() + "\n";
				CharsetEncoder ce = Charset.defaultCharset().newEncoder();
				IoBuffer buf = IoBuffer.allocate(s.length()).setAutoExpand(true);
				buf.putString(s, ce);
				buf.flip();
				out.write(buf);
				System.out.println("send : " + s);
			}
		}
	}
}

class Entity {
	private static final int ADD = 1, SUBTRACT = 2, MULTIPLY = 3, DIVIDE = 4;
	
	private int op, n1, n2;

	public Entity(int op, int n1, int n2) {
		this.op = op;
		this.n1 = n1;
		this.n2 = n2;
	}

	public int result() {
		switch(op) {
			case ADD :
				return n1 + n2;
			case SUBTRACT :
				return n1 - n2;
			case MULTIPLY :
				return n1 * n2;
			case DIVIDE :
				return n1 / n2;
		}
		return 0;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder().append(n1);
		if (op == ADD)
			sb.append(" + ");
		else if (op == SUBTRACT)
			sb.append(" - ");
		else if (op == MULTIPLY)
			sb.append(" * ");
		else if (op == DIVIDE)
			sb.append(" / ");
		sb.append(n2);
		return sb.toString();
	}
	
	public String toFullString() {
		return toString() + " = " + result();
	}
}
