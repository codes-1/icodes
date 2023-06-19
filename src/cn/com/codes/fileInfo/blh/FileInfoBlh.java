package cn.com.codes.fileInfo.blh;

import java.util.List;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import cn.com.codes.bugManager.blh.BugManagerBlh;
import cn.com.codes.bugManager.dto.BugManagerDto;
import cn.com.codes.common.dto.PageModel;
import cn.com.codes.common.util.FileInfoVo;
import cn.com.codes.common.util.StringUtils;
import cn.com.codes.fileInfo.dto.FileInfoDto;
import cn.com.codes.fileInfo.service.FileInfoService;
import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.common.JsonUtil;
import cn.com.codes.framework.common.ListObject;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.object.FileInfo;
import cn.com.codes.object.User;


public class FileInfoBlh extends BusinessBlh {
	
	private static Logger logger = Logger.getLogger(FileInfoBlh.class);
	private FileInfoService fileInfoService;
	
	
	public FileInfoService getFileInfoService() {
		return fileInfoService;
	}
	public void setFileInfoService(FileInfoService fileInfoService) {
		this.fileInfoService = fileInfoService;
	}

	public View getFileInfoByTypeId(BusiRequestEvent req){
		FileInfoDto dto = super.getDto(FileInfoDto.class, req);
		if(dto.getFileInfo()!=null){
			List<FileInfo> fileInfos = fileInfoService.getFileInfoByTypeId(dto);
			super.writeResult(JsonUtil.toJson(fileInfos));
			
		}
		return super.globalAjax();
	}
	
	public View addFileInfo(BusiRequestEvent req){
		FileInfoDto dto = super.getDto(FileInfoDto.class, req);
		List<FileInfoVo> fileInfos = dto.getFileInfos();
		List<FileInfoVo> filesInfo = null;
		if(fileInfos!=null && fileInfos.size()>0){
			if(logger.isInfoEnabled()) {
				//System.out.println(fileInfos.toString());
			}
			JSONArray json = JSONArray.fromObject(fileInfos.toString().replace("[[", "[").replace("]]", "]]"));
			filesInfo= (List<FileInfoVo>)JSONArray.toCollection(json, FileInfoVo.class);
			dto.setFileInfos(filesInfo);
		}
		fileInfoService.addFileInfo(dto);
		super.writeResult(JsonUtil.toJson("success"));
		return super.globalAjax();
	}
	
	public View delteById(BusiRequestEvent req){
		FileInfoDto dto = super.getDto(FileInfoDto.class, req);
		if(dto.getFileInfo()!=null && !StringUtils.isNullOrEmpty(dto.getFileInfo().getFileId())){
			fileInfoService.deleteById(dto);
			super.writeResult(JsonUtil.toJson("success"));
			
		}
		return super.globalAjax();
	}
	
}
