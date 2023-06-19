package cn.com.codes.analysisManager.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aspectj.weaver.ast.Var;

import cn.com.codes.analysisManager.dto.AnalysisDto;
import cn.com.codes.analysisManager.service.AnalysisService;
import cn.com.codes.bugManager.blh.BugFlowConst;
import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.framework.app.services.BaseServiceImpl;
import cn.com.codes.framework.security.Visit;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.object.Function;

public class AnalysisServiceImpl extends BaseServiceImpl implements AnalysisService {
	
	private static StringBuffer menuSql = new StringBuffer();
	private static StringBuffer priviSql = new StringBuffer();
	
	static {
//		menuSql.append("select distinct " );
//		menuSql.append(" f.FUNCTIONID as itemId,f.PARENTID as paretId,f.FUNCTIONNAME as itemName," );
//		menuSql.append(" f.LEVELNUM as levelNum,f.url ,f.SEQ" );
//		menuSql.append("  from T_FUNCTION f ,");
//		menuSql.append("  (select  distinct rf.FUNCTIONID from   T_ROLE_FUNCTION_REAL rf ,");
//		menuSql.append("      (select distinct ur.ROLEID from   T_USER_ROLE_REAL ur ,T_USER u where u.id=? and u.COMPANYID=? and ur.USERID=u.ID)  myrole ");
//		menuSql.append("  where rf.ROLEID= myrole.ROLEID )  myfunction ");
//		menuSql.append(" where  f.FUNCTIONID=myfunction.FUNCTIONID AND f.ISLEAF<>1 and (f.PAGE ='1'  or f.URL='/analysis/analysisAction!goAnalysisMain.action')ORDER BY f.LEVELNUM,f.PARENTID,f.SEQ");
		
		menuSql.append(" select distinct  f.FUNCTIONID ,f.PARENTID ," );
		menuSql.append(" f.FUNCTIONNAME , f.LEVELNUM ,f.url ,f.SEQ" );
		menuSql.append(" from T_FUNCTION f" );
		menuSql.append(" INNER JOIN T_ROLE_FUNCTION_REAL rf ON F.FUNCTIONID = rf.FUNCTIONID" );
		menuSql.append(" inner join T_USER_ROLE_REAL ur on rf.ROLEID = ur.ROLEID" );
		menuSql.append(" inner join T_USER  u on ur.userid=u.id" );
		menuSql.append(" where u.id= ? and u.COMPANYID=? " );
		menuSql.append(" and  f.ISLEAF<>1" );
		menuSql.append(" and (f.PAGE ='1'  or f.URL='/analysis/analysisAction!goAnalysisMain.action')" );
		menuSql.append(" ORDER BY f.LEVELNUM,f.PARENTID,f.SEQ" );
		
		priviSql.append(" select  distinct f.SECURITY_URL from ");
		priviSql.append("   T_FUNCTION f ,");
		priviSql.append("  (select distinct rf.FUNCTIONID from ");
		priviSql.append("  T_ROLE_FUNCTION_REAL rf ,");
		priviSql.append("  (select ur.ROLEID from ");
		priviSql.append("    T_USER_ROLE_REAL ur ,");
		priviSql.append("   T_USER  u");
		priviSql.append("   where u.ID=?  and ")
				.append(" ur.USERID=u.ID)  myrole");
		priviSql.append("   where rf.ROLEID= myrole.ROLEID )  myfunction ");
		priviSql.append("    where f.FUNCTIONID=myfunction.FUNCTIONID AND f.ISLEAF=1 AND f.SECURITY_URL is not null AND  f.PAGE ='1' ");
	}
	

	public void goAnalysisMain(AnalysisDto analysisDto){
		this.loadRepPrivilege();
		this.loadRepTree(analysisDto);
	}
	
	
	private void loadRepPrivilege(){
		
		List<Object> powerList = this.findBySql(priviSql.toString(), null, SecurityContextHolderHelp.getUserId());
		Set<String> repPrivileges = new HashSet<String>();
		Visit visit = SecurityContextHolder.getContext().getVisit();
		if(powerList!=null&&!powerList.isEmpty()){
			for(int i=0; i<powerList.size(); i++){
				String secUrl = (String)powerList.get(i);
				String[] secUrlArr= secUrl.split(";");
				for(String url :secUrlArr){
					repPrivileges.add(url);
				}
				
			}			
		}
		if(visit.getUserInfo().getRepPrivilege()!=null){
			visit.getUserInfo().getRepPrivilege().clear();
		}
		
		visit.getUserInfo().setRepPrivilege(repPrivileges);
	}
	
