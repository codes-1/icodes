/**
 * 
 */
package cn.com.codes.object;

import java.io.Serializable;


public class UserTestCasePkg implements Serializable{

	private String userPkgId;
	private String packageId;
	private String userId;
	/**
	 * @return the userPkgId
	 */
	public String getUserPkgId() {
		return userPkgId;
	}
	/**
	 * @param userPkgId the userPkgId to set
	 */
	public void setUserPkgId(String userPkgId) {
		this.userPkgId = userPkgId;
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
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	
	
}
