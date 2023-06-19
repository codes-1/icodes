package cn.com.codes.object;

import java.util.Date;

import cn.com.codes.framework.transmission.JsonInterface;

public class OtherMission implements JsonInterface {

	/**  
	* 字段:      字段名称
	* @Fields serialVersionUID : TODO 
	*/
	private static final long serialVersionUID = 1L;
	private String missionId;
	private String missionName;
	private Long missionCategory;
	private String missionType;
	private String projectId;
	private String projectType;
	private String chargePersonId;
	private Long emergencyDegree;
	private Long difficultyDegree;
	private Date predictStartTime;
	private Date predictEndTime;
	private String standardWorkload;
	private String actualWorkload;
	private String description;
	private String completionDegree;
	private Date createTime;
	private Date updateTime;
	private String createUserId;
	private String status;
	private String stopReason;
	private String missionNum;
   
	public OtherMission() {
	}
	
	
	


	public String getMissionId() {
		return missionId;
	}





	public void setMissionId(String missionId) {
		this.missionId = missionId;
	}





	public String getMissionName() {
		return missionName;
	}





	public void setMissionName(String missionName) {
		this.missionName = missionName;
	}





	public Long getMissionCategory() {
		return missionCategory;
	}





	public void setMissionCategory(Long missionCategory) {
		this.missionCategory = missionCategory;
	}





	public String getMissionType() {
		return missionType;
	}





	public void setMissionType(String missionType) {
		this.missionType = missionType;
	}





	public String getProjectId() {
		return projectId;
	}





	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}





	public String getChargePersonId() {
		return chargePersonId;
	}





	public void setChargePersonId(String chargePersonId) {
		this.chargePersonId = chargePersonId;
	}





	public Long getEmergencyDegree() {
		return emergencyDegree;
	}





	public void setEmergencyDegree(Long emergencyDegree) {
		this.emergencyDegree = emergencyDegree;
	}





	public Long getDifficultyDegree() {
		return difficultyDegree;
	}





	public void setDifficultyDegree(Long difficultyDegree) {
		this.difficultyDegree = difficultyDegree;
	}





	public Date getPredictStartTime() {
		return predictStartTime;
	}





	public void setPredictStartTime(Date predictStartTime) {
		this.predictStartTime = predictStartTime;
	}





	public Date getPredictEndTime() {
		return predictEndTime;
	}





	public void setPredictEndTime(Date predictEndTime) {
		this.predictEndTime = predictEndTime;
	}





	public String getStandardWorkload() {
		return standardWorkload;
	}





	public void setStandardWorkload(String standardWorkload) {
		this.standardWorkload = standardWorkload;
	}





	public String getActualWorkload() {
		return actualWorkload;
	}





	public void setActualWorkload(String actualWorkload) {
		this.actualWorkload = actualWorkload;
	}





	public String getDescription() {
		return description;
	}





	public void setDescription(String description) {
		this.description = description;
	}





	public String getCompletionDegree() {
		return completionDegree;
	}





	public void setCompletionDegree(String completionDegree) {
		this.completionDegree = completionDegree;
	}





	public String getProjectType() {
		return projectType;
	}





	public void setProjectType(String projectType) {
		this.projectType = projectType;
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





	public String getCreateUserId() {
		return createUserId;
	}





	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}





	public String getStatus() {
		return status;
	}





	public void setStatus(String status) {
		this.status = status;
	}





	public String getStopReason() {
		return stopReason;
	}





	public void setStopReason(String stopReason) {
		this.stopReason = stopReason;
	}





	public String getMissionNum() {
		return missionNum;
	}





	public void setMissionNum(String missionNum) {
		this.missionNum = missionNum;
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