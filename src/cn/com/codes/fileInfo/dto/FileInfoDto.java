package cn.com.codes.fileInfo.dto;

import java.util.List;

import cn.com.codes.common.util.FileInfoVo;
import cn.com.codes.framework.transmission.dto.BaseDto;
import cn.com.codes.object.FileInfo;


public class FileInfoDto extends BaseDto{
	
	/**  
	* 字段:      字段名称
	* @Fields serialVersionUID : TODO 
	*/
	private static final long serialVersionUID = 1L;

	private FileInfo fileInfo;
	private String type;//类型 bug/case
	private Long typeId;//类型id bugid/caseid
	private List<FileInfoVo> fileInfos;
	
	public FileInfo getFileInfo() {
		return fileInfo;
	}

	public void setFileInfo(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public List<FileInfoVo> getFileInfos() {
		return fileInfos;
	}

	public void setFileInfos(List<FileInfoVo> fileInfos) {
		this.fileInfos = fileInfos;
	}
	
}
