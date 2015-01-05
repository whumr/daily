package test.demob;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class CFactory implements ProtocolCodecFactory {
	
	private ProtocolDecoder decoder;
	private ProtocolEncoder encoder;
	
	public CFactory(String name) {
		this(name, false);
	}
	
	public CFactory(String name, boolean showLog) {
		decoder = new BDecoder(name, showLog);
		encoder = new BEncoder(name, showLog);
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		return encoder;
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		return decoder;
	}

}
