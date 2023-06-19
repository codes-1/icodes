package cn.com.codes.testTaskManager.blh;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.dto.PageModel;
import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.common.HtmlListComponent;
import cn.com.codes.framework.common.HtmlListQueryObj;
import cn.com.codes.framework.common.JsonUtil;
import cn.com.codes.framework.common.ListObject;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.transmission.JsonInterface;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.object.BugBaseInfo;
import cn.com.codes.object.BugHandHistory;
import cn.com.codes.object.SoftwareVersion;
import cn.com.codes.object.TaskUseActor;
import cn.com.codes.object.TestFlowInfo;
import cn.com.codes.object.TestTaskDetail;
import cn.com.codes.testTaskManager.dto.TestTaskManagerDto;
import cn.com.codes.testTaskManager.dto.VTestkTaskActor;
import cn.com.codes.testTaskManager.service.TestTaskDetailService;

public class TestTaskDetailBlh extends BusinessBlh {
	
	private static Logger logger = Logger.getLogger(TestTaskDetailBlh.class);
	private static Map flwCheFlag = new HashMap();
	
	TestTaskDetailService testTaskService ;


	public View softVerList(BusiRequestEvent req){
		
		TestTaskManagerDto dto = super.getDto(TestTaskManagerDto.class, req);
		List<?> list = testTaskService.getSoftVerList(dto);
		
		StringBuffer sb = new StringBuffer();
		PageModel pageModel = new PageModel(); 
		pageModel.setRows(list);
		pageModel.setTotal(dto.getTotal());
//		dto.toJson2(list, sb);
//		if(dto.getIsAjax()!=null){
//			super.writeResult(sb.toString());
			super.writeResult(JsonUtil.toJson(pageModel));
			return super.globalAjax();
//		}
//		dto.setListStr(sb.toString());
//		return super.getView();
	}
	
	public View softVerListLoad(BusiRequestEvent req){
		
		TestTaskManagerDto dto = super.getDto(TestTaskManagerDto.class, req);
//		this.buildSystemVersion(dto);
//		List<?> list = testTaskService.getSoftVerList(dto);
		List<SoftwareVersion> list = testTaskService.getSoftVerList(dto);
//		List<SoftwareVersion> hqlWithValuesMap = testTaskService.findByHqlWithValuesMap(dto.getHql(), dto.getPageNo(), 
//				dto.getPageSize(), "s.taskid", dto.getHqlParamMaps(), false);
//		List<SoftwareVersion> softs = new ArrayList<SoftwareVersion>();
//		for (int i = 0; i < list.size(); i++) {
//			if(!"2".equals(String.valueOf(list.get(i).getVerStatus()))){//过滤掉verStatus为2的数据
//				softs.add(list.get(i));
//			}
//		} 
		
		StringBuffer sb = new StringBuffer();
		PageModel pageModel = new PageModel(); 
		pageModel.setRows(list);
		Integer total =  (Integer) SecurityContextHolder.getContext().getAttr("pageInfoTotalRows");
		pageModel.setTotal(total);
//		dto.toJson2(list, sb);
//		if(dto.getIsAjax()!=null){
//			super.writeResult(sb.toString());
			super.writeResult(JsonUtil.toJson(pageModel));
			return super.globalAjax();
//		}
//		dto.setListStr(sb.toString());
//		return super.getView();
	}
	

	private void buildSystemVersion(TestTaskManagerDto dto) {
		StringBuffer hqlBuffer = new StringBuffer();
		hqlBuffer.append(" select new cn.com.codes.object.softwareVersion(s.taskid,s.versionNum,");
		hqlBuffer.append(" s.insdate,s.upddate,s.remark,s.seq,s.verStatus,s.versionId) ");
		
		HashMap<String,Object> hashMap = new HashMap<String, Object>();
		hashMap.put("taskid", SecurityContextHolderHelp.getCurrTaksId());
		
//		SoftwareVersion softVer = dto.getSoftVer();
		
//		if(softVer != null){
			hqlBuffer.append(" from cn.com.codes.object.softwareVersion s");
			dto.setHqlParamMaps(hashMap);
			dto.setHql(hqlBuffer.toString());
//			return;
//		}
	}

