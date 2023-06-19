package cn.com.codes.framework.common;

import java.util.Map;

public class PersonalBusiness {
	//我负责的项目
	//我需审批的项目
	//我需审批的进度
	//我收到的消息
	//我的今日任务
	//我的Bug
	//我收到的工作报告
	//我参与的工作流
	//我发起的工作流
	//我最近执行过的测试用例
	//我需审批的通用任务工时
	
	private int myProject = 0;
	private int myBug = 0;
	private int myMessage = 0;
	private int myTask = 0;
	private int myPCAP = 0;
	private int myPAP = 0;
	private int myReport = 0;
	private int myWorkFlow = 0;
	private int myLaunchWorkFlow = 0;
	private int myTestCase = 0;
	private int myCTWhAP = 0;
	private String myProjectHql;
	private String myBugHql;
	private String myMessageHql;
	private String myTaskHql;
	private String myPCAPHql;
	private String myPAPHql;
	private String myReportHql;
	private String myWorkFlowHql;
	private String myLaunchWorkFlowHql;
	private String myTestCaseHql;
	private String myCTWhAPHql;
	private Map<String,Object> myProjectMaps;
	private Map<String,Object> myBugMaps;
	private Map<String,Object> myMessageMaps;
	private Map<String,Object> myTaskMaps;
	private Map<String,Object> myPCAPMaps;
	private Map<String,Object> myPAPMaps;
	private Map<String,Object> myReportMaps;
	private Map<String,Object> myWorkFlowMaps;
	private Map<String,Object> myLaunchWorkFlowMaps;
	private Map<String,Object> myTestCaseMaps;
	private Map<String,Object> myCTWhAPMaps;
	
	public PersonalBusiness(){
	}

	public int getMyProject() {
		return myProject;
	}

	public void setMyProject(int myProject) {
		this.myProject = myProject;
	}

	public int getMyBug() {
		return myBug;
	}

	public void setMyBug(int myBug) {
		this.myBug = myBug;
	}

	public int getMyMessage() {
		return myMessage;
	}

	public void setMyMessage(int myMessage) {
		this.myMessage = myMessage;
	}

	public int getMyTask() {
		return myTask;
	}

	public void setMyTask(int myTask) {
		this.myTask = myTask;
	}

	public int getMyPCAP() {
		return myPCAP;
	}

	public void setMyPCAP(int myPCAP) {
		this.myPCAP = myPCAP;
	}

	public int getMyPAP() {
		return myPAP;
	}

	public void setMyPAP(int myPAP) {
		this.myPAP = myPAP;
	}

	public int getMyReport() {
		return myReport;
	}

	public void setMyReport(int myReport) {
		this.myReport = myReport;
	}

	public int getMyWorkFlow() {
		return myWorkFlow;
	}

	public void setMyWorkFlow(int myWorkFlow) {
		this.myWorkFlow = myWorkFlow;
	}

	public int getMyLaunchWorkFlow() {
		return myLaunchWorkFlow;
	}

	public void setMyLaunchWorkFlow(int myLaunchWorkFlow) {
		this.myLaunchWorkFlow = myLaunchWorkFlow;
	}

	public int getMyTestCase() {
		return myTestCase;
	}

	public void setMyTestCase(int myTestCase) {
		this.myTestCase = myTestCase;
	}

	public String getMyProjectHql() {
		return myProjectHql;
	}

	public void setMyProjectHql(String myProjectHql) {
		this.myProjectHql = myProjectHql;
	}

	public String getMyBugHql() {
		return myBugHql;
	}

	public void setMyBugHql(String myBugHql) {
		this.myBugHql = myBugHql;
	}

	public String getMyMessageHql() {
		return myMessageHql;
	}

	public void setMyMessageHql(String myMessageHql) {
		this.myMessageHql = myMessageHql;
	}

	public String getMyTaskHql() {
		return myTaskHql;
	}

	public void setMyTaskHql(String myTaskHql) {
		this.myTaskHql = myTaskHql;
	}

	public String getMyPCAPHql() {
		return myPCAPHql;
	}

	public void setMyPCAPHql(String myPCAPHql) {
		this.myPCAPHql = myPCAPHql;
	}

	public String getMyPAPHql() {
		return myPAPHql;
	}

	public void setMyPAPHql(String myPAPHql) {
		this.myPAPHql = myPAPHql;
	}

	public String getMyReportHql() {
		return myReportHql;
	}

	public void setMyReportHql(String myReportHql) {
		this.myReportHql = myReportHql;
	}

