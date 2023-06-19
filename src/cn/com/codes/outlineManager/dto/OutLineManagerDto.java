package cn.com.codes.outlineManager.dto;

import java.util.List;

import cn.com.codes.framework.transmission.dto.BaseDto;
import cn.com.codes.object.ModuleVerRec;
import cn.com.codes.object.TestTaskDetail;

public class OutLineManagerDto extends BaseDto {
	
	private String taskId ;
	private Long currNodeId ;
	private Integer currLevel;
	private Integer initLevel;
	private String command ;
	private Long parentNodeId ;
	private Long targetId ;
	private String nodeName ;
	private Integer moduleState;
	private String moduleNum;
	private String assignNIds ;
	private String userIds ;
	private String currVer;
	private int isCommit ;
	private Long klc;
	private TestTaskDetail task;
	private List<ModuleVerRec> mvList;
	private String[] moduleData = new String[10];
	private int[] caseCount = new int[10];
	private double[] quotiety = new double[10];
	private Integer[] scrpCount = new Integer[10];
	private Integer[] sceneCount = new Integer[10];
	private int reqType ;
	private Integer currSeq;
	private Boolean isMoveOpera =false;
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	//0为根节点
	public Long getCurrNodeId() {
		return currNodeId;
	}

	public void setCurrNodeId(Long currNodeId) {
		this.currNodeId = currNodeId;
	}

	public Integer getCurrLevel() {
		return currLevel;
	}

	public void setCurrLevel(Integer currLevel) {
		this.currLevel = currLevel;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String[] getModuleData() {
		return moduleData;
	}

	public void setModuleData(String[] moduleData) {
		this.moduleData = moduleData;
	}

	public Long getParentNodeId() {
		return parentNodeId;
	}

	public void setParentNodeId(Long parentNodeId) {
		this.parentNodeId = parentNodeId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public Integer getModuleState() {
		return moduleState;
	}

	public void setModuleState(Integer moduleState) {
		this.moduleState = moduleState;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public String getAssignNIds() {
		return assignNIds;
	}

	public void setAssignNIds(String assignNIds) {
		this.assignNIds = assignNIds;
	}

	public Long getKlc() {
		return klc;
	}

	public void setKlc(Long klc) {
		this.klc = klc;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getModuleNum() {
		return moduleNum;
	}

	public void setModuleNum(String moduleNum) {
		this.moduleNum = moduleNum;
	}

	public String getCurrVer() {
		return currVer;
	}

	public void setCurrVer(String currVer) {
		this.currVer = currVer;
	}

	public int getIsCommit() {
		return isCommit;
	}

	public void setIsCommit(int isCommit) {
		this.isCommit = isCommit;
	}

	public Integer getInitLevel() {
		return initLevel;
	}

	public void setInitLevel(Integer initLevel) {
		this.initLevel = initLevel;
	}

	public TestTaskDetail getTask() {
		return task;
	}

	public void setTask(TestTaskDetail task) {
		this.task = task;
	}

	public List<ModuleVerRec> getMvList() {
		return mvList;
	}

	public void setMvList(List<ModuleVerRec> mvList) {
		this.mvList = mvList;
	}
	public int[] getCaseCount() {
		return caseCount;
	}

	public void setCaseCount(int[] caseCount) {
		this.caseCount = caseCount;
	}

	public double[] getQuotiety() {
		return quotiety;
	}

	public void setQuotiety(double[] quotiety) {
		this.quotiety = quotiety;
	}

	public Integer[] getScrpCount() {
		return scrpCount;
	}

	public void setScrpCount(Integer[] scrpCount) {
		this.scrpCount = scrpCount;
	}

	public Integer[] getSceneCount() {
		return sceneCount;
	}

	public void setSceneCount(Integer[] sceneCount) {
		this.sceneCount = sceneCount;
	}

	public int getReqType() {
		return reqType;
	}

	public void setReqType(int reqType) {
		this.reqType = reqType;
	}

	public Integer getCurrSeq() {
		return currSeq;
	}

	public void setCurrSeq(Integer currSeq) {
		this.currSeq = currSeq;
	}

	public Boolean getIsMoveOpera() {
		return isMoveOpera;
	}

	public void setIsMoveOpera(Boolean isMoveOpera) {
		this.isMoveOpera = isMoveOpera;
	}
	

}
