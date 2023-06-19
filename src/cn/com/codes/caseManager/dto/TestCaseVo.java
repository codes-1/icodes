package cn.com.codes.caseManager.dto;

import java.util.Date;

import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.transmission.JsonInterface;

public class TestCaseVo implements JsonInterface {

	private Long testCaseId;
	private String prefixCondition;
	private String testCaseDes;
	private Integer weight;
	private Integer testResult;
	private String testActor;
	private Date exeDate;
	private Long testVer;
	private String remark;
	private String testActorName;
	private String testVerName;
	private String taskName;
	private String taskId;
	private Long moduleId;

	public TestCaseVo(){
		
	}
	
	public TestCaseVo(Long testCaseId,String prefixCondition,String testCaseDes,Integer weight,Integer testResult
			,String testActor,Date exeDate,Long testVer,String remark,String taskId,Long moduleId){
		this.testCaseId = testCaseId;
		this.prefixCondition = prefixCondition;
		this.testCaseDes = testCaseDes;
		this.weight= weight;
		this.testResult = testResult;
		this.testActor = testActor;
		this.exeDate = exeDate;
		this.testVer = testVer;
		this.remark = remark;
		this.taskId = taskId;
		this.moduleId = moduleId;
	}
	public Long getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(Long testCaseId) {
		this.testCaseId = testCaseId;
	}

	public String getPrefixCondition() {
		return prefixCondition;
	}

	public void setPrefixCondition(String prefixCondition) {
		this.prefixCondition = prefixCondition;
	}

	public String getTestCaseDes() {
		return testCaseDes;
	}

	public void setTestCaseDes(String testCaseDes) {
		this.testCaseDes = testCaseDes;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getTestResult() {
		return testResult;
	}

	public void setTestResult(Integer testResult) {
		this.testResult = testResult;
	}

	public String getTestActor() {
		return testActor;
	}

	public void setTestActor(String testActor) {
		this.testActor = testActor;
	}

	public Date getExeDate() {
		return exeDate;
	}

	public void setExeDate(Date exeDate) {
		this.exeDate = exeDate;
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

	public String toStrList() {
		return "";
	}

	public String toStrUpdateInit() {

		return "";
	}

	public String toStrUpdateRest() {
		return "";
	}

	public String getTestActorName() {
		return testActorName;
	}

	public void setTestActorName(String testActorName) {
		this.testActorName = testActorName;
	}

	public String getTestVerName() {
		return testVerName;
	}

	public void setTestVerName(String testVerName) {
		this.testVerName = testVerName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public byte byteValue() {
		return moduleId.byteValue();
	}

	public int compareTo(Long anotherLong) {
		return moduleId.compareTo(anotherLong);
	}

	public double doubleValue() {
		return moduleId.doubleValue();
	}

	public boolean equals(Object obj) {
		return moduleId.equals(obj);
	}

	public float floatValue() {
		return moduleId.floatValue();
	}

	public int hashCode() {
		return moduleId.hashCode();
	}

	public int intValue() {
		return moduleId.intValue();
	}

	public long longValue() {
		return moduleId.longValue();
	}

	public short shortValue() {
		return moduleId.shortValue();
	}

	public String toString() {
		return moduleId.toString();
	}
	
	public void toString(StringBuffer sbf) {
		sbf.append("{");
		sbf.append("id:'");
		sbf.append(getTestCaseId().toString()).append("_").append(testVer==null?"0":testVer);
		sbf.append("',data: [0,'"+this.testCaseId+"','");
		sbf.append(testCaseDes == null ? "" : testCaseDes);
		sbf.append("','");
		sbf.append(testResult == null ? "未测试" : convCase(testResult));
		sbf.append("','");
		sbf.append(testActorName == null ? "" : testActorName);
		sbf.append("','");
		sbf.append(testVerName == null ? "" : testVerName);
		sbf.append("','");
		sbf.append(exeDate == null ? "" : StringUtils.formatLongDate(exeDate));
		sbf.append("','");
		sbf.append(weight == null ? "" : weight);
		sbf.append("','");
		sbf.append(taskName == null ? "" : taskName);
		sbf.append("','");
		sbf.append(remark == null ? "" : remark);
		sbf.append("','");
		sbf.append(taskId);
		sbf.append("','");
		sbf.append(moduleId);
		sbf.append("'");
		sbf.append("]");
		sbf.append("}");
	}

	public static String convCase(Integer testStatus){
		
		if(testStatus==2){
			return "通过" ;
		}else if(testStatus==3){
			return "未通过" ;
		}else if(testStatus==4){
			return "不适用" ;
		}else if(testStatus==5){
			return "阻塞" ;
		}
		return "";
	}

}
