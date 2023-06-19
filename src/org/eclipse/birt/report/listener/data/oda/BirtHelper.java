package org.eclipse.birt.report.listener.data.oda;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.springframework.web.context.ServletContextAware;

import cn.com.codes.framework.app.view.UniversalView;
import cn.com.codes.framework.security.SecurityInterceptor;
import cn.com.codes.framework.security.SecurityLog;
import cn.com.codes.framework.security.VisitUser;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;

public class BirtHelper implements MethodInterceptor, ServletContextAware {

	private ViewScene viewScene;

	private SecurityLog securityLog;

	private ServletContext sc;

	private boolean isAuthentication = true;

	private boolean recordTime = false;

	private static Logger logger = Logger.getLogger(SecurityInterceptor.class);

	public void before(Method method, Object[] args, Object target)
			throws Throwable {

		BusiRequestEvent ReqEvent = (BusiRequestEvent) args[0];
		viewScene.recoverUserInfo(ReqEvent);
	}

	private boolean  before(MethodInvocation invoke) {

		BusiRequestEvent ReqEvent = (BusiRequestEvent) invoke.getArguments()[0];
		try {
			return viewScene.recoverUserInfo(ReqEvent);
		} catch (Exception e) {
			return true;
		}
	}

	public Object invoke(MethodInvocation invoke) throws Throwable {

		boolean rest = this.before(invoke);
		if(!rest){
			UniversalView view = new UniversalView();
			view.displayData("forward", "index");
			return view.toResponse();
		}
		Object result = null;
		if (recordTime) {
			StopWatch clock = new StopWatch();
			clock.start(); // 计时开始
			result = invoke.proceed();
			clock.stop(); // 计时结束
			Class[] params = invoke.getMethod().getParameterTypes();
			String[] simpleParams = new String[params.length];
			for (int i = 0; i < params.length; i++) {
				simpleParams[i] = params[i].getSimpleName();
			}
		} else {
			result = invoke.proceed();
		}
		this.after(invoke);
		return result;
	}


	private void after(MethodInvocation invoke) {
		try {
			if (isAuthentication) {
				securityLog.addLog(invoke, getUser());
			}

		} catch (Exception e) {
			logger.error(e);
		}
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

	private VisitUser getUser() {
		return SecurityContextHolder.getContext().getUserInfo();
	}


	public static void main(String[] args) {

		Set<String> powerSet = new HashSet<String>();
	}

	public SecurityLog getSecurityLog() {
		return securityLog;
	}

	public void setSecurityLog(SecurityLog securityLog) {
		this.securityLog = securityLog;
	}

	public ViewScene getViewScene() {
		return viewScene;
	}

	public void setViewScene(ViewScene viewScene) {
		this.viewScene = viewScene;
	}
	

}
