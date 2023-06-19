package cn.com.codes.object;

import java.util.Date;

import cn.com.codes.object.OutlineInfo;
import cn.com.codes.object.OutlineTeamMember;
import cn.com.codes.object.User;



public class OutlineTeamMember implements java.io.Serializable {

	private String moduleMemberId ;
	private String taskId;
	private Long testPhase;
	private Date insdate;
	private Date upddate;
	private String companyId;
	
	private Long moduleId;
	private String userId;
	private Integer userRole;
	private String userName;
	private User user ;
	private OutlineInfo outline;
	public OutlineTeamMember() {
	}
	public OutlineTeamMember(String moduleMemberId,Long moduleId) {
		this.moduleMemberId = moduleMemberId;
		this.moduleId = moduleId;
	}
	public OutlineTeamMember(String moduleMemberId) {
		this.moduleMemberId = moduleMemberId;
	}
	public OutlineTeamMember(String moduleMemberId,Long moduleId,String userId,Integer userRole) {
		this.moduleMemberId = moduleMemberId;
		this.moduleId = moduleId;
		this.userId = userId;
		this.userRole = userRole;
	}

	public OutlineTeamMember(Long moduleId,String userId,Integer userRole) {
		this.moduleId = moduleId;
		this.userId = userId;
		this.userRole = userRole;
	}
	
	public OutlineTeamMember(String userId,Integer userRole) {
		this.userId = userId;
		this.userRole = userRole;
	}
	public String getModuleMemberId() {
		return this.moduleMemberId;
	}

	public void setModuleMemberId(String moduleMemberId) {
		this.moduleMemberId = moduleMemberId;
	}


	public Long getTestPhase() {
		return this.testPhase;
	}

	public void setTestPhase(Long testPhase) {
		this.testPhase = testPhase;
	}

	public Date getInsdate() {
		return this.insdate;
	}

	public void setInsdate(Date insdate) {
		this.insdate = insdate;
	}

	public Date getUpddate() {
		return this.upddate;
	}

	public void setUpddate(Date upddate) {
		this.upddate = upddate;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getUserRole() {
		return userRole;
	}

	public void setUserRole(Integer userRole) {
		this.userRole = userRole;
	}
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof OutlineTeamMember))
			return false;
		OutlineTeamMember castOther = (OutlineTeamMember) other;

		return ((this.getModuleId() == castOther.getModuleId()) || (this
				.getModuleId() != null
				&& castOther.getModuleId() != null && this.getModuleId()
				.equals(castOther.getModuleId())))
				&& ((this.getUserId() == castOther.getUserId()) || (this
						.getUserId() != null
						&& castOther.getUserId() != null && this.getUserId()
						.equals(castOther.getUserId())))
				&& ((this.getUserRole() == castOther.getUserRole()) || (this
						.getUserRole() != null
						&& castOther.getUserRole() != null && this
						.getUserRole().equals(castOther.getUserRole())));
	}


	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getModuleId() == null ? 0 : this.getModuleId().hashCode());
		result = 37 * result
				+ (getUserId() == null ? 0 : this.getUserId().hashCode());
		result = 37 * result
				+ (getUserRole() == null ? 0 : this.getUserRole().hashCode());
		return result;
	}

	public OutlineInfo getOutline() {
		return outline;
	}

	public void setOutline(OutlineInfo outline) {
		this.outline = outline;
	}

}