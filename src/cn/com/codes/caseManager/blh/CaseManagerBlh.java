package cn.com.codes.caseManager.blh;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.opensymphony.webwork.ServletActionContext;

import cn.com.codes.bugManager.dto.BoardVo;
import cn.com.codes.bugManager.service.BugCommonService;
import cn.com.codes.caseManager.dto.CaseCountVo;
import cn.com.codes.caseManager.dto.CaseManagerDto;
import cn.com.codes.caseManager.dto.TestCaseVo;
import cn.com.codes.caseManager.dto.TreeJsonVo;
import cn.com.codes.caseManager.service.CaseManagerService;
import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.dto.PageModel;
import cn.com.codes.common.service.DrawHtmlListDateService;
import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.common.HtmlListComponent;
import cn.com.codes.framework.common.JsonUtil;
import cn.com.codes.framework.common.ListObject;
import cn.com.codes.framework.exception.DataBaseException;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.transmission.JsonInterface;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.impExpManager.service.ImpExpManagerService;
import cn.com.codes.object.CaseExeHistory;
import cn.com.codes.object.OutlineInfo;
import cn.com.codes.object.SoftwareVersion;
import cn.com.codes.object.TestCaseInfo;
import cn.com.codes.object.TestResult;
import cn.com.codes.object.TestTaskDetail;
import cn.com.codes.object.TypeDefine;
import cn.com.codes.object.User;
import cn.com.codes.outlineManager.service.OutLineManagerService;
import cn.com.codes.testTaskManager.service.TestTaskDetailService;

public class CaseManagerBlh extends BusinessBlh {

	private CaseManagerService caseService;
	private TestTaskDetailService testTaskService ;
	private OutLineManagerService outLineService;
	private DrawHtmlListDateService drawHtmlListDateService;
	private ImpExpManagerService impExpManagerService;
	private BugCommonService bugCommonService;
	
