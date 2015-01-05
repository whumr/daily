package test.download;

import org.apache.mina.core.buffer.IoBuffer;

public class SendMessages implements java.io.Serializable {

	private static final long serialVersionUID = -6939279318854085980L;
	
	public String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getmsgleng() {
		return msg.getBytes().length;
	}

	public void encoder(IoBuffer b) {
		b.put(msg.getBytes());
	}

}
