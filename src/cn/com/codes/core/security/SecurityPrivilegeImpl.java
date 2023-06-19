package cn.com.codes.core.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.framework.common.CryptUtil;
import cn.com.codes.framework.common.config.PropertiesBean;
import cn.com.codes.framework.common.util.Context;
import cn.com.codes.framework.jms.log.LogProducer;
import cn.com.codes.framework.security.SecurityPrivilege;
import cn.com.codes.framework.security.Visit;
import cn.com.codes.framework.security.VisitUser;
import cn.com.codes.framework.security.filter.SecurityContext;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.object.Function;
import cn.com.codes.object.Role;
import cn.com.codes.object.SingleTestTask;
import cn.com.codes.object.User;



public class SecurityPrivilegeImpl extends SecurityPrivilege {

	private static Logger logger = Logger.getLogger(SecurityPrivilegeImpl.class);
	private LogProducer logMessageProducer;
	private static StringBuffer loadUserHql = new StringBuffer();
//	private static StringBuffer ProjectTask = new StringBuffer();
	private static StringBuffer menuHql = new StringBuffer();
	private static StringBuffer priviHql = new StringBuffer();
	private static PropertiesBean conf ;
	static{
		loadUserHql.append("select new  User(id,name ,loginName,companyId,password, isAdmin, myHome, docUserId) ");
		loadUserHql.append(" from User where loginName=? and status=1 ");
		
//		ProjectTask.append("select new  SingleTestTask(taskId,proNum,proName,devDept,testPhase,psmName,planStartDate,planEndDate,status,planDocName) ");
//		ProjectTask.append(" from SingleTestTask where psmId=? ");/*and status=1*/
		
		menuHql.append(" select distinct  new Function (p.functionId,p.parentId,p.functionName,p.level,p.url ,p.seq )");
		menuHql.append(" from User u join  u.roleList r  join r.privilege p where  u.id=? and u.companyId=? ")
		.append("and p.isleaf<>1 and p.pageType ='0' order by p.level,p.parentId,p.seq");
		
		priviHql.append("select distinct p.secutiryUrl ");
		priviHql.append(" from User u join  u.roleList r  join r.privilege p where u.id=? and ")
		.append("u.companyId=? and p.isleaf=1 and p.secutiryUrl is not null  and p.pageType ='0'" );
		
	}
	

	public SecurityPrivilegeImpl() {
		conf = (PropertiesBean) Context.getInstance().getBean("ContextProperties");

	}


	public Set<String> getCheckedPrivilege() {

		StringBuffer sql = new StringBuffer();
		sql.append("select  distinct f.SECURITY_URL from  T_FUNCTION f where SECURITY_URL is not null  and isleaf =1");
		List<Object> powerList = hibernateGenericController.findBySql(
				sql.toString(), null);
		Set<String> needCheckUrls = new HashSet<String>();
		if (logger.isInfoEnabled()) {
			logger.info(sql.toString());
		}
		for (Object urlInfo : powerList) {
			if(urlInfo==null||urlInfo.toString().trim().equals("")){
				continue;
			}
			String[] urlMethod = this.protectUrlParse(urlInfo.toString().trim());
			for(String url:urlMethod){
				if (!needCheckUrls.contains(url.trim())) {
					needCheckUrls.add(url.trim());
				}
			}
		}
		return needCheckUrls;
	}

	private String[] protectUrlParse(String protectUrl){
		if(protectUrl.indexOf("!")<0){
			return new String[]{protectUrl};
		}
		String[] urlSplit = protectUrl.split("!");
		String action = urlSplit[0];
		String[] urlMethod = urlSplit[1].split(";");
		
		for(int i=0; i<urlMethod.length; i++){
			urlMethod[i] =action +"!"+ urlMethod[i];
		}
		return urlMethod;
	}

	public void setUserPrivilege(VisitUser user) {
		setPrivilege(user);
	}

