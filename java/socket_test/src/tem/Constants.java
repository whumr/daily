package tem;

public class Constants {

	public static final int port = 8131;
	public static final String charset = "UTF-8";
	
	public static final class OP_CODE {
		public static final long REGIST = 0x1;
		public static final long LOGON = 0x2;
		public static final long LOGOUT = 0x3;
	}
}
