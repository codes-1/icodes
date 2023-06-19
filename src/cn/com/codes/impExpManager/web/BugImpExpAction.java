package cn.com.codes.impExpManager.web;

import java.io.InputStream;
import java.util.HashMap;

import cn.com.codes.bugManager.dto.BugManagerDto;
import cn.com.codes.framework.app.blh.BaseBizLogicHandler;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;
import cn.com.codes.impExpManager.blh.BugImpExpBlh;

public class BugImpExpAction extends BaseAction {

	private BugManagerDto dto ;
	private BugImpExpBlh bugImpExpBlh;
	private InputStream inputStream;
	@Override
	protected void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException {
		if(dto==null){
			dto = new BugManagerDto();
		}
		reqEvent.setDto(dto);	

	}
	protected String _processResponse() throws BaseException {
		HashMap<?, ?> displayData = (HashMap<?, ?>) _getDisplayData();
		inputStream = (InputStream) displayData.get("excel");
		return SUCCESS;
	}
	public BugManagerDto getDto() {
		return dto;
	}
	public void setDto(BugManagerDto dto) {
		this.dto = dto;
	}
	public BugImpExpBlh getBugImpExpBlh() {
		return bugImpExpBlh;
	}
	public void setBugImpExpBlh(BugImpExpBlh bugImpExpBlh) {
		this.bugImpExpBlh = bugImpExpBlh;
	}
	
	public  BaseBizLogicHandler getBlh(){
		  
		return bugImpExpBlh;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
}
