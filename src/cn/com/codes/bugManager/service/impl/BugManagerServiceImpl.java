package cn.com.codes.bugManager.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.codes.bugManager.blh.BugFlowConst;
import cn.com.codes.bugManager.dto.BugManagerDto;
import cn.com.codes.bugManager.dto.BugShortMsgDto;
import cn.com.codes.bugManager.dto.RelaCaseDto;
import cn.com.codes.bugManager.service.BugCommonService;
import cn.com.codes.bugManager.service.BugFlowControlService;
import cn.com.codes.bugManager.service.BugManagerService;
import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.util.FileInfoVo;
import cn.com.codes.framework.app.services.BaseServiceImpl;
import cn.com.codes.framework.common.ListObject;
import cn.com.codes.object.BugBaseInfo;
import cn.com.codes.object.BugHandHistory;
import cn.com.codes.object.BugShortMsg;
import cn.com.codes.object.CaseBugRela;
import cn.com.codes.object.CaseExeHistory;
import cn.com.codes.object.DefaultTypeDefine;
import cn.com.codes.object.FileInfo;
import cn.com.codes.object.OutlineInfo;
import cn.com.codes.object.SoftwareVersion;
import cn.com.codes.object.TestCaseInfo;
import cn.com.codes.object.TestResult;
import cn.com.codes.object.TypeDefine;
import cn.com.codes.object.User;
import cn.com.codes.testTaskManager.dto.CurrTaskInfo;
import cn.com.codes.testTaskManager.service.TestTaskDetailService;

