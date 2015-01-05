package test.demoa.code;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class AEncoder extends ProtocolEncoderAdapter {

	private String name;
	
	public AEncoder(String name) {
		super();
		this.name = name;
	}

	@Override
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		// TODO Auto-generated method stub
		String msg = message + "\t" + name + " encode--> " + new SimpleDateFormat("HH:mm:ss").format(new Date());
		IoBuffer buffer = IoBuffer.allocate(msg.length(), false);   
        buffer.setAutoExpand(true);   
        buffer.putString(msg, Charset.forName("UTF-8").newEncoder());
        buffer.flip();   
        out.write(buffer);
	}

}
