package cn.com.codes.object;

import cn.com.codes.framework.transmission.JsonInterface;

public class Project implements JsonInterface {

	/**  
	* 字段:      字段名称
	* @Fields serialVersionUID : TODO 
	*/
	private static final long serialVersionUID = 1L;
	private String projectId;
	private String projectName;
	private String projectType;
	private String createId;
	
   
	public String getProjectType() {
		return projectType;
	}


	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}


	public Project() {
	}
	
	
	public String getProjectId() {
		return projectId;
	}


	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}


	public String getProjectName() {
		return projectName;
	}


	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}


	public String getCreateId() {
		return createId;
	}


	public void setCreateId(String createId) {
		this.createId = createId;
	}


	@Override
	public String toStrUpdateInit() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String toStrList() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String toStrUpdateRest() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void toString(StringBuffer bf) {
		// TODO Auto-generated method stub
		
	}
}