	public View loadVerSel(BusiRequestEvent req){
		TestTaskManagerDto dto = super.getDto(TestTaskManagerDto.class, req);
		StringBuffer hql = new StringBuffer();
		hql.append("select new cn.com.codes.framework.common.ListObject(");
		hql.append(" versionId as keyObj ,versionNum as valueObj ) from SoftwareVersion ")
		.append(" where taskid=? and verStatus=0 order by seq desc ");
		String taskId = dto.getTaskId();
		if(taskId==null||taskId==""){
			taskId = SecurityContextHolderHelp.getCurrTaksId();
		}
		List<ListObject> listDates  = testTaskService.findByHql(hql.toString(), taskId);
//		super.writeResult(HtmlListComponent.toSelectStr(listDates));
		super.writeResult(JsonUtil.toJson(listDates));
		
		return super.globalAjax();
	}
	public View addSoftVer(BusiRequestEvent req){
		
		TestTaskManagerDto dto = super.getDto(TestTaskManagerDto.class, req);
		SoftwareVersion softVer = dto.getSoftVer();
		softVer.setInsdate(new Date());
		softVer.setUpddate(softVer.getInsdate());
		if(!"".equals(softVer.getVersionId())&&softVer.getVersionId()!=null){
			softVer.setTaskid(softVer.getTaskid());
			testTaskService.update(softVer);
		}else{
			softVer.setVerStatus(0);
			softVer.setTaskid(SecurityContextHolderHelp.getCurrTaksId());
			String chkRest = testTaskService.softVerRepChk(softVer);
			if(!chkRest.equals("")){
				super.writeResult(chkRest);
				return super.globalAjax();
			}
			testTaskService.add(softVer);
		}
		if(dto.getIsAjax()!=null){
			super.writeResult("success^"+softVer.getVersionId());
			return super.globalAjax();
		}
		return super.getView();
	}
	

	public View udateSoftVer(BusiRequestEvent req){
		
		TestTaskManagerDto dto = super.getDto(TestTaskManagerDto.class, req);
		SoftwareVersion softVer = dto.getSoftVer();
		String chkRest = testTaskService.softVerRepChk(softVer);
		if(!chkRest.equals("")){
			super.writeResult(chkRest);
			return super.globalAjax();
		}
		StringBuffer hql = new StringBuffer();
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		hql.append("update SoftwareVersion set versionNum=? ,")
		.append("remark=?,seq=?,upddate= ? where versionId=? and taskid=?");
//		testTaskService.getHibernateGenericController().executeUpdate(hql.toString(), softVer.getVersionNum()
//				,softVer.getRemark(),softVer.getSeq(),new Date(),softVer.getVersionId(),taskId);
		SoftwareVersion softwareVersion = testTaskService.get(SoftwareVersion.class, dto.getSoftVer().getVersionId());
		softwareVersion.setBugs(null);
		if(dto.getIsAjax()!=null){
//			super.writeResult("success");
			super.writeResult(super.addJsonPre("dto.softVer", softwareVersion));
			return super.globalAjax();
		}
		return super.getView();    
	}
	
	public View delSoftVer(BusiRequestEvent req){
		
		TestTaskManagerDto dto = super.getDto(TestTaskManagerDto.class, req);
		
		String hql = " select new TestTaskDetail(outlineState,testPhase,currentVersion,testSeq) from TestTaskDetail where taskId=?";
		TestTaskDetail taskDetal = (TestTaskDetail)testTaskService.findByHql(hql,dto.getSoftVer().getTaskid()).get(0);//SecurityContextHolderHelp.getCurrTaksId()
		if(taskDetal.getOutlineState()==0){
			//因versionNum设置了不为空,删除时hibernate 要检查这属性,所以这里随便测试一下  
			dto.getSoftVer().setVersionNum("liuyg");
			testTaskService.delete(dto.getSoftVer());
			//System.out.println("");   
		}else{
			dto.getSoftVer().setVerStatus(2);
			return this.softVerSwStatus(req);
		}
		if(dto.getIsAjax()!=null){
			super.writeResult("success");
			return super.globalAjax();
		}
		return super.getView();
	}
	
	public View softVerSwStatus(BusiRequestEvent req){
		
		TestTaskManagerDto dto = super.getDto(TestTaskManagerDto.class, req);
		StringBuffer hql = new StringBuffer();
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		hql.append("update SoftwareVersion set verStatus=? ")
		.append("where versionId=? and taskid=?");
		testTaskService.getHibernateGenericController().executeUpdate(hql.toString(), dto.getSoftVer().getVerStatus(),dto.getSoftVer().getVersionId(),dto.getSoftVer().getTaskid());
		if(dto.getIsAjax()!=null){
			super.writeResult("success");
			return super.globalAjax();
		}
		return super.getView();
	}
	//修改初始化
	public View  flwSetInit(BusiRequestEvent req){
		TestTaskManagerDto dto = super.getDto(TestTaskManagerDto.class, req);
		String taskId = dto.getTaskId();
		if(taskId==null||"".equals(taskId)){
			taskId = SecurityContextHolderHelp.getCurrTaksId();
		}
		//测试阶段不等于空时，是
		if(taskId==null||"".equals(taskId)||dto.getComeFrom()==null){
			return super.getView("choiceTask");
		}
		testTaskService.updateInit(dto);
		Integer testTaskState = dto.getDetail().getTestTaskState();
		if(testTaskState == null|| testTaskState==3){
			this.initWhenReady(dto);
		}else{
			this.init(dto);
		}
		dto.getDetail().setTestFlow(null);
		dto.getDetail().setUseActor(null);
		super.writeResult(JsonUtil.toJson(dto));
//		if("0".equals(dto.getTaskType())){
//			return super.getView("flwSetInitSingle");
//		}
		return super.getView();
//		return super.globalAjax();
	}
	public View checkAssign(BusiRequestEvent req){
		TestTaskManagerDto dto = super.getDto(TestTaskManagerDto.class, req);
		if(dto.getDetail().getOutlineState()!=1&&dto.getDetail().getTestTaskState()!=0){
			super.writeResult("ok");
		}else{
			String SelStrName = dto.getSelStrName();
			String chgId = dto.getChgUserIds();
		}
		return super.globalAjax();
	}
	

