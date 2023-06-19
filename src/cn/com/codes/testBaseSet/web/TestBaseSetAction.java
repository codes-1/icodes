package cn.com.codes.testBaseSet.web;

import java.util.HashMap;

import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;
import cn.com.codes.testBaseSet.blh.TestBaseSetBlh;
import cn.com.codes.testBaseSet.dto.TestBaseSetDto;

public class TestBaseSetAction extends BaseAction<TestBaseSetBlh> {

	private TestBaseSetDto dto = new TestBaseSetDto();
	
	protected void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException {
		reqEvent.setDto(dto);
	}

	
	protected String _processResponse() throws BaseException {
		
		HashMap<?, ?> displayData = (HashMap<?, ?>) _getDisplayData();
		return forwardPage(displayData);
	}


	public TestBaseSetDto getDto() {
		return dto;
	}


	public void setDto(TestBaseSetDto dto) {
		this.dto = dto;
	}


}
