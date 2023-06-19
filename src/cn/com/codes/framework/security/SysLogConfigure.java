package cn.com.codes.framework.security;

import java.util.Date;

import cn.com.codes.object.LogConfig;

/**
 * TLogConfigHyId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class SysLogConfigure implements java.io.Serializable {

	

	private String id;
	private String title;
	private String method;
	private String args;

	
	public SysLogConfigure() {
	}
	private Date insertDate;
	private Date updateDate;
	
	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	public SysLogConfigure(String id) {
		this.id = id;
	}

	
	public SysLogConfigure(String id, String title, String method, String args) {
		this.id = id;
		this.title = title;
		this.method = method;
		this.args = args;
	}

	

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMethod() {
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getArgs() {
		return this.args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof LogConfig))
			return false;
		LogConfig castOther = (LogConfig) other;

		return (this.getId() == castOther.getId()) || (this.getId() != null
				&& castOther.getId() != null && this.getId().equals(
				castOther.getId()));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
		return result;
	}

}