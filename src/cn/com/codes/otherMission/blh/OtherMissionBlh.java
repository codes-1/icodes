package cn.com.codes.otherMission.blh;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.dto.PageModel;
import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.common.ConvertObjArrayToVo;
import cn.com.codes.framework.common.JsonUtil;
import cn.com.codes.framework.security.VisitUser;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.transmission.JsonInterface;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.object.ConcernOtherMission;
import cn.com.codes.object.IterationTaskReal;
import cn.com.codes.object.MissionLog;
import cn.com.codes.object.OtherMission;
import cn.com.codes.object.Project;
import cn.com.codes.object.SingleTestTask;
import cn.com.codes.object.User;
import cn.com.codes.object.UserOtherMission;
import cn.com.codes.otherMission.dto.OtherMissionDto;
import cn.com.codes.otherMission.service.OtherMissionService;
public class OtherMissionBlh extends BusinessBlh {

	private OtherMissionService otherMissionService;
	private static Logger logger = Logger.getLogger(OtherMissionBlh.class);
	

	public View otherMissionList(BusiRequestEvent req){
		return super.getView();
	}
	

	public View toMeCharge(BusiRequestEvent req){
		return super.getView();
	}
	

	public View toMeJoin(BusiRequestEvent req){
		return super.getView();
	}
	

	public View toMeConcern(BusiRequestEvent req){
		return super.getView();
	}
	

	public View allMissions(BusiRequestEvent req){
		return super.getView();
	}
	

	public View overview(BusiRequestEvent req){
		return super.getView();
	}

	@SuppressWarnings("unchecked")
	public View otherMissionListLoad(BusiRequestEvent req){
		OtherMissionDto dto = super.getDto(OtherMissionDto.class, req);
		//构造hql查询语句
		this.buildOtherMissionListHql(dto);
		List<OtherMission> otherMissions  = otherMissionService.findByHqlWithValuesMap(dto);
		PageModel pg = new PageModel();
		pg.setRows(otherMissions);
		pg.setTotal(dto.getTotal());
		
		super.writeResult(JsonUtil.toJson(pg));
		return super.globalAjax();
	}
	
	

	@SuppressWarnings("unchecked")
	public View add(BusiRequestEvent req){
		OtherMissionDto dto = super.getDto(OtherMissionDto.class, req);
		//保存任务编号
		OtherMissionDto otherMissionDto = new OtherMissionDto();
		otherMissionDto.setPageNo(1);
		otherMissionDto.setPageSize(1);
		this.buildOtherMissionListHql(otherMissionDto);
		List<OtherMission> otherMissions  = otherMissionService.findByHqlWithValuesMap(otherMissionDto);
		if(otherMissions != null && otherMissions.size() > 0){
			if(!StringUtils.isNullOrEmpty(otherMissions.get(0).getMissionNum())){
				int number = Integer.parseInt(otherMissions.get(0).getMissionNum()) + 1;
				dto.getOtherMission().setMissionNum(String.valueOf(number));
			}else{
				dto.getOtherMission().setMissionNum("1");
			}
		}else{
			dto.getOtherMission().setMissionNum("1");
		}
		String flag = otherMissionService.addOtherMission(dto);
		if(flag.equals("existed")){
			super.writeResult("existed");
		}else{
			super.writeResult("success");
		}
		return super.globalAjax();
	}
	

	public View update(BusiRequestEvent req){
		OtherMissionDto dto = super.getDto(OtherMissionDto.class, req);
		String flag = otherMissionService.updateOtherMission(dto);
		if(flag.equals("existed")){
			super.writeResult("existed");
		}else{
			super.writeResult("success");
		}
		return super.globalAjax();
	}
	

	public View delete(BusiRequestEvent req){
		OtherMissionDto dto = super.getDto(OtherMissionDto.class, req);
		otherMissionService.deleteOtherMission(dto);
		super.writeResult("success");
		return super.globalAjax();
	}
	

	public View getUsers(BusiRequestEvent req){
		OtherMissionDto dto = super.getDto(OtherMissionDto.class, req);
		String[] userIds  = otherMissionService.getUsers(dto);
		String ids = "";
		if(userIds != null){
			for(int i=0;i<userIds.length;i++){
				if(i != userIds.length-1){
					ids = ids + userIds[i] + ",";
				}else{
					ids = ids + userIds[i];
				}
			}
		}
		super.writeResult(ids);
		return super.globalAjax();
	}	
	

