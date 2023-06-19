package cn.com.codes.roleManager.service;

import cn.com.codes.framework.app.services.BaseService;
import cn.com.codes.object.Role;
import cn.com.codes.roleManager.dto.RoleDto;

public interface RoleService extends BaseService {

	// 根据角色id得到权限集合，更新再保存角色
	public void grantAuth(Role role, String functionIds);

	// 无条件得到权限树字符串
	public String getAuthTree();

	// 根据角色id得到自己的权限树,不带复选,只浏览用
	public String browserAuthTreeById(String roleId);

	// 根据角色id得到对应的权限树字符串
	public void getAuthTreeById(RoleDto dto);

	// 批量删除角色下的用户
	public void deleteUserFromRole(String roleId, String userIds);

	// 给角色添加用户的方法

	public void addUserToRole(String roleId, String userIds);
	
	public void addRole(RoleDto dto);
	
	public void updateRole(RoleDto dto);

	public void deleteRole(RoleDto dto);
	
	public void deleteRole(Role role);
}
