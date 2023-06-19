package cn.com.codes.iteration.dto;

import java.util.Date;

import cn.com.codes.framework.transmission.JsonInterface;


public class IterationVo implements JsonInterface {

	private static final long serialVersionUID = 7666219762321844175L;
	
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
	

	/**  
	 * @return iterationId 
	 */
	public String getIterationId() {
		return iterationId;
	}


	/**  
	 * @param iterationId iterationId 
	 */
	public void setIterationId(String iterationId) {
		this.iterationId = iterationId;
	}


	/**  
	 * @return iterationBagName 
	 */
	public String getIterationBagName() {
		return iterationBagName;
	}


	/**  
	 * @param iterationBagName iterationBagName 
	 */
	public void setIterationBagName(String iterationBagName) {
		this.iterationBagName = iterationBagName;
	}


	/**  
	 * @return createPerson 
	 */
	public String getCreatePerson() {
		return createPerson;
	}


	/**  
	 * @param createPerson createPerson 
	 */
	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}


	/**  
	 * @return userId 
	 */
	public String getUserId() {
		return userId;
	}


	/**  
	 * @param userId userId 
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}


	/**  
	 * @return createTime 
	 */
	public Date getCreateTime() {
		return createTime;
	}


	/**  
	 * @param createTime createTime 
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	/**  
	 * @return updateTime 
	 */
	public Date getUpdateTime() {
		return updateTime;
	}


	/**  
	 * @param updateTime updateTime 
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	/**  
	 * @return note 
	 */
	public String getNote() {
		return note;
	}


	/**  
	 * @param note note 
	 */
	public void setNote(String note) {
		this.note = note;
	}


	/**  
	 * @return associationProject 
	 */
	public String getAssociationProject() {
		return associationProject;
	}


	/**  
	 * @param associationProject associationProject 
	 */
	public void setAssociationProject(String associationProject) {
		this.associationProject = associationProject;
	}


	/**  
	 * @return taskId 
	 */
	public String getTaskId() {
		return taskId;
	}


	/**  
	 * @param taskId taskId 
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
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
	* <p>Title: </p> 
	* <p>Description: </p>  
	*/
	public IterationVo() {
		super();
		// TODO Auto-generated constructor stub
	}


	/**  
	* <p>Title: </p> 
	* <p>Description: </p> 
	* @param iterationId
	* @param iterationBagName
	* @param createPerson
	* @param userId
	* @param createTime
	* @param updateTime
	* @param note
	* @param associationProject
	* @param taskId
	* @param status 
	*/
	public IterationVo(String iterationId, String iterationBagName,
			String createPerson, String userId, Date createTime,
			Date updateTime, String note, String associationProject,
			String taskId, String status) {
		super();
		this.iterationId = iterationId;
		this.iterationBagName = iterationBagName;
		this.createPerson = createPerson;
		this.userId = userId;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.note = note;
		this.associationProject = associationProject;
		this.taskId = taskId;
		this.status = status;
	}


	/* (非 Javadoc)   
	* <p>Title: toString</p>   
	* <p>Description: </p>   
	* @return   
	* @see java.lang.Object#toString()   
	*/
	@Override
	public String toString() {
		return "IterationVo [iterationId=" + iterationId
				+ ", iterationBagName=" + iterationBagName + ", createPerson="
				+ createPerson + ", userId=" + userId + ", createTime="
				+ createTime + ", updateTime=" + updateTime + ", note=" + note
				+ ", associationProject=" + associationProject + ", taskId="
				+ taskId + ", status=" + status + "]";
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

}
