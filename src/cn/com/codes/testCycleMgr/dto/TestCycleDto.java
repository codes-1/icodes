package cn.com.codes.testCycleMgr.dto;

import cn.com.codes.framework.transmission.dto.BaseDto;
import cn.com.codes.object.TestCycleTask;

public class TestCycleDto extends BaseDto {

	private TestCycleTask testCycleTask;

	public TestCycleTask getTestCycleTask() {
		return testCycleTask;
	}

	public void setTestCycleTask(TestCycleTask testCycleTask) {
		this.testCycleTask = testCycleTask;
	}
}
