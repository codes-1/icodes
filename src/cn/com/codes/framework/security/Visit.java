package cn.com.codes.framework.security;

import java.util.HashSet;
import java.util.Set;

import cn.com.codes.framework.security.VisitUser;


public  class Visit {

	private VisitUser userInfo;
	private Set<String> errors ;
	private String taskId =null;
	private String upDirectory;
	private boolean menuLoad = false;
	private String flag;//标志位，是否参与项目
	private String haveProject;//标志位，是否有项目
	//分析度量
	private String analyProjectName;
	private String analyProNum;

	public void clearErrors() {
		if(errors==null){
			return;
		}
		errors.clear();
	}

	public Set<String> getErrors() {
		return errors;
	}

	public void addError(String err) {
		if(errors==null){
			errors = new HashSet<String>();
		}
		errors.add(err);
	}

	public void setErrors(Set<String> errors) {
		this.errors = errors;
	}

	public VisitUser getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(VisitUser userInfo) {
		this.userInfo = userInfo;
	}

	public  <T> T getUserInfo(Class<T>  clasz){
		return (T)this.userInfo;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getUpDirectory() {
		return upDirectory;
	}

	public void setUpDirectory(String upDirectory) {
		this.upDirectory = upDirectory;
	}

	public boolean isMenuLoad() {
		return menuLoad;
	}

	public void setMenuLoad(boolean menuLoad) {
		this.menuLoad = menuLoad;
	}

	/**  
	* @return flag 
	*/
	public String getFlag() {
		return flag;
	}

	/**  
	* @param flag flag 
	*/
	public void setFlag(String flag) {
		this.flag = flag;
	}

	/**
	 * @return the analyProjectName
	 */
	public String getAnalyProjectName() {
		return analyProjectName;
	}

	/**
	 * @param analyProjectName the analyProjectName to set
	 */
	public void setAnalyProjectName(String analyProjectName) {
		this.analyProjectName = analyProjectName;
	}

	/**
	 * @return the analyProNum
	 */
	public String getAnalyProNum() {
		return analyProNum;
	}

	/**
	 * @param analyProNum the analyProNum to set
	 */
	public void setAnalyProNum(String analyProNum) {
		this.analyProNum = analyProNum;
	}

	public String getHaveProject() {
		return haveProject;
	}

	public void setHaveProject(String haveProject) {
		this.haveProject = haveProject;
	}

    
	
}