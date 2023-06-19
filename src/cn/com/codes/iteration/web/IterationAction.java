package cn.com.codes.iteration.web;

import cn.com.codes.framework.app.blh.BaseBizLogicHandler;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;
import cn.com.codes.iteration.blh.IterationBlh;
import cn.com.codes.iteration.dto.IterationDto;



public class IterationAction extends BaseAction<IterationBlh> {

	private static final long serialVersionUID = 1L;
	private IterationDto dto = new IterationDto();
	private IterationBlh iterationBlh;

	public IterationDto getDto() {
		return dto;
	}

	public void setDto(IterationDto dto) {
		this.dto = dto;
	}

	public IterationBlh getIterationBlh() {
		return iterationBlh;
	}

	public void setIterationBlh(IterationBlh iterationBlh) {
		this.iterationBlh = iterationBlh;
	}
	
	public BaseBizLogicHandler getBlh(){
		return iterationBlh;
	}

	@Override
	protected void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException {
		reqEvent.setDto(dto);
	}

}
