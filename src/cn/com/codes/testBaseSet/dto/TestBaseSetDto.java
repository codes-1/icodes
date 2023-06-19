package cn.com.codes.testBaseSet.dto;

import java.util.ArrayList;
import java.util.List;

import cn.com.codes.framework.common.ListObject;
import cn.com.codes.framework.transmission.dto.BaseDto;
import cn.com.codes.testBaseSet.dto.TestBaseSetVo;

public class TestBaseSetDto extends BaseDto {

	private String subName;
	private String flag;
	
	private static List<ListObject> subList = new ArrayList();
	static{
		subList.add(new ListObject("用例优先级", "用例优先级"));
		subList.add(new ListObject("用例类型", "用例类型"));
		subList.add(new ListObject("BUG频率", "BUG频率"));
		subList.add(new ListObject("BUG类型", "BUG类型"));
		subList.add(new ListObject("BUG等级", "BUG等级"));
		subList.add(new ListObject("BUG发现时机", "BUG发现时机"));
		subList.add(new ListObject("BUG优先级", "BUG优先级"));
		subList.add(new ListObject("BUG来源", "BUG来源"));
		//subList.add(new ListObject("BUG引入原因", "BUG引入原因"));
		subList.add(new ListObject("BUG测试时机", "BUG测试时机"));
		//BUG引入原因
		//subList.add(new ListObject("BUG引入阶段", "BUG引入阶段"));
		
		subList.add(new ListObject("BUG引入原因", "BUG引入原因"));
		subList.add(new ListObject("BUG发生平台", "BUG发生平台"));
		subList.add(new ListObject("任务类别", "任务类别"));
		subList.add(new ListObject("任务紧急程度", "任务紧急程度"));
		subList.add(new ListObject("任务难易程度", "任务难易程度"));
	}
	
	private TestBaseSetVo testBaseSet ;
	
	public String getSubName() {
		return subName;
	}

	public void setSubName(String subName) {
		this.subName = subName;
	}
	
	public static List<ListObject> getSubList() {
		return subList;
	}

	public TestBaseSetVo getTestBaseSet() {
		return  testBaseSet;
	}

	public void setTestBaseSet(TestBaseSetVo testBaseSet) {
		this.testBaseSet = testBaseSet;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

}
