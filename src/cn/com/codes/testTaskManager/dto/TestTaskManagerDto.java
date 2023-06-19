package cn.com.codes.testTaskManager.dto;

import java.util.List;

import cn.com.codes.framework.transmission.dto.BaseDto;
import cn.com.codes.object.BugHandHistory;
import cn.com.codes.object.SoftwareVersion;
import cn.com.codes.object.TaskUseActor;
import cn.com.codes.object.TestFlowInfo;
import cn.com.codes.object.TestTaskDetail;
import cn.com.codes.testTaskManager.dto.VTestkTaskActor;

public class TestTaskManagerDto extends BaseDto {

	TestTaskDetail detail ;
	//用例Review 人Id
	private String caseReviewId ;
	// 测试人员ID
	private String testerId = "";
	// 测试确认人ID
	private String testerConfId = "";
	// 分析人ID
	private String analyserId = "";
	// 分配人ID
	private String assignationId = "";
	// 编码人员ID
	private String programmerId = "";
	// 开发确认人ID
	private String empolderAffirmantId = "";
	// 开发负责人ID
	private String empolderLeaderId = "";
	//测试负责人ID
	private String testLeadId ;
	
	//项目关注人ID
	private String proRelaPersonId ;
	//用例Review人员
	
	private String caseReviewer;
	// 测试人员
	private String tester = "";
	//测试确认人
	private String testerConf = "";
	// 分析人
	private String analyser = "";
	// 分配人
	private String assigner = "";

	// 编码人员
	private String programmer = "";
	// 开发确认人
	private String empolderAffirmant = "";
	// 开发负责人
	private String empolderLeader = "";
	//测试负责人
	private String testLead ;
	//测试确认
	private String testerConfName ;
	
	//项目关注人姓名
	
	private String proRelaPerson;

	private String relaTaskName ;
	
	//查找人员时，选择组的
	private String groupId;
	//任务名称
	private String taskName ;

	private String taskId;
	private int testPhase;
	private List<VTestkTaskActor> testActroList;
	private List<TestFlowInfo>    testFlowList;
	private List<TaskUseActor>   useActorPoList;
	private String testFlowStr ;
	
	private String initFlowStr;
	//键值对形式，且多个键值对间用^隔开的jsdon 形式
	private String testFlwKeyValueStr;
	//模块ID,新建版本时用
	private String moduleIds;
	
	private String SelStrName;//校验人员变动时，人员名字所在字符名，校验时用来查没处理的BUG
	private String chgUserIds;//校验人员变动时,被删除的人员ID串
	
	private SoftwareVersion softVer;
	
	//private String remFlwDetal; //删除流程明细，形式为2-5 3-5 被删除的流程用空格隔开，流程后用杠来指明删除后他的下的流程，用这标记来生成BUG历史
	List<BugHandHistory> historyAdjustList;
	
	private String taskType;
	
	private String comeFrom;
	public TestTaskDetail getDetail() {
		return detail;
	}

	public void setDetail(TestTaskDetail detail) {
		this.detail = detail;
	}

	public String getEmpolderLeaderId() {
		return empolderLeaderId;
	}

	public void setEmpolderLeaderId(String empolderLeaderId) {
		this.empolderLeaderId = empolderLeaderId;
	}

	public String getTesterId() {
		return testerId;
	}

	public void setTesterId(String testerId) {
		this.testerId = testerId;
	}



	public String getAnalyserId() {
		return analyserId;
	}

	public void setAnalyserId(String analyserId) {
		this.analyserId = analyserId;
	}

	public String getAssignationId() {
		return assignationId;
	}

	public void setAssignationId(String assignationId) {
		this.assignationId = assignationId;
	}

	public String getProgrammerId() {
		return programmerId;
	}

	public void setProgrammerId(String programmerId) {
		this.programmerId = programmerId;
	}

	public String getEmpolderAffirmantId() {
		return empolderAffirmantId;
	}

	public void setEmpolderAffirmantId(String empolderAffirmantId) {
		this.empolderAffirmantId = empolderAffirmantId;
	}


	public String getAnalyser() {
		return analyser;
	}

	public void setAnalyser(String analyser) {
		this.analyser = analyser;
	}

	public String getAssigner() {
		return assigner;
	}

	public void setAssigner(String assigner) {
		this.assigner = assigner;
	}

	public String getTester() {
		return tester;
	}

	public void setTester(String tester) {
		this.tester = tester;
	}

	public String getProgrammer() {
		return programmer;
	}

