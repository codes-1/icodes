package cn.com.codes.userManager.blh;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.sun.org.apache.bcel.internal.generic.NEW;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.dto.PageModel;
import cn.com.codes.common.util.Utilities;
import cn.com.codes.common.util.uploadExcel.parsexml.ParseXmlTool;
import cn.com.codes.common.util.uploadExcel.parsexml.XmlEntity;
import cn.com.codes.common.util.uploadExcel.validate.ValidateExcel;
import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.common.CryptUtil;
import cn.com.codes.framework.common.HtmlListQueryObj;
import cn.com.codes.framework.common.JsonUtil;
import cn.com.codes.framework.common.ListObject;
import cn.com.codes.framework.common.config.PropertiesBean;
import cn.com.codes.framework.common.util.Context;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.exception.DataBaseException;
import cn.com.codes.framework.security.SecurityPrivilege;
import cn.com.codes.framework.security.UrlSecurityCheckInterceptor;
import cn.com.codes.framework.security.Visit;
import cn.com.codes.framework.security.VisitUser;
import cn.com.codes.framework.security.filter.SecurityContext;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.transmission.JsonInterface;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.object.Company;
import cn.com.codes.object.Group;
import cn.com.codes.object.User;
import cn.com.codes.userManager.dto.GroupVo;
import cn.com.codes.userManager.dto.UserManagerDto;
import cn.com.codes.userManager.service.UserManagerService;

//insert into t_function(functionid, functionname, parentid, levelnum, isleaf, seq , page, security_url)values('10', '导入用户', '3', 4, 1, 10, 0, 'userManagerAction!importUsers');
//insert into t_role_function_real select t.roleid ,'10' as FUNCTIONID ,t.insertdate ,t.updatedate from t_role t where t.rolename='superRole';

public class UserManagerBlh extends BusinessBlh {

	private UserManagerService userManagerService;
	private SecurityPrivilege securityPrivilege ;
	private static String validCodeChk = "true";
	private PropertiesBean conf ; 
	
    private static int userCount =500;
	public View drawMenu(BusiRequestEvent req){
		
		return super.globalAjax();
	}
	public UserManagerBlh(){
		conf = (PropertiesBean) Context.getInstance().getBean("ContextProperties");
		String validCodeSw = conf.getProperty("itest.validCode");
		if(validCodeSw!=null&&!"true".equalsIgnoreCase(validCodeSw.trim())){
			validCodeChk = "false";
		}
	}
	
	public View chgPersonMgr(BusiRequestEvent req){
		UserManagerDto dto = getDto(req);
		
		String userId = dto.getUserIds();
		int mgrFlg = dto.getUser().getIsAdmin();
		if(mgrFlg==2){
			mgrFlg = 0;
		}else{
			mgrFlg = 2;
		}
		if(!"admin".equals(SecurityContextHolderHelp.getLoginName())){
			writeResult("deny");
			return getView("ajaxRest");
		}
		String companyId = SecurityContextHolderHelp.getCompanyId();
		Object[] paras = new Object[]{mgrFlg,userId,companyId};
		String hql = "update cn.com.codes.object.User u set u.isAdmin=? where u.id=? and u.companyId=? and u.loginName<>'admin'";
		userManagerService.executeUpdateByHql(hql, paras);
		writeResult("success");
		dto = null;
		return getView("ajaxRest");
	}
	public View update2Init(BusiRequestEvent req){
		UserManagerDto dto = getDto(req);
		userManagerService.reSetPwd(dto.getUserIds());
		writeResult("success");
		dto = null;
		return getView("ajaxRest");
	}
	public View userMaintence(BusiRequestEvent req) {
		UserManagerDto dto = getDto(req);
		User user = dto.getUser();
		user.setCompanyId(SecurityContextHolderHelp.getCompanyId());
		if (user.getId() == null || "".equals(user.getId())) {
			if(!canAddUser()){
				writeResult("overCount");	
				dto = null;
				return getView("ajaxRest");
			}
			if(user.getIsAdmin() == null){
				user.setIsAdmin(0);
			}
			user.setStatus(1);
			user.setDelFlag(0);
		} 
		String pwdChek = this.oldPwdCheck(user);
		if(pwdChek!=null){
			super.writeResult(pwdChek);
			return super.globalAjax();
		}
		String flag = "";
		if(user.getId() == null || "".equals(user.getId())){
			synchronized(this){
				flag = userManagerService.userMaintence(dto);
			}
		}else{
			flag = userManagerService.userMaintence(dto);
		}

		writeResult(flag);	
		dto = null;
		return getView("ajaxRest");
	}

	private String oldPwdCheck(User user){
		if(user.getId() == null || "".equals(user.getId())){
			return null;
		}
		StringBuffer hql = new StringBuffer();
		hql.append("from cn.com.codes.object.User  ");
		hql.append("  where id=? and companyId=? ");
		String companyId = SecurityContextHolderHelp.getCompanyId();
		List<User> list = userManagerService.findByHql(hql.toString(), user.getId(),companyId);
		User userTemp = list.get(0);
		user.setInsertDate(userTemp.getInsertDate());
		user.setDocUserId(userTemp.getDocUserId());
		user.setSvn(userTemp.getSvn());
		if(user.getChgPwdFlg()==null){
			user.setPassword(userTemp.getPassword());	
		}
		else if(user.getChgPwdFlg()!=null&&user.getChgPwdFlg().equals(user.getId())){
			String oldPwd = user.getOldPwd();
			if(!userTemp.getPassword().equals(CryptUtil.cryptString(oldPwd))){
				return "customMsg^原密码不正确" ;
			}else{
				String decodePwd = CryptUtil.cryptString(user.getPassword());
				user.setPassword(decodePwd);
			}
		}else if(user.getChgPwdFlg().equals("")){ 
			user.setPassword(userTemp.getPassword());
		}else if("2".equals(user.getChgPwdFlg())){
			String oldPwd = user.getOldPwd();
			if(!userTemp.getPassword().equals(CryptUtil.cryptString(oldPwd))){
				return "customMsg^原密码不正确" ;
			}else{
				user.setPassword(userTemp.getPassword());	
			}
		}
			/*else{
		}
			user.setPassword(userTemp.getPassword());
		}*/
		return null;
	}
	public View addUser(BusiRequestEvent req){
		UserManagerDto dto = getDto(req);
		Date currDate = new Date();
		dto.getUser().setInsertDate(currDate);
		dto.getUser().setUpdateDate(currDate);
		String loginName = dto.getUser().getLoginName().trim();
		dto.getUser().setLoginName(loginName);
		synchronized(this){
			if(canAddUser()){
				return this.userMaintence(req);
			}else{
				writeResult("overCount");	
				dto = null;
				return getView("ajaxRest");
			}
			
		}
		
	}
	private boolean canAddUser(){
		String sql = "select count(*) from T_USER where STATUS <>'0'";
		List list = userManagerService.findBySql(sql, null);
		if(list==null||list.isEmpty()){
			return true;
		}else{
			if((Integer.parseInt(list.get(0).toString())+1)>userCount){
				return false;
			}else{
				return true;
			}
		}
	}
	
