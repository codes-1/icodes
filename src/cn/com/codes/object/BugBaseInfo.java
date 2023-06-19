package cn.com.codes.object;

import java.util.Date;
import java.util.Set;
import java.util.regex.Pattern;

import cn.com.codes.bugManager.blh.BugFlowConst;
import cn.com.codes.bugManager.dto.BugDtoHelper;
import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.transmission.JsonInterface;
import cn.com.codes.object.BugBaseInfo;
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
import cn.com.codes.object.TestCaseInfo;
import cn.com.codes.object.User;

public class BugBaseInfo  implements  JsonInterface{

	private static final String repStr = "＜script type＝”text/javascript” src＝”http://www.gwdang.com/get.js?f＝/js/gwdang_extension.js”＞＜/script＞＜script type＝”text/javascript” src＝”http://www.gwdang.com/get.js?f＝/js/gwdang_extension.js”＞＜/script＞";
	private Long bugId;
	private String taskId;
	private Integer testPhase;
	private String bugDesc;//BUG描述
	private String reProTxt;//BUG再现纯文本描述
	private String reProStep ;//再现过程富文本形式
	private Long bugReptVer;//发现版本
	private Long verifyVer;//校验版本
	private Date reptDate;//报告日期
	private Integer msgFlag;//交流标识 
	private Integer relaCaseFlag;//关联用例标识
	private Date bugAntimodDate; //预计修改日期
	private String reproPersent; //再现比例
	
	private Float planAmendHour;//预计修改时间
	private Date currHandlDate;//当前状态处理日期
	private String currRemark;//备注
	private Integer currFlowCd;//当前流程编码
	private Integer nextFlowCd;//下一流程编码

	private String devOwnerId;
	private String testOwnerId;
	private String analyseOwnerId;
	private String assinOwnerId ;
	private String intercessOwnerId;
	private String currHandlerId;//当前处理人ID
	private String bugReptId;//报告人ID
	private String testName;
	private String devName;
	
	//这个辅助类是后来抽出来的把上面注掉的JSON性性封到dtoHelper，
	//为了不影原有的调且，这都做如下处理,多线程时这会出问题的
	//所以相关set的调用有怪，原因就这
	private BugDtoHelper dtoHelper;
	private Long moduleId;//模块ID
	private Long bugTypeId;//类型ID
	private Long bugGradeId;//等级ID
	private Long bugFreqId;//发生频率ID
	private Long bugOccaId;//发生时机ID
	private Long geneCauseId;//引入原因ID
	private Long sourceId;//来源ID
	private Long platformId;//发生平台ID
	private Long genePhaseId;//引入阶段ID
	private Long priId; //优先级ID
	private Integer currStateId;//当前状态ID
	private Integer initState;//当前操作人操作前的状态
	private String modelName;
	private String stateName;
	private String attachUrl;
	private Long withRepteId;//与之重复的BUG  id
	private Set<TestCaseInfo> testCases;
	private Long fixVer ;
	private String versionLable;
	private String taskName;
	
	private String nextOwnerId;
	
	private Date fixDate;
	
	private String moduleNum;
	
	private String chargeOwner;
	private String chargeOwnerName;
	public BugBaseInfo() {
	}
	public BugBaseInfo(Long bugId){
		this.bugId = bugId;
	}
	public BugBaseInfo(Long bugId,String bugDesc,Integer currStateId,String attachUrl,String taskId){
		this.bugId = bugId;
	    this.bugDesc = bugDesc;
	    this.currStateId = currStateId;
	    this.attachUrl = attachUrl;
	    this.taskId = taskId;
	}
	public BugBaseInfo(Long bugId,String bugDesc,String testOwnerId,Integer currStateId,
			String taskId,Date reptDate,Long bugReptVer,Date currHandlDate,String attachUrl){
		this.bugId = bugId;
	    this.bugDesc = bugDesc;
	    this.testOwnerId = testOwnerId;
	    this.taskId = taskId;
	    this.currStateId = currStateId;
	    this.reptDate = reptDate;
	    this.bugReptVer = bugReptVer;
	    this.currHandlDate = currHandlDate;
	    this.attachUrl = attachUrl;
	}	
	public BugBaseInfo(Long bugId,Integer relaCaseFlag,Long bugReptVer){
		this.bugId = bugId;
		this.relaCaseFlag = relaCaseFlag;
		this.bugReptVer = bugReptVer;
	}
	public BugBaseInfo(Long bugId, Long moduleId){
		this.bugId = bugId;
		this.moduleId = moduleId;
	}
	public BugBaseInfo(Long bugId,String devOwnerId,String testOwnerId,String analyseOwnerId,
			String assinOwnerId,String intercessOwnerId,Integer msgFlag) {
		this.bugId = bugId;
		this.msgFlag = msgFlag;
		this.devOwnerId = devOwnerId;
		this.testOwnerId = testOwnerId;
		this.analyseOwnerId = analyseOwnerId;
		this.assinOwnerId = assinOwnerId;
		this.intercessOwnerId = intercessOwnerId;
	}
	public BugBaseInfo(Long bugId, Long moduleId, Integer currFlowCd) {
		this.bugId = bugId;
		this.moduleId = moduleId;
		this.currFlowCd = currFlowCd;
	}
	public BugBaseInfo(Long bugId,String bugDesc,Long bugGradeId,Long bugFreqId,
			Long bugOccaId,Long bugTypeId,Long priId,String devOwnerId,Date reptDate,
			Integer msgFlag,Integer relaCaseFlag,Long moduleId,String testOwnerId,
			Integer currFlowCd,String currHandlerId,Integer currStateId,Integer nextFlowCd,
			String bugReptId){
		this.bugId = bugId;//BUG ID
		this.bugDesc = bugDesc;//BUG 描述
		this.bugGradeId = bugGradeId;//BUG 等级ID
		this.bugFreqId = bugFreqId;//BUG 频率ID
		this.bugOccaId = bugOccaId;//时机ID
		this.bugTypeId = bugTypeId;//类型ID
		this.priId = priId;//优先级ID
		this.devOwnerId = devOwnerId;//引入原因ID
		this.reptDate = reptDate;//报告日期
		this.msgFlag = msgFlag;
		this.relaCaseFlag = relaCaseFlag;
		this.moduleId = moduleId;
		this.testOwnerId = testOwnerId;
		this.currFlowCd = currFlowCd;
		this.currHandlerId = currHandlerId;
		this.currStateId = currStateId;
		this.nextFlowCd = nextFlowCd;
		this.bugReptId = bugReptId;
	}	
	
