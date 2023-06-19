package cn.com.codes.bugManager.blh;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import com.opensymphony.webwork.ServletActionContext;

import cn.com.codes.bugManager.dto.BoardVo;
import cn.com.codes.bugManager.dto.BugAddinitDto;
import cn.com.codes.bugManager.dto.BugManagerDto;
import cn.com.codes.bugManager.service.BugCommonService;
import cn.com.codes.bugManager.service.BugFlowControlService;
import cn.com.codes.bugManager.service.BugManagerService;
import cn.com.codes.caseManager.dto.TreeJsonVo;
import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.dto.PageModel;
import cn.com.codes.common.service.DrawHtmlListDateService;
import cn.com.codes.common.util.FileInfoVo;
import cn.com.codes.common.util.StringUtils;
import cn.com.codes.fileInfo.dto.FileInfoDto;
import cn.com.codes.fileInfo.service.FileInfoService;
import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.common.HtmlListComponent;
import cn.com.codes.framework.common.JsonUtil;
import cn.com.codes.framework.common.ListObject;
import cn.com.codes.framework.exception.DataBaseException;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.impExpManager.service.ImpExpManagerService;
import cn.com.codes.object.BugBaseInfo;
import cn.com.codes.object.BugFreq;
import cn.com.codes.object.BugGrade;
import cn.com.codes.object.BugHandHistory;
import cn.com.codes.object.BugOpotunity;
import cn.com.codes.object.BugPri;
import cn.com.codes.object.BugQueryInfo;
import cn.com.codes.object.BugType;
import cn.com.codes.object.DefaultTypeDefine;
import cn.com.codes.object.FileInfo;
import cn.com.codes.object.OutlineInfo;
import cn.com.codes.object.OutlineTeamMember;
import cn.com.codes.object.TaskUseActor;
import cn.com.codes.object.TestFlowInfo;
import cn.com.codes.object.TestTaskDetail;
import cn.com.codes.object.TypeDefine;
import cn.com.codes.object.User;
import cn.com.codes.outlineManager.service.OutLineManagerService;
import cn.com.codes.testTaskManager.blh.TestTaskDetailBlh;
import cn.com.codes.testTaskManager.dto.CurrTaskInfo;
import cn.com.codes.testTaskManager.service.TestTaskDetailService;
import cn.com.codes.bugManager.blh.BugFlowConst;
import cn.com.codes.bugManager.blh.BugFlowControlBlh;
import cn.com.codes.bugManager.blh.BugManagerBlh;


public class BugManagerBlh extends BusinessBlh {

	private static Logger logger = Logger.getLogger(BugManagerBlh.class);
	private BugManagerService bugManagerService ;
	private OutLineManagerService outLineService;
	private TestTaskDetailService testTaskService ;
	private BugCommonService bugCommonService;
	private BugFlowControlService bugFlowControlService ;
	private DrawHtmlListDateService drawHtmlListDateService;
	private BugFlowControlBlh bugFlowControlBlh;
	private ImpExpManagerService impExpManagerService;
	private FileInfoService fileInfoService;
	
	public View index(BusiRequestEvent req){
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		String taskId = dto.getTaskId();
		//System.out.println("case ==========index");
		if(taskId!=null){  
			StringBuffer hql = new StringBuffer("select new TestTaskDetail(outlineState,testPhase,")
			.append("currentVersion,testSeq,reltCaseFlag) from TestTaskDetail where taskId=? and companyId=?");
			List<TestTaskDetail> taskList =testTaskService.findByHql(hql.toString(), dto.getTaskId(),SecurityContextHolderHelp.getCompanyId());
			if(taskList==null||taskList.isEmpty()){
				throw new DataBaseException("非法提交的测试任务数据，不受理");
			}
			SecurityContextHolderHelp.setCurrTaksId(taskId);
			dto.setTaskId(taskId);
		}
		return super.getView();
	}
	
	private boolean isFindMyBug(){
		
		Cookie[] cookies = SecurityContextHolder.getContext().getRequest().getCookies();
		if (cookies != null) {
			Cookie cookie = null;
			for (int i = 0; i < cookies.length; i++) {
				cookie = cookies[i];
				if (cookie.getName().equalsIgnoreCase("queryMyBug")&&"1".equals(cookie.getValue())) {
					return true;
				}
			}
		}
		return false;
	}
	public View loadMyBugWithModule(BusiRequestEvent req) {
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		//作下面的检查，是为了防止前端篡改taskId
		if(dto.getTaskId()!=null&&!"".equals(dto.getTaskId().trim())){
			StringBuffer hql = new StringBuffer("select new TestTaskDetail(outlineState,testPhase,")
			.append("currentVersion,testSeq,reltCaseFlag) from TestTaskDetail where taskId=? and companyId=?");
			List<TestTaskDetail> taskList =testTaskService.findByHql(hql.toString(), dto.getTaskId(),SecurityContextHolderHelp.getCompanyId());
			if(taskList==null||taskList.size()==0){
				if(dto.getIsAjax()!=null){
					super.writeResult("1/10/0$");
					return super.globalAjax();
				}
				dto.setOutLineState(0);
				dto.setListStr("1/10/0$");
				return super.getView();
			}
			SecurityContextHolder.getContext().getVisit().setTaskId(dto.getTaskId());
		}else if(SecurityContextHolderHelp.getCurrTaksId()==null&&dto.getTaskId()==null){
			if(dto.getIsAjax()!=null){
				super.writeResult("1/10/0$");
				return super.globalAjax();
			}
			dto.setOutLineState(0);
			dto.setListStr("1/10/0$");
			return super.getView();
		}
		CurrTaskInfo currTaskInfo = testTaskService.getCurrTaskInfo();
		dto.setTestFlow(currTaskInfo.getTestFlow());
		dto.setRoleInTask(currTaskInfo.getRoleInTask());
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		dto.setTaskId(taskId);
		dto.setRelCaseSwitch(currTaskInfo.getRelCaseSwitch());
		dto.setStateList(BugFlowConst.getStateList());
		if(isFindMyBug())
			dto.setDefBug(1);

		
		dto.setOutLineState(currTaskInfo.getOutLineState());
		List<BugBaseInfo> bugs = bugCommonService.findBug(dto);
		List<TypeDefine> typeList = dto.getTypeList();
		Map<String ,TypeDefine >  tdMap =  null;
		if(bugs!=null&&bugs.size()>0){
			if(typeList!=null){
				tdMap = bugCommonService.convertTdMap(dto);
				bugCommonService.setBugsRelaTypeDefine(tdMap, bugs);
			}else{
				this.setRelaTypeDefine(bugs);
			}
			this.setRelaUser(bugs);
			this.setRelaTaskName(bugs);
			this.setStateName(bugs);
		}
		tdMap = null;
		dto.setTypeList(null);
		typeList=null;
		StringBuffer sb = new StringBuffer();
		dto.toJson2((List)bugs, sb);
		if("true".equalsIgnoreCase(dto.getIsAjax())){
			super.writeResult(sb.toString());
			return super.globalAjax();
		}
		dto.setCountStr((impExpManagerService.getBugCountStr(taskId) +" (状态为：撤销,无效 ,重复 ,非错 ,关闭/撤销 ,修正/描述不当的BUG不算有效BUG)"));
		dto.setListStr(sb.toString());
		return super.getView();
	}
		
