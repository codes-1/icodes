package cn.com.codes.framework.app.blh;

import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.RequestEvent;
import cn.com.codes.framework.transmission.events.ResponseEvent;

public abstract class BaseBizLogicHandler {

	
	public BaseBizLogicHandler() {

	}

	public abstract ResponseEvent performTask(RequestEvent requestevent)
			throws BaseException;

}
