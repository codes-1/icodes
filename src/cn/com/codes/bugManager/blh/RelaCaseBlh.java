package cn.com.codes.bugManager.blh;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.codes.bugManager.dto.RelaCaseDto;
import cn.com.codes.bugManager.service.BugCommonService;
import cn.com.codes.bugManager.service.BugManagerService;
import cn.com.codes.caseManager.service.CaseManagerService;
import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.dto.PageModel;
import cn.com.codes.common.service.DrawHtmlListDateService;
import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.common.HtmlListComponent;
import cn.com.codes.framework.common.JsonUtil;
import cn.com.codes.framework.common.ListObject;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.object.BugBaseInfo;
import cn.com.codes.object.BugFreq;
import cn.com.codes.object.BugGrade;
import cn.com.codes.object.BugOpotunity;
import cn.com.codes.object.BugPri;
import cn.com.codes.object.BugSource;
import cn.com.codes.object.BugType;
import cn.com.codes.object.CaseBugRela;
import cn.com.codes.object.CaseExeHistory;
import cn.com.codes.object.DefaultTypeDefine;
import cn.com.codes.object.GeneCause;
import cn.com.codes.object.ImportPhase;
import cn.com.codes.object.OccurPlant;
import cn.com.codes.object.OutlineInfo;
import cn.com.codes.object.SoftwareVersion;
import cn.com.codes.object.TestCaseInfo;
import cn.com.codes.object.TestResult;
import cn.com.codes.object.TypeDefine;
import cn.com.codes.testTaskManager.service.TestTaskDetailService;

public class RelaCaseBlh extends BusinessBlh {
	 //
	private CaseManagerService caseService;
	private BugCommonService bugCommonService;
	private BugManagerService bugManagerService;
	private TestTaskDetailService testTaskService ;
	private DrawHtmlListDateService drawHtmlListDateService;
	
