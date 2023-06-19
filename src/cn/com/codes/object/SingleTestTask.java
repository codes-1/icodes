package cn.com.codes.object;

import java.util.Date;
import java.util.Set;

import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.transmission.JsonInterface;
import cn.com.codes.singleTestTaskManager.dto.TestLeaderVo;
import cn.com.codes.object.SimpleUser;
import cn.com.codes.object.TaskUseActor;
import cn.com.codes.object.TestTaskDetail;



public class SingleTestTask implements JsonInterface {

	private String taskId;
	private Date insDate;
	private Date updDate;
	private String companyId;
	private String createId;
	private String psmId;
	private String proName;
	private String proNum;
	private String devDept;
	private Integer status;
	private Date planStartDate;
	private Date planEndDate;
	private Date factStartDate;
	private Date factEndDate;
	private Integer testPhase;
	private String psmName;
	private SimpleUser psm;
	private String planDocName;
	private TestTaskDetail testTaskDetal;
	private Set<SimpleUser> testLds;
	private Set<TestLeaderVo> testLdVos;
	private Set<TaskUseActor> useActor ;
	private String taskType;
	private String filterFlag;
	private String taskProjectId;
	private Integer outlineState;

	public SingleTestTask() {
	}

	public SingleTestTask(String taskId,String proNum,String proName,String devDept,
			Integer testPhase,String psmName,Date planStartDate,
			Date planEndDate,Integer status,String planDocName) {
		this.taskId = taskId;
		this.proNum = proNum;
		this.proName = proName ;
		this.devDept = devDept;
		this.testPhase = testPhase;
		this.psmName = psmName;
		this.planStartDate = planStartDate;
		this.planEndDate = planEndDate;
		this.status = status;	
		this.planDocName = planDocName;
	}
	public SingleTestTask(String taskId,String proName,String proNum) {
		this.taskId = taskId;
		this.proNum = proNum;
		this.proName = proName ;
	}
	public SingleTestTask(String taskId,String proName,String proNum,String taskProjectId) {
		this.taskId = taskId;
		this.proNum = proNum;
		this.proName = proName ;
		this.taskProjectId = taskProjectId;
	}
	public SingleTestTask(String taskId,String proName) {
		this.taskId = taskId;
		this.proName = proName ;
	}
	