	public View pasteCase(BusiRequestEvent req){
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
		String command = dto.getCommand();
		if(!"cp".equals(command)&&!"ct".equals(command)){
			return super.globalAjax();
		}
		String remark = dto.getRemark();
		if(remark==null||"".equals(remark.trim())){
			super.writeResult(JsonUtil.toJson("noCaseSel"));
			return super.globalAjax();
		}
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		String hql = "select new OutlineInfo(moduleNum,moduleId) from OutlineInfo where taskId=? and moduleId = ? and moduleState!=1 ";
		List<OutlineInfo> nodeList = outLineService.findByHql(hql, taskId,dto.getCurrNodeId());
		OutlineInfo currNode = nodeList.get(0);
		if(nodeList==null||nodeList.isEmpty()){
			super.writeResult(JsonUtil.toJson("nodeHaveDel"));
			return super.globalAjax();
		}
		
		String[] idStrArr = remark.split("_");
		List<Long> idList = new ArrayList<Long>();
		for(String id :idStrArr){
			if(!"".equals(id)&&id!=null){
				idList.add(new Long(id));
			}
		}
		//hql = "select new TestCaseInfo(testCaseId, testCaseDes,weight, attachUrl, testData, expResult, operDataRichText,caseTypeId, priId) from TestCaseInfo where taskId=:taskId and testCaseId in(:ids) ";
		hql = " from TestCaseInfo where taskId=:taskId and testCaseId in(:ids) ";
		Map praValuesMap = new HashMap(2);
		praValuesMap.put("taskId", taskId);
		praValuesMap.put("ids", idList);
		List<TestCaseInfo> initCaseList = caseService.findByHqlWithValuesMap(hql, praValuesMap, false);
		if(initCaseList==null ||initCaseList.isEmpty()){
			super.writeResult(JsonUtil.toJson("selCaseDel"));
			return super.globalAjax();			
		}
		this.copyCasePrepare(dto, initCaseList, currNode);
		if("cp".equals(command)){
			caseService.copyCase(dto);
			dto.setAttr("delFlg", "cp");
		}else{
			this.setCanDelCaseId(dto, idList);
			caseService.pasteCase(dto);
		}
//		initCaseList = null;
//		
//		idList = null;
//		dto.setTaskId(taskId);
//		List caseList = caseService.loadCase(dto,currNode.getModuleNum());
//		currNode = null;
//		if(caseList.size()!=0){
//			this.setRelaUser(caseList);
//			this.setRelaTaskName(caseList);
//			this.setRelaType(caseList);
//		}
//		dto.setHql(null);
//		dto.setHqlParamMaps(null);
//		StringBuffer sb = new StringBuffer();
//		this.caseListToJson(caseList, sb);
//		super.writeResult(dto.getAttr("delFlg")+"$"+sb.toString());
		super.writeResult(JsonUtil.toJson("success"));
		return super.globalAjax();
	}
	
	
	private void setCanDelCaseId(CaseManagerDto dto,List<Long> idList ){
		String hql = "select new TestResult(resultId,testCaseId) from TestResult where testCaseId in(:ids)" ;
		Map praValuesMap = new HashMap(1);
		praValuesMap.put("ids", idList);
		List<TestResult> restList = caseService.findByHqlWithValuesMap(hql, praValuesMap, false);
		if(restList==null||restList.isEmpty()){
			dto.setAttr("delCaseIds", idList);
			dto.setAttr("delFlg", "delA");
		}else{
			List<Long> delIds = new ArrayList<Long>();
			for(Long id :idList){
				boolean canDel = true;
				for(TestResult tr :restList){
					if(id.intValue()==tr.getTestCaseId().intValue()){
						canDel = false;
						break;
					}	
				}
				if(canDel){
					delIds.add(id);
				}
			}
			if(delIds.isEmpty()){
				dto.setAttr("delCaseIds", null);
				dto.setAttr("delFlg", "noDelA");
			}else{
				dto.setAttr("delCaseIds", delIds);
				dto.setAttr("delFlg", "noDelP");
			}
		}
	}
	private void copyCasePrepare(CaseManagerDto dto,List<TestCaseInfo> initCaseList,OutlineInfo currNode ){
		List<TestCaseInfo> addCaseList = new ArrayList<TestCaseInfo>(initCaseList.size());
		List<CaseExeHistory> exeHistoryList = new ArrayList<CaseExeHistory>(initCaseList.size());
		String myId = SecurityContextHolderHelp.getUserId();
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		int testStatus = 1;
		if(!this.isReview(taskId)){
			testStatus =1;
		}else{
			testStatus =0;
		}
		for(TestCaseInfo tc :initCaseList){
			TestCaseInfo newTc = new TestCaseInfo();
			newTc.setCreatdate(new Date());
			newTc.setUpddate(newTc.getCreatdate());
			newTc.setCreaterId(myId);
			newTc.setModuleId(currNode.getModuleId());
			newTc.setModuleNum(currNode.getModuleNum());
			newTc.setCaseTypeId(tc.getCaseTypeId());
			newTc.setPriId(tc.getPriId());
			newTc.setPrefixCondition(tc.getPrefixCondition());
			newTc.setTestCaseDes(tc.getTestCaseDes());
			newTc.setTestData(tc.getTestData());
			newTc.setOperDataRichText(tc.getOperDataRichText());
			newTc.setExpResult(tc.getExpResult());
			newTc.setAttachUrl(tc.getAttachUrl());
			newTc.setTaskId(taskId);
			newTc.setIsReleased(0);
			newTc.setTestStatus(testStatus);
			addCaseList.add(newTc);
			
			CaseExeHistory his = new CaseExeHistory();
			his.setTaskId(taskId);
			his.setOperaType(1);
			//his.setTestCaseId(testCase.getTestCaseId());
			his.setModuleId(currNode.getModuleId());
			his.setExeDate(newTc.getUpddate());
			
			his.setTestActor(myId);
			exeHistoryList.add(his);
		}
		dto.setAttr("addCaseList", addCaseList);
		dto.setAttr("exeHistoryList", exeHistoryList);
	}
	public View loadNodedetalData(BusiRequestEvent req){
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
		String[] idsArr =  dto.getCountStr().split("_");
		if(idsArr==null||idsArr.length==0){
			super.writeResult("");
			return super.globalAjax();
		}
		
		List<Long> nodeIds = new ArrayList<Long>();
		for(String id :idsArr){
			if(!"".equals(id)&&id!=null){
				nodeIds.add(new Long(id));
			}
		}
		if(nodeIds.isEmpty()){
			super.writeResult("");
			return super.globalAjax();
		}
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		if(taskId==null){
			super.writeResult("");
			return super.globalAjax();
		}
		boolean onlyNormal = "onlyNormal".equals(dto.getRemark())?true:false;
		List<OutlineInfo>  loadNodes  = impExpManagerService.getOutLineInfo(taskId, nodeIds, onlyNormal);
		if(loadNodes==null||loadNodes.isEmpty()){
			super.writeResult("");
			loadNodes = null;
			return super.globalAjax();
		}
		List resultSet = impExpManagerService.getOutLineDetailInfo(taskId, loadNodes, onlyNormal);
		if(resultSet==null||resultSet.isEmpty()){
			for(OutlineInfo node :loadNodes){
				//这里用来存用例数
				node.setCaseCount(0);
				//这里用来存BUG数
				node.setScrpCount(0);
			}
			StringBuffer sb = new StringBuffer();
			for (OutlineInfo outLine : loadNodes) {
				sb.append(";").append(outLine.getModuleId());
				sb.append(",").append(outLine.getCaseCount());
				sb.append(",").append(outLine.getScrpCount());
			}
			String ajaxRest = sb.length() > 2 ? sb.substring(1).toString() : "";
			super.writeResult(ajaxRest);
			loadNodes = null;
			return super.globalAjax();
		}
		
		for(OutlineInfo node :loadNodes){
			//这里用来存用例数
			node.setCaseCount(0);
			//这里用来存BUG数
			node.setScrpCount(0);
			Iterator it = resultSet.iterator();
			while (it.hasNext()) {
				Object values[] = (Object[]) it.next();

				if(node.getModuleId().toString().equals(values[0].toString())||(node.getModuleNum()!=null&&values[1]!=null&&values[1].toString().startsWith(node.getModuleNum()))){
					node.setCaseCount(node.getCaseCount()+Integer.parseInt(values[2].toString()));
					node.setScrpCount(node.getScrpCount()+Integer.parseInt(values[3].toString()));
				}else if(node.getModuleNum()==null){
					node.setCaseCount(node.getCaseCount()+Integer.parseInt(values[2].toString()));
					node.setScrpCount(node.getScrpCount()+Integer.parseInt(values[3].toString()));
				}
				
			}
			it = null;
		}
//		StringBuffer sb = new StringBuffer();
//		for (OutlineInfo outLine : loadNodes) {
//			sb.append(";").append(outLine.getModuleId());
//			sb.append(",").append(outLine.getCaseCount());
//			sb.append(",").append(outLine.getScrpCount());
//		}
//		String ajaxRest = sb.length() > 2 ? sb.substring(1).toString() : "";
//		super.writeResult(ajaxRest);
		List<CaseCountVo> caseCountVos = this.formattList(loadNodes);
		super.writeResult(JsonUtil.toJson(caseCountVos));
		resultSet = null;
		loadNodes = null;
		return super.globalAjax();
	}
	
