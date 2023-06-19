package cn.com.codes.fileInfo.web;

import java.util.HashMap;

import cn.com.codes.fileInfo.blh.FileInfoBlh;
import cn.com.codes.fileInfo.dto.FileInfoDto;
import cn.com.codes.framework.app.blh.BaseBizLogicHandler;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;


public class FileInfoAction extends BaseAction<FileInfoBlh>{

	/**  
	* 字段:      字段名称
	* @Fields serialVersionUID : TODO 
	*/
	private static final long serialVersionUID = 1L;
	
	FileInfoDto dto ;
	private FileInfoBlh fileInfoBlh;
	
	protected void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException {
		if(dto==null){
			dto = new FileInfoDto();
		}
		reqEvent.setDto(dto);

	}

	protected String _processResponse() throws BaseException {
		HashMap<?, ?> displayData = (HashMap<?, ?>) _getDisplayData();
		return forwardPage(displayData);
	}

	public FileInfoDto getDto() {
		return dto;
	}

	public void setDto(FileInfoDto dto) {
		this.dto = dto;
	}

	public FileInfoBlh getFileInfoBlh() {
		return fileInfoBlh;
	}

	public void setFileInfoBlh(FileInfoBlh fileInfoBlh) {
		this.fileInfoBlh = fileInfoBlh;
	}
	public  BaseBizLogicHandler getBlh(){
		  
		return fileInfoBlh;
	}
	
}
