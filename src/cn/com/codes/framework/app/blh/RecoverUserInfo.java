package cn.com.codes.framework.app.blh;

import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.RequestEvent;

public interface RecoverUserInfo {

	public void recoverUserInfo(RequestEvent req) throws BaseException ;
	public void recoverUserInfo(String userId)  ;
}
