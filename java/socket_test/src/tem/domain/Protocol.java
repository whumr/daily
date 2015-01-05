package tem.domain;


public class Protocol {

	private int version = 0x101;
	private long code = 0x1001;
	private long fUserId = 0x0;
	private long tUserId = 0x0;
	int length;
	private String content;
	
	public Protocol() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Protocol(int version, long code, long fUserId, long tUserId,
			int length, String content) {
		super();
		this.version = version;
		this.code = code;
		this.fUserId = fUserId;
		this.tUserId = tUserId;
		this.length = length;
		this.content = content;
	}
	
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public long getCode() {
		return code;
	}
	public void setCode(long code) {
		this.code = code;
	}
	public long getfUserId() {
		return fUserId;
	}
	public void setfUserId(long fUserId) {
		this.fUserId = fUserId;
	}
	public long gettUserId() {
		return tUserId;
	}
	public void settUserId(long tUserId) {
		this.tUserId = tUserId;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(Integer.toHexString(version)).append("#")
		.append(Long.toHexString(code)).append("#")
		.append(fUserId).append("#")
		.append(tUserId).append("#")
		.append(length).append("#")
		.append(content);
		return sb.toString();
	}
}
