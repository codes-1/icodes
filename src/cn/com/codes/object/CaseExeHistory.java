package cn.com.codes.object;

import java.util.Date;

import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.transmission.JsonInterface;
import cn.com.codes.object.CaseExeHistory;

public class CaseExeHistory implements JsonInterface {

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
	private String  testVerNum;
	private String testActorName;
	private Long plantFormId;
	private Integer operaType;// 操作类型1:增加;2修改;3审核;4执行;5:修正
	

	public CaseExeHistory() {

	}

	public CaseExeHistory(Long resultId) {
		this.resultId = resultId;
	}

	public CaseExeHistory(Long resultId, Long testVer, Integer testResult) {
		this.resultId = resultId;
		this.testVer = testVer;
		this.testResult = testResult;
	}
	
	public CaseExeHistory(Long resultId, Long testVer, Integer testResult,String testActor,Date exeDate,String remark,Integer operaType) {
		this.resultId = resultId;
		this.testVer = testVer;
		this.testResult = testResult;
		this.testActor = testActor;
		this.exeDate = exeDate;
		this.remark = remark;
		this.operaType = operaType;
	}

	public CaseExeHistory copy() {
		CaseExeHistory rest = new CaseExeHistory();
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



	public Date getExeDate() {
		return exeDate;
	}

	public void setExeDate(Date exeDate) {
		this.exeDate = exeDate;
	}



	public Long getPlantFormId() {
		return plantFormId;
	}

	public void setPlantFormId(Long plantFormId) {
		this.plantFormId = plantFormId;
	}

	public Integer getOperaType() {
		return operaType;
	}

	public void setOperaType(Integer operaType) {
		this.operaType = operaType;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CaseExeHistory))
			return false;
		CaseExeHistory castOther = (CaseExeHistory) other;
		return ((this.getResultId() == castOther.getResultId()) || (this
				.getResultId() != null
				&& castOther.getResultId() != null && this.getResultId()
				.equals(castOther.getResultId())));
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result
				+ (getResultId() == null ? 0 : this.getResultId().hashCode());

		return result;
	}

	// public String toString() {
	// return softVer.toString();
	// }

	public void toString(StringBuffer sbf) {

		sbf.append("{");
		sbf.append("id:'");
		sbf.append(getResultId().toString());
		sbf.append("',data: [0,'"+getResultId().toString()+"','");
		sbf.append(testActorName == null ? "" : testActorName);
		sbf.append("','");
		sbf.append(operaType == null ? "" : getOperaName(operaType));
		sbf.append("','");
		sbf.append(testResult == null ? "" : convCase(testResult));
		sbf.append("','");
		sbf.append(testVerNum == null ? "" : testVerNum);
		sbf.append("','");
		sbf.append(exeDate == null ? "" : StringUtils.formatLongDate(exeDate));
		sbf.append("','");
		sbf.append(remark == null ? "" : remark);
		sbf.append("'");
		sbf.append("]");
		sbf.append("}");
	}

	public static String convCase(Integer testStatus) {
		if (testStatus == 0) {
			// return "ReView" ;
			return "待审核";
		} else if (testStatus == 1) {
			return "未测试";
		} else if (testStatus == 2) {
			return "通过";
		} else if (testStatus == 3) {
			return "未通过";
		} else if (testStatus == 4) {
			return "不适用";
		} else if (testStatus == 5) {
			return "阻塞";
		} else if (testStatus == 6) {
			return "待修正";
		}
		return "";
	}

	public static String getOperaName(Integer operaType) {
		// 操作类型1:增加;2修改;3审核;4执行
		if (operaType == 1) {
			return "增加用例";
		} else if (operaType == 2) {
			return "修改用例";
		} else if (operaType == 3) {
			return "审核用例";
		} else if (operaType == 4) {
			return "执行用例";
		} else if (operaType == 5) {
			return "修正用例";
		}
		return "";
	}

	public String toStrList() {
		return null;
	}

	public String toStrUpdateInit() {
		return null;
	}

	public String toStrUpdateRest() {
		return null;
	}

	public String getTestActorName() {
		return testActorName;
	}

	public void setTestActorName(String testActorName) {
		this.testActorName = testActorName;
	}

	public String getTestVerNum() {
		return testVerNum;
	}

	public void setTestVerNum(String testVerNum) {
		this.testVerNum = testVerNum;
	}
}