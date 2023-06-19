package cn.com.codes.framework.security.filter;

import org.springframework.util.Assert;

import cn.com.codes.framework.security.filter.SecurityContext;
import cn.com.codes.framework.security.filter.SecurityContextImpl;



/**
 * @author liuyg
 *
 */
public class SecurityContextHolder {

	private static ThreadLocal contextHolder = new ThreadLocal();
	
	public static void clearContext() {
        contextHolder.set(null);
    }

    public static SecurityContext getContext() {
    	
    	SecurityContext sc = (SecurityContext)contextHolder.get();
        if (sc == null) {
        	sc = new SecurityContextImpl();
            contextHolder.set(sc);
        }

        return sc;
    }

    public static void setContext(SecurityContext context) {
        Assert.notNull(context, "Only non-null SecurityContext instances are permitted");
        contextHolder.set(context);
    }

}