	public String getCreateId() {
		return this.createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public Set<SimpleUser> getTestLds() {
		return testLds;
	}



	public void setTestLds(Set<SimpleUser> testLds) {
		this.testLds = testLds;
	}
	public SimpleUser getPsm() {
		return this.psm;
	}

	public void setCreateId(SimpleUser psm) {
		this.psm = psm;
	}
	public String getPsmId() {
		return this.psmId;
	}

	public void setPsmId(String psmId) {
		this.psmId = psmId;
	}

	public String getProName() {
		return this.proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getProNum() {
		return this.proNum;
	}

	public void setProNum(String proNum) {
		this.proNum = proNum;
	}

	public String getDevDept() {
		return this.devDept;
	}

	public void setDevDept(String devDept) {
		this.devDept = devDept;
	}



	public String getTaskId() {
		return taskId;
	}



	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}



	public Date getInsDate() {
		return insDate;
	}



	public void setInsDate(Date insDate) {
		this.insDate = insDate;
	}



	public Date getUpdDate() {
		return updDate;
	}



	public void setUpdDate(Date updDate) {
		this.updDate = updDate;
	}



	public String getCompanyId() {
		return companyId;
	}



	public Integer getStatus() {
		return status;
	}



	public void setStatus(Integer status) {
		this.status = status;
	}



	public Date getPlanStartDate() {
		return planStartDate;
	}



	public void setPlanStartDate(Date planStartDate) {
		this.planStartDate = planStartDate;
	}



	public Date getPlanEndDate() {
		return planEndDate;
	}



	public void setPlanEndDate(Date planEndDate) {
		this.planEndDate = planEndDate;
	}



	public Date getFactStartDate() {
		return factStartDate;
	}



	public void setFactStartDate(Date factStartDate) {
		this.factStartDate = factStartDate;
	}



	public Date getFactEndDate() {
		return factEndDate;
	}



	public void setFactEndDate(Date factEndDate) {
		this.factEndDate = factEndDate;
	}



	public Integer getTestPhase() {
		return testPhase;
	}

	public void setTestPhase(Integer testPhase) {
		this.testPhase = testPhase;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	


	public String getPlanDocName() {
		return planDocName;
	}



	public void setPlanDocName(String planDocName) {
		this.planDocName = planDocName;
	}



	public void setPsm(SimpleUser psm) {
		this.psm = psm;
	}



	public TestTaskDetail getTestTaskDetal() {
		return testTaskDetal;
	}



	public void setTestTaskDetal(TestTaskDetail testTaskDetal) {
		this.testTaskDetal = testTaskDetal;
	}

	public String getPsmName() {
		return psmName;
	}

	public void setPsmName(String psmName) {
		this.psmName = psmName;
	}
	public Set<TestLeaderVo> getTestLdVos() {
		return testLdVos;
	}

	public void setTestLdVos(Set<TestLeaderVo> testLdVos) {
		this.testLdVos = testLdVos;
	}

	public Set<TaskUseActor> getUseActor() {
		return useActor;
	}

	public void setUseActor(Set<TaskUseActor> useActor) {
		this.useActor = useActor;
	}
	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}	
   public String toStrList(){
        StringBuffer sbf = new StringBuffer();
        sbf.append("{");
        sbf.append("id:'");
        sbf.append(getTaskId().toString());
        sbf.append("',data: [0,'','");
        sbf.append(insDate == null ? "" : StringUtils.formatShortDate(insDate ));
        sbf.append("','");
        sbf.append(updDate == null ? "" : StringUtils.formatShortDate(updDate ));
        sbf.append("','");
        sbf.append(companyId == null ? "" : companyId );
        sbf.append("','");
        sbf.append(createId == null ? "" : createId );
        sbf.append("','");
        sbf.append(psmId == null ? "" : psmId );
        sbf.append("','");
        sbf.append(proName == null ? "" : proName );
        sbf.append("','");
        sbf.append(proNum == null ? "" : proNum );
        sbf.append("','");
        sbf.append(devDept == null ? "" : devDept );
        sbf.append("','");
        sbf.append(status == null ? "" : status );
        sbf.append("','");
        sbf.append(planStartDate == null ? "" : StringUtils.formatShortDate(planStartDate ));
        sbf.append("','");
        sbf.append(planEndDate == null ? "" : StringUtils.formatShortDate(planEndDate ));
        sbf.append("','");
        sbf.append(factStartDate == null ? "" : StringUtils.formatShortDate(factStartDate ));
        sbf.append("','");
        sbf.append(factEndDate == null ? "" : StringUtils.formatShortDate(factEndDate ));
        sbf.append("','");
        sbf.append(testPhase == null ? "" : testPhase );
        sbf.append("'");
        sbf.append("]");
        sbf.append("}");
        return sbf.toString();
    }

    public String toStrUpdateInit(){
        StringBuffer sbf = new StringBuffer();
        sbf.append("taskId=");
        sbf.append(getTaskId().toString());
        sbf.append("^");
        sbf.append("insDate=").append(StringUtils.formatShortDate(insDate));
        sbf.append("^");
        sbf.append("updDate=").append(StringUtils.formatShortDate(updDate));
        sbf.append("^");
        sbf.append("createId=").append( createId);
        sbf.append("^");
        sbf.append("psmId=").append(psmId);
        sbf.append("^");
        sbf.append("proName=").append(proName);
        sbf.append("^");
        sbf.append("proNum=").append(proNum);
        sbf.append("^");
        sbf.append("devDept=").append(devDept);
        sbf.append("^");
        sbf.append("status=").append(status);
        sbf.append("^");
        sbf.append("planStartDate=").append(StringUtils.formatShortDate(planStartDate));
        sbf.append("^");
        sbf.append("planEndDate=").append(StringUtils.formatShortDate(planEndDate));
        sbf.append("^");
        sbf.append("attachUrl=").append(planDocName == null ? "" : planDocName);
        sbf.append("^");
        sbf.append("testPhase=").append( testPhase);
        sbf.append("^");
        sbf.append("psmName=").append(psm.getLoginName()).append("(").append(psm.getName()).append("");
        return sbf.toString();
    }

    public String toStrUpdateRest(){
        StringBuffer sbf = new StringBuffer();
        sbf.append(getTaskId().toString());
        sbf.append("^");
        sbf.append("0,,");
        sbf.append(proNum );
        sbf.append(",");
        sbf.append( proName );
        sbf.append(",");
        sbf.append(devDept );
        sbf.append(",");
        sbf.append(testPhase == 1 ? "集成测试" : (testPhase==0?"单元测试":"系统测试"));
        sbf.append(",");
        sbf.append(this.psmName);
        sbf.append(",");
        sbf.append(StringUtils.formatShortDate(planStartDate ));
        sbf.append(",");
        sbf.append(StringUtils.formatShortDate(planEndDate ));
        sbf.append(",");
        sbf.append(planDocName == null ? "" : planDocName );
        sbf.append(",");
        sbf.append(convertStatus(status));
        sbf.append(",");
        sbf.append( "");
        sbf.append(",");
        sbf.append(taskType == null ? "" : taskType );
        return sbf.toString();
     }

    public void toString(StringBuffer sbf){
        sbf.append("{");
        sbf.append("id:'");
        sbf.append(getTaskId().toString());
        sbf.append("',data: [0,'','");
        sbf.append(proNum );
        sbf.append("','");
        sbf.append( proName );
        sbf.append("','");
        sbf.append(devDept );
        sbf.append("','");
        sbf.append(testPhase == 1 ? "集成测试" : (testPhase==0?"单元测试":"系统测试"));
        sbf.append("','");
        sbf.append(this.psmName == null ? "" : psmName);
        sbf.append("','");
        sbf.append(StringUtils.formatShortDate(planStartDate ));
        sbf.append("','");
        sbf.append(StringUtils.formatShortDate(planEndDate ));
        sbf.append("','");
        sbf.append(planDocName == null ? "" : planDocName );
        sbf.append("','");
        sbf.append(convertStatus(status));
        sbf.append("','");
        if(testLdVos==null||testLdVos.isEmpty()){
        	sbf.append( "");
        }else{
        	this.userJsonConvert(sbf);
        }
        sbf.append("','");
        sbf.append(taskType == null ? "" : taskType );
        sbf.append("'");
        sbf.append("]");
        sbf.append("}");
    }

    private String convertStatus(Integer status){
    	if(status==0){
    		return "进行中";
    	}else if(status==1){
    		return "完成";
    	}else if(status==2){
    		return "结束";
    	}else if(status==3){
    		return "准备";
    	}
    	return "";
    }
    
	private void userJsonConvert(StringBuffer sbf){
		int i=1;
		for(TestLeaderVo usr :testLdVos){
			if(i>1){
				sbf.append("，").append(usr.getName()).append("(").append(usr.getLoginName()).append(")");
			}else{
				sbf.append(usr.getLoginName()).append("(").append(usr.getName()).append(")");
			}
			i++;
		}
	}

	/**  
	* @return filterFlag 
	*/
	public String getFilterFlag() {
		return filterFlag;
	}

	/**  
	* @param filterFlag filterFlag 
	*/
	public void setFilterFlag(String filterFlag) {
		this.filterFlag = filterFlag;
	}

	/**  
	* @return taskProjectId 
	*/
	public String getTaskProjectId() {
		return taskProjectId;
	}

	/**  
	* @param taskProjectId taskProjectId 
	*/
	public void setTaskProjectId(String taskProjectId) {
		this.taskProjectId = taskProjectId;
	}

	/**  
	* @return outlineState 
	*/
	public Integer getOutlineState() {
		return outlineState;
	}

	/**  
	* @param outlineState outlineState 
	*/
	public void setOutlineState(Integer outlineState) {
		this.outlineState = outlineState;
	}


}