package cn.com.codes.object;

import cn.com.codes.object.ModuleaVersionId;

public class ModuleaVersionId implements java.io.Serializable {

	

	private String taskId;
	private Integer testPhase;
	private String moduleId;

	
	public ModuleaVersionId() {
	}

	
	public ModuleaVersionId(String taskId, Integer testPhase, String moduleId) {
		this.taskId = taskId;
		this.testPhase = testPhase;
		this.moduleId = moduleId;
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
		if (!(other instanceof ModuleaVersionId))
			return false;
		ModuleaVersionId castOther = (ModuleaVersionId) other;

		return ((this.getTaskId() == castOther.getTaskId()) || (this
				.getTaskId() != null
				&& castOther.getTaskId() != null && this.getTaskId().equals(
				castOther.getTaskId())))
				&& ((this.getTestPhase() == castOther.getTestPhase()) || (this
						.getTestPhase() != null
						&& castOther.getTestPhase() != null && this
						.getTestPhase().equals(castOther.getTestPhase())))
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
				+ (getTestPhase() == null ? 0 : this.getTestPhase().hashCode());
		result = 37 * result
				+ (getModuleId() == null ? 0 : this.getModuleId().hashCode());
		return result;
	}
}