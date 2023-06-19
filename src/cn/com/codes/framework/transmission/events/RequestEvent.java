package cn.com.codes.framework.transmission.events;

import cn.com.codes.framework.transmission.dto.BaseDto;
import cn.com.codes.framework.transmission.events.IEvent;

public abstract class RequestEvent implements IEvent {

	/**
	 * 用户ID 
	 */
	protected String userID;

	protected String dealMethod;

	private BaseDto dto; 
	
	/**
	 * 保留下来，再不用Spring ioc 用它得到BLH
	 */
	protected String blhName;
	
	public RequestEvent() {
	}

	public RequestEvent(String userID) {
		
		this.userID = userID;
	}

	public RequestEvent(String userID,String blhName) {
		
		this.userID = userID;
		this.blhName = blhName;
	}
	public String getDealMethod() {
		return dealMethod;
	}

	public void setDealMethod(String dealMethod) {
		this.dealMethod = dealMethod;
	}

	public String getBlhName() {
		return blhName;
	}

	public void setBlhName(String blhName) {
		this.blhName = blhName;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public BaseDto getDto() {
		return dto;
	}

	public void setDto(BaseDto dto) {
		this.dto = dto;
		//dto.setOperationId(userID);
	}

	
}
