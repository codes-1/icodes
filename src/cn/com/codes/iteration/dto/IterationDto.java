package cn.com.codes.iteration.dto;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.codes.common.dto.PageModel;
import cn.com.codes.framework.transmission.dto.BaseDto;
import cn.com.codes.object.BugBaseInfo;
import cn.com.codes.object.IterationList;
import cn.com.codes.object.OtherMission;
import cn.com.codes.object.SingleTestTask;
import cn.com.codes.object.TestCasePackage;
import cn.com.codes.object.UserTestCasePkg;


public class IterationDto extends BaseDto {

	private static final long serialVersionUID = 1L;
	
	//迭代列表
	private IterationList iterationList;
	
	private SingleTestTask singleTestTask;
	private BugBaseInfo bugBaseInfo;
	private OtherMission otherMission;
	private TestCasePackage testCasePackage;
	private String bugCardId;
	private String testCaseP;
	private String otherMissionS;
	private List<Map<String, Object>> baseInfos;
	private List<Map<String, Object>> missions;
	private List<Map<String, Object>> casePackages;
	private List<IterationList> iterationLists;
	private List<BugBaseInfo> bugBaseInfos;
	private PageModel pageModel;
	private List<UserTestCasePkg> userTestCasePkgs;
	
	public SingleTestTask getSingleTestTask() {
		return singleTestTask;
	}

	public void setSingleTestTask(SingleTestTask singleTestTask) {
		this.singleTestTask = singleTestTask;
	}

	public BugBaseInfo getBugBaseInfo() {
		return bugBaseInfo;
	}

	public void setBugBaseInfo(BugBaseInfo bugBaseInfo) {
		this.bugBaseInfo = bugBaseInfo;
	}

	public OtherMission getOtherMission() {
		return otherMission;
	}

	public void setOtherMission(OtherMission otherMission) {
		this.otherMission = otherMission;
	}

	public TestCasePackage getTestCasePackage() {
		return testCasePackage;
	}

	public void setTestCasePackage(TestCasePackage testCasePackage) {
		this.testCasePackage = testCasePackage;
	}

	public IterationList getIterationList() {
		return iterationList;
	}

	public void setIterationList(IterationList iterationList) {
		this.iterationList = iterationList;
	}

	/**  
	* @return bugCardId 
	*/
	public String getBugCardId() {
		return bugCardId;
	}

	/**  
	* @param bugCardId bugCardId 
	*/
	public void setBugCardId(String bugCardId) {
		this.bugCardId = bugCardId;
	}

	/**  
	* @return testCaseP 
	*/
	public String getTestCaseP() {
		return testCaseP;
	}

	/**  
	* @param testCaseP testCaseP 
	*/
	public void setTestCaseP(String testCaseP) {
		this.testCaseP = testCaseP;
	}

	/**  
	* @return otherMissionS 
	*/
	public String getOtherMissionS() {
		return otherMissionS;
	}

	/**  
	* @param otherMissionS otherMissionS 
	*/
	public void setOtherMissionS(String otherMissionS) {
		this.otherMissionS = otherMissionS;
	}

	/**  
	* @return iterationLists 
	*/
	public List<IterationList> getIterationLists() {
		return iterationLists;
	}

	/**  
	* @param iterationLists iterationLists 
	*/
	public void setIterationLists(List<IterationList> iterationLists) {
		this.iterationLists = iterationLists;
	}

	/**  
	 * @return baseInfos 
	 */
	public List<Map<String, Object>> getBaseInfos() {
		return baseInfos;
	}

	/**  
	 * @param baseInfos baseInfos 
	 */
	public void setBaseInfos(List<Map<String, Object>> baseInfos) {
		this.baseInfos = baseInfos;
	}

	/**  
	 * @return missions 
	 */
	public List<Map<String, Object>> getMissions() {
		return missions;
	}

	/**  
	 * @param missions missions 
	 */
	public void setMissions(List<Map<String, Object>> missions) {
		this.missions = missions;
	}

	/**  
	 * @return casePackages 
	 */
	public List<Map<String, Object>> getCasePackages() {
		return casePackages;
	}

	/**  
	 * @param casePackages casePackages 
	 */
	public void setCasePackages(List<Map<String, Object>> casePackages) {
		this.casePackages = casePackages;
	}

	/**  
	* @return bugBaseInfos 
	*/
	public List<BugBaseInfo> getBugBaseInfos() {
		return bugBaseInfos;
	}

	/**  
	* @param bugBaseInfos bugBaseInfos 
	*/
	public void setBugBaseInfos(List<BugBaseInfo> bugBaseInfos) {
		this.bugBaseInfos = bugBaseInfos;
	}

	/**  
	* @return pageModel 
	*/
	public PageModel getPageModel() {
		return pageModel;
	}

	/**  
	* @param pageModel pageModel 
	*/
	public void setPageModel(PageModel pageModel) {
		this.pageModel = pageModel;
	}

	/**  
	* @return userTestCasePkgs 
	*/
	public List<UserTestCasePkg> getUserTestCasePkgs() {
		return userTestCasePkgs;
	}

	/**  
	* @param userTestCasePkgs userTestCasePkgs 
	*/
	public void setUserTestCasePkgs(List<UserTestCasePkg> userTestCasePkgs) {
		this.userTestCasePkgs = userTestCasePkgs;
	}

	
	
}
