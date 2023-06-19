package cn.com.codes.otherMission.dto;


import cn.com.codes.framework.transmission.dto.BaseDto;
import cn.com.codes.object.OtherMission;
import cn.com.codes.object.Project;

public class OtherMissionDto extends BaseDto {

	/**  
	* 字段:      字段名称
	* @Fields serialVersionUID : TODO 
	*/
	private static final long serialVersionUID = 1L;
	//存放其他任务
	private OtherMission otherMission;
	//存放传来的任务参与者多个userIds
	private String userIds;
	//存放项目信息
	private Project project;
	//存放其他任务
	//private UserOtherMission userOtherMission;
	//存放其他任务分配的人员
	//private List<UserOtherMission> userOtherMissions;
	//存放传过来的任务参与者userId
	private String userId;
	//存放传来的任务关注者多个用户id
	private String concernIds;
	//存放传来的任务关注者的用户id
	private String concernId;
	//存放需要关注的任务的missionIds
	private String missionIds;
	//存放关注者的id;
	private String uId;
	//过滤迭代已关联的任务，不重复关联
	private String relaMissionId;
	
	private String projectId;
	
	private String related;
	
	private String usersId;
	private String usersName;
	//存放人员分组
	private String groupId;
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public OtherMission getOtherMission() {
		return otherMission;
	}

	public void setOtherMission(OtherMission otherMission) {
		this.otherMission = otherMission;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getConcernIds() {
		return concernIds;
	}

	public void setConcernIds(String concernIds) {
		this.concernIds = concernIds;
	}

	public String getConcernId() {
		return concernId;
	}

	public void setConcernId(String concernId) {
		this.concernId = concernId;
	}

	public String getMissionIds() {
		return missionIds;
	}

	public void setMissionIds(String missionIds) {
		this.missionIds = missionIds;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**  
	* @return relaMissionId 
	*/
	public String getRelaMissionId() {
		return relaMissionId;
	}

	/**  
	* @param relaMissionId relaMissionId 
	*/
	public void setRelaMissionId(String relaMissionId) {
		this.relaMissionId = relaMissionId;
	}

	public String getRelated() {
		return related;
	}

	public void setRelated(String related) {
		this.related = related;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getUsersId() {
		return usersId;
	}

	public void setUsersId(String usersId) {
		this.usersId = usersId;
	}

	public String getUsersName() {
		return usersName;
	}

	public void setUsersName(String usersName) {
		this.usersName = usersName;
	}

//	public List<UserOtherMission> getUserOtherMissions() {
//		return userOtherMissions;
//	}
//
//	public void setUserOtherMissions(List<UserOtherMission> userOtherMissions) {
//		this.userOtherMissions = userOtherMissions;
//	}
//
//	public UserOtherMission getUserOtherMission() {
//		return userOtherMission;
//	}
//
//	public void setUserOtherMission(UserOtherMission userOtherMission) {
//		this.userOtherMission = userOtherMission;
//	}
}
