package cn.com.codes.object;

import java.util.Date;
import java.util.Set;

import cn.com.codes.object.BugBaseInfo;
import cn.com.codes.object.CaseExeHistory;
import cn.com.codes.object.SoftwareVersion;
import cn.com.codes.object.TestResult;
import cn.com.codes.object.User;



public class TestResult implements java.io.Serializable {
	
	private Long resultId;
	private String taskId;
	private Integer testPhase;
	private Integer testResult;
	private String testActor;
	private Date exeDate;
	private Date upddate;
	private Long moduleId;
	private String remark;
	private Long testCaseId;
	private Long testVer;
	private SoftwareVersion softVer;
	private User exePersion ;
	private Long plantFormId;
	private cn.com.codes.object.TestCaseInfo testCaseInfo;
	
	public TestResult() {
	}

	public TestResult(Long resultId) {
		this.resultId = resultId;
	}
	public TestResult(Long resultId,Long testCaseId) {
		this.resultId = resultId;
		this.testCaseId = testCaseId;
	}
	public TestResult(Long resultId,Long testVer,Integer testResult) {
		this.resultId = resultId;
		this.testVer = testVer;
		this.testResult = testResult;
	}	

	public TestResult copy(){
		TestResult rest = new TestResult();
		rest.setExeDate(this.getExeDate());
		rest.setTaskId(this.getTaskId());
		rest.setTestActor(this.getTestActor());
		rest.setTestResult(3);
		rest.setModuleId(this.getModuleId());
		rest.setTestCaseId(this.getTestCaseId());
		rest.setRemark(this.getRemark());
		return rest;
	}
	public Long getResultId() {
		return this.resultId;
	}

	public void setResultId(Long resultId) {
		this.resultId = resultId;
	}

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Integer getTestPhase() {
		return this.testPhase;
	}

	public void setTestPhase(Integer testPhase) {
		this.testPhase = testPhase;
	}

	public Integer getTestResult() {
		return this.testResult;
	}

	public void setTestResult(Integer testResult) {
		this.testResult = testResult;
	}

	public String getTestActor() {
		return this.testActor;
	}

	public void setTestActor(String testActor) {
		this.testActor = testActor;
	}

	public Date getUpddate() {
		return this.upddate;
	}

	public void setUpddate(Date upddate) {
		this.upddate = upddate;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public Long getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(Long testCaseId) {
		this.testCaseId = testCaseId;
	}

	public Long getTestVer() {
		return testVer;
	}

	public void setTestVer(Long testVer) {
		this.testVer = testVer;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public User getExePersion() {
		return exePersion;
	}

	public void setExePersion(User exePersion) {
		this.exePersion = exePersion;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TestResult))
			return false;
		TestResult castOther = (TestResult) other;
		return ((this.getTestCaseId() == castOther.getTestCaseId()) || (this
				.getTestCaseId() != null
				&& castOther.getTestCaseId() != null && this.getTestCaseId()
				.equals(castOther.getTestCaseId())))
				&& ((this.getTestVer() == castOther.getTestVer()) || (this
						.getTestVer() != null
						&& castOther.getTestVer() != null && this.getTestVer()
						.equals(castOther.getTestVer())));
	}
	public int hashCode() {
		int result = 17;
		result = 37 * result
				+ (getTestCaseId() == null ? 0 : this.getTestCaseId().hashCode());
		result = 37 * result
				+ (getTestVer() == null ? 0 : this.getTestVer().hashCode());
		return result;
	}

	public Date getExeDate() {
		return exeDate;
	}

	public void setExeDate(Date exeDate) {
		this.exeDate = exeDate;
	}

	public Set<BugBaseInfo> getBugs() {
		return softVer.getBugs();
	}

	public Date getInsdate() {
		return softVer.getInsdate();
	}

	public Integer getSeq() {
		return softVer.getSeq();
	}

	public String getTaskid() {
		return softVer.getTaskid();
	}

	public Long getVersionId() {
		return softVer.getVersionId();
	}

	public String getVersionNum() {
		return softVer.getVersionNum();
	}

	public Integer getVerStatus() {
		return softVer.getVerStatus();
	}

	public void setBugs(Set<BugBaseInfo> bugs) {
		softVer.setBugs(bugs);
	}

	public void setInsdate(Date insdate) {
		softVer.setInsdate(insdate);
	}

	public void setSeq(Integer seq) {
		softVer.setSeq(seq);
	}

	public void setTaskid(String taskid) {
		softVer.setTaskid(taskid);
	}

	public void setVersionId(Long versionId) {
		softVer.setVersionId(versionId);
	}

	public void setVersionNum(String versionNum) {
		softVer.setVersionNum(versionNum);
	}

	public void setVerStatus(Integer verStatus) {
		softVer.setVerStatus(verStatus);
	}

	public String toString() {
		return softVer.toString();
	}

	public void toString(StringBuffer sbf) {
		softVer.toString(sbf);
	}

	public String toStrList() {
		return softVer.toStrList();
	}

	public String toStrUpdateInit() {
		return softVer.toStrUpdateInit();
	}

	public String toStrUpdateRest() {
		return softVer.toStrUpdateRest();
	}

	public SoftwareVersion getSoftVer() {
		return softVer;
	}

	public void setSoftVer(SoftwareVersion softVer) {
		this.softVer = softVer;
	}

	public cn.com.codes.object.TestCaseInfo getTestCaseInfo() {
		return testCaseInfo;
	}

	public void setTestCaseInfo(cn.com.codes.object.TestCaseInfo testCaseInfo) {
		this.testCaseInfo = testCaseInfo;
	}

	public Long getPlantFormId() {
		return plantFormId;
	}

	public void setPlantFormId(Long plantFormId) {
		this.plantFormId = plantFormId;
	}
	
	public CaseExeHistory convert2ExeHistory(){
		
		CaseExeHistory exeHist = new CaseExeHistory();
		exeHist.setTaskId(this.taskId);
		exeHist.setTestResult(this.testResult);
		exeHist.setTestActor(this.testActor);
		exeHist.setExeDate(this.exeDate);
		exeHist.setModuleId(this.moduleId);
		exeHist.setRemark(this.remark);
		exeHist.setTestCaseId(this.testCaseId);
		exeHist.setTestVer(this.testVer);
		exeHist.setPlantFormId(this.plantFormId);
		int status = this.getTestResult();
		if(status==1||status==6){
			exeHist.setOperaType(3);
		}
		if(status==2||status==3||status==4||status==5){
			exeHist.setOperaType(4);
		}
		return exeHist;
		
	}
}