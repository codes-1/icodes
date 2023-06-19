package cn.com.codes.object;

import java.util.Date;

import cn.com.codes.object.ModuleVersion;



public class ModuleVersion implements java.io.Serializable {

	private Long ModuleVerId;
	private String version;
	private Date insdate;
	private Date upddate;
	private Integer seq;
	private String taskId;
	private Integer testPhase;
	private String moduleId;

	public ModuleVersion() {
	}

	public ModuleVersion(Long ModuleVerId) {
		this.ModuleVerId = ModuleVerId;
	}

	public Long getModuleVerId() {
		return this.ModuleVerId;
	}

	public void setModuleVerId(Long ModuleVerId) {
		this.ModuleVerId = ModuleVerId;
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Integer getTestPhase() {
		return testPhase;
	}

	public void setTestPhase(Integer testPhase) {
		this.testPhase = testPhase;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ModuleVersion))
			return false;
		ModuleVersion castOther = (ModuleVersion) other;

		return ((this.getTaskId() == castOther.getTaskId()) || (this
				.getTaskId() != null
				&& castOther.getTaskId() != null && this.getTaskId().equals(
				castOther.getTaskId())))
				&& ((this.getModuleId() == castOther.getModuleId()) || (this
						.getModuleId() != null
						&& castOther.getModuleId() != null && this
						.getModuleId().equals(castOther.getModuleId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getTaskId() == null ? 0 : this.getTaskId().hashCode());
		result = 37 * result
				+ (getModuleId() == null ? 0 : this.getModuleId().hashCode());
		return result;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}
}