	public View getConcerns(BusiRequestEvent req){
		OtherMissionDto dto = super.getDto(OtherMissionDto.class, req);
		String[] userIds  = otherMissionService.getConcerns(dto);
		String ids = "";
		if(userIds != null){
			for(int i=0;i<userIds.length;i++){
				if(i != userIds.length-1){
					ids = ids + userIds[i] + ",";
				}else{
					ids = ids + userIds[i];
				}
			}
		}
		super.writeResult(ids);
		return super.globalAjax();
	}	
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void buildOtherMissionListHql(OtherMissionDto dto){
		StringBuffer hql = new StringBuffer();
		OtherMission otherMission = dto.getOtherMission();
		hql.append("from OtherMission omn where 1=1 ");
		Map praValMap = new HashMap();
		if(otherMission!=null){
			//根据任务名称查询
			if(otherMission.getMissionName()!=null&&!"".equals(otherMission.getMissionName())){
				hql.append("  and  omn.missionName like :missionName ");
				praValMap.put("missionName", "%"+otherMission.getMissionName()+"%");
			}
			//根据任务负责人查询
			if(otherMission.getChargePersonId()!=null&&!"".equals(otherMission.getChargePersonId())){
				hql.append("  and  omn.chargePersonId =:chargePersonId ");
				praValMap.put("chargePersonId", otherMission.getChargePersonId());
			}
			//根据任务创建者查询
			if(otherMission.getCreateUserId()!=null&&!"".equals(otherMission.getCreateUserId())){
				hql.append("  and  omn.createUserId =:createUserId ");
				praValMap.put("createUserId", otherMission.getCreateUserId());
			}
			//根据任务状态查询
			if(otherMission.getStatus()!=null&&!"".equals(otherMission.getStatus())){
				hql.append("  and  omn.status =:status ");
				praValMap.put("status", otherMission.getStatus());
			}
			//根据所属项目查询
			if(otherMission.getProjectId()!=null&&!"".equals(otherMission.getProjectId())){
				hql.append("  and  omn.projectId =:projectId ");
				praValMap.put("projectId", otherMission.getProjectId());
			}
		}
		//根据任务参与者查询
		if(dto.getUserId()!=null&&!"".equals(dto.getUserId())){
			String hql1 = "from UserOtherMission uom where uom.userId=?";
			List<UserOtherMission> userOtherMissions = otherMissionService.findByHql(hql1, dto.getUserId());
			if(userOtherMissions != null && userOtherMissions.size() > 0){
				List<String> missionIds = new ArrayList<String>();
				for(int i=0;i<userOtherMissions.size();i++){
					missionIds.add(userOtherMissions.get(i).getMissionId());
				}
				hql.append("  and  omn.missionId in (:missionIds) ");
				praValMap.put("missionIds", missionIds);
			}else{
				List<String> missionIds = new ArrayList<String>();
				missionIds.add("123");
				hql.append("  and  omn.missionId in (:missionIds) ");
				praValMap.put("missionIds", missionIds);
			}
		}
		//根据任务关注者查询任务
		if(dto.getConcernId()!=null&&!"".equals(dto.getConcernId())){
			String hql1 = "from ConcernOtherMission com where com.userId=?";
			List<ConcernOtherMission> concernOtherMissions = otherMissionService.findByHql(hql1, dto.getConcernId());
			if(concernOtherMissions != null && concernOtherMissions.size() > 0){
				List<String> missionIds = new ArrayList<String>();
				for(int i=0;i<concernOtherMissions.size();i++){
					missionIds.add(concernOtherMissions.get(i).getMissionId());
				}
				hql.append("  and  omn.missionId in (:missionIdss) ");
				praValMap.put("missionIdss", missionIds);
			}else{
				List<String> missionIds = new ArrayList<String>();
				missionIds.add("123");
				hql.append("  and  omn.missionId in (:missionIds) ");
				praValMap.put("missionIds", missionIds);
			}
		}
		
		if(!StringUtils.isNullOrEmpty(dto.getRelaMissionId())){
			List<String> relaMissions = new ArrayList<String>();
			String[] relaMissionId = dto.getRelaMissionId().split(" ");
			for(int i=0;i<relaMissionId.length;i++){
				relaMissions.add(relaMissionId[i]);
			}
			
			hql.append(" and omn.missionId not in (:relaMissionIds) ");
			praValMap.put("relaMissionIds", relaMissions);
		}
		
		
		if(dto.getRelaMissionId() != null){
			List<String> relaMissions1 = new ArrayList<String>();
			String hql1 = "from IterationTaskReal it where 1=1 and it.iterationMissionId is not null";
			List<IterationTaskReal> iterationTaskReals = otherMissionService.findByHql(hql1);
			if(iterationTaskReals != null && iterationTaskReals.size() > 0){
				for(int i=0;i<iterationTaskReals.size();i++){
					relaMissions1.add(iterationTaskReals.get(i).getMissionId());
				}
				hql.append(" and omn.missionId not in (:relaMissionIds1) ");
				praValMap.put("relaMissionIds1", relaMissions1);
			}
		}
		
		hql.append(" order by omn.createTime desc");
		dto.setHql(hql.toString());
		if(logger.isInfoEnabled()){
			logger.info(hql.toString());
		}
		dto.setHqlParamMaps(praValMap);
	}


