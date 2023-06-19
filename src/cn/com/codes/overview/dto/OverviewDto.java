package cn.com.codes.overview.dto;


import cn.com.codes.framework.transmission.dto.BaseDto;

public class OverviewDto extends BaseDto {

	/**  
	* 字段:      字段名称
	* @Fields serialVersionUID : TODO 
	*/
	private static final long serialVersionUID = 1L;
	//项目id
	private String taskId;
	//负责人id
	private String chargePersonId;
	//参与人id
	private String joinId;
	//创建人id
	private String createId;
	//迭代id
	private String iterationId;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getChargePersonId() {
		return chargePersonId;
	}

	public void setChargePersonId(String chargePersonId) {
		this.chargePersonId = chargePersonId;
	}

	public String getJoinId() {
		return joinId;
	}

	public void setJoinId(String joinId) {
		this.joinId = joinId;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getIterationId() {
		return iterationId;
	}

	public void setIterationId(String iterationId) {
		this.iterationId = iterationId;
	}
	
}
