package cn.com.codes.impExpManager.web;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import cn.com.codes.caseManager.dto.CaseManagerDto;
import cn.com.codes.framework.app.blh.BaseBizLogicHandler;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;
import cn.com.codes.impExpManager.blh.CaseImpExpBlh;

public class CaseImpExpAction extends BaseAction {

	private CaseManagerDto dto ;
	private CaseImpExpBlh caseImpExpBlh;
	private InputStream inputStream;
	private File importFile;  
	private String importFileContentType; 
	private String importFileFileName; 
	@Override
	protected void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException {
		if(dto==null){
			dto = new CaseManagerDto();
		}
		reqEvent.setDto(dto);	

	}
	protected String _processResponse() throws BaseException {
		HashMap<?, ?> displayData = (HashMap<?, ?>) _getDisplayData();
		inputStream = (InputStream) displayData.get("excel");
		return SUCCESS;
	}
	public CaseManagerDto getDto() {
		return dto;
	}
	public void setDto(CaseManagerDto dto) {
		this.dto = dto;
	}

	
	public  BaseBizLogicHandler getBlh(){
		  
		return caseImpExpBlh;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public CaseImpExpBlh getCaseImpExpBlh() {
		return caseImpExpBlh;
	}
	public void setCaseImpExpBlh(CaseImpExpBlh caseImpExpBlh) {
		this.caseImpExpBlh = caseImpExpBlh;
	}
	public File getImportFile() {
		return importFile;
	}
	public void setImportFile(File importFile) {
		this.importFile = importFile;
	}
	public String getImportFileContentType() {
		return importFileContentType;
	}
	public void setImportFileContentType(String importFileContentType) {
		this.importFileContentType = importFileContentType;
	}
	public String getImportFileFileName() {
		return importFileFileName;
	}
	public void setImportFileFileName(String importFileFileName) {
		this.importFileFileName = importFileFileName;
	}

}
