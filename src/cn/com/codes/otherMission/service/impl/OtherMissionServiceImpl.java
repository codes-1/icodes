package cn.com.codes.otherMission.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.mapping.Array;

import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.app.services.BaseServiceImpl;
import cn.com.codes.object.ConcernOtherMission;
import cn.com.codes.object.MissionLog;
import cn.com.codes.object.OtherMission;
import cn.com.codes.object.Project;
import cn.com.codes.object.SingleTestTask;
import cn.com.codes.object.User;
import cn.com.codes.object.UserOtherMission;
import cn.com.codes.otherMission.dto.OtherMissionDto;
import cn.com.codes.otherMission.service.OtherMissionService;

public class OtherMissionServiceImpl extends BaseServiceImpl implements OtherMissionService {

	public String addOtherMission(OtherMissionDto otherMissionDto){
		//判断任务名称是否已存在
		String hql = "from OtherMission om where om.missionName=?";
		@SuppressWarnings("unchecked")
		List<OtherMission> otherMissions = this.findByHql(hql, otherMissionDto.getOtherMission().getMissionName());
		if(otherMissions != null && otherMissions.size() > 0){
			return "existed";
		}
		//保存t_other_mission表
		otherMissionDto.getOtherMission().setCreateTime(new Date());
		this.add(otherMissionDto.getOtherMission());
		//保存t_user_other_mission表
		if(!StringUtils.isNullOrEmpty(otherMissionDto.getUserIds())){
			String[] userIds = otherMissionDto.getUserIds().split(",");
			List<UserOtherMission> userOtherMissions = new ArrayList<UserOtherMission>();
			for(int i=0;i<userIds.length;i++){
				UserOtherMission userOtherMission = new UserOtherMission();
				userOtherMission.setMissionId(otherMissionDto.getOtherMission().getMissionId());
				userOtherMission.setUserId(userIds[i]);
				userOtherMissions.add(userOtherMission);
			}
			this.batchSaveOrUpdate(userOtherMissions);
		}
		//保存t_concern_other_mission表
		if(!StringUtils.isNullOrEmpty(otherMissionDto.getConcernIds())){
			String[] concernIds = otherMissionDto.getConcernIds().split(",");
			List<ConcernOtherMission> concernOtherMissions = new ArrayList<ConcernOtherMission>();
			for(int i=0;i<concernIds.length;i++){
				ConcernOtherMission concernOtherMission = new ConcernOtherMission();
				concernOtherMission.setMissionId(otherMissionDto.getOtherMission().getMissionId());
				concernOtherMission.setUserId(concernIds[i]);
				concernOtherMissions.add(concernOtherMission);
			}
			this.batchSaveOrUpdate(concernOtherMissions);
		}
		//保存t_mission_log表
		MissionLog missionLog = new MissionLog();
		missionLog.setMissionId(otherMissionDto.getOtherMission().getMissionId());
		missionLog.setOperateType("1");
		missionLog.setOperateDetail("创建任务并分配");
		missionLog.setOperatePerson(otherMissionDto.getOtherMission().getCreateUserId());
		missionLog.setOperateTime(new Date());
		this.add(missionLog);
		return "success";
	}
	
