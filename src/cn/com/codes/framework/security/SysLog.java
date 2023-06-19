package cn.com.codes.framework.security;

import java.util.Date;

import cn.com.codes.framework.security.SysLog;



public abstract class SysLog implements java.io.Serializable {



	private String logId;
	private String operDesc;
	private String operId;
	private Date operDate;
	private String operSummary;
	private Integer logType;
	private String accessIp ;
	


	
	public SysLog() {
	}


	public SysLog(String logId) {
		this.logId = logId;
	}





	public String getLogId() {
		return this.logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getOperDesc() {
		return this.operDesc;
	}

	public void setOperDesc(String operDesc) {
		this.operDesc = operDesc;
	}



	public Date getOperDate() {
		return this.operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

	public String getOperSummary() {
		return this.operSummary;
	}

	public void setOperSummary(String operSummary) {
		this.operSummary = operSummary;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SysLog))
			return false;
		SysLog castOther = (SysLog) other;

		return (this.getLogId() == castOther.getLogId()) || (this.getLogId() != null
				&& castOther.getLogId() != null && this.getLogId().equals(
				castOther.getLogId()));
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result
				+ (getLogId() == null ? 0 : this.getLogId().hashCode());
		return result;
	}


	public String getOperId() {
		return operId;
	}


	public void setOperId(String operId) {
		this.operId = operId;
	}


	public Integer getLogType() {
		return logType;
	}


	public void setLogType(Integer logType) {
		this.logType = logType;
	}


	public String getAccessIp() {
		return accessIp;
	}


	public void setAccessIp(String accessIp) {
		this.accessIp = accessIp;
	}



}