	private void loadRepTree(AnalysisDto analysisDto){
		
		List<Object[]> menuList = this.findBySql(menuSql.toString(), 
				null,SecurityContextHolderHelp.getUserId(), SecurityContextHolderHelp.getCompanyId());
		StringBuffer authTree = new StringBuffer();
		if(menuList==null||menuList.size()==0){
			analysisDto.setTreeStr(authTree.toString()) ;
			return;
		}
		
		List<Function> menus = new ArrayList<Function>();
		for(Object[] menu :menuList){
			Function function = new Function();
			function.setFunctionId((String)menu[0]);
			function.setParentId((String)menu[1]);
			function.setFunctionName((String)menu[2]);
			if(menu[4] != null){
				function.setUrl(menu[4].toString());
			}
			menus.add(function);
		}
		menus.get(0).setParentId("0");
		for(Function sfun :menus){
			authTree.append(sfun.getParentId());
			authTree.append(",");
			authTree.append(sfun.getFunctionId());
			authTree.append(",");
			authTree.append(sfun.getFunctionName());
			authTree.append(",");
			authTree.append(sfun.getUrl());
			authTree.append(";");			
		}
		analysisDto.setTreeStr(authTree.toString()) ;
	}
	//日修改bug趋势 
	public void getDevDayFixTrend(AnalysisDto analysisDto){
		String taskId = analysisDto.getTaskId();
		String startDate = analysisDto.getStartDate();
		String endDate = analysisDto.getEndDate();
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select base.*  from (select  count(*) as currCount,"+
       "date_format(bh.insdate, '%Y-%c-%d') as fixDate,"+
       "u.name as devName"+
  " from t_bughandhistory bh "+
  "join t_user u on bh.handler  = u.id"+
 " where bh.CURR_DAY_FINAL = 1"+
  " and bh.bugstate = 13"+
  " and  bh.insdate BETWEEN date_format('" + startDate + "','%Y-%c-%d') "+
								" and date_format('" + endDate + "','%Y-%c-%d') "+
 "  and bh.task_id ='"+taskId+"'" +
 " group by date_format(bh.insdate, '%Y-%c-%d'), u.name"+
" union all"+
" select 0 as currCount , date_format('" + startDate + "', '%Y-%c-%d') as fixDate, us.name as devName"+
 " from t_task_useactor ua"+
 " join t_user us on us.id = ua.userid"+
 " where ua.taskid ='"+taskId+"'" +
 "  and ua.actor = 5"+
   
" union all"+

 " select count(base1.insdate) as currCount,"+
" base1.insdate as fixDate,"+
" base1.devName "+
 " from ( select fixSql.*  from"+
        "  (select bh.bugcardid,"+
               "  date_format(bh.insdate, '%Y-%c-%d') as insdate"+
                         "   ,u.name as devName"+
                               "   from t_bughandhistory bh"+
                                 " join t_user u on bh.handler  = u.id"+
                               "  where bh.task_id ='"+taskId+"'" +
                                  " and bh.CURR_DAY_FINAL = 0"+
                                   " and bh.bugstate = 13"+
                                  " and  bh.insdate BETWEEN date_format('" + startDate + "','%Y-%c-%d') "+
								" and date_format('" + endDate + "','%Y-%c-%d') "+
        "  ) fixSql,"+
        "   (select bh.bugcardid,"+
                 " date_format(bh.insdate, '%Y-%c-%d') as insdate"+
                          ",u.name as devName"+
                                 " from t_bughandhistory bh "+
                                "  join t_user u on bh.handler  = u.id"+
                              "   where bh.task_id ='"+taskId+"'" +
                                   " and bh.CURR_DAY_FINAL = 1"+
                                 "  and bh.bugstate in (14, 15, 22, 23)"+
                                   " and bh.insdate BETWEEN date_format('" + startDate + "','%Y-%c-%d') "+
								" and date_format('" + endDate + "','%Y-%c-%d') "+
              " )fix2Sql"+
 
 " where fixSql.insdate = fix2Sql.insdate"+
                           " and fixSql.bugcardid = fix2Sql.bugcardid"+
" ) base1 "+
    " group by base1.insdate,base1.devName"+
  " )base "+
 "  order by TO_DAYS(base.fixDate) asc ");
		List<Object[]> lists = this.findBySql(sqlStr.toString());
		analysisDto.setAlsResult(lists);
	}
	//日编写用例趋势 
	public void getWriteCaseDayTrend(AnalysisDto analysisDto){
		String taskId = analysisDto.getTaskId();
		String startDate = analysisDto.getStartDate();
		String endDate = analysisDto.getEndDate();
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select date_format(tc.CREATDATE, '%Y-%c-%d') as wirteDate,"+
      " u.name,"+
      " count(tc.CREATDATE) as caseCount"+
	  " from t_user u"+
	  " join t_testcasebaseinfo tc on tc.createrId = u.id "+
	  " where  tc.taskid = '"+taskId+"' "+
	  " and tc.CREATDATE BETWEEN date_format('" + startDate + "','%Y-%c-%d') and date_format('" + endDate + "','%Y-%c-%d')"+
	  " group by date_format(tc.CREATDATE, '%Y-%c-%d'), tc.createrId"+
				" order by tc.CREATDATE asc");
      //" order by date_format(tc.CREATDATE, '%Y-%c-%d')");
		List<Object[]> lists = this.findBySql(sqlStr.toString());
		analysisDto.setAlsResult(lists);
	}
	//获取测试人员 
	public void getTester(AnalysisDto analysisDto){
		String taskId = analysisDto.getTaskId();
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT us. NAME, '测试人员' AS tester FROM t_task_useactor ua "+
"JOIN t_user us ON us.id = ua.userid WHERE ua.taskid = '"+taskId+"' AND ua.actor = 1");
		List<Object[]> lists = this.findBySql(sqlStr.toString());
		analysisDto.setAlsResult(lists);
	}
	//日提交bug以总数趋势02
	public void getCommitExistBugCom(AnalysisDto analysisDto){
		String taskId = analysisDto.getTaskId();
		String startDate = analysisDto.getStartDate();
		String endDate = analysisDto.getEndDate();
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT count(b.bugdisvdate) AS disCount,"+
			" date_format(b.bugdisvdate, '%Y-%c-%d') AS disDate FROM"+
			" t_bugbaseinfo b WHERE b.current_state > 6"+
			" AND b.current_state <> 16 AND date_format(b.bugdisvdate, '%Y-%c-%d')"+
			" BETWEEN date_format( '"+startDate+"', '%Y-%c-%d') AND date_format( '"+endDate+"', '%Y-%c-%d') AND b.task_id = '"+taskId+"'"+
			" GROUP BY date_format(b.bugdisvdate, '%Y-%c-%d')"+
			" order by TO_DAYS(disDate) asc");
		List<Object[]> lists = this.findBySql(sqlStr.toString());
		analysisDto.setAlsResult(lists);
	}	
	//日提交bug以及总数趋势01
	public void getCommitExistBugAll(AnalysisDto analysisDto){
		String taskId = analysisDto.getTaskId();
		String startDate = analysisDto.getStartDate();
		String endDate = analysisDto.getEndDate();
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select buildDayBugInfo.vDate as insdate,"+
				" count(buildDayBugInfo.vDate) as existCount "+
				" from( select dateInfo.vDate from ( select date_format("+
				" DATE_ADD('"+startDate+"', INTERVAL id DAY), '%Y-%c-%d' ) as vDate"+
				" from T_HELPER h where h.id <= DATEDIFF('"+endDate+"', '"+startDate+"')"+
				" order BY h.id ) dateInfo, t_bugbaseinfo b"+
				" where b.task_id = '"+taskId+"'"+
				" and b.current_state > 6 and b.current_state <> 16 and b.current_state NOT IN (13, 14, 15, 22, 23)"+
				" and date_format(b.bugdisvdate, '%Y-%c-%d') <= dateInfo.vDate and date_format("+
				" ifnull(b.fix_date, curdate()), '%Y-%c-%d' ) >= dateInfo.vDate"+
				" union all select buildDayBugInfo2.vDate as insdate"+
				" from ( select dateInfo2.vDate from ( select date_format( DATE_ADD('"+startDate+"', INTERVAL id DAY),"+
				" '%Y-%c-%d' ) as vDate from T_HELPER h where h.id <= DATEDIFF('"+endDate+"','"+startDate+"')"+
				" order BY h.id ) dateInfo2, t_bugbaseinfo b2"+
				" where b2.task_id = '"+taskId+"' and b2.current_state > 6"+
				" and b2.current_state <> 16 and b2.current_state IN (14, 15, 22, 23)"+
				" and date_format(b2.bugdisvdate, '%Y-%c-%d') <= dateInfo2.vDate"+
				" and date_format( b2.CURRENT_HandL_DATE, '%Y-%c-%d' ) >= dateInfo2.vDate"+
				" ) buildDayBugInfo2 UNION ALL select buildDayBugInfo3.vDate as insdate"+
				" from ( select dateInfo3.vDate from ( select date_format( DATE_ADD('"+startDate+"', INTERVAL id DAY),"+
				" '%Y-%c-%d' ) as vDate from T_HELPER h where h.id <= DATEDIFF('"+endDate+"', '"+startDate+"')"+
				" order BY h.id ) dateInfo3, t_bugbaseinfo b3"+
				" where b3.task_id = '"+taskId+"'"+
				" and b3.current_state = 13 and date_format(b3.bugdisvdate, '%Y-%c-%d') <= dateInfo3.vDate"+
				" and date_format(curdate(), '%Y-%c-%d') >= dateInfo3.vDate ) buildDayBugInfo3"+
				" ) buildDayBugInfo group by buildDayBugInfo.vDate order by TO_DAYS(buildDayBugInfo.vDate) asc");
		List<Map<String, Object>> lists = this.findBySqlByJDBC(sqlStr.toString());
		analysisDto.setExistAllBugData(lists) ;
	}	
	//日编写用例趋势以及明细 
	public void getTesterExeCaseDayTrend(AnalysisDto analysisDto){
		String taskId = analysisDto.getTaskId();
		String startDate = analysisDto.getStartDate();
		String endDate = analysisDto.getEndDate();
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT base.exeCount,base.exeRest,base.exeDate,u.NAME,base.exeCost"+
			" FROM t_user u,"+
			" (SELECT count(rs.testresult) AS exeCount,rs.testactor AS tester,(rs.testresult) AS exeRest,"+
			" date_format(rs.insdate, '%Y-%c-%d') AS exeDate,"+
			" sum(ifnull(tc.weight, 2) * 5) AS exeCost"+
			" FROM t_testresult rs"+
			" JOIN t_testcasebaseinfo tc ON rs.testcaseid = tc.testcaseid"+
			" GROUP BY rs.testresult,rs.testactor,rs.insdate,rs.taskid"+
			" HAVING rs.taskid = '"+taskId+"'"+
	        " AND rs.insdate BETWEEN date_format('" + startDate + "','%Y-%c-%d') and date_format('" + endDate + "','%Y-%c-%d')"+
			" AND rs.testresult IN (2, 3, 5)"+
			" ) base"+
			" WHERE u.id = base.tester"+
			" ORDER BY TO_DAYS(base.exeDate) asc");
		List<Object[]> lists = this.findBySql(sqlStr.toString());
		analysisDto.setAlsResult(lists);
	}
	//提交|打开|待处理|修改|关闭BUG趋势
	public void getReptFixCloseDayTrend(AnalysisDto analysisDto){
		String taskId = analysisDto.getTaskId();
		String startDate = analysisDto.getStartDate();
		String endDate = analysisDto.getEndDate();
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select trend1.*, existTrend.existCount from (select sum(repNum) as repNum, " + 
	              "sum(openNum) as openNum, sum(fixNum) as fixNum,sum(closeNum) as closeNum, " + 
                "insdate, sum(repNum+fixNum+closeNum+openNum) as currSum " + 
                "from (select count(b.bugdisvdate) as repNum, 0 as openNum,0 as fixNum, " + 
                "0 as closeNum,date_format(b.bugdisvdate, '%Y-%c-%d') as insdate " + 
                "from t_bugbaseinfo b where b.task_id = '" + taskId + "' and b.current_state > 6 and b.current_state<>16 " + 
                "and date_format(b.bugdisvdate,'%Y-%c-%d') >= date('" + startDate + "') and " +
                " date_format(b.bugdisvdate,'%Y-%c-%d') <= date('" + endDate + "') group by date_format(b.bugdisvdate, '%Y-%c-%d') " +   
                "union all select 0 as repNum, count(bh.insdate) as openNum,0 as fixNum, " + 
                "0 as closeNum,date_format(bh.insdate, '%Y-%c-%d') as insdate " + 
                "from t_bughandhistory bh,t_bugbaseinfo b " + 
                "where bh.task_id='" + taskId + "' and bh.CURR_DAY_FINAL = 1 and (bh.bugstate between 7 and 11 or " + 
                "bh.bugstate=24 ) and date_format(bh.insdate,'%Y-%c-%d') >= date('" + startDate + "') and " + 
                "date_format(bh.insdate,'%Y-%c-%d') <= date('" + endDate + "') and b.current_state > 6 and b.current_state<>16 " + 
                "and b.bugcardid = bh.bugcardid group by date_format(bh.insdate, '%Y-%c-%d') " +        
                "union all select 0 as repNum,count(base2.insdate) as openNum,0 as fixNum, " + 
                "0 as closeNum, base2.insdate from (select openSql.insdate " + 
                "from (select distinct bh.bugcardid, date_format(bh.insdate, '%Y-%c-%d') as insdate " + 
                "from t_bughandhistory bh where bh.task_id = '" + taskId + "' and bh.CURR_DAY_FINAL = 0 " + 
                "and (bh.bugstate between 7 and 11 or bh.bugstate=24 ) " + 
                "and date_format(bh.insdate,'%Y-%c-%d') >= date('" + startDate + "') and " + 
                "date_format(bh.insdate,'%Y-%c-%d') <= date('" + endDate + "')) openSql,(select distinct bh.bugcardid,date_format(bh.insdate, '%Y-%c-%d') as insdate " + 
                "from t_bughandhistory bh,t_bugbaseinfo b where bh.task_id = '" + taskId + "' " + 
                "and bh.CURR_DAY_FINAL = 1 and bh.bugstate in (13,14, 15, 22, 23) and b.current_state > 6 and b.current_state<>16 " + 
   			  "and b.bugcardid = bh.bugcardid and date_format(bh.insdate,'%Y-%c-%d') >= date('" + startDate + "') and " + 
                "date_format(bh.insdate,'%Y-%c-%d') <= date('" + endDate + "')) fix2Sql where openSql.insdate = fix2Sql.insdate and openSql.bugcardid = fix2Sql.bugcardid  " +                  
                ") base2 group by base2.insdate union all select 0 as repNum, 0 as openNum, count(bh.insdate) as fixNum, " + 
                "0 as closeNum, date_format(bh.insdate, '%Y-%c-%d') as insdate from t_bughandhistory bh " + 
                "where bh.task_id='" + taskId + "' and bh.CURR_DAY_FINAL = 1 and bh.bugstate = 13 " + 
                "and date_format(bh.insdate,'%Y-%c-%d') >= date('" + startDate + "') and " + 
                "date_format(bh.insdate,'%Y-%c-%d') <= date('" + endDate + "') group by date_format(bh.insdate, '%Y-%c-%d')  union all " + 
	              "select 0 as repNum, 0 as openNum, count(base.insdate) as fixNum, 0 as closeNum, " + 
 	              "base.insdate from (select fixSql.insdate from (select bh.bugcardid, " + 
                "date_format(bh.insdate, '%Y-%c-%d') as insdate from t_bughandhistory bh " + 
                "where bh.task_id = '" + taskId + "' and bh.CURR_DAY_FINAL = 0 and bh.bugstate = 13 " + 
                "and date_format(bh.insdate,'%Y-%c-%d') >= date('" + startDate + "') and " + 
                "date_format(bh.insdate,'%Y-%c-%d') <= date('" + endDate + "')) fixSql, (select bh.bugcardid, date_format(bh.insdate, '%Y-%c-%d') as insdate " + 
                "from t_bughandhistory bh where bh.task_id = '" + taskId + "' and bh.CURR_DAY_FINAL = 1 " + 
                "and bh.bugstate in (14, 15, 22, 23) and date_format(bh.insdate,'%Y-%c-%d') >= date('" + startDate + "') and " + 
                "date_format(bh.insdate,'%Y-%c-%d') <= date('" + endDate + "')) fix2Sql  where fixSql.insdate = fix2Sql.insdate and fixSql.bugcardid = fix2Sql.bugcardid " + 
  	          ") base group by base.insdate  union all select 0 as repNum, 0 as openNum, 0 as fixNum, " + 
                "count(bh.insdate) as closeNum, date_format(bh.insdate, '%Y-%c-%d') as insdate " + 
                "from t_bughandhistory bh where  bh.task_id='" + taskId + "' and bh.CURR_DAY_FINAL = 1 and bh.bugstate in (14, 15, 22, 23) " + 
                "and date_format(bh.insdate,'%Y-%c-%d') >= date('" + startDate + "') and " + 
                "date_format(bh.insdate,'%Y-%c-%d') <= date('" + endDate + "') group by date_format(bh.insdate, '%Y-%c-%d')) baseData " + 
                "group by insdate order by insdate) trend1 left outer join ( " + 
                "select buildDayBugInfo.vDate as insdate,count(buildDayBugInfo.vDate)as existCount " + 
                "from (select dateInfo.vDate from (select " + 
                "date_format(DATE_ADD(date_format('" + startDate + "','%Y-%c-%d'),INTERVAL id DAY), '%Y-%c-%d')  as vDate " + 
                "from T_HELPER h where h.id <= DATEDIFF(date_format('" +  endDate + "','%Y-%c-%d'),date_format('" + startDate + "','%Y-%c-%d')) " + 
                "order by h.id) dateInfo, t_bugbaseinfo b where b.task_id = '" + taskId + "' " + 
                "and b.current_state > 6 and b.current_state <> 16 and b.current_state  not in (13,14, 15, 22, 23) " + 
                "and date_format(b.bugdisvdate, '%Y-%c-%d') <= dateInfo.vDate " + 
                "and date_format(ifnull(b.fix_date,  curdate()), '%Y-%c-%d') >= dateInfo.vDate union  all " + 
                "select buildDayBugInfo2.vDate as insdate from (select dateInfo2.vDate " + 
                "from (select date_format(DATE_ADD(date_format('" + startDate + "','%Y-%c-%d'),INTERVAL id DAY), '%Y-%c-%d')  as vDate " + 
                "from T_HELPER h where h.id <= DATEDIFF(date_format('" +  endDate + "','%Y-%c-%d'),date_format('" + startDate + "','%Y-%c-%d')) order by h.id) dateInfo2, " + 
                "t_bugbaseinfo b2 where b2.task_id = '" + taskId + "' " + 
                "and b2.current_state > 6 and b2.current_state <> 16 and b2.current_state in (14, 15, 22, 23) " + 
                "and date_format(b2.bugdisvdate, '%Y-%c-%d') <= dateInfo2.vDate " + 
                "and date_format(b2.CURRENT_HANDL_DATE, '%Y-%c-%d') >= dateInfo2.vDate ) buildDayBugInfo2  " +          
                "union all select buildDayBugInfo3.vDate as insdate from ( select dateInfo3.vDate " + 
                "from (select date_format(DATE_ADD(date_format('" + startDate + "','%Y-%c-%d'),INTERVAL id DAY), '%Y-%c-%d')  as vDate " + 
                "from T_HELPER h where h.id <= DATEDIFF(date_format('" + endDate + "','%Y-%c-%d'),date_format('" + startDate  + "','%Y-%c-%d')) " + 
                "order by h.id) dateInfo3, t_bugbaseinfo b3 where b3.task_id = '" + taskId + "' " + 
                "and b3.current_state =13 and date_format(b3.bugdisvdate, '%Y-%c-%d') <= " + 
                "dateInfo3.vDate and date_format(curdate(), '%Y-%c-%d') >= dateInfo3.vDate " + 
                ") buildDayBugInfo3 ) buildDayBugInfo group by buildDayBugInfo.vDate " + 
                "order by buildDayBugInfo.vDate ) existTrend on  existTrend.insdate = trend1.insdate ORDER BY " + 
		        "TO_DAYS(trend1.insdate) ASC");
		System.console();
		List<Object[]> lists = this.findBySql(sqlStr.toString());
		analysisDto.setAlsResult(lists);
		}
	
	    //获取--日提交|关闭BUG趋势
		public void getTesterDayCommitTrend(AnalysisDto analysisDto){
			String taskId = analysisDto.getTaskId();
			String startDate = analysisDto.getStartDate();
			String endDate = analysisDto.getEndDate();
			StringBuilder sqlStr = new StringBuilder();
			
			sqlStr.append("select count(*) as currCount,date_format(b.bugdisvdate, '%Y-%c-%d') as disDate," +
                          "u.name from t_bugbaseinfo b join t_user u on b.bugdisvperid = u.id " +
                          "where  b.current_state not in (2,3,4,5,22,16)  "  +
                          "and date(date_format(b.bugdisvdate, '%Y-%c-%d')) between date_format('" + startDate + "','%Y-%c-%d')  and " +
                          "date_format('" + endDate + "','%Y-%c-%d') and b.task_id = '" + taskId + "' " +
                          "group by date_format(b.bugdisvdate, '%Y-%c-%d'), u.name union all " + 
                          "select 0 as currCount, date_format('" + startDate + "','%Y-%c-%d') as disDate ,us.name " + 
                          "from t_task_useactor ua join t_user us on us.id = ua.userid " + 
                           "where ua.taskid = '" + taskId + "' and ua.actor = 1"+
					        " ORDER BY TO_DAYS(disDate) asc");
			
			List<Object[]> lists = this.findBySql(sqlStr.toString());
			analysisDto.setAlsResult(lists);
		}
		
		//获取--日提交|关闭BUG趋势---关闭
		public void getTesterDayCommitTrendForClosed(AnalysisDto analysisDto){
			String taskId = analysisDto.getTaskId();
			String startDate = analysisDto.getStartDate();
			String endDate = analysisDto.getEndDate();
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append("select count(*) as currCount, date_format(b.CURRENT_HANDL_DATE, '%Y-%c-%d') as disDate, " + 
                          "u.name from t_bugbaseinfo b join t_user u on b.bugdisvperid = u.id " + 
                          "where  (b.CURRENT_STATE= 14 or b.CURRENT_STATE= 15 or b.CURRENT_STATE= 22 or b.CURRENT_STATE= 23) " + 
                          "and date_format(b.CURRENT_HANDL_DATE, '%Y-%c-%d') between date_format('" + startDate + "','%Y-%c-%d') and " + 
                          "date_format('" + endDate + "','%Y-%c-%d') and b.task_id = '" + taskId + "' group by date_format(b.CURRENT_HANDL_DATE, '%Y-%c-%d'), u.name " + 
                          "union all select 0 as currCount, date_format('" + startDate + "','%Y-%c-%d') as disDate ,us.name " + 
                          "from t_task_useactor ua join t_user us on us.id = ua.userid " + 
                          "where ua.taskid = '" + taskId + "' and ua.actor = 1"+
					      " ORDER BY TO_DAYS(disDate) asc");
			
			List<Object[]> lists = this.findBySql(sqlStr.toString());
			analysisDto.setAlsResult(lists);
		}
		
		//获取--版本间提交及BUG总数趋势
		public void getCommitExistBugBuildStat(AnalysisDto analysisDto){
			String taskId = analysisDto.getTaskId();
			String startDate = analysisDto.getStartDate();
			String endDate = analysisDto.getEndDate();
			StringBuilder sqlStr = new StringBuilder();		
			
			sqlStr.append("select commitinfo.*,allBugInfo2.* from ( " +
                          "select baseInfo.version_num, count(baseInfo.bugcardid) as disCount,baseInfo.seq " + 
                          "from (select verInfo.version_num, verInfo.seq, b.bugcardid " +
                          "from (select v.version_id, v.version_num, v.seq from t_software_version v " + 
                          "where v.taskid='" + taskId + "' and v.ver_status = 0 order by v.seq) verInfo " + 
                          "left join t_bugbaseinfo b on b.discover_ver = verInfo.version_id and  b.current_state > 6 and b.current_state<>16 and b.current_state<>22) baseInfo " +
                          "group by baseInfo.version_num, baseInfo.seq order by baseInfo.seq )commitinfo " +
                          "left outer join  ( select allBugInfo.vverNum,count(allBugInfo.vverNum) as existCount " + 
                          "from (select verInfo.vverNum, verInfo.vseq from (select disVerInfo.disVerSeq, fixVerInfo.fixVerSeq " + 
                          "from (select bugInfo.Bugcardid, ver.seq as disVerSeq, ver.version_num as disVer " +
                          "from t_bugbaseinfo bugInfo join t_software_version ver on ver.version_id =  bugInfo.discover_ver " + 
                           "and bugInfo.Task_Id = ver.taskid where bugInfo.Task_Id ='" + taskId + "' and ver.ver_status = 0 " + 
                           "and bugInfo.current_state > 6 and bugInfo.current_state <> 16  and bugInfo.current_state <> 22 " + 
                          "order by ver.seq) disVerInfo  left join (select bugInfo.Bugcardid, ver.seq as fixVerSeq " + 
                           "from t_bugbaseinfo bugInfo join t_software_version ver on ver.version_id = bugInfo.Fix_Version " + 
                          "and bugInfo.Task_Id = ver.taskid where bugInfo.Task_Id ='" + taskId + "' " + 
                          "and ver.ver_status = 0 and bugInfo.current_state > 6 and bugInfo.current_state <> 16  and bugInfo.current_state <> 22 " + 
                          "order by ver.seq) fixVerInfo on disVerInfo.Bugcardid = fixVerInfo.Bugcardid) bugBaseInfo, " + 
                          "(select v.version_num as vverNum, v.seq as vseq from t_software_version v " + 
                          "where v.taskid ='" + taskId + "'  and v.ver_status = 0 order by v.seq) verInfo " + 
                          "where bugBaseInfo.disVerSeq <= verInfo.vseq and ifnull(bugBaseInfo.fixVerSeq, 999999999) >= verInfo.vseq " +
                          "ORDER BY verInfo.vseq) allBugInfo group by allBugInfo.vverNum, allBugInfo.vseq " + 
                          "order by allBugInfo.vseq )allBugInfo2  " + 
                          "on commitinfo.version_num = allBugInfo2.vverNum order by commitinfo.seq");
			List<Object[]> lists = this.findBySql(sqlStr.toString());
			analysisDto.setAlsResult(lists);
		}
		
		//开发人员待改BUG统计
		public void getBugFixPersonStat(AnalysisDto analysisDto){
			String taskId = analysisDto.getTaskId();
			String startDate = analysisDto.getStartDate();
			String endDate = analysisDto.getEndDate();
			StringBuilder sqlStr = new StringBuilder();
			
			sqlStr.append("select count(*) as currCount, devName, bugtype " + 
                          "from (select u.name as devName, bugtp.enumname as bugtype " + 
                          "from T_BUGBASEINFO b join t_user u on u.id = b.dev_owner " + 
                          "join T_TYPEDEFINE bugtp on b.buglevel = bugtp.enumid " + 
                          "where b.DEV_OWNER is not null and b.TASK_ID = '" + taskId + "' " + 
                          "and (b.CURRENT_STATE between 7 and 11 or b.CURRENT_STATE between 17 and 21) " + 
                          "union all select us.name as devName, '80' as bugtype from t_task_useactor ua " + 
                          "join t_user us on us.id = ua.userid where ua.taskid = '" + taskId + "' and ua.actor = 5 " + 
                          "union all select '80' as devName, bugtp.enumname as bugtype from T_TYPEDEFINE bugtp " + 
                          "where bugtp.indentifier = 2 ) base group by base.devName, base.bugtype ");
			List<Object[]> lists = this.findBySql(sqlStr.toString());
			analysisDto.setAlsResult(lists);
		}
		
		//开发人员修改BUG分析
		public void getDevFixDataSet(AnalysisDto analysisDto){
			String taskId = analysisDto.getTaskId();
			String startDate = analysisDto.getStartDate();
			String endDate = analysisDto.getEndDate();
			StringBuilder sqlStr = new StringBuilder();
			
			sqlStr.append("select count(*) as currCount, devName, bugtype " + 
                          "from (select u.name as devName, bugtp.enumname as bugtype " + 
                          "from T_BUGBASEINFO b join t_user u on u.id = b.dev_owner " + 
                          "join T_TYPEDEFINE bugtp on b.buglevel = bugtp.enumid " + 
                          "where b.DEV_OWNER is not null  and b.TASK_ID = '" + taskId + "' " +
                          "and (b.CURRENT_STATE =13 or b.CURRENT_STATE =14) union all " + 
                          "select us.name as devName, '80' as bugtype from t_task_useactor ua " + 
                          "join t_user us on us.id = ua.userid where ua.taskid = '" + taskId + "' " + 
                          "and ua.actor = 5 union all select '80' as devName, bugtp.enumname as bugtype " + 
                          "from T_TYPEDEFINE bugtp where bugtp.indentifier = 2 ) base " + 
                          "group by base.devName, base.bugtype ");
			List<Object[]> lists = this.findBySql(sqlStr.toString());
			analysisDto.setAlsResult(lists);
		}
		
		//已关闭BUG按天龄期统计
		public void getBugExistDayStat(AnalysisDto analysisDto){
			String taskId = analysisDto.getTaskId();
			String startDate = analysisDto.getStartDate();
			String endDate = analysisDto.getEndDate();
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append("select base.age ,base.BUGLEVEL,count(*) as bugCount from ( " + 
                          "select baseInfo.age, ty.enumname as BUGLEVEL " + 
                          "FROM t_typedefine ty, (select '0天' as age, b.BUGLEVEL " +  
                          "from t_bugbaseinfo b  where b.task_id =  '" + taskId + "' and b.current_state in (14, 15, 22, 23) " + 
                          "and date_format(b.BUGDISVDATE, '%Y-%c-%d') = date_format(b.CURRENT_HANDL_DATE, '%Y-%c-%d') " + 
                          "union all (select '1天' as age, b.BUGLEVEL from t_bugbaseinfo b " + 
                          "where b.task_id =  '" + taskId + "' and  b.current_state in (14, 15, 22, 23) " + 
                          "and DATEDIFF(date_format(b.CURRENT_HANDL_DATE, '%Y-%c-%d'), date_format(b.BUGDISVDATE, '%Y-%c-%d')) = 1) " + 
                          "union all ( select '2天' as age, b.BUGLEVEL from t_bugbaseinfo b " + 
                          "where b.task_id =  '" + taskId + "' and  b.current_state in (14, 15, 22, 23) and  DATEDIFF(date_format(b.CURRENT_HANDL_DATE, '%Y-%c-%d'), " + 
                          "date_format(b.BUGDISVDATE, '%Y-%c-%d'))  = 2 ) " + 
                          "union all (select '3天' as age, b.BUGLEVEL  from t_bugbaseinfo b " + 
                          "where b.task_id =  '" + taskId + "' and  b.current_state in (14, 15, 22, 23) and DATEDIFF(date_format(b.CURRENT_HANDL_DATE, '%Y-%c-%d'), " + 
                          "date_format(b.BUGDISVDATE, '%Y-%c-%d'))  = 3 ) union all ( " + 
                          "select '3天以上' as age, b.BUGLEVEL from t_bugbaseinfo b " + 
                          "where b.task_id =  '" + taskId + "' and  b.current_state in (14, 15, 22, 23) and DATEDIFF(date_format(b.CURRENT_HANDL_DATE, '%Y-%c-%d'), " + 
                          "date_format(b.BUGDISVDATE, '%Y-%c-%d'))  > 3 )) baseInfo where ty.enumid = baseInfo.BUGLEVEL " + 
                          "and ty.indentifier = 2  union all (select '0天' as age, '80' as BUGLEVEL from dual) " + 
                          "union all (select '1天' as age, '80' as BUGLEVEL from dual) union all (select '2天' as age, '80' as BUGLEVEL from dual) " + 
                          "union all (select '3天' as age, '80' as BUGLEVEL from dual) union all (select '3天以上' as age, '80' as BUGLEVEL from dual) " + 
                          " )base  group by base.age,base.BUGLEVEL");
			
			List<Object[]> lists = this.findBySql(sqlStr.toString());
			analysisDto.setAlsResult(lists);
		}
		
		
		//待处理BUG按天绝对龄期分析
		public void getBugExistDay4NoFixStatAbsolute(AnalysisDto analysisDto){
			String taskId = analysisDto.getTaskId();
			String startDate = analysisDto.getStartDate();
			String endDate = analysisDto.getEndDate();
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append("select base.age ,base.BUGLEVEL,count(*) as bugCount from ( " +
                          "select baseInfo.age, ty.enumname as BUGLEVEL " + 
                          "FROM t_typedefine ty, (select '0天' as age, b.BUGLEVEL " + 
                          "from t_bugbaseinfo b where b.task_id = '" + taskId + "' and b.current_state   not in (3,4,5,14, 15, 22, 23) " + 
                          "and date_format(b.BUGDISVDATE, '%Y-%c-%d') =  date_format(curdate(), '%Y-%c-%d') " + 
                          "union all (select '1天' as age, b.BUGLEVEL from t_bugbaseinfo b " + 
                          "where b.task_id = '" + taskId + "' and  b.current_state   not in (3,4,5,14, 15, 22, 23) " + 
                          "and DATEDIFF(date_format(curdate(), '%Y-%c-%d'), date_format(b.BUGDISVDATE, '%Y-%c-%d')) = 1 " + 
                          ") union all ( select '2天' as age, b.BUGLEVEL from t_bugbaseinfo b " + 
                          "where b.task_id = '" + taskId + "' and  b.current_state   not in (3,4,5,14, 15, 22, 23) " +
                          "and  DATEDIFF(date_format(curdate(), '%Y-%c-%d'), date_format(b.BUGDISVDATE, '%Y-%c-%d'))  = 2 " +
                          ") union all (select '3天' as age, b.BUGLEVEL from t_bugbaseinfo b " + 
                          "where b.task_id = '" + taskId + "' and  b.current_state   not in (3,4,5,14, 15, 22, 23) " + 
                          "and DATEDIFF(date_format(curdate(), '%Y-%c-%d'), date_format(b.BUGDISVDATE, '%Y-%c-%d'))  = 3 " + 
                          ") union all ( select '3天以上' as age, b.BUGLEVEL from t_bugbaseinfo b " + 
                          "where b.task_id = '" + taskId + "' and  b.current_state not in (3,4,5,14, 15, 22, 23) " + 
                          "and  DATEDIFF(date_format(curdate(), '%Y-%c-%d'), date_format(b.BUGDISVDATE, '%Y-%c-%d'))  > 3 " + 
                          ")) baseInfo where ty.enumid = baseInfo.BUGLEVEL  and ty.indentifier = 2 " +
                          "union all (select '0天' as age, '80' as BUGLEVEL from dual) " + 
							"union all (select '1天' as age, '80' as BUGLEVEL from dual) " +
							"union all (select '2天' as age, '80' as BUGLEVEL from dual) " +
							"union all (select '3天' as age, '80' as BUGLEVEL from dual) " +
							"union all (select '3天以上' as age, '80' as BUGLEVEL from dual) " +
                            ")base group by base.age,base.BUGLEVEL");
			List<Object[]> lists = this.findBySql(sqlStr.toString());
			analysisDto.setAlsResult(lists);
		}
		
		//待处理BUG按天龄期分析
		public void getBugExistDay4NoFixStat(AnalysisDto analysisDto){
			String taskId = analysisDto.getTaskId();
			String startDate = analysisDto.getStartDate();
			String endDate = analysisDto.getEndDate();
			StringBuilder sqlStr = new StringBuilder();		
		    
		sqlStr.append("select base.age ,base.BUGLEVEL,count(*) as bugCount from ( " +
					"select baseInfo.age, ty.enumname as BUGLEVEL FROM t_typedefine ty," +
					"(select '0天' as age, b.BUGLEVEL from t_bugbaseinfo b "+
					"where b.task_id = '" + taskId + "'  and b.current_state   not in (3,4,5,14, 15, 22, 23) " +
					"and date_format(b.CURRENT_HANDL_DATE, '%Y-%c-%d') = date_format(curdate(), '%Y-%c-%d') "+
					"union all (select '1天' as age, b.BUGLEVEL from t_bugbaseinfo b " +
					"where b.task_id = '" + taskId + "'  and  b.current_state   not in (3,4,5,14, 15, 22, 23) " + 
					"and DATEDIFF(date_format(curdate(), '%Y-%c-%d'),date_format(b.CURRENT_HANDL_DATE, '%Y-%c-%d')) = 1) union all ( " +
					"select '2天' as age, b.BUGLEVEL from t_bugbaseinfo b " +
					"where b.task_id = '" + taskId + "'  and  b.current_state   not in (3,4,5,14, 15, 22, 23) " +
					"and  DATEDIFF(date_format(curdate(), '%Y-%c-%d'),date_format(b.CURRENT_HANDL_DATE, '%Y-%c-%d'))  = 2)  union all (select '3天' as age, b.BUGLEVEL " +
					"from t_bugbaseinfo b where b.task_id = '" + taskId + "'  and  b.current_state   not in (3,4,5,14, 15, 22, 23) and DATEDIFF(date_format(curdate(), '%Y-%c-%d'), " +
					"date_format(b.CURRENT_HANDL_DATE, '%Y-%c-%d'))  = 3) union all ( " +
					"select '3天以上' as age, b.BUGLEVEL  from t_bugbaseinfo b " +
					"where b.task_id = '" + taskId + "'  and  b.current_state not in (3,4,5,14, 15, 22, 23) " +
					"and ( DATEDIFF(date_format(curdate(), '%Y-%c-%d'),date_format(b.CURRENT_HANDL_DATE, '%Y-%c-%d') )  > 3 or ( " +
					"date_format( b.CURRENT_HANDL_DATE, '%Y-%c-%d') = date_format(b.BUGDISVDATE, '%Y-%c-%d') " +
					"and DATEDIFF(date_format(curdate(), '%Y-%c-%d'),date_format(b.BUGDISVDATE, '%Y-%c-%d') " +
					")  >3)))) baseInfo " +
					"where ty.enumid = baseInfo.BUGLEVEL and ty.indentifier = 2 " +
					"union all (select '0天' as age, '80' as BUGLEVEL from dual) " +
					"union all (select '1天' as age, '80' as BUGLEVEL from dual) " +
					"union all (select '2天' as age, '80' as BUGLEVEL from dual) " +
					"union all (select '3天' as age, '80' as BUGLEVEL from dual) "  +
					"union all (select '3天以上' as age, '80' as BUGLEVEL from dual) " +
					")base group by base.age,base.BUGLEVEL ");
		List<Object[]> lists = this.findBySql(sqlStr.toString());
		analysisDto.setAlsResult(lists);
	}
	
	//BUG类型统计
	public void getBugTypeStat(AnalysisDto analysisDto){
		String taskId = analysisDto.getTaskId();
		String startDate = analysisDto.getStartDate();
		String endDate = analysisDto.getEndDate();
		StringBuilder sqlStr = new StringBuilder();		
/*		sqlStr.append("select baseInfo.enumname, sum(baseInfo.bugCount) as bugCount " + 
                      "from (select bugBase.bugCount, ty.enumname from t_typedefine ty " + 
                      "join (select t.bugtype, count(t.bugtype) bugCount from t_bugbaseinfo t " + 
                      "group by t.task_id, t.current_state, t.bugtype " + 
                      "having t.task_id = '" + taskId + "'  and t.current_state > 6 and t.current_state > 6) bugBase  " + 
                      "on bugBase.bugtype =ty.enumid " + 
                      "union all select 0, tp.enumname from t_typedefine tp where tp.indentifier = 0) baseInfo " + 
                      "group by baseInfo.enumname");*/
		
		sqlStr.append("select baseInfo.enumname, sum(baseInfo.bugCount) as bugCount " + 
                      "from (select bugBase.bugCount, ty.enumname from t_typedefine ty " + 
                      "join (select t.bugtype, count(t.bugtype) bugCount from t_bugbaseinfo t " + 
                      "group by t.task_id, t.current_state, t.bugtype " + 
                      "having t.task_id = '" + taskId + "' and t.current_state > 6 and t.current_state !=22) bugBase " + 
                      "on bugBase.bugtype =ty.enumid union all " + 
                      "select 0, tp.enumname from t_typedefine tp where tp.indentifier = 0) baseInfo " + 
                      "group by baseInfo.enumname");
		List<Object[]> lists = this.findBySql(sqlStr.toString());
		analysisDto.setAlsResult(lists);
	}
	
	//BUG引入阶段分析
	public void getBugImpPhaseStat(AnalysisDto analysisDto){
		String taskId = analysisDto.getTaskId();
		String startDate = analysisDto.getStartDate();
		String endDate = analysisDto.getEndDate();
		StringBuilder sqlStr = new StringBuilder();	
	/*	sqlStr.append("select baseInfo.enumname, sum(baseInfo.bugCount) as bugCount " + 
                     "from (select bugBase.bugCount, ty.enumname from t_typedefine ty " + 
                     "join (select t.GENERATEPHASE, count(t.GENERATEPHASE) bugCount from t_bugbaseinfo t " + 
                     "group by t.task_id, t.current_state, t.GENERATEPHASE " + 
                     "having t.task_id = '" + taskId + "'  and t.current_state > 6 and  t.current_state != 16) bugBase " +  
                     "on bugBase.GENERATEPHASE =ty.enumid union all " + 
                     "select 0, tp.enumname from t_typedefine tp where tp.indentifier = 1) baseInfo " + 
                     "group by baseInfo.enumname");*/
		sqlStr.append("select baseInfo.enumname, sum(baseInfo.bugCount) as bugCount " +
                      "from (select bugBase.bugCount, ty.enumname from t_typedefine ty " + 
                      "join (select t.GENERATEPHASE, count(t.GENERATEPHASE) bugCount from t_bugbaseinfo t " + 
                      "group by t.task_id, t.current_state, t.GENERATEPHASE " + 
                      "having t.task_id = '" + taskId + "' and t.current_state > 6 and  t.current_state != 16 and  t.current_state != 22) bugBase " + 
                      "on bugBase.GENERATEPHASE =ty.enumid union all " + 
                      "select 0, tp.enumname from t_typedefine tp where tp.indentifier = 1) baseInfo " + 
                      "group by baseInfo.enumname");
		List<Object[]> lists = this.findBySql(sqlStr.toString());
		analysisDto.setAlsResult(lists);
	}
	
	//测试人员BUG质量分析
	public void getTesterBugQuality(AnalysisDto analysisDto){
		String taskId = analysisDto.getTaskId();
		String startDate = analysisDto.getStartDate();
		String endDate = analysisDto.getEndDate();
		StringBuilder sqlStr = new StringBuilder();	
/*		sqlStr.append("select bugBaseInfo.testName,count(bugBaseInfo.bugcardid) as bugCount,count(bugBaseInfo.msg_id) as msgCount from ( " +      
				      "select base.*, msg.msg_id from  (select u.name as testName,b.bugcardid from T_BUGBASEINFO b " + 
				      "join t_user u on u.id = b.bugdisvperid where b.DEV_OWNER is not null " + 
				      "and b.TASK_ID =  '" + taskId + "'  and b.CURRENT_STATE >6 and   b.CURRENT_STATE !=16 " + 
				      "union all select us.name as devName,null as bugcardid from t_task_useactor ua " + 
				      "join t_user us on us.id = ua.userid where ua.taskid = '" + taskId + "'  " + 
				      "and ua.actor = 1 and us.id not in ( " + 
				      "select distinct bugdisvperid from T_BUGBASEINFO b where b.task_id =  '" + taskId + "'  ) " + 
				      ")base left join T_MSG_COMMUNION msg on base.bugcardid = msg.bug_id  " + 
                      ") bugBaseInfo group by bugBaseInfo.testName " ); */
		sqlStr.append("select bugBaseInfo.testName,count(bugBaseInfo.bugcardid) as bugCount,count(bugBaseInfo.msg_id) as msgCount from ( " +  
                      "select base.*, msg.msg_id from (select u.name as testName,b.bugcardid from T_BUGBASEINFO b " + 
                      "join t_user u on u.id = b.bugdisvperid where b.DEV_OWNER is not null " + 
                      "and b.TASK_ID = '" + taskId + "' and b.CURRENT_STATE >6 and   b.CURRENT_STATE !=16 " + 
                      "union all select us.name as devName,null as bugcardid  from t_task_useactor ua " + 
                      "join t_user us on us.id = ua.userid where ua.taskid = '" + taskId + "' " + 
                      "and ua.actor = 1 and us.id not in (select distinct bugdisvperid from T_BUGBASEINFO b where b.task_id = '" + taskId + "' " + 
                      "))base left join T_MSG_COMMUNION msg on base.bugcardid = msg.bug_id  " + 
                      ") bugBaseInfo group by bugBaseInfo.testName ");
			List<Object[]> lists = this.findBySql(sqlStr.toString());
			analysisDto.setAlsResult(lists);
	}
	
	//BUG状态分布统计
	public void getBugStatusDistbuStat(AnalysisDto analysisDto){
		String taskId = analysisDto.getTaskId();
		String startDate = analysisDto.getStartDate();
		String endDate = analysisDto.getEndDate();
		StringBuilder sqlStr = new StringBuilder();	
		sqlStr.append("select b.current_state, count(*) from t_BUGBASEINFO b " + 
                      "where b.TASK_ID = '" + taskId + "' " + 
                      "group by b.current_state order by b.current_state " ); 
		
		List<Object[]> lists = this.findBySql(sqlStr.toString());
		for(Integer i=0;i<lists.size();i++){
			Integer bugstatus = (Integer)lists.get(i)[0];
			String stateName = BugFlowConst.getStateName(bugstatus);
			lists.get(i)[0] = stateName;
		}
		analysisDto.setAlsResult(lists);
	}
	
	//获取测试人员提交关闭bug统计 
	public void getTesterBugStat(AnalysisDto analysisDto){
		String taskId = analysisDto.getTaskId(); 
		StringBuilder sqlStr = new StringBuilder();	
		sqlStr.append("SELECT count(*) AS currCount,devName,bugtype"+
	    " FROM ( SELECT u. NAME AS devName, bugtp.enumname AS bugtype"+
	    " FROM T_BUGBASEINFO b"+
	    " JOIN t_user u ON u.id = b.bugdisvperid"+
	    " JOIN T_TYPEDEFINE bugtp ON b.buglevel = bugtp.enumid"+
	    " WHERE b.TASK_ID = '"+taskId+"'"+
	    " AND b.CURRENT_STATE NOT IN (2, 3, 4, 5, 22, 16)"+
	    " UNION ALL"+
	    " SELECT us. NAME AS devName, '80' AS bugtype"+
	    " FROM t_task_useactor ua"+
	    " JOIN t_user us ON us.id = ua.userid"+
	    " WHERE ua.taskid = '"+taskId+"'"+
	    " AND ua.actor = 1"+
	    " UNION ALL"+
	    " SELECT '80' AS devName, bugtp.enumname AS bugtype"+
	    " FROM T_TYPEDEFINE bugtp"+
	    " WHERE bugtp.indentifier = 2"+
	    " ) base GROUP BY base.devName, base.bugtype" ); 
		
		List<Object[]> lists = this.findBySql(sqlStr.toString());
		analysisDto.setAlsResult(lists);
	}
	//获取测试人员提交关闭bug统计02 
		public void getTesterBugStatClose(AnalysisDto analysisDto){
			String taskId = analysisDto.getTaskId(); 
			StringBuilder sqlStr = new StringBuilder();	
			sqlStr.append("SELECT count(*) AS currCount, devName,"+
	" bugtype FROM ( SELECT u. NAME AS devName, bugtp.enumname AS bugtype"+
	" FROM T_BUGBASEINFO b JOIN t_user u ON u.id = b.CURRENT_HANDLER"+
	" JOIN T_TYPEDEFINE bugtp ON b.buglevel = bugtp.enumid WHERE"+
	" b.TASK_ID = '"+taskId+"' AND ("+
	" b.CURRENT_STATE = 14 OR b.CURRENT_STATE = 15 OR b.CURRENT_STATE = 22"+
	" OR b.CURRENT_STATE = 23 )"+
	" UNION ALL SELECT us. NAME AS devName, '80' AS bugtype"+
	" FROM t_task_useactor ua JOIN t_user us ON us.id = ua.userid"+
	" WHERE ua.taskid = '"+taskId+"' AND ua.actor = 1"+
	" UNION ALL SELECT '80' AS devName, bugtp.enumname AS bugtype"+
	" FROM T_TYPEDEFINE bugtp WHERE bugtp.indentifier = 2"+
	" ) base GROUP BY base.devName, base.bugtype" ); 
			
			List<Object[]> lists = this.findBySql(sqlStr.toString());
			analysisDto.setAlsResult(lists);
		}
	
	
	//获取已关闭bug按周龄统计 
	public void getBugExistWeekStat(AnalysisDto analysisDto){
		String taskId = analysisDto.getTaskId(); 
		StringBuilder sqlStr = new StringBuilder();	
		sqlStr.append("SELECT base.age, base.BUGLEVEL, count(base.age) AS bugCount"+
			  " FROM ( SELECT baseInfo.age, ty.enumname AS BUGLEVEL"+
			  " FROM t_typedefine ty,"+
		" ( SELECT '1周' AS age, b.BUGLEVEL"+
					" FROM t_bugbaseinfo b"+
				" WHERE"+
				" b.task_id = '"+taskId+"'"+
							" AND b.current_state IN (14, 15, 22, 23)"+
				" AND DATEDIFF("+
						" date_format( b.CURRENT_HANDL_DATE, '%Y-%c-%d'),"+
					" date_format(b.BUGDISVDATE, '%Y-%c-%d')) <= 7"+
							" UNION ALL"+
				" ( SELECT '2周' AS age, b.BUGLEVEL"+
							" FROM t_bugbaseinfo b"+
						" WHERE b.task_id = '"+taskId+"'"+
								" AND b.current_state IN (14, 15, 22, 23)"+
						" AND DATEDIFF("+
								" date_format( b.CURRENT_HANDL_DATE,'%Y-%c-%d'),"+
							" date_format(b.BUGDISVDATE, '%Y-%c-%d')) >= 8"+
									" AND DATEDIFF("+
								" date_format(b.CURRENT_HANDL_DATE,'%Y-%c-%d'),"+
							" date_format(b.BUGDISVDATE, '%Y-%c-%d')) <= 14"+
							" ) UNION ALL("+
							" SELECT '3周' AS age, b.BUGLEVEL"+
						" FROM t_bugbaseinfo b"+
						" WHERE b.task_id = '"+taskId+"'"+
								" AND b.current_state IN (14, 15, 22, 23)"+
						" AND DATEDIFF("+
								" date_format(b.BUGDISVDATE, '%Y-%c-%d'),"+
							" date_format( b.CURRENT_HANDL_DATE,'%Y-%c-%d')) < 0"+
							" AND DATEDIFF(date_format(b.CURRENT_HANDL_DATE,'%Y-%c-%d'),"+
								" date_format(b.BUGDISVDATE, '%Y-%c-%d')) >= 15"+
									" AND DATEDIFF(date_format(b.CURRENT_HANDL_DATE,'%Y-%c-%d'"+
								" ),date_format(b.BUGDISVDATE, '%Y-%c-%d')) <= 21"+
							" )"+
				" UNION ALL(SELECT'4周' AS age,b.BUGLEVEL"+
						" FROM t_bugbaseinfo b"+
						" WHERE b.task_id = '"+taskId+"'"+
								" AND b.current_state IN (14, 15, 22, 23)"+
						" AND DATEDIFF(date_format(b.BUGDISVDATE, '%Y-%c-%d'),"+
								" date_format(b.CURRENT_HANDL_DATE,'%Y-%c-%d')"+
							" ) < 0 AND DATEDIFF("+
								" date_format(b.CURRENT_HANDL_DATE,'%Y-%c-%d'),"+
							" date_format(b.BUGDISVDATE, '%Y-%c-%d')) >= 22"+
									" AND DATEDIFF("+
								" date_format(b.CURRENT_HANDL_DATE,'%Y-%c-%d'),"+
							" date_format(b.BUGDISVDATE, '%Y-%c-%d')) <= 28"+
							" )UNION ALL("+
							" SELECT'4周以上' AS age,b.BUGLEVEL"+
						" FROM t_bugbaseinfo b"+
						" WHERE b.task_id = '"+taskId+"'"+
								" AND b.current_state IN (14, 15, 22, 23)"+
						" AND DATEDIFF(date_format(b.BUGDISVDATE, '%Y-%c-%d'),"+
								" date_format(b.CURRENT_HANDL_DATE,'%Y-%c-%d')) < 0"+
							" AND DATEDIFF("+
								" date_format(b.CURRENT_HANDL_DATE,'%Y-%c-%d'),"+
							" date_format(b.BUGDISVDATE, '%Y-%c-%d')) >= 29"+
							" )) baseInfo"+
					" WHERE ty.enumid = baseInfo.BUGLEVEL"+
				" AND ty.indentifier = 2"+
				" UNION ALL"+
		" (SELECT '1周' AS age, '80' AS BUGLEVEL"+
					" FROM DUAL )"+
		" UNION ALL"+
		" (SELECT '2周' AS age, '80' AS BUGLEVEL"+
					" FROM DUAL )"+
		" UNION ALL"+
		" (SELECT '3周' AS age, '80' AS BUGLEVEL"+
					" FROM DUAL )"+
		" UNION ALL"+
		" (SELECT '4周' AS age, '80' AS BUGLEVEL"+
					" FROM DUAL)"+
		" UNION ALL"+
		" (SELECT '4周以上' AS age, '80' AS BUGLEVEL"+
					" FROM DUAL)"+
	" ) base GROUP BY base.age, base.BUGLEVEL" ); 
		
		List<Object[]> lists = this.findBySql(sqlStr.toString());
		analysisDto.setAlsResult(lists);
	}
	//获取待处理bug按周龄统计01 
	public void getBugExistWeek4NoFixStat(AnalysisDto analysisDto){
		String taskId = analysisDto.getTaskId(); 
		StringBuilder sqlStr = new StringBuilder();	
		sqlStr.append("SELECT base.age, base.BUGLEVEL,"+
			" count(base.age) AS bugCount"+
	        " FROM ( SELECT baseInfo.age, ty.enumname AS BUGLEVEL FROM"+
			" t_typedefine ty, ( SELECT '1周' AS age, b.BUGLEVEL FROM t_bugbaseinfo b"+
			" WHERE b.task_id = '"+taskId+"'"+
			" AND b.current_state NOT IN (3, 4, 5, 14, 15, 22, 23)"+
			" AND DATEDIFF( date_format( b.CURRENT_HANDL_DATE,'%Y-%c-%d'),"+
			" date_format(b.BUGDISVDATE, '%Y-%c-%d')) <= 7"+
			" UNION ALL (SELECT '2周' AS age,b.BUGLEVEL FROM t_bugbaseinfo b"+
			" WHERE b.task_id = '"+taskId+"'"+
			" AND b.current_state NOT IN (3, 4, 5, 14, 15, 22, 23) AND DATEDIFF("+
			" date_format( b.CURRENT_HANDL_DATE, '%Y-%c-%d'),"+
			" date_format(b.BUGDISVDATE, '%Y-%c-%d')) >= 8"+
			" AND DATEDIFF( date_format( b.CURRENT_HANDL_DATE, '%Y-%c-%d'),"+
			" date_format(b.BUGDISVDATE, '%Y-%c-%d') ) <= 14)"+
			" UNION ALL(SELECT'3周' AS age,b.BUGLEVEL"+
			" FROM t_bugbaseinfo b WHERE b.task_id = '"+taskId+"'"+
			" AND b.current_state NOT IN (3, 4, 5, 14, 15, 22, 23)"+
			" AND DATEDIFF( date_format(b.BUGDISVDATE, '%Y-%c-%d'),"+
			" date_format( b.CURRENT_HANDL_DATE, '%Y-%c-%d' ) ) < 0"+
			" AND DATEDIFF( date_format( b.CURRENT_HANDL_DATE, '%Y-%c-%d' ),"+
			" date_format(b.BUGDISVDATE, '%Y-%c-%d') ) >= 15"+
			" AND DATEDIFF( date_format( b.CURRENT_HANDL_DATE, '%Y-%c-%d' ),"+
			" date_format(b.BUGDISVDATE, '%Y-%c-%d') ) <= 21 )"+
			" UNION ALL ( SELECT '4周' AS age, b.BUGLEVEL FROM t_bugbaseinfo b"+
			" WHERE b.task_id = '"+taskId+"'"+
			" AND b.current_state NOT IN (3, 4, 5, 14, 15, 22, 23)"+
			" AND DATEDIFF( date_format(b.BUGDISVDATE, '%Y-%c-%d'),"+
			" date_format( b.CURRENT_HANDL_DATE, '%Y-%c-%d' ) ) < 0"+
			" AND DATEDIFF( date_format( b.CURRENT_HANDL_DATE, '%Y-%c-%d' ),"+
			" date_format(b.BUGDISVDATE, '%Y-%c-%d') ) >= 22"+
			" AND DATEDIFF( date_format( b.CURRENT_HANDL_DATE, '%Y-%c-%d' ),"+
			" date_format(b.BUGDISVDATE, '%Y-%c-%d')"+
			" ) <= 28 ) UNION ALL"+
			" ( SELECT '4周以上' AS age, b.BUGLEVEL FROM t_bugbaseinfo b"+
			" WHERE b.task_id = '"+taskId+"'"+
			" AND b.current_state NOT IN (3, 4, 5, 14, 15, 22, 23)"+
			" AND DATEDIFF( date_format(b.BUGDISVDATE, '%Y-%c-%d'),"+
			" date_format( b.CURRENT_HANDL_DATE, '%Y-%c-%d' ) ) < 0"+
			" AND DATEDIFF( date_format( b.CURRENT_HANDL_DATE, '%Y-%c-%d' ),"+
			" date_format(b.BUGDISVDATE, '%Y-%c-%d') ) >= 29 ) ) baseInfo"+
			" WHERE ty.enumid = baseInfo.BUGLEVEL AND ty.indentifier = 2"+
			" UNION ALL ( SELECT '1周' AS age, '80' AS BUGLEVEL FROM DUAL )"+
			" UNION ALL ( SELECT '2周' AS age, '80' AS BUGLEVEL FROM DUAL )"+
			" UNION ALL ( SELECT '3周' AS age, '80' AS BUGLEVEL FROM DUAL )"+
			" UNION ALL ( SELECT '4周' AS age, '80' AS BUGLEVEL FROM DUAL )"+
			" UNION ALL ( SELECT '4周以上' AS age, '80' AS BUGLEVEL FROM DUAL ) ) base"+
			" GROUP BY base.age, base.BUGLEVEL" ); 
		
		List<Object[]> lists = this.findBySql(sqlStr.toString());
		analysisDto.setAlsResult(lists);
	}
	//获取待处理bug按周龄统计02 
		public void getBugExistWeek4NoFixStatAbsolute(AnalysisDto analysisDto){
			String taskId = analysisDto.getTaskId(); 
			StringBuilder sqlStr = new StringBuilder();	
			sqlStr.append("SELECT base.age, base.BUGLEVEL, count(base.age) AS bugCount FROM"+
	" ( SELECT baseInfo.age, ty.enumname AS BUGLEVEL FROM t_typedefine ty, ("+
	" SELECT '1周' AS age, b.BUGLEVEL FROM t_bugbaseinfo b"+
	" WHERE b.task_id = '"+taskId+"' AND b.current_state NOT IN (3, 4, 5, 14, 15, 22, 23)"+
	" AND DATEDIFF( date_format(curdate(), '%Y-%c-%d'), date_format(b.BUGDISVDATE, '%Y-%c-%d')"+
	" ) <= 7 UNION ALL ( SELECT '2周' AS age, b.BUGLEVEL FROM t_bugbaseinfo b"+
	" WHERE b.task_id = '"+taskId+"' AND b.current_state NOT IN (3, 4, 5, 14, 15, 22, 23)"+
	" AND DATEDIFF( date_format(curdate(), '%Y-%c-%d'), date_format(b.BUGDISVDATE, '%Y-%c-%d')"+
	" ) >= 8 AND DATEDIFF( date_format(curdate(), '%Y-%c-%d'), date_format(b.BUGDISVDATE, '%Y-%c-%d')"+
	" ) <= 14 ) UNION ALL ( SELECT '3周' AS age, b.BUGLEVEL FROM t_bugbaseinfo b"+
	" WHERE b.task_id = '"+taskId+"' AND b.current_state NOT IN (3, 4, 5, 14, 15, 22, 23)"+
	" AND DATEDIFF( date_format(curdate(), '%Y-%c-%d'), date_format(b.BUGDISVDATE, '%Y-%c-%d')"+
	" ) >= 15 AND DATEDIFF( date_format(curdate(), '%Y-%c-%d'), date_format(b.BUGDISVDATE, '%Y-%c-%d')"+
	" ) <= 21 ) UNION ALL ( SELECT '4周' AS age, b.BUGLEVEL FROM t_bugbaseinfo b"+
	" WHERE b.task_id = '"+taskId+"' AND b.current_state NOT IN (3, 4, 5, 14, 15, 22, 23)"+
	" AND DATEDIFF( date_format(curdate(), '%Y-%c-%d'), date_format(b.BUGDISVDATE, '%Y-%c-%d')"+
	" ) >= 22 AND DATEDIFF( date_format(curdate(), '%Y-%c-%d'), date_format(b.BUGDISVDATE, '%Y-%c-%d')"+
	" ) <= 28 ) UNION ALL ( SELECT '4周以上' AS age, b.BUGLEVEL FROM t_bugbaseinfo b"+
	" WHERE b.task_id = '"+taskId+"' AND b.current_state NOT IN (3, 4, 5, 14, 15, 22, 23)"+
	" AND DATEDIFF( date_format(curdate(), '%Y-%c-%d'), date_format(b.BUGDISVDATE, '%Y-%c-%d')"+
	" ) >= 29 ) ) baseInfo WHERE ty.enumid = baseInfo.BUGLEVEL AND ty.indentifier = 2"+
	" UNION ALL ( SELECT '1周' AS age, '80' AS BUGLEVEL FROM DUAL )"+
	" UNION ALL ( SELECT '2周' AS age, '80' AS BUGLEVEL FROM DUAL )"+
	" UNION ALL ( SELECT '3周' AS age, '80' AS BUGLEVEL FROM DUAL )"+
	" UNION ALL ( SELECT '4周' AS age, '80' AS BUGLEVEL FROM DUAL )"+
	" UNION ALL ( SELECT '4周以上' AS age, '80' AS BUGLEVEL FROM DUAL ) ) base"+
	" GROUP BY base.age, base.BUGLEVEL" ); 
			
			List<Object[]> lists = this.findBySql(sqlStr.toString());
			analysisDto.setAlsResult(lists);
		}
	//获取bug等级统计 
	public void getBugGradeStat(AnalysisDto analysisDto){
		String taskId = analysisDto.getTaskId(); 
		StringBuilder sqlStr = new StringBuilder();	
		sqlStr.append("SELECT baseInfo.enumname, sum(baseInfo.bugCount) AS bugCount"+
				" FROM ( SELECT bugBase.bugCount, ty.enumname"+
				" FROM t_typedefine ty"+
				" JOIN ( SELECT t.BUGLEVEL, count(t.BUGLEVEL) bugCount"+
				" FROM t_bugbaseinfo t"+
				" GROUP BY t.task_id, t.current_state, t.BUGLEVEL "+
				" HAVING t.task_id = '"+taskId+"'"+
				" AND t.current_state > 6"+
				" AND t.current_state != 16"+
				" AND t.current_state != 22"+
				" ) bugBase ON bugBase.BUGLEVEL = ty.enumid"+
				" UNION ALL"+
				" SELECT 0, tp.enumname"+
				" FROM t_typedefine tp"+
				" WHERE tp.indentifier = 2 ) baseInfo"+
				" GROUP BY baseInfo.enumname" ); 
		
		List<Object[]> lists = this.findBySql(sqlStr.toString());
		analysisDto.setAlsResult(lists);
	}
	//获取遗留bug分析 
	public void getBugBequeathStat(AnalysisDto analysisDto){
		String taskId = analysisDto.getTaskId(); 
		StringBuilder sqlStr = new StringBuilder();	
		sqlStr.append("SELECT baseInfo.enumname, sum(baseInfo.bugCount) AS bugCount"+
				" FROM ( SELECT bugBase.bugCount, ty.enumname"+
				" FROM t_typedefine ty"+
				" JOIN ( SELECT t.bugtype, count(t.bugtype) bugCount"+
				" FROM t_bugbaseinfo t"+
				" GROUP BY t.task_id, t.current_state, t.bugtype"+
				" HAVING t.task_id = '"+taskId+"'"+
				" AND t.current_state = 23"+
				" ) bugBase ON bugBase.bugtype = ty.enumid"+
				" UNION ALL"+
				" SELECT 0, tp.enumname"+
				" FROM t_typedefine tp"+
				" WHERE tp.indentifier = 0 ) baseInfo"+
				" GROUP BY baseInfo.enumname" ); 
		
		List<Object[]> lists = this.findBySql(sqlStr.toString());
		analysisDto.setAlsResult(lists);
	}
	//获取bug密度分析 
	public void getBugDensityStat(AnalysisDto analysisDto){
		String taskId = analysisDto.getTaskId(); 
		StringBuilder sqlStr = new StringBuilder();	
		sqlStr.append("SELECT outDate.MODULENAME, outDate.KLC,"+
	" count(outDate.bugcardid) AS bugCount FROM ( SELECT"+
	" outInfo.MODULENUM, outInfo.KLC, outInfo.MODULENAME, b.bugcardid"+
	" FROM ( SELECT OUTL.MODULENAME, ifnull(OUTL.KLC, 0) AS KLC, OUTL.MODULENUM"+
	" FROM T_OUTLINEINFO OUTL WHERE OUTL.TASKID ='"+taskId+"'"+
	" AND OUTL.MODULELEVEL = 2 ) outInfo"+
	" LEFT JOIN t_BUGBASEINFO b ON b.modulenum LIKE cONCAT(outInfo.MODULENUM, '%')"+
	" AND b.TASK_ID ='"+taskId+"' AND b.current_state > 6 AND b.current_state != 22"+
	" ) outDate GROUP BY outDate.MODULENAME " ); 
		
		List<Object[]> lists = this.findBySql(sqlStr.toString());
		analysisDto.setAlsResult(lists);
	}
	//获取bug密度分析02 
		public void getBugDensityStatType(AnalysisDto analysisDto){
			String taskId = analysisDto.getTaskId(); 
			StringBuilder sqlStr = new StringBuilder();	
			sqlStr.append("SELECT outDate.MODULENAME, outDate.ENUMNAME, count(outDate.ENUMNAME) AS bugcount"+
					" FROM ( SELECT outInfo.MODULENAME, b.buglevel, t.ENUMNAME"+
					" FROM ( SELECT OUTL.MODULENAME, OUTL.MODULENUM"+
					" FROM T_OUTLINEINFO OUTL WHERE OUTL.TASKID ='"+taskId+"'"+
					" AND OUTL.MODULELEVEL = 2 ) outInfo"+
					" LEFT JOIN t_BUGBASEINFO b ON b.modulenum LIKE cONCAT(outInfo.MODULENUM, '%')"+
					" JOIN t_typedefine t ON b.BUGLEVEL = t.ENUMID AND t.INDENTIFIER = 2"+
					" AND b.TASK_ID ='"+taskId+"' AND b.current_state > 6"+
					" AND b.current_state != 22 ) outDate GROUP BY"+
					" outDate.MODULENAME, outDate.buglevel" ); 
			
			List<Object[]> lists = this.findBySql(sqlStr.toString());
			analysisDto.setAlsResult(lists);
		}
	//获取bug密度分析03 
		public void getBugDensityStatBugType(AnalysisDto analysisDto){
			String taskId = analysisDto.getTaskId(); 
			StringBuilder sqlStr = new StringBuilder();	
			sqlStr.append(" SELECT outDate.MODULENAME, outDate.ENUMNAME, count(outDate.ENUMNAME) AS bugcount"+
					" FROM ( SELECT outInfo.MODULENAME, b.BUGTYPE, t.ENUMNAME"+
					" FROM ( SELECT OUTL.MODULENAME, OUTL.MODULENUM FROM T_OUTLINEINFO OUTL"+
					" WHERE OUTL.TASKID ='"+taskId+"' AND OUTL.MODULELEVEL = 2"+
					" ) outInfo LEFT JOIN t_BUGBASEINFO b ON b.modulenum LIKE cONCAT(outInfo.MODULENUM, '%')"+
					" JOIN t_typedefine t ON b.BUGTYPE = t.ENUMID AND t.INDENTIFIER = 0"+
					" AND b.TASK_ID ='"+taskId+"' AND b.current_state > 6"+
					" AND b.current_state != 22 ) outDate GROUP BY"+
					" outDate.MODULENAME, outDate.BUGTYPE" ); 
			
			List<Object[]> lists = this.findBySql(sqlStr.toString());
			analysisDto.setAlsResult(lists);
		}
		
		public void getBugModuleDistbuStat(AnalysisDto analysisDto){
			String taskId = analysisDto.getTaskId();
			String moduleIds = analysisDto.getModuleIds();
			StringBuilder sqlStr = new StringBuilder();	
			sqlStr.append("select  outData.name , count(outData.bugcardid  ) as bugCount from " + 
                          "( select outlInfo.name,b.bugcardid  from (SELECT OUTL.MODULEID, " + 
                          "CONCAT(oa.modulename , '/', OUTL.MODULENAME) as name, OUTL.MODULENUM " + 
                          "FROM T_OUTLINEINFO OUTL inner join T_OUTLINEINFO oa on OUTL.Supermoduleid = oa.moduleid " + 
                          "WHERE OUTL.Moduleid in (" + moduleIds + ") and OUTL.TASKID = '"+taskId+"' ) outlInfo " + 
                          "left join  t_BUGBASEINFO b on b.modulenum  like  CONCAT(outlInfo.MODULENUM , '%') " + 
                          "and  b.current_state not in (2, 3, 4, 5, 22, 16) and b.TASK_ID = '"+taskId+"'  " + 
                          ") outData  group by outData.name order by outData.name");
			List<Object[]> lists = this.findBySql(sqlStr.toString());
			analysisDto.setAlsResult(lists);
		}
		
		//测试需求项BUG分布明细--BUG数
		public void getBugModuleDistbuForNum(AnalysisDto analysisDto){
			String taskId = analysisDto.getTaskId();
			String moduleIds = analysisDto.getModuleIds();
			StringBuilder sqlStr = new StringBuilder();	
			sqlStr.append("select  outData.name , count(outData.bugcardid  ) as bugCount from " + 
                          "( select outlInfo.name,b.bugcardid  from (SELECT OUTL.MODULEID, " + 
                          "CONCAT(oa.modulename , '/', OUTL.MODULENAME) as name, OUTL.MODULENUM " + 
                          "FROM T_OUTLINEINFO OUTL inner join T_OUTLINEINFO oa on OUTL.Supermoduleid = oa.moduleid " + 
                          "WHERE OUTL.Moduleid in (" + moduleIds + ") and OUTL.TASKID = '"+taskId+"' ) outlInfo " + 
                          "left join  t_BUGBASEINFO b on b.modulenum  like  CONCAT(outlInfo.MODULENUM , '%') " + 
                          "and  b.current_state not in (2, 3, 4, 5, 22, 16) and b.TASK_ID = '"+taskId+"'  " + 
                          ") outData  group by outData.name order by outData.name");
			List<Object[]> lists = this.findBySql(sqlStr.toString());
			analysisDto.setAlsResult(lists);
		}
		
		//测试需求项BUG分布明细--BUG等级
		public void getBugModuleDistbuForLevel(AnalysisDto analysisDto){
			String taskId = analysisDto.getTaskId();
			String moduleIds = analysisDto.getModuleIds();
			StringBuilder sqlStr = new StringBuilder();	
			sqlStr.append("select outDate.MODULENAME,outDate.ENUMNAME, count(outDate.ENUMNAME) as bugcount from ( " + 
                          "select outInfo.MODULENAME ,b.buglevel ,t.ENUMNAME from " + 
                          "(select OUTL.MODULENAME,OUTL.MODULENUM FROM T_OUTLINEINFO OUTL " + 
                          "where  OUTL.TASKID ='"+taskId+"'  AND OUTL.Moduleid in (" + moduleIds + ")) outInfo " + 
                          "left join  t_BUGBASEINFO b on b.modulenum like cONCAT(outInfo.MODULENUM,'%') " + 
                          "join t_typedefine t on b.BUGLEVEL = t.ENUMID  and t.INDENTIFIER = 2 " + 
                          "and b.TASK_ID='"+taskId+"' and b.current_state > 6  and  b.current_state != 22 " + 
                          ") outDate group by  outDate.MODULENAME ,outDate.buglevel");
			
			List<Object[]> lists = this.findBySql(sqlStr.toString());
			analysisDto.setAlsResult(lists);
		}
		
		//测试需求项BUG分布明细--BUG类型
		public void getBugModuleDistbuForType(AnalysisDto analysisDto){
			String taskId = analysisDto.getTaskId();
			String moduleIds = analysisDto.getModuleIds();
			StringBuilder sqlStr = new StringBuilder();	
			sqlStr.append("select outDate.MODULENAME,outDate.ENUMNAME, count(outDate.ENUMNAME) as bugcount from ( " + 
                          "select outInfo.MODULENAME ,b.BUGTYPE ,t.ENUMNAME from " + 
                          "(select OUTL.MODULENAME,OUTL.MODULENUM FROM T_OUTLINEINFO OUTL " + 
                          "where  OUTL.TASKID ='"+taskId+"'   AND OUTL.Moduleid in (" + moduleIds + ")) outInfo " + 
                          "left join  t_BUGBASEINFO b on b.modulenum like cONCAT(outInfo.MODULENUM,'%') " + 
                          "join t_typedefine t on b.BUGTYPE = t.ENUMID  and t.INDENTIFIER = 0 " + 
                          "and b.TASK_ID='"+taskId+"' and b.current_state > 6  and  b.current_state != 22 " + 
                          ") outDate group by  outDate.MODULENAME ,outDate.BUGTYPE");
			
			List<Object[]> lists = this.findBySql(sqlStr.toString());
			analysisDto.setAlsResult(lists);
		}
		
		//责任人分析
		public void getChargeOwner(AnalysisDto analysisDto){
			String taskId = analysisDto.getTaskId(); 
			String startDate = analysisDto.getStartDate();
			String endDate = analysisDto.getEndDate();
			StringBuilder sqlStr = new StringBuilder();	
			sqlStr.append("select ifnull(t.name,'未指定') as NAME, count(*) as bugCount " +
                         "from t_bugbaseinfo  u left join t_user t  on u.CHARGE_OWNER=t.ID " +
                         "where date_format(u.BUGDISVDATE, '%Y-%c-%d') >= date('" + startDate + "') and date_format(u.BUGDISVDATE, '%Y-%c-%d') <= date('" + endDate + "') " +
                         "and   u.CURRENT_STATE not in (2,3,4,5,22,16) and u.TASK_ID='" + taskId  + "' group by u.CHARGE_OWNER");
			
			List<Object[]> lists = this.findBySql(sqlStr.toString());
			analysisDto.setAlsResult(lists);
		}
		
		//责任人引入原因分析
	    public void getImportCaseByProject(AnalysisDto analysisDto){
	    	String taskId = analysisDto.getTaskId(); 
	    	String startDate = analysisDto.getStartDate();
			String endDate = analysisDto.getEndDate();
			StringBuilder sqlStr = new StringBuilder();	
			sqlStr.append("select  IFNULL(u.name ,'暂未指定')as NAME, IFNULL(bugtp.enumname,'暂未指定') as BUGTYPE,count(*) as bugCount " + 
                          "from T_BUGBASEINFO b left join t_user u on u.id = b.charge_owner " + 
                          "left join T_TYPEDEFINE bugtp on b.GENERATEPHASE = bugtp.enumid where " + 
                          "b.CURRENT_STATE not in (2,3,4,5,22,16) and b.task_id='"+taskId+"' " + 
                          "and date_format(b.BUGDISVDATE, '%Y-%c-%d') >= date('" + startDate + "') and date_format(b.BUGDISVDATE, '%Y-%c-%d') <= date('" + endDate + "') " + 
                          "group by   u.name ,bugtp.enumname");
			
			List<Object[]> lists = this.findBySql(sqlStr.toString());
			analysisDto.setAlsResult(lists);
		}
	    
	  //简要统计--截止到时间末BUG状态分布情况
       public void getBeforeOpenBugSummary(AnalysisDto analysisDto){
    		String taskId = analysisDto.getTaskId(); 
			StringBuilder sqlStr = new StringBuilder();	
		
			sqlStr.append("select b.current_state, count(*) as bug_count " + 
                    "from t_BUGBASEINFO b where b.TASK_ID='"+taskId+"'  " + 
                    "group by b.current_state order by b.current_state");
		
			List<Object[]> lists = this.findBySql(sqlStr.toString());
			for(Integer i=0;i<lists.size();i++){
				Integer bugstatus = (Integer)lists.get(i)[0];
				String stateName = BugFlowConst.getStateName(bugstatus);
				lists.get(i)[0] = stateName;
			}
			
			analysisDto.setAlsResult(lists);
		}
       
       //简要统计--时间段内新增、修改和关闭BUG概况
       public void getNewFixCloseBugSummary(AnalysisDto analysisDto){
    		String taskId = analysisDto.getTaskId(); 
    		String startDate = analysisDto.getStartDate();
    		String endDate = analysisDto.getEndDate();
			StringBuilder sqlStr = new StringBuilder();	
			sqlStr.append("SELECT count(bugcardid) as fiX_count ,0 as close_count ,0 as new_conut  ,1 as typeCount " +
                    "FROM t_bughandhistory t  where  TASK_ID='"+taskId+"' and t.bugstate =13 " + 
                    "and date_format(t.INSDATE,'%Y-%c-%d') >= date('" + startDate + "') and date_format(t.INSDATE,'%Y-%c-%d') <= date('" + endDate + "') " +  
                    "union all " + 
                    "SELECT 0 as fiX_count ,count(bugstate) as close_count,0 new_conut  ,2 as typeCount " + 
                    "FROM t_bughandhistory t  where  TASK_ID='"+taskId+"' and t.bugstate in (14,15,22,23) " + 
                    "and date_format(t.INSDATE,'%Y-%c-%d') >= date('" + startDate + "') and date_format(t.INSDATE,'%Y-%c-%d') <= date('" + endDate + "') " + 
                    "union all SELECT 0 as fiX_count,0 as close_count,count(bugcardid) as new_conut  ,3 as typeCount " + 
                    "FROM t_bugbaseinfo t where TASK_ID='"+taskId+"' and " + 
                    "date_format(t.bugdisvdate,'%Y-%c-%d') >= date_format('" + startDate + "','%Y-%c-%d') and date_format(t.bugdisvdate,'%Y-%c-%d') <= date('" + endDate + "')");
			List<Object[]> lists = this.findBySql(sqlStr.toString());
			analysisDto.setAlsResult(lists);
		}
}