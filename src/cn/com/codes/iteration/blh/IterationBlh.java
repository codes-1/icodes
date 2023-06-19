package cn.com.codes.iteration.blh;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.com.codes.bugManager.blh.BugManagerBlh;
import cn.com.codes.common.dto.PageModel;
import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.common.JsonUtil;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.security.VisitUser;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.iteration.dto.IterationDto;
import cn.com.codes.iteration.dto.IterationVo;
import cn.com.codes.iteration.service.IterationService;
import cn.com.codes.object.BugBaseInfo;
import cn.com.codes.object.IterationBugReal;
import cn.com.codes.object.IterationList;
import cn.com.codes.object.IterationTaskReal;
import cn.com.codes.object.IterationTestcasepackageReal;
import cn.com.codes.object.OtherMission;
import cn.com.codes.object.TestCasePackage;
import cn.com.codes.object.UserTestCasePkg;


public class IterationBlh extends BusinessBlh {
	
	private BugManagerBlh bugManagerBlh;
	private IterationService iterationService;
	private static Logger logger = Logger.getLogger(IterationBlh.class);
	
	public View iterationList(BusiRequestEvent req){
		return super.getView();
	}
	

	@SuppressWarnings("unchecked")
	public View iterationDataListLoad(BusiRequestEvent req){
		IterationDto dto = super.getDto(IterationDto.class, req);

		Integer isAdmin = SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getIsAdmin();
		PageModel pageModel = new PageModel(); 
		if(isAdmin==1||isAdmin==2){
			//hql查询
			this.buildIterationDataListHql(dto);
			List<IterationList> iterLists = iterationService.findByHqlWithValuesMap(dto);
			pageModel.setRows(iterLists);
			Integer total =  (Integer) SecurityContextHolder.getContext().getAttr("pageInfoTotalRows");
			pageModel.setTotal(total);
		}else {
			String useId = SecurityContextHolder.getContext().getVisit().getUserInfo(VisitUser.class).getId();
			dto.getIterationList().setUserId(useId);
			List<IterationVo> vos = new ArrayList<IterationVo>();
			//获取不同人的参与的迭代信息(不带查询条件)
			if(StringUtils.isNullOrEmpty(dto.getIterationList().getCreatePerson()) && StringUtils.isNullOrEmpty(dto.getIterationList().getIterationBagName())
					&& StringUtils.isNullOrEmpty(dto.getIterationList().getAssociationProject())){
				vos = iterationService.differentPersonWatchIterationDataInfo(dto);
			}else{
				//获取不同人的参与的迭代信息(带查询条件)
				vos = iterationService.differentPersonWatchIterationDataInfoWithParams(dto);
			}
			
			if(vos == null){
				pageModel.setRows(new ArrayList<IterationVo>());
				pageModel.setTotal(0);
			}else{
				pageModel.setRows(vos);
				pageModel.setTotal(dto.getTotal());
			}
			
		}
		
		super.writeResult(JsonUtil.toJson(pageModel));
		return super.globalAjax();
	}

	/** 
	* 方法名:          differentPersonWatchIterationDataInfo
	* 方法功能描述:     获取不同人的参与的迭代信息
	* @param:         
	* @return:        
	* @Author:        your name
	* @Create Date:   2018年12月27日 下午4:55:46
	*/
	@SuppressWarnings("unchecked")
	private List<IterationList> differentPersonWatchIterationDataInfo(IterationDto dto) {
		IterationList iterationList = dto.getIterationList();
		String userId = iterationList.getUserId();
		HashMap<String,Object> hashMap = new HashMap<String,Object>();
		StringBuffer buffer = new StringBuffer();
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
				hashMap.put("createUserId", userId);
				
				buffer.append(" and om.project_id is not null) proTask");
				
				buffer.append(" where pro.project_id = proTask.project_id union "
						+ " SELECT distinct t.taskid as project_id , t.PRO_NAME as project_name "
						+ " FROM t_single_test_task t join t_task_useactor uat on t.taskid=uat.taskid "
						+ " and uat.is_enable=1");
				buffer.append(" and uat.userid=:userId2)");
				hashMap.put("userId2", userId);
				
				buffer.append(" mytask where mytask.project_id = it.task_id");
				buffer.append(" or it.user_id=:userId3");
				hashMap.put("userId3", userId);
				
			}
		}
		
		buffer.append(" union SELECT it.* FROM t_iteration_list  it where it.task_id is null");
		
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
		
