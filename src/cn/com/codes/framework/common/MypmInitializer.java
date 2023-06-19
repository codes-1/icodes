package cn.com.codes.framework.common;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MypmInitializer  implements ServletContextListener{
	
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

	public void contextInitialized(ServletContextEvent sce) {
//		ServletContext context = sce.getServletContext();
//		File nativeDir = new File(context.getRealPath("/WEB-INF/native/"));
//		File[] libs = nativeDir.listFiles();
//		for (int i = 0; i < libs.length; i++) {
//			if (libs[i].isFile())
//				try {
//					System.load(new File(nativeDir, libs[i].getName()).getPath());
//				} catch (Throwable t) {
//					//t.printStackTrace();
//					//System.exit(0);
//				}
//		}
	}
}
