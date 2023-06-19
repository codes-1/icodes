package cn.com.codes.msgManager.web;

import java.util.HashMap;

import cn.com.codes.framework.app.blh.BaseBizLogicHandler;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;
import cn.com.codes.msgManager.blh.CommonMsgBlh;
import cn.com.codes.msgManager.dto.CommonMsgDto;

public class CommonMsgAction extends BaseAction {

	private CommonMsgDto dto = new CommonMsgDto();
	private CommonMsgBlh commonMsgBlh;
	protected void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException {
		reqEvent.setDto(dto);

	}


	protected String _processResponse() throws BaseException {
		HashMap<?, ?> displayData = (HashMap<?, ?>) _getDisplayData();
		return forwardPage(displayData);
	}


	public CommonMsgDto getDto() {
		return dto;
	}


	public void setDto(CommonMsgDto dto) {
		this.dto = dto;
	}


	public CommonMsgBlh getCommonMsgBlh() {
		return commonMsgBlh;
	}


	public void setCommonMsgBlh(CommonMsgBlh commonMsgBlh) {
		this.commonMsgBlh = commonMsgBlh;
	}
	public  BaseBizLogicHandler getBlh(){
		  
		return commonMsgBlh;
	}
	

}
