package cn.com.codes.fileInfo.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.codes.common.util.FileInfoVo;
import cn.com.codes.common.util.StringUtils;
import cn.com.codes.fileInfo.dto.FileInfoDto;
import cn.com.codes.fileInfo.service.FileInfoService;
import cn.com.codes.framework.app.services.BaseServiceImpl;
import cn.com.codes.object.FileInfo;


public class FileInfoServiceImpl extends BaseServiceImpl implements FileInfoService{

	@SuppressWarnings("unchecked")
	public List<FileInfo> getFileInfoByTypeId(FileInfoDto dto) {
		// TODO Auto-generated method stub
		StringBuffer hql = new StringBuffer("select new FileInfo(fileId,type,typeId,filePath,relativeName) from FileInfo f where 1=1 ");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		FileInfo fileInfo = dto.getFileInfo();
		if(!StringUtils.isNullOrEmpty(fileInfo.getType())){
			hql.append(" and f.type=:type");
			paraMap.put("type", fileInfo.getType());
		}
		if(fileInfo.getTypeId()!=null){
			hql.append(" and f.typeId=:typeId");
			paraMap.put("typeId", fileInfo.getTypeId());
		}
		
		List<FileInfo> fileInfos = this.findByHqlWithValuesMap(hql.toString(), paraMap, true);
		if(fileInfos!=null&& fileInfos.size()>0){
			return fileInfos;
		}else{
			return null;
		}
	}

	@Override
	public void addFileInfo(FileInfoDto dto) {
		// TODO Auto-generated method stub
		List<FileInfoVo> fileList = dto.getFileInfos();
		if(fileList!=null && fileList.size()>0){
			for (FileInfoVo fileInfoVo : fileList) {
				FileInfo fileInfo = new FileInfo();
				fileInfo.setType(dto.getType());
				fileInfo.setTypeId(dto.getTypeId());
				fileInfo.setRelativeName(fileInfoVo.getFileOriginalName());
				fileInfo.setFilePath(fileInfoVo.getFileUrl());
				this.add(fileInfo);
			}
		}
	}

	@Override
	public void deleteById(FileInfoDto dto) {
		// TODO Auto-generated method stub
		String hql = "delete from FileInfo where fileId=?" ;
		this.getHibernateGenericController().executeUpdate(hql, dto.getFileInfo().getFileId());
	}
	
}