	protected void setPrivilege(VisitUser user) {
		
		List<Object> powerList = getUserPrivilege(user.getId());
		Set<String> userPrivileges = new HashSet<String>();
		if(powerList == null){
			user.setPrivilege(userPrivileges);
			return ;
		}

		for(Object urlInfo : powerList){
			if(urlInfo==null||urlInfo.toString().trim().equals("")){
				continue;
			}
			String[] urlMethod = this.protectUrlParse(urlInfo.toString().trim());
			for(String url:urlMethod){
				userPrivileges.add(url);
			}
			
		}
		//System.out.println(userPrivileges);
		user.setPrivilege(userPrivileges);
	}

	public void postChecked(boolean authentication) {

	}

	private Visit getVisit() {

		return SecurityContextHolder.getContext().getVisit();
	}

	public  boolean loginWithIpChk(String loginName,String pwd){
		StringBuffer hql = new StringBuffer();
		hql.append("select u ");
		hql.append(" from User u left join fetch u.roleList r where u.loginName=? and u.password =? and u.status=1 ");
		List<User> list = hibernateGenericController.findByFreePara(hql.toString(), loginName,this.encodePwd(pwd));
		if(list==null||list.isEmpty()){
			return false;
		}
		User user = list.get(0);
		if(!this.ipChk(user.getRoleList())){
			return false;
		}
		Visit visit = SecurityContextHolder.getContext().getVisit();
		if(visit !=null&&!visit.getUserInfo(VisitUser.class).getLoginName().equals(loginName)){
			visit = new Visit();//这是不是本人重新登录
		}  
		if(visit==null){
			visit = new Visit();
		}
		VisitUser vu = user.copy2VisitUser();
		SecurityContext sec = SecurityContextHolder.getContext();
		String mailBugId = SecurityContextHolder.getContext().getRequest().getParameter("mailBugId");
		if(mailBugId==null||"".equals(mailBugId.trim())){
			sec.setSessionAttr("myHome", user.getMyHome()==null?"":user.getMyHome());
		}
		sec.setVisit(visit);
		this.setUserPrivilege(vu);
		visit.setUserInfo(vu);
		this.writeLoginLog(loginName,sec);
		return true;
	}
	
	private boolean ipChk(List<Role> roleList){
		if(roleList==null||roleList.isEmpty()||roleList.size()==0){
			return true;
		}
		StringBuffer ipsSb = new StringBuffer();
		for(Role role:roleList){
			if(role.getAccessIp()!=null&&!"".equals(role.getAccessIp().trim()))
			ipsSb.append(";").append(role.getAccessIp());
		}
		if(ipsSb.toString().equals("")){
			return true;
		}
		String ips = ipsSb.toString().substring(1);
		String[] ipsArr = ips.split(";");
		String fromId = SecurityContextHolder.getContext().getIpAddr();
		for(String ip :ipsArr){
			if(ip.trim().equals(fromId)){
				return true;
			}
		}
		return false;
	}
	public boolean login(String loginName, String pwd) {
		
		List<User> users = hibernateGenericController.findByFreePara(loadUserHql.toString(), loginName);
		if(users==null||users.isEmpty()){
			return false;
		}
		if (users.size() > 0) {
			if (users.get(0).getPassword().equals(this.encodePwd(pwd))) {
				//重新登录时，加载菜单方式不一样，这里要取原来的visit 以得到加载菜单标记
				Visit visit = SecurityContextHolder.getContext().getVisit();
				if(visit !=null&&!visit.getUserInfo(VisitUser.class).getLoginName().equals(loginName)){
					visit = new Visit();//这是不是本人重新登录
				} 
				if(visit !=null){
					//同一客户端,登录且有同一session存在 
					SecurityContextHolder.getContext().setAttr("reptLogin", "");
				}  
				if(visit==null){
					visit = new Visit();
				}
				User user = users.get(0);
				String userId = user.getId();
				VisitUser vu = user.copy2VisitUser();
//				Integer status = 0;
				SecurityContext sec = SecurityContextHolder.getContext();
				String mailBugId = SecurityContextHolder.getContext().getRequest().getParameter("mailBugId");
//				List<SingleTestTask> singleTestTasks = hibernateGenericController.findByProperties(SingleTestTask.class, new String[]{"psmId","status"}, new Object[]{userId,status});
				String sqls = "SELECT COUNT(tac.ACTORID) as num from t_task_useactor tac WHERE tac.USERID='"+userId+"'";
				List<Map<String,Object>> findBySqlByJDBC = hibernateGenericController.findBySqlByJDBC(sqls);
				if(mailBugId==null||"".equals(mailBugId.trim())){
					sec.setSessionAttr("myHome", user.getMyHome()==null?"":user.getMyHome());
				}
				
				Map<String, Object> map = findBySqlByJDBC.get(0);
				Integer number =  Integer.parseInt(String.valueOf(map.get("num"))); 
				/*if("admin".equals(user.getLoginName()))*/
                if(user.getIsAdmin()==1||user.getIsAdmin()==2){
                	String hql1 = "select new cn.com.codes.object.SingleTestTask(stt.taskId,stt.proName,stt.proNum,stt.taskProjectId) from SingleTestTask stt where 1=1 and stt.taskId is not null";
            		List<SingleTestTask> singleTestTasks = hibernateGenericController.findByFreePara(hql1);
            		visit.setFlag("1");
            		if(singleTestTasks.size() == 0){
            			visit.setHaveProject("0");
            		}else{
            			visit.setHaveProject("1");
            		}
                	
				}else{
					if (number>0) {
						visit.setFlag("1");
					}else {
						visit.setFlag("0");
					}	
					visit.setHaveProject("1");
				}
				
				sec.setVisit(visit);
				this.setUserPrivilege(vu);
				visit.setUserInfo(vu);
				this.writeLoginLog(loginName,sec);
				return true;
			}
			return false;
		} else {
			return false;
		}
	}

