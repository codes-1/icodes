package cn.com.codes.singleTestTaskManager.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.app.services.BaseServiceImpl;
import cn.com.codes.object.SimpleUser;
import cn.com.codes.object.SingleTestTask;
import cn.com.codes.object.TestTaskDetail;
import cn.com.codes.singleTestTaskManager.dto.SingleTestTaskDto;
import cn.com.codes.singleTestTaskManager.service.SingleTestTaskService;

public class SingleTestTaskServiceImpl extends BaseServiceImpl implements SingleTestTaskService {

	public String addSingleTest(SingleTestTask singleTest){
		//判断项目名称是否已存在
		String hql = "from SingleTestTask s where s.proName=? and s.status <> 4";
		@SuppressWarnings("unchecked")
		List<SingleTestTask> singleTestTasks = this.findByHql(hql, singleTest.getProName());
		if(singleTestTasks != null && singleTestTasks.size() > 0){
			return "existed";
		}
		singleTest.setCreateId(SecurityContextHolderHelp.getUserId());
		singleTest.setInsDate(new Date());
		singleTest.setUpdDate(singleTest.getInsDate());
		singleTest.setStatus(3);
		singleTest.setCompanyId(SecurityContextHolderHelp.getCompanyId());
		singleTest.setFilterFlag(singleTest.getFilterFlag());
		singleTest.setPsmId(singleTest.getPsmId());
//		if(!StringUtils.isNullOrEmpty(singleTest.getTaskProjectId())){
		singleTest.setTaskProjectId(singleTest.getTaskProjectId());
		String taskId = "";
		if(singleTest.getFilterFlag().equals("0")){
			taskId = UUID.randomUUID().toString().replace("-", "").toLowerCase();
			singleTest.setTaskProjectId(null);
		}else if(singleTest.getFilterFlag().equals("1")){
			taskId = singleTest.getTaskProjectId();
		}
		singleTest.setTaskId(taskId);
		this.add(singleTest);
//		}else{
//			singleTest.setTaskProjectId("");
//		}
		TestTaskDetail testTaskDetal = new TestTaskDetail();
		testTaskDetal.setCompanyId(singleTest.getCompanyId());
		testTaskDetal.setInsdate(new Date());
		testTaskDetal.setCreateId(singleTest.getCreateId());
		testTaskDetal.setTaskState(3);
		testTaskDetal.setOutlineState(0);
		testTaskDetal.setReltCaseFlag(0);
		testTaskDetal.setCustomBug(0);
		testTaskDetal.setCustomCase(0);
		testTaskDetal.setUpgradeFlag(0);
		
		testTaskDetal.setTaskId(singleTest.getTaskId());
		testTaskDetal.setTestPhase(singleTest.getTestPhase());
		testTaskDetal.setProjectId(singleTest.getTaskId());//非监管测试项目，项目ID和任务ID相同   
		this.add(testTaskDetal);
		return "success";
	}
	
	public String updateSingleTest(SingleTestTask singleTest,boolean updateOutLint){


		if(updateOutLint) { //如己改项目名，就更新测试需求的根节点名称，
			String hql="update OutlineInfo set moduleName=? where superModuleId=0 and taskId=?";
			super.getHibernateGenericController().executeUpdate(hql, singleTest.getProName(),singleTest.getTaskId());
		}
		
		singleTest.setUpdDate(new Date());
		this.update(singleTest);
		return "success";
	}
	
	public void deleteSingleTest(SingleTestTask singleTest){
		SingleTestTask task = this.get(SingleTestTask.class, singleTest.getTaskId());
		if(task.getStatus()==3){
			if("0".equals(task.getFilterFlag())) {
				String sql = "select count(*) from t_other_mission where project_id=?";
				int missionCount = super.getJdbcTemplateWrapper().queryForInt(sql, singleTest.getTaskId());
				sql = "select count(*) from t_iteration_list where task_id=?";
				int itCount = super.getJdbcTemplateWrapper().queryForInt(sql, singleTest.getTaskId());
				if(missionCount==0&&itCount==0) {
					this.executeUpdateByHql("delete from  TestTaskDetail where taskId =? and companyId=? ", new Object[]{singleTest.getTaskId(),SecurityContextHolderHelp.getCompanyId()});
					this.executeUpdateByHql("delete from  SingleTestTask where taskId =? and companyId=? ", new Object[]{singleTest.getTaskId(),SecurityContextHolderHelp.getCompanyId()});
				}else {
					task.setStatus(4);
					this.update(task);
					this.executeUpdateByHql("update TestTaskDetail set testTaskState=4 where taskId =? and companyId=?  ", new Object[]{singleTest.getTaskId(),SecurityContextHolderHelp.getCompanyId()});
				}
			}else {
				this.executeUpdateByHql("delete from  TestTaskDetail where taskId =? and companyId=? ", new Object[]{singleTest.getTaskId(),SecurityContextHolderHelp.getCompanyId()});
				this.executeUpdateByHql("delete from  SingleTestTask where taskId =? and companyId=? ", new Object[]{singleTest.getTaskId(),SecurityContextHolderHelp.getCompanyId()});
			}
		}else{
			task.setStatus(4);
			this.update(task);
			this.executeUpdateByHql("update TestTaskDetail set testTaskState=4 where taskId =? and companyId=?  ", new Object[]{singleTest.getTaskId(),SecurityContextHolderHelp.getCompanyId()});
		}
	}
	
