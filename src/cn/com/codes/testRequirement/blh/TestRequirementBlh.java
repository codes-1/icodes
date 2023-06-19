package cn.com.codes.testRequirement.blh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.common.ListObject;
import cn.com.codes.framework.common.config.PropertiesBean;
import cn.com.codes.framework.common.util.Context;
import cn.com.codes.framework.exception.DataBaseException;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.object.OutlineInfo;
import cn.com.codes.object.OutlineTeamMember;
import cn.com.codes.object.TestTaskDetail;
import cn.com.codes.testRequirement.dto.TestRequirementDto;
import cn.com.codes.testRequirement.service.TestRequirementService;
import cn.com.codes.testTaskManager.service.TestTaskDetailService;

public class TestRequirementBlh extends BusinessBlh {

	private static Logger logger = Logger.getLogger(TestRequirementBlh.class);
	private  TestRequirementService testRequirementService ;
	private TestTaskDetailService testTaskService ;
	private static Map moduleNumControl = new  Hashtable();
	
	public View initList(BusiRequestEvent req){
		TestRequirementDto dto = super.getDto(TestRequirementDto.class, req);
		String taskId = dto.getTaskId();
		if(taskId!=null){
			StringBuffer hql = new StringBuffer("select new TestTaskDetail(outlineState,testPhase,")
			.append("currentVersion,testSeq,reltCaseFlag) from TestTaskDetail where taskId=? and companyId=?");
			List<TestTaskDetail> taskList =testTaskService.findByHql(hql.toString(), dto.getTaskId(),SecurityContextHolderHelp.getCompanyId());
			if(taskList==null||taskList.isEmpty()){
				throw new DataBaseException("非法提交的测试任务数据，不受理");
			}
			SecurityContextHolderHelp.setCurrTaksId(taskId);
			dto.setTaskId(taskId);
//			String nodeTreeStr = this.toTreeStr(testRequirementService.loadTree(dto));
//			SecurityContextHolder.getContext().setAttr("nodeDataStr", nodeTreeStr);
		}else{
			taskId = SecurityContextHolderHelp.getCurrTaksId();
			if(this.getTestTaskState(taskId)!=3){//为3时当前项目没设置测试流程
				dto.setTaskId(taskId);
			}
		}
		return super.getView();
	} 
	
	private Integer getTestTaskState(String taskId){
		String hql = "select new TestTaskDetail(taskId,testTaskState) from TestTaskDetail where taskId=? and companyId=?" ;
		List<TestTaskDetail> taskList =testTaskService.findByHql(hql.toString(), taskId,SecurityContextHolderHelp.getCompanyId());
		if(taskList==null||taskList.isEmpty()){
			return 3;
		}
		return taskList.get(0).getTestTaskState();
	}
	
	public View loadTree(BusiRequestEvent req){
		TestRequirementDto dto = super.getDto(TestRequirementDto.class, req);
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		dto.setTaskId(taskId);
		if(taskId==null){
			if(dto.getIsAjax() != null){
				super.writeResult("0,1,无数据,0.1");
				dto = null;
				return super.globalAjax();
			}
			SecurityContextHolder.getContext().setAttr("nodeDataStr", "0,1,无数据,0,1");
			return super.getView();
		}
		String nodeTreeStr = this.toTreeStr(testRequirementService.loadTree(dto));
		if(dto.getIsAjax()==null){
			SecurityContextHolder.getContext().setAttr("nodeDataStr", nodeTreeStr);
			return super.getView();
		}
		super.writeResult(nodeTreeStr);
		dto = null;
		return super.globalAjax();
	}
	
	public View updateNode(BusiRequestEvent req){
		TestRequirementDto dto = super.getDto(TestRequirementDto.class, req);
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		StringBuffer sb = new StringBuffer("select new OutlineInfo(");
		sb.append(" moduleId,superModuleId,isleafNode, moduleName,moduleState,moduleNum )");
		sb.append(" from OutlineInfo where  superModuleId=? and moduleId<>? and taskId=?  order by moduleId");
		List<OutlineInfo> list = testRequirementService.findByHql(sb.toString(),dto.getParentNodeId(),dto.getCurrNodeId(),taskId);
		String nodeName = dto.getNodeName();
		//校验重名
		for(OutlineInfo outline :list){
			if(outline.getModuleName().equals(nodeName)){
				list.clear();
				list.add(outline);
				super.writeResult("reName_"+this.toTreeStr(list));
				return super.globalAjax();
			}
		}
		testRequirementService.updateNode(dto);
		super.writeResult("sucess");
		dto = null;
		return super.globalAjax();
	}
	