	public  boolean loginById(String id){
		StringBuffer hql = new StringBuffer();
		hql.append("select new  User(id,name ,loginName,companyId,password,isAdmin,myHome,docUserId) ");
		hql.append(" from User where id=? and status=1 ");
		List<User> users = hibernateGenericController.findByFreePara(loadUserHql.toString(), id);
		if(users==null||users.isEmpty()){
			return false;
		}
		User user = users.get(0);
		VisitUser vu = user.copy2VisitUser();
		SecurityContext sec = SecurityContextHolder.getContext();
		Visit visit = new Visit();
		sec.setVisit(visit);
		visit.setUserInfo(vu);
		return true;
	}

	
	private String encodePwd(String pwd) {
		//return pwd;
		return CryptUtil.cryptString(pwd);
	}

	//用HQL查询时，慢很多，所以用原生SQL查了 主要是用HQL要一妙半，用SQL只要不到500ms
	private List<Function> getCacheMenu(String userId,String compId){
		List<Function> menus  =hibernateGenericController.findByFreePara(menuHql.toString(),userId,compId);
		return menus;
	}

	private List getCachePrivilege(String userId,String compId){
		List menus  =hibernateGenericController.findByFreePara(priviHql.toString(),userId,compId);
		return menus; 
	}
	
	
	private List<Function> getUserMenu() {

		StringBuffer sb = new StringBuffer();
		sb.append("select distinct " );
		sb.append(" f.FUNCTIONID as itemId,f.PARENTID as paretId,f.FUNCTIONNAME as itemName," );
		sb.append(" f.LEVELNUM as levelNum,f.url ,f.SEQ" );
		sb.append("  from T_FUNCTION f ,");
		sb.append("  (select  distinct rf.FUNCTIONID from   T_ROLE_FUNCTION_REAL rf ,");
		sb.append("      (select distinct ur.ROLEID from   T_USER_ROLE_REAL ur ,T_USER u where u.COMPANYID=? and ur.USERID=u.ID)  myrole ");
		sb.append("  where rf.ROLEID= myrole.ROLEID )  myfunction ");
		sb.append("where  f.FUNCTIONID=myfunction.FUNCTIONID AND f.ISLEAF<>1 AND  f.PAGE ='0' ORDER BY f.LEVELNUM,f.PARENTID,f.SEQ");

		List<Object[]> powerList = hibernateGenericController.findBySql(sb.toString(), null, SecurityContextHolderHelp.getCompanyId());
		List<Function> menus = new ArrayList<Function>();
		for(Object[] menu :powerList){
			Function function = new Function();
			function.setFunctionId((String)menu[0]);
			function.setParentId((String)menu[1]);
			function.setFunctionName((String)menu[2]);
			function.setLevel(Integer.valueOf(menu[3].toString()));
			if(menu[4] != null){
				function.setUrl(menu[4].toString());
			}
			menus.add(function);
		}
		return menus;
	}
	public boolean securityCheck(String url, Set<String> needCheckUrls) {
		if (needCheckUrls.size() == 0) {
			return true;
		}
		//项目列表无需校验
		if("projectAction!listProjects".equals(url)){
			return true;
		}
		Visit visit = getVisit();
		if (visit == null) {
			return true;
		}
		VisitUser vu = null;
		if(visit!=null){
			vu = visit.getUserInfo();
		}
		//如果是管理人员，WBS不检查
		if(("taskAction!taskLists".equals(url)||"testTaskManagerAction!flwSetInit".equals(url)||
				"bugManagerAction!loadMyBug".equals(url)||
				"bugManagerAction!loadAllMyBug".equals(url))&&visit != null&&vu!=null&&vu.getIsAdmin()>=1){
			return true;
		}

		if (needCheckUrls.contains(url)) {
			//Visit vist = visit;
//			VisitUser user = vist.getUserInfo(VisitUser.class);
//			String companyId = user.getCompanyId();
//			String userId = vist.getUserInfo().getId();
//			Map<String, Date> compCheUser = privilegeChgUser.get(companyId);
//			if (compCheUser != null && compCheUser.get(userId) != null) {
//				this.refreshUserPrivilege(user);
//			}
			if (!visit.getUserInfo().getPrivilege().contains(url)) {
				if (logger.isInfoEnabled()) {
					logger.info("您没有[" + url + "]的操作权限");
				}
				return false;
			}
		}

		return true;
	}