	public View addProject(BusiRequestEvent req){
		OtherMissionDto dto = super.getDto(OtherMissionDto.class, req);
		Boolean status = otherMissionService.addProject(dto);
		if(status){
			super.writeResult("success");
		}else{
			super.writeResult("failed");
		}
		return super.globalAjax();
	}
	

	public View getProjectLists(BusiRequestEvent req){
		OtherMissionDto dto = super.getDto(OtherMissionDto.class, req);
		//List<Project> projects  = otherMissionService.getProjectLists(dto);
		String sql = "SELECT TASKID as projectId , PRO_NAME as projectName  FROM   t_single_test_task "+
					 " where filter_flag <>1 and status_flg!=4 "+
					 " union" +
					 " SELECT  distinct  pro.project_id, pro.project_name  FROM t_project pro ";
		List<Map<String,Object>> objectsList = otherMissionService.commonfindBySqlByJDBC(sql,false,new HashMap<String,Object>()); 
		PageModel pg = new PageModel();
		if(objectsList == null){
			pg.setRows(new ArrayList<Map<String,Object>>());
		}else{
			pg.setRows(objectsList);
		}
		super.writeResult(JsonUtil.toJson(pg));
		return super.globalAjax();
	}
	
	public View getProjectLists1(BusiRequestEvent req){
		OtherMissionDto dto = super.getDto(OtherMissionDto.class, req);
		List<Project> projects  = otherMissionService.getProjectLists1(dto);
		PageModel pg = new PageModel();
		if(projects == null){
			pg.setRows(new ArrayList<Project>());
		}else{
			pg.setRows(projects);
		}
		super.writeResult(JsonUtil.toJson(pg));
		return super.globalAjax();
	}
	
	public View getProjectLists2(BusiRequestEvent req){
		OtherMissionDto dto = super.getDto(OtherMissionDto.class, req);
		List<Project> projects  = otherMissionService.getProjectLists2(dto);
		PageModel pg = new PageModel();
		if(projects == null){
			pg.setRows(new ArrayList<Project>());
		}else{
			pg.setRows(projects);
		}
		super.writeResult(JsonUtil.toJson(pg));
		return super.globalAjax();
	}
	

