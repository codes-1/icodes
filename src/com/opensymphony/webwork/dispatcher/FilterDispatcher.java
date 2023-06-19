/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.webwork.dispatcher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.birt.report.listener.ViewerServletContextListener;

import com.opensymphony.util.ClassLoaderUtil;
import com.opensymphony.webwork.RequestUtils;
import com.opensymphony.webwork.WebWorkConstants;
import com.opensymphony.webwork.WebWorkStatics;
import com.opensymphony.webwork.config.Configuration;
import com.opensymphony.webwork.dispatcher.mapper.ActionMapper;
import com.opensymphony.webwork.dispatcher.mapper.ActionMapperFactory;
import com.opensymphony.webwork.dispatcher.mapper.ActionMapping;
import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.interceptor.component.ComponentConfiguration;
import com.opensymphony.xwork.interceptor.component.ComponentManager;
import com.opensymphony.xwork.interceptor.component.DefaultComponentManager;

import cn.com.codes.framework.common.util.Context;
import cn.com.codes.framework.common.util.MypmBean;
import cn.com.codes.framework.hibernate.HibernateGenericController;
import cn.com.codes.framework.jms.CommonMessageListener;
import cn.com.codes.userManager.blh.PasswordTool;

/**
 * Master filter for WebWork that handles four distinct responsibilities:
 *
 * <ul>
 *
 * <li>Executing actions</li>
 *
 * <li>Cleaning up the {@link ActionContext} (see note)</li>
 *
 * <li>Serving static content</li>
 *
 * <li>Kicking off XWork's IoC for the request lifecycle</li>
 *
 * </ul>
 *
 * <p/> <b>IMPORTANT</b>: this filter must be mapped to all requests. Unless you know exactly what you are doing, always
 * map to this URL pattern: /*
 *
 * <p/> <b>Executing actions</b>
 *
 * <p/> This filter executes actions by consulting the {@link ActionMapper} and determining if the requested URL should
 * invoke an action. If the mapper indicates it should, <b>the rest of the filter chain is stopped</b> and the action is
 * invoked. This is important, as it means that filters like the SiteMesh filter must be placed <b>before</b> this
 * filter or they will not be able to decorate the output of actions.
 *
 * <p/> <b>Cleaning up the {@link ActionContext}</b>
 *
 * <p/> This filter will also automatically clean up the {@link ActionContext} for you, ensuring that no memory leaks
 * take place. However, this can sometimes cause problems integrating with other products like SiteMesh. See {@link
 * ActionContextCleanUp} for more information on how to deal with this.
 *
 * <p/> <b>Serving static content</b>
 *
 * <p/> This filter also serves common static content needed when using various parts of WebWork, such as JavaScript
 * files, CSS files, etc. It works by looking for requests to /webwork/*, and then mapping the value after "/webwork/"
 * to common packages in WebWork and, optionally, in your class path. By default, the following packages are
 * automatically searched:
 *
 * <ul>
 *
 * <li>com.opensymphony.webwork.static</li>
 *
 * <li>template</li>
 *
 * </ul>
 *
 * <p/> This means that you can simply request /webwork/xhtml/styles.css and the XHTML UI theme's default stylesheet
 * will be returned. Likewise, many of the AJAX UI components require various JavaScript files, which are found in the
 * com.opensymphony.webwork.static package. If you wish to add additional packages to be searched, you can add a comma
 * separated (space, tab and new line will do as well) list in the filter init parameter named "packages". <b>Be
 * careful</b>, however, to expose any packages that may have sensitive information, such as properties file with
 * database access credentials.
 *
 * <p/> <b>Kicking off XWork's IoC for the request lifecycle</b>
 *
 * <p/> This filter also kicks off the XWork IoC request scope, provided that you are using XWork's IoC. All you have to
 * do to get started with XWork's IoC is add a components.xml file to WEB-INF/classes and properly set up the {@link
 * com.opensymphony.webwork.lifecycle.LifecycleListener} in web.xml. See the IoC docs for more information. <p/>
 *
 * @author Patrick Lightbody
 * @author tm_jee
 * @see com.opensymphony.webwork.lifecycle.LifecycleListener
 * @see ActionMapper
 * @see ActionContextCleanUp
 * @since 2.2
 */
public class FilterDispatcher implements Filter, WebWorkStatics {
    private static final Log LOG = LogFactory.getLog(FilterDispatcher.class);

    protected FilterConfig filterConfig;
    protected String[] pathPrefixes;

