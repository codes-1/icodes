package cn.com.codes.iteration.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.codes.common.dto.PageModel;
import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.app.services.BaseServiceImpl;
import cn.com.codes.framework.jdbc.JdbcTemplateWrapper;
import cn.com.codes.framework.security.VisitUser;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.iteration.dto.IterationDto;
import cn.com.codes.iteration.dto.IterationVo;
import cn.com.codes.iteration.service.IterationService;
import cn.com.codes.object.BugBaseInfo;
import cn.com.codes.object.IterationBugReal;
import cn.com.codes.object.IterationList;
import cn.com.codes.object.IterationTaskReal;
import cn.com.codes.object.IterationTestcasepackageReal;
import cn.com.codes.object.OtherMission;
import cn.com.codes.object.TestCaseInfo;
import cn.com.codes.object.TestCasePackage;
import cn.com.codes.object.UserTestCasePkg;


public class IterationServiceImpl extends BaseServiceImpl implements IterationService {
	
	private JdbcTemplateWrapper jdbcTemplateWrapper; 
	
	
	@Override
	public void addIterationData(IterationDto dto) {
		// TODO Auto-generated method stub
		this.add(dto.getIterationList());
//		String iterationId = dto.getIterationList().getIterationId();
//		
//		if(!StringUtils.isNullOrEmpty(dto.getBugCardId())){//bug关联,t_iteration_bug_real
//			String[] splits = dto.getBugCardId().split(" ");
//			ArrayList<IterationBugReal> iterationBugRealList = new ArrayList<IterationBugReal>();
//			for (int i = 0; i < splits.length; i++) {
//				IterationBugReal iterationBugReal = new IterationBugReal();
//				iterationBugReal.setIterationId(iterationId);
//				iterationBugReal.setBugCardId(splits[i]);
//				iterationBugRealList.add(iterationBugReal);
//			}
//			this.batchSaveOrUpdate(iterationBugRealList);
//		}
//		
//		if(!StringUtils.isNullOrEmpty(dto.getOtherMissionS())){//task关联,t_iteration_task_real
//			String[] otherMissi = dto.getOtherMissionS().split(" ");
//			ArrayList<IterationTaskReal> IterationTaskRealList = new ArrayList<IterationTaskReal>();
//			for (int i = 0; i < otherMissi.length; i++) {
//				 IterationTaskReal iterationTaskReal = new IterationTaskReal();
//				 iterationTaskReal.setIterationId(iterationId);
//				 iterationTaskReal.setMissionId(otherMissi[i]);
//				 IterationTaskRealList.add(iterationTaskReal);
//			}
//			this.batchSaveOrUpdate(IterationTaskRealList);
//		}
//		
//		
//		if(!StringUtils.isNullOrEmpty(dto.getTestCaseP())){//testcasepackage关联,t_iteration_testcasepackage_real
//			String[] testcase = dto.getTestCaseP().split(" ");
//			ArrayList<IterationTestcasepackageReal> iterationTestcaseList = new ArrayList<IterationTestcasepackageReal>();
//			for (int i = 0; i < testcase.length; i++) {
//				IterationTestcasepackageReal iterationTestcase = new IterationTestcasepackageReal();
//				iterationTestcase.setIterationId(iterationId);
//				iterationTestcase.setPackageId(testcase[i]);
//				iterationTestcaseList.add(iterationTestcase);
//			}
//			this.batchSaveOrUpdate(iterationTestcaseList);
//		}
		
	}

	@Override
	public void updateIterationData(IterationDto dto) {
		IterationList iterationList = this.get(IterationList.class, dto.getIterationList().getIterationId());
		dto.getIterationList().setCreateTime(iterationList.getCreateTime());
		dto.getIterationList().setUpdateTime(new Date());
		if((iterationList.getTaskId()).equals(dto.getIterationList().getTaskId())){
//			this.update(dto.getIterationList());
			dto.getIterationList().setStatus("0");
			deleteIterationAboutInfo(dto);
			addIterationData(dto);
		}else {
			/*原来*/
			/*iterationList.setStatus("1");
			this.update(iterationList);*/
			/*改后*/
			this.delete(iterationList);
			
			dto.getIterationList().setIterationId("");
			dto.getIterationList().setStatus("0");
			addIterationData(dto);
		}
//		iterationList.setStatus("1");
//		this.update(iterationList);
	}

