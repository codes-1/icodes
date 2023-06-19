package cn.com.codes.roleManager.blh;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.dto.PageModel;
import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.common.JsonUtil;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.object.Role;
import cn.com.codes.object.SimpleFunction;
import cn.com.codes.object.User;
import cn.com.codes.roleManager.dto.RoleDto;
import cn.com.codes.roleManager.service.RoleService;
import cn.com.codes.userManager.dto.UserVo;
import cn.com.codes.roleManager.blh.RoleBlh;


public class RoleBlh extends BusinessBlh {

	private RoleService roleService;
	private static Logger log = Logger.getLogger(RoleBlh.class);

	public View addRole(BusiRequestEvent req){
		RoleDto dto = (RoleDto) req.getDto();
		dto.getRole().setInsertDate(new Date());
		dto.getRole().setCompanyId(SecurityContextHolderHelp.getCompanyId());
		boolean chkRest = roleService.reNameChkInComp("Role", dto.getRole().getRoleName(), "roleName", null, null);
		if(chkRest){
			super.writeResult("reName");
			return super.globalAjax();
		}
		roleService.addRole(dto);
		if("true".equals(dto.getIsAjax())){
			super.writeResult(JsonUtil.toJson("success^"+dto.getRole().getRoleId()));
			dto.setRole(null);
			dto = null;
			return super.globalAjax();
		}
		return super.getView();
	}
	public View updRole(BusiRequestEvent req){
		RoleDto dto = (RoleDto) req.getDto();
		boolean chkRest = roleService.reNameChkInComp("Role", dto.getRole().getRoleName(), "roleName", "roleId", dto.getRole().getRoleId());
		if(chkRest){
			super.writeResult("reName");
			return super.globalAjax();
		}
		roleService.updateRole(dto);
		if("true".equals(dto.getIsAjax())){
			dto.setRole(null);
			dto = null;
			super.writeResult("success");
			return super.globalAjax();
		}
		return super.getView();
	}	
	public View updRoleInit(BusiRequestEvent req){
		RoleDto dto = (RoleDto) req.getDto();
		Role role = roleService.get(Role.class, dto.getRole().getRoleId());
		if("true".equals(dto.getIsAjax())){
			role.setFunction(null);
			role.setUser(null);
			role.setPrivilege(null);
			role.setUserVoSet(null);
			super.writeResult(super.addJsonPre("dto.role", role));
			dto.setRole(null);
			dto = null;
			return super.globalAjax();
		}
		return super.getView();
	}		
	public View deleteRole(BusiRequestEvent req) throws BaseException {
		RoleDto dto = (RoleDto) req.getDto();
		roleService.deleteRole(dto);
		if("true".equals(dto.getIsAjax())){
			super.writeResult("success");
			return super.globalAjax();
		}
		return super.getView();
	}

	//这里完全可以是没返回值的方法，这里是做返回非view的示例而己
	public View browserAuth(BusiRequestEvent req) throws BaseException {
		return super.getView();
	}
	
	public View loadBrowserAuth(BusiRequestEvent req) throws BaseException {
		RoleDto dto = (RoleDto) req.getDto();
		dto.setAuthTree(roleService.browserAuthTreeById(dto.getRole()
				.getRoleId()));
		super.writeResult(JsonUtil.toJson(dto.getAuthTree()));
		return super.globalAjax();
	}
	
	public View loadAuthTree(BusiRequestEvent req){
		RoleDto dto = (RoleDto) req.getDto();
		StringBuffer allNodeHql = new StringBuffer();
		allNodeHql.append("select new SimpleFunction(functionId,functionName,parentId) ");
		allNodeHql.append("from SimpleFunction order by seq");
		List<SimpleFunction> allNodeList = roleService.findByHql(allNodeHql.toString());
		StringBuffer authTree = new StringBuffer();
		int count =0;
		for (int i = 0; i < allNodeList.size(); i++) {
			SimpleFunction f = allNodeList.get(i);
			if(count>0){
				authTree.append(";");
			}
			authTree.append(f.getParentId());
			authTree.append(",");
			authTree.append(f.getFunctionId());
			authTree.append(",");
			authTree.append(f.getFunctionName());
			count++;
		}
		dto.setAuthTree(authTree.toString());
		return super.getView();
	}
	public View authGrant(BusiRequestEvent req) throws BaseException {
		return super.getView();
	}
	
