/**
 * 
 */
package cn.com.codes.testCasePackageManage.dto;

import java.util.Date;
import java.util.List;
import java.util.Set;

import cn.com.codes.framework.transmission.dto.BaseDto;
import cn.com.codes.object.BugBaseInfo;
import cn.com.codes.object.CasePri;
import cn.com.codes.object.CaseType;
import cn.com.codes.object.OutlineInfo;
import cn.com.codes.object.TestCasePackage;
import cn.com.codes.object.TestResult;


public class TestCasePackageDto  extends BaseDto {

	private List<Object[]> pgkResult;
	private TestCasePackage testCasePackage;
	private String queryParam;
	private String selectedUserIds;//可分配人id
	private String selectedTestCaseIds;//用例包关联的测试用例id
	
	private String packageId;
	private String taskId;
	private Long moduleId;
	private Long testCaseId;
	private String createrId;
	private String prefixCondition;
	private String testCaseDes;
	private Integer isReleased;
	private Date creatdate;
	private String attachUrl;
	private Long caseTypeId ;
	private Long priId ;
	private Integer weight=2;
	private Integer testStatus;
	private String typeName;
	private String priName;
	private String moduleNum;
	private String authorName;
	private String auditId;
	private String auditerNmae;
	private String taskName;
	private String packageIdJoin;

	public TestCasePackageDto(){
		
	}
	
	/**
	 * @param packageId
	 * @param taskId
	 * @param moduleId
	 * @param testCaseId
	 * @param createrId
	 * @param prefixCondition
	 * @param testCaseDes
	 * @param isReleased
	 * @param creatdate
	 * @param attachUrl
	 * @param caseTypeId
	 * @param priId
	 * @param weight
	 * @param testStatus
	 * @param moduleNum
	 * @param auditId
	 */
	public TestCasePackageDto(String packageId, String taskId, Long moduleId,
			Long testCaseId, String createrId, String prefixCondition,
			String testCaseDes, Integer isReleased, Date creatdate,
			String attachUrl, Long caseTypeId, Long priId, Integer weight,
			Integer testStatus,String moduleNum, String auditId) {
		super();
		this.packageId = packageId;
		this.taskId = taskId;
		this.moduleId = moduleId;
		this.testCaseId = testCaseId;
		this.createrId = createrId;
		this.prefixCondition = prefixCondition;
		this.testCaseDes = testCaseDes;
		this.isReleased = isReleased;
		this.creatdate = creatdate;
		this.attachUrl = attachUrl;
		this.caseTypeId = caseTypeId;
		this.priId = priId;
		this.weight = weight;
		this.testStatus = testStatus;
		this.moduleNum = moduleNum;
		this.auditId = auditId;
	}


	

	/**
	 * @return the pgkResult
	 */
	public List<Object[]> getPgkResult() {
		return pgkResult;
	}

	/**
	 * @param pgkResult the pgkResult to set
	 */
	public void setPgkResult(List<Object[]> pgkResult) {
		this.pgkResult = pgkResult;
	}

	/**
	 * @return the testCasePackage
	 */
	public TestCasePackage getTestCasePackage() {
		return testCasePackage;
	}

	/**
	 * @param testCasePackage the testCasePackage to set
	 */
	public void setTestCasePackage(TestCasePackage testCasePackage) {
		this.testCasePackage = testCasePackage;
	}

	/**
	 * @return the queryParam
	 */
	public String getQueryParam() {
		return queryParam;
	}

	/**
	 * @param queryParam the queryParam to set
	 */
	public void setQueryParam(String queryParam) {
		this.queryParam = queryParam;
	}

	/**
	 * @return the selectedUserIds
	 */
	public String getSelectedUserIds() {
		return selectedUserIds;
	}

	/**
	 * @param selectedUserIds the selectedUserIds to set
	 */
	public void setSelectedUserIds(String selectedUserIds) {
		this.selectedUserIds = selectedUserIds;
	}

	/**
	 * @return the selectedTestCaseIds
	 */
	public String getSelectedTestCaseIds() {
		return selectedTestCaseIds;
	}

	/**
	 * @param selectedTestCaseIds the selectedTestCaseIds to set
	 */
	public void setSelectedTestCaseIds(String selectedTestCaseIds) {
		this.selectedTestCaseIds = selectedTestCaseIds;
	}

	/**
	 * @return the packageId
	 */
	public String getPackageId() {
		return packageId;
	}