	public SingleTestTask updInit(SingleTestTaskDto dto){
		String hql = "from SingleTestTask s join fetch s.psm where s.taskId=? and s.companyId=?";
		List<SingleTestTask> list = this.findByHql(hql, dto.getSingleTest().getTaskId(),SecurityContextHolderHelp.getCompanyId());
		if(list!=null||!list.isEmpty()){
			SingleTestTask task = list.get(0);
			//不要注掉下面的代码,当缓存过期时,PSM是延迟加载
			if(task.getPsm()==null){
				hql = "from SimpleUser where id=?";
				List<SimpleUser> userList = this.findByHql(hql, task.getPsmId());
				task.setPsm(userList.get(0));
			}
			task.getPsm().getName();
			return task;
		}
		return null;
	}
	
	public String[] getTaskBugDateLimit(String taskId){
		String hql = "select insDate  from BugHandHistory where taskId = ? or"
				+ "der by insDate " ;
		List list = this.findByHqlPage(hql, 1, 1, "insDate", taskId);
		Date startDate = null;
		if(list!=null&&!list.isEmpty()){
			startDate = (Date)list.get(0);
		}
		Date endDate = null;
		hql = "select insDate  from BugHandHistory where taskId = ? order by insDate desc" ;
		list = this.findByHqlPage(hql, 1, 1, "insDate", taskId);
		if(list!=null&&!list.isEmpty()){
			endDate = (Date)list.get(0);
		}
		
		if(startDate!=null){
			return new String[]{StringUtils.formatShortDate(startDate),StringUtils.formatShortDate(endDate)};
		}
		return new String[]{"",""};
	}
	
	public String[] getTaskeExeCaseDateLimit(String taskId){
		String hql = "select exeDate  from TestResult where taskId = ? order by exeDate " ;
		//List list = this.findByHql(hql, 1, 1, "exeDate", taskId);
		List list = this.getHibernateGenericController().findByHql(hql, 1, 1, taskId);
		Date startDate = null;
		if(list!=null&&!list.isEmpty()){
			startDate = (Date)list.get(0);
		}
		Date endDate = null;
		hql = "select exeDate  from TestResult where taskId = ? order by exeDate desc" ;
		//list = this.findByHql(hql, 1, 1, "exeDate", taskId);
		list = this.getHibernateGenericController().findByHql(hql, 1, 1, taskId);
		if(list!=null&&!list.isEmpty()){
			endDate = (Date)list.get(0);
		}
		
		if(startDate!=null){
			return new String[]{StringUtils.formatShortDate(startDate),StringUtils.formatShortDate(endDate)};
		}
		return new String[]{"",""};		
	}
	
	public String[] getTaskeWriteCaseDateLimit(String taskId){
		String hql = "select creatdate  from TestCaseInfo where taskId = ? order by creatdate " ;
		//List list = this.findByHql(hql, 1, 1, "exeDate", taskId);
		List list = this.getHibernateGenericController().findByHql(hql, 1, 1, taskId);
		Date startDate = null;
		if(list!=null&&!list.isEmpty()){
			startDate = (Date)list.get(0);
		}
		Date endDate = null;
		hql = "select creatdate  from TestCaseInfo where taskId = ? order by creatdate desc" ;
		//list = this.findByHql(hql, 1, 1, "exeDate", taskId);
		list = this.getHibernateGenericController().findByHql(hql, 1, 1, taskId);
		if(list!=null&&!list.isEmpty()){
			endDate = (Date)list.get(0);
		}
		
		if(startDate!=null){
			return new String[]{StringUtils.formatShortDate(startDate),StringUtils.formatShortDate(endDate)};
		}
		return new String[]{"",""};			
		
	}

	/* (非 Javadoc)   
	* <p>Title: getAllProName</p>   
	* <p>Description: </p>   
	* @param dto   
	* @see cn.com.codes.singleTestTaskManager.service.SingleTestTaskService#getAllProName(cn.com.codes.singleTestTaskManager.dto.SingleTestTaskDto)   
	*/
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String,Object>> getAllProName(SingleTestTaskDto dto) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" SELECT ts.PRO_NAME AS proName,ts.TASKID as taskId FROM t_single_test_task ts ");
		List<Map<String,Object>> testTasks = this.findBySqlByJDBC(buffer.toString());
		return testTasks;
	}

}