	public View loadGrantAuthInit(BusiRequestEvent req) throws BaseException {
		RoleDto dto = (RoleDto) req.getDto();
		Role role = dto.getRole();
		role = roleService.get(Role.class, role.getRoleId());
		roleService.getAuthTreeById(dto);
		List<SimpleFunction> allNodeList  = (List<SimpleFunction>)dto.getAttr("allNodeList");
		StringBuffer authTree = new StringBuffer();
		int count =0;
		for (int i = 0; i < allNodeList.size(); i++) {
			SimpleFunction f = allNodeList.get(i);
			if(count>0){
				authTree.append(";");
			}
			authTree.append(f.getParentId());
			authTree.append(",");
			authTree.append(f.getFunctionId());
			authTree.append(",");
			authTree.append(f.getFunctionName());
			count++;
		}
		allNodeList = null;
		dto.setAuthTree(authTree.toString());
		List authList = (List)dto.getAttr("authList");
		count= 0;
		if(authList!=null&&authList.size()>0){
			StringBuffer authSb = new StringBuffer();
			for(Object auth:authList){
				if(count>0){
					authSb.append(" ");
				}
				authSb.append(auth.toString());
				count++;
			}
			dto.setFunctionIds(authSb.toString());
		}
		
		dto.clearDataContainer();
		
		super.writeResult(JsonUtil.toJson(dto));
		return super.globalAjax();
	}

	public View grantRoleAuth(BusiRequestEvent req) throws BaseException {
		RoleDto dto = (RoleDto) req.getDto();
		Role role = dto.getRole();
		String functionIds = dto.getFunctionIds().trim();
		roleService.grantAuth(role, functionIds);
		super.writeResult("success");
		return super.globalAjax();
	}

	public View roleList(BusiRequestEvent req) throws BaseException {
		return super.getView();
	}
	