	public String updateOtherMission(OtherMissionDto otherMissionDto){
		//判断任务名称是否已存在
		String hql = "from OtherMission om where om.missionName=?";
		@SuppressWarnings("unchecked")
		List<OtherMission> otherMissions = this.findByHql(hql, otherMissionDto.getOtherMission().getMissionName());
		if(otherMissions != null && otherMissions.size() == 1 && !otherMissions.get(0).getMissionId().equals(otherMissionDto.getOtherMission().getMissionId())){
			return "existed";
		}
		if(otherMissions != null && otherMissions.size() == 1 && otherMissions.get(0).getMissionId().equals(otherMissionDto.getOtherMission().getMissionId())){
			//更新t_other_mission表
			otherMissions.get(0).setDescription(otherMissionDto.getOtherMission().getDescription());
			otherMissions.get(0).setChargePersonId(otherMissionDto.getOtherMission().getChargePersonId());
			otherMissions.get(0).setMissionCategory(otherMissionDto.getOtherMission().getMissionCategory());
			otherMissions.get(0).setProjectId(otherMissionDto.getOtherMission().getProjectId());
			otherMissions.get(0).setProjectType(otherMissionDto.getOtherMission().getProjectType());
			otherMissions.get(0).setEmergencyDegree(otherMissionDto.getOtherMission().getEmergencyDegree());
			otherMissions.get(0).setDifficultyDegree(otherMissionDto.getOtherMission().getDifficultyDegree());
			otherMissions.get(0).setPredictStartTime(otherMissionDto.getOtherMission().getPredictStartTime());
			otherMissions.get(0).setPredictEndTime(otherMissionDto.getOtherMission().getPredictEndTime());
			otherMissions.get(0).setStandardWorkload(otherMissionDto.getOtherMission().getStandardWorkload());
			otherMissions.get(0).setUpdateTime(new Date());
			this.update(otherMissions.get(0));
		}else{
			//更新t_other_mission表
			otherMissionDto.getOtherMission().setUpdateTime(new Date());
			this.update(otherMissionDto.getOtherMission());
		}
		
		//更新t_user_other_mission表
		//先删除t_user_other_mission原有记录
		if(!StringUtils.isNullOrEmpty(otherMissionDto.getOtherMission().getMissionId())){
			this.executeUpdateByHql("delete from UserOtherMission where missionId =? ", new Object[]{otherMissionDto.getOtherMission().getMissionId()});
		}
		//保存t_user_other_mission新纪录
		if(!StringUtils.isNullOrEmpty(otherMissionDto.getUserIds())){
			String[] userIds = otherMissionDto.getUserIds().split(",");
			List<UserOtherMission> userOtherMissions = new ArrayList<UserOtherMission>();
			for(int i=0;i<userIds.length;i++){
				UserOtherMission userOtherMission = new UserOtherMission();
				userOtherMission.setMissionId(otherMissionDto.getOtherMission().getMissionId());
				userOtherMission.setUserId(userIds[i]);
				userOtherMissions.add(userOtherMission);
			}
			this.batchSaveOrUpdate(userOtherMissions);
		}
		
		//更新t_concern_other_mission表
		//先删除t_concern_other_mission原有记录
		if(!StringUtils.isNullOrEmpty(otherMissionDto.getOtherMission().getMissionId())){
			this.executeUpdateByHql("delete from ConcernOtherMission where missionId =? ", new Object[]{otherMissionDto.getOtherMission().getMissionId()});
		}
		//保存t_concern_other_mission新纪录
		if(!StringUtils.isNullOrEmpty(otherMissionDto.getConcernIds())){
			String[] concernIds = otherMissionDto.getConcernIds().split(",");
			List<ConcernOtherMission> concernOtherMissions = new ArrayList<ConcernOtherMission>();
			for(int i=0;i<concernIds.length;i++){
				ConcernOtherMission concernOtherMission = new ConcernOtherMission();
				concernOtherMission.setMissionId(otherMissionDto.getOtherMission().getMissionId());
				concernOtherMission.setUserId(concernIds[i]);
				concernOtherMissions.add(concernOtherMission);
			}
			this.batchSaveOrUpdate(concernOtherMissions);
		}
		return "success";
	}
	
