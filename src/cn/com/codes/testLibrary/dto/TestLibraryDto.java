package cn.com.codes.testLibrary.dto;


import cn.com.codes.framework.transmission.dto.BaseDto;
import cn.com.codes.object.TestCaseLibrary;
import cn.com.codes.object.TestCaseLibraryDetail;

public class TestLibraryDto extends BaseDto {

	/**  
	* 字段:      字段名称
	* @Fields serialVersionUID : TODO 
	*/
	private static final long serialVersionUID = 1L;
	//存放测试用例库实例
	private TestCaseLibrary testCaseLibrary;
	//存放测试用例库中具体测试用例
	private TestCaseLibraryDetail testCaseLibraryDetail;
	//存放用例类型Id
	private String libraryId;
	//存放用例类型code
	private String libraryCode;
	//存放多个测试用例的id
	private String caseIdS;
	//存放推荐者id
	private String recommendUserId;
	//存放推荐理由
	private String recommendReason;
	//存放审核的testCaseIds
	private String testCaseIds;
	/*//存放审核人id
	private String examineUserId;*/
	//用到从用例库选择用例导入到测试用例的参数
	private String taskId;
	private String moduleId;
	private String moduleNum;
	private String createrId;
	private String numbers;
	
	public TestCaseLibrary getTestCaseLibrary() {
		return testCaseLibrary;
	}
	public void setTestCaseLibrary(TestCaseLibrary testCaseLibrary) {
		this.testCaseLibrary = testCaseLibrary;
	}
	public TestCaseLibraryDetail getTestCaseLibraryDetail() {
		return testCaseLibraryDetail;
	}
	public void setTestCaseLibraryDetail(TestCaseLibraryDetail testCaseLibraryDetail) {
		this.testCaseLibraryDetail = testCaseLibraryDetail;
	}
	public String getLibraryId() {
		return libraryId;
	}
	public void setLibraryId(String libraryId) {
		this.libraryId = libraryId;
	}
	public String getLibraryCode() {
		return libraryCode;
	}
	public void setLibraryCode(String libraryCode) {
		this.libraryCode = libraryCode;
	}
	public String getCaseIdS() {
		return caseIdS;
	}
	public void setCaseIdS(String caseIdS) {
		this.caseIdS = caseIdS;
	}
	public String getRecommendUserId() {
		return recommendUserId;
	}
	public void setRecommendUserId(String recommendUserId) {
		this.recommendUserId = recommendUserId;
	}
	public String getRecommendReason() {
		return recommendReason;
	}
	public void setRecommendReason(String recommendReason) {
		this.recommendReason = recommendReason;
	}
	/*public String getExamineUserId() {
		return examineUserId;
	}
	public void setExamineUserId(String examineUserId) {
		this.examineUserId = examineUserId;
	}*/
	public String getTestCaseIds() {
		return testCaseIds;
	}
	public void setTestCaseIds(String testCaseIds) {
		this.testCaseIds = testCaseIds;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getCreaterId() {
		return createrId;
	}
	public void setCreaterId(String createrId) {
		this.createrId = createrId;
	}
	public String getNumbers() {
		return numbers;
	}
	public void setNumbers(String numbers) {
		this.numbers = numbers;
	}
	public String getModuleNum() {
		return moduleNum;
	}
	public void setModuleNum(String moduleNum) {
		this.moduleNum = moduleNum;
	}
}