	public View bugRelaCase(BusiRequestEvent req){
		RelaCaseDto dto = super.getDto(RelaCaseDto.class, req);
		this.buildBugRelaCaseTestRest(dto);
		bugManagerService.bugRelaCase(dto);
		super.writeResult("success");
		return super.globalAjax();
	}
	private void buildBugRelaCaseTestRest(RelaCaseDto dto){
		
		if(dto.getTestCaseIds()==null||dto.getTestCaseIds().trim().equals("")){
			return;
		}
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		String testActorId = SecurityContextHolderHelp.getUserId();
		Long moduleId = dto.getModuleId();
		String remark = "关联用例自动生成测试结果";
		String[] caseIdsArr = dto.getTestCaseIds().trim().split(",");
		List<TestResult> resultList = new ArrayList<TestResult>();
		List<CaseExeHistory> hisList = new ArrayList<CaseExeHistory>();
		List<Long> bugIds = new ArrayList<Long>(1);
		bugIds.add(dto.getBugId());
		SoftwareVersion softVer = this.getRelaBugVers(bugIds).get(0);
		String upCaseHql = "update TestCaseInfo set testStatus=3 ,auditId=:auditId, upddate=:upddate where testCaseId in(:ids)" ;
		Map paraMap = new HashMap(3);
		paraMap.put("auditId", SecurityContextHolderHelp.getUserId());
		paraMap.put("upddate", new Date());
		List<Long> ids = new ArrayList<Long>(caseIdsArr.length);
		for(String caseIdStr:caseIdsArr){
			if("".equals(caseIdStr)||" ".equals(caseIdStr)){
				continue; 
			}
//			Long testCaseId = new Long(caseIdStr);
			Long testCaseId =Long.valueOf(caseIdStr);
			TestResult rest = new TestResult();
			rest.setExeDate(new Date());
			rest.setTaskId(taskId);
			rest.setTestActor(testActorId);
			rest.setTestResult(3);
			rest.setModuleId(moduleId);
			rest.setTestCaseId(testCaseId);
			rest.setRemark(remark);	
			resultList.add(rest);
			rest.setTestVer(softVer.getVersionId());
			hisList.add(rest.convert2ExeHistory());
			ids.add(testCaseId);
		}
		paraMap.put("ids",ids);
		dto.setHql(upCaseHql);
		dto.setHqlParamMaps(paraMap);
		dto.setAttr("resultList", resultList);
		dto.setAttr("hisList", hisList);
	}
	public View caseRelaBug(BusiRequestEvent req){
		RelaCaseDto dto = super.getDto(RelaCaseDto.class, req);
		this.buildCaseRelaBugTestRest(dto);
		bugManagerService.caseRelaBug(dto);
		this.writeGridUpInfo(dto.getCurrCaseTestRedList());
		return super.globalAjax();
	}
	private void writeGridUpInfo(List<TestResult> currCaseTestRedList){
		
		if(currCaseTestRedList!=null&&!currCaseTestRedList.isEmpty()){
			StringBuffer sb = new StringBuffer();
			for(TestResult rest :currCaseTestRedList){
				sb.append(rest.getTestCaseId()+"_"+rest.getTestVer())
				.append(" ").append(rest.getSoftVer().getVersionNum())
				.append("$");
			}
			super.writeResult(sb.substring(0,sb.length()-1).toString());
		}
		
	}
	private void buildCaseRelaBugTestRest(RelaCaseDto dto){
		if(dto.getBugIds()==null||dto.getBugIds().trim().equals("")){
			return;
		}
		String[] bugIdsArr = dto.getBugIds().trim().split(" ");
		List<Long> bugIds = new ArrayList<Long>();
		for(String bugId:bugIdsArr){
			if("".equals(bugId)||" ".equals(bugId)){
				continue; 
			}
			bugIds.add(new Long(bugId));
		}
		bugIdsArr = null;
		bugManagerService.sortLongList(bugIds);
		List<SoftwareVersion> bugVers = this.getRelaBugVers(bugIds);
		bugIds = null;
		List<TestResult> resultList = new ArrayList<TestResult>();
		List<CaseExeHistory> hisList = new ArrayList<CaseExeHistory>();
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		String testActorId = SecurityContextHolderHelp.getUserId();
		Long moduleId = dto.getModuleId();
		Long testCaseId = dto.getTestCaseId();
		String remark = "关联BUG自动生成测试结果";
		for(SoftwareVersion softVer:bugVers){
			TestResult rest = new TestResult();
			rest.setExeDate(new Date());
			rest.setTaskId(taskId);
			rest.setTestActor(testActorId);
			rest.setTestResult(3);
			rest.setModuleId(moduleId);
			rest.setTestCaseId(testCaseId);
			rest.setRemark(remark);
			rest.setSoftVer(softVer);
			rest.setTestVer(softVer.getVersionId());
			resultList.add(rest);
			hisList.add(rest.convert2ExeHistory());
		}
		dto.setAttr("resultList", resultList);
		dto.setAttr("hisList", hisList);

	}
//	private List<Long> getRelaBugVers(List<Long> bugIds){
//		String hql = "select distinct bugReptVer from BugBaseInfo where bugId in(:bugIds) and bugReptVer is not null";
//		Map values = new HashMap(1);
//		values.put("bugIds", bugIds);
//		List<Long> vers = bugManagerService.findByHqlWithValuesMap(hql, values, false);
//		return vers;
//	}
	
	private List<SoftwareVersion> getRelaBugVers(List<Long> bugIds){
		String hql = "select distinct new SoftwareVersion(sv.versionId,sv.versionNum)  from SoftwareVersion sv join sv.bugs bug where bug.taskId=:taskId and bug.bugId in(:bugIds)  ";
		Map values = new HashMap(1);
		values.put("bugIds", bugIds);
		values.put("taskId", SecurityContextHolderHelp.getCurrTaksId());
		List<SoftwareVersion> vers = bugManagerService.findByHqlWithValuesMap(hql, values, false);
		return vers;
	}
	 
