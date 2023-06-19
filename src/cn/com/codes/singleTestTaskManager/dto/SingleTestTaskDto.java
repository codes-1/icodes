package cn.com.codes.singleTestTaskManager.dto;

import cn.com.codes.framework.transmission.dto.BaseDto;
import cn.com.codes.object.SingleTestTask;

public class SingleTestTaskDto extends BaseDto {

	private SingleTestTask singleTest ;
	private String operCmd ="";
	private String taskIdField;
	private String taskNameField;
	private String repTemplet;//报表模板名称
	private String dateChk ;
	
	public SingleTestTask getSingleTest() {
		return singleTest;
	}

	public void setSingleTest(SingleTestTask singleTest) {
		this.singleTest = singleTest;
	}

	public String getOperCmd() {
		return operCmd;
	}

	public void setOperCmd(String operCmd) {
		this.operCmd = operCmd;
	}

	public String getTaskIdField() {
		return taskIdField;
	}

	public void setTaskIdField(String taskIdField) {
		this.taskIdField = taskIdField;
	}

	public String getTaskNameField() {
		return taskNameField;
	}

	public void setTaskNameField(String taskNameField) {
		this.taskNameField = taskNameField;
	}

	public String getRepTemplet() {
		return repTemplet;
	}

	public void setRepTemplet(String repTemplet) {
		this.repTemplet = repTemplet;
	}

	public String getDateChk() {
		return dateChk;
	}

	public void setDateChk(String dateChk) {
		this.dateChk = dateChk;
	}


}
