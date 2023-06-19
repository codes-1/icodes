package cn.com.codes.object;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.transmission.JsonInterface;
import cn.com.codes.object.TaskUseActor;
import cn.com.codes.object.TestFlowInfo;

public class TestTaskDetail implements JsonInterface, Serializable {

	private String taskId;
	private Integer testPhase;
	private String currentVersion;
	private String relaTaskId;
	private Integer testSeq;
	private Integer realPhase =0;
	private Integer reltCaseFlag =0;
	// 大刚状态
	private Integer outlineState;
	// 升级标志
	private Integer upgradeFlag;
	private Date insdate;
	private Date upddate;
	// 项目规模（代码行数）
	private Double pjsize;
	private String companyId;
	// 自定义用例字段标识
	private Integer customCase =0;
	// 自定义BUG字段标识
	private Integer customBug =0;
	private String createId;

	// 测试任务状态
	private Integer testTaskState =3;
	// 任务状态
	private Integer taskState;

	// bug逾期不改时Mail标记
	private Integer mailOverdueBug =0;
	// 有需要修改的问题时Mail标记
	private Integer mailDevFix=0;
	// 建立测试版本时Mail标记
	private Integer mailNewVer =0;
	// 有需要仲裁的问题时Mail标记
	private Integer mailVerdict=0;
	//汇报测试状况
	private Integer mailReport = 0;
	
	//测试流程
	Set<TestFlowInfo> testFlow ;
	//各环节人员
	Set<TaskUseActor> useActor ;
	//修改前
	Set<TestFlowInfo> oldTestFlow ;
	//修改前
	Set<TaskUseActor> oldUseActor ;

	//所属项目ID
	private String projectId ;
	public TestTaskDetail() {
	}

	public TestTaskDetail(String taskId) {
		this.taskId = taskId;
	}
	public TestTaskDetail(String taskId, String projectId) {
		this.taskId = taskId;
		this.projectId = projectId;
	}
	public TestTaskDetail(Integer outlineState,Integer testPhase) {
		this.outlineState = outlineState;
		this.testPhase = testPhase;
	}
	public TestTaskDetail(String taskId,Integer testTaskState) {
		this.taskId = taskId;
		this.testTaskState = testTaskState;
	}
	
	public TestTaskDetail(Integer testPhase,String currentVersion) {
		this.testPhase = testPhase;
		this.currentVersion = currentVersion;
	}

		
	public TestTaskDetail(Integer outlineState,Integer testPhase,String currentVersion) {
		this.outlineState = outlineState;
		this.testPhase = testPhase;
		this.currentVersion = currentVersion;
	}
	public TestTaskDetail(String taskId,Integer testPhase,String currentVersion) {
		this.taskId = taskId;
		this.testPhase = testPhase;
		this.currentVersion = currentVersion;
	}
	public TestTaskDetail(Integer outlineState,Integer testPhase,String currentVersion,Integer testSeq) {
		this.outlineState = outlineState;
		this.testPhase = testPhase;
		this.currentVersion = currentVersion;
		this.testSeq = testSeq;
	}
	public TestTaskDetail(String taskId,  String projectId,Integer mailDevFix,Integer mailVerdict,Integer mailOverdueBug,Integer mailReport){
		this.mailDevFix = mailDevFix;
		this.mailReport = mailReport;
		this.mailVerdict = mailVerdict;
		this.mailOverdueBug = mailOverdueBug;
		this.taskId = taskId;
		this.projectId = projectId;
	}	
	public TestTaskDetail(Integer outlineState,Integer testPhase,String currentVersion,Integer testSeq,Integer reltCaseFlag) {
		this.outlineState = outlineState;
		this.testPhase = testPhase;
		this.currentVersion = currentVersion;
		this.testSeq = testSeq;
		this.reltCaseFlag = reltCaseFlag;
	}	

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Integer getTestPhase() {
		return testPhase;
	}

	public void setTestPhase(Integer testPhase) {
		this.testPhase = testPhase;
	}

