package cn.com.codes.object;

import java.util.Date;
import java.util.List;
import java.util.Set;

import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.transmission.JsonInterface;
import cn.com.codes.object.BugBaseInfo;
import cn.com.codes.object.CasePri;
import cn.com.codes.object.CaseType;
import cn.com.codes.object.OutlineInfo;
import cn.com.codes.object.TestCaseInfo;
import cn.com.codes.object.TestResult;



public class TestCaseInfo implements JsonInterface{

	private String taskId;
	private Long moduleId;
	private Long testCaseId;
	private String createrId;
	private String prefixCondition;
	private String testCaseDes;
	private String testData;
	private String operDataRichText;
	private String expResult;
	private Integer isReleased;
	private Date creatdate;
	private String attachUrl;
	private Long caseTypeId ;
	private Long priId ;
	private Integer weight=2;
	private Integer testStatus;
	private String typeSelStr ;
	private String priSelStr ;
	private String remark;
	private String typeName;
	private String priName;
	private Date upddate;
	private OutlineInfo outline;
	private CaseType caseType;
	private CasePri casePri;
	private String moduleNum;
	private Set<TestResult> testResult ;
	private List<TestResult> testResultList ;
	private Set<BugBaseInfo> bugs;
	private String mdPath;
	private String authorName;
	private String auditId;
	private String auditerNmae;
	private String taskName;
	private String moduleName;
	public TestCaseInfo() {
	}
	
	public TestCaseInfo(Long testCaseId,String testCaseDes,Integer testStatus,Long caseTypeId,Long priId) {
		this.testCaseId = testCaseId;
		this.testCaseDes = testCaseDes;
		this.testStatus = testStatus;
		this.caseTypeId = caseTypeId;
		this.priId = priId;
	}
	public TestCaseInfo(Long testCaseId,String prefixCondition,String testCaseDes,String testData,String expResult) {
		this.testCaseId = testCaseId;
		this.prefixCondition=prefixCondition;
		this.testCaseDes = testCaseDes;
		this.testData = testData;
		this.expResult = expResult;
	}
	public TestCaseInfo(Long testCaseId,String prefixCondition,String testCaseDes,Integer isReleased,Integer testStatus,
			Integer weight,String createrId,String attachUrl,String auditId,
			Long caseTypeId,Long priId,Long moduleId,String taskId,Date creatdate) {
		this.testCaseId = testCaseId;
		this.prefixCondition=prefixCondition;
		this.testCaseDes = testCaseDes;
		this.isReleased = isReleased;
		this.testStatus = testStatus;
		this.weight = weight; 
		this.createrId = createrId;
		this.attachUrl = attachUrl;
		this.auditId = auditId;
		this.caseTypeId = caseTypeId;
		this.priId = priId;
		this.moduleId = moduleId;
		this.taskId = taskId;
		this.creatdate = creatdate;
	}	

	public TestCaseInfo(Long testCaseId,String prefixCondition,String testCaseDes,
			Integer weight,String attachUrl,String testData,String expResult,String operDataRichText,
			Long caseTypeId,Long priId) {
		this.testCaseId = testCaseId;
		this.prefixCondition = prefixCondition;
		this.testCaseDes = testCaseDes;
		this.weight = weight; 
		this.attachUrl = attachUrl;
		this.caseTypeId = caseTypeId;
		this.priId = priId;
		this.expResult = expResult;
		this.operDataRichText = operDataRichText;
		this.testData = testData;
	}	
	public TestCaseInfo(Long testCaseId,String prefixCondition,String testCaseDes,Integer testStatus,Long caseTypeId,Long priId){
		this.testCaseId = testCaseId;
		this.prefixCondition = prefixCondition;
		this.testCaseDes = testCaseDes;
		this.testStatus = testStatus;
		this.caseTypeId = caseTypeId;
		this.priId = priId;
	}
	public String getAuthorName() {
		return authorName;
	}

	public String getAuditerNmae() {
		return auditerNmae;
	}

	public void setAuditerNmae(String auditerNmae) {
		this.auditerNmae = auditerNmae;
	}

	public String getAuditId(){
		return this.auditId;
	}