	public View loadLeftTree(BusiRequestEvent req) {
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
//		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		String id = ServletActionContext.getRequest().getParameter("id");
		if(!StringUtils.isNullOrEmpty(id)){
			dto.setCurrNodeId(Long.valueOf(id));
		}
//		String taskId = SecurityContextHolderHelp.getCurrTaksId();
//		dto.setTaskId(taskId);
		String taskId = dto.getTaskId();
		if(taskId==null || taskId==""){
			if(dto.getIsAjax() != null){
				super.writeResult("0,1,无数据,0.1");
				dto = null;
				return super.globalAjax();
			}
			SecurityContextHolder.getContext().setAttr("nodeDataStr", "0,1,无数据,0.1");
			return super.getView();
		}
//		String nodeDataStr = this.toTreeStr(outLineService.loadNormalNode(taskId, dto.getCurrNodeId()));
//		if (dto.getIsAjax() == null) {
//			SecurityContextHolder.getContext().setAttr("nodeDataStr", nodeDataStr);
//			return super.getView();
//		}
//		super.writeResult(nodeDataStr);
//		dto = null;
//		return super.globalAjax();
//		String nodeDataStr = this.toTreeStr(outLineService.loadNormalNode(taskId, dto.getCurrNodeId()));
		List<TreeJsonVo> treeJsonVos = this.toTreeJson(outLineService.loadNormalNode(taskId, dto.getCurrNodeId()),dto.getCurrNodeId());
		if (dto.getIsAjax() == null) {
//			SecurityContextHolder.getContext().setAttr("nodeDataStr", nodeDataStr);
			return super.getView();
		}
//		super.writeResult(nodeDataStr);
		super.writeResult(JsonUtil.toJson(treeJsonVos));
//		dto = null;
		return super.globalAjax();
	}	
	public View getBugStatInfo(BusiRequestEvent req){
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		Long currNodeId = dto.getCurrNodeId();
		//System.out.println("moduleId=" +currNodeId);
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		if(currNodeId==null){
			super.writeResult(impExpManagerService.getBugCountStr(taskId) +" (状态为：撤销,无效 ,重复 ,非错 ,关闭/撤销 ,修正/描述不当的BUG不算有效BUG)");
			return super.globalAjax();
		}
		String hql = "select new OutlineInfo(moduleNum,moduleId) from OutlineInfo where moduleId=? and taskId=?";
		
		List<OutlineInfo> list =impExpManagerService.findByHql(hql, currNodeId,taskId);
		if(list==null||list.isEmpty()){
			super.writeResult("测试需求不存在");
			return super.globalAjax();
		}
		OutlineInfo outLine = list.get(0);
		if(outLine.getModuleNum()!=null){
			super.writeResult(impExpManagerService.getBugCountStr(taskId,outLine.getModuleNum()) +" (状态为：撤销,无效 ,重复 ,非错 ,关闭/撤销 ,修正/描述不当的BUG不算有效BUG)");
		}else{
			super.writeResult(impExpManagerService.getBugCountStr(taskId) +" (状态为：撤销,无效 ,重复 ,非错 ,关闭/撤销 ,修正/描述不当的BUG不算有效BUG)");
		}
		return super.globalAjax();
	}
	public View getAllBugStatInfo(BusiRequestEvent req){
		
		super.writeResult(impExpManagerService.getBugCountStr() +" (状态为：撤销,无效 ,重复 ,非错 ,关闭/撤销 ,修正/描述不当的BUG不算有效BUG)");
		return super.globalAjax();
	}
	public View loadOwnerBug(BusiRequestEvent req){
		
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		String selUser = "select new cn.com.codes.object.User(id,name,loginName) from cn.com.codes.object.User where loginName=? and delFlag=0";
		List userList = bugCommonService.findByHql(selUser, dto.getCurrOwner());
		if(userList==null||userList.isEmpty()){
			throw new DataBaseException("该帐户己被删除");
		}
		BugBaseInfo bug = new BugBaseInfo();
		dto.setModuleName(((User)userList.get(0)).getUniqueName());
		if(userList==null||userList.isEmpty()){
			bug.setNextOwnerId("mypm");
		}else{
			bug.setNextOwnerId(((User)userList.get(0)).getId());	
			//如是本人设置本人的BUG
			//if(bug.getNextOwnerId().equals(SecurityContextHolderHelp.getUserId())){
			//	dto.setDefBug(1);
			//}
			
		}
		dto.setBug(bug);
		
		if(dto.getLoadType()==1){//特定项目我的BUG
			CurrTaskInfo currTaskInfo = testTaskService.getCurrTaskInfo();
			dto.setTestFlow(currTaskInfo.getTestFlow());
			dto.setRoleInTask(currTaskInfo.getRoleInTask());
			String taskId = SecurityContextHolderHelp.getCurrTaksId();
			dto.setTaskId(taskId);
			dto.setRelCaseSwitch(currTaskInfo.getRelCaseSwitch());
			dto.setStateList(BugFlowConst.getStateList());
			dto.setDefBug(0);
			dto.setOutLineState(currTaskInfo.getOutLineState());
			List<BugBaseInfo> bugs = bugCommonService.findBug(dto);
			List<TypeDefine> typeList = dto.getTypeList();
			Map<String ,TypeDefine >  tdMap =  null;
			if(bugs!=null&&bugs.size()>0){
				if(typeList!=null){
					tdMap = bugCommonService.convertTdMap(dto);
					bugCommonService.setBugsRelaTypeDefine(tdMap, bugs);
				}else{
					this.setRelaTypeDefine(bugs);
				}
				this.setRelaUser(bugs);
				this.setRelaTaskName(bugs);
			}
			tdMap = null;
			dto.setTypeList(null);
			typeList=null;
			StringBuffer sb = new StringBuffer();
			dto.toJson2((List)bugs, sb);
			dto.setCountStr((impExpManagerService.getBugCountStr(taskId) +" (状态为：撤销,无效 ,重复 ,非错 ,关闭/撤销 ,修正/描述不当的BUG不算有效BUG)"));
			dto.setListStr(sb.toString());
			return super.getView("loadMyBug");
		}else{
			dto.setAppScope(0);
			dto.setStateList(BugFlowConst.getStateList());
			List<BugBaseInfo> bugs = bugCommonService.findAllMyBug(dto);
			List<TypeDefine> typeList = dto.getTypeList();
			Map<String ,TypeDefine >  tdMap =  null;
			if(bugs!=null&&bugs.size()>0){
				if(typeList!=null){
					tdMap = bugCommonService.convertTdMap(dto);
					bugCommonService.setBugsRelaTypeDefine(tdMap, bugs);
				}else{
					this.setRelaTypeDefine(bugs);
				}
				this.setRelaUser(bugs);
				this.setRelaTaskName(bugs);
			}
			StringBuffer sb = new StringBuffer();
			dto.toJson2((List)bugs, sb);
			dto.setCountStr((impExpManagerService.getBugCountStr() +" (状态为：撤销,无效 ,重复 ,非错 ,关闭/撤销 ,修正/描述不当的BUG不算有效BUG)"));
			dto.setListStr(sb.toString());
			return super.getView("loadAllMyBug");
		}
	}
		
	public View loadBugBoard(BusiRequestEvent req){
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		int loadType = dto.getLoadType();
		List<Object[]> countList = null;
		if(loadType==1){
			countList = bugCommonService.loadBugBoard(SecurityContextHolderHelp.getCurrTaksId());
		}else{
			countList = bugCommonService.loadBugBoard(null);
		}
		if(countList==null||countList.isEmpty()){
//			dto.setListStr("");
			PageModel pg = new PageModel();
			pg.setTotal(0);
			pg.setRows(new ArrayList<BoardVo>());
			super.writeResult(JsonUtil.toJson(pg));
		}else{
			List list = new ArrayList(countList.size());
			for(Object[] objs :countList){
				BoardVo vo = new BoardVo();
				vo.setUserName(objs[0].toString());
				vo.setWhCount(objs[1]==null?0:Integer.parseInt(objs[1].toString()));
				vo.setHCount(objs[2]==null?0:Integer.parseInt(objs[2].toString()));
				vo.setLoginName(objs[3].toString());
				list.add(vo);
			}
//			StringBuffer sb = new StringBuffer();
//			dto.toJson2(list, sb);
//			dto.setListStr(sb.toString());
			PageModel pg = new PageModel();
			pg.setTotal(list.size());
			pg.setRows(list);
			super.writeResult(JsonUtil.toJson(pg));
		}
		return super.globalAjax();
//		return super.getView();
	}
	public View isAssigner(BusiRequestEvent req){
		
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		CurrTaskInfo currTaskInfo = this.getCurrTaskInfo(dto.getTaskId());
		if(currTaskInfo.getRoleInTask()!=null&&"".equals(currTaskInfo.getRoleInTask())
				&&currTaskInfo.getRoleInTask().indexOf("4")>=0){
			super.writeResult("true");
		}else{
			super.writeResult("false");
		}
		return super.globalAjax();
	}
	
	public View batchAssFindByquery(BusiRequestEvent req){
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		String hql = "from BugQueryInfo where queryId=? and ownerId=?";
		String myUserId = SecurityContextHolderHelp.getUserId();
		Long queryId = dto.getQueryId();
		List queryList = bugCommonService.findByHql(hql, queryId,myUserId);
		if(queryList==null||queryList.size()==0){
			super.writeResult("deleted");
			return super.globalAjax();
		}
		BugQueryInfo queryInfo = (BugQueryInfo)queryList.get(0);
		queryInfo.praValueStr2Map();
		StringBuffer qhql = new StringBuffer(queryInfo.getHqlCondiStr());
		if(qhql.indexOf("b.taskId=:taskId")<0){
			qhql.append(" and b.taskId=:taskId");
		}
//		if(qhql.indexOf("b.currStateId=:currStateId")>=0){
//			String hqlTemp = qhql.toString();
//			hqlTemp.replace("b.currStateId=:currStateId", "(b.currStateId=17 or b.currStateId=25) ");
//			qhql = new StringBuffer(hqlTemp);
//		}else{
			
//		}
		qhql.append(" and (b.currStateId=17 or b.currStateId=25 )")	; 
		if(dto.getDefBug()==1){
			qhql.append(" and b.assinOwnerId=:assinOwnerId ")	;
			queryInfo.getPraValues().put("assinOwnerId", myUserId);  
		}
		
		queryInfo.getPraValues().remove("currStateId");
		queryInfo.getPraValues().put("taskId", SecurityContextHolderHelp.getCurrTaksId());
		//queryInfo.setHqlCondiStr(queryInfo.getHqlCondiStr()+" and b.assinOwnerId=:assinOwnerId ");
		queryInfo.setHqlCondiStr(queryInfo.getHqlSelectData()+ qhql.toString().replaceAll(" b.currStateId=:currStateId", " 1=1 "));
		
		queryInfo.setCurrPraValues(queryInfo.getPraValues());
		dto.setQueryInfo(queryInfo);
		List bugs = bugCommonService.findAssignBug(dto);
		List<TypeDefine> typeList = dto.getTypeList();
		Map<String ,TypeDefine >  tdMap =  null;
		if(bugs!=null&&bugs.size()>0){
			if(typeList!=null){
				tdMap = bugCommonService.convertTdMap(dto);
				bugCommonService.setBugsRelaTypeDefine(tdMap, bugs);
			}else{
				this.setRelaTypeDefine(bugs);
			}
			 
			this.setRelaUser(bugs);
			this.setRelaTaskName(bugs);
		}
		tdMap =  null;
		dto.setTypeList(null);
		typeList=null;
		StringBuffer sb = new StringBuffer();
		dto.toJson2(bugs, sb);
		queryInfo = null;
		super.writeResult(sb.toString());
		dto = null;
		return super.globalAjax();
	}
    public View batchAssignExe(BusiRequestEvent req){
    	//执行批量分配并发MAIL，写分配历史
    	BugManagerDto dto = super.getDto(BugManagerDto.class, req);
    	int result = bugCommonService.executeAssignBug(dto);
    	if(result==0){
    		super.writeResult("failed");
    		return super.globalAjax();
    	}
    	dto.setPageNo(1);
		List<BugBaseInfo> bugs = bugCommonService.findAssignBug(dto);
		bugCommonService.sendBatchBugAssignNotice(dto);
		List<TypeDefine> typeList = dto.getTypeList();
		Map<String ,TypeDefine >  tdMap =  null;
		if(bugs!=null&&bugs.size()>0){
			if(typeList!=null){
				tdMap = bugCommonService.convertTdMap(dto);
				bugCommonService.setBugsRelaTypeDefine(tdMap, bugs);
			}else{
				this.setRelaTypeDefine(bugs);
			}
			this.setRelaUser(bugs);
			this.setRelaTaskName(bugs);
		}
		tdMap = null;
		dto.setTypeList(null);
		typeList=null;
		StringBuffer sb = new StringBuffer();
		dto.toJson2((List)bugs, sb);
		super.writeResult("sucessAssign_"+sb.toString());
		return super.globalAjax();

    }
    
