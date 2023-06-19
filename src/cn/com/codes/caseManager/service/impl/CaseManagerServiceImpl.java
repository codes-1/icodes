package cn.com.codes.caseManager.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import cn.com.codes.caseManager.dto.CaseManagerDto;
import cn.com.codes.caseManager.dto.TestCaseVo;
import cn.com.codes.caseManager.service.CaseManagerService;
import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.util.FileInfoVo;
import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.app.services.BaseServiceImpl;
import cn.com.codes.framework.common.config.PropertiesBean;
import cn.com.codes.framework.common.util.Context;
import cn.com.codes.framework.security.VisitUser;
import cn.com.codes.object.CaseExeHistory;
import cn.com.codes.object.CasePri;
import cn.com.codes.object.CaseType;
import cn.com.codes.object.FileInfo;
import cn.com.codes.object.OutlineInfo;
import cn.com.codes.object.TestCaseInfo;
import cn.com.codes.object.TestResult;
import cn.com.codes.object.TestTaskDetail;
import cn.com.codes.testTaskManager.service.TestTaskDetailService;
import cn.com.codes.caseManager.service.impl.CaseManagerServiceImpl;

public class CaseManagerServiceImpl extends BaseServiceImpl implements
		CaseManagerService {
	private TestTaskDetailService testTaskService ;
	private static Logger loger = Logger.getLogger(CaseManagerServiceImpl.class);


	public void copyCase(CaseManagerDto dto){
		List<TestCaseInfo> addCaseList = (List<TestCaseInfo>)dto.getAttr("addCaseList");
		List<CaseExeHistory> exeHistoryList = (List<CaseExeHistory>)dto.getAttr("exeHistoryList");
		for(int i=0; i<addCaseList.size(); i++){
			TestCaseInfo tc = addCaseList.get(i);
			this.add(tc);
			CaseExeHistory his = exeHistoryList.get(i);
			his.setTestCaseId(tc.getTestCaseId());
			this.add(his);
		}
		addCaseList = null;
		exeHistoryList = null;
	}
	
	public void pasteCase(CaseManagerDto dto){
		List<TestCaseInfo> addCaseList = (List<TestCaseInfo>)dto.getAttr("addCaseList");
		List<CaseExeHistory> exeHistoryList = (List<CaseExeHistory>)dto.getAttr("exeHistoryList");
		for(int i=0; i<addCaseList.size(); i++){
			TestCaseInfo tc = addCaseList.get(i);
			this.add(tc);
			CaseExeHistory his = exeHistoryList.get(i);
			his.setTestCaseId(tc.getTestCaseId());
			this.add(his);
		}
		if(dto.getAttr("delCaseIds")!=null){
			String delHql = "delete from TestCaseInfo where testCaseId in(:ids) and taskId= :taskId";
			Map praValues = new HashMap(2);
			praValues.put("ids", dto.getAttr("delCaseIds"));
			praValues.put("taskId", SecurityContextHolderHelp.getCurrTaksId());
			this.getHibernateGenericController().excuteBatchHql(delHql, praValues);
			praValues = null;			
		}

	}
	public List loadCaseBoard(String taskId){
		PropertiesBean conf = (PropertiesBean) Context.getInstance().getBean("ContextProperties");
		if(conf.getProperty("config.db.driver")!=null&&conf.getProperty("config.db.driver").indexOf("mysql")>0){
			return this.loadCaseBoard4Mysql(taskId);
		}else{
			return this.loadCaseBoard4Oracle(taskId);
		}
	}
	private List loadCaseBoard4Oracle(String taskId){
		StringBuffer caseBoardSql = new StringBuffer();
		caseBoardSql.append("select teamMember.tm, ");
		caseBoardSql.append("ahCountTable.ahCount, ");
		caseBoardSql.append("hCountTable.hCount , bwhandCountTable.bwhCount,bhandCountTable.bhCount, teamMember.loginname ");
		caseBoardSql.append("from (select distinct (u.loginname || '(' || u.name || ')') as tm  ,u.loginname ");
		caseBoardSql.append("from T_TASK_USEACTOR ua ");
		caseBoardSql.append("inner join t_user u on ua.userid = u.id ");
		caseBoardSql.append("where ua.is_enable = 1 and u.DELFLAG=0 ");
		caseBoardSql.append("and ua.actor in (1, 2, 8, 9) ");
		caseBoardSql.append("and ua.taskid =:taskId) teamMember ");
		caseBoardSql.append("left join (select (u.loginname || '(' || u.name || ')') as owner, ");
		caseBoardSql.append("count(u.loginname) as ahCount ");
		caseBoardSql.append("from t_exe_history h ");
		caseBoardSql.append("inner join t_user u on u.id = h.testactor ");
		caseBoardSql.append("where h.taskid =:taskId ");
		caseBoardSql.append("group by u.loginname, u.name) ahCountTable on teamMember.tm = ");
		caseBoardSql.append("ahCountTable.owner ");
		caseBoardSql.append("left join (select (u.loginname || '(' || u.name || ')') as owner, ");
		caseBoardSql.append(" count(u.loginname) as hCount ");
		caseBoardSql.append("from t_exe_history h ");
		caseBoardSql.append("inner join t_user u on u.id = h.testactor ");
		caseBoardSql.append("where h.taskid =:taskId ");
		caseBoardSql.append("and to_char(h.insdate, 'yyyy-mm-dd') =:currData  ");
		caseBoardSql.append("group by u.loginname, u.name) hCountTable " +
				"on teamMember.tm =hCountTable.owner ");
		caseBoardSql.append("left join (select (u.loginname || '(' || u.name || ')') as owner, ");
		caseBoardSql.append("count(*) as bwhCount ");
		caseBoardSql.append("from t_bugbaseinfo b ");
		caseBoardSql.append("inner join t_user u on u.id = b.bug_num ");
		caseBoardSql.append("where b.current_state not in (2,4, 5, 14, 15, 22, 23) ");
		caseBoardSql.append("and b.task_id =:taskId ");
		caseBoardSql.append("group by u.loginname, u.name) bwhandCountTable on teamMember.tm = ");
		caseBoardSql.append(" bwhandCountTable.owner ");
		caseBoardSql.append("left join (select (u.loginname || '(' || u.name || ')') as owner, ");
		caseBoardSql.append("count(*) as bhCount ");
		caseBoardSql.append("from t_bughandhistory h ");
		caseBoardSql.append("inner join t_user u on u.id = h.handler ");
		caseBoardSql.append("where h.task_id =:taskId ");
		caseBoardSql.append("and to_char(h.insdate, 'yyyy-mm-dd') =:currData ");
		caseBoardSql.append("group by u.loginname, u.name) bhandCountTable" +
				" on teamMember.tm = bhandCountTable.owner ");
		Map praValues = new HashMap();
		praValues.put("taskId", SecurityContextHolderHelp.getCurrTaksId());
		praValues.put("currData", StringUtils.formatShortDate(new Date()));
		List list = this.findBySqlWithValuesMap(caseBoardSql.toString(), null, praValues);
		caseBoardSql = null;
		praValues = null;
		return list;
	}
	private List loadCaseBoard4Mysql(String taskId){
		StringBuffer caseBoardSql = new StringBuffer();
		caseBoardSql.append("select teamMember.useName, ");
		caseBoardSql.append("ahCountTable.ahCount, ");
		caseBoardSql.append("hCountTable.hCount , bwhandCountTable.bwhCount,bhandCountTable.bhCount, teamMember.loginname ");
		//caseBoardSql.append("from (select distinct u.name  as tm  ,u.loginname ");
		caseBoardSql.append("from (select distinct concat(concat(u.loginname,'('),concat(u.name,')')) as tm  ,u.loginname , u.name as useName ");
		caseBoardSql.append("from T_TASK_USEACTOR ua ");
		caseBoardSql.append("inner join t_user u on ua.userid = u.id ");
		caseBoardSql.append("where ua.is_enable = 1 and u.DELFLAG=0 ");
		caseBoardSql.append("and ua.actor in (1, 2, 8, 9) ");
		caseBoardSql.append("and ua.taskid =:taskId) teamMember ");
		caseBoardSql.append("left join (select distinct concat(concat(u.loginname,'('),concat(u.name,')')) as owner, ");
		caseBoardSql.append("count(u.loginname) as ahCount ");
		caseBoardSql.append("from t_exe_history h ");
		caseBoardSql.append("inner join t_user u on u.id = h.testactor ");
		caseBoardSql.append("where h.taskid =:taskId ");
		caseBoardSql.append("group by u.loginname, u.name) ahCountTable on teamMember.tm = ");
		caseBoardSql.append("ahCountTable.owner ");
		caseBoardSql.append("left join (select distinct concat(concat(u.loginname,'('),concat(u.name,')')) as owner, ");
		caseBoardSql.append(" count(u.loginname) as hCount ");
		caseBoardSql.append("from t_exe_history h ");
		caseBoardSql.append("inner join t_user u on u.id = h.testactor ");
		caseBoardSql.append("where h.taskid =:taskId ");
		caseBoardSql.append("and date_format(h.insdate, '%Y-%c-%d') =date('"+StringUtils.formatShortDate(new Date())+"')   ");
		caseBoardSql.append("group by u.loginname, u.name) hCountTable " +
				"on teamMember.tm =hCountTable.owner ");
		caseBoardSql.append("left join (select distinct  concat(concat(u.loginname,'('),concat(u.name,')')) as owner, ");
		caseBoardSql.append("count(*) as bwhCount ");
		caseBoardSql.append("from t_bugbaseinfo b ");
		caseBoardSql.append("inner join t_user u on u.id = b.bug_num ");
		caseBoardSql.append("where b.current_state not in (2,4, 5, 14, 15, 22, 23) ");
		caseBoardSql.append("and b.task_id =:taskId ");
		caseBoardSql.append("group by u.loginname, u.name) bwhandCountTable on teamMember.tm = ");
		caseBoardSql.append(" bwhandCountTable.owner ");
		caseBoardSql.append("left join (select distinct concat(concat(u.loginname,'('),concat(u.name,')')) as owner, ");
		caseBoardSql.append("count(*) as bhCount ");
		caseBoardSql.append("from t_bughandhistory h ");
		caseBoardSql.append("inner join t_user u on u.id = h.handler ");
		caseBoardSql.append("where h.task_id =:taskId ");
		caseBoardSql.append("and date_format(h.insdate, '%Y-%c-%d') =date('"+StringUtils.formatShortDate(new Date())+"')  ");
		caseBoardSql.append("group by u.loginname, u.name) bhandCountTable" +
				" on teamMember.tm = bhandCountTable.owner ");
		Map praValues = new HashMap();
		praValues.put("taskId", SecurityContextHolderHelp.getCurrTaksId());
		//praValues.put("currData", StringUtils.formatShortDate(new Date()));
		List list = this.findBySqlWithValuesMap(caseBoardSql.toString(), null, praValues);
		caseBoardSql = null;
		praValues = null;
		return list;
	}
	
	public List<CaseExeHistory> loadHistory(CaseManagerDto dto){
		StringBuffer hql = new StringBuffer();
		hql.append("select new CaseExeHistory(resultId, testVer, testResult,testActor,exeDate,remark, operaType)");
		hql.append(" from CaseExeHistory where testCaseId=? and taskId=? order by exeDate desc ");
		return this.findByHqlPage(hql.toString(), dto.getPageNo(), dto.getPageSize(), "resultId", dto.getTestCaseInfo().getTestCaseId(),dto.getTaskId());
	}
	public void batchAudit(CaseManagerDto dto){
		this.batchAuditPrepare(dto);
		this.excuteBatchHql(dto.getHql(), dto.getHqlParamMaps());
		List<CaseExeHistory> hisList = (List<CaseExeHistory>)dto.getAttr("hisList");
		for(CaseExeHistory caseHis :hisList){
			this.add(caseHis);
		}
	}
	
	private void batchAuditPrepare(CaseManagerDto dto){
		
		String[] caseIdsArr = dto.getRemark().trim().split("，");
		List<Long> caseIdList = new ArrayList<Long>(caseIdsArr.length);
		for(String id :caseIdsArr ){
			caseIdList.add(Long.parseLong(id));
		}
		String upHql = "update TestCaseInfo set testStatus=:testStatus ,auditId=:auditId ,upddate =:upddate where testCaseId in(:ids) and taskId =:taskId";
		dto.setHql(upHql);
		Map praValues = new HashMap(5);
		String currUserId = SecurityContextHolderHelp.getUserId();
		praValues.put("testStatus", Integer.parseInt(dto.getCommand()));
		praValues.put("auditId", currUserId);
		Date exeDate = new Date();
		praValues.put("upddate", exeDate);
		praValues.put("ids",caseIdList);
		praValues.put("taskId",dto.getTaskId());
		dto.setHqlParamMaps(praValues);
		List<CaseExeHistory> hisList = new ArrayList<CaseExeHistory>(caseIdsArr.length);
		for(Long caseId :caseIdList){
			CaseExeHistory his = new CaseExeHistory();
			his.setTaskId(dto.getTaskId());
			his.setTestCaseId(caseId);
			his.setExeDate(exeDate);
			his.setOperaType(3);
			his.setTestActor(currUserId);
			his.setTestResult(Integer.parseInt(dto.getCommand()));
			his.setModuleId(dto.getCurrNodeId());
			his.setRemark("批量审批");
			hisList.add(his);
		}
		dto.setAttr("hisList", hisList);
	}

	
	public TestCaseInfo quickQueryCase(Long CaseId,Long moduleId,String taskId){
		StringBuffer hql = new StringBuffer();
		hql.append("select new TestCaseInfo(testCaseId,prefixCondition,testCaseDes,");
		hql.append("isReleased,testStatus,weight,createrId,");
		hql.append("attachUrl,auditId,caseTypeId,priId,moduleId,taskId,creatdate)");
		hql.append(" from TestCaseInfo  where testCaseId=?"); 
		if(moduleId!=null){
			hql.append(" and moduleId="+moduleId);
		}
		if(!StringUtils.isNullOrEmpty(taskId)){
			hql.append(" and taskId='"+taskId+"'");
		}
		List<TestCaseInfo> list = this.findByHql(hql.toString(), CaseId);
		if(list!=null&&!list.isEmpty()){
			StringBuffer taskHql = new StringBuffer("select new TestTaskDetail(outlineState,testPhase,")
			.append("currentVersion,testSeq,reltCaseFlag) from TestTaskDetail where taskId=? and companyId=?");
			String compId = SecurityContextHolderHelp.getCompanyId();
			List<TestTaskDetail> taskList = this.findByHql(taskHql.toString(), list.get(0).getTaskId(),compId);
			if(taskList==null||taskList.isEmpty()){
				return null;
			}else{
				return list.get(0);
			}
		}
		return null ;
	}
	public List loadAuditCase(CaseManagerDto dto,String moduleNum){
		this.buildLoadAuditCaseHql(dto, moduleNum);
//		dto.setCanReview(0);
//		if(!"true".equals(dto.getIsAjax())&&this.isCanReview(dto.getTaskId())){
//			dto.setCanReview(1);
//		}
//		dto.setIsTestLeader(0);
//		//首次进入用例列表时，不调用AJAX时查询，
//		if(!"true".equals(dto.getIsAjax())&&this.isTestLeader(dto.getTaskId())){
//			dto.setIsTestLeader(1);
//		}
		return this.findByHqlWithValuesMap(dto.getHql(), dto.getPageNo(), dto.getPageSize(), "testCaseId", dto.getHqlParamMaps(), false);
	}
	
	public List loadCase(CaseManagerDto dto,String moduleNum){
		this.buildLoadCaseHql(dto, moduleNum);
		dto.setCanReview(0);
		if(!"true".equals(dto.getIsAjax())&&this.isCanReview(dto.getTaskId())){
			dto.setCanReview(1);
		}
		dto.setIsTestLeader(0);
		//首次进入用例列表时，不调用AJAX时查询，
		if(!"true".equals(dto.getIsAjax())&&this.isTestLeader(dto.getTaskId())){
			dto.setIsTestLeader(1);
		}
		return this.findByHqlWithValuesMap(dto.getHql(), dto.getPageNo(), dto.getPageSize(), "testCaseId", dto.getHqlParamMaps(), false);
	}
    private boolean isCanReview(String taskId){
    	String hql = "select new TaskUseActor(actor) from TaskUseActor where taskId=? and userId=? and actor=?";
    	List list = this.findByHql(hql, taskId,SecurityContextHolderHelp.getUserId(),9);
    	if(list.size()>0){
    		return true;
    	}
    	return false;
    }
    private boolean isTestLeader(String taskId){
    	String hql = "select new TaskUseActor(actor) from TaskUseActor where taskId=? and userId=? and actor=?";
    	List list = this.findByHql(hql, taskId,SecurityContextHolderHelp.getUserId(),8);
    	if(list.size()>0){
    		return true;
    	}   	
    	return false;
    }
	public boolean isCanDel(Long caseId){
		String hql ="from TestResult where testCaseId=?" ;
		Object[] prav =  new Object[]{caseId};
		int count = this.getHibernateGenericController().getResultCount(hql, prav, "resultId").intValue();
		return count==0?true:false;
	}
	
	public void delCase(Long caseId){
		String hql = "delete from TestCaseInfo where testCaseId=? and taskId=?";
		this.getHibernateGenericController().executeUpdate(hql, caseId,SecurityContextHolderHelp.getCurrTaksId());
		//同时删除用例文件记录。重新添加文件信息
		hql = "delete from FileInfo where typeId=?";
		this.getHibernateGenericController().executeUpdate(hql,caseId);
	}
	@SuppressWarnings("unchecked")
	public void exeCase(CaseManagerDto dto) {
		this.exePerpare(dto);
		Integer testResult = dto.getTestCaseInfo().getTestStatus();
		//只有不是审核时，才写测试记录
		Long testCaseId = dto.getTestCaseInfo().getTestCaseId();
		if(testResult!=1&&testResult!=6){
			//先查询同一版本有无测试结果，如有则执行update操作
			String taskId= dto.getTaskId();
			Long currVer= dto.getRest().getTestVer();
			String hql ="select new TestResult(resultId) from TestResult where testCaseId=? and testVer=? and taskId=?";
			List<TestResult> list = this.findByHql(hql, testCaseId,currVer,taskId);
			if(list==null||list.size()==0){
				this.add(dto.getRest());
			}else{
				TestResult restOld = list.get(0);
				Long restId = restOld.getResultId();
				TestResult rest = dto.getRest();
				rest.setResultId(restId);
				this.update(rest);
				//可能此前因并发多出一条测试记录来，所以要删掉
				if(list.size()>1){
					hql="delete from TestResult where testCaseId=? and testVer=? and taskId=? and resultId<>? ";
					this.getHibernateGenericController().executeUpdate(hql, testCaseId,currVer,taskId,restId);
				}
			}

		}
//		else if(testResult==1||testResult==6){
//			this.executeUpdateByHqlWithValuesMap(dto.getHql(), dto.getHqlParamMaps());
//			//写执行历史 统计工作量时,从执行历史查看
//			this.add(dto.getRest().convert2ExeHistory());
//		}
		//写执行历史 统计工作量时,从执行历史查看
		this.add(dto.getRest().convert2ExeHistory());
		this.executeUpdateByHqlWithValuesMap(dto.getHql(), dto.getHqlParamMaps());
//		//先删除用例文件记录。重新添加文件信息
//		String hql = "delete from FileInfo where typeId=?";
//		this.getHibernateGenericController().executeUpdate(hql, testCaseId);
//		
		List<FileInfoVo> fileInfos = dto.getFileInfos();
		List<FileInfoVo> filesInfo = null;
		if(fileInfos!=null && fileInfos.size()>0){
			JSONArray json = JSONArray.fromObject(fileInfos.toString().replace("[[", "[").replace("]]", "]]"));
			filesInfo= (List<FileInfoVo>)JSONArray.toCollection(json, FileInfoVo.class);
			for (FileInfoVo fileInfoVo : filesInfo) {
				FileInfo fileInfo = new FileInfo();
				fileInfo.setType("case");
				fileInfo.setTypeId(testCaseId);
				fileInfo.setRelativeName(fileInfoVo.getFileOriginalName());
				fileInfo.setFilePath(fileInfoVo.getFileUrl());
				this.add(fileInfo);
			}
		}
		String testCasepkgId = dto.getTestCasePackId();
		if(!StringUtils.isNullOrEmpty(testCasepkgId)){
			String testActorId = SecurityContextHolderHelp.getUserId();
			String hql="update from TestCase_CasePkg set executorId = ? , execStatus=? where testCaseId=? and packageId=? ";
			this.getHibernateGenericController().executeUpdate(hql,testActorId, testResult,testCaseId.toString(),testCasepkgId);
		}
		
	}

	private void exePerpare(CaseManagerDto dto){
        //下面的程序是后来改的,有点乱,原来审批和执行是分开的,现在要在用例列表加最新测试结果,所以改成如下,
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		dto.setTaskId(taskId);
		String remark =  dto.getRemark();
		Integer testResult = dto.getTestCaseInfo().getTestStatus();
		String testActorId = SecurityContextHolderHelp.getUserId();
		Long testCaseId = dto.getTestCaseInfo().getTestCaseId();
		String testCasepkgId = dto.getTestCasePackId();
//		if(testResult==1||testResult==6){
//			StringBuffer hql= new StringBuffer("update TestCaseInfo ");
//			hql.append("set testStatus=:testStatus,upddate=:upddate, ");
//			hql.append(" auditId=:auditId ");
//			Map praValues = new HashMap(7);
//			if(remark!=null&&!"".equals(remark.trim())){
//				hql.append(" ,remark=:remark ");
//				praValues.put("remark", remark);
//			}
//			praValues.put("auditId", testActorId);
//			hql.append(" where testCaseId=:testCaseId and taskId=:taskId ");	
//			dto.setHql(hql.toString());
//			praValues.put("testStatus", testResult);
//			praValues.put("testCaseId", testCaseId);
//			praValues.put("taskId", taskId);
//			praValues.put("upddate", new Date());
//			dto.setHqlParamMaps(praValues);
		//}else{
			Long moduleId = dto.getTestCaseInfo().getModuleId();
			TestResult rest = new TestResult();
			rest.setExeDate(new Date());
			rest.setTaskId(taskId);
			rest.setTestActor(testActorId);
			rest.setTestResult(testResult);
			rest.setModuleId(moduleId);
			rest.setTestCaseId(testCaseId);
			if(!StringUtils.isNullOrEmpty(testCasepkgId)){
				if(!StringUtils.isNullOrEmpty(remark)){
					remark = "(用例包中执行)"+" "+remark;
				}else{
					remark="用例包中执行.";
				}
			}
			rest.setRemark(remark);
			rest.setTestVer(dto.getExeVerId());
			dto.setRest(rest);
			//构造当前执行结果
			StringBuffer hql= new StringBuffer("update TestCaseInfo ");
			hql.append("set testStatus=:testStatus,upddate=:upddate, ");
			hql.append(" auditId=:auditId ");
			Map praValues = new HashMap(7);
			if(remark!=null&&!"".equals(remark.trim())){
				hql.append(" ,remark=:remark ");
				praValues.put("remark", remark);
			}
			praValues.put("auditId", testActorId);
			hql.append(" where testCaseId=:testCaseId and taskId=:taskId ");	
			dto.setHql(hql.toString());
			praValues.put("testStatus", testResult);
			praValues.put("testCaseId", testCaseId);
			praValues.put("taskId", taskId);
			praValues.put("upddate", new Date());
			dto.setHqlParamMaps(praValues);
		//}

	}
	public List<TestCaseInfo> loadCaseRest(CaseManagerDto dto){
		String QueryHql = "select new OutlineInfo(moduleId,moduleLevel,moduleNum) from OutlineInfo where taskId=? and moduleId=?";
		StringBuffer hql = new StringBuffer();
		hql.append("select new TestCaseInfo(testCaseId,prefixCondition,testCaseDes,testData,expResult) ");
		hql.append(" from TestCaseInfo t  where  t.taskId=:taskId ");
		Map praValuesMap =new  HashMap(2);
		OutlineInfo outline = (OutlineInfo)this.findByHql(QueryHql, dto.getTaskId(),dto.getCurrNodeId()).get(0);
		if(outline.getModuleLevel()>1){
			hql.append(" and t.moduleNum like :moduleNum ");
			praValuesMap.put("moduleNum", "%"+outline.getModuleNum()+"%");
		}
		praValuesMap.put("taskId",  dto.getTaskId());

		List<TestCaseInfo> list = this.findByHqlWithValuesMap(hql.toString(), dto.getPageNo(), dto.getPageSize(), "testCaseId", praValuesMap, false);
		QueryHql = "select new TestResult(resultId,testVer,testResult) from TestResult where testCaseId=?";
		for(TestCaseInfo testCase :list){
			List restList = this.findByHql(QueryHql, testCase.getTestCaseId());
			testCase.setTestResultList(restList);
		}
		return list;
	}

	public List<TestCaseVo> loadExeRecord(CaseManagerDto dto){
		this.buildViewHistHql(dto);
		List<TestCaseVo> list = this.findByHqlWithValuesMap(dto.getHql(), dto.getPageNo(), dto.getPageSize(), "tc.testCaseId", dto.getHqlParamMaps(), false);
		return list;
	}
	
	public int getLastExeCaseCount(){
		
		CaseManagerDto dto = new CaseManagerDto();
		this.buildLastExeHql(dto);
		int totalRows = hibernateGenericController.getResultCountWithValuesMap(dto.getHql(), dto.getHqlParamMaps(), "tc.testCaseId", false).intValue();
		return totalRows;
	}
	
	public TestCaseVo quickQueryLastExeCase(Long CaseId){
		StringBuffer hql = new StringBuffer();
		hql.append("select new cn.com.codes.caseManager.dto.TestCaseVo (tc.testCaseId,tc.prefixCondition,tc.testCaseDes,tc.weight,");
		hql.append(" rest.testResult,rest.testActor,rest.exeDate,rest.testVer,rest.remark,tc.taskId,tc.moduleId) ");
		hql.append("from  TestCaseInfo tc  join tc.testResult rest  join rest.exePersion exeP  where  exeP.companyId=? and tc.testCaseId=?");
		List<TestCaseVo> list = this.findByHql(hql.toString(), SecurityContextHolderHelp.getCompanyId(),CaseId);
		if(list!=null&&!list.isEmpty()){
			return list.get(0);
		}
		return null ;		
	}
	public List<TestCaseVo> loadLastExeCase(CaseManagerDto dto){
		
		this.buildLastExeHql(dto);
		List<TestCaseVo>  list = this.findByHqlWithValuesMap(dto.getHql(), dto.getPageNo(), dto.getPageSize(), "tc.testCaseId", dto.getHqlParamMaps(), false);
		return list;
	}
	
	private void buildViewHistHql(CaseManagerDto dto){
		StringBuffer hql = new StringBuffer();
		hql.append("select  new cn.com.codes.caseManager.dto.TestCaseVo (tc.testCaseId,tc.prefixCondition,tc.testCaseDes,tc.weight,");
		hql.append(" rest.testResult,rest.testActor,rest.exeDate,rest.testVer,rest.remark,tc.taskId,tc.moduleId) ");
		hql.append("from  TestCaseInfo tc left join tc.testResult rest  where tc.testStatus !=0 and tc.testStatus!=6 ");
		hql.append(" and tc.taskId=:taskId");
		Map values = new HashMap();
		values.put("taskId", SecurityContextHolderHelp.getCurrTaksId());
		if(dto.getQueryHelp()!=null){
			if(dto.getQueryHelp().getCaseTypeId()!=null&&dto.getQueryHelp().getCaseTypeId()!=-1){
				hql.append(" and tc.caseTypeId=:caseTypeId");
				values.put("caseTypeId", dto.getQueryHelp().getCaseTypeId());
			}
			if(dto.getQueryHelp().getPriId()!=null&&dto.getQueryHelp().getPriId()!=-1){
				hql.append(" and tc.priId=:priId");
				values.put("priId", dto.getQueryHelp().getPriId());
			}
			if(dto.getQueryHelp().getTestResult()!=null&&dto.getQueryHelp().getTestResult()!=-1){
					if(dto.getQueryHelp().getTestResult()==1){//未测试
						hql.append(" and rest is null"); //用不存在ID来获得未测试的用例
					}else{
						hql.append(" and rest.testResult=:testResult");
						values.put("testResult", dto.getQueryHelp().getTestResult());	
					}				
			}
			if(dto.getQueryHelp().getExeVerId()!=null&&dto.getQueryHelp().getExeVerId()!=-1){
				hql.append(" and rest.testVer=:testVer");
				values.put("testVer", dto.getQueryHelp().getExeVerId());
			}
			if(dto.getQueryHelp().getExeDate()!=null){
				hql.append(" and rest.exeDate>=:exeDate and rest.exeDate<=:nextDate");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dto.getQueryHelp().getExeDate());
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				values.put("exeDate", calendar.getTime());
				calendar.set(Calendar.HOUR_OF_DAY, 23);
				calendar.set(Calendar.MINUTE, 59);
				calendar.set(Calendar.SECOND, 59);
				values.put("nextDate", calendar.getTime());
			}

			if(dto.getQueryHelp().getActorId()!=null&&!"".equals(dto.getQueryHelp().getActorId().trim())){
				hql.append(" and rest.testActor in(:testActor)");
				List<String> testActorList = new ArrayList<String>();
				String[] actorIds = dto.getQueryHelp().getActorId().trim().split(" ");
				for(String id :actorIds){
					testActorList.add(id);
				}
				actorIds = null;
				this.sortStringList(testActorList);
				values.put("testActor",testActorList);
			}
			
			if(dto.getQueryHelp().getCreaterId()!=null&&!"".equals(dto.getQueryHelp().getCreaterId().trim())){
				hql.append(" and tc.createrId in(:createrId)");
				List<String> authorList = new ArrayList<String>();
				String[] authorIds = dto.getQueryHelp().getCreaterId().trim().split(" ");
				for(String id :authorIds){
					authorList.add(id);
				}
				authorIds = null;
				this.sortStringList(authorList);
				values.put("createrId",authorList);
			}
			if(dto.getQueryHelp().getAuditerId()!=null&&!"".equals(dto.getQueryHelp().getAuditerId().trim())){
				hql.append(" and tc.auditId in(:auditId)");
				List<String> auditIdList = new ArrayList<String>();
				String[] actorIds = dto.getQueryHelp().getAuditerId().trim().split(" ");
				for(String id :actorIds){
					auditIdList.add(id);
				}
				actorIds = null;
				this.sortStringList(auditIdList);
				values.put("auditId", auditIdList);
			}
			if(dto.getQueryHelp().getTestCaseDes()!=null&&!"".equals(dto.getQueryHelp().getTestCaseDes().trim())){
				hql.append(" and tc.testCaseDes like:testCaseDes");
				values.put("testCaseDes", "%"+dto.getQueryHelp().getTestCaseDes().trim()+"%");
			}
			if(dto.getQueryHelp().getWeight()!=null){
				hql.append(" and tc.weight=:weight");
				values.put("weight", dto.getQueryHelp().getWeight());
			}
		}
		hql.append(" order by rest.exeDate desc ,tc.upddate desc");	
		dto.setHql(hql.toString());
		dto.setHqlParamMaps(values);
		
	}
	
	private void buildLastExeHql(CaseManagerDto dto){
		
		StringBuffer hql = new StringBuffer();
		hql.append("select new cn.com.codes.caseManager.dto.TestCaseVo (tc.testCaseId,tc.prefixCondition,tc.testCaseDes,tc.weight,");
		hql.append(" rest.testResult,rest.testActor,rest.exeDate,rest.testVer,rest.remark,tc.taskId,tc.moduleId) ");
		hql.append("from  TestCaseInfo tc  join tc.testResult rest  join rest.exePersion exeP  where  exeP.companyId=:companyId");
		Map values = new HashMap();
		values.put("companyId", SecurityContextHolderHelp.getCompanyId());
		if(dto.getTaskId()!=null&&!"".equals(dto.getTaskId())){
			
			hql.append(" and tc.taskId=:taskId");
			values.put("taskId", dto.getTaskId());
		}
		if(dto.getQueryHelp()!=null){
			if(dto.getQueryHelp().getCaseTypeId()!=null&&dto.getQueryHelp().getCaseTypeId()!=-1){
				hql.append(" and tc.caseTypeId=:caseTypeId");
				values.put("caseTypeId", dto.getQueryHelp().getCaseTypeId());
			}
			if(dto.getQueryHelp().getPriId()!=null&&dto.getQueryHelp().getPriId()!=-1){
				hql.append(" and tc.priId=:priId");
				values.put("priId", dto.getQueryHelp().getPriId());
			}
			if(dto.getQueryHelp().getTestResult()!=null&&dto.getQueryHelp().getTestResult()!=-1){
					if(dto.getQueryHelp().getTestResult()==1){//未测试
						hql.append(" and rest is null"); //用不存在ID来获得未测试的用例
					}else{
						hql.append(" and rest.testResult=:testResult");
						values.put("testResult", dto.getQueryHelp().getTestResult());	
					}				
			}
			if(dto.getQueryHelp().getExeVerId()!=null&&dto.getQueryHelp().getExeVerId()!=-1){
				hql.append(" and rest.testVer=:testVer");
				values.put("testVer", dto.getQueryHelp().getExeVerId());
			}
			if(dto.getQueryHelp().getExeDate()!=null){
				hql.append(" and rest.exeDate>=:exeDate and rest.exeDate<=:nextDate");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dto.getQueryHelp().getExeDate());
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				values.put("exeDate", calendar.getTime());
				calendar.set(Calendar.HOUR_OF_DAY, 23);
				calendar.set(Calendar.MINUTE, 59);
				calendar.set(Calendar.SECOND, 59);
				values.put("nextDate", calendar.getTime());
			}else{
				hql.append(" and rest.exeDate>=:exeDate and rest.exeDate<=:nextDate");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				values.put("nextDate", calendar.getTime());
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.add(Calendar.DATE,-6);
				values.put("exeDate", calendar.getTime());
			}

			
			if(dto.getQueryHelp().getCreaterId()!=null&&!"".equals(dto.getQueryHelp().getCreaterId().trim())){
				hql.append(" and tc.createrId in(:createrId)");
				List<String> authorList = new ArrayList<String>();
				String[] authorIds = dto.getQueryHelp().getCreaterId().trim().split(" ");
				for(String id :authorIds){
					authorList.add(id);
				}
				authorIds = null;
				this.sortStringList(authorList);
				values.put("createrId",authorList);
			}
			if(dto.getQueryHelp().getAuditerId()!=null&&!"".equals(dto.getQueryHelp().getAuditerId().trim())){
				hql.append(" and tc.auditId in(:auditId)");
				List<String> auditIdList = new ArrayList<String>();
				String[] actorIds = dto.getQueryHelp().getAuditerId().trim().split(" ");
				for(String id :actorIds){
					auditIdList.add(id);
				}
				actorIds = null;
				this.sortStringList(auditIdList);
				values.put("auditId", auditIdList);
			}
			if(dto.getQueryHelp().getTestCaseDes()!=null&&!"".equals(dto.getQueryHelp().getTestCaseDes().trim())){
				hql.append(" and tc.testCaseDes like:testCaseDes");
				values.put("testCaseDes", "%"+dto.getQueryHelp().getTestCaseDes().trim()+"%");
			}
			if(dto.getQueryHelp().getWeight()!=null){
				hql.append(" and tc.weight=:weight");
				values.put("weight", dto.getQueryHelp().getWeight());
			}
		}else{
			hql.append(" and rest.exeDate>=:exeDate and rest.exeDate<=:nextDate");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			values.put("nextDate", calendar.getTime());
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.add(Calendar.DATE,-6);
			values.put("exeDate", calendar.getTime());
		}
		int isAdmin = SecurityContextHolderHelp.getUserIsAdmin();
		//if(isAdmin<1){
			hql.append(" and rest.testActor in(:testActor)");
			List<String> testActorList = new ArrayList<String>();
			testActorList.add(SecurityContextHolderHelp.getUserId());
			this.sortStringList(testActorList);
			values.put("testActor",testActorList);
		//}
		hql.append(" order by rest.exeDate desc,tc.upddate desc");	
		dto.setHql(hql.toString());
		dto.setHqlParamMaps(values);
		
	}
	private void buildLoadAuditCaseHql(CaseManagerDto dto,String moduleNum){
		StringBuffer hql = new StringBuffer();
		Map praValuesMap = new HashMap();
		hql.append("select new TestCaseInfo(testCaseId,prefixCondition,testCaseDes,");
		hql.append("isReleased,testStatus,weight,createrId,");
		hql.append("attachUrl,auditId,caseTypeId,priId,moduleId,taskId,creatdate)");
		hql.append(" from TestCaseInfo  where testStatus=0 "); 
		if(moduleNum!=null&&!"".equals(moduleNum)){
			hql.append("  and moduleNum like :moduleNum");
			praValuesMap.put("moduleNum", moduleNum+"%");
		}
		if(dto.getTestCaseInfo()==null){
			hql.append(" and taskId=:taskId  order by upddate desc");
			praValuesMap.put("taskId", dto.getTaskId());
			dto.setHqlParamMaps(praValuesMap);
			hql.append(" order by upddate desc");
			dto.setHql(hql.toString());
			return;
		}
		if(dto.getTestCaseInfo().getCaseTypeId()!=null&&dto.getTestCaseInfo().getCaseTypeId()!=-1){
			hql.append(" and caseTypeId=:caseTypeId");
			praValuesMap.put("caseTypeId", dto.getTestCaseInfo().getCaseTypeId());
		}
		if(dto.getTestCaseInfo().getPriId()!=null&&dto.getTestCaseInfo().getPriId()!=-1){
			hql.append(" and priId=:priId");
			praValuesMap.put("priId", dto.getTestCaseInfo().getPriId());
		}
		if(dto.getTestCaseInfo().getCreaterId()!=null&&!"".equals(dto.getTestCaseInfo().getCreaterId().trim())){
			hql.append(" and createrId in(:createrId)");
			List<String> authorList = new ArrayList<String>();
			String[] authorIds = dto.getTestCaseInfo().getCreaterId().trim().split(" ");
			for(String id :authorIds){
				authorList.add(id);
			}
			authorIds = null;
			this.sortStringList(authorList);
			praValuesMap.put("createrId",authorList);
		}
		if(dto.getTestCaseInfo().getWeight()!=null){
			hql.append(" and weight = :weight");
			praValuesMap.put("weight", dto.getTestCaseInfo().getWeight());
		}
		if(dto.getTestCaseInfo().getTestCaseDes()!=null&&!"".equals(dto.getTestCaseInfo().getTestCaseDes().trim())){
			hql.append(" and testCaseDes like :testCaseDes");
			praValuesMap.put("testCaseDes", "%"+dto.getTestCaseInfo().getTestCaseDes().trim()+"%");
		}
		hql.append(" and taskId=:taskId  order by upddate desc");
		praValuesMap.put("taskId", dto.getTaskId());
		dto.setHql(hql.toString());
		dto.setHqlParamMaps(praValuesMap);
	}
	
	private void buildLoadCaseHql(CaseManagerDto dto,String moduleNum){
		StringBuffer hql = new StringBuffer();
		Map praValuesMap = new HashMap();
		praValuesMap.put("taskId", dto.getTaskId());
		hql.append("select new TestCaseInfo(testCaseId,prefixCondition,testCaseDes,");
		hql.append("isReleased,testStatus,weight,createrId,");
		hql.append("attachUrl,auditId,caseTypeId,priId,moduleId,taskId,creatdate)");
		hql.append(" from TestCaseInfo  where taskId=:taskId "); 
		if(moduleNum!=null&&!"".equals(moduleNum)){
			hql.append("  and moduleNum like :moduleNum");
			praValuesMap.put("moduleNum", moduleNum+"%");
		}
		if(dto.getTestCaseInfo()==null){
			dto.setHqlParamMaps(praValuesMap);
			hql.append(" order by upddate desc");
			dto.setHql(hql.toString());
			return;
		}
		if(dto.getTestCaseInfo().getCaseTypeId()!=null&&dto.getTestCaseInfo().getCaseTypeId()!=-1){
			hql.append(" and caseTypeId=:caseTypeId");
			praValuesMap.put("caseTypeId", dto.getTestCaseInfo().getCaseTypeId());
		}
		if(dto.getTestCaseInfo().getPriId()!=null&&dto.getTestCaseInfo().getPriId()!=-1){
			hql.append(" and priId=:priId");
			praValuesMap.put("priId", dto.getTestCaseInfo().getPriId());
		}
		if(dto.getTestCaseInfo().getTestStatus()!=null&&dto.getTestCaseInfo().getTestStatus()!=-1){
			hql.append(" and testStatus=:testStatus");
			praValuesMap.put("testStatus", dto.getTestCaseInfo().getTestStatus());
		}
		if(dto.getTestCaseInfo().getCreaterId()!=null&&!"".equals(dto.getTestCaseInfo().getCreaterId().trim())){
			hql.append(" and createrId in(:createrId)");
			List<String> authorList = new ArrayList<String>();
			String[] authorIds = dto.getTestCaseInfo().getCreaterId().trim().split(" ");
			for(String id :authorIds){
				authorList.add(id);
			}
			authorIds = null;
			this.sortStringList(authorList);
			praValuesMap.put("createrId",authorList);
		}
		if(dto.getTestCaseInfo().getAuditId()!=null&&!"".equals(dto.getTestCaseInfo().getAuditId().trim())){
			hql.append(" and auditId in(:auditId)");
			List<String> auditIdList = new ArrayList<String>();
			String[] actorIds = dto.getTestCaseInfo().getAuditId().trim().split(" ");
			for(String id :actorIds){
				auditIdList.add(id);
			}
			actorIds = null;
			this.sortStringList(auditIdList);
			praValuesMap.put("auditId", auditIdList);
		}
		if(dto.getTestCaseInfo().getWeight()!=null){
			hql.append(" and weight = :weight");
			praValuesMap.put("weight", dto.getTestCaseInfo().getWeight());
		}
		if(dto.getTestCaseInfo().getTestCaseDes()!=null&&!"".equals(dto.getTestCaseInfo().getTestCaseDes().trim())){
			hql.append(" and testCaseDes like :testCaseDes");
			praValuesMap.put("testCaseDes", "%"+dto.getTestCaseInfo().getTestCaseDes().trim()+"%");
		}
		hql.append(" order by upddate desc");
		dto.setHql(hql.toString());
		dto.setHqlParamMaps(praValuesMap);
	}
	public TestTaskDetailService getTestTaskService() {
		return testTaskService;
	}
	public void setTestTaskService(TestTaskDetailService testTaskService) {
		this.testTaskService = testTaskService;
	}

	public void addOrUpCase(CaseManagerDto dto ){

		TestCaseInfo testCase = dto.getTestCaseInfo();
		testCase.setTaskId(SecurityContextHolderHelp.getCurrTaksId());
		testCase.setUpddate(new Date());
		//修正后再做修改时，就自动改为reView状态
		CaseExeHistory his = new CaseExeHistory();
		his.setTaskId(testCase.getTaskId());
		if(testCase.getTestStatus()!=null && testCase.getTestStatus()==6){
			testCase.setTestStatus(0);
			his.setOperaType(5);
		}
		his.setTestResult(testCase.getTestStatus());
		if(testCase.getTestCaseId()==null){
			testCase.setCreatdate(new Date());
			testCase.setUpddate(testCase.getCreatdate());
			VisitUser creator = SecurityContextHolderHelp.getMyUserInfo();
			testCase.setAuthorName(creator.getUniqueName());
			String taskId = testCase.getTaskId();
			if (StringUtils.isNullOrEmpty(taskId)) {
				taskId = SecurityContextHolderHelp.getCurrTaksId();
			}
			dto.getTestCaseInfo().setCreaterId(SecurityContextHolderHelp.getUserId());
			String outlineHql = "select new OutlineInfo(moduleId,moduleLevel,moduleNum) from OutlineInfo where taskId=? and moduleId=?";
			OutlineInfo outline = (OutlineInfo)testTaskService.findByHql(outlineHql, taskId,testCase.getModuleId()).get(0);
			if(outline!=null){
				dto.getTestCaseInfo().setModuleNum(outline.getModuleNum());
			}
			if(!this.isReview(taskId)){
				dto.getTestCaseInfo().setTestStatus(1);
			}else{
				dto.getTestCaseInfo().setTestStatus(0);
			}
			dto.getTestCaseInfo().setIsReleased(0);
			this.add(testCase);
			his.setOperaType(1);

		}else{
			testCase.setAuditId(SecurityContextHolderHelp.getUserId());
			this.update(testCase);
			his.setOperaType(2);
			
		}
		his.setTestCaseId(testCase.getTestCaseId());
		his.setModuleId(testCase.getModuleId());
		his.setExeDate(testCase.getUpddate());
		his.setTestActor(SecurityContextHolderHelp.getUserId());
		this.add(his);
		
		//先删除用例文件记录。重新添加文件信息
//		String hql = "delete from FileInfo where typeId=?";
//		this.getHibernateGenericController().executeUpdate(hql, testCase.getTestCaseId());
		List<FileInfoVo> fileInfos = dto.getFileInfos();
		List<FileInfoVo> filesInfo = null;
		if(fileInfos!=null && fileInfos.size()>0){
			JSONArray json = JSONArray.fromObject(fileInfos.toString().replace("[[", "[").replace("]]", "]]"));
			filesInfo= (List<FileInfoVo>)JSONArray.toCollection(json, FileInfoVo.class);
			for (FileInfoVo fileInfoVo : filesInfo) {
				FileInfo fileInfo = new FileInfo();
				fileInfo.setType("case");
				fileInfo.setTypeId(testCase.getTestCaseId());
				fileInfo.setRelativeName(fileInfoVo.getFileOriginalName());
				fileInfo.setFilePath(fileInfoVo.getFileUrl());
				this.add(fileInfo);
			}
		}
		
		CaseType caseType = this.get(CaseType.class, testCase.getCaseTypeId());
		testCase.setTypeName(caseType.getTypeName());
		CasePri pri = this.get(CasePri.class, testCase.getPriId());
		testCase.setPriName(pri.getTypeName());
	}

	private boolean isReview(String taskId){
		String flwHql = "select new TestFlowInfo(testFlowCode) from TestFlowInfo where taskId=? and testFlowCode=9";
		List flwList = this.findByHql(flwHql, taskId);
		if(flwList==null||flwList.size()==0){
			return false;
		}else{
			return true;
		}
		
	}
	
	@Override
	public void saveImpotTestCase(List<TestCaseInfo> casrList) {
		
		for(TestCaseInfo testCase :casrList){
			this.add(testCase);
			CaseExeHistory his = new CaseExeHistory();
			his.setTestCaseId(testCase.getTestCaseId());
			his.setModuleId(testCase.getModuleId());
			his.setExeDate(testCase.getUpddate());
			his.setTestActor(SecurityContextHolderHelp.getUserId());
			his.setTaskId(testCase.getTaskId());
			his.setRemark("导入用例");
			this.add(his);
		}
		
	}

}
