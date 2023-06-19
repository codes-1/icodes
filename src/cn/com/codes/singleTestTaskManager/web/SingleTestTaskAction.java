package cn.com.codes.singleTestTaskManager.web;

import cn.com.codes.framework.app.blh.BaseBizLogicHandler;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;
import cn.com.codes.singleTestTaskManager.blh.SingleTestTaskBlh;
import cn.com.codes.singleTestTaskManager.dto.SingleTestTaskDto;

public class SingleTestTaskAction extends BaseAction<SingleTestTaskBlh> {

	private SingleTestTaskDto dto = new SingleTestTaskDto();
	private SingleTestTaskBlh singleTestTaskBlh ;
	protected void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException {
		reqEvent.setDto(dto);

	}

	public SingleTestTaskDto getDto() {
		return dto;
	}

	public void setDto(SingleTestTaskDto dto) {
		this.dto = dto;
	}
	public  BaseBizLogicHandler getBlh(){
		  
		return singleTestTaskBlh;
	}

	public SingleTestTaskBlh getSingleTestTaskBlh() {
		return singleTestTaskBlh;
	}

	public void setSingleTestTaskBlh(SingleTestTaskBlh singleTestTaskBlh) {
		this.singleTestTaskBlh = singleTestTaskBlh;
	}
	

}
