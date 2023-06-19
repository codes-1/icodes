package cn.com.codes.bugManager.web;

import java.util.HashMap;

import cn.com.codes.bugManager.blh.BugShortMsgBlh;
import cn.com.codes.bugManager.dto.BugShortMsgDto;
import cn.com.codes.framework.app.blh.BaseBizLogicHandler;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;

public class BugShortMsgAction extends BaseAction {

	BugShortMsgDto dto = new BugShortMsgDto();
	private BugShortMsgBlh bugShortMsgBlh;
	
	protected void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException {
		reqEvent.setDto(dto);

	}

	protected String _processResponse() throws BaseException {
		HashMap<?, ?> displayData = (HashMap<?, ?>) _getDisplayData();
		return forwardPage(displayData);
	}

	public BugShortMsgDto getDto() {
		return dto;
	}

	public void setDto(BugShortMsgDto dto) {
		this.dto = dto;
	}

	public BugShortMsgBlh getBugShortMsgBlh() {
		return bugShortMsgBlh;
	}

	public void setBugShortMsgBlh(BugShortMsgBlh bugShortMsgBlh) {
		this.bugShortMsgBlh = bugShortMsgBlh;
	}
	public  BaseBizLogicHandler getBlh(){
		  
		return bugShortMsgBlh;
	}
	

}