	public View loadRoleList(BusiRequestEvent req) throws BaseException {

		RoleDto dto = (RoleDto) req.getDto();
		this.buildRoleQuery(dto);
		List roleList = roleService.findByHqlWithValuesMap(dto.getHql(), dto.getPageNo(), dto.getPageSize(), 
				"r.roleId", dto.getHqlParamMaps(), false);
		setRoleRelaUser(roleList);
		//System.out.println(JsonUtil.toJson(roleList));
		dto.setHqlParamMaps(null);
		dto.setHql(null);
		
		PageModel pageModel = new PageModel(); 
		pageModel.setRows(roleList);
		Integer total =  (Integer) SecurityContextHolder.getContext().getAttr("pageInfoTotalRows");
		pageModel.setTotal(total);
		
		super.writeResult(JsonUtil.toJson(pageModel));
		return super.globalAjax();
	
	}
	private void setRoleRelaUser(List<Role> roleList){
		if(roleList==null||roleList.size()==0){
			return;
		}
		StringBuffer hql = new StringBuffer();
		hql.append("select distinct new cn.com.codes.userManager.dto.UserVo( u.id,(u.loginName||'('||u.name||')') as name ,u.loginName,r.roleId as roleId) from User u  join u.roleList r where (u.isAdmin =0 or u.isAdmin =2) and r.roleId in(:roleIds) and u.delFlag=0");
		List<String> roleIds = new ArrayList<String>(roleList.size());
		for(Role role :roleList){
			roleIds.add(role.getRoleId());
		}
		roleService.sortStringList(roleIds);
		Map praValuesMap = new HashMap();
		praValuesMap.put("roleIds", roleIds);
		List<UserVo> userList = roleService.findByHqlWithValuesMap(hql.toString(), praValuesMap, false);;
		if(userList!=null&&userList.size()>0){
			for(Role role :roleList){
				for(UserVo user :userList){
					if(user.getRoleId().equals(role.getRoleId())){
						if(role.getUserVoSet()==null){
							role.setUserVoSet(new HashSet<UserVo>());
						}
						role.getUserVoSet().add(user);
					}
				}
			}
		}
	}
	private void buildRoleQuery(RoleDto dto ){
		Role role = dto.getRole();
		StringBuffer hql = new StringBuffer();
		hql.append("select distinct new Role( ");
		hql.append("r.roleId,r.roleName,r.remark,r.accessIp) ");
		Map praValuesMap = new HashMap();
		praValuesMap.put("compId", SecurityContextHolderHelp.getCompanyId());
		dto.setHqlParamMaps(praValuesMap);
		if(role==null){
			hql.append("from Role r where r.companyId=:compId");
			dto.setHql(hql.toString());
			return ;
		}
		if(role.getUserIds()!=null&&!"".equals(role.getUserIds().trim())
				&&role.getFunIds()!=null&&!"".equals(role.getFunIds().trim())){
			hql.append("from Role r join r.user u join r.function f  ");
			hql.append("where r.companyId=:compId and u.id in (:uIds) ");
			hql.append("and  f.functionId in (:fIds)");
			String[] uids = role.getUserIds().split(" ");
			List<String> uList = new ArrayList<String>();
			for(String uId :uids){
				uList.add(uId);
			}
			roleService.sortStringList(uList);
			uids = null;
			praValuesMap.put("uIds", uList);	
			String[] fids = role.getFunIds().split(" ");
			List<String> fList = new ArrayList<String>();
			for(String fId :fids){
				fList.add(fId);
			}
			roleService.sortStringList(fList);
			praValuesMap.put("fIds", fList);
			fids = null;
		}else if(role.getUserIds()!=null&&!"".equals(role.getUserIds().trim())){
			hql.append("from Role r join r.user u  ");
			hql.append("where r.companyId=:compId and u.id in (:uIds)");
			String[] uids = role.getUserIds().split(" ");
			List<String> uList = new ArrayList<String>();
			for(String uId :uids){
				uList.add(uId);
			}
			roleService.sortStringList(uList);
			uids = null;
			praValuesMap.put("uIds", uList);					
		}else if(role.getFunIds()!=null&&!"".equals(role.getFunIds().trim())){
			hql.append("from Role r join r.function f  ");
			hql.append("where r.companyId=:compId and f.functionId in (:fIds) ");
			String[] fids = role.getFunIds().split(" ");
			List<String> fList = new ArrayList<String>();
			for(String fId :fids){
				fList.add(fId);
			}
			roleService.sortStringList(fList);
			praValuesMap.put("fIds", fList);
			fids = null;
		}
		if(hql.indexOf(" from Role")<0){
			hql.append("from Role r where r.companyId=:compId ");
		}
		if(role.getRoleName()!=null&&!"".equals(role.getRoleName().trim())){
			hql.append("and r.roleName like :roleName");
			praValuesMap.put("roleName", "%"+role.getRoleName()+"%");
		}
		if(role.getAccessIp()!=null&&!"".equals(role.getAccessIp().trim())){
			hql.append("and r.accessIp=:accessIp");
			praValuesMap.put("accessIp", role.getAccessIp());
		}
		dto.setHql(hql.toString());
	}
	public View roleUserList(BusiRequestEvent req) throws BaseException {

		return super.getView();
	}
	
	public View loadRoleUserList(BusiRequestEvent req) throws BaseException {

		RoleDto dto = (RoleDto) req.getDto();
		this.buildUserQuery(dto, true);
		List userList = roleService.findByHqlWithValuesMap(dto.getHql(), dto.getPageNo(),
				dto.getPageSize(), "u.id", dto.getHqlParamMaps(), false);
		
		PageModel pageModel = new PageModel(); 
		pageModel.setRows(userList);
		Integer total =  (Integer) SecurityContextHolder.getContext().getAttr("pageInfoTotalRows");
		pageModel.setTotal(total);
		
		super.writeResult(JsonUtil.toJson(pageModel));
		return super.globalAjax();
	}
	
