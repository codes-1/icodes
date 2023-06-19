package cn.com.codes.roleManager.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.framework.app.services.BaseServiceImpl;
import cn.com.codes.object.Function;
import cn.com.codes.object.Role;
import cn.com.codes.object.SimpleFunction;
import cn.com.codes.object.SimpleUser;
import cn.com.codes.roleManager.dto.RoleDto;
import cn.com.codes.roleManager.service.RoleService;


public class RoleServiceImpl extends BaseServiceImpl implements RoleService {

	public void addRole(RoleDto dto){
		Role role = dto.getRole();
		if(role.getAccessIp()!=null&&"多个IP用分号(;)隔开".equals(role.getAccessIp().trim())){
			role.setAccessIp(null);
		}
		this.add(role);
	}
	
	public void deleteRole(RoleDto dto){
		Role role = dto.getRole();
		role = this.get(Role.class, role.getRoleId());
		if("superRole".equals(role.getRoleName())){
			return;
		}
		this.delete(role);
	}
	
	// 根据角色id得到对应的权限树字符串来形成树行结构
	@SuppressWarnings("unchecked")
	public void getAuthTreeById(RoleDto dto) {

		StringBuffer allNodeHql = new StringBuffer();
		allNodeHql.append("select new SimpleFunction(functionId,functionName,parentId) ");
		allNodeHql.append("from SimpleFunction order by seq");
		List<SimpleFunction> allNodeList = this.findByHql(allNodeHql.toString());
		dto.setAttr("allNodeList", allNodeList);
	
		allNodeHql = null;
		StringBuffer authHql = new StringBuffer();
		authHql.append("select f.functionId ");
		authHql.append("from Role r  join r.function  f where r.roleId=? and r.companyId=? and f.isleaf=1 order by f.seq");
		List authList = this.findByHql(authHql.toString(),dto.getRole().getRoleId(),
				SecurityContextHolderHelp.getCompanyId());
		dto.setAttr("authList", authList);
	}

	// 根据角色id得到自己的权限树,不带复选,只浏览用
	public String browserAuthTreeById(String roleId) {

		StringBuffer hql = new StringBuffer();
		hql.append("select new SimpleFunction(f.functionId,f.functionName,f.parentId) ");
		hql.append(" from Role r join r.function f where r.roleId=? and r.companyId=? order by f.seq");
		List<SimpleFunction> authList = this.findByHql(hql.toString(), roleId,SecurityContextHolderHelp.getCompanyId());
		StringBuffer authTree = new StringBuffer();
		if(authList==null||authList.size()==0)
			return authTree.toString();
		for(SimpleFunction sfun :authList){
			authTree.append(sfun.getParentId());
			authTree.append(",");
			authTree.append(sfun.getFunctionId());
			authTree.append(",");
			authTree.append(sfun.getFunctionName());
			authTree.append(";");			
		}
		return authTree.toString();
	}

	public void grantAuth(Role role, String functionIds) {

		String[] functionIdArray = functionIds.trim().split(" ");
		Set<SimpleFunction> newFouctions = new HashSet<SimpleFunction>();
		role = (Role)this.findByHql("from Role  r left join fetch r.function where r.roleId=?", 
				role.getRoleId()).get(0);
		Set<SimpleFunction> functions = role.getFunction();
		for (String functionId : functionIdArray) {
			if(functionId.equals("")){
				continue;
			}
			SimpleFunction function = new SimpleFunction();
			function.setFunctionId(functionId);
			newFouctions.add(function);
		}
		boolean haveChg = false;
		if(newFouctions.containsAll(functions)&&!functions.containsAll(newFouctions)) {
			functions.addAll(newFouctions);
			haveChg = true;
		}else {
			for (Iterator<SimpleFunction> it = functions.iterator(); it.hasNext();) {
				SimpleFunction f = (SimpleFunction) it.next();
				if (!newFouctions.contains(f)) {
					it.remove();
					haveChg = true;
				}
			}
			functions.addAll(newFouctions);
		}
		if(haveChg){
			role.setFunction(functions);
			this.update(role);
			this.notifyPriviListener(role.getRoleId());
		}
	}

	private void notifyPriviListener(String roleId){
		String hql = "from Role r left join fetch r.user where r.roleId=?";
		Role roleTemp = (Role)this.findByHql(hql, roleId).get(0);
		SecurityContextHolderHelp.notifyPriviListener(roleTemp.getUser());
	}
	

	public void deleteRole(Role role){
//		this.notifyPriviListener(role.getRoleId());
//		String delHql = "delete from Role where roleId=? and roleName<>? and companyId=?" ;
//		this.getHibernateGenericController().executeUpdate(sb.toString(),);
//		this.delete(role);
	}
	// 无条件得到权限树字符串
	public String getAuthTree() {
		List<Function> list = this.getAll(
				Function.class, "seq", true);
		StringBuffer authTree = new StringBuffer();
		for (Function f : list) {
			authTree.append(f.getParentFunction().getFunctionId());
			authTree.append(",");
			authTree.append(f.getFunctionId());
			authTree.append(",");
			authTree.append(f.getFunctionName());
			authTree.append(";");
		}
		return authTree.toString();
	}
	
	public void deleteUserFromRole(String roleId, String userIds) {
		String hql = "from Role r left join fetch r.user left join fetch r.function where r.roleId=? and r.roleName<>?";
		List list = this.findByHql(hql, roleId,"superRole");
		if(list==null||list.isEmpty()){
			return;
		}
		Role role = (Role)list.get(0);
		Set<SimpleUser> users = role.getUser();
		for (String userId : userIds.split(",")) {
			if("".equals(userId)||" ".equals(userId)){
				continue; 
			}
			SimpleUser user = new SimpleUser();
			user.setId(userId);
			users.remove(user);
		}
		this.update(role);
		
	
	}

	public void addUserToRole(String roleId, String userIds) {
				
		String hql = "from Role r left join fetch r.user left join fetch r.function where r.roleId=? and r.roleName<>?";
		List list = this.findByHql(hql, roleId,"superRole");
		if(list==null||list.isEmpty()){
			return;
		}
		Role role = (Role)list.get(0);
		Set<SimpleUser> users = role.getUser();
		for (String userId : userIds.trim().split(",")) {
			if("".equals(userId)||" ".equals(userId)){
				continue; 
			}
			SimpleUser user = new SimpleUser();
			user.setId(userId);
			users.add(user);
		}
		if(!users.isEmpty()){
			role.setUser(users);
		}
		this.update(role);
		
	}

	
	public void updateRole(RoleDto dto){
		//因为权限和用户都配了inverse="false" 所以这里只能用HQL update
		Role role = dto.getRole();
		StringBuffer sb = new StringBuffer();
		if("YES".equals(dto.getIsAdmin())){
			sb.append("update Role set roleName=?,remark=?,updateDate=?,");
			sb.append("accessIp=? where roleId=? and roleName= ?");
			this.getHibernateGenericController().executeUpdate(sb.toString(),
					"superRole","superRole",new Date(),role.getAccessIp(),role.getRoleId(),"superRole");
			role = this.get(Role.class, role.getRoleId());
			return;
		}
		sb.append("update Role set roleName=?,remark=?,updateDate=?,");
		sb.append("accessIp=? where roleId=? and roleName <> ?");
		this.getHibernateGenericController().executeUpdate(sb.toString(),
				role.getRoleName(),role.getRemark(),new Date(),role.getAccessIp(),role.getRoleId(),"superRole");
		role = this.get(Role.class, role.getRoleId());
	}




	
	
}
