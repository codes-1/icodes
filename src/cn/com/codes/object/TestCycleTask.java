package cn.com.codes.object;

import java.util.Date;

import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.transmission.JsonInterface;

public class TestCycleTask implements JsonInterface {

	private Long id;
	private String name;
	private String ownerId;
	private Integer status;
	private String taskId;
	private String exeEnv;
	private Integer cycleType;
	private Date planStartDate;
	private Date planEndDate;
	private Date factStartDate;
	private Date factEndDate;
	private Date createDate;
	private String createId;
	private String remark;
	private String result;
	private String updateId;
	private Date updateDate;
	private String attachUrl;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getExeEnv() {
		return exeEnv;
	}
	public void setExeEnv(String exeEnv) {
		this.exeEnv = exeEnv;
	}
	public Integer getCycleType() {
		return cycleType;
	}
	public void setCycleType(Integer cycleType) {
		this.cycleType = cycleType;
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getUpdateId() {
		return updateId;
	}
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	public String toStrList(){
	        StringBuffer sbf = new StringBuffer();
	        sbf.append("{");
	        sbf.append("id:'");
	        sbf.append(getId().toString());
	        sbf.append("',data: [0,'','");
	        sbf.append(name == null ? "" : name );
	        sbf.append("','");
	        sbf.append(ownerId == null ? "" : ownerId );
	        sbf.append("','");
	        sbf.append(status == null ? "" : status );
	        sbf.append("','");
	        sbf.append(taskId == null ? "" : taskId );
	        sbf.append("','");
	        sbf.append(exeEnv == null ? "" : exeEnv );
	        sbf.append("','");
	        sbf.append(cycleType == null ? "" : cycleType );
	        sbf.append("','");
	        sbf.append(planStartDate == null ? "" : StringUtils.formatShortDate(planStartDate ));
	        sbf.append("','");
	        sbf.append(planEndDate == null ? "" : StringUtils.formatShortDate(planEndDate ));
	        sbf.append("','");
	        sbf.append(factStartDate == null ? "" : StringUtils.formatShortDate(factStartDate ));
	        sbf.append("','");
	        sbf.append(factEndDate == null ? "" : StringUtils.formatShortDate(factEndDate ));
	        sbf.append("','");
	        sbf.append(createDate == null ? "" : StringUtils.formatShortDate(createDate ));
	        sbf.append("','");
	        sbf.append(createId == null ? "" : createId );
	        sbf.append("','");
	        sbf.append(remark == null ? "" : remark );
	        sbf.append("','");
	        sbf.append(result == null ? "" : result );
	        sbf.append("','");
	        sbf.append(updateId == null ? "" : updateId );
	        sbf.append("','");
	        sbf.append(updateDate == null ? "" : StringUtils.formatShortDate(updateDate ));
	        sbf.append("'");
	        sbf.append("]");
	        sbf.append("}");
	        return sbf.toString();
	   }

	   public String toStrUpdateInit(){
	        StringBuffer sbf = new StringBuffer();
	        sbf.append("id=");
	        sbf.append(getId().toString());
	        sbf.append("^");
	        sbf.append("name=").append(name == null ? "" : name);
	        sbf.append("^");
	        sbf.append("ownerId=").append(ownerId == null ? "" : ownerId);
	        sbf.append("^");
	        sbf.append("status=").append(status == null ? "" : status);
	        sbf.append("^");
	        sbf.append("taskId=").append(taskId == null ? "" : taskId);
	        sbf.append("^");
	        sbf.append("exeEnv=").append(exeEnv == null ? "" : exeEnv);
	        sbf.append("^");
	        sbf.append("cycleType=").append(cycleType == null ? "" : cycleType);
	        sbf.append("^");
	        sbf.append("planStartDate=").append(planStartDate == null ? "" : StringUtils.formatShortDate(planStartDate));
	        sbf.append("^");
	        sbf.append("planEndDate=").append(planEndDate == null ? "" : StringUtils.formatShortDate(planEndDate));
	        sbf.append("^");
	        sbf.append("factStartDate=").append(factStartDate == null ? "" : StringUtils.formatShortDate(factStartDate));
	        sbf.append("^");
	        sbf.append("factEndDate=").append(factEndDate == null ? "" : StringUtils.formatShortDate(factEndDate));
	        sbf.append("^");
	        sbf.append("createDate=").append(createDate == null ? "" : StringUtils.formatShortDate(createDate));
	        sbf.append("^");
	        sbf.append("createId=").append(createId == null ? "" : createId);
	        sbf.append("^");
	        sbf.append("remark=").append(remark == null ? "" : remark);
	        sbf.append("^");
	        sbf.append("result=").append(result == null ? "" : result);
	        sbf.append("^");
	        sbf.append("updateId=").append(updateId == null ? "" : updateId);
	        sbf.append("^");
	        sbf.append("updateDate=").append(updateDate == null ? "" : StringUtils.formatShortDate(updateDate));
	        return sbf.toString();
	   }

	   
	   public String toStrUpdateRest(){
	        StringBuffer sbf = new StringBuffer();
	        sbf.append(getId().toString());
	        sbf.append("^");
	        sbf.append("0,,");
	        sbf.append(name == null ? "" : name );
	        sbf.append(",");
	        sbf.append(ownerId == null ? "" : ownerId );
	        sbf.append(",");
	        sbf.append(status == null ? "" : status );
	        sbf.append(",");
	        sbf.append(taskId == null ? "" : taskId );
	        sbf.append(",");
	        sbf.append(exeEnv == null ? "" : exeEnv );
	        sbf.append(",");
	        sbf.append(cycleType == null ? "" : cycleType );
	        sbf.append(",");
	        sbf.append(planStartDate == null ? "" : StringUtils.formatShortDate(planStartDate ));
	        sbf.append(",");
	        sbf.append(planEndDate == null ? "" : StringUtils.formatShortDate(planEndDate ));
	        sbf.append(",");
	        sbf.append(factStartDate == null ? "" : StringUtils.formatShortDate(factStartDate ));
	        sbf.append(",");
	        sbf.append(factEndDate == null ? "" : StringUtils.formatShortDate(factEndDate ));
	        sbf.append(",");
	        sbf.append(createDate == null ? "" : StringUtils.formatShortDate(createDate ));
	        sbf.append(",");
	        sbf.append(createId == null ? "" : createId );
	        sbf.append(",");
	        sbf.append(remark == null ? "" : remark );
	        sbf.append(",");
	        sbf.append(result == null ? "" : result );
	        sbf.append(",");
	        sbf.append(updateId == null ? "" : updateId );
	        sbf.append(",");
	        sbf.append(updateDate == null ? "" : StringUtils.formatShortDate(updateDate ));
	        return sbf.toString();
	     }

	   public void toString(StringBuffer sbf){
	        sbf.append("{");
	        sbf.append("id:'");
	        sbf.append(getId().toString());
	        sbf.append("',data: [0,'','");
	        sbf.append(name == null ? "" : name );
	        sbf.append("','");
	        sbf.append(ownerId == null ? "" : ownerId );
	        sbf.append("','");
	        sbf.append(status == null ? "" : status );
	        sbf.append("','");
	        sbf.append(taskId == null ? "" : taskId );
	        sbf.append("','");
	        sbf.append(exeEnv == null ? "" : exeEnv );
	        sbf.append("','");
	        sbf.append(cycleType == null ? "" : cycleType );
	        sbf.append("','");
	        sbf.append(planStartDate == null ? "" : StringUtils.formatShortDate(planStartDate ));
	        sbf.append("','");
	        sbf.append(planEndDate == null ? "" : StringUtils.formatShortDate(planEndDate ));
	        sbf.append("','");
	        sbf.append(factStartDate == null ? "" : StringUtils.formatShortDate(factStartDate ));
	        sbf.append("','");
	        sbf.append(factEndDate == null ? "" : StringUtils.formatShortDate(factEndDate ));
	        sbf.append("','");
	        sbf.append(createDate == null ? "" : StringUtils.formatShortDate(createDate ));
	        sbf.append("','");
	        sbf.append(createId == null ? "" : createId );
	        sbf.append("','");
	        sbf.append(remark == null ? "" : remark );
	        sbf.append("','");
	        sbf.append(result == null ? "" : result );
	        sbf.append("','");
	        sbf.append(updateId == null ? "" : updateId );
	        sbf.append("','");
	        sbf.append(updateDate == null ? "" : StringUtils.formatShortDate(updateDate ));
	        sbf.append("'");
	        sbf.append("]");
	        sbf.append("}");
	    }

		public boolean equals(Object other) {
			if ((this == other))
				return true;
			if ((other == null))
				return false;
			if (!(other instanceof TestCycleTask))
				return false;
			TestCycleTask castOther = (TestCycleTask) other;
			return ((this.getId()== castOther.getId()) || (this.getId() != null
					&& castOther.getId() != null && this.getId().intValue()==
					castOther.getId().intValue()));
		}

		public int hashCode() {
			int result = 17;
			result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
			return result;
		}
		public String getAttachUrl() {
			return attachUrl;
		}
		public void setAttachUrl(String attachUrl) {
			this.attachUrl = attachUrl;
		}
}
