package cn.com.codes.framework.exception;

import cn.com.codes.framework.common.PropertyReader;


public class BaseException extends Exception {
	// 错误信息ID
	private final String messageID;

	// 参数列表
	private String[] argumentValues;

	// 原始异常
	private Exception linkedException;

	// 错误信息
	private String message;

	// 跳转页面
	private String dispathPage;

	/**
	 * 用指定MessageID构造新异常
	 * 
	 * @param messageID
	 *            指定的错误信息ID
	 */
	public BaseException(String messageID) {
		this.messageID = messageID;
	}

	public BaseException(String message, boolean simple) {

		this.message = message;
		this.messageID = null;

	}

	public BaseException(String message,Exception exception, boolean simple) {

		this.message = message;
		this.linkedException = exception;
		this.messageID = null;

	}
	
	
	/**
	 * 用指定MessageID和原始异常构造新异常
	 * 
	 * @param messageID
	 *            指定的错误信息ID
	 * @param exception
	 *            原始异常
	 */
	public BaseException(String messageID, Exception exception) {
		this.messageID = messageID;
		this.linkedException = exception;
	}

	/**
	 * 用指定MessageID、原始异常、跳转页面和表现形式构造新异常
	 * 
	 * @param messageID
	 *            指定的错误信息ID
	 * @param exception
	 *            原始异常
	 * @param dispathPage
	 *            跳转页面
	 * @param occasion
	 *            表现形式
	 */
	public BaseException(String messageID, Exception exception,
			String dispathPage) {
		this.messageID = messageID;
		this.linkedException = exception;
		this.dispathPage = dispathPage; 
	}

	/**
	 * 用指定MessageID和参数列表构造新异常
	 * 
	 * @param messageID
	 *            指定的错误信息ID
	 * @param argumentValue
	 *            指定错误信息的参数
	 * @histroy 2003-09-11 new ZhangLiang
	 */
	public BaseException(String messageID, String argumentValue) {
		this.messageID = messageID;
		this.setArgumentValues(argumentValue);
	}

	/**
	 * 用指定MessageID、参数列表和原始异常构造新异常
	 * 
	 * @param messageID
	 *            指定的错误信息ID
	 * @param argumentValues
	 *            指定错误信息的参数
	 * @param exception
	 *            原始异常
	 * @histroy 2003-09-11 new ZhangLiang
	 */
	public BaseException(String messageID, String argumentValue,
			Exception exception) {
		this.messageID = messageID;
		this.setArgumentValues(argumentValue);
		this.linkedException = exception;
	}

	/**
	 * 用指定MessageID和参数列表构造新异常
	 * 
	 * @param messageID
	 *            指定的错误信息ID
	 * @param argumentValues
	 *            指定错误信息的参数列表
	 */
	public BaseException(String messageID, String[] argumentValues) {
		this.messageID = messageID;
		this.argumentValues = argumentValues;
	}

	/**
	 * 用指定MessageID、参数列表和原始异常构造新异常
	 * 
	 * @param messageID
	 *            指定的错误信息ID
	 * @param argumentValues
	 *            指定错误信息的参数列表
	 * @param exception
	 *            原始异常
	 */
	public BaseException(String messageID, String[] argumentValues,
			Exception exception) {
		this.messageID = messageID;
		this.argumentValues = argumentValues;
		this.linkedException = exception;
	}

	/**
	 * 获得原始的异常
	 * 
	 * @return 原始异常
	 */
	public Exception getLinkedException() {
		return linkedException;
	}

	/**
	 * 设定原始异常
	 * 
	 * @param exception
	 *            原始异常
	 */
	public void setLinkedException(Exception exception) {
		linkedException = exception;
	}

	/**
	 * 获得错误信息ID
	 * 
	 * @return 错误信息ID
	 */
	public String getMessageID() {
		return messageID;
	}

	/**
	 * 获得错误信息
	 * 
	 * @return 错误信息
	 */
	public String getMessage() {
		if (message == null && messageID != null) {
			message = "错误代码:" + messageID + "\n"
					+ PropertyReader.getInstance().getProperty(messageID);
		}

		return message;
	}

	/**
	 * 设置单个参数
	 * 
	 * @param argumentValue
	 *            参数值
	 */
	public void setArgumentValues(String argumentValue) {
		argumentValues = new String[1];
		argumentValues[0] = argumentValue;
	}

	/**
	 * 设置参数列表
	 * 
	 * @param argumentValues
	 *            参数列表
	 */
	public void setArgumentValues(String[] argumentValues) {
		this.argumentValues = argumentValues;
	}

	/**
	 * 设置跳转页面
	 * 
	 * @param dispatchPage
	 *            跳转页面
	 */
	public void setDispatchPage(String dispatchPage) {
		this.dispathPage = dispathPage;
	}

	/**
	 * 获得跳转页面
	 * 
	 * @return 跳转页面
	 */
	public String getDispatchPage() {
		return this.dispathPage;
	}

	public String toString() {
		String excepInfo = getClass().getName() + ":" + messageID + ":"
				+ message + ": ";
		if (linkedException != null) {
			excepInfo += linkedException.getMessage();

		}
		return excepInfo;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}