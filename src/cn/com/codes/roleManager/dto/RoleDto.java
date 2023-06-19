package cn.com.codes.roleManager.dto;

import cn.com.codes.framework.transmission.dto.BaseDto;
import cn.com.codes.object.Role;
import cn.com.codes.object.User;

public class RoleDto extends BaseDto {

	/**
	 * 传输页面数据与后台数据的dto类
	 */
	private static final long serialVersionUID = 1L;

	private String functionIds;// 所有权限的id集合

	private String authInRole; // 角色所具有的权限树字符串

	private String userIds;// 角色中所有用户的id数组

	private Role role;

	private String authTree;

	private User user;
	private String isAdmin;
	
	public String getAuthInRole() {
		return authInRole;
	}

	public void setAuthInRole(String authInRole) {
		this.authInRole = authInRole;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getFunctionIds() {
		return functionIds;
	}

	public void setFunctionIds(String functionIds) {
		this.functionIds = functionIds;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getAuthTree() {
		return authTree;
	}

	public void setAuthTree(String authTree) {
		this.authTree = authTree;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}

}
