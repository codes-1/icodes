package cn.com.codes.msgManager.dto;

import java.util.Set;

import cn.com.codes.framework.transmission.dto.BaseDto;
import cn.com.codes.object.BroadcastMsg;
import cn.com.codes.object.User;

public class CommonMsgDto extends BaseDto {

	private BroadcastMsg broMsg;
	private int isView;
	private String recipMailAddress;
	Set<User> recpiUser;
	public BroadcastMsg getBroMsg() {
		return broMsg;
	}

	public void setBroMsg(BroadcastMsg broMsg) {
		this.broMsg = broMsg;
	}

	public int getIsView() {
		return isView;
	}

	public void setIsView(int isView) {
		this.isView = isView;
	}

	public String getRecipMailAddress() {
		return recipMailAddress;
	}

	public void setRecipMailAddress(String recipMailAddress) {
		this.recipMailAddress = recipMailAddress;
	}

	public Set<User> getRecpiUser() {
		return recpiUser;
	}

	public void setRecpiUser(Set<User> recpiUser) {
		this.recpiUser = recpiUser;
	}
	

}
