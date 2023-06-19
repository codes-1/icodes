package cn.com.codes.object;

import java.util.Date;

import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.transmission.JsonInterface;

public class BugCaseCycleRela implements JsonInterface {

	private Long id;
	private Integer cycleTaskId;
	private Integer bugOrCaseId;
	private String exeId;
	private Date exeDate;
	private String exeResult;
	private Integer cycleType;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getCycleTaskId() {
		return cycleTaskId;
	}
	public void setCycleTaskId(Integer cycleTaskId) {
		this.cycleTaskId = cycleTaskId;
	}
	public Integer getBugOrCaseId() {
		return bugOrCaseId;
	}
	public void setBugOrCaseId(Integer bugOrCaseId) {
		this.bugOrCaseId = bugOrCaseId;
	}
	public String getExeId() {
		return exeId;
	}
	public void setExeId(String exeId) {
		this.exeId = exeId;
	}
	public Date getExeDate() {
		return exeDate;
	}
	public void setExeDate(Date exeDate) {
		this.exeDate = exeDate;
	}
	public String getExeResult() {
		return exeResult;
	}
	public void setExeResult(String exeResult) {
		this.exeResult = exeResult;
	}
	public Integer getCycleType() {
		return cycleType;
	}
	public void setCycleType(Integer cycleType) {
		this.cycleType = cycleType;
	}
	
	   public String toStrList(){
	        StringBuffer sbf = new StringBuffer();
	        sbf.append("{");
	        sbf.append("id:'");
	        sbf.append(getId().toString());
	        sbf.append("',data: [0,'','");
	        sbf.append(cycleTaskId == null ? "" : cycleTaskId );
	        sbf.append("','");
	        sbf.append(bugOrCaseId == null ? "" : bugOrCaseId );
	        sbf.append("','");
	        sbf.append(exeId == null ? "" : exeId );
	        sbf.append("','");
	        sbf.append(exeDate == null ? "" : StringUtils.formatShortDate(exeDate ));
	        sbf.append("','");
	        sbf.append(exeResult == null ? "" : exeResult );
	        sbf.append("','");
	        sbf.append(cycleType == null ? "" : cycleType );
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
	        sbf.append("cycleTaskId=").append(cycleTaskId == null ? "" : cycleTaskId);
	        sbf.append("^");
	        sbf.append("bugOrCaseId=").append(bugOrCaseId == null ? "" : bugOrCaseId);
	        sbf.append("^");
	        sbf.append("exeId=").append(exeId == null ? "" : exeId);
	        sbf.append("^");
	        sbf.append("exeDate=").append(exeDate == null ? "" : StringUtils.formatShortDate(exeDate));
	        sbf.append("^");
	        sbf.append("exeResult=").append(exeResult == null ? "" : exeResult);
	        sbf.append("^");
	        sbf.append("cycleType=").append(cycleType == null ? "" : cycleType);
	        return sbf.toString();
	    }

	   public String toStrUpdateRest(){
	        StringBuffer sbf = new StringBuffer();
	        sbf.append(getId().toString());
	        sbf.append("^");
	        sbf.append("0,,");
	        sbf.append(cycleTaskId == null ? "" : cycleTaskId );
	        sbf.append(",");
	        sbf.append(bugOrCaseId == null ? "" : bugOrCaseId );
	        sbf.append(",");
	        sbf.append(exeId == null ? "" : exeId );
	        sbf.append(",");
	        sbf.append(exeDate == null ? "" : StringUtils.formatShortDate(exeDate ));
	        sbf.append(",");
	        sbf.append(exeResult == null ? "" : exeResult );
	        sbf.append(",");
	        sbf.append(cycleType == null ? "" : cycleType );
	        return sbf.toString();
	     }

	   public void toString(StringBuffer sbf){
	        sbf.append("{");
	        sbf.append("id:'");
	        sbf.append(getId().toString());
	        sbf.append("',data: [0,'','");
	        sbf.append(cycleTaskId == null ? "" : cycleTaskId );
	        sbf.append("','");
	        sbf.append(bugOrCaseId == null ? "" : bugOrCaseId );
	        sbf.append("','");
	        sbf.append(exeId == null ? "" : exeId );
	        sbf.append("','");
	        sbf.append(exeDate == null ? "" : StringUtils.formatShortDate(exeDate ));
	        sbf.append("','");
	        sbf.append(exeResult == null ? "" : exeResult );
	        sbf.append("','");
	        sbf.append(cycleType == null ? "" : cycleType );
	        sbf.append("'");
	        sbf.append("]");
	        sbf.append("}");
	    }


		public boolean equals(Object other) {
			if ((this == other))
				return true;
			if ((other == null))
				return false;
			if (!(other instanceof BugCaseCycleRela))
				return false;
			BugCaseCycleRela castOther = (BugCaseCycleRela) other;
			return ((this.getId()== castOther.getId()) || (this.getId() != null
					&& castOther.getId() != null && this.getId().intValue()==
					castOther.getId().intValue())
					||(this.getBugOrCaseId()!=null&&this.getCycleType()!=null&&this.getCycleTaskId()!=null
					   &&castOther.getBugOrCaseId()!=null&&castOther.getCycleType()!=null&&castOther.getCycleTaskId()!=null
					   &&this.getBugOrCaseId().intValue()==castOther.getBugOrCaseId().intValue()&&this.getCycleTaskId().intValue()==castOther.getCycleTaskId().intValue()
					   &&this.getCycleType().intValue()==castOther.getCycleType().intValue()
					));
		}

		public int hashCode() {
			int result = 17;
			result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
			result = 37 * result + (getBugOrCaseId() == null ? 0 : this.getBugOrCaseId().hashCode());
			result = 37 * result + (getCycleType() == null ? 0 : this.getCycleType().hashCode());
			result = 37 * result + (getCycleTaskId() == null ? 0 : this.getCycleTaskId().hashCode());
			return result;
		}
}
