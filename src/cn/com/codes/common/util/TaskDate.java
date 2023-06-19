package cn.com.codes.common.util;

import java.util.Date;

import cn.com.codes.common.util.StringUtils;
import cn.com.codes.common.util.TaskDate;

public class TaskDate {
	private Date planStartDate;
	private Date planEndDate;
	private Float planWh;
	
	public TaskDate(){
	}
	
	public TaskDate(Date planStartDate, Date planEndDate, Float planWh){
		this.planStartDate = planStartDate;
		this.planEndDate = planEndDate;
		this.planWh = planWh;
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

	public Float getPlanWh() {
		return planWh;
	}

	public void setPlanWh(Float planWh) {
		this.planWh = planWh;
	}
	
	public void taskDateInfo(TaskDate taskDate, Date planStartDate, Date planEndDate, Float planWh){
		taskDate.setPlanStartDate(planStartDate);
		taskDate.setPlanEndDate(planEndDate);
		taskDate.setPlanWh(planWh);
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("planStartDate=");
		sb.append(StringUtils.formatShortDate(planStartDate));
		sb.append("^planEndDate=");
		sb.append(StringUtils.formatShortDate(planEndDate));
		sb.append("^planWh=");
		sb.append(planWh);
		return sb.toString();
	}
	
	public String toStrUpdateRest(){
		StringBuffer sb = new StringBuffer();
		sb.append(StringUtils.formatShortDate(planStartDate));
		sb.append("^");
		sb.append(StringUtils.formatShortDate(planEndDate));
		sb.append("^");
		sb.append(planWh);
		return sb.toString();
	}
}