    public View batchAssign(BusiRequestEvent req){
    	BugManagerDto dto = super.getDto(BugManagerDto.class, req);
    	CurrTaskInfo currTaskInfo = this.getCurrTaskInfo(dto.getTaskId());
    	if(dto.getTaskId()!=null&&!"".equals(dto.getTaskId())){
    		SecurityContextHolderHelp.setCurrTaksId(dto.getTaskId());
    	}
		//CurrTaskInfo currTaskInfo = testTaskService.getCurrTaskInfo();
		dto.setTestFlow(currTaskInfo.getTestFlow());
		dto.setRoleInTask(currTaskInfo.getRoleInTask());
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		dto.setTaskId(taskId);
		dto.setRelCaseSwitch(currTaskInfo.getRelCaseSwitch());
		dto.setStateList(BugFlowConst.getStateList());
		if(!"true".equalsIgnoreCase(dto.getIsAjax())){//不走ajax说明是从通过页面跑转过来的,这里缺省就是查本人要分配的
			dto.setDefBug(1);
		}
		
		dto.setOutLineState(currTaskInfo.getOutLineState());
		List<BugBaseInfo> bugs = bugCommonService.findAssignBug(dto);
		List<TypeDefine> typeList = dto.getTypeList();
		Map<String ,TypeDefine >  tdMap =  null;
		if(bugs!=null&&bugs.size()>0){
			if(typeList!=null){
				tdMap = bugCommonService.convertTdMap(dto);
				bugCommonService.setBugsRelaTypeDefine(tdMap, bugs);
			}else{
				this.setRelaTypeDefine(bugs);
			}
			this.setRelaUser(bugs);
			this.setRelaTaskName(bugs);
		}
		tdMap = null;
		dto.setTypeList(null);
		typeList=null;
		StringBuffer sb = new StringBuffer();
		dto.toJson2((List)bugs, sb);
		if("true".equalsIgnoreCase(dto.getIsAjax())){
			super.writeResult(sb.toString());
			return super.globalAjax();
		}
		dto.setListStr(sb.toString());
    	return super.getView();
    }
    
    public View jump2HandlingBug(BusiRequestEvent req){
    	
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		BugBaseInfo bug = bugCommonService.qucikQuery(dto.getBug().getBugId());
		if(bug!=null){
//			throw new DataBaseException("BUG不存在,可能己被删除");
//		}else{
			List<BugBaseInfo> bugs = new ArrayList<BugBaseInfo>(1);
			bugs.add(bug);
			dto.setTaskId(bug.getTaskId());
			SecurityContextHolder.getContext().getVisit().setTaskId(bug.getTaskId());
			CurrTaskInfo currTaskInfo = testTaskService.getCurrTaskInfo();
			dto.setTestFlow(currTaskInfo.getTestFlow());
			dto.setRoleInTask(currTaskInfo.getRoleInTask());
			String taskId = SecurityContextHolderHelp.getCurrTaksId();
			dto.setTaskId(taskId);
			dto.setRelCaseSwitch(currTaskInfo.getRelCaseSwitch());
			dto.setStateList(BugFlowConst.getStateList());
			dto.setDefBug(1);
			dto.setOutLineState(currTaskInfo.getOutLineState());
			this.setRelaTypeDefine(bugs);
			this.setRelaUser(bugs);
			this.setRelaTaskName(bugs);
			StringBuffer sb = new StringBuffer();
			dto.toJson2((List)bugs, sb);
			dto.setListStr("1/20/1$"+sb.toString());
			//super.writeResult(sb.toString());
			dto.setCountStr((impExpManagerService.getBugCountStr(taskId) +" (状态为：撤销,无效 ,重复 ,非错 ,关闭/撤销 ,修正/描述不当的BUG不算有效BUG)"));
		}
		
		return super.getView("loadMyBug");
    }
	public View handSub(BusiRequestEvent req){
		this.taskLockHand();
		return bugFlowControlBlh.handSub(req);
	}
	public View quickQuery(BusiRequestEvent req){
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		BugBaseInfo bug = bugCommonService.qucikQuery(dto.getBug().getBugId());
		if(bug==null){
			super.writeResult(JsonUtil.toJson(""));
		}else{
			List<BugBaseInfo> bugs = new ArrayList<BugBaseInfo>(1);
			bugs.add(bug);
			this.setRelaTypeDefine(bugs);
			this.setRelaUser(bugs);
			this.setRelaTaskName(bugs);
			this.setStateName(bugs);
//			StringBuffer sb = new StringBuffer();
//			dto.toJson2((List)bugs, sb);
//			super.writeResult(sb.toString());
			PageModel pg = new PageModel();
			pg.setRows(bugs);
//			Integer total =  (Integer) SecurityContextHolder.getContext().getAttr("pageInfoTotalRows");
			pg.setTotal(1);
			super.writeResult(JsonUtil.toJson(pg));
		}
		return super.globalAjax();
	}
	public View viewBugDetal(BusiRequestEvent req){
		
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		String taskId = dto.getTaskId();
		if(taskId==null){
			String hql = "select  new  BugBaseInfo(bugId,bugDesc,currStateId,attachUrl,taskId) from BugBaseInfo where bugId=? ";
			List<BugBaseInfo> bugList = bugCommonService.findByHql(hql, dto.getBug().getBugId());
			SecurityContextHolderHelp.setCurrTaksId(bugList.get(0).getTaskId());
		}else{  
			StringBuffer hql = new StringBuffer("select new TestTaskDetail(outlineState,testPhase,")
			.append("currentVersion,testSeq,reltCaseFlag) from TestTaskDetail where taskId=? and companyId=?");
			List<TestTaskDetail> taskList =testTaskService.findByHql(hql.toString(), dto.getTaskId(),SecurityContextHolderHelp.getCompanyId());
			if(taskList==null||taskList.isEmpty()){
				throw new DataBaseException("非法提交的测试任务数据，不受理");
			}    
			SecurityContextHolderHelp.setCurrTaksId(taskId);
 
			dto.setTaskId(taskId);
			
		}
		
		if(dto.getIsAjax()==null){
			return bugFlowControlBlh.viewDetal(req);
		}
		bugFlowControlBlh.viewDetal(req);
		BugBaseInfo bug = dto.getBug();
		bug.setStateName(BugFlowConst.getStateName(bug.getCurrStateId()));
		bug.setTestCases(null);
		super.writeResult(JsonUtil.toJson(bug));
		return super.globalAjax();
	}
	public View bugHand(BusiRequestEvent req){
		//把当前测试流程也转到页面去
		return bugFlowControlBlh.bugHand(req);
	}

	public View loadHistory(BusiRequestEvent req){
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		Long bugId = dto.getBug().getBugId();
		List bugHistory = bugCommonService.getBugHistory(bugId,dto.getPageNo());
//		StringBuffer sb = new StringBuffer();
//		dto.toJson(bugHistory, sb);
		PageModel pg = new PageModel();
		pg.setRows(bugHistory);
		Integer total =  (Integer) SecurityContextHolder.getContext().getAttr("pageInfoTotalRows");
		pg.setTotal(total);
//		super.writeResult(sb.toString());
		super.writeResult(JsonUtil.toJson(pg));
		dto = null;
		return super.globalAjax();
	}


	public View findByquery(BusiRequestEvent req){
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		String hql = "from BugQueryInfo where queryId=? and ownerId=?";
		String myUserId = SecurityContextHolderHelp.getUserId();
		Long queryId = dto.getQueryId();
		List queryList = bugCommonService.findByHql(hql, queryId,myUserId);
		if(queryList==null||queryList.size()==0){
			super.writeResult("deleted");
			return super.globalAjax();
		}
		BugQueryInfo queryInfo = (BugQueryInfo)queryList.get(0);
		dto.setHqlParamMaps(queryInfo.praValueStr2Map());

		if(queryInfo.getOnlyMe()==1){
			//String taskId = SecurityContextHolderHelp.getCurrTaksId();
			String taskId = queryInfo.getTaskId();
			if(taskId!=null){
				//SecurityContextHolderHelp.setCurrTaksId(taskId);
				//这里不用通用的,单猜调和本地的getCurrTaskInfo方法
				CurrTaskInfo taskFlowRoleInfo = this.getCurrTaskInfo(taskId);
				dto.setTestFlow(taskFlowRoleInfo.getTestFlow());
				dto.setRoleInTask(taskFlowRoleInfo.getRoleInTask());
				if(isFindMyBug())
					dto.setDefBug(1);
			}else{
				if(isFindMyBug())
					dto.setDefBug(1);
				dto.setAllTestTask(true);
			}
		}
		dto.setQueryInfo(queryInfo);
		List bugs = bugCommonService.findByQuery(dto);
		List<TypeDefine> typeList = dto.getTypeList();
		Map<String ,TypeDefine >  tdMap =  null;
		if(bugs!=null&&bugs.size()>0){
			if(typeList!=null){
				tdMap = bugCommonService.convertTdMap(dto);
				bugCommonService.setBugsRelaTypeDefine(tdMap, bugs);
			}else{
				this.setRelaTypeDefine(bugs);
			}
			 
			this.setRelaUser(bugs);
			this.setRelaTaskName(bugs);
		}
		tdMap =  null;
		dto.setTypeList(null);
		typeList=null;
		StringBuffer sb = new StringBuffer();
		dto.toJson2(bugs, sb);
		super.writeResult(sb.toString());
		dto.setHqlParamMaps(null);
		dto = null;

		return super.globalAjax();
	}
	
