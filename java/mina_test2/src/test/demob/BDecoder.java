package test.demob;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class BDecoder extends CumulativeProtocolDecoder {
	
	private String name;
	private boolean showLog;

	public BDecoder(String name, boolean showLog) {
		super();
		this.name = name;
		this.showLog = showLog;
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		// TODO Auto-generated method stub
		Msg msg = new Msg(in.getInt(), in.getInt(), in.getInt(), 
				in.getString(Charset.forName(Constants.DEFAULTCHARSET).newDecoder()));
		out.write(msg);
		if(showLog) {
			String log = msg.toString() + "\t" + name + " decode";
			System.out.println(log);
		}
		return true;
	}

}
