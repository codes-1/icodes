package cn.com.codes.object;

import java.util.Date;

import cn.com.codes.object.User;

public class BugShortMsg implements java.io.Serializable {

	private Long msgId;
	private String taskId;
	private Long bugId;
	private String senderId;
	private String message;
	private Date insDate;
	private Integer recipCd;
	private User sender;

	public BugShortMsg() {
	}

	public BugShortMsg(String taskId, Long bugId) {
		this.taskId = taskId;
		this.bugId = bugId;
	}

	public BugShortMsg(String taskId, Long bugId, String senderId,
			String message, Date insDate) {
		this.taskId = taskId;
		this.bugId = bugId;
		this.senderId = senderId;
		this.message = message;
		this.insDate = insDate;
	}

	public Long getMsgId() {
		return this.msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Long getBugId() {
		return this.bugId;
	}

	public void setBugId(Long bugId) {
		this.bugId = bugId;
	}

	public String getSenderId() {
		return this.senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getInsDate() {
		return this.insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = insDate;
	}

	public Integer getRecipCd() {
		return recipCd;
	}

	public void setRecipCd(Integer recipCd) {
		this.recipCd = recipCd;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

}