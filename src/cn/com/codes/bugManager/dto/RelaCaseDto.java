package cn.com.codes.bugManager.dto;

import java.util.List;

import cn.com.codes.framework.transmission.dto.BaseDto;
import cn.com.codes.object.BugBaseInfo;
import cn.com.codes.object.CaseBugRela;
import cn.com.codes.object.TestCaseInfo;
import cn.com.codes.object.TestResult;
import cn.com.codes.object.TypeDefine;

public class RelaCaseDto extends BaseDto {

	private Long testCaseId;
	private Long bugId;
	private Long moduleId;
	private String bugIds;//邮BUGID 与空格分隔组件的字符串
	private String testCaseIds; //邮caseID 与空格分隔组件的字符串
	private String mdPath;
	private String testFlow;
	private String roleInTask;
	private BugBaseInfo bug ;
	private TestCaseInfo testCaseInfo;
	private List<TypeDefine> typeList;
	private List<BugBaseInfo> relaBugList;
	private List<TestCaseInfo> relaCaseList;
	private Long bugReptVer;
	private String taskId;
	private int isExeRela;
	private List<TestResult> currCaseTestRedList ;
	private String currCaseTestRedSet;
	private CaseBugRela caseBugRela;
	
	public Long getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(Long testCaseId) {
		this.testCaseId = testCaseId;
	}

	public Long getBugId() {
		return bugId;
	}

	public void setBugId(Long bugId) {
		this.bugId = bugId;
	}



	public List<TypeDefine> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<TypeDefine> typeList) {
		this.typeList = typeList;
	}


	public List<TestCaseInfo> getRelaCaseList() {
		return relaCaseList;
	}

	public void setRelaCaseList(List<TestCaseInfo> relaCaseList) {
		this.relaCaseList = relaCaseList;
	}

	public List<BugBaseInfo> getRelaBugList() {
		return relaBugList;
	}

	public void setRelaBugList(List<BugBaseInfo> relaBugList) {
		this.relaBugList = relaBugList;
	}

	public String getBugIds() {
		return bugIds;
	}

	public void setBugIds(String bugIds) {
		this.bugIds = bugIds;
	}

	public String getTestCaseIds() {
		return testCaseIds;
	}

	public void setTestCaseIds(String testCaseIds) {
		this.testCaseIds = testCaseIds;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public TestCaseInfo getTestCaseInfo() {
		return testCaseInfo;
	}

	public void setTestCaseInfo(TestCaseInfo testCaseInfo) {
		this.testCaseInfo = testCaseInfo;
	}

	public String getMdPath() {
		return mdPath;
	}

	public void setMdPath(String mdPath) {
		this.mdPath = mdPath;
	}

	public String getTestFlow() {
		return testFlow;
	}

	public void setTestFlow(String testFlow) {
		this.testFlow = testFlow;
	}

	public String getRoleInTask() {
		return roleInTask;
	}

	public void setRoleInTask(String roleInTask) {
		this.roleInTask = roleInTask;
	}

	public BugBaseInfo getBug() {
		return bug;
	}

	public void setBug(BugBaseInfo bug) {
		this.bug = bug;
	}


	public Long getBugReptVer() {
		return bugReptVer==null?0:bugReptVer;
	}

	public void setBugReptVer(Long bugReptVer) {
		this.bugReptVer = bugReptVer;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public int getIsExeRela() {
		return isExeRela;
	}

	public void setIsExeRela(int isExeRela) {
		this.isExeRela = isExeRela;
	}

	public String getCurrCaseTestRedSet() {
		return currCaseTestRedSet;
	}

	public void setCurrCaseTestRedSet(String currCaseTestRedSet) {
		this.currCaseTestRedSet = currCaseTestRedSet;
	}

	public List<TestResult> getCurrCaseTestRedList() {
		return currCaseTestRedList;
	}

	public void setCurrCaseTestRedList(List<TestResult> currCaseTestRedList) {
		this.currCaseTestRedList = currCaseTestRedList;
	}

	/**  
	* @return caseBugRela 
	*/
	public CaseBugRela getCaseBugRela() {
		return caseBugRela;
	}

	/**  
	* @param caseBugRela caseBugRela 
	*/
	public void setCaseBugRela(CaseBugRela caseBugRela) {
		this.caseBugRela = caseBugRela;
	}


}
