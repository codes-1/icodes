package cn.com.codes.caseManager.dto;

import java.io.File;
import java.util.List;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.util.FileInfoVo;
import cn.com.codes.framework.transmission.dto.BaseDto;
import cn.com.codes.object.TestCaseInfo;
import cn.com.codes.object.TestResult;
import cn.com.codes.caseManager.dto.QueryHelp;

public class CaseManagerDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String taskId ;
	private Long currNodeId ;
	private String command ;
	private String crName;
	private String actorName;
	private int remQuery ;
	private TestCaseInfo testCaseInfo;
	private TestResult rest;
	private int isReview;
    private int canReview;
    private int isTestLeader;
    private int testPhase; //测试性质，1为集成，2为系统
    private Integer outLineState;
    private Long exeVerId;
    private String remark; 
    private QueryHelp queryHelp;
    private String countStr;
    private String impFilePath;
	private File importFile;
	private String importFileContentType;
	private String importFileFileName;
	private List<FileInfoVo> fileInfos;//文件信息
	/*存放前台传来的用例Id*/
	private String caseIds;
	private String testCasePackId;
	public String getTaskId() {
		return taskId==null||"".equals(taskId)?SecurityContextHolderHelp.getCurrTaksId():taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Long getCurrNodeId() {
		return currNodeId;
	}

	public void setCurrNodeId(Long currNodeId) {
		this.currNodeId = currNodeId;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public TestCaseInfo getTestCaseInfo() {
		return testCaseInfo;
	}

	public void setTestCaseInfo(TestCaseInfo testCase) {
		this.testCaseInfo = testCase;
	}

	public String getCrName() {
		return crName;
	}

	public void setCrName(String crName) {
		this.crName = crName;
	}

	public String getActorName() {
		return actorName;
	}

	public void setActorName(String actorName) {
		this.actorName = actorName;
	}

	public int getRemQuery() {
		return remQuery;
	}

	public void setRemQuery(int remQuery) {
		this.remQuery = remQuery;
	}

	public TestResult getRest() {
		return rest;
	}

	public void setRest(TestResult rest) {
		this.rest = rest;
	}

	public int getIsReview() {
		return isReview;
	}

	public void setIsReview(int isReview) {
		this.isReview = isReview;
	}

	public int getCanReview() {
		return canReview;
	}

	public void setCanReview(int canReview) {
		this.canReview = canReview;
	}

	public int getIsTestLeader() {
		return isTestLeader;
	}

	public void setIsTestLeader(int isTestLeader) {
		this.isTestLeader = isTestLeader;
	}

//	public String getVerStr() {
//		return verStr;
//	}
//
//	public void setVerStr(String verStr) {
//		this.verStr = verStr;
//	}
//
//	public String getRestGrid() {
//		return restGrid;
//	}
//
//	public void setRestGrid(String restGrid) {
//		this.restGrid = restGrid;
//	}
//
//	public int getTestSeqCount() {
//		return testSeqCount;
//	}
//
//	public void setTestSeqCount(int testSeqCount) {
//		this.testSeqCount = testSeqCount;
//	}

	public int getTestPhase() {
		return testPhase;
	}

	public void setTestPhase(int testPhase) {
		this.testPhase = testPhase;
	}

	public Integer getOutLineState() {
		return outLineState;
	}

	public void setOutLineState(Integer outLineState) {
		this.outLineState = outLineState;
	}

	public Long getExeVerId() {
		return exeVerId;
	}

	public void setExeVerId(Long exeVerId) {
		this.exeVerId = exeVerId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public QueryHelp getQueryHelp() {
		return queryHelp;
	}

	public void setQueryHelp(QueryHelp queryHelp) {
		this.queryHelp = queryHelp;
	}
	public String getCountStr() {
		return this.countStr;
	}

	public void setCountStr(String countStr) {
		this.countStr = countStr;
	}

	public String getImpFilePath() {
		return impFilePath;
	}

	public void setImpFilePath(String impFilePath) {
		this.impFilePath = impFilePath;
	}

	public File getImportFile() {
		return importFile;
	}

	public void setImportFile(File importFile) {
		this.importFile = importFile;
	}

	public String getImportFileContentType() {
		return importFileContentType;
	}

	public void setImportFileContentType(String importFileContentType) {
		this.importFileContentType = importFileContentType;
	}

	public String getImportFileFileName() {
		return importFileFileName;
	}

	public void setImportFileFileName(String importFileFileName) {
		this.importFileFileName = importFileFileName;
	}

	public List<FileInfoVo> getFileInfos() {
		return fileInfos;
	}

	public void setFileInfos(List<FileInfoVo> fileInfos) {
		this.fileInfos = fileInfos;
	}

	public String getCaseIds() {
		return caseIds;
	}

	public void setCaseIds(String caseIds) {
		this.caseIds = caseIds;
	}

	public String getTestCasePackId() {
		return testCasePackId;
	}

	public void setTestCasePackId(String testCasePackId) {
		this.testCasePackId = testCasePackId;
	}
	
}