	private CurrTaskInfo getCurrTaskInfo(String taskId){
		
		String  hql= "from TestFlowInfo where  taskId=? order by testFlowCode";
		List<TestFlowInfo> testFlow = bugCommonService.findByHql(hql, taskId);
		CurrTaskInfo currTaskInfo = new CurrTaskInfo();
		currTaskInfo.setFlowList(testFlow);
		StringBuffer flow = new StringBuffer();
		StringBuffer role = new StringBuffer();
		String userId = SecurityContextHolderHelp.getUserId();
		hql = "from TaskUseActor where taskId=? and userId=? and isEnable = 1 order by actor";
		List<TaskUseActor> useActor = bugCommonService.findByHql(hql,taskId,userId);;
		for(TestFlowInfo flw:testFlow){
			flow.append(flw.getTestFlowCode()).append(" ");
		}
		currTaskInfo.setActorList(useActor);
		//只有流程启用角色才算，
		for(TaskUseActor actor :useActor){
				role.append(actor.getActor()).append(" ");
		}
		currTaskInfo.setTestFlow(flow.toString().trim());
		currTaskInfo.setRoleInTask(role.toString().trim());
		hql = " select new TestTaskDetail(outlineState,testPhase,currentVersion,testSeq,reltCaseFlag) from TestTaskDetail where taskId=?";
		List<TestTaskDetail> taskList = bugCommonService.findByHql(hql, taskId);
		if(taskList==null||taskList.isEmpty()){
			SecurityContextHolder.getContext().getVisit().setTaskId(null);
			throw new DataBaseException(" 测试任务不存在，不受理");
		}
		TestTaskDetail taskDetal = taskList.get(0);
		currTaskInfo.setOutLineState(taskDetal.getOutlineState());
		currTaskInfo.setTestPhase(taskDetal.getTestPhase());
		currTaskInfo.setRelCaseSwitch(taskDetal.getReltCaseFlag());
		return currTaskInfo;
	}
	public View saveQuery(BusiRequestEvent req){
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		if(taskId==null){
			SecurityContextHolderHelp.setCurrTaksId(dto.getTaskId());
			taskId = dto.getTaskId();
		}
		dto.setTaskId(taskId);
		Long queryId = dto.getQueryId();
		BugQueryInfo queryInfo = bugCommonService.saveQueryInfo(dto);
		if(dto.getLoadType()==1){
			return this.loadQueryList(req);
		}
		if(queryId==null){//这里是增加操作
			super.writeResult("sucess^"+queryInfo.getQueryId());
		}else{
			super.writeResult("sucess");
		}
		dto = null;
		return super.globalAjax();
	}
	public View queryDetail(BusiRequestEvent req){
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		super.writeResult(bugCommonService.getQueryJsonStr(dto.getQueryId()));
		dto = null;
		return super.globalAjax();
	}
	
	public View delQuery(BusiRequestEvent req){
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		Long queryId = dto.getQueryId();
		String hql = "delete from BugQueryInfo where queryId=? and ownerId=?";
		String myUserId = SecurityContextHolderHelp.getUserId();
		bugCommonService.getHibernateGenericController().executeUpdate(hql, queryId,myUserId);
		dto = null;
		return super.globalAjax();
	}
	public View findInit(BusiRequestEvent req){
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		//下面的代码中,只要是中我的所有BUG列表过来的,taskId都为空
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		dto.setTaskId(taskId);
		bugCommonService.findInit(dto);
		if(dto.getLoadType()==1){
			bugCommonService.initBugListDate(dto);
			if(taskId!=null&&!"".equals(taskId.trim())){
				List<ListObject> verList = testTaskService.getVerSelList();
				dto.getBug().getDtoHelper().setVerSelStr(HtmlListComponent.toSelectStr(verList, "$"));
			}
		}
		if(taskId!=null&&!"".equals(taskId.trim())){
			List<User> testActor = (List<User>)dto.getAttr("testActor");
			List<User> devActor = (List<User>)dto.getAttr("devActor");
			List<ListObject> testList = drawHtmlListDateService.convertUser2ListObj(testActor);
			List<ListObject> devList = drawHtmlListDateService.convertUser2ListObj(devActor);
			dto.getBug().setTestSelStr(HtmlListComponent.toSelectStr(testList, "$"));
			dto.getBug().setDevStr(HtmlListComponent.toSelectStr(devList, "$"));
		}

		dto.getBug().setQuerySelStr(HtmlListComponent.toSelectStr(dto.getQueryList(), "$"));
		if(dto.getLoadType()==1){
			super.writeResult(dto.getBug().toStrFindInit(true)+"^stateList="+JsonUtil.toJson(BugFlowConst.getStateList()).toString());
		}else{
			super.writeResult(dto.getBug().toStrFindInit(false));
		}
		dto = null;
		return super.globalAjax();
	}
	public View loadQueryList(BusiRequestEvent req){
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		StringBuffer hqlSb = new StringBuffer("select new BugQueryInfo(queryId, onlyMe,taskId,queryName)");
		hqlSb.append(" from BugQueryInfo where ownerId=? and (taskId =? or taskId is null)");
		String myUserId = SecurityContextHolderHelp.getUserId();
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		if(taskId==null){
			taskId = dto.getTaskId();
		}
		List<BugQueryInfo> queryList = bugCommonService.findByHql(hqlSb.toString(), myUserId,taskId);
		super.writeResult(this.queryList2Json(queryList));
		req.setDto(null);
		return super.globalAjax();
	}

	public View loadQuery4AllTaskList(BusiRequestEvent req){
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		StringBuffer hqlSb = new StringBuffer("select new BugQueryInfo(queryId, onlyMe,taskId,queryName)");
		hqlSb.append(" from BugQueryInfo where ownerId=? and (taskId =? or taskId is null)");
		String myUserId = SecurityContextHolderHelp.getUserId();
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		if(taskId==null){
			taskId = dto.getTaskId();
		}
		List<BugQueryInfo> queryList = bugCommonService.findByHql(hqlSb.toString(), myUserId,taskId);
		super.writeResult(this.queryList2Json(queryList));
		req.setDto(null);
		return super.globalAjax();
	}
	public View loadMdPerson(BusiRequestEvent req){
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		dto.setIsAjax("true");
		Long currNodeId = dto.getCurrNodeId();
		List<User> users = outLineService.getModuleMemb(currNodeId,dto.getLoadType());
		List<ListObject> listData = drawHtmlListDateService.convertUser2ListObj(users);
//		super.writeResult(HtmlListComponent.toSelectStr(listData, "$"));
//		dto = null;
//		return super.globalAjax();
		
		PageModel pg = new PageModel();
		pg.setRows(listData);
		Integer total=0;
		if(listData!=null && listData.size()>0){
			total=listData.size();
		}
		
//		Integer total =  (Integer) SecurityContextHolder.getContext().getAttr("pageInfoTotalRows");
		pg.setTotal(total);
		super.writeResult(JsonUtil.toJson(pg));
		return super.globalAjax();
	}
	public View findBug(BusiRequestEvent req) {
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		//String taskId = SecurityContextHolderHelp.getCurrTaksId();
		String taskId = dto.getTaskId();
		/*if(taskId==null){
			SecurityContextHolderHelp.setCurrTaksId(dto.getTaskId());
			taskId = dto.getTaskId();
		}*/
		if("treeView".equals(dto.getModuleName())){//树形显示模示下的查询
			Long moduleId = dto.getBug().getModuleId();
			//System.out.println(moduleId);
			if(moduleId!=null){
				String hql = "select new OutlineInfo(moduleNum,moduleId) from OutlineInfo where moduleId=? and taskId=?";
				List<OutlineInfo> list = bugManagerService.findByHql(hql, moduleId,taskId);
				if(list==null||list.isEmpty()){
					super.writeResult("failed");
					return super.globalAjax();
				}
				OutlineInfo outLine = list.get(0);
				//System.out.println("==="+outLine.getModuleNum());
				if(outLine.getModuleNum()!=null){
					dto.getBug().setModuleNum(outLine.getModuleNum());
				}				
			}
			//设置测试需求ID为空，这时在查询时，用测试需求编号来查
			dto.getBug().setModuleId(null);
		}
		CurrTaskInfo taskFlowRoleInfo = testTaskService.getCurrTaskInfo();
		dto.setTestFlow(taskFlowRoleInfo.getTestFlow());
		dto.setRoleInTask(taskFlowRoleInfo.getRoleInTask());
		//String taskId = SecurityContextHolderHelp.getCurrTaksId();
		dto.setTaskId(taskId);
		//System.out.println("===222");
		List<BugBaseInfo> bugs = bugManagerService.loadBug(dto);
		List<TypeDefine> typeList = dto.getTypeList();
		Map<String ,TypeDefine >  tdMap =  null;
		if(bugs!=null&&bugs.size()>0){
			if(typeList!=null){
				tdMap = bugCommonService.convertTdMap(dto);
				bugCommonService.setBugsRelaTypeDefine(tdMap, bugs);
			}else{
				this.setRelaTypeDefine(bugs);
			}
			 
			this.setRelaUser(bugs);
			this.setRelaTaskName(bugs);
			this.setStateName(bugs);
		}
		if(dto.getSaveQuery()==1){
			BugQueryInfo queryInfo = dto.getQueryInfo();
			//下面的判断一定要在praValues2Str前执行
			if(dto.getDefBug()==1){
				queryInfo.setOnlyMe(1);
			}else{
				queryInfo.setOnlyMe(0);
			}
			if(dto.getAppScope()!=1){
				queryInfo.setTaskId(dto.getTaskId());
			}else{//把生成的模块条件去掉
				queryInfo.setHqlCondiStr(queryInfo.getHqlCondiStr().replace("b.moduleId=:moduleId", "1=1"));
			}
			//下面的操作必须在上面两个if后面
			queryInfo.setParaValueStr(queryInfo.praValues2Str());
			queryInfo.setHqlCondiStr(queryInfo.getHqlCondiStr().substring(queryInfo.getHqlCondiStr().indexOf("where")));
			queryInfo.setQueryName(dto.getQueryName());
			bugManagerService.add(queryInfo);
		}
		tdMap =  null;
		dto.setTypeList(null);
		typeList=null;
//		StringBuffer sb = new StringBuffer();
//		dto.toJson2((List)bugs, sb);
		if("true".equalsIgnoreCase(dto.getIsAjax())){
			if(dto.getSaveQuery()==1){
//				super.writeResult(dto.getQueryInfo().getQueryId() +"$"+sb.toString());
			}else{
//				super.writeResult(sb.toString());
			}
		}
//		dto.setListStr(sb.toString());
//		return super.getView();
		PageModel pg = new PageModel();
		if(bugs==null){
			bugs = new ArrayList<BugBaseInfo>();
		}
		pg.setRows(bugs);
		Integer total =  (Integer) SecurityContextHolder.getContext().getAttr("pageInfoTotalRows");
		pg.setTotal(total);
		super.writeResult(JsonUtil.toJson(pg));
		return super.globalAjax();
	}

