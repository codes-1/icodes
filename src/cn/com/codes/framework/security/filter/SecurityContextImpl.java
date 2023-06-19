package cn.com.codes.framework.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.com.codes.framework.common.Global;
import cn.com.codes.framework.security.Visit;
import cn.com.codes.framework.security.VisitUser;
import cn.com.codes.object.Function;
import cn.com.codes.framework.security.filter.SecurityContext;
import cn.com.codes.framework.security.filter.SecurityContextImpl;

/**
 * @author liuyg
 * 
 */
public class SecurityContextImpl implements SecurityContext {

	private static final Log log = LogFactory.getLog(SecurityContextImpl.class);
	private HttpServletRequest request;
	private HttpServletResponse response;
	private boolean authentication = false;
	private String deniedMsg;
	private String ipAddress;

	public SecurityContextImpl() {

	}

	private void init() {
		if (request == null) {
			return;
		}
		Object obj = request.getSession().getAttribute(Global.VISITOR);
		if (obj != null) {
			((Visit) obj).clearErrors();
		}
	}

	public VisitUser getUserInfo() {

		if (request == null) {
			return null;
		}
		Object obj = request.getSession().getAttribute(Global.VISITOR);

		if (obj != null) {
			return ((Visit) obj).getUserInfo();
		}
		return null;
	}

	public void setError(String error) {
		if (request == null) {
			return;
		}
		Object obj = request.getSession().getAttribute(Global.VISITOR);
		if (obj != null) {
			((Visit) obj).addError(error);
		}

	}

	public Set<String> getErrors() {
		if (request == null) {
			return null;
		}
		Object obj = request.getSession().getAttribute(Global.VISITOR);
		if (obj != null) {
			return ((Visit) obj).getErrors();
		}

		return null;

	}

	public void setVisit(Visit visit) {

		request.getSession().setAttribute(Global.VISITOR, visit);
		request.getSession().setAttribute("logined", "");
	}

	/**
	 * @deprecated
	 */
	public void setMenu(List<Function> menu){
		request.getSession().setAttribute("myMenu", menu);
	}
	/**
	 * @deprecated
	 */
	public List<Function> getMenu(){
		Object menu = request.getSession().getAttribute("myMenu");
		if(menu==null){
			return null;
		}
		return (List<Function>)menu;
	}
	/**
	 * @deprecated
	 */
	public void eraseMenu(){
		request.getSession().removeAttribute("myMenu");
	}
	public Visit getVisit() {
		if (request == null) {
			return null;
		}
		Object obj = request.getSession().getAttribute(Global.VISITOR);
		if (obj != null) {
			return (Visit) obj;
		}
		return null;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
		init();
	}

	public void setAttr(String attrName, Object obj) {

		request.setAttribute(attrName, obj);
	}

	public Object getAttr(String attrName) {
		return request.getAttribute(attrName);
	}
	public void setSessionAttr(String attrName, Object obj){
		request.getSession().setAttribute(attrName, obj);
	}

	public Object getSessionAttr(String attrName){
		return request.getSession().getAttribute(attrName);
	}
	public boolean isAuthenticated() {
		return authentication;
	}

	public void setAuthenticated(boolean authentication) {

		this.authentication = authentication;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void writeResponse(String str) {
		if (response == null) {
			log.warn("=====response is null ======");
		} else {
			try {
				PrintWriter writer = response.getWriter();
				writer.write(str);
				writer.flush();
			} catch (IOException e) {
				log.error(e);
				e.printStackTrace();
			}
		}

	}

	public void appendToResponse(String str) {
		try {
			PrintWriter writer = response.getWriter();
			writer.append(str);
			writer.flush();
		} catch (IOException e) {
			log.error(e);
			e.printStackTrace();
		}
	}

	public void setDeniedMsg(String msg) {
		this.deniedMsg = msg;
	}

	public String getDeniedMsg() {
		return deniedMsg;
	}

	public String getIpAddr() {
		String ip = request.getHeader("x-forwarded-for");
		// Enumeration enu =request.getHeaderNames();
//		 while(enu.hasMoreElements()){
//		// System.out.println(enu.nextElement().toString());
//		 }
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		 //System.out.println(request.getHeader("host"));
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		 //返向多个代理时,报第一个
		if (ip.indexOf(",") > 0) {
			ip = ip.split(",")[0];
		}
		if (!ip.equals(request.getRemoteAddr())) {
			//ip = ip + " 通过代理服器:" + request.getRemoteAddr() + "中转";
		}
		return ip;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getViewCode(){
		//如处理是为了防止打开登录页在后 session过期时再登录
		if(request.getSession().getAttribute(Global.VALID_CODE)==null){
			return null;
		}
		return request.getSession().getAttribute(Global.VALID_CODE).toString();
	}
	public void eraseViewCode(){
		request.getSession().removeAttribute(Global.VALID_CODE);
	}
	
	public static void main(String args[]){
		System.out.println("测试中文乱码");
	}

	public HttpServletResponse getResponse() {
		return response;
	}
}