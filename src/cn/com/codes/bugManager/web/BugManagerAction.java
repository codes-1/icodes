package cn.com.codes.bugManager.web;

import java.util.HashMap;

import cn.com.codes.bugManager.blh.BugManagerBlh;
import cn.com.codes.bugManager.dto.BugManagerDto;
import cn.com.codes.framework.app.blh.BaseBizLogicHandler;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;

public class BugManagerAction extends BaseAction<BugManagerBlh> {

	BugManagerDto dto ;
	private BugManagerBlh bugManagerBlh;
	
	protected void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException {
		if(dto==null){
			dto = new BugManagerDto();
		}
		reqEvent.setDto(dto);

	}

	protected String _processResponse() throws BaseException {
		HashMap<?, ?> displayData = (HashMap<?, ?>) _getDisplayData();
		return forwardPage(displayData);
	}

	public BugManagerDto getDto() {
		return dto;
	}

	public void setDto(BugManagerDto dto) {
		this.dto = dto;
	}

	public BugManagerBlh getBugManagerBlh() {
		return bugManagerBlh;
	}

	public void setBugManagerBlh(BugManagerBlh bugManagerBlh) {
		this.bugManagerBlh = bugManagerBlh;
	}
	public  BaseBizLogicHandler getBlh(){
		  
		return bugManagerBlh;
	}
	

}