	public void setRelaTypeDefine(List<BugBaseInfo> list){
		if(list==null||list.isEmpty()){
			return;
		}
		Map<String ,DefaultTypeDefine >  tdMap = bugManagerService.getRelaTypeDefine(list);
		if(tdMap==null||tdMap.isEmpty()){
			return ;
		}
		DefaultTypeDefine td = null;
		for(BugBaseInfo bug: list){
			if(bug.getBugFreqId()!=null){
				td = tdMap.get(bug.getBugFreqId().toString());
				if(td!=null){
					bug.setBugFreq(td.newTypeDefine(BugFreq.class));
				}
			}
			if(bug.getBugTypeId()!=null){
				td = tdMap.get(bug.getBugTypeId().toString());
				if(td!=null){
					bug.setBugType(td.newTypeDefine(BugType.class));
				}
			}	
			if(bug.getBugGradeId()!=null){
				td = tdMap.get(bug.getBugGradeId().toString());
				if(td!=null){
					bug.setBugGrade(td.newTypeDefine(BugGrade.class));
				}
			}
			if(bug.getBugOccaId()!=null){
				td = tdMap.get(bug.getBugOccaId().toString());
				if(td!=null){
					bug.setBugOpotunity(td.newTypeDefine(BugOpotunity.class));
				}
			}
			if(bug.getPriId()!=null){
				td = tdMap.get(bug.getPriId().toString());
				if(td!=null){
					bug.setBugPri(td.newTypeDefine(BugPri.class));
				}
			}
//			if(bug.getGeneCauseId()!=null){
//				td = tdMap.get(bug.getGeneCauseId().toString());
//				if(td!=null){
//					bug.setGeneCause(td.newTypeDefine(GeneCause.class));
//				}
//			}
//			if(bug.getSourceId()!=null){
//				td = tdMap.get(bug.getSourceId().toString());
//				if(td!=null){
//					bug.setBugSource(td.newTypeDefine(BugSource.class));
//				}
//			}
//			if(bug.getPlatformId()!=null){
//				td = tdMap.get(bug.getPlatformId().toString());
//				if(td!=null){
//					bug.setOccurPlant(td.newTypeDefine(OccurPlant.class));
//				}
//			}			
//			if(bug.getGenePhaseId()!=null){
//				td = tdMap.get(bug.getGenePhaseId().toString());
//				if(td!=null){
//					bug.setImportPhase(td.newTypeDefine(ImportPhase.class));
//				}
//			}			
		}
		tdMap.clear();
	}
	
	@SuppressWarnings("unchecked")
	public View add(BusiRequestEvent req){
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		List<FileInfoVo> fileInfos = dto.getFileInfos();
		List<FileInfoVo> filesInfo = null;
		if(fileInfos!=null && fileInfos.size()>0){
			JSONArray json = JSONArray.fromObject(fileInfos.toString().replace("[[", "[").replace("]]", "]]"));
			filesInfo= (List<FileInfoVo>)JSONArray.toCollection(json, FileInfoVo.class);
			dto.setFileInfos(filesInfo);
		}
		BugBaseInfo bug = dto.getBug();
		bug.setReptDate(new Date());
		bug.setBugReptId(SecurityContextHolderHelp.getUserId());
		bug.setTaskId(SecurityContextHolderHelp.getCurrTaksId());
		if(bug.getNextFlowCd()>2){
			bug.setTestOwnerId(bug.getBugReptId());
			//bug.setVerifyVer(bug.getBugReptVer());
		}
		//Map currVerInfo = testTaskService.getCurrTestSeqAndVer(bug.getTaskId(), bug.getModuleId());
		//bug.setCurrVer((String)currVerInfo.get("currVer"));
		//bug.setBugReptVer(bug.getCurrVer());
		bug.setCurrHandlDate(new Date());
		bug.setCurrHandlerId(bug.getBugReptId());
		bug.setInitState(bug.getCurrStateId());
		bug.setRelaCaseFlag(0);
		bug.setMsgFlag(0);
		bug.setCurrFlowCd(1);
		bug.setMsgFlag(0);
		bug.setNextOwnerId(this.getNextHandler(bug));
		bug.setModelName(dto.getModuleName());
		String hql ="select new OutlineInfo(moduleNum,moduleId)  from OutlineInfo where taskId=? and moduleId=?";
		List<OutlineInfo> outLineList = bugManagerService.findByHql(hql, bug.getTaskId(),bug.getModuleId());
		bug.setModuleNum(outLineList.get(0).getModuleNum());
		//System.out.println(dto.getModuleName());
		bugManagerService.addBug(dto);
		BugHandHistory bugHistory = new BugHandHistory();
		bugHistory.setInitState(bug.getInitState());
		bugHistory.setBugState(bug.getCurrStateId());
		//bugHistory.setHandlerName(bug.getModelName());
		bugHistory.setHandlerId(bug.getCurrHandlerId());
		bugHistory.setInsDate(bug.getCurrHandlDate());
		bugHistory.setTestFlowCd(bug.getCurrFlowCd());
		bugHistory.setBugId(bug.getBugId());
		bugHistory.setTaskId(bug.getTaskId());
		bugHistory.setModuleId(bug.getModuleId());
		bugHistory.setRemark("新提交问题且无分配流程");
		bugHistory.setHandResult("状态为: "+BugFlowConst.getStateName(bug.getCurrStateId()));
		//bugHistory.setTestSeq(bug.getTestSeq());
		bugHistory.setCurrVer(dto.getCurrVer());
		bugHistory.setCurrDayFinal(1);
		bugCommonService.sendBugNotice(bugHistory, dto.getBug(),null);
		bugHistory = null;
		bug.settestName(SecurityContextHolderHelp.getMyRealDisplayName());
		List<BugBaseInfo> list = new ArrayList<BugBaseInfo>(1);
		list.add(dto.getBug());
		this.setRelaTaskName(list);
//		super.writeResult("success^"+bug.toStrUpdateRest());
		super.writeResult("success");
		dto = null;
		return super.globalAjax();
	}
	
