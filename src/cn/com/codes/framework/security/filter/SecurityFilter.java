package cn.com.codes.framework.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.com.codes.framework.security.Visit;
import cn.com.codes.framework.security.filter.SecurityContext;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.security.filter.SecurityContextImpl;
import cn.com.codes.framework.security.filter.SecurityFilter;

public class SecurityFilter implements Filter {

	private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
	private static Logger logger = Logger.getLogger(SecurityFilter.class);
	private static String urlMathExpress = "^((?!login.jsp|login.action|login|autoLogin|reLogin).)*$";
	///itest/impExpMgr/bugImpExpAction!expBug.action
	public static Pattern pt;
	private FilterConfig filterConfig;
	//private static String welcomePage = "jsp/userManager/login.jsp";
	//private static String welcomePage = "itest/jsp/login.jsp";
	private static String welcomePage = "login.htm";
	protected String encoding = null;
	final private static String ENCODING_VALUE = "encoding";
	private static String appName = "mypm";
	public void destroy() {

	}

	public SecurityFilter() {
		super();
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletResponse httpResponse = (HttpServletResponse) response;
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml; charset=UTF-8");// jiangke
		httpResponse.setHeader("cache-Control", "no-cache"); // HTTP
		httpResponse.setHeader("pragma", "no-cache"); // HTTP
		httpResponse.setHeader("Cache-Control", "no-store");
		httpResponse.setDateHeader("expires", 0); // prevents
		String url = ((HttpServletRequest) request).getRequestURL().toString();
		
		
		String uri = ((HttpServletRequest) request).getRequestURI();
//		String ContextPath = ((HttpServletRequest) request).getContextPath();
//		String ServletPath = ((HttpServletRequest) request).getServletPath();
//		String PathInfo = ((HttpServletRequest) request).getPathInfo();
//		String QueryString = ((HttpServletRequest) request).getQueryString();
		//不让直接输放登录JSP和MAIN  JSP 
		if(uri!=null&&("/itest/itest/jsp/login.jsp".equals(uri)||uri.indexOf("/itest/itest/jsp/login.jsp")>=0)) {
			((HttpServletResponse)response).sendRedirect(((HttpServletRequest) request).getContextPath()+"/login.htm");
			return;
		}
		if(uri!=null&&("/itest/itest/jsp/main.jsp".equals(uri)||uri.indexOf("/itest/itest/jsp/main.jsp")>=0)) {
			((HttpServletResponse)response).sendRedirect(((HttpServletRequest) request).getContextPath()+"/login.htm");
			return;
		}
//		if(((HttpServletRequest) request).getSession().getAttribute("logined") == null &&url.indexOf("itest/jsp/main.jsp")>=0) {
//			((HttpServletResponse)response).sendRedirect(((HttpServletRequest) request).getContextPath()+"/login.htm");
//			return;
//		}

//		if (url.indexOf("login.jsp") > 0) {
//			Cookie[] cookies = ((HttpServletRequest) request).getCookies();
//			String loginName = "";
//			String pwd = "";
//			String autoLogin = "";
//			if (cookies != null) {
//				for (int i = 0; i < cookies.length; i++) {
//					Cookie c = cookies[i];
//					if (c.getName().equalsIgnoreCase("loginId")) {
//						loginName = c.getValue();
//					}
//					if (c.getName().equalsIgnoreCase("attachInfo")) {
//						pwd = c.getValue();
//					}
//					if (c.getName().equalsIgnoreCase("autoLogin")) {
//						autoLogin = c.getValue();
//					}
//				}
//				if (!"".equals(autoLogin)&&!"".equals(pwd)&&!"".equals(loginName) ) {
//					this.goAutoLoginPage(request, response);
//					//this.goLoginPage(request, response);
//					return;
//				}
//				((HttpServletRequest) request).getSession().invalidate();
//				chain.doFilter(request, response);
//				SecurityContextHolder.clearContext();
//				return;
//			}
//		}

		if (((HttpServletRequest) request).getSession().getAttribute("logined") == null&&pt.matcher(url).matches()) {
			if (logger.isInfoEnabled()) {
				logger.info("no access url==============:" + url);
			}
			 if (isAjaxRequest((HttpServletRequest) request)) {
				//this.goAjaxPageRest(response);
				 this.goLoginPage(request, response);
				return;
			}
			this.goLoginPage(request, response);
			return;
		}

//		if (url.indexOf("frameset") > 0 || url.indexOf("output") > 0) {// 这是为报表
//			String repTemplete = ((HttpServletRequest) request)
//					.getParameter("__report");
//			String templeteName = repTemplete.substring(repTemplete
//					.lastIndexOf("/") + 1);
//			templeteName = templeteName.substring(0, templeteName.indexOf("."));
//			if (!this.repSecurityChek(templeteName)) {
////				this.goLoginPage(request, response);
////				return;
//				SecurityContextHolder.clearContext();
//			 	RequestDispatcher rd = null;
//				rd = filterConfig.getServletContext().getRequestDispatcher("/jsp/common/reptHint.jsp"); 
//				SecurityContextHolder.clearContext();
//				rd.forward(request, response);
//			}else{
////				if(!AnalysisBlh.canViewRept()){
////				 	RequestDispatcher rd = null;
////					rd = filterConfig.getServletContext().getRequestDispatcher("/jsp/bugManager/hello.jsp"); 
////					SecurityContextHolder.clearContext();
////					rd.forward(request, response);
////					return;
////				}
//			}
//		}
		SecurityContext context = new SecurityContextImpl();
		context.setRequest((HttpServletRequest) request);
		context.setResponse(httpResponse);
		SecurityContextHolder.setContext(context);
		
		chain.doFilter(request, response);
		SecurityContextHolder.clearContext();
	}

