package cn.com.codes.bugManager.service.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import cn.com.codes.bugManager.blh.BugFlowConst;
import cn.com.codes.bugManager.dto.BugManagerDto;
import cn.com.codes.bugManager.service.BugCommonService;
import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.app.services.BaseServiceImpl;
import cn.com.codes.framework.common.HtmlListQueryObj;
import cn.com.codes.framework.common.ListObject;
import cn.com.codes.framework.common.ResourceUtils;
import cn.com.codes.framework.common.config.PropertiesBean;
import cn.com.codes.framework.common.util.Context;
import cn.com.codes.framework.jms.mail.MailProducer;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.security.filter.SecurityFilter;
import cn.com.codes.msgManager.dto.MailBean;
import cn.com.codes.msgManager.service.CommonMsgService;
import cn.com.codes.object.BugBaseInfo;
import cn.com.codes.object.BugFreq;
import cn.com.codes.object.BugGrade;
import cn.com.codes.object.BugHandHistory;
import cn.com.codes.object.BugOpotunity;
import cn.com.codes.object.BugPri;
import cn.com.codes.object.BugQueryInfo;
import cn.com.codes.object.BugSource;
import cn.com.codes.object.BugType;
import cn.com.codes.object.GeneCause;
import cn.com.codes.object.ImportPhase;
import cn.com.codes.object.OccurPlant;
import cn.com.codes.object.OutlineInfo;
import cn.com.codes.object.SingleTestTask;
import cn.com.codes.object.TaskUseActor;
import cn.com.codes.object.TestTaskDetail;
import cn.com.codes.object.TypeDefine;
import cn.com.codes.object.User;
import cn.com.codes.testTaskManager.dto.CurrTaskInfo;
import cn.com.codes.testTaskManager.service.TestTaskDetailService;



