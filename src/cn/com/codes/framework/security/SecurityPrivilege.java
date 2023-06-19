package cn.com.codes.framework.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.framework.hibernate.HibernateGenericController;
import cn.com.codes.object.SimpleUser;

public abstract class SecurityPrivilege {

	/**
	 * Map<String,Map<String,Date>> Map<公司ID,Map<用户ID,权限变更时间>>
	 */
	protected final static Map<String, Map<String, Date>> privilegeChgUser = new ConcurrentHashMap<String, Map<String, Date>>();
	
	private int scanInterval ;
	public HibernateGenericController hibernateGenericController;

	public abstract Set<String> getCheckedPrivilege();

	public abstract boolean securityCheck(String url,
			Set<String> needCheckUrls) ;
	
	public abstract void setUserPrivilege(VisitUser user);

	public abstract boolean login(String loginName,String pwd);
	
	public abstract boolean loginById(String id);
	
	
	public abstract boolean loginWithIpChk(String loginName,String pwd);
	
	public HibernateGenericController getHibernateGenericController() {
		return hibernateGenericController;
	}

	
	public void setHibernateGenericController(
			HibernateGenericController hibernateGenericController) {
		this.hibernateGenericController = hibernateGenericController;
	}
	
	public static void notifyPriviListener(String userIds){
		if(userIds==null||"".equals(userIds.trim())){
			return;
		}
		String compId = SecurityContextHolderHelp.getCompanyId();
		String[] userArr = userIds.split(" ");
		for(String id:userArr){
			if(privilegeChgUser.get(compId)==null){
				 Map<String, Date> priviMap = new HashMap<String, Date>();
				 privilegeChgUser.put(compId, priviMap);
			}else{
				privilegeChgUser.get(compId).put(id, new Date());
			}
		}
	}
	
	public static void notifyPriviListener(Set<SimpleUser> users){
		if(users==null||users.isEmpty()){
			return;
		}
		String compId = SecurityContextHolderHelp.getCompanyId();
		for(SimpleUser user:users){
			if(privilegeChgUser.get(compId)==null){
				 Map<String, Date> priviMap = new HashMap<String, Date>();
				 privilegeChgUser.put(compId, priviMap);
			}else{
				privilegeChgUser.get(compId).put(user.getId(), new Date());
			}
		}		
	}
	
	
	/**
	 * scan privilegeChgUser if privi is change and time out will remove it form privilegeChgUser
	 */
	public abstract void scanPriviNotice();
	

	public int getScanInterval() {
		return scanInterval;
	}

	public void setScanInterval(int scanInterval) {
		this.scanInterval = scanInterval;
	}

	public static Map<String, Map<String, Date>> getPrivilegeChgUser() {
		return privilegeChgUser;
	}
}