	protected static boolean isAjaxRequest(HttpServletRequest request) {
		String requestType = request.getHeader("X-Requested-With");
		if (requestType != null && "XMLHttpRequest".equals(requestType)) {
			return true;
		}
		return false;
	}
	private boolean repSecurityChek(String url) {
		Visit visit = SecurityContextHolder.getContext().getVisit();
		if (visit == null) {
			return false;
		}
		if ("cost_prediction".equals(url)) {
			if (visit.getUserInfo().getPrivilege().contains(url)) {
				//writeOperaLog();
				return true;
			}
			return false;
		}
		// 很可能没点度量分析,这时没加载报表,所以visit.getUserInfo().getRepPrivilege()为null
		if (visit.getUserInfo().getRepPrivilege() == null) {
			return false;
		}
		if (visit.getUserInfo().getRepPrivilege().contains(url)) {
			//writeOperaLog();
			return true;
		}
		return false;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		pt = Pattern.compile(urlMathExpress);
		this.encoding = filterConfig.getInitParameter(ENCODING_VALUE);
		//SecurityFilter.appName = filterConfig.getServletContext().getContextPath();
	}

	private void goAjaxPageRest(ServletResponse response) {
		try {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setContentType(CONTENT_TYPE);
			PrintWriter out = response.getWriter();
			out.print("overdue");
			out.flush();
			SecurityContextHolder.clearContext();
		} catch (IOException iox) {
			filterConfig.getServletContext().log(iox.getMessage());
		}
	}

	private void goLoginPage(ServletRequest request, ServletResponse response) {
		try {
			SecurityContextHolder.clearContext();
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setContentType(CONTENT_TYPE);
			httpResponse.setHeader("sessionstatus", "timeout");
			PrintWriter out = response.getWriter();
			StringBuffer sb = new StringBuffer();
			sb.append("<html><head><script type='text/javascript'>function toLgin() { top.location='");
			sb.append(httpRequest.getContextPath()).append("/").append(
					welcomePage).append("'}</script>");
			sb.append("</head><body onload='toLgin()'></body></html>");
			out.print(sb.toString());
			SecurityContextHolder.clearContext();
		} catch (IOException iox) {
			filterConfig.getServletContext().log(iox.getMessage());
		}
	}

	private void goAutoLoginPage(ServletRequest request,
			ServletResponse response) {
		try {
			SecurityContextHolder.clearContext();
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setContentType(CONTENT_TYPE);
			PrintWriter out = response.getWriter();
			StringBuffer sb = new StringBuffer();
			sb.append("<html><head><script type='text/javascript'>function toLgin() { top.location='");
			sb.append(httpRequest.getContextPath()).append("/").append(
					"jsp/userManager/autoLogin.jsp").append("'}</script>");
			sb.append("</head><body onload='toLgin()'></body></html>");
			out.print(sb.toString());
			SecurityContextHolder.clearContext();
		} catch (IOException iox) {
			filterConfig.getServletContext().log(iox.getMessage());
		}
	}

	public static void main(String[] arg) {

		String path = "/report/overview/abstract.rptdesign";
		System.out.println(path.substring(path.lastIndexOf("/") + 1));
	}

	public static String getAppName() {
		return appName;
	}

}
