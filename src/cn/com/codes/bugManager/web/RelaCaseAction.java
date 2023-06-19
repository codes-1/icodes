package cn.com.codes.bugManager.web;

import java.util.HashMap;

import cn.com.codes.bugManager.blh.RelaCaseBlh;
import cn.com.codes.bugManager.dto.RelaCaseDto;
import cn.com.codes.framework.app.blh.BaseBizLogicHandler;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;

public class RelaCaseAction extends BaseAction {

	RelaCaseDto dto ;
	private RelaCaseBlh relaCaseBlh ; 
	protected void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException {
		if(dto==null){
			dto = new RelaCaseDto();
		}
		reqEvent.setDto(dto);

	}

	protected String _processResponse() throws BaseException {
		HashMap<?, ?> displayData = (HashMap<?, ?>) _getDisplayData();
		return forwardPage(displayData);
	}

	public RelaCaseDto getDto() {
		return dto;
	}

	public void setDto(RelaCaseDto dto) {
		this.dto = dto;
	}

	public RelaCaseBlh getRelaCaseBlh() {
		return relaCaseBlh;
	}

	public void setRelaCaseBlh(RelaCaseBlh relaCaseBlh) {
		this.relaCaseBlh = relaCaseBlh;
	}
	public  BaseBizLogicHandler getBlh(){
		  
		return relaCaseBlh;
	}
	

}
