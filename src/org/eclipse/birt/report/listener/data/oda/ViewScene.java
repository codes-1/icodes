package org.eclipse.birt.report.listener.data.oda;

import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.RequestEvent;

public interface ViewScene {

	public boolean recoverUserInfo(RequestEvent req) throws BaseException ;
}
