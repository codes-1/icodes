package cn.com.codes.object;

import java.util.Date;

import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.transmission.JsonInterface;

public class BugHandHistory implements JsonInterface {

	private Long historyId;
	private String taskId;
	private Long moduleId;
	private Long bugId;
	private Integer testFlowCd;
	private Integer bugState;
	private String remark;
	private String handlerId;
	private Date insDate;
	private Integer testSeq;
	private String moduleNum;
	private Long currVer;
	private String flwNodeName;
	private Integer initState;
	private String handlerName;
	private String handResult;
	private Integer currDayFinal;
	public BugHandHistory() {
	}

	public BugHandHistory(Long historyId, String remark,
			String handlerId, String handResult, Date insDate) {
		this.historyId = historyId;
		this.remark = remark;
		this.handlerId = handlerId;
		this.handResult = handResult;
		this.insDate = insDate;
	}

	public BugHandHistory(Long historyId, Long moduleId, Long bugId,
			Integer testFlowCd, Integer bugState) {
		this.historyId = historyId;
		this.moduleId = moduleId;
		this.bugId = bugId;
		this.testFlowCd = testFlowCd;
		this.bugState = bugState;
	}

	public Long getCurrVer() {
		return this.currVer;
	}

	public void setCurrVer(Long currVer) {
		this.currVer = currVer;
	}

	public Long getHistoryId() {
		return historyId;
	}

	public void setHistoryId(Long historyId) {
		this.historyId = historyId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public Long getBugId() {
		return bugId;
	}

	public void setBugId(Long bugId) {
		this.bugId = bugId;
	}

	public Integer getTestFlowCd() {
		return testFlowCd;
	}

	public void setTestFlowCd(Integer testFlowCd) {
		this.testFlowCd = testFlowCd;
	}

	public Integer getBugState() {
		return bugState;
	}

	public void setBugState(Integer bugState) {
		this.bugState = bugState;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getHandlerId() {
		return handlerId;
	}

	public void setHandlerId(String handlerId) {
		this.handlerId = handlerId;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = insDate;
	}

	public Integer getTestSeq() {
		return testSeq;
	}

	public void setTestSeq(Integer testSeq) {
		this.testSeq = testSeq;
	}

	public String getModuleNum() {
		return moduleNum;
	}

	public void setModuleNum(String moduleNum) {
		this.moduleNum = moduleNum;
	}

	public String getFlwNodeName() {
		return flwNodeName;
	}

	public void setFlwNodeName(String flwNldeName) {
		this.flwNodeName = flwNldeName;
	}

	public Integer getInitState() {
		return initState;
	}

	public void setInitState(Integer initState) {
		this.initState = initState;
	}

	public String getHandlerName() {
		return handlerName;
	}

	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}
	public String getHandResult() {
		return handResult;
	}

	public void setHandResult(String handResult) {
		this.handResult = handResult;
	}
	
	public String toStrList() {
		StringBuffer sbf = new StringBuffer();
		sbf.append("{");
		sbf.append("id:'");
		sbf.append(getHistoryId().toString());
		sbf.append("',data: [0,'','");
		sbf.append(handlerName);
		sbf.append("','");
		sbf.append(handResult);
		sbf.append("','");
		sbf.append(remark == null ? "" : remark);
		sbf.append("','");
		sbf.append(StringUtils.formatLongDate(insDate));
		sbf.append("'");
		sbf.append("]");
		sbf.append("}");
		return sbf.toString();
	}

	public String toStrUpdateInit() {
		return null;
	}

	public String toStrUpdateRest() {

		return null;
	}

	public void toString(StringBuffer sbf) {
	}

	public Integer getCurrDayFinal() {
		return currDayFinal;
	}

	public void setCurrDayFinal(Integer currDayFinal) {
		this.currDayFinal = currDayFinal;
	}


}