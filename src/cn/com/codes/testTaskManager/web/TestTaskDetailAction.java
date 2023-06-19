package cn.com.codes.testTaskManager.web;

import java.util.HashMap;

import cn.com.codes.framework.app.blh.BaseBizLogicHandler;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;
import cn.com.codes.testTaskManager.blh.TestTaskDetailBlh;
import cn.com.codes.testTaskManager.dto.TestTaskManagerDto;

public class TestTaskDetailAction extends BaseAction<TestTaskDetailBlh> {

	TestTaskManagerDto dto = new TestTaskManagerDto();
	private TestTaskDetailBlh testTaskBlh;
	
	protected void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException {
		reqEvent.setDto(dto);
	}

	protected String _processResponse() throws BaseException {
		HashMap<?, ?> displayData = (HashMap<?, ?>) _getDisplayData();
		return forwardPage(displayData);
	}

	public TestTaskManagerDto getDto() {
		return dto;
	}

	public void setDto(TestTaskManagerDto dto) {
		this.dto = dto;
	}

	public TestTaskDetailBlh getTestTaskBlh() {
		return testTaskBlh;
	}

	public void setTestTaskBlh(TestTaskDetailBlh testTaskBlh) {
		this.testTaskBlh = testTaskBlh;
	}
	public  BaseBizLogicHandler getBlh(){
		  
		return testTaskBlh;
	}
}
