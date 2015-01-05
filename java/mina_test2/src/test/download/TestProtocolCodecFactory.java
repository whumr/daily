package test.download;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

public class TestProtocolCodecFactory extends DemuxingProtocolCodecFactory {
	
	private final ProtocolEncoder encoder = new TestEncoder();
    private final ProtocolDecoder decoder = new TestDecoder();
    

    /**
     * {@inheritDoc}
     */
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }

    /**
     * {@inheritDoc}
     */
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }
}