	/* (非 Javadoc)   
	* <p>Title: getBugTaskTestcaseInfo</p>   
	* <p>Description: </p>   
	* @param dto   
	* @see cn.com.codes.iteration.service.IterationService#getBugTaskTestcaseInfo(cn.com.codes.iteration.dto.IterationDto)   
	*/
	@SuppressWarnings("unchecked")
	@Override
	public void getBugTaskTestcaseInfo(IterationDto dto) {
		// TODO Auto-generated method stub
		String iteraId = dto.getIterationList().getIterationId();
		String status = "0";
		String sql1= " SELECT bu.BUGDESC AS descr,bu.BUGCARDID AS bugCId "
				+ "FROM t_iteration_list it LEFT JOIN t_iteration_bug_real itb "
				+ "ON it.iteration_id = itb.iteration_id LEFT JOIN t_bugbaseinfo bu "
				+ "ON itb.bug_card_id = bu.BUGCARDID WHERE it.iteration_id='"+iteraId+"' AND it.status='"+status+"' ";
		 List<Map<String, Object>> bugMaps = this.findBySqlByJDBC(sql1);
		if(bugMaps.size() > 0){
			for (int i = 0; i < bugMaps.size(); i++) {
				Map<String, Object> map = bugMaps.get(i);
				Long bugCId = (Long)map.get("bugCId");
				if(bugCId!=null){
					dto.setBaseInfos(bugMaps);
				}else {
					dto.setBaseInfos(null);
				}
			}
		}else {
			dto.setBaseInfos(null);
		}
		
		String sql2 = " SELECT omi.mission_name AS missName,omi.mission_id AS missionId,omi.description AS descp "
				+ "FROM t_iteration_list it LEFT JOIN t_iteration_task_real itt "
				+ "ON it.iteration_id = itt.iteration_id LEFT JOIN t_other_mission omi "
				+ "ON itt.mission_id = omi.mission_id WHERE it.iteration_id='"+iteraId+"' "
				+ "AND it.status='"+status+"'";
		List<Map<String,Object>> otherMisList = this.findBySqlByJDBC(sql2);
		if(otherMisList.size() > 0){
			for (int i = 0; i < otherMisList.size(); i++) {
				Map<String, Object> map = otherMisList.get(i);
				String missionId = (String)map.get("missionId");
				if(missionId!=null){
					dto.setMissions(otherMisList);
				}else {
					dto.setMissions(null);
				}
			}
			
		}else {
			dto.setMissions(null);
		}
		
		String sql3 ="SELECT tcs.package_name AS packName,tcs.executor AS executors,tcs.id AS caseId "
				+ "FROM t_iteration_list it LEFT JOIN t_iteration_testcasepackage_real itc "
				+ "ON it.iteration_id = itc.iteration_id LEFT JOIN t_testcasepackage tcs "
				+ "ON tcs.id = itc.package_id WHERE it.iteration_id='"+iteraId+"' "
				+ "AND it.status='"+status+"' ";
		List<Map<String,Object>> testCaseList = this.findBySqlByJDBC(sql3);
		if(testCaseList.size() > 0){
			for (int i = 0; i < testCaseList.size(); i++) {
				Map<String, Object> map = testCaseList.get(i);
				String caseId = (String)map.get("caseId");
				if(caseId!=null){
					dto.setCasePackages(testCaseList);
				}else {
					dto.setCasePackages(null);
				}
			}
			
		}else {
			dto.setCasePackages(null);
		}
	}

	/* (非 Javadoc)   
	* <p>Title: deleteIterationAboutInfo</p>   
	* <p>Description: </p>   
	* @param dto   
	* @see cn.com.codes.iteration.service.IterationService#deleteIterationAboutInfo(cn.com.codes.iteration.dto.IterationDto)   
	*/
	@Override
	public void deleteIterationAboutInfo(IterationDto dto) {
		//删除t_iteration_bug_real的信息
		this.executeUpdateByHql(" delete from IterationBugReal where iterationId = ? ", new Object[]{dto.getIterationList().getIterationId()});
		
		//删除t_iteration_task_real的信息
		this.executeUpdateByHql(" delete from IterationTaskReal where iterationId = ? ", new Object[]{dto.getIterationList().getIterationId()});
		
		//删除t_iteration_testcasepackage_real的信息
		this.executeUpdateByHql(" delete from IterationTestcasepackageReal where iterationId = ? ", new Object[]{dto.getIterationList().getIterationId()});
		
		//删除t_iteration_list的信息
		this.executeUpdateByHql(" delete from IterationList where iterationId = ? ", new Object[]{dto.getIterationList().getIterationId()});
	}