    private SimpleDateFormat df = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss");
    private final Calendar lastModifiedCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    private final String lastModified = df.format(lastModifiedCal.getTime());

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    public void destroy() {
    	DispatcherUtils du = DispatcherUtils.getInstance(); // should not be null as it is initialized in init(FilterConfig)
    	if (du == null) {
    		LOG.warn("something is seriously wrong, DispatcherUtil is not initialized (null) ");
    	}
    	du.cleanup();
    }
//	private long  getHaveViewCount(){
//		HibernateGenericController hibernate = (HibernateGenericController) Context.getInstance().getBean(
//		"hibernateGenericController");
//		Long count=null;;
//		try {
//			count = new Long(hibernate.findBy("select count(*) from t_syslog where ACCESS_IP='1289630248984' ", 1).get(0).toString());
//		}catch (NumberFormatException e) {
//			return 0;
//		}catch (Exception e) {
//			return 0;
//		}
//		//return  count;
//		return  1000;
//	}
//	
//	private void reptInit(){
//		
//		long viewCount = getHaveViewCount();
//		MypmBean conf = MypmBean.getInstacne();
//		if(viewCount==0){
//			String vc = conf.getProperty(PasswordTool.DecodePasswd("2QK8S_[#[#3CY%U("));
//			long infactVc = 0;
//			try {
//				infactVc = new Long(PasswordTool.DecodePasswd(vc));
//			}catch (NumberFormatException e) {
//			}catch (Exception e) {	
//			}
//			if(!"0".equals(PasswordTool.DecodePasswd(vc).trim())){
//				CommonMessageListener.setReptViewCount(infactVc);
//			}
//		}else{
//			String vcStr = String.valueOf(viewCount);
//			String envcStr = PasswordTool.EncodePasswd(vcStr);
//			while(!vcStr.equals(PasswordTool.DecodePasswd(envcStr))){
//				envcStr = PasswordTool.EncodePasswd(vcStr);
//			}
//			conf.setProperty(PasswordTool.DecodePasswd("2QK8S_[#[#3CY%U("),envcStr);
//			try {
//				conf.write();
//			} catch (IOException e) {
//			}			
//		}
//		CommonMessageListener.setReptViewCount(viewCount);
//		long reptLimitCount = (300+200);
//		try {
//			reptLimitCount = new Long(PasswordTool.DecodePasswd(conf.getProperty(PasswordTool.DecodePasswd("K?R<R29PQl5#-%PHbe"))));
//		} catch (NumberFormatException e) {
//			reptLimitCount= (300+200);
//		}catch (Exception e) {	
//		}
//		CommonMessageListener.setReptLimitCount(reptLimitCount);
//		ViewerServletContextListener.setHaveLoadRepInfo(true);
//	}
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        String param = filterConfig.getInitParameter("packages");
        String packages = "com.opensymphony.webwork.static template com.opensymphony.webwork.interceptor.debugging";
        if (param != null) {
            packages = param + " " + packages;
        }
        this.pathPrefixes = parse(packages);
        DispatcherUtils.initialize(filterConfig.getServletContext());
		 if(LOG.isInfoEnabled()){
			 DateFormat lfmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss, SSS");
			System.out.println(lfmt.format(new Date())+" org.apache.catalina.core.StandardHostDeployer install");
		 }
		 //this.reptInit();
    }

    protected String[] parse(String packages) {
        if (packages == null) {
            return null;
        }
        List pathPrefixes = new ArrayList();

        StringTokenizer st = new StringTokenizer(packages, ", \n\t");
        while (st.hasMoreTokens()) {
            String pathPrefix = st.nextToken().replace('.', '/');
            if (!pathPrefix.endsWith("/")) {
                pathPrefix += "/";
            }
            pathPrefixes.add(pathPrefix);
        }

        return (String[]) pathPrefixes.toArray(new String[pathPrefixes.size()]);
    }

