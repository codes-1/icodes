package cn.com.codes.msgManager.dto;

public class MailBean implements java.io.Serializable {

	String recip;
	String toCcMails;
	String msg;
	String subject;
	String[] attachPhName;
	boolean isMimeMail ;
	String recUserIds;
	public String getRecip() {
		return recip;
	}
	public void setRecip(String recip) {
		this.recip = recip;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String[] getAttachPhName() {
		return attachPhName;
	}
	public void setAttachPhName(String[] attachPhName) {
		this.attachPhName = attachPhName;
	}
	public boolean isMimeMail() {
		return isMimeMail;
	}
	public void setMimeMail(boolean isMimeMail) {
		this.isMimeMail = isMimeMail;
	}
	public String getRecUserIds() {
		return recUserIds;
	}
	public void setRecUserIds(String recUserIds) {
		this.recUserIds = recUserIds;
	}
	public String getToCcMails() {
		return toCcMails;
	}
	public void setToCcMails(String toCcMails) {
		this.toCcMails = toCcMails;
	}


}
