package cn.com.codes.bugManager.dto;

import cn.com.codes.framework.transmission.dto.BaseDto;
import cn.com.codes.object.BugShortMsg;

public class BugShortMsgDto extends BaseDto {

	private BugShortMsg shortMsg = new BugShortMsg();
	private int reSetMsgLink;
	private String taskId ;
	public BugShortMsg getShortMsg() {
		return shortMsg;
	}

	public void setShortMsg(BugShortMsg shortMsg) {
		this.shortMsg = shortMsg;
	}

	public int getReSetMsgLink() {
		return reSetMsgLink;
	}

	public void setReSetMsgLink(int reSetMsgLink) {
		this.reSetMsgLink = reSetMsgLink;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	



}