	public View loadRelaBug(BusiRequestEvent req) {
		RelaCaseDto dto = super.getDto(RelaCaseDto.class, req);
		if(dto.getTaskId()!=null&&!"".equals(dto.getTaskId().trim())){
			SecurityContextHolder.getContext().getVisit().setTaskId(dto.getTaskId());	
		}
		List<BugBaseInfo> bugs = bugManagerService.getRelaBug(dto);
		List<TypeDefine> typeList = dto.getTypeList();
		Map<String ,TypeDefine >  tdMap =  null;
		if(bugs!=null&&bugs.size()>0){
			if(typeList!=null){
				tdMap = this.convertTdMap(typeList);
				bugCommonService.setBugsRelaTypeDefine(tdMap, bugs);
			}else{
				this.setRelaTypeDefine(bugs);
			}
		}
		if(dto.getMdPath()==null||dto.getMdPath().equals("")){
			String mdPath = bugManagerService.getMdPath(dto.getModuleId());
			dto.setMdPath(mdPath);			
		}
		StringBuffer relaBugSb = new StringBuffer();
		List<BugBaseInfo> relaBugList = dto.getRelaBugList();
		if(relaBugList!=null&&relaBugList.size()>0){
			for(BugBaseInfo bug:relaBugList){
				relaBugSb.append(bug.getBugId()).append(" ");
			}
		}
		tdMap = null;
		dto.setRelaBugList(null);
		StringBuffer sb = new StringBuffer();
		dto.toJson2((List)bugs, sb);
		//dto.setJsonData((List)bugs);
		dto.setTypeList(null);
		dto.setBugIds(relaBugSb.toString().trim());
		typeList=null;
		if("true".equalsIgnoreCase(dto.getIsAjax())){
			//super.writeResult(dto.toListStrJson());
			super.writeResult(sb.toString());
			return super.globalAjax();
		}
		dto.setListStr(sb.toString());
		return super.getView();
	}
	private void setRelaTypeDefine(List<BugBaseInfo> list){
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
	private Map<String ,TypeDefine > convertTdMap(List<TypeDefine> typeList ){
		Map<String ,TypeDefine> map = new HashMap<String ,TypeDefine>();
		for(TypeDefine td :typeList){
			if( td instanceof BugType){
				map.put("BugType"+td.getTypeId(), td);
			}else if(td instanceof BugGrade){
				map.put("BugGrade"+td.getTypeId(), td);
			}else if(td instanceof BugOpotunity){
				map.put("BugOpotunity"+td.getTypeId(), td);
			}else if(td instanceof BugFreq){
				map.put("BugFreq"+td.getTypeId(), td);
			}else if(td instanceof BugPri){
				map.put("BugPri"+td.getTypeId(), td);
			}else if(td instanceof GeneCause){
				map.put("GeneCause"+td.getTypeId(), td);
			}else if(td instanceof OccurPlant){
				map.put("OccurPlant"+td.getTypeId(), td);
			}else if(td instanceof BugSource){
				map.put("BugSource"+td.getTypeId(), td);
			}else if(td instanceof ImportPhase){
				map.put("ImportPhase"+td.getTypeId(), td);
			}
		}
		return map;
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
	public View addCaseInit(BusiRequestEvent req){
		RelaCaseDto dto = super.getDto(RelaCaseDto.class, req);
		if(dto.getTestCaseInfo()==null){
			dto.setTestCaseInfo(new TestCaseInfo());
		}
		if(dto.getMdPath()==null||dto.getMdPath().equals("")){
			String mdPath = bugManagerService.getMdPath(dto.getModuleId());
			dto.getTestCaseInfo().setMdPath(mdPath);			
		}
		//做增中的初始化
		List<ListObject> casetypeList = drawHtmlListDateService.getTypeDefine("CaseType");
		List<ListObject> casePriList = drawHtmlListDateService.getTypeDefine("CasePri");
		dto.getTestCaseInfo().setTypeSelStr(HtmlListComponent.toSelectStr(casetypeList,"$"));
		dto.getTestCaseInfo().setPriSelStr(HtmlListComponent.toSelectStr(casePriList,"$"));
		dto.getTestCaseInfo().setCreaterId(SecurityContextHolderHelp.getUserId());
		dto.getTestCaseInfo().setPriId(new Long(-1));
		dto.getTestCaseInfo().setCaseTypeId(new Long(-1));
		dto.getTestCaseInfo().setExpResult("可在过程及数据中用表格形式写预期结果,或单独在此填写");
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		dto.getTestCaseInfo().setTaskId(taskId);
		//Map verAndSeq =  testTaskService.getCurrTestSeqAndVer(taskId, dto.getModuleId());
		//String currVer =verAndSeq.get("currVer").toString();
		//Integer testSeq = (Integer)verAndSeq.get("testSeq");
		//dto.getTestCaseInfo().setCreatVer(currVer);
		//dto.getTestCaseInfo().setInitSeq(testSeq);
		String outlineHql = "select new OutlineInfo(moduleId,moduleLevel,moduleNum) from OutlineInfo where taskId=? and moduleId=?";
		OutlineInfo outline = (OutlineInfo)testTaskService.findByHql(outlineHql, taskId,dto.getModuleId()).get(0);
		dto.getTestCaseInfo().setModuleNum(outline.getModuleNum());
		if(!this.isReview(taskId)){
			dto.getTestCaseInfo().setTestStatus(1);
		}else{
			dto.getTestCaseInfo().setTestStatus(0);
		}
		//dto.getTestCaseInfo().setExeDate(null);
		dto.getTestCaseInfo().setRemark(null);
		dto.getTestCaseInfo().setAttachUrl(null);
		//dto.getTestCaseInfo().setTestActorId(null);
		//dto.getTestCaseInfo().setTestSeq(null);
		dto.getTestCaseInfo().setTestData(null);
		dto.getTestCaseInfo().setTestCaseId(null);
		dto.getTestCaseInfo().setIsReleased(0);
		//dto.getTestCaseInfo().setCurrVer(null);
		dto.getTestCaseInfo().setCreaterId(SecurityContextHolderHelp.getUserId());
		casePriList = null;
		casetypeList = null;
		dto.getTestCaseInfo().setIsReleased(0);
		dto.getTestCaseInfo().setModuleId(dto.getModuleId());
		super.writeResult(dto.getTestCaseInfo().toStrUpdateInit());
		dto = null;
		return super.globalAjax();
	}
	public View  loadRelaCase(BusiRequestEvent req){
		RelaCaseDto dto = super.getDto(RelaCaseDto.class, req);
		if(dto.getTaskId()!=null&&!"".equals(dto.getTaskId().trim())){
			SecurityContextHolder.getContext().getVisit().setTaskId(dto.getTaskId());	
		}
		List<TestCaseInfo> caseList = bugManagerService.getRelaCase(dto);
		if(caseList.size()>0){
			List<TypeDefine> typeList = dto.getTypeList();
			Map<String ,TypeDefine> map = new HashMap<String ,TypeDefine>();
			for(TypeDefine td :typeList){
				map.put(td.getTypeId().toString(), td);
			}
			List<TestCaseInfo> relaCaseList = null;
			StringBuffer relaCaseSb = new StringBuffer();
			relaCaseList = dto.getRelaCaseList();
			for(TestCaseInfo testCase:caseList){
				testCase.setPriName(map.get(testCase.getPriId().toString()).getTypeName());
				testCase.setTypeName(map.get(testCase.getCaseTypeId().toString()).getTypeName());			
			}
			if(relaCaseList!=null){
				for(TestCaseInfo relaCase:relaCaseList){
					relaCaseSb.append(relaCase.getTestCaseId()).append(" ");
				}				
			}
			map = null;
			dto.setRelaCaseList(null);
			dto.setTestCaseIds(relaCaseSb.toString().trim());
			dto.setRelaCaseList(caseList);
			relaCaseList = null;
		}
		if("true".equals(dto.getIsAjax())){
//			super.writeResult(super.listToJson((List)caseList));JsonUtil.toJson(pg)
//			super.writeResult(JsonUtil.toJson((List)caseList));
			PageModel pageModel = new PageModel();
			Integer total = (Integer) SecurityContextHolder.getContext().getAttr("pageInfoTotalRows");
			pageModel.setTotal(total);
			pageModel.setRows(caseList);
//			dto.setTotal(caseList.size());
//			super.writeResult(JsonUtil.toJson(dto));
			super.writeResult(JsonUtil.toJson(pageModel));
			return super.globalAjax();
		}
//		dto.setListStr(super.listToJson((List)caseList));
		dto.setListStr(JsonUtil.toJson((List)caseList));
		return super.getView();
	}
	
	/** 
	* 方法名:          getBugRelaCaseData
	* 方法功能描述:      获取bug关联的实例
	* @param:         
	* @return:        
	* @Author:       
	* @Create 
	*/
	public View getBugRelaCaseData(BusiRequestEvent req){
		RelaCaseDto dto = super.getDto(RelaCaseDto.class, req);
		List<CaseBugRela> caseBugLists = bugManagerService.findByProperties(CaseBugRela.class, new String[]{"bugId"}, new Object[]{dto.getCaseBugRela().getBugId()});
		super.writeResult(JsonUtil.toJson(caseBugLists));
		return super.globalAjax();
	}


	public BugManagerService getBugManagerService() {
		return bugManagerService;
	}

	public void setBugManagerService(BugManagerService bugManagerService) {
		this.bugManagerService = bugManagerService;
	}

	public BugCommonService getBugCommonService() {
		return bugCommonService;
	}

	public void setBugCommonService(BugCommonService bugCommonService) {
		this.bugCommonService = bugCommonService;
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
	

}
