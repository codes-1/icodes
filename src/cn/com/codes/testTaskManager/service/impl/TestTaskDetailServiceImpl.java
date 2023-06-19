package cn.com.codes.testTaskManager.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.framework.app.services.BaseServiceImpl;
import cn.com.codes.framework.common.ListObject;
import cn.com.codes.framework.exception.DataBaseException;
import cn.com.codes.framework.hibernate.HibernateGenericController;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.object.BugHandHistory;
import cn.com.codes.object.OutlineInfo;
import cn.com.codes.object.SingleTestTask;
import cn.com.codes.object.SoftwareVersion;
import cn.com.codes.object.TaskUseActor;
import cn.com.codes.object.TestFlowInfo;
import cn.com.codes.object.TestTaskDetail;
import cn.com.codes.object.User;
import cn.com.codes.testTaskManager.dto.CurrTaskInfo;
import cn.com.codes.testTaskManager.dto.TestTaskManagerDto;
import cn.com.codes.testTaskManager.dto.VTestkTaskActor;
import cn.com.codes.testTaskManager.service.TestTaskDetailService;

public class TestTaskDetailServiceImpl extends BaseServiceImpl implements
		TestTaskDetailService {

	private static Logger logger = Logger.getLogger(TestTaskDetailServiceImpl.class);
	
	public List<SoftwareVersion> getSoftVerList(TestTaskManagerDto dto){
		StringBuffer hql = new StringBuffer();
		List<SoftwareVersion> softs = new ArrayList<SoftwareVersion>();
		hql.append("select new SoftwareVersion(versionId,versionNum,remark,seq,verStatus) ");
		hql.append(" from SoftwareVersion where taskid=? and verStatus!=2 order by upddate desc ");
		String taskId =  SecurityContextHolderHelp.getCurrTaksId();
		List<SoftwareVersion> list = this.findByHqlPage(hql.toString(), dto.getPageNo(), dto.getPageSize(), "versionId",  taskId);
		for (SoftwareVersion softwareVersion : list) {
			softwareVersion.setTaskid(taskId);
			softs.add(softwareVersion);
		}
		return list;
	}
	@SuppressWarnings("unchecked")
	public List<SoftwareVersion> getSoftVerListLoad(TestTaskManagerDto dto){
		StringBuffer hql = new StringBuffer();
		hql.append("select new SoftwareVersion(taskid,versionId,versionNum,remark,seq,verStatus,insdate,upddate) from SoftwareVersion ");
		String taskId =  SecurityContextHolderHelp.getCurrTaksId();
		HashMap<String,Object> hashMap = new HashMap<String, Object>();
		List<SoftwareVersion> list = this.findByHqlWithValuesMap(hql.toString(), dto.getPageNo(), dto.getPageSize(), "versionId", hashMap, false);
		return list;
	}
	public int getNextFlowCd(int currFlowCd){
		String hql = "select min(testFlowCode) as seq  from TestFlowInfo where taskId=? and testFlowCode >?" ;
		String nextFlowCd = this.findByHql(hql, SecurityContextHolderHelp.getCurrTaksId(),currFlowCd).get(0).toString();
		return Integer.parseInt(nextFlowCd);
	}
	public void updateInit(TestTaskManagerDto dto){
		boolean proChk = false;
		String taskId = dto.getTaskId();
		if(taskId==null){
			taskId = SecurityContextHolderHelp.getCurrTaksId();
			proChk = true;
		}else{
			SecurityContextHolderHelp.setCurrTaksId(taskId);
			String hql = "select new SingleTestTask(taskId,proName,proNum) from SingleTestTask where taskId=? and companyId=?";
			List<SingleTestTask> taskList = this.findByHql(hql, taskId,SecurityContextHolderHelp.getCompanyId());
			if(taskList==null||taskList.isEmpty()){
				throw new DataBaseException("非法提交的测试任务数据，不受理");
			}
			dto.setTaskName(taskList.get(0).getProName()); 
			proChk=true;
		}
		if(!proChk){
			throw new DataBaseException("该项目不可用或未确定计划");
		}
		if(taskId==null){
			throw new DataBaseException("企图绕过任务列表进入，不受理");
		}
		List<TestTaskDetail> taskList = this.findByHql("from TestTaskDetail where taskId=? and companyId=?",
				taskId,SecurityContextHolderHelp.getCompanyId());
		if(taskList==null||taskList.isEmpty()){
			throw new DataBaseException("非法提交的测试任务数据，不受理");
		}
		
		TestTaskDetail detail = taskList.get(0);
		dto.setDetail(detail);
		String relaTaskId = detail.getRelaTaskId() ;
		if(dto.getTaskName()==null){
			String qTaskHql = "select new SingleTestTask(taskId,proName,proNum) from SingleTestTask where taskId=? and companyId=?";
			List<SingleTestTask> list = this.findByHql(qTaskHql, taskId,SecurityContextHolderHelp.getCompanyId());
			dto.setTaskName(list.get(0).getProName());
		}
		dto.setDetail(detail);
		List<VTestkTaskActor> testActroList =this.getTestActor(taskId);
		dto.setTestActroList(testActroList);
		String hqlForTestFlow = "select new TestFlowInfo(testFlowCode) from TestFlowInfo where taskId=? order by testFlowCode" ;
		List<TestFlowInfo> list = (List<TestFlowInfo>)this.findByHql(hqlForTestFlow, taskId);
		dto.setTestFlowList(list);
	}

	private List<VTestkTaskActor> getTestActor(String taskId ){
		StringBuffer hql = new StringBuffer();
		hql.append("  select new cn.com.codes.testTaskManager.dto.VTestkTaskActor(");
		hql.append(" u.id as userId,(u.loginName||'('||u.name||')') as userName,ta.actor as actor )");
		hql.append(" from TaskUseActor ta join ta.user u where ta.taskId=? and u.delFlag=0 and u.status=1 ");
		return this.findByHql(hql.toString(), taskId);
	}

	public void update(TestTaskManagerDto dto){
		TestTaskDetail detail = dto.getDetail();
		dto.getDetail().setCompanyId(SecurityContextHolderHelp.getCompanyId());
		Set<TestFlowInfo> testFlow = detail.getTestFlow();
		Set<TestFlowInfo> oldTestFlow = detail.getOldTestFlow();
		//如测试己开始
//		if(dto.getDetail().getOutlineState()==1&&dto.getDetail().getTestTaskState()==0){
//			
//		}
		this.saveRealSet(oldTestFlow, testFlow, "testFlowId", null); 
		Set<TaskUseActor> useActor = detail.getUseActor();
		Set<TaskUseActor> oldUseActor = detail.getOldUseActor();
		String cacheRegion = "cn.com.codes.testTaskManager.dto.VTestkTaskActor" ;
		this.saveRealSet(oldUseActor, useActor, "actorId", cacheRegion);
		if(dto.getDetail().getTestTaskState()==3&&!"0".equals(dto.getTaskType())){
			dto.getDetail().setTestTaskState(0);
			this.updateTestTaskFlwFlg(detail.getTaskId());
		}else if(dto.getDetail().getTestTaskState()==3&&"0".equals(dto.getTaskType())){
			this.executeUpdateByHql("update SingleTestTask set status=0 where taskId=?", new Object[]{detail.getTaskId()});
		}
		if(dto.getDetail().getTestTaskState()==3){
			dto.getDetail().setTestTaskState(0);
		}
		this.update(detail);
		String outQueryHql = "from OutlineInfo where taskId=?";
		List outlineList = this.findByHql(outQueryHql, detail.getTaskId());
		if(outlineList==null||outlineList.size()==0){
			OutlineInfo outline = new OutlineInfo();
			outline.setModuleState(0);
			outline.setIsleafNode(1);
			outline.setModuleLevel(1);
			outline.setTaskId(detail.getTaskId());
			outline.setCompanyId(detail.getCompanyId());
			outline.setTestPhase(detail.getTestPhase());
			outline.setSuperModuleId(new Long(0));
			String hql = "select new SingleTestTask(taskId,proName,proNum) from SingleTestTask where taskId=? and companyId=?";
			List<SingleTestTask> taskList = this.findByHql(hql, detail.getTaskId(),detail.getCompanyId());
			outline.setModuleName(taskList.get(0).getProName());
			
			this.add(outline);	
		}
		
		if(dto.getAttr("bugAdjustMap")!=null){
			Map<String,Map> bugAdjustMap =(Map<String,Map>)dto.getAttr("bugAdjustMap");
			Iterator it = bugAdjustMap.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String, Map> me = (Map.Entry<String, Map>)it.next();
				this.executeUpdateByHqlWithValuesMap(me.getKey(), me.getValue());
			}
			bugAdjustMap = null;
			dto.removeAttr("bugAdjustMap");
			this.writeBugHistory(dto.getHistoryAdjustList());
			dto.setHistoryAdjustList(null);
		}
		oldUseActor = null;
	}
	
	private void writeBugHistory(List<BugHandHistory> historyAdjustList){
		if(historyAdjustList==null||historyAdjustList.isEmpty()){
			return;
		}
		String upFianl = "update BugHandHistory set currDayFinal=0 where bugId=? and insDate >=? and insDate < ?and currDayFinal=1";
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date now = calendar.getTime();
		for(BugHandHistory history:historyAdjustList){
			this.getHibernateGenericController().executeUpdate(upFianl.toString(), history.getBugId(),now,new Date());
			this.add(history);
		}
	}
	private void updateTestTaskFlwFlg(String taskId){
		
		String hql = "update Task set isSetFlw=1 where id=? ";
		this.getHibernateGenericController().executeUpdate(hql, taskId);
	}
	public String getCurrVer(String taskId,Long moduleId){
		//这里把testSeq也查出来是为了缓存测试轮次，执行用例时，需要这个值
		String hql = " select new TestTaskDetail(outlineState,testPhase,currentVersion,testSeq) from TestTaskDetail where taskId=?";
		TestTaskDetail taskDetal = (TestTaskDetail)this.findByHql(hql, taskId).get(0);
		if(taskDetal.getTestPhase()==2||taskDetal.getOutlineState()==0){
			return taskDetal.getCurrentVersion();
		}
		Long parentId = getTopParentId(moduleId);
		return this.get(OutlineInfo.class, parentId).getCurrVer();
	}

	
	public List<User> getTeamMembByActCd(Integer actorCd){
		

		String hql = "select new User(u.id,u.name ,u.loginName) from TaskUseActor ua join ua .user u where ua.taskId=? and ua.isEnable = 1  and u.delFlag=0 and u.status=1 order by actor";
		if(actorCd!=null&&actorCd>0){
			if(actorCd==2||actorCd==1||actorCd==8){
				if(actorCd==1||actorCd==8){
					hql = "select new User(u.id,u.name ,u.loginName) from TaskUseActor ua join ua .user u where ua.taskId=? and ua.actor=1 and ua.isEnable = 1 and u.delFlag=0 and u.status=1 order by actor";
				}else{
					hql = "select new User(u.id,u.name ,u.loginName) from TaskUseActor ua join ua .user u where ua.taskId=? and ua.actor=2 and ua.isEnable = 1 and u.delFlag=0 and u.status=1 order by actor";
				}
				return this.findByHql(hql, SecurityContextHolderHelp.getCurrTaksId());	
			}else{
				hql = "select new User(u.id,u.name ,u.loginName) from TaskUseActor ua join ua .user u where ua.taskId=? and ua.actor=? and ua.isEnable = 1 and u.delFlag=0 and u.status=1 order by actor";
				return this.findByHql(hql, SecurityContextHolderHelp.getCurrTaksId(),actorCd);
			}
		}
		return this.findByHql(hql, SecurityContextHolderHelp.getCurrTaksId());	
	}
	public List<TestFlowInfo> getCurrTestFlow(){
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		String  hql= "from TestFlowInfo where  taskId=? order by testFlowCode";
		List<TestFlowInfo> testFlow = this.findByHql(hql, taskId);
		return testFlow;
	}

	public CurrTaskInfo getCurrTaskInfo(){
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		String  hql= "from TestFlowInfo where  taskId=? order by testFlowCode";
		List<TestFlowInfo> testFlow = this.findByHql(hql, taskId);
		CurrTaskInfo currTaskInfo = new CurrTaskInfo();
		currTaskInfo.setFlowList(testFlow);
		StringBuffer flow = new StringBuffer();
		StringBuffer role = new StringBuffer();
		String userId = SecurityContextHolderHelp.getUserId();
		hql = "select ta from TaskUseActor ta  join ta.user u where ta.taskId=?   and ta.isEnable = 1 and u.delFlag=0 and u.status=1 and u.id=? order by actor";
		//hql = "from TaskUseActor where taskId=? and userId=? and isEnable = 1 order by actor";
		List<TaskUseActor> useActor = this.findByHql(hql,taskId,userId);;
		for(TestFlowInfo flw:testFlow){
			flow.append(flw.getTestFlowCode()).append(" ");
		}
		//TestFlowInfo flowObj = new TestFlowInfo();
		//flowObj.setTaskId(taskId);
		currTaskInfo.setActorList(useActor);
		//增加isEnable 后注掉下面的 
//		List<TaskUseActor> myActorList = new ArrayList<TaskUseActor>();
//		currTaskInfo.setActorList(myActorList);
//		//只有流程启用角色才算，
//		for(TaskUseActor actor :useActor){
//			flowObj.setTestFlowCode(actor.getActor());
//			if(testFlow.contains(flowObj)){
//				role.append(actor.getActor()).append(" ");
//				myActorList.add(actor);
//			}
//		}
		//只有流程启用角色才算，
		for(TaskUseActor actor :useActor){
				role.append(actor.getActor()).append(" ");
		}
		currTaskInfo.setTestFlow(flow.toString().trim());
		currTaskInfo.setRoleInTask(role.toString().trim());
		hql = " select new TestTaskDetail(outlineState,testPhase,currentVersion,testSeq,reltCaseFlag) from TestTaskDetail where taskId=?";
		List<TestTaskDetail> taskList = this.findByHql(hql, taskId);
		if(taskList==null||taskList.isEmpty()){
			SecurityContextHolder.getContext().getVisit().setTaskId(null);
			throw new DataBaseException(" 测试任务不存在，不受理");
		}
		TestTaskDetail taskDetal = taskList.get(0);
		//currTaskInfo.setCurrentVersion(taskDetal.getCurrentVersion());
		currTaskInfo.setOutLineState(taskDetal.getOutlineState());
		//currTaskInfo.setTestSeq(taskDetal.getTestSeq());
		currTaskInfo.setTestPhase(taskDetal.getTestPhase());
		currTaskInfo.setRelCaseSwitch(taskDetal.getReltCaseFlag());
		return currTaskInfo;
	}
	public CurrTaskInfo getCurrTaskDetalInfo(){
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		String  hql= "from TestFlowInfo where  taskId=? order by testFlowCode";
		List<TestFlowInfo> testFlow = this.findByHql(hql, taskId);
		CurrTaskInfo currTaskInfo = new CurrTaskInfo();
		currTaskInfo.setFlowList(testFlow);
		StringBuffer flow = new StringBuffer();
		StringBuffer role = new StringBuffer();
		hql = "select ta from TaskUseActor ta  join ta.user u where ta.taskId=?   and ta.isEnable = 1 and u.delFlag=0 and u.status=1  order by actor";
		//from TaskUseActor ta join ta.user u where ta.taskId=? and u.delFlag=0 and u.status=1
		List<TaskUseActor> useActor = this.findByHql(hql,taskId);
		for(TestFlowInfo flw:testFlow){
			flow.append(flw.getTestFlowCode()).append(" ");
		}
		TestFlowInfo flowObj = new TestFlowInfo();
		flowObj.setTaskId(taskId);
		//List<TaskUseActor> myActorList = new ArrayList<TaskUseActor>();
		currTaskInfo.setActorList(useActor);
		//只有流程启用角色才算，
		StringBuffer devIdStrb = new StringBuffer();
		StringBuffer testIdStrb = new StringBuffer();
		StringBuffer analysIdStrb = new StringBuffer();
		StringBuffer assinIdStrb = new StringBuffer();
		StringBuffer intercsIdStrb = new StringBuffer();
		StringBuffer devChkIdStrb = new StringBuffer();
		StringBuffer testChkIdStrb = new StringBuffer();
		StringBuffer testLdIdStrb = new StringBuffer();
		String userId = SecurityContextHolderHelp.getUserId();
		for(TaskUseActor actor :useActor){
			//flowObj.setTestFlowCode(actor.getActor());
			//if(testFlow.contains(flowObj)){
				if(actor.getActor()==1){
					testIdStrb.append(",").append(actor.getUserId());
				}else if(actor.getActor()==2){
					testChkIdStrb.append(",").append(actor.getUserId());
				}else if(actor.getActor()==3){
					analysIdStrb.append(",").append(actor.getUserId());
				}else if(actor.getActor()==4){
					assinIdStrb.append(",").append(actor.getUserId());
				}else if(actor.getActor()==5){
					devIdStrb.append(",").append(actor.getUserId());
				}else if(actor.getActor()==6){
					devChkIdStrb.append(",").append(actor.getUserId());
				}else if(actor.getActor()==7){
					intercsIdStrb.append(",").append(actor.getUserId());
				}else if(actor.getActor()==8){
					testLdIdStrb.append(",").append(actor.getUserId());
				}
				if(actor.getUserId().equals(userId)){
					//myActorList.add(actor);
					role.append(actor.getActor()).append(" ");
				}
			//}
		}
		currTaskInfo.setTestFlow(flow.toString().trim());
		currTaskInfo.setRoleInTask(role.toString().trim());
		
		currTaskInfo.setTestIdStr(testIdStrb.length()>2?testIdStrb.substring(1).toString():"");
		currTaskInfo.setTestChkIdStr(testChkIdStrb.length()>2?testChkIdStrb.substring(1).toString():"");
		currTaskInfo.setAnalysIdStr(analysIdStrb.length()>2?analysIdStrb.substring(1).toString():"");
		currTaskInfo.setAssinIdStr(assinIdStrb.length()>2?assinIdStrb.substring(1).toString():"");
		currTaskInfo.setDevIdStr(devIdStrb.length()>2?devIdStrb.substring(1).toString():null);
		currTaskInfo.setDevChkIdStr(devChkIdStrb.length()>2?devChkIdStrb.substring(1).toString():"");
		currTaskInfo.setIntercsIdStr(intercsIdStrb.length()>2?intercsIdStrb.substring(1).toString():"");
		currTaskInfo.setTestLdIdStr(testLdIdStrb.length()>2?testLdIdStrb.substring(1).toString():"");
		
		hql = " select new TestTaskDetail(outlineState,testPhase,currentVersion,testSeq,reltCaseFlag) from TestTaskDetail where taskId=?";
		List<TestTaskDetail> list = this.findByHql(hql, taskId);
		if(list==null||list.isEmpty()){
			throw new DataBaseException("非法提交的测试任务数据，不受理");
		}
		TestTaskDetail taskDetal = list.get(0);
		//currTaskInfo.setCurrentVersion(taskDetal.getCurrentVersion());
		//currTaskInfo.setTestSeq(taskDetal.getTestSeq());
		currTaskInfo.setTestPhase(taskDetal.getTestPhase());
		return currTaskInfo;
	}
	
	public Map getCurrTestSeqAndVer(String taskId,Long moduleId){
		//这里把testSeq也查出来是为了缓存测试轮次，执行用例时，需要这个值
		Map rest = new HashMap(2);
		String hql = " select new TestTaskDetail(outlineState,testPhase,currentVersion,testSeq) from TestTaskDetail where taskId=?";
		TestTaskDetail taskDetal = (TestTaskDetail)this.findByHql(hql, taskId).get(0);
		if(taskDetal.getTestPhase()==2||taskDetal.getOutlineState()==0){
			 rest.put("testSeq", taskDetal.getTestSeq());
			 rest.put("currVer", taskDetal.getCurrentVersion());
		}else{
			Long parentId = getTopParentId(moduleId);
			rest.put("currVer", get(OutlineInfo.class, parentId).getCurrVer());	
			hql = "select max(seq) as seq  from TestResult where moduleId=? and taskId=?" ;
			List list = this.findByHql(hql, parentId,taskId);
			String testSeq =null;
			if(list!=null&&!list.isEmpty()&&list.get(0)!=null){
				testSeq = list.get(0).toString();
			}
			if("0".equals(testSeq)||testSeq==null){
				testSeq="1";
			}
			rest.put("testSeq", Integer.valueOf(testSeq));
		}
		return rest;
	}
	//查询二级模块“父”模块ID
	private Long getTopParentId(Long moduleId){
		String hql = "select new OutlineInfo(moduleId,superModuleId,moduleLevel) from OutlineInfo where moduleId=?" ;
		OutlineInfo outLine = (OutlineInfo)this.findByHql(hql, moduleId).get(0);
		if(outLine.getModuleLevel()!=2){
			return getTopParentId(outLine.getSuperModuleId());
		}else{
			return outLine.getModuleId();
		}
	}
	public String createVerCheck(TestTaskManagerDto dto){

		return null;
	}
	
	private String checkCaseState(TestTaskManagerDto dto){
		
		return "";
	}
	
	private String checkBugState(TestTaskManagerDto dto){
		
		return "";
	}
	
	private String buildCheckBugHql(TestTaskManagerDto dto){
		String moduleIds = dto.getModuleIds();
		//系统测试,当为all时，是集成测试，但所有模块都要校验
		if(moduleIds==null||"".equals(moduleIds.trim())||"all".equals(moduleIds)){
			
			
		}else{ //集成测试 reltTestCaseFlag
			
		}
		return "";
	}
	
	private String buildCheckCaseHql(TestTaskManagerDto dto){
		String moduleIds = dto.getModuleIds();
		//系统测试,当为all时，是集成测试，但所有模块都要校验
		if(moduleIds==null||"".equals(moduleIds.trim())||"all".equals(moduleIds)){
			
			
		}else{ //集成测试 reltTestCaseFlag
			
		}
		return "";
	}
	
	public void saveTestTaskDet(String taskId,Integer testPhase,String projectId){
		TestTaskDetail testTask = new TestTaskDetail();
		testTask.setTaskId(taskId);
		testTask.setTestPhase(testPhase);
		testTask.setProjectId(projectId);
		testTask.setCompanyId(SecurityContextHolderHelp.getCompanyId());
		testTask.setInsdate(new Date());
		testTask.setCreateId(SecurityContextHolderHelp.getUserId());
		testTask.setTaskState(3);
		testTask.setOutlineState(0);
		testTask.setReltCaseFlag(0);
		testTask.setCustomBug(0);
		testTask.setCustomCase(0);
		testTask.setUpgradeFlag(0);
		this.add(testTask);
	}
	public String softVerRepChk(SoftwareVersion softVer){
		StringBuffer hql = new StringBuffer();
		hql.append("select count(versionNum)");
		hql.append(" from SoftwareVersion");
		hql.append(" where taskid=? and versionNum=?");
		hql.append(" and versionId !=?");
		final String countHql= hql.toString();
		final String taskId = SecurityContextHolderHelp.getCurrTaksId();
		final String versionNum = softVer.getVersionNum();
		final Integer seq = softVer.getSeq();
		final Long versionId = softVer.getVersionId()==null?0:softVer.getVersionId();
		StringBuffer rest = new StringBuffer();
		List countlist = (List)this.getHibernateGenericController().getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)throws HibernateException {
				Query queryObject = session.createQuery(countHql).setCacheable(true);
				queryObject.setParameter(0, taskId);
				queryObject.setParameter(1, versionNum);
				queryObject.setParameter(2, versionId);
				return queryObject.list();
			}
		}, true);
		int count = HibernateGenericController.toLong(countlist.get(0)).intValue();
		if(count>0){
			rest.append("numRep");
		}
		final String countHql2 = countHql.replace("versionNum", "seq");
		countlist = (List)this.getHibernateGenericController().getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)throws HibernateException {
				Query queryObject = session.createQuery(countHql2).setCacheable(true);
				queryObject.setParameter(0, taskId);
				queryObject.setParameter(1, seq);
				queryObject.setParameter(2, versionId);
				return queryObject.list();
			}
		}, true);
		count = HibernateGenericController.toLong(countlist.get(0)).intValue();
		if(count>0){
			rest.append("seqRep");
		}
		return rest.toString();
	}
	
	public List<ListObject> getVerSelList(){
		StringBuffer hql = new StringBuffer();
		hql.append("select new cn.com.codes.framework.common.ListObject(");
		hql.append(" versionId as keyObj ,versionNum as valueObj ) from SoftwareVersion ")
		.append(" where taskid=? and verStatus=0 order by seq desc ");
		List<ListObject> listDates  = this.findByHql(hql.toString(), SecurityContextHolderHelp.getCurrTaksId());
		return listDates;
	}
	
	public SingleTestTask getProNameAndPmName(String taskId){
		try {
			String hql = " from SingleTestTask t join fetch t.psm  where t.taskId=? and t.companyId=?";
			List list = this.findByHql(hql, taskId,SecurityContextHolderHelp.getCompanyId());
			if(list!=null&&!list.isEmpty()){
				SingleTestTask task = (SingleTestTask)list.get(0);
				task.setPsmName(task.getPsm().getName());
				return task;
			}else{
				StringBuilder sb = new StringBuilder();
				sb.append("select p.name as proName, u.name psmName, ts.name");
				sb.append("  from t_test_task_detail t ");
				sb.append("inner join T_TASK ts on t.taskid = ts.versionid ");
				sb.append(" inner join T_project p on p.id = ts.projectid ");
				sb.append(" inner join T_user u on p.principal = u.id ");
				sb.append(" where t.taskid <> t.projectid");
				sb.append(" and p.status = 1 ");
				sb.append(" and t.taskid = ?");
				List<?> resultSet = this.findBySql(sb.toString(), null, taskId);
				if (resultSet == null || resultSet.isEmpty()) {
					return null;
				}
				Iterator it = resultSet.iterator();
				SingleTestTask task  = new SingleTestTask();
				while (it.hasNext()) {
					Object values[] = (Object[]) it.next();
					task.setProName(values[0].toString()+"/"+values[2].toString());
					task.setPsmName(values[1].toString());
				}
				return task;
			}
			
		} catch (RuntimeException e) {
			logger.error(e);
			//e.printStackTrace();
		}
		return null;
	}
}
