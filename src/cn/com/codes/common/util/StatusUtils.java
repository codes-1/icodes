package cn.com.codes.common.util;

import java.util.HashMap;

public final class StatusUtils {
	private static HashMap precddenceMap = new HashMap();
	private static HashMap taskStatusMap = new HashMap();
	private static HashMap taskTypeMap = new HashMap();
	private static HashMap projectPlanFlagMap = new HashMap();
	private static HashMap projectPlanTypeMap = new HashMap();
	private static HashMap isFinishMap = new HashMap();
	private static HashMap calendarTypeMap = new HashMap();
	private static HashMap weekDayMap = new HashMap();
	private static HashMap weekDayComMap = new HashMap();
	private static HashMap isDefaultMap = new HashMap();
	private static HashMap resourceTypeMap = new HashMap();	
	//private static HashMap resourceStatusMap = new HashMap();
	private static HashMap pCApprovalStatusMap = new HashMap();
	private static HashMap versionLogTypeMap = new HashMap();
	private static HashMap reportTypeMap = new HashMap();
	private static HashMap workflowStatusMap = new HashMap();
	private static HashMap temporaryMap = new HashMap();
	
	static{
		precddenceMap.put(2, "最高");
		precddenceMap.put(1, "高");
		precddenceMap.put(0, "一般");
		precddenceMap.put(-1, "底");
		precddenceMap.put(-2, "最低");
		
		taskStatusMap.put(0, "未开始");
		taskStatusMap.put(1, "滞后");
		taskStatusMap.put(2, "正常进行");
		taskStatusMap.put(3, "提前开始");
		taskStatusMap.put(4, "提前结束");
		taskStatusMap.put(5, "正常结束");
		taskStatusMap.put(6, "延期结束");
		taskStatusMap.put(7, "人为终止");
		taskStatusMap.put(8, "延期");
		taskStatusMap.put(9, "");
		
		taskTypeMap.put(0, "普通任务");
		taskTypeMap.put(1, "集成测试");
		taskTypeMap.put(2, "系统测试");
		
		projectPlanFlagMap.put(0, "未完成计划");
		projectPlanFlagMap.put(1, "完成计划");
		projectPlanFlagMap.put(2, "计划未审批");
		projectPlanFlagMap.put(3, "变更未审批");
		projectPlanFlagMap.put(4, "恢复未审批");
		projectPlanFlagMap.put(-2, "计划未通过");
		projectPlanFlagMap.put(-3, "变更未通过");
		projectPlanFlagMap.put(-4, "恢复未通过");
		
		isFinishMap.put(0, "未开始");
		isFinishMap.put(1, "滞后");
		isFinishMap.put(2, "正常进行");
		isFinishMap.put(3, "提前开始");
		isFinishMap.put(4, "提前结束");
		isFinishMap.put(5, "正常结束");
		isFinishMap.put(6, "延期结束");
		isFinishMap.put(7, "人为终止");
		isFinishMap.put(8, "延期");
		
		projectPlanTypeMap.put(0, "从结束时间往前排");
		projectPlanTypeMap.put(1, "从开始时间往后排");
		
		calendarTypeMap.put(0, "项目日历");
		calendarTypeMap.put(1, "任务日历");
		
		weekDayMap.put("0", "周日");
		weekDayMap.put("1", "周一");
		weekDayMap.put("2", "周二");
		weekDayMap.put("3", "周三");
		weekDayMap.put("4", "周四");
		weekDayMap.put("5", "周五");
		weekDayMap.put("6", "周六");
		
		weekDayComMap.put(1, "周日");
		weekDayComMap.put(2, "周一");
		weekDayComMap.put(3, "周二");
		weekDayComMap.put(4, "周三");
		weekDayComMap.put(5, "周四");
		weekDayComMap.put(6, "周五");
		weekDayComMap.put(7, "周六");
		
		isDefaultMap.put(1, "是");
		isDefaultMap.put(0, "");
		
		resourceTypeMap.put(0, "工时资源");
		resourceTypeMap.put(1, "材料资源");
		resourceTypeMap.put(2, "成本资源");
		
		//resourceStatusMap.put(0, "未开始");
		//resourceStatusMap.put(1, "使用中");
		//resourceStatusMap.put(2, "已结束");
		
		pCApprovalStatusMap.put(-1, "审批未通过");
		pCApprovalStatusMap.put(0, "未审批");
		pCApprovalStatusMap.put(1, "审批通过");
		pCApprovalStatusMap.put(2, "标记为非法数据");
		
		versionLogTypeMap.put(1, "项目计划");
		versionLogTypeMap.put(2, "版本变更");
		versionLogTypeMap.put(3, "版本恢复");
		
		reportTypeMap.put(0, "工作日报");
		reportTypeMap.put(1, "工作周报");
		reportTypeMap.put(2, "工作月报");
		reportTypeMap.put(3, "工作季报");
		
		workflowStatusMap.put(0, "进行中");
		workflowStatusMap.put(1, "完成");
		workflowStatusMap.put(2, "人为终止");
		
		temporaryMap.put(0, "");
		temporaryMap.put(1, "临时资源");
	}
	
	public static String getPrecddence(Integer precddence){
		return (String)precddenceMap.get(precddence);
	}
	
	public static String getTaskType(Integer type){
		return (String)taskTypeMap.get(type);
	}
	
	public static String getTaskStatus(Integer status){
		return (String)taskStatusMap.get(status);
	}
	
	public static String getProjectPlanFlag(Integer planFlag){
		return (String)projectPlanFlagMap.get(planFlag);
	}
	
	public static String getProjectPlanTypeMap(Integer planType){
		return (String)projectPlanTypeMap.get(planType);
	}
	
	public static String getIsFinishMap(Integer isFinish){
		return (String)isFinishMap.get(isFinish);
	}
	
	public static String getCalendarType(Integer type){
		return (String)calendarTypeMap.get(type);
	}
	
	public static String getChineseWeekDay(String weekDays){
		if(weekDays == null || "".equals(weekDays))
			return "";
		StringBuffer bf = new StringBuffer();
		String weekDay[] = weekDays.replaceAll(" ", "").split(",");
		bf.append("<br>");
		for(int i = 0; i < weekDay.length; i++){
			bf.append(weekDayMap.get(weekDay[i]));
			bf.append("<br>");
		}
		bf.append("<br>");
		return bf.toString();
	}
	
	public static String getChineseWeekDay(int dayOfWeek){
		return weekDayComMap.get(dayOfWeek).toString();
	}
	
	public static String getIsDefault(Integer isDefault){
		return (String)isDefaultMap.get(isDefault);
	}
	
	public static String getResourceTypeMap(Integer type){
		return (String)resourceTypeMap.get(type);
	}
	
	//public static String getResourceStatusMap(Integer status){
		//return (String)resourceStatusMap.get(status);
	//}
	
	public static String getPCApprovalStatusMap(Integer status){
		return (String)pCApprovalStatusMap.get(status);
	}
	
	public static String getVersionLogTypeMap(Integer type){
		return (String)versionLogTypeMap.get(type);
	}
	
	public static String getReportTypeMap(Integer type){
		return (String)reportTypeMap.get(type);
	}
	
	public static String getWorkFlowMap(Integer status){
		return (String)workflowStatusMap.get(status);
	}
	
	public static String getTemporaryMap(Integer temporary){
		return (String)temporaryMap.get(temporary);
	}
}
