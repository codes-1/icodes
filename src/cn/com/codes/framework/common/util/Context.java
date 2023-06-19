package cn.com.codes.framework.common.util;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.io.Resource;

import cn.com.codes.framework.common.util.Context;

public class Context implements ApplicationContextAware {

	// Singleton instance
	private static Context instance;

	// The Spring's application context
	private static ApplicationContext applicationContext;

	private Context() {
		Context.instance = this;
	}

	public static Context getInstance() {
		return instance;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		Context.applicationContext = applicationContext;
	}

	/**
	 * Retrieves a bean registered in the Spring context.
	 * 
	 * @param id
	 *            The bean identifier
	 * @return The bean instance
	 */
	public Object getBean(String id) {
		return applicationContext.getBean(id);
	}

	/**
	 * Retrieves a bean registered in the Spring context using a class name. At
	 * first the fully qualified class name is checked, then if nothing was
	 * found the simple class name is used as bean id.
	 * 
	 * @param clazz
	 *            The bean identifier as class name
	 * @return The bean instance
	 */
	public Object getBean(Class clazz) {
		String id = clazz.getName();

		if (!applicationContext.containsBean(id))
			id = id.substring(id.lastIndexOf('.') + 1);

		return getBean(id);
	}

	/**
	 * Reloads the Spring application context.
	 * <p>
	 * NOTE: use carefully, invoke only during setup when the config.xml is
	 * changed
	 */
	// public static void refresh() {
	// if (applicationContext != null) {
	// try {
	// ((AbstractApplicationContext) applicationContext).stop();
	// ((AbstractApplicationContext) applicationContext).start();
	// ((AbstractApplicationContext) applicationContext).refresh();
	// } catch (Throwable e) {
	// // e.printStackTrace();
	// }
	// }
	// }

	public static void initContext() {
		if (applicationContext != null) {
			try {
				//System.out.println("==================initContext1==================");
				((AbstractApplicationContext) applicationContext).stop();
				((AbstractApplicationContext) applicationContext).start();
				((AbstractApplicationContext) applicationContext).refresh();
			} catch (Throwable e) {
				// e.printStackTrace();
			}
		}
	}

	public static void reloadCacheData() {
		
		if (applicationContext != null) {
			//System.out.println("==================initContext22==================");
			try {
				((AbstractApplicationContext) applicationContext).stop();
				((AbstractApplicationContext) applicationContext).start();
				((AbstractApplicationContext) applicationContext).refresh();
			} catch (Throwable e) {
				// e.printStackTrace();
			}
		}
	}

	public Resource[] getResources(String resourcePattern) {
		try {
			return this.applicationContext.getResources(resourcePattern);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Returns a Spring-Resource
	 * 
	 * @return
	 */
	public Resource getResource(String resourceLocation) {
		return this.applicationContext.getResource(resourceLocation);
	}

}
