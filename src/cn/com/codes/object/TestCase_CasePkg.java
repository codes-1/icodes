/**
 * 
 */
package cn.com.codes.object;

import java.io.Serializable;


public class TestCase_CasePkg implements Serializable{
	
   private String pkgCaseId;
   private String packageId;
   private String testCaseId;
   private String executorId;
   private Integer execStatus; 
	/**
	 * @return the pkgCaseId
	 */
	public String getPkgCaseId() {
		return pkgCaseId;
	}
	/**
	 * @param pkgCaseId the pkgCaseId to set
	 */
	public void setPkgCaseId(String pkgCaseId) {
		this.pkgCaseId = pkgCaseId;
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
	 * @return the testCaseId
	 */
	public String getTestCaseId() {
		return testCaseId;
	}
	/**
	 * @param testCaseId the testCaseId to set
	 */
	public void setTestCaseId(String testCaseId) {
		this.testCaseId = testCaseId;
	}
	/**
	 * @return the executorId
	 */
	public String getExecutorId() {
		return executorId;
	}
	/**
	 * @param executorId the executorId to set
	 */
	public void setExecutorId(String executorId) {
		this.executorId = executorId;
	}
	/**
	 * @return the execStatus
	 */
	public Integer getExecStatus() {
		return execStatus;
	}
	/**
	 * @param execStatus the execStatus to set
	 */
	public void setExecStatus(Integer execStatus) {
		this.execStatus = execStatus;
	}
   
	
}