public class BugCommonServiceImpl extends BaseServiceImpl implements
		BugCommonService {

	private CommonMsgService mypmCommonMsgService;
	private MailProducer mailProducer;
	private TestTaskDetailService testTaskService ;
	private JavaMailSender mailSender;
	private static Logger logger = Logger.getLogger(BugCommonServiceImpl.class);
	
	public List loadOwnerBug(String taskId,String loginName){
		
		StringBuffer hql = new StringBuffer("select new BugBaseInfo(");
		hql.append(" bugId,bugDesc,bugGradeId,bugFreqId,bugOccaId,")
		   .append(" bugTypeId,priId, devOwnerId,reptDate,msgFlag,")
		   .append(" relaCaseFlag, moduleId, testOwnerId,currFlowCd,")
		   .append(" currHandlerId,currStateId,nextFlowCd,bugReptId,taskId)");
		if(taskId==null||"".equals(taskId.trim())){
			hql.append(" from BugBaseInfo b where b.nextOwnerId= ? ");
		}else{
			hql.append(" from BugBaseInfo b where b.nextOwnerId= ? and b.taskId=?");
		}
		
		
		return null;
	}
	
	public List<Map<String,Object>>  loadBugBoardCurrNotInPro(String taskId) {
		StringBuffer bugHql = new StringBuffer("select u.name as owner, count(*) as whCount ,u.loginname  from t_bugbaseinfo b \n");
		bugHql.append( "inner join t_user u on u.id = b.bug_num \n");
		bugHql.append( " where b.current_state not in (2,4, 5, 14, 15, 22, 23) and \n");
		bugHql.append( "  b.task_id ='"+taskId+"' \n");
		bugHql.append( "  and   u.id not in (select ua.userid from  T_TASK_USEACTOR ua where ua.taskid='"+taskId+"' ) \n");
		bugHql.append( "   group by u.loginname \n");
		List<Map<String,Object>>  countlist = this.findBySqlByJDBC (bugHql.toString());
		return countlist;
	}

	public List loadBugBoard4Mysql(String taskId){
		if(taskId==null||"".equals(taskId.trim())){
			StringBuffer bugBoardSql = new StringBuffer();
			bugBoardSql.append(" select teamMember.tm, whandCountTable.whCount, handCountTable.hCount, teamMember.loginname ");
			bugBoardSql.append("  from (select distinct concat(concat(u.loginname,'('),concat(u.name,')')) as tm ,u.loginname ");
			bugBoardSql.append("from T_TASK_USEACTOR ua ");
			bugBoardSql.append("inner join t_user u on ua.userid = u.id ");
			bugBoardSql.append("inner join t_test_task_detail td   on ua.TASKID = td.TASKID and td.TEST_TASK_TATE != 4 ");
			bugBoardSql.append("where ua.is_enable = 1 and u.DELFLAG=0 ");
			bugBoardSql.append(") teamMember ");
			bugBoardSql.append("left join (select concat(concat(u.loginname,'('),concat(u.name,')')) as owner, ");
			bugBoardSql.append("count(*) as whCount ");
			bugBoardSql.append("from t_bugbaseinfo b ");
			bugBoardSql.append("inner join t_user u on u.id = b.bug_num ");
			bugBoardSql.append("inner join t_test_task_detail td   on b.TASK_ID = td.TASKID and td.TEST_TASK_TATE != 4 ");
			bugBoardSql.append("where b.current_state not in (4, 5, 14, 15, 22, 23) ");
			bugBoardSql.append("group by u.loginname, u.name) whandCountTable on teamMember.tm = ");
			bugBoardSql.append("whandCountTable.owner ");
			bugBoardSql.append("left join (select concat(concat(u.loginname,'('),concat(u.name,')')) as owner, ");
			bugBoardSql.append("count(*) as hCount ");
			bugBoardSql.append("from t_bughandhistory h ");
			bugBoardSql.append("inner join t_user u on u.id = h.handler ");
			bugBoardSql.append("inner join t_test_task_detail td   on h.TASK_ID = td.TASKID and td.TEST_TASK_TATE != 4 ");
			bugBoardSql.append("where date_format(h.insdate, '%Y-%c-%d') =date('"+StringUtils.formatShortDate(new Date())+"')  ");
			bugBoardSql.append("group by u.loginname, u.name) handCountTable on " +
						"teamMember.tm =handCountTable.owner	 ");	
			List list = this.findBySql(bugBoardSql.toString(), null);
			bugBoardSql = null;
			return list;
		}else{

			StringBuffer bugBoardSql = new StringBuffer();
			bugBoardSql.append(" select teamMember.tm, whandCountTable.whCount, handCountTable.hCount , teamMember.loginname");
			bugBoardSql.append("  from (select distinct u.name as tm ,u.loginname ");
			bugBoardSql.append("from T_TASK_USEACTOR ua ");
			bugBoardSql.append("inner join t_user u on ua.userid = u.id ");
			bugBoardSql.append("where ua.is_enable = 1 and u.DELFLAG=0  ");
			bugBoardSql.append("and ua.taskid =:taskId) teamMember ");
			bugBoardSql.append("left join (select u.name as owner, ");
			bugBoardSql.append("count(*) as whCount ");
			bugBoardSql.append("from t_bugbaseinfo b ");
			bugBoardSql.append("inner join t_user u on u.id = b.bug_num ");
			bugBoardSql.append("where b.current_state not in (4, 5, 14, 15, 22, 23) ");
			bugBoardSql.append("and b.task_id =:taskId ");
			bugBoardSql.append("group by u.loginname, u.name) whandCountTable on teamMember.tm = ");
			bugBoardSql.append("whandCountTable.owner ");
			bugBoardSql.append("left join (select u.name as owner, ");
			bugBoardSql.append("count(*) as hCount ");
			bugBoardSql.append("from t_bughandhistory h ");
			bugBoardSql.append("inner join t_user u on u.id = h.handler ");
			bugBoardSql.append("where h.task_id =:taskId ");
			bugBoardSql.append("and date_format(h.insdate, '%Y-%c-%d') =date('"+StringUtils.formatShortDate(new Date())+"') ");
			bugBoardSql.append("group by u.loginname, u.name) handCountTable on " +
						"teamMember.tm =handCountTable.owner	 ");
			Map praValues = new HashMap();
			praValues.put("taskId", SecurityContextHolderHelp.getCurrTaksId());
			List list = this.findBySqlWithValuesMap(bugBoardSql.toString(), null, praValues);
			bugBoardSql = null;
			praValues = null;
			return list;
		}
	}
	public List loadBugBoard(String taskId){
		PropertiesBean conf = (PropertiesBean) Context.getInstance().getBean("ContextProperties");
		if(conf.getProperty("config.db.driver")!=null&&conf.getProperty("config.db.driver").indexOf("mysql")>0){
			return this.loadBugBoard4Mysql(taskId);
		}else{
			return this.loadBugBoard4Oracle(taskId);
		}
	}
	

	public List loadBugBoard4Oracle(String taskId){
		if(taskId==null||"".equals(taskId.trim())){
			StringBuffer bugBoardSql = new StringBuffer();
			bugBoardSql.append(" select teamMember.tm, whandCountTable.whCount, handCountTable.hCount, teamMember.loginname ");
			bugBoardSql.append("  from (select distinct (u.loginname || '(' || u.name || ')') as tm ,u.loginname ");
			bugBoardSql.append("from T_TASK_USEACTOR ua ");
			bugBoardSql.append("inner join t_user u on ua.userid = u.id ");
			bugBoardSql.append("where ua.is_enable = 1 and u.DELFLAG=0 ");
			bugBoardSql.append(") teamMember ");
			bugBoardSql.append("left join (select (u.loginname || '(' || u.name || ')') as owner, ");
			bugBoardSql.append("count(*) as whCount ");
			bugBoardSql.append("from t_bugbaseinfo b ");
			bugBoardSql.append("inner join t_user u on u.id = b.bug_num ");
			bugBoardSql.append("where b.current_state not in (4, 5, 14, 15, 22, 23) ");
			bugBoardSql.append("group by u.loginname, u.name) whandCountTable on teamMember.tm = ");
			bugBoardSql.append("whandCountTable.owner ");
			bugBoardSql.append("left join (select (u.loginname || '(' || u.name || ')') as owner, ");
			bugBoardSql.append("count(*) as hCount ");
			bugBoardSql.append("from t_bughandhistory h ");
			bugBoardSql.append("inner join t_user u on u.id = h.handler ");
			bugBoardSql.append("where to_char(h.insdate, 'yyyy-mm-dd') =? ");
			bugBoardSql.append("group by u.loginname, u.name) handCountTable on " +
						"teamMember.tm =handCountTable.owner	 ");	
			List list = this.findBySql(bugBoardSql.toString(), null, StringUtils.formatShortDate(new Date()));
			bugBoardSql = null;
			return list;
		}else{
			StringBuffer bugBoardSql = new StringBuffer();
			bugBoardSql.append(" select teamMember.tm, whandCountTable.whCount, handCountTable.hCount , teamMember.loginname");
			bugBoardSql.append("  from (select distinct (u.loginname || '(' || u.name || ')') as tm ,u.loginname ");
			bugBoardSql.append("from T_TASK_USEACTOR ua ");
			bugBoardSql.append("inner join t_user u on ua.userid = u.id ");
			bugBoardSql.append("where ua.is_enable = 1 and u.DELFLAG=0 ");
			bugBoardSql.append("and ua.taskid =:taskId) teamMember ");
			bugBoardSql.append("left join (select (u.loginname || '(' || u.name || ')') as owner, ");
			bugBoardSql.append("count(*) as whCount ");
			bugBoardSql.append("from t_bugbaseinfo b ");
			bugBoardSql.append("inner join t_user u on u.id = b.bug_num ");
			bugBoardSql.append("where b.current_state not in (2,4, 5, 14, 15, 22, 23) ");
			bugBoardSql.append("and b.task_id =:taskId ");
			bugBoardSql.append("group by u.loginname, u.name) whandCountTable on teamMember.tm = ");
			bugBoardSql.append("whandCountTable.owner ");
			bugBoardSql.append("left join (select (u.loginname || '(' || u.name || ')') as owner, ");
			bugBoardSql.append("count(*) as hCount ");
			bugBoardSql.append("from t_bughandhistory h ");
			bugBoardSql.append("inner join t_user u on u.id = h.handler ");
			bugBoardSql.append("where h.task_id =:taskId ");
			bugBoardSql.append("and to_char(h.insdate, 'yyyy-mm-dd') =:currData ");
			bugBoardSql.append("group by u.loginname, u.name) handCountTable on " +
						"teamMember.tm =handCountTable.owner	 ");
			Map praValues = new HashMap();
			praValues.put("taskId", SecurityContextHolderHelp.getCurrTaksId());
			praValues.put("currData", StringUtils.formatShortDate(new Date()));
			List list = this.findBySqlWithValuesMap(bugBoardSql.toString(), null, praValues);
			bugBoardSql = null;
			praValues = null;
			return list;
		}
	}
	public BugBaseInfo qucikQuery(Long bugId){
		
		StringBuffer bugHql = new StringBuffer("select new BugBaseInfo(");
		bugHql.append(" bugId,bugDesc,bugGradeId,bugFreqId,bugOccaId,")
		      .append(" bugTypeId,priId, devOwnerId,reptDate,msgFlag,")
		      .append(" relaCaseFlag, moduleId, testOwnerId,currFlowCd,")
		       .append(" currHandlerId,currStateId,nextFlowCd,bugReptId,taskId)");
		bugHql.append(" from BugBaseInfo b where b.bugId=?");
		List<BugBaseInfo> list = this.findByHql(bugHql.toString(), bugId);
		if(list!=null&&!list.isEmpty()){
			StringBuffer hql = new StringBuffer("select new TestTaskDetail(outlineState,testPhase,")
			.append("currentVersion,testSeq,reltCaseFlag) from TestTaskDetail where taskId=? and companyId=?");
			String compId = SecurityContextHolderHelp.getCompanyId();
			List<TestTaskDetail> taskList = this.findByHql(hql.toString(), list.get(0).getTaskId(),compId);
			if(taskList==null||taskList.isEmpty()){
				return null;
			}else{
				return list.get(0);
			}
		}
		return null;
	}
	
	public void updatHandSub(BugManagerDto dto,BugHandHistory bugHistory){
		//用HQL更新以防并发时，一些数据覆盖
		this.buildBugUpHql(dto);
		if(bugHistory!=null){
			String upFianl = "update BugHandHistory set currDayFinal=0 where bugId=? and insDate >=? and insDate < ?and currDayFinal=1";
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			Date now = calendar.getTime();
			this.getHibernateGenericController().executeUpdate(upFianl.toString(), bugHistory.getBugId(),now,new Date());
			this.add(bugHistory);
		}
		this.executeUpdateByHqlWithValuesMap(dto.getHql(), dto.getHqlParamMaps());
		this.getHibernateGenericController().getSessionFactory().getCurrentSession().flush();
		dto.setHqlParamMaps(null);
	}
	
	public List<BugHandHistory> getBugHistory(Long bugId,int pageNo){
		StringBuffer hql = new StringBuffer("select new BugHandHistory(");
		hql.append("historyId,remark,handlerId,handResult,insDate) from BugHandHistory ");
		hql.append(" where bugId=? and taskId=? order by insDate");
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		List<BugHandHistory> historyList = this.findByHqlPage(hql.toString(), pageNo, 20, "historyId", bugId,taskId);
		if(historyList!=null&&historyList.size()>0){
			String userHql = "select new User(id,name,loginName) from User where id=? and companyId=?";
			String compId = SecurityContextHolderHelp.getCompanyId();
			User hander = null;
			for(BugHandHistory his:historyList){
				hander =(User)this.findByHql(userHql, his.getHandlerId(),compId).get(0);
				his.setHandlerName(hander.getUniqueName());
			}
		}
		return historyList;
	}
	
	private void  buildBugUpHql(BugManagerDto dto){
		BugBaseInfo bug =dto.getBug();
		//以后改成用HQL方式保存，只存部分属性，防止并发时，测试人员的修改覆盖开发人员处理的状态
		StringBuffer upHql = new StringBuffer("update BugBaseInfo set moduleId=:moduleId,");
		upHql.append("bugDesc=:bugDesc,bugTypeId=:bugTypeId,bugGradeId=:bugGradeId,platformId=:platformId,");
		upHql.append("sourceId=:sourceId,bugOccaId=:bugOccaId,geneCauseId=:geneCauseId,");
		upHql.append("priId=:priId,bugFreqId=:bugFreqId,reProStep=:reProStep,reProTxt=:reProTxt,");
		upHql.append("currHandlerId=:currHandlerId,nextFlowCd=:nextFlowCd,currFlowCd=:currFlowCd,");
		upHql.append("currHandlDate=:currHandlDate,withRepteId=:withRepteId,initState=:initState,");
		upHql.append("currStateId=:currStateId,reproPersent=:reproPersent,genePhaseId=:genePhaseIdp,");
		upHql.append("bugAntimodDate=:bugAntimodDate,planAmendHour=:planAmendHour,currRemark=:currRemark,");
		upHql.append("devOwnerId=:devOwnerId,testOwnerId=:testOwnerId,analyseOwnerId=:analyseOwnerId,");
		upHql.append("assinOwnerId=:assinOwnerId,intercessOwnerId=:intercessOwnerId,nextOwnerId=:nextOwnerId");
		upHql.append(",fixDate=:fixDate,moduleNum=:moduleNum,chargeOwner=:chargeOwner");
		//"13", "已改"
		Map praValues = new HashMap();
		praValues.put("moduleNum", bug.getModuleNum());
		if(bug.getCurrStateId()==13||bug.getCurrStateId()==26){
			praValues.put("fixDate", new Date());
		}else{
			praValues.put("fixDate", null);
		}
		
		if(bug.getVerifyVer()!=null){
			upHql.append(",verifyVer=:verifyVer ");
			praValues.put("verifyVer", bug.getVerifyVer());
		}
		
		if(bug.getFixVer()!=null){
			upHql.append(",fixVer=:fixVer ");
			praValues.put("fixVer", bug.getFixVer());
		}
		upHql.append(" where bugId=:bugId and taskId=:taskId");
		dto.setHql(upHql.toString());
		dto.setHqlParamMaps(praValues);
		praValues.put("moduleId", bug.getModuleId());
		praValues.put("bugDesc", bug.getBugDesc().trim());
		praValues.put("bugTypeId", bug.getBugTypeId());
		praValues.put("bugGradeId", bug.getBugGradeId());
		praValues.put("platformId", bug.getPlatformId());
		praValues.put("sourceId", bug.getSourceId());
		praValues.put("bugOccaId", bug.getBugOccaId());
		praValues.put("geneCauseId", bug.getGeneCauseId());
		praValues.put("priId", bug.getPriId());
		praValues.put("bugFreqId", bug.getBugFreqId());
		praValues.put("reProStep", bug.getReProStep().trim());
		praValues.put("reProTxt", bug.getReProTxt().trim());
		praValues.put("bugId", bug.getBugId());
		praValues.put("taskId", dto.getBug().getTaskId());
		praValues.put("currHandlerId", SecurityContextHolderHelp.getUserId());
		praValues.put("nextFlowCd", bug.getNextFlowCd());
		praValues.put("currFlowCd", bug.getCurrFlowCd());
		praValues.put("currHandlDate", new Date());
		praValues.put("withRepteId", bug.getWithRepteId());
		praValues.put("initState", bug.getInitState());
		praValues.put("currStateId", bug.getCurrStateId());
		praValues.put("reproPersent", bug.getReproPersent());
		praValues.put("genePhaseIdp", bug.getGenePhaseId());
		praValues.put("bugAntimodDate", bug.getBugAntimodDate());
		praValues.put("planAmendHour", bug.getPlanAmendHour());
		praValues.put("currRemark", bug.getCurrRemark());
		praValues.put("devOwnerId", bug.getDevOwnerId());
		praValues.put("testOwnerId", bug.getTestOwnerId());
		praValues.put("analyseOwnerId", bug.getAnalyseOwnerId());
		praValues.put("assinOwnerId", bug.getAssinOwnerId());
		praValues.put("intercessOwnerId", bug.getIntercessOwnerId());
		praValues.put("chargeOwner", (bug.getChargeOwner()==null||"".equals(bug.getChargeOwner()))?bug.getDevOwnerId():bug.getChargeOwner());
		//"22", "关闭/撤销"
		//"23", "关闭/遗留"
		//"14", "关闭/己解决"
		//"15", "关闭/不再现"
		//"4", "无效
		//"5", "撤销"  这几种状态不设置下一处理人员
		//且当前处理人员等于下一处理人时
		if((bug.getCurrStateId()==4||bug.getCurrStateId()==5||bug.getCurrStateId()==14
				||bug.getCurrStateId()==15||bug.getCurrStateId()==22||bug.getCurrStateId()==23)
				){
			//&&bug.getCurrHandlerId().equals(this.getNextOwner(bug))
			praValues.put("nextOwnerId", "");
		}else{//在某些情况下,没有弹出选择下一人,比如改为己改时,下一人为自动为测试人员,这里要纠正一下
			if(bug.getNextOwnerId()==null||"".equals(bug.getNextOwnerId())){
				bug.setNextOwnerId(this.getNextOwner(bug));
			}
			praValues.put("nextOwnerId", bug.getNextOwnerId());
		}
		
		if(bug.getCurrFlowCd()==2){
			if(SecurityContextHolderHelp.getUserId().equals(bug.getTestOwnerId())){
				praValues.put("testOwnerId", bug.getBugReptId());
			}
		}
	}
	
	private String getNextOwner(BugBaseInfo bug){
		int flowCd = bug.getNextFlowCd();
		if(flowCd==1||flowCd==2||flowCd==8){
			return bug.getTestOwnerId();

		}else if(flowCd==3){
			return bug.getAnalyseOwnerId();
		}else if(flowCd==4){
			return bug.getAssinOwnerId();
		}else if(flowCd==5||flowCd==6){
			return bug.getDevOwnerId();
		}else if(flowCd==7){
			return bug.getIntercessOwnerId();
		}			
		return null;
	}
	public String getQueryJsonStr(Long queryId){
		StringBuffer hql = new StringBuffer("select new BugQueryInfo(queryId, onlyMe, paraValueStr,taskId,queryName)");
		hql.append(" from BugQueryInfo where queryId = ? and ownerId=?");
		String myUserId = SecurityContextHolderHelp.getUserId();
		List queryList = this.findByHql(hql.toString(), 
				queryId,myUserId);
		if(queryList==null||queryList.size()==0){
			return "failed^查询器己被删除";
		}
		BugQueryInfo queryInfo = (BugQueryInfo)queryList.get(0);
		Map parValues = queryInfo.praValueStr2Map();
		StringBuffer jsonSb = new StringBuffer();
		if(queryInfo.getTaskId()!=null){
			jsonSb.append("^appScope=0");
		}else{
			jsonSb.append("^appScope=1");
		}
		jsonSb.append("^defBugId=").append(queryInfo.getOnlyMe());
		jsonSb.append("^queryName=").append(queryInfo.getQueryName());
		Iterator it = parValues.entrySet().iterator();
		String userHql = "select new User(id,name,loginName) from User where id=? and companyId=?";
		String compId = SecurityContextHolderHelp.getCompanyId();
		User owner = null;
		while(it.hasNext()){
			Map.Entry me = (Map.Entry)it.next();
			if(me.getKey().equals("taskId")||me.getKey().equals("repEndDate")){
				continue;
			}
			if(me.getKey().equals("reptDate")){
				jsonSb.append("^").append(me.getKey()).append("F=")
				.append(StringUtils.formatShortDate((Date)me.getValue()));
				continue;
			}
			
			jsonSb.append("^").append(me.getKey()).append("F=").append(me.getValue());
			if(me.getKey().equals("testOwnerId")){
				List userList  = this.findByHql(userHql, me.getValue(),compId);
				if(userList!=null&&userList.size()>0){
					owner = (User)userList.get(0);
					jsonSb.append("^testOwnNameF=").append(owner.getLoginName()).append("(").append(owner.getName()).append(")");
				}
			}
			if(me.getKey().equals("devOwnerId")){
				List userList  = this.findByHql(userHql, me.getValue(),compId);
				if(userList!=null&&userList.size()>0){
					owner = (User)userList.get(0);
					jsonSb.append("^devOwnerNameF=").append(owner.getLoginName()).append("(").append(owner.getName()).append(")");
				}
			}
			if(me.getKey().equals("nextOwnerId")){
				List userList  = this.findByHql(userHql, me.getValue(),compId);
				if(userList!=null&&userList.size()>0){
					owner = (User)userList.get(0);
					jsonSb.append("^nextOwnerIdFName=").append(owner.getLoginName()).append("(").append(owner.getName()).append(")");
				}
			}
			if(me.getKey().equals("moduleId")){
				jsonSb.append("^moduleNameF=").append(this.getMdPathName((Long)me.getValue(), ""));
			}
		}
		
		return jsonSb.substring(1).toString();
	}
	
	public String getMdPathName(Long moduleId,String pathName){
		String hql = "select new OutlineInfo(moduleName,moduleId,moduleLevel,superModuleId) from OutlineInfo where moduleId=?";
		OutlineInfo outline = null;
		try{
			outline= (OutlineInfo)this.findByHql(hql, moduleId).get(0);
		}catch(IndexOutOfBoundsException e){
			return "";
		}
		if("".equals(pathName)){
			pathName=outline.getModuleName();
		}else{
			pathName+="/"+outline.getModuleName();
		}
		if(outline.getModuleLevel()==1){
			if(pathName.indexOf("/")>0){
				String[] pathNames = pathName.split("/");
				StringBuffer sb = new StringBuffer();
				for(int i =pathNames.length-1; i>=0; i--){
					sb.append(pathNames[i]).append("/");
				}
				return sb.substring(0,sb.length()-1).toString();
			}
			return pathName;
		}else {
			return getMdPathName(outline.getSuperModuleId(),pathName);
		}
	}
	
	
	public BugQueryInfo saveQueryInfo(BugManagerDto dto){
		
		BugQueryInfo queryInfo = this.buildQueryInfo(dto,false);
		Map praValues = queryInfo.getPraValues();
		queryInfo.setQueryId(dto.getQueryId());
		//下面的判断一定要在praValues2Str前执行
		if(dto.getDefBug()==1){
			queryInfo.setOnlyMe(1);
		}else{
			queryInfo.setOnlyMe(0);
		}
		if(dto.getAppScope()!=1){
			queryInfo.setTaskId(dto.getTaskId());
		}else{//把生成的模块条件去掉
			queryInfo.setHqlCondiStr(queryInfo.getHqlCondiStr().replace("b.moduleId=:moduleId", " 1=1 "));
			
		}
		//下面的操作必须在上面两个if后面
		queryInfo.setParaValueStr(queryInfo.praValues2Str());

		queryInfo.setHqlCondiStr(queryInfo.getHqlCondiStr().substring(queryInfo.getHqlCondiStr().indexOf("where")));
		queryInfo.setQueryName(dto.getQueryName());
		queryInfo.setOwnerId(SecurityContextHolderHelp.getUserId());
		this.saveOrUpdate(queryInfo);
		return queryInfo;
	}

	public void findInit(BugManagerDto dto){
		if(dto.getLoadType()==1){
			List<TypeDefine>  typeList = this.getBugListData();
			dto.setTypeList(typeList);
		}
		dto.setQueryList(this.getMyQuery());
		//taskId 为空说明是从我的所有BUG列表过来的查询
		if(dto.getTaskId()!=null&&!"".equals(dto.getTaskId().trim())){
			String hql =" select new TaskUseActor(userId,actor,actorId) from TaskUseActor where taskId=? and (actor=1 or actor=2 or actor=5 or actor=8) ";
			List<TaskUseActor> ActorList = this.findByHql(hql, dto.getTaskId());
			List<String> testIds = new ArrayList<String>();
			List<String> devIds = new ArrayList<String>();
			for(TaskUseActor actor :ActorList){
				if(actor.getActor()==5){
					devIds.add(actor.getUserId());
				}else{
					testIds.add(actor.getUserId());
				}
			}
			hql="select new User(id,name ,loginName) from User where id in (:ids) and 1=1";
			Map praValues = new HashMap();
			praValues.put("ids", testIds);
			List<User> testActor = this.findByHqlWithValuesMap(hql, praValues, false);
			praValues.put("ids", devIds);
			List<User> devActor = this.findByHqlWithValuesMap(hql, praValues, false);
			dto.setAttr("testActor", testActor);
			dto.setAttr("devActor", devActor);
		}
		String hqlCount = "";
		Object[] praVal = null;
		String myUserId = SecurityContextHolderHelp.getUserId();
		if(dto.getTaskId()!=null&&!"".equals(dto.getTaskId().trim())){
			hqlCount = " from BugQueryInfo where ownerId=? and (taskId =? or taskId is null)";
			praVal = new Object[]{myUserId,dto.getTaskId()};
		}else{
			hqlCount = " from BugQueryInfo where ownerId=? ";
			praVal = new Object[]{myUserId};
		}
		
		int queryCount = this.getHibernateGenericController().getResultCount(hqlCount,praVal, "queryId").intValue();
		if(dto.getBug()==null){
			BugBaseInfo bug = new BugBaseInfo();
			dto.setBug(bug);			
		}
		dto.getBug().setQueryCount(queryCount);
	}
	private List<ListObject> getMyQuery(){
		HtmlListQueryObj queryObj = new HtmlListQueryObj();
		queryObj.setKeyPropertyName("queryId");
		queryObj.setValuePropertyName("queryName");
		Map calParaValues = new HashMap();
		calParaValues.put("ownerId", SecurityContextHolderHelp.getUserId());
		queryObj.setParaValues(calParaValues);
		queryObj.setConditions(" WHERE ownerId=:ownerId ");
		queryObj.setHqlObjName(" BugQueryInfo");
		return this.getListData(queryObj, false);
	}
	public void setRelaUser(BugBaseInfo bug){
		List<String> actorIds = new ArrayList<String>();
		String myUserId = SecurityContextHolderHelp.getUserId();
		String loginName = SecurityContextHolderHelp.getLoginName();
		String name = SecurityContextHolderHelp.getMyRealName();
		if(bug.getTestOwnerId()!=null){
			if(myUserId.equals(bug.getTestOwnerId())){
				User testOwn = new User();
				testOwn.setLoginName(loginName);
				testOwn.setName(name);
				bug.setTestOwner(testOwn);
			}else{
				actorIds.add(bug.getTestOwnerId());
			}
		}
		if(bug.getChargeOwner()!=null){
			if(myUserId.equals(bug.getChargeOwner())){
				bug.setChargeOwnerName(name);
			}else{
				actorIds.add(bug.getChargeOwner());
			}
		}
		if(bug.getDevOwnerId()!=null){
			if(myUserId.equals(bug.getDevOwnerId())){
				User devOwn = new User();
				devOwn.setLoginName(loginName);
				devOwn.setName(name);
				bug.setDevOwner(devOwn);
			}else{
				if(!actorIds.contains(bug.getDevOwnerId())){
					actorIds.add(bug.getDevOwnerId());
				}
			}
		}
		if(bug.getCurrHandlerId()!=null&&(bug.getCurrStateId()==3||bug.getCurrStateId()==4||bug.getCurrStateId()==5||bug.getCurrStateId()==14
				||bug.getCurrStateId()==15||bug.getCurrStateId()==22||bug.getCurrStateId()==23)){
			if(myUserId.equals(bug.getCurrHandlerId())){
				User currOwn = new User();
				currOwn.setLoginName(loginName);
				currOwn.setName(name);
				bug.setCurrHander(currOwn);
			}else{			
				if(!actorIds.contains(bug.getCurrHandlerId())){
					actorIds.add(bug.getCurrHandlerId());
				}
			}
		}else if(bug.getAnalyseOwnerId()!=null&&bug.getNextFlowCd()==3){
			if(myUserId.equals(bug.getAnalyseOwnerId())){
				User analysOwn = new User();
				analysOwn.setLoginName(loginName);
				analysOwn.setName(name);
				bug.setAnalysOwner(analysOwn);
			}else{	
				if(!actorIds.contains(bug.getAnalyseOwnerId())){
					actorIds.add(bug.getAnalyseOwnerId());
				}	
			}
		}else if(bug.getAssinOwnerId()!=null&&bug.getNextFlowCd()==4){
			if(myUserId.equals(bug.getAssinOwnerId())){
				User ansnOwn = new User();
				ansnOwn.setLoginName(loginName);
				ansnOwn.setName(name);
				bug.setAssinOwner(ansnOwn);
			}else{
				if(!actorIds.contains(bug.getAssinOwnerId())){
					actorIds.add(bug.getAssinOwnerId());
				}
			}
		}else if(bug.getIntercessOwnerId()!=null&&bug.getNextFlowCd()==7){
			if(myUserId.equals(bug.getIntercessOwnerId())){
				User inteOwn = new User();
				inteOwn.setLoginName(loginName);
				inteOwn.setName(name);
				bug.setIntecesOwner(inteOwn);
			}else{
				if(!actorIds.contains(bug.getIntercessOwnerId())){
				actorIds.add(bug.getIntercessOwnerId());
				}
			}
		}
		if(myUserId.equals(bug.getBugReptId())){
			User auther = new User();
			auther.setLoginName(loginName);
			auther.setName(name);
			bug.setAuthor(auther);
		}else{
			if(!actorIds.contains(bug.getBugReptId())){
				actorIds.add(bug.getBugReptId());
			}
		}
		
		if(actorIds.size()==0){
			return ;
		}
		String hql="select new User(id,name ,loginName) from User where id in (:ids)";
		Map praValues = new HashMap(1);
		this.sortStringList(actorIds);
		praValues.put("ids", actorIds);
		List<User> actorUsers = this.findByHqlWithValuesMap(hql, praValues, false);
		for(User actor:actorUsers){
			if(actor.getId().equals(bug.getTestOwnerId())){
				bug.setTestOwner(actor);
			}
			if(actor.getId().equals(bug.getCurrHandlerId())){
				bug.setCurrHander(actor);
			}
			if(actor.getId().equals(bug.getDevOwnerId())){
				bug.setDevOwner(actor);
			}
			if(actor.getId().equals(bug.getAnalyseOwnerId())){
				bug.setAnalysOwner(actor);
			}
			if(actor.getId().equals(bug.getAssinOwnerId())){
				bug.setAssinOwner(actor);
			}
			if(actor.getId().equals(bug.getIntercessOwnerId())){
				bug.setIntecesOwner(actor);
			}
			if(actor.getId().equals(bug.getBugReptId())){
				bug.setAuthor(actor);
			}
			if(actor.getId().equals(bug.getCurrHandlerId())){
				bug.setCurrHander(actor);
			}
			if(actor.getId().equals(bug.getChargeOwner())){
				bug.setChargeOwnerName(actor.getName());
			}
		}
	}

	public void setRelaType(BugBaseInfo bug){
		String compId = SecurityContextHolderHelp.getCompanyId();
		String hql = "from TypeDefine where (compId=? or compId=1) and typeId=?";
		if(bug.getBugFreqId()!=null){
			BugFreq bugFreq = (BugFreq)this.findByHql(hql, compId,bug.getBugFreqId()).get(0);
			bug.setBugFreq(bugFreq);
		}
		if(bug.getBugGradeId()!=null){
			BugGrade bugGrade = (BugGrade)this.findByHql(hql, compId,bug.getBugGradeId()).get(0);
			bug.setBugGrade(bugGrade);
		}
		if(bug.getBugOccaId()!=null){
			BugOpotunity bugOpotunity = (BugOpotunity)this.findByHql(hql, compId,bug.getBugOccaId()).get(0);
			bug.setBugOpotunity(bugOpotunity);
		}
		if(bug.getPriId()!=null){
			BugPri bugPri = (BugPri)this.findByHql(hql, compId,bug.getPriId()).get(0);
			bug.setBugPri(bugPri);
		}

		 if(bug.getGenePhaseId()!=null){
			 ImportPhase importPhase = (ImportPhase)this.findByHql(hql, compId,bug.getGenePhaseId()).get(0);
			 bug.setImportPhase(importPhase);
		 }
		 if(bug.getBugTypeId()!=null){
			 BugType bugType = (BugType)this.findByHql(hql, compId,bug.getBugTypeId()).get(0);
			 bug.setBugType(bugType);
		 }
		 if(bug.getSourceId()!=null){
			 BugSource bugSource = (BugSource)this.findByHql(hql, compId,bug.getSourceId()).get(0);
			 bug.setBugSource(bugSource);
		 }
		 if(bug.getPlatformId()!=null){
			 OccurPlant occurPlant = (OccurPlant)this.findByHql(hql, compId,bug.getPlatformId()).get(0);
			 bug.setOccurPlant(occurPlant);
		 }

		 if(bug.getGeneCauseId()!=null){
			 GeneCause geneCause = (GeneCause)this.findByHql(hql, compId,bug.getGeneCauseId()).get(0);
			 bug.setGeneCause(geneCause);
		 }
	}
	public void initBugListDate(BugManagerDto dto){
		List<TypeDefine> typeList = dto.getTypeList();
		if(typeList==null){
			return;
		}
		StringBuffer bugTypeSb = new StringBuffer();
		StringBuffer bugGradeSb = new StringBuffer();
		StringBuffer bugFreqSb = new StringBuffer();
		StringBuffer ugOpotSb = new StringBuffer();
		StringBuffer geneCauseSb = new StringBuffer();
		StringBuffer BugSrcSb = new StringBuffer();
		StringBuffer pltFormSb = new StringBuffer();
		StringBuffer bugPriSb = new StringBuffer();
		StringBuffer impPhaSb = new StringBuffer();
		TypeDefine bugType=null;
		TypeDefine bugGrade=null;
		TypeDefine bugFreq=null;
		TypeDefine ugOpot=null;
		TypeDefine geneCause=null;
		TypeDefine bugSrc=null;
		TypeDefine pltForm=null;
		TypeDefine bugPri=null;
		TypeDefine impPha=null;
		for(TypeDefine td :typeList){
			if( td instanceof BugType){
				if(td.getPreference()!=null && td.getPreference()==1){
					bugType = td;
				}
				bugTypeSb.append("$").append(td.getTypeId()).append(";").append(td.getTypeName());
			}else if(td instanceof BugGrade){
				if(td.getPreference()!=null &&td.getPreference()==1){
					bugGrade = td;
				}
				bugGradeSb.append("$").append(td.getTypeId()).append(";").append(td.getTypeName());
			}else if(td instanceof BugOpotunity){
				if(td.getPreference()!=null &&td.getPreference()==1){
					ugOpot = td;
				}
				ugOpotSb.append("$").append(td.getTypeId()).append(";").append(td.getTypeName());
			}else if(td instanceof BugFreq){
				if(td.getPreference()!=null &&td.getPreference()==1){
					bugFreq = td;
				}
				bugFreqSb.append("$").append(td.getTypeId()).append(";").append(td.getTypeName());
			}else if(td instanceof BugPri){
				if(td.getPreference()!=null &&td.getPreference()==1){
					bugPri = td;
				}
				bugPriSb.append("$").append(td.getTypeId()).append(";").append(td.getTypeName());
			}else if(td instanceof GeneCause){
				if(td.getPreference()!=null &&td.getPreference()==1){
					geneCause = td;
				}
				geneCauseSb.append("$").append(td.getTypeId()).append(";").append(td.getTypeName());
			}else if(td instanceof OccurPlant){
				if(td.getPreference()!=null &&td.getPreference()==1){
					pltForm = td;
				}
				pltFormSb.append("$").append(td.getTypeId()).append(";").append(td.getTypeName());
			}else if(td instanceof BugSource){
				if(td.getPreference()!=null &&td.getPreference()==1){
					bugSrc = td;
				}
				BugSrcSb.append("$").append(td.getTypeId()).append(";").append(td.getTypeName());
			}else if(td instanceof ImportPhase){
				if(td.getPreference()!=null &&td.getPreference()==1){
					impPha = td;
				}
				impPhaSb.append("$").append(td.getTypeId()).append(";").append(td.getTypeName());
			}
		}
		
		if(bugType!=null){
			bugTypeSb.append("$").append("def_"+bugType.getTypeId()).append(";").append(bugType.getTypeName());
		}
		if(bugGrade!=null){
			bugGradeSb.append("$").append("def_"+bugGrade.getTypeId()).append(";").append(bugGrade.getTypeName());
		}
		if(bugFreq!=null){
			bugFreqSb.append("$").append("def_"+bugFreq.getTypeId()).append(";").append(bugFreq.getTypeName());
		}
		if(ugOpot!=null){
			ugOpotSb.append("$").append("def_"+ugOpot.getTypeId()).append(";").append(ugOpot.getTypeName());
		}
		if(geneCause!=null){
			geneCauseSb.append("$").append("def_"+geneCause.getTypeId()).append(";").append(geneCause.getTypeName());
		}
		if(bugSrc!=null){
			BugSrcSb.append("$").append("def_"+bugSrc.getTypeId()).append(";").append(bugSrc.getTypeName());
		}
		if(pltForm!=null){
			pltFormSb.append("$").append("def_"+pltForm.getTypeId()).append(";").append(pltForm.getTypeName());
		}
		if(bugPri!=null){
			bugPriSb.append("$").append("def_"+bugPri.getTypeId()).append(";").append(bugPri.getTypeName());
		}
		if(impPha!=null){
			impPhaSb.append("$").append("def_"+impPha.getTypeId()).append(";").append(impPha.getTypeName());
		}
		
		dto.getBug().setTypeSelStr(bugTypeSb.toString().equals("")?"":bugTypeSb.substring(1));
		dto.getBug().setGradeSelStr(bugGradeSb.toString().equals("")?"":bugGradeSb.substring(1));
		dto.getBug().setFreqSelStr(bugFreqSb.toString().equals("")?"":bugFreqSb.substring(1));
		dto.getBug().setOccaSelStr(ugOpotSb.toString().equals("")?"":ugOpotSb.substring(1));
		dto.getBug().setPriSelStr(bugPriSb.toString().equals("")?"":bugPriSb.substring(1));
		dto.getBug().setGeCaseSelStr(geneCauseSb.toString().equals("")?"":geneCauseSb.substring(1));
		dto.getBug().setPlantFormSelStr(pltFormSb.toString().equals("")?"":pltFormSb.substring(1));
		dto.getBug().setSourceSelStr(BugSrcSb.toString().equals("")?"":BugSrcSb.substring(1));
		dto.getBug().setGenePhaseSelStr(impPhaSb.toString().equals("")?"":impPhaSb.substring(1));
	}
	
	public void setActorListData(CurrTaskInfo currTaskInfo,BugBaseInfo bug){
		String hql = "select new  User(id,name ,loginName) from User where id in(:ids)";
		List<String> ids = new ArrayList<String>();
		String[] actorIds = currTaskInfo.getTestAndLdStr().split(",");
		for(String actorId :actorIds){
			ids.add(actorId);
		}
		Map praValuesMap = new HashMap();
		praValuesMap.put("ids", ids);
		List<User> actors = this.findByHqlWithValuesMap(hql, praValuesMap, false);
		StringBuffer sb = new StringBuffer();
		int i = 1;
		for(User actor:actors){
			sb.append(actor.getId()).append(";").append(actor.getLoginName()).append("(").append(actor.getName()).append(")");
			if(i!=actors.size()){
				sb.append("$");
			}
			i++;
		}
		bug.getDtoHelper().setTestLdIdSelStr(sb.toString());
		if(currTaskInfo.getTestFlow().indexOf("2")>=0&&currTaskInfo.getTestChkAndLdStr()!=""){
			i=1;
			ids.clear();
			actors.clear();
			sb = new StringBuffer();
			actorIds = currTaskInfo.getTestChkAndLdStr().split(",");
			for(String actorId :actorIds){
				ids.add(actorId);
			}
			praValuesMap.put("ids", ids);
			actors = this.findByHqlWithValuesMap(hql, praValuesMap, false);
			for(User actor:actors){
				sb.append(actor.getId()).append(";").append(actor.getLoginName()).append("(").append(actor.getName()).append(")");
				//sb.append(actor.getId()).append(";").append(actor.getName()).append("(").append(actor.getLoginName()).append(")");
				if(i!=actors.size()){
					sb.append("$");
				}
				i++;
			}
			bug.getDtoHelper().setTestChkIdSelStr(sb.toString());
		}
		if(currTaskInfo.getTestFlow().indexOf("3")>=0&&currTaskInfo.getAnalysIdStr()!=""){
			i=1;
			ids.clear();
			actors.clear();
			sb = new StringBuffer();
			actorIds = currTaskInfo.getAnalysIdStr().split(",");
			for(String actorId :actorIds){
				ids.add(actorId);
			}
			praValuesMap.put("ids", ids);
			actors = this.findByHqlWithValuesMap(hql, praValuesMap, false);
			for(User actor:actors){
				sb.append(actor.getId()).append(";").append(actor.getLoginName()).append("(").append(actor.getName()).append(")");
				//sb.append(actor.getId()).append(";").append(actor.getName()).append("(").append(actor.getLoginName()).append(")");
				if(i!=actors.size()){
					sb.append("$");
				}
				i++;
			}
			bug.getDtoHelper().setAnalySelStr(sb.toString());
		}
		
		if(currTaskInfo.getTestFlow().indexOf("4")>=0&&currTaskInfo.getAssinIdStr()!=""){
			i=1;
			ids.clear();
			actors.clear();
			sb = new StringBuffer();
			actorIds = currTaskInfo.getAssinIdStr().split(",");
			for(String actorId :actorIds){
				ids.add(actorId);
			}
			praValuesMap.put("ids", ids);
			actors = this.findByHqlWithValuesMap(hql, praValuesMap, false);
			for(User actor:actors){
				sb.append(actor.getId()).append(";").append(actor.getLoginName()).append("(").append(actor.getName()).append(")");
				//sb.append(actor.getId()).append(";").append(actor.getName()).append("(").append(actor.getLoginName()).append(")");
				if(i!=actors.size()){
					sb.append("$");
				}
				i++;
			}
			bug.getDtoHelper().setAssignSelStr(sb.toString());
		}
		i=1;
		ids.clear();
		actors.clear();
		sb = new StringBuffer();
		actorIds = currTaskInfo.getDevIdStr().split(",");
		for(String actorId :actorIds){
			ids.add(actorId);
		}
		praValuesMap.put("ids", ids);
		actors = this.findByHqlWithValuesMap(hql, praValuesMap, false);
		for(User actor:actors){
			sb.append(actor.getId()).append(";").append(actor.getLoginName()).append("(").append(actor.getName()).append(")");
			//sb.append(actor.getId()).append(";").append(actor.getName()).append("(").append(actor.getLoginName()).append(")");
			if(i!=actors.size()){
				sb.append("$");
			}
			i++;
		}
		bug.getDtoHelper().setDevStr(sb.toString());	
		
		if(currTaskInfo.getTestFlow().indexOf("6")>=0&&currTaskInfo.getDevChkIdStr()!=""){
			i=1;
			ids.clear();
			actors.clear();
			sb = new StringBuffer();
			actorIds = currTaskInfo.getDevChkIdStr().split(",");
			for(String actorId :actorIds){
				ids.add(actorId);
			}
			praValuesMap.put("ids", ids);
			actors = this.findByHqlWithValuesMap(hql, praValuesMap, false);
			for(User actor:actors){
				sb.append(actor.getId()).append(";").append(actor.getLoginName()).append("(").append(actor.getName()).append(")");
				//sb.append(actor.getId()).append(";").append(actor.getName()).append("(").append(actor.getLoginName()).append(")");
				if(i!=actors.size()){
					sb.append("$");
				}
				i++;
			}
			bug.getDtoHelper().setDevChkIdSelStr(sb.toString());
		}
		i=1;
		ids.clear();
		actors.clear();
		sb = new StringBuffer();
		actorIds = currTaskInfo.getIntercsIdStr().split(",");
		for(String actorId :actorIds){
			ids.add(actorId);
		}
		praValuesMap.put("ids", ids);
		actors = this.findByHqlWithValuesMap(hql, praValuesMap, false);
		for(User actor:actors){
			sb.append(actor.getId()).append(";").append(actor.getLoginName()).append("(").append(actor.getName()).append(")");
			//sb.append(actor.getId()).append(";").append(actor.getName()).append("(").append(actor.getLoginName()).append(")");
			if(i!=actors.size()){
				sb.append("$");
			}
			i++;
		}
		bug.getDtoHelper().setInterCesSelStr(sb.toString());
	}
	public List<TypeDefine>  getBugListData(){
		String hql = "from TypeDefine where (compId=? or compId=1) and status=1";
		String compId = SecurityContextHolderHelp.getCompanyId();
		List<TypeDefine> typeList = this.findByHql(hql, compId);
		return typeList;
	}
	public List<BugBaseInfo> findByQuery(BugManagerDto dto){
		
		BugQueryInfo queryInfo = (BugQueryInfo)dto.getQueryInfo();
		if(queryInfo.getTaskId()==null){
			queryInfo.setHqlCondiStr(queryInfo.getHqlSelectData() +queryInfo.getHqlCondiStr().replaceFirst("b.taskId=:taskId", "1=1"));
		}else{
			queryInfo.setHqlCondiStr(queryInfo.getHqlSelectData() +queryInfo.getHqlCondiStr());
		}
		
		if(queryInfo.getTaskId()==null){
			queryInfo.setOnlyMyCondSec(this.appendAllMyBugHqlByQuery(dto).toString());
		}else{
			queryInfo.setOnlyMyCondSec(this.appendMyBugHqlByQuery(dto).toString());
			if(queryInfo.getPraValues().get("taskId")==null){
				queryInfo.getPraValues().put("taskId", SecurityContextHolderHelp.getCurrTaksId());
			}
		}
		
		queryInfo.setCurrPraValues(queryInfo.getPraValues());
		return this.findBug(dto);
	}
	public List<BugBaseInfo> findBug(BugManagerDto dto){
		BugQueryInfo queryInfo =this.buildQueryInfo(dto,false);
		List<BugBaseInfo> list= this.findByHqlWithValuesMap(queryInfo.getCurrHql(), dto.getPageNo(), 
				dto.getPageSize(), "bugId", queryInfo.getCurrPraValues(), false);
		if(dto.getSaveQuery()==1){
			queryInfo.setOnlyMe(dto.getAppScope());
			queryInfo.setOnlyMe(dto.getDefBug());
			queryInfo.setQueryName(dto.getQueryName());
			queryInfo.setOwnerId(SecurityContextHolderHelp.getUserId());
			dto.setQueryInfo(queryInfo);
		}
		if(list==null||list.size()==0){
			return null;
		}		
		dto.setQueryId(queryInfo.getQueryId());
		String hql = "select new cn.com.codes.object.DefaultTypeDefine(typeId,typeName) from TypeDefine where compId=? or compId=1";
		String compId = SecurityContextHolderHelp.getCompanyId();
		Long typeCount = this.getHibernateGenericController().getResultCount(hql, new Object[]{compId},"typeId");
		if(typeCount<100){
			hql = "from TypeDefine where compId=? or compId=1";
			List<TypeDefine> typeList = this.findByHql(hql, compId);
			dto.setTypeList(typeList);
		}
		return list;
	}
	
	public Map<String ,TypeDefine > convertTdMap(BugManagerDto dto){
		Map<String ,TypeDefine> map = new HashMap<String ,TypeDefine>();
		List<TypeDefine> typeList = dto.getTypeList();
		StringBuffer bugTypeSb = null ;
		StringBuffer bugGradeSb = null ;
		StringBuffer bugFreqSb = null ;
		StringBuffer ugOpotSb = null ;
		StringBuffer geneCauseSb = null ;
		StringBuffer BugSrcSb = null ;
		StringBuffer pltFormSb = null ;
		StringBuffer bugPriSb = null ;
		StringBuffer impPhaSb = null ;
		if(dto.getLoadType()==1){
			bugTypeSb = new StringBuffer();
			bugGradeSb = new StringBuffer();
			bugFreqSb = new StringBuffer();
			ugOpotSb = new StringBuffer();
			geneCauseSb = new StringBuffer();
			BugSrcSb = new StringBuffer();
			pltFormSb = new StringBuffer();
			bugPriSb = new StringBuffer();
			impPhaSb = new StringBuffer();
		}

		for(TypeDefine td :typeList){
			if( td instanceof BugType){
				map.put("BugType"+td.getTypeId(), td);
				if(dto.getLoadType()==1){
					bugTypeSb.append("$").append(td.getTypeId()).append(";").append(td.getTypeName());
				}
			}else if(td instanceof BugGrade){
				map.put("BugGrade"+td.getTypeId(), td);
				if(dto.getLoadType()==1){
					bugGradeSb.append("$").append(td.getTypeId()).append(";").append(td.getTypeName());
				}
			}else if(td instanceof BugOpotunity){
				map.put("BugOpotunity"+td.getTypeId(), td);
				if(dto.getLoadType()==1){
					ugOpotSb.append("$").append(td.getTypeId()).append(";").append(td.getTypeName());
				}
			}else if(td instanceof BugFreq){
				map.put("BugFreq"+td.getTypeId(), td);
				if(dto.getLoadType()==1){
					bugFreqSb.append("$").append(td.getTypeId()).append(";").append(td.getTypeName());
				}
			}else if(td instanceof BugPri){
				map.put("BugPri"+td.getTypeId(), td);
				if(dto.getLoadType()==1){
					bugPriSb.append("$").append(td.getTypeId()).append(";").append(td.getTypeName());
				}
			}else if(td instanceof GeneCause){
				map.put("GeneCause"+td.getTypeId(), td);
				if(dto.getLoadType()==1){
					geneCauseSb.append("$").append(td.getTypeId()).append(";").append(td.getTypeName());
				}
			}else if(td instanceof OccurPlant){
				map.put("OccurPlant"+td.getTypeId(), td);
				if(dto.getLoadType()==1){
					pltFormSb.append("$").append(td.getTypeId()).append(";").append(td.getTypeName());
				}
			}else if(td instanceof BugSource){
				map.put("BugSource"+td.getTypeId(), td);
				if(dto.getLoadType()==1){
					BugSrcSb.append("$").append(td.getTypeId()).append(";").append(td.getTypeName());
				}
			}else if(td instanceof ImportPhase){
				map.put("ImportPhase"+td.getTypeId(), td);
				if(dto.getLoadType()==1){
					impPhaSb.append("$").append(td.getTypeId()).append(";").append(td.getTypeName());
				}
			}
		}
		if(dto.getLoadType()==1){
			dto.getBug().setTypeSelStr(bugTypeSb.toString().equals("")?"":bugTypeSb.substring(1));
			dto.getBug().setGradeSelStr(bugGradeSb.toString().equals("")?"":bugGradeSb.substring(1));
			dto.getBug().setFreqSelStr(bugFreqSb.toString().equals("")?"":bugFreqSb.substring(1));
			dto.getBug().setOccaSelStr(ugOpotSb.toString().equals("")?"":ugOpotSb.substring(1));
			dto.getBug().setPriSelStr(bugPriSb.toString().equals("")?"":bugPriSb.substring(1));
			dto.getBug().setGeCaseSelStr(geneCauseSb.toString().equals("")?"":geneCauseSb.substring(1));
			dto.getBug().setPlantFormSelStr(pltFormSb.toString().equals("")?"":pltFormSb.substring(1));
			dto.getBug().setSourceSelStr(BugSrcSb.toString().equals("")?"":BugSrcSb.substring(1));
			dto.getBug().setGenePhaseSelStr(impPhaSb.toString().equals("")?"":impPhaSb.substring(1));
		}
		return map;
	}
	public void setBugsRelaTypeDefine(Map<String ,TypeDefine > tdMap ,List<BugBaseInfo> bugs){
		if(tdMap!=null&&tdMap.isEmpty()){
			return;
		}
		if(bugs!=null&&bugs.size()>0){
			for(BugBaseInfo bug :bugs){
				bug.setBugGrade((BugGrade)tdMap.get("BugGrade"+bug.getBugGradeId()));
				bug.setBugFreq((BugFreq)tdMap.get("BugFreq"+bug.getBugFreqId()));
				bug.setBugOpotunity((BugOpotunity)tdMap.get("BugOpotunity"+bug.getBugOccaId()));
				bug.setBugType((BugType)tdMap.get("BugType"+bug.getBugTypeId()));
				bug.setBugPri((BugPri)tdMap.get("BugPri"+bug.getPriId()));
				bug.setGeneCause((GeneCause)tdMap.get("GeneCause"+bug.getBugGradeId()));
			}
		}
	}

	
	/**
	 * 
	 * @param dto
	 * @param allTestTask 是否是从所有测试项目中查询
	 * @return
	 */
	private BugQueryInfo buildQueryInfo(BugManagerDto dto,boolean allTestTask){
		if(dto.getQueryInfo()!=null){
			return (BugQueryInfo)dto.getQueryInfo();
		}
		BugQueryInfo queryInfo = new BugQueryInfo();
		queryInfo.setOnlyMe(0);
		StringBuffer hql = new StringBuffer("select new BugBaseInfo(");
		hql.append(" bugId,bugDesc,bugGradeId,bugFreqId,bugOccaId,")
		   .append(" bugTypeId,priId, devOwnerId,reptDate,msgFlag,")
		   .append(" relaCaseFlag, moduleId, testOwnerId,currFlowCd,")
		   .append(" currHandlerId,currStateId,nextFlowCd,bugReptId,taskId)");
		Map praValues = new HashMap();
		hql.append(" from BugBaseInfo b where 1=1 ");
        boolean inputCondition = false;
		if(dto.getBug()!=null){
			String chargeOwnerId = dto.getBug().getChargeOwner();
			if(chargeOwnerId!=null&&!"".equals(chargeOwnerId.trim())){
				hql.append(" and b.chargeOwner=:chargeOwnerId");
				praValues.put("chargeOwnerId", chargeOwnerId);
				inputCondition = true;
			}
			String testOwnerId = dto.getBug().getTestOwnerId();
			if(testOwnerId!=null &&!"-1".equals(testOwnerId)&&!"".equals(testOwnerId)){
				hql.append(" and b.testOwnerId=:testOwnerId");
				praValues.put("testOwnerId", testOwnerId);
				inputCondition = true;
			}
			Date reptDate = dto.getBug().getReptDate();
			if(reptDate!=null){
				hql.append(" and b.reptDate>=:reptDate and b.reptDate<:repEndDate");
				praValues.put("reptDate", reptDate);	
				
				Calendar calender = Calendar.getInstance();
				if(dto.getReptDateEnd()==null){
					calender.setTime(reptDate);
				}else{
					calender.setTime(dto.getReptDateEnd());
				}
				calender.add(Calendar.DAY_OF_YEAR, 1);
				Date repEndDate = calender.getTime();
				praValues.put("repEndDate", repEndDate);
				inputCondition = true;
			}		
			Long platformId = dto.getBug().getPlatformId();
			if(platformId!=null&&platformId!=-1){
				hql.append(" and b.platformId=:platformId");
				praValues.put("platformId", platformId);	
				inputCondition = true;
			}
			Long moduleId = dto.getBug().getModuleId();
			if(moduleId!=null&&!"".equals(moduleId.toString())&&!"-1".equals(moduleId.toString())){
				hql.append(" and b.moduleId=:moduleId");
				praValues.put("moduleId", moduleId);
				inputCondition = true;
			}
			String moduleNum = dto.getBug().getModuleNum();
			if(moduleNum!=null&&!"".equals(moduleNum.toString())){
				hql.append(" and b.moduleNum like :moduleNum");
				praValues.put("moduleNum", moduleNum +"%");
				inputCondition = true;
			}
			Long gradeId = dto.getBug().getBugGradeId();
			if(gradeId!=null&&gradeId!=-1){
				hql.append(" and b.bugGradeId=:bugGradeId");
				praValues.put("bugGradeId", gradeId);
				inputCondition = true;
			}
			Long bugFreqId = dto.getBug().getBugFreqId();
			if(bugFreqId!=null&&bugFreqId!=-1){
				hql.append(" and b.bugFreqId=:bugFreqId");
				praValues.put("bugFreqId", bugFreqId);
				inputCondition = true;
			}
			Long bugOccaId = dto.getBug().getBugOccaId();
			if(bugOccaId!=null&&bugOccaId!=-1){
				hql.append(" and b.bugOccaId=:bugOccaId");
				praValues.put("bugOccaId", bugOccaId);
				inputCondition = true;
			}	
//			Integer currStateId = dto.getBug().getCurrStateId();
//			if(currStateId!=null&&currStateId!=-1){
//				hql.append(" and b.currStateId=:currStateId");
//				praValues.put("currStateId", currStateId);
//				inputCondition = true;
//			}
			Long priId = dto.getBug().getPriId();
			if(priId!=null&&priId!=-1){
				hql.append(" and b.priId=:priId");
				praValues.put("priId", priId);
				inputCondition = true;
			}
			Long sourceId = dto.getBug().getSourceId();
			if(sourceId!=null&&sourceId!=-1){
				hql.append(" and b.sourceId=:sourceId");
				praValues.put("sourceId", sourceId);
				inputCondition = true;
			}
			Long genePhaseId = dto.getBug().getGenePhaseId();
			if(genePhaseId!=null&&genePhaseId!=-1){
				hql.append(" and b.genePhaseId=:genePhaseId");
				praValues.put("genePhaseId", genePhaseId);
				inputCondition = true;
			}
			Integer relaCaseFlag = dto.getBug().getRelaCaseFlag();
			if(relaCaseFlag!=null&&relaCaseFlag!=-1){
				inputCondition = true;
				if(relaCaseFlag==1){
					hql.append(" and b.relaCaseFlag>=1");
				}else{
					hql.append(" and b.relaCaseFlag<1");
				}
			}			
			Long geneCauseId = dto.getBug().getGeneCauseId();
			if(geneCauseId!=null&&geneCauseId!=-1){
				hql.append(" and b.geneCauseId=:geneCauseId");
				praValues.put("geneCauseId", geneCauseId);
				inputCondition = true;
			}
			Long bugTypeId = dto.getBug().getBugTypeId();
			if(bugTypeId!=null&&bugTypeId!=-1){
				hql.append(" and b.bugTypeId=:bugTypeId");
				praValues.put("bugTypeId", bugTypeId);
				inputCondition = true;
			}		
			String devOwnerId = dto.getBug().getDevOwnerId();
			if(devOwnerId!=null&&!"-1".equals(devOwnerId)&&!"".equals(devOwnerId)){
				hql.append(" and b.devOwnerId=:devOwnerId");
				praValues.put("devOwnerId", devOwnerId);
				inputCondition = true;
			}
			String nextOwnerId = dto.getBug().getNextOwnerId();
			if(nextOwnerId!=null&&!"-1".equals(nextOwnerId)&&!"".equals(nextOwnerId)){
				hql.append(" and b.nextOwnerId=:nextOwnerId ");
				praValues.put("nextOwnerId", nextOwnerId);
			}
			String bugDesc = dto.getBug().getBugDesc();
			if(bugDesc!=null&&!bugDesc.trim().equals("")){
				hql.append(" and (b.bugDesc like :bugDesc or b.reProStep like :bugDesc)");
				praValues.put("bugDesc", "%"+bugDesc+"%");
				inputCondition = true;
				
			}
			Long bugReptVer = dto.getBug().getBugReptVer();
			//if(bugReptVer!=null&&bugReptVer!=-1&&!"".equals(devOwnerId)){
			if(bugReptVer!=null&&bugReptVer!=-1){
				hql.append(" and b.bugReptVer=:bugReptVer");
				praValues.put("bugReptVer", bugReptVer);
				inputCondition = true;
			}
			if(dto.getDefBug()==1){
				queryInfo.setOnlyMe(1);
				Map queryPraValues = new HashMap();
				queryPraValues.putAll(praValues);
				queryInfo.setPraValues(queryPraValues);
				inputCondition = true;
			}else {
				queryInfo.setPraValues(praValues);
			}
		}else{
			if(dto.getReptDateEnd()!=null){
				hql.append(" and b.reptDate>=:reptDate and b.reptDate<:repEndDate");
				praValues.put("reptDate", dto.getReptDateEnd());	
				Calendar calender = Calendar.getInstance();
				calender.setTime(dto.getReptDateEnd());
				calender.add(Calendar.DAY_OF_YEAR, 1);
				Date repEndDate = calender.getTime();
				praValues.put("repEndDate", repEndDate);
				inputCondition = true;
			}
		}
		Integer[] currStateIds = null;
		if(dto.getCurrStateIds()!=null){
			currStateIds = dto.getCurrStateIds();
		}
		if(currStateIds!=null){
			if(currStateIds.length==1&& currStateIds[0].intValue()!=-1){
				hql.append(" and b.currStateId=:currStateId");
				praValues.put("currStateId", currStateIds[0]);
				inputCondition = true;
			}else if(currStateIds.length>1){
				
				List states = new ArrayList ();
				for(Integer currState :currStateIds){
					if(currState.intValue()!=-1){
						states.add(currState);
					}

				}
				if(states.size()==1){
					hql.append(" and b.currStateId=:currStateId");
					praValues.put("currStateId", states.get(0));
				}else{
					hql.append(" and b.currStateId in(:currStateId)");
					praValues.put("currStateId", states);
				}
				
				inputCondition = true;
			}
			
		}
		if(!allTestTask){
			hql.append("  and b.taskId=:taskId");
			praValues.put("taskId", dto.getTaskId());
		}
		
		if(!StringUtils.isNullOrEmpty(dto.getBugJoinId())){
			List<Long> bugJoins = new ArrayList<Long>();
			String[] bugIds = dto.getBugJoinId().split(" ");
			for(int i=0;i<bugIds.length;i++){
				bugJoins.add(Long.valueOf(bugIds[i]));
			}
			
			hql.append(" and b.bugId not in (:bugIdXs) ");
			praValues.put("bugIdXs", bugJoins);
		}
		
		dto.setHqlParamMaps(praValues);
		StringBuffer onlyMySb = null;
		if(!allTestTask&&!dto.isAllTestTask()){
			onlyMySb = this.appendMyBugHql(dto);
		}else{
			if(dto.getBug()!=null&&!inputCondition ){ //不为真说明没输入任何查询条件,要调appendAllMyBugHql
				List<String> taskIds  = null;
				if(dto.getTaskId()!=null){
					taskIds = new ArrayList(1);
					taskIds.add(dto.getTaskId());
				}else{
					taskIds = this.getMyTestTask();
				}
				
				if(taskIds==null||taskIds.isEmpty()){//参与的项目为空
					//放一个不存在的项目，以过滤数据
					hql.append("  and b.taskId=:taskId");
					praValues.put("taskId", "liuyg");
					onlyMySb = this.appendAllMyBugHql(dto);
				}else{
					onlyMySb = new StringBuffer(" and b.taskId in(:tasks)   ");
					praValues.put("tasks", taskIds);
					if(dto.getTaskId()==null&&dto.getDefBug()==1){
						onlyMySb.append(this.appendAllMyBugHql(dto).toString());
					}
				}
			}else{
				List<String> taskIds  = null;
				if(dto.getTaskId()!=null){
					taskIds = new ArrayList(1);
					taskIds.add(dto.getTaskId());
					onlyMySb = new StringBuffer(" and b.taskId in(:tasks)  ");
					praValues.put("tasks", taskIds);
					onlyMySb.append(this.appendAllMyBugHql(dto).toString());
				}else if(dto.isAllTestTask()){
					taskIds = this.getMyTestTask();
					if(taskIds==null||taskIds.isEmpty()){//参与的项目为空
						//放一个不存在的项目，以过滤数据
						hql.append("  and b.taskId=:taskId");
						praValues.put("taskId", "liuyg");
						onlyMySb = this.appendAllMyBugHql(dto);
					}else{
						onlyMySb = new StringBuffer(" and b.taskId in(:tasks)   ");
						praValues.put("tasks", taskIds);
					}
					if(dto.getTaskId()==null&&dto.getDefBug()==1){
						onlyMySb.append(this.appendAllMyBugHql(dto).toString());
					}
				}else{
					onlyMySb = this.appendAllMyBugHql(dto);
				}
			}
		}
		//因为可以查询的同时也保存查器，在这里设置的HQL含select子句，
		//如保存，在保存时再截取
		queryInfo.setOnlyMyCondSec(onlyMySb.toString() +"  order by b.currHandlDate desc ");
		queryInfo.setHqlCondiStr(hql.toString());
		queryInfo.setCurrPraValues(praValues);
		return queryInfo;
	}
	
	//下面的方法是优化时重写了，实际和appendAllMyBugHql一查一样了，原来查询果要判断流程有及角色，现在加了一个字段nextOwnerId后
	//不需要再判断了
	private StringBuffer  appendMyBugHql(BugManagerDto dto){
		StringBuffer sb = new StringBuffer();
		if(dto.getDefBug()==1){
			Map praValues = dto.getHqlParamMaps();
			sb.append(" and ( (b.currHandlerId=:ownerId and b.currStateId not in(4,5,14,15,22,23))or ") ;
			sb.append("  b.nextOwnerId=:ownerId ");			
			sb.append("  ) ") ;
			String currUserId = SecurityContextHolderHelp.getUserId();
			praValues.put("ownerId", currUserId);
			return sb;
		}
		return sb;
	}

	
	private StringBuffer  appendMyBugHqlByQuery(BugManagerDto dto){
		StringBuffer sb = new StringBuffer();
		if(dto.getDefBug()==1){
			Map praValues = dto.getHqlParamMaps();
			sb.append(" and ( (b.currHandlerId=:ownerId and b.currStateId not in(4,5,14,15,22,23))or ") ;
			sb.append("  b.nextOwnerId=:ownerId ");			
			sb.append("  )  ") ;
			String currUserId = SecurityContextHolderHelp.getUserId();
			praValues.put("ownerId", currUserId);
			return sb;
		}
		
		return sb;
	}
	
	//下面的方法是优化时重写了，实际和appendAllMyBugHql一查一样了，原来查询果要判断流程有及角色，现在加了一个字段nextOwnerId后
	//不需要再判断了
	private StringBuffer  appendAllMyBugHql(BugManagerDto dto){
		StringBuffer sb = new StringBuffer();
		if(dto.getDefBug()==1){
			Map praValues = dto.getHqlParamMaps();
			sb.append(" and ( (b.currHandlerId=:ownerId and b.currStateId not in(4,5,14,15,22,23))or ") ;
			sb.append("  b.nextOwnerId=:ownerId ");			
			sb.append("  )  ") ;
			String currUserId = SecurityContextHolderHelp.getUserId();
			praValues.put("ownerId", currUserId);
			return sb;
		}
		return sb;
	}

	
	private StringBuffer  appendAllMyBugHqlByQuery(BugManagerDto dto){
		StringBuffer sb = new StringBuffer();
		Map praValues = dto.getHqlParamMaps();
		if(dto.getDefBug()==1){
			sb.append(" and ( (b.currHandlerId=:ownerId and b.currStateId not in(4,5,14,15,22,23))or ") ;
			sb.append("  b.nextOwnerId=:ownerId ");			
			sb.append("  )  ") ;
			String currUserId = SecurityContextHolderHelp.getUserId();
			praValues.put("ownerId", currUserId);
			return sb;
		}
		List<String> taskIds  = null;
		taskIds = this.getMyTestTask();
		if(taskIds==null||taskIds.isEmpty()){//参与的项目为空
			//放一个不存在的项目，以过滤数据
			sb.append("  and b.taskId=:taskId");
			praValues.put("taskId", "liuyg");
		}else{
			sb.append(" and b.taskId in(:tasks)   ");
			praValues.put("tasks", taskIds);
		}
		return sb;
	}

	public List getMyTestTask(){
		Map praValMap = new HashMap();
		praValMap.put("companyId", SecurityContextHolderHelp.getCompanyId());
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct singletest0_.TASKID as taskId")
		   .append("   from T_SINGLE_TEST_TASK singletest0_ ");
        sql.append(" inner join t_test_task_detail td on td.taskid = singletest0_.taskid ");
        sql.append("and singletest0_.COMPANYID = :companyId ");
		sql.append(" and singletest0_.STATUS_FLG <3 ");
		int isAdmin = SecurityContextHolderHelp.getUserIsAdmin();
	   	if(isAdmin<1){
	   		sql.append(" and td.taskid in " );
			sql.append("(select distinct ua.TASKID ")
			.append("from T_TASK_USEACTOR ua ")
			.append("where ua.TASKID = td.taskid ")
			.append("and ua.is_enable = 1 ")
			.append("and ua.userid = :userId) ");
			praValMap.put("userId", SecurityContextHolderHelp.getUserId());
	   	}

		if(logger.isInfoEnabled()){
			logger.info(sql.toString());
		}		
		
		return this.findBySqlWithValuesMap(sql.toString(), null, praValMap);
	}

	public int getAllMyBugCount(){
		//BugManagerDto dto = new BugManagerDto();
		//BugQueryInfo queryInfo =this.buildQueryInfo(dto,true);
		//int totalRows = hibernateGenericController.getResultCountWithValuesMap(queryInfo.getCurrHql(), queryInfo.getCurrPraValues(), "bugId", true).intValue();
		Map praValues = new HashMap();
		String currUserId = SecurityContextHolderHelp.getUserId();
		praValues.put("owner", currUserId);
		
		int totalRows = hibernateGenericController.getResultCountBySqlWithValuesMap(getAllMyBugCoutSql(), praValues).intValue();
		return totalRows;
	}
	
//	private String getAllMyBugCoutSql(){
//
//		StringBuffer sb = new StringBuffer();
//		sb.append("select count(bugbaseinf0_.BUGCARDID) as col_0_0_ ")
//		  .append("from T_BUGBASEINFO bugbaseinf0_  ")
//		  .append("where   ")
//		  .append("(bugbaseinf0_.CURRENT_STATE in (1, 2, 11, 13) and ")
//		  .append("bugbaseinf0_.TEST_OWNER =:owner) or ")
//	      .append("( bugbaseinf0_.CURRENT_STATE = 24 and ")
//		  .append("bugbaseinf0_.ANALYSER_OWNER =:owner) or ")
//		  .append("((bugbaseinf0_.CURRENT_STATE = 25 or bugbaseinf0_.CURRENT_STATE = 22) and ")
//		  .append("bugbaseinf0_.ASSIGNER_OWNER =:owner )or ")
//		  .append("(bugbaseinf0_.CURRENT_STATE in (6, 7, 18, 8, 9, 10, 12, 16) and ")
//		  .append("bugbaseinf0_.DEV_OWNER =:owner) or ")
//		  .append("(bugbaseinf0_.CURRENT_STATE in (9, 11, 12, 17, 20, 22, 23) and ")
//		  .append("bugbaseinf0_.INTERCESS_OWNER =:owner) ");	
//		return sb.toString();
//	}

	private String getAllMyBugCoutSql(){

		StringBuffer sb = new StringBuffer();
		sb.append("select count(*) as col_0_0_ ")
		  .append("from T_BUGBASEINFO bugbaseinf0_  ")
		  .append("where  bugbaseinf0_.bug_num =:owner ");
		return sb.toString();
	}
	public List<BugBaseInfo> findAllMyBug(BugManagerDto dto){
		BugQueryInfo queryInfo =this.buildQueryInfo(dto,true);
		List<BugBaseInfo> list= this.findByHqlWithValuesMap(queryInfo.getCurrHql(), dto.getPageNo(), 
				dto.getPageSize(), "bugId", queryInfo.getCurrPraValues(), false);
		if(dto.getSaveQuery()==1){
			queryInfo.setOnlyMe(dto.getDefBug());
			queryInfo.setQueryName(dto.getQueryName());
			queryInfo.setOwnerId(SecurityContextHolderHelp.getUserId());
			dto.setQueryInfo(queryInfo);
		}
		if(list==null||list.size()==0){
			return new ArrayList<BugBaseInfo>();
		}		
		dto.setQueryId(queryInfo.getQueryId());
		String hql = "select new cn.com.codes.object.DefaultTypeDefine(typeId,typeName) from TypeDefine where compId=? or compId=1";
		String compId = SecurityContextHolderHelp.getCompanyId();
		Long typeCount = this.getHibernateGenericController().getResultCount(hql, new Object[]{compId},"typeId");
		if(typeCount<100){
			hql = "from TypeDefine where compId=? or compId=1";
			List<TypeDefine> typeList = this.findByHql(hql, compId);
			dto.setTypeList(typeList);
		}
		return list;
	}
	
	private TestTaskDetail getTaskMailInfo(String taskId){
		String hql = "select new TestTaskDetail(taskId, projectId, mailDevFix,mailVerdict,mailOverdueBug,mailReport) from TestTaskDetail where taskId=?" ;
		TestTaskDetail taskDt = (TestTaskDetail)this.findByHql(hql, taskId).get(0);
		return taskDt;
	}
	/**
	 * 
	 * @param dto
	 * @param allTestTask 是否是从所有测试项目中查询
	 * @return
	 */
	private BugQueryInfo buildAssignQueryInfo(BugManagerDto dto){
		if(dto.getQueryInfo()!=null){
			return (BugQueryInfo)dto.getQueryInfo();
		}
		BugQueryInfo queryInfo = new BugQueryInfo();
		StringBuffer hql = new StringBuffer("select new BugBaseInfo(");
		hql.append(" bugId,bugDesc,bugGradeId,bugFreqId,bugOccaId,")
		   .append(" bugTypeId,priId, devOwnerId,reptDate,msgFlag,")
		   .append(" relaCaseFlag, moduleId, testOwnerId,currFlowCd,")
		   .append(" currHandlerId,currStateId,nextFlowCd,bugReptId,taskId)");
		Map praValues = new HashMap();
		hql.append(" from BugBaseInfo b where ( b.currStateId=25 or b.currStateId=17)  and b.taskId=:taskId   ");
		if(dto.getDefBug()==1){
			hql.append("  and b.assinOwnerId=:assinOwnerId ");
			praValues.put("assinOwnerId", SecurityContextHolderHelp.getUserId());
		}
		praValues.put("taskId", dto.getTaskId());
		
		if(dto.getBug()!=null){
			String testOwnerId = dto.getBug().getTestOwnerId();
			if(testOwnerId!=null &&!"-1".equals(testOwnerId)&&!"".equals(testOwnerId)){
				hql.append(" and b.testOwnerId=:testOwnerId");
				praValues.put("testOwnerId", testOwnerId);
			}
			Date reptDate = dto.getBug().getReptDate();
			if(reptDate!=null){
				hql.append(" and b.reptDate>=:reptDate and b.reptDate<:repEndDate");
				praValues.put("reptDate", reptDate);	
				Calendar calender = Calendar.getInstance();
				calender.setTime(reptDate);
				calender.add(Calendar.DAY_OF_YEAR, 1);
				Date repEndDate = calender.getTime();
				praValues.put("repEndDate", repEndDate);
			}		
			Long platformId = dto.getBug().getPlatformId();
			if(platformId!=null&&platformId!=-1){
				hql.append(" and b.platformId=:platformId");
				praValues.put("platformId", platformId);	
			}
			
			Long moduleId = dto.getBug().getModuleId();
			if(moduleId!=null&&!"".equals(moduleId.toString())&&!"-1".equals(moduleId.toString())){
				hql.append(" and b.moduleId=:moduleId");
				praValues.put("moduleId", moduleId);
			}
			Long gradeId = dto.getBug().getBugGradeId();
			if(gradeId!=null&&gradeId!=-1){
				hql.append(" and b.bugGradeId=:bugGradeId");
				praValues.put("bugGradeId", gradeId);
			}
			Long bugFreqId = dto.getBug().getBugFreqId();
			if(bugFreqId!=null&&bugFreqId!=-1){
				hql.append(" and b.bugFreqId=:bugFreqId");
				praValues.put("bugFreqId", bugFreqId);
			}
			Long bugOccaId = dto.getBug().getBugOccaId();
			if(bugOccaId!=null&&bugOccaId!=-1){
				hql.append(" and b.bugOccaId=:bugOccaId");
				praValues.put("bugOccaId", bugOccaId);
			}	
			Integer currStateId = dto.getBug().getCurrStateId();
//			if(currStateId!=null&&currStateId!=-1){
//				hql.append(" and b.currStateId=:currStateId");
//				praValues.put("currStateId", currStateId);
//			}
			Long priId = dto.getBug().getPriId();
			if(priId!=null&&priId!=-1){
				hql.append(" and b.priId=:priId");
				praValues.put("priId", priId);
			}
			Long sourceId = dto.getBug().getSourceId();
			if(sourceId!=null&&sourceId!=-1){
				hql.append(" and b.sourceId=:sourceId");
				praValues.put("sourceId", sourceId);
			}
			Long genePhaseId = dto.getBug().getGenePhaseId();
			if(genePhaseId!=null&&genePhaseId!=-1){
				hql.append(" and b.genePhaseId=:genePhaseId");
				praValues.put("genePhaseId", genePhaseId);
			}
			Integer relaCaseFlag = dto.getBug().getRelaCaseFlag();
			if(relaCaseFlag!=null&&relaCaseFlag!=-1){
				if(relaCaseFlag==1){
					hql.append(" and b.relaCaseFlag>=1");
				}else{
					hql.append(" and b.relaCaseFlag<1");
				}
				//praValues.put("relaCaseFlag", relaCaseFlag);
			}
			
			Long geneCauseId = dto.getBug().getGeneCauseId();
			if(geneCauseId!=null&&geneCauseId!=-1){
				hql.append(" and b.geneCauseId=:geneCauseId");
				praValues.put("geneCauseId", geneCauseId);
			}
			Long bugTypeId = dto.getBug().getBugTypeId();
			if(bugTypeId!=null&&bugTypeId!=-1){
				hql.append(" and b.bugTypeId=:bugTypeId");
				praValues.put("bugTypeId", bugTypeId);
			}		
			String devOwnerId = dto.getBug().getDevOwnerId();
			if(devOwnerId!=null&&!"-1".equals(devOwnerId)&&!"".equals(devOwnerId)){
				hql.append(" and b.devOwnerId=:devOwnerId");
				praValues.put("devOwnerId", devOwnerId);
			}
			
			String nextOwnerId = dto.getBug().getNextOwnerId();
			if(nextOwnerId!=null&&!"-1".equals(nextOwnerId)&&!"".equals(nextOwnerId)){
				hql.append(" and b.nextOwnerId=:nextOwnerId ");
				praValues.put("nextOwnerId", nextOwnerId);
			}
			String bugDesc = dto.getBug().getBugDesc();
			if(bugDesc!=null&&!bugDesc.trim().equals("")){
				hql.append(" and (b.bugDesc like :bugDesc or b.reProStep like :bugDesc)");
				praValues.put("bugDesc", "%"+bugDesc+"%");				
			}
			Long bugReptVer = dto.getBug().getBugReptVer();
			if(bugReptVer!=null&&bugReptVer!=-1&&!"".equals(devOwnerId)){
				hql.append(" and b.bugReptVer=:bugReptVer");
				praValues.put("bugReptVer", bugReptVer);
			}
		}
		dto.setHqlParamMaps(praValues);
		queryInfo.setHqlCondiStr(hql.toString());
		queryInfo.setCurrPraValues(praValues);
		return queryInfo;
	}
	public List<BugBaseInfo> findAssignBug(BugManagerDto dto){
		
		BugQueryInfo queryInfo =this.buildAssignQueryInfo(dto);
		List<BugBaseInfo> list= this.findByHqlWithValuesMap(queryInfo.getCurrHql(), dto.getPageNo(), 
				dto.getPageSize(), "bugId", queryInfo.getCurrPraValues(), false);	
		//List<BugBaseInfo> list= this.findByHqlWithValuesMap(hqlsb.toString(), dto.getPageNo(), dto.getPageSize(), "bugId", praValues,false);
		queryInfo = null;
		if(list==null||list.isEmpty()){
			return null;
		}
		String hql = "select new cn.com.codes.object.DefaultTypeDefine(typeId,typeName) from TypeDefine where compId=? or compId=1";
		String compId = SecurityContextHolderHelp.getCompanyId();
		Long typeCount = this.getHibernateGenericController().getResultCount(hql, new Object[]{compId},"typeId");
		if(typeCount<100){
			hql = "from TypeDefine where compId=? or compId=1";
			List<TypeDefine> typeList = this.findByHql(hql, compId);
			dto.setTypeList(typeList);
		}
		return list;
	}
	public void sendBatchBugAssignNotice(BugManagerDto dto){
		TestTaskDetail taskd = this.getTaskMailInfo(dto.getTaskId());
		PropertiesBean conf = (PropertiesBean) Context.getInstance().getBean("ContextProperties");
		String bugMsgFlg = conf.getProperty("mypm.sendBugMsg");
		if(!"true".equals(bugMsgFlg)&&bugMsgFlg!=null&&taskd.getMailDevFix()==0){
			return;
		}
		List<String> recpiList = new ArrayList<String>(1);
		recpiList.add(dto.getCurrOwner());
		StringBuilder sb = new StringBuilder("BUG摘要如下:<br> 项目" +dto.getStateName()+"<br>" +"  BUG 编号如下:<br>");
		String[] bugIdArr = dto.getProjectId().split("，");
		for(String bugId :bugIdArr){
			sb.append("<br>" +bugId);
		}
		if("true".equals(bugMsgFlg)||bugMsgFlg==null){
			mypmCommonMsgService.sendMsg(recpiList, dto.getStateName()+"项目批量分配BUG通知", sb.toString(), false);	
		}
		if(taskd.getMailDevFix()==1){
			String url = ((HttpServletRequest)SecurityContextHolder.getContext().getRequest()).getRequestURL().toString();
			url = url.split(SecurityFilter.getAppName())[0] +SecurityFilter.getAppName()+"/userManager/userManagerAction!loginWithBug.action?mailBugId="+dto.getTaskId();
			sb.append(" <br> <a href='"+url+"'>在MYPM中查看我的BUG</a>");
			MailBean mb = new MailBean();
			mb.setMimeMail(true);
			mb.setRecip(this.getMailAddrByUserIds(recpiList));
			mb.setSubject(dto.getStateName()+"项目批量分配BUG通知");
			mb.setMsg(sb.toString());
			mailProducer.sendMail(mb);
		}
	}
	public void sendBugNotice(BugHandHistory history,BugBaseInfo bug,String mailLinkUrl){
		if(history==null){  
			return;
		}
		TestTaskDetail taskd = this.getTaskMailInfo(bug.getTaskId());
		MailBean mailBean = null ;
		if(bug==null){
			
		}else if("新提交问题且无分配流程".equals(history.getRemark())){
			mailBean = this.buildMail(history.getRemark(),bug,taskd,mailLinkUrl);
		}else{
			mailBean = this.buildMail(history.getHandResult(),bug,taskd,mailLinkUrl);
		}
		PropertiesBean conf = (PropertiesBean) Context.getInstance().getBean("ContextProperties");
		String bugMsgFlg = conf.getProperty("itest.sendBugMsg ");
		MailBean chargeMailBean = null;
		if("true".equals(bugMsgFlg)||bugMsgFlg==null){

			List<String> recpiList = new ArrayList<String>();
			if(mailBean==null&&bug.getNextOwnerId()!=null&&!"".equals(bug.getNextOwnerId())){
				recpiList.add(bug.getNextOwnerId());
			}else{
				if(mailBean==null){
					if(history.getHandResult()!=null&&history.getHandResult().indexOf("责任人")>=0&&bug.getChargeOwner()!=null&&!"".equals(bug.getChargeOwner().trim())){
						chargeMailBean = new MailBean();
						chargeMailBean.setMimeMail(true);
						chargeMailBean.setMsg(buildChargeMailText(bug,taskd));
						chargeMailBean.setSubject("("+bug.getBugId()+")BUG责任人指派通知:"+bug.getBugDesc());
						chargeMailBean.setRecip(this.getMailAddrByUserIds(bug.getChargeOwner(), " "));
						sendMail2ChargeOwner(chargeMailBean);
					}
					return;
				}
				String[] recpiArry = mailBean.getRecUserIds().split(" ");
				for(String id :recpiArry){
					recpiList.add(id);
				}
			}

			//mypmCommonMsgService.sendMsg(recpiList, mailBean.getSubject(), "测试项目:\n"+this.getTaskName(taskd) +"\n描述为:\n" +bug.getBugDesc() +" 的BUG\n处理为 " +bug.getProcessLog(), false);
			String title =null;
			if(mailBean!=null){
				title =mailBean.getSubject();
			}else{
				title ="("+bug.getBugId()+")BUG通知: "+bug.getBugDesc();
			}
			
			mypmCommonMsgService.sendMsg(recpiList, title, "BUG摘要如下:<br>"+
					"  项目" +this.getTaskName(taskd)+"<br>" +
					"  测试需求项:" +bug.getModelName()+"<br>" +
					"  BUG 编号:" +bug.getBugId() +"<br>" +
					"  BUG 状态:" + BugFlowConst.getStateName(bug.getCurrStateId()) +"<br>" +
					"  BUG 等级:" +bug.getBugGrade().getTypeName() +"<br>" +
					"  BUG 优先级:" +bug.getBugPri().getTypeName() +"<br>" +
					"  BUG 频率:" +bug.getBugFreq().getTypeName() +"<br>" +
					"  BUG 发现版本:" +bug.getReptVersion().getVersionNum() +"<br>" +
					"  BUG描述:<br>"+"    "+bug.getBugDesc()+"<br>" +
					"  处理明细: " +"    "+(bug.getProcessLog()==null?"新提交BUG":bug.getProcessLog()), false);
		}
		
		if(mailBean==null){
			if(history.getHandResult()!=null&&history.getHandResult().indexOf("责任人")>=0&&bug.getChargeOwner()!=null&&!"".equals(bug.getChargeOwner().trim())){
				chargeMailBean = new MailBean();
				chargeMailBean.setMimeMail(true);
				chargeMailBean.setMsg(buildChargeMailText(bug,taskd));
				chargeMailBean.setSubject("("+bug.getBugId()+")BUG责任人指派通知:"+bug.getBugDesc());
				chargeMailBean.setRecip(this.getMailAddrByUserIds(bug.getChargeOwner(), " "));
				sendMail2ChargeOwner(chargeMailBean);
			}
			return;
		}
		
		if(history.getHandResult()!=null&&history.getHandResult().indexOf("责任人")>=0&&bug.getChargeOwner()!=null&&!"".equals(bug.getChargeOwner().trim())
				&&mailBean.getRecip().indexOf(bug.getChargeOwner())<0){
			chargeMailBean = new MailBean();
			chargeMailBean.setMimeMail(mailBean.isMimeMail());
			chargeMailBean.setMsg(mailBean.getMsg());
			chargeMailBean.setSubject("("+bug.getBugId()+")BUG责任人指派通知:"+bug.getBugDesc());
			chargeMailBean.setRecip(this.getMailAddrByUserIds(bug.getChargeOwner(), " "));
		}
		if(null!=taskd.getMailOverdueBug()&&taskd.getMailOverdueBug()==1){
			this.sendMail(mailBean);
		}else{
			mailProducer.sendMail(mailBean);
		}
		if(chargeMailBean!=null){
			sendMail2ChargeOwner(chargeMailBean);
		}
		
		
		return;
	}
	
	private String buildChargeMailText(BugBaseInfo bug,TestTaskDetail taskd){
		String taskName = this.getTaskName(taskd);
		String url = ((HttpServletRequest)SecurityContextHolder.getContext().getRequest()).getRequestURL().toString();
		//url = url.split(SecurityFilter.getAppName())[0] +SecurityFilter.getAppName()+"/userManager/userManagerAction!loginWithBug.action?mailBugId="+bug.getBugId();
		url = url.split(SecurityFilter.getAppName())[0] +SecurityFilter.getAppName()+"/login.htm?mailBugId="+bug.getBugId();
		String msg = "BUG摘要如下:<br>"+
				"  项目" +taskName+"<br>" +
				"  测试需求项:" +bug.getModelName()+"<br>" +
				"  BUG 编号:" +bug.getBugId() +"<br>" +
				"  BUG 状态:" + BugFlowConst.getStateName(bug.getCurrStateId()) +"<br>" +
				"  BUG 等级:" +bug.getBugGrade().getTypeName() +"<br>" +
				"  BUG 优先级:" +bug.getBugPri().getTypeName() +"<br>" +
				"  BUG 频率:" +bug.getBugFreq().getTypeName() +"<br>" +
				"  BUG 发现版本:" +bug.getReptVersion().getVersionNum() +"<br>" +
				"  BUG描述:<br>"+"    "+bug.getBugDesc()+"<br>"
				+" <br>  <a href='"+url+"'>在itest中查看BUG</a>";
		return msg;
	}
	private void sendMail2ChargeOwner(MailBean mailBean){
		mailProducer.sendMail(mailBean);
	}
	private void sendMail(MailBean mailBean){
		
		SendMail sendMail = new SendMail(mailBean,mailSender);
		Thread td = new Thread(sendMail);
		td.start();
	}
	
	class SendMail implements Runnable {
		MailBean mailBean ;
		JavaMailSender mailSenderHelp;
		public SendMail(MailBean mailBean,JavaMailSender mailSenderHelp){
			this.mailBean = mailBean;
			this.mailSenderHelp = mailSenderHelp;
		}
		public void run(){
//			SimpleMailMessage mail = new SimpleMailMessage();  
//			JavaMailSenderImpl sendImp = (JavaMailSenderImpl)mailSender;
//			mail.setFrom(sendImp.getUsername());  
//			mail.setTo(mailBean.getRecip().split(";")); 
//			if(mailBean.getToCcMails()!=null&&!"".equals(mailBean.getToCcMails().trim())){
//				mail.setTo(mailBean.getToCcMails().split(";"));
//			}
//			mail.setSubject(mailBean.getSubject());  
//			mail.setText(mailBean.getMsg());  
//			mailSenderHelp.send(mail);
			
			if((mailBean.getRecip()==null||"".equals(mailBean.getRecip().trim()))&&(mailBean.getToCcMails()==null&&"".equals(mailBean.getToCcMails().trim()))){
				return;
			}
			MimeMessage mm  =mailSenderHelp.createMimeMessage();
			MimeMessageHelper mmh = null;
			JavaMailSenderImpl sendImp = (JavaMailSenderImpl)mailSenderHelp;
			try {
				mmh = new MimeMessageHelper(mm,true,"utf-8");
				mmh.setSubject(mailBean.getSubject()) ;   
				mmh.setText("<html><head></head><body>"+mailBean.getMsg()+"</body></html>",true);  
				mmh.setFrom(sendImp.getUsername());
				if(mailBean.getRecip()==null||"".equals(mailBean.getRecip().trim())){
					mmh.setTo(mailBean.getToCcMails().split(";"));
					//System.out.println("to is nulll====so mailBean.getToCcMails()=" +mailBean.getToCcMails());
				}else{
					//System.out.println("to is not nulll====so ailBean.getRecip() ="+mailBean.getRecip() +"  mailBean.getToCcMails()=" +mailBean.getToCcMails());
					mmh.setTo(mailBean.getRecip().split(";"));
					if(mailBean.getToCcMails()!=null&&!"".equals(mailBean.getToCcMails().trim())){
						mmh.setCc(mailBean.getToCcMails().split(";"));
					}
				}
				if(mailBean.getAttachPhName()!=null){
					String upDirectory = SecurityContextHolderHelp.getUpDirectory();
					for(String phName :mailBean.getAttachPhName()){
						URL url = ResourceUtils.getFileURL(upDirectory+File.separator+phName);
						File file = new File(url.getFile());
						if(phName.indexOf("/")==phName.lastIndexOf("/")){
							String flieName =MimeUtility.encodeWord(phName);
							mmh.addAttachment(flieName,file); 					
						}else{
							String flieName =MimeUtility.encodeWord(phName.substring(phName.lastIndexOf("/")));
							mmh.addAttachment(flieName,file); 	
						}			
					}
				}
				mailSenderHelp.send(mm);
				
			} catch (UnsupportedEncodingException e) {
				logger.error(e);
				//e.printStackTrace();
			} catch (MessagingException e) {
				logger.error(e);
				//e.printStackTrace();
			} 
		}
	}
	private String buildFormInMail(){
		
		String url = ((HttpServletRequest)SecurityContextHolder.getContext().getRequest()).getRequestURL().toString();
		url = url.split(SecurityFilter.getAppName())[0] +SecurityFilter.getAppName()+"/bugManager/bugManagerAction!loadAllMyBug.action";
		StringBuilder sb = new StringBuilder("<form  method=\"post\" id=\"quickLoingForm\" name=\"quickLoingForm\" action=\""+url+"\" target='_blank'>");
		sb.append("<input type=\"hidden\" id=\"mypmQuickSearch\" name=\"mypmQuickSearch\" value=\"1\"/>");
		sb.append("<input type=\"hidden\" id=\"queryStr\" name=\"queryStr\" value=''/>");
		sb.append("</form>	");
		return sb.toString();
	}
	private MailBean buildMail(String handResult,BugBaseInfo bug,TestTaskDetail taskd,String mailLinkUrl){
		MailBean mailBean = null;

		StringBuilder sb = new StringBuilder();
		if("新提交问题且无分配流程".equals(handResult)&&((taskd.getMailDevFix()!=null &&taskd.getMailDevFix()==1)|| (taskd.getMailOverdueBug()!=null && taskd.getMailOverdueBug()==1))){
			mailBean = new MailBean();
			String taskName = this.getTaskName(taskd);
			if(bug.getDevOwnerId()!=null&&!"".equals(bug.getDevOwnerId())){
				mailBean.setRecUserIds(bug.getDevOwnerId());
				mailBean.setSubject("("+bug.getBugId()+")BUG指派通知:"+bug.getBugDesc());
				if(taskd.getMailDevFix()==1){
					mailBean.setRecip(this.getMailAddrByUserIds(bug.getDevOwnerId(), " "));
				}
			}else if(bug.getCurrStateId()==1){
				mailBean.setRecUserIds(bug.getTestOwnerId());
				mailBean.setSubject("("+bug.getBugId()+")BUG互验通知:"+bug.getBugDesc());
				if(taskd.getMailDevFix()==1){
					mailBean.setRecip(this.getMailAddrByUserIds(bug.getTestOwnerId(), " "));
				}
			}if(bug.getCurrStateId()==24){
				mailBean.setRecUserIds(bug.getAnalyseOwnerId());
				mailBean.setSubject("("+bug.getBugId()+")BUG分析通知:"+bug.getBugDesc());
				if(taskd.getMailDevFix()==1){
					mailBean.setRecip(this.getMailAddrByUserIds(bug.getAnalyseOwnerId(), " "));
				}
			}if(bug.getCurrStateId()==25){
				mailBean.setRecUserIds(bug.getAssinOwnerId());
				mailBean.setSubject("("+bug.getBugId()+")BUG分配通知:"+bug.getBugDesc());
				if(taskd.getMailDevFix()==1){
					mailBean.setRecip(this.getMailAddrByUserIds(bug.getAssinOwnerId(), " "));
				}
			}

			mailBean.setMsg("BUG摘要如下:<br>"+
					"  项目" +taskName+"<br>" +
					"  测试需求项:" +bug.getModelName()+"<br>" +
					"  BUG 编号:" +bug.getBugId() +"<br>" +
					"  BUG 状态:" + BugFlowConst.getStateName(bug.getCurrStateId()) +"<br>" +
					"  BUG 等级:" +bug.getBugGrade().getTypeName() +"<br>" +
					"  BUG 优先级:" +bug.getBugPri().getTypeName() +"<br>" +
					"  BUG 频率:" +bug.getBugFreq().getTypeName() +"<br>" +
					"  BUG 发现版本:" +bug.getReptVersion().getVersionNum() +"<br>" +
					"  BUG描述:<br>"+"    "+bug.getBugDesc()+"<br>" 
					//"  <button type=\"button\" onclick=\"javascript:document.getElementById('quickLoingForm').submit();\"> 在MYPM中查看BUG</button> "+buildFormInMail()
			         );		
			
		}else if(handResult.indexOf("测试人员")>=0&&taskd!=null&&(taskd.getMailDevFix()==1||taskd.getMailOverdueBug()==1)){
			mailBean = new MailBean();
			mailBean.setSubject("("+bug.getBugId()+")BUG指派通知: "+bug.getBugDesc());
			if(taskd.getMailDevFix()==1){
				mailBean.setRecip(this.getMailAddrByUserIds(bug.getTestOwnerId(), " "));
			}
			String taskName = this.getTaskName(taskd);
			mailBean.setRecUserIds(bug.getTestOwnerId());
			mailBean.setMsg("BUG摘要如下:<br>"+
					"  项目" +taskName+"<br>" +
					"  测试需求项:" +bug.getModelName()+"<br>" +
					"  BUG 编号:" +bug.getBugId() +"<br>" +
					"  BUG 状态:" + BugFlowConst.getStateName(bug.getCurrStateId()) +"<br>" +
					"  BUG 等级:" +bug.getBugGrade().getTypeName() +"<br>" +
					"  BUG 优先级:" +bug.getBugPri().getTypeName() +"<br>" +
					"  BUG 频率:" +bug.getBugFreq().getTypeName() +"<br>" +
					"  BUG 发现版本:" +bug.getReptVersion().getVersionNum() +"<br>" +
					"  BUG描述:<br>"+"    "+bug.getBugDesc()+"<br>");
		}else if(handResult.indexOf("开发人员")>=0&&taskd!=null&&((taskd.getMailDevFix()!=null &&taskd.getMailDevFix()==1)||(taskd.getMailOverdueBug()!=null && taskd.getMailOverdueBug()==1))){
			mailBean = new MailBean();
			mailBean.setSubject("("+bug.getBugId()+")BUG指派通知:"+bug.getBugDesc());
			String taskName = this.getTaskName(taskd);
			mailBean.setRecUserIds(bug.getDevOwnerId());
			if(taskd.getMailDevFix()==1){
				mailBean.setRecip(this.getMailAddrByUserIds(bug.getDevOwnerId(), " "));
			}
			mailBean.setMsg("BUG摘要如下:<br>"+
					"  项目" +taskName+"<br>" +
					"  测试需求项:" +bug.getModelName()+"<br>" +
					"  BUG 编号:" +bug.getBugId() +"<br>" +
					"  BUG 状态:" + BugFlowConst.getStateName(bug.getCurrStateId()) +"<br>" +
					"  BUG 等级:" +bug.getBugGrade().getTypeName() +"<br>" +
					"  BUG 优先级:" +bug.getBugPri().getTypeName() +"<br>" +
					"  BUG 频率:" +bug.getBugFreq().getTypeName() +"<br>" +
					"  BUG 发现版本:" +bug.getReptVersion().getVersionNum() +"<br>" +
					"  BUG描述:<br>"+"    "+bug.getBugDesc()+"<br>");			
		}else if(handResult.indexOf("修改截止日期")>=0&&taskd!=null&&((taskd.getMailDevFix()!=null && taskd.getMailDevFix()==1)||(taskd.getMailOverdueBug() !=null && taskd.getMailOverdueBug()==1))){
			mailBean = new MailBean();
			mailBean.setSubject("("+bug.getBugId()+")BUG修改截止日期通知:"+bug.getBugDesc());
			String taskName = this.getTaskName(taskd);
			mailBean.setRecUserIds(bug.getDevOwnerId());
			if(taskd.getMailDevFix()==1){
				mailBean.setRecip(this.getMailAddrByUserIds(bug.getDevOwnerId(), " "));
			}
			mailBean.setMsg("BUG摘要如下:<br>"+
					"  项目" +taskName+"<br>" +
					"  测试需求项:" +bug.getModelName()+"<br>" +
					"  BUG 编号:" +bug.getBugId() +"<br>" +
					"  BUG 状态:" + BugFlowConst.getStateName(bug.getCurrStateId()) +"<br>" +
					"  BUG 等级:" +bug.getBugGrade().getTypeName() +"<br>" +
					"  BUG 优先级:" +bug.getBugPri().getTypeName() +"<br>" +
					"  BUG 频率:" +bug.getBugFreq().getTypeName() +"<br>" +
					"  BUG 发现版本:" +bug.getReptVersion().getVersionNum() +"<br>" +
					"  设置修改截止日期为:"+StringUtils.formatShortDate(bug.getBugAntimodDate())+" 修改工作量为:"+bug.getPlanAmendHour() +"小时<br>" +
					"  BUG描述:<br>"+"    "+bug.getBugDesc()+"<br>");
			//mailBean.setMsg(taskName+"中描述为:"+bug.getBugDesc()+" 的BUG设置了修改工作期限，请登录MYPM。详情如下\n:" +handResult);			
		}else if(handResult.indexOf("仲裁人")>=0&&taskd!=null&&((taskd.getMailVerdict()!=null && taskd.getMailVerdict()==1)||(taskd.getMailOverdueBug()!=null && taskd.getMailOverdueBug()==1))){
			mailBean = new MailBean();
			//"12", "分歧"
			if(bug.getCurrStateId()==12){
				mailBean.setSubject("("+bug.getBugId()+")BUG请求仲载通知: "+bug.getBugDesc());
			}else{
				mailBean.setSubject("("+bug.getBugId()+")BUG请求审批通知: "+bug.getBugDesc());
			}
			String taskName = this.getTaskName(taskd);
			mailBean.setRecUserIds(bug.getIntercessOwnerId());
			if(taskd.getMailVerdict()==1){
				mailBean.setRecip(this.getMailAddrByUserIds(bug.getIntercessOwnerId(), " "));
			}
			if(bug.getCurrStateId()==12){
				mailBean.setMsg("BUG摘要如下:<br>"+
						"  项目" +taskName+"<br>" +
						"  测试需求项:" +bug.getModelName()+"<br>" +
						"  BUG 编号:" +bug.getBugId() +"<br>" +
						"  BUG 状态:" + BugFlowConst.getStateName(bug.getCurrStateId()) +"<br>" +
						"  BUG 等级:" +bug.getBugGrade().getTypeName() +"<br>" +
						"  BUG 优先级:" +bug.getBugPri().getTypeName() +"<br>" +
						"  BUG 频率:" +bug.getBugFreq().getTypeName() +"<br>" +
						"  BUG 发现版本:" +bug.getReptVersion().getVersionNum() +"<br>" +
						"  BUG描述:<br>"+"    "+bug.getBugDesc()+"<br>"+
						"  因开发人员和测试人员意见不一，现请求您仲载" +"<br>" +
						"  请求仲载理由:<br>    " +bug.getCurrRemark()
						);
				//mailBean.setMsg(taskName+"中描述为:"+bug.getBugDesc()+" 的BUG因开发人员和测试人员意见不一，现请求您仲载，请登录MYPM");			
			}else{
				//"19", "挂起/不计划修改"
				//20", "挂起/下版本修改"
				if(bug.getCurrStateId()==19){
					mailBean.setMsg("BUG摘要如下:<br>"+
							"  项目" +taskName+"<br>" +
							"  测试需求项:" +bug.getModelName()+"<br>" +
							"  BUG 编号:" +bug.getBugId() +"<br>" +
							"  BUG 状态:" + BugFlowConst.getStateName(bug.getCurrStateId()) +"<br>" +
							"  BUG 等级:" +bug.getBugGrade().getTypeName() +"<br>" +
							"  BUG 优先级:" +bug.getBugPri().getTypeName() +"<br>" +
							"  BUG 频率:" +bug.getBugFreq().getTypeName() +"<br>" +
							"  BUG 发现版本:" +bug.getReptVersion().getVersionNum() +"<br>" +
							"  BUG描述:<br>"+"    "+bug.getBugDesc()+"<br>"+
							"  当前置为\"挂起/不计划修改状态\",现请求您审批");
					//mailBean.setMsg(taskName+"中描述为:"+bug.getBugDesc()+" 的BUG置为:挂起/不计划修改，现请求您审批，请登录MYPM");			
				}else if(bug.getCurrStateId()==20){
					mailBean.setMsg("BUG摘要如下:<br>"+
							"  项目" +taskName+"<br>" +
							"  测试需求项:" +bug.getModelName()+"<br>" +
							"  BUG 编号:" +bug.getBugId() +"<br>" +
							"  BUG 状态:" + BugFlowConst.getStateName(bug.getCurrStateId()) +"<br>" +
							"  BUG 等级:" +bug.getBugGrade().getTypeName() +"<br>" +
							"  BUG 优先级:" +bug.getBugPri().getTypeName() +"<br>" +
							"  BUG 频率:" +bug.getBugFreq().getTypeName() +"<br>" +
							"  BUG 发现版本:" +bug.getReptVersion().getVersionNum() +"<br>" +
							"  BUG描述:<br>"+"    "+bug.getBugDesc()+"<br>"+
							"  当前置为\"挂起/下版本修改\"状态,现请求您审批");
					//mailBean.setMsg(taskName+"中描述为:"+bug.getBugDesc()+" 的BUG置为:挂起/下版本修改，现请求您审批，请登录MYPM");			
				}else {
					mailBean.setMsg("BUG摘要如下:<br>"+
							"  项目" +taskName+"<br>" +
							"  测试需求项:" +bug.getModelName()+"<br>" +
							"  BUG 编号:" +bug.getBugId() +"<br>" +
							"  BUG 状态:" + BugFlowConst.getStateName(bug.getCurrStateId()) +"<br>" +
							"  BUG 等级:" +bug.getBugGrade().getTypeName() +"<br>" +
							"  BUG 优先级:" +bug.getBugPri().getTypeName() +"<br>" +
							"  BUG 频率:" +bug.getBugFreq().getTypeName() +"<br>" +
							"  BUG 发现版本:" +bug.getReptVersion().getVersionNum() +"<br>" +
							"  BUG描述:<br>"+"    "+bug.getBugDesc()+"<br>"+
							"  当前置为\"重分配\"状态,现请求您审批");
					//mailBean.setMsg(taskName+"中描述为:"+bug.getBugDesc()+" 的BUG置为:挂起/下版本修改，现请求您审批，请登录MYPM");			
				}
			}
		}else if(handResult.indexOf("分析人")>=0&&taskd!=null&&((taskd.getMailDevFix()!=null && taskd.getMailDevFix()==1)|| (taskd.getMailDevFix()!=null && taskd.getMailOverdueBug()==1))){
			mailBean = new MailBean();
			//mailBean.setSubject("BUG分析通知 ");
			mailBean.setSubject("("+bug.getBugId()+")BUG分析通知:"+bug.getBugDesc());
			String taskName = this.getTaskName(taskd);
			mailBean.setRecUserIds(bug.getAnalyseOwnerId());
			if(taskd.getMailDevFix()==1){
				mailBean.setRecip(this.getMailAddrByUserIds(bug.getAnalyseOwnerId(), " "));
			}
			mailBean.setMsg("BUG摘要如下:<br>"+
					"  项目" +taskName+"<br>" +
					"  测试需求项:" +bug.getModelName()+"<br>" +
					"  BUG 编号:" +bug.getBugId() +"<br>" +
					"  BUG 状态:" + BugFlowConst.getStateName(bug.getCurrStateId()) +"<br>" +
					"  BUG 等级:" +bug.getBugGrade().getTypeName() +"<br>" +
					"  BUG 优先级:" +bug.getBugPri().getTypeName() +"<br>" +
					"  BUG 频率:" +bug.getBugFreq().getTypeName() +"<br>" +
					"  BUG 发现版本:" +bug.getReptVersion().getVersionNum() +"<br>" +
					"  BUG描述:<br>"+"    "+bug.getBugDesc()+"<br>");
			//mailBean.setMsg(taskName+"中描述为:"+bug.getBugDesc()+" 的BUG指派给您分析，请登录MYPM");			
		}else if(handResult.indexOf("分配人")>=0&&taskd!=null&&((taskd.getMailDevFix()!=null && taskd.getMailDevFix()==1)||(taskd.getMailDevFix()!=null && taskd.getMailOverdueBug()==1))){
			mailBean = new MailBean();
			mailBean.setSubject("("+bug.getBugId()+")BUG分配通知:"+bug.getBugDesc());
			String taskName = this.getTaskName(taskd);
			mailBean.setRecUserIds(bug.getAssinOwnerId());
			if(taskd.getMailDevFix()==1){
				mailBean.setRecip(this.getMailAddrByUserIds(bug.getAssinOwnerId(), " "));
			}
			mailBean.setMsg("BUG摘要如下:<br>"+
					"  项目" +taskName+"<br>" +
					"  测试需求项:" +bug.getModelName()+"<br>" +
					"  BUG 编号:" +bug.getBugId() +"<br>" +
					"  BUG 状态:" + BugFlowConst.getStateName(bug.getCurrStateId()) +"<br>" +
					"  BUG 等级:" +bug.getBugGrade().getTypeName() +"<br>" +
					"  BUG 优先级:" +bug.getBugPri().getTypeName() +"<br>" +
					"  BUG 频率:" +bug.getBugFreq().getTypeName() +"<br>" +
					"  BUG 发现版本:" +bug.getReptVersion().getVersionNum() +"<br>" +
					"  BUG描述:<br>"+"    "+bug.getBugDesc()+"<br>");
			//mailBean.setMsg(taskName+"中描述为:"+bug.getBugDesc()+" 的BUG指派给您分配，请登录MYPM");			
		}else{
			if(bug.getCurrStateId()==13){//"13", "已改";
				mailBean = new MailBean();
				mailBean.setSubject("("+bug.getBugId()+")BUG己改通知:"+bug.getBugDesc());
				String taskName = this.getTaskName(taskd);
				if(!bug.getTestOwnerId().equals(bug.getBugReptId())){
					mailBean.setRecUserIds(bug.getTestOwnerId()+" "+bug.getBugReptId());
					mailBean.setRecip(this.getMailAddrByUserIds(bug.getTestOwnerId()+" "+bug.getBugReptId(), " "));
				}else{
					mailBean.setRecUserIds(bug.getTestOwnerId());
					mailBean.setRecip(this.getMailAddrByUserIds(bug.getTestOwnerId(), " "));
				}
				mailBean.setMsg("BUG摘要如下:<br>"+
						"  项目" +taskName+"<br>" +
						"  测试需求项:" +bug.getModelName()+"<br>" +
						"  BUG 编号:" +bug.getBugId() +"<br>" +
						"  BUG 状态:" + BugFlowConst.getStateName(bug.getCurrStateId()) +"<br>" +
						"  BUG 等级:" +bug.getBugGrade().getTypeName() +"<br>" +
						"  BUG 优先级:" +bug.getBugPri().getTypeName() +"<br>" +
						"  BUG 频率:" +bug.getBugFreq().getTypeName() +"<br>" +
						"  BUG 发现版本:" +bug.getReptVersion().getVersionNum() +"<br>" +
						"  BUG描述:<br>"+"    "+bug.getBugDesc()+"<br>"+
						"  修复明细如下<br>:" +"    "+handResult);
				//mailBean.setMsg(taskName+"中描述为:"+bug.getBugDesc()+" 的BUG己修改，请登录MYPM。详情如下\n:" +handResult);			
			}else if(bug.getCurrStateId()==26){//"13", "已改";
				mailBean = new MailBean();
				mailBean.setSubject("("+bug.getBugId()+")BUG己改/同步到测试环境通知:"+bug.getBugDesc());
				String taskName = this.getTaskName(taskd);
				if(!bug.getTestOwnerId().equals(bug.getBugReptId())){
					mailBean.setRecUserIds(bug.getTestOwnerId()+" "+bug.getBugReptId());
					mailBean.setRecip(this.getMailAddrByUserIds(bug.getTestOwnerId()+" "+bug.getBugReptId(), " "));
				}else{
					mailBean.setRecUserIds(bug.getTestOwnerId());
					mailBean.setRecip(this.getMailAddrByUserIds(bug.getTestOwnerId(), " "));
				}
				mailBean.setMsg("BUG摘要如下:<br>"+
						"  项目" +taskName+"<br>" +
						"  测试需求项:" +bug.getModelName()+"<br>" +
						"  BUG 编号:" +bug.getBugId() +"<br>" +
						"  BUG 状态:" + BugFlowConst.getStateName(bug.getCurrStateId()) +"<br>" +
						"  BUG 等级:" +bug.getBugGrade().getTypeName() +"<br>" +
						"  BUG 优先级:" +bug.getBugPri().getTypeName() +"<br>" +
						"  BUG 频率:" +bug.getBugFreq().getTypeName() +"<br>" +
						"  BUG 发现版本:" +bug.getReptVersion().getVersionNum() +"<br>" +
						"  BUG描述:<br>"+"    "+bug.getBugDesc()+"<br>"+
						"  修复明细如下<br>:" +"    "+handResult);
				//mailBean.setMsg(taskName+"中描述为:"+bug.getBugDesc()+" 的BUG己修改，请登录MYPM。详情如下\n:" +handResult);			
			}else if(bug.getCurrStateId()==14){//"14", "关闭/己解决";
				mailBean = new MailBean();
				mailBean.setSubject("("+bug.getBugId()+")BUG关闭/己解决通知:"+bug.getBugDesc());
				String taskName = this.getTaskName(taskd);
				mailBean.setRecUserIds(bug.getDevOwnerId());
				mailBean.setRecip(this.getMailAddrByUserIds(bug.getDevOwnerId(), " "));
				mailBean.setMsg("BUG摘要如下:<br>"+
						"  项目" +taskName+"<br>" +
						"  测试需求项:" +bug.getModelName()+"<br>" +
						"  BUG 编号:" +bug.getBugId() +"<br>" +
						"  BUG 状态:" + BugFlowConst.getStateName(bug.getCurrStateId()) +"<br>" +
						"  BUG 等级:" +bug.getBugGrade().getTypeName() +"<br>" +
						"  BUG 优先级:" +bug.getBugPri().getTypeName() +"<br>" +
						"  BUG 频率:" +bug.getBugFreq().getTypeName() +"<br>" +
						"  BUG 发现版本:" +bug.getReptVersion().getVersionNum() +"<br>" +
						"  BUG描述:<br>"+"    "+bug.getBugDesc()+"<br>"+
						"  关闭明细如下<br>:" +"    "+handResult);
				//mailBean.setMsg(taskName+"中描述为:"+bug.getBugDesc()+" 的BUG置为关闭/己解决，请登录MYPM。详情如下<br>:" +handResult);							
			}else if(bug.getCurrStateId()==15){//"15", "关闭/不再现";
				mailBean = new MailBean();
				mailBean.setSubject("("+bug.getBugId()+")BUG关闭/不再现通知:"+bug.getBugDesc());
				String taskName = this.getTaskName(taskd);
				if(bug.getCurrHandlerId().equals(bug.getTestOwnerId())){
					mailBean.setRecUserIds(bug.getDevOwnerId());
					mailBean.setRecip(this.getMailAddrByUserIds(bug.getDevOwnerId(), " "));
				}else{
					mailBean.setRecUserIds(bug.getTestOwnerId());
					mailBean.setRecip(this.getMailAddrByUserIds(bug.getTestOwnerId(), " "));
				}
				mailBean.setMsg("BUG摘要如下:<br>"+
						"  项目" +taskName+"<br>" +
						"  测试需求项:" +bug.getModelName()+"<br>" +
						"  BUG 编号:" +bug.getBugId() +"<br>" +
						"  BUG 状态:" + BugFlowConst.getStateName(bug.getCurrStateId()) +"<br>" +
						"  BUG 等级:" +bug.getBugGrade().getTypeName() +"<br>" +
						"  BUG 优先级:" +bug.getBugPri().getTypeName() +"<br>" +
						"  BUG 频率:" +bug.getBugFreq().getTypeName() +"<br>" +
						"  BUG 发现版本:" +bug.getReptVersion().getVersionNum() +"<br>" +
						"  BUG描述:<br>"+"    "+bug.getBugDesc()+"<br>"+
						"  关闭明细如下<br>:" +"    "+handResult);
				//mailBean.setMsg(taskName+"中描述为:"+bug.getBugDesc()+" 的BUG置为关闭/不再现，请登录MYPM。详情如下\n:" +handResult);							
			}else if(bug.getCurrStateId()==22){//"22", "关闭/撤销"
				mailBean = new MailBean();
				mailBean.setSubject("("+bug.getBugId()+")BUG关闭/撤销通知:"+bug.getBugDesc());
				if(bug.getCurrHandlerId().equals(bug.getIntercessOwnerId())){
					mailBean.setRecUserIds(bug.getTestOwnerId()+" "+bug.getDevOwnerId());
					mailBean.setRecip(this.getMailAddrByUserIds(bug.getTestOwnerId()+" "+bug.getDevOwnerId(), " "));
				}else{
					if(bug.getCurrHandlerId().equals(bug.getTestOwnerId())){
						mailBean.setRecUserIds(bug.getDevOwnerId());
						mailBean.setRecip(this.getMailAddrByUserIds(bug.getDevOwnerId(), " "));
					}else{
						mailBean.setRecUserIds(bug.getTestOwnerId());
						mailBean.setRecip(this.getMailAddrByUserIds(bug.getTestOwnerId(), " "));
					}
				}
				String taskName = this.getTaskName(taskd);
				mailBean.setMsg("BUG摘要如下:<br>"+
						"  项目" +taskName+"<br>" +
						"  测试需求项:" +bug.getModelName()+"<br>" +
						"  BUG 编号:" +bug.getBugId() +"<br>" +
						"  BUG 状态:" + BugFlowConst.getStateName(bug.getCurrStateId()) +"<br>" +
						"  BUG 等级:" +bug.getBugGrade().getTypeName() +"<br>" +
						"  BUG 优先级:" +bug.getBugPri().getTypeName() +"<br>" +
						"  BUG 频率:" +bug.getBugFreq().getTypeName() +"<br>" +
						"  BUG 发现版本:" +bug.getReptVersion().getVersionNum() +"<br>" +
						"  BUG描述:<br>"+"    "+bug.getBugDesc()+"<br>"+
						"  关闭明细如下<br>:" +"    "+handResult);
				//mailBean.setMsg(taskName+"中描述为:"+bug.getBugDesc()+" 的BUG置为关闭/撤销，请登录MYPM。详情如下\n:" +handResult);							
			}else if(bug.getCurrStateId()==23){//"23", "关闭/遗留"
				mailBean = new MailBean();
				mailBean.setSubject("("+bug.getBugId()+")BUG关闭/遗留通知:"+bug.getBugDesc());
				String taskName = this.getTaskName(taskd);
				mailBean.setRecUserIds(bug.getTestOwnerId()+" "+bug.getDevOwnerId());
				mailBean.setRecip(this.getMailAddrByUserIds(bug.getTestOwnerId()+" "+bug.getDevOwnerId(), " "));
				mailBean.setMsg("BUG摘要如下:<br>"+
						"  项目" +taskName+"<br>" +
						"  测试需求项:" +bug.getModelName()+"<br>" +
						"  BUG 编号:" +bug.getBugId() +"<br>" +
						"  BUG 状态:" + BugFlowConst.getStateName(bug.getCurrStateId()) +"<br>" +
						"  BUG 等级:" +bug.getBugGrade().getTypeName() +"<br>" +
						"  BUG 优先级:" +bug.getBugPri().getTypeName() +"<br>" +
						"  BUG 频率:" +bug.getBugFreq().getTypeName() +"<br>" +
						"  BUG 发现版本:" +bug.getReptVersion().getVersionNum() +"<br>" +
						"  BUG描述:<br>"+"    "+bug.getBugDesc()+"<br>"+
						"  关闭明细如下<br>:" +"    "+handResult);
				//mailBean.setMsg(taskName+"中描述为:"+bug.getBugDesc()+" 的BUG己修改，请登录MYPM。详情如下\n:" +handResult);							
			}
		}	
		if(mailBean!=null){
			mailBean.setMimeMail(true);
			String url = ((HttpServletRequest)SecurityContextHolder.getContext().getRequest()).getRequestURL().toString();
			if(mailLinkUrl!=null&&!"".equals(mailLinkUrl.trim())){
				url = url.split(SecurityFilter.getAppName())[0] +SecurityFilter.getAppName()+mailLinkUrl;
			}else{
				url = url.split(SecurityFilter.getAppName())[0] +SecurityFilter.getAppName()+"/userManager/userManagerAction!loginWithBug.action?mailBugId="+bug.getBugId();
			}
			mailBean.setMsg(mailBean.getMsg() +"<br>  <a href='"+url+"'>在MYPM中查看BUG</a>");
			
			//抄送项目关注人
			if(null!=taskd.getMailOverdueBug() && taskd.getMailOverdueBug()==1){
				String hql = "select ta from TaskUseActor ta  join ta.user u where ta.taskId=?   and ta.isEnable = 1 and u.delFlag=0 and u.status=1 and ta.actor=11  order by actor";
				List<TaskUseActor> useActorList = this.findByHql(hql,taskd.getTaskId());
				if(useActorList!=null&&!useActorList.isEmpty()){
					StringBuffer proRelaPersionIds = new StringBuffer();
					for(TaskUseActor taskUseActor :useActorList){
						proRelaPersionIds.append(" ").append(taskUseActor.getUserId());
					}
					mailBean.setToCcMails(this.getMailAddrByUserIds(proRelaPersionIds.toString().trim(), " "));
				}
			}
		}
		return mailBean;
	}
	private String getTaskName(TestTaskDetail taskDt){
		String hql = null;
		hql = "select new  SingleTestTask(taskId,proName,proNum) from SingleTestTask where taskId=?" ;
		SingleTestTask singleTask = (SingleTestTask)this.findByHql(hql, taskDt.getTaskId()).get(0);
		return "名称为:"+singleTask.getProName()+"的测试项目";
		
	}
	public int executeAssignBug(BugManagerDto dto){
		this.buildAssignHql(dto);
		this.excuteBatchHql(dto.getHql(), dto.getHqlParamMaps());
		this.batchHistory(dto);
		dto.setHqlParamMaps(null);
		dto.setHql(null);
		return 1;
	}
	private void batchHistory(BugManagerDto dto){
		
		String upFianl = "update BugHandHistory set currDayFinal=0 where bugId in (:bugIds) and insDate >=:currDateStart and insDate <:currDateEnd and currDayFinal=1";
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date now = calendar.getTime();
		Map praValues = new HashMap();
		ArrayList<Long> bugIds = new ArrayList<Long>();
		String[] bugIdArr = dto.getProjectId().split("，");
		for(String bugId :bugIdArr){
			bugIds.add(Long.valueOf(bugId));
		}
		praValues.put("bugIds",bugIds);
		praValues.put("currDateStart",now);
		praValues.put("currDateEnd",new Date());
		this.excuteBatchHql(upFianl, praValues);
		praValues = null;
		for(Long bugId :bugIds){
			BugHandHistory bugHistory = new BugHandHistory();
			bugHistory.setInitState(25);
			if(dto.getTestFlow().indexOf("2")>=0){
				bugHistory.setBugState(7);
				bugHistory.setHandResult("把状态为设置为:待改/再现;开发人员设置为:" +dto.getModuleName());
			}else{
				bugHistory.setBugState(10);
				bugHistory.setHandResult("把状态为设置为:待改;开发人员设置为:"+dto.getModuleName());
			}
			bugHistory.setHandlerId(SecurityContextHolderHelp.getUserId());
			bugHistory.setInsDate(new Date());
			bugHistory.setTestFlowCd(4);
			bugHistory.setTaskId(dto.getTaskId());
			bugHistory.setRemark("批量分配原始状态为分配或重分配");
			bugHistory.setCurrDayFinal(1);
			bugHistory.setBugId(bugId);
			this.add(bugHistory);
		}
		bugIds = null;
	}
	
	private void buildAssignHql(BugManagerDto dto){
		StringBuffer hql = new StringBuffer();
		hql.append("update BugBaseInfo set devOwnerId=:devOwnerId,currHandlerId=:currHandlerId,")
		   .append( " currHandlDate=:currHandlDate,initState=:initState ,nextFlowCd=:nextFlowCd,currRemark=:currRemark,")
		   .append( " chargeOwner=:chargeOwner ,")
		   .append(" currFlowCd=:currFlowCd,currStateId=:currStateId,nextOwnerId=:devOwnerId where taskId=:taskId and bugId in (:bugIds)");
		Map praValues = new HashMap();
		praValues.put("devOwnerId",dto.getCurrOwner());
		praValues.put("currHandlerId",SecurityContextHolderHelp.getUserId());
		praValues.put("currHandlDate",new Date());
		praValues.put("initState",25);//就算原来是重分配,也统一看成是分配状态
		if(dto.getTestFlow().indexOf("2")>=0){
			praValues.put("currStateId",7);//待改/再现
		}else{
			praValues.put("currStateId",10);//待改
		}
		praValues.put("chargeOwner",dto.getCurrOwner());
		praValues.put("nextFlowCd",5);
		praValues.put("currFlowCd",4);
		praValues.put("currRemark","批量分配");
		
		ArrayList bugIds = new ArrayList();
		String[] bugIdArr = dto.getProjectId().split("，");
		for(String bugId :bugIdArr){
			bugIds.add(Long.valueOf(bugId));
		}
		praValues.put("taskId",dto.getTaskId());
		praValues.put("bugIds",bugIds);
		dto.setHql(hql.toString());
		dto.setHqlParamMaps(praValues);
	}
	public CommonMsgService getMypmCommonMsgService() {
		return mypmCommonMsgService;
	}

	public void setMypmCommonMsgService(CommonMsgService mypmCommonMsgService) {
		this.mypmCommonMsgService = mypmCommonMsgService;
	}

	public MailProducer getMailProducer() {
		return mailProducer;
	}

	public void setMailProducer(MailProducer mailProducer) {
		this.mailProducer = mailProducer;
	}

	public TestTaskDetailService getTestTaskService() {
		return testTaskService;
	}

	public void setTestTaskService(TestTaskDetailService testTaskService) {
		this.testTaskService = testTaskService;
	}

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}



}
