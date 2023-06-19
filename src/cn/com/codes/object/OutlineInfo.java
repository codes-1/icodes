package cn.com.codes.object;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import cn.com.codes.framework.transmission.JsonInterface;
import cn.com.codes.object.OutlineTeamMember;
import cn.com.codes.object.TestCaseInfo;
import cn.com.codes.object.User;



public class OutlineInfo implements JsonInterface {

	/**  
	* 字段:      字段名称
	* @Fields serialVersionUID : TODO 
	*/
	private static final long serialVersionUID = 1L;
	private Long moduleId;
	private String taskId;
	private Integer testPhase;
	private String moduleName;
	private Integer moduleLevel;
	private Integer isleafNode;
	private Long superModuleId;
	private Integer moduleState;
	private Long klc;
	private Date insdate;
	private Date upddate;
	private String companyId;
	private String moduleNum;
	private String currVer;
	private Set<TestCaseInfo> testCase ;
	private Set<OutlineTeamMember> teamMember ;
	private Set<User> devMember ;
	private OutlineTeamMember member;
	private Integer caseCount;
	private Double quotiety; 
	private Integer reqType;
	private Integer scrpCount;
	private Integer sceneCount;
	private Integer seq;
	//导入用例时，用这个来记录父模块
	private String superModuleName;
	
	public OutlineInfo() {
	}

	public OutlineInfo(String moduleNum) {
		this.moduleNum = moduleNum;
	}
	public OutlineInfo(Integer seq,Long moduleId) {
		this.moduleId = moduleId;
		this.seq = seq;
	}
	public OutlineInfo(Long moduleId) {
		this.moduleId = moduleId;
	}	
	public OutlineInfo(String moduleNum,Long moduleId) {
		this.moduleNum = moduleNum;
		this.moduleId = moduleId;
	}
	public OutlineInfo(Long moduleId,String moduleName) {
		this.moduleId = moduleId;
		this.moduleName = moduleName;
	}
	public OutlineInfo(Long moduleId,Set<OutlineTeamMember> teamMember) {
		this.moduleId = moduleId;
		this.teamMember = teamMember;
	}
	public OutlineInfo(Long moduleId,OutlineTeamMember member) {
		this.moduleId = moduleId;
		this.member = member;
	}
	public OutlineInfo(Long moduleId,Integer moduleLevel) {
		this.moduleId = moduleId;
		this.moduleLevel = moduleLevel;
	}
	
	public OutlineInfo(Long moduleId,Long superModuleId,Integer isleafNode, String moduleName,Integer moduleState,String moduleNum,Integer moduleLevel){
		
		this.moduleId = moduleId;
		this.superModuleId = superModuleId;
		this.isleafNode = isleafNode;
		this.moduleName =  moduleName;
		this.moduleState = moduleState;
		this.moduleNum = moduleNum;
		this.moduleLevel = moduleLevel;
		
		
	}
	public OutlineInfo(String moduleName,Long moduleId,Integer moduleLevel,Long superModuleId) {
		this.moduleId = moduleId;
		this.moduleLevel = moduleLevel;
		this.moduleName = moduleName;
		this.superModuleId = superModuleId;
	}
	public OutlineInfo(Long moduleId,Long superModuleId,Integer moduleLevel) {
		this.moduleId = moduleId;
		this.superModuleId = superModuleId;
		this.moduleLevel = moduleLevel;
	}
	public OutlineInfo(Long moduleId,Integer moduleLevel,String moduleNum) {
		this.moduleId = moduleId;
		this.moduleLevel = moduleLevel;
		this.moduleNum = moduleNum;
	}
	public OutlineInfo(Long moduleId,Integer moduleLevel,String moduleNum,String moduleName) {
		this.moduleId = moduleId;
		this.moduleLevel = moduleLevel;
		this.moduleNum = moduleNum;
		this.moduleName = moduleName;
	}
	public OutlineInfo(Long moduleId,Long superModuleId,Integer moduleLevel,Integer isleafNode) {
		this.moduleId = moduleId;
		this.superModuleId = superModuleId;
		this.moduleLevel = moduleLevel;
		this.isleafNode = isleafNode;
	}
	public OutlineInfo(Long moduleId,Long superModuleId,Integer isleafNode,String moduleName,Integer moduleState,Integer moduleLevel,String moduleNum) {
		this.moduleId = moduleId;
		this.superModuleId = superModuleId;
		this.isleafNode = isleafNode;
		this.moduleName = moduleName;
		this.moduleState = moduleState;
		this.moduleLevel = moduleLevel;
		this.moduleNum = moduleNum;
	}
	public OutlineInfo(Long moduleId,Long superModuleId,Integer isleafNode,String moduleName,
			Integer moduleState,String moduleNum,Long klc,Integer moduleLevel) {
		this.moduleId = moduleId;
		this.superModuleId = superModuleId;
		this.isleafNode = isleafNode;
		this.moduleName = moduleName;
		this.moduleState = moduleState;
		this.moduleNum = moduleNum;
		this.klc = klc;
		this.moduleLevel = moduleLevel;
	}	
	public OutlineInfo(Long moduleId,Long superModuleId,Integer isleafNode,String moduleName,
			Integer moduleState,String moduleNum,Long klc,Integer moduleLevel,Integer caseCount,Double quotiety ,Integer scrpCount,Integer sceneCount,Integer reqType) {
		this.moduleId = moduleId;
		this.superModuleId = superModuleId;
		this.isleafNode = isleafNode;
		this.moduleName = moduleName;
		this.moduleState = moduleState;
		this.moduleNum = moduleNum;
		this.klc = klc;
		this.moduleLevel = moduleLevel;
		this.caseCount = caseCount;
		this.quotiety = quotiety;
		this.scrpCount = scrpCount;
		this.sceneCount = sceneCount;
		this.reqType = reqType;
		
	}	
	public OutlineInfo(Integer testPhase, Integer moduleLevel, Integer isleafNode,
			Integer moduleState, String companyId) {
		this.testPhase = testPhase;
		this.moduleLevel = moduleLevel;
		this.isleafNode = isleafNode;
		this.moduleState = moduleState;
		this.companyId = companyId;
	}