	public TestCaseInfo(Long testCaseId) {
		this.testCaseId = testCaseId;
	}
	public String getMdPath(){
		return this.mdPath;
	}
	public void setMdPath(String mdPath){
		this.mdPath = mdPath;
	}
	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}


	public Long getModuleId() {
		return this.moduleId;
	}

	public void setModuleId(Long moduleid) {
		this.moduleId = moduleid;
	}

	public Long getTestCaseId() {
		return this.testCaseId;
	}

	public void setTestCaseId(Long testCaseId) {
		this.testCaseId = testCaseId;
	}

	public String getCreaterId() {
		return this.createrId;
	}

	public void setCreaterId(String createrId) {
		this.createrId = createrId;
	}

	public String getPrefixCondition() {
		return prefixCondition;
	}

	public void setPrefixCondition(String prefixCondition) {
		this.prefixCondition = prefixCondition;
	}

	public String getTestCaseDes() {
		return this.testCaseDes;
	}

	public void setTestCaseDes(String testCaseDes) {
		this.testCaseDes = testCaseDes;
	}

	public String getExpResult() {
		return this.expResult;
	}

	public void setExpResult(String expresult) {
		this.expResult = expresult;
	}

	public Date getCreatdate() {
		return this.creatdate;
	}

	public void setCreatdate(Date creatdate) {
		this.creatdate = creatdate;
	}


	public Date getUpddate() {
		return this.upddate;
	}

	public void setUpddate(Date upddate) {
		this.upddate = upddate;
	}

	public Integer getIsReleased() {
		return isReleased;
	}

	public void setIsReleased(Integer isReleased) {
		this.isReleased = isReleased;
	}

	public Set<TestResult> getTestResult() {
		return testResult;
	}

	public void setTestResult(Set<TestResult> testResult) {
		this.testResult = testResult;
	}


	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}


	public Long getCaseTypeId() {
		return caseTypeId;
	}

	public void setCaseTypeId(Long casetTypeId) {
		this.caseTypeId = casetTypeId;
	}

	

	public String getOperDataRichText() {
		return operDataRichText;
	}

	public void setOperDataRichText(String operDataRichText) {
		this.operDataRichText = operDataRichText;
	}

	public Long getPriId() {
		return priId;
	}

	public void setPriId(Long priId) {
		this.priId = priId;
	}

	public Integer getTestStatus() {
		return testStatus;
	}

	public void setTestStatus(Integer testStatus) {
		this.testStatus = testStatus;
	}
	
	public String getTypeSelStr() {
		return typeSelStr;
	}

	public void setTypeSelStr(String typeSelStr) {
		this.typeSelStr = typeSelStr;
	}

	public String getPriSelStr() {
		return priSelStr;
	}

	public void setPriSelStr(String priSelStr) {
		this.priSelStr = priSelStr;
	}
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getPriName() {
		return priName;
	}

	public void setPriName(String priName) {
		this.priName = priName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	public OutlineInfo getOutline() {
		return outline;
	}
	public void setOutline(OutlineInfo outline) {
		this.outline = outline;
	}
	public CaseType getCaseType() {
		return caseType;
	}
	public void setCaseType(CaseType caseType) {
		this.caseType = caseType;
	}
	public CasePri getCasePri() {
		return casePri;
	}
	public void setCasePri(CasePri casePri) {
		this.casePri = casePri;
	}
	public String getModuleNum() {
		return moduleNum;
	}
	public void setModuleNum(String moduleNum) {
		this.moduleNum = moduleNum;
	}

	public List<TestResult> getTestResultList() {
		return testResultList;
	}
	public void setTestResultList(List<TestResult> testResultList) {
		this.testResultList = testResultList;
	}
	
	public String getTestData() {
		return testData;
	}
	public void setTestData(String testData) {
		this.testData = testData;
	}
	public Set<BugBaseInfo> getBugs() {
		return bugs;
	}
	public void setBugs(Set<BugBaseInfo> bugs) {
		this.bugs = bugs;
	}
	public String getAttachUrl() {
		return attachUrl;
	}

	public void setAttachUrl(String attachUrl) {
		this.attachUrl = attachUrl;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}
	
	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TestCaseInfo))
			return false;
		TestCaseInfo castOther = (TestCaseInfo) other;

		return (this.getTestCaseId() == castOther.getTestCaseId()) || (this
						.getTestCaseId() != null
						&& castOther.getTestCaseId() != null && this
						.getTestCaseId().equals(castOther.getTestCaseId()));
	}

	public int hashCode() {
		int result = 17;
		result = 37
				* result
				+ (getTestCaseId() == null ? 0 : this.getTestCaseId()
						.hashCode());
		return result;
	}
	
    public String toStrUpdateInit(){
        StringBuffer sbf = new StringBuffer();
        sbf.append("testCaseId=").append(getTestCaseId()==null?"":getTestCaseId().toString());
        sbf.append("^");
        sbf.append("prefixCondition=").append(prefixCondition==null?"":prefixCondition);
        sbf.append("testCaseDes=").append(testCaseDes==null?"":testCaseDes);
        sbf.append("^");
        sbf.append("typeSelStr=").append(typeSelStr == null ? "" : typeSelStr);
        sbf.append("^");
        sbf.append("priSelStr=").append(priSelStr == null ? "" : priSelStr);
        sbf.append("^");
        sbf.append("taskId=").append(taskId);
        sbf.append("^");
        sbf.append("moduleId=").append(moduleId);
        sbf.append("^");
        sbf.append("createrId=").append(createrId);
        sbf.append("^");
        sbf.append("operDataRichText=").append(operDataRichText);
        sbf.append("^");
        sbf.append("isReleased=").append(isReleased == null ? "" : isReleased);
        sbf.append("^");
        sbf.append("creatdate=").append( StringUtils.formatLongDate(creatdate));
        sbf.append("^");
        sbf.append("attachUrl=").append(attachUrl == null ? "" : attachUrl);
        sbf.append("^");
        sbf.append("caseTypeId=").append(caseTypeId);
        sbf.append("^");
        sbf.append("priId=").append(priId);
        sbf.append("^");
        sbf.append("weight=").append(weight == null ? "" : weight);
        sbf.append("^");
        sbf.append("auditId=").append(auditId == null ? "" : auditId);
        sbf.append("^");
        sbf.append("testStatus=").append(testStatus);
        sbf.append("^");
        sbf.append("expResult=").append(expResult == null ? "" : expResult );
        sbf.append("^");
        sbf.append("remark=").append(remark == null ? "" : remark );
        sbf.append("^");
        sbf.append("moduleNum=").append(moduleNum == null ? "" : moduleNum );
        if(mdPath!=null&&!"".equals(mdPath)){
            sbf.append("^");
            sbf.append("mdPath=").append(mdPath);
        }
        return sbf.toString();
    }

    public String toStrUpdateRest(){
        StringBuffer sbf = new StringBuffer();
        sbf.append(getTestCaseId().toString());
        sbf.append("^");
        //sbf.append("0,,");
        sbf.append("0,").append(getTestCaseId().toString()).append(",");
        sbf.append(prefixCondition == null ? "" : prefixCondition );
        sbf.append(testCaseDes == null ? "" : testCaseDes );
        sbf.append(",");
        sbf.append(testData == null ? "" : testData );
        sbf.append(",");
        sbf.append(expResult == null ? "" : expResult );
        sbf.append(",");
        sbf.append(testStatus == null ? "" : convCase(testStatus) );
        sbf.append(",");
        sbf.append(caseTypeId == null ? "" : typeName );      
        sbf.append(",");
        sbf.append(priId == null ? "" : priName );
        sbf.append(",");
		if(testStatus!=null&&testStatus!=0&&testStatus!=4&&testStatus!=6&&auditerNmae == null){
			sbf.append("自动审批");
		}else{
			sbf.append(auditerNmae == null ? "" : auditerNmae );
		}
        sbf.append(",");
        //sbf.append(currVer == null ? "" : currVer );  
        sbf.append("");
        sbf.append(",");
        //sbf.append(exeDate == null ? "" : StringUtils.formatShortDate(exeDate ));
        sbf.append("");
        sbf.append(",");
        sbf.append(authorName == null ? "" : authorName);        
        sbf.append(",");
        sbf.append(weight == null|| weight==0? "" : weight );
		sbf.append(",");
		sbf.append(taskName == null ? "" : taskName );
		sbf.append(",");
		sbf.append(StringUtils.formatLongDate(creatdate));
		sbf.append(",");
		sbf.append(attachUrl == null ? "" : attachUrl );
		sbf.append(",");
		sbf.append(isReleased == null ? "" : isReleased );
		sbf.append(",");
		sbf.append(moduleId == null ? "" : moduleId );
        return sbf.toString();
     }


	public void toString(StringBuffer sbf){
        sbf.append("{");
		sbf.append("id:'");
		sbf.append(getTestCaseId().toString());
		sbf.append("',data: [0,'"+this.testCaseId+"','");
		sbf.append(prefixCondition);
		sbf.append(testCaseDes);
		sbf.append("','");
		sbf.append("");//原本是操作过过，现在不用了
		sbf.append("','");
		sbf.append("" );//原本是预期结果，现在不用了
		sbf.append("','");
		sbf.append(testStatus == null ? "" : convCase(testStatus) );
		sbf.append("','");
		sbf.append(typeName);
		sbf.append("','");
		sbf.append(priName);		
		sbf.append("','");
//		if(testStatus!=0&&testStatus!=4&&testStatus!=6&&auditerNmae == null){
//			sbf.append("自动审批");
//		}else{
//			sbf.append(auditerNmae == null ? "" : auditerNmae );
//		}
		if(testStatus!=0&&testStatus!=4&&testStatus!=6&&auditerNmae == null){
			sbf.append("自动审批");
		}else{
			sbf.append(auditerNmae == null ? "" : auditerNmae );
		}
		sbf.append("','");
		//sbf.append(currVer== null ? "" : currVer );
		sbf.append("");
		sbf.append("','");
		//sbf.append(exeDate == null ? "" : StringUtils.formatShortDate(exeDate ));
		sbf.append("");
		sbf.append("','");
		sbf.append(authorName);
		sbf.append("','");
		sbf.append(weight == null ? "" : weight );
		sbf.append("','");
		sbf.append(taskName == null ? "" : taskName );
		sbf.append("','");
		sbf.append(this.creatdate == null ? "" : StringUtils.formatLongDate(creatdate ) );
		sbf.append(attachUrl == null ? "" : attachUrl );
		sbf.append("','");
		sbf.append("','");
		sbf.append(isReleased);
		sbf.append("','");
		sbf.append(moduleId);
		sbf.append("'");
		sbf.append("]");
		sbf.append("}");
	}
	public String toStrList(){
        StringBuffer sbf = new StringBuffer();
        sbf.append("{");
		sbf.append("id:'");
		sbf.append(testCaseId.toString());
		sbf.append("',data: [0,'"+this.testCaseId+"','");
		sbf.append(prefixCondition);
		sbf.append(testCaseDes);
		sbf.append("','");
		sbf.append(testStatus == null ? "" : convCase(testStatus) );
		sbf.append("','");
		sbf.append(typeName );
		sbf.append("','");
		sbf.append(priName);
		sbf.append("','");
		//sbf.append(haveRela);
		sbf.append("");
		sbf.append("'");
		sbf.append("]");
		sbf.append("}");
		return sbf.toString();
	}


	public static String convCase(Integer testStatus){
		if(testStatus==0){
			//return "ReView" ;
			return "待审核" ;
		}else if(testStatus==1){
			return "未测试" ;
		}else if(testStatus==2){
			return "通过" ;
		}else if(testStatus==3){
			return "未通过" ;
		}else if(testStatus==4){
			return "不适用" ;
		}else if(testStatus==5){
			return "阻塞" ;
		}else if(testStatus==6){
			return "待修正" ;
		}
		return "";
	}
	public static String convCase2(Integer testStatus){
		if(testStatus==0){
			return "待审核" ;
		}else if(testStatus==1){
			return "己审核" ;
		}else if(testStatus==2){
			return "己审核" ;
		}else if(testStatus==3){
			return "己审核" ;
		}else if(testStatus==4){
			return "不适用" ;
		}else if(testStatus==5){
			return "己审核" ;
		}else if(testStatus==6){
			return "待修正" ;
		}
		return "";
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
}