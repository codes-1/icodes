package cn.com.codes.object;

import java.io.Serializable;
import java.util.Date;

import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.transmission.JsonInterface;
import cn.com.codes.object.TaskUseActor;
import cn.com.codes.object.User;

public class TaskUseActor implements JsonInterface, Serializable {

	private Date insdate;
	private Date upddate;
	private String actorId;
	private String taskId;
	private String userId;
	private Integer actor;
	private Integer isEnable;
	private User user;

	public TaskUseActor() {
	}
	public TaskUseActor(String userId,Integer actor) {
		this.userId = userId;
		this.actor = actor;
	}
	public TaskUseActor(Integer actor) {
		this.actor = actor;
	}

	public TaskUseActor(String taskId,String userId,Integer actor) {
		this.taskId= taskId;
		this.userId = userId;
		this.actor = actor;
	}
	public TaskUseActor(String userId,Integer actor,String actorId) {
		this.actorId= actorId;
		this.userId = userId;
		this.actor = actor;
	}	
	public TaskUseActor(String actorId) {
		this.actorId = actorId;
	}

	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public Date getInsdate() {
		return insdate;
	}

	public void setInsdate(Date insdate) {
		this.insdate = insdate;
	}

	public Date getUpddate() {
		return upddate;
	}

	public void setUpddate(Date upddate) {
		this.upddate = upddate;
	}

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskid) {
		this.taskId = taskid;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userid) {
		this.userId = userid;
	}

	public Integer getActor() {
		return this.actor;
	}

	public void setActor(Integer actor) {
		this.actor = actor;
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
		if (!(other instanceof TaskUseActor))
			return false;
		TaskUseActor castOther = (TaskUseActor) other;

		return ((this.getTaskId() == castOther.getTaskId()) || (this
				.getTaskId() != null
				&& castOther.getTaskId() != null && this.getTaskId().equals(
				castOther.getTaskId())))
				&& ((this.getUserId() == castOther.getUserId()) || (this
						.getUserId() != null
						&& castOther.getUserId() != null && this.getUserId()
						.equals(castOther.getUserId())))
				&& ((this.getActor() == castOther.getActor()) || (this
						.getActor() != null
						&& castOther.getActor() != null && this.getActor()
						.equals(castOther.getActor())))
				&& ((this.getIsEnable() == castOther.getIsEnable()) || (this
						.getIsEnable() != null
						&& castOther.getIsEnable() != null && this.getIsEnable()
						.equals(castOther.getIsEnable())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getTaskId() == null ? 0 : this.getTaskId().hashCode());
		result = 37 * result
				+ (getUserId() == null ? 0 : this.getUserId().hashCode());
		result = 37 * result
				+ (getActor() == null ? 0 : this.getActor().hashCode());
		result = 37 * result
		+ (this.getIsEnable() == null ? 0 : getIsEnable().hashCode());
		//System.out.println("hashCode=="+result);
		return result;
	}

	public String toStrList() {
		StringBuffer sbf = new StringBuffer();
		sbf.append("{");
		sbf.append("id:'");
		sbf.append(getActorId().toString());
		sbf.append("',data: [0,'','");
		sbf.append(insdate == null ? "" : StringUtils.formatShortDate(insdate));
		sbf.append("'");
		sbf.append(upddate == null ? "" : StringUtils.formatShortDate(upddate));
		sbf.append("'");
		sbf.append("]");
		sbf.append("}");
		return sbf.toString();
	}

	public String toStrUpdateInit() {
		StringBuffer sbf = new StringBuffer();
		sbf.append(getActorId().toString());
		sbf.append("^");
		sbf.append("insdate=").append(
				insdate == null ? "" : StringUtils.formatShortDate(insdate));
		sbf.append("^");
		sbf.append("upddate=").append(
				upddate == null ? "" : StringUtils.formatShortDate(upddate));
		return sbf.toString();
	}

	public String toStrUpdateRest() {
		StringBuffer sbf = new StringBuffer();
		sbf.append(getActorId().toString());
		sbf.append("0,,");
		sbf.append(insdate == null ? "" : StringUtils.formatShortDate(insdate));
		sbf.append("'");
		sbf.append(upddate == null ? "" : StringUtils.formatShortDate(upddate));
		return sbf.toString();
	}

	public void toString(StringBuffer bf) {

	}
	public Integer getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

}