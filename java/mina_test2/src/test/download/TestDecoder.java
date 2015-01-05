package test.download;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class TestDecoder implements ProtocolDecoder {


	@Override
	public void finishDecode(org.apache.mina.core.session.IoSession session,
			ProtocolDecoderOutput out) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws Exception {
		// TODO Auto-generated method stub
		byte[] b = new byte[in.limit()];
		in.get(b);
		System.out.println("decode:" + in.position());
		SendMessages send = new SendMessages();
		String s = new String(b, "UTF-8");
		send.setMsg(s);
		out.write(send);
	}

}
