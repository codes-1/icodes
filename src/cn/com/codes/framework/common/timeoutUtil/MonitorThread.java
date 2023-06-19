package cn.com.codes.framework.common.timeoutUtil;

import static cn.com.codes.framework.common.LogWrap.info;

import org.apache.log4j.Logger;

import cn.com.codes.framework.common.timeoutUtil.MonitorThread;

public class MonitorThread extends Thread {
	private static final Logger log = Logger.getLogger(MonitorThread.class);
	private boolean flag;
	private Object result; 
	private long timeout;

	public MonitorThread(Object defaultResult, long timeout) {
		this.result = defaultResult;
		this.timeout = timeout;
		this.setDaemon(true);
	}

	public void run() {
		try {
			Thread.sleep(timeout);
			setResult(null);
		} catch (InterruptedException e) {
			//TimeoutHelper.exceptionFlag = true;
			info(log, "MonitorThread监控线程出错, 错误原因：" + e.getMessage());
		}
	}

	public synchronized void waitFor() throws InterruptedException {
		while (!flag) {
			wait();
		}
	}

	public synchronized void setResult(Object o) {
		if (!flag) {
			if (o != null)
				result = o;
			flag = true;
			notifyAll();
		}
	}

	public Object getResult() {
		return result;
	}
}