	public String getMyWorkFlowHql() {
		return myWorkFlowHql;
	}

	public void setMyWorkFlowHql(String myWorkFlowHql) {
		this.myWorkFlowHql = myWorkFlowHql;
	}

	public String getMyLaunchWorkFlowHql() {
		return myLaunchWorkFlowHql;
	}

	public void setMyLaunchWorkFlowHql(String myLaunchWorkFlowHql) {
		this.myLaunchWorkFlowHql = myLaunchWorkFlowHql;
	}

	public String getMyTestCaseHql() {
		return myTestCaseHql;
	}

	public void setMyTestCaseHql(String myTestCaseHql) {
		this.myTestCaseHql = myTestCaseHql;
	}

	public Map<String, Object> getMyProjectMaps() {
		return myProjectMaps;
	}

	public void setMyProjectMaps(Map<String, Object> myProjectMaps) {
		this.myProjectMaps = myProjectMaps;
	}

	public Map<String, Object> getMyBugMaps() {
		return myBugMaps;
	}

	public void setMyBugMaps(Map<String, Object> myBugMaps) {
		this.myBugMaps = myBugMaps;
	}

	public Map<String, Object> getMyMessageMaps() {
		return myMessageMaps;
	}

	public void setMyMessageMaps(Map<String, Object> myMessageMaps) {
		this.myMessageMaps = myMessageMaps;
	}

	public Map<String, Object> getMyTaskMaps() {
		return myTaskMaps;
	}

	public void setMyTaskMaps(Map<String, Object> myTaskMaps) {
		this.myTaskMaps = myTaskMaps;
	}

	public Map<String, Object> getMyPCAPMaps() {
		return myPCAPMaps;
	}

	public void setMyPCAPMaps(Map<String, Object> myPCAPMaps) {
		this.myPCAPMaps = myPCAPMaps;
	}

	public Map<String, Object> getMyPAPMaps() {
		return myPAPMaps;
	}

	public void setMyPAPMaps(Map<String, Object> myPAPMaps) {
		this.myPAPMaps = myPAPMaps;
	}

	public Map<String, Object> getMyReportMaps() {
		return myReportMaps;
	}

	public void setMyReportMaps(Map<String, Object> myReportMaps) {
		this.myReportMaps = myReportMaps;
	}

	public Map<String, Object> getMyWorkFlowMaps() {
		return myWorkFlowMaps;
	}

	public void setMyWorkFlowMaps(Map<String, Object> myWorkFlowMaps) {
		this.myWorkFlowMaps = myWorkFlowMaps;
	}

	public Map<String, Object> getMyLaunchWorkFlowMaps() {
		return myLaunchWorkFlowMaps;
	}

	public void setMyLaunchWorkFlowMaps(Map<String, Object> myLaunchWorkFlowMaps) {
		this.myLaunchWorkFlowMaps = myLaunchWorkFlowMaps;
	}

	public Map<String, Object> getMyTestCaseMaps() {
		return myTestCaseMaps;
	}

	public void setMyTestCaseMaps(Map<String, Object> myTestCaseMaps) {
		this.myTestCaseMaps = myTestCaseMaps;
	}

	public int getMyCTWhAP() {
		return myCTWhAP;
	}

	public void setMyCTWhAP(int myCTWhAP) {
		this.myCTWhAP = myCTWhAP;
	}

	public String getMyCTWhAPHql() {
		return myCTWhAPHql;
	}

	public void setMyCTWhAPHql(String myCTWhAPHql) {
		this.myCTWhAPHql = myCTWhAPHql;
	}

	public Map<String, Object> getMyCTWhAPMaps() {
		return myCTWhAPMaps;
	}

	public void setMyCTWhAPMaps(Map<String, Object> myCTWhAPMaps) {
		this.myCTWhAPMaps = myCTWhAPMaps;
	}

	public String toString(){
		StringBuffer bf = new StringBuffer();
		bf.append(myBug);
		bf.append("$");
		bf.append(myMessage);
		bf.append("$");
		bf.append(myTask);
		bf.append("$");
		bf.append(myProject);
		bf.append("$");
		bf.append(myPCAP);
		bf.append("$");
		bf.append(myPAP);
		bf.append("$");
		bf.append(myWorkFlow);
		bf.append("$");
		bf.append(myLaunchWorkFlow);
		bf.append("$");
		bf.append(myReport);
		bf.append("$");
		bf.append(myTestCase);
		bf.append("$");
		bf.append(myCTWhAP);
		return bf.toString();
	}
}
