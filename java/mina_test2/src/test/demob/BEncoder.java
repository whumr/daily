package test.demob;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class BEncoder extends ProtocolEncoderAdapter {

	private String name;
	private boolean showLog;
	
	public BEncoder(String name, boolean showLog) {
		super();
		this.name = name;
		this.showLog = showLog;
	}

	@Override
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		// TODO Auto-generated method stub
		Msg msg = (Msg)message;
		if(showLog) {
			String log = msg.toString() + "\t" + name + " encode";
			System.out.println(log);
		}
		IoBuffer buffer = IoBuffer.allocate(0, false);   
        buffer.setAutoExpand(true);   
        buffer.putInt(msg.getCode());
        buffer.putInt(msg.getFuserId());
        buffer.putInt(msg.getTuserId());
        buffer.putString(msg.getMsg(), Charset.forName(Constants.DEFAULTCHARSET).newEncoder());
        buffer.flip();   
        out.write(buffer);
	}
}
