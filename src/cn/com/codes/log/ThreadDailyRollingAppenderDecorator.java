package cn.com.codes.log;


import java.io.File;
import java.io.IOException;
 
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Layout;
 
 

public class ThreadDailyRollingAppenderDecorator extends
		DailyRollingFileAppender implements Cloneable {
	private int maxFileSize;
	private int maxThreadSize;
	private String filePath;
	private String threadLogFileName;
	DailyRollingFileAppender fileAppender;
 
	public ThreadDailyRollingAppenderDecorator() {
		super();
	}
 
	public ThreadDailyRollingAppenderDecorator(Layout layout, String filename,
			String datePattern) throws IOException {
		super(layout, filename, datePattern);
	}
	
 
	public ThreadDailyRollingAppenderDecorator(
			ThreadDailyRollingAppenderDecorator fileAppender,String threadName) throws IOException {
		this(fileAppender.getLayout(),fileAppender.getFileName(threadName),fileAppender.getDatePattern());
		this.fileAppender = fileAppender;
	}
 
	public DailyRollingFileAppender getFileAppender() {
		return fileAppender;
	}
 
	public void setFileAppender(DailyRollingFileAppender fileAppender) {
		this.fileAppender = fileAppender;
	}
 
	public int getMaxFileSize() {
		return maxFileSize;
	}
 
	public void setMaxFileSize(int maxFileSize) {
		this.maxFileSize = maxFileSize;
	}
 
	public Object copy() {
		try {
			return this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
 
	public int getMaxThreadSize() {
		return maxThreadSize;
	}
 
	public void setMaxThreadSize(int maxThreadSize) {
		this.maxThreadSize = maxThreadSize;
	}
 
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
 
	public String getThreadLogFileName() {
		return threadLogFileName;
	}
 
	public void setThreadLogFileName(String threadLogFileName) {
		this.threadLogFileName = threadLogFileName;
	}
 
	public void init(String threadName) {
		this.name = threadName;
		this.setFile(getFileName(threadName));
		try {
			this.setFile(this.fileName, true, false, this.bufferSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	/**
	 * @return
	 */
	public  String getFileName(String threadName) {
		return this.filePath + File.separator + threadName + File.separator
				+ this.threadLogFileName;
	}
 
}
