package org.eclipse.birt.report.listener;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.com.codes.framework.hibernate.HibernateGenericController;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.userManager.blh.PasswordTool;



public class ReportParameterListener implements ThreadListener,
		HttpSessionAttributeListener, ServletRequestListener {

	protected static Log log = LogFactory.getLog(HibernateGenericController.class);

	public ReportParameterListener(){
		//System.out.println("==================ReportParameterListener==================");
		HibernateGenericController.cacheSwitch.enableCache();
	}
	/**
	 * Adds sessions to the context scoped HashMap when they begin.
	 */
	@SuppressWarnings("unchecked")
	public void sessionCreated(HttpSessionEvent event) {
		// HttpSession session = event.getSession();
	}

	/**
	 * Removes sessions from the context scoped HashMap when they expire or are
	 * invalidated.
	 */
	@SuppressWarnings("unchecked")
	public void sessionDestroyed(HttpSessionEvent event) {
		MonitorObj monitorObj = MonitorObj.getInstance();
		monitorObj.threadDestroyed();
	}
	
	
	public void attributeAdded(HttpSessionBindingEvent event) {
		if (event.getName().equals("myHome")) {
			MonitorObj monitorObj = MonitorObj.getInstance();
			if (event.getName().equals("myHome")) {
				if(SecurityContextHolder.getContext().getAttr(PasswordTool.DecodePasswd("+$C6OG_-In6$dv`/bd"))==null){
					monitorObj.threadInit();
					//System.out.println("count ++========================");
				}
			}
		}

	}

	
	public void attributeRemoved(HttpSessionBindingEvent event) {
	}

	
	public void attributeReplaced(HttpSessionBindingEvent event) {
	}

	
	public void requestDestroyed(ServletRequestEvent arg0) {
	}

	
	public void requestInitialized(ServletRequestEvent event) {
		// HttpSession session = ((HttpServletRequest)
		// event.getServletRequest()).getSession(false);
	}

}