	public void setProgrammer(String programmer) {
		this.programmer = programmer;
	}

	public String getEmpolderAffirmant() {
		return empolderAffirmant;
	}

	public void setEmpolderAffirmant(String empolderAffirmant) {
		this.empolderAffirmant = empolderAffirmant;
	}

	public String getEmpolderLeader() {
		return empolderLeader;
	}

	public void setEmpolderLeader(String empolderLeader) {
		this.empolderLeader = empolderLeader;
	}

	public String getTesterConfId() {
		return testerConfId;
	}

	public void setTesterConfId(String testerConfId) {
		this.testerConfId = testerConfId;
	}

	public String getTesterConfName() {
		return testerConfName;
	}

	public void setTesterConfName(String testerConfName) {
		this.testerConfName = testerConfName;
	}

	public String getTestLeadId() {
		return testLeadId;
	}

	public void setTestLeadId(String testLeadId) {
		this.testLeadId = testLeadId;
	}

	public String getTesterConf() {
		return testerConf;
	}

	public void setTesterConf(String testerConf) {
		this.testerConf = testerConf;
	}

	public String getTestLead() {
		return testLead;
	}

	public void setTestLead(String testLead) {
		this.testLead = testLead;
	}

	public List<VTestkTaskActor> getTestActroList() {
		return testActroList;
	}

	public void setTestActroList(List<VTestkTaskActor> testActroList) {
		this.testActroList = testActroList;
	}

	public List<TestFlowInfo> getTestFlowList() {
		return testFlowList;
	}

	public void setTestFlowList(List<TestFlowInfo> testFlowList) {
		this.testFlowList = testFlowList;
	}



	public String getRelaTaskName() {
		return relaTaskName;
	}

	public void setRelaTaskName(String realTaskName) {
		this.relaTaskName = realTaskName;
	}



	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getTestFlowStr() {
		return testFlowStr;
	}

	public void setTestFlowStr(String testFlowStr) {
		this.testFlowStr = testFlowStr;
	}

	public List<TaskUseActor> getUseActorPoList() {
		return useActorPoList;
	}

	public void setUseActorPoList(List<TaskUseActor> useActorPoList) {
		this.useActorPoList = useActorPoList;
	}

	public String getTestFlwKeyValueStr() {
		return testFlwKeyValueStr;
	}

	public void setTestFlwKeyValueStr(String testFlwKeyValueStr) {
		this.testFlwKeyValueStr = testFlwKeyValueStr;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getModuleIds() {
		return moduleIds;
	}

	public void setModuleIds(String moduleIds) {
		this.moduleIds = moduleIds;
	}

	public String getCaseReviewId() {
		return caseReviewId;
	}

	public void setCaseReviewId(String caseReviewId) {
		this.caseReviewId = caseReviewId;
	}

	public String getCaseReviewer() {
		return caseReviewer;
	}

	public void setCaseReviewer(String caseReviewer) {
		this.caseReviewer = caseReviewer;
	}

	public String getInitFlowStr() {
		return initFlowStr;
	}

	public void setInitFlowStr(String initFlowStr) {
		this.initFlowStr = initFlowStr;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public int getTestPhase() {
		return testPhase;
	}

	public void setTestPhase(int testPhase) {
		this.testPhase = testPhase;
	}

	public String getSelStrName() {
		return SelStrName;
	}

	public void setSelStrName(String selStrName) {
		SelStrName = selStrName;
	}

	public String getChgUserIds() {
		return chgUserIds;
	}

	public void setChgUserIds(String chgUserIds) {
		this.chgUserIds = chgUserIds;
	}

	public SoftwareVersion getSoftVer() {
		return softVer;
	}

	public void setSoftVer(SoftwareVersion softVer) {
		this.softVer = softVer;
	}

	public List<BugHandHistory> getHistoryAdjustList() {
		return historyAdjustList;
	}

	public void setHistoryAdjustList(List<BugHandHistory> historyAdjustList) {
		this.historyAdjustList = historyAdjustList;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getComeFrom() {
		return comeFrom;
	}

	public void setComeFrom(String comeFrom) {
		this.comeFrom = comeFrom;
	}

	public String getProRelaPersonId() {
		return proRelaPersonId;
	}

	public void setProRelaPersonId(String proRelaPersonId) {
		this.proRelaPersonId = proRelaPersonId;
	}

	public String getProRelaPerson() {
		return proRelaPerson;
	}

	public void setProRelaPerson(String proRelaPerson) {
		this.proRelaPerson = proRelaPerson;
	}


}
