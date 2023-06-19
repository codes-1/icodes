package cn.com.codes.framework.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.com.codes.framework.hibernate.HibernateGenericController;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.security.Button;
import cn.com.codes.framework.security.ButtonContainerService;
import cn.com.codes.framework.security.ButtonTag;
import cn.com.codes.framework.security.VisitUser;

public class ButtonTag extends TagSupport {

	private Log logger = LogFactory.getLog(ButtonTag.class);
	private static Map navigatorMap = new HashMap();
	private static HibernateGenericController hibernateGenericController;
	private String page;
	private String pagination = "true";
	private String filter = "false";
	private String hidden = "false";
	private String find = "false";
	private String checkAll = "false";
	private String imgPath = "";
	private String sw = "false";
	private String reFresh = "true";
	private String reFreshHdl = "true";
	private String back = "true";
	private String backHdl = "true";
	private String setHome = "false";
	private String hand = "true";
	static{
		navigatorMap.put("role", "角色列表");
		navigatorMap.put("userManager/user", "用户列表");
		navigatorMap.put("userManager/group", "用户组列表");
		navigatorMap.put("outLineManager", "测试需求分解列表");
		navigatorMap.put("bugManager", "项目缺陷管理");
		navigatorMap.put("caseManager", "测试用例列表");
		navigatorMap.put("msgManager", "消息管理列表");
		navigatorMap.put("testBaseSet", "测试基础数据设置");
		navigatorMap.put("singleTestTask", "测试项目列表");
		navigatorMap.put("flwSet", "测试项目 测试流程设置");
		navigatorMap.put("swTestTaskList", "测试项目切换选择列表");
		navigatorMap.put("sendMsgManager", "发布消息");	
		navigatorMap.put("allMySpr", "缺陷管理");
		//navigatorMap.put("batchAssgin", "批量分配视图");
		
		
	}
	
