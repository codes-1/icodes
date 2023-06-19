package cn.com.codes.userManager.dto;

import java.io.File;

import cn.com.codes.framework.transmission.dto.BaseDto;
import cn.com.codes.object.Company;
import cn.com.codes.object.Group;
import cn.com.codes.object.User;

public class UserManagerDto extends BaseDto {

	private User user ;
	private Company company ;
	private Group group ;
	private String userIds;//组中所有用户的id数组
	private String userName;//查询时用
	private String userFilesRealPath ;
	private String viewCode;
	private String inVoldPwd;
	private String vpassword;
	private File importUser;
	public UserManagerDto(){
		
	}
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	public String getUserIds() {
		return userIds;
	}
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	public String getUserFilesRealPath() {
		return userFilesRealPath;
	}
	public void setUserFilesRealPath(String userFilesRealPath) {
		this.userFilesRealPath = userFilesRealPath;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getViewCode() {
		return viewCode;
	}
	public void setViewCode(String viewCode) {
		this.viewCode = viewCode;
	}
	public String getInVoldPwd() {
		return inVoldPwd;
	}
	public void setInVoldPwd(String inVoldPwd) {
		this.inVoldPwd = inVoldPwd;
	}
	public String getVpassword() {
		return vpassword;
	}
	public void setVpassword(String vpassword) {
		this.vpassword = vpassword;
	}
	public File getImportUser() {
		return importUser;
	}
	public void setImportUser(File importUser) {
		this.importUser = importUser;
	}

}
