package org.eclipse.birt.report.listener;

public class MonitorObj {

	private Long threadCount = null;
	private static final MonitorObj instance = new MonitorObj();

	private MonitorObj() {
		threadCount = new Long(0);
	}

	public final static MonitorObj getInstance() {
		return instance;
	}

	public synchronized void threadInit() {
		this.threadCount = this.threadCount + 1;
	}

	public synchronized void threadDestroyed() {
		if (this.threadCount > 0) {
			this.threadCount = this.threadCount - 1;
		}
	}
	public Long getThreadCount() {
		return threadCount;
	}

}
