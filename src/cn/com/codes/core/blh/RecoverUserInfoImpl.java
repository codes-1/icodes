package cn.com.codes.core.blh;

import org.apache.log4j.Logger;

import cn.com.codes.framework.app.blh.RecoverUserInfo;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.hibernate.HibernateGenericController;
import cn.com.codes.framework.security.SecurityPrivilege;
import cn.com.codes.framework.security.Visit;
import cn.com.codes.framework.security.VisitUser;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.transmission.events.RequestEvent;
import cn.com.codes.object.User;
import cn.com.codes.core.blh.RecoverUserInfoImpl;

public class RecoverUserInfoImpl implements RecoverUserInfo {

	private static Logger logger = Logger.getLogger(RecoverUserInfoImpl.class);

	public HibernateGenericController hibernateGenericController;

	public SecurityPrivilege securityPrivilege;

	public void recoverUserInfo(String userId){

	}
	/**
	 * 如session 丢�??恢复session中用户信??	 * 
	 * @param req
	 */
	public void recoverUserInfo(RequestEvent req) throws BaseException {

	
	}

	public HibernateGenericController getHibernateGenericController() {
		return hibernateGenericController;
	}

	public void setHibernateGenericController(
			HibernateGenericController hibernateGenericController) {
		this.hibernateGenericController = hibernateGenericController;
	}

	public SecurityPrivilege getSecurityPrivilege() {
		return securityPrivilege;
	}

	public void setSecurityPrivilege(SecurityPrivilege securityPrivilege) {
		this.securityPrivilege = securityPrivilege;
	}


}
