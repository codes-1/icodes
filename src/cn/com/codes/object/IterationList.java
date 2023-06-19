package cn.com.codes.object;

import java.util.Date;
import java.util.Set;

import cn.com.codes.framework.transmission.JsonInterface;


public class IterationList implements JsonInterface {

	private static final long serialVersionUID = 1L;
	
	private String iterationId;
	private String iterationBagName;
	private String createPerson;
	private String userId;
	private Date createTime;
	private Date updateTime;
	private String note;
	private String associationProject;
	private String taskId;
	private String status;
	private Date startTime;
	private Date endTime;
	
	public IterationList(){
		
	}
	
	public IterationList(String iterationId, String iterationBagName,
			String createPerson, String userId, Date createTime,
			Date updateTime, String note,String status) {
		this.iterationId = iterationId;
		this.iterationBagName = iterationBagName;
		this.createPerson = createPerson;
		this.userId = userId;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.note = note;
		this.status = status;
	}

	public String getAssociationProject() {
		return associationProject;
	}

	public void setAssociationProject(String associationProject) {
		this.associationProject = associationProject;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getIterationId() {
		return iterationId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setIterationId(String iterationId) {
		this.iterationId = iterationId;
	}

	public String getIterationBagName() {
		return iterationBagName;
	}

	public void setIterationBagName(String iterationBagName) {
		this.iterationBagName = iterationBagName;
	}

	public String getCreatePerson() {
		return createPerson;
	}
	
	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	@Override
	public String toStrUpdateInit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toStrList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toStrUpdateRest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void toString(StringBuffer bf) {
		// TODO Auto-generated method stub
	}

	/**  
	* @return status 
	*/
	public String getStatus() {
		return status;
	}

	/**  
	* @param status status 
	*/
	public void setStatus(String status) {
		this.status = status;
	}

	/**  
	* @return startTime 
	*/
	public Date getStartTime() {
		return startTime;
	}

	/**  
	* @param startTime startTime 
	*/
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**  
	* @return endTime 
	*/
	public Date getEndTime() {
		return endTime;
	}

	/**  
	* @param endTime endTime 
	*/
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
