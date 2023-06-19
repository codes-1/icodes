package cn.com.codes.framework.transmission.events;

import cn.com.codes.framework.transmission.events.RequestEvent;

public class BusiRequestEvent extends RequestEvent {

	

	private String webContext ;
	public BusiRequestEvent(String userID) {
		super(userID);
	}
	
	public BusiRequestEvent(String userID,String blhName){
		super(userID,blhName);
	}
	 
	public BusiRequestEvent() {
		super();
	}

	public String getWebContext() {
		return webContext;
	}
	public void setWebContext(String webContext) {
		this.webContext = webContext;
	}
}