	public View selingUerList(BusiRequestEvent req){
		
		RoleDto dto = (RoleDto) req.getDto();
		this.buildUserQuery(dto, false);
		List userList = roleService.findByHqlWithValuesMap(dto.getHql(), dto.getPageNo(),
				dto.getPageSize(), "u.id", dto.getHqlParamMaps(), false);
		
		PageModel pageModel = new PageModel(); 
		pageModel.setRows(userList);
		Integer total =  (Integer) SecurityContextHolder.getContext().getAttr("pageInfoTotalRows");
		pageModel.setTotal(total);
		
		super.writeResult(JsonUtil.toJson(pageModel));
		return super.globalAjax();
		
	}
	private void buildUserQuery(RoleDto dto ,boolean inRole){
		
		String roleId = dto.getRole().getRoleId();
		StringBuffer hql = new StringBuffer();
		hql.append("select distinct new User(u.id,(u.loginName||'('||u.name||')') as name,u.loginName,u.tel,");
		hql.append("u.officeTel,u.email,u.headShip,u.status,u.employeeId) ") ;
		Map praValuesMap = new HashMap();
		praValuesMap.put("compId", SecurityContextHolderHelp.getCompanyId());
		praValuesMap.put("roleId", roleId);
		dto.setHqlParamMaps(praValuesMap);
		if(dto.getUser()==null){
			if(inRole){
				hql.append(" from User u join u.roleList r where r.roleId=:roleId and u.companyId=:compId ");
			}else{
				hql.append(" from User u  where  u.delFlag=0 and u.status='1' and  u.companyId=:compId and u.id not in(select ru.id from Role r join r.user ru where r.roleId=:roleId) ");
			}
			hql.append("and (u.isAdmin =0 or u.isAdmin =2) order by u.loginName");
			dto.setHql(hql.toString());
			return ;
		}else{
			User user = dto.getUser();
			if(!"-1".equals(user.getGroupIds().trim())){
				if(inRole){
					hql.append("from User u join u.groupList g  join u.roleList r ");
					hql.append("where u.companyId=:compId and  r.roleId =:roleId ");
				}else{
					hql.append(" from User u  join u.groupList g where u.delFlag=0 and  u.companyId=:compId and u.id not in(select ru.id from Role r join r.user ru where r.roleId=:roleId)");
				}
				hql.append("and g.id=:gid ");
				praValuesMap.put("gid", user.getGroupIds());
			}else{
				if(inRole){
					hql.append("from User u join u.roleList r where  r.roleId=:roleId and u.companyId=:compId ");
				}else{
					hql.append(" from User u   where  u.delFlag=0 and   u.companyId=:compId and u.id not in(select ru.id from Role r join r.user ru where r.roleId=:roleId) ");
				}
			}
			
			if(user.getLoginName()!=null&&!"".equals(user.getLoginName().trim())){
				hql.append("and u.loginName like :loginName ");
				praValuesMap.put("loginName", "%"+user.getLoginName()+"%");
			}
			if(user.getName()!=null&&!"".equals(user.getName().trim())){
				hql.append("and u.name like :name ");
				praValuesMap.put("name", "%"+user.getName()+"%");
			}
			if(user.getEmployeeId()!=null&&!"".equals(user.getEmployeeId().trim())){
				hql.append("and u.employeeId like :employeeId ");
				praValuesMap.put("employeeId", "%"+user.getEmployeeId()+"%");
			}
			if(user.getOfficeTel()!=null&&!"".equals(user.getOfficeTel().trim())){
				hql.append("and u.officeTel like :officeTel ");
				praValuesMap.put("officeTel", "%"+user.getOfficeTel()+"%");
			}

			if(user.getStatus()!=-1){
				hql.append("and u.status = :status ");
				praValuesMap.put("status", user.getStatus());
			}
		}
		hql.append("and (u.isAdmin =0 or u.isAdmin =2) order by u.loginName");
		dto.setHql(hql.toString());
	}


	public View delUsrFromRole(BusiRequestEvent req) throws BaseException {

		RoleDto dto = (RoleDto) req.getDto();
		String userIds = dto.getUserIds();
		roleService.deleteUserFromRole(dto.getRole().getRoleId(), userIds);
		super.writeResult("success");
		return super.globalAjax();
	}

	public View userManager(BusiRequestEvent req) throws BaseException{
		return this.roleUserList(req);
	}
	public View addUserToRole(BusiRequestEvent req) throws BaseException {

		RoleDto dto = (RoleDto) req.getDto();
		String userIds = dto.getUserIds();
		roleService.addUserToRole(dto.getRole().getRoleId(), userIds);
		super.writeResult("success");
		return super.globalAjax();
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

}
