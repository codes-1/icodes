package cn.com.codes.common.web;

import java.util.HashMap;

import cn.com.codes.common.blh.CommonBlh;
import cn.com.codes.common.dto.CommonDto;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;

public class CommonAction extends BaseAction<CommonBlh> {

	private CommonDto dto = new CommonDto();
	@Override
	protected void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException {
		reqEvent.setDto(dto);
	}

	@Override
	protected String _processResponse() throws BaseException {
		HashMap<?, ?> displayData = (HashMap<?, ?>) _getDisplayData();
		return forwardPage(displayData);
	}

	public CommonDto getDto() {
		return dto;
	}

	public void setDto(CommonDto dto) {
		this.dto = dto;
	}

}
