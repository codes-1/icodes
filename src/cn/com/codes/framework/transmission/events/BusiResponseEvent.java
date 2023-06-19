package cn.com.codes.framework.transmission.events;

import java.util.Map;

import cn.com.codes.framework.transmission.events.ResponseEvent;

public class BusiResponseEvent extends ResponseEvent {
	
	/**
	 * 执行的任务ID 暂时不用这个属性，将来在BLH中用它来判断业务流程
	 */
	private String TaskID = null;

	public BusiResponseEvent(String userID) {
		super(userID);
	}

	public BusiResponseEvent() {
		super(null);
		userID = "";
	}

	/**
	 *  下面这几个保留起来，暂时不用
	 */
	private String viewName = null;

	private String msg = null;

	private boolean errFlag = false;

	private String objID;

	

	private Map displayData = null;

	public void setErrFlag(boolean errFlag) {
		this.errFlag = errFlag;
	}

	public boolean isErrFlag() {
		return errFlag;
	}


	public String getMsg() {
		return msg;
	}

	public String getObjID() {
		return objID;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public void setObjID(String objID) {
		this.objID = objID;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}



	public Map getDisplayData() {
		return displayData;
	}

	public void setDisplayData(Map map) {
		displayData = map;
	}

	public void setTaskID(String taskID) {
		this.TaskID = taskID;
	}

	public String getTaskID() {
		return this.TaskID;
	}

	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("ResponseEvent = {\r\n").append("\tsessionID = ");
		result.append(userID).append("\r\n");
		result.append("\treponseCode = ").append(reponseCode).append("\r\n");
		result.append("\r\n").append("}\r\n");
		return result.toString();

	}

}