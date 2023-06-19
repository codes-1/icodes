package cn.com.codes.overview.blh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.common.JsonUtil;
import cn.com.codes.framework.security.VisitUser;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.impExpManager.service.ImpExpManagerService;
import cn.com.codes.object.ConcernOtherMission;
import cn.com.codes.object.OtherMission;
import cn.com.codes.otherMission.service.OtherMissionService;
import cn.com.codes.overview.dto.DataVo;
import cn.com.codes.overview.dto.OverviewDto;

public class OverviewBlh extends BusinessBlh {

	private static Logger logger = Logger.getLogger(OverviewBlh.class);
	
	private ImpExpManagerService impExpManagerService;
	
	private OtherMissionService otherMissionService;
	

	public View loadInformation(BusiRequestEvent req){
		return super.getView();
	}
	

	@SuppressWarnings({ "unchecked", "unused" })
	public View getDetails(BusiRequestEvent req){
		OverviewDto dto = super.getDto(OverviewDto.class, req);
		//得到用例统计信息
		DataVo dataVo = impExpManagerService.getCaseCount(dto.getTaskId());
		//得到bug统计信息
		impExpManagerService.getBugCount(dto.getTaskId(),dataVo);
		/*统计我创建的任务*/
		OverviewDto overviewDto = new OverviewDto();
		overviewDto.setTaskId(dto.getTaskId());
		overviewDto.setCreateId(dto.getCreateId());
		this.buildOtherMissionListHql(overviewDto);
		List<OtherMission> otherMissions  = otherMissionService.findByHqlWithValuesMap(overviewDto);
		dataVo.setMeCreate(overviewDto.getTotal());
		/*统计我负责的任务*/
		OverviewDto overviewDto1 = new OverviewDto();
		overviewDto1.setTaskId(dto.getTaskId());
		overviewDto1.setChargePersonId(dto.getChargePersonId());
		this.buildOtherMissionListHql(overviewDto1);
		List<OtherMission> otherMissions1  = otherMissionService.findByHqlWithValuesMap(overviewDto1);
		dataVo.setMeCharge(overviewDto1.getTotal());
		/*统计我参与的任务*/
		OverviewDto overviewDto2 = new OverviewDto();
		overviewDto2.setTaskId(dto.getTaskId());
		overviewDto2.setJoinId(dto.getJoinId());
		this.buildOtherMissionListHql(overviewDto2);
		List<OtherMission> otherMissions2  = otherMissionService.findByHqlWithValuesMap(overviewDto2);
		dataVo.setMeJoin(overviewDto2.getTotal());
		/*统计和项目相关的所有任务*/
		OverviewDto overviewDto3 = new OverviewDto();
		overviewDto3.setTaskId(dto.getTaskId());
		this.buildOtherMissionListHql(overviewDto3);
		List<OtherMission> otherMissions3  = otherMissionService.findByHqlWithValuesMap(overviewDto3);
		dataVo.setMeAll(overviewDto3.getTotal());
		
		super.writeResult(JsonUtil.toJson(dataVo));
		return super.globalAjax();
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void buildOtherMissionListHql(OverviewDto dto){
		StringBuffer hql = new StringBuffer();
		hql.append("from OtherMission omn where 1=1 ");
		Map praValMap = new HashMap();
		//根据任务负责人查询
		if(dto.getChargePersonId()!=null&&!"".equals(dto.getChargePersonId())){
			hql.append("  and  omn.chargePersonId =:chargePersonId ");
			praValMap.put("chargePersonId", dto.getChargePersonId());
		}
		//根据任务创建者查询
		if(dto.getCreateId()!=null&&!"".equals(dto.getCreateId())){
			hql.append("  and  omn.createUserId =:createUserId ");
			praValMap.put("createUserId", dto.getCreateId());
		}
		//根据所属项目查询
		if(dto.getTaskId()!=null&&!"".equals(dto.getTaskId())){
			hql.append("  and  omn.projectId =:projectId ");
			praValMap.put("projectId", dto.getTaskId());
		}
		//根据任务参与者查询
		/*if(dto.getJoinId()!=null&&!"".equals(dto.getJoinId())){
			String hql1 = "from UserOtherMission uom where uom.userId=?";
			List<UserOtherMission> userOtherMissions = otherMissionService.findByHql(hql1, dto.getJoinId());
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
			
		}*/
		//根据任务关注者查询任务
		if(dto.getJoinId()!=null&&!"".equals(dto.getJoinId())){
			String hql1 = "from ConcernOtherMission com where com.userId=?";
			List<ConcernOtherMission> concernOtherMissions = otherMissionService.findByHql(hql1, dto.getJoinId());
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
		hql.append(" order by omn.createTime asc");
		dto.setHql(hql.toString());
		if(logger.isInfoEnabled()){
			logger.info(hql.toString());
		}
		dto.setHqlParamMaps(praValMap);
	}

	

	public View getIterationDetails(BusiRequestEvent req){
		OverviewDto dto = super.getDto(OverviewDto.class, req);
		List<List<Map<String,Object>>> iterationDetails = new ArrayList<List<Map<String,Object>>>();
		/*得到用例信息*/
		String sql = "SELECT  count(ifnull(pkg_case.execStatus ,1))as countNum,"+
						" ifnull(pkg_case.execStatus ,1) as status"+
						" FROM t_iteration_testcasepackage_real it_pkg_rel"+
						" join t_testcase_casepkg pkg_case on it_pkg_rel.package_id=pkg_case.packageid"+
						" where   it_pkg_rel.iteration_id='"+dto.getIterationId()+"' "+
						" group by pkg_case.execStatus";
		List<Map<String,Object>> caseLists = otherMissionService.commonfindBySqlByJDBC(sql,false,new HashMap<String,Object>()); 
		if(caseLists != null && caseLists.size() > 0){
			iterationDetails.add(caseLists);
		}else{
			iterationDetails.add(new ArrayList<Map<String,Object>>());
		}
		/*得到bug信息*/
		String sql1 = "select * from ("+
				" (select count(*) as allcount from"+
				" (select bug.bugcardid from t_iteration_list it"+
				" join   t_bugbaseinfo bug on bug.task_id=it.task_id"+
				" and    it.iteration_id='"+dto.getIterationId()+"' "+
				" join  t_iteration_bug_real it_bug_real  on bug.bugcardid= it_bug_real.bug_card_id"+
				" ) as base ) allBug,"+
				" (select count(*) as validCout  from"+
				" (select bug.bugcardid from t_iteration_list it"+
				" join   t_bugbaseinfo bug on bug.task_id=it.task_id"+
				" and    it.iteration_id='"+dto.getIterationId()+"' "+
				" and    bug.CURRENT_STATE not in (2,3,4,5,22)"+
				" join  t_iteration_bug_real it_bug_real  on bug.bugcardid= it_bug_real.bug_card_id)as  base2)validBug"+
				" ,	"+
				" (select count(*)  as closedCount from"+
				" (select bug.bugcardid from t_iteration_list it"+
				" join   t_bugbaseinfo bug on bug.task_id=it.task_id"+
				" and    it.iteration_id='"+dto.getIterationId()+"' "+
				" and    bug.CURRENT_STATE  in (14,15,23)"+
				" join  t_iteration_bug_real it_bug_real  on bug.bugcardid= it_bug_real.bug_card_id) as base3) closeBug"+
				" ,"+
				" (select count(*) as fixCount   from"+
				" (select bug.bugcardid from t_iteration_list it"+
				" join   t_bugbaseinfo bug on bug.task_id=it.task_id"+
				" and    it.iteration_id='"+dto.getIterationId()+"' "+
				" and    (bug.CURRENT_STATE= 13 or bug.CURRENT_STATE= 26)"+
				" join  t_iteration_bug_real it_bug_real  on bug.bugcardid= it_bug_real.bug_card_id)as base4 )fixBug"+
				" ,	"+
				" (select count(*) as noBugCount  from"+
				" (select bug.bugcardid from t_iteration_list it"+
				" join   t_bugbaseinfo bug on bug.task_id=it.task_id"+
				" and    it.iteration_id='"+dto.getIterationId()+"' "+
				" and    bug.CURRENT_STATE=16"+
				" join  t_iteration_bug_real it_bug_real  on bug.bugcardid= it_bug_real.bug_card_id)as base5 ) noBug"+
				" )";
		List<Map<String,Object>> bugLists = otherMissionService.commonfindBySqlByJDBC(sql1,false,new HashMap<String,Object>());
		if(bugLists != null && bugLists.size() > 0){
			iterationDetails.add(bugLists);
		}else{
			iterationDetails.add(new ArrayList<Map<String,Object>>());
		}
		/*得到任务信息*/
		String sql2 = "SELECT count(om.status)as countNum ,om.status"+
				" FROM t_iteration_task_real it"+
				" join  t_other_mission om   on om.mission_id=it.mission_id"+
				" where it.iteration_id='"+dto.getIterationId()+"' "+
				" group by om.status ";
		List<Map<String,Object>> missionsLists = otherMissionService.commonfindBySqlByJDBC(sql2,false,new HashMap<String,Object>()); 
		if(missionsLists != null && missionsLists.size() > 0){
			iterationDetails.add(missionsLists);
		}else{
			iterationDetails.add(new ArrayList<Map<String,Object>>());
		}
		super.writeResult(JsonUtil.toJson(iterationDetails));
		return super.globalAjax();
	}
	
	public ImpExpManagerService getImpExpManagerService() {
		return impExpManagerService;
	}

	public void setImpExpManagerService(ImpExpManagerService impExpManagerService) {
		this.impExpManagerService = impExpManagerService;
	}

	public OtherMissionService getOtherMissionService() {
		return otherMissionService;
	}

	public void setOtherMissionService(OtherMissionService otherMissionService) {
		this.otherMissionService = otherMissionService;
	}
}
