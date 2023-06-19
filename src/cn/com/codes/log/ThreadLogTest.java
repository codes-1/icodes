package cn.com.codes.log;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
 

public class ThreadLogTest {
	static Logger logger=Logger.getLogger(ThreadLogTest.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			ExecutorService executor = Executors.newCachedThreadPool();
			
//			ExecutorService executor = Executors. newFixedThreadPool(10);
//			logger.info("任务开始执行");
// 
//			for (int i = 0; i < 10; i++) {
//				MyThread thread = new MyThread(String.valueOf(i));
//				executor.submit(thread);
//			}
//			executor.shutdown();
			
//			String logName = ThreadLogger.findLogFormatFilePath();
//			String logName = "asdadad";
//			Logger logger = ThreadLogger.getLogger(logName);
//			logger.info(logName + "_" + Thread.currentThread().getName() + " started!");
//			 
//			logger.error("this is error!");
//	 
//			logger.info(logName + "_" + Thread.currentThread().getName() + " finished!");
			
			String filePath ="E:\\workspace3\\mypmNew4.2\\WebRoot\\itest\\logs\\2020-07-14\\4028e40372e5a0750172e5a1a7380001.log";
			if(filePath.contains("WebRoot")){
				filePath = filePath.substring(filePath.indexOf("WebRoot"), filePath.length());
				filePath = filePath.replace("WebRoot", "");
				logger.info("filePath:"+filePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("任务结束！");

		
	}
 
}
