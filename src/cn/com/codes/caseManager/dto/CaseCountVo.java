package cn.com.codes.caseManager.dto;


public class CaseCountVo {
	private Long moduleId;
	private Integer caseCount;
	private Integer scrpCount;
	
	public Long getModuleId() {
		return moduleId;
	}
	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}
	public Integer getCaseCount() {
		return caseCount;
	}
	public void setCaseCount(Integer caseCount) {
		this.caseCount = caseCount;
	}
	public Integer getScrpCount() {
		return scrpCount;
	}
	public void setScrpCount(Integer scrpCount) {
		this.scrpCount = scrpCount;
	}
	
}