	private boolean canAddUser(List impUserList){
		
		String sql = "select count(*) from T_USER where STATUS <>'0'";
		List list = userManagerService.findBySql(sql, null);
		if((list==null||list.isEmpty())&&impUserList.size()>userCount){
			return false;
		}else if((list==null||list.isEmpty())&&impUserList.size()<=userCount){
			return true;
		}else{
			if((Integer.parseInt(list.get(0).toString())+impUserList.size())>userCount){
				return false;
			}else{
				return true;
			}
		}
	}
	public View updUser(BusiRequestEvent req){
		UserManagerDto dto = getDto(req);
		User user = dto.getUser();
		if(user.getChgPwdFlg()!=null&&user.getChgPwdFlg().equals(user.getId())){
			String oldPwd = user.getOldPwd();
			StringBuffer hql = new StringBuffer();
			hql.append("select new  User(id,name ,loginName,companyId,password,isAdmin,myHome,docUserId) ");
			hql.append(" from User where id=? and companyId=? ");
			String companyId = SecurityContextHolderHelp.getCompanyId();
			List<User> list = userManagerService.findByHql(hql.toString(), user.getId(),companyId);
			if(list.get(0).getPassword().equals(CryptUtil.cryptString(oldPwd))){
				super.writeResult("customMsg^原密码不正确");
				return super.globalAjax();
			}
		}
		return this.userMaintence(req);
	}
	public View addGroup(BusiRequestEvent req){
		return this.groupMaintence(req);
	}
	
	public View updGroup(BusiRequestEvent req){
		  return this.groupMaintence(req);
	}
	public View groupUpInit(BusiRequestEvent req){
		UserManagerDto dto = getDto(req);
		Group group = userManagerService.get(Group.class, dto.getGroup().getId());
		StringBuffer hql = new StringBuffer();
		hql.append("select new User(u.id,u.name ,u.loginName,g.id as groupIds) from User u  join u.groupList g where g.id =? order by u.loginName");
		List<User> userList = userManagerService.findByHql(hql.toString(), group.getId()); 
		
		if(userList!=null&&userList.size()>0){ 
			 group.setUser(userManagerService.listToSet(userList));
		}else{
			group.setUser(null);
		}
		super.writeResult(JsonUtil.toJson(group));
		
//		super.writeResult(group.toStrUpdateInit());
		return super.globalAjax();
			
	}
	
	@SuppressWarnings("unchecked")
	public View notInGroup(BusiRequestEvent req){
		UserManagerDto dto = getDto(req);
		Group group = userManagerService.get(Group.class, dto.getGroup().getId());
		StringBuffer hql = new StringBuffer();
		hql.append("select new User(u.id,u.name ,u.loginName,g.id as groupIds) from User u  join u.groupList g where g.id =? order by u.loginName");
		List<User> userList = userManagerService.findByHql(hql.toString(), group.getId());
		List<String> userIds01 = new ArrayList<String>();
		 
		if(userList!=null&&userList.size()>0){
			for(int i=0;i<userList.size();i++){
				//将存在于改组的用户id存于userIds01
				userIds01.add(userList.get(i).getId());
			}
			Map praValMap = new HashMap();
			//查找user表中没有以上组员id的用户
			StringBuffer hql1 = new StringBuffer();
			hql1.append("select new cn.com.codes.object.User(u.id,u.name,u.loginName,u.tel,");
			hql1.append("u.officeTel,u.email,u.headShip,u.status,u.employeeId,u.isAdmin) ") ;
			hql1.append("from cn.com.codes.object.User u where u.id not in (:ids) ");
			praValMap.put("ids",userIds01);
			//拼接查询条件
			if(dto.getUser()!=null){
				if(dto.getUser().getLoginName()!=null&&!"".equals(dto.getUser().getLoginName().trim())){
					hql1.append(" and u.loginName like :loginName");
					praValMap.put("loginName", "%"+dto.getUser().getLoginName()+"%");
				}
				if(dto.getUser().getName()!=null&&!"".equals(dto.getUser().getName().trim())){
					hql1.append(" and u.name like :name");
					praValMap.put("name", "%"+dto.getUser().getName()+"%");
				}
				if(dto.getUser().getEmployeeId()!=null&&!"".equals(dto.getUser().getEmployeeId().trim())){
					hql1.append(" and u.employeeId like :employeeId");
					praValMap.put("employeeId", "%"+dto.getUser().getEmployeeId()+"%");
				}
				if(dto.getUser().getOfficeTel()!=null&&!"".equals(dto.getUser().getOfficeTel().trim())){
					hql1.append(" and u.officeTel like :officeTel");
					praValMap.put("officeTel", "%"+dto.getUser().getOfficeTel()+"%");
				}

				if(dto.getUser().getStatus()!=null&&dto.getUser().getStatus()!=-1){
					hql1.append(" and u.status = :status");
					praValMap.put("status", dto.getUser().getStatus());
				}
			}
			hql1.append(" order by u.loginName");
			dto.setHql(hql1.toString());
			dto.setHqlParamMaps(praValMap);
			
		}else{
			StringBuffer hql02 = new StringBuffer();
			hql02.append("select new cn.com.codes.object.User(u.id,u.name,u.loginName,u.tel) ");
			Map praValMap01 = new HashMap();
			praValMap01.put("companyId", SecurityContextHolderHelp.getCompanyId());
			hql02.append("from cn.com.codes.object.User u where u.delFlag=0 and u.companyId=:companyId  order by u.loginName");
			dto.setHql(hql02.toString()); 
			dto.setHqlParamMaps(praValMap01);
		} 
		List<User> usersNotInGroup = userManagerService.findByHqlWithValuesMap(dto.getHql(), dto.getPageNo(), 
                dto.getPageSize(), "u.id", dto.getHqlParamMaps(), false); 
		PageModel pg = new PageModel();
		Integer total =  (Integer) SecurityContextHolder.getContext().getAttr("pageInfoTotalRows");
		pg.setRows(usersNotInGroup);
		pg.setTotal(total);
		dto.setHql(null);
		dto.setHqlParamMaps(null);
		dto.setUser(null);
		dto = null;
		super.writeResult(JsonUtil.toJson(pg));
		return super.globalAjax();
		/*group.setUser(userManagerService.listToSet(usersNotInGroup));
		super.writeResult(JsonUtil.toJson(group));
//		super.writeResult(group.toStrUpdateInit());
		return super.globalAjax();*/
	 

	}
	
