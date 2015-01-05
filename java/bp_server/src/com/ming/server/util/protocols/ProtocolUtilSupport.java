package com.ming.server.util.protocols;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.ming.server.common.domain.Constants.PROTOCOL_TYPE;
import com.ming.server.common.domain.Protocol;
import com.ming.server.util.ProtocolFactory;
import com.ming.server.util.ProtocolUtil;

public class ProtocolUtilSupport implements ProtocolUtil {

	private static Logger logger = Logger.getLogger(ProtocolUtilSupport.class);
	
	protected String version;
	
	public byte readType(DataInputStream in) throws IOException {
		return in.readByte();
	}

	public int readVersion(DataInputStream in) throws IOException {
		return in.readInt();
	}
	
	@Override
	public Protocol readProtocol(DataInputStream in) throws IOException {
		// TODO Auto-generated method stub
		return readProtocol(in, false);
	}
	
	private void readData(DataInputStream in, Protocol protocol, String field, int type) throws Exception {
		switch(type) {
			case PROTOCOL_TYPE.INT: 
				setValue(protocol, field, in.readInt(), int.class);
				break;
			case PROTOCOL_TYPE.LONG: 
				setValue(protocol, field, in.readLong(), long.class);
				break;
			case PROTOCOL_TYPE.UFF_STRING: 
				setValue(protocol, field, in.readUTF(), String.class);
				break;
			case PROTOCOL_TYPE.SHORT: 
				setValue(protocol, field, in.readShort(), short.class);
				break;
			case PROTOCOL_TYPE.BYTE: 
				setValue(protocol, field, in.readByte(), byte.class);
				break;
			case PROTOCOL_TYPE.CHAR: 
				setValue(protocol, field, in.readChar(), char.class);
				break;
			case PROTOCOL_TYPE.BOOLEAN: 
				setValue(protocol, field, in.readBoolean(), boolean.class);
				break;
			case PROTOCOL_TYPE.DOUBLE: 
				setValue(protocol, field, in.readDouble(), double.class);
				break;
			case PROTOCOL_TYPE.FLOAT: 
				setValue(protocol, field, in.readFloat(), float.class);
				break;
		}
	}
	
	private void setValue(Protocol protocol, String field, Object value, Class<? extends Object> type) throws Exception {
		Method method = null;
		try {
			method = protocol.getClass().getDeclaredMethod(field, type);
		} catch (NoSuchMethodException e) {
			try {
				method = protocol.getClass().getSuperclass().getDeclaredMethod(field, type);
			} catch (NoSuchMethodException e1) {
				logger.error(protocol.getClass().getName() + " has no method " + field, e1);
			}
		} 
		method.invoke(protocol, value);
	}

	@Override
	public Protocol readProtocol(DataInputStream in, boolean closeIO)
			throws IOException {
		// TODO Auto-generated method stub
		Protocol protocol = null;
		try {
			protocol = ProtocolFactory.getProtocol(version, readType(in));
			String[] fieldsOrder = protocol.getFieldOrder()[0];
			int[] fieldsType = protocol.getFieldType();
			for(int i = 0; i < fieldsOrder.length; i ++) {
				readData(in, protocol, fieldsOrder[i], fieldsType[i]);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(closeIO)
				in.close();
			return null;
		}
		if(closeIO)
			in.close();
		return protocol;
	}

	@Override
	public void writeProtocol(DataOutputStream out, Protocol protocol)
			throws IOException {
		// TODO Auto-generated method stub
		writeProtocol(out, protocol, false);
	}

	@Override
	public void writeProtocol(DataOutputStream out, Protocol protocol,
			boolean closeIO) throws IOException {
		// TODO Auto-generated method stub
		if(protocol != null) {
			out.writeInt(protocol.getVersion());
			out.writeByte(protocol.getType());
			String[] fieldsOrder = protocol.getFieldOrder()[1];
			int[] fieldsType = protocol.getFieldType();
			try {
				for(int i = 0; i < fieldsOrder.length; i ++) {
					writeData(out, protocol, fieldsOrder[i], fieldsType[i]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(closeIO) {
					out.flush();
					out.close();
				}
			}
		}
	}
	
	private void writeData(DataOutputStream out, Protocol protocol, String field, int type) throws Exception {
		switch(type) {
			case PROTOCOL_TYPE.INT: 
				out.writeInt((Integer)writeValue(protocol, field));
				break;
			case PROTOCOL_TYPE.LONG: 
				out.writeLong((Long)writeValue(protocol, field));
				break;
			case PROTOCOL_TYPE.UFF_STRING: 
				out.writeUTF((String)writeValue(protocol, field));
				break;
			case PROTOCOL_TYPE.SHORT: 
				out.writeShort((Short)writeValue(protocol, field));
				break;
			case PROTOCOL_TYPE.BYTE: 
				out.writeByte((Byte)writeValue(protocol, field));
				break;
			case PROTOCOL_TYPE.CHAR: 
				out.writeChar((Integer)writeValue(protocol, field));
				break;
			case PROTOCOL_TYPE.BOOLEAN: 
				out.writeBoolean((Boolean)writeValue(protocol, field));
				break;
			case PROTOCOL_TYPE.DOUBLE: 
				out.writeDouble((Double)writeValue(protocol, field));
				break;
			case PROTOCOL_TYPE.FLOAT: 
				out.writeFloat((Float)writeValue(protocol, field));
				break;
		}
	}
	
	private Object writeValue(Protocol protocol, String field) throws Exception {
		Method method = null;
		try {
			method = protocol.getClass().getDeclaredMethod(field);
		} catch (NoSuchMethodException e) {
			try {
				method = protocol.getClass().getSuperclass().getDeclaredMethod(field);
			} catch (NoSuchMethodException e1) {
				logger.error(protocol.getClass().getName() + " has no method " + field, e1);
				return null;
			}
		} 
		return method.invoke(protocol);
	}

	@Override
	public void setVersion(String version) {
		// TODO Auto-generated method stub
		this.version = version;
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return version;
	}
}
