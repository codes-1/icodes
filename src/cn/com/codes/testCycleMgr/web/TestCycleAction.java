package cn.com.codes.testCycleMgr.web;

import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;
import cn.com.codes.testCycleMgr.blh.TestCycleBlh;
import cn.com.codes.testCycleMgr.dto.TestCycleDto;

public class TestCycleAction extends BaseAction<TestCycleBlh> {

	
	private TestCycleDto dto = new TestCycleDto();
	@Override
	protected void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException {
		

	}
	public TestCycleDto getDto() {
		return dto;
	}
	public void setDto(TestCycleDto dto) {
		this.dto = dto;
	}

}
