package cn.com.codes.analysisManager.dto;

import java.util.List;
import java.util.Map;

import com.opensymphony.util.Data;

import cn.com.codes.framework.transmission.dto.BaseDto;

public class AnalysisDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private String blhPar;//&analysisDto.blhPar=
	private String parameter;
	private String rptdesign;
	private Integer status;
	private String projectId;
	private String treeStr;
	private String projectCost;
	private String ids;
	private String canView ;
	
	//分析度量查询报表
	private String taskId;
	private String startDate;
	private String endDate;
	private List<Object[]> alsResult;
	private List<Map<String, Object>> existAllBugData;
	private String moduleIds;	
	
	//项目切换--【分析度量】
	private String analyProjectName;
	private String analyPlanStartDate;
	private String analyPlanEndDate;
	private String analyProNum;
	
 

	public List<Map<String, Object>> getExistAllBugData() {
		return existAllBugData;
	}

	public void setExistAllBugData(List<Map<String, Object>> existAllBugData) {
		this.existAllBugData = existAllBugData;
	}

	public String getBlhPar() {
		return blhPar;
	}

	public void setBlhPar(String blhPar) {
		this.blhPar = blhPar;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getRptdesign() {
		return rptdesign;
	}

	public void setRptdesign(String rptdesign) {
		this.rptdesign = rptdesign;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getTreeStr() {
		return treeStr;
	}

	public void setTreeStr(String treeStr) {
		this.treeStr = treeStr;
	}

	public String getProjectCost() {
		return projectCost;
	}

	public void setProjectCost(String projectCost) {
		this.projectCost = projectCost;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getCanView() {
		return canView;
	}

	public void setCanView(String canView) {
		this.canView = canView;
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
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<Object[]> getAlsResult() {
		return alsResult;
	}

	public void setAlsResult(List<Object[]> alsResult) {
		this.alsResult = alsResult;
	}

	/**
	 * @return the analyProjectName
	 */
	public String getAnalyProjectName() {
		return analyProjectName;
	}

	/**
	 * @param analyProjectName the analyProjectName to set
	 */
	public void setAnalyProjectName(String analyProjectName) {
		this.analyProjectName = analyProjectName;
	}

	/**
	 * @return the analyPlanStartDate
	 */
	public String getAnalyPlanStartDate() {
		return analyPlanStartDate;
	}

	/**
	 * @param analyPlanStartDate the analyPlanStartDate to set
	 */
	public void setAnalyPlanStartDate(String analyPlanStartDate) {
		this.analyPlanStartDate = analyPlanStartDate;
	}

	/**
	 * @return the analyPlanEndDate
	 */
	public String getAnalyPlanEndDate() {
		return analyPlanEndDate;
	}

	/**
	 * @param analyPlanEndDate the analyPlanEndDate to set
	 */
	public void setAnalyPlanEndDate(String analyPlanEndDate) {
		this.analyPlanEndDate = analyPlanEndDate;
	}

	/**
	 * @return the analyProNum
	 */
	public String getAnalyProNum() {
		return analyProNum;
	}

	/**
	 * @param analyProNum the analyProNum to set
	 */
	public void setAnalyProNum(String analyProNum) {
		this.analyProNum = analyProNum;
	}

	/**
	 * @return the moduleIds
	 */
	public String getModuleIds() {
		return moduleIds;
	}

	/**
	 * @param moduleIds the moduleIds to set
	 */
	public void setModuleIds(String moduleIds) {
		this.moduleIds = moduleIds;
	}
	
	
}
