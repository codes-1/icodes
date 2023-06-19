package cn.com.codes.framework.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.framework.common.JsonUtil;
import cn.com.codes.framework.common.config.PropertiesBean;
import cn.com.codes.framework.common.util.Context;
import cn.com.codes.framework.hibernate.HibernateGenericController;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.object.Function;
import cn.com.codes.framework.security.MenuServlet;
import cn.com.codes.framework.security.SecurityPrivilege;
import cn.com.codes.framework.security.UrlSecurityCheckInterceptor;

public class MenuServlet extends HttpServlet {

	private Log logger = LogFactory.getLog(MenuServlet.class);
	private ServletConfig config;
	
	private static HibernateGenericController hibernateGenericController;
	private static StringBuffer menuHql = new StringBuffer();
	private static StringBuffer priviHql = new StringBuffer();
	private static StringBuffer menuSql = new StringBuffer();
	private static SecurityPrivilege securityPrivilege;
	private static PropertiesBean conf;
	//private static String repBugUrl = null;
	static{
		menuHql.append(" select distinct  new Function (p.functionId,p.parentId,p.functionName,p.level,p.url ,p.seq )");
		menuHql.append(" from User u join  u.roleList r  join r.privilege p where  u.id=? and u.companyId=? and p.pageType ='0' ")
		.append("and p.isleaf<>1 order by p.level,p.parentId,p.seq");
		
		priviHql.append("select distinct p.secutiryUrl ");
		priviHql.append(" from User u join  u.roleList r  join r.privilege p where u.id=? and ")
		.append("u.companyId=? and p.isleaf=1 and p.pageType ='0' and p.secutiryUrl is not null" );
		
//		menuSql.append("select distinct " );
//		menuSql.append(" f.FUNCTIONID as itemId,f.PARENTID as paretId,f.FUNCTIONNAME as itemName," );
//		menuSql.append(" f.LEVELNUM as levelNum,f.url ,f.SEQ" );
//		menuSql.append("  from T_FUNCTION f ,");
//		menuSql.append("  (select  distinct rf.FUNCTIONID from   T_ROLE_FUNCTION_REAL rf ,");
//		menuSql.append("      (select distinct ur.ROLEID from   T_USER_ROLE_REAL ur ,T_USER u where u.id=? and u.COMPANYID=? and ur.USERID=u.ID)  myrole ");
//		menuSql.append("  where rf.ROLEID= myrole.ROLEID )  myfunction ");
//		menuSql.append("where  f.FUNCTIONID=myfunction.FUNCTIONID AND f.ISLEAF<>1 and f.PAGE ='0' ORDER BY f.LEVELNUM,f.PARENTID,f.SEQ");
		menuSql.append(" select distinct  f.FUNCTIONID ,f.PARENTID ," );
		menuSql.append(" f.FUNCTIONNAME , f.LEVELNUM ,f.url ,f.SEQ" );
		menuSql.append(" from T_FUNCTION f" );
		menuSql.append(" INNER JOIN T_ROLE_FUNCTION_REAL rf ON F.FUNCTIONID = rf.FUNCTIONID" );
		menuSql.append(" inner join T_USER_ROLE_REAL ur on rf.ROLEID = ur.ROLEID" );
		menuSql.append(" inner join T_USER  u on ur.userid=u.id" );
		menuSql.append(" where u.id= ? and u.COMPANYID=? " );
		menuSql.append(" and  f.ISLEAF<>1" );
		menuSql.append(" and f.PAGE ='0' " );
		menuSql.append(" ORDER BY f.LEVELNUM,f.PARENTID,f.SEQ" );
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		if(request.getParameter("MypmTestPermission")!=null){
			if(securityPrivilege==null){
				securityPrivilege = (SecurityPrivilege)Context.getInstance().getBean("securityPrivilege");
			}
			String testUrl = request.getParameter("MypmTestPermission").replace("lygEn", "!");
			Set<String> needCheckUrls = UrlSecurityCheckInterceptor.needCheckUrls;
			if((testUrl==null||"".equals(testUrl))||needCheckUrls==null){
				response.getWriter().write("deny");
			}else{
				 if(!securityPrivilege.securityCheck(testUrl+".action", needCheckUrls)){
					 response.getWriter().write("deny");
				 }else{
					 response.getWriter().write("pass");
				 }
			}		
			return;
		}
		doPost(request, response);
	}

//	private void cryptPwd(HttpServletRequest request, HttpServletResponse response){
//		 PreparedStatement ps = null;
//		 Connection conn = null;
//		 String compId = "";
//		 Statement st = null;
//		 String url = conf.getProperty("config.db.url");
//		 String dbUser =PasswordTool.DecodePasswd(conf.getProperty("config.db.url"));
//		 String dbUserPwd = PasswordTool.DecodePasswd(conf.getProperty("config.db.url"));
//		 try {
//			conn = DriverManager
//				.getConnection(
//						url,
//						dbUser,dbUserPwd);
//			conn.setAutoCommit(false);
//			st = conn.createStatement();
//		} catch (SQLException e) {
//			logger.error(e);
//			e.printStackTrace();
//		}
//		
//	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		if(request.getParameter("reLogin")!=null){
			this.reLogin(request, response);
			return;
		}
		//登录成功后用异步方式先cache menu
		if(SecurityContextHolder.getContext().getVisit().isMenuLoad()&&request.getParameter("cacheMenu")!=null){
			if(conf.getProperty("config.db.driver")!=null&&conf.getProperty("config.db.driver").indexOf("mysql")>0){
				return;
			}
			String compId = SecurityContextHolderHelp.getCompanyId();
			String userId = SecurityContextHolderHelp.getUserId();
			this.cacheMenu(userId, compId);
			this.cachePrivilege(userId, compId);
			return;
		}else if(SecurityContextHolder.getContext().getVisit().isMenuLoad()&&request.getParameter("reload")!=null){
			//重加载时，从cache中取
			String compId = SecurityContextHolderHelp.getCompanyId();
			String userId = SecurityContextHolderHelp.getUserId();
			PrintWriter writer = response.getWriter();
			List<Function>  list = getDbdelegate().findByFreePara(menuHql.toString(),userId,compId);
			//System.out.println(JsonUtil.toJson(list));
			//writer.write(this.drawMenu(list,request));
			writer.write(JsonUtil.toJson(list));
			SecurityContextHolder.getContext().getVisit().setMenuLoad(true);
			return;
		}else if(SecurityContextHolder.getContext().getVisit().isMenuLoad()){
			//不是重加载，直接返回
			return;
		}
		String compId = SecurityContextHolderHelp.getCompanyId();
        String userId = SecurityContextHolderHelp.getUserId();
		List<Function>  list = getDbdelegate().findByFreePara(menuHql.toString(),userId,compId);
		PrintWriter writer = response.getWriter();
		//writer.write(this.drawMenu(list,request));
		
		writer.write(JsonUtil.toJson(list));
		SecurityContextHolder.getContext().getVisit().setMenuLoad(true);
		writer.flush();
		return;
	}

	private String drawMenu(List<Function> menu,HttpServletRequest request){
		
		if(menu==null){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("    var menu = new dhtmlXMenuObject(\"menuObj\");");
		sb.append("    menu.setImagePath(conextPath+").append("\"/dhtmlx/menu/codebase/imgs/\");");
		String lastTopId = "null";
		int topMenuCont = 0;
		for(int i=1; i<5; i++){
			String parentId = null;
			int position = 0;
			String beforeItemId = null ;
			for(Function fun : menu){
				if(fun.getLevel() == i ){
					if(fun.getLevel()==1){
						topMenuCont ++;
					}
					if(parentId == null){
						parentId = fun.getParentId();
					}else if(!parentId.equals(fun.getParentId())){
						position = 0;
						parentId = fun.getParentId(); 
					}else{
						position ++ ;
					}
					String itemId = fun.getFunctionId();
					String itemName = fun.getFunctionName();
					if(i == 1 && position ==0){
						beforeItemId = fun.getFunctionId(); 
						lastTopId = beforeItemId;
						sb.append("    menu.addNewSibling(null, \"").append(itemId).append("\", \"").append(itemName.trim()).append("\", false);");
					}else if( i == 1 && position >0){
						sb.append("    menu.addNewSibling(\"").append(beforeItemId).append("\", \"").append(itemId).append("\", \"").append(itemName.trim()).append("\", false);");
						beforeItemId = fun.getFunctionId(); 
						lastTopId = beforeItemId;
					}else{
						sb.append("    menu.addNewChild(\"").append(parentId).append("\","+position).append(", \"").append(itemId).append("\", \"").append(itemName.trim()).append("\", false);");
						position ++ ;
						sb.append("    menu.addNewSeparator(\"").append(itemId).append("\",\"sep_").append(itemId+"\");");
					}
					if( fun.getUrl() != null){
						sb.append("    menu.setUserData('").append(itemId).append("','action','").append(fun.getUrl().trim()).append("');");
					}
				}
			}			
		}
		if(lastTopId.equals("null")){		
			sb.append("    menu.addNewSibling(null").append(", \"").append("setMyHome").append("\", \"").append("定制首页").append("\", false);");			
			sb.append("    menu.addNewSibling('setMyHome'").append(", \"").append("mypmHome").append("\", \"").append("首页").append("\", false);");			
		}else{	
			sb.append("    menu.addNewSibling(\"").append(lastTopId).append("\", \"").append("setMyHome").append("\", \"").append("定制首页").append("\", false);");
			sb.append("    menu.addNewSibling(\"").append("setMyHome").append("\", \"").append("mypmHome").append("\", \"").append("首页").append("\", false);");
		}	
		sb.append("    menu.addNewChild(\"setMyHome\",0, \"sgleLst\", \"单列表\", false);"); 
		if(UrlSecurityCheckInterceptor.urlSecurityCheck("outLineAction!initList")){
			sb.append("    menu.addRadioButton(\"child\", \"sgleLst\", 0, \"testReqMgr\", \"测试需求管理\", \"ghome\", false, false);");
		}
		if(UrlSecurityCheckInterceptor.urlSecurityCheck("caseManagerAction!loadCase")){
			sb.append("    menu.addRadioButton(\"child\", \"sgleLst\", 1, \"testCaseMgr\", \"测试用例管理\", \"ghome\", false, false);");
		}		
		sb.append("    menu.addRadioButton(\"child\", \"sgleLst\", 2, \"allMySprHome\", \"缺陷管理\", \"ghome\", false, false);");
		
		sb.append("    menu.addRadioButton(\"child\", \"sgleLst\", 3, \"lastExeCaseHome\", \"我最近执行用例\", \"ghome\", false, false);");

		if(UrlSecurityCheckInterceptor.urlSecurityCheck("bugManagerAction!loadMyBug")){
			sb.append("    menu.addRadioButton(\"child\", \"sgleLst\", 4, \"sprHome\", \"当前项目缺陷管理\", \"ghome\", false, false);");
		}
		if(UrlSecurityCheckInterceptor.urlSecurityCheck("caseManagerAction!loadCase")){
			sb.append("    menu.addRadioButton(\"child\", \"sgleLst\", 5, \"caseHome\", \"当前用例列表\", \"ghome\", false, false);");
		}  
		sb.append("    menu.addRadioButton(\"child\", \"sgleLst\", 14, \"cancelLstHome\", \"\", \"ghome\", false, false);");
		sb.append("    menu.addNewSeparator(\"sgleLst\", \"sep_home\");");
	    sb.append("    menu.addNewChild(\"setMyHome\",2, \"mutLst\", \"多列表\", false);");
		sb.append("    menu.addNewChild(\"mutLst\",0, \"mutLstC\", \"\", false);"); 
		sb.append("    menu.addCheckbox(\"sibling\", \"mutLstC\", 0, \"selMsg\", \"我的消息\", false, false);"); 
		sb.append("    menu.addCheckbox(\"sibling\", \"selMsg\", 1, \"selBug\", \"我的BUG\", false, false);"); 
		sb.append("    menu.hideItem(\"mutLstC\");");  
		sb.append("    menu.hideItem(\"cancelLstHome\");");  
		sb.append("    menu.addNewSibling(\"").append("mypmHome").append("\", \"").append("help").append("\", \"").append("帮助").append("\", false);");	
		sb.append("    menu.addNewSibling(\"").append("help").append("\", \"").append("reptMypm").append("\", \"").append("向MYPM反馈").append("\", false);");	
		sb.append("    menu.addNewSibling(\"").append("reptMypm").append("\", \"").append("logOut").append("\", \"").append("退出").append("\", false);");	
		sb.append("    menu.addNewChild(\"help\",0, \"tutoria\", \"简易向导\", false);"); 
		sb.append("    menu.addNewChild(\"help\",1, \"mypmTestMgrManual\", \"用户手册\", false);"); 
		sb.append("    menu.addNewChild(\"help\",2, \"helpPro\", \"MYPM产品说明书\", false);");
		sb.append("    menu.addNewChild(\"help\",4, \"solut\", \"MYPM解决方案\", false);");
		sb.append("    menu.addNewChild(\"help\",3, \"repMypmBug\", \"向MYPM反馈|建议\", false);");
		sb.append("    menu.addNewChild(\"help\",5, \"helpAbt\", \"关于MYPM\", false);");
		sb.append("    menu.attachEvent(\"onClick\", function(id) {");
		sb.append("        if(menu.getItemText(id)=='修改个人信息'){");
		sb.append("            upMyInfo();return;"); 
		sb.append("        }");
		sb.append("        if(menu.getItemText(id)=='向MYPM反馈'){");
		sb.append("            sendFdInit();return;");
		sb.append("        }");
		sb.append("        if(id=='mypmHome'){");
		sb.append("            goMyHome();return;");
		sb.append("        }");
		sb.append("        if(id=='tutoria'){");
		sb.append("            viewGuid();");
		sb.append("            return;"); 
		sb.append("        }");
		sb.append("        if(id=='mypmTestMgrManual'){");
		sb.append("            openAtta('mypmTestMgrManual.doc');");
		sb.append("            return;");
		sb.append("        }");
		sb.append("        if(id.indexOf('help')>=0){");
		sb.append("            showProDetl(id);");
		sb.append("            return;");
		sb.append("        }");
		sb.append("        if(id=='solut'){");
		sb.append("            viewSultion();");
		sb.append("            return;");
		sb.append("        }");
		sb.append("        if(id=='logOut'){");
		sb.append("            reLogin();return;");
		sb.append("        }");		
		sb.append("        var urlCmd = menu.getUserData(id, 'action');");
		sb.append("        if(urlCmd==null){");
		sb.append("            return;");
		sb.append("        }");
		sb.append("        if(urlCmd=='/caseManager/caseManagerAction!loadCase.action'){");
		sb.append("            urlCmd='/caseManager/caseManagerAction!index.action';");
		sb.append("        }");
		sb.append("        var url =conextPath +urlCmd;");
		//sb.append("        currUrl =url;");
		sb.append("        try{proW_ch.hide();}catch(err){}");
		sb.append("        try{licenseW_ch.hide();}catch(err){}");
		sb.append("        try{tutoriaW_ch.hide();}catch(err){}"); 
		sb.append("        parent.mypmMain.location=url;");
		sb.append("    });");
		sb.append("    menu.attachEvent(\"onCheckboxClick\", menuCheckboxClick);");
		sb.append("    menu.attachEvent(\"onRadioClick\", menuRadioClick);");
		SecurityContextHolder.getContext().getVisit().setMenuLoad(true);
		return sb.toString();
	}
	public void init(ServletConfig config) throws ServletException {
		this.config = config;
		conf = (PropertiesBean) Context.getInstance().getBean("ContextProperties");
		//repBugUrl = conf.getProperty("product.bugs");
	}
	//用HQL查询时，慢很多，所以用原生SQL查了 主要是用HQL要一妙半，用SQL只要不到500ms
	private void cacheMenu(String userId,String compId){
		getDbdelegate().findByFreePara(menuHql.toString(),userId,compId);
		
	}
	private void cachePrivilege(String userId,String compId){
		getDbdelegate().findByFreePara(priviHql.toString(),userId,compId);
		
	}
	private List<Function> getUserMenu() {

		StringBuffer sb = new StringBuffer();
		sb.append("select distinct " );
		sb.append(" f.FUNCTIONID as itemId,f.PARENTID as paretId,f.FUNCTIONNAME as itemName," );
		sb.append(" f.LEVELNUM as levelNum,f.url ,f.SEQ" );
		sb.append("  from T_FUNCTION f ,");
		sb.append("  (select  distinct rf.FUNCTIONID from   T_ROLE_FUNCTION_REAL rf ,");
		sb.append("      (select distinct ur.ROLEID from   T_USER_ROLE_REAL ur ,T_USER u where u.id=? and u.COMPANYID=? and ur.USERID=u.ID)  myrole ");
		sb.append("  where rf.ROLEID= myrole.ROLEID )  myfunction ");
		sb.append("where  f.FUNCTIONID=myfunction.FUNCTIONID AND f.ISLEAF<>1 and f.PAGE ='0' ORDER BY f.LEVELNUM,f.PARENTID,f.SEQ");

		List<Object[]> powerList = getDbdelegate().findBySql(sb.toString(), 
				null,SecurityContextHolderHelp.getUserId(), SecurityContextHolderHelp.getCompanyId());
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
	
	private  List getUserPrivilege(String userId){
		
		StringBuffer sql = new StringBuffer();
		sql.append("select  distinct f.SECURITY_URL from ");
		sql.append("   T_FUNCTION f ,");
		sql.append("  (select distinct rf.FUNCTIONID from ");
		sql.append("  T_ROLE_FUNCTION_REAL rf ,");
		sql.append("  (select ur.ROLEID from ");
		sql.append("    T_USER_ROLE_REAL ur ,");
		sql.append("   T_USER  u");
		sql.append("   where u.ID='" + userId + "' and "
				+ "ur.USERID=u.ID)  myrole");
		sql.append("   where rf.ROLEID= myrole.ROLEID )  myfunction ");
		sql.append("    where f.FUNCTIONID=myfunction.FUNCTIONID AND f.ISLEAF=1 AND f.SECURITY_URL is not null ");
		if (logger.isInfoEnabled()) {
			logger.info(sql.toString());
		}
		List<Object> powerList = getDbdelegate().findBySql(sql
				.toString(), null);
		return powerList;
	}
	
	private HibernateGenericController getDbdelegate(){
		if(hibernateGenericController==null){
			hibernateGenericController =(HibernateGenericController)WebApplicationContextUtils
			.getWebApplicationContext(config.getServletContext()).getBean("hibernateGenericController");
		}
		return hibernateGenericController;
	}
	
	private void reLogin(ServletRequest request, ServletResponse response) { 
		PrintWriter out  = null;
		String contextPath = null;
		try {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			out = response.getWriter();
			contextPath = httpRequest.getContextPath();
			httpRequest.getSession().invalidate();
			StringBuffer sb = new StringBuffer();
			sb.append("<html><head><script type='text/javascript'>function toLgin() { top.location='");
			sb.append(contextPath)
			.append("/jsp/userManager/reLogin.jsp")
			.append("'}</script>");
			sb.append("</head><body onload='toLgin()'></body></html>");
			out.print(sb.toString());
			return ;
		}catch (IOException iox) {
			logger.error(iox);
		}catch(Exception e){
			logger.error(e);
		}
		//当session己过期,再重新登录时,肯定会有Session already invlidated的异常,这时也要跳转到登录页
		if(out!=null){
			StringBuffer sb = new StringBuffer();
			sb.append("<html><head><script type='text/javascript'>function toLgin() { top.location='");
			sb.append(contextPath)
			.append("/jsp/userManager/login.jsp")
			.append("'}</script>");
			sb.append("</head><body onload='toLgin()'></body></html>");
			out.print(sb.toString());			
		}
	}
	
	public static void main(String[] args){
		System.out.println("测试");
	}
}
