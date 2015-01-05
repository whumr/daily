package test.download;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class TestEncoder implements ProtocolEncoder {
	
	/**
	 * 编码消息 zxp
	 * 
	 */
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		SendMessages msg = (SendMessages) message;
		IoBuffer b = IoBuffer.allocate(msg.getmsgleng());
		b.setAutoExpand(true);
		// 编码消息
		msg.encoder(b);
		// b.putInt(97);
		// b.put("a".getBytes());
		b.flip();
		out.write(b);
	}

	/**
	 * 获取编码自定义消息类型(多个)
	 * 
	 */
	public Set<Class<?>> getMessageTypes() {
		Set<Class<?>> set = new HashSet<Class<?>>();
		set.add(SendMessages.class);
		// 返回指定映射的不可修改视图 即只读
		return Collections.unmodifiableSet(set);
	}

	@Override
	public void dispose(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