	public View getProjectListsRelated(BusiRequestEvent req){
		OtherMissionDto dto = super.getDto(OtherMissionDto.class, req);
//		StringBuffer sb = new StringBuffer();
//		this.buildTaskListSql(dto);
//		dto.setPageNo(1);
//		dto.setPageSize(1000);
//		List list  = otherMissionService.findBySqlWithValuesMap(dto, new taskListVo());
//		List<Project>  projects = this.getProjectsSelf(dto);
//		if(projects==null) {
//			projects = new ArrayList<Project>();
//		}
//
//		if(list != null && list.size() > 0){
//			for(int i=0;i<list.size();i++){
//				Project project = new Project();
//				project.setProjectId(((SingleTestTask)list.get(i)).getTaskId());
//				project.setProjectName(((SingleTestTask)list.get(i)).getProName());
//				projects.add(project);
//			}
//		}
		/*String sql = "SELECT TASKID as projectId , PRO_NAME as projectName  FROM   t_single_test_task "+
					 " where filter_flag <>1  and (CREATE_ID='"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId()+"' or psm_id='"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId()+"') and status_flg!=4 "+
					 " union "+
					 " SELECT  distinct  pro.project_id, pro.project_name  FROM t_project pro , "+
					 " (SELECT om.* FROM t_other_mission om "+
					 " join   t_user_other_mission uom on om.mission_id=uom.mission_id and om.project_id is not null "+
					 " and uom.user_id='"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId()+"' "+
					 " union "+
					 " SELECT om.* FROM t_other_mission om "+
					 " join   t_concern_other_mission com on om.mission_id=com.mission_id and om.project_id is not null "+
					 " and com.user_id='"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId()+"' "+
					 " union "+
					 " SELECT om.* FROM t_other_mission om  where   om.create_user_id='"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getLoginName()+"' "+
					 " and om.project_id is not null) proTask "+
					 " where pro.project_id = proTask.project_id "+
					 " union"+
					 " select project_id as projectId , project_name as projectName from t_project"+
					 " where create_id ='"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId()+"' ";*/
		String sql = "SELECT TASKID as projectId , PRO_NAME as projectName  FROM   t_single_test_task"+
				" where filter_flag <>1  and STATUS_FLG <> 4 and (CREATE_ID='"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId()+"' or psm_id='"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId()+"') and status_flg!=4"+
				" union"+
				" SELECT  distinct  pro.project_id, pro.project_name  FROM t_project pro ,"+
				" (SELECT om.* FROM t_other_mission om"+
				" join   t_user_other_mission uom on om.mission_id=uom.mission_id and om.project_id is not null"+
				" and uom.user_id='"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId()+"' "+
				" union"+
				" SELECT om.* FROM t_other_mission om"+
				" join   t_concern_other_mission com on om.mission_id=com.mission_id and om.project_id is not null"+
				" and com.user_id='"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId()+"' "+
				" union"+
				" SELECT om.* FROM t_other_mission om  where   om.create_user_id='"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getLoginName()+"' "+
				" and om.project_id is not null) proTask"+
				" where pro.project_id =proTask.project_id"+
				" union"+
				" SELECT  distinct t.taskid as project_id , t.PRO_NAME as project_name  FROM t_single_test_task t"+
				" join t_task_useactor uat on t.taskid=uat.taskid and uat.is_enable=1 and t.STATUS_FLG <> 4 "+
				" and uat.userid='"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId()+"' "+
				" union"+
				" SELECT    pro.project_id, pro.project_name  FROM t_project pro  where create_id='"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId()+"' "+
				" union"+
				" SELECT  distinct  pro.taskid as projectId, pro.pro_name as projectName  FROM t_single_test_task  pro ,"+
				" (SELECT om.* FROM t_other_mission om"+
				" join   t_user_other_mission uom on om.mission_id=uom.mission_id and om.project_id is not null"+
				" and uom.user_id='"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId()+"' "+
				" union"+
				" SELECT om.* FROM t_other_mission om"+
				" join   t_concern_other_mission com on om.mission_id=com.mission_id and om.project_id is not null"+
				" and com.user_id='"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId()+"' "+
				" union"+
				" SELECT om.* FROM t_other_mission om  where   om.create_user_id='"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getLoginName()+"' "+
				" and om.project_id is not null) proTask"+
				" where pro.taskid =proTask.project_id and pro.STATUS_FLG <> 4 ";

		List<Map<String,Object>> objectsList = otherMissionService.commonfindBySqlByJDBC(sql,false,new HashMap<String,Object>()); 
		PageModel pg = new PageModel();
		if(objectsList == null){
			pg.setRows(new ArrayList<Map<String,Object>>());
		}else{
			pg.setRows(objectsList);
		}
		pg.setTotal(dto.getTotal());
		super.writeResult(JsonUtil.toJson(pg));
		return super.globalAjax();
	}
	

	public View getPeopleLists(BusiRequestEvent req){
		OtherMissionDto dto = super.getDto(OtherMissionDto.class, req);
		List<User> users  = otherMissionService.getPeopleLists(dto);
		PageModel pg = new PageModel();
		if(users == null){
			pg.setRows(new ArrayList<User>());
		}else{
			pg.setRows(users);
		}
		super.writeResult(JsonUtil.toJson(pg));
		return super.globalAjax();
	}

	public View getUserNames(BusiRequestEvent req){
		OtherMissionDto dto = super.getDto(OtherMissionDto.class, req);
		String[] userNames  = otherMissionService.getUserNames(dto);
		String names = "";
		if(userNames != null){
			for(int i=0;i<userNames.length;i++){
				if(i != userNames.length-1){
					names = names + userNames[i] + ",";
				}else{
					names = names + userNames[i];
				}
			}
		}
		super.writeResult(names);
		return super.globalAjax();
	}
	

