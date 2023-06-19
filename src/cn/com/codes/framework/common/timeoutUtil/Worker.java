package cn.com.codes.framework.common.timeoutUtil;

import static cn.com.codes.framework.common.LogWrap.info;

import org.apache.log4j.Logger;

import cn.com.codes.framework.common.timeoutUtil.MonitorThread;
import cn.com.codes.framework.common.timeoutUtil.WorkThread;
import cn.com.codes.framework.common.timeoutUtil.Worker;

public class Worker {
	private static final Logger log = Logger.getLogger(Worker.class);
	public Object work(String methodName, Object defaultValue, long timeout,
			Object currentObj, Object[] paramValues, int[] primitiveTypeIndex) {
		if (paramValues == null) {
			paramValues = new Object[] {};
		}
		if (primitiveTypeIndex == null) {
			primitiveTypeIndex = new int[] {};
		}
		try {
			MonitorThread monitor = new MonitorThread(defaultValue, timeout);
			WorkThread worker = new WorkThread(monitor, currentObj, methodName,
					paramValues, primitiveTypeIndex);

			worker.start();
			monitor.start();

			monitor.waitFor();

			if (worker.isAlive()) {
				worker.interrupt();
			}
			if (monitor.isAlive()) {
				monitor.interrupt();
			}
			return monitor.getResult();
		} catch (Exception e) {
			info(log, "监控方法出错, 错误原因：" + e.getMessage());
			return false;
		}

	}
}