package content;

public class Messages {
	private String msgcontent;
	private String msgStatus;
	private int userid;
	
	public Messages (String content, String status, int userId) {
		this.msgcontent = content;
		this.msgStatus = status;
		this.userid = userId;
	}
	
	public int getUserId() {
		return userid;
	}
	
	public void setUserId(int userid) {
		this.userid = userid;
	}
	
	public String getMsgcontent() {
		return msgcontent;
	}
	
	public void setMsgcontent(String msgcontent) {
		this.msgcontent = msgcontent;
	}
	
	public String getMsgStatus() {
		return msgStatus;
	}
	
	public void setMsgStatus(String msgStatus) {
		this.msgStatus = msgStatus;
	}
	
}
