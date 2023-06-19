package cn.com.codes.bugManager.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.com.codes.common.util.FileInfoVo;
import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.common.ListObject;
import cn.com.codes.framework.transmission.dto.BaseDto;
import cn.com.codes.object.BugBaseInfo;
import cn.com.codes.object.BugQueryInfo;
import cn.com.codes.object.FileInfo;
import cn.com.codes.object.TypeDefine;
import cn.com.codes.testTaskManager.dto.CurrTaskInfo;

public class BugManagerDto extends BaseDto {

	private BugBaseInfo bug ;
	private BugQueryInfo queryInfo;
	private FileInfo fileInfo;
	private String roleInTask ;
	private String testFlow;
	private String taskId ;
	private Long currNodeId ;
	private int defBug;//是否查询与自己相关的BUG标记，1为是
	private List<TypeDefine> typeList;
	private String moduleName;  ///除正常使用外,批量分配时,用来临时存被分配的开发人员姓名
	private String stateName;//状态名称 ///除正常使用外,批量分配时,用来临时存放项目名称
	private List<ListObject> queryList ;
	private CurrTaskInfo currTaskInfo;
	private List<ListObject> stateList;
	private List<ListObject> verList;
	private int saveQuery;
	private String queryName;
	private Long queryId;
	private int loadType;
	private int appScope;
	private int relCaseSwitch;
	private Integer outLineState;
	private String currOwner;
	private Long currVer;
	private String projectId;//除正常使用外,批量分配时,用来临时存bugid 
	private String initReProStep;
	private boolean allTestTask;
	private String countStr;
	
	private Date reptDateEnd;
	private String stateIds;
	private Integer currStateIds[] ;
	private String isDetailFlag;//标识是否为详情页 只能查看
	
	private String taskFlag;//标识 不为"" 不从session中取taskId
	
	private String bugJoinId;//返回已选的bugId
	
	private List<FileInfoVo> fileInfos;//文件信息
	
	public BugBaseInfo getBug() {
		return bug;
	}

	public void setBug(BugBaseInfo bug) {
		this.bug = bug;
	}
	
	public FileInfo getFileInfo() {
		return fileInfo;
	}

	public void setFileInfo(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
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

	public String getTaskId() {
		return taskId;
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

	public int getDefBug() {
		return defBug;
	}

	public void setDefBug(int defBug) {
		this.defBug = defBug;
	}


	public List<TypeDefine> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<TypeDefine> typeList) {
		this.typeList = typeList;
	}


	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public CurrTaskInfo getCurrTaskInfo() {
		return currTaskInfo;
	}

	public void setCurrTaskInfo(CurrTaskInfo currTaskInfo) {
		this.currTaskInfo = currTaskInfo;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public List<ListObject> getStateList() {
		if(stateList==null){//加这个处理是为了当不在测试任务上午文进，BUG列表页面不，出一个为空的警告
			return new ArrayList<ListObject> (1);
		}
		return stateList;
	}

	public void setStateList(List<ListObject> stateList) {
		this.stateList = stateList;
	}

	public int getSaveQuery() {
		return saveQuery;
	}

	public void setSaveQuery(int saveQuery) {
		this.saveQuery = saveQuery;
	}

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public BugQueryInfo getQueryInfo() {
		return queryInfo;
	}

	public void setQueryInfo(BugQueryInfo queryInfo) {
		this.queryInfo = queryInfo;
	}

	public Long getQueryId() {
		return queryId;
	}

	public void setQueryId(Long queryId) {
		this.queryId = queryId;
	}

	public List<ListObject> getQueryList() {
		return queryList;
	}

	public void setQueryList(List<ListObject> queryList) {
		this.queryList = queryList;
	}

	public int getLoadType() {
		return loadType;
	}

	public void setLoadType(int loadType) {
		this.loadType = loadType;
	}

	public int getAppScope() {
		return appScope;
	}

	public void setAppScope(int appScope) {
		this.appScope = appScope;
	}

	public int getRelCaseSwitch() {
		return relCaseSwitch;
	}

	public void setRelCaseSwitch(int relCaseSwitch) {
		this.relCaseSwitch = relCaseSwitch;
	}

	public String getCurrOwner() {
		return currOwner;
	}

	public void setCurrOwner(String currOwner) {
		this.currOwner = currOwner;
	}

	public Integer getOutLineState() {
		return outLineState;
	}

	public void setOutLineState(Integer outLineState) {
		this.outLineState = outLineState;
	}

	public Long getCurrVer() {
		return currVer;
	}

	public void setCurrVer(Long currVer) {
		this.currVer = currVer;
	}

	public List<ListObject> getVerList() {
		return verList;
	}

	public void setVerList(List<ListObject> verList) {
		this.verList = verList;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getInitReProStep() {
		return initReProStep;
	}

	public void setInitReProStep(String initReProStep) {
		this.initReProStep = initReProStep;
	}

	public boolean isAllTestTask() {
		return allTestTask;
	}

	public void setAllTestTask(boolean allTestTask) {
		this.allTestTask = allTestTask;
	}
	public String getCountStr() {
		return this.countStr;
	}

	public void setCountStr(String countStr) {
		this.countStr = countStr;
	}

	public Date getReptDateEnd() {
		return reptDateEnd;
	}

	public void setReptDateEnd(Date reptDateEnd) {
		this.reptDateEnd = reptDateEnd;
	}
	
	public String getStateIds() {
		return stateIds;
	}

	public void setStateIds(String stateIds) {
		this.stateIds = stateIds;
		String[] ids = stateIds.split(",");
		if(ids!=null &&ids.length>0){
			Integer[] arr =new Integer[ids.length]; 
			for (int i = 0; i < ids.length; i++) {
				arr[i] = Integer.valueOf(ids[i]);
			}
			this.currStateIds = arr;
		}
		
	}

	public Integer[] getCurrStateIds() {
		return currStateIds;
	}

	public void setCurrStateIds(Integer[] currStateIds) {
		this.currStateIds = currStateIds;
	}

	public String getIsDetailFlag() {
		return isDetailFlag;
	}

	public void setIsDetailFlag(String isDetailFlag) {
		this.isDetailFlag = isDetailFlag;
	}

	public String getTaskFlag() {
		return taskFlag;
	}

	public void setTaskFlag(String taskFlag) {
		this.taskFlag = taskFlag;
	}

	/**  
	* @return bugJoinId 
	*/
	public String getBugJoinId() {
		return bugJoinId;
	}

	/**  
	* @param bugJoinId bugJoinId 
	*/
	public void setBugJoinId(String bugJoinId) {
		this.bugJoinId = bugJoinId;
	}

	public List<FileInfoVo> getFileInfos() {
		return fileInfos;
	}

	public void setFileInfos(List<FileInfoVo> fileInfos) {
		this.fileInfos = fileInfos;
	}
	
}