	/* (非 Javadoc)   
	* <p>Title: bugDetail</p>   
	* <p>Description: </p>   
	* @param dto   
	* @see cn.com.codes.iteration.service.IterationService#bugDetail(cn.com.codes.iteration.dto.IterationDto)   
	*/
	@SuppressWarnings("unchecked")
	@Override
	public void bugDetail(IterationDto dto) {
		String sqlBug = " SELECT itbs.BUGDESC AS bugDesc,itbs.BUGCARDID AS bugId,itbs.TASK_ID AS taskId, "
				+ "itbs.BUGTYPE AS bugTypeId,itbs.CURRENT_STATE AS currStateId,itbs.MODULEID AS moduleId, "
				+ "itbs.BUGLEVEL AS bugGradeId,itbs.GENERATECAUSE AS geneCauseId,itbs.DISCOVER_VER AS bugReptVer, "
				+ "itbs.PRI AS priId,itbs.TEST_OWNER AS testOwnerId, "
				+ "itbs.DEV_OWNER AS devOwnerId,itbs.BUGDISVDATE AS reptDate "
				+ "FROM t_iteration_list it INNER JOIN t_iteration_bug_real itb "
				+ "ON it.iteration_id = itb.iteration_id INNER JOIN t_bugbaseinfo itbs ON itbs.BUGCARDID = itb.bug_card_id "
				+ "WHERE it.iteration_id='"+dto.getIterationList().getIterationId()+"' ";
		
		PageModel pageModel = dto.getPageModel();
		pageModel.setQueryHql(sqlBug);
		pageModel.setPageNo(dto.getPageNo());
		pageModel.setPageSize(dto.getPageSize());
		
		super.getJdbcTemplateWrapper().fillPageModelData(pageModel,null, "BUGCARDID");
		super.getJdbcTemplateWrapper().converDbColumnName2ObjectPropName((List<Map<String, Object>>) dto.getPageModel().getRows());

	}

	/* (非 Javadoc)   
	* <p>Title: batchSaveTestCasePackage</p>   
	* <p>Description: </p>   
	* @param iterationTestcaseList   
	* @see cn.com.codes.iteration.service.IterationService#batchSaveTestCasePackage(java.util.ArrayList)   
	*/
	@Override
	public void batchSaveTestCasePackage(
			List<IterationTestcasepackageReal> iterationTestcaseList) {
		this.batchSaveOrUpdate(iterationTestcaseList);
	}

	/* (非 Javadoc)   
	* <p>Title: TestCaseDetail</p>   
	* <p>Description: </p>   
	* @param dto
	* @return   
	* @see cn.com.codes.iteration.service.IterationService#TestCaseDetail(cn.com.codes.iteration.dto.IterationDto)   
	*/
	@SuppressWarnings("unchecked")
	@Override
	public void TestCaseDetail(IterationDto dto) {
		String sqlCase = " SELECT tca.id AS packageId,tca.taskId AS taskId, "
				+ "tca.package_name AS packageName,tca.exec_environment AS execEnvironment, "
				+ "tca.executor AS executor,tca.REMARK AS remark "
				+ "FROM t_iteration_list it INNER JOIN t_iteration_testcasepackage_real itt "
				+ "ON it.iteration_id = itt.iteration_id INNER JOIN t_testcasepackage tca "
				+ "ON tca.id = itt.package_id "
				+ "WHERE it.iteration_id = '"+dto.getIterationList().getIterationId()+"' ";
		

		PageModel pageModel = dto.getPageModel();
		pageModel.setQueryHql(sqlCase);
		pageModel.setPageNo(dto.getPageNo());
		pageModel.setPageSize(dto.getPageSize());
		
		super.getJdbcTemplateWrapper().fillPageModelData(pageModel,null, "id");
//		//查询用例测试包的信息
//		searchTestCasePackageInfo(dto);
		super.getJdbcTemplateWrapper().converDbColumnName2ObjectPropName((List<Map<String, Object>>) dto.getPageModel().getRows());
	
	}

