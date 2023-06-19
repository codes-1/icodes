package cn.com.codes.testTaskManager.dto;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.object.TaskUseActor;
import cn.com.codes.object.TestFlowInfo;

public class CurrTaskInfo implements Serializable{

	private String roleInTask ;
	private String testFlow;
	private Integer testPhase;
	//private Integer testSeq;
	private int outLineState ;
	//private String currentVersion;
	List<TestFlowInfo> flowList;
	List<TaskUseActor> actorList;
	private String devIdStr;
	private String testIdStr;
	private String analysIdStr;
	private String assinIdStr ;
	private String intercsIdStr;
	private String devChkIdStr;
	private String testChkIdStr;
	private String testLdIdStr;
	private int relCaseSwitch;

	
	public String getTestAndLdStr(){
		return testIdStr +"," +testLdIdStr;
	}
	
	public String getTestAndtestChkStr(){
		if(testChkIdStr!=null&&!"".equals(testChkIdStr)){
			return testIdStr +"," +testLdIdStr;
		}
		return testIdStr;
	}
	public String getTestChkAndLdStr(){
		
		return testChkIdStr +"," +testLdIdStr;

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
		return SecurityContextHolderHelp.getCurrTaksId();
	}
	public Integer getTestPhase() {
		return testPhase;
	}
	public void setTestPhase(Integer testPhase) {
		this.testPhase = testPhase;
	}
//	public Integer getTestSeq() {
//		return testSeq;
//	}
//	public void setTestSeq(Integer testSeq) {
//		this.testSeq = testSeq;
//	}
//	public String getCurrentVersion() {
//		return currentVersion;
//	}
//	public void setCurrentVersion(String currentVersion) {
//		this.currentVersion = currentVersion;
//	}
	public List<TestFlowInfo> getFlowList() {
		return flowList;
	}
	public void setFlowList(List<TestFlowInfo> flowList) {
		this.flowList = flowList;
	}
	public List<TaskUseActor> getActorList() {
		return actorList;
	}
	public void setActorList(List<TaskUseActor> actorList) {
		this.actorList = actorList;
	}
	public String getDevIdStr() {
		return devIdStr;
	}
	public void setDevIdStr(String devIdStr) {
		this.devIdStr = devIdStr;
	}
	public String getTestIdStr() {
		return testIdStr;
	}
	public void setTestIdStr(String testIdStr) {
		this.testIdStr = testIdStr;
	}
	public String getAnalysIdStr() {
		return analysIdStr;
	}
	public void setAnalysIdStr(String analysIdStr) {
		this.analysIdStr = analysIdStr;
	}
	public String getAssinIdStr() {
		return assinIdStr;
	}
	public void setAssinIdStr(String assinIdStr) {
		this.assinIdStr = assinIdStr;
	}
	public String getIntercsIdStr() {
		return intercsIdStr;
	}
	public void setIntercsIdStr(String intercsIdStr) {
		this.intercsIdStr = intercsIdStr;
	}
	public String getDevChkIdStr() {
		return devChkIdStr;
	}
	public void setDevChkIdStr(String devChkIdStr) {
		this.devChkIdStr = devChkIdStr;
	}
	public String getTestChkIdStr() {
		return testChkIdStr;
	}
	public void setTestChkIdStr(String testChkIdStr) {
		this.testChkIdStr = testChkIdStr;
	}
	public String getTestLdIdStr() {
		return testLdIdStr;
	}
	public void setTestLdIdStr(String testLdIdStr) {
		this.testLdIdStr = testLdIdStr;
	}

	public int getRelCaseSwitch() {
		return relCaseSwitch;
	}

	public void setRelCaseSwitch(int relCaseSwitch) {
		this.relCaseSwitch = relCaseSwitch;
	}

	public int getOutLineState() {
		return outLineState;
	}

	public void setOutLineState(int outLineState) {
		this.outLineState = outLineState;
	}
	
	public boolean isTeamMember(String userId){
		if(actorList==null||actorList.isEmpty()){
			return false;
		}
		for(TaskUseActor ac : actorList){
			if(ac.getUserId().equals(userId)){
				return true;
			}
		}
		return false;
	}
}