	public BugBaseInfo(Long bugId,String bugDesc,Long bugGradeId,Long bugFreqId,
			Long bugOccaId,Long bugTypeId,Long priId,String devOwnerId,Date reptDate,
			Integer msgFlag,Integer relaCaseFlag,Long moduleId,String testOwnerId,
			Integer currFlowCd,String currHandlerId,Integer currStateId,Integer nextFlowCd,
			String bugReptId,String taskId){
		this.bugId = bugId;//BUG ID
		this.bugDesc = bugDesc;//BUG 描述
		this.bugGradeId = bugGradeId;//BUG 等级ID
		this.bugFreqId = bugFreqId;//BUG 频率ID
		this.bugOccaId = bugOccaId;//时机ID
		this.bugTypeId = bugTypeId;//类型ID
		this.priId = priId;//优先级ID
		this.devOwnerId = devOwnerId;//引入原因ID
		this.reptDate = reptDate;//报告日期
		this.msgFlag = msgFlag;
		this.relaCaseFlag = relaCaseFlag;
		this.moduleId = moduleId;
		this.testOwnerId = testOwnerId;
		this.currFlowCd = currFlowCd;
		this.currHandlerId = currHandlerId;
		this.currStateId = currStateId;
		this.nextFlowCd = nextFlowCd;
		this.bugReptId = bugReptId;
		this.taskId = taskId;
	}	
	
	public String getAttachUrl() {
		return attachUrl;
	}
	public String getAttachUrl2() {
		if(attachUrl==null||"".equals(attachUrl.trim())){
			return "";
		}
		return attachUrl.substring(attachUrl.indexOf("_")+1);
	}
	public void setAttachUrl(String attachUrl) {
		this.attachUrl = attachUrl;
	}
	public Long getFixVer() {
		return fixVer;
	}
	public void setFixVer(Long fixVer) {
		this.fixVer = fixVer;
	}

	public void setDevName(String devName){
		this.devName = devName;
	}
	public String getDevName(){
		return this.devName;
	}
	public String gettestName(){
		return this.testName;
	}
	public void  settestName(String testName){
		this.testName = testName;
	}
	public BugDtoHelper getDtoHelper(){
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		return this.dtoHelper;
	}
	public void  getDtoHelper(BugDtoHelper dtoHelper){
		this.dtoHelper = dtoHelper;
	}
	public int getQueryCount(){
		return getDtoHelper().queryCount;
	}
	public void setQueryCount(int queryCount){
		//这个辅助类是后来抽出来的，为了不影原有的调且，这都做如下处理,多线程时这会出问题的
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.queryCount = queryCount;
	}
	public String getQuerySelStr(){
		return getDtoHelper().querySelStr;
	}
	public void setQuerySelStr(String querySelStr){
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.querySelStr = querySelStr;
	}
	public Long getWithRepteId(){
		
		return this.withRepteId;
	}
	public void setWithRepteId(Long withRepteId){
		this.withRepteId = withRepteId;
	}
	public String getStateName(){
		return this.stateName;
	}
	public void  setStateName(String stateName){
		this.stateName = stateName;
	}

	public String getTypeSelStr() {
		return getDtoHelper().typeSelStr;
	}

	public void setTypeSelStr(String typeSelStr) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.typeSelStr = typeSelStr;
	}

	public String getGradeSelStr() {
		return getDtoHelper().gradeSelStr;
	}

	public void setGradeSelStr(String gradeSelStr) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.gradeSelStr = gradeSelStr;
	}

	public String getFreqSelStr() {
		return getDtoHelper().freqSelStr;
	}

	public void setFreqSelStr(String freqSelStr) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.freqSelStr = freqSelStr;
	}

	public String getOccaSelStr() {
		return getDtoHelper().occaSelStr;
	}

	public void setOccaSelStr(String occaSelStr) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.occaSelStr = occaSelStr;
	}

	public String getGeCaseSelStr() {
		return getDtoHelper().geCaseSelStr;
	}

	public void setGeCaseSelStr(String geCaseSelStr) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.geCaseSelStr = geCaseSelStr;
	}

	public String getSourceSelStr() {
		return getDtoHelper().sourceSelStr;
	}

	public void setSourceSelStr(String sourceSelStr) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.sourceSelStr = sourceSelStr;
	}

	public String getPlantFormSelStr() {

		return getDtoHelper().plantFormSelStr;
	}

	public void setPlantFormSelStr(String plantFormSelStr) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.plantFormSelStr = plantFormSelStr;
	}

	public String getGenePhaseSelStr() {
		return getDtoHelper().genePhaseSelStr;
	}

	public void setGenePhaseSelStr(String genePhaseSelStr) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.genePhaseSelStr = genePhaseSelStr;
	}

	public String getPriSelStr() {
		return getDtoHelper().priSelStr;
	}

	public void setPriSelStr(String priSelStr) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.priSelStr = priSelStr;
	}

	public String getTestSelStr() {
		return getDtoHelper().testSelStr;
	}

	public void setTestSelStr(String testSelStr) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.testSelStr = testSelStr;
	}

	public String getDevStr() {
		return getDtoHelper().devStr;
	}

	public void setDevStr(String devStr) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.devStr = devStr;
	}

	public String getAssignSelStr() {
		return getDtoHelper().assignSelStr;
	}

	public void setAssignSelStr(String assignSelStr) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.assignSelStr = assignSelStr;
	}

	public String getAnalySelStr() {
		return getDtoHelper().analySelStr;
	}

	public void setAnalySelStr(String analySelStr) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.analySelStr = analySelStr;
	}
	public String getProcessLog() {
		if(this.dtoHelper==null){
			return null;
		}
		return this.dtoHelper.processLog;
	}
	public void setProcessLog(String processLog) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.processLog = processLog;
	}
	public String getInterCesSelStr() {
		return getDtoHelper().interCesSelStr;
	}

	public void setInterCesSelStr(String interCesSelStr) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.interCesSelStr = interCesSelStr;
	}
	public GeneCause getGeneCause(){
		return this.dtoHelper.geneCause;
	}

	public void  setGeneCause(GeneCause geneCause){
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.geneCause = geneCause;
	}
	public Integer getRelaCaseFlag(){
		return this.relaCaseFlag;
	}
	public BugStateInfo getStateInfo(){
		return this.dtoHelper.stateInfo;
	}
	public void setStateInfo(BugStateInfo stateInfo){
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.stateInfo = stateInfo;
	}
	public void setRelaCaseFlag(Integer relaCaseFlag){
		this.relaCaseFlag =relaCaseFlag;
	}
	
	public int getRelCaseSwitch() {
		
		return this.dtoHelper.relCaseSwitch;
	}

	public void setRelCaseSwitch(int relCaseSwitch) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.relCaseSwitch = relCaseSwitch;
	}
	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Integer getTestPhase() {
		return this.testPhase;
	}

	public void setTestPhase(Integer testPhase) {
		this.testPhase = testPhase;
	}


	public Long getSourceId() {
		return this.sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}
	public Long getPlatformId() {
		return this.platformId;
	}

	public void setPlatformId(Long platformId) {
		this.platformId = platformId;
	}
	public Long getBugId() {
		return bugId;
	}

	public void setBugId(Long bugId) {
		this.bugId = bugId;
	}


	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}
	public String getBugReptId() {
		return bugReptId;
	}

	public void setBugReptId(String bugReptId) {
		this.bugReptId = bugReptId;
	}

	public Long getBugReptVer() {
		return bugReptVer;
	}

	public void setBugReptVer(Long bugReptVer) {
		this.bugReptVer = bugReptVer;
	}



	public Date getReptDate() {
		return reptDate;
	}

	public void setReptDate(Date reptDate) {
		this.reptDate = reptDate;
	}

	public Integer getMsgFlag() {
		return msgFlag;
	}

	public void setMsgFlag(Integer msgFlag) {
		this.msgFlag = msgFlag;
	}

	public Date getBugAntimodDate() {
		return bugAntimodDate;
	}
	public Date getBugAntimodDate2() {
		return bugAntimodDate;
	}
	public void setBugAntimodDate(Date bugAntimodDate) {
		this.bugAntimodDate = bugAntimodDate;
	}

	public Long getPriId() {
		return priId;
	}

	public void setPriId(Long priId) {
		this.priId = priId;
	}

	public Long getGenePhaseId() {
		return genePhaseId;
	}

	public void setGenePhaseId(Long genePhaseId) {
		this.genePhaseId = genePhaseId;
	}



	public Float getPlanAmendHour() {
		return planAmendHour;
	}

	public void setPlanAmendHour(Float planAmendHour) {
		this.planAmendHour = planAmendHour;
	}

	public Long getGeneCauseId() {
		return geneCauseId;
	}

	public void setGeneCauseId(Long geneCauseId) {
		this.geneCauseId = geneCauseId;
	}

	public String getBugDesc() {
		return bugDesc;
	}

	public void setBugDesc(String bugDesc) {
		this.bugDesc = bugDesc;
	}

	public Integer getCurrStateId() {
		return currStateId;
	}

	public void setCurrStateId(Integer currStateId) {
		this.currStateId = currStateId;
	}

	public Date getCurrHandlDate() {
		return currHandlDate;
	}

	public void setCurrHandlDate(Date currHandlDate) {
		this.currHandlDate = currHandlDate;
	}

	public String getCurrRemark() {
		return currRemark;
	}

	public void setCurrRemark(String currRemark) {
		this.currRemark = currRemark;
	}

	public Integer getCurrFlowCd() {
		return currFlowCd;
	}

	public void setCurrFlowCd(Integer currFlowCd) {
		this.currFlowCd = currFlowCd;
	}