	public View switchState(BusiRequestEvent req){
		TestRequirementDto dto = super.getDto(TestRequirementDto.class, req);
		testRequirementService.switchState(dto);
		super.writeResult("sucess");
		dto = null;
		return super.globalAjax();
	}
	
	public View deleteNode(BusiRequestEvent req){
		TestRequirementDto dto = super.getDto(TestRequirementDto.class, req);
		dto.setIsAjax("true");
		if(!isCanDelete(dto.getCurrNodeId())){
			super.writeResult("canNotDel");
			return super.globalAjax();
		}
		OutlineInfo outline = new OutlineInfo();
		outline.setModuleId(dto.getCurrNodeId());
		outline.setTaskId(dto.getTaskId());
		outline.setSuperModuleId(dto.getParentNodeId());
		testRequirementService.delete(outline);
		super.writeResult("sucess");
		dto = null;
		return super.globalAjax();
	}
	public View move(BusiRequestEvent req){
		
		TestRequirementDto dto = super.getDto(TestRequirementDto.class, req);
		if(this.moveCheck(dto).equals("ok")){
			Long targetId = dto.getTargetId();
			String hql = "from OutlineInfo where taskId=? and moduleId=?";
			String taskId = SecurityContextHolderHelp.getCurrTaksId();
			OutlineInfo parent = (OutlineInfo)testRequirementService.findByHql(hql, taskId,targetId).get(0);
			OutlineInfo current = testRequirementService.get(OutlineInfo.class, dto.getCurrNodeId());
			dto.setInitLevel(current.getModuleLevel());
			dto.setTaskId(taskId);
			int levelOffSet = current.getModuleLevel()-parent.getModuleLevel();
			String moduleNum = this.getNextNum(targetId.toString(),parent.getModuleNum());
			String oldModuleNum = current.getModuleNum();
			List<Map<String, Object>> adjustInfo = this.getChildAdjustInfo(dto,levelOffSet,moduleNum,oldModuleNum);
			dto.setCurrLevel(parent.getModuleLevel()+1);
			dto.setModuleNum(moduleNum);
			String taskHql = "select new TestTaskDetail(outlineState,testPhase,currentVersion) from TestTaskDetail where taskId=?";
			TestTaskDetail task = (TestTaskDetail)testRequirementService.findByHql(taskHql, dto.getTaskId()).get(0);
			dto.setTask(task);
			task.setTaskId(dto.getTaskId());
			//模快己没版本，所以注掉下面的代码
			//this.genMvInfo(dto);
			testRequirementService.move(dto,adjustInfo);
			super.writeResult("sucess");
		}else{
			super.writeResult("cancel");
		}
		dto = null;
		return super.globalAjax();
	}
	private void genMvInfo(TestRequirementDto dto){
		TestTaskDetail task = dto.getTask();
		if(task.getTestPhase()==2){
			return ;
		}
		Long parentId = null;
		if(task.getOutlineState()==1&&dto.getCurrLevel()==2){
			parentId = getTopParentId(dto.getCurrNodeId());
		}
		if(parentId==null){
			//模快己没版本，所以注掉下面的代码
			//dto.setCurrVer(testTaskService.getCurrVer(task.getTaskId(), dto.getCurrNodeId()));
			return;
		}
		//模快己没版本，所以注掉下面的代码
//		String hql ="from ModuleVerRec where moduleId=? order by seq";
//		List<ModuleVerRec> list = testRequirementService.findByHql(hql, parentId);
//		List<ModuleVerRec> mvlist = new ArrayList<ModuleVerRec>();
//		for(ModuleVerRec mv :list ){
//			ModuleVerRec newMv = mv.copyToNew(dto.getCurrNodeId());
//			mvlist.add(newMv);
//		}
//		if(list!=null&&list.size()>0){
//			dto.setCurrVer(list.get(list.size()-1).getModuleVersion());
//		}
//		dto.setMvList(mvlist);
	}
	//查询二级模块“父”模块ID
	private Long getTopParentId(Long moduleId){
		String hql = "select new OutlineInfo(moduleId,superModuleId,moduleLevel) from OutlineInfo where moduleId=?" ;
		OutlineInfo outLine = (OutlineInfo)testRequirementService.findByHql(hql, moduleId).get(0);
		if(outLine.getModuleLevel()!=2){
			return getTopParentId(outLine.getSuperModuleId());
		}else{
			return outLine.getModuleId();
		}
	}
	public View  loadPeople(BusiRequestEvent req){
		TestRequirementDto dto = super.getDto(TestRequirementDto.class, req);
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		dto.setTaskId(taskId);
		List list = testRequirementService.loadTreePeople(dto);
		if("true".equals(dto.getIsAjax())){
			StringBuffer sb = new StringBuffer();
			dto.toJson(list, sb);
			super.writeResult(sb.toString());
			return super.globalAjax();
		}
		dto.setJsonData(list);
		return super.getView("loadPeople");
	}
	
