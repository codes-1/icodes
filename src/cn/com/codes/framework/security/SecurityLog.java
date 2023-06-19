package cn.com.codes.framework.security;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;

import cn.com.codes.framework.security.VisitUser;



public interface SecurityLog {

	public void addLog(MethodInvocation invoke,VisitUser user);
	
	public void addLog(Method method,Object[] args,VisitUser user);
}