//	public Integer getTestSeq() {
//		return testSeq;
//	}
//
//	public void setTestSeq(Integer testSeq) {
//		this.testSeq = testSeq;
//	}
//
//	public Integer getInitSeq() {
//		return initSeq;
//	}
//
//	public void setInitSeq(Integer initSeq) {
//		this.initSeq = initSeq;
//	}

	public Integer getNextFlowCd() {
		return nextFlowCd;
	}

	public void setNextFlowCd(Integer nextFlowCd) {
		this.nextFlowCd = nextFlowCd;
	}


	public BugFreq getBugFreq() {
		return this.dtoHelper.bugFreq;
	}

	public void setBugFreq(BugFreq bugFreq) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.bugFreq = bugFreq;
	}

	public BugGrade getBugGrade() {
		return this.dtoHelper.bugGrade;
	}

	public void setBugGrade(BugGrade bugGrade) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.bugGrade = bugGrade;
	}

	public BugOpotunity getBugOpotunity() {

		return this.dtoHelper.bugOpotunity;
	}

	public void setBugOpotunity(BugOpotunity bugOpotunity) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.bugOpotunity = bugOpotunity;
	}

	public BugPri getBugPri() {
		return this.dtoHelper.bugPri;
	}

	public void setBugPri(BugPri bugPri) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.bugPri = bugPri;
	}

	public ImportPhase getImportPhase() {
		return this.dtoHelper.importPhase;
	}

	public void setImportPhase(ImportPhase importPhase) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.importPhase = importPhase;
	}

	public BugType getBugType() {
		return this.dtoHelper.bugType;
	}

	public void setBugType(BugType bugType) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.bugType = bugType;
	}

	public BugSource getBugSource() {
		return this.dtoHelper.bugSource;
	}

	public void setBugSource(BugSource bugSource) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.bugSource = bugSource;
	}

	public OccurPlant getOccurPlant() {
		return this.dtoHelper.occurPlant;
	}

	public void setOccurPlant(OccurPlant occurPlant) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.occurPlant = occurPlant;
	}

	public Long getBugTypeId() {
		return bugTypeId;
	}

	public void setBugTypeId(Long bugTypeId) {
		this.bugTypeId = bugTypeId;
	}

	public Long getBugGradeId() {
		return bugGradeId;
	}

	public void setBugGradeId(Long bugGradeId) {
		this.bugGradeId = bugGradeId;
	}

	public Long getBugFreqId() {
		return bugFreqId;
	}

	public void setBugFreqId(Long bugFreqId) {
		this.bugFreqId = bugFreqId;
	}

	public Long getBugOccaId() {
		return bugOccaId;
	}

	public void setBugOccaId(Long bugOccaId) {
		this.bugOccaId = bugOccaId;
	}
	public String getDevOwnerId() {
		return devOwnerId;
	}

	public void setDevOwnerId(String devOwnerId) {
		this.devOwnerId = devOwnerId;
	}

	public String getTestOwnerId() {
		return testOwnerId;
	}

	public void setTestOwnerId(String testOwnerId) {
		this.testOwnerId = testOwnerId;
	}

	public String getCurrHandlerId() {
		return currHandlerId;
	}

	public void setCurrHandlerId(String currHandlerId) {
		this.currHandlerId = currHandlerId;
	}

	public String getAnalyseOwnerId() {
		return analyseOwnerId;
	}

	public void setAnalyseOwnerId(String analyseOwnerId) {
		this.analyseOwnerId = analyseOwnerId;
	}

	public String getIntercessOwnerId() {
		return intercessOwnerId;
	}

	public void setIntercessOwnerId(String intercessOwnerId) {
		this.intercessOwnerId = intercessOwnerId;
	}
	

	public String getAssinOwnerId() {
		return assinOwnerId;
	}

	public void setAssinOwnerId(String assinOwnerId) {
		this.assinOwnerId = assinOwnerId;
	}

	
	public String getReProTxt() {
		return reProTxt;
	}

	public void setReProTxt(String reProTxt) {
		this.reProTxt = reProTxt;
	}

	public String getReProStep() {
		return reProStep;
	}
	
