package cn.com.codes.testRequirement.web;

import java.util.HashMap;

import cn.com.codes.framework.app.blh.BaseBizLogicHandler;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;
import cn.com.codes.testRequirement.blh.TestRequirementBlh;
import cn.com.codes.testRequirement.dto.TestRequirementDto;


public class TestRequirementAction extends BaseAction<TestRequirementBlh> {

	TestRequirementDto dto = new TestRequirementDto();
	private TestRequirementBlh testRequirementBlh;
	
	protected void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException {
		reqEvent.setDto(dto);

	}

	
	protected String _processResponse() throws BaseException {
		HashMap<?, ?> displayData = (HashMap<?, ?>) _getDisplayData();
		return forwardPage(displayData);
	}


	public TestRequirementDto getDto() {
		return dto;
	}


	public void setDto(TestRequirementDto dto) {
		this.dto = dto;
	}



	public  BaseBizLogicHandler getBlh(){
		  
		return testRequirementBlh;
	}
	

}
