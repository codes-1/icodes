package cn.com.codes.object;

import cn.com.codes.framework.transmission.JsonInterface;


public class FileInfo implements JsonInterface{
	
	private static final long serialVersionUID = 1L;
	
	private String fileId;//文件id
	private String type;//类型 bug/case
	private Long typeId;//类型id bugid/caseid
	private String filePath;//文件相对路径
	private String relativeName;//文件原始名称
	
	public FileInfo(){
		
	}
	
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
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

	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getRelativeName() {
		return relativeName;
	}
	public void setRelativeName(String relativeName) {
		this.relativeName = relativeName;
	}
	
	public FileInfo(String fileId,String type,Long typeId,String filePath,String relativeName){
		this.fileId=fileId;
		this.type=type;
		this.typeId=typeId;
		this.filePath=filePath;
		this.relativeName=relativeName;
	}
	
	/* (非 Javadoc)   
	* <p>Title: toStrUpdateInit</p>   
	* <p>Description: </p>   
	* @return   
	* @see cn.com.codes.framework.transmission.JsonInterface#toStrUpdateInit()   
	*/
	@Override
	public String toStrUpdateInit() {
		// TODO Auto-generated method stub
		return null;
	}
	/* (非 Javadoc)   
	* <p>Title: toStrList</p>   
	* <p>Description: </p>   
	* @return   
	* @see cn.com.codes.framework.transmission.JsonInterface#toStrList()   
	*/
	@Override
	public String toStrList() {
		// TODO Auto-generated method stub
		return null;
	}
	/* (非 Javadoc)   
	* <p>Title: toStrUpdateRest</p>   
	* <p>Description: </p>   
	* @return   
	* @see cn.com.codes.framework.transmission.JsonInterface#toStrUpdateRest()   
	*/
	@Override
	public String toStrUpdateRest() {
		// TODO Auto-generated method stub
		return null;
	}
	/* (非 Javadoc)   
	* <p>Title: toString</p>   
	* <p>Description: </p>   
	* @param bf   
	* @see cn.com.codes.framework.transmission.JsonInterface#toString(java.lang.StringBuffer)   
	*/
	@Override
	public void toString(StringBuffer bf) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
