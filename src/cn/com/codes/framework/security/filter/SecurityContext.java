package cn.com.codes.framework.security.filter;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.codes.framework.security.Visit;
import cn.com.codes.framework.security.VisitUser;
import cn.com.codes.object.Function;

public interface SecurityContext {

	public VisitUser getUserInfo();

	public void setError(String errors);

	public Set<String> getErrors();

	public Visit getVisit();

	public HttpServletRequest getRequest();
	
	public HttpServletResponse getResponse();
	
	public void setRequest(HttpServletRequest request);

	public void setAttr(String attrName, Object obj);

	public Object getAttr(String attrName);
	
	public void setSessionAttr(String attrName, Object obj);

	public Object getSessionAttr(String attrName);

	public void setVisit(Visit visit);
	
	public boolean isAuthenticated();
	
	public void setAuthenticated(boolean authentication);
	
	public void setResponse(HttpServletResponse response);
	
	public void writeResponse(String str);
	
	public void appendToResponse(String str);
	
	public void setDeniedMsg(String msg);
	
	public String getDeniedMsg();
	
	public String getIpAddr();
	
	public void setIpAddress(String ipAddress);
	
	public void setMenu(List<Function> menu);
	
	public List<Function> getMenu();
	
	public String getViewCode();
	
	public void eraseViewCode();
	
	public void eraseMenu();
	
	
}

