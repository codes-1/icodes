package cn.com.codes.framework.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import cn.com.codes.framework.servlet.CacheUtilsFactory;
import cn.com.codes.framework.servlet.CacheUtilsFactoryImpl;
import cn.com.codes.framework.servlet.CacheUtilsServlet;

public final class CacheUtilsServlet extends HttpServlet {
	private static final Logger logger = Logger
			.getLogger(CacheUtilsServlet.class);

	public void init() throws ServletException {
		try {
			CacheUtilsFactory cacheUtilsFactory = CacheUtilsFactoryImpl.getInstance();
			
		} catch (Exception e) {
			logger.fatal("Can't initialize project component.", e);
		}
		super.init();
	}
}