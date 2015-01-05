package test.demoa.code;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class ADecoder extends CumulativeProtocolDecoder {
	
	private String name;

	public ADecoder(String name) {
		super();
		this.name = name;
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		// TODO Auto-generated method stub
		String msg = in.getString(Charset.forName("UTF-8").newDecoder());
		out.write(msg + "\t" + name + " decode--> " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
		return true;
	}

}