	public View delGroup(BusiRequestEvent req){
		UserManagerDto dto = getDto(req);
		userManagerService.delete(dto.getGroup());
		super.writeResult("success");
		return super.getView();
	}
	private  View groupMaintence(BusiRequestEvent req) {
		UserManagerDto dto = getDto(req);
		boolean chkRest = userManagerService.reNameChkInComp("Group", dto.getGroup().getName(), "name", "id", dto.getGroup().getId());
		if(chkRest){
			super.writeResult("reName");
			return super.globalAjax();
		}
		userManagerService.groupMaintence(dto);
		super.writeResult("success^"+dto.getGroup().getId());
		return super.globalAjax();
	}

	public View userList(BusiRequestEvent req) {

//     	UserManagerDto dto = getDto(req);
//		this.buildFindUserQuery(dto);
//		List userList = userManagerService.findByHqlWithValuesMap(dto.getHql(), dto.getPageNo(), 
//                dto.getPageSize(), "u.id", dto.getHqlParamMaps(), false);
//		if(!"true".equals(dto.getIsAjax())){
//			dto.setJsonData(userList);
//			return getView("userList");
//		}
//		writeResult(super.listToJson(userList));
//		dto = null;
//		return getView("ajaxRest");
		
		return super.getView();
	}
	
	@SuppressWarnings("rawtypes")
	public View userListLoad(BusiRequestEvent req) {
		UserManagerDto dto = super.getDto(UserManagerDto.class,req);
		this.buildFindUserQuery(dto);
		List userList = userManagerService.findByHqlWithValuesMap(dto.getHql(), dto.getPageNo(), 
        dto.getPageSize(), "u.id", dto.getHqlParamMaps(), false);
		PageModel pg = new PageModel();
		Integer total =  (Integer) SecurityContextHolder.getContext().getAttr("pageInfoTotalRows");
		pg.setRows(userList);
		pg.setTotal(total);
		super.writeResult(JsonUtil.toJson(pg));
		return super.globalAjax();

	}
	public View findUsers(BusiRequestEvent req) {

		UserManagerDto dto = getDto(req);
		this.buildFindUserQuery(dto);
		List userList = userManagerService.findByHqlWithValuesMap(dto.getHql(), dto.getPageNo(), 
				                           dto.getPageSize(), "u.id", dto.getHqlParamMaps(), false);
		PageModel pg = new PageModel();
		Integer total =  (Integer) SecurityContextHolder.getContext().getAttr("pageInfoTotalRows");
		pg.setRows(userList);
		pg.setTotal(total);
		dto.setHql(null);
		dto.setHqlParamMaps(null);
		dto = null;
		super.writeResult(JsonUtil.toJson(pg));
		return super.globalAjax();
		
//		this.writeResult(super.listToJson(userList));
//		dto.setHql(null);
//		dto.setHqlParamMaps(null);
//		dto = null;
//		return getView("ajaxRest"); 
	}
	
	private void buildFindUserQuery(UserManagerDto dto){
		StringBuffer hql = new StringBuffer();
		hql.append("select new cn.com.codes.object.User(u.id,u.name,u.loginName,u.tel,");
		hql.append("u.officeTel,u.email,u.headShip,u.status,u.employeeId,u.isAdmin) ") ;
		Map praValMap = new HashMap();
		praValMap.put("companyId", SecurityContextHolderHelp.getCompanyId());
		User user = dto.getUser();
		if(user==null){
			dto.setHqlParamMaps(praValMap);
			hql.append("from cn.com.codes.object.User u where u.delFlag=0 and u.companyId=:companyId  order by u.loginName");
			//hql.append("from User u where u.delFlag=0 ");
			dto.setHql(hql.toString());
			return ;
		}
		if(user.getGroupIds()!=null&&!"-1".equals(user.getGroupIds())){
			hql.append("from cn.com.codes.object.User u join u.groupList gp ");
			hql.append("where gp.id=:gpId  and u.delFlag=0 and u.companyId=:companyId");
			//hql.append(" and ");
			praValMap.put("gpId", user.getGroupIds());
		}else{
			hql.append("from cn.com.codes.object.User u where u.delFlag=0 and u.companyId=:companyId");
			//hql.append("from User u where u.delFlag=0 ");
		}
		
		if(user.getLoginName()!=null&&!"".equals(user.getLoginName().trim())){
			hql.append(" and u.loginName like :loginName");
			praValMap.put("loginName", "%"+user.getLoginName()+"%");
		}
		if(user.getName()!=null&&!"".equals(user.getName().trim())){
			hql.append(" and u.name like :name");
			praValMap.put("name", "%"+user.getName()+"%");
		}
		if(user.getEmployeeId()!=null&&!"".equals(user.getEmployeeId().trim())){
			hql.append(" and u.employeeId like :employeeId");
			praValMap.put("employeeId", "%"+user.getEmployeeId()+"%");
		}
		if(user.getOfficeTel()!=null&&!"".equals(user.getOfficeTel().trim())){
			hql.append(" and u.officeTel like :officeTel");
			praValMap.put("officeTel", "%"+user.getOfficeTel()+"%");
		}

		if(user.getStatus()!=null&&user.getStatus()!=-1){
			hql.append(" and u.status = :status");
			praValMap.put("status", user.getStatus());
		}
		hql.append(" order by u.loginName");
		dto.setHqlParamMaps(praValMap);
		dto.setHql(hql.toString());
	}
	public View groupList(BusiRequestEvent req) {

		return super.getView();
	}
	
