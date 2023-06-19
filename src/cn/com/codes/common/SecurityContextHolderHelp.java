package cn.com.codes.common;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import cn.com.codes.framework.app.services.BaseService;
import cn.com.codes.framework.security.SecurityPrivilege;
import cn.com.codes.framework.security.Visit;
import cn.com.codes.framework.security.VisitUser;
import cn.com.codes.framework.security.filter.SecurityContext;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.object.SimpleUser;
import cn.com.codes.common.SecurityContextHolderHelp;




public class SecurityContextHolderHelp {

	private static Logger log = Logger.getLogger(SecurityContextHolderHelp.class);
	
	
	public static HttpSession getSession(){
		return SecurityContextHolder.getContext().getRequest().getSession();
	}

	public static String getCompanyId() {
		SecurityContext sc = SecurityContextHolder.getContext();
		Visit visit = sc.getVisit();
		if(visit==null){
			return null;
		}
		VisitUser user = visit.getUserInfo(VisitUser.class);
		return user.getCompanyId();
	}
	
	public static boolean isPersionChg(String userId){
		
		Map<String, Date> persionListener = SecurityPrivilege.getPrivilegeChgUser().get(SecurityContextHolderHelp.getCompanyId());
		if(persionListener==null){
			return false;
		}
		if(persionListener.containsKey(userId)){
			return true;
		}
		return false ;
		
	}
	
	public static synchronized void removePersionChgUser(String userId){
		 Map<String, Date> persionListener = SecurityPrivilege.getPrivilegeChgUser().get(SecurityContextHolderHelp.getCompanyId());
		 if(persionListener==null){
			 return;
		 }
		 persionListener.remove(userId);
	}

	public static synchronized void notifyPriviListener(Set<SimpleUser> users){
		try{
			SecurityPrivilege.notifyPriviListener(users);
		}catch(Exception e){
			log.error(e);
		}
	}
	public static String getUserId() {
		SecurityContext sc = SecurityContextHolder.getContext();
		Visit visit = sc.getVisit();
		VisitUser user = visit.getUserInfo(VisitUser.class);
		return  user.getId();
	}
	
	public static Integer getUserIsAdmin(){
		SecurityContext sc = SecurityContextHolder.getContext();
		Visit visit = sc.getVisit();
		if (visit == null) {
			return 0;
		}
		VisitUser user = visit.getUserInfo(VisitUser.class);
		return user.getIsAdmin() == null ? 0 : user.getIsAdmin().intValue();
	}
	
	public static String getLoginName(){
		SecurityContext sc = SecurityContextHolder.getContext();
		if(sc==null||sc.getUserInfo()==null){
			return null;
		}
		return sc.getUserInfo().getLoginName();
	}
	
	public static String getMyRealName(){
		SecurityContext sc = SecurityContextHolder.getContext();
		return sc.getVisit().getUserInfo(VisitUser.class).getName();
	}
	
	public static String getMyRealDisplayName(){
		SecurityContext sc = SecurityContextHolder.getContext();
		VisitUser user = sc.getVisit().getUserInfo(VisitUser.class);
		return user.getLoginName() + "(" + user.getName() + ")";
	}
	
	public static VisitUser getMyUserInfo(){
		SecurityContext sc = SecurityContextHolder.getContext();
		return sc.getVisit().getUserInfo(VisitUser.class);
	}
	
	public static String getCurrTaksId(){
		SecurityContext sc = SecurityContextHolder.getContext();
		return sc.getVisit().getTaskId();
	}
	public static void setCurrTaksId(String taskId){
		SecurityContext sc = SecurityContextHolder.getContext();
		sc.getVisit().setTaskId(taskId);
	}
	public static void setAttr(String key, Object obj){
		SecurityContext sc = SecurityContextHolder.getContext();
		sc.setAttr(key, obj);
	}
	
	public static String getUpDirectory(){
		SecurityContext sc = SecurityContextHolder.getContext();
		Visit visit = sc.getVisit();
		return visit.getUpDirectory();
	}
	

	public static <T> T getEntryUnderTask(Class<T>  clasz,Object id,String idProName){
		
		BaseService mypmBaseService = null;//(BaseService)Context.getInstance().getBean("myPmbaseService");
		String hql  = "from " +clasz.getSimpleName() +"where " +idProName +"?" ;
		List list = mypmBaseService.findByHql(hql, id);
		return (T)list.get(0);
	}
	
	//分析度量
	public static String getCurrTaksProName(){
		SecurityContext sc = SecurityContextHolder.getContext();
		return sc.getVisit().getAnalyProjectName();
	}
	public static void setCurrTaksProName(String analyProjectName){
		SecurityContext sc = SecurityContextHolder.getContext();
		sc.getVisit().setAnalyProjectName(analyProjectName);
	}
	
	public static String getCurrTaksAnalyProNum(){
		SecurityContext sc = SecurityContextHolder.getContext();
		return sc.getVisit().getAnalyProNum();
	}
	public static void setCurrTaksAnalyProNum(String analyproNum){
		SecurityContext sc = SecurityContextHolder.getContext();
		sc.getVisit().setAnalyProNum(analyproNum);
	}
	
	
}
