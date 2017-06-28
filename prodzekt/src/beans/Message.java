package beans;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Message implements Serializable{
	private String sender;
	private String receiver;
	private String content;
	
	private boolean seen;
	private boolean isReport;
	private int messageId;

	
	public Message(String sender, String receiver, String content) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
		this.seen = false;
		this.isReport = false;
		this.messageId = hashCode();
	}
	
	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isSeen() {
		return seen;
	}
	public void setSeen(boolean seen) {
		this.seen = seen;
	}
	public boolean isReport() {
		return isReport;
	}
	public void setReport(boolean isReport) {
		this.isReport = isReport;
	}
	
	
	

}
