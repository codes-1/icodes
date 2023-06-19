package cn.com.codes.framework.common;

import cn.com.codes.framework.security.UrlSecurityCheckInterceptor;

public class UserPrivilege{
	//private Log logger = LogFactory.getLog(UserPrivilege.class);
	
	public static boolean securityCheck(String url) {
//		if (getVisit() == null) {
//			return false;
//		}
//		Visit vist = getVisit();
//		VisitUser user = vist.getUserInfo(VisitUser.class);
//		if (!vist.getUserInfo().getPrivilege().contains(url.trim())) {
//			return false;
//		}
//		return true;
		return UrlSecurityCheckInterceptor.urlSecurityCheck(url);
	}
	
	
//	private static Visit getVisit() {
//		return SecurityContextHolder.getContext().getVisit();
//	}
}
