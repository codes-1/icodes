package cn.com.codes.framework.common.timeoutUtil;


import static cn.com.codes.framework.common.LogWrap.info;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import cn.com.codes.framework.common.timeoutUtil.MonitorThread;
import cn.com.codes.framework.common.timeoutUtil.TimeoutHelper;
import cn.com.codes.framework.common.timeoutUtil.WorkThread;

public class WorkThread extends Thread {
	private static final Logger log = Logger.getLogger(WorkThread.class);
	MonitorThread monitor;
	String methodName;
	Object[] paramValues;
	Class[] paramClasses;
	Object currentObj;

	public WorkThread(MonitorThread monitor, Object currentObj,
			String methodName, Object[] paramValues, int[] primitiveTypeIndex) throws Exception{
		this.monitor = monitor;
		this.methodName = methodName;
		this.paramValues = paramValues;
		this.currentObj = currentObj;
		paramClasses = new Class[paramValues.length];

		for (int i = 0; i < primitiveTypeIndex.length; i++) {
			paramClasses[primitiveTypeIndex[i]] = (Class) (paramValues[primitiveTypeIndex[i]]
					.getClass().getDeclaredField("TYPE").get(null));

		}
		for (int i = 0; i < paramValues.length; i++) {
			if (paramClasses[i] == null) {
				paramClasses[i] = paramValues[i].getClass();
			}
		}

		this.setDaemon(true); 
		}

	public void run() {
		Method method = null;
		try {
			method = currentObj.getClass().getDeclaredMethod(methodName,
					paramClasses);
			Object value = method.invoke(currentObj, paramValues);
			monitor.setResult(value);
		} catch (Exception e) {
			TimeoutHelper.exceptionFlag = true;
			info(log, "WorkThread监控线程出错, 错误原因：" + e.getMessage());
		}
	}
}