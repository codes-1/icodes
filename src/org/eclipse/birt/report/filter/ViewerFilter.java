/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import cn.com.codes.framework.hibernate.HibernateGenericController;
import cn.com.codes.framework.jms.CommonMessageListener;
import cn.com.codes.framework.security.filter.SecurityContextHolder;

/**
 * Filter class for birt viewer. It is according to Servlet 2.3 specification.
 * Filter http request and set character encoding to UTF-8
 * 
 */
public class ViewerFilter implements Filter {

	// default encoding
	protected String encoding = "UTF-8"; //$NON-NLS-1$

	// filter config
	protected FilterConfig filterConfig = null;

	/**
	 * Default constructor
	 */
	public ViewerFilter() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		this.encoding = null;
		this.filterConfig = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (request.getCharacterEncoding() == null && encoding != null)
			request.setCharacterEncoding(encoding);
		if(!canViewRept()){
		 	RequestDispatcher rd = null;
			rd = filterConfig.getServletContext().getRequestDispatcher("/jsp/common/sysHint.jsp"); 
			SecurityContextHolder.clearContext();
			rd.forward(request, response);
			return;
		}
		chain.doFilter(request, response);
	}

	public static boolean canViewRept(){
		//System.out.println("=========="+CommonMessageListener.getReptLimitCount());
		if((CommonMessageListener.getReptViewCount())>=(CommonMessageListener.getReptLimitCount())){
			return false;
		}
		return true;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		HibernateGenericController.cacheSwitch.enableCache();
	}

}
