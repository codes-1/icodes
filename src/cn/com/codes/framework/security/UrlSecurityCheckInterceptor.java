package cn.com.codes.framework.security;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;

import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.security.SecurityPrivilege;
import cn.com.codes.framework.security.UrlSecurityCheckInterceptor;
import cn.com.codes.framework.security.Visit;

public class UrlSecurityCheckInterceptor implements Interceptor,
		ServletContextAware {
	private static Logger logger = Logger.getLogger(UrlSecurityCheckInterceptor.class);
	private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
	private static ServletContext sc;
	private static boolean dedugModel = false;
	protected  static Set<String> needCheckUrls = new HashSet<String>();
	private static boolean  isAuthentication = true;
	private static SecurityPrivilege securityPrivilege;
	
	public void destroy() {

	}
	
	public void init() {

	}

	public String intercept(ActionInvocation invocation) throws Exception {
		
		Visit visit = SecurityContextHolder.getContext().getVisit();
		String currSecUrl = getUrl(invocation);
		HttpServletRequest request = ServletActionContext.getRequest();

		if (!urlCheck(currSecUrl)) {
			
			ServletResponse response = ServletActionContext.getResponse();
			if(isAjaxRequest((HttpServletRequest) request)) {
				return this.goAjaxDenyPage((ServletRequest)request, response); 
			}
			BaseException exc = new BaseException("您没有当前操作权限", true);
			request.setAttribute("EXP_INFO", exc);
			return "globalException";				
			
		}
		return invocation.invoke();
	}
	
	protected static boolean isAjaxRequest(HttpServletRequest request) {
		String requestType = request.getHeader("X-Requested-With");
		if (requestType != null && "XMLHttpRequest".equals(requestType)) {
			return true;
		}
		return false;
	}
	private String  goAjaxReloadMenu(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setContentType(CONTENT_TYPE);
		PrintWriter writer = response.getWriter();
		writer.write("MypmPermissionChg");
		writer.flush();
		return "globalAjaxRest";
	}
	
	private String  goAjaxDenyPage(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setContentType(CONTENT_TYPE);
		PrintWriter writer = response.getWriter();
		writer.write("deny");
		writer.flush();
		return "globalAjaxRest";
	}
	public  boolean urlCheck(String currSecUrl) {
		if(currSecUrl.indexOf("initDatabases") != -1||currSecUrl.indexOf("userManagerAction!login")>=0){
			return true;
		}
//		if (!isAuthentication) {
//			return true;
//		}
		if (!securityPrivilege.securityCheck(currSecUrl, this.getNeedCheckUrls())) {
			return false;
		}
		return true;
	}
	private static Visit getVisit() {

		return SecurityContextHolder.getContext().getVisit();
	}
	
	public static  boolean urlSecurityCheck(String currSecUrl) {
		if (!isAuthentication) {
			return true;
		}
		if (!securityCheck(currSecUrl, getNeedCheckUrls())) {
			return false;
		}
		return true;
	}
	
	public static boolean securityCheck(String url, Set<String> needCheckUrls) {
		//System.out.println(url);
//		if("projectAction!listVersionLogs".equals(url)){
//			return true;
//		}
		if (needCheckUrls.size() == 0) {
			return true;
		}
		//项目列表无需校验
		if("projectAction!listProjects".equals(url)){
			return true;
		}
		if (getVisit() == null) {
			return true;
		}
		if (needCheckUrls.contains(url)) {
			Visit vist = getVisit();
			if (!vist.getUserInfo().getPrivilege().contains(url)) {
				if (logger.isInfoEnabled()) {
					logger.info("您没有[" + url + "]的操作权限");
				}
				return false;
			}
		}

		return true;
	}
	
	public void setServletContext(ServletContext sc) {
		this.sc = sc;
		String isDebug = this.sc.getInitParameter("isDebug");
		if ("true".equalsIgnoreCase(isDebug)) {
			this.dedugModel = true;
		}
		if ("false".equalsIgnoreCase(sc.getInitParameter("isAuthentication"))) {
			this.isAuthentication = false;
		}
	}

	private static Set<String> getNeedCheckUrls() {

		if (needCheckUrls.size() == 0 && !dedugModel) {
			initNeedCheckUrls();
		} else if (dedugModel) {
			initNeedCheckUrls();
		}
		return needCheckUrls;
	}

	private static void initNeedCheckUrls() {
		if (securityPrivilege == null) {
			securityPrivilege = (SecurityPrivilege) WebApplicationContextUtils
					.getWebApplicationContext(sc).getBean("securityPrivilege");
		}
		needCheckUrls.clear();
		needCheckUrls = securityPrivilege.getCheckedPrivilege();
	}

	private String getUrl(ActionInvocation invocation) {
		StringBuffer url = new StringBuffer(invocation.getProxy().getActionName());
		url.append("!");
		url.append(invocation.getProxy().getMethod());
		return url.toString();
//		return invocation.getProxy().getActionName() + "!"
//				+ invocation.getProxy().getMethod();
	}

	public SecurityPrivilege getSecurityPrivilege() {
		return securityPrivilege;
	}

	public void setSecurityPrivilege(SecurityPrivilege securityPrivilege) {
		this.securityPrivilege = securityPrivilege;
	}
}