	/* (非 Javadoc)   
	* <p>Title: batchSaveIteraTask</p>   
	* <p>Description: </p>   
	* @param iterationTaskRealList   
	* @see cn.com.codes.iteration.service.IterationService#batchSaveIteraTask(java.util.ArrayList)   
	*/
	@Override
	public void batchSaveIteraTask(
			List<IterationTaskReal> iterationTaskRealList) {
		this.batchSaveOrUpdate(iterationTaskRealList);
	}

	/* (非 Javadoc)   
	* <p>Title: TaskDetail</p>   
	* <p>Description: </p>   
	* @param dto
	* @return   
	* @see cn.com.codes.iteration.service.IterationService#TaskDetail(cn.com.codes.iteration.dto.IterationDto)   
	*/
	@SuppressWarnings("unchecked")
	@Override
	public void TaskDetail(IterationDto dto) {
		String sqlTask = " SELECT tm.mission_id AS missionId,tm.mission_name AS missionName, "
				+ "tm.project_id AS projectId,tm.charge_person_id AS chargePersonId, "
				+ "tm.actual_workload AS actualWorkload,tm.description AS description, "
				+ "tm.completion_degree AS completionDegree,tm.status AS status "
				+ "FROM t_iteration_list it INNER JOIN t_iteration_task_real ita "
				+ "ON it.iteration_id = ita.iteration_id INNER JOIN t_other_mission tm "
				+ "ON tm.mission_id = ita.mission_id WHERE it.iteration_id = '"+dto.getIterationList().getIterationId()+"' ";
		
		PageModel pageModel = dto.getPageModel();
		pageModel.setQueryHql(sqlTask);
		pageModel.setPageNo(dto.getPageNo());
		pageModel.setPageSize(dto.getPageSize());
		
		super.getJdbcTemplateWrapper().fillPageModelData(pageModel,null, "mission_id");
		super.getJdbcTemplateWrapper().converDbColumnName2ObjectPropName((List<Map<String, Object>>) dto.getPageModel().getRows());
	}

	/* (非 Javadoc)   
	* <p>Title: batchSaveIteraBug</p>   
	* <p>Description: </p>   
	* @param iterationBugReals   
	* @see cn.com.codes.iteration.service.IterationService#batchSaveIteraBug(java.util.List)   
	*/
	@Override
	public void batchSaveIteraBug(List<IterationBugReal> iterationBugReals) {
		// TODO Auto-generated method stub
		this.batchSaveOrUpdate(iterationBugReals);
	}