	/**
	 * 
	 * @author liuyg 功能：这里实现是否是无需安全校验的接口方法
	 * @param className
	 * @return
	 */
	private boolean isOmitMethod(String className) {
		
		return true;
	}

	private void refreshUserPrivilege(VisitUser user) {
		String userId = user.getId();
		this.setUserPrivilege(user);
		String companyId = user.getCompanyId();
		Map<String, Date> compCheUser = privilegeChgUser.get(companyId);
		compCheUser.remove(userId);
	}

//	class removeChgUser implements Runnable {
//
//		public void run() {
//			if(logger.isInfoEnabled()){
//				logger.info("removeChgUser Thread is running =========");
//			}
//			while(true){
//				if (SecurityPrivilege.getPrivilegeChgUser().size() > 0) {
//					Set set = SecurityPrivilege.getPrivilegeChgUser().entrySet();
//					for (Iterator<Map<String, Date>> it = set.iterator(); it
//							.hasNext();) {
//						Map.Entry companyEntry = (Map.Entry)it.next();
//						Map<String, Date> companyMap = (Map<String, Date>)companyEntry.getValue();
//						String companyKey = (String)companyEntry.getKey();
//						Set currCompanySet = companyMap.entrySet();
//						for (Iterator<Map<String, Date>> companyIt = currCompanySet
//								.iterator(); companyIt.hasNext();) {
//							Map.Entry entry = (Map.Entry) companyIt.next();
//							String key = (String) entry.getKey();
//							Date putDate = (Date) entry.getValue();
//		                    Date now = new Date();
//							if ((now.getTime() - putDate.getTime()) >= 1800000) {
//								companyIt.remove();
//								logger.info("removeChgUser Thread is remove =========");
//							}
//						}
//						if(companyMap.isEmpty()){
//							it.remove();
//						}
//					}
//				}
//				try {
//					logger.info("removeChgUser Thread is sleep =========");
//					Thread.sleep(6000);
//				} catch (InterruptedException e) {
//					logger.error(e.getMessage());
//				}
//			}
//		}
//
//	}
//	public static void main(String[] args){
//		SecurityPrivilegeImpl sc = new SecurityPrivilegeImpl();
//		
//		Thread addThread = new Thread(sc.new addChgUser());
//		Thread remThread = new Thread(sc.new removeChgUser());
//		addThread.start();
//		remThread.start();
//		
//	}
//	class addChgUser implements Runnable {
//
//		public void run() {
//			if(logger.isInfoEnabled()){
//				logger.info("addChgUser Thread is running =========");
//			}
//			int i = 0;
//			while(i<10){
//				 //Map<String, Map<String, Date>>
//					Map<String, Date> map  = new HashMap<String, Date>();
//					SecurityPrivilege.getPrivilegeChgUser().put(String.valueOf(i), map);
//					for(int l=0; l<50;l++){
//						map.put(String.valueOf(l), new Date());
//						logger.info("addChgUser Thread is add =========");
//					}
//				
//				try {
//					logger.info("addChgUser Thread is sleep =========");
//					Thread.sleep(300);
//				} catch (InterruptedException e) {
//					logger.error(e.getMessage());
//					e.printStackTrace();
//				}
//					i++;
//			}
//		}
//
//	}
	private void writeLoginLog(String loginName,SecurityContext sec){
//		String recFlag = conf.getProperty("mypm.recLogin.log");
//		if(!"true".equals(recFlag)){
//			return;
//		}
//		SysLog log = new OperaLog();
//		log.setLogType(0);
//		log.setOperDesc(loginName);
//		log.setOperSummary("用户登录");
//		log.setOperDate(new Date());
//		log.setAccessIp(sec.getIpAddr());
//		logMessageProducer.log(log);
	}

