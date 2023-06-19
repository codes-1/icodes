package cn.com.codes.framework.security.config;

import java.util.HashMap;

import com.opensymphony.webwork.ServletActionContext;

import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.security.Function;
import cn.com.codes.framework.transmission.dto.BaseDto;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;
import cn.com.codes.framework.security.config.SecurityConfigBlh;

public class SecurityConfigAction extends BaseAction<SecurityConfigBlh> {

	private Function founction;

	protected void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException {

		BaseDto dto = new BaseDto();
		dto.setAttr("founction", founction);

		reqEvent.setDto(dto);
	}

	protected String _processResponse() throws BaseException {
		HashMap<?, ?> displayData = (HashMap<?, ?>) _getDisplayData();
		BaseDto dto = (BaseDto) displayData.get("dto");
		founction = (Function) dto.getAttr("founction");
		if ("setMethodsInit".equals(super.getBlhControlFlow())) {

			return "setMethodsInit";
		}

		return "functionList";
	}

	@Override
	protected String _getCustomBlhControlFlow(BusiRequestEvent reqEvent) {

		return null;
	}

	public Function getFounction() {
		return founction;
	}

	public void setFounction(Function founction) {
		this.founction = founction;
	}

}
