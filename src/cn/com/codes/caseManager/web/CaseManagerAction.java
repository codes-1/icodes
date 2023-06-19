package cn.com.codes.caseManager.web;

import java.io.File;
import java.util.HashMap;

import cn.com.codes.caseManager.blh.CaseManagerBlh;
import cn.com.codes.caseManager.dto.CaseManagerDto;
import cn.com.codes.framework.app.blh.BaseBizLogicHandler;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;

public class CaseManagerAction extends BaseAction<CaseManagerBlh> {

	CaseManagerDto dto = new CaseManagerDto();
	private File attchFile = null;
	private CaseManagerBlh caseBlh;
	protected void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException {
		reqEvent.setDto(dto);
		//System.out.println(super.getBlhControlFlow());
	}

	protected String _processResponse() throws BaseException {
		HashMap<?, ?> displayData = (HashMap<?, ?>) _getDisplayData();
		return forwardPage(displayData);
	}

	public CaseManagerDto getDto() {
		return dto;
	}

	public void setDto(CaseManagerDto dto) {
		this.dto = dto;
	}

	public File getAttchFile() {
		return attchFile;
	}

	public void setAttchFile(File attchFile) {
		this.attchFile = attchFile;
		dto.setAttr("attchFile", attchFile);
	}

	public CaseManagerBlh getCaseBlh() {
		return caseBlh;
	}

	public void setCaseBlh(CaseManagerBlh caseBlh) {
		this.caseBlh = caseBlh;
	}
	public  BaseBizLogicHandler getBlh(){
		  
		return caseBlh;
	}
	

}
