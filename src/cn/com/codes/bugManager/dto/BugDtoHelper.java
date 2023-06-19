package cn.com.codes.bugManager.dto;

import cn.com.codes.object.BugFreq;
import cn.com.codes.object.BugGrade;
import cn.com.codes.object.BugOpotunity;
import cn.com.codes.object.BugPri;
import cn.com.codes.object.BugSource;
import cn.com.codes.object.BugStateInfo;
import cn.com.codes.object.BugType;
import cn.com.codes.object.GeneCause;
import cn.com.codes.object.ImportPhase;
import cn.com.codes.object.OccurPlant;
import cn.com.codes.object.SoftwareVersion;
import cn.com.codes.object.User;

public class BugDtoHelper {

	public String testSelStr;
	public String devStr;
	public String assignSelStr;
	public String analySelStr;
	public String interCesSelStr;
	
	public String devChkIdSelStr;
	public String testChkIdSelStr;
	public String testLdIdSelStr;
	public String verSelStr;
	
	public String querySelStr;
	public String typeSelStr;
	public String gradeSelStr;
	public String freqSelStr;
	public String occaSelStr;
	public String geCaseSelStr;
	public String sourceSelStr;
	public String plantFormSelStr;
	public String genePhaseSelStr;
	public String priSelStr;
	public String roleInTask ;
	public String testFlow;
	public int queryCount;
	public int relCaseSwitch;
	public BugFreq bugFreq;//频率
	public BugGrade bugGrade;//等级
	public BugOpotunity bugOpotunity;//时机
	public BugPri bugPri;//优先级
	public ImportPhase importPhase ;//引及阶段
	public BugType bugType;//BUG类型
	public BugSource bugSource;//bug来源
	public OccurPlant occurPlant;//发生平台 
	public BugStateInfo stateInfo;//状态
	public GeneCause geneCause ;//引入原因
	public SoftwareVersion reptVersion;
	public SoftwareVersion currVersion;
	public User currHander ;//当前处理人
	public User devOwner ;//所属开发人员
	public User testOwner ; //所属测试人员，
	public User analysOwner;
	public User assinOwner;
	public User intecesOwner;
	public User author;
	public  String staFlwMemStr;;
	public String  processLog;
	
