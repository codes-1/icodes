package cn.com.codes.singleTestTaskManager.blh;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.dto.PageModel;
import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.common.ConvertObjArrayToVo;
import cn.com.codes.framework.common.JsonUtil;
import cn.com.codes.framework.transmission.JsonInterface;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.object.SingleTestTask;
import cn.com.codes.singleTestTaskManager.dto.SingleTestTaskDto;
import cn.com.codes.singleTestTaskManager.dto.TestLeaderVo;
import cn.com.codes.singleTestTaskManager.service.SingleTestTaskService;
import cn.com.codes.singleTestTaskManager.blh.SingleTestTaskBlh;


public class SingleTestTaskBlh extends BusinessBlh {

	private SingleTestTaskService singleTestTaskService;
	private static Logger logger = Logger.getLogger(SingleTestTaskBlh.class);
	
	public View magrTaskList(BusiRequestEvent req){
		
//		SingleTestTaskDto dto = super.getDto(SingleTestTaskDto.class, req);
//		StringBuffer sb = new StringBuffer();
////		this.buildMagrListHql(dto);
////		List list  = singleTestTaskService.findByHqlWithValuesMap(dto);
//		dto.setOperCmd("magrTestTask");
//		this.buildTaskListSql(dto);
//		List list  = singleTestTaskService.findBySqlWithValuesMap(dto, new taskListVo());
//		this.setRelaTestLdUser(list);
//		//dto.toJson2(list, sb);
//		PageModel pg = new PageModel();
//		pg.setRows(list);
//		pg.setTotal(dto.getTotal());
//		super.writeResult(JsonUtil.toJson(pg));
		//if(dto.getIsAjax()!=null){
			//super.writeResult(sb.toString());
			//return super.globalAjax();
		///}
		//dto.setListStr(sb.toString());
		return super.getView();
		//return super.globalAjax();
	}
	
	
	public View magrTaskListLoad(BusiRequestEvent req){
		
		SingleTestTaskDto dto = super.getDto(SingleTestTaskDto.class, req);
		StringBuffer sb = new StringBuffer();
//		this.buildMagrListHql(dto);
//		List list  = singleTestTaskService.findByHqlWithValuesMap(dto);
		dto.setOperCmd("magrTestTask");
		this.buildTaskListSql(dto);
		List list  = singleTestTaskService.findBySqlWithValuesMap(dto, new taskListVo());
		this.setRelaTestLdUser(list);
		//dto.toJson2(list, sb);
		PageModel pg = new PageModel();
		pg.setRows(list);
		pg.setTotal(dto.getTotal());
		super.writeResult(JsonUtil.toJson(pg));
		//if(dto.getIsAjax()!=null){
			//super.writeResult(sb.toString());
			//return super.globalAjax();
		///}
		//dto.setListStr(sb.toString());
		return super.getView();
//		return super.globalAjax();
	}
	
	public View flwSetInit(BusiRequestEvent req){
		SingleTestTaskDto dto = super.getDto(SingleTestTaskDto.class, req);
		String taskId = dto.getSingleTest()==null?null:dto.getSingleTest().getTaskId();
		if(taskId==null){
			taskId = SecurityContextHolderHelp.getCurrTaksId();
		}
		if(taskId==null||"".equals(taskId)){
			return super.getView("choiceTask");
		}
		return super.getView("setFlwPage");
	}
	public View swTestTaskList(BusiRequestEvent req){
		
		SingleTestTaskDto dto = super.getDto(SingleTestTaskDto.class, req);
		if(dto.getOperCmd()==null){
			dto.setOperCmd("myBugList");
		}
		StringBuffer sb = new StringBuffer();
		this.buildTaskListSql(dto);
		List list  = singleTestTaskService.findBySqlWithValuesMap(dto, new taskListVo());
		this.setRelaTestLdUser(list);
		PageModel pg = new PageModel();
		pg.setRows(list);
		pg.setTotal(dto.getTotal());
		super.writeResult(JsonUtil.toJson(pg));
		return super.globalAjax();
//		dto.toJson2(list, sb);
//		if(dto.getIsAjax()!=null){
//			super.writeResult(sb.toString());
//			return super.globalAjax();
//		}
//		dto.setListStr(sb.toString());
//		return super.getView();
	}
	
	public View swTestTask4Report(BusiRequestEvent req){
		
		SingleTestTaskDto dto = super.getDto(SingleTestTaskDto.class, req);
		if(dto.getOperCmd()==null){
			dto.setOperCmd("myBugList");
		}
		if( dto.getSingleTest()==null){
			dto.setPageSize(10);
		}
		StringBuffer sb = new StringBuffer();
		this.buildTaskListSql(dto);
		List list  = singleTestTaskService.findBySqlWithValuesMap(dto, new taskListVo());
		this.setRelaTestLdUser(list);
		dto.toJson2(list, sb);
		if(dto.getIsAjax()!=null){
			super.writeResult(sb.toString());
			return super.globalAjax();
		}
		dto.setListStr(sb.toString());
		return super.getView();
	}
	