	public List<CaseCountVo> formattList(List<OutlineInfo>  loadNodes) {
		List<CaseCountVo> caseCountVos = new ArrayList<CaseCountVo>();
		for (OutlineInfo outLine : loadNodes) {
			CaseCountVo caseCountVo = new CaseCountVo();
			caseCountVo.setModuleId(outLine.getModuleId());
			caseCountVo.setCaseCount(outLine.getCaseCount());
			caseCountVo.setScrpCount(outLine.getScrpCount());
			caseCountVos.add(caseCountVo);
		}
		return caseCountVos;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public View loadCaseBoard(BusiRequestEvent req){
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
		List<Object[]> caseCountList = caseService.loadCaseBoard(taskId);
		List<BoardVo> resultList = new ArrayList();
		if(caseCountList!=null&&!caseCountList.isEmpty()){
			for(Object[] objs :caseCountList){      
				BoardVo vo = new BoardVo();
				vo.setUserName(objs[0].toString());
				vo.setWhCount(objs[1]==null?0:Integer.parseInt(objs[1].toString()));
				vo.setHCount(objs[2]==null?0:Integer.parseInt(objs[2].toString()));
				vo.setBwhCount(objs[3]==null?0:Integer.parseInt(objs[3].toString()));
				vo.setBhCount(objs[4]==null?0:Integer.parseInt(objs[4].toString()));
				vo.setLoginName(objs[5].toString());
				vo.setTeamActor("测试");
				resultList.add(vo);
			}

		}
		
		List<Object[]> bugCountList = null;
		//List<BoardVo> resultList2 = new ArrayList();
		bugCountList = bugCommonService.loadBugBoard(taskId);
		if(bugCountList!=null&&!bugCountList.isEmpty()){
			//bwhCount  待处理bug数
			//hcount   原始结果信中今日处理BUG次数，VO中要设置到 bhcount表示今日处理BUG次数， 以当日处理用列数区分区，因为新版这两个面版合一起了
			for(Object[] objs :bugCountList){ 
				BoardVo vo = new BoardVo();
				vo.setUserName(objs[0].toString());
				vo.setWhCount(0);
				vo.setHCount(0);
				vo.setBwhCount(objs[1]==null?0:Integer.parseInt(objs[1].toString()));
				vo.setBhCount(objs[2]==null?0:Integer.parseInt(objs[2].toString()));
				vo.setLoginName(objs[3].toString());
				vo.setTeamActor("研发");
				boolean flag = false;
				for(BoardVo voTemp :resultList) {
					if(voTemp.getUserName().equals(vo.getUserName())) {
						flag = true;
						voTemp.setBwhCount(vo.getBwhCount());
						voTemp.setBhCount(vo.getBhCount());
						break;
					}
				}
				if(!flag) {
					resultList.add(vo);
				}
				
			}
		}
		List<Map<String,Object>> bugCountListCurrNotInPro = bugCommonService.loadBugBoardCurrNotInPro(taskId);
		if(bugCountListCurrNotInPro!=null&&!bugCountListCurrNotInPro.isEmpty()) {
			for(Map<String,Object> objs :bugCountListCurrNotInPro){ 
				BoardVo vo = new BoardVo();
				vo.setUserName(objs.get("owner").toString());
				vo.setWhCount(0);
				vo.setHCount(0);
				vo.setBwhCount(objs.get("whCount")==null?0:Integer.parseInt(objs.get("whCount").toString()));
				vo.setBhCount(0);
				vo.setLoginName(objs.get("loginname").toString());
				vo.setTeamActor("当前未参与项目");
				resultList.add(vo);
			}
		}
		super.writeResult(JsonUtil.toJson(resultList));
		return super.globalAjax();
	}
	public void boardtoJson(List<BoardVo> list,StringBuffer sb){
		int i =0 ;
		if(list != null && !list.isEmpty()){
			sb.append("{rows: [");
			for(BoardVo obj:list){
				i++ ;
				if(i != list.size()){
					obj.toString2(sb);
					sb.append(",");
				}else{
					obj.toString2(sb);
				}
			}
			sb.append("]}");				
		}		
	    Object pageInfo = SecurityContextHolder.getContext().getAttr("pageInfo");
	    if(pageInfo != null){
	    	 sb.insert(0, pageInfo.toString()).toString();
	    }
	}
	public View quickQuery(BusiRequestEvent req){
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
		TestCaseInfo testCase = caseService.quickQueryCase(dto.getTestCaseInfo().getTestCaseId(),dto.getTestCaseInfo().getModuleId(),dto.getTestCaseInfo().getTaskId());
		if(testCase==null){
			super.writeResult(JsonUtil.toJson(""));
		}else{
			SecurityContextHolderHelp.setCurrTaksId(testCase.getTaskId());
			List<TestCaseInfo> caseList = new ArrayList<TestCaseInfo>(1);
			caseList.add(testCase);
			this.setRelaUser(caseList);
			this.setRelaTaskName(caseList);
			this.setRelaType(caseList);
//			StringBuffer sb = new StringBuffer();
//			this.caseListToJson(caseList, sb);
//			super.writeResult(sb.toString());
			PageModel pg = new PageModel();
			pg.setRows(caseList);
//			Integer total =  (Integer) SecurityContextHolder.getContext().getAttr("pageInfoTotalRows");
			pg.setTotal(1);
			super.writeResult(JsonUtil.toJson(pg));
		}
		return super.globalAjax();
	}
	public View batchAuditInit(BusiRequestEvent req){
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
		Long currNodeId = dto.getCurrNodeId();
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		dto.setTaskId(taskId);
		StringBuffer hql = new StringBuffer();
		OutlineInfo outLine = null;
		if(currNodeId==null){
			String taskHql = " select new TestTaskDetail(outlineState,testPhase,currentVersion,testSeq,reltCaseFlag) from TestTaskDetail where taskId=? and companyId=?";
			List<TestTaskDetail> taskList = caseService.findByHql(taskHql, taskId,SecurityContextHolderHelp.getCompanyId());
			if(taskList==null||taskList.isEmpty()){
				dto.setListStr("1/10/0$");
				dto.setOutLineState(0);
				return super.getView();
			}
			TestTaskDetail taskDetal = taskList.get(0);
			dto.setOutLineState(taskDetal.getOutlineState());
			hql.append("select new OutlineInfo(moduleNum,moduleId) from OutlineInfo where superModuleId =0 and taskId=?");
			List<OutlineInfo> list = caseService.findByHql(hql.toString(), taskId);
			outLine = list.get(0);
		}else{
			hql.append("select new OutlineInfo(moduleNum,moduleId) from OutlineInfo where moduleId=? and taskId=?");
			List<OutlineInfo> list =caseService.findByHql(hql.toString(), currNodeId,taskId);
			outLine = list.get(0);
		}
		dto.setCurrNodeId(outLine.getModuleId());
		List caseList = caseService.loadAuditCase(dto,outLine.getModuleNum());
		if(caseList.size()!=0){
			this.setRelaUser(caseList);
			this.setRelaTaskName(caseList);
			this.setRelaType(caseList);
		}
		dto.setHql(null);
		dto.setHqlParamMaps(null);
		StringBuffer sb = new StringBuffer();
		this.caseListToJson(caseList, sb);
		if("true".equals(dto.getIsAjax())){
//			super.writeResult(sb.toString());
			super.writeResult(JsonUtil.toJson(caseList));
			return super.globalAjax();
		}
		dto.setCountStr(impExpManagerService.getCaseCountStr(taskId));
		dto.setListStr(sb.toString());
		return super.getView();
	}
	public View batchAuditSub(BusiRequestEvent req){
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		dto.setTaskId(taskId);
		caseService.batchAudit(dto);
		//Long currNodeId = dto.getCurrNodeId();
		
		dto.setPageNo(1);
		return this.batchAuditInit(req);
	}
	

	public View viewCaseHistory(BusiRequestEvent req){
		
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
		dto.setTaskId(taskId);
		List<CaseExeHistory> hisList = caseService.loadHistory(dto);
		List exeRecordList = hisList;
		this.setHisRealActor(hisList);
		this.setHisRealVer(hisList);
		StringBuffer sb = new StringBuffer();
		dto.toJson2(exeRecordList, sb);
		if(dto.getIsAjax()!=null){
//			super.writeResult(sb.toString());
			super.writeResult(JsonUtil.toJson(exeRecordList));
			return super.globalAjax();
		}
		dto.setListStr(sb.toString());
		return super.getView();
	}
	
	
	public View quickQueryLastExeCase(BusiRequestEvent req){
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
		TestCaseVo vo = caseService.quickQueryLastExeCase(dto.getTestCaseInfo().getTestCaseId());
		if(vo==null){
			super.writeResult("");
		}else{
			SecurityContextHolderHelp.setCurrTaksId(vo.getTaskId());
			List<TestCaseVo> exeList  = new ArrayList<TestCaseVo>(1);
			exeList.add(vo);
			List exeRecordList = exeList;
			this.setRealActor((List<TestCaseVo>)exeRecordList);
			this.setRealVer((List<TestCaseVo>)exeRecordList);
			this.setHisRelaTaskName(exeList);
			StringBuffer sb = new StringBuffer();
			dto.toJson2(exeRecordList, sb);
//			super.writeResult(sb.toString());
			super.writeResult(JsonUtil.toJson(exeRecordList));
			
		}
		return super.globalAjax();
	}	
	
	public View loadTree(BusiRequestEvent req) {
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
		String id = ServletActionContext.getRequest().getParameter("id");
		if(!StringUtils.isNullOrEmpty(id)){
			dto.setCurrNodeId(Long.valueOf(id));
		}
//		String taskId = SecurityContextHolderHelp.getCurrTaksId();
//		dto.setTaskId(taskId);
		String taskId = dto.getTaskId();
		if(taskId==null||taskId==""){
			if(dto.getIsAjax() != null){
				super.writeResult("0,1,无数据,0,1");
				dto = null;
				return super.globalAjax();
			}
			SecurityContextHolder.getContext().setAttr("nodeDataStr", "0,1,无数据,0,1");
			return super.getView();
		}
//		String nodeDataStr = this.toTreeStr(outLineService.loadNormalNode(taskId, dto.getCurrNodeId()));
		List<TreeJsonVo> treeJsonVos = this.toTreeJson(outLineService.loadNormalNode(taskId, dto.getCurrNodeId()),dto.getCurrNodeId());
		if (dto.getIsAjax() == null) {
//			SecurityContextHolder.getContext().setAttr("nodeDataStr", nodeDataStr);
			return super.getView();
		}
//		super.writeResult(JsonUtil.toJson(nodeDataStr));
		super.writeResult(JsonUtil.toJson(treeJsonVos));
//		dto = null;
		return super.globalAjax();
	}

	public View index(BusiRequestEvent req){
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
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
	public View getCaseStatInfo(BusiRequestEvent req){
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
		Long currNodeId = dto.getCurrNodeId();
		String hql = "select new OutlineInfo(moduleNum,moduleId) from OutlineInfo where moduleId=? and taskId=?";
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		List<OutlineInfo> list =caseService.findByHql(hql, currNodeId,taskId);
		if(list==null||list.isEmpty()){
			super.writeResult(JsonUtil.toJson("测试需求不存在"));
			return super.globalAjax();
		}
		OutlineInfo outLine = list.get(0);
		if(outLine.getModuleNum()!=null){
			super.writeResult(JsonUtil.toJson(impExpManagerService.getCaseCountStr(taskId,outLine.getModuleNum())));
		}else{
			super.writeResult(JsonUtil.toJson(impExpManagerService.getCaseCountStr(taskId)));
		}
		return super.globalAjax();
	}
	
	public View  loadCase(BusiRequestEvent req){
		return super.getView();
	}
	
	
	public View  loadCaseList(BusiRequestEvent req){
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
		Long currNodeId = dto.getCurrNodeId();
		String taskId = dto.getTaskId();
		if(taskId==null){
			taskId = SecurityContextHolderHelp.getCurrTaksId();
			dto.setTaskId(taskId);
		}else{
			String taskHql = " select new TestTaskDetail(outlineState,testPhase,currentVersion,testSeq,reltCaseFlag) from TestTaskDetail where taskId=? and companyId=?";
			List<TestTaskDetail> taskList = caseService.findByHql(taskHql, taskId,SecurityContextHolderHelp.getCompanyId());
			if(taskList==null||taskList.size()==0){
				if(dto.getIsAjax()!=null){
					super.writeResult(JsonUtil.toJson("1/10/0$"));
					return super.globalAjax();
				}
				dto.setOutLineState(0);
				dto.setListStr("1/10/0$");
				super.writeResult(JsonUtil.toJson(dto));
				return super.globalAjax();
			}
			SecurityContextHolderHelp.setCurrTaksId(taskId);
		}
		dto.setIsReview(0);
		StringBuffer hql = new StringBuffer();
		OutlineInfo outLine = null;
		if(taskId==null){
			dto.setListStr("1/10/0$");
			dto.setOutLineState(0);
			super.writeResult(JsonUtil.toJson(dto));
			return super.globalAjax();
		}

		if(currNodeId==null){
			String taskHql = " select new TestTaskDetail(outlineState,testPhase,currentVersion,testSeq,reltCaseFlag) from TestTaskDetail where taskId=? and companyId=?";
			List<TestTaskDetail> taskList = caseService.findByHql(taskHql, taskId,SecurityContextHolderHelp.getCompanyId());
			if(taskList==null||taskList.isEmpty()){
				dto.setListStr("1/10/0$");
				dto.setOutLineState(0);
				super.writeResult(JsonUtil.toJson(dto));
				return super.globalAjax();
			}
			TestTaskDetail taskDetal = taskList.get(0);
			dto.setOutLineState(taskDetal.getOutlineState());
			hql.append("select new OutlineInfo(moduleNum,moduleId) from OutlineInfo where superModuleId =0 and taskId=?");
			List<OutlineInfo> list = caseService.findByHql(hql.toString(), taskId);
			if(list==null||list.isEmpty()){
				dto.setListStr("1/10/0$");
				super.writeResult(JsonUtil.toJson(dto));
				return super.globalAjax();
			}
			outLine = list.get(0);
		}else{
			hql.append("select new OutlineInfo(moduleNum,moduleId) from OutlineInfo where moduleId=? and taskId=?");
			List<OutlineInfo> list =caseService.findByHql(hql.toString(), currNodeId,taskId);
			if(list==null||list.isEmpty()){
				dto.setListStr("1/10/0$");
				super.writeResult(JsonUtil.toJson(dto));
				return super.globalAjax();
			}
			outLine = list.get(0);
		}
		dto.setCurrNodeId(outLine.getModuleId());
		List caseList = caseService.loadCase(dto,outLine.getModuleNum());
		if(caseList.size()!=0){
			this.setRelaUser(caseList);
			this.setRelaTaskName(caseList);
			this.setRelaType(caseList);
		}
		dto.setHql(null);
		dto.setHqlParamMaps(null);
		StringBuffer sb = new StringBuffer();
//		this.caseListToJson(caseList, sb);
		if("true".equals(dto.getIsAjax())){
			PageModel pg = new PageModel();
			pg.setRows(caseList);
			Integer total =  (Integer) SecurityContextHolder.getContext().getAttr("pageInfoTotalRows");
			pg.setTotal(total);
			super.writeResult(JsonUtil.toJson(pg));
			return super.globalAjax();
//			super.writeResult(sb.toString());
//			return super.globalAjax();
		}
		//设置用户能否进行用例审核权限
		if((!"true".equals(dto.getIsAjax()))&&dto.getCanReview()==1&&this.isReview(taskId)){
			dto.setIsReview(1);
		}
		dto.setCountStr(impExpManagerService.getCaseCountStr(taskId));
		dto.setListStr(sb.toString());
//		return super.getView("loadCase");
		super.writeResult(JsonUtil.toJson(dto));
		return super.globalAjax();
	}
	
	private void setRelaType(List<TestCaseInfo> caseList){
		 Map<String,TypeDefine> typeMap= caseService.getRelaTypeDefine(caseList, "priId","caseTypeId");
		 if(typeMap.isEmpty()){
			 return;
		 }
		 for(TestCaseInfo testCase:caseList ){
			 if(typeMap.get(testCase.getCaseTypeId().toString())!=null){
				 testCase.setTypeName(typeMap.get(testCase.getCaseTypeId().toString()).getTypeName());
			 }
			 if(typeMap.get(testCase.getPriId().toString())!=null){
				 testCase.setPriName(typeMap.get(testCase.getPriId().toString()).getTypeName());
			 }
		 }
		 typeMap = null;
	}
	private void setRelaUser(List<TestCaseInfo> caseList){
		Map<String,User> userMap= caseService.getRelaUserWithName(caseList, "createrId","auditId");
		User own = null;
		for(TestCaseInfo testCase :caseList){
			own = userMap.get(testCase.getCreaterId());
			if(own!=null)
			 testCase.setAuthorName(own.getUniqueName());
			if(testCase.getAuditId()!=null){
				own = userMap.get(testCase.getAuditId());
				if(own==null){
					continue;
				}
				testCase.setAuditerNmae(own.getUniqueName());					
			}
			own = null;
		}
		own= null;
		userMap = null;
	}
	
	private void  caseListToJson(List<TestCaseInfo> list,StringBuffer sb){
		int i =0 ;
		if(list != null && list.size()>0){
			sb.append("{rows: [");
			for(JsonInterface obj:list){
				i++ ;
				if(i != list.size()){
					obj.toString(sb);
					sb.append(",");
				}else{
					obj.toString(sb);
				}
			}
			sb.append("]}");				
		}		
	    Object pageInfo = SecurityContextHolder.getContext().getAttr("pageInfo");
	    if(pageInfo != null){
	    	 sb.insert(0, pageInfo.toString());
	    }
	}
	public View logicDel(BusiRequestEvent req){
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
		dto.setIsAjax("true");
		caseService.exeCase(dto);
		super.writeResult("success");
		dto = null;
		return super.globalAjax();
	}
	public View exeCase(BusiRequestEvent req){
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
		dto.setIsAjax("true");
		caseService.exeCase(dto);
		super.writeResult("success");
		dto = null;
		return super.globalAjax();
	}
	public View auditCase(BusiRequestEvent req){
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
		dto.setIsAjax("true");
		caseService.exeCase(dto);
		super.writeResult("success");
		dto = null;
		return super.globalAjax();
	}
	public View blockCase(BusiRequestEvent req){
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
		dto.setIsAjax("true");
		caseService.exeCase(dto);
		super.writeResult("success");
		dto = null;
		return super.globalAjax();
	}
	public View addInit(BusiRequestEvent req){
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
		List<ListObject> typeList = drawHtmlListDateService.getTypeDefine("CaseType");
		List<ListObject> casePriList = drawHtmlListDateService.getTypeDefine("CasePri");
		TestCaseInfo testCaseInfo = new TestCaseInfo();
		dto.setTestCaseInfo(testCaseInfo);
		dto.getTestCaseInfo().setTypeSelStr(HtmlListComponent.toSelectStr(typeList,"$"));
		dto.getTestCaseInfo().setPriSelStr(HtmlListComponent.toSelectStr(casePriList,"$"));
		dto.getTestCaseInfo().setCreaterId(SecurityContextHolderHelp.getUserId());
		dto.getTestCaseInfo().setIsReleased(0);
		dto.getTestCaseInfo().setModuleId(dto.getCurrNodeId());
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		dto.setTaskId(taskId);
		dto.getTestCaseInfo().setTaskId(dto.getTaskId());
		dto.getTestCaseInfo().setPriId(new Long(-1));
		dto.getTestCaseInfo().setCaseTypeId(new Long(-1));
		dto.getTestCaseInfo().setExpResult("可在过程及数据中用表格形式写预期结果,或单独在此填写");
		String outlineHql = "select new OutlineInfo(moduleId,moduleLevel,moduleNum) from OutlineInfo where taskId=? and moduleId=?";
		OutlineInfo outline = (OutlineInfo)testTaskService.findByHql(outlineHql, taskId,dto.getCurrNodeId()).get(0);
		dto.getTestCaseInfo().setModuleNum(outline.getModuleNum());
		if(!this.isReview(taskId)){
			dto.getTestCaseInfo().setTestStatus(1);
		}else{
			dto.getTestCaseInfo().setTestStatus(0);
		}
		dto.getTestCaseInfo().setRemark(null);
		dto.getTestCaseInfo().setAttachUrl(null);
		dto.getTestCaseInfo().setTestData(null);
		dto.getTestCaseInfo().setTestCaseId(null);
		dto.getTestCaseInfo().setIsReleased(0);
		dto.getTestCaseInfo().setCreaterId(SecurityContextHolderHelp.getUserId());
		if("true".equals(dto.getIsAjax())){
			super.writeResult(dto.getTestCaseInfo().toStrUpdateInit());
			dto = null;
			return super.globalAjax();
		}
		return super.getView("addInit");
	}
	
	public View lastExeCase(BusiRequestEvent req){
		
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
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
		}
		List<TestCaseVo> exeList  = caseService.loadLastExeCase(dto);
		List exeRecordList = exeList;
		this.setRealActor((List<TestCaseVo>)exeRecordList);
		this.setRealVer((List<TestCaseVo>)exeRecordList);
		this.setHisRelaTaskName(exeList);
		StringBuffer sb = new StringBuffer();
		dto.toJson2(exeRecordList, sb);
		if(dto.getIsAjax()!=null){
//			super.writeResult(sb.toString());
			super.writeResult(JsonUtil.toJson(exeRecordList));
			return super.globalAjax();
		}
		dto.setListStr(sb.toString());
		return super.getView();
	}

	public View viewHistory(BusiRequestEvent req){
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
		dto.setTaskId(taskId);
		List<TestCaseVo> exeList = caseService.loadExeRecord(dto);
		List exeRecordList = exeList;
		this.setRealActor((List<TestCaseVo>)exeRecordList);
		this.setRealVer((List<TestCaseVo>)exeRecordList);
		this.setHisRelaTaskName(exeList);
		StringBuffer sb = new StringBuffer();
		dto.toJson2(exeRecordList, sb);
		if(dto.getIsAjax()!=null){
//			super.writeResult(sb.toString());
			
//			super.writeResult(JsonUtil.toJson(exeRecordList));
//			return super.globalAjax();
			
			PageModel pg = new PageModel();
			pg.setRows(exeRecordList);
			Integer total =  (Integer) SecurityContextHolder.getContext().getAttr("pageInfoTotalRows");
			pg.setTotal(total);
			super.writeResult(JsonUtil.toJson(pg));
			return super.globalAjax();
		}
		dto.setListStr(sb.toString());
		return super.getView();
	}
	
	private void setRelaTaskName(List<TestCaseInfo> testCases){
		if(testCases==null||testCases.isEmpty()){
			return;
		}
		Map<String,ListObject> taskMap= caseService.getRelaTestTasks(testCases, "taskId");
		ListObject lstObj = null;
		for(TestCaseInfo testCase :testCases){
			lstObj = taskMap.get(testCase.getTaskId());
			if(lstObj==null){
				continue;
			}
			testCase.setTaskName(lstObj.getValueObj());
		}
		lstObj= null;
		taskMap = null;
	}
	private void setHisRelaTaskName(List<TestCaseVo> testCases){
		if(testCases==null||testCases.isEmpty()){
			return;
		}
		Map<String,ListObject> taskMap= caseService.getRelaTestTasks(testCases, "taskId");
		ListObject lstObj = null;
		for(TestCaseVo testCase :testCases){
			lstObj = taskMap.get(testCase.getTaskId());
			if(lstObj==null){
				continue;
			}
			testCase.setTaskName(lstObj.getValueObj());
		}
		lstObj= null;
		taskMap = null;
	}
	private void setRealVer(List<TestCaseVo> exeRecordList){
		if(exeRecordList==null||exeRecordList.isEmpty()){
			return;
		}
		Map<String,SoftwareVersion> verMap= caseService.getRelaVers(exeRecordList, "testVer");
		SoftwareVersion ver = null;
		for(TestCaseVo tcVo :exeRecordList){
			if(tcVo.getTestVer()!=null){
				ver = verMap.get(tcVo.getTestVer().toString());
				if(ver==null){
					continue;
				}
				tcVo.setTestVerName(ver.getVersionNum());
				ver = null;
			}
		}
		ver= null;
		verMap = null;		
	}
	private void setHisRealVer(List<CaseExeHistory> exeRecordList){
		if(exeRecordList==null||exeRecordList.isEmpty()){
			return;
		}
		Map<String,SoftwareVersion> verMap= caseService.getRelaVers(exeRecordList, "testVer");
		SoftwareVersion ver = null;
		for(CaseExeHistory his :exeRecordList){
			if(his.getTestVer()!=null){
				ver = verMap.get(his.getTestVer().toString());
				if(ver==null){
					continue;
				}
				his.setTestVerNum(ver.getVersionNum());
				ver = null;
			}
		}
		ver= null;
		verMap = null;		
	}
	private void setRealActor(List<TestCaseVo> exeRecordList){
		if(exeRecordList==null||exeRecordList.isEmpty()){
			return;
		}
		Map<String,User> userMap= caseService.getRelaUserWithName(exeRecordList, "testActor");
		User own = null;
		for(TestCaseVo tcVo :exeRecordList){
			if(tcVo.getTestActor()!=null){
				own = userMap.get(tcVo.getTestActor());
				if(own==null){
					continue;
				}
				tcVo.setTestActorName(own.getUniqueName());
				own = null;
			}
		}
		own= null;
		userMap = null;
	}
	private void setHisRealActor(List<CaseExeHistory> exeRecordList){
		if(exeRecordList==null||exeRecordList.isEmpty()){
			return;
		}
		Map<String,User> userMap= caseService.getRelaUserWithName(exeRecordList, "testActor");
		User own = null;
		for(CaseExeHistory his :exeRecordList){
			if(his.getTestActor()!=null){
				own = userMap.get(his.getTestActor());
				if(own==null){
					continue;
				}
				his.setTestActorName(own.getUniqueName());
				own = null;
			}
		}
		own= null;
		userMap = null;
	}
	private boolean isReview(String taskId){
		String flwHql = "select new TestFlowInfo(testFlowCode) from TestFlowInfo where taskId=? and testFlowCode=9";
		List flwList = caseService.findByHql(flwHql, taskId);
		if(flwList==null||flwList.size()==0){
			return false;
		}else{
			return true;
		}
		
	}
	public View initDropList(BusiRequestEvent req){
		List<ListObject> typeList = drawHtmlListDateService.getTypeDefine("CaseType");
		List<ListObject> casePriList = drawHtmlListDateService.getTypeDefine("CasePri");
		List<List<ListObject>> list = new ArrayList<List<ListObject>>();
		list.add(typeList);
		list.add(casePriList);
		super.writeResult("success"+HtmlListComponent.toSelectStrWithBreak(list));
		return super.globalAjax();
	}
	
	public View dropListWithVer(BusiRequestEvent req){
		List<ListObject> typeList = drawHtmlListDateService.getTypeDefine("CaseType");
		List<ListObject> casePriList = drawHtmlListDateService.getTypeDefine("CasePri");
		List<List<ListObject>> list = new ArrayList<List<ListObject>>();
		list.add(typeList);
		list.add(casePriList);
		if(SecurityContextHolderHelp.getCurrTaksId()!=null){
			StringBuffer hql = new StringBuffer();
			hql.append("select new cn.com.codes.framework.common.ListObject(");
			hql.append(" versionId as keyObj ,versionNum as valueObj ) from SoftwareVersion ")
			.append(" where taskid=? and verStatus=0 order by seq desc ");
			List<ListObject> listDates  = testTaskService.findByHql(hql.toString(), SecurityContextHolderHelp.getCurrTaksId());
			list.add(listDates);
		}
		super.writeResult("success"+HtmlListComponent.toSelectStrWithBreak(list));
		return super.globalAjax();
	}

	public View upInit(BusiRequestEvent req){
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
		String hql="from TestCaseInfo where testCaseId=? and taskId=?";
		String taskId = dto.getTaskId();
		if(taskId!=null&&!"".equals(taskId)){
			
		}else{
			taskId = SecurityContextHolderHelp.getCurrTaksId();
		}
		
		Long caseId =  dto.getTestCaseInfo().getTestCaseId();
		List list = caseService.findByHql(hql,caseId,taskId);
		if("true".equals(dto.getIsAjax())){
			if(list==null||list.size()==0){
				super.writeResult("failed^当前记录己被删除");
				return super.globalAjax();
			} 
			SecurityContextHolderHelp.setCurrTaksId(taskId);
			TestCaseInfo testCase = (TestCaseInfo)list.get(0);
			List<ListObject> typeList = drawHtmlListDateService.getTypeDefine("CaseType");
			List<ListObject> casePriList = drawHtmlListDateService.getTypeDefine("CasePri");
			testCase.setTypeSelStr(HtmlListComponent.toSelectStr(typeList,"$"));
			testCase.setPriSelStr(HtmlListComponent.toSelectStr(casePriList,"$"));
//			super.writeResult(JsonUtil.toJson(testCase.toStrUpdateInit()));
			testCase.setBugs(null);
			testCase.setTestResult(null);
			super.writeResult(super.addJsonPre("dto.testCaseInfo", testCase));
			return super.globalAjax();
		}
		return super.getView();
	}
	
	public View viewDetal(BusiRequestEvent req){
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
		String hql="from TestCaseInfo where testCaseId=? and taskId=?";
		String taskId = dto.getTaskId();
		if(taskId!=null&&!"".equals(taskId)){ 
		}else{
			taskId = SecurityContextHolderHelp.getCurrTaksId();
		}
		
		Long caseId =  dto.getTestCaseInfo().getTestCaseId();
		List list = caseService.findByHql(hql,caseId,taskId);
		if("true".equals(dto.getIsAjax())){
			if(list==null||list.size()==0){
				super.writeResult("failed^当前记录己被删除");
				return super.globalAjax();
			} 
			SecurityContextHolderHelp.setCurrTaksId(taskId);
			TestCaseInfo testCase = (TestCaseInfo)list.get(0);
			List<ListObject> typeList = drawHtmlListDateService.getTypeDefine("CaseType");
			List<ListObject> casePriList = drawHtmlListDateService.getTypeDefine("CasePri");
			testCase.setTypeSelStr(HtmlListComponent.toSelectStr(typeList,"$"));
			testCase.setPriSelStr(HtmlListComponent.toSelectStr(casePriList,"$"));
//			super.writeResult(JsonUtil.toJson(testCase.toStrUpdateInit()));
			testCase.setBugs(null);
			testCase.setTestResult(null);
			super.writeResult(super.addJsonPre("dto.testCaseInfo", testCase));
			return super.globalAjax();
		}
		return super.getView();
	}
	public View exeCaseInit(BusiRequestEvent req){
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
		String hql="from TestCaseInfo where testCaseId=? and taskId=?";
		String taskId = dto.getTaskId();
		if(taskId!=null&&!"".equals(taskId)){
			
		}else{
			taskId = SecurityContextHolderHelp.getCurrTaksId();
		}
		Long caseId =  dto.getTestCaseInfo().getTestCaseId();
		List list = caseService.findByHql(hql,caseId,taskId);
		if("true".equals(dto.getIsAjax())){
			if(list.size()<0){
				super.writeResult("failed^当前记录己被删除");
				return super.globalAjax();
			} 
			SecurityContextHolderHelp.setCurrTaksId(taskId);
			TestCaseInfo testCase = (TestCaseInfo)list.get(0);
			List<ListObject> typeList = drawHtmlListDateService.getTypeDefine("CaseType");
			List<ListObject> casePriList = drawHtmlListDateService.getTypeDefine("CasePri");
			testCase.setTypeSelStr(HtmlListComponent.toSelectStr(typeList,"$"));
			testCase.setPriSelStr(HtmlListComponent.toSelectStr(casePriList,"$"));
//			super.writeResult(JsonUtil.toJson(testCase.toStrUpdateInit()));
			testCase.setBugs(null);
			testCase.setTestResult(null);
			super.writeResult(super.addJsonPre("dto.testCaseInfo", testCase));
			return super.globalAjax();
		}
		return super.getView();
	}
	@SuppressWarnings("unchecked")
	public View addCase(BusiRequestEvent req){
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
//		List<FileInfoVo> fileInfos = dto.getFileInfos();
//		List<FileInfoVo> filesInfo = null;
//		if(fileInfos!=null && fileInfos.size()>0){
//			JSONArray json = JSONArray.fromObject(fileInfos.toString().replace("[[", "[").replace("]]", "]]"));
//			filesInfo= (List<FileInfoVo>)JSONArray.toCollection(json, FileInfoVo.class);
//			dto.setFileInfos(filesInfo);
//		}
		caseService.addOrUpCase(dto);
		List<TestCaseInfo> caseList = new ArrayList<TestCaseInfo>(1) ;
		caseList.add(dto.getTestCaseInfo());
		this.setRelaTaskName(caseList);
//		super.writeResult("success^"+dto.getTestCaseInfo().toStrUpdateRest());
		super.writeResult("success");
		return super.globalAjax();
	}
	@SuppressWarnings("unchecked")
	public View upCase(BusiRequestEvent req){
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
//		List<FileInfoVo> fileInfos = dto.getFileInfos();
//		List<FileInfoVo> filesInfo = null;
//		if(fileInfos!=null && fileInfos.size()>0){
//			JSONArray json = JSONArray.fromObject(fileInfos.toString().replace("[[", "[").replace("]]", "]]"));
//			filesInfo= (List<FileInfoVo>)JSONArray.toCollection(json, FileInfoVo.class);
//			dto.setFileInfos(filesInfo);
//		}
		caseService.addOrUpCase(dto);
		List<TestCaseInfo> caseList = new ArrayList<TestCaseInfo>(1) ;
		caseList.add(dto.getTestCaseInfo());
		this.setRelaTaskName(caseList);
		super.writeResult("success");
		dto = null;
		return super.globalAjax();
	}	
	
	public View delCase(BusiRequestEvent req){
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
		int shuliang = 0;
		if(!StringUtils.isNullOrEmpty(dto.getCaseIds())){
			for(int i=0;i<dto.getCaseIds().split(",").length;i++){
				Long caseId = Long.parseLong(dto.getCaseIds().split(",")[i]);
				if(caseService.isCanDel(caseId)){
					caseService.delCase(caseId);
				}else{
					shuliang = shuliang + 1;
				}
			}
			if(shuliang == 0){
				super.writeResult("success");
				return super.globalAjax();
			}
			super.writeResult(shuliang + "条用例有执行记录，无法删除");
			return super.globalAjax();
		}else{
			super.writeResult("请选择需要删除的记录");
			return super.globalAjax();
		}
	}
	private String toTreeStr(List<OutlineInfo> list) {
		//System.out.println("+++++++++++++++++++++++++++++++++++++list:"+list.toString());
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
					treeJsonVo.setModuleNum(outLine.getModuleNum());
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
					treeJsonVoC.setModuleNum(outLine.getModuleNum());
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
				treeJsonVoC.setModuleNum(outLine.getModuleNum());
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
	
	public OutLineManagerService getOutLineService() {
		return outLineService;
	}

	public void setOutLineService(OutLineManagerService outLineService) {
		this.outLineService = outLineService;
	}

	public CaseManagerService getCaseService() {
		return caseService;
	}

	public void setCaseService(CaseManagerService caseService) {
		this.caseService = caseService;
	}

	public DrawHtmlListDateService getDrawHtmlListDateService() {
		return drawHtmlListDateService;
	}

	public void setDrawHtmlListDateService(
			DrawHtmlListDateService drawHtmlListDateService) {
		this.drawHtmlListDateService = drawHtmlListDateService;
	}

	public TestTaskDetailService getTestTaskService() {
		return testTaskService;
	}

	public void setTestTaskService(TestTaskDetailService testTaskService) {
		this.testTaskService = testTaskService;
	}
	
	public ImpExpManagerService getImpExpManagerService() {
		return impExpManagerService;
	}

	public void setImpExpManagerService(ImpExpManagerService impExpManagerService) {
		this.impExpManagerService = impExpManagerService;
	}

	public View  uploadDemo(BusiRequestEvent req){
		return super.getView();
	}


	public BugCommonService getBugCommonService() {
		return bugCommonService;
	}


	public void setBugCommonService(BugCommonService bugCommonService) {
		this.bugCommonService = bugCommonService;
	}
	
	public View caseAdd(BusiRequestEvent req) {
		return super.getView();
	}
	
	public View caseEdit(BusiRequestEvent req) {
		return super.getView();
	}
	
	public View caseLook(BusiRequestEvent req) {
		return super.getView();
	}
}
