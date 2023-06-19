package cn.com.codes.outlineManager.web;

import java.util.HashMap;

import cn.com.codes.framework.app.blh.BaseBizLogicHandler;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;
import cn.com.codes.outlineManager.blh.OutLineManagerBlh;
import cn.com.codes.outlineManager.dto.OutLineManagerDto;

public class OutLineManagerAction extends BaseAction<OutLineManagerBlh> {

	OutLineManagerDto dto = new OutLineManagerDto();
	private OutLineManagerBlh outLineBlh;
	
	protected void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException {
		reqEvent.setDto(dto);

	}

	
	protected String _processResponse() throws BaseException {
		HashMap<?, ?> displayData = (HashMap<?, ?>) _getDisplayData();
		return forwardPage(displayData);
	}


	public OutLineManagerDto getDto() {
		return dto;
	}


	public void setDto(OutLineManagerDto dto) {
		this.dto = dto;
	}


	public OutLineManagerBlh getOutLineBlh() {
		return outLineBlh;
	}


	public void setOutLineBlh(OutLineManagerBlh outLineBlh) {
		this.outLineBlh = outLineBlh;
	}
	public  BaseBizLogicHandler getBlh(){
		  
		return outLineBlh;
	}
	

}