	private String getNextHandler(BugBaseInfo bug ){
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
	public View addInit(BusiRequestEvent req){
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		if(dto.getTaskId()!=null&&!"".equals(dto.getTaskId().trim())){
			SecurityContextHolder.getContext().getVisit().setTaskId(dto.getTaskId());	
		}
		taskLockHand();
		CurrTaskInfo currTaskInfo = testTaskService.getCurrTaskInfo();
		if(currTaskInfo.getOutLineState()==0){
			dto = null;
			super.writeResult("noOutLine");
			return super.globalAjax();
		}
		List<TestFlowInfo> testFlow = currTaskInfo.getFlowList();
		BugBaseInfo bug = new BugBaseInfo();
		dto.setBug(bug);
		dto.getBug().setTestFlow(currTaskInfo.getTestFlow());
		dto.getBug().setRoleInTask(currTaskInfo.getRoleInTask());
		dto.getBug().setTestPhase(currTaskInfo.getTestPhase());
		//dto.getBug().setInitSeq(currTaskInfo.getTestSeq());
		//dto.getBug().setTestSeq(currTaskInfo.getTestSeq());
		dto.getBug().setRelCaseSwitch(currTaskInfo.getRelCaseSwitch());
		if(dto.getLoadType()==1){
			List<TypeDefine> typeList = bugCommonService.getBugListData();
			dto.setTypeList(typeList);
			bugCommonService.initBugListDate(dto);	
			List<ListObject> verList = testTaskService.getVerSelList();
			dto.getBug().getDtoHelper().setVerSelStr(HtmlListComponent.toSelectStr(verList, "$"));
		}
		int nextFlow = testFlow.get(1).getTestFlowCode();
		List<User> actorUser = null;
		if(nextFlow==2){//测试互验
			actorUser = testTaskService.getTeamMembByActCd(2);
			 List<ListObject> actorList = drawHtmlListDateService.convertUser2ListObj(actorUser);
			 dto.getBug().setTestSelStr(HtmlListComponent.toSelectStr(actorList, "$"));
			 dto.getBug().setNextFlowCd(2);
			 dto.getBug().setCurrStateId(1);//待置
			 dto.getBug().setStateName(BugFlowConst.getStateName(dto.getBug().getCurrStateId()));
		}else if(nextFlow==3){//分析问题
			actorUser = testTaskService.getTeamMembByActCd(3);
			 List<ListObject> actorList = drawHtmlListDateService.convertUser2ListObj(actorUser);
			 dto.getBug().setNextFlowCd(3);
			 dto.getBug().setCurrStateId(24);//分析
			 dto.getBug().setAnalySelStr(HtmlListComponent.toSelectStr(actorList, "$"));
			 dto.getBug().setStateName(BugFlowConst.getStateName(dto.getBug().getCurrStateId()));
		}else if(nextFlow==4){//分配问题
			actorUser = testTaskService.getTeamMembByActCd(4);
			 List<ListObject> actorList = drawHtmlListDateService.convertUser2ListObj(actorUser);
			 dto.getBug().setAssignSelStr(HtmlListComponent.toSelectStr(actorList, "$"));
			 dto.getBug().setNextFlowCd(4);
			 dto.getBug().setCurrStateId(25);//分配
			 dto.getBug().setStateName(BugFlowConst.getStateName(dto.getBug().getCurrStateId()));
		}else {//开发修改问题
			 actorUser = testTaskService.getTeamMembByActCd(5);
			 List<ListObject> actorList = drawHtmlListDateService.convertUser2ListObj(actorUser);
			 dto.getBug().setDevStr(HtmlListComponent.toSelectStr(actorList, "$"));
			 dto.getBug().setNextFlowCd(5);
			 dto.getBug().setCurrStateId(10);//待改
			 dto.getBug().setStateName(BugFlowConst.getStateName(dto.getBug().getCurrStateId()));
			 dto.getBug().setTestOwnerId(SecurityContextHolderHelp.getUserId());
		}
		dto.getBug().setCurrFlowCd(1);
		
		
		if(dto.getLoadType()==1){
			super.writeResult(dto.getBug().toStrAddInit(true));
		}else{
			super.writeResult(dto.getBug().toStrAddInit(false));
		}
		BugAddinitDto bugAddinitDto = dto.getBug().getDtoHelper().createAddInitDto();
		bugAddinitDto.currFlowCd = dto.getBug().getCurrFlowCd();
		bugAddinitDto.nextFlowCd = dto.getBug().getNextFlowCd();
		if(dto.getBug().getTestPhase()!=null){
			bugAddinitDto.testPhase = dto.getBug().getTestPhase();
		}
		bugAddinitDto.currStateId = dto.getBug().getCurrStateId();
		bugAddinitDto.stateName = dto.getBug().getStateName();
		//System.out.println(JsonUtil.toJson(bugAddinitDto));
		dto = null;
		return super.globalAjax();
	}
	
	public View delete(BusiRequestEvent req){
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		if(dto.getTaskId()!=null&&!"".equals(dto.getTaskId().trim())){
			SecurityContextHolder.getContext().getVisit().setTaskId(dto.getTaskId());	
		}
		bugManagerService.delete(dto.getBug().getBugId());
		super.writeResult("success");
		dto = null;
		return super.globalAjax();
	}
	public View upInit(BusiRequestEvent req){
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		if(dto.getTaskId()!=null&&!"".equals(dto.getTaskId().trim())){
			SecurityContextHolder.getContext().getVisit().setTaskId(dto.getTaskId());	
		}
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		dto.setTaskId(taskId);
		taskLockHand();
		bugManagerService.upInitPrepare(dto);
		bugManagerService.upInit(dto);
		bugCommonService.initBugListDate(dto);
		dto.getBug().setVerSelStr(HtmlListComponent.toSelectStr(dto.getVerList(), "$"));
		bugFlowControlBlh.setCurrOwner(dto,dto.getBug().getNextFlowCd());
		dto.getBug().setTestCases(null);
		dto.setCurrTaskInfo(null);
		super.writeResult(JsonUtil.toJson(dto));
		dto = null;
		return super.globalAjax();
//		return super.getView();
	}
	
	public View update(BusiRequestEvent req){
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		dto.setTaskId(taskId);
		BugBaseInfo bug =dto.getBug();
		String hql ="select new OutlineInfo(moduleNum,moduleId)  from OutlineInfo where taskId=? and moduleId=?";
		List<OutlineInfo> outLineList = bugManagerService.findByHql(hql, taskId,bug.getModuleId());
		bug.setModuleNum(outLineList.get(0).getModuleNum());
		bugManagerService.update(dto);
		List<BugBaseInfo> list = new ArrayList<BugBaseInfo>(1);
		list.add(dto.getBug());
		dto.getBug().setTaskId(taskId);
		this.setRelaTaskName(list);
		super.writeResult("success");
		dto = null;
		return super.globalAjax();
	}
	public View getInTaskRole(BusiRequestEvent req){
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		if(dto.getTaskId()!=null&&!"".equals(dto.getTaskId().trim())){
			SecurityContextHolder.getContext().getVisit().setTaskId(dto.getTaskId());	
		}else{
			super.writeResult("failed");
			return super.globalAjax();
		}
		CurrTaskInfo currTaskInfo = testTaskService.getCurrTaskInfo();
		super.writeResult(currTaskInfo.getRoleInTask());
		return super.globalAjax();
	}
	public View loadMyBug(BusiRequestEvent req) {
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		//作下面的检查，是为了防止前端篡改taskId
		if(dto.getTaskId()!=null&&!"".equals(dto.getTaskId().trim())){
			StringBuffer hql = new StringBuffer("select new TestTaskDetail(outlineState,testPhase,")
			.append("currentVersion,testSeq,reltCaseFlag) from TestTaskDetail where taskId=? and companyId=? and testTaskState!=4 ");
			List<TestTaskDetail> taskList =testTaskService.findByHql(hql.toString(), dto.getTaskId(),SecurityContextHolderHelp.getCompanyId());
			if(taskList==null||taskList.size()==0){
				PageModel pg = new PageModel();
				pg.setRows(null);
				pg.setTotal(0);
				if(dto.getIsAjax()!=null){
					
					super.writeResult(JsonUtil.toJson(pg));
					return super.globalAjax();
				}
				dto.setOutLineState(0);
//				dto.setListStr("1/10/0$");
				super.writeResult(JsonUtil.toJson(pg));
				super.writeResult(JsonUtil.toJson(dto));
				return super.globalAjax();
			}
			SecurityContextHolder.getContext().getVisit().setTaskId(dto.getTaskId());
		}else if(SecurityContextHolderHelp.getCurrTaksId()==null&&dto.getTaskId()==null){
			PageModel pg = new PageModel();
			pg.setRows(null);
			pg.setTotal(0);
			if(dto.getIsAjax()!=null){
				super.writeResult(JsonUtil.toJson(pg));
				return super.globalAjax();
			}
			dto.setOutLineState(0);
//			dto.setListStr("1/10/0$");
			super.writeResult(JsonUtil.toJson(pg));
			return super.globalAjax();
		}
		CurrTaskInfo currTaskInfo = testTaskService.getCurrTaskInfo();
		dto.setTestFlow(currTaskInfo.getTestFlow());
		dto.setRoleInTask(currTaskInfo.getRoleInTask());
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		dto.setTaskId(taskId);
		dto.setRelCaseSwitch(currTaskInfo.getRelCaseSwitch());
		dto.setStateList(BugFlowConst.getStateList());
		if(isFindMyBug())
			dto.setDefBug(1);
		dto.setOutLineState(currTaskInfo.getOutLineState());
		Long moduleId =null;
		if(dto.getBug()!=null){
			moduleId = dto.getBug().getModuleId();
		}
		if(moduleId!=null){
			String hql = "select new OutlineInfo(moduleNum,moduleId) from OutlineInfo where moduleId=? and taskId=?";
			List<OutlineInfo> list = bugManagerService.findByHql(hql, moduleId,taskId);
			if(list==null||list.isEmpty()){
				super.writeResult("failed");
				return super.globalAjax();
			}
			OutlineInfo outLine = list.get(0);
			//System.out.println("==="+outLine.getModuleNum());
			if(outLine.getModuleNum()!=null){
				dto.getBug().setModuleNum(outLine.getModuleNum());
			}				
			//设置测试需求ID为空，这时在查询时，用测试需求编号来查
			dto.getBug().setModuleId(null);
		}
		
		List<BugBaseInfo> bugs = bugCommonService.findBug(dto);
		List<TypeDefine> typeList = dto.getTypeList();
		Map<String ,TypeDefine >  tdMap =  null;
		if(bugs!=null&&bugs.size()>0){
			if(typeList!=null){
				tdMap = bugCommonService.convertTdMap(dto);
				bugCommonService.setBugsRelaTypeDefine(tdMap, bugs);
			}else{
				this.setRelaTypeDefine(bugs);
			}
			this.setRelaUser(bugs);
			this.setRelaTaskName(bugs);
			this.setStateName(bugs);
		}
		tdMap = null;
		dto.setTypeList(null);
		typeList=null;
//		StringBuffer sb = new StringBuffer();
//		dto.toJson2((List)bugs, sb);
		if("true".equalsIgnoreCase(dto.getIsAjax())){
			PageModel pg = new PageModel();
			if(bugs!=null && bugs.size()>0){
				pg.setRows(bugs);
			}else{
				pg.setRows(new ArrayList<BugBaseInfo>() );
			}
			Integer total =  (Integer) SecurityContextHolder.getContext().getAttr("pageInfoTotalRows");
			pg.setTotal(total);
			super.writeResult(JsonUtil.toJson(pg));
			return super.globalAjax();
		}
		dto.setCountStr((impExpManagerService.getBugCountStr(taskId) +" (状态为：撤销,无效 ,重复 ,非错 ,关闭/撤销 ,修正/描述不当的BUG不算有效BUG)"));
//		dto.setListStr(sb.toString());
//		return super.getView("loadMyBug");
		super.writeResult(JsonUtil.toJson(dto));
		return super.globalAjax();
	}
	
	public View loadAllMyBug(BusiRequestEvent req) {
		return super.getView();
	}
	
	public View loadAllMyBugList(BusiRequestEvent req) {
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		if(StringUtils.isNullOrEmpty(dto.getTaskFlag())){
			String taskId = SecurityContextHolderHelp.getCurrTaksId();
			if(taskId!=null&&!taskId.trim().equals("")){
				return loadMyBug(req);
			}
		}
		
		dto.setAppScope(0);
		dto.setStateList(BugFlowConst.getStateList());
//		if(dto.getDefBug()==0){
//			dto.setDefBug(1);
//		}
		if(isFindMyBug())
			dto.setDefBug(1);
		List<BugBaseInfo> bugs = bugCommonService.findAllMyBug(dto);
		List<TypeDefine> typeList = dto.getTypeList();
		Map<String ,TypeDefine >  tdMap =  null;
		if(bugs!=null&&bugs.size()>0){
			if(typeList!=null){
				tdMap = bugCommonService.convertTdMap(dto);
				bugCommonService.setBugsRelaTypeDefine(tdMap, bugs);
			}else{
				this.setRelaTypeDefine(bugs);
			}
			this.setRelaUser(bugs);
			this.setRelaTaskName(bugs);
			this.setStateName(bugs);
		}
		if(dto.getSaveQuery()==1){
			BugQueryInfo queryInfo = dto.getQueryInfo();
			//下面的判断一定要在praValues2Str前执行
			if(dto.getDefBug()==1){
				queryInfo.setOnlyMe(1);
			}else{
				queryInfo.setOnlyMe(0);
			}
			if(dto.getAppScope()!=1){
				queryInfo.setTaskId(dto.getTaskId());
			}else{//把生成的模块条件去掉
				queryInfo.setHqlCondiStr(queryInfo.getHqlCondiStr().replace("b.moduleId=:moduleId", "1=1"));
			}
			//下面的操作必须在上面两个if后面
			queryInfo.setParaValueStr(queryInfo.praValues2Str());
			queryInfo.setHqlCondiStr(queryInfo.getHqlCondiStr().substring(queryInfo.getHqlCondiStr().indexOf("where")));
			queryInfo.setQueryName(dto.getQueryName());
			bugManagerService.add(queryInfo);
		}
		tdMap = null;
		dto.setTypeList(null);
		typeList=null;
		/*StringBuffer sb = new StringBuffer();
		dto.toJson2((List)bugs, sb);*/
		if("true".equalsIgnoreCase(dto.getIsAjax())){
			if(dto.getSaveQuery()==1){
//				super.writeResult(dto.getQueryInfo().getQueryId() +"$"+sb.toString());
				super.writeResult(JsonUtil.toJson(bugs));
			}else{
//				super.writeResult(sb.toString());
				PageModel pg = new PageModel();
				if(bugs!=null && bugs.size()>0){
					pg.setRows(bugs);
				}else{
					pg.setRows(new ArrayList<BugBaseInfo>());
				}
				Integer total =  (Integer) SecurityContextHolder.getContext().getAttr("pageInfoTotalRows");
				pg.setTotal(total);
				super.writeResult(JsonUtil.toJson(pg));
			}
			return super.globalAjax();
		}
		dto.setCountStr((impExpManagerService.getBugCountStr() +" (状态为：撤销,无效 ,重复 ,非错 ,关闭/撤销 ,修正/描述不当的BUG不算有效BUG)"));
//		dto.setListStr(sb.toString());
//		return super.getView();
		super.writeResult(JsonUtil.toJson(dto));
		return super.globalAjax();
	}
	

	public void setStateName(List<BugBaseInfo> list){
		for(BugBaseInfo bug: list){
			bug.setStateName(BugFlowConst.getStateName(bug.getCurrStateId()));
		}
	}
	
	public View sw2AllMyBug(BusiRequestEvent req) {
		
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		//关联重复BUG的查找,不设置为 null
//		if(dto.isAllTestTask()){
//			SecurityContextHolderHelp.setCurrTaksId(null);
//		}
		dto.setAppScope(0);
		dto.setStateList(BugFlowConst.getStateList());
		List<BugBaseInfo> bugs = bugCommonService.findAllMyBug(dto);
		List<TypeDefine> typeList = dto.getTypeList();
		Map<String ,TypeDefine >  tdMap =  null;
		if(bugs!=null&&bugs.size()>0){
			if(typeList!=null){
				tdMap = bugCommonService.convertTdMap(dto);
				bugCommonService.setBugsRelaTypeDefine(tdMap, bugs);
			}else{
				this.setRelaTypeDefine(bugs);
			}
			this.setRelaUser(bugs);
			this.setRelaTaskName(bugs);
		}
		if(dto.getSaveQuery()==1){
			BugQueryInfo queryInfo = dto.getQueryInfo();
			//下面的判断一定要在praValues2Str前执行
			if(dto.getDefBug()==1){
				queryInfo.setOnlyMe(1);
			}else{
				queryInfo.setOnlyMe(0);
			}
			if(dto.getAppScope()!=1){
				queryInfo.setTaskId(dto.getTaskId());
			}else{//把生成的模块条件去掉
				queryInfo.setHqlCondiStr(queryInfo.getHqlCondiStr().replace("b.moduleId=:moduleId", "1=1"));
			}
			//下面的操作必须在上面两个if后面
			queryInfo.setParaValueStr(queryInfo.praValues2Str());
			queryInfo.setHqlCondiStr(queryInfo.getHqlCondiStr().substring(queryInfo.getHqlCondiStr().indexOf("where")));
			queryInfo.setQueryName(dto.getQueryName());
			bugManagerService.add(queryInfo);
		}
		tdMap = null;
		dto.setTypeList(null);
		typeList=null;
		StringBuffer sb = new StringBuffer();
		dto.toJson2((List)bugs, sb);
		if("true".equalsIgnoreCase(dto.getIsAjax())){
			if(dto.getSaveQuery()==1){
				super.writeResult(dto.getQueryInfo().getQueryId() +"$"+sb.toString());
			}else{
				super.writeResult(sb.toString());
			}
			return super.globalAjax();
		}
		dto.setCountStr((impExpManagerService.getBugCountStr() +" (状态为：撤销,无效 ,重复 ,非错 ,关闭/撤销 ,修正/描述不当的BUG不算有效BUG)"));
		dto.setListStr(sb.toString());
		return super.getView("loadAllMyBug");
	}
	
	public void setRelaUser(List<BugBaseInfo> bugs){
		Map<String,User> userMap= bugCommonService.getRelaUserWithName(bugs, "testOwnerId","devOwnerId");
		User own = null;
		for(BugBaseInfo bug :bugs){
			own = userMap.get(bug.getTestOwnerId());
			if(own==null){
				continue;
			}
			bug.settestName(own.getUniqueName());
			if(bug.getDevOwnerId()!=null&&!"".equals(bug.getDevOwnerId())){
				own = userMap.get(bug.getDevOwnerId());
				bug.setDevName(own.getUniqueName());					
			}
			own = null;
		}
		own= null;
		userMap = null;
	}
	
	public void setRelaTaskName(List<BugBaseInfo> bugs){
		Map<String,ListObject> taskMap= bugCommonService.getRelaTestTasks(bugs, "taskId");
		ListObject lstObj = null;
		for(BugBaseInfo bug :bugs){
			lstObj = taskMap.get(bug.getTaskId());
			if(lstObj==null){
				continue;
			}
			bug.setTaskName(lstObj.getValueObj());
		}
		lstObj= null;
		taskMap = null;
	}
	
	
	public View loadTree(BusiRequestEvent req) {
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
//		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		String id = ServletActionContext.getRequest().getParameter("id");
		if(!StringUtils.isNullOrEmpty(id)){
			dto.setCurrNodeId(Long.valueOf(id));
		}
//		String taskId = SecurityContextHolderHelp.getCurrTaksId();
//		dto.setTaskId(taskId);
		String taskId = dto.getTaskId();
		if(taskId==null || taskId==""){
			if(dto.getIsAjax() != null){
				super.writeResult("0,1,无数据,0.1");
				dto = null;
				return super.globalAjax();
			}
			SecurityContextHolder.getContext().setAttr("nodeDataStr", "0,1,无数据,0.1");
			return super.getView();
		}
//		String nodeDataStr = this.toTreeStr(outLineService.loadNormalNode(taskId, dto.getCurrNodeId()));
		List<TreeJsonVo> treeJsonVos = this.toTreeJson(outLineService.loadNormalNode(taskId, dto.getCurrNodeId()),dto.getCurrNodeId());
		if (dto.getIsAjax() == null) {
//			SecurityContextHolder.getContext().setAttr("nodeDataStr", nodeDataStr);
			return super.getView();
		}
//		super.writeResult(nodeDataStr);
		super.writeResult(JsonUtil.toJson(treeJsonVos));
//		dto = null;
		return super.globalAjax();
	}
	public View loadReportTree(BusiRequestEvent req) {
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		String taskId = dto.getTaskId();
		dto.setTaskId(taskId);
		if(taskId==null){
			if(dto.getIsAjax() != null){
				super.writeResult("0,1,无数据,0.1");
				dto = null;
				return super.globalAjax();
			}
			SecurityContextHolder.getContext().setAttr("nodeDataStr", "0,1,无数据,0.1");
			return super.getView();
		}
//		String nodeDataStr = this.toTreeStr(outLineService.loadNormalNode(taskId, dto.getCurrNodeId()));
//		if (dto.getIsAjax() == null) {
//			SecurityContextHolder.getContext().setAttr("nodeDataStr", nodeDataStr);
//			return super.getView();
//		}
//		super.writeResult(nodeDataStr);
//		dto = null;
//		return super.globalAjax();
//		String nodeDataStr = this.toTreeStr(outLineService.loadNormalNode(taskId, dto.getCurrNodeId()));
		List<TreeJsonVo> treeJsonVos = this.toTreeJson(outLineService.loadNormalNode(taskId, dto.getCurrNodeId()),dto.getCurrNodeId());
		if (dto.getIsAjax() == null) {
//			SecurityContextHolder.getContext().setAttr("nodeDataStr", nodeDataStr);
			return super.getView();
		}
//		super.writeResult(nodeDataStr);
		super.writeResult(JsonUtil.toJson(treeJsonVos));
//		dto = null;
		return super.globalAjax();
	}
	private List getModulesByDevId(String devId,String taskId){
		StringBuffer hql = new StringBuffer("select new OutlineTeamMember(moduleMemberId,moduleId) ");
		hql.append("from OutlineTeamMember otm join  otm.outline ou where ou.isleafNode=1 and otm.taskId=?");
		List<OutlineTeamMember> list = outLineService.findByHql(hql.toString(), taskId);
		List<Long> moduleList = new ArrayList<Long>();
		if(list!=null &&list.size()>0){
			for(OutlineTeamMember otm :list){
				moduleList.add(otm.getModuleId());
			}
			return moduleList;
		}
		return null;
	}
	private String queryList2Json(List<BugQueryInfo> queryList){
		
		if(queryList.size()==0||queryList==null){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		int count=1;
		for(BugQueryInfo query :queryList){
			sb.append(query.getQueryId());
			sb.append(";");
			if(query.getTaskId()==null){
				if(query.getOnlyMe()==1){
					sb.append(query.getQueryName()).append("[与我有关--跨任务]");
				}else{
					sb.append(query.getQueryName()).append("[跨任务]");
				}
			}else{
				if(query.getOnlyMe()==1){
					sb.append(query.getQueryName()).append("[与我有关]");
				}else{
					sb.append(query.getQueryName());
				}
			}
			if(count != queryList.size()){
				sb.append("$");
			}
			count++;
		}
		return sb.toString();
	}
	private String toTreeStr(List<OutlineInfo> list) {
		StringBuffer sb = new StringBuffer();
		for (OutlineInfo outLine : list) {
			sb.append(";").append(outLine.getSuperModuleId());
			sb.append(",").append(outLine.getModuleId());
			sb.append(",").append(outLine.getModuleName());
			sb.append(",").append(outLine.getIsleafNode());
			sb.append(",").append(outLine.getModuleState());
		}
		return sb.length() > 2 ? sb.substring(1).toString() : "";
	}
	
	private List<TreeJsonVo> toTreeJson(List<OutlineInfo> list,Long currNodeId) {
//		Map<String, OutlineInfo> map = new HashMap<String, OutlineInfo>();
		List<TreeJsonVo> treeJsonVos = new ArrayList<TreeJsonVo>();
		if(currNodeId==null){
			TreeJsonVo treeJsonVo = new TreeJsonVo();
			for (OutlineInfo outLine : list) {
				if(outLine.getSuperModuleId()==0){
					treeJsonVo.setId(outLine.getModuleId().toString());
					treeJsonVo.setText(outLine.getModuleName());
//					treeJsonVo.setState(this.formatterStr(outLine.getModuleState()));
//					treeJsonVo.setState(this.formatterStr(outLine.getIsleafNode()));
					treeJsonVo.setState("open");
					treeJsonVo.setRootNode(true);
					treeJsonVo.setLeaf(this.formatterInt(outLine.getIsleafNode()));
				}else{
					TreeJsonVo treeJsonVoC = new TreeJsonVo();
					treeJsonVoC.setId(outLine.getModuleId().toString());
					treeJsonVoC.setText(outLine.getModuleName());
					treeJsonVoC.setState(this.formatterStr(outLine.getIsleafNode()));
					treeJsonVoC.setLeaf(this.formatterInt(outLine.getIsleafNode()));
					treeJsonVoC.setRootNode(false);
					treeJsonVo.getChildren().add(treeJsonVoC);
				}
			}
			treeJsonVos.add(treeJsonVo);
		}else{
			for (OutlineInfo outLine : list) {
				TreeJsonVo treeJsonVoC = new TreeJsonVo();
				treeJsonVoC.setId(outLine.getModuleId().toString());
				treeJsonVoC.setText(outLine.getModuleName());
				treeJsonVoC.setState(this.formatterStr(outLine.getIsleafNode()));
				treeJsonVoC.setLeaf(this.formatterInt(outLine.getIsleafNode()));
				treeJsonVoC.setRootNode(false);
				treeJsonVos.add(treeJsonVoC);
			}
		}
		
		return treeJsonVos;
	}
	
	public String formatterStr(Integer value) {
		if(value==0){
			return "closed";
		}else{
			return "open";
		}
	}
	
	public Boolean formatterInt(Integer value) {
		if(value==0){
			return false;
		}else{
			return true;
		}
	}
	
	//如果任务流程正处于改动中，先等待，最多只等3秒
	private void taskLockHand(){
		int i =1;
		if(TestTaskDetailBlh.isLock()){
			while(TestTaskDetailBlh.isLock()){
				if(i>3){
					return;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					logger.error(e);
				}
				i++;
			}
		}else{
			return;
		}
	}
	
	public BugManagerService getBugManagerService() {
		return bugManagerService;
	}

	public void setBugManagerService(BugManagerService bugManagerService) {
		this.bugManagerService = bugManagerService;
	}

	public OutLineManagerService getOutLineService() {
		return outLineService;
	}

	public void setOutLineService(OutLineManagerService outLineService) {
		this.outLineService = outLineService;
	}
	public TestTaskDetailService getTestTaskService() {
		return testTaskService;
	}
	public void setTestTaskService(TestTaskDetailService testTaskService) {
		this.testTaskService = testTaskService;
	}
	public DrawHtmlListDateService getDrawHtmlListDateService() {
		return drawHtmlListDateService;
	}
	public void setDrawHtmlListDateService(
			DrawHtmlListDateService drawHtmlListDateService) {
		this.drawHtmlListDateService = drawHtmlListDateService;
	}

	public BugCommonService getBugCommonService() {
		return bugCommonService;
	}

	public void setBugCommonService(BugCommonService bugCommonService) {
		this.bugCommonService = bugCommonService;
	}
	
	public FileInfoService getFileInfoService() {
		return fileInfoService;
	}
	
	public void setFileInfoService(FileInfoService fileInfoService) {
		this.fileInfoService = fileInfoService;
	}

	public BugFlowControlService getBugFlowControlService() {
		return bugFlowControlService;
	}

	public void setBugFlowControlService(BugFlowControlService bugFlowControlService) {
		this.bugFlowControlService = bugFlowControlService;
	}

	public BugFlowControlBlh getBugFlowControlBlh() {
		return bugFlowControlBlh;
	}

	public void setBugFlowControlBlh(BugFlowControlBlh bugFlowControlBlh) {
		this.bugFlowControlBlh = bugFlowControlBlh;
	}
	
	public ImpExpManagerService getImpExpManagerService() {
		return impExpManagerService;
	}

	public void setImpExpManagerService(ImpExpManagerService impExpManagerService) {
		this.impExpManagerService = impExpManagerService;
	}
	
	public View bugEdit(BusiRequestEvent req) {
		return super.getView();
	}
	public View bugAdd(BusiRequestEvent req) {
		return super.getView();
	}
	public View bugHandView(BusiRequestEvent req) {
		return super.getView();
	}
	public View bugFind(BusiRequestEvent req) {
		return super.getView();
	}
	public View bugDetail(BusiRequestEvent req) {
		return super.getView();
	}
	public View addExampleInfoPage(BusiRequestEvent req) {
		return super.getView();
	}
	
	

	public View getFileInfoByTypeId(BusiRequestEvent req){
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		if(dto.getFileInfo()!=null){
			FileInfoDto fileInfoDto = new FileInfoDto();
			fileInfoDto.setFileInfo(dto.getFileInfo());
			List<FileInfo> fileInfos = fileInfoService.getFileInfoByTypeId(fileInfoDto);
			super.writeResult(JsonUtil.toJson(fileInfos));
			
		}
		return super.globalAjax();
	}
	
}