//	public static boolean canViewRept(){
//		if((CommonMessageListener.getReptViewCount())>=(CommonMessageListener.getReptLimitCount())){
//			return false;
//		}
//		return true;
//	}
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        ServletContext servletContext = filterConfig.getServletContext();
       // request.setAttribute("public", canViewRept()?"1":"0");
        // prepare the request no matter what - this ensures that the proper character encoding
        // is used before invoking the mapper (see WW-9127)
        DispatcherUtils du = DispatcherUtils.getInstance();
        du.prepare(request, response);
        try {
        	// Wrap request first, just in case it is multipart/form-data
        	// parameters might not be accessible through before encoding (ww-1278)
            request = du.wrapRequest(request, servletContext);
        } catch (IOException e) {
            String message = "Could not wrap servlet request with MultipartRequestWrapper!";
            LOG.error(message, e);
            throw new ServletException(message, e);
        }
        

        ActionMapper mapper = ActionMapperFactory.getMapper();
        ActionMapping mapping = mapper.getMapping(request);

        if (mapping == null) {
            // there is no action in this request, should we look for a static resource?
            String resourcePath = RequestUtils.getServletPath(request);

            if ("".equals(resourcePath) && null != request.getPathInfo()) {
                resourcePath = request.getPathInfo();
            }

            if ("true".equals(Configuration.get(WebWorkConstants.WEBWORK_SERVE_STATIC_CONTENT)) 
                    && resourcePath.startsWith("/webwork")) {
                String name = resourcePath.substring("/webwork".length());
                findStaticResource(name, response);
            } else {
                // this is a normal request, let it pass through
                chain.doFilter(request, response);
            }
            // WW did its job here
            return;
        }


        Object o = null;
        try {

            setupContainer(request);
            o = beforeActionInvocation(request, servletContext);

            du.serviceAction(request, response, servletContext, mapping);
        } finally {
            afterActionInvocation(request, servletContext, o);
            ActionContextCleanUp.cleanUp(req);
        }
    }

    protected void afterActionInvocation(HttpServletRequest request, Object o, Object o1) {
        // nothing by default, but a good hook for scoped ioc integration
    }

    protected Object beforeActionInvocation(HttpServletRequest request, ServletContext servletContext) {
        // nothing by default, but a good hook for scoped ioc integration
        return null;
    }

    protected void setupContainer(HttpServletRequest request) {
        ComponentManager container = null;
        HttpSession session = request.getSession(false);
        ComponentManager fallback = null;
        if (session != null) {
            fallback = (ComponentManager) session.getAttribute(ComponentManager.COMPONENT_MANAGER_KEY);
        }

        ServletContext servletContext = getServletContext(session);
        if (fallback == null) {
            fallback = (ComponentManager) servletContext.getAttribute(ComponentManager.COMPONENT_MANAGER_KEY);
        }

        if (fallback != null) {
            container = createComponentManager();
            container.setFallback(fallback);
        }

        ComponentConfiguration config = (ComponentConfiguration) servletContext.getAttribute("ComponentConfiguration");
        if (config != null) {
            if (container == null) {
                container = createComponentManager();
            }

            config.configure(container, "request");
            request.setAttribute(ComponentManager.COMPONENT_MANAGER_KEY, container);
        }
    }

    /**
     * Servlet 2.3 specifies that the servlet context can be retrieved from the session. Unfortunately, some versions of
     * WebLogic can only retrieve the servlet context from the filter config. Hence, this method enables subclasses to
     * retrieve the servlet context from other sources.
     *
     * @param session the HTTP session where, in Servlet 2.3, the servlet context can be retrieved
     * @return the servlet context.
     */
    protected ServletContext getServletContext(HttpSession session) {
        return filterConfig.getServletContext();
    }

    protected void findStaticResource(String name, HttpServletResponse response) throws IOException {
        if (!name.endsWith(".class")) {
            for (int i = 0; i < pathPrefixes.length; i++) {
                InputStream is = findInputStream(name, pathPrefixes[i]);
                if (is != null) {
                    // set the content-type header
                    String contentType = getContentType(name);
                    if (contentType != null) {
                        response.setContentType(contentType);
                    }

                    // set heading information for caching static content
                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                    response.setHeader("Date",df.format(cal.getTime())+" GMT");
                    cal.add(Calendar.DAY_OF_MONTH,1);
                    response.setHeader("Expires",df.format(cal.getTime())+" GMT");
                    response.setHeader("Retry-After",df.format(cal.getTime())+" GMT");
                    response.setHeader("Cache-Control","public");
                    response.setHeader("Last-Modified",lastModified+" GMT");

                    try {
                        copy(is, response.getOutputStream());
                    } finally {
                        is.close();
                    }
                    return;
                }
            }
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private String getContentType(String name) {
        // NOT using the code provided activation.jar to avoid adding yet another dependency
        // this is generally OK, since these are the main files we server up
        if (name.endsWith(".js")) {
            return "text/javascript";
        } else if (name.endsWith(".css")) {
            return "text/css";
        } else if (name.endsWith(".html")) {
            return "text/html";
        } else if (name.endsWith(".txt")) {
            return "text/plain";
        } else if (name.endsWith(".gif")) {
            return "image/gif";
        } else if (name.endsWith(".jpg") || name.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (name.endsWith(".png")) {
            return "image/png";
        } else {
            return null;
        }
    }

    protected void copy(InputStream input, OutputStream output) throws IOException {
        final byte[] buffer = new byte[4096];
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
	// some app server eg WebSphere6 doesn't like it if didn't flush (WW-1384)
	output.flush();
    }

    protected InputStream findInputStream(String name, String packagePrefix) throws IOException {
        String resourcePath;
        if (packagePrefix.endsWith("/") && name.startsWith("/")) {
            resourcePath = packagePrefix + name.substring(1);
        } else {
            resourcePath = packagePrefix + name;
        }

        String enc = (String) Configuration.get(WebWorkConstants.WEBWORK_I18N_ENCODING);
        resourcePath = URLDecoder.decode(resourcePath, enc);

        return ClassLoaderUtil.getResourceAsStream(resourcePath, getClass());
    }

    /**
     * handle .. chars here and other URL hacks
     */
    protected boolean checkUrl(URL url, String rawResourcePath) {

        // ignore folder resources - they provide streams too ! dunno why :)
        if (url.getPath().endsWith("/")) {
            return false;
        }

        // check for parent path access
        // NOTE : most servlet containers shoudl resolve .. chars in the request url anyway
        if (url.toExternalForm().indexOf(rawResourcePath) == -1) {
            return false;
        }

        return true;
    }

    /**
     * Returns a new <tt>DefaultComponentManager</tt> instance. This method is useful for developers wishing to subclass
     * this class and provide a different implementation of <tt>DefaultComponentManager</tt>.
     *
     * @return a new <tt>DefaultComponentManager</tt> instance.
     */
    protected DefaultComponentManager createComponentManager() {
        return new DefaultComponentManager();
    }
    


}