	private  List getUserPrivilege(String userId){
		Visit vist = SecurityContextHolder.getContext().getVisit();
		if(vist!=null&&vist.isMenuLoad()){
			return this.getCachePrivilege(userId,  SecurityContextHolderHelp.getCompanyId());
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select  distinct f.SECURITY_URL from ");
		sql.append("   T_FUNCTION f ,");
		sql.append("  (select distinct rf.FUNCTIONID from ");
		sql.append("  T_ROLE_FUNCTION_REAL rf ,");
		sql.append("  (select ur.ROLEID from ");
		sql.append("    T_USER_ROLE_REAL ur ,");
		sql.append("   T_USER  u");
		sql.append("   where u.ID='").append(userId).append("' and ")
				.append("ur.USERID=u.ID)  myrole");
		sql.append("   where rf.ROLEID= myrole.ROLEID )  myfunction ");
		sql.append("    where f.FUNCTIONID=myfunction.FUNCTIONID  AND f.SECURITY_URL is not null and f.page !=1 ");
		if (logger.isInfoEnabled()) {
			logger.info(sql.toString());
		}
		List<Object> powerList = hibernateGenericController.findBySql(sql
				.toString(), null);
		return powerList;
	}
	public  void scanPriviNotice(){
		if (privilegeChgUser.size() > 0) {
			Set set = privilegeChgUser.entrySet();
			for (Iterator<Map<String, Date>> it = set.iterator(); it
					.hasNext();) {
				Map.Entry companyEntry = (Map.Entry)it.next();
				Map<String, Date> companyMap = (Map<String, Date>)companyEntry.getValue();
				String companyKey = (String)companyEntry.getKey();
				Set currCompanySet = companyMap.entrySet();
				for (Iterator<Map<String, Date>> companyIt = currCompanySet
						.iterator(); companyIt.hasNext();) {
					Map.Entry entry = (Map.Entry) companyIt.next();
					String key = (String) entry.getKey();
					Date putDate = (Date) entry.getValue();
                    Date now = new Date();
					if ((now.getTime() - putDate.getTime()) >= (super.getScanInterval()*60*1000)) {
						companyIt.remove();
					}
				}
				if(companyMap.isEmpty()){
					it.remove();
				}
			}
		}
	}
	public  List getMenu(String userId){
		return null;
	}


	public LogProducer getLogMessageProducer() {
		return logMessageProducer;
	}

	public void setLogMessageProducer(LogProducer logMessageProducer) {
		this.logMessageProducer = logMessageProducer;
	}


}