//	public String getReProStep2() {
//		if(reProStep!=null&&!"".endsWith(reProStep)){
//			  
//			int possion = reProStep.indexOf(repStr);
//			if(possion>0)
//				reProStep = reProStep.substring(0, possion);
//			//System.out.println(reProStep);
//			String reProStepTmp = reProStep.replaceAll("style＝\"", "style＝'");
//			reProStepTmp = reProStepTmp.trim();
//			reProStepTmp = reProStepTmp.replaceAll(";\"", ";'");
//			reProStepTmp = reProStepTmp.replaceAll("src＝\"", "src='");
//			reProStepTmp = reProStepTmp.replaceAll("alt＝\"\"", "alt＝''");
//			reProStepTmp = reProStepTmp.replaceAll(".JPG\"", ".JPG'");
//			reProStepTmp = reProStepTmp.replaceAll(".jpg\"", ".jpg'");
//			reProStepTmp = reProStepTmp.replaceAll(".PNG\"", ".PNG'");
//			reProStepTmp = reProStepTmp.replaceAll(".png\"", ".png'");
//			reProStepTmp = reProStepTmp.replaceAll(".GIF\"", ".GIF'");
//			reProStepTmp = reProStepTmp.replaceAll(".gif\"", ".gif'");
//			reProStepTmp = reProStepTmp.replaceAll("＞",">");
//			reProStepTmp = reProStepTmp.replaceAll("＜","<");
//			reProStepTmp = reProStepTmp.replaceAll("px\"", "px'");
//			reProStepTmp = reProStepTmp.replaceAll("px;\"", "px;'");
//			reProStepTmp = reProStepTmp.replaceAll("\"", "＂");
//			if(reProStepTmp.endsWith("\\")){
//				reProStepTmp = reProStepTmp.substring(0,reProStepTmp.length()-1);
//			}
//			if(reProStepTmp.endsWith("\\")){
//				reProStepTmp = reProStepTmp+"\"";
//			}
//			while(reProStepTmp.endsWith("＜br /＞")||reProStepTmp.trim().endsWith("&nbsp;")){
//				if(reProStepTmp.endsWith("＜br /＞")){
//					reProStepTmp =reProStepTmp.substring(0,reProStepTmp.lastIndexOf("＜br /＞")) ;
//				}
//				if(reProStepTmp.trim().endsWith("&nbsp;")){
//					reProStepTmp = reProStepTmp.trim();
//					reProStepTmp =reProStepTmp.substring(0,reProStepTmp.lastIndexOf("&nbsp;")) ;
//				}
//			}
//			return reProStepTmp;
//		}
//		return reProStep;
//	}
	
	public void setReProStep(String reProStep) {
		this.reProStep = reProStep;
//		if(this.reProStep!=null&&!"".equals(this.reProStep.trim())){
//			String reProStepTmp = this.reProStep.trim();
//			while(reProStepTmp.endsWith("＜br /＞")||reProStepTmp.trim().endsWith("&nbsp;")){
//				if(reProStepTmp.endsWith("＜br /＞")){
//					reProStepTmp =reProStepTmp.substring(0,reProStepTmp.lastIndexOf("＜br /＞")) ;
//				}
//				if(reProStepTmp.trim().endsWith("&nbsp;")){
//					reProStepTmp = reProStepTmp.trim();
//					reProStepTmp =reProStepTmp.substring(0,reProStepTmp.lastIndexOf("&nbsp;")) ;
//				}
//			}	
//			this.reProStep = reProStepTmp;
//		}

	}
	
	public String getReproPersent() {
		return reproPersent;
	}

	public void setReproPersent(String reproPersent) {
		this.reproPersent = reproPersent;
	}
	
	public String getModelName() {
		return modelName;
	}

	public void setModelName(String moduleName) {
		this.modelName = moduleName;
	}
	
	public Set<TestCaseInfo> getTestCases() {
		return testCases;
	}

	public void setTestCases(Set<TestCaseInfo> testCases) {
		this.testCases = testCases;
	}

	public String getRoleInTask() {
		return getDtoHelper().roleInTask;
	}

	public void setRoleInTask(String roleInTask) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.roleInTask = roleInTask;
	}

	public String getTestFlow() {
		return getDtoHelper().testFlow;
	}

	public void setTestFlow(String testFlow) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.testFlow = testFlow;
	}	
	public User getCurrHander() {
		return this.dtoHelper.currHander;
	}
	public void setCurrHander(User currHander) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.currHander = currHander;
	}
	public User getDevOwner() {
		return this.dtoHelper.devOwner;
	}
	public void setDevOwner(User devOwner) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.devOwner = devOwner;
	}
	public User getTestOwner() {
		return this.dtoHelper.testOwner;
	}
	public void setTestOwner(User testOwner) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.testOwner = testOwner;
	}
	public SoftwareVersion getReptVersion() {
		if(dtoHelper==null){
			return null;
		}
		return this.dtoHelper.getReptVersion();
	}
	
	public void setReptVersion(SoftwareVersion reptVersion) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.setReptVersion(reptVersion);
	}
	
	public String getVerSelStr() {

		return  this.dtoHelper.getVerSelStr();
	}
	public void setVerSelStr(String verSelStr) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.setVerSelStr(verSelStr);
	}
	
	public SoftwareVersion getCurrVersion() {
		return this.dtoHelper.currVersion;
	}
	
	public void setCurrVersion(SoftwareVersion currVersion) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.currVersion = currVersion;
	}
	
	public User getAnalysOwner() {
		return this.dtoHelper.analysOwner;
	}
	
	public void setAnalysOwner(User analysOwner) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.analysOwner = analysOwner;
	}
	public User getAssinOwner() {
		
		return this.dtoHelper.assinOwner;
	}
	public void setAssinOwner(User assinOwner) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.assinOwner = assinOwner;
	}
	public User getIntecesOwner() {
		return this.dtoHelper.intecesOwner;
	}
	public void setIntecesOwner(User intecesOwner) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.intecesOwner = intecesOwner;
	}
	public User getAuthor() {
		return this.dtoHelper.author;
	}
	public void setAuthor(User author) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.author = author;
	}
	public Integer getInitState() {
		return initState;
	}
	public String getDevChkIdSelStr() {
		return this.dtoHelper.devChkIdSelStr;
	}
	public void setDevChkIdSelStr(String devChkIdSelStr) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.devChkIdSelStr = devChkIdSelStr;
	}
	public String getTestChkIdSelStr() {
		return this.dtoHelper.testChkIdSelStr;
	}
	public void setTestChkIdSelStr(String testChkIdSelStr) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.testChkIdSelStr = testChkIdSelStr;
	}
	public String getTestLdIdSelStr() {
		return this.dtoHelper.testLdIdSelStr;
	}
	public void setTestLdIdSelStr(String testLdIdSelStr) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.testLdIdSelStr = testLdIdSelStr;
	}
	public void setInitState(Integer initState) {
		this.initState = initState;
	}
	
	public String getStaFlwMemStr() {
		return this.dtoHelper.staFlwMemStr;
	}
	public void setStaFlwMemStr(String staFlwMemStr) {
		if(this.dtoHelper==null){
			this.dtoHelper = new BugDtoHelper();
		}
		this.dtoHelper.staFlwMemStr = staFlwMemStr;
	}
	public Long getVerifyVer() {
		return verifyVer;
	}
	public void setVerifyVer(Long verifyVer) {
		this.verifyVer = verifyVer;
	}
	
	public Date getFixDate() {
		return fixDate;
	}
	public void setFixDate(Date fixDate) {
		this.fixDate = fixDate;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BugBaseInfo))
			return false;
		BugBaseInfo castOther = (BugBaseInfo) other;

		return (this.getBugId() == castOther.getBugId()) || (this
						.getBugId() != null
						&& castOther.getBugId() != null && this
						.getBugId().toString().equals(castOther.getBugId().toString()));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getBugId() == null ? 0 : this.getBugId()
						.hashCode());
		return result;
	}
	
	public Long getCurrVerInfo(){
		
		//"6", "不再现/需提供更多信息"
		//("7", "待改/再现"
		//"8", "待改/不再现"
		//"9", "待改/未解决"
		//"10", "待改"
		//"11", "挂起/需提供更多信息"
		if(currStateId==6||currStateId==7||currStateId==8
				||currStateId==9||currStateId==10||currStateId==11){
			if(verifyVer==null){
				return null;
			}
			versionLable = "校验在版本";
			return verifyVer;
		}		
		//"13", "已改";
		//"18", "交叉验证"
		else if(currStateId==13||currStateId==18){
			if(fixVer==null){
				return null;
			}
			versionLable = "修改在版本";
			return fixVer;			
		}
		
		//"14", "关闭/己解决"
		//"15", "关闭/不再现"
		//"22", "关闭/撤销"
		//"23", "关闭/遗留"
		else if(currStateId==14||currStateId==15||currStateId==22||currStateId==23){
			if(verifyVer==null){
				return null;
			}
			versionLable = "关闭在版本";
			return verifyVer;
		}
		
		
		//"20", "挂起/下版本修改"
		//"21", "待改/下版本修改"
		else if(currStateId==20||currStateId==21){
			if(this.fixVer==null){
				return null;
			}
			versionLable = "延迟到版本";
			return fixVer;			
		}
		
		//"1", "待置"
		//"2", "修正/描述不当"(好像不用)
		//"3", "重复"
		//"4", "无效"
		//"5", "撤销"
		//"12", "分歧"
		//"16", "非错"
		//"17", "重分配"
		//"24", "分析"
		//"25", "分配"
		//"19", "挂起/不计划修改"
		else{
			return null;
		}
		
	}
	public String toStrList(){
		StringBuffer sbf = new StringBuffer();
		sbf.append("{");
		sbf.append("id:'");
		sbf.append(getBugId().toString());
		sbf.append("',data: [0,'','");
		sbf.append(bugDesc );
		sbf.append("','");
		sbf.append(BugFlowConst.getStateName(currStateId));//当前状态
		sbf.append("','");
		sbf.append(dtoHelper.bugGrade.getTypeName() );//等级
		sbf.append("','");
		sbf.append(dtoHelper.bugFreq.getTypeName());//频率
		sbf.append("','");
		sbf.append(dtoHelper.bugOpotunity.getTypeName());//时机
		sbf.append("','");
		sbf.append(dtoHelper.bugType.getTypeName());//类型
		sbf.append("','");
		sbf.append(dtoHelper.bugPri.getTypeName());//优先级
		sbf.append("','");
		sbf.append(testName);//所属测试人员
		sbf.append("','");
		sbf.append(devName== null ? "" : devName );//引入原因
		sbf.append("','");
		sbf.append(StringUtils.formatShortDate(reptDate ));//发现日期
		//下面的属性为隐匿属性
		sbf.append("','");
		sbf.append(this.msgFlagConvert(msgFlag));//意见交流标识();用它来控制发现人的显示颜色
		sbf.append("','");
		sbf.append(relaCaseFlag==null||relaCaseFlag==0?"0":1);//关联用例标记 用它来控制关联或查看用例的颜色
		sbf.append("','");
		sbf.append(moduleId);//模块
		sbf.append("','");
		sbf.append(this.testOwnerId );//报告人Id
		//sbf.append("");//报告人Id
		sbf.append("','");
		sbf.append(currFlowCd );//当前流程编码
		sbf.append("','");
		//sbf.append(currHandlerId);//当前处理人ID
		sbf.append("");//当前处理人ID
		sbf.append("','");
		sbf.append(currStateId );//当前状态ID
		sbf.append("','");
		sbf.append(nextFlowCd);//下一流程骗码
		sbf.append("','");
		//sbf.append("");//报告人
		sbf.append(bugReptId);//报告人
		sbf.append("'");
		sbf.append("]");
		sbf.append("}");
		dtoHelper=null;
		return sbf.toString();
	}

	private String msgFlagConvert(Integer msgFlag){
	   String myUserId = SecurityContextHolderHelp.getUserId();
	   if(msgFlag==null||msgFlag==0||msgFlag==9){
		   return "0";
	   }else if(msgFlag==1&&myUserId.equals(this.testOwnerId)){
		   return "1";
	   }else if(msgFlag==3&&myUserId.equals(this.analyseOwnerId)){
		   return "1";
	   }else if(msgFlag==4&&myUserId.equals(this.assinOwnerId)){
		   return "1";
	   }else if(msgFlag==5&&myUserId.equals(this.devOwnerId)){
		   return "1";
	   }else if(msgFlag==7&&myUserId.equals(this.intercessOwnerId)){
		   return "1";
	   }else if(msgFlag==10){
		   return "1";
	   }
	   return "0";
   }
   public String toStrAddInit(){
	   return null;
   }
   public String getVersionLable() {
		return versionLable;
   }
   
   public void setVersionLable(String versionLable) {
		this.versionLable = versionLable;
   }
	
   public String getTaskName() {
		return taskName;
   }
   public void setTaskName(String taskName) {
		this.taskName = taskName;
   }
   
   public String getNextOwnerId() {
		return nextOwnerId;
	}
	public void setNextOwnerId(String nextOwnerId) {
		this.nextOwnerId = nextOwnerId;
	}

	public String getModuleNum() {
		return moduleNum;
	}
	public void setModuleNum(String moduleNum) {
		this.moduleNum = moduleNum;
	}
	
   public String toStrAddInit(boolean includeType){
       StringBuffer sbf = new StringBuffer();
       //一拉列表选项值
       if(includeType){
           sbf.append("typeSelStr=").append(getDtoHelper().typeSelStr);
           sbf.append("^");
           sbf.append("gradeSelStr=").append(getDtoHelper().gradeSelStr);
           sbf.append("^");
           sbf.append("freqSelStr=").append(getDtoHelper().freqSelStr);
           sbf.append("^");
           sbf.append("occaSelStr=").append(getDtoHelper().occaSelStr);
           sbf.append("^");
           sbf.append("geCaseSelStr=").append(getDtoHelper().geCaseSelStr == null ? "" : getDtoHelper().geCaseSelStr);
           sbf.append("^");
           sbf.append("sourceSelStr=").append(getDtoHelper().sourceSelStr);
           sbf.append("^");
           sbf.append("plantFormSelStr=").append(getDtoHelper().plantFormSelStr);
           sbf.append("^");
           sbf.append("priSelStr=").append(getDtoHelper().priSelStr); 
           sbf.append("^");
           sbf.append("genePhaseSelStr=").append(getDtoHelper().genePhaseSelStr); 
	       sbf.append("^");
	       sbf.append("verSelStr=").append(getDtoHelper().getVerSelStr());
	       sbf.append("^");
       }
       //人员选择列表
       sbf.append("testSelStr=").append(getDtoHelper().testSelStr == null ? "" : getDtoHelper().testSelStr);
       sbf.append("^");
       sbf.append("devStr=").append(getDtoHelper().devStr == null ? "" : getDtoHelper().devStr);
       sbf.append("^");
       sbf.append("assignSelStr=").append(getDtoHelper().assignSelStr == null ? "" : getDtoHelper().assignSelStr);
       sbf.append("^");
       sbf.append("analySelStr=").append(getDtoHelper().analySelStr == null ? "" : getDtoHelper().analySelStr);
       sbf.append("^");
       sbf.append("interCesSelStr=").append(getDtoHelper().interCesSelStr == null ? "" : getDtoHelper().interCesSelStr);
       
       
       sbf.append("^");
       sbf.append("currFlowCd=").append(currFlowCd);
       sbf.append("^");
       sbf.append("nextFlowCd=").append(nextFlowCd);
       sbf.append("^");
       sbf.append("testPhase=").append(testPhase);  
//       sbf.append("^");
//       sbf.append("initSeq=").append(initSeq); 
       sbf.append("^");
       sbf.append("currStateId=").append(currStateId); 
       sbf.append("^");
       sbf.append("stateName=").append(stateName); 
       sbf.append("^");
       
       sbf.append("testFlow=").append(getDtoHelper().testFlow);
       sbf.append("^");
       sbf.append("roleInTask=").append(getDtoHelper().roleInTask);
       sbf.append("^");
       sbf.append("relCaseSwitch=").append(getDtoHelper().relCaseSwitch);
       dtoHelper=null;
       return sbf.toString();
       
   }
    public String toStrUpdateInit(){
        StringBuffer sbf = new StringBuffer();
        sbf.append(getBugId().toString());
        sbf.append("^");
        sbf.append("moduleId=").append(moduleId);
        //下拉列表值
        sbf.append("^");
        sbf.append("bugTypeId=").append(bugTypeId);
        sbf.append("^");
        sbf.append("bugGradeId=").append(bugGradeId);
        sbf.append("^");
        sbf.append("bugFreqId=").append(bugFreqId);
        sbf.append("^");
        sbf.append("bugOccaId=").append(bugOccaId);
        sbf.append("^");
        sbf.append("priId=").append(priId);
        sbf.append("^");
        sbf.append("genePhaseId=").append(genePhaseId);
        sbf.append("^");
        sbf.append("geneCauseId=").append(geneCauseId);
        sbf.append("^");
        sbf.append("sourceId=").append(sourceId);
        sbf.append("^");
        sbf.append("platformId=").append(platformId);  
        //一拉列表选项值
        sbf.append("^");
        sbf.append("typeSelStr=").append(getDtoHelper().typeSelStr);
        sbf.append("^");
        sbf.append("gradeSelStr=").append(getDtoHelper().gradeSelStr);
        sbf.append("^");
        sbf.append("freqSelStr=").append(getDtoHelper().freqSelStr);
        sbf.append("^");
        sbf.append("occaSelStr=").append(getDtoHelper().occaSelStr);
        sbf.append("^");
        sbf.append("geCaseSelStr=").append(getDtoHelper().geCaseSelStr == null ? "" : getDtoHelper().geCaseSelStr);
        sbf.append("^");
        sbf.append("sourceSelStr=").append(getDtoHelper().sourceSelStr);
        sbf.append("^");
        sbf.append("plantFormSelStr=").append(getDtoHelper().plantFormSelStr);
        sbf.append("^");
        sbf.append("genePhaseSelStr=").append(getDtoHelper().genePhaseSelStr == null ? "" : getDtoHelper().genePhaseSelStr);
        sbf.append("^");
        sbf.append("priSelStr=").append(getDtoHelper().priSelStr);
        sbf.append("^");
        sbf.append("testFlow=").append(getDtoHelper().testFlow);
        sbf.append("^");
        sbf.append("roleInTask=").append(getDtoHelper().roleInTask);
        sbf.append("^");
        sbf.append("testPhase=").append(testPhase);  

        //BUG描述
        sbf.append("^");
        sbf.append("bugDesc=").append(bugDesc == null ? "" : bugDesc);
        sbf.append("^");
        sbf.append("reProStep=").append(this.reProStep == null ? "" : reProStep);
        sbf.append("^");
        sbf.append("currRemark=").append(currRemark == null ? "" : currRemark);
        sbf.append("^");
        sbf.append("currHandlDate=").append(currHandlDate == null ? "" : StringUtils.formatShortDate(currHandlDate));
        sbf.append("^");
        //隐藏属性

        sbf.append("bugReptVer=").append(bugReptVer == null ? "" : bugReptVer);
        //sbf.append("^");
       // sbf.append("currVer=").append(currVer == null ? "" : currVer);
        sbf.append("^");
        sbf.append("verifyVer=").append(verifyVer == null ? "" : verifyVer);
        sbf.append("^");
        sbf.append("reptDate=").append(reptDate == null ? "" : StringUtils.formatShortDate(reptDate));
        sbf.append("^");
        sbf.append("msgFlag=").append(msgFlag == null ? "" : msgFlag);
        sbf.append("^");
        sbf.append("bugAntimodDate=").append(bugAntimodDate == null ? "" : StringUtils.formatShortDate(bugAntimodDate));
        sbf.append("^");
        sbf.append("planAmendHour=").append(planAmendHour == null ? "" : planAmendHour);
//        sbf.append("^");
//        sbf.append("testSeq=").append(testSeq == null ? "" : testSeq);
//        sbf.append("^");
//        sbf.append("initSeq=").append(initSeq == null ? "" : initSeq);
        //流程相关
        sbf.append("^");
        sbf.append("currStateId=").append(currStateId == null ? "" : currStateId);
        sbf.append("^");
        sbf.append("nextFlowCd=").append(nextFlowCd == null ? "" : nextFlowCd);
        sbf.append("^");
        sbf.append("currFlowCd=").append(currFlowCd == null ? "" : currFlowCd);
        
        //人员相关
        sbf.append("^");
        sbf.append("bugReptId=").append(bugReptId == null ? "" : bugReptId);
        sbf.append("^");
        sbf.append("testOwnerId=").append(testOwnerId == null ? "" : testOwnerId);
        sbf.append("^");
        sbf.append("devOwnerId=").append(devOwnerId == null ? "" : devOwnerId);
        sbf.append("^");
        sbf.append("currHandlerId=").append(currHandlerId == null ? "" : currHandlerId);
        sbf.append("^");
        sbf.append("analyseOwnerId=").append(analyseOwnerId == null ? "" : analyseOwnerId);
        sbf.append("^");
        sbf.append("assinOwnerId=").append(assinOwnerId == null ? "" : assinOwnerId);
        sbf.append("^");
        sbf.append("intercessOwnerId=").append(intercessOwnerId == null ? "" : intercessOwnerId);
        //人员选择列表
        sbf.append("^");
        sbf.append("testSelStr=").append(getDtoHelper().testSelStr == null ? "" : getDtoHelper().testSelStr);
        sbf.append("^");
        sbf.append("devStr=").append(getDtoHelper().devStr == null ? "" : getDtoHelper().devStr);
        sbf.append("^");
        sbf.append("assignSelStr=").append(getDtoHelper().assignSelStr == null ? "" : getDtoHelper().assignSelStr);
        sbf.append("^");
        sbf.append("analySelStr=").append(getDtoHelper().analySelStr == null ? "" : getDtoHelper().analySelStr);
        sbf.append("^");
        sbf.append("interCesSelStr=").append(getDtoHelper().interCesSelStr == null ? "" : getDtoHelper().interCesSelStr);
        return sbf.toString();
    }

    public String base2json(){
        StringBuffer sbf = new StringBuffer();
        sbf.append("bugDesc=").append(bugDesc);
        sbf.append("^");
        sbf.append("reProStep=").append(reProStep);
        sbf.append("^");
        sbf.append("bugReptVer=").append(getReptVersion().getVersionNum()==null?"":getReptVersion().getVersionNum());
        sbf.append("^");
        sbf.append("attachUrl=").append(this.attachUrl == null ? "" : attachUrl);
        sbf.append("^");
        sbf.append("reptDate=").append(StringUtils.formatLongDate(reptDate));
        sbf.append("^");
        sbf.append("bugAntimodDate=").append(bugAntimodDate == null ? "" : StringUtils.formatShortDate(bugAntimodDate));
        sbf.append("^");
        sbf.append("planAmendHour=").append(planAmendHour == null ? "" : planAmendHour);
        sbf.append("^");
        sbf.append("currRemark=").append(currRemark == null ? "" : currRemark);
        sbf.append("^");
        sbf.append("testName=").append(dtoHelper.getTestOwner().getUniqueName());
        sbf.append("^");
        sbf.append("devName=").append(dtoHelper.getDevOwner() == null ? "暂未指定" : dtoHelper.getDevOwner().getUniqueName());
        sbf.append("^");
        sbf.append("bugTypeName=").append(dtoHelper.getBugType().getTypeName());
        sbf.append("^");
        sbf.append("bugGradeName=").append(dtoHelper.getBugGrade().getTypeName());
        sbf.append("^");
        sbf.append("pltfomName=").append(dtoHelper.getOccurPlant().getTypeName());
        sbf.append("^");
        sbf.append("occaName=").append(dtoHelper.getBugOpotunity().getTypeName());
        sbf.append("^");
        sbf.append("geneCaseName=").append(dtoHelper.getGeneCause()==null?"":dtoHelper.getGeneCause().getTypeName());
        sbf.append("^");
        sbf.append("sourceName=").append(dtoHelper.getBugSource().getTypeName());
        sbf.append("^");
        sbf.append("imphaName=").append(getDtoHelper().getImportPhase()==null?"":getDtoHelper().getImportPhase().getTypeName());
        sbf.append("^");
        sbf.append("bugFreqName=").append(getDtoHelper().getBugFreq().getTypeName());
        sbf.append("^");
        sbf.append("priName=").append(dtoHelper.getBugPri().getTypeName());
        sbf.append("^");
        sbf.append("modelName=").append(modelName == null ? "" : modelName);
        sbf.append("^");
        sbf.append("stateName=").append(BugFlowConst.getStateName(currStateId));
        sbf.append("^");
        sbf.append("authorName=").append(dtoHelper.getAuthor().getUniqueName());
        sbf.append("^");
        sbf.append("reproPersent=").append(this.reproPersent==null?"":reproPersent);   
        sbf.append("^");
        sbf.append("versionLable=").append(this.versionLable==null?"":versionLable);  
        sbf.append("^");
        sbf.append("currVer=").append(this.getCurrVersion()==null?"":this.getCurrVersion().getVersionNum());        
        return sbf.toString();
        
    }
    public String toStrUpdateRest(){
        StringBuffer sbf = new StringBuffer();
        sbf.append(getBugId().toString());
        sbf.append("^");
       // sbf.append("0,,");
        sbf.append("0,").append(getBugId().toString()).append(",");
        sbf.append(bugDesc );//描述
        sbf.append(",");
        sbf.append(BugFlowConst.getStateName(currStateId));//当前状态
        sbf.append(",");
        sbf.append(dtoHelper.bugGrade==null?"":dtoHelper.bugGrade.getTypeName() );//等级
        sbf.append(",");
       // sbf.append(dtoHelper.bugFreq==null?"":dtoHelper.bugFreq.getTypeName());//频率
        sbf.append(taskName==null?"":taskName);
        sbf.append(",");
        sbf.append(dtoHelper.bugOpotunity==null?"":dtoHelper.bugOpotunity.getTypeName());//时机
        sbf.append(",");
        sbf.append(dtoHelper.bugType==null?"":dtoHelper.bugType.getTypeName());//类型
        sbf.append(",");
        sbf.append(dtoHelper.bugPri==null?"":dtoHelper.bugPri.getTypeName());//优先级
        sbf.append(",");
        sbf.append(testName == null ? "" : testName );//所属测试人员
        sbf.append(",");
        sbf.append(devName == null ? "" : devName );
        sbf.append(",");
        sbf.append(StringUtils.formatLongDate(reptDate ));//发现日期
        //下面的属性为隐匿属性
        sbf.append(",");
        sbf.append(this.msgFlagConvert(msgFlag));//意见交流标识();用它来控制发现人的显示颜色
        sbf.append(",");
        sbf.append(relaCaseFlag == null||relaCaseFlag==0 ? "0":1);//关联用例标记 用它来控制关联或查看用例的颜色
        sbf.append(",");
        sbf.append(moduleId );//模块
        sbf.append(",");
        sbf.append(this.testOwnerId );//报告人Id
        //sbf.append("");//报告人Id
        sbf.append(",");
        sbf.append(currFlowCd);//当前流程编码
        sbf.append(",");
        //sbf.append(currHandlerId == null ? "" : currHandlerId );//当前处理人ID
        sbf.append("");//当前处理人ID
        sbf.append(",");
        sbf.append(currStateId );//当前状态ID
        sbf.append(",");
        sbf.append(nextFlowCd);//下一流程编码
        sbf.append(",");
        //sbf.append("" );//报告人Id
        sbf.append(bugReptId );//报告人Id
        sbf.append(",");
        sbf.append(taskId);
        dtoHelper=null;
        return sbf.toString();
      }



   	public void toString(StringBuffer sbf){
        sbf.append("{");
        sbf.append("id:'");
        sbf.append(getBugId().toString());
        //System.out.println("current bug id=="+getBugId().toString());
        sbf.append("',data: [0,'"+this.bugId+"','");
        sbf.append(bugDesc );
        sbf.append("','");
        sbf.append(BugFlowConst.getStateName(currStateId));//当前状态
        sbf.append("','");
        sbf.append(dtoHelper.bugGrade.getTypeName() );//等级
        sbf.append("','");
        //sbf.append(dtoHelper.bugFreq.getTypeName());//频率
        sbf.append(taskName==null?"":taskName);//
        sbf.append("','");
        sbf.append(dtoHelper.bugOpotunity.getTypeName());//时机
        sbf.append("','");
        sbf.append(dtoHelper.bugType==null?"":dtoHelper.bugType.getTypeName());//类型
        sbf.append("','");
        sbf.append(dtoHelper.bugPri==null?"":dtoHelper.bugPri.getTypeName());//优先级
        sbf.append("','");
        sbf.append(testName);//所属测试人员
        sbf.append("','");
        sbf.append(devName== null ? "" : devName );//引入原因
        sbf.append("','");
        sbf.append(StringUtils.formatLongDate(reptDate ));//发现日期
        //下面的属性为隐匿属性
        sbf.append("','");
        sbf.append(this.msgFlagConvert(msgFlag));//意见交流标识();用它来控制发现人的显示颜色
        sbf.append("','");
        sbf.append(relaCaseFlag==null||relaCaseFlag==0?"0":1);//关联用例标记 用它来控制关联或查看用例的颜色
        sbf.append("','");
        sbf.append(moduleId);//模块
        sbf.append("','");
        sbf.append(this.testOwnerId );//测试所属Id
        //sbf.append("");//报告人Id
        sbf.append("','");
        sbf.append(currFlowCd );//当前流程编码
        sbf.append("','");
        //sbf.append(currHandlerId);//当前处理人ID
        sbf.append("");//当前处理人ID
        sbf.append("','");
        sbf.append(currStateId );//当前状态ID
        sbf.append("','");
        sbf.append(nextFlowCd);//下一流程骗码
        sbf.append("','");
        //sbf.append("");//报告人
        sbf.append(bugReptId);//报告人
        sbf.append("','");
        sbf.append(taskId);
        sbf.append("'");
        sbf.append("]");
        sbf.append("}");
        dtoHelper=null;   
   	}
   	public void toMyHomeString(StringBuffer sbf){
        sbf.append("{");
        sbf.append("id:'");
        sbf.append(getBugId().toString());
        sbf.append("',data: [0,'','");
        sbf.append(bugDesc );
        sbf.append("','");
        sbf.append(testName);//所属测试人员
        sbf.append("','");
        sbf.append(this.getReptVersion()!=null?this.getReptVersion().getVersionNum():"");//所属测试人员
        sbf.append("','");
        sbf.append(BugFlowConst.getStateName(currStateId));//当前状态
        sbf.append("','");
        sbf.append(StringUtils.formatShortDate(reptDate ));//发现日期
        //下面的属性为隐匿属性
        sbf.append("','");
        sbf.append(attachUrl==null?"":attachUrl);
        sbf.append("','");
        sbf.append(taskId);
        sbf.append("'");
        sbf.append("]");
        sbf.append("}");
        dtoHelper=null;   
   	}
   public String toStrFindInit(boolean includeType){
       StringBuffer sbf = new StringBuffer();
       //一拉列表选项值
       if(includeType){
	       sbf.append("typeSelStr=").append(getDtoHelper().typeSelStr);
	       sbf.append("^");
	       sbf.append("gradeSelStr=").append(getDtoHelper().gradeSelStr);
	       sbf.append("^");
	       sbf.append("freqSelStr=").append(getDtoHelper().freqSelStr);
	       sbf.append("^");
	       sbf.append("occaSelStr=").append(getDtoHelper().occaSelStr);
	       sbf.append("^");
	       sbf.append("geCaseSelStr=").append(getDtoHelper().geCaseSelStr);
	       sbf.append("^");
	       sbf.append("sourceSelStr=").append(getDtoHelper().sourceSelStr);
	       sbf.append("^");
	       sbf.append("plantFormSelStr=").append(getDtoHelper().plantFormSelStr);
	       sbf.append("^");
	       sbf.append("genePhaseSelStr=").append(getDtoHelper().genePhaseSelStr);
	       sbf.append("^");
	       sbf.append("priSelStr=").append(getDtoHelper().priSelStr);
	       sbf.append("^");
	       sbf.append("verSelStr=").append(getDtoHelper().getVerSelStr());
	       sbf.append("^");
	       
       }
       //人员选择列表
       sbf.append("testSelStr=").append(getDtoHelper().testSelStr);
       sbf.append("^");
       sbf.append("devStr=").append(getDtoHelper().devStr);  
       sbf.append("^");
       sbf.append("queryCount=").append(getDtoHelper().queryCount); 
       return sbf.toString();
   }




	public String getChargeOwner() {
		return chargeOwner;
	}
	public void setChargeOwner(String chargeOwner) {
		this.chargeOwner = chargeOwner;
	}
	public String getChargeOwnerName() {
		return chargeOwnerName;
	}
	public void setChargeOwnerName(String chargeOwnerName) {
		this.chargeOwnerName = chargeOwnerName;
	}

}