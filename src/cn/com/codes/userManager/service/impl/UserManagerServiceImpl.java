package cn.com.codes.userManager.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.service.DrawHtmlListDateService;
import cn.com.codes.common.util.Utilities;
import cn.com.codes.framework.app.services.BaseServiceImpl;
import cn.com.codes.framework.common.CryptUtil;
import cn.com.codes.object.Group;
import cn.com.codes.object.User;
import cn.com.codes.roleManager.service.RoleService;
import cn.com.codes.userManager.dto.UserManagerDto;
import cn.com.codes.userManager.service.UserManagerService;

public class UserManagerServiceImpl extends BaseServiceImpl implements
		UserManagerService {

	private RoleService roleService;
	
	private DrawHtmlListDateService drawHtmlListDateService;
	
	public void reSetPwd(String userId){
		User user = this.get(User.class, userId);
		String upHql = "update  User u set u.password=? WHERE u.id =? and u.companyId=? ";
		String companyId = SecurityContextHolderHelp.getCompanyId() ;
		this.executeUpdateByHql(upHql,  new Object[]{CryptUtil.cryptString("itest"),userId,companyId});
		
	}
	public void ldeleteUser(String userId,int delCount){
		
		String delHql = "update  User u set u.delFlag ="+delCount +" ,u.status=0 WHERE u.id =? and u.companyId=? and isAdmin=0";
		String companyId = SecurityContextHolderHelp.getCompanyId() ;
		Object[] values = new Object[]{userId,companyId};
		this.executeUpdateByHql(delHql, values);
	}
	public void updateUserSuperUserFlg(String userId,Integer superUserFlg){
		String hql = "update cn.com.codes.object.User set isAdmin=? where id=? and companyId=?";
		String companyId = SecurityContextHolderHelp.getCompanyId();
		this.executeUpdateByHql(hql, new Object[]{superUserFlg,userId,companyId});
	}
	public User login(User loginUser) {

		String[] propertyNames = new String[] { "loginName", "password" };
		String[] values = new String[] { loginUser.getLoginName(),
				loginUser.getPassword() };
		List<User> list = this.findByProperties(
				User.class, propertyNames, values);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}


	public String userMaintence(UserManagerDto dto){
		//判断该类型是否已存在
		String hql = "from User u where u.loginName=?";
		String flag = "0";
		@SuppressWarnings("unchecked")
		List<User> user01 = this.findByHql(hql,dto.getUser().getLoginName());
		if(dto.getUser().getId() == null || "".equals(dto.getUser().getId())){
			if(user01 != null && user01.size() > 0){
			return "existed";
			}	
		}else{
			if(user01 != null && user01.size() == 1 && !user01.get(0).getId().equals(dto.getUser().getId())){
				flag = "0";
				return "existed"; 
			}else if(user01 != null && user01.size() == 1 && user01.get(0).getId().equals(dto.getUser().getId())){ 
				flag = "1";
				
			}
		}
		User user = dto.getUser();
		user.setCompanyId(SecurityContextHolderHelp.getCompanyId());
		if(user.getGroupIds() != null && !"".equals(user.getGroupIds())){
			String[] groupIds = user.getGroupIds().trim().split(",");  
			if(user.getGroupList()==null){
				List<Group> groupList = new ArrayList<Group>(groupIds.length);
				user.setGroupList(groupList);
			}
			for(String groupId : groupIds){
				Group group = new Group();
				group.setId(groupId);
				user.getGroupList().add(group);
			}
		}
		if(user.getId() == null || "".equals(user.getId())){
			//user.setMyHome("/bugManager/bugManagerAction!loadAllMyBug.action");
			user.setInsertDate(new Date());
			user.setUpdateDate(user.getInsertDate());
			String decodePwd = CryptUtil.cryptString(user.getPassword());
			user.setPassword(decodePwd);
			this.add(user);
		}else{
			if("1".equals(flag)){
				user01.get(0).setUpdateDate(new Date()); 
				user01.get(0).setName(dto.getUser().getName());
				user01.get(0).setPassword(dto.getUser().getPassword());
				user01.get(0).setEmail(dto.getUser().getEmail());
				user01.get(0).setQuestion(dto.getUser().getQuestion());
				user01.get(0).setAnswer(dto.getUser().getAnswer());
				user01.get(0).setCompanyId(dto.getUser().getCompanyId());
				user01.get(0).setEmployeeId(dto.getUser().getEmployeeId());  
				user01.get(0).setOfficeTel(dto.getUser().getOfficeTel()); 
				user01.get(0).setHeadShip(dto.getUser().getHeadShip());
				user01.get(0).setStatus(dto.getUser().getStatus()); 
				user01.get(0).setLoginName(dto.getUser().getLoginName());
				user01.get(0).setId(dto.getUser().getId());
				user01.get(0).setTel(dto.getUser().getTel());
				user01.get(0).setMyHome(dto.getUser().getMyHome());
				user01.get(0).setDelFlag(dto.getUser().getDelFlag());
				user01.get(0).setIsAdmin(dto.getUser().getIsAdmin());
				user01.get(0).setInsertDate(dto.getUser().getInsertDate()); 
				user01.get(0).setGroupList(dto.getUser().getGroupList()); 
				this.update(user01.get(0));	
			}
			
		}
		return "success";
	}
	public void groupMaintence(UserManagerDto dto){
		addUserToGroup(dto.getGroup());
		dto.getGroup().setAdminFlag(0);
		dto.getGroup().setCompanyId(SecurityContextHolderHelp.getCompanyId());
		if(dto.getGroup().getId()!=null&&!"".equals(dto.getGroup().getId().trim())){
			dto.getGroup().setUpdateDate(dto.getGroup().getInsertDate());
			this.update(dto.getGroup());
			return;
			
		}
		dto.getGroup().setInsertDate(new Date());
		dto.getGroup().setUpdateDate(dto.getGroup().getInsertDate());
		this.add(dto.getGroup());
		
	}
	private void addUserToGroup(Group group) {
		if(group.getUserIds()==null||"".equals(group.getUserIds())){
			group.setUser(null);
			return;
		}
		String[] uids = group.getUserIds().trim().split(" ");
		Set<User> users = new HashSet<User>(uids.length);
		for (String userId : uids) {
			User user = new User();
			user.setId(userId);
			users.add(user);
		}
		group.setUser(users);
		uids = null;
	}

	@SuppressWarnings("unchecked")
	public void deleteUserFromGroup(String GroupId, String userIds) {

		Group group = this.get(Group.class,
				GroupId);
		Collection<User> users = group.getUser();
		for (String userId : userIds.split(",")) {
			User user = new User();
			user.setId(userId);
			users.remove(user);

		}
		this.update(group);

	}

	public User userUpdateInit(String userId){
		String hql ="from User u left join fetch u.groupList where u.id=? and u.companyId=?" ;
		List<User> list = this.findByHql(hql, userId,SecurityContextHolderHelp.getCompanyId());
		if(list==null||list.size()==0){
			return null ;
		}else{
			User user = list.get(0);
			Hibernate.initialize(user.getGroupList());
			return user ;
		}
	}
	

	
	public void importUsers(UserManagerDto dto){
		Date tempDate = new Date();
		String companyId = SecurityContextHolderHelp.getCompanyId();
		String repeatMessage = "下列登录帐号重复:<br>";
		boolean serialFlag = true;
		List serialUsers = this.findByHqlWithValuesMap(dto.getHql(), dto.getHqlParamMaps(), false);
		if(Utilities.isNullOrEmpty(serialUsers)){
			serialFlag = false;
		}
		List users = dto.getObjectList();
		User user = null;
		String loginName;
		if(serialFlag){
			Iterator it = users.iterator();
			while(it.hasNext()){
				loginName = ((User)it.next()).getLoginName();
				if(serialUsers.contains(loginName)){
					repeatMessage += loginName + "<br>";
				}
			}
		}
		if("下列登录帐号重复:<br>".equals(repeatMessage)){
			Iterator it = users.iterator();
			while(it.hasNext()){
				user = (User)it.next();
				user.setCompanyId(companyId);
				user.setIsAdmin(0);
				user.setStatus(1);
				user.setQuestion(user.getLoginName());
				user.setAnswer(user.getLoginName());
				user.setInsertDate(tempDate);
				user.setUpdateDate(tempDate);
				user.setDelFlag(0);
				String decodePwd = CryptUtil.cryptString(user.getPassword());
				user.setPassword(decodePwd);
				user.setoldPwd(user.getPassword());
				//user.setMyHome("/bugManager/bugManagerAction!loadAllMyBug.action");
				this.add(user);
			}
			repeatMessage = "导入成功";
		}
		dto.setCustomMessage(repeatMessage);
	}
	
	public void updateUserStatus(String userId,String status){
		String companyId = SecurityContextHolderHelp.getCompanyId();
		String upHql = "update  User u set u.status=? WHERE u.id =? and u.companyId=?  and isAdmin=0";
		Object[] values = new Object[]{Integer.parseInt(status),userId,companyId};
		executeUpdateByHql(upHql, values);
	}
	public DrawHtmlListDateService getDrawHtmlListDateService() {
		return drawHtmlListDateService;
	}

	public void setDrawHtmlListDateService(
			DrawHtmlListDateService drawHtmlListDateService) {
		this.drawHtmlListDateService = drawHtmlListDateService;
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}


}
