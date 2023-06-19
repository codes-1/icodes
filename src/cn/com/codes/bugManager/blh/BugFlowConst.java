package cn.com.codes.bugManager.blh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.codes.framework.common.ListObject;
import cn.com.codes.bugManager.blh.BugFlowConst;

public class BugFlowConst {

	private static Map<String,String> stateMap = new HashMap<String,String>(25);
	private static List<ListObject> stateList = new ArrayList(); 
	static{  
		stateMap.put("1", "待置");
		stateMap.put("2", "修正/描述不当");
		stateMap.put("3", "重复");
		stateMap.put("4", "无效");//
		stateMap.put("5", "撤销");//
		stateMap.put("6", "不再现/需提供更多信息");
		stateMap.put("7", "待改/再现");
		stateMap.put("8", "待改/不再现");
		stateMap.put("9", "待改/未解决");
		stateMap.put("10", "待改");
		stateMap.put("11", "挂起/需提供更多信息");
		stateMap.put("12", "分歧");
		stateMap.put("13", "已改");
		stateMap.put("26", "己改/同步到测试环境");
		stateMap.put("14", "关闭/己解决");//
		stateMap.put("15", "关闭/不再现");//
		stateMap.put("16", "非错");
		stateMap.put("17", "重分配");
		stateMap.put("18", "交叉验证");
		stateMap.put("19", "挂起/不计划修改");
		stateMap.put("20", "挂起/下版本修改");
		stateMap.put("21", "待改/下版本修改");
		stateMap.put("22", "关闭/撤销");//
		stateMap.put("23", "关闭/遗留");//
		stateMap.put("24", "分析");
		stateMap.put("25", "分配");
		
		
		
		stateList.add(new ListObject("1", "待置"));
		stateList.add(new ListObject("2", "修正/描述不当"));
		stateList.add(new ListObject("3", "重复"));
		stateList.add(new ListObject("4", "无效"));
		stateList.add(new ListObject("5", "撤销"));
		stateList.add(new ListObject("6", "不再现/需提供更多信息"));
	
		stateList.add(new ListObject("7", "待改/再现"));
		stateList.add(new ListObject("8", "待改/不再现"));
		stateList.add(new ListObject("9", "待改/未解决"));
		stateList.add(new ListObject("10", "待改"));//无测试互验时，就用这状态
		
		stateList.add(new ListObject("11", "挂起/需提供更多信息"));
		stateList.add(new ListObject("12", "分歧"));
		stateList.add(new ListObject("13", "已改"));
		stateList.add(new ListObject("26", "己改/同步到测试环境"));
		
		stateList.add(new ListObject("14", "关闭/己解决"));
		stateList.add(new ListObject("15", "关闭/不再现"));//11状态，后测试人员可以设置该状态
		stateList.add(new ListObject("16", "非错"));

		
		stateList.add(new ListObject("17", "重分配"));

		
		stateList.add(new ListObject("18", "交叉验证"));
		
		
		stateList.add(new ListObject("19", "挂起/不计划修改"));//不改
		
		stateList.add(new ListObject("20", "挂起/下版本修改"));//延迟
		
		
		stateList.add(new ListObject("21", "待改/下版本修改"));//顺延
			
		stateList.add(new ListObject("22", "关闭/撤销"));
		stateList.add(new ListObject("23", "关闭/遗留"));
		
		
		stateList.add(new ListObject("24", "分析"));
		
		
		stateList.add(new ListObject("25", "分配"));
		
		//
		
		
	}
	public static String getStateName(Integer stateId){
		if(stateId==null){
			return null;
		}
		return stateMap.get(stateId.toString());
	}
	   //测试流属
    public static final int FINDBUG_FLOW_CODE = 1; 
    public static final int TSTR_CNFM_FLOW_CODE = 2; 
    public static final int ANALYSIS_FLOW_CODE = 3; 
    public static final int ASSIGN_FLOW_CODE = 4; 
    public static final int CHANGE_FLOW_CODE = 5; 
    public static final int DVLPR_CNFM_FLOW_CODE = 6; 
    public static final int DVLP_LDR_FLOW_CODE = 7; 
    public static final int FINAL_FLOW_CODE = 8; 


    
	public static List<ListObject> getStateList() {
		return stateList;
	}
	public static void setStateList(List<ListObject> stateList) {
		BugFlowConst.stateList = stateList;
	}

}