	public View update(BusiRequestEvent req){
		TestTaskManagerDto dto = super.getDto(TestTaskManagerDto.class, req);
		this.updatePrepare(dto);
		this.bugAdjust(dto);
		boolean havePut = false;
		if(dto.getAttr("bugAdjustMap")!=null){
			flwCheFlag.put(dto.getDetail().getTaskId(), null);
			havePut = true;
		}
		try {
			testTaskService.update(dto);
		} catch (RuntimeException e) {
			logger.error(e);
			throw e;
		}finally{
			if(havePut){
				flwCheFlag.remove(dto.getDetail().getTaskId());
			}
		}
		super.writeResult("success");
		return super.getView("ajaxRest");
	}
	

	public static boolean isLock(){
		return flwCheFlag.get(SecurityContextHolderHelp.getCurrTaksId())==null?false:true;
	}
	private void bugAdjust(TestTaskManagerDto dto){
		//如测试己开始
		if(dto.getDetail().getOutlineState()==1&&dto.getDetail().getTestTaskState()==0){
			String flwTmp = dto.getTestFlowStr();
			String flwInitTmp = dto.getInitFlowStr();
			if(flwInitTmp.equals(flwTmp)){
				dto.setAttr("bugAdjustMap", null);
				return;
			}
			if(flwTmp.endsWith("9")){
				flwTmp = flwTmp.substring(0,flwTmp.length()-2);
			}else if(flwTmp.startsWith("9")){
				flwTmp = flwTmp.substring(2,flwTmp.length());
			}
			if(flwInitTmp.endsWith("9")){
				flwInitTmp = flwInitTmp.substring(0,flwInitTmp.length()-2);
			}else if(flwInitTmp.startsWith("9")){
				flwInitTmp = flwInitTmp.substring(2,flwInitTmp.length());
			}
			String[] initFlwArr = flwInitTmp.split(",");
			StringBuffer removeFlwSb = new StringBuffer();
			for(int i=0 ;i<initFlwArr.length ;i++){
				if(flwTmp.indexOf(initFlwArr[i])<0){
					if(removeFlwSb.length()>=1){
						removeFlwSb.append(",").append(initFlwArr[i]);
					}else{
						removeFlwSb.append(initFlwArr[i]);
					}
				}
			}
			if(!removeFlwSb.toString().equals("")){
				String[] currFlwArr = flwTmp.split(",");
				String[] removFlwArr = removeFlwSb.toString().split(",");
				Map<String,Map> bugAdjustMap = new HashMap<String,Map>();
				dto.setAttr("bugAdjustMap",bugAdjustMap);
				for(String remFlw:removFlwArr){
					for(String currFlw:currFlwArr){
						if(Integer.parseInt(currFlw)>Integer.parseInt(remFlw)){
							 this.getAdustHql(dto,Integer.parseInt(remFlw), Integer.parseInt(currFlw));
							break;
						}
					}
				}
			}
		}
	}