	public String getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}

	public String getRelaTaskId() {
		return relaTaskId;
	}

	public void setRelaTaskId(String relaTaskId) {
		this.relaTaskId = relaTaskId;
	}

	public Integer getRealPhase() {
		return realPhase;
	}

	public void setRealPhase(Integer realPhase) {
		this.realPhase = realPhase;
	}

	public Integer getOutlineState() {
		return outlineState;
	}

	public void setOutlineState(Integer outlineState) {
		this.outlineState = outlineState;
	}

	public Integer getUpgradeFlag() {
		return upgradeFlag;
	}

	public void setUpgradeFlag(Integer upgradeFlag) {
		this.upgradeFlag = upgradeFlag;
	}

	public Date getInsdate() {
		return insdate;
	}

	public void setInsdate(Date insdate) {
		this.insdate = insdate;
	}

	public Date getUpddate() {
		return upddate;
	}

	public void setUpddate(Date upddate) {
		this.upddate = upddate;
	}

	public Double getPjsize() {
		return pjsize;
	}

	public void setPjsize(Double pjsize) {
		this.pjsize = pjsize;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Integer getCustomCase() {
		return customCase;
	}

	public void setCustomCase(Integer customCase) {
		this.customCase = customCase;
	}

	public Integer getCustomBug() {
		return customBug;
	}

	public void setCustomBug(Integer customBug) {
		this.customBug = customBug;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public Integer getReltCaseFlag() {
		return reltCaseFlag;
	}

	public void setReltCaseFlag(Integer reltCaseFlag) {
		this.reltCaseFlag = reltCaseFlag;
	}

	public Integer getTestTaskState() {
		return testTaskState;
	}

	public void setTestTaskState(Integer testTaskState) {
		this.testTaskState = testTaskState;
	}

	public Integer getTaskState() {
		return taskState;
	}

	public void setTaskState(Integer taskState) {
		this.taskState = taskState;
	}

	
	public Set<TestFlowInfo> getTestFlow() {
		return testFlow;
	}

	public void setTestFlow(Set<TestFlowInfo> testFlow) {
		this.testFlow = testFlow;
	}

	public Set<TaskUseActor> getUseActor() {
		return useActor;
	}

	public void setUseActor(Set<TaskUseActor> useActor) {
		this.useActor = useActor;
	}
	
	public Integer getMailReport() {
		return mailReport;
	}

	public void setMailReport(Integer mailReport) {
		this.mailReport = mailReport;
	}
	
	public void toString(StringBuffer bf){
		
	}

	public Integer getMailOverdueBug() {
		return mailOverdueBug;
	}

	public void setMailOverdueBug(Integer mailOverdueBug) {
		this.mailOverdueBug = mailOverdueBug;
	}

	public Integer getMailDevFix() {
		return mailDevFix;
	}

	public void setMailDevFix(Integer mailDevFix) {
		this.mailDevFix = mailDevFix;
	}

	public Integer getMailNewVer() {
		return mailNewVer;
	}

	public void setMailNewVer(Integer mailNewVer) {
		this.mailNewVer = mailNewVer;
	}

	public Integer getMailVerdict() {
		return mailVerdict;
	}

	public void setMailVerdict(Integer mailVerdict) {
		this.mailVerdict = mailVerdict;
	}
	
	public Set<TestFlowInfo> getOldTestFlow() {
		return oldTestFlow;
	}

	public void setOldTestFlow(Set<TestFlowInfo> oldTestFlow) {
		this.oldTestFlow = oldTestFlow;
	}

	public Set<TaskUseActor> getOldUseActor() {
		return oldUseActor;
	}

	public void setOldUseActor(Set<TaskUseActor> oldUseActor) {
		this.oldUseActor = oldUseActor;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public Integer getTestSeq() {
		return testSeq;
	}

	public void setTestSeq(Integer testSeq) {
		this.testSeq = testSeq;
	}
	public String toStrList(){
		StringBuffer sbf = new StringBuffer();
		sbf.append("{");
		sbf.append("id:'");
		sbf.append(getTaskId().toString());
		sbf.append("',data: [0,'','");
		sbf.append(testPhase == null ? "" : testPhase );
		sbf.append("'");
		sbf.append(currentVersion == null ? "" : currentVersion );
		sbf.append("'");
		sbf.append(relaTaskId == null ? "" : relaTaskId );
		sbf.append("'");
		sbf.append(realPhase == null ? "" : realPhase );
		sbf.append("'");
		sbf.append(reltCaseFlag == null ? "" : reltCaseFlag );
		sbf.append("'");
		sbf.append(outlineState == null ? "" : outlineState );
		sbf.append("'");
		sbf.append(upgradeFlag == null ? "" : upgradeFlag );
		sbf.append("'");
		sbf.append(insdate == null ? "" : StringUtils.formatShortDate(insdate ));
		sbf.append("'");
		sbf.append(upddate == null ? "" : StringUtils.formatShortDate(upddate ));
		sbf.append("'");
		sbf.append(pjsize == null ? "" : pjsize );
		sbf.append("'");
		sbf.append(companyId == null ? "" : companyId );
		sbf.append("'");
		sbf.append(customCase == null ? "" : customCase );
		sbf.append("'");
		sbf.append(customBug == null ? "" : customBug );
		sbf.append("'");
		sbf.append(createId == null ? "" : createId );
		sbf.append("'");
		sbf.append(testTaskState == null ? "" : testTaskState );
		sbf.append("'");
		sbf.append(taskState == null ? "" : taskState );
		sbf.append("'");
		sbf.append(mailOverdueBug == null ? "" : mailOverdueBug );
		sbf.append("'");
		sbf.append(mailDevFix == null ? "" : mailDevFix );
		sbf.append("'");
		sbf.append(mailNewVer == null ? "" : mailNewVer );
		sbf.append("'");
		sbf.append(mailVerdict == null ? "" : mailVerdict );
		sbf.append("'");
		sbf.append("]");
		sbf.append("}");
		return sbf.toString();
	}
	
	public String toStrUpdateInit(){
	    StringBuffer sbf = new StringBuffer();
	    sbf.append(getTaskId().toString());
	    sbf.append("^");
		sbf.append("testPhase=").append(testPhase == null ? "" : testPhase);
		sbf.append("^");
		sbf.append("currentVersion=").append(currentVersion == null ? "" : currentVersion);
		sbf.append("^");
		sbf.append("relaTaskId=").append(relaTaskId == null ? "" : relaTaskId);
		sbf.append("^");
		sbf.append("realPhase=").append(realPhase == null ? "" : realPhase);
		sbf.append("^");
		sbf.append("reltTestCaseFlag=").append(reltCaseFlag == null ? "" : reltCaseFlag);
		sbf.append("^");
		sbf.append("outlineState=").append(outlineState == null ? "" : outlineState);
		sbf.append("^");
		sbf.append("upgradeFlag=").append(upgradeFlag == null ? "" : upgradeFlag);
		sbf.append("^");
		sbf.append("insdate=").append(insdate == null ? "" : StringUtils.formatShortDate(insdate));
		sbf.append("^");
		sbf.append("upddate=").append(upddate == null ? "" : StringUtils.formatShortDate(upddate));
		sbf.append("^");
		sbf.append("pjsize=").append(pjsize == null ? "" : pjsize);
		sbf.append("^");
		sbf.append("companyId=").append(companyId == null ? "" : companyId);
		sbf.append("^");
		sbf.append("customCase=").append(customCase == null ? "" : customCase);
		sbf.append("^");
		sbf.append("customBug=").append(customBug == null ? "" : customBug);
		sbf.append("^");
		sbf.append("createId=").append(createId == null ? "" : createId);
		sbf.append("^");
		sbf.append("testTaskState=").append(testTaskState == null ? "" : testTaskState);
		sbf.append("^");
		sbf.append("taskState=").append(taskState == null ? "" : taskState);
		sbf.append("^");
		sbf.append("mailBug=").append(mailOverdueBug == null ? "" : mailOverdueBug);
		sbf.append("^");
		sbf.append("mailQuestion=").append(mailDevFix == null ? "" : mailDevFix);
		sbf.append("^");
		sbf.append("mailEdition=").append(mailNewVer == null ? "" : mailNewVer);
		sbf.append("^");
		sbf.append("mailConfirm=").append(mailVerdict == null ? "" : mailVerdict);
	    return sbf.toString();
	}
	
	public String toStrUpdateRest(){
		StringBuffer sbf = new StringBuffer();
	    sbf.append(getTaskId().toString());
	    sbf.append("0,,");
		sbf.append(testPhase == null ? "" : testPhase );
		sbf.append("'");
		sbf.append(currentVersion == null ? "" : currentVersion );
		sbf.append("'");
		sbf.append(relaTaskId == null ? "" : relaTaskId );
		sbf.append("'");
		sbf.append(realPhase == null ? "" : realPhase );
		sbf.append("'");
		sbf.append(reltCaseFlag == null ? "" : reltCaseFlag );
		sbf.append("'");
		sbf.append(outlineState == null ? "" : outlineState );
		sbf.append("'");
		sbf.append(upgradeFlag == null ? "" : upgradeFlag );
		sbf.append("'");
		sbf.append(insdate == null ? "" : StringUtils.formatShortDate(insdate ));
		sbf.append("'");
		sbf.append(upddate == null ? "" : StringUtils.formatShortDate(upddate ));
		sbf.append("'");
		sbf.append(pjsize == null ? "" : pjsize );
		sbf.append("'");
		sbf.append(companyId == null ? "" : companyId );
		sbf.append("'");
		sbf.append(customCase == null ? "" : customCase );
		sbf.append("'");
		sbf.append(customBug == null ? "" : customBug );
		sbf.append("'");
		sbf.append(createId == null ? "" : createId );
		sbf.append("'");
		sbf.append(testTaskState == null ? "" : testTaskState );
		sbf.append("'");
		sbf.append(taskState == null ? "" : taskState );
		sbf.append("'");
		sbf.append(mailOverdueBug == null ? "" : mailOverdueBug );
		sbf.append("'");
		sbf.append(mailDevFix == null ? "" : mailDevFix );
		sbf.append("'");
		sbf.append(mailNewVer == null ? "" : mailNewVer );
		sbf.append("'");
		sbf.append(mailVerdict == null ? "" : mailVerdict );
	    return sbf.toString();
	}

}