	public void deleteOtherMission(OtherMissionDto otherMissionDto){
		/*OtherMission otherMission2 = this.get(OtherMission.class, otherMissionDto.getOtherMission().getMissionId());*/
		//删除t_other_mission表
		/*if(otherMission2 != null){*/
		this.executeUpdateByHql("delete from OtherMission where missionId =? ", new Object[]{otherMissionDto.getOtherMission().getMissionId()});
		/*}*/
		//删除t_user_other_mission表
		this.executeUpdateByHql("delete from UserOtherMission where missionId =? ", new Object[]{otherMissionDto.getOtherMission().getMissionId()});
		//删除t_concern_other_mission表
		this.executeUpdateByHql("delete from ConcernOtherMission where missionId =? ", new Object[]{otherMissionDto.getOtherMission().getMissionId()});
		//删除t_mission_log表
		this.executeUpdateByHql("delete from MissionLog where missionId =? ", new Object[]{otherMissionDto.getOtherMission().getMissionId()});
		//删除t_iteration_task_real表
		this.executeUpdateByHql("delete from IterationTaskReal where missionId =? ", new Object[]{otherMissionDto.getOtherMission().getMissionId()});
	}
	
	public String[] getUsers(OtherMissionDto otherMissionDto){
		String hql = "from UserOtherMission uom where uom.missionId=?";
		@SuppressWarnings("unchecked")
		List<UserOtherMission> userOtherMissions = this.findByHql(hql, otherMissionDto.getOtherMission().getMissionId());
		if(userOtherMissions != null && userOtherMissions.size() > 0){
			String[] userIds = new String[userOtherMissions.size()];
			for(int i=0;i<userOtherMissions.size();i++){
				userIds[i] = userOtherMissions.get(i).getUserId();
			}
			return userIds;
		}else{
			return null;
		}
	}
	
	public String[] getConcerns(OtherMissionDto otherMissionDto){
		String hql = "from ConcernOtherMission com where com.missionId=?";
		@SuppressWarnings("unchecked")
		List<ConcernOtherMission> concernOtherMissions = this.findByHql(hql, otherMissionDto.getOtherMission().getMissionId());
		if(concernOtherMissions != null && concernOtherMissions.size() > 0){
			String[] userIds = new String[concernOtherMissions.size()];
			for(int i=0;i<concernOtherMissions.size();i++){
				userIds[i] = concernOtherMissions.get(i).getUserId();
			}
			return userIds;
		}else{
			return null;
		}
	}
	