	/* (非 Javadoc)   
	* <p>Title: differentPersonWatchIterationDataInfo</p>   
	* <p>Description: </p>   
	* @param dto
	* @return   
	* @see cn.com.codes.iteration.service.IterationService#differentPersonWatchIterationDataInfo(cn.com.codes.iteration.dto.IterationDto)   
	*/
	@SuppressWarnings("unchecked")
	@Override
	public List<IterationVo> differentPersonWatchIterationDataInfo(IterationDto dto) {
		IterationList iterationList = dto.getIterationList();
		String userId = iterationList.getUserId();
		String loginName = SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getLoginName();
		HashMap<String,Object> hashMap = new HashMap<String,Object>();
//		PageModel pageModel = new PageModel();
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select  * from ( ") ;
		buffer.append(" SELECT it.* FROM t_iteration_list it ,(SELECT TASKID as project_id ,PRO_NAME as project_name FROM t_single_test_task where filter_flag <>1 ");
		if(null!=iterationList){
			if(!StringUtils.isNullOrEmpty(userId)){
				buffer.append(" and (CREATE_ID=:createId");
				buffer.append(" or psm_id=:psmId) and status_flg!=4 ");
				hashMap.put("createId", userId);
				hashMap.put("psmId", userId);
				
				buffer.append(" union SELECT distinct pro.project_id, pro.project_name "
						+ " FROM t_project pro ,(SELECT om.* FROM t_other_mission om "
						+ " join t_user_other_mission uom on om.mission_id=uom.mission_id "
						+ " and om.project_id is not null");
				
				buffer.append(" and uom.user_id=:userId ");
				hashMap.put("userId", userId);
				
				buffer.append(" union SELECT om.* FROM t_other_mission om join t_concern_other_mission com "
						+ " on om.mission_id=com.mission_id and om.project_id is not null");
				
				buffer.append(" and com.user_id=:userId1");
				hashMap.put("userId1", userId);
				
				buffer.append(" union SELECT om.* FROM t_other_mission om where");
				buffer.append(" om.create_user_id=:createUserId");
				hashMap.put("createUserId", loginName);
				
				buffer.append(" and om.project_id is not null) proTask");
				
				buffer.append(" where pro.project_id = proTask.project_id union "
						+ " SELECT distinct t.taskid as project_id , t.PRO_NAME as project_name "
						+ " FROM t_single_test_task t join t_task_useactor uat on t.taskid=uat.taskid "
						+ " and uat.is_enable=1");
				buffer.append(" and uat.userid=:userId2 ");
				hashMap.put("userId2", userId);
				
				buffer.append(" UNION"+
						" SELECT"+
						" pro.project_id,"+
						" pro.project_name"+
						" FROM"+
						" t_project pro"+
						" WHERE"+
						" create_id ='"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId()+"' "+
						" UNION"+
						" SELECT DISTINCT"+
						" pro.taskid AS projectId,"+
						" pro.pro_name AS projectName"+
						" FROM"+
						" t_single_test_task pro,"+
						" ("+
						" SELECT"+
						" om.*"+
						" FROM"+
						" t_other_mission om"+
						" JOIN t_user_other_mission uom ON om.mission_id = uom.mission_id"+
						" AND om.project_id IS NOT NULL"+
						" AND uom.user_id ='"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId()+"' "+
						" UNION"+
						" SELECT"+
						" om.*"+
						" FROM"+
						" t_other_mission om"+
						" JOIN t_concern_other_mission com ON om.mission_id = com.mission_id"+
						" AND om.project_id IS NOT NULL"+
						" AND com.user_id ='"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId()+"' "+
						" UNION"+
						" SELECT"+
						" om.*"+
						" FROM"+
						" t_other_mission om"+
						" WHERE"+
						" om.create_user_id ='"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getLoginName()+"' "+
						" AND om.project_id IS NOT NULL"+
						" ) proTask"+
						" WHERE"+
						" pro.taskid = proTask.project_id)");
				buffer.append(" mytask where it.iteration_id is not null ");
				/*if(!StringUtils.isNullOrEmpty(iterationList.getCreatePerson())){
					buffer.append(" and it.create_person like :create_person");
					hashMap.put("create_person", "%"+iterationList.getCreatePerson()+"%");
				}
				if(!StringUtils.isNullOrEmpty(iterationList.getIterationBagName())){
					buffer.append(" and it.iteration_bag_name like :iteration_bag_name");
					hashMap.put("iteration_bag_name", "%"+iterationList.getIterationBagName()+"%");
				}
				if(!StringUtils.isNullOrEmpty(iterationList.getAssociationProject())){
					buffer.append(" and it.association_project =:association_project");
					hashMap.put("association_project", iterationList.getAssociationProject());
				}*/
				buffer.append(" and (mytask.project_id = it.task_id or it.user_id=:userId3) ");
				hashMap.put("userId3", userId);
				/*if(StringUtils.isNullOrEmpty(iterationList.getAssociationProject()) && StringUtils.isNullOrEmpty(iterationList.getIterationBagName()) && StringUtils.isNullOrEmpty(iterationList.getCreatePerson())){
					buffer.append(" or it.user_id=:userId3");
					hashMap.put("userId3", userId);
				}*/
			}
		}
		
		
		buffer.append(" union SELECT it1.* FROM t_iteration_list  it1 where it1.task_id is null");
		buffer.append(" and it1.user_id=:userId4 ");
		buffer.append(   ")  allMyIt order by allMyIt.create_time desc ");
		hashMap.put("userId4", userId);
//		if(null!=iterationList){
//			if(!StringUtils.isNullOrEmpty(iterationList.getCreatePerson())){
//				buffer.append(" and it1.create_person like :create_person1");
//				hashMap.put("create_person1", "%"+iterationList.getCreatePerson()+"%");
//			}
//			if(!StringUtils.isNullOrEmpty(iterationList.getIterationBagName())){
//				buffer.append(" and it1.iteration_bag_name like :iteration_bag_name1");
//				hashMap.put("iteration_bag_name1", "%"+iterationList.getIterationBagName()+"%");
//			}
//			if(!StringUtils.isNullOrEmpty(iterationList.getAssociationProject())){
//				buffer.append(" and it1.association_project =:association_project1");
//				hashMap.put("association_project1", iterationList.getAssociationProject());
//			}
//		}
		
//		String sql=" SELECT it.* FROM t_iteration_list it, "
//				+ " (SELECT TASKID as project_id ,PRO_NAME as project_name  "
//				+ " FROM t_single_test_task where filter_flag <>1  "
//				+ " and (CREATE_ID='"+userId+"' "
//				+ " or psm_id='"+userId+"') "
//				+ " and status_flg!=4 "
//				+ " union SELECT distinct pro.project_id,pro.project_name "
//				+ " FROM t_project pro ,(SELECT om.* FROM t_other_mission om join "
//				+ " t_user_other_mission uom on om.mission_id=uom.mission_id and "
//				+ " om.project_id is not null and uom.user_id='"+userId+"' "
//				+ " union SELECT om.* FROM t_other_mission om join t_concern_other_mission com "
//				+ " on om.mission_id=com.mission_id and om.project_id is not null "
//				+ " and com.user_id='"+userId+"' "
//				+ " union SELECT om.* FROM t_other_mission om  where "
//				+ " om.create_user_id='"+userId+"' "
//				+ " and om.project_id is not null) proTask "
//				+ " where pro.project_id =proTask.project_id union "
//				+ " SELECT distinct t.taskid as project_id ,t.PRO_NAME as project_name "
//				+ " FROM t_single_test_task t join t_task_useactor uat on t.taskid=uat.taskid "
//				+ " and uat.is_enable=1 and uat.userid='"+userId+"') mytask "
//				+ " where mytask.project_id = it.task_id or it.user_id='"+userId+"' "
//				+ " union SELECT it.*  FROM t_iteration_list it where it.task_id is null";
		
//		    dto.setHql(buffer.toString());
//		    dto.setHqlParamMaps(hashMap);
//		    pageModel.setHqlParamMap(hashMap);
//		    pageModel.setQueryHql(buffer.toString());
//		    pageModel.setPageNo(dto.getPageNo());
//		    pageModel.setPageSize(dto.getPageSize());
		 	int totalRows = this.getJdbcTemplateWrapper().getResultCountWithValuesMap(buffer.toString(),
				 "*", hashMap);
		 	dto.setTotal(totalRows);
		 	if(totalRows>0) {
			    List<IterationVo> iterationVos = this.getJdbcTemplateWrapper().queryAllMatchListWithParaMap(buffer.toString(), IterationVo.class, hashMap, dto.getPageNo(), dto.getPageSize(), "*");
			    return iterationVos;
		 	}else {
		 		return null;
		 	}
		 	

	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<IterationVo> differentPersonWatchIterationDataInfoWithParams(IterationDto dto) {
		IterationList iterationList = dto.getIterationList();
		String userId = iterationList.getUserId();
		String loginName = SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getLoginName();
		HashMap<String,Object> hashMap = new HashMap<String,Object>();
		StringBuffer buffer = new StringBuffer();
		buffer.append(" SELECT allIterations.* FROM (SELECT it.* FROM t_iteration_list it ,(SELECT TASKID as project_id ,PRO_NAME as project_name FROM t_single_test_task where filter_flag <>1 ");
		if(null!=iterationList){
			if(!StringUtils.isNullOrEmpty(userId)){
				buffer.append(" and (CREATE_ID=:createId");
				buffer.append(" or psm_id=:psmId) and status_flg!=4 ");
				hashMap.put("createId", userId);
				hashMap.put("psmId", userId);
				
				buffer.append(" union SELECT distinct pro.project_id, pro.project_name "
						+ " FROM t_project pro ,(SELECT om.* FROM t_other_mission om "
						+ " join t_user_other_mission uom on om.mission_id=uom.mission_id "
						+ " and om.project_id is not null");
				
				buffer.append(" and uom.user_id=:userId ");
				hashMap.put("userId", userId);
				
				buffer.append(" union SELECT om.* FROM t_other_mission om join t_concern_other_mission com "
						+ " on om.mission_id=com.mission_id and om.project_id is not null");
				
				buffer.append(" and com.user_id=:userId1");
				hashMap.put("userId1", userId);
				
				buffer.append(" union SELECT om.* FROM t_other_mission om where");
				buffer.append(" om.create_user_id=:createUserId");
				hashMap.put("createUserId", loginName);
				
				buffer.append(" and om.project_id is not null) proTask");
				
				buffer.append(" where pro.project_id = proTask.project_id union "
						+ " SELECT distinct t.taskid as project_id , t.PRO_NAME as project_name "
						+ " FROM t_single_test_task t join t_task_useactor uat on t.taskid=uat.taskid "
						+ " and uat.is_enable=1");
				buffer.append(" and uat.userid=:userId2 ");
				hashMap.put("userId2", userId);
				
				buffer.append(" UNION"+
						" SELECT"+
						" pro.project_id,"+
						" pro.project_name"+
						" FROM"+
						" t_project pro"+
						" WHERE"+
						" create_id ='"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId()+"' "+
						" UNION"+
						" SELECT DISTINCT"+
						" pro.taskid AS projectId,"+
						" pro.pro_name AS projectName"+
						" FROM"+
						" t_single_test_task pro,"+
						" ("+
						" SELECT"+
						" om.*"+
						" FROM"+
						" t_other_mission om"+
						" JOIN t_user_other_mission uom ON om.mission_id = uom.mission_id"+
						" AND om.project_id IS NOT NULL"+
						" AND uom.user_id ='"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId()+"' "+
						" UNION"+
						" SELECT"+
						" om.*"+
						" FROM"+
						" t_other_mission om"+
						" JOIN t_concern_other_mission com ON om.mission_id = com.mission_id"+
						" AND om.project_id IS NOT NULL"+
						" AND com.user_id ='"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId()+"' "+
						" UNION"+
						" SELECT"+
						" om.*"+
						" FROM"+
						" t_other_mission om"+
						" WHERE"+
						" om.create_user_id ='"+SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getLoginName()+"' "+
						" AND om.project_id IS NOT NULL"+
						" ) proTask"+
						" WHERE"+
						" pro.taskid = proTask.project_id)");
				buffer.append(" mytask where it.iteration_id is not null ");
				buffer.append(" and (mytask.project_id = it.task_id or it.user_id=:userId3) ");
				hashMap.put("userId3", userId);
			}
		}
		buffer.append(" union SELECT it1.* FROM t_iteration_list  it1 where it1.task_id is null");
		buffer.append(" and it1.user_id=:userId4 ) allIterations where allIterations.iteration_id is not null ");
		hashMap.put("userId4", userId);
		if(!StringUtils.isNullOrEmpty(iterationList.getCreatePerson())){
			buffer.append(" and allIterations.create_person =:create_person1");
			hashMap.put("create_person1", iterationList.getCreatePerson());
		}
		if(!StringUtils.isNullOrEmpty(iterationList.getIterationBagName())){
			buffer.append(" and allIterations.iteration_bag_name like :iteration_bag_name1");
			hashMap.put("iteration_bag_name1", "%"+iterationList.getIterationBagName()+"%");
		}
		if(!StringUtils.isNullOrEmpty(iterationList.getAssociationProject())){
			buffer.append(" and allIterations.association_project =:association_project1");
			hashMap.put("association_project1", iterationList.getAssociationProject());
		}
		
		buffer.append(" order by allIterations.create_time desc ");
		int totalRows = this.getJdbcTemplateWrapper().getResultCountWithValuesMap(buffer.toString(),
				 "*", hashMap);
		 	dto.setTotal(totalRows);
		 	if(totalRows>0) {
		 		List<IterationVo> iterationVos = this.getJdbcTemplateWrapper().queryAllMatchListWithParaMap(buffer.toString(), IterationVo.class, hashMap, dto.getPageNo(), dto.getPageSize(), "it.iteration_id");
		 		return iterationVos;
		 	}else {
		 		return null;
		 	}

	}

	/* (非 Javadoc)   
	* <p>Title: searchTestCasePackageInfo</p>   
	* <p>Description: </p>   
	* @param packageId
	* @return   
	* @see cn.com.codes.iteration.service.IterationService#searchTestCasePackageInfo(java.lang.String)   
	*/
	@Override
	public List<UserTestCasePkg> searchTestCasePackageInfo(String packageId) {
		List<UserTestCasePkg> userTestLists = this.findByProperties(UserTestCasePkg.class, new String[] {"packageId"}, new Object[]{packageId});
		if(null!=userTestLists&&userTestLists.size()>0){
			return userTestLists;
		}
		return new ArrayList<UserTestCasePkg>();
	}
}
