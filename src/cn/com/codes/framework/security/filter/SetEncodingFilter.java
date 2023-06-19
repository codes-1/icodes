package cn.com.codes.framework.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class SetEncodingFilter implements Filter {

	protected String encoding = null;

	/**
	 * Should a character encoding specified by the client be ignored?
	 */
	protected boolean ignore = true;

	protected FilterConfig filterConfig;

	final private static String ENCODING_VALUE = "encoding";

	public void init(FilterConfig config) throws ServletException {
		this.filterConfig = config;
		this.encoding = filterConfig.getInitParameter(ENCODING_VALUE);
	}

	/**
	 * 选择并设置这个request要使用的字符编码
	 */
	public void doFilter(ServletRequest srequest, ServletResponse sresponse,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) srequest;
		request.setCharacterEncoding(encoding);
		chain.doFilter(srequest, sresponse);
	}

	public void setFilterConfig(final FilterConfig filterConfig) {
		this.filterConfig = filterConfig;

	}

	/**
	 * Take this filter out of service.
	 */
	public void destroy() {
		this.filterConfig = null;
	}
}
