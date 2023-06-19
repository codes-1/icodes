package cn.com.codes.bugManager.dto;

public class BugAddinitDto {

	public String testSelStr;
	public String devStr;
	public String assignSelStr;
	public String analySelStr;
	public String interCesSelStr;
	public String verSelStr;
	public String querySelStr;
	public String typeSelStr;
	public String gradeSelStr;
	public String freqSelStr;
	public String occaSelStr;
	public String geCaseSelStr;
	public String sourceSelStr;
	public String plantFormSelStr;
	public String genePhaseSelStr;
	public String priSelStr;

	public int currFlowCd;
	public int nextFlowCd;

	public int testPhase;
	public int currStateId;
	public String stateName;

	public String roleInTask;
	public String testFlow;
	public int relCaseSwitch;

	public String getTestSelStr() {
		return testSelStr;
	}

	public void setTestSelStr(String testSelStr) {
		this.testSelStr = testSelStr;
	}

	public String getDevStr() {
		return devStr;
	}

	public void setDevStr(String devStr) {
		this.devStr = devStr;
	}

	public String getAssignSelStr() {
		return assignSelStr;
	}

	public void setAssignSelStr(String assignSelStr) {
		this.assignSelStr = assignSelStr;
	}

	public String getAnalySelStr() {
		return analySelStr;
	}

	public void setAnalySelStr(String analySelStr) {
		this.analySelStr = analySelStr;
	}

	public String getInterCesSelStr() {
		return interCesSelStr;
	}

	public void setInterCesSelStr(String interCesSelStr) {
		this.interCesSelStr = interCesSelStr;
	}

	public String getQuerySelStr() {
		return querySelStr;
	}

	public void setQuerySelStr(String querySelStr) {
		this.querySelStr = querySelStr;
	}

	public String getTypeSelStr() {
		return typeSelStr;
	}

	public void setTypeSelStr(String typeSelStr) {
		this.typeSelStr = typeSelStr;
	}

	public String getGradeSelStr() {
		return gradeSelStr;
	}

	public void setGradeSelStr(String gradeSelStr) {
		this.gradeSelStr = gradeSelStr;
	}

	public String getFreqSelStr() {
		return freqSelStr;
	}

	public void setFreqSelStr(String freqSelStr) {
		this.freqSelStr = freqSelStr;
	}

	public String getOccaSelStr() {
		return occaSelStr;
	}

	public void setOccaSelStr(String occaSelStr) {
		this.occaSelStr = occaSelStr;
	}

	public String getGeCaseSelStr() {
		return geCaseSelStr;
	}

	public void setGeCaseSelStr(String geCaseSelStr) {
		this.geCaseSelStr = geCaseSelStr;
	}

	public String getSourceSelStr() {
		return sourceSelStr;
	}

	public void setSourceSelStr(String sourceSelStr) {
		this.sourceSelStr = sourceSelStr;
	}

	public String getPlantFormSelStr() {
		return plantFormSelStr;
	}

	public void setPlantFormSelStr(String plantFormSelStr) {
		this.plantFormSelStr = plantFormSelStr;
	}

	public String getGenePhaseSelStr() {
		return genePhaseSelStr;
	}

	public void setGenePhaseSelStr(String genePhaseSelStr) {
		this.genePhaseSelStr = genePhaseSelStr;
	}

	public String getPriSelStr() {
		return priSelStr;
	}

	public void setPriSelStr(String priSelStr) {
		this.priSelStr = priSelStr;
	}

	public String getRoleInTask() {
		return roleInTask;
	}

	public void setRoleInTask(String roleInTask) {
		this.roleInTask = roleInTask;
	}

	public String getTestFlow() {
		return testFlow;
	}

	public void setTestFlow(String testFlow) {
		this.testFlow = testFlow;
	}

	public int getRelCaseSwitch() {
		return relCaseSwitch;
	}

	public void setRelCaseSwitch(int relCaseSwitch) {
		this.relCaseSwitch = relCaseSwitch;
	}

	public String getVerSelStr() {
		if (verSelStr == null) {
			return "";
		}
		return verSelStr;
	}

	public void setVerSelStr(String verSelStr) {
		this.verSelStr = verSelStr;
	}

}
