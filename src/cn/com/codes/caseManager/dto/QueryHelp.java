package cn.com.codes.caseManager.dto;

import java.util.Date;

public class QueryHelp {

	private Long caseTypeId;
	private Long priId;
	private Integer testResult;
	private Long exeVerId;
	private Date exeDate;
	private String actorId;
	private String createrId;
	private String auditerId;
	private Integer weight;
	private String testCaseDes;

	public Long getCaseTypeId() {
		return caseTypeId;
	}

	public void setCaseTypeId(Long caseTypeId) {
		this.caseTypeId = caseTypeId;
	}

	public Long getPriId() {
		return priId;
	}

	public void setPriId(Long priId) {
		this.priId = priId;
	}

	public String getTestCaseDes() {
		return testCaseDes;
	}

	public void setTestCaseDes(String testCaseDes) {
		this.testCaseDes = testCaseDes;
	}

	public Integer getTestResult() {
		return testResult;
	}

	public void setTestResult(Integer testResult) {
		this.testResult = testResult;
	}


	public Long getExeVerId() {
		return exeVerId;
	}

	public void setExeVerId(Long exeVerId) {
		this.exeVerId = exeVerId;
	}

	public Date getExeDate() {
		return exeDate;
	}

	public void setExeDate(Date exeDate) {
		this.exeDate = exeDate;
	}

	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public String getCreaterId() {
		return createrId;
	}

	public void setCreaterId(String createrId) {
		this.createrId = createrId;
	}

	public String getAuditerId() {
		return auditerId;
	}

	public void setAuditerId(String auditerId) {
		this.auditerId = auditerId;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	

}