	public Boolean addProject(OtherMissionDto otherMissionDto){
		//判断项目是否已存在
		String hql = "from Project p where p.projectName=?";
		@SuppressWarnings("unchecked")
		List<Project> projects = this.findByHql(hql, otherMissionDto.getProject().getProjectName());
		if(projects.size() > 0){
			return false;
		}else{
			//保存t_project表
			this.add(otherMissionDto.getProject());
			return true;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Project> getProjectLists(OtherMissionDto otherMissionDto){
		String hql = "from Project p where 1=1 and p.projectId is not null";
		List<Project> projects = this.findByHql(hql);
		String hql1 = "select new cn.com.codes.object.SingleTestTask(stt.taskId,stt.proName,stt.proNum,stt.taskProjectId) from SingleTestTask stt where 1=1 and stt.taskId is not null and stt.filterFlag != '1'";
		List<SingleTestTask> singleTestTasks = this.findByHql(hql1);
		if(projects==null) {
			projects = new ArrayList<Project>();
		}
		if(singleTestTasks != null && singleTestTasks.size() > 0){
			for(int i=0;i<singleTestTasks.size();i++){
				Project project = new Project();
				project.setProjectId(singleTestTasks.get(i).getTaskId());
				project.setProjectName(singleTestTasks.get(i).getProName());
				project.setProjectType("0");
				projects.add(project);
			}
			//return projects;
		}
		return projects;
	}
	
	@SuppressWarnings("unchecked")
	public List<Project> getProjectLists2(OtherMissionDto otherMissionDto){
		List<Project> projects = new ArrayList<Project>();
		String hql1 = "select new cn.com.codes.object.SingleTestTask(stt.taskId,stt.proName,stt.proNum,stt.taskProjectId) from SingleTestTask stt where 1=1 and stt.taskId is not null and stt.filterFlag = '1'";
		List<SingleTestTask> singleTestTasks = this.findByHql(hql1);
		List<String> taskIds = new ArrayList<String>();
		if(singleTestTasks != null && singleTestTasks.size() > 0){
			for(int i=0;i<singleTestTasks.size();i++){
				taskIds.add(singleTestTasks.get(i).getTaskProjectId());
			}
		}
		String hql = "from Project p where 1=1 and p.projectId is not null";
		List<Project> projects1 = this.findByHql(hql);
		if(projects1 != null && projects1.size() > 0){
			if(taskIds.size() > 0){
				for(int i=0;i<projects1.size();i++){
					int uuu = 0;
					for(int p=0;p<taskIds.size();p++){
						if(projects1.get(i).getProjectId().equals(taskIds.get(p))){
							break;
						}else{
							uuu = uuu + 1;
						}
					}
					if(uuu == taskIds.size()){
						projects.add(projects1.get(i));
					}
				}
			}else{
				return projects1;
			}
			if(!StringUtils.isNullOrEmpty(otherMissionDto.getProjectId())){
				for(int ll=0;ll<projects1.size();ll++){
					if(otherMissionDto.getProjectId().equals(projects1.get(ll).getProjectId())){
						projects.add(projects1.get(ll));
					}
				}
			}
			return projects;
		}
		return new ArrayList<Project>();
	}
	
	@SuppressWarnings("unchecked")
	public List<Project> getProjectLists1(OtherMissionDto otherMissionDto){
		String hql = "from Project p where 1=1 and p.projectId is not null";
		List<Project> projects = this.findByHql(hql);
		String hql1 = "select new cn.com.codes.object.SingleTestTask(stt.taskId,stt.proName,stt.proNum,stt.taskProjectId) from SingleTestTask stt where 1=1 and stt.taskId is not null";
		List<SingleTestTask> singleTestTasks = this.findByHql(hql1);
		List<Project> projects2 = new ArrayList<Project>();
		List<String> projectNameList = new ArrayList<String>();
		if(singleTestTasks != null && singleTestTasks.size() > 0){
			for(int i=0;i<singleTestTasks.size();i++){
				Project project = new Project();
				project.setProjectId(singleTestTasks.get(i).getTaskId());
				project.setProjectName(singleTestTasks.get(i).getProName());
				project.setProjectType("0");
				projects2.add(project);
				if(!StringUtils.isNullOrEmpty(singleTestTasks.get(i).getTaskProjectId())){
					projectNameList.add(singleTestTasks.get(i).getTaskProjectId());
				}
			}
		}
		if(projects != null && projects.size() > 0){
			if(projectNameList.size() > 0){
				for(int t=0;t<projects.size();t++){
					int yyy = 0;
					for(int y=0;y<projectNameList.size();y++){
						if(projects.get(t).getProjectId().equals(projectNameList.get(y))){
							break;
						}else{
							yyy = yyy + 1;
						}
					}
					if(projectNameList.size() == yyy){
						projects2.add(projects.get(t));
					}
				}
			}else{
				for(int t=0;t<projects.size();t++){
					projects2.add(projects.get(t));
				}
			}
			
		}
		/*if(projects != null && projects.size() > 0 && singleTestTasks != null && singleTestTasks.size() > 0){
			for(int i=0;i<singleTestTasks.size();i++){
				Project project = new Project();
				project.setProjectId(singleTestTasks.get(i).getTaskId());
				project.setProjectName(singleTestTasks.get(i).getProName());
				project.setProjectType("0");
				projects.add(project);
			}
			return projects;
		}*/
		return projects2;
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<User> getPeopleLists(OtherMissionDto otherMissionDto){
		String hql1 = "";
		List<User> users = new ArrayList<User>();
		if(!StringUtils.isNullOrEmpty(otherMissionDto.getFilterStr()) && StringUtils.isNullOrEmpty(otherMissionDto.getGroupId())){
			hql1 = "select new cn.com.codes.object.User(u.id,u.name) from User u where u.status =? and u.name like ?";
			users = this.findByHql(hql1, 1,"%"+otherMissionDto.getFilterStr()+"%");
		}else if(StringUtils.isNullOrEmpty(otherMissionDto.getFilterStr()) && StringUtils.isNullOrEmpty(otherMissionDto.getGroupId())){
			hql1 = "select new cn.com.codes.object.User(u.id,u.name) from User u where u.status =? ";
			users = this.findByHql(hql1, 1);
		}else if(!StringUtils.isNullOrEmpty(otherMissionDto.getFilterStr()) && !StringUtils.isNullOrEmpty(otherMissionDto.getGroupId())){
			hql1 = "select new cn.com.codes.object.User(u.id,u.name) from User u join u.groupList g where u.status =? and g.id=? and u.name like ?";
			users = this.findByHql(hql1, 1,otherMissionDto.getGroupId(),"%"+otherMissionDto.getFilterStr()+"%");
		}else if(StringUtils.isNullOrEmpty(otherMissionDto.getFilterStr()) && !StringUtils.isNullOrEmpty(otherMissionDto.getGroupId())){
			hql1 = "select new cn.com.codes.object.User(u.id,u.name) from User u join u.groupList g where u.status =? and g.id=?";
			users = this.findByHql(hql1, 1,otherMissionDto.getGroupId());
		}
		
		if(users != null && users.size() > 0){
			return users;
		}
		return null;
	}
	
	public void updateStatus(OtherMissionDto otherMissionDto){
		String hql = "from OtherMission om where om.missionId=?";
		@SuppressWarnings("unchecked")
		List<OtherMission> otherMissions = this.findByHql(hql, otherMissionDto.getOtherMission().getMissionId());
		if(otherMissions != null && otherMissions.size() > 0){
			String hql1 = "from User ue where ue.id=?";
			@SuppressWarnings("unchecked")
			List<User> users = this.findByHql(hql1, otherMissions.get(0).getChargePersonId());
			String userName = users.get(0).getLoginName();
			//保存t_mission_log表
			if(!otherMissions.get(0).getCompletionDegree().equals(otherMissionDto.getOtherMission().getCompletionDegree())){
				MissionLog missionLog = new MissionLog();
				missionLog.setMissionId(otherMissionDto.getOtherMission().getMissionId());
				missionLog.setOperateType("3");
				missionLog.setOperatePerson(userName);
				missionLog.setOperateDetail("填写进度为"+otherMissionDto.getOtherMission().getCompletionDegree());
				missionLog.setOperateTime(new Date());
				this.add(missionLog);
			}
			if(!otherMissions.get(0).getActualWorkload().equals(otherMissionDto.getOtherMission().getActualWorkload())){
				MissionLog missionLog = new MissionLog();
				missionLog.setMissionId(otherMissionDto.getOtherMission().getMissionId());
				missionLog.setOperateType("4");
				missionLog.setOperatePerson(userName);
				missionLog.setOperateDetail("修改实际工作量为"+otherMissionDto.getOtherMission().getActualWorkload()+"小时");
				missionLog.setOperateTime(new Date());
				this.add(missionLog);
			}
			if(!otherMissions.get(0).getStatus().equals(otherMissionDto.getOtherMission().getStatus())){
				MissionLog missionLog = new MissionLog();
				missionLog.setMissionId(otherMissionDto.getOtherMission().getMissionId());
				missionLog.setOperateType("2");
				missionLog.setOperatePerson(userName);
				String status = "";
				if(otherMissionDto.getOtherMission().getStatus().equals("0")){
					status = "分配";
				}else if(otherMissionDto.getOtherMission().getStatus().equals("1")){
					status = "进行中";
				}else if(otherMissionDto.getOtherMission().getStatus().equals("2")){
					status = "完成";
				}else if(otherMissionDto.getOtherMission().getStatus().equals("3")){
					status = "终止";
				}else if(otherMissionDto.getOtherMission().getStatus().equals("4")){
					status = "暂停";
				}
				missionLog.setOperateDetail("修改状态为"+status);
				missionLog.setOperateTime(new Date());
				this.add(missionLog);
			}
			//更新t_other_mission表
			otherMissions.get(0).setActualWorkload(otherMissionDto.getOtherMission().getActualWorkload());
			otherMissions.get(0).setCompletionDegree(otherMissionDto.getOtherMission().getCompletionDegree());
			otherMissions.get(0).setStatus(otherMissionDto.getOtherMission().getStatus());
			otherMissions.get(0).setStopReason(otherMissionDto.getOtherMission().getStopReason());
			this.update(otherMissions.get(0)); 
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String[] getUserNames(OtherMissionDto otherMissionDto){
		String hql = "from UserOtherMission uom where uom.missionId=?";
		List<UserOtherMission> userOtherMissions = this.findByHql(hql, otherMissionDto.getOtherMission().getMissionId());
		if(userOtherMissions != null && userOtherMissions.size() > 0){
			List<String> userIds = new ArrayList<String>();
			for(int i=0;i<userOtherMissions.size();i++){
				userIds.add(userOtherMissions.get(i).getUserId());
			}
			Map praValMap = new HashMap();
			String hql1 = "select new cn.com.codes.object.User(u.id,u.name) from User u where u.id in (:ids) ";
			praValMap.put("ids",userIds);
			otherMissionDto.setHql(hql1.toString());
			otherMissionDto.setHqlParamMaps(praValMap);
			otherMissionDto.setPageNo(1);
			otherMissionDto.setPageSize(300);
			List<User> users = this.findByHqlWithValuesMap(otherMissionDto);
			if(users != null && users.size() > 0){
				String[] userNames = new String[users.size()];
				for(int p=0;p<users.size();p++){
					userNames[p] = users.get(p).getName();
				}
				return userNames;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String[] getConcernNames(OtherMissionDto otherMissionDto){
		String hql = "from ConcernOtherMission com where com.missionId=?";
		List<ConcernOtherMission> concernOtherMissions = this.findByHql(hql, otherMissionDto.getOtherMission().getMissionId());
		if(concernOtherMissions != null && concernOtherMissions.size() > 0){
			List<String> userIds = new ArrayList<String>();
			for(int i=0;i<concernOtherMissions.size();i++){
				userIds.add(concernOtherMissions.get(i).getUserId());
			}
			Map praValMap = new HashMap();
			String hql1 = "select new cn.com.codes.object.User(u.id,u.name) from User u where u.id in (:ids) ";
			praValMap.put("ids",userIds);
			otherMissionDto.setHql(hql1.toString());
			otherMissionDto.setHqlParamMaps(praValMap);
			otherMissionDto.setPageNo(1);
			otherMissionDto.setPageSize(300);
			List<User> users = this.findByHqlWithValuesMap(otherMissionDto);
			if(users != null && users.size() > 0){
				String[] userNames = new String[users.size()];
				for(int p=0;p<users.size();p++){
					userNames[p] = users.get(p).getName();
				}
				return userNames;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void concernMissions(OtherMissionDto otherMissionDto){
		String[] missionIds = otherMissionDto.getMissionIds().split(",");
		List<ConcernOtherMission> concernOtherMissions = new ArrayList<ConcernOtherMission>();
		for(int i=0;i<missionIds.length;i++){
			//查询当前用户是否已经关注该项目
			String hql = "from ConcernOtherMission com where com.missionId=? and com.userId=?";
			List<ConcernOtherMission> concernOtherMissions1 = this.findByHql(hql, missionIds[i],otherMissionDto.getuId());
			if(concernOtherMissions1 != null && concernOtherMissions1.size() > 0){
				continue;
			}else{
				ConcernOtherMission concernOtherMission = new ConcernOtherMission();
				concernOtherMission.setMissionId(missionIds[i]);
				concernOtherMission.setUserId(otherMissionDto.getuId());
				concernOtherMissions.add(concernOtherMission);
			}
		}
		this.batchSaveOrUpdate(concernOtherMissions);
	}
}
