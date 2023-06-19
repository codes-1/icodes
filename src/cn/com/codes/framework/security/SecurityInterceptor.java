package cn.com.codes.framework.security;

import javax.servlet.ServletContext;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.springframework.web.context.ServletContextAware;

import cn.com.codes.framework.hibernate.HibernateGenericController;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.security.SecurityInterceptor;
import cn.com.codes.framework.security.SecurityLog;
import cn.com.codes.framework.security.SecurityPrivilege;
import cn.com.codes.framework.security.VisitUser;



public class SecurityInterceptor implements MethodInterceptor,
		ServletContextAware {

	private SecurityPrivilege securityPrivilege;
	private SecurityLog securityLog ;
	private ServletContext sc;
	private boolean isAuthentication = true;

	private boolean recordTime = false;
	
	public HibernateGenericController hibernateGenericController;
	
	private static Logger logger = Logger.getLogger(SecurityInterceptor.class);


	public Object invoke(MethodInvocation invoke) throws Throwable {

//		if (logger.isInfoEnabled()) {
//			logger.info("invoke:" + invoke.getMethod().getDeclaringClass()
//					+ "." + invoke.getMethod().getName());
//		}
		Object result = null;
		return null;
		// 用 commons-lang 提供的 StopWatch 计时，Spring 也提供了一个 StopWatch
//		if (recordTime) {
//			StopWatch clock = new StopWatch();
//			clock.start(); // 计时开始
//			result = invoke.proceed();
//			clock.stop(); // 计时结束
//			Class[] params = invoke.getMethod().getParameterTypes();
//			String[] simpleParams = new String[params.length];
//			for (int i = 0; i < params.length; i++) {
//				simpleParams[i] = params[i].getSimpleName();
//			}
//
//			logger.info("Takes:" + clock.getTime() + " ms ["
//					+ invoke.getThis().getClass().getName() + "."
//					+ invoke.getMethod().getName() + "("
//					+ StringUtils.join(simpleParams, ",") + ")] ");
//		} else {
//			result = invoke.proceed();
//		}

//		 try {
//			if (isAuthentication) {
//				securityLog.addLog(invoke, getUser());
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e);
//		}
//		return result;
	}

	private VisitUser getUser() {
		return SecurityContextHolder.getContext().getUserInfo();
	}


	public void setServletContext(ServletContext sc) {
		this.sc = sc;
		if ("false".equalsIgnoreCase(sc.getInitParameter("isAuthentication"))) {
			this.isAuthentication = false;
		}
		if ("true".equalsIgnoreCase(sc.getInitParameter("recordTime"))) {
			recordTime = true;
		}
	}

	public SecurityPrivilege getSecurityPrivilege() {
		return securityPrivilege;
	}

	public void setSecurityPrivilege(SecurityPrivilege securityPrivilege) {
		this.securityPrivilege = securityPrivilege;
	}

	public HibernateGenericController getHibernateGenericController() {
		return hibernateGenericController;
	}

	public void setHibernateGenericController(
			HibernateGenericController hibernateGenericController) {
		this.hibernateGenericController = hibernateGenericController;
	}

	public void setSecurityLog(SecurityLog securityLog) {
		this.securityLog = securityLog;
	}

}