	/**
	 * @param packageId the packageId to set
	 */
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return the moduleId
	 */
	public Long getModuleId() {
		return moduleId;
	}

	/**
	 * @param moduleId the moduleId to set
	 */
	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	/**
	 * @return the testCaseId
	 */
	public Long getTestCaseId() {
		return testCaseId;
	}

	/**
	 * @param testCaseId the testCaseId to set
	 */
	public void setTestCaseId(Long testCaseId) {
		this.testCaseId = testCaseId;
	}

	/**
	 * @return the createrId
	 */
	public String getCreaterId() {
		return createrId;
	}

	/**
	 * @param createrId the createrId to set
	 */
	public void setCreaterId(String createrId) {
		this.createrId = createrId;
	}

	/**
	 * @return the prefixCondition
	 */
	public String getPrefixCondition() {
		return prefixCondition;
	}

	/**
	 * @param prefixCondition the prefixCondition to set
	 */
	public void setPrefixCondition(String prefixCondition) {
		this.prefixCondition = prefixCondition;
	}

	/**
	 * @return the testCaseDes
	 */
	public String getTestCaseDes() {
		return testCaseDes;
	}

	/**
	 * @param testCaseDes the testCaseDes to set
	 */
	public void setTestCaseDes(String testCaseDes) {
		this.testCaseDes = testCaseDes;
	}

	/**
	 * @return the isReleased
	 */
	public Integer getIsReleased() {
		return isReleased;
	}

	/**
	 * @param isReleased the isReleased to set
	 */
	public void setIsReleased(Integer isReleased) {
		this.isReleased = isReleased;
	}

	/**
	 * @return the creatdate
	 */
	public Date getCreatdate() {
		return creatdate;
	}

	/**
	 * @param creatdate the creatdate to set
	 */
	public void setCreatdate(Date creatdate) {
		this.creatdate = creatdate;
	}

	/**
	 * @return the attachUrl
	 */
	public String getAttachUrl() {
		return attachUrl;
	}

	/**
	 * @param attachUrl the attachUrl to set
	 */
	public void setAttachUrl(String attachUrl) {
		this.attachUrl = attachUrl;
	}

	/**
	 * @return the caseTypeId
	 */
	public Long getCaseTypeId() {
		return caseTypeId;
	}

	/**
	 * @param caseTypeId the caseTypeId to set
	 */
	public void setCaseTypeId(Long caseTypeId) {
		this.caseTypeId = caseTypeId;
	}

	/**
	 * @return the priId
	 */
	public Long getPriId() {
		return priId;
	}

	/**
	 * @param priId the priId to set
	 */
	public void setPriId(Long priId) {
		this.priId = priId;
	}

	/**
	 * @return the weight
	 */
	public Integer getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	/**
	 * @return the testStatus
	 */
	public Integer getTestStatus() {
		return testStatus;
	}

	/**
	 * @param testStatus the testStatus to set
	 */
	public void setTestStatus(Integer testStatus) {
		this.testStatus = testStatus;
	}

	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @return the priName
	 */
	public String getPriName() {
		return priName;
	}

	/**
	 * @param priName the priName to set
	 */
	public void setPriName(String priName) {
		this.priName = priName;
	}

	/**
	 * @return the moduleNum
	 */
	public String getModuleNum() {
		return moduleNum;
	}

	/**
	 * @param moduleNum the moduleNum to set
	 */
	public void setModuleNum(String moduleNum) {
		this.moduleNum = moduleNum;
	}

	/**
	 * @return the authorName
	 */
	public String getAuthorName() {
		return authorName;
	}

	/**
	 * @param authorName the authorName to set
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	/**
	 * @return the auditId
	 */
	public String getAuditId() {
		return auditId;
	}

	/**
	 * @param auditId the auditId to set
	 */
	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	/**
	 * @return the auditerNmae
	 */
	public String getAuditerNmae() {
		return auditerNmae;
	}

	/**
	 * @param auditerNmae the auditerNmae to set
	 */
	public void setAuditerNmae(String auditerNmae) {
		this.auditerNmae = auditerNmae;
	}

	/**
	 * @return the taskName
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * @param taskName the taskName to set
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	/**  
	* @return packageIdJoin 
	*/
	public String getPackageIdJoin() {
		return packageIdJoin;
	}

	/**  
	* @param packageIdJoin packageIdJoin 
	*/
	public void setPackageIdJoin(String packageIdJoin) {
		this.packageIdJoin = packageIdJoin;
	}

	
}