	public String getTestSelStr() {
		return testSelStr;
	}
	public void setTestSelStr(String testSelStr) {
		this.testSelStr = testSelStr;
	}
	public String getDevStr() {
		return devStr;
	}
	public void setDevStr(String devStr) {
		this.devStr = devStr;
	}
	public String getAssignSelStr() {
		return assignSelStr;
	}
	public void setAssignSelStr(String assignSelStr) {
		this.assignSelStr = assignSelStr;
	}
	public String getAnalySelStr() {
		return analySelStr;
	}
	public void setAnalySelStr(String analySelStr) {
		this.analySelStr = analySelStr;
	}
	public String getInterCesSelStr() {
		return interCesSelStr;
	}
	public void setInterCesSelStr(String interCesSelStr) {
		this.interCesSelStr = interCesSelStr;
	}
	public String getQuerySelStr() {
		return querySelStr;
	}
	public void setQuerySelStr(String querySelStr) {
		this.querySelStr = querySelStr;
	}
	public String getTypeSelStr() {
		return typeSelStr;
	}
	public void setTypeSelStr(String typeSelStr) {
		this.typeSelStr = typeSelStr;
	}
	public String getGradeSelStr() {
		return gradeSelStr;
	}
	public void setGradeSelStr(String gradeSelStr) {
		this.gradeSelStr = gradeSelStr;
	}
	public String getFreqSelStr() {
		return freqSelStr;
	}
	public void setFreqSelStr(String freqSelStr) {
		this.freqSelStr = freqSelStr;
	}
	public String getOccaSelStr() {
		return occaSelStr;
	}
	public void setOccaSelStr(String occaSelStr) {
		this.occaSelStr = occaSelStr;
	}
	public String getGeCaseSelStr() {
		return geCaseSelStr;
	}
	public void setGeCaseSelStr(String geCaseSelStr) {
		this.geCaseSelStr = geCaseSelStr;
	}
	public String getSourceSelStr() {
		return sourceSelStr;
	}
	public void setSourceSelStr(String sourceSelStr) {
		this.sourceSelStr = sourceSelStr;
	}
	public String getPlantFormSelStr() {
		return plantFormSelStr;
	}
	public void setPlantFormSelStr(String plantFormSelStr) {
		this.plantFormSelStr = plantFormSelStr;
	}
	public String getGenePhaseSelStr() {
		return genePhaseSelStr;
	}
	public void setGenePhaseSelStr(String genePhaseSelStr) {
		this.genePhaseSelStr = genePhaseSelStr;
	}
	public String getPriSelStr() {
		return priSelStr;
	}
	public void setPriSelStr(String priSelStr) {
		this.priSelStr = priSelStr;
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
	public int getQueryCount() {
		return queryCount;
	}
	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}
	public int getRelCaseSwitch() {
		return relCaseSwitch;
	}
	public void setRelCaseSwitch(int relCaseSwitch) {
		this.relCaseSwitch = relCaseSwitch;
	}
	public BugFreq getBugFreq() {
		return bugFreq;
	}
	public void setBugFreq(BugFreq bugFreq) {
		this.bugFreq = bugFreq;
	}
	public BugGrade getBugGrade() {
		return bugGrade;
	}
	public void setBugGrade(BugGrade bugGrade) {
		this.bugGrade = bugGrade;
	}
	public BugOpotunity getBugOpotunity() {
		return bugOpotunity;
	}
	public void setBugOpotunity(BugOpotunity bugOpotunity) {
		this.bugOpotunity = bugOpotunity;
	}
	public BugPri getBugPri() {
		return bugPri;
	}
	public void setBugPri(BugPri bugPri) {
		this.bugPri = bugPri;
	}
	public ImportPhase getImportPhase() {
		return importPhase;
	}
	public void setImportPhase(ImportPhase importPhase) {
		this.importPhase = importPhase;
	}
	public BugType getBugType() {
		return bugType;
	}
	public void setBugType(BugType bugType) {
		this.bugType = bugType;
	}
	public BugSource getBugSource() {
		return bugSource;
	}
	public void setBugSource(BugSource bugSource) {
		this.bugSource = bugSource;
	}
	public OccurPlant getOccurPlant() {
		return occurPlant;
	}
	public void setOccurPlant(OccurPlant occurPlant) {
		this.occurPlant = occurPlant;
	}
	public BugStateInfo getStateInfo() {
		return stateInfo;
	}
	public void setStateInfo(BugStateInfo stateInfo) {
		this.stateInfo = stateInfo;
	}
	public GeneCause getGeneCause() {
		return geneCause;
	}
	public void setGeneCause(GeneCause geneCause) {
		this.geneCause = geneCause;
	}
	public User getCurrHander() {
		return currHander;
	}
	public void setCurrHander(User currHander) {
		this.currHander = currHander;
	}
	public User getDevOwner() {
		return devOwner;
	}
	public void setDevOwner(User devOwner) {
		this.devOwner = devOwner;
	}
	public User getTestOwner() {
		return testOwner;
	}
	public void setTestOwner(User testOwner) {
		this.testOwner = testOwner;
	}
	public User getAnalysOwner() {
		return analysOwner;
	}
	public void setAnalysOwner(User analysOwner) {
		this.analysOwner = analysOwner;
	}
	public User getAssinOwner() {
		return assinOwner;
	}
	public void setAssinOwner(User assinOwner) {
		this.assinOwner = assinOwner;
	}
	public User getIntecesOwner() {
		return intecesOwner;
	}
	public void setIntecesOwner(User intecesOwner) {
		this.intecesOwner = intecesOwner;
	}
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	public String getDevChkIdSelStr() {
		return devChkIdSelStr;
	}
	public void setDevChkIdSelStr(String devChkIdSelStr) {
		this.devChkIdSelStr = devChkIdSelStr;
	}
	public String getTestChkIdSelStr() {
		return testChkIdSelStr;
	}
	public void setTestChkIdSelStr(String testChkIdSelStr) {
		this.testChkIdSelStr = testChkIdSelStr;
	}
	public String getTestLdIdSelStr() {
		return testLdIdSelStr;
	}
	public void setTestLdIdSelStr(String testLdIdSelStr) {
		this.testLdIdSelStr = testLdIdSelStr;
	}
	public String getStaFlwMemStr() {
		return staFlwMemStr;
	}
	public void setStaFlwMemStr(String staFlwMemStr) {
		this.staFlwMemStr = staFlwMemStr;
	}
	public String getProcessLog() {
		return processLog;
	}
	public void setProcessLog(String processLog) {
		this.processLog = processLog;
	}
	public SoftwareVersion getReptVersion() {
		return reptVersion;
	}
	public void setReptVersion(SoftwareVersion reptVersion) {
		this.reptVersion = reptVersion;
	}
	public String getVerSelStr() {
		if(verSelStr==null){
			return "";
		}
		return verSelStr;
	}
	public void setVerSelStr(String verSelStr) {
		this.verSelStr = verSelStr;
	}
	public SoftwareVersion getCurrVersion() {
		return currVersion;
	}
	public void setCurrVersion(SoftwareVersion currVersion) {
		this.currVersion = currVersion;
	}

	//重构前端时，这里加的对像拷贝 
	public BugAddinitDto createAddInitDto() {
		BugAddinitDto  bugAddinitDto= new BugAddinitDto();
		bugAddinitDto.analySelStr = this.analySelStr;
		bugAddinitDto.assignSelStr = this.assignSelStr;
		bugAddinitDto.devStr = this.devStr;
		bugAddinitDto.freqSelStr = this.freqSelStr;
		bugAddinitDto.geCaseSelStr = this.geCaseSelStr;
		bugAddinitDto.genePhaseSelStr = this.genePhaseSelStr;
		bugAddinitDto.gradeSelStr = this.gradeSelStr;
		bugAddinitDto.interCesSelStr = this.interCesSelStr;
		bugAddinitDto.plantFormSelStr = this.plantFormSelStr;
		bugAddinitDto.priSelStr = this.priSelStr;
		bugAddinitDto.roleInTask = this.roleInTask;
		bugAddinitDto.sourceSelStr = this.sourceSelStr;
		bugAddinitDto.testFlow = this.testFlow;
		bugAddinitDto.relCaseSwitch = this.relCaseSwitch;
		bugAddinitDto.typeSelStr = this.typeSelStr;
		bugAddinitDto.testSelStr = this.testSelStr;
		bugAddinitDto.verSelStr = this.verSelStr;
		bugAddinitDto.occaSelStr = this.occaSelStr;
		return bugAddinitDto;
	}
}
