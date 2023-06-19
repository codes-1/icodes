package cn.com.codes.impExpManager.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.com.codes.bugManager.dto.BugManagerDto;
import cn.com.codes.caseManager.dto.CaseManagerDto;
import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.app.services.BaseServiceImpl;
import cn.com.codes.framework.common.config.PropertiesBean;
import cn.com.codes.framework.common.util.Context;
import cn.com.codes.impExpManager.service.ImpExpManagerService;
import cn.com.codes.object.OutlineInfo;
import cn.com.codes.object.TestTaskDetail;
import cn.com.codes.overview.dto.DataVo;



public class ImpExpManagerServiceImpl extends BaseServiceImpl implements
		ImpExpManagerService {

	private static StringBuffer expBugMysqlSql = new StringBuffer();
	private static StringBuffer expCaseMysqlSql = new StringBuffer();
	private static StringBuffer expBugOracleSql = new StringBuffer();
	private static StringBuffer expCaseOracleSql = new StringBuffer();
	static{
		expBugOracleSql.append("select t.bugcardid ,");
		expBugOracleSql.append("   t.bugDesc,");
		expBugOracleSql.append("   t.reproduct_txt ,");
		expBugOracleSql.append("   t.current_state ,");
		expBugOracleSql.append("   tplevel.enumname as levelName,");
	    expBugOracleSql.append("   tptype.enumname as typeName,");
	    expBugOracleSql.append("   tpocca.enumname as occaName,");
	    expBugOracleSql.append("   urep.name  as reptName,");
	    expBugOracleSql.append("   t.BUGDISVDATE  ,");
	    expBugOracleSql.append("   vrep.version_num as disVer,");
	    expBugOracleSql.append("   ufix.name as fixName ,");
	    expBugOracleSql.append("   t.fix_date ,");
	    expBugOracleSql.append("   ucls.name as clsName ,");
	    expBugOracleSql.append("   t.current_handl_date ,");
	    expBugOracleSql.append("   vres.version_num as reslVer,");
	    expBugOracleSql.append("   o.modulename as modulename,");
	    expBugOracleSql.append("   o2.modulename as superModuleName");
	    expBugOracleSql.append(" from t_bugbaseinfo t");
	    expBugOracleSql.append(" inner join t_typedefine tplevel on tplevel.enumid = t.buglevel");
	    expBugOracleSql.append(" inner join t_typedefine tptype on tptype.enumid = t.bugtype");
	    expBugOracleSql.append(" inner join t_typedefine tpocca on tpocca.enumid = t.bugocca");
	    expBugOracleSql.append(" inner join t_user urep on t.BUGDISVPERID = urep.id");
	    expBugOracleSql.append(" inner join t_software_version vrep on vrep.version_id = t.discover_ver");
	    expBugOracleSql.append(" left join t_user ufix on ufix.id = t.dev_owner");
	    expBugOracleSql.append(" left join t_user ucls on ucls.id = t.current_handler");
	    expBugOracleSql.append(" left join t_software_version vres on vres.version_id = t.FIX_VERSION");
	    expBugOracleSql.append(" inner join t_outlineinfo o on o.moduleid = t.moduleid");
	    expBugOracleSql.append(" inner join t_outlineinfo o2 on o2.moduleid = o.supermoduleid");
	    expBugOracleSql.append(" where 1=1 ");
	    
	    
		expBugMysqlSql.append("select v.*");
	    expBugMysqlSql.append(" from t_bugbaseinfo t");
	    expBugMysqlSql.append(" inner join v_expBugView v on v.bugcardid = t.bugcardid");
	    expBugMysqlSql.append(" where 1=1 ");    
	    
	    expCaseOracleSql.append("select t.testcaseid, ");
	    expCaseOracleSql.append("   o2.modulename as superMname, ");
	    expCaseOracleSql.append("   o.modulename, ");
	    expCaseOracleSql.append("   t.testcasedes, ");
	    expCaseOracleSql.append("   t.testcaseoperdata, ");
	    expCaseOracleSql.append("   t.EXPRESULT, ");
	    expCaseOracleSql.append("   t.status, ");
	    expCaseOracleSql.append("   u.name as author, ");
	    expCaseOracleSql.append("   u2.name as exeName, ");
	    expCaseOracleSql.append("   tp.enumname as typeName, ");
	    expCaseOracleSql.append("   tpri.enumname as priName ,");
	    expCaseOracleSql.append("   t.UPDDATE as exeDate ");
	    expCaseOracleSql.append("   from t_testcasebaseinfo t ");
	    expCaseOracleSql.append("   inner join t_outlineinfo o on o.moduleid = t.moduleid ");
	    expCaseOracleSql.append("   inner join t_outlineinfo o2 on o2.moduleid = o.supermoduleid ");
	    expCaseOracleSql.append("   inner join t_user u on t.createrid = u.id ");
	    expCaseOracleSql.append("   left join t_user u2 on t.createrid = u2.id ");
	    expCaseOracleSql.append("    inner join t_typedefine tp on tp.enumid = t.casetype ");
	    expCaseOracleSql.append("   inner join t_typedefine tpri on tpri.enumid = t.case_pri ");
	    expCaseOracleSql.append("   where 1=1 ");
	    
	    
	    expCaseMysqlSql.append("select v.* ");
	    expCaseMysqlSql.append("   from t_testcasebaseinfo t ");
	    expCaseMysqlSql.append("   inner join v_expCaseView v  on v.testcaseid = t.testcaseid ");
	    expCaseMysqlSql.append("   where 1=1 ");
	}
	 
	private OutlineInfo getCurrOutLine(CaseManagerDto dto){
		Long currNodeId = dto.getCurrNodeId();
		StringBuffer hql = new StringBuffer();
		OutlineInfo outLine = null;
		if(currNodeId==null){
			String taskHql = " select new TestTaskDetail(outlineState,testPhase,currentVersion,testSeq,reltCaseFlag) from TestTaskDetail where taskId=? and companyId=?";
			List<TestTaskDetail> taskList = this.findByHql(taskHql, dto.getTaskId(),SecurityContextHolderHelp.getCompanyId());
			TestTaskDetail taskDetal = taskList.get(0);
			dto.setOutLineState(taskDetal.getOutlineState());
			hql.append("select new OutlineInfo(moduleNum,moduleId) from OutlineInfo where superModuleId =0 and taskId=?");
			List<OutlineInfo> list = this.findByHql(hql.toString(), dto.getTaskId());
			outLine = list.get(0);
			list = null;
		}else{
			hql.append("select new OutlineInfo(moduleNum,moduleId) from OutlineInfo where moduleId=? and taskId=?");
			List<OutlineInfo> list =this.findByHql(hql.toString(), currNodeId,dto.getTaskId());
			outLine = list.get(0);
			list = null;
		}
		dto.setCurrNodeId(outLine.getModuleId());
		return outLine;
	}
	public void buildCaseWhereSql(CaseManagerDto dto){
		StringBuffer sql = new StringBuffer();
		Map praValuesMap = new HashMap();
		praValuesMap.put("taskId", dto.getTaskId());
		OutlineInfo outLine = this.getCurrOutLine(dto);
		
		if(outLine.getModuleNum()!=null&&!"".equals(outLine.getModuleNum())){
			sql.append("  and t.MODULENUM like :moduleNum");
			praValuesMap.put("moduleNum", outLine.getModuleNum()+"%");
		}
		if(dto.getTestCaseInfo()==null){
			dto.setHqlParamMaps(praValuesMap);
			sql.append(" and t.TASKID =:taskId ");
			sql.append(" order by upddate desc");
			dto.setHql((this.getExpCaseSql()+sql.toString()));
			return;
		}
		if(dto.getTestCaseInfo().getCaseTypeId()!=null&&dto.getTestCaseInfo().getCaseTypeId()!=-1){
			sql.append(" and t.CASETYPE=:caseTypeId");
			praValuesMap.put("caseTypeId", dto.getTestCaseInfo().getCaseTypeId());
		}
		if(dto.getTestCaseInfo().getPriId()!=null&&dto.getTestCaseInfo().getPriId()!=-1){
			sql.append(" and t.CASE_PRI=:priId");
			praValuesMap.put("priId", dto.getTestCaseInfo().getPriId());
		}
		if(dto.getTestCaseInfo().getTestStatus()!=null&&dto.getTestCaseInfo().getTestStatus()!=-1){
			sql.append(" and t.STATUS=:testStatus");
			praValuesMap.put("testStatus", dto.getTestCaseInfo().getTestStatus());
		}
		if(dto.getTestCaseInfo().getCreaterId()!=null&&!"".equals(dto.getTestCaseInfo().getCreaterId().trim())){
			sql.append(" and t.CREATERID in(:createrId)");
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
			sql.append(" and t.ADUIT_ID in(:auditId)");
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
			sql.append(" and t.WEIGHT = :weight");
			praValuesMap.put("weight", dto.getTestCaseInfo().getWeight());
		}
		if(dto.getTestCaseInfo().getTestCaseDes()!=null&&!"".equals(dto.getTestCaseInfo().getTestCaseDes().trim())){
			sql.append(" and t.TESTCASEDES like :testCaseDes");
			praValuesMap.put("testCaseDes", "%"+dto.getTestCaseInfo().getTestCaseDes().trim()+"%");
		}
		sql.append(" and t.TASKID =:taskId");
		sql.append(" order by upddate desc");
		dto.setHql((this.getExpCaseSql()+sql.toString()));
		dto.setHqlParamMaps(praValuesMap);
		
	}


	public String getCaseCountStr(String taskId,String moduleNum){
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as caseCount, t.status ");
		sql.append("  from t_testcasebaseinfo t ");
		sql.append(" where t.moduleNum like ?  and t.taskid = ?  ");
		sql.append("group by t.status ");
		List countlist = this.findBySql(sql.toString(),null,moduleNum+"%",taskId);
		if(countlist==null||countlist.isEmpty()){
			return "总用例数：0";
		}
		int allCount = 0;
		int passCount = 0;
		int failedCount = 0;
		int blockCount = 0;
		int noTestCount =0;
		int invalidCount = 0;
		int waitModifyCount = 0;
		int waitAuditCount = 0;
		
		
		Iterator it = countlist.iterator();
		while (it.hasNext()) {
			Object values[] = (Object[]) it.next();
			int testStatus = Integer.parseInt(values[1].toString());
			int count = Integer.parseInt(values[0].toString());
			allCount = allCount + count;
			if(testStatus==0){
				waitAuditCount +=count;
			}else if(testStatus==1){
				noTestCount +=count;
			}else if(testStatus==2){
				passCount =count;
			}else if(testStatus==3){
				failedCount =count;
			}else if(testStatus==4){
				invalidCount =count;
			}else if(testStatus==5){
				blockCount =count;
			}else if(testStatus==6){
				waitModifyCount =count;
			}
		}
		return "总用例数："+allCount +" 通过数："+passCount +" 未通过数："+failedCount +" 阻塞数："+blockCount+
		" 未测试数：" +noTestCount +" 不适用数：" +invalidCount +" 待修正数："+waitModifyCount +" 待审核数："+waitAuditCount
		;
	}
	public String getBugCountStr(String taskId,String moduleNum){
		String sql = "select count(*) from T_BUGBASEINFO where  MODULENUM like ? and TASK_ID=?";
		List countlist = this.findBySql(sql,null,moduleNum+"%",taskId);
		int allCount = Integer.parseInt(countlist.get(0).toString());
		countlist = null;
		if(allCount!=0){
			sql = null;
			sql = "select count(*) from T_BUGBASEINFO where CURRENT_STATE not in (2,3,4,5,22,16) and MODULENUM like ? and TASK_ID=?";
			countlist = this.findBySql(sql,null,moduleNum+"%",taskId);
			int validCout = Integer.parseInt(countlist.get(0).toString());
			sql = null;
			sql = "select count(*) from T_BUGBASEINFO where CURRENT_STATE  in (13,14,15,23) and MODULENUM like ? and TASK_ID=?";
			countlist = null;
			countlist = this.findBySql(sql,null,moduleNum+"%",taskId);
			int resolCount = Integer.parseInt(countlist.get(0).toString());
			return "总BUG数："+allCount +" 有效BUG数："+validCout +" 己改/关闭BUG数：" +resolCount;
		}
		return "总BUG数："+allCount +" 有效BUG数：0 己改/关闭BUG数：0";
	}
	public String getCaseCountStr(String taskId){
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as caseCount, t.status ");
		sql.append("  from t_testcasebaseinfo t ");
		sql.append(" where t.taskid = ? ");
		sql.append("group by t.status ");
		List countlist = this.findBySql(sql.toString(),null,taskId);
		if(countlist==null||countlist.isEmpty()){
			return "总用例数：0";
		}
		int allCount = 0;
		int passCount = 0;
		int failedCount = 0;
		int blockCount = 0;
		int noTestCount =0;
		int invalidCount = 0;
		int waitModifyCount = 0;
		int waitAuditCount = 0;
		
		
		Iterator it = countlist.iterator();
		while (it.hasNext()) {
			Object values[] = (Object[]) it.next();
			int testStatus = Integer.parseInt(values[1].toString());
			int count = Integer.parseInt(values[0].toString());
			allCount = allCount + count;
			if(testStatus==0){
				waitAuditCount +=count;
			}else if(testStatus==1){
				noTestCount +=count;
			}else if(testStatus==2){
				passCount =count;
			}else if(testStatus==3){
				failedCount =count;
			}else if(testStatus==4){
				invalidCount =count;
			}else if(testStatus==5){
				blockCount =count;
			}else if(testStatus==6){
				waitModifyCount =count;
			}
		}
		return "总用例数："+allCount +" 通过数："+passCount +" 未通过数："+failedCount +" 阻塞数："+blockCount+
		" 未测试数：" +noTestCount +" 不适用数：" +invalidCount +" 待修正数："+waitModifyCount +" 待审核数："+waitAuditCount
		;
	}
	
	public DataVo getCaseCount(String taskId){
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as caseCount, t.status ");
		sql.append("  from t_testcasebaseinfo t ");
		sql.append(" where t.taskid = ? ");
		sql.append("group by t.status ");
		List countlist = this.findBySql(sql.toString(),null,taskId);
		if(countlist==null||countlist.isEmpty()){
			return new DataVo();
		}
		int allCount = 0;
		int passCount = 0;
		int failedCount = 0;
		int blockCount = 0;
		int noTestCount =0;
		int invalidCount = 0;
		int waitModifyCount = 0;
		int waitAuditCount = 0;
		
		
		Iterator it = countlist.iterator();
		while (it.hasNext()) {
			Object values[] = (Object[]) it.next();
			int testStatus = Integer.parseInt(values[1].toString());
			int count = Integer.parseInt(values[0].toString());
			allCount = allCount + count;
			if(testStatus==0){
				waitAuditCount +=count;
			}else if(testStatus==1){
				noTestCount +=count;
			}else if(testStatus==2){
				passCount =count;
			}else if(testStatus==3){
				failedCount =count;
			}else if(testStatus==4){
				invalidCount =count;
			}else if(testStatus==5){
				blockCount =count;
			}else if(testStatus==6){
				waitModifyCount =count;
			}
		}
		DataVo dataVo = new DataVo();
		dataVo.setAllCount(allCount);
		dataVo.setBlockCount(blockCount);
		dataVo.setFailedCount(failedCount);
		dataVo.setInvalidCount(invalidCount);
		dataVo.setNoTestCount(noTestCount);
		dataVo.setPassCount(passCount);
		dataVo.setWaitAuditCount(waitAuditCount);
		dataVo.setWaitModifyCount(waitModifyCount);
		return dataVo;

	}
	public String getBugCountStr(String taskId){
		String sql = "select count(*) from T_BUGBASEINFO where TASK_ID=?";
		List countlist = this.findBySql(sql,null,taskId);
		int allCount = Integer.parseInt(countlist.get(0).toString());
		countlist = null;
		if(allCount!=0){
			sql = null;
			sql = "select count(*) from T_BUGBASEINFO where CURRENT_STATE not in (2,3,4,5,22,16) and TASK_ID=?";
			countlist = this.findBySql(sql,null,taskId);
			int validCout = Integer.parseInt(countlist.get(0).toString());
			sql = null;
			sql = "select count(*) from T_BUGBASEINFO where CURRENT_STATE  in (13,14,15,23) and TASK_ID=?";
			countlist = null;
			countlist = this.findBySql(sql,null,taskId);
			int resolCount = Integer.parseInt(countlist.get(0).toString());
			return "总BUG数："+allCount +" 有效BUG数："+validCout +" 己改/关闭BUG数：" +resolCount;
		}
		return "总BUG数："+allCount +" 有效BUG数：0 己改/关闭BUG数：0";
	}
	
	public void getBugCount(String taskId,DataVo dataVo){
		
		//validCout 不含装态不16，也就是非错未确认的
		String sql = "select * from ( \n";
		sql += " (select count(*) as allcount from T_BUGBASEINFO where TASK_ID=?)as aa,  \n"
				+ "(select count(*) as validCout  from T_BUGBASEINFO where CURRENT_STATE not in (2,3,4,5,22) and TASK_ID=? ) bb,\n";
		sql += " (select count(*)  as closedCount from T_BUGBASEINFO where CURRENT_STATE  in (14,15,23) and TASK_ID=?) cc,\n";
		sql +=" (select count(*) as fixCount  from T_BUGBASEINFO where (CURRENT_STATE  = 13 or CURRENT_STATE=26 )  and TASK_ID= ?)dd,"
				+ "(select count(*) as noBugCount  from T_BUGBASEINFO where CURRENT_STATE  = 16  and TASK_ID= ?)ee ) ";
		List<Object[]> countlist = this.findBySql(sql,null,taskId,taskId,taskId,taskId,taskId);
		dataVo.setBugAllCount(Integer.parseInt(countlist.get(0)[0].toString()));
		dataVo.setBugValidCout(Integer.parseInt(countlist.get(0)[1].toString()));
		dataVo.setBugResolCount(Integer.parseInt(countlist.get(0)[2].toString()));
		dataVo.setFixNoConfirmCount(Integer.parseInt(countlist.get(0)[3].toString()));
		dataVo.setNoBugNoConfirmCount(Integer.parseInt(countlist.get(0)[4].toString()));
	}
	
	public String getBugCountStr(){
		String companyId = SecurityContextHolderHelp.getCompanyId();
		String sql = "select count(b.task_id) from T_BUGBASEINFO b join T_TEST_TASK_DETAIL ts on b.task_id=ts.taskid where  ts.TEST_TASK_TATE!=4 and ts.COMPANYID=? ";
		List countlist = this.findBySql(sql,null,companyId);
		int allCount = Integer.parseInt(countlist.get(0).toString());
		countlist = null;
		if(allCount!=0){
			sql = null;
			sql = "select count(b.task_id) from T_BUGBASEINFO b join T_TEST_TASK_DETAIL ts on b.task_id=ts.taskid where  ts.TEST_TASK_TATE!=4 and b.CURRENT_STATE not in (2,3,4,5,22,16) and ts.COMPANYID=? ";
			countlist = this.findBySql(sql,null,companyId);
			int validCout = Integer.parseInt(countlist.get(0).toString());
			sql = null;
			sql = "select count(b.task_id) from T_BUGBASEINFO b join T_TEST_TASK_DETAIL ts on b.task_id=ts.taskid where  ts.TEST_TASK_TATE!=4 and  b.CURRENT_STATE  in (13,14,15,23) and ts.COMPANYID=? ";
			countlist = null;
			countlist = this.findBySql(sql,null,companyId);;
			int resolCount = Integer.parseInt(countlist.get(0).toString());
			return "总BUG数："+allCount +" 有效BUG数："+validCout +" 己改/关闭BUG数：" +resolCount;
		}
		return "总BUG数: "+allCount +" 有效BUG数：0 己改/关闭BUG数：0";
	}
	
	public void buildBugWhereSql(BugManagerDto dto){
		
        boolean inputCondition = false;
        StringBuffer sql = new StringBuffer();
        Map praValues = new HashMap();
        praValues.put("taskId", dto.getTaskId());
        dto.setHqlParamMaps(praValues);
		if(dto.getBug()!=null){
			String testOwnerId = dto.getBug().getTestOwnerId();
			if(testOwnerId!=null &&!"-1".equals(testOwnerId)&&!"".equals(testOwnerId)){
				sql.append(" and t.TEST_OWNER=:TEST_OWNER ");
				praValues.put("TEST_OWNER", testOwnerId);
				inputCondition = true;
			}
			String chargeOwnerId = dto.getBug().getChargeOwner();
			if(chargeOwnerId!=null&&!"".equals(chargeOwnerId.trim())){
				sql.append(" and t.CHARGE_OWNER=:chargeOwnerId");
				praValues.put("chargeOwnerId", chargeOwnerId);
				inputCondition = true;
			}
			Date reptDate = dto.getBug().getReptDate();
			if(reptDate!=null&&dto.getReptDateEnd()==null){
				sql.append(" and to_char(t.BUGDISVDATE,'yyyy-mm-dd') =:reptDate ");
				praValues.put("reptDate", StringUtils.formatShortDate(reptDate));	
				inputCondition = true;
			}else if(reptDate==null&&dto.getReptDateEnd()!=null){
				sql.append(" and to_char(t.BUGDISVDATE,'yyyy-mm-dd') =:reptDate ");
				praValues.put("reptDate", StringUtils.formatShortDate(dto.getReptDateEnd()));	
				inputCondition = true;
			}else if(reptDate!=null&&dto.getReptDateEnd()!=null){
				sql.append(" and t.BUGDISVDATE>=:reptDate  and t.BUGDISVDATE<=:reptDateEnd");
				try {
					praValues.put("reptDate",StringUtils.parseLongDate(StringUtils.formatShortDate(reptDate)+" 00:00:00"));	
					
					praValues.put("reptDateEnd",StringUtils.parseLongDate(StringUtils.formatShortDate(dto.getReptDateEnd())+" 23:59:59"));	
					inputCondition = true;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}		
			Long platformId = dto.getBug().getPlatformId();
			if(platformId!=null&&platformId!=-1){
				sql.append(" and t.PLATFORMID=:platformId");
				praValues.put("platformId", platformId);	
				inputCondition = true;
			}
			Long moduleId = dto.getBug().getModuleId();
			if(moduleId!=null&&!"".equals(moduleId.toString())&&!"-1".equals(moduleId.toString())){
				sql.append(" and t.MODULEID=:moduleId");
				praValues.put("moduleId", moduleId);
				inputCondition = true;
			}
			String moduleNum = dto.getBug().getModuleNum();
			if(moduleNum!=null){
				sql.append(" and t.MODULENUM like:moduleNum");
				praValues.put("moduleNum", moduleNum+"%");
				inputCondition = true;
			}
			Long gradeId = dto.getBug().getBugGradeId();
			if(gradeId!=null&&gradeId!=-1){
				sql.append(" and t.BUGLEVEL=:bugGradeId");
				praValues.put("bugGradeId", gradeId);
				inputCondition = true;
			}
			Long bugFreqId = dto.getBug().getBugFreqId();
			if(bugFreqId!=null&&bugFreqId!=-1){
				sql.append(" and t.BUGFREQ=:bugFreqId");
				praValues.put("bugFreqId", bugFreqId);
				inputCondition = true;
			}

			Long bugOccaId = dto.getBug().getBugOccaId();
			if(bugOccaId!=null&&bugOccaId!=-1){
				sql.append(" and t.BUGOCCA=:bugOccaId");
				praValues.put("bugOccaId", bugOccaId);
				inputCondition = true;
			}	
//			Integer currStateId = dto.getBug().getCurrStateId();
//			if(currStateId!=null&&currStateId!=-1){
//				sql.append(" and t.CURRENT_STATE=:currStateId");
//				praValues.put("currStateId", currStateId);
//				inputCondition = true;
//			}
			
			Long priId = dto.getBug().getPriId();
			if(priId!=null&&priId!=-1){
				sql.append(" and t.PRI=:priId");
				praValues.put("priId", priId);
				inputCondition = true;
			}
			Long sourceId = dto.getBug().getSourceId();
			if(sourceId!=null&&sourceId!=-1){
				sql.append(" and t.SOURCE=:sourceId");
				praValues.put("sourceId", sourceId);
				inputCondition = true;
			}
			Long genePhaseId = dto.getBug().getGenePhaseId();
			if(genePhaseId!=null&&genePhaseId!=-1){
				sql.append(" and t.GENERATEPHASE=:genePhaseId");
				praValues.put("genePhaseId", genePhaseId);
				inputCondition = true;
			}
			Integer relaCaseFlag = dto.getBug().getRelaCaseFlag();
			if(relaCaseFlag!=null&&relaCaseFlag!=-1){
				inputCondition = true;
				if(relaCaseFlag==1){
					sql.append(" and t.REAL_CASE_FLAG>=1");
				}else{
					sql.append(" and t.REAL_CASE_FLAG<1");
				}
			}
			Long geneCauseId = dto.getBug().getGeneCauseId();
			if(geneCauseId!=null&&geneCauseId!=-1){
				sql.append(" and t.GENERATECAUSE=:geneCauseId");
				praValues.put("geneCauseId", geneCauseId);
				inputCondition = true;
			}
			Long bugTypeId = dto.getBug().getBugTypeId();
			if(bugTypeId!=null&&bugTypeId!=-1){
				sql.append(" and t.BUGTYPE=:bugTypeId");
				praValues.put("bugTypeId", bugTypeId);
				inputCondition = true;
			}		
			String devOwnerId = dto.getBug().getDevOwnerId();
			if(devOwnerId!=null&&!"-1".equals(devOwnerId)&&!"".equals(devOwnerId)){
				sql.append(" and t.DEV_OWNER=:devOwnerId");
				praValues.put("devOwnerId", devOwnerId);
				inputCondition = true;
			}
			String nextOwnerId = dto.getBug().getNextOwnerId();
			if(nextOwnerId!=null&&!"-1".equals(nextOwnerId)&&!"".equals(nextOwnerId)){
				sql.append(" and t.bug_num=:nextOwnerId ");
				praValues.put("nextOwnerId", nextOwnerId);
			}
			String bugDesc = dto.getBug().getBugDesc();
			if(bugDesc!=null&&!bugDesc.trim().equals("")){
				sql.append(" and (t.BUGDESC like :bugDesc or t.REPRODUCTSTEP like :bugDesc)");
				praValues.put("bugDesc", "%"+bugDesc+"%");
				inputCondition = true;
				
			}
			Long bugReptVer = dto.getBug().getBugReptVer();
			if(bugReptVer!=null&&bugReptVer!=-1&&!"".equals(devOwnerId)){
				sql.append(" and t.DISCOVER_VER=:bugReptVer");
				praValues.put("bugReptVer", bugReptVer);
				inputCondition = true;
			}
		}
		Integer[] currStateIds = dto.getCurrStateIds();
		if(currStateIds!=null){
			if(currStateIds.length==1&& currStateIds[0].intValue()!=-1){
				sql.append(" and t.CURRENT_STATE=:currStateId");
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
					sql.append(" and t.CURRENT_STATE=:currStateId");
					praValues.put("currStateId", states.get(0));
				}else{
					sql.append(" and t.CURRENT_STATE in(:currStateId)");
					praValues.put("currStateId", states);
				}
				
				inputCondition = true;
			}
			
		}
		if(dto.getDefBug()==1){
			inputCondition = true;
			//this.appendMyBugHql(dto, sql);
			sql.append("  and  t.task_id = :taskId  and  (t.bug_num=:nextOwner or (t.CURRENT_HANDLER=:nextOwner and t.CURRENT_STATE not in(4,5,14,15,22,23))) order by t.CURRENT_HANDL_DATE desc");
			praValues.put("nextOwner", SecurityContextHolderHelp.getUserId());
		}else{
			sql.append("  and  t.task_id = :taskId   order by t.CURRENT_HANDL_DATE desc");
		}
		dto.setHql((this.getExpBugSql() +sql.toString()));
	}

	public  String getExpBugSql() {
		PropertiesBean conf = (PropertiesBean) Context.getInstance().getBean("ContextProperties");
		if(conf.getProperty("config.db.driver")!=null&&conf.getProperty("config.db.driver").indexOf("mysql")>0){
			return expBugMysqlSql.toString();
		}else{
			return expBugOracleSql.toString();
		}
	}
	public  String getExpCaseSql(){
		PropertiesBean conf = (PropertiesBean) Context.getInstance().getBean("ContextProperties");
		if(conf.getProperty("config.db.driver")!=null&&conf.getProperty("config.db.driver").indexOf("mysql")>0){
			return expCaseMysqlSql.toString();
		}else{
			return expCaseOracleSql.toString();
		}
	}
	public List getOutLineDetailInfo(String taskId,List<OutlineInfo> loadNodes ,boolean onlyNormal){

		if(loadNodes==null||loadNodes.isEmpty()){
			return null;
		}
		//排序时，把moduleid加和，是为了和ORACLe兼容，当然这样在MYSQL下牺牲了点性能
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t.moduleid,t.modulenum ,count(t.modulenum) as caseCount,0 as bugCount ");
		sql.append("FROM t_testcasebaseinfo t ");
		sql.append(" join t_outlineinfo o on t.moduleid = o.moduleid  ");
		sql.append(" where t.taskid=?  ");
		String likeStr = this.buildOutLineNumSql(loadNodes);
		if(onlyNormal){
			sql.append(" and o.modulestate !=1 ");
		}
		sql.append(" and (" +likeStr+") ");
		sql.append(" group by t.modulenum,t.moduleid ");
		sql.append(" union all ");
		 
		sql.append(" SELECT t.moduleid ,o.modulenum ,0 as caseCount,count(t.moduleid) as bugCount   ");
		sql.append(" FROM t_bugbaseinfo t join t_outlineinfo o on t.moduleid = o.moduleid ");
		sql.append("  where task_id=?  ");
//		if(onlyNormal){
//			sql.append(" and o.modulestate !=1 ");
//		}
		sql.append(" and (" +likeStr+") ");
		sql.append(" group by o.modulenum,t.moduleid  ");
		return this.findBySql(sql.toString(), null, taskId,taskId);
	}
	
	private String buildOutLineNumSql(List<OutlineInfo>  loadNodes){
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<loadNodes.size(); i++){
			OutlineInfo outLine = loadNodes.get(i);
			
			if(i==0){
				sb.append(" o.moduleNum like '").append(outLine.getModuleNum()).append("%' ") ;
			}else{
				sb.append(" or o.moduleNum like '").append(outLine.getModuleNum()).append("%' ") ;
			}
		}
		return sb.toString();
	}
	
	public List<OutlineInfo> getOutLineInfo(String taskId,List<Long> listId ,boolean onlyNormal){
		//这样的查询，是为因加载节点时，己查询，这时在查时，可以命中缓存
		StringBuffer hql = new StringBuffer("select new OutlineInfo(");
		hql.append(" moduleId,superModuleId,isleafNode, moduleName,moduleState,moduleNum ,klc,moduleLevel,caseCount,quotiety ,scrpCount,sceneCount,reqType)");
		hql.append(" from OutlineInfo where  taskId=:taskId and moduleId in (:listId)  order by seq ,moduleId ");
		Map praValuesMap = new HashMap(2);
		praValuesMap.put("taskId", taskId);
		praValuesMap.put("listId", listId);
		List<OutlineInfo> list = this.findByHqlWithValuesMap(hql.toString(), praValuesMap, false);
		praValuesMap = null;
		return list;
	}
}