	private void getAdustHql(TestTaskManagerDto dto,int remFlw,int nextFlw){
		StringBuffer hql = new StringBuffer("update BugBaseInfo ");
		hql.append("set currHandlerId=:currId,currHandlDate=:currDate,nextOwnerId=:nextOwnerId ");
		Map praVaMap = new HashMap();
		praVaMap.put("currId", SecurityContextHolderHelp.getUserId());
		praVaMap.put("currDate", new Date());
		List<BugHandHistory> historyList = new ArrayList<BugHandHistory>();
		StringBuffer fHql = new StringBuffer();
		fHql.append("select new BugBaseInfo(bugId, moduleId) ");
		fHql.append("from BugBaseInfo where taskId=? and currStateId=? and reptDate>?");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)-1);
		Date repDate = calendar.getTime();
		//flwName +"系统自动生成处理历史"
		if(remFlw==2&&nextFlw==3){
			hql.append(",currStateId=:currState,analyseOwnerId=:analyOwnId,initState=1,nextFlowCd=3");
			hql.append(" where taskId=:taskId and currStateId=1");
			praVaMap.put("taskId", dto.getDetail().getTaskId());
			praVaMap.put("currState", 24);
			praVaMap.put("analyOwnId", dto.getAttr("3").toString());
			praVaMap.put("nextOwnerId", praVaMap.get("analyOwnId"));
			this.buildAdjustHistory(fHql.toString(), 1, 2, 24, "把状态从待置改为分析", repDate, historyList);
		}else if(remFlw==2&&nextFlw==4){
			hql.append(",currStateId=:currState,assinOwnerId=:anasiOwnId,initState=1,nextFlowCd=4");
			hql.append(" where taskId=:taskId and currStateId=1");
			praVaMap.put("taskId", dto.getDetail().getTaskId());
			praVaMap.put("currState", 25);
			praVaMap.put("anasiOwnId", dto.getAttr("4").toString());
			praVaMap.put("nextOwnerId", praVaMap.get("anasiOwnId"));
			this.buildAdjustHistory(fHql.toString(), 1, 2, 25, "把状态从待置改为分配", repDate, historyList);
		}else if(remFlw==2&&nextFlw==5){
			hql.append(",currStateId=:currState,devOwnerId=:devOwnerId,initState=1,nextFlowCd=5");
			hql.append(" where taskId=:taskId and currStateId=1");
			praVaMap.put("taskId", dto.getDetail().getTaskId());
			praVaMap.put("currState", 10);
			praVaMap.put("devOwnerId", dto.getAttr("5").toString());
			praVaMap.put("nextOwnerId", praVaMap.get("devOwnerId"));
			this.buildAdjustHistory(fHql.toString(), 1, 2, 10, "把状态从待置改为待改", repDate, historyList);
		}else if(remFlw==3&&nextFlw==4){
			hql.append(",currStateId=:currState,assinOwnerId=:anasiOwnId,initState=24,nextFlowCd=4");
			hql.append(" where taskId=:taskId and currStateId=24");
			praVaMap.put("taskId", dto.getDetail().getTaskId());
			praVaMap.put("currState", 25);
			praVaMap.put("anasiOwnId", dto.getAttr("4").toString());
			praVaMap.put("nextOwnerId", praVaMap.get("anasiOwnId"));
			this.buildAdjustHistory(fHql.toString(), 24, 3, 25, "把状态从分析改为分配", repDate, historyList);
		}else if(remFlw==3&&nextFlw==5){
			hql.append(",currStateId=:currState,devOwnerId=:devOwnerId,initState=24,nextFlowCd=5");
			hql.append(" where taskId=:taskId and currStateId=24");
			praVaMap.put("taskId", dto.getDetail().getTaskId());
			praVaMap.put("currState", 10);
			praVaMap.put("devOwnerId", dto.getAttr("5").toString());
			praVaMap.put("nextOwnerId", praVaMap.get("devOwnerId"));
			this.buildAdjustHistory(fHql.toString(), 24, 3, 10, "把状态从分析改为待改", repDate, historyList);
		}else if(remFlw==4&&nextFlw==5){
			hql.append(",currStateId=:currState,devOwnerId=:devOwnerId,initState=25,nextFlowCd=5");
			hql.append(" where taskId=:taskId and currStateId=25");
			praVaMap.put("taskId", dto.getDetail().getTaskId());
			praVaMap.put("currState", 10);
			praVaMap.put("devOwnerId", dto.getAttr("5").toString());
			praVaMap.put("nextOwnerId", praVaMap.get("devOwnerId"));
			this.buildAdjustHistory(fHql.toString(), 25, 4, 10, "把状态从分配改为待改", repDate, historyList);
		}else if(remFlw==6){
			hql.append(",currStateId=:currState,initState=18,nextFlowCd=8");
			hql.append(" where taskId=:taskId and currStateId=18");
			praVaMap.put("taskId", dto.getDetail().getTaskId());
			praVaMap.put("currState", 13);
			//这时候指定一个测试人员,不一下是原来的BUG提交人
			praVaMap.put("nextOwnerId", dto.getAttr("1").toString());
			this.buildAdjustHistory(fHql.toString(), 18, 6, 13, "把状态从交叉验证改为己改", repDate, historyList);
		}
		Map<String,Map> bugAdjustMap =(Map<String,Map>)dto.getAttr("bugAdjustMap");
		bugAdjustMap.put(hql.toString(), praVaMap);
		dto.setHistoryAdjustList(historyList);
	}
	
	private void buildAdjustHistory(String hql,Integer currStateId , Integer handFlwCd,
			Integer targetSta,String proLog ,Date repDate,List<BugHandHistory> hisList){
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		List<BugBaseInfo> list =  testTaskService.findByHql(hql, taskId,currStateId,repDate);
		String cancelFlwName = "";
		if(handFlwCd==2){
			cancelFlwName = "取消测试互验流程";
		}else if(handFlwCd==3){
			cancelFlwName = "取消分析流程";
		}else if(handFlwCd==4){
			cancelFlwName = "取消分配流程";
		}else if(handFlwCd==6){
			cancelFlwName = "取开发互检流程";
		}
		if(list!=null||!list.isEmpty()){
			for(BugBaseInfo bug :list){
				BugHandHistory bugHistory = new BugHandHistory();
				bugHistory.setInitState(currStateId);
				bugHistory.setBugState(targetSta);
				bugHistory.setHandlerId(SecurityContextHolderHelp.getUserId());
				bugHistory.setInsDate(new Date());
				bugHistory.setTestFlowCd(handFlwCd);
				bugHistory.setBugId(bug.getBugId());
				bugHistory.setTaskId(taskId);
				bugHistory.setModuleId(bug.getModuleId());
				bugHistory.setRemark(cancelFlwName);
				bugHistory.setHandResult(proLog);
				bugHistory.setCurrDayFinal(1);	
				hisList.add(bugHistory);
			}
		}

	}
	public View choiseTaskList(BusiRequestEvent req){
		TestTaskManagerDto dto = super.getDto(TestTaskManagerDto.class, req);
		String taskId = dto.getDetail().getTaskId();
		String projectId = dto.getDetail().getProjectId();
		this.writeResult(HtmlListComponent.toJsonList(getListObjs(taskId,projectId)));
		return super.getView("ajaxRest");
	}
	
	public View newVerValidate(BusiRequestEvent req){
		TestTaskManagerDto dto = super.getDto(TestTaskManagerDto.class, req);
		this.writeResult(testTaskService.createVerCheck(dto));
		return super.getView("ajaxRest");
	}
	private List<ListObject> getListObjs(String taskId,String projectId){
		HtmlListQueryObj queryObj = new HtmlListQueryObj();
		queryObj.setKeyPropertyName("id");
		queryObj.setValuePropertyName("name");
		Map<String,Object> calParaValues = new HashMap<String,Object>();
		calParaValues.put("companyId", SecurityContextHolderHelp.getCompanyId());
		calParaValues.put("projectId", projectId);
		calParaValues.put("id", taskId);
		queryObj.setParaValues(calParaValues);
		queryObj.setConditions(" where projectId=:projectId and id<>:id and companyId=:companyId and taskType<>0");
		queryObj.setHqlObjName(" Task");
		List<HtmlListQueryObj> list = new ArrayList<HtmlListQueryObj>();
		list.add(queryObj);
		return  testTaskService.getListData(list, false).get(0);
	}
	//修改提交前的预处理:设置流程及人员信息
	private void updatePrepare(TestTaskManagerDto dto){
		dto.setIsAjax("true");
		String taskId = dto.getDetail().getTaskId();
		dto.getDetail().setTestFlow(new HashSet<TestFlowInfo>());
		dto.getDetail().setUseActor(new HashSet<TaskUseActor>());
		List<TaskUseActor> actorList = testTaskService.findByHql("from TaskUseActor where taskId=?", taskId);
		List<TestFlowInfo> testFlwList = testTaskService.findByHql("from TestFlowInfo where taskId=?", taskId);
		
		dto.getDetail().setOldTestFlow(testTaskService.listToSet(testFlwList));
		dto.getDetail().setOldUseActor(testTaskService.listToSet(actorList));
		//很可能其他人员己建了大刚，所以这里用从库里查出来的重设置一下
		//dto.getDetail().setOutlineState(upingDetail.getOutlineState());
		//设置对应测试流程的人员及流程信息
		String flowStr = dto.getTestFlowStr();
		//下面的处理是为了防止前台篡改数据，把必选流程自动补上
		if("".equals(flowStr)){
			flowStr = "1,5,7,8";
			dto.setTestFlowStr(flowStr);
		}
		if(flowStr.indexOf("1")<0){
			flowStr = "1," +flowStr;
		}
		if(flowStr.indexOf("5")<0){
			flowStr = flowStr+",5" ;
		}
		if(flowStr.indexOf("7")<0){
			flowStr = flowStr +",7";
		}
		if(flowStr.indexOf("8")<0){
			flowStr = flowStr+",8";
		}
		String[] flowArr = flowStr.trim().split(",");
		for(String flowCode :flowArr){
			String[] actorIds = this.getUserByFlowCode(Integer.parseInt(flowCode), dto);
			for(String userId :actorIds){
				if(userId==null||"".equals(userId.trim())){
					continue;
				}
				TaskUseActor taskUA = new TaskUseActor(taskId,userId.trim(),Integer.parseInt(flowCode));
				taskUA.setIsEnable(1);
				dto.getDetail().getUseActor().add(taskUA);
				dto.setAttr(flowCode, userId);
			}
			TestFlowInfo testFlw = new TestFlowInfo(taskId,Integer.parseInt(flowCode));
			dto.getDetail().getTestFlow().add(testFlw);
		}
		setDisableFlwActr(dto);
		
	}
	

	//设置选了人员，但流程没选择的人员
	private void setDisableFlwActr(TestTaskManagerDto dto){
		String flowStr = dto.getTestFlowStr();
		String[] flowArr = flowStr.trim().split(",");
		//找到没选用的流程
		List<Integer> disaFlwList = new ArrayList<Integer>();
		for(int i=1; i<12; i++){
			boolean checkedFlw =false;
			for(String flwCode :flowArr){
				if(Integer.parseInt(flwCode)==i){
					checkedFlw = true;
					break;
				}
			}
			//没有10这流程
			if(!checkedFlw&&i!=10){
				disaFlwList.add(i);
			}
		}
		String taskId = dto.getDetail().getTaskId();
		for(Integer flowCode:disaFlwList){
			String[] actorIds = this.getUserByFlowCode(flowCode, dto);
			if(actorIds==null){
				continue;
			}
			for(String userId :actorIds){
				if(userId==null||"".equals(userId.trim())){
					continue;
				}
				TaskUseActor taskUA = new TaskUseActor(taskId,userId.trim(),flowCode);
				taskUA.setIsEnable(0);
				dto.getDetail().getUseActor().add(taskUA);
			}		
		}
	}
	
	private String[] getUserByFlowCode(Integer flowCode,TestTaskManagerDto dto){
		
		switch(flowCode){
		case 1:
			return dto.getTesterId().trim().split(" ") ;
		case 2:
			return dto.getTesterConfId().trim().split(" ") ;
		case 3:
			return dto.getAnalyserId().trim().split(" ") ;
		case 4:
			return dto.getAssignationId().trim().split(" ");
		case 5:
			return dto.getProgrammerId().trim().split(" ");
		case 6:
			return dto.getEmpolderAffirmantId().trim().split(" ");
		case 7:
			return dto.getEmpolderLeaderId().trim().split(" ");
		case 8:
			return dto.getTestLeadId().trim().split(" ");
		case 9:
			return dto.getCaseReviewId().trim().split(" ");
		case 11:
			return dto.getProRelaPersonId().trim().split(" ");
		default:
			return null ;
		}
	}
	public View selectMember3(BusiRequestEvent req){
		TestTaskManagerDto dto = super.getDto(TestTaskManagerDto.class, req);
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct u.id as userId, u.name as userName \n");
		sb.append("  from t_task_resource_real tr\n");
		sb.append("  join t_resource r on tr.resourceid = r.id\n");
		sb.append("  left join t_user_group_rela ug on r.userid = ug.userid\n");
		sb.append("  join t_user u on u.id = r.userid\n");
		String groupId = dto.getGroupId();
		if(groupId!= null &&! "".equals(groupId)&&!"-1".equals(groupId)){
			sb.append("where ug.GROUPID =?");
		}
		List<Object[]> memberList = new ArrayList<Object[]>();
		if(groupId!= null &&! "".equals(groupId)&&!"-1".equals(groupId)){
			memberList = (List<Object[]>)testTaskService.findBySql(sb.toString().replaceAll("left join", "join"),null,groupId);
		}else{
			memberList = (List<Object[]>)testTaskService.findBySql(sb.toString(),null);
		}
		List<JsonInterface> memberes = new ArrayList<JsonInterface>();
		for(Object[] objs:memberList){
			ListObject lb = new ListObject(objs[0].toString(),objs[1].toString());
			memberes.add(lb);
		}
		super.writeResult(super.listToJson(memberes));
		memberes = null;
		return super.getView("ajaxRest");
		
	}

	/**
	 * 查询任务参与人员
	 * @param req
	 * @return
	 */
	public View selectMember(BusiRequestEvent req){
		TestTaskManagerDto dto = super.getDto(TestTaskManagerDto.class, req);
		String hql = buildMemberHql();
		List<JsonInterface> memberList = null;
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		String compId = SecurityContextHolderHelp.getCompanyId();
		String activeProId = this.getActiveProId(taskId);
		String activeTaskId = this.getActiveTaskId(taskId, activeProId);
		memberList = (List<JsonInterface>)testTaskService.findByHql(hql,activeTaskId,compId);
		if(memberList==null){
			memberList = new ArrayList<JsonInterface>();
		}
		super.writeResult(super.listToJson(memberList));
		memberList = null;
		return super.getView("ajaxRest");
	}


	public View selectProMember(BusiRequestEvent req){
		TestTaskManagerDto dto = super.getDto(TestTaskManagerDto.class, req);
		String hql = buildProMemberHql();
		List<JsonInterface> memberList = new ArrayList<JsonInterface>();
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		String compId = SecurityContextHolderHelp.getCompanyId();
		String activeProId = this.getActiveProId(taskId);
		memberList = (List<JsonInterface>)testTaskService.findByHql(hql,activeProId,compId);
		super.writeResult(super.listToJson(memberList));
		memberList = null;
		return super.getView("ajaxRest");
		
	}
	private String getActiveProId(String initTaskId){
		
		String hql = "select new cn.com.codes.framework.common.ListObject(t.projectId as keyObj ,t.name as valueObj)   from  Task t where id=? and companyId=?" ;
		String compId = SecurityContextHolderHelp.getCompanyId();
		List<ListObject> taskList = testTaskService.findByHql(hql,initTaskId,compId);
		if(taskList==null||taskList.isEmpty()){
			return null;
		}
		ListObject task = taskList.get(0);
		String initProId = task.getKeyObj();
		//hql = "select new cn.com.codes.framework.common.ListObject(t.id as keyObj ,t.name as valueObj,t.realProjectId as remark)   from  Project t where status=1 and realProjectId=?";
		hql = "select new cn.com.codes.framework.common.ListObject(t.id as keyObj ,t.name as valueObj,t.realProjectId as remark)   from  Project t where  t.id=?";
		
		List<ListObject> proList = testTaskService.findByHql(hql,initProId);
		ListObject proInfo = proList.get(0);
		//if(proInfo.getKeyObj().equals(proInfo.getRemark())){
			initProId = proInfo.getRemark();
		//}else{
		//	hql = "select new cn.com.codes.framework.common.ListObject(t.id as keyObj ,t.name as valueObj,t.realProjectId as remark)   from  Project t where  id=?";
		//}
		//if(proList==null||proList.isEmpty()){
			hql = "select new cn.com.codes.framework.common.ListObject(t.id as keyObj ,t.name as valueObj)   from  Project t where status=1 and realProjectId=?";
			proList = testTaskService.findByHql(hql,initProId);
			if(proList==null||proList.isEmpty()){
				return null;
			}
		//}
		taskList = null;
		String activeProId = proList.get(0).getKeyObj();
		proList = null;
		return activeProId;
	}
	private String getActiveTaskId(String initTaskId,String activeProId){
		String hql = "select new cn.com.codes.framework.common.ListObject(t.id as keyObj ,t.name as valueObj)   from Task t  where projectId=? and versionId=?" ;
		List<ListObject> taskList = testTaskService.findByHql(hql,activeProId,initTaskId);
		if(taskList==null||taskList.isEmpty()){
			return null;
		}
		return taskList.get(0).getKeyObj();
	}
	private String buildMemberHql(){ 
		
		StringBuffer sb = new StringBuffer("select new cn.com.codes.framework.common.ListObject( u.id as keyObj,(u.loginName||'('||u.name||')') as valueObj )");
		sb.append(" from TaskResourceReal tr join tr.resource r  join r.relaUser u  where tr.taskId=? and tr.companyId=? and r.type=0");
		return sb.toString();
	}

	private String buildProMemberHql(){ 
		
		StringBuffer sb = new StringBuffer("select distinct new cn.com.codes.framework.common.ListObject( u.id as keyObj,(u.loginName||'('||u.name||')') as valueObj )");
		sb.append(" from TaskResourceReal tr join tr.resource r  join r.relaUser u  where tr.projectId=? and tr.companyId=? and r.type=0");
		return sb.toString();
	}
	private void init(TestTaskManagerDto dto){
		this.setUserActorInfo(dto);
		this.setTestFlowInfo(dto);
	}
	
	private void initWhenReady(TestTaskManagerDto dto){
		TestTaskDetail detail  = dto.getDetail();
		dto.setDetail(detail);
		dto.setTestFlowStr("1,5,7,8");
		detail.setUpgradeFlag(0); 
	}
	private void setTestFlowInfo(TestTaskManagerDto dto){
		List<TestFlowInfo>    testFlowList = dto.getTestFlowList() ;
		StringBuffer flowKeyValueSb = new StringBuffer();
		StringBuffer flowStrSb = new StringBuffer();
		for(TestFlowInfo tf :testFlowList){
			Integer flowCode = tf.getTestFlowCode();
			switch(flowCode){
			case 1:
				 flowKeyValueSb.append("^reportBug=true");
				 flowStrSb.append(",").append(flowCode);
				 break;
			case 2:
				flowKeyValueSb.append("^testerBugConfirm=true");
				flowStrSb.append(",").append(flowCode);
				break;
			case 3:
				flowKeyValueSb.append("^analyse=true");
				flowStrSb.append(",").append(flowCode);
				break;
			case 4:
				flowKeyValueSb.append("^assignation=true");
				flowStrSb.append(",").append(flowCode);
				break;
			case 5:
				flowKeyValueSb.append("^devFixBug=true");
				flowStrSb.append(",").append(flowCode);
				break;
			case 6:
				flowKeyValueSb.append("^devConfirmFix=true");
				flowStrSb.append(",").append(flowCode);
				break;
			case 7:
				flowKeyValueSb.append("^arbitrateBug=true");
				flowStrSb.append(",").append(flowCode);
				break;
			case 8:
				flowKeyValueSb.append("^testerVerifyFix=true");
				flowStrSb.append(",").append(flowCode);
				break;
			case 9:
				flowKeyValueSb.append("^caseReview=true");
				flowStrSb.append(",").append(flowCode);
				break;
			case 11:
				flowKeyValueSb.append("^proRelaPersonFlg=true");
				flowStrSb.append(",").append(flowCode);
				break;
			default:
				break;
			}
		}
		dto.setTestFlwKeyValueStr(flowKeyValueSb.toString());
		dto.setTestFlowStr(flowStrSb.length()>2?flowStrSb.substring(1).toString():"");
		dto.setInitFlowStr(dto.getTestFlowStr());
	}
	
	private void setUserActorInfo(TestTaskManagerDto dto){
		//用例Review 人员ID
		StringBuffer caseReviewId = new StringBuffer("");
		// 测试人员ID
		StringBuffer testerId = new StringBuffer("");
		// 测试确认人ID
		StringBuffer testerConfId = new StringBuffer("");
		// 分析人ID
		StringBuffer analyserId = new StringBuffer("");
		// 分配人ID
		StringBuffer assignationId = new StringBuffer("");
		// 编码人员ID
		StringBuffer programmerId = new StringBuffer("");
		// 开发确认人ID
		StringBuffer empolderAffirmantId = new StringBuffer("");
		// 开发负责人ID
		StringBuffer empolderLeaderId = new StringBuffer("");
		//测试负责人ID
		StringBuffer testLeadId  = new StringBuffer("");
		
		
		//关注人ID
		StringBuffer proRelaPersonId  = new StringBuffer("");
		
		//用例Review 人员 caseReviewer 
		StringBuffer caseReviewer = new StringBuffer("");
		// 测试人员
		StringBuffer tester = new StringBuffer("");
		//测试确认人
		StringBuffer testerConf = new StringBuffer("");
		// 分析人
		StringBuffer analyser = new StringBuffer("");
		// 分配人
		StringBuffer assigner = new StringBuffer("");

		// 编码人员
		StringBuffer programmer = new StringBuffer("");
		// 开发确认人
		StringBuffer empolderAffirmant = new StringBuffer("");
		// 开发负责人
		StringBuffer empolderLeader = new StringBuffer("");
		//测试负责人
		StringBuffer testLead = new StringBuffer("");
		
		//关注人
		StringBuffer proRelaPerson = new StringBuffer("");
		
		// 查询测试任务中不同环境的参与人员
		
		List<VTestkTaskActor> testActroList = dto.getTestActroList();
		for (VTestkTaskActor taskActor : testActroList) {
			switch (taskActor.getActor().intValue()) {
			case 1:
				testerId.append(" ").append(taskActor.getUserId());
				tester.append(",").append(taskActor.getUserName());
				break;
			case 2:
				testerConfId.append(" ").append(taskActor.getUserId());
				testerConf.append(",").append(taskActor.getUserName());
				break;
			case 3:
				analyserId.append(" ").append(taskActor.getUserId());
				analyser.append(",").append(taskActor.getUserName());
				break;
			case 4:
				assignationId.append(" ").append(taskActor.getUserId());
				assigner.append(",").append(taskActor.getUserName());
				break;
			case 5:
				programmerId.append(" ").append(taskActor.getUserId());
				programmer.append(",").append(taskActor.getUserName());
				break;
			case 6:
				empolderAffirmantId.append(" ").append(taskActor.getUserId());
				empolderAffirmant.append(",").append(taskActor.getUserName());
				break;
			case 7:
				empolderLeaderId.append(" ").append(taskActor.getUserId());
				empolderLeader.append(",").append(taskActor.getUserName());
				break;
			case 8:
				testLeadId.append(" ").append(taskActor.getUserId());
				testLead.append(",").append(taskActor.getUserName());
				break;
			case 9:
				caseReviewId.append(" ").append(taskActor.getUserId());
				caseReviewer.append(",").append(taskActor.getUserName());
				break;
			case 11:
				proRelaPersonId.append(" ").append(taskActor.getUserId());
				proRelaPerson.append(",").append(taskActor.getUserName());
				break;
			default:
				break;
			}
		}
		dto.setTesterId(testerId.length()>2?testerId.substring(1):"");
		dto.setTester(tester.length()>2?tester.substring(1):"");
		dto.setTesterConfId(testerConfId.length()>2?testerConfId.substring(1):"");
		dto.setTesterConf(testerConf.length()>2?testerConf.substring(1):"");
		dto.setAnalyserId(analyserId.length()>2?analyserId.substring(1):"");
		dto.setAnalyser(analyser.length()>2?analyser.substring(1):"");
		dto.setAssignationId(assignationId.length()>2?assignationId.substring(1):"");
		dto.setAssigner(assigner.length()>2?assigner.substring(1):null);
		dto.setProgrammerId(programmerId.length()>2?programmerId.substring(1):"");
		dto.setProgrammer(programmer.length()>2?programmer.substring(1):"");
		dto.setEmpolderAffirmantId(empolderAffirmantId.length()>2?empolderAffirmantId.substring(1):"");
		dto.setEmpolderAffirmant(empolderAffirmant.length()>2?empolderAffirmant.substring(1):"");
		dto.setEmpolderLeaderId(empolderLeaderId.length()>2?empolderLeaderId.substring(1):"");
		dto.setEmpolderLeader(empolderLeader.length()>2?empolderLeader.substring(1):"");
		dto.setTestLeadId(testLeadId.length()>2?testLeadId.substring(1):"");
		dto.setTestLead(testLead.length()>2?testLead.substring(1):"");
		dto.setCaseReviewId(caseReviewId.length()>2?caseReviewId.substring(1):"");
		dto.setCaseReviewer(caseReviewer.length()>2?caseReviewer.substring(1):""); 
		
		dto.setProRelaPersonId(proRelaPersonId.length()>2?proRelaPersonId.substring(1):"");
		dto.setProRelaPerson(proRelaPerson.length()>2?proRelaPerson.substring(1):"");
	}

	public TestTaskDetailService getTestTaskService() {
		return testTaskService;
	}

	public void setTestTaskService(TestTaskDetailService testTaskService) {
		this.testTaskService = testTaskService;
	}
}