	public OutlineInfo(Long moduleId,Long superModuleId,Integer isleafNode,String moduleName,Integer moduleState,String moduleNum){
		this.moduleId = moduleId;
		this.superModuleId = superModuleId;
		this.isleafNode = isleafNode;
		this.moduleName = moduleName;
		this.moduleState = moduleState;
		this.moduleNum = moduleNum;
	}

	
	public Date getInsdate() {
		return this.insdate;
	}

	public void setInsdate(Date insdate) {
		this.insdate = insdate;
	}

	public Date getUpddate() {
		return this.upddate;
	}

	public void setUpddate(Date upddate) {
		this.upddate = upddate;
	}


	public Long getModuleId() {
		return moduleId;
	}


	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
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


	public String getModuleName() {
		return moduleName;
	}


	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}


	public Integer getModuleLevel() {
		return moduleLevel;
	}


	public void setModuleLevel(Integer moduleLevel) {
		this.moduleLevel = moduleLevel;
	}


	public Integer getIsleafNode() {
		return isleafNode;
	}


	public void setIsleafNode(Integer isleafNode) {
		this.isleafNode = isleafNode;
	}


	public Long getSuperModuleId() {
		return superModuleId;
	}


	public void setSuperModuleId(Long superModuleId) {
		this.superModuleId = superModuleId;
	}


	public Integer getModuleState() {
		return moduleState;
	}


	public void setModuleState(Integer moduleState) {
		this.moduleState = moduleState;
	}


	public java.lang.Long getKlc() {
		return klc;
	}


	public void setKlc(java.lang.Long klc) {
		this.klc = klc;
	}


	public String getCompanyId() {
		return companyId;
	}


	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}


	public Set<OutlineTeamMember> getTeamMember() {
		return teamMember;
	}


	public void setTeamMember(Set<OutlineTeamMember> teamMember) {
		this.teamMember = teamMember;
	}


	public String getModuleNum() {
		return moduleNum;
	}


	public void setModuleNum(String moduleNum) {
		this.moduleNum = moduleNum;
	}


	public Set<TestCaseInfo> getTestCase() {
		return testCase;
	}

	public String getCurrVer() {
		return currVer;
	}

	public void setCurrVer(String currVer) {
		this.currVer = currVer;
	}

	public Set<User> getDevMember() {
		return devMember;
	}

	public void setDevMember(Set<User> devMember) {
		this.devMember = devMember;
	}

	public void setTestCase(Set<TestCaseInfo> testCase) {
		this.testCase = testCase;
	}
	
	public OutlineTeamMember getMember() {
		return member;
	}

	public void setMember(OutlineTeamMember member) {
		this.member = member;
	}

	public Integer getCaseCount() {
		return caseCount;
	}

	public void setCaseCount(Integer caseCount) {
		this.caseCount = caseCount;
	}

	public Double getQuotiety() {
		return quotiety;
	}

	public void setQuotiety(Double quotiety) {
		this.quotiety = quotiety;
	}

	public Integer getReqType() {
		return reqType;
	}

	public void setReqType(Integer reqType) {
		this.reqType = reqType;
	}

	public Integer getScrpCount() {
		return scrpCount;
	}

	public void setScrpCount(Integer scrpCount) {
		this.scrpCount = scrpCount;
	}

	public Integer getSceneCount() {
		return sceneCount;
	}

	public void setSceneCount(Integer sceneCount) {
		this.sceneCount = sceneCount;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

   public String toStrList(){
        StringBuffer sbf = new StringBuffer();
        sbf.append("{");
        sbf.append("id:'");
        sbf.append(getModuleId().toString());
        sbf.append("',data: [0,'','");
        sbf.append(moduleName == null ? "" : moduleName );
        sbf.append("','");
        sbf.append(klc == null? "" : this.moduleLevel==2?klc:"" );
        sbf.append("','");
        sbf.append(teamMember == null ? "" :  devTeamMember2Str(teamMember));
        sbf.append("','");
        sbf.append(isleafNode == null ? "" : isleafNode );
        sbf.append("'");
        sbf.append("]");
        sbf.append("}");
        return sbf.toString();
    }
   	

	   
    public String toStrUpdateInit(){
    	 return null;
    }

    public String toStrUpdateRest(){
        return null;
 }
    private String devTeamMember2Str(Set<OutlineTeamMember> teamMember){
    	if(teamMember.size()==0){
    		return " ',' ";
    	}
    	StringBuffer sbIds = new StringBuffer(" ");
    	StringBuffer sbNames = new StringBuffer();
    	int i = 0 ;
		for (Iterator<OutlineTeamMember> it = teamMember.iterator(); it.hasNext();){
			OutlineTeamMember member = it.next();
			if(member.getUserRole()!=1)
				continue;
			if(i>0){
				sbNames.append(" ");
				sbIds.append("_");
			}
			sbNames.append(member.getUser().getName());
			sbIds.append(member.getUser().getId());
			i++;
		}
		if(!sbIds.toString().equals(" "))
			sbIds.append("','").append(sbNames.toString().trim());
		else
			return " ',' ";
    	return sbIds.toString().trim();
    }
    private String devLTeamMember2Str(Set<OutlineTeamMember> teamMember){
    	if(teamMember.size()==0){
    		return " ',' ";
            //sbf.append(" ");
            //sbf.append("','");
            //sbf.append(" ");
    	}
    	StringBuffer sbIds = new StringBuffer(" ");
    	StringBuffer sbNames = new StringBuffer();
    	int i = 0 ;
		for (Iterator<OutlineTeamMember> it = teamMember.iterator(); it.hasNext();){
			OutlineTeamMember member = it.next();
			if(member.getUserRole()!=3)
				continue;
			if(i>0){
				sbNames.append(" ");
				sbIds.append("_");
			}
			sbNames.append(member.getUser().getName());
			sbIds.append(member.getUser().getId());
			i++;
		}
		if(!sbIds.toString().equals(" "))
			sbIds.append("','").append(sbNames.toString().trim());
		else
			return " ',' ";
    	return sbIds.toString().trim();
    }
	public void toString(StringBuffer sbf) {
        sbf.append("{");
        sbf.append("id:'");
        sbf.append(getModuleId().toString());
        sbf.append("',data: [0,'','");
        sbf.append(moduleName == null ? "" : moduleName );
        sbf.append("','");
        sbf.append(klc == null? "" : this.moduleLevel==2?klc:"" );
        sbf.append("','");
        sbf.append(isleafNode == null ? "" : isleafNode );
        sbf.append("','");
        sbf.append(quotiety == null? "":quotiety);
        sbf.append("','");
        sbf.append(caseCount == null ? "" : caseCount);
        sbf.append("','");
        sbf.append(teamMember == null ? "" :  devTeamMember2Str(teamMember));
        sbf.append("','");
        sbf.append(teamMember == null ? "" :  devLTeamMember2Str(teamMember));
        //sbf.append(" ");
        //sbf.append("','");
        //sbf.append(" ");
        sbf.append("','");
        sbf.append((scrpCount == null ||scrpCount==0)? "" :  scrpCount);
        sbf.append("','");
        sbf.append((sceneCount == null ||sceneCount==0)? "" :  sceneCount);
        sbf.append("','");
        sbf.append(reqType == null ? "0" :  reqType);
        sbf.append("'");
        sbf.append("]");
        sbf.append("}");	
		
	}

	public String getSuperModuleName() {
		return superModuleName;
	}

	public void setSuperModuleName(String superModuleName) {
		this.superModuleName = superModuleName;
	}


}