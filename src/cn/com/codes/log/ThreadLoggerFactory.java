package cn.com.codes.log;


import java.io.IOException;
import java.util.Enumeration;
 

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
 

public class ThreadLoggerFactory {
	static  final String prefix="cn.com.codes.log";
	static Logger parentLogger ;
	static {
		parentLogger=Logger.getLogger(prefix);
	}
	public  static Logger getLogger(){
		Logger logger =Logger.getLogger(prefix+Thread.currentThread().getName());
		addAppender(logger,Thread.currentThread().getName());
		return logger;
	}
	/**
	 * @param name
	 */
	@SuppressWarnings("unchecked")
	private static void addAppender(Logger logger,String threadName) {
		Enumeration<Appender> appends=parentLogger.getAllAppenders();
		if(!logger.getAllAppenders().hasMoreElements()){
			while (appends.hasMoreElements()) {
				Appender appender = (Appender) appends.nextElement();
				if(appender instanceof ThreadDailyRollingAppenderDecorator){
					ThreadDailyRollingAppenderDecorator myappender=(ThreadDailyRollingAppenderDecorator)appender;
					try {
						logger.addAppender(new ThreadDailyRollingAppenderDecorator(myappender,threadName));
					} catch (IOException e) {
						e.printStackTrace();
					}
					continue;
				}
				logger.addAppender(appender);
			}
		}
		
	}
}
