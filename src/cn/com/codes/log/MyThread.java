package cn.com.codes.log;

import org.apache.log4j.Logger;


class MyThread implements Runnable{
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	String logName;
	 
	public MyThread(String logName) {
		this.logName = logName;
	}
	
	
	public void run() {
			// 在run方法内实现线程独立的logger实例
			Logger logger = ThreadLogger.getLogger(logName,"");
	 
			logger.info(logName + "_" + Thread.currentThread().getName() + " started!");
	 
			logger.error("this is error!");
	 
			logger.info(logName + "_" + Thread.currentThread().getName() + " finished!");
	}
	
}
