package cn.com.codes.framework.upload;

public class UploadListener implements OutputStreamListener {
	// 保存状态的内部类对象
	private FileUploadStats fileUploadStats = new FileUploadStats();

	// 构造方法
	public UploadListener(long totalSize) {
		fileUploadStats.setTotalSize(totalSize);
	}
	public UploadListener(){
		
	}
	public void setTotalSize(long totalSize){
		fileUploadStats.setTotalSize(totalSize);
	}
	public void start() {
		// 设置当前状态为开始
		fileUploadStats.setCurrentStatus("start");
	}

	public void bytesRead(int byteCount) {
		// 将已读取的数据保存到状态对象中
		fileUploadStats.incrementBytesRead(byteCount);
		// 设置当前的状态为读取过程中
		fileUploadStats.setCurrentStatus("reading");
	}

	public void error(String s) {
		// 设置当前状态为出错
		fileUploadStats.setCurrentStatus("error");
	}

	public void done() {
		// 设置当前已读取数据等于总数据大小
		fileUploadStats.setBytesRead(fileUploadStats.getTotalSize());
		// 设置当前状态为完成
		fileUploadStats.setCurrentStatus("done");
	}

	public FileUploadStats getFileUploadStats() {
		// 返回当前状态对象
		return fileUploadStats;
	}

	// 保存状态类
	public static class FileUploadStats {
		private long totalSize = 0;// 总数据的大小
		private long bytesRead = 0;// 已读数据大小
		private long startTime = System.currentTimeMillis();// 开始读取的时间
		private String currentStatus = "none";// 默认的状态

		public long getTotalSize()// 属性totalSize的get方法
		{
			return totalSize - 100;
		}

		public void setTotalSize(long totalSize) {
			this.totalSize = totalSize;
		}

		public long getBytesRead()// 属性bytesRead的get方法
		{
			return bytesRead;
		}

		public long getElapsedTimeInSeconds()// 获得已经上传得时间
		{
			return (System.currentTimeMillis() - startTime) / 1000;
		}

		public String getCurrentStatus()// 属性currentStatus的get方法
		{
			return currentStatus;
		}

		public void setCurrentStatus(String currentStatus) {
			this.currentStatus = currentStatus;
		}

		public void setBytesRead(long bytesRead) {
			this.bytesRead = bytesRead;
		}

		public void incrementBytesRead(int byteCount) {
			this.bytesRead += byteCount;
		}
	}
}