	public View groupListLoad(BusiRequestEvent req) { 
		
		UserManagerDto dto = super.getDto(UserManagerDto.class,req);
		this.buildGroupQuery(dto);
		List groupList = userManagerService.findByHqlWithValuesMap(dto.getHql(), dto.getPageNo(),
				dto.getPageSize(), "g.id",dto.getHqlParamMaps(),false);
		this.setGroupRelaUser(groupList);
		PageModel pg = new PageModel();
		Integer total =  (Integer) SecurityContextHolder.getContext().getAttr("pageInfoTotalRows");
		pg.setRows(groupList);
		pg.setTotal(total);
		super.writeResult(JsonUtil.toJson(pg));
		return super.globalAjax();
		
		
		
//		StringBuffer sbf = new StringBuffer();
//		dto.toJson2(groupList, sbf);
//		System.out.println(JsonUtil.toJson(groupList));
//		dto.setHql(null);
//		dto.setHqlParamMaps(null);
//		if("true".equals(dto.getIsAjax())){
//			super.writeResult(sbf.toString());
//			dto = null;
//			return super.globalAjax();
//		}
//		dto.setListStr(sbf.toString());
//		return super.getView();
	}
	
	private void setGroupRelaUser(List<GroupVo> groupList){
		if(groupList==null||groupList.size()==0){
			return;
		}
		StringBuffer hql = new StringBuffer();
		hql.append("select new User(u.id,u.name ,u.loginName,g.id as groupIds) from User u  join u.groupList g where g.id in(:gids) order by u.loginName");
		List<String> gids = new ArrayList<String>(groupList.size());
		for(GroupVo group :groupList){
			gids.add(group.getId());
		}
		userManagerService.sortStringList(gids);
		Map praValuesMap = new HashMap();
		praValuesMap.put("gids", gids);
		List<User> userList = userManagerService.findByHqlWithValuesMap(hql.toString(), praValuesMap, false);;
		if(userList!=null&&userList.size()>0){
			for(GroupVo group :groupList){
				for(User user :userList){
					if(user.getGroupIds().equals(group.getId())){
						if(group.getUser()==null){
							group.setUser(new HashSet<User>());
						}
						group.getUser().add(user);
					}
				}
			}
		}
	}
	@SuppressWarnings("unchecked")
	private void buildGroupQuery(UserManagerDto dto ){
		StringBuffer hql = new StringBuffer();
		@SuppressWarnings("rawtypes")
		Map praValuesMap = new HashMap();
		praValuesMap.put("companyId", SecurityContextHolderHelp.getCompanyId());
		//这里用GroupVo是因为用Group时，hibernate报错，
		hql.append(" select new cn.com.codes.userManager.dto.GroupVo(g.id,g.name,g.remark)");
		List<String> uIds = new ArrayList<String>();
		if(dto.getGroup()!=null&&dto.getGroup().getUserIds()!=null&&!"".equals(dto.getGroup().getUserIds())){
			hql.append(" from Group g join g. user u where g.companyId=:companyId and u.id in (:uIds)");
			for(String uId :dto.getGroup().getUserIds().split(" ")){
				uIds.add(uId);
			}
			praValuesMap.put("uIds", uIds);
		}else{
			hql.append(" from Group g  where g.companyId=:companyId");
//			hql.append(" from Group g join g. user u where g.companyId=:companyId ");
		}
		if(dto.getGroup()!=null&&dto.getGroup().getName()!=null&&!"".equals(dto.getGroup().getName().trim())){
			hql.append(" and g.name like :name ");
			praValuesMap.put("name", "%"+dto.getGroup().getName()+"%");
		}
//		if(dto.getGroup()!=null&&dto.getUser().getName()!=null){
//			hql.append(" and u.name like :name ");
//			praValuesMap.put("name", "%"+dto.getUser().getName()+"%");
//		}
		hql.append(" order by g.updateDate desc ");
		dto.setHql(hql.toString());
		dto.setHqlParamMaps(praValuesMap);
	}
	private List<ListObject> getListObjs(){
		
		HtmlListQueryObj queryObj = new HtmlListQueryObj();
		queryObj.setKeyPropertyName("id");
		queryObj.setValuePropertyName("name");
		Map<String,Object> calParaValues = new HashMap<String,Object>();
		calParaValues.put("companyId", SecurityContextHolderHelp.getCompanyId());
		queryObj.setParaValues(calParaValues);
		queryObj.setConditions(" WHERE companyId=:companyId ");
		queryObj.setHqlObjName(" Group");
		List<HtmlListQueryObj> list = new ArrayList<HtmlListQueryObj>();
		list.add(queryObj);
		return  userManagerService.getListData(list, false).get(0);
	}
	
	
	public void choiseGroups(BusiRequestEvent req){
		List<ListObject> pageList =  getListObjs();
		StringBuffer sb = new StringBuffer("");
		int i =0 ;
		if(pageList != null && pageList.size()>0){
			sb.append("{rows: [");
			for(ListObject listObject : pageList){
				i++ ;
				if(i != pageList.size()){
					listObject.toJson(sb);
					sb.append(",");
				}else{
					listObject.toJson(sb);
				}
			}
			sb.append("]}");				
		}
		/*this.writeResult(sb.toString());*/
		this.writeResult(JsonUtil.toJson(pageList));
	}
	
	public void groupSel1(BusiRequestEvent req){
//		this.writeResult(HtmlListComponent.toSelectStr(getListObjs()));
		List<ListObject> listObjectsAllList = new ArrayList<ListObject>();
		ListObject listObject = new ListObject();
		listObject.setKeyObj("");
		listObject.setValueObj("所有组");
		listObject.setRemark("所有组");
		listObjectsAllList.add(listObject);
		List<ListObject> listObjects = getListObjs();
		if(listObjects != null && listObjects.size() > 0){
			listObjectsAllList.addAll(listObjects);
		}
		this.writeResult(JsonUtil.toJson(listObjectsAllList));
		/*return getView("ajaxRest");*/
	}
	
	public void groupSel(BusiRequestEvent req){
//		this.writeResult(HtmlListComponent.toSelectStr(getListObjs()));
		List<ListObject> listObjectsAllList = new ArrayList<ListObject>();
		List<ListObject> listObjects = getListObjs();
		if(listObjects != null && listObjects.size() > 0){
			listObjectsAllList.addAll(listObjects);
		}
		this.writeResult(JsonUtil.toJson(listObjectsAllList));
		/*return getView("ajaxRest");*/
	}
	
