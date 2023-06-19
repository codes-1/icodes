package cn.com.codes.fileInfo.service;

import java.util.List;

import cn.com.codes.fileInfo.dto.FileInfoDto;
import cn.com.codes.framework.app.services.BaseService;
import cn.com.codes.object.FileInfo;



public interface FileInfoService extends BaseService{



	public List<FileInfo> getFileInfoByTypeId(FileInfoDto dto);


	public void deleteById(FileInfoDto dto);


	public void addFileInfo(FileInfoDto dto);

}
