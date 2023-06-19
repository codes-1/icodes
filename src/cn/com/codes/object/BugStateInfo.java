package cn.com.codes.object;



public class BugStateInfo implements java.io.Serializable {

	

	private Integer stateId;
	private String stateName;

	public BugStateInfo() {
	}

	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}




}