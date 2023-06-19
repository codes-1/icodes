package cn.com.codes.log;



import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.springframework.web.context.ContextLoader;

 
public class ThreadLogger {
	
	private static final Logger logger = Logger.getLogger(ThreadLogger.class);
	/**
	 * 日志格式化配置文件路径
	 */
	private static final String logFormatFilePath;
	
	
	static {
		logFormatFilePath = findLogFormatFilePath();
	}
	
    /**
    * 方法名:  findLogFormatFilePath
    * 方法功能描述: 获取日志格式化文件的路径
    * @return 文件路径
    * @Author: 张飞
    * @Create Date:  2015年1月12日 下午4:38:57
     */
	public static String findLogFormatFilePath() {
		ServletContext  servletContext= ContextLoader.getCurrentWebApplicationContext().getServletContext();
		String logFormatFilePath = servletContext.getRealPath(File.separator);
		logger.info("logFormatFilePath = "+logFormatFilePath);
		logFormatFilePath = logFormatFilePath+File.separator + "itest"+File.separator+"logs"+File.separator;
		// 日志文件按照每天分文件夹存放
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		logFormatFilePath = logFormatFilePath + sdf.format(new Date()) +File.separator;
		return logFormatFilePath;
	}
	
	
	public static Logger getLogger(String branchPath,String logName) {
		Logger logger = null;
 
		logger = Logger.getLogger(logName);
		PatternLayout layout = new PatternLayout("[%d{MM-dd HH:mm:ss}] %-5p %-8t %m%n");
 
		// 日志文件按照每天分文件夹存放
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		String logPath = logFormatFilePath + sdf.format(new Date()) + "/";
		String logPath = logFormatFilePath;
		if(StringUtils.isNotBlank(branchPath)){
			logPath+=branchPath+File.separator;
		}
		// 文件输出
		ThreadLogger.ThreadFileAppender fileAppender = null;
 
		try {
			fileAppender = new ThreadFileAppender(layout, logPath, logName, "yyyy-MM-dd");
		} catch (IOException e) {
			e.printStackTrace();
		}
		fileAppender.setAppend(false);
		fileAppender.setImmediateFlush(true);
		fileAppender.setThreshold(Level.DEBUG);
 
		// 绑定到logger
		logger.setLevel(Level.DEBUG);
		logger.addAppender(fileAppender);
 
		return logger;
	}
 
	/*
	 * 继承了log4j类的内部类
	 */
	public static class ThreadFileAppender extends DailyRollingFileAppender {
		public ThreadFileAppender(Layout layout, String filePath, String fileName, String datePattern)
				throws IOException {
			super(layout, filePath + fileName + ".log", datePattern);
		}
	}
	
	
}
