/**
 * 
 */
package cn.com.codes.object;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import cn.com.codes.framework.transmission.JsonInterface;


public class TestCasePackage implements Serializable{
	
	private String packageId;
	private String taskId;
	private String packageName;
	private String executor;
	private String execEnvironment;
	private String remark;
	private Date createTime;
	private Date updateTime;
	private String createrId;
	private Set<UserTestCasePkg> userTestCasePkgs;
/*	private Set<TestCase_CasePkg> testCase_CasePkgs;*/
	
	/**
	 * 
	 */
	public TestCasePackage() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the packageId
	 */
	public String getPackageId() {
		return packageId;
	}
	/**
	 * @param packageId the packageId to set
	 */
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	
	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}


	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}


	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}
	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	/**
	 * @return the executor
	 */
	public String getExecutor() {
		return executor;
	}
	/**
	 * @param executor the executor to set
	 */
	public void setExecutor(String executor) {
		this.executor = executor;
	}
	/**
	 * @return the execEnvironment
	 */
	public String getExecEnvironment() {
		return execEnvironment;
	}
	/**
	 * @param execEnvironment the execEnvironment to set
	 */
	public void setExecEnvironment(String execEnvironment) {
		this.execEnvironment = execEnvironment;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}



	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}



	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}



	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}



	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the userTestCasePkgs
	 */
	public Set<UserTestCasePkg> getUserTestCasePkgs() {
		return userTestCasePkgs;
	}

	/**
	 * @param userTestCasePkgs the userTestCasePkgs to set
	 */
	public void setUserTestCasePkgs(Set<UserTestCasePkg> userTestCasePkgs) {
		this.userTestCasePkgs = userTestCasePkgs;
	}

	/**
	 * @return the createrId
	 */
	public String getCreaterId() {
		return createrId;
	}

	/**
	 * @param createrId the createrId to set
	 */
	public void setCreaterId(String createrId) {
		this.createrId = createrId;
	}

	
}