	public View selectMember(BusiRequestEvent req){
		TestRequirementDto dto = super.getDto(TestRequirementDto.class, req);
		List memberes = this.getTemMemberListObjs(SecurityContextHolderHelp.getCurrTaksId());
		super.writeResult(super.listToJson(memberes));
		dto = null;
		return super.globalAjax();
	}

	
	public View  assignPeople(BusiRequestEvent req){
		TestRequirementDto dto = super.getDto(TestRequirementDto.class, req);
		String[] useridsArr = dto.getUserIds().split("_");
		Set<OutlineTeamMember> devMermberSet = new HashSet<OutlineTeamMember>();
		String compId = SecurityContextHolderHelp.getCompanyId();
		List<Long> assignIdList = testRequirementService.getAllAssignNode(dto);
		for(String userId :useridsArr){
			if("".equals(userId)){
				continue;
			}
			for(Long nodeId :assignIdList){
				OutlineTeamMember member = new OutlineTeamMember(nodeId,userId, 1) ;
				member.setTaskId(dto.getTaskId());
				member.setCompanyId(compId);
				devMermberSet.add(member);
			}
		}
		Set<OutlineTeamMember> oldMermberSet = new HashSet<OutlineTeamMember>();
		List<OutlineTeamMember> oldMember = testRequirementService.getInitAsignPeople(dto,assignIdList);
		oldMermberSet.addAll(oldMember);
		testRequirementService.saveRealSet(oldMermberSet, devMermberSet, "moduleMemberId", null);
		oldMermberSet = null;
		if("true".equals(dto.getIsAjax())){
			super.writeResult("sucess");
			return super.globalAjax();
		}
		return super.getView("assignPeople");
	}
	public View setKlog(BusiRequestEvent req){
		TestRequirementDto dto = super.getDto(TestRequirementDto.class, req);
		String hql = "update OutlineInfo set klc=? where moduleId=?";
		testRequirementService.executeUpdateByHql(hql, new Object[]{dto.getKlc(),dto.getCurrNodeId()});
		super.writeResult("sucess");
		dto = null;
		return super.globalAjax();
	}
	
	private synchronized String getNextNum(String parentId,String parentNum){
		PropertiesBean conf = (PropertiesBean) Context.getInstance().getBean("ContextProperties");
		String maxLevelSql = null;
		if(conf.getProperty("config.db.driver")!=null&&conf.getProperty("config.db.driver").indexOf("mysql")>0){
			maxLevelSql = "select IfNull(max(t.modulenum),0) as count  from t_outlineinfo t  where t.superModuleId = ?" ;
		}else{
			maxLevelSql = "select nvl(max(t.modulenum),0) as count  from t_outlineinfo t  where t.superModuleId = ?" ;
		}
		List<Object> numList= testRequirementService.findBySql(maxLevelSql, null, parentId);
		String moduleNum = numList.get(0).toString();
		if("0".equals(moduleNum)){
			if(parentNum==null){
				return "001";
			}
			return parentNum+"001";
		}
		String countStr = moduleNum.substring(moduleNum.length()-3);
		int count = Integer.parseInt(countStr);
		count ++;
		String prefix = moduleNum.substring(0,moduleNum.lastIndexOf(countStr));
		String nextNum = prefix+String.valueOf((count+1000)).substring(1);
		return nextNum;
	}
	private String getNexnum(String moduleNum){
		String countStr = moduleNum.substring(moduleNum.length()-3);
		int count = Integer.parseInt(countStr);
		count ++;
		String prefix = moduleNum.substring(0,moduleNum.lastIndexOf(countStr));
		String nextNum = prefix+String.valueOf((count+1000)).substring(1);
		return nextNum;		
	}