	public View getBugDateLimit(BusiRequestEvent req){
		SingleTestTaskDto dto = super.getDto(SingleTestTaskDto.class, req);
		String taskId = dto.getSingleTest().getTaskId();
		String[] dateLimeArr = singleTestTaskService.getTaskBugDateLimit(taskId);
		super.writeResult(dateLimeArr[0] +"_"+dateLimeArr[1]);
		return super.globalAjax();
	}
	
	public View getExeCaseDateLimit(BusiRequestEvent req){
		SingleTestTaskDto dto = super.getDto(SingleTestTaskDto.class, req);
		String taskId = dto.getSingleTest().getTaskId();
		String[] dateLimeArr = singleTestTaskService.getTaskeExeCaseDateLimit(taskId);
		super.writeResult(dateLimeArr[0] +"_"+dateLimeArr[1]);
		return super.globalAjax();
	}
	
	public View getWriteCaseDateLimit(BusiRequestEvent req){
		SingleTestTaskDto dto = super.getDto(SingleTestTaskDto.class, req);
		String taskId = dto.getSingleTest().getTaskId();
		String[] dateLimeArr = singleTestTaskService.getTaskeWriteCaseDateLimit(taskId);
		super.writeResult(dateLimeArr[0] +"_"+dateLimeArr[1]);
		return super.globalAjax();
	}
	public View flwSetList(BusiRequestEvent req){
		
		SingleTestTaskDto dto = super.getDto(SingleTestTaskDto.class, req);
		dto.setOperCmd("flwSet");
		StringBuffer sb = new StringBuffer();
		this.buildTaskListSql(dto);
		List list  = singleTestTaskService.findBySqlWithValuesMap(dto, new taskListVo());
		this.setRelaTestLdUser(list);
		dto.toJson2(list, sb);
		if(dto.getIsAjax()!=null){
			super.writeResult(sb.toString());
			return super.globalAjax();
		}
		dto.setListStr(sb.toString());
		return super.getView();
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
	private void setRelaTestLdUser(List<SingleTestTask> singleTaskList){
		if(singleTaskList==null||singleTaskList.isEmpty()){
			return;
		}
		StringBuffer hql = new StringBuffer();
		hql.append("select new cn.com.codes.singleTestTaskManager.dto.TestLeaderVo(u.id,u.name ,")
		   .append(	" u.loginName,ua.taskId ) from User u  join u.taskUseActors ua where " )
		   .append(	"ua.taskId in(:taskIds) and ua.actor=8");
		List<String> taskIds = new ArrayList<String>(singleTaskList.size());
		for(SingleTestTask task :singleTaskList){
			taskIds.add(task.getTaskId());
		}
		singleTestTaskService.sortStringList(taskIds);
		Map praValuesMap = new HashMap();
		praValuesMap.put("taskIds", taskIds);
		List<TestLeaderVo> userList = singleTestTaskService.findByHqlWithValuesMap(hql.toString(), praValuesMap, false);
		if(userList!=null&&userList.size()>0){
			for(SingleTestTask task :singleTaskList){
				for(TestLeaderVo user :userList){
					if(user.getTaskId().equals(task.getTaskId())){
						if(task.getTestLdVos()==null){
							task.setTestLdVos(new HashSet<TestLeaderVo>());
						}
						task.getTestLdVos().add(user);
					}
				}
			}
		}	
	}
	public View add(BusiRequestEvent req){
		SingleTestTaskDto dto = super.getDto(SingleTestTaskDto.class, req);
		if("".equals(dto.getSingleTest().getTestPhase())||dto.getSingleTest().getTestPhase()==null){
			dto.getSingleTest().setTestPhase(0);
		}
		String flag = singleTestTaskService.addSingleTest(dto.getSingleTest());
		if(flag.equals("existed")){
//			super.writeResult("failed");
			super.writeResult(JsonUtil.toJson(null));
			return super.globalAjax();
		}else{
			dto.getSingleTest().setTaskType("0");
			//super.writeResult("success$"+dto.getSingleTest().toStrUpdateRest());
			super.writeResult(JsonUtil.toJson(dto.getSingleTest()));
			return super.globalAjax();
		}
	}
	
	public View update(BusiRequestEvent req){
		SingleTestTaskDto dto = super.getDto(SingleTestTaskDto.class, req);
		SingleTestTask singleTest = dto.getSingleTest();
		SingleTestTask task = singleTestTaskService.get(SingleTestTask.class, singleTest.getTaskId());
		singleTest.setCompanyId(SecurityContextHolderHelp.getCompanyId());
		//以防篡改数据
		if(!task.getCompanyId().equals(singleTest.getCompanyId())){
			return super.globalAjax();
		}
		singleTest.setCreateId(task.getCreateId());
		singleTest.setInsDate(task.getInsDate());
//		singleTest.setStatus(task.getStatus());
		singleTest.setTestPhase(task.getTestPhase());
		String result  = "";
		if(!task.getProName().equals(singleTest.getProName())){// 说明修改了项目名，大刚那边也要做更新 
			result = this.checkProNameUq(singleTest);
			if(!"existed".equals(result)) {
				result = singleTestTaskService.updateSingleTest(dto.getSingleTest(),true);
			}
			
		}else {
			result = singleTestTaskService.updateSingleTest(dto.getSingleTest(),false);
		}
		
		if("existed".equals(result)) {
//			super.writeResult("existed");
			super.writeResult(JsonUtil.toJson(null));
			return super.globalAjax();
		}else{
			dto.getSingleTest().setTaskType("0");
			//super.writeResult("success$"+dto.getSingleTest().toStrUpdateRest());	
			super.writeResult(JsonUtil.toJson(dto.getSingleTest()));
			return super.globalAjax();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private String checkProNameUq(SingleTestTask singleTest) {
//		String hql = "from SingleTestTask s where s.proName=? and taskId <>? and s.and s.status <> 4";
//		List<SingleTestTask> singleTestTasks = singleTestTaskService.findByHql(hql, singleTest.getProName(),singleTest.getTaskId());
		String sql="SELECT * FROM t_single_test_task s WHERE s.PRO_NAME = ? AND s.TASKID = ? AND s.STATUS_FLG <> 4";
		List<SingleTestTask> singleTestTasks = singleTestTaskService.findBySql(sql.toString(), SingleTestTask.class, singleTest.getProName(),singleTest.getTaskId());
		//String hql1="update OutlineInfo set moduleName=? where superModuleId=0 and taskId=?";
		if(singleTestTasks != null && singleTestTasks.size() > 0){
//			super.getHibernateGenericController().executeUpdate(hql, singleTest.getProName(),singleTest.getTaskId());
			return "existed";
		}else {
			singleTest.setUpdDate(new Date());
			//singleTestTaskService.update(singleTest);
			//singleTestTaskService.executeUpdateByHql(hql1, new Object[]{singleTest.getProName(),singleTest.getTaskId()});
			return "notExisted";
		}
	}


	public View updInit(BusiRequestEvent req){
		SingleTestTaskDto dto = super.getDto(SingleTestTaskDto.class, req);
		SingleTestTask task  = singleTestTaskService.updInit(dto);
		if(task==null){
			super.writeResult("failed^记录己被删除");
		}
		//super.writeResult("success$"+task.toStrUpdateInit());
		task.setPsm(null);
		task.setTestTaskDetal(null);
		task.setUseActor(null);
		super.writeResult(super.addJsonPre("dto.singleTest", task));
		return super.globalAjax();
	}	
	public View delete(BusiRequestEvent req){
		SingleTestTaskDto dto = super.getDto(SingleTestTaskDto.class, req);
		singleTestTaskService.deleteSingleTest(dto.getSingleTest());
		if(dto.getSingleTest().getTaskId().equals(SecurityContextHolderHelp.getCurrTaksId())){
			SecurityContextHolderHelp.setCurrTaksId(null);
		}
		super.writeResult("success");
		return super.globalAjax();
	}
	
	private void buildTaskListSql(SingleTestTaskDto dto) {
		Map praValMap = new HashMap();
		praValMap.put("companyId", SecurityContextHolderHelp.getCompanyId());
		StringBuffer sql = new StringBuffer();
		sql.append("select base.taskid,")
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
				.append("from (select singletest0_.TASKID,")
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
		if ("flwSet".equals(dto.getOperCmd())){
			sql.append(" and singletest0_.STATUS_FLG <4 ");
		}else{
//			sql.append(" and singletest0_.STATUS_FLG <=3 ");以前版本只显示0、进行中，1、完成，2、结束，3、准备
			sql.append(" and singletest0_.STATUS_FLG <=6 and singletest0_.STATUS_FLG !=4");//现在都全部展示
		}
		sql.append(" inner join t_test_task_detail td on td.taskid = singletest0_.taskid ");
		
		if ("caseList".equals(dto.getOperCmd())) {
			sql.append(" and td.TEST_TASK_TATE !=3 ");
		}
		int isAdmin = SecurityContextHolderHelp.getUserIsAdmin().intValue();
		if (isAdmin < 1) {
			sql.append(" and (td.taskid in ").append(
					"(select distinct ua.TASKID ").append(
					"from T_TASK_USEACTOR ua ").append(
					"where ua.TASKID = td.taskid ").append(
					"and ua.is_enable = 1 ")
					.append("and ua.userid = :userId ) ");
			praValMap.put("userId", SecurityContextHolderHelp.getUserId());
			if("flwSet".equals(dto.getOperCmd())||"magrTestTask".equals(dto.getOperCmd())){
				sql.append(" or  singletest0_.CREATE_ID=:userId" );
			}
			sql.append(" )");
		}


		sql.append(") base ");
		SingleTestTask singleTask = dto.getSingleTest();
		if (singleTask != null) {
			sql.append("where 1=1 ");
			if (singleTask.getProNum() != null
					&& !"".equals(singleTask.getProNum())) {
				sql.append("  and  base.pro_num like :proNum ");
				praValMap.put("proNum", (new StringBuilder("%")).append(
						singleTask.getProNum()).append("%").toString());
			}
			if (singleTask.getProName() != null
					&& !"".equals(singleTask.getProName())) {
				sql.append(" and  base.pro_name like :proName ");
				praValMap.put("proName", (new StringBuilder("%")).append(
						singleTask.getProName()).append("%").toString());
			}
			if (singleTask.getDevDept() != null
					&& !"".equals(singleTask.getDevDept())) {
				sql.append(" and  base.dev_dept like :devDept ");
				praValMap.put("devDept", (new StringBuilder("%")).append(
						singleTask.getDevDept()).append("%").toString());
			}
			
			if (null !=singleTask.getStatus()&&singleTask.getStatus().intValue() != -1) {
				sql.append(" and  base.status_flg = :status ");
				praValMap.put("status", singleTask.getStatus());
			}
		}
		sql.append("order by base.upddate desc, base.STATUS_FLG ");
		dto.setHql(sql.toString());
		if (logger.isInfoEnabled())
			logger.info(sql.toString());
		dto.setHqlParamMaps(praValMap);
	}

	
	private void buildMagrListHql(SingleTestTaskDto dto){
		
		StringBuffer hql = new StringBuffer();
		SingleTestTask singleTask = dto.getSingleTest();
		hql.append("select new SingleTestTask(t.taskId,t.proNum,t.proName,t.devDept,")
		   .append(" t.testPhase, (p.loginName||'('||p.name||')') as psmName,t.planStartDate,") 
		   .append("t.planEndDate,t.status,t.planDocName) from SingleTestTask t ")
		   .append(" join t.psm p where t.companyId =:companyId and t.status <4");
		Map praValMap = new HashMap();
		praValMap.put("companyId", SecurityContextHolderHelp.getCompanyId());
		if(singleTask!=null){
			if(singleTask.getProNum()!=null&&!"".equals(singleTask.getProNum())){
				hql.append("  and  t.proNum like :proNum ");
				praValMap.put("proNum", "%"+singleTask.getProNum()+"%");
			}
			if(singleTask.getProName()!=null&&!"".equals(singleTask.getProName())){
				hql.append(" and  t.proName like :proName ");
				praValMap.put("proName", "%"+singleTask.getProName()+"%");
			}	
			if(singleTask.getDevDept()!=null&&!"".equals(singleTask.getDevDept())){
				hql.append(" and  t.devDept like :devDept ");
				praValMap.put("devDept", "%"+singleTask.getDevDept()+"%");
			}
			if(singleTask.getStatus()!=-1){
				hql.append(" and  t.status = :status ");
				praValMap.put("status",singleTask.getStatus());
			}
		}
		hql.append(" order by t.updDate desc ,t.status asc");
		dto.setHql(hql.toString());
		if(logger.isInfoEnabled()){
			logger.info(hql.toString());
		}
		dto.setHqlParamMaps(praValMap);
	}

	public SingleTestTaskService getSingleTestTaskService() {
		return singleTestTaskService;
	}

	public void setSingleTestTaskService(SingleTestTaskService singleTestTaskService) {
		this.singleTestTaskService = singleTestTaskService;
	}

	public View putTaskIdSession(BusiRequestEvent req){
		SingleTestTaskDto dto = super.getDto(SingleTestTaskDto.class, req);
		if(dto.getSingleTest().getTaskId()!=null&&!"".equals(dto.getSingleTest().getTaskId())){
    		SecurityContextHolderHelp.setCurrTaksId(dto.getSingleTest().getTaskId());
    	}
		super.writeResult("success");
		return super.globalAjax();
	}
	

	public View getAddAllProject(BusiRequestEvent req){
		SingleTestTaskDto dto = super.getDto(SingleTestTaskDto.class, req);
		List<Map<String,Object>> singleMaps = singleTestTaskService.getAllProName(dto);
		
		super.writeResult(JsonUtil.toJson(singleMaps));
		return super.globalAjax();
	}

}