	public int doStartTag() throws JspException {
		try {
			drawButton();
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	private void drawButton() throws IOException {
		Map<String, List<Button>> container = ButtonContainerService.container;
		StringBuffer sb = new StringBuffer();
		sb.append("    pmBar = new dhtmlXToolbarObject(\"toolbarObj\");\n");
		String imagesDirec = "/dhtmlx/toolbar/images/";
		imgPath = imagesDirec;
		String rootPath = ((HttpServletRequest) pageContext.getRequest()).getContextPath();
		this.imgPath = rootPath + this.imgPath;
		sb.append("pmBar.setIconsPath(\"").append(imgPath).append("\");\n");
		List<Button> buttonlist = null;
		if (page != null) {
			buttonlist = container.get(page);
		}

		if ("true".equalsIgnoreCase(checkAll)) {
			sb.append("pmBar.addButton(\"allChk\", 0, \"\", \"ch_false.gif\");\n");
			sb.append("pmBar.setItemToolTip(\"allChk\", \"全选\");\n");
		}
		int i = 1;
		VisitUser vu = null;
		if (buttonlist != null) {
			vu = SecurityContextHolder.getContext().getVisit().getUserInfo();
			Set<String> privilege = vu.getPrivilege();
			for (Button button : buttonlist) {
				if (button.isShare() && button.getIcon() != null) {
					if (!"".equals(button.getIcon())) {
						sb.append("pmBar.addButton(\"")
						  .append(button.getId())
						  .append("\",").append(i).append(" , \"\", \"")
						  .append(button.getIcon()).append("\");\n");
						sb.append("pmBar.setItemToolTip(\"")
						  .append(button.getId()).append("\", \"")
						  .append(button.getName()).append("\");\n");
					} else {
						sb.append("pmBar.addButton(\"").append(button.getId())
						  .append("\",").append(i).append(" ,'")
						  .append(button.getName()).append("', \"\");\n");
						i++;
						sb.append("pmBar.addSeparator(\"")
						  .append(button.getId()).append("Sp\",")
						  .append(i).append(");\n");
					}
					i++;
				} else if (!button.isShare() && button.getIcon() != null) {
					if (privilege != null&&privilege.contains(button.getUrl())
							&& sw.equals("false")) {
						if (!"".equals(button.getIcon())) {
							sb.append("pmBar.addButton(\"")
							  .append(button.getId()).append("\",").append(i)
							  .append(" , \"\", \"")
							  .append(button.getIcon()).append("\");\n");
							sb.append("pmBar.setItemToolTip(\"")
							  .append(button.getId()).append("\", \"")
							  .append(button.getName()).append("\");\n");
						} else {
							sb.append("pmBar.addButton(\"")
							  .append(button.getId()).append("\",").append(i)
							  .append(" ,'").append(button.getName())
							  .append("', \"\");\n");
							i++;
							sb.append("pmBar.addSeparator(\"")
							  .append(button.getId()).append("Sp\",").append(i)
							  .append(");\n");
						}
						i++;
					} else if (privilege != null&& !privilege.contains(button.getUrl())
							&& sw.equals("true")) {
						if (!"".equals(button.getIcon())) {
							sb.append("pmBar.addButton(\"")
							  .append(button.getId()).append("\",").append(i)
							  .append(" , \"\", \"")
							  .append(button.getIcon()).append("\");\n");
							sb.append("pmBar.hideItem('").append(i).append(
									"')\n;");
							sb.append("pmBar.setItemToolTip(\"").append(
									button.getId()).append("\", \"").append(
									button.getName()).append("\");\n");
						} else {
							sb.append("pmBar.addButton(\"")
							  .append(button.getId()).append("\",").append(i)
							  .append(" ,'").append(button.getName())
							  .append("', \"\");\n");
							sb.append("pmBar.hideItem('").append(i).append("')\n;");
							i++;
							sb.append("pmBar.addSeparator(\"")
							  .append(button.getId()).append("Sp\",").append(i)
							  .append(");\n");
						}
						i++;
					}
				}
			}
			if (sw.equalsIgnoreCase("true")) {
				sb.append("pmBar.addSeparator(\"mypmSw99999,\"").append(i).append(");\n");
				sb.append("pmBar.hideItem(\"mypmSw99999\");");
			}
		}

		if ("true".equalsIgnoreCase(filter)) {
			sb.append("pmBar.addButton(\"filter\",").append(i).append(" , \"\", \"filter.gif\");\n");
			sb.append("pmBar.setItemToolTip(\"filter\", \"过滤\");\n");
			i++;
		}

		if ("true".equalsIgnoreCase(hidden)) {
			sb.append("pmBar.addButton(\"hidden\",").append(i).append(" , \"\", \"hiddenCol.gif\");\n");
				sb.append("pmBar.setItemToolTip(\"hidden\", \"显示|隐藏列\");\n");
			
			i++;
		}
		if("userManager/user".equals(page)&&vu!=null&&vu.getLoginName().equals("admin")){
			sb.append("pmBar.addButton(\"setMgrPersion\",").append(i).append(" , \"\", \"mgrPerson.gif\");\n");
			sb.append("pmBar.setItemToolTip(\"setMgrPersion\", \"设置用户为管理人员使其可查看任何项目员\");\n");
			i++;
		}
		if ("true".equalsIgnoreCase(find)) {
			sb.append("pmBar.addButton(\"find\",").append(i).append(" , \"\", \"search.gif\");\n");
			sb.append("pmBar.setItemToolTip(\"find\", \"查询\");\n");
			i++;
		}
		if("testBaseSet".equals(page)){
			sb.append("pmBar.addSeparator(\"sep_testBasek1\",").append(i).append(");\n");
			i++;
			sb.append("pmBar.addText(\"swTestBase\",").append(i).append(", \"切换数据类型\");");
			i++;
			sb.append("pmBar.addButtonSelect(\"testBaseList\",").append(i).append(", \"\", testBaseOpts);\n");
			i++;
			sb.append("pmBar.addSeparator(\"sep_testBasek2\",").append(i).append(");\n");
			i++;
			sb.append("pmBar.setListOptionSelected('testBaseList', 'all');\n");
			sb.append("pmBar.setItemText('testBaseList', pmBar.getListOptionText('testBaseList', 'all'));\n");
		}
		if ("true".equalsIgnoreCase(reFresh)) {
			sb.append("pmBar.addButton(\"reFreshP\",").append(i).append(" , \"\", \"page_refresh.gif\");\n");
			sb.append("pmBar.setItemToolTip(\"reFreshP\", \"刷新页面\");\n");
			i++;
		}
		if ("true".equalsIgnoreCase(back)) {
			sb.append("pmBar.addButton(\"back\",").append(i).append(" , \"\", \"back.gif\");\n");
			sb.append("pmBar.setItemToolTip(\"back\", \"返回\");\n");
			i++;
		}
		if("true".equalsIgnoreCase(setHome)){
			sb.append("pmBar.addButton(\"custHome\",").append(i).append(" , \"\", \"myHome.png\");\n");
			sb.append("pmBar.setItemToolTip(\"custHome\", \"设置当前页为我的MYPM主页\");\n");
			i++;
		}
		if ("true".equalsIgnoreCase(pagination)) {
			for (int j = i, k = 1; j < i + 9; j++, k++) {
				if (k == 1) {
					sb.append("pmBar.addButton(\"first\",").append(j)
					  .append(" , \"\", \"first.gif\", \"first.gif\");\n");
					sb.append("pmBar.setItemToolTip(\"first\", \"第一页\");\n");
				} else if (k == 2) {
					sb.append("pmBar.addButton(\"pervious\",").append(j)
					  .append(", \"\", \"pervious.gif\", \"pervious.gif\");\n");
					sb.append("pmBar.setItemToolTip(\"pervious\", \"上一页\");\n");
				} else if (k == 3) {
					sb.append("pmBar.addSlider(\"slider\",").append(j).append(", 80, 1, 30, 1, \"\", \"\", \"%v\");\n");
					sb.append("pmBar.setItemToolTip(\"slider\", \"滚动条翻页\");\n");
				} else if (k == 4) {
					sb.append("pmBar.addButton(\"next\",").append(j).append(", \"\", \"next.gif\", \"next.gif\");\n");
					sb.append("pmBar.setItemToolTip(\"next\", \"下一页\");\n");
				} else if (k == 5) {
					sb.append("pmBar.addButton(\"last\",").append(j).append(", \"\", \"last.gif\", \"last.gif\");\n");
					sb.append("pmBar.setItemToolTip(\"last\", \"末页\");\n");
				} else if (k == 6) {
					sb.append("pmBar.addInput(\"page\",").append(j).append(", \"\", 25);\n");
				} else if (k == 7) {
					sb.append("pmBar.addText(\"pageMessage\",").append(j)
					  .append(", \"\");\n");
				} else if (k == 8) {
					sb.append("pmBar.addText(\"pageSizeText\",").append(j)
					  .append(", \"每页\");\n");
					j++;
					sb.append("var opts = Array(Array('id1', 'obj', '10'), Array('id2', 'obj', '15'), Array('id3', 'obj', '20'), Array('id4', 'obj', '25'));\n");
					sb.append("pmBar.addButtonSelect(\"pageP\",").append(j)
					  .append(", \"page\", opts);\n");
					sb.append("pmBar.setItemToolTip(\"pageP\", \"每页记录数\");\n");
					j++;
					sb.append("pmBar.addText(\"pageSizeTextEnd\",").append(j)
					  .append(", \"条\");\n");
					sb.append("pmBar.setListOptionSelected('pageP','id1');");
				}
			}
		}
		if("true".equalsIgnoreCase(pagination)){
			i = i+10;
			if("outLineManager".equals(page)||"bugManager".equals(page)||"caseManager".equals(page)||"allMySpr".equals(page)||"testCycle".equals(page)){
				i++;
				sb.append("pmBar.addSeparator(\"sep_swTask3\",").append(i).append("); ");
				i++;
				sb.append("pmBar.addButton(\"sw2Task\",").append(i).append(", \"切换项目\", \"\");");
				i++;
				if("bugManager".equals(page)||"allMySpr".equals(page)){
					sb.append("var taskOpts = Array(Array('allTask', 'obj', '所有'), Array('sw2TaskAction', 'obj', '切换到指定项目'));\n");
					i++;
					sb.append("pmBar.addButtonSelect('selTaskPage',").append(i).append(",'所有', taskOpts);\n");
					i++;
				}
			}
			i++;
			if("bugManager".equals(page)||"allMySpr".equals(page)||"caseManager".equals(page)){
				sb.append("pmBar.addText(\"qSearch\",").append(i);
				sb.append(", \"编号快查\");\n");
				i++;
				sb.append("pmBar.addInput(\"qSearchIpt\",").append(i).append(", \"\", 80);\n");
			}
			i++;
			sb.append("pmBar.addSeparator(\"sep_swTask2\",").append(i).append("); ");
			sb.append("pmBar.addButton(\"currPosition\",").append(i).append(", \" ").append(navigatorMap.get(page)).append("\", \"hand.gif\", \"hand.gif\"); ");
		}else{
			if("outLineManager".equals(page)||"bugManager".equals(page)||"caseManager".equals(page)||"allMySpr".equals(page)){
				i++;
				sb.append("pmBar.addSeparator(\"sep_swTask3\",").append(i).append("); ");
				i++;
				sb.append("pmBar.addButton(\"sw2Task\",").append(i).append(", \"切换项目\", \"\");");
				i++;
				if("bugManager".equals(page)||"allMySpr".equals(page)){
					sb.append("var taskOpts = Array(Array('allTask', 'obj', '所有'), Array('sw2TaskAction', 'obj', '切换到指定项目'));\n");
					i++;
					sb.append("pmBar.addButtonSelect('selTaskPage',").append(i).append(",'所有', taskOpts);\n");
					i++;
				}
			}
			i++;
			if("bugManager".equals(page)||"allMySpr".equals(page)||"caseManager".equals(page)){
				sb.append("pmBar.addText(\"qSearch\",").append(i);
				sb.append(", \"编号快查\");\n");
				i++;
				sb.append("pmBar.addInput(\"qSearchIpt\",").append(i).append(", \"\", 80);\n");	
			}
			i++;
			sb.append("pmBar.addSeparator(\"sep_swTask2\",").append(i).append("); ");
			i++;
			sb.append("pmBar.addButton(\"currPosition\",").append(i).append(", \" ").append(navigatorMap.get(page)).append("\", \"hand.gif\", \"hand.gif\"); ");
		}
		
		if ("true".equals(this.reFresh) && "true".equals(this.reFreshHdl)) {
			sb.append("\npmBar.attachEvent(\"onClick\", function(id) {\n");
			sb.append("	if(id ==\"reFreshP\"){\n");
			sb.append("   pageAction(pageNo, pageSize);\n");
			sb.append("	}\n");
			sb.append("});\n");
		}
		if ("true".equalsIgnoreCase(back) && "true".equals(this.backHdl)) {
			sb.append("\npmBar.attachEvent(\"onClick\", function(id) {\n");
			sb.append("	if(id ==\"back\"){\n");
			sb.append("   var reUrl= document.referrer;\n");
			sb.append("   if(reUrl!=\"\"&&(reUrl.indexOf('main.jsp')>0||reUrl.indexOf('myHome.jsp')>0)){\n");
			sb.append("   	history.back();\n");
			sb.append("   	return;\n");
			sb.append("   }\n");			
			sb.append("   parent.mypmMain.location=reUrl;\n");
			sb.append("	}\n");
			sb.append("});\n");
		}
		JspWriter out = pageContext.getOut();
		out.println(sb.toString());
	}

	/**
	 * @deprecated
	 */
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
	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getPagination() {
		return pagination;
	}

	public void setPagination(String pagination) {
		this.pagination = pagination;
	}

	public String getHidden() {
		return hidden;
	}

	public void setHidden(String hidden) {
		this.hidden = hidden;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getFind() {
		return find;
	}

	public void setFind(String find) {
		this.find = find;
	}

	public String getCheckAll() {
		return checkAll;
	}

	public void setCheckAll(String checkAll) {
		this.checkAll = checkAll;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getSw() {
		return sw;
	}

	public void setSw(String sw) {
		this.sw = sw;
	}

	public String getReFresh() {
		return reFresh;
	}

	public void setReFresh(String refresh) {
		this.reFresh = refresh;
	}

	public String getBack() {
		return back;
	}

	public void setBack(String back) {
		this.back = back;
	}

	public String getReFreshHdl() {
		return reFreshHdl;
	}

	public void setReFreshHdl(String reFreshHdl) {
		this.reFreshHdl = reFreshHdl;
	}

	public String getBackHdl() {
		return backHdl;
	}

	public void setBackHdl(String backHdl) {
		this.backHdl = backHdl;
	}

	public String getSetHome() {
		return setHome;
	}

	public void setSetHome(String setHome) {
		this.setHome = setHome;
	}

	public String getHand() {
		return hand;
	}

	public void setHand(String hand) {
		this.hand = hand;
	}
	

}