//		if(){
//			
//		}
//		    dto.setHql(buffer.toString());
//		    dto.setHqlParamMaps(hashMap);
//			Class[] classArr = null;
//			List<IterationList> itLists = iterationService.findBySqlWithValuesMap(dto,classArr);
//		    List<IterationList> itLists = iterationService.findBySqlWithValuesMap(buffer.toString(), dto.getPageNo(), dto.getPageSize(), hashMap);
//			if (null!=itLists&&itLists.size()>0) {
//				return itLists;
//			}else {
				return null;
//			}
	}


	private void buildIterationDataListHql(IterationDto dto) {
			StringBuffer hql = new StringBuffer();
			Map<String,Object> hashMap = new HashMap<String,Object>();
			IterationList iterationList = dto.getIterationList();
			hql.append(" from IterationList it where 1=1 ");
			
			if(null != iterationList){
				//根据迭代包名称
				if(!StringUtils.isNullOrEmpty(iterationList.getIterationBagName())){
					hql.append("and it.iterationBagName like :iterationBagName ");
					hashMap.put("iterationBagName", "%"+iterationList.getIterationBagName()+"%");
				}
				
				//根据关联项目
				if(!StringUtils.isNullOrEmpty(iterationList.getAssociationProject())){
					hql.append("and it.associationProject like :associationProject ");
					hashMap.put("associationProject", "%"+iterationList.getAssociationProject()+"%");
				}
				
				//根据创建人
				if(!StringUtils.isNullOrEmpty(iterationList.getCreatePerson())){
					hql.append("and it.createPerson like :createPerson ");
					hashMap.put("createPerson", "%"+iterationList.getCreatePerson()+"%");
				}
				
				//根据user_id
//				if(!StringUtils.isNullOrEmpty(iterationList.getUserId())){
//					hql.append("and it.userId =:userId ");
//					hashMap.put("userId", iterationList.getUserId());
//				}
				
				//根据status,0未删除状态，1删除
				if(!StringUtils.isNullOrEmpty(iterationList.getStatus())){
					hql.append("and it.status =:status ");
					hashMap.put("status", "0");
				}
				
				//根据taskid
				if(!StringUtils.isNullOrEmpty(iterationList.getTaskId())){
					hql.append("and it.taskId =:taskId ");
					hashMap.put("taskId", iterationList.getTaskId());
				}
				
				//根据时间
				if(iterationList.getCreateTime() != null&&!"".equals(iterationList.getCreateTime())){
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date createTime = iterationList.getCreateTime();
					String timeFormat = dateFormat.format(createTime);
					hql.append("and it.createTime =:createTime ");
					hashMap.put("createTime",timeFormat);
				}
			}
			
			hql.append(" order by it.createTime desc");
			dto.setHql(hql.toString());
			if(logger.isInfoEnabled()){
				logger.info(hql.toString());
			}
			dto.setHqlParamMaps(hashMap);
			
	}



	public View saveOrUpdateIteration(BusiRequestEvent req){
		IterationDto dto = super.getDto(IterationDto.class, req);
		if("".equals(dto.getIterationList().getIterationId())){
			dto.getIterationList().setStatus("0");//0:未更改关联，1：更改关联，未删除数据
			dto.getIterationList().setCreateTime(new Date());
			iterationService.addIterationData(dto);
		}else{
			iterationService.updateIterationData(dto);
		}
		super.writeResult("success");
		return super.globalAjax();
	}
	

	public View saveTestCasePackage(BusiRequestEvent req){
		IterationDto dto = super.getDto(IterationDto.class, req);
		if(!StringUtils.isNullOrEmpty(dto.getTestCaseP())){//testcasepackage关联,t_iteration_testcasepackage_real
			String[] testcase = dto.getTestCaseP().split(",");
			List<IterationTestcasepackageReal> iterationTestcaseList = new ArrayList<IterationTestcasepackageReal>();
			for (int i = 0; i < testcase.length; i++) {
				IterationTestcasepackageReal iterationTestcase = new IterationTestcasepackageReal();
				iterationTestcase.setIterationId(dto.getIterationList().getIterationId());
				iterationTestcase.setPackageId(testcase[i]);
				iterationTestcaseList.add(iterationTestcase);
			}
			iterationService.batchSaveTestCasePackage(iterationTestcaseList);
			
//			batchSaveOrUpdate(iterationTestcaseList);
		}
		super.writeResult("success");
		return super.globalAjax();
	}
	

	public View saveTaskReal(BusiRequestEvent req){
		IterationDto dto = super.getDto(IterationDto.class, req);
		if(!StringUtils.isNullOrEmpty(dto.getOtherMissionS())){//task关联,t_iteration_task_real
			String[] otherMissi = dto.getOtherMissionS().split(",");
			List<IterationTaskReal> IterationTaskRealList = new ArrayList<IterationTaskReal>();
			for (int i = 0; i < otherMissi.length; i++) {
				 IterationTaskReal iterationTaskReal = new IterationTaskReal();
				 iterationTaskReal.setIterationId(dto.getIterationList().getIterationId());
				 iterationTaskReal.setMissionId(otherMissi[i]);
				 IterationTaskRealList.add(iterationTaskReal);
			}
			iterationService.batchSaveIteraTask(IterationTaskRealList);
		}
		super.writeResult("success");
		return super.globalAjax();
	}
	

	public View saveBugReal(BusiRequestEvent req){
		IterationDto dto = super.getDto(IterationDto.class, req);
		if(!StringUtils.isNullOrEmpty(dto.getBugCardId())){//task关联,t_iteration_bug_real
			String[] bugCards = dto.getBugCardId().split(",");
			List<IterationBugReal> iterationBugReals = new ArrayList<IterationBugReal>();
			for (int i = 0; i < bugCards.length; i++) {
				 IterationBugReal iterationBugReal = new IterationBugReal();
				 iterationBugReal.setIterationId(dto.getIterationList().getIterationId());
				 iterationBugReal.setBugCardId(bugCards[i]);
				 iterationBugReals.add(iterationBugReal);
			}
			iterationService.batchSaveIteraBug(iterationBugReals);
		}
		
		super.writeResult("success");
		return super.globalAjax();
	}
	
	

	public View findUpdateIterationInfo(BusiRequestEvent req){
		IterationDto dto = super.getDto(IterationDto.class, req);
		List<IterationList> iterList = iterationService.findByProperties(IterationList.class, 
									     new String[]{"iterationId"},
											new Object[]{dto.getIterationList().getIterationId()});
		if(iterList.size() > 0){
			dto.setIterationLists(iterList);
		}else {
			dto.setIterationLists(null);
		}
		iterationService.getBugTaskTestcaseInfo(dto);
		
		super.writeResult(JsonUtil.toJson(dto));
		return super.globalAjax();
	}
	

	public View deleteIterationInfo(BusiRequestEvent req){
		IterationDto dto = super.getDto(IterationDto.class, req);
		iterationService.deleteIterationAboutInfo(dto);
		super.writeResult("success");
		return super.globalAjax();
	}
	

	@SuppressWarnings("unchecked")
	public View searchBugDetail(BusiRequestEvent req){
		IterationDto dto = super.getDto(IterationDto.class, req);
		if(null==dto.getPageModel()){
			dto.setPageModel(new PageModel());
		}
		iterationService.bugDetail(dto);
		List<BugBaseInfo> bugBaseInfos = new ArrayList<BugBaseInfo>();
		List<Map<String, Object>> infos = (List<Map<String, Object>>) dto.getPageModel().getRows();
		PageModel pageModel = new PageModel();
		if(infos!=null){
			if(infos.size()>0){
				for (int i = 0; i < infos.size(); i++) {
					BugBaseInfo bugBaseInfo = new BugBaseInfo();
					String taskId = (String) infos.get(i).get("taskId");
					String bugDesc = (String) infos.get(i).get("bugDesc");
					Long bugId = (Long) infos.get(i).get("bugId");
					Long bugTypeId = (Long) infos.get(i).get("bugTypeId");
					Integer currStateId = (Integer) infos.get(i).get("currStateId");
					Long bugGradeId = (Long) infos.get(i).get("bugGradeId");
					Long geneCauseId = (Long) infos.get(i).get("geneCauseId");
					Long priId = (Long) infos.get(i).get("priId");
					String testOwnerId = (String) infos.get(i).get("testOwnerId");
					String devOwnerId = (String) infos.get(i).get("devOwnerId");
					Date reptDate = (Date) infos.get(i).get("reptDate");
					Long bugReptVer = (Long) infos.get(i).get("bugReptVer");
					Long moduleId = (Long) infos.get(i).get("moduleId");
					
					bugBaseInfo.setModuleId(moduleId);
					bugBaseInfo.setBugReptVer(bugReptVer);
					bugBaseInfo.setTaskId(taskId);
					bugBaseInfo.setBugDesc(bugDesc);
					bugBaseInfo.setBugId(bugId);
					bugBaseInfo.setBugTypeId(bugTypeId);
					bugBaseInfo.setCurrStateId(currStateId);
					bugBaseInfo.setGeneCauseId(geneCauseId);
					bugBaseInfo.setPriId(priId);
					bugBaseInfo.setTestOwnerId(testOwnerId);
					bugBaseInfo.setDevOwnerId(devOwnerId);
					bugBaseInfo.setReptDate(reptDate);
					bugBaseInfo.setBugGradeId(bugGradeId);
					bugBaseInfo.setTestCases(null);
					
					bugBaseInfos.add(bugBaseInfo);
				}
				
				bugManagerBlh.setRelaTypeDefine(bugBaseInfos);
				bugManagerBlh.setRelaUser(bugBaseInfos);
				bugManagerBlh.setRelaTaskName(bugBaseInfos);
				bugManagerBlh.setStateName(bugBaseInfos);
			}
			pageModel.setRows(bugBaseInfos);
			pageModel.setTotal(dto.getPageModel().getTotal());
		}else{
			pageModel.setRows(null);
			pageModel.setTotal(0);
		}
		
		super.writeResult(JsonUtil.toJson(pageModel));
		return super.globalAjax();
	}
	

	@SuppressWarnings("unchecked")
	public View searchTestCaseDetail(BusiRequestEvent req){
		IterationDto dto = super.getDto(IterationDto.class, req);
		if(null==dto.getPageModel()){
			dto.setPageModel(new PageModel());
		}
		iterationService.TestCaseDetail(dto);
		List<TestCasePackage> packages = new ArrayList<TestCasePackage>();
		List<Map<String, Object>> infos = (List<Map<String, Object>>) dto.getPageModel().getRows();
		PageModel pageModel = new PageModel();
		if(infos!=null){
			if(infos.size()>0){
				for (int i = 0; i < infos.size(); i++) {
					TestCasePackage testCasePackage = new TestCasePackage();
					
					String packageId = (String) infos.get(i).get("packageId");
					String taskId = (String) infos.get(i).get("taskId");
					String packageName = (String) infos.get(i).get("packageName");
					String execEnvironment = (String) infos.get(i).get("execEnvironment");
					String executor = (String) infos.get(i).get("executor");
					String remark = (String) infos.get(i).get("remark");
					
					testCasePackage.setPackageId(packageId);
					testCasePackage.setTaskId(taskId);
					testCasePackage.setPackageName(packageName);
					testCasePackage.setExecEnvironment(execEnvironment);
					testCasePackage.setExecutor(executor);
					testCasePackage.setRemark(remark);
					//查询用例包的信息
					List<UserTestCasePkg>  userTestcaseL = iterationService.searchTestCasePackageInfo(packageId);
					Set<UserTestCasePkg> userSets = new HashSet<>(userTestcaseL);
					testCasePackage.setUserTestCasePkgs(userSets);
					
					packages.add(testCasePackage);
				}
				
			}
			pageModel.setRows(packages);
			pageModel.setTotal(dto.getPageModel().getTotal());
		}else{
			pageModel.setRows(null);
			pageModel.setTotal(0);
		}
		
		super.writeResult(JsonUtil.toJson(pageModel));
		return super.globalAjax();
	}
	
	@SuppressWarnings("unchecked")
	public View searchIteraTaskDetail(BusiRequestEvent req){
		IterationDto dto = super.getDto(IterationDto.class, req);
		if(null==dto.getPageModel()){
			dto.setPageModel(new PageModel());
		}
		
		iterationService.TaskDetail(dto);
		List<OtherMission> OtherMissions = new ArrayList<OtherMission>();
		List<Map<String, Object>> infos = (List<Map<String, Object>>) dto.getPageModel().getRows();
		PageModel pageModel = new PageModel();
		if(infos!=null){
			if(infos.size()>0){
				for (int i = 0; i < infos.size(); i++) {
					OtherMission OtherMission = new OtherMission();
					String missionId = (String) infos.get(i).get("missionId");
					String missionName = (String) infos.get(i).get("missionName");
					String projectId = (String) infos.get(i).get("projectId");
					String chargePersonId = (String) infos.get(i).get("chargePersonId");
					String actualWorkload = (String) infos.get(i).get("actualWorkload");
					String description = (String) infos.get(i).get("description");
					String completionDegree = (String) infos.get(i).get("completionDegree");
					String status = (String) infos.get(i).get("status");
					
					OtherMission.setMissionId(missionId);
					OtherMission.setProjectId(projectId);
					OtherMission.setMissionName(missionName);
					OtherMission.setChargePersonId(chargePersonId);
					OtherMission.setActualWorkload(actualWorkload);
					OtherMission.setDescription(description);
					OtherMission.setCompletionDegree(completionDegree);
					OtherMission.setStatus(status);
					
					OtherMissions.add(OtherMission);
				}
				
			}
			pageModel.setRows(OtherMissions);
			pageModel.setTotal(dto.getPageModel().getTotal());
		}else{
			pageModel.setRows(null);
			pageModel.setTotal(0);
		}
		
		super.writeResult(JsonUtil.toJson(pageModel));
		return super.globalAjax();
	}
	
	public View iterationBugLayout(BusiRequestEvent req)throws BaseException{
			return super.getView();
	}
	
	public View iterationTestCaseLayout(BusiRequestEvent req)throws BaseException{
		return super.getView();
	}
	
	public View iterationTaskLayout(BusiRequestEvent req)throws BaseException{
		return super.getView();
	}
	
	
	/**  
	 * @return iterationService 
	 */
	public IterationService getIterationService() {
		return iterationService;
	}

	/**  
	 * @param iterationService iterationService 
	 */
	public void setIterationService(IterationService iterationService) {
		this.iterationService = iterationService;
	}

	/**  
	* @return bugManagerBlh 
	*/
	public BugManagerBlh getBugManagerBlh() {
		return bugManagerBlh;
	}

	/**  
	* @param bugManagerBlh bugManagerBlh 
	*/
	public void setBugManagerBlh(BugManagerBlh bugManagerBlh) {
		this.bugManagerBlh = bugManagerBlh;
	}
}
