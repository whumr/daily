package tem.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import tem.domain.Protocol;

public class ProtocolUtil {

	public static void write(OutputStream out, Protocol protocol) throws IOException {
		write(out, protocol, false);
	}

	public static void write(OutputStream out, Protocol protocol, boolean closeIO) throws IOException {
		if(protocol != null && out != null) {
			DataOutputStream dout = new DataOutputStream(out);
			dout.writeInt(protocol.getVersion());
			dout.writeLong(protocol.getCode());
			dout.writeLong(protocol.getfUserId());
			dout.writeLong(protocol.gettUserId());
			dout.writeInt(protocol.getLength());
			dout.writeUTF(protocol.getContent());
			dout.flush();
			if(closeIO)
				dout.close();
		}
	}

	public static Protocol read(InputStream in) throws IOException {
		return read(in, false);
	}

	public static Protocol read(InputStream in, boolean closeIO) throws IOException {
		if(in != null) {
			Protocol protocol = new Protocol();
			DataInputStream din = new DataInputStream(in);
			
			int version = din.readInt();
			protocol.setVersion(version);
			System.out.println(version);
			
			long code = din.readLong();
			protocol.setCode(code);
			System.out.println(code);
			
			long fUserId = din.readLong();
			protocol.setfUserId(fUserId);
			System.out.println(fUserId);
			
			long tUserId = din.readLong();
			protocol.settUserId(tUserId);
			System.out.println(tUserId);
			
			int length = din.readInt();
			protocol.setLength(length);
			System.out.println(length);
			
			String content = din.readUTF();
			protocol.setContent(content);
			System.out.println(content);
			
			if(closeIO)
				din.close();
			return protocol;
		}
		return null;
	}
}