	private List<ListObject> getTemMemberListObjs(String taskId){
		
		StringBuffer sb = new StringBuffer();
		sb.append("select new cn.com.codes.framework.common.ListObject(");
		sb.append(" u.id as keyObj,(u.loginName||'('||u.name||')') as valueObj )");
		sb.append(" from TaskUseActor ta  join ta.user u where taskId=? and actor=5 ");
		return  testRequirementService.findByHql(sb.toString(), taskId);
	}
	
	
	private String moveCheck(TestRequirementDto dto){
		Long targetId = dto.getTargetId();
		Long sourceId = dto.getCurrNodeId();
		StringBuffer sb = new StringBuffer();
		sb.append("select count(p.modulename) as count");
		sb.append("  from t_outlineinfo c, ");
		sb.append(" (select s.modulename from t_outlineinfo s where s.SUPERMODULEID =? ) p");
		sb.append(" where c.modulename = p.modulename and c.moduleid =? and c.TASKID=?");
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		int repCount =testRequirementService.getHibernateGenericController().getResultCountBySql(sb.toString(), "p.modulename",targetId,sourceId,taskId).intValue();
		if(repCount==0){
			return "ok";
		}
		return "cancel";	
	}
	private boolean isCanDelete(Long moduleId){
		
		return true ;
	}
	private String toTreeStr(List<OutlineInfo> list){
		if(list==null||list.isEmpty()){
			return "0,1,功能分解树尚未建立,0.1";
		}
		StringBuffer sb = new StringBuffer();
		for(OutlineInfo outLine :list){
			sb.append(";").append(outLine.getSuperModuleId());
			sb.append(",").append(outLine.getModuleId());
			sb.append(",").append(outLine.getModuleName());
			sb.append(",").append(outLine.getIsleafNode());
			sb.append(",").append(outLine.getModuleState());
		}
		return sb.length()>2?sb.substring(1).toString() :"";
	}
	
	public View addNodes(BusiRequestEvent req){
		TestRequirementDto dto = super.getDto(TestRequirementDto.class, req);
		String command  = dto.getCommand();
		Long parentId = dto.getParentNodeId();
		List<OutlineInfo>  list = new ArrayList<OutlineInfo>();
		String comapnyId = SecurityContextHolderHelp.getCompanyId();
		if("addchild".equals(command)){
			parentId = dto.getCurrNodeId();
			dto.setParentNodeId(parentId);
		}
		if(parentId==0){
			throw new DataBaseException("不能增加根节点");
		}
		//这样查询是为了防止前台篡改ID提交 所以加上任务Id
		String hql = "from OutlineInfo where moduleId=? and taskId=?" ;
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		OutlineInfo parent = (OutlineInfo)testRequirementService.findByHql(hql, parentId,taskId).get(0);
		Integer currLevel = parent.getModuleLevel()+1; 
		if(this.reNameCheck(dto)!=null){
			return super.globalAjax();
		}
		String moduleNum = this.getNextNum(parentId.toString(), parent.getModuleNum());
		int i =0;
		String currVer = null;
		//集成测试时，二级模块要设置当前片本
		if(currLevel==2){
			hql = " select new TestTaskDetail(outlineState,testPhase,currentVersion) from TestTaskDetail where taskId=?";
			TestTaskDetail taskDetal = (TestTaskDetail)testRequirementService.findByHql(hql, SecurityContextHolderHelp.getCurrTaksId()).get(0);
			//集成测试，一级模块不建版本了，所以下面的己无用
			//if(taskDetal.getTestPhase()==1){
			//	currVer = testTaskService.getCurrVer(SecurityContextHolderHelp.getCurrTaksId(), parentId);
			//}
		}
		for(String name:dto.getModuleData()){
			if(name!=null&&!"".equals(name.trim())){
				OutlineInfo outline = new OutlineInfo();
				outline.setIsleafNode(1);
				outline.setModuleState(0);
				outline.setTaskId(SecurityContextHolderHelp.getCurrTaksId());
				outline.setModuleName(name);
				outline.setSuperModuleId(parentId);
				outline.setModuleLevel(currLevel);
				outline.setCompanyId(comapnyId);
				outline.setCurrVer(currVer);
				if(i==0){
					outline.setModuleNum(moduleNum);
				}else{
					moduleNum = this.getNexnum(moduleNum);
					outline.setModuleNum(moduleNum);
				}
				list.add(outline);
				i++;
			}
		}
		List rest = testRequirementService.addNodes(list);
		super.writeResult(this.toTreeStr(rest));
		dto = null;
		return super.globalAjax();
	}