	public View getConcernNames(BusiRequestEvent req){
		OtherMissionDto dto = super.getDto(OtherMissionDto.class, req);
		String[] userNames  = otherMissionService.getConcernNames(dto);
		String names = "";
		if(userNames != null){
			for(int i=0;i<userNames.length;i++){
				if(i != userNames.length-1){
					names = names + userNames[i] + ",";
				}else{
					names = names + userNames[i];
				}
			}
		}
		super.writeResult(names);
		return super.globalAjax();
	}

	public View updateStatus(BusiRequestEvent req){
		OtherMissionDto dto = super.getDto(OtherMissionDto.class, req);
		otherMissionService.updateStatus(dto);
		super.writeResult("success");
		return super.globalAjax();
	}


	public View concernMissions(BusiRequestEvent req){
		OtherMissionDto dto = super.getDto(OtherMissionDto.class, req);
		otherMissionService.concernMissions(dto);
		super.writeResult("success");
		return super.globalAjax();
	}
	

	@SuppressWarnings("unchecked")
	public View getMissionLog(BusiRequestEvent req){
		OtherMissionDto dto = super.getDto(OtherMissionDto.class, req);
		//构造hql查询语句
		this.buildOtherMissionLogListHql(dto);
		List<MissionLog> missionLogs  = otherMissionService.findByHqlWithValuesMap(dto);
		PageModel pg = new PageModel();
		pg.setRows(missionLogs);
		pg.setTotal(dto.getTotal());
		super.writeResult(JsonUtil.toJson(pg));
		return super.globalAjax();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void buildOtherMissionLogListHql(OtherMissionDto dto){
		StringBuffer hql = new StringBuffer();
		OtherMission otherMission = dto.getOtherMission();
		hql.append("from MissionLog ml where 1=1 ");
		Map praValMap = new HashMap();
		if(otherMission!=null){
			//根据任务Id查询
			if(otherMission.getMissionId()!=null&&!"".equals(otherMission.getMissionId())){
				hql.append("  and  ml.missionId = :missionId ");
				praValMap.put("missionId", otherMission.getMissionId());
			}
		}
		hql.append(" order by ml.operateTime desc");
		dto.setHql(hql.toString());
		if(logger.isInfoEnabled()){
			logger.info(hql.toString());
		}
		dto.setHqlParamMaps(praValMap);
	}
	
	private void buildTaskListSql(OtherMissionDto dto) {
		Map praValMap = new HashMap();
		praValMap.put("companyId", SecurityContextHolderHelp.getCompanyId());
		StringBuffer sql = new StringBuffer();
		sql.append("select ")
				.append("base.taskid,")
				.append("base.pro_num,")
				.append("base.pro_name,")
				.append("base.dev_dept,")
				.append("base.test_phase,")
				.append("base.LOGINNAME,")
				.append("base.NAME,")
				.append("base.plan_start_date,")
				.append("base.plan_end_date,")
				.append("base.status_flg,")
				.append("base.psm_id,")
				.append("base.filter_flag,")
				.append("base.plan_attach,")
				.append("base.tasktype,")
				.append("base.OUTLINESTATE ")
				.append("from (select ")
				.append("singletest0_.TASKID,")
				.append("singletest0_.PRO_NUM,")
				.append("singletest0_.PRO_NAME,")
				.append("singletest0_.DEV_DEPT,")
				.append("singletest0_.TEST_PHASE,")
				.append("singletest0_.FILTER_FLAG,")
				.append("singletest0_.PSM_ID,")
				.append("simpleuser1_.LOGINNAME , simpleuser1_.NAME ,")
				.append("singletest0_.PLAN_START_DATE,")
				.append("singletest0_.PLAN_END_DATE,")
				.append("singletest0_.STATUS_FLG,")
				.append("singletest0_.PLAN_ATTACH,")
				.append("'0' as taskType,")
				.append("singletest0_.upddate as upddate,")
				.append("td.OUTLINESTATE ")
				.append("from T_SINGLE_TEST_TASK singletest0_ ")
				.append("inner join T_USER simpleuser1_ on singletest0_.PSM_ID = ")
				.append("simpleuser1_.ID ").append(
						"and singletest0_.COMPANYID = :companyId ");
		/*if ("flwSet".equals(dto.getOperCmd())){
			sql.append(" and singletest0_.STATUS_FLG <4 ");
		}else{*/
			sql.append(" and singletest0_.STATUS_FLG <=3 and singletest0_.FILTER_FLAG != '1' ");
		/*}*/
		sql.append(" inner join t_test_task_detail td on td.taskid = singletest0_.taskid ");
		
		/*if ("caseList".equals(dto.getOperCmd())) {*/
			sql.append(" and td.TEST_TASK_TATE !=3 ");
		/*}*/
		int isAdmin = SecurityContextHolderHelp.getUserIsAdmin().intValue();
		if (isAdmin < 1) {
			sql.append(" and (td.taskid in ").append(
					"(select distinct ua.TASKID ").append(
					"from T_TASK_USEACTOR ua ").append(
					"where ua.TASKID = td.taskid ").append(
					"and ua.is_enable = 1 ")
					.append("and ua.userid = :userId ) ");
			praValMap.put("userId", SecurityContextHolderHelp.getUserId());
			/*if("flwSet".equals(dto.getOperCmd())||"magrTestTask".equals(dto.getOperCmd())){
				sql.append(" or  singletest0_.CREATE_ID=:userId" );
			}*/
			sql.append(" )");
		}


		sql.append(") base ");
//		SingleTestTask singleTask = dto.getSingleTest();
//		if (singleTask != null) {
//			sql.append("where 1=1 ");
//			if (singleTask.getProNum() != null
//					&& !"".equals(singleTask.getProNum())) {
//				sql.append("  and  base.pro_num like :proNum ");
//				praValMap.put("proNum", (new StringBuilder("%")).append(
//						singleTask.getProNum()).append("%").toString());
//			}
//			if (singleTask.getProName() != null
//					&& !"".equals(singleTask.getProName())) {
//				sql.append(" and  base.pro_name like :proName ");
//				praValMap.put("proName", (new StringBuilder("%")).append(
//						singleTask.getProName()).append("%").toString());
//			}
//			if (singleTask.getDevDept() != null
//					&& !"".equals(singleTask.getDevDept())) {
//				sql.append(" and  base.dev_dept like :devDept ");
//				praValMap.put("devDept", (new StringBuilder("%")).append(
//						singleTask.getDevDept()).append("%").toString());
//			}
//			
//			if (null !=singleTask.getStatus()&&singleTask.getStatus().intValue() != -1) {
//				sql.append(" and  base.status_flg = :status ");
//				praValMap.put("status", singleTask.getStatus());
//			}
//		}
		sql.append("order by base.upddate desc, base.STATUS_FLG ");
		dto.setHql(sql.toString());
		if (logger.isInfoEnabled())
			logger.info(sql.toString());
		dto.setHqlParamMaps(praValMap);
	}
	
	private List<Project> getProjectsSelf(OtherMissionDto dto){
		List<Project> projects = new ArrayList<Project>();
		if(dto.getRelated().equals("create")){
			String hql = "from Project p where 1=1 and p.projectId in (select om.projectId from OtherMission om where om.projectType = '1' and om.createUserId=?)";
			projects = otherMissionService.findByHql(hql,SecurityContextHolderHelp.getLoginName());
			return projects;
		}else if(dto.getRelated().equals("charge")){
			String hql = "from Project p where 1=1 and p.projectId in (select om.projectId from OtherMission om where om.projectType = '1' and om.chargePersonId=?)";
			projects = otherMissionService.findByHql(hql,SecurityContextHolderHelp.getUserId());
			return projects;
		}else if(dto.getRelated().equals("join")){
			String hql = "from Project p where 1=1 and p.projectId in (select om.projectId from OtherMission om where om.projectType = '1' and om.missionId in (select uom.missionId from UserOtherMission uom where uom.userId = ?))";
			projects = otherMissionService.findByHql(hql,SecurityContextHolderHelp.getUserId());
			return projects;
		}else if(dto.getRelated().equals("concern")){
			String hql = "from Project p where 1=1 and p.projectId in (select om.projectId from OtherMission om where om.projectType = '1' and om.missionId in (select com.missionId from ConcernOtherMission com where com.userId = ?))";
			projects = otherMissionService.findByHql(hql,SecurityContextHolderHelp.getUserId());
			return projects;
		}else if(dto.getRelated().equals("all")){
			String hql = "from Project p where 1=1 and p.projectId in (select om.projectId from OtherMission om where om.projectType = '1' and om.missionId in (select uom.missionId from UserOtherMission uom where uom.userId = ?))";
			projects = otherMissionService.findByHql(hql,SecurityContextHolderHelp.getUserId());
			String hql1 = "from Project p where 1=1 and p.projectId in (select om.projectId from OtherMission om where om.projectType = '1' and om.createUserId=?)";
			List<Project> project1 = otherMissionService.findByHql(hql1,SecurityContextHolderHelp.getLoginName());
			String hql2 = "from Project p where 1=1 and p.projectId in (select om.projectId from OtherMission om where om.projectType = '1' and om.missionId in (select com.missionId from ConcernOtherMission com where com.userId = ?))";
			List<Project> project2 = otherMissionService.findByHql(hql2,SecurityContextHolderHelp.getUserId());
			List<Project> allProjects = new ArrayList<Project>();
			if(projects != null && projects.size() > 0){
				for(int i=0;i<projects.size();i++){
					allProjects.add(projects.get(i));
				}
			}
			if(allProjects.size() > 0 && project1 != null && project1.size() > 0){
				int uu = 0;
				for(int p=0;p<allProjects.size();p++){
					if(allProjects.get(p).equals(project1.get(0).getProjectId())){
						break;
					}else{
						uu = uu + 1;
					}
				}
				if(allProjects.size() == uu){
					allProjects.add(project1.get(0));
				}
			}
			if(allProjects.size() == 0 && project1 != null && project1.size() > 0){
				allProjects.add(project1.get(0));
			}
			if(allProjects.size() > 0 && project2 != null && project2.size() > 0){
				List<Project> projectttts = new ArrayList<Project>();
				for(int yu=0;yu<project2.size();yu++){
					int tyu = 0;
					for(int qq=0;qq<allProjects.size();qq++){
						if(project2.get(yu).getProjectId().equals(allProjects.get(qq).getProjectId())){
							break;
						}else{
							tyu = tyu + 1;
						}
					}
					if(tyu == allProjects.size()){
						projectttts.add(project2.get(yu));
					}
				}
				if(projectttts.size() > 0){
					for(int ter=0;ter<projectttts.size();ter++){
						allProjects.add(projectttts.get(ter));
					}
				}
			}
			if(allProjects.size() == 0 && project2 != null && project2.size() > 0){
				allProjects = project2;
			}
			return allProjects;
		}
		return projects;
	}
	
	class taskListVo implements ConvertObjArrayToVo{
		public List<?> convert(List<?> resultSet){
			if(resultSet==null||resultSet.isEmpty()){
				return null;
			}
			List<JsonInterface> list = new ArrayList<JsonInterface>(resultSet.size());
			Iterator it = resultSet.iterator();
			while(it.hasNext()){
				SingleTestTask task = new SingleTestTask();
				Object values[] = (Object[])it.next();
				task.setTaskId(values[0].toString());
				task.setProNum(values[1].toString());
				task.setProName(values[2]==null?"":values[2].toString());
				task.setDevDept(values[3]==null?"":values[3].toString());
				task.setTestPhase(Integer.parseInt(values[4].toString()));
				task.setPsmName(values[5].toString()+"("+values[6].toString()+")");
				task.setPlanStartDate((Date)values[7]);
				task.setPlanEndDate((Date)values[8]);
				task.setStatus(Integer.parseInt(values[9].toString()));
				//task.setPlanDocName(values[9]==null?"":values[10].toString());
				task.setPlanDocName(values[10]==null?"":values[10].toString());
				task.setTaskType(values[11]==null?"":values[11].toString());
				task.setPsmId(values[10]==null?"":values[10].toString());
				task.setFilterFlag(values[13]==null?"":values[13].toString());
				task.setOutlineState(Integer.valueOf(values[14]==null?"":values[14].toString()));
				list.add(task);
			}
			return list;
		}
	}
	

	public View getOtherMissionList(BusiRequestEvent req){
		OtherMissionDto dto = super.getDto(OtherMissionDto.class, req);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(date); 
		cal.add(Calendar.MONTH, -6);
		date = cal.getTime();
		String agoTime = format.format(date);
		/*管理员,查出所有任务*/
		if((SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getIsAdmin() == 1 || SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getIsAdmin() == 2) && StringUtils.isNullOrEmpty(dto.getUsersId())){
			String sql = "SELECT om.* FROM t_other_mission om  where om.mission_id is not null ";
			HashMap<String, Object> map = new HashMap<String, Object>();
			if(dto.getOtherMission() != null){
				if(!StringUtils.isNullOrEmpty(dto.getOtherMission().getProjectId())){
					sql = sql + " and om.project_id= " + "'"+dto.getOtherMission().getProjectId()+"'";
					/*map.put("projectId", dto.getOtherMission().getProjectId());*/
				}else{
					sql = sql + " and om.create_time > "+ "'"+agoTime+"'";
				}
			}
			List<Map<String,Object>> missions = otherMissionService.commonfindBySqlByJDBC(sql, true, map); 
			if(missions != null && missions.size() > 0){
				super.writeResult(JsonUtil.toJson(missions));
			}else{
				super.writeResult(JsonUtil.toJson(new ArrayList<Map<String,Object>>()));
			}
		}else if((SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getIsAdmin() == 1 || SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getIsAdmin() == 2) && !StringUtils.isNullOrEmpty(dto.getUsersId())){
			User user = otherMissionService.get(User.class, dto.getUsersId());
			String sql = "select misson.* from "+
					" (SELECT om.* FROM t_other_mission om "+
					" join t_user_other_mission uom on om.mission_id=uom.mission_id "+
					" and uom.user_id="+ "'"+dto.getUsersId()+"'"+
					" union "+
					" SELECT om.* FROM t_other_mission om "+
					" join   t_concern_other_mission com on om.mission_id=com.mission_id "+
					" and com.user_id="+ "'"+dto.getUsersId()+"'"+
					" union "+
					" SELECT om.* FROM t_other_mission om  where om.create_user_id="+ "'"+user.getLoginName()+"'"+
					" ) misson where misson.mission_id is not null ";
			HashMap<String, Object> map = new HashMap<String, Object>();
			/*map.put("userId1", SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId());
			map.put("userId2", SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId());
			map.put("userId3", SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getLoginName());*/
			if(dto.getOtherMission() != null){
				if(!StringUtils.isNullOrEmpty(dto.getOtherMission().getProjectId())){
					sql = sql + " and misson.project_id ="+ "'"+dto.getOtherMission().getProjectId()+"'";
					/*map.put("projectId", dto.getOtherMission().getProjectId());*/
				}
			}
			List<Map<String,Object>> missions = otherMissionService.commonfindBySqlByJDBC(sql, true, map); 
			if(missions != null && missions.size() > 0){
				super.writeResult(JsonUtil.toJson(missions));
			}else{
				super.writeResult(JsonUtil.toJson(new ArrayList<Map<String,Object>>()));
			}
		}else{
			String sql = "select misson.* from "+
						" (SELECT om.* FROM t_other_mission om "+
						" join t_user_other_mission uom on om.mission_id=uom.mission_id "+
						" and uom.user_id="+ "'"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId()+"'"+
						" union "+
						" SELECT om.* FROM t_other_mission om "+
						" join   t_concern_other_mission com on om.mission_id=com.mission_id "+
						" and com.user_id="+ "'"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId()+"'"+
						" union "+
						" SELECT om.* FROM t_other_mission om  where om.create_user_id="+ "'"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getLoginName()+"'"+
						" ) misson where misson.mission_id is not null ";
			HashMap<String, Object> map = new HashMap<String, Object>();
			/*map.put("userId1", SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId());
			map.put("userId2", SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId());
			map.put("userId3", SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getLoginName());*/
			if(dto.getOtherMission() != null){
				if(!StringUtils.isNullOrEmpty(dto.getOtherMission().getProjectId())){
					sql = sql + " and misson.project_id ="+ "'"+dto.getOtherMission().getProjectId()+"'";
					/*map.put("projectId", dto.getOtherMission().getProjectId());*/
				}else{
					sql = sql + " and misson.create_time > "+ "'"+agoTime+"'";
				}
			}
			List<Map<String,Object>> missions = otherMissionService.commonfindBySqlByJDBC(sql, true, map); 
			if(missions != null && missions.size() > 0){
				super.writeResult(JsonUtil.toJson(missions));
			}else{
				super.writeResult(JsonUtil.toJson(new ArrayList<Map<String,Object>>()));
			}
		}
		
		return super.globalAjax();
	}
	
	public OtherMissionService getOtherMissionService() {
		return otherMissionService;
	}


	public void setOtherMissionService(OtherMissionService otherMissionService) {
		this.otherMissionService = otherMissionService;
	}
}