	public View deleteUser(BusiRequestEvent req) {
		
		UserManagerDto dto = getDto(req);
		String ids = dto.getUser().getId(); 
		if(ids == null || "".equals(ids)){
			this.writeResult("failed");
			return getView("ajaxRest");
		}
		String delHql = "DELETE FROM User u WHERE u.id in (:userId)";
		List<Map<String, Object>> praValuesList = new ArrayList<Map<String, Object>>();
		String[] idsArray = ids.split(",");
		List<String> idsList = new ArrayList<String>();
		for(String id : idsArray){
			idsList.add(id);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", idsList);
		praValuesList.add(map);
		if(userManagerService.excuteBatchHql(delHql,praValuesList)){
			this.writeResult("success");
		}else{
			this.writeResult("failed");
		}
		dto = null;
		return getView("ajaxRest");
	}
	public View swUserStatus(BusiRequestEvent req){
		UserManagerDto dto = getDto(req);
//		String upHql = "update  User u set u.status=? WHERE u.id =? and u.companyId=?  and isAdmin=0";
//		Object[] values = new Object[]{dto.getUser().getStatus(),dto.getUser().getId(),SecurityContextHolderHelp.getCompanyId()};
//		userManagerService.executeUpdateByHql(upHql, values);
		userManagerService.updateUserStatus(dto.getUser().getId(), dto.getUser().getStatus().toString());
		super.writeResult("success");
		return getView("ajaxRest");
	}
	public View ldeleteUser(BusiRequestEvent req){
		
		UserManagerDto dto = getDto(req);
		String ids = dto.getUser().getId(); 
		if(ids == null || "".equals(ids)){
			this.writeResult("failed");
			dto = null;
			return getView("ajaxRest");
		}
		if(!this.canDelChk(ids.trim())){
			this.writeResult("haveBug");
			dto = null;
			return getView("ajaxRest");			
		}
		String hql = "select new User(id,name ,loginName) from cn.com.codes.object.User where id=?";
		User delUser = (User)userManagerService.findByHql(hql, ids.trim()).get(0);
		hql = " select new User(id,name ,loginName,delFlag) from cn.com.codes.object.User where loginName=:loginName and delFlag<>0 order by delFlag desc" ;
		Map<String, Object> praValuesMap = new HashMap<String, Object>(1);
		praValuesMap.put("loginName", delUser.getLoginName());
		Object userObj = userManagerService.getHibernateGenericController().findMaxMinByHqlWithValuesMap(hql, praValuesMap, false);
		praValuesMap = null;
		int delCount = 1;
		if(userObj!=null){
			delCount = delCount + ((User)userObj).getDelFlag();
		}
		userManagerService.ldeleteUser(dto.getUser().getId(),delCount);
		this.writeResult("success");
		dto = null;
		return getView("ajaxRest");
	}
	
	private boolean canDelChk(String userId){
		String hql = "from BugBaseInfo where nextOwnerId ='" +userId+"'";
		Long count = userManagerService.getHibernateGenericController().getResultCount(hql, null, "nextOwnerId");
		if(count>0){
			return false;
		}
		return true;
	}
	
	public View  setMyInfoInit(BusiRequestEvent req) {
//
//		UserManagerDto dto = getDto(req);
//		String myId = SecurityContextHolderHelp.getUserId();
//		User user = userManagerService.userUpdateInit(myId);
//		if(user==null){
//			throw new DataBaseException("查询数据出错或用户己被删除");
//		}
//		dto.setUser(user);
//		List<Group> groupList = user.getGroupList();
//		int i = 0 ;
//		for(Group group : groupList){
//			if(i == 0){
//				user.setGroupIds(group.getId());
//				user.setGroupNames(group.getName());
//			}else{
//				user.setGroupIds(user.getGroupIds()+"," +group.getId());
//				user.setGroupNames(user.getGroupNames()+"," +group.getName());
//			}
//			i++;
//		}
//		user.setPassword("4567KK6slygjk");
//		user.setoldPwd("4567KK6slygjk");
//		dto.setInVoldPwd("4567KK6slygjk");
//		dto.setVpassword("4567KK6slygjk");
		return super.getView();
	}	
	
	public View  setMyInfoInitLoad(BusiRequestEvent req) {

		UserManagerDto dto = getDto(req);
		String myId = SecurityContextHolderHelp.getUserId();
		User user = userManagerService.userUpdateInit(myId);
		if(user==null){
			throw new DataBaseException("查询数据出错或用户己被删除");
		}
		dto.setUser(user);
		List<Group> groupList = user.getGroupList();
		int i = 0 ;
		for(Group group : groupList){
			if(i == 0){
				user.setGroupIds(group.getId());
				user.setGroupNames(group.getName());
			}else{
				user.setGroupIds(user.getGroupIds()+"," +group.getId());
				user.setGroupNames(user.getGroupNames()+"," +group.getName());
			}
			i++;
		}
		user.setPassword("4567KK6slygjk");
		user.setoldPwd("4567KK6slygjk");
		dto.setInVoldPwd("4567KK6slygjk");
		dto.setVpassword("4567KK6slygjk");
		 
		user.setRoleList(null);
		user.setDevMemb(null);
		user.setTaskUseActors(null);
		user.setGroupList(null);
//		return super.getView();
//		super.writeResult(JsonUtil.toJson(dto));
		super.writeResult(super.addJsonPre("dto.user",user));
		return super.globalAjax();
		 
	}	
	
	public View updateMyInfo(BusiRequestEvent req){
		UserManagerDto dto = getDto(req);
		String myId = SecurityContextHolderHelp.getUserId();
		//取用户管理员标记,这个数据不带到页面上,防止别人篡改 
		User user = userManagerService.userUpdateInit(myId);
		dto.getUser().setIsAdmin(user.getIsAdmin());
		return this.userMaintence(req);
	}

	public void userInitData(BusiRequestEvent req) {

		UserManagerDto dto = getDto(req);
		User user = userManagerService.userUpdateInit(dto.getUser().getId());
		if(user==null){
			this.writeResult("failed");
			return;
		}
		//user.setChgPwdFlg(user.getId());
		dto.setUser(user);
		List<Group> groupList = user.getGroupList();
		int i = 0 ;
		for(Group group : groupList){
			if(i == 0){
				user.setGroupIds(group.getId());
				user.setGroupNames(group.getName());
			}else{
				user.setGroupIds(user.getGroupIds()+"," +group.getId());
				user.setGroupNames(user.getGroupNames()+"," +group.getName());
			}
			i++;
		}
		user.setGroupList(null);
		user.setRoleList(null);
		user.setDevMemb(null);
		user.setTaskUseActors(null);
		user.setPassword("4567KK6slygjk");
		user.setoldPwd("4567KK6slygjk");
		dto.setInVoldPwd("4567KK6slygjk");
		dto.setVpassword("4567KK6slygjk");
//		super.writeResult(JsonUtil.toJson(user));
		super.writeResult(super.addJsonPre("dto.user",user));
		
		
	}

	public void chagUsrStatus(BusiRequestEvent req){
		
		
	}

	public View updateAdminInit(BusiRequestEvent req) {

		UserManagerDto dto = getDto(req);
		Company comp = userManagerService.get(Company.class, dto.getCompany().getId());
		User user = new User();
		dto.setUser(user);
		dto.getUser().setLoginName(comp.getLoginName());
		this.writeResult(comp.toStrUpdateInit());
		return getView("ajaxRest");
		
	}

	public View deleteUserFromGroup(BusiRequestEvent req){
		UserManagerDto dto = super.getDto(UserManagerDto.class,req);
		Group group = dto.getGroup();
		userManagerService.deleteUserFromGroup(group.getId(), dto.getUserIds());
		return getView("groupUserList");
		
	}
	
	public UserManagerDto getDto(BusiRequestEvent req) {

		return (UserManagerDto) req.getDto();
	}

	public void deleteComp(BusiRequestEvent req){
		UserManagerDto dto = getDto(req);
		String ids = dto.getCompany().getId(); 
		if(ids == null || "".equals(ids)){
			this.writeResult("failed");
			return ;
		}
		String delHql = "update  Company  c  set c.delFlag = 1 where c.id in(:compIds)";
		List<Map<String, Object>> praValuesList = new ArrayList<Map<String, Object>>();
		String[] idsArray = ids.split(",");
		List<String> idList = new ArrayList<String>();
		for(String id : idsArray){
			idList.add(id);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("compIds", idList);
		praValuesList.add(map);
		if(userManagerService.excuteBatchHql(delHql,praValuesList)){
			this.writeResult("success");
		}else{
			this.writeResult("failed");
		}
	}

	public View login(BusiRequestEvent req){
		UserManagerDto dto = getDto(req);
		User user = dto.getUser();
		String loginName01 = user.getLoginName().trim();
		user.setLoginName(loginName01);
		if(user==null){
			super.writeResult("企图非法登录，系统不受理");
			return getView("ajaxRest");
		}
//		SecurityContext sec  = SecurityContextHolder.getContext();
//		String viewCode = sec.getViewCode();
//		if("true".equals(validCodeChk) && viewCode==null){
//			this.writeResult("overdue");
//			return super.globalAjax();
//		}
//		if("true".equals(validCodeChk)&&!viewCode.equals(dto.getViewCode())){
//			super.writeResult("viewCdError");
//			return getView("ajaxRest");
//		}
		boolean result = false;
		if(conf.getProperty("itest.accIpLimit")==null||"true".equals(conf.getProperty("itest.accIpLimit"))){
			result = securityPrivilege.loginWithIpChk(user.getLoginName(), user.getPassword());
		}else{
			result = securityPrivilege.login(user.getLoginName(), user.getPassword());
		}
		if(result){
			//StringBuffer sb  = new StringBuffer();
			//sb.append("loginMYPMsuccess^").append(adjustHomeUrl());
			Map resultMap  = new HashMap();
			resultMap.put("loginItest", "success");
			//resultMap.put("HomeUrl", adjustHomeUrl());
			//resultMap.put("HomeUrl", "itest/jsp/main.jsp");
			resultMap.put("HomeUrl", "");
			//this.writeResult(sb.toString());
			this.writeResult(JsonUtil.toJson(resultMap));
			//sec.eraseViewCode();
			return super.globalAjax();
		}else {
			Map resultMap  = new HashMap();
			resultMap.put("loginItest", "failed");
			this.writeResult(JsonUtil.toJson(resultMap));
			return super.globalAjax();
		}
		//return getView("login");
	}
	
	public View getUserUrls(BusiRequestEvent req){
		UserManagerDto dto = getDto(req);
		SecurityContext sc = SecurityContextHolder.getContext();
		Visit visit = sc.getVisit();
		VisitUser user =  null;
		if(visit!=null){
			user = visit.getUserInfo(VisitUser.class);
		}
		Set<String> userPrivileges   =  user.getPrivilege();
		if(userPrivileges!=null&&!userPrivileges.isEmpty()){
			this.writeResult(JsonUtil.toJson(userPrivileges));
		}else{
			this.writeResult(JsonUtil.toJson(new HashSet<String>()));
		}
		
		//sec.eraseViewCode();
		return super.globalAjax();
	}
	
	public View loginWithProAduit(BusiRequestEvent req){
		UserManagerDto dto = getDto(req);
		if(SecurityContextHolder.getContext().getVisit()!=null){
			return getView();
		}
		return getView("autoLogin");
	}
	
	public View loginWithProcessAduit(BusiRequestEvent req){
		UserManagerDto dto = getDto(req);
		if(SecurityContextHolder.getContext().getVisit()!=null){
			return getView();
		}
		return getView("autoLogin");
	}
	
	public View loginWithBug(BusiRequestEvent req){
		UserManagerDto dto = getDto(req);
		if(SecurityContextHolder.getContext().getVisit()!=null){
			if(dto.getAttr("mailBugId")!=null&&!"".equals(dto.getAttr("mailBugId").toString())&&dto.getAttr("mailBugId").toString().length()==32){
				return getView("loginWithBug");
			}
			return getView();
		}
		return getView("autoLogin");
	}
	
	public View autoLogin(BusiRequestEvent req){
		UserManagerDto dto = getDto(req);
		if(SecurityContextHolder.getContext().getVisit()!=null&&dto.getAttr("mailBugId")!=null&&!"".equals(dto.getAttr("mailBugId").toString())){
			if(dto.getAttr("mailBugId").toString().length()==32){
				return getView("loginWithBatchBug");
			}
			if(!"true".equals(dto.getIsAjax())){
				return getView("loginWithBug");
			}
		}
		User user = dto.getUser();
		if(user==null){
			super.writeResult("企图非法登录，系统不受理");
			return getView("ajaxRest");
		}
		boolean result = false;
		if(conf.getProperty("mypm.accIpLimit")==null||"true".equals(conf.getProperty("mypm.accIpLimit"))){
			result = securityPrivilege.loginWithIpChk(user.getLoginName(), user.getPassword());
		}else{
			result = securityPrivilege.login(user.getLoginName(), user.getPassword());
		}
		if(result){
			StringBuffer sb  = new StringBuffer();
			SecurityContext sec = SecurityContextHolder.getContext();
			if(dto.getAttr("mailBugId")!=null&&!"".equals(dto.getAttr("mailBugId").toString())){
				try{
					String tempMyHome = null;
					if(dto.getAttr("mailBugId").toString().length()==32){//从批量分配BUG通知中登录过来的
						tempMyHome ="/bugManager/bugManagerAction!loadMyBug.action?dto.taskId="+dto.getAttr("mailBugId");
					}else{
						tempMyHome ="/bugManager/bugManagerAction!jump2HandlingBug.action?dto.bug.bugId="+Integer.parseInt(dto.getAttr("mailBugId").toString());
					}
					dto.clearDataContainer();
					sec.setSessionAttr("myHome", tempMyHome);
				}catch(Exception e){};
			}
			sb.append("loginMYPMsuccess^").append(adjustHomeUrl());
			this.writeResult(sb.toString());
			return super.globalAjax();
		}
		return getView("autoLogin");
	}
	private String adjustHomeUrl(){
		SecurityContext sec  = SecurityContextHolder.getContext();
		String myHomeUrl = sec.getSessionAttr("myHome").toString();
		if("".equals(myHomeUrl)){
			return "/bugManager/bugManagerAction!loadAllMyBug.action";
		}
		if(myHomeUrl.length()<=3){
			return myHomeUrl;
		}
		String urlTemp = myHomeUrl;
		try {
			urlTemp = urlTemp.substring(urlTemp.lastIndexOf("/")+1);
			urlTemp = urlTemp.substring(0,urlTemp.indexOf("."));
		} catch (RuntimeException e) {
			return "/bugManager/bugManagerAction!loadAllMyBug.action";
		}
		if(UrlSecurityCheckInterceptor.urlSecurityCheck(urlTemp))
			return myHomeUrl;
		return "/bugManager/bugManagerAction!loadAllMyBug.action";
	}
	public static void main(String[] arg){
		String ss = "ssss/ffd/bugManagerAction!loadMyBug.action" ;
		ss = ss.substring(ss.lastIndexOf("/")+1);
		ss = ss.substring(0,ss.indexOf("."));
		System.out.println(ss);
	}
	public View loadMyHome(BusiRequestEvent req){
		
		return super.globalAjax();
	}
	

	public View selectUser(BusiRequestEvent req){
		UserManagerDto dto = super.getDto(UserManagerDto.class, req);
		String groupId = null;
		if(dto.getGroup()!=null){
			groupId = dto.getGroup().getId();
		}
		List<Object[]> memberList = new ArrayList<Object[]>();
		String compId = SecurityContextHolderHelp.getCompanyId();
		if(groupId!= null &&!"".equals(groupId)&&!"-1".equals(groupId)){
			StringBuffer sb = new StringBuffer();
			sb.append("select  u.id as userId, (u.loginName||'('||u.name||')') as userName ");
			sb.append("  from User u join u.groupList g ");
			sb.append("  where g.id=? and u.companyId=? and u.delFlag=0 and u.status=1");
			if(dto.getUserName()!=null&&!"".equals(dto.getUserName().trim())){
				sb.append( "and (u.name like ? or u.loginName like ?) order by u.loginName");
				memberList = (List<Object[]>)userManagerService.findByHql(sb.toString(),groupId,compId,"%"+dto.getUserName()+"%","%"+dto.getUserName()+"%");
			}else{
				sb.append( " order by u.loginName");
				memberList = (List<Object[]>)userManagerService.findByHql(sb.toString(),groupId,compId);
			}
		}else{
			if(dto.getUserName()!=null&&!"".equals(dto.getUserName().trim())){
				String hql = "select u.id,u.loginName||'('||u.name||')'  from User u where u.companyId=? and (u.name like ? or u.loginName like ?) and u.delFlag=0 and u.status=1 order by u.loginName";
				memberList = (List<Object[]>)userManagerService.findByHql(hql,compId,"%"+dto.getUserName()+"%","%"+dto.getUserName()+"%");
			}else{
				String hql = "select id,loginName||'('||name||')'  from User where companyId=? and delFlag=0 and status=1 order by loginName";
				
				memberList = (List<Object[]>)userManagerService.findByHql(hql,compId);
			}
		}
		List<JsonInterface> memberes = new ArrayList<JsonInterface>();
		for(Object[] objs:memberList){
			ListObject lb = new ListObject(objs[0].toString(),objs[1].toString());
			memberes.add(lb);
		}
		PageModel pg = new PageModel();
		Integer total =  memberes.size();
//		Integer total =  (Integer) SecurityContextHolder.getContext().getAttr("pageInfoTotalRows");
		pg.setRows(memberes);
		pg.setTotal(total);
		memberes = null;
		super.writeResult(JsonUtil.toJson(pg));
		return super.globalAjax();
		
		
//		super.writeResult(super.listToJson(memberes));
//		memberes = null;
//		return super.getView("ajaxRest");
		
	}
	
	public View selectUserInAll(BusiRequestEvent req){
		UserManagerDto dto = super.getDto(UserManagerDto.class, req);
		String groupId = null;
		if(dto.getGroup()!=null){
			groupId = dto.getGroup().getId();
		}
		List<Object[]> memberList = new ArrayList<Object[]>();
		String compId = SecurityContextHolderHelp.getCompanyId();
		if(groupId!= null &&!"".equals(groupId)&&!"-1".equals(groupId)){
			StringBuffer sb = new StringBuffer();
			sb.append("select  u.id as userId, (u.loginName||'('||u.name||')') as userName ");
			sb.append("  from User u join u.groupList g ");
			sb.append("  where g.id=? and u.companyId=? and u.delFlag=0 ");
			if(dto.getUserName()!=null&&!"".equals(dto.getUserName().trim())){
				sb.append( "and (u.name like ? or u.loginName like ?) order by u.loginName");
				memberList = (List<Object[]>)userManagerService.findByHql(sb.toString(),groupId,compId,"%"+dto.getUserName()+"%","%"+dto.getUserName()+"%");
			}else{
				sb.append( " order by u.loginName");
				memberList = (List<Object[]>)userManagerService.findByHql(sb.toString(),groupId,compId);
			}
		}else{
			if(dto.getUserName()!=null&&!"".equals(dto.getUserName().trim())){
				String hql = "select u.id,u.loginName||'('||u.name||')'  from User u where u.companyId=? and (u.name like ? or u.loginName like ?) and u.delFlag=0 and u.status=1 order by u.loginName";
				memberList = (List<Object[]>)userManagerService.findByHql(hql,compId,"%"+dto.getUserName()+"%","%"+dto.getUserName()+"%");
			}else{
				String hql = "select id,loginName||'('||name||')'  from User where companyId=? and delFlag=0 and status=1 order by loginName";
				
				memberList = (List<Object[]>)userManagerService.findByHql(hql,compId);
			}
		}
		List<JsonInterface> memberes = new ArrayList<JsonInterface>();
		for(Object[] objs:memberList){
			ListObject lb = new ListObject(objs[0].toString(),objs[1].toString());
			memberes.add(lb);
		}
//		super.writeResult(super.listToJson(memberes));
		super.writeResult(JsonUtil.toJson(memberes));
		memberes = null;
		return super.getView("ajaxRest");
		
	}
	
	public View loadDefaultSelUser(BusiRequestEvent req){
		UserManagerDto dto = super.getDto(UserManagerDto.class, req);
		List<Object[]> memberList = new ArrayList<Object[]>();
		String compId = SecurityContextHolderHelp.getCompanyId();
		String hql = "select id,loginName||'('||name||')'  from User where companyId=? and delFlag=0 and status=1 order by loginName";
		memberList = (List<Object[]>) userManagerService.findByHqlPage(hql,dto.getPageNo(), dto.getPageSize(), "id", compId);
		
		List<JsonInterface> memberes = new ArrayList<JsonInterface>();
		for(Object[] objs:memberList){
			ListObject lb = new ListObject(objs[0].toString(),objs[1].toString());
			memberes.add(lb);
		}
		
		PageModel pageModel = new PageModel(); 
		pageModel.setRows(memberes);
		Integer total =  (Integer) SecurityContextHolder.getContext().getAttr("pageInfoTotalRows");
		pageModel.setTotal(total);
		super.writeResult(JsonUtil.toJson(pageModel));
//		super.writeResult(super.listToJson(memberes));
		memberes = null;
		return super.getView("ajaxRest");
		
	}
	
	public View loadDefaultSelUserInAll(BusiRequestEvent req){
		UserManagerDto dto = super.getDto(UserManagerDto.class,req);
		List<Object[]> memberList = new ArrayList<Object[]>();
		String compId = SecurityContextHolderHelp.getCompanyId();
		String hql = "select id,loginName||'('||name||')'  from User where companyId=? and delFlag=0  order by loginName";
		memberList = (List<Object[]>) userManagerService.findByHqlPage(hql, dto.getPageNo(),  dto.getPageSize(), "id", compId);
        List<JsonInterface> memberes = new ArrayList<JsonInterface>();
		for(Object[] objs:memberList){
			ListObject lb = new ListObject(objs[0].toString(),objs[1].toString());
			memberes.add(lb);
		}
		PageModel pg = new PageModel();
		Integer total =  (Integer) SecurityContextHolder.getContext().getAttr("pageInfoTotalRows");
		pg.setRows(memberes);
		pg.setTotal(total);
		super.writeResult(JsonUtil.toJson(pg));
		memberes = null;
		return super.globalAjax();
 		
//		super.writeResult(super.listToJson(memberes));
//		memberes = null;
//		return super.getView("ajaxRest"); 
		
	}
	public View importUsers(BusiRequestEvent req) throws BaseException {
		UserManagerDto dto = getDto(req);
		FileInputStream inputStream = null;
		POIFSFileSystem fs = null;
		String customMessage = "";
		try {
			inputStream = new FileInputStream(dto.getImportUser());
			fs = new POIFSFileSystem(inputStream);
			ParseXmlTool test = new ParseXmlTool();
			XmlEntity xmlEntity = test.parseXml("mypmUser.xml");
			ValidateExcel vExcel = new ValidateExcel(fs, xmlEntity);
			customMessage = vExcel.validateExcel();
			if (customMessage.equals("")) {
				List list = vExcel.getEntityList();
				if(Utilities.isNullOrEmpty(list)){
					customMessage = "excel中没有数据";
				}else{
					String repeatName = validateRepeat(list);
					if (!repeatName.equalsIgnoreCase("")) {
						customMessage = "excel中下列登录帐号重复:<br>" + repeatName;
					}else{
						dto.setObjectList(list);
						dto.setHql("SELECT u.loginName FROM User u where u.delFlag=0 ");
						synchronized(this){
							userManagerService.importUsers(dto);
							customMessage = dto.getCustomMessage();
						}
					}
				}
			}
		} catch (IOException e) {
			customMessage = "读取导入文件发生错误";
		}finally{
			try {
				if(inputStream!=null){
					inputStream.close();
				}
			} catch (Exception e1) {
				customMessage = "读取导入文件发生错误";
			}
		}
		dto.setCustomMessage(customMessage);
		this.buildFindUserQuery(dto);
		List userList = userManagerService.findByHqlWithValuesMap(dto.getHql(), dto.getPageNo(), dto.getPageSize(), "u.id", dto.getHqlParamMaps(), false);
		dto.setJsonData(userList);
		//return getView("userList");
		super.writeResult(customMessage);
		return super.globalAjax();
	}
	
	// 校验是否有重复的用户名
	@SuppressWarnings("unchecked")
	private String validateRepeat(final List entityList) {
		StringBuffer repeatName = new StringBuffer();
		 for (int i = 0; i < entityList.size(); i++) {
			User impUser = (User) entityList.get(i);
			String oneStr = impUser.getLoginName();
			if (oneStr == null || oneStr.equalsIgnoreCase("")) {
				continue;
			}
			for (int j = i + 1; j < entityList.size(); j++) {
				User two = (User) entityList.get(j);
				String twoStr = two.getLoginName();
				if (oneStr != null && twoStr != null) {
					if (oneStr.equalsIgnoreCase(twoStr)) {
						if(repeatName.indexOf(oneStr)<0){
							repeatName.append("<br>").append(oneStr);
						}
					}
				}
			}
		}
//		 if(repeatName.indexOf("<br>")>0){
//			 return repeatName.substring(1).toString();
//		 }
		return repeatName.toString();
	}
	
	private UserManagerService getUserManagerService() {
		return userManagerService;
	}

	public void setUserManagerService(UserManagerService userManagerService) {
		this.userManagerService = userManagerService;
	}

	public SecurityPrivilege getSecurityPrivilege() {
		return securityPrivilege;
	}

	public void setSecurityPrivilege(SecurityPrivilege securityPrivilege) {
		this.securityPrivilege = securityPrivilege;
	}
	
	public View groupAll(BusiRequestEvent req){
		this.writeResult(JsonUtil.toJson(getListObjs()));
		return super.globalAjax();
	}
	
	public View userGroupMemberList(BusiRequestEvent req) throws BaseException {

		return super.getView();
	}
}
