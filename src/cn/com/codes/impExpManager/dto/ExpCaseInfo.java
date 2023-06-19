package cn.com.codes.impExpManager.dto;

import java.util.Date;

public class ExpCaseInfo {

	private String testCaseId;
	private String superMname;
	private String moduleName;
	private String testCaseDes;
	private String testCaseOperData;
	private String expResult;
	private int status;
	private String author;
	private String exeName;
	private String typeName;
	private String priName;
	private Date exeDate;

	public String getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(String testCaseId) {
		this.testCaseId = testCaseId;
	}

	public String getSuperMname() {
		return superMname;
	}

	public void setSuperMname(String superMname) {
		this.superMname = superMname;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getTestCaseDes() {
		return testCaseDes;
	}

	public void setTestCaseDes(String testCaseDes) {
		this.testCaseDes = testCaseDes;
	}

	public String getTestCaseOperData() {
		return testCaseOperData;
	}

	public void setTestCaseOperData(String testCaseOperData) {
		this.testCaseOperData = testCaseOperData;
	}

	public String getExpResult() {
		return expResult;
	}

	public void setExpResult(String expResult) {
		this.expResult = expResult;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getExeName() {
		return exeName;
	}

	public void setExeName(String exeName) {
		this.exeName = exeName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getPriName() {
		return priName;
	}

	public void setPriName(String priName) {
		this.priName = priName;
	}

	public Date getExeDate() {
		return exeDate;
	}

	public void setExeDate(Date exeDate) {
		this.exeDate = exeDate;
	}
}
