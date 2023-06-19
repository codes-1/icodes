package cn.com.codes.framework.security.config;

import java.util.HashMap;

import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.security.SysLogConfigure;
import cn.com.codes.framework.transmission.dto.BaseDto;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;
import cn.com.codes.framework.security.config.LogConfigBlh;

public class LogConfigAction extends BaseAction<LogConfigBlh> {

	
	private SysLogConfigure logConfigure ;

	protected void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException {

		BaseDto dto = new BaseDto();
		dto.setAttr("logConfigure", logConfigure);
		reqEvent.setDto(dto);
	}

	protected String _processResponse() throws BaseException {
		HashMap<?, ?> displayData = (HashMap<?, ?>) _getDisplayData();
		BaseDto dto = (BaseDto) displayData.get("dto");
		logConfigure = (SysLogConfigure) dto.getAttr("logConfigure");
		if ("modifyInit".equals(super.getBlhControlFlow())) {
			return "modify";
		}
		if ("addInit".equals(super.getBlhControlFlow())) {
			return "modify";
		}
		return "list";
	}

	@Override
	protected String _getCustomBlhControlFlow(BusiRequestEvent reqEvent) {

		return null;
	}

	public SysLogConfigure getLogConfigure() {
		return logConfigure;
	}

	public void setLogConfigure(SysLogConfigure logConfigure) {
		this.logConfigure = logConfigure;
	}

}