public class BugManagerServiceImpl extends BaseServiceImpl implements
		BugManagerService {

	private BugCommonService bugCommonService;
	private TestTaskDetailService testTaskService ;
	private BugFlowControlService bugFlowControlService ;

	
	public void bugRelaCase(RelaCaseDto dto){
		String hql = "from CaseBugRela where bugId=?";
		List list = this.findByHql(hql, dto.getBugId());
		String myId = SecurityContextHolderHelp.getUserId();
		if((dto.getTestCaseIds()==null||dto.getTestCaseIds().trim().equals(""))
				&&list!=null&&list.size()>0){
			this.getHibernateGenericController().executeUpdate("delete from CaseBugRela where bugId=?", dto.getBugId());
			this.getHibernateGenericController().executeUpdate("update BugBaseInfo set relaCaseFlag=0 where bugId=?", dto.getBugId());
		}else if(list!=null&&list.size()>0&&list.size()>0&&dto.getTestCaseIds()!=null&&!dto.getTestCaseIds().trim().equals("")){
			Set<CaseBugRela> newSet = new HashSet<CaseBugRela>();
			String[] caseIdsArr = dto.getTestCaseIds().split(",");
			int i=0;
			for(String caseIdStr:caseIdsArr){
				if(caseIdStr.trim().equals("")){
					continue;
				}
				CaseBugRela cbr = new CaseBugRela();
				cbr.setCreaterId(myId);
				cbr.setBugId(dto.getBugId());
				cbr.setTestCaseId(Long.valueOf(caseIdStr));
				newSet.add(cbr);
				i++;
			}
			Set<CaseBugRela> oldSet = new HashSet<CaseBugRela>();
			oldSet.addAll(list);
			int oldCount = oldSet.size();
			this.saveRealSet(oldSet, newSet, "bugCaseRelaId", null);
			if(oldSet.size()!=oldCount){
				this.getHibernateGenericController().executeUpdate("update BugBaseInfo set relaCaseFlag=? where bugId=?", i,dto.getBugId());
			}
			oldSet = null;
			this.excuteBatchHql(dto.getHql(), dto.getHqlParamMaps());
			this.writeRelaTestReuslt(dto);
		}else if((list==null||list.size()==0)&&dto.getTestCaseIds()!=null&&!dto.getTestCaseIds().trim().equals("")){
			String[] caseIdsArr = dto.getTestCaseIds().trim().split(",");
			int i=0;
			for(String caseIdStr:caseIdsArr){
				if(caseIdStr.trim().equals("")){
					continue;
				}
				CaseBugRela cbr = new CaseBugRela();
				cbr.setBugCaseRelaId(null);
				cbr.setBugId(dto.getBugId());
				cbr.setTestCaseId(Long.valueOf(caseIdStr));
				cbr.setCreaterId(myId);
				this.add(cbr);	
				i++;
			}
			this.getHibernateGenericController().executeUpdate("update BugBaseInfo set relaCaseFlag=? where bugId=?", i,dto.getBugId());
			this.excuteBatchHql(dto.getHql(), dto.getHqlParamMaps());
			this.writeRelaTestReuslt(dto);
		}
	}
	
	private void writeRelaTestReuslt(RelaCaseDto dto){
		
		List<TestResult> resultList = (List<TestResult>)dto.getAttr("resultList");
		List<TestResult> currCaseTestRedList = null;
		if(dto.getIsExeRela()==1){
			currCaseTestRedList = new ArrayList<TestResult>();
		}
		boolean haveRedNoTest = false;
		for(TestResult rest :resultList){
			Long testCaseId = rest.getTestCaseId();
			String taskId = SecurityContextHolderHelp.getCurrTaksId();
			String hql ="select new TestResult(resultId) from TestResult where testCaseId=? and testVer=? and taskId=?";
			List<TestResult> list = this.findByHql(hql, testCaseId,rest.getTestVer(),taskId);
			//当前页中含未测试的用例，时只要取任意一上测试结果放入list
			if(dto.getIsExeRela()==1&&dto.getCurrCaseTestRedSet().indexOf("_0")>0&&!haveRedNoTest){
				if(dto.getCurrCaseTestRedSet().indexOf(rest.getTestCaseId()+"_"+rest.getTestVer())<0){
					currCaseTestRedList.add(rest);
					//只加一次
					haveRedNoTest = true;
				}
			}
			if(list==null||list.size()==0){
				this.add(rest);
			}else{
				TestResult restOld = list.get(0);
				Long restId = restOld.getResultId();
				rest.setResultId(restId);
				if(dto.getIsExeRela()==1&&dto.getCurrCaseTestRedSet().indexOf(rest.getTestCaseId()+"_"+rest.getTestVer())>=0){
					currCaseTestRedList.add(rest);
				}
				this.update(rest);
				//可能此前因并发多出一条测试记录来，所以要删掉
				if(list.size()>1){
					hql="delete from TestResult where testCaseId=? and testVer=? and taskId=? and resultId<>? ";
					this.getHibernateGenericController().executeUpdate(hql, testCaseId,rest.getTestVer(),taskId,restId);
				}
			}
		}
		dto.setCurrCaseTestRedList(currCaseTestRedList);
		List<CaseExeHistory> hisList = (List<CaseExeHistory>)dto.getAttr("hisList");
		for(CaseExeHistory his :hisList){
			this.add(his);
		}
		hisList = null;
		dto.clearDataContainer();
		resultList = null;
	}
	public void caseRelaBug(RelaCaseDto dto){
		
		String hql = "from CaseBugRela where testCaseId=?";
		List<CaseBugRela> list = this.findByHql(hql, dto.getTestCaseId());
		if((dto.getBugIds()==null||dto.getBugIds().trim().equals(""))&&list!=null&&list.size()>0){
			this.getHibernateGenericController().executeUpdate("delete from CaseBugRela where testCaseId=?", dto.getTestCaseId());
			for(CaseBugRela cbr:list){
				this.getHibernateGenericController().executeUpdate("update BugBaseInfo set relaCaseFlag=relaCaseFlag-1 where bugId=?",cbr.getBugId());
			}
		}else if(dto.getBugIds()!=null&&!dto.getBugIds().trim().equals("")&&list!=null&&list.size()>0){
			String[] bugIdsArr = dto.getBugIds().trim().split(" ");
			Set<CaseBugRela> newSet = new HashSet<CaseBugRela>();
			Set<CaseBugRela> oldSet = new HashSet<CaseBugRela>();
			oldSet.addAll(list);
			for(String bugId:bugIdsArr){
				if(bugId.trim().equals("")){
					continue;
				}
				CaseBugRela cbr = new CaseBugRela();
				cbr.setBugId(new Long(bugId));
				cbr.setTestCaseId(dto.getTestCaseId());
				newSet.add(cbr);
				if(!oldSet.contains(cbr)){
					this.getHibernateGenericController().executeUpdate("update BugBaseInfo set relaCaseFlag=relaCaseFlag+1 where bugId=?",cbr.getBugId());
				}
			}
			Object[] objs = oldSet.toArray();
			for(int i=0; i<objs.length; i++){
				CaseBugRela oldCbr = (CaseBugRela)objs[i];
				if(!newSet.contains(oldCbr)){
					this.getHibernateGenericController().executeUpdate("update BugBaseInfo set relaCaseFlag=relaCaseFlag-1 where bugId=?",oldCbr.getBugId());
				}
			}
			this.saveRealSet(oldSet, newSet, "bugCaseRelaId", null);
			oldSet = null;
			String upCaseHql = "update TestCaseInfo set testStatus=3 ,auditId=?, upddate=? where testCaseId=?" ;
			this.getHibernateGenericController().executeUpdate(upCaseHql, SecurityContextHolderHelp.getUserId(),new Date(),dto.getTestCaseId());
			this.writeRelaTestReuslt(dto);
		}else if(dto.getBugIds()!=null&&!dto.getBugIds().trim().equals("")&&(list!=null||list.size()==0)){
			String[] bugIdsArr = dto.getBugIds().split(" ");
			for(String bugId:bugIdsArr){
				if(bugId.trim().equals("")){
					continue;
				}
				CaseBugRela cbr = new CaseBugRela();
				cbr.setBugCaseRelaId(null);
				cbr.setBugId(new Long(bugId));
				cbr.setTestCaseId(dto.getTestCaseId());
				this.add(cbr);	
				this.getHibernateGenericController().executeUpdate("update BugBaseInfo set relaCaseFlag=relaCaseFlag+1 where bugId=?",new Long(bugId));
			}
			String upCaseHql = "update TestCaseInfo set testStatus=3 ,auditId=?, upddate=? where testCaseId=?" ;
			this.getHibernateGenericController().executeUpdate(upCaseHql, SecurityContextHolderHelp.getUserId(),new Date(),dto.getTestCaseId());
			this.writeRelaTestReuslt(dto);
		}
	}


	
	public List getRelaCase(RelaCaseDto dto){
		StringBuffer hql = new StringBuffer("select new TestCaseInfo(testCaseId,testCaseDes,testStatus,caseTypeId,priId)");
		hql.append(" from TestCaseInfo where moduleId=? and taskId=? and testStatus  !=0 and testStatus  !=6 ");
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		List caseList= this.findByHqlPage(hql.toString(), dto.getPageNo(), dto.getPageSize(), "testCaseId", dto.getModuleId(),taskId);
		if(caseList.size()>0){
			String caseHql = "from CasePri where compId=? or compId=1";
			String compId = SecurityContextHolderHelp.getCompanyId();
			List<TypeDefine> typeList = this.findByHql(caseHql, compId);
			caseHql = "from CaseType where compId=? or compId=1";
			typeList.addAll(this.findByHql(caseHql, compId));
			dto.setTypeList(typeList);
			String qHql = "select new BugBaseInfo(bugId,relaCaseFlag,bugReptVer) from BugBaseInfo where bugId=? and  taskId=?";
			BugBaseInfo bug = (BugBaseInfo)this.findByHql(qHql, dto.getBugId(),taskId).get(0);
			if(bug.getRelaCaseFlag()!=null&&bug.getRelaCaseFlag()>=1){
				qHql = "select new cn.com.codes.object.TestCaseInfo(cb.testCaseId as testCaseId) from CaseBugRela cb where cb.bugId=?" ;
				List<TestCaseInfo> relaCaseList = this.findByHql(qHql, dto.getBugId());
				dto.setRelaCaseList(relaCaseList);
				dto.setBugReptVer(bug.getBugReptVer());
			}
			bug = null;
		}
		return caseList;
	}
	
	
	public List<BugBaseInfo> getRelaBug(RelaCaseDto dto){
		StringBuffer hql = new StringBuffer("select new BugBaseInfo(");
		hql.append(" bugId,bugDesc,bugGradeId,bugFreqId,bugOccaId,").append(
				" bugTypeId,priId, devOwnerId,reptDate,msgFlag,").append(
				" relaCaseFlag, moduleId, testOwnerId,currFlowCd,").append(
				" currHandlerId,currStateId,nextFlowCd,bugReptId)").append(
				" from BugBaseInfo b where b.moduleId=? and b.taskId=?");
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		List<BugBaseInfo> list = this.findByHqlPage(hql.toString(),
				dto.getPageNo(), dto.getPageSize(), "bugId", dto.getModuleId(),
				taskId);
		if(list.size()==0){
			return list;
		}

		String HqlStr = "select new TypeDefine(typeId,typeName) from TypeDefine where compId=? or compId=1";
		String compId = SecurityContextHolderHelp.getCompanyId();
		Long typeCount = this.getHibernateGenericController().getResultCount(HqlStr, new Object[]{compId},"typeId");
		if(typeCount<100){
			HqlStr = "from TypeDefine where compId=? or compId=1";
			List<TypeDefine> typeList = this.findByHql(HqlStr, compId);
			dto.setTypeList(typeList);
		}
		String myUserId = SecurityContextHolderHelp.getUserId();
		String myName = SecurityContextHolderHelp.getMyRealDisplayName();
		String hqlStr = "select new  User(id,name ,loginName) from User where id=?" ;
		User own = null;
		for(BugBaseInfo bug :list){
			if(bug.getTestOwnerId().equals(myUserId)){
				bug.settestName(myName);
			}else{
				own = (User)this.findByHql(hqlStr, bug.getTestOwnerId()).get(0);
				bug.settestName(own.getUniqueName());
			}
			if(bug.getDevOwnerId()!=null&&!"".equals(bug.getDevOwnerId())){
				if(myUserId.equals(bug.getDevOwnerId())){
					bug.setDevName(myName);
				}else{
					own = (User)this.findByHql(hqlStr, bug.getDevOwnerId()).get(0);
					bug.setDevName(own.getUniqueName());					
				}
			}
			own = null;
		}
		HqlStr = "select new cn.com.codes.object.BugBaseInfo(cb.bugId as bugId) from CaseBugRela cb where cb.testCaseId=?" ;
		List<BugBaseInfo> relaBugList = this.findByHql(HqlStr, dto.getTestCaseId());
		dto.setRelaBugList(relaBugList);
		return list;
	}
	public void sendMsg(BugShortMsg shortMsg){
		this.add(shortMsg);
		String hql = "update BugBaseInfo set msgFlag=? where bugId=? and taskId=?";
		this.getHibernateGenericController().executeUpdate(hql, shortMsg.getRecipCd(),shortMsg.getBugId(),shortMsg.getTaskId());
	}
	public List<Object[]> findShortMsg(BugShortMsgDto dto){
		StringBuffer hql = new StringBuffer();
		
		hql.append("select msg.msgId, u.loginName||'('||u.name||')' ,msg.message,msg.recipCd,msg.insDate ");
		hql.append("from BugShortMsg msg,User u where msg.bugId= ? and msg.senderId=u.id and msg.taskId=? order by msg.insDate desc");
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		List<Object[]> list = this.findByHqlPage(hql.toString(), dto.getPageNo(), dto.getPageSize(), "msg.msgId", dto.getShortMsg().getBugId(),taskId);
		if(list.size()>0){
			this.reSetMsgFlag(dto);
		}
		return list;
	}

	private void reSetMsgFlag(BugShortMsgDto dto){
		
		StringBuffer hql = new StringBuffer("select new BugBaseInfo");
		hql.append(" (bugId,devOwnerId,testOwnerId,analyseOwnerId, assinOwnerId, intercessOwnerId,msgFlag) ");
		hql.append(" from BugBaseInfo where bugId=?");
		Long bugId =  dto.getShortMsg().getBugId();
//		String upHql = "update BugBaseInfo set msgFlag=9 where bugId=? and msgFlag in(1,3,4,5,7) ";
//		this.getHibernateGenericController().executeUpdate(upHql, bugId);
		BugBaseInfo bug = (BugBaseInfo)this.findByHql(hql.toString(), bugId).get(0);
		String myId = SecurityContextHolderHelp.getUserId();

		dto.setReSetMsgLink(0);
		if(bug.getMsgFlag()==1&&myId.equals(bug.getTestOwnerId())){
			//this.getHibernateGenericController().executeUpdate(upHql, bugId);
			dto.setReSetMsgLink(1);
		}else if(bug.getMsgFlag()==3&&myId.equals(bug.getAnalyseOwnerId())){
			//this.getHibernateGenericController().executeUpdate(upHql, bugId);
			dto.setReSetMsgLink(1);
		}else if(bug.getMsgFlag()==4&&myId.equals(bug.getAssinOwnerId())){
			//this.getHibernateGenericController().executeUpdate(upHql, bugId);
			dto.setReSetMsgLink(1);
		}else if(bug.getMsgFlag()==5&&myId.equals(bug.getDevOwnerId())){
			//this.getHibernateGenericController().executeUpdate(upHql, bugId);
			dto.setReSetMsgLink(1);
		}else if(bug.getMsgFlag()==7&&myId.equals(bug.getIntercessOwnerId())){
			//this.getHibernateGenericController().executeUpdate(upHql, bugId);
			dto.setReSetMsgLink(1);
		}
	}
	public void delete(Long bugId){
		String hql ="delete from BugBaseInfo where bugId=? and taskId=?";
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		this.getHibernateGenericController().executeUpdate(hql, bugId,taskId);
		hql = "delete from BugHandHistory where bugId = ?";
		this.getHibernateGenericController().executeUpdate(hql, bugId);
		hql = "delete from CaseBugRela where bugId=?" ;
		this.getHibernateGenericController().executeUpdate(hql, bugId);
		
		hql = "delete from FileInfo where typeId=?" ;
		this.getHibernateGenericController().executeUpdate(hql, bugId);
	}
	public void update(BugManagerDto dto){
		
		this.buildBugUpHql(dto);
		this.executeUpdateByHqlWithValuesMap(dto.getHql(), dto.getHqlParamMaps());
		dto.setHqlParamMaps(null);
		String myId = SecurityContextHolderHelp.getUserId();
		String hql = "select new User(id,name ,loginName) from User where id=?" ;
		//从前台取，这里就不用处理了
		User Owner = null;
		BugBaseInfo bug =dto.getBug();
		if(myId.equals(bug.getTestOwnerId())){
			bug.settestName(SecurityContextHolderHelp.getMyRealDisplayName());
		}else{
			Owner = (User)this.findByHql(hql, bug.getTestOwnerId()).get(0);
			bug.settestName(Owner.getUniqueName());			
		}
		if(bug.getDevOwnerId()!=null&&!"".equals(bug.getDevOwnerId())){
			Owner = (User)this.findByHql(hql, dto.getBug().getDevOwnerId()).get(0);
			dto.getBug().setDevName(Owner.getUniqueName());
		}
	}
	private void  buildBugUpHql(BugManagerDto dto){
		BugBaseInfo bug =dto.getBug();
		//以后改成用HQL方式保存，只存部分属性，防止并发时，测试人员的修改覆盖开发人员处理的状态
		StringBuffer upHql = new StringBuffer("update BugBaseInfo set moduleId=:moduleId,");
		upHql.append("bugDesc=:bugDesc,bugTypeId=:bugTypeId,bugGradeId=:bugGradeId,platformId=:platformId,");
		upHql.append("sourceId=:sourceId,bugOccaId=:bugOccaId,geneCauseId=:geneCauseId,");
		upHql.append("priId=:priId,bugFreqId=:bugFreqId,reProStep=:reProStep,reProTxt=:reProTxt,");
		upHql.append("reproPersent=:reproPersent,bugReptVer=:bugReptVer,moduleNum=:moduleNum ");
		Map praValues = new HashMap(15);
		if(dto.getBug().getAttachUrl()!=null&&!"".equals(dto.getBug().getAttachUrl().trim())){
			upHql.append(" ,attachUrl=:attachUrl");
			praValues.put("attachUrl", dto.getBug().getAttachUrl());
		}
		upHql.append(" where bugId=:bugId and taskId=:taskId");
		dto.setHql(upHql.toString());
		dto.setHqlParamMaps(praValues);
		praValues.put("moduleNum", bug.getModuleNum());
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
		praValues.put("bugReptVer", bug.getBugReptVer());
		praValues.put("taskId", dto.getTaskId());
		praValues.put("reproPersent", bug.getReproPersent());
		
	}
	public List<BugBaseInfo> loadBug(BugManagerDto dto) {
		return bugCommonService.findBug(dto);
	}
	public void upInitPrepare(BugManagerDto dto){
		CurrTaskInfo currTaskInfo = testTaskService.getCurrTaskDetalInfo();
		dto.setCurrTaskInfo(currTaskInfo);
		dto.getBug().setTestPhase(currTaskInfo.getTestPhase());
		BugBaseInfo bug  = null;
		String hql = "from BugBaseInfo where bugId = ? and taskId=? ";
		bug = (BugBaseInfo)this.findByHql(hql, dto.getBug().getBugId(),dto.getTaskId()).get(0);
		dto.setBug(bug);
		dto.setInitReProStep(bug.getReProStep());
		bug.setTestFlow(currTaskInfo.getTestFlow());
		bug.setRoleInTask(currTaskInfo.getRoleInTask());
		if(dto.getLoadType()==1){
			List<TypeDefine> typeList = bugCommonService.getBugListData();
			dto.setTypeList(typeList);
			List<ListObject> verList =  testTaskService.getVerSelList();
			dto.setVerList(verList);
		}
		Long verInfoId = dto.getBug().getCurrVerInfo();
		if(verInfoId==null){
			hql = "select new SoftwareVersion(versionId,versionNum) from SoftwareVersion where versionId=? ";
			List<SoftwareVersion> list = bugCommonService.findByHql(hql, dto.getBug().getBugReptVer());
			if(list!=null&&!list.isEmpty()){
				SoftwareVersion reptVer = list.get(0);
				dto.getBug().setReptVersion(reptVer);
			}			
		}else{
			hql = "select new SoftwareVersion(versionId,versionNum) from SoftwareVersion where versionId=? or versionId=? ";
			List<SoftwareVersion> list = bugCommonService.findByHql(hql, dto.getBug().getBugReptVer(),verInfoId);
			if(list!=null||!list.isEmpty()){
				for(SoftwareVersion sv :list){
					if(verInfoId.toString().equals(sv.getVersionId().toString())){
						dto.getBug().setCurrVersion(sv);
					}
					if(dto.getBug().getBugReptVer().toString().equals(sv.getVersionId().toString())){
						dto.getBug().setReptVersion(sv);
					}
				}
			}
		}
	}
	public void upInit(BugManagerDto dto){
		bugFlowControlService.upInitContl(dto.getCurrTaskInfo(), dto.getBug());
		bugCommonService.setRelaType(dto.getBug());
		dto.setStateName(BugFlowConst.getStateName(dto.getBug().getCurrStateId()));
		dto.setModuleName(bugCommonService.getMdPathName(dto.getBug().getModuleId(),""));
		bugCommonService.setRelaUser(dto.getBug());
	}
	public String getMdPath(Long moduleId){
		return this.getMdPathName(moduleId, "");
	}
	private String getMdPathName(Long moduleId,String pathName){
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
	public void addBug(BugManagerDto dto) {
		if(dto.getBug().getDevOwnerId()!=null&&!"".equals(dto.getBug().getDevOwnerId())&&(dto.getBug().getChargeOwner()==null||"".equals(dto.getBug().getChargeOwner()))){
			dto.getBug().setChargeOwner(dto.getBug().getDevOwnerId());
		}
		this.add(dto.getBug());
		BugHandHistory bugHistory = new BugHandHistory();
		BugBaseInfo bug = dto.getBug();
		bugHistory.setInitState(bug.getInitState());
		bugHistory.setBugState(bug.getCurrStateId());
		//bugHistory.setHandlerName(bug.getModelName());
		bugHistory.setHandlerId(bug.getCurrHandlerId());
		bugHistory.setInsDate(bug.getCurrHandlDate());
		bugHistory.setTestFlowCd(bug.getCurrFlowCd());
		bugHistory.setBugId(bug.getBugId());
		bugHistory.setTaskId(bug.getTaskId());
		bugHistory.setModuleId(bug.getModuleId());
		bugHistory.setRemark("新提交问题");
		bugHistory.setHandResult("状态为: "+BugFlowConst.getStateName(bug.getCurrStateId()));
		//bugHistory.setTestSeq(bug.getTestSeq());
		bugHistory.setCurrVer(dto.getCurrVer());
		bugHistory.setCurrDayFinal(1);
		this.add(bugHistory);
		List<FileInfoVo> fileList = dto.getFileInfos();
		if(fileList!=null && fileList.size()>0){
			for (FileInfoVo fileInfoVo : fileList) {
				FileInfo fileInfo = new FileInfo();
				fileInfo.setType("bug");
				fileInfo.setTypeId(bug.getBugId());
				fileInfo.setRelativeName(fileInfoVo.getFileOriginalName());
				fileInfo.setFilePath(fileInfoVo.getFileUrl());
				this.add(fileInfo);
			}
		}
//		String myId = SecurityContextHolderHelp.getUserId();
//		String hql = "select new User(id,name ) from User where id=?" ;
//		User Owner = null;
//		BugBaseInfo bug = dto.getBug();
		//从前台取，这里就不用处理了
//		if(myId.equals(bug.getTestOwnerId())){
//			bug.settestName(SecurityContextHolderHelp.getMyRealName());
//		}else{
//			Owner = (User)this.findByHql(hql, bug.getTestOwnerId()).get(0);
//			bug.settestName(Owner.getName());			
//		}
//		if(bug.getDevOwnerId()!=null&&bug.getDtoHelper().devOwner!=null){
//			dto.getBug().setDevName(bug.getDtoHelper().devOwner.getUniqueName().split("\\(")[0]);
//		}else if(bug.getDevOwnerId()!=null&&!"".equals(bug.getDevOwnerId())){
//			Owner = (User)this.findByHql(hql, dto.getBug().getDevOwnerId()).get(0);
//			dto.getBug().setDevName(Owner.getName());
//		}
	}
	public Map<String,DefaultTypeDefine> getRelaTypeDefine(List<BugBaseInfo> list){
		if(list==null||list.size()==0){
			return null;
		}
		List<Long> typeDefineIds = new ArrayList<Long>();
		for(BugBaseInfo obj :list){
			if(obj.getBugFreqId()!=null&&!typeDefineIds.contains(obj.getBugFreqId())){
				typeDefineIds.add(obj.getBugFreqId());
			}
			if(obj.getBugTypeId()!=null&&!typeDefineIds.contains(obj.getBugTypeId())){
				typeDefineIds.add(obj.getBugTypeId());
			}	
			if(obj.getBugGradeId()!=null&&!typeDefineIds.contains(obj.getBugGradeId())){
				typeDefineIds.add(obj.getBugGradeId());
			}
			if(obj.getBugOccaId()!=null&&!typeDefineIds.contains(obj.getBugOccaId())){
				typeDefineIds.add(obj.getBugOccaId());
			}
			if(obj.getPriId()!=null&&!typeDefineIds.contains(obj.getPriId())){
				typeDefineIds.add(obj.getPriId());
			}
//			if(obj.getGeneCauseId()!=null&&!typeDefineIds.contains(obj.getGeneCauseId())){
//				typeDefineIds.add(obj.getGeneCauseId());
//			}
//			if(obj.getSourceId()!=null&&!typeDefineIds.contains(obj.getSourceId())){
//				typeDefineIds.add(obj.getSourceId());
//			}
//			if(obj.getPlatformId()!=null&&!typeDefineIds.contains(obj.getPlatformId())){
//				typeDefineIds.add(obj.getPlatformId());
//			}			
//			if(obj.getGenePhaseId()!=null&&!typeDefineIds.contains(obj.getGenePhaseId())){
//				typeDefineIds.add(obj.getGenePhaseId());
//			}
		}
		String hql = " select new cn.com.codes.object.DefaultTypeDefine(typeId,typeName) from TypeDefine where typeId in(:ids) " ;
		Map praValuesMap = new HashMap(1);
		//排序是为了同样的用户ID但顺序不一样时，统一排序以尽可能我多的用缓存
		this.sortLongList(typeDefineIds);
		praValuesMap.put("ids", typeDefineIds);
		if(typeDefineIds.size()==0){
			return new HashMap<String,DefaultTypeDefine>(1);
		}
		List<DefaultTypeDefine> typeList = this.findByHqlWithValuesMap(hql, praValuesMap, false);
		if(typeList==null||typeList.isEmpty()){
			return new HashMap<String,DefaultTypeDefine>(1);
		}
		Map<String,DefaultTypeDefine> typeMap = new HashMap<String,DefaultTypeDefine>(typeList.size());
		for(TypeDefine tp :typeList){
			typeMap.put(tp.getTypeId().toString(), (DefaultTypeDefine)tp);
		}
		praValuesMap = null;
		typeDefineIds = null;	
		return typeMap;
	}

	public void updateBug(BugManagerDto dto) {

	}

	public void handlBug(BugManagerDto dto) {

	}

	public BugFlowControlService getBugFlowControlService() {
		return bugFlowControlService;
	}

	public void setBugFlowControlService(BugFlowControlService bugFlowControlService) {
		this.bugFlowControlService = bugFlowControlService;
	}

	public BugCommonService getBugCommonService() {
		return bugCommonService;
	}

	public void setBugCommonService(BugCommonService bugCommonService) {
		this.bugCommonService = bugCommonService;
	}
	public TestTaskDetailService getTestTaskService() {
		return testTaskService;
	}
	public void setTestTaskService(TestTaskDetailService testTaskService) {
		this.testTaskService = testTaskService;
	}
	

}
