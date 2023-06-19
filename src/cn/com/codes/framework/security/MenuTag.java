package cn.com.codes.framework.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.framework.hibernate.HibernateGenericController;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.object.Function;
import cn.com.codes.framework.security.MenuTag;

public class MenuTag extends TagSupport {

	private Log logger = LogFactory.getLog(MenuTag.class);
	private static HibernateGenericController hibernateGenericController;
	private static StringBuffer menuHql = new StringBuffer();
	static{
		menuHql.append(" select distinct  new Function (p.functionId,p.parentId,p.functionName,p.level,p.url ,p.seq )");
		menuHql.append(" from User u join  u.roleList r  join r.privilege p where u.companyId=? and p.isleaf<>1 order by p.level,p.parentId,p.seq" );
	}

	public int doStartTag() throws JspException { 

	
			try {
				drwaMenu();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(e);
			}
	

		return SKIP_BODY;
	}
	
	private void drwaMenu()throws IOException{
		
		List<Function>  menu = this.getUserMenu();
		if(menu==null){
			return;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("    var menu = new dhtmlXMenuObject(\"menuObj\");\n");
		sb.append("    menu.setImagePath(conextPath+").append("\"/dhtmlx/menu/codebase/imgs/\");\n");
		for(int i=1; i<5; i++){
			String parentId = null;
			int position = 0;
			String beforeItemId = null ;
			for(Function fun : menu){
				if(fun.getLevel() == i ){
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
						//sb.append("    menu.addNewSibling(null, \""+itemId+"\", \""+itemName.trim() +"\", false);\n");
						sb.append("    menu.addNewSibling(null, \"").append(itemId).append("\", \"").append(itemName.trim()).append("\", false);\n");
					}else if( i == 1 && position >0){
						//sb.append("    menu.addNewSibling(\""+beforeItemId+"\", \""+itemId+"\", \""+itemName.trim() +"\", false);\n");
						sb.append("    menu.addNewSibling(\"").append(beforeItemId).append("\", \"").append(itemId).append("\", \"").append(itemName.trim()).append("\", false);\n");
						beforeItemId = fun.getFunctionId(); 
					}else{
						//sb.append("    menu.addNewChild(\""+parentId+"\","+position+", \""+itemId+"\", \""+itemName.trim()+"\", false);\n");
						sb.append("    menu.addNewChild(\"").append(parentId).append("\","+position).append(", \"").append(itemId).append("\", \"").append(itemName.trim()).append("\", false);\n");
						position ++ ;
						//sb.append("    menu.addNewSeparator(\""+itemId+"\",\"sep_"+itemId+"\");\n");
						sb.append("    menu.addNewSeparator(\"").append(itemId).append("\",\"sep_").append(itemId+"\");\n");
					}
					if( fun.getUrl() != null){
						//sb.append("    menu.setUserData('"+itemId +"','action','"+ fun.getUrl().trim()+"');\n");
						sb.append("    menu.setUserData('").append(itemId).append("','action','").append(fun.getUrl().trim()).append("');\n");
					}
				}
			}			
		}
		sb.append("    menu.attachEvent(\"onClick\", function(id) {\n");
		sb.append("        var url =conextPath +menu.getUserData(id, \"action\");\n");
		sb.append("        alert(url);\n");
		sb.append("    });\n");
		JspWriter out=pageContext.getOut(); 
		out.println(sb.toString());
		SecurityContextHolder.getContext().eraseMenu();
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
		sb.append("where  f.FUNCTIONID=myfunction.FUNCTIONID AND f.ISLEAF<>1 ORDER BY f.LEVELNUM,f.PARENTID,f.SEQ");

		List<Object[]> powerList = getDbdelegate().findBySql(sb.toString(), null, SecurityContextHolderHelp.getCompanyId());
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
	private HibernateGenericController getDbdelegate() {
		if (hibernateGenericController == null) {
			hibernateGenericController = (HibernateGenericController) WebApplicationContextUtils
					.getWebApplicationContext(pageContext.getServletContext())
					.getBean("hibernateGenericController");
		}
		return hibernateGenericController;
	}
}