	private String reNameCheck(TestRequirementDto dto){
		StringBuffer sb = new StringBuffer("select new OutlineInfo(");
		sb.append(" moduleId,superModuleId,isleafNode, moduleName,moduleState,moduleNum )");
		sb.append(" from OutlineInfo where  superModuleId=? order by moduleId");
		List<OutlineInfo>  list = testRequirementService.findByHql(sb.toString(),dto.getParentNodeId());
		for(String name:dto.getModuleData()){
			if(name!=null&&!"".equals(name.trim())){
				for(OutlineInfo outline :list){
					if(outline.getModuleName().equals(name)){
						super.writeResult("reName_"+name+"$"+this.toTreeStr(list));
						return "reName";
					}
				}
			}
		}
		return null;
	}
	public View submitModule(BusiRequestEvent req){
		TestRequirementDto dto = super.getDto(TestRequirementDto.class, req);
		String  taskId = SecurityContextHolderHelp.getCurrTaksId();
		dto.setTaskId(taskId);
		String taskHql = "select new TestTaskDetail(outlineState,testPhase,currentVersion) from TestTaskDetail where taskId=?";
		TestTaskDetail task = (TestTaskDetail)testRequirementService.findByHql(taskHql, dto.getTaskId()).get(0);
		task.setTaskId(taskId);
		if(task.getOutlineState()==1){
			super.writeResult("sucess");
			return super.globalAjax();
		}
		String rootNodeIdHql = "select new OutlineInfo(moduleId) from OutlineInfo where taskId=? and superModuleId =0 " ;
		OutlineInfo rootOutLine = (OutlineInfo)testRequirementService.findByHql(rootNodeIdHql, taskId).get(0);
		int i =1;
		if(moduleNumControl.get(taskId)!=null){
			while(moduleNumControl.get(taskId)!=null){
				if(i<4){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						logger.error(e);
					}					
				}else{
					setModuleMumExe(taskId,rootOutLine,task);
					super.writeResult("sucess");
					return super.globalAjax();					
				}
				i++;
			}				
		}
		setModuleMumExe(taskId,rootOutLine,task);
		super.writeResult("sucess");
		dto = null;
		return super.globalAjax();
	}
	
	private void setModuleMumExe(String taskId ,OutlineInfo rootOutLine,TestTaskDetail task){
		try {
			moduleNumControl.put(task.getTaskId(), "");
			String hql = this.buildHqlForSetMum();
			List<OutlineInfo> modList = testRequirementService.findByHql(hql,taskId, rootOutLine.getModuleId());
			String maxLevelSql = "select max(t.modulelevel) from t_outlineinfo t  where t.taskid = ?" ;
			List<Object> level= testRequirementService.findBySql(maxLevelSql, null, taskId);
			int maxLevel = Integer.parseInt(level.get(0).toString());
			List<Map<String, Object>> NumInfoList = this.getModNumInMemory(modList, maxLevel,task.getCurrentVersion());
			testRequirementService.submitMoudle(NumInfoList, taskId);
		}catch (NumberFormatException e) {
			logger.error(e);
		}catch (RuntimeException e){
			logger.error(e);
			throw e;
		}finally{
			moduleNumControl.remove(taskId);
		}
	}
	
	private List<Map<String, Object>> getModNumInMemory(List<OutlineInfo> modList,int maxLevel,String currVer){
		List<Map<String, Object>> NumInfoList = new ArrayList<Map<String, Object>>();
		Map<String,String> parentHolder = new HashMap();
		for(int i=2; i<maxLevel+1;i++){
			int count =1;
			String currentParentId =  null ;
			for(OutlineInfo obj :modList){
				if(obj.getModuleLevel()==i){
					if(i==2){
						int num = 1000+count;
						String moduleNum = String.valueOf(num).substring(1);
						Map map = new HashMap(2);
						map.put("moduleId", obj.getModuleId());
						map.put("moduleNum", moduleNum);
						map.put("currVer", currVer);
						NumInfoList.add(map);
						if(obj.getIsleafNode()==0){
							parentHolder.put(obj.getModuleId().toString(), moduleNum);
						}
						count++;
					}else{
						if(currentParentId==null){
							currentParentId = obj.getSuperModuleId().toString();
							count =1;
						}else if(!currentParentId.equals(obj.getSuperModuleId().toString())){
							currentParentId = obj.getSuperModuleId().toString();
							count =1;
						}else{
							count ++;
						}
						int num = 1000+count;
						String moduleNum =parentHolder.get(currentParentId) +String.valueOf(num).substring(1);
						Map map = new HashMap(2);
						map.put("moduleId", obj.getModuleId());
						map.put("moduleNum", moduleNum);
						map.put("currVer", currVer);
						NumInfoList.add(map);
						if(obj.getIsleafNode()==0){
							parentHolder.put(obj.getModuleId().toString(), moduleNum);
						}
					}
				}
			}
		}		
		return NumInfoList;
	}

	private String buildHqlForSetMum(){
		StringBuffer sb = new StringBuffer();
		sb.append("select new OutlineInfo");
		sb.append("  (moduleId,superModuleId, moduleLevel,isleafNode)");
		sb.append("  from OutlineInfo");
		sb.append("  where taskId=? and moduleId<>?");
		sb.append("  order by moduleLevel, superModuleId, moduleId");
		return sb.toString();
	}
	private List<Map<String, Object>> getChildAdjustInfo( TestRequirementDto dto ,int levelOffSet,String parentNum,String oldParentNum){
		
		Long currentId = dto.getCurrNodeId();
		String hql = this.buildLoadAllChild();
		List<OutlineInfo> list = testRequirementService.findByHql(hql, oldParentNum+"%",dto.getTaskId(),currentId);
		List<Map<String, Object>> adjustInfoList = new ArrayList<Map<String, Object>>(list.size());
		//这是后来加的，主要是更新用例的块编号，这么做是为了用你查询时，以后不用视图了
		List<Map<String, Object>> adjustCaseInfoList = new ArrayList<Map<String, Object>>(list.size());
		for(OutlineInfo obj:list){
			Map map = new HashMap(3);
			Map caseMap = new HashMap(3);
			map.put("moduleId", obj.getModuleId());
			map.put("moduleLevel", obj.getModuleLevel()-levelOffSet+1);
			map.put("moduleNum", obj.getModuleNum().replaceFirst(oldParentNum, parentNum));
			
			caseMap.put("moduleId", obj.getModuleId());
			caseMap.put("moduleNum", obj.getModuleNum().replaceFirst(oldParentNum, parentNum));
			caseMap.put("taskId", dto.getTaskId());
			if((obj.getModuleLevel()-levelOffSet+1)>100){
				adjustInfoList.clear();
				adjustInfoList = null;
				adjustCaseInfoList.clear();
				adjustCaseInfoList = null;
				throw new DataBaseException("最多只能99级");
				
			}
			adjustInfoList.add(map);
			adjustCaseInfoList.add(caseMap);
		}
		dto.setHqlParamLists(adjustCaseInfoList);
		return adjustInfoList;
	}

	private String buildLoadAllChild(){
		StringBuffer sb = new StringBuffer();
		sb.append(" select  new OutlineInfo(moduleId, moduleLevel, moduleNum) from OutlineInfo where  moduleNum like ? and taskId=? and moduleId<>? order by moduleId");
		return sb.toString();
	}
	public TestRequirementService gettestRequirementService() {
		return testRequirementService;
	}

	public void settestRequirementService(TestRequirementService testRequirementService) {
		this.testRequirementService = testRequirementService;
	}

	public TestTaskDetailService getTestTaskService() {
		return testTaskService;
	}

	public void setTestTaskService(TestTaskDetailService testTaskService) {
		this.testTaskService = testTaskService;
	}

	

}
