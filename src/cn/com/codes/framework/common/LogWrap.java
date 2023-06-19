package cn.com.codes.framework.common;

import org.apache.log4j.Logger;

public class LogWrap {

	public static void info(Logger logger, Object msgObj) {
		if (logger.isInfoEnabled()) {
			logger.info(msgObj);
		}

	}

	public static void debug(Logger logger, Object msgObj) {
		if (logger.isDebugEnabled()) {
			logger.debug(msgObj);
		}

	}

	public static void error(Logger logger, Object msgObj) {

		logger.error(msgObj);

	}

	public static void info(Class c, Object msgObj) {
		Logger logger = Logger.getLogger(c);
		if (logger.isInfoEnabled()) {
			logger.info(msgObj);
		}
		logger = null;
	}

	public static void debug(Class c, Object msgObj) {
		Logger logger = Logger.getLogger(c);
		if (logger.isDebugEnabled()) {
			logger.debug(msgObj);
		}
		logger = null;
	}

	public static void error(Class c, Object msgObj) {
		Logger logger = Logger.getLogger(c);
		logger.error(msgObj);
		logger = null;
	}
}
