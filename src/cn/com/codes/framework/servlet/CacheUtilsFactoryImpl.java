package cn.com.codes.framework.servlet;


import java.util.HashMap;

import org.apache.log4j.Logger;

import cn.com.codes.framework.servlet.CacheUtilsFactory;
import cn.com.codes.framework.servlet.CacheUtilsFactoryImpl;

public class CacheUtilsFactoryImpl implements CacheUtilsFactory {
	public static final String FACTORY_IMPL_NAME = "cn.com.codes.framework.servlet.CacheUtilsFactoryImpl";
	private static Logger logger = Logger.getRootLogger();
	private static HashMap map = new HashMap();
	private HashMap cacheRegionMap;
	
	public static CacheUtilsFactoryImpl getInstance() {
		return CacheUtilsFactoryImpl.getInstance(FACTORY_IMPL_NAME);
	}

	public static CacheUtilsFactoryImpl getInstance(String className) {
		CacheUtilsFactoryImpl singleton = (CacheUtilsFactoryImpl) map.get(className);

		if (singleton != null) {
			return singleton;
		}

		try {
			Class klass = getClass(className);
			singleton = (CacheUtilsFactoryImpl) klass.newInstance();
		} catch (Exception e) {
			logger.fatal("Couldn't find class " + className);
		}

		map.put(className, singleton);
		logger.info("created singleton: " + singleton);

		return singleton;
	}

	private static Class getClass(String className)
			throws ClassNotFoundException {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();

		if (classLoader == null)
			classLoader = CacheUtilsFactoryImpl.class.getClassLoader();

		return (classLoader.loadClass(className));
	}

	public HashMap getCacheRegionMap() {
		return cacheRegionMap;
	}

	public void setCacheRegionMap(HashMap cacheRegionMap) {
		this.cacheRegionMap = cacheRegionMap;
	}
}
