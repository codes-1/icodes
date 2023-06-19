package cn.com.codes.outlineManager.blh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.opensymphony.webwork.ServletActionContext;

import cn.com.codes.caseManager.dto.TreeJsonVo;
import cn.com.codes.caseManager.service.CaseManagerService;
import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.dto.PageModel;
import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.common.JsonUtil;
import cn.com.codes.framework.common.ListObject;
import cn.com.codes.framework.common.config.PropertiesBean;
import cn.com.codes.framework.common.util.Context;
import cn.com.codes.framework.exception.DataBaseException;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.object.CasePri;
import cn.com.codes.object.CaseType;
import cn.com.codes.object.OutlineInfo;
import cn.com.codes.object.OutlineTeamMember;
import cn.com.codes.object.TestCaseInfo;
import cn.com.codes.object.TestTaskDetail;
import cn.com.codes.outlineManager.dto.OutLineManagerDto;
import cn.com.codes.outlineManager.service.OutLineManagerService;
import cn.com.codes.testTaskManager.service.TestTaskDetailService;
import cn.com.codes.outlineManager.blh.OutLineManagerBlh;

public class OutLineManagerBlh extends BusinessBlh {

	private static Logger logger = Logger.getLogger(OutLineManagerBlh.class);
	private OutLineManagerService outLineService;
	private TestTaskDetailService testTaskService;
	private CaseManagerService caseService;
	private static Map moduleNumControl = new Hashtable();

	public View setReqState(BusiRequestEvent req) {
		OutLineManagerDto dto = super.getDto(OutLineManagerDto.class, req);
		String queryHql = "select new OutlineInfo(moduleId,superModuleId,isleafNode ,moduleName, moduleState, moduleNum) from  OutlineInfo where   moduleId=? and taskId=?";
		List<OutlineInfo> nodeList = outLineService.findByHql(queryHql,
				dto.getCurrNodeId(), SecurityContextHolderHelp.getCurrTaksId());
		if (nodeList == null | nodeList.isEmpty()) {
			super.writeResult("haveDel");
			return super.globalAjax();
		}
		if (nodeList.get(0).getModuleState().intValue() == 1) {
			super.writeResult("haveStop");
			return super.globalAjax();
		}
		String hql = "update OutlineInfo set moduleState = ? where   moduleId=? and taskId=? and moduleState!=1 ";
		outLineService.getHibernateGenericController().executeUpdate(hql,
				dto.getModuleState(), dto.getCurrNodeId(),
				SecurityContextHolderHelp.getCurrTaksId());
		super.writeResult("success");
		return super.globalAjax();
	}

	public View itemMoveUp(BusiRequestEvent req) {
		OutLineManagerDto dto = super.getDto(OutLineManagerDto.class, req);
		synchronized (this) {
			OutlineInfo moveNodeInfo = outLineService.get(OutlineInfo.class,
					dto.getCurrNodeId());
			if (dto.getParentNodeId().intValue() != moveNodeInfo
					.getSuperModuleId().intValue()) {
				throw new DataBaseException("当测试需求项，己被他人移动到其他需求项下");
			}
			if (moveNodeInfo.getSuperModuleId().intValue() == 0) {
				return super.globalAjax();
			}
			if (moveNodeInfo.getSeq() == null) {
				outLineService.initSeq(moveNodeInfo);
			}
			dto.setCurrSeq(moveNodeInfo.getSeq());
			outLineService.moveUpItem(dto);
		}
		// 移动后,要重新加载其父亲下的子节点
		dto.setCurrNodeId(dto.getParentNodeId());
		dto.setIsMoveOpera(true);
		String nodeTreeStr = this.toTreeStr(outLineService.loadTree(dto));
		super.writeResult(nodeTreeStr);
		return super.globalAjax();
	}

	public View itemMoveDown(BusiRequestEvent req) {
		OutLineManagerDto dto = super.getDto(OutLineManagerDto.class, req);
		synchronized (this) {
			OutlineInfo moveNodeInfo = outLineService.get(OutlineInfo.class,
					dto.getCurrNodeId());
			if (dto.getParentNodeId().intValue() != moveNodeInfo
					.getSuperModuleId().intValue()) {
				throw new DataBaseException("当测试需求项，己被他人移动到其他需求项下");
			}
			if (moveNodeInfo.getSuperModuleId().intValue() == 0) {
				return super.globalAjax();
			}
			if (moveNodeInfo.getSeq() == null) {
				outLineService.initSeq(moveNodeInfo);
			}
			dto.setCurrSeq(moveNodeInfo.getSeq());
			outLineService.moveDownItem(dto);
		}
		// 移动后,要不重新加载其父亲下的子节点
		dto.setCurrNodeId(dto.getParentNodeId());
		dto.setIsMoveOpera(true);
		String nodeTreeStr = this.toTreeStr(outLineService.loadTree(dto));
		super.writeResult(nodeTreeStr);
		return super.globalAjax();
	}

	public View initList(BusiRequestEvent req) {
		OutLineManagerDto dto = super.getDto(OutLineManagerDto.class, req);
		String taskId = dto.getTaskId();
		List<TestTaskDetail> taskList = null;
		if (taskId != null) {
			StringBuffer hql = new StringBuffer(
					"select new TestTaskDetail(outlineState,testPhase,")
					.append("currentVersion,testSeq,reltCaseFlag) from TestTaskDetail where taskId=? and companyId=?");
			taskList = testTaskService.findByHql(
					hql.toString(), dto.getTaskId(),
					SecurityContextHolderHelp.getCompanyId());
			if (taskList == null || taskList.isEmpty()) {
				throw new DataBaseException("非法提交的测试任务数据，不受理");
			}
			SecurityContextHolderHelp.setCurrTaksId(taskId);

			dto.setTaskId(taskId);
		} else {
			taskId = SecurityContextHolderHelp.getCurrTaksId();

			if (this.getTestTaskState(taskId) != 3) {// 为3时当前项目没设置测试流程
				dto.setTaskId(taskId);
			}
		}
		PageModel pg = new PageModel();
		pg.setRows(taskList);
		pg.setTotal(dto.getTotal());
		super.writeResult(JsonUtil.toJson(pg));
		return super.globalAjax();
	}

	public View index(BusiRequestEvent req) {
		OutLineManagerDto dto = super.getDto(OutLineManagerDto.class, req);
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		dto.setTaskId(taskId);
		if (taskId != null) {
			StringBuffer hql = new StringBuffer(
					"select new TestTaskDetail(outlineState,testPhase,")
					.append("currentVersion,testSeq,reltCaseFlag) from TestTaskDetail where taskId=? and companyId=?");
			TestTaskDetail taskList = (TestTaskDetail)testTaskService.findByHql(
					hql.toString(), dto.getTaskId(),
					SecurityContextHolderHelp.getCompanyId()).get(0);
			if (taskList == null) {
				throw new DataBaseException("非法提交的测试任务数据，不受理");
			}
			SecurityContextHolderHelp.setCurrTaksId(taskId);

			dto.setTaskId(taskId);
			dto.setIsCommit(taskList.getOutlineState());
		} else {
			taskId = SecurityContextHolderHelp.getCurrTaksId();

			if (this.getTestTaskState(taskId) != 3) {// 为3时当前项目没设置测试流程
				dto.setTaskId(taskId);
			}
		}
		return super.getView();
	}

	public View testRequireMain(BusiRequestEvent req) {
		OutLineManagerDto dto = super.getDto(OutLineManagerDto.class, req);
		String taskId = dto.getTaskId();
		if (taskId != null) {
			StringBuffer hql = new StringBuffer(
					"select new TestTaskDetail(outlineState,testPhase,")
					.append("currentVersion,testSeq,reltCaseFlag) from TestTaskDetail where taskId=? and companyId=?");
			List<TestTaskDetail> taskList = testTaskService.findByHql(
					hql.toString(), dto.getTaskId(),
					SecurityContextHolderHelp.getCompanyId());
			if (taskList == null || taskList.isEmpty()) {
				throw new DataBaseException("非法提交的测试任务数据，不受理");
			}
			SecurityContextHolderHelp.setCurrTaksId(taskId);

			dto.setTaskId(taskId);
		} else {
			taskId = SecurityContextHolderHelp.getCurrTaksId();

			if (this.getTestTaskState(taskId) != 3) {// 为3时当前项目没设置测试流程
				dto.setTaskId(taskId);
			}
		}
		return super.getView();
	}

	private Integer getTestTaskState(String taskId) {
		String hql = "select new TestTaskDetail(taskId,testTaskState) from TestTaskDetail where taskId=? and companyId=?";
		List<TestTaskDetail> taskList = testTaskService.findByHql(
				hql.toString(), taskId,
				SecurityContextHolderHelp.getCompanyId());
		if (taskList == null || taskList.isEmpty()) {
			return 3;
		}
		return taskList.get(0).getTestTaskState();
	}

	public View loadTree(BusiRequestEvent req) {
		OutLineManagerDto dto = super.getDto(OutLineManagerDto.class, req);
		String id = ServletActionContext.getRequest().getParameter("id");
		if(!StringUtils.isNullOrEmpty(id)){
			dto.setCurrNodeId(Long.valueOf(id));
		}
		String taskId = dto.getTaskId();
		if (taskId != null) {
			List<OutlineInfo> outLineInfoList = outLineService.loadTree(dto);
			//String nodeTreeStr = this.toTreeStr(outLineInfoList);
			List<TreeJsonVo> treeJsonVos = this.toTreeJson(outLineInfoList, dto.getCurrNodeId());
			if (dto.getIsAjax() == null) {
				//SecurityContextHolder.getContext().setAttr("nodeDataStr",nodeTreeStr);
				return super.getView();
			}
			super.writeResult(JsonUtil.toJson(treeJsonVos));
			//dto = null;
			
			SecurityContextHolderHelp.setCurrTaksId(taskId);
			dto.setTaskId(taskId);
		} else {
			if (dto.getIsAjax() != null) {
				super.writeResult("0,1,无数据,0.1");
				dto = null;
				return super.globalAjax();
			}
			SecurityContextHolder.getContext().setAttr("nodeDataStr",
					"0,1,无数据,0,1");
			return super.getView();
		}
		
		
		return super.globalAjax();
	}

	public View updateNode(BusiRequestEvent req) {
		OutLineManagerDto dto = super.getDto(OutLineManagerDto.class, req);
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		StringBuffer sb = new StringBuffer("select new OutlineInfo(");
		sb.append(" moduleId,superModuleId,isleafNode, moduleName,moduleState,moduleNum,moduleLevel )");
		sb.append(" from OutlineInfo where  superModuleId=? and moduleId<>? and taskId=?  order by moduleId");
		List<OutlineInfo> list = outLineService.findByHql(sb.toString(),
				dto.getParentNodeId(), dto.getCurrNodeId(), taskId);
		String nodeName = dto.getNodeName();
		// 校验重名
		for (OutlineInfo outline : list) {
			if (outline.getModuleName().equals(nodeName)) {
				list.clear();
				list.add(outline);
				super.writeResult("reName_" + this.toTreeStr(list));
				return super.globalAjax();
			}
		}
		outLineService.updateNode(dto);
		super.writeResult("sucess");
		dto = null;
		return super.globalAjax();
	}

	public View switchState(BusiRequestEvent req) {
		OutLineManagerDto dto = super.getDto(OutLineManagerDto.class, req);
		outLineService.switchState(dto);
		super.writeResult("sucess");
		dto = null;
		return super.globalAjax();
	}

	public View deleteNode(BusiRequestEvent req) {
		OutLineManagerDto dto = super.getDto(OutLineManagerDto.class, req);
		dto.setIsAjax("true");
		if (!isCanDelete(dto.getCurrNodeId())) {
			super.writeResult("canNotDel");
			return super.globalAjax();
		}
		OutlineInfo outline = new OutlineInfo();
		outline.setModuleId(dto.getCurrNodeId());
		outline.setTaskId(dto.getTaskId());
		outline.setSuperModuleId(dto.getParentNodeId());
		outLineService.delete(outline);
		super.writeResult("sucess");
		dto = null;
		return super.globalAjax();
	}

	public synchronized View move(BusiRequestEvent req) {

		OutLineManagerDto dto = super.getDto(OutLineManagerDto.class, req);
		if (this.moveCheck(dto).equals("ok")) {
			Long targetId = dto.getTargetId();
			String hql = "from OutlineInfo where taskId=? and moduleId=?";
			String taskId = SecurityContextHolderHelp.getCurrTaksId();
			OutlineInfo parent = (OutlineInfo) outLineService.findByHql(hql,
					taskId, targetId).get(0);
			OutlineInfo current = outLineService.get(OutlineInfo.class,
					dto.getCurrNodeId());
			dto.setInitLevel(current.getModuleLevel());
			dto.setTaskId(taskId);
			int levelOffSet = current.getModuleLevel()
					- parent.getModuleLevel();
			String moduleNum = this.getNextNum(targetId.toString(),
					parent.getModuleNum());
			String oldModuleNum = current.getModuleNum();

			String seqStr = moduleNum.substring(moduleNum.length() - 2);
			int seq = Integer.parseInt(seqStr) + 1;
			dto.setCurrSeq(seq);
			dto.setModuleNum(moduleNum);
			List<Map<String, Object>> adjustInfo = this.getChildAdjustInfo(dto,
					levelOffSet, moduleNum, oldModuleNum);
			dto.setCurrLevel(parent.getModuleLevel() + 1);

			String taskHql = "select new TestTaskDetail(outlineState,testPhase,currentVersion) from TestTaskDetail where taskId=?";
			TestTaskDetail task = (TestTaskDetail) outLineService.findByHql(
					taskHql, dto.getTaskId()).get(0);
			dto.setTask(task);
			task.setTaskId(dto.getTaskId());
			// 模快己没版本，所以注掉下面的代码
			// this.genMvInfo(dto);
			outLineService.move(dto, adjustInfo);
			super.writeResult("sucess");
		} else {
			super.writeResult("cancel");
		}
		dto = null;
		return super.globalAjax();
	}

	private void genMvInfo(OutLineManagerDto dto) {
		TestTaskDetail task = dto.getTask();
		if (task.getTestPhase() == 2) {
			return;
		}
		Long parentId = null;
		if (task.getOutlineState() == 1 && dto.getCurrLevel() == 2) {
			parentId = getTopParentId(dto.getCurrNodeId());
		}
		if (parentId == null) {
			// 模快己没版本，所以注掉下面的代码
			// dto.setCurrVer(testTaskService.getCurrVer(task.getTaskId(),
			// dto.getCurrNodeId()));
			return;
		}
		// 模快己没版本，所以注掉下面的代码
		// String hql ="from ModuleVerRec where moduleId=? order by seq";
		// List<ModuleVerRec> list = outLineService.findByHql(hql, parentId);
		// List<ModuleVerRec> mvlist = new ArrayList<ModuleVerRec>();
		// for(ModuleVerRec mv :list ){
		// ModuleVerRec newMv = mv.copyToNew(dto.getCurrNodeId());
		// mvlist.add(newMv);
		// }
		// if(list!=null&&list.size()>0){
		// dto.setCurrVer(list.get(list.size()-1).getModuleVersion());
		// }
		// dto.setMvList(mvlist);
	}

	// 查询二级模块“父”模块ID
	private Long getTopParentId(Long moduleId) {
		String hql = "select new OutlineInfo(moduleId,superModuleId,moduleLevel) from OutlineInfo where moduleId=?";
		OutlineInfo outLine = (OutlineInfo) outLineService.findByHql(hql,
				moduleId).get(0);
		if (outLine.getModuleLevel() != 2) {
			return getTopParentId(outLine.getSuperModuleId());
		} else {
			return outLine.getModuleId();
		}
	}
//"teamMember":[{"moduleMemberId":null,"taskId":null,"testPhase":null,"insdate":null,"upddate":null,"companyId":null,"moduleId":null,"userId":"402890bb39abfd340139ae25f9e60006","userRole":1,"userName":null,"user":{"privilege":null,"visiableButton":null,"isAdmin":0,"id":"402890bb39abfd340139ae25f9e60006","loginName":null,"myHome":null,"companyId":null,"name":"zhangxiaofei(张晓斐)","password":null,"employeeId":null,"status":null,"email":null,"tel":null,"question":null,"answer":null,"insertDate":null,"updateDate":null,"roleList":null,"groupList":null,"delFlag":null,"officeTel":null,"groupNames":null,"groupIds":null,"headShip":null,"joinCompDate":null,"oldPwd":null,"uniqueName":"null","docUserId":null,"devMemb":null,"taskUseActors":null,"svn":null,"chgPwdFlg":null},"outline":null}],"devMember":null,"member":null,"caseCount":3,"quotiety":1.0,"reqType":0,"scrpCount":null,"sceneCount":null,"seq":null,"superModuleName":null}
	public View loadPeople(BusiRequestEvent req) {
		OutLineManagerDto dto = super.getDto(OutLineManagerDto.class, req);
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		dto.setTaskId(taskId);
		List list = outLineService.loadTreePeople(dto);
		if ("true".equals(dto.getIsAjax())) {
//			StringBuffer sb = new StringBuffer();
//			dto.toJson2(list, sb);
//			super.writeResult(sb.toString());
			PageModel pg = new PageModel();
			pg.setRows(list);
			pg.setTotal(dto.getTotal());
			super.writeResult(JsonUtil.toJson(pg));
			return super.globalAjax();
		}
		dto.setJsonData(list);
		return super.getView("loadPeople");
	}

	public View selectMember(BusiRequestEvent req) {
		OutLineManagerDto dto = super.getDto(OutLineManagerDto.class, req);
		List memberes = this.getTemMemberListObjs(
				SecurityContextHolderHelp.getCurrTaksId(), dto.getReqType());
//		super.writeResult(super.listToJson(memberes));
//		dto = null;
//		return super.globalAjax();
		PageModel pg = new PageModel();
		pg.setRows(memberes);
		pg.setTotal(dto.getTotal());
		super.writeResult(JsonUtil.toJson(pg));
		return super.globalAjax();
	}

	public View selectAllMember(BusiRequestEvent req) {
		OutLineManagerDto dto = super.getDto(OutLineManagerDto.class, req);
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct new cn.com.codes.framework.common.ListObject(");
		sb.append(" u.id as keyObj,(u.loginName||'('||u.name||')') as valueObj )");
		sb.append(" from TaskUseActor ta  join ta.user u where   ta.taskId=? ");
		List memberes = outLineService.findByHql(sb.toString(),
				SecurityContextHolderHelp.getCurrTaksId());
		super.writeResult(super.listToJson(memberes));
		dto = null;
		return super.globalAjax();
	}

	public View selectAllEnableMember(BusiRequestEvent req) {
		OutLineManagerDto dto = super.getDto(OutLineManagerDto.class, req);
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct new cn.com.codes.framework.common.ListObject(");
		sb.append(" u.id as keyObj,(u.loginName||'('||u.name||')') as valueObj )");
		sb.append(" from TaskUseActor ta  join ta.user u where  ta.isEnable = 1 and ta.taskId=? ");
		List memberes = outLineService.findByHql(sb.toString(),
				SecurityContextHolderHelp.getCurrTaksId());
		super.writeResult(super.listToJson(memberes));
		dto = null;
		return super.globalAjax();
	}

	public View assignPeople(BusiRequestEvent req) {
		OutLineManagerDto dto = super.getDto(OutLineManagerDto.class, req);
		String[] useridsArr = dto.getUserIds().split("_");
		Set<OutlineTeamMember> devMermberSet = new HashSet<OutlineTeamMember>();
		String compId = SecurityContextHolderHelp.getCompanyId();
		List<Long> assignIdList = outLineService.getAllAssignNode(dto);
		for (String userId : useridsArr) {
			if ("".equals(userId)) {
				continue;
			}
			for (Long nodeId : assignIdList) {
				OutlineTeamMember member = new OutlineTeamMember(nodeId,
						userId, (dto.getReqType() == 5 ? 1 : 3));
				member.setTaskId(dto.getTaskId());
				member.setCompanyId(compId);
				devMermberSet.add(member);
			}
		}
		Set<OutlineTeamMember> oldMermberSet = new HashSet<OutlineTeamMember>();
		List<OutlineTeamMember> oldMember = outLineService.getInitAsignPeople(
				dto, assignIdList);
		oldMermberSet.addAll(oldMember);
		outLineService.saveRealSet(oldMermberSet, devMermberSet,
				"moduleMemberId", null);
		oldMermberSet = null;
		if ("true".equals(dto.getIsAjax())) {
			super.writeResult("sucess");
			return super.globalAjax();
		}
		return super.getView("assignPeople");
	}

	public View setKlog(BusiRequestEvent req) {
		OutLineManagerDto dto = super.getDto(OutLineManagerDto.class, req);
		String hql = "update OutlineInfo set klc=? where moduleId=?";
		outLineService.executeUpdateByHql(hql,
				new Object[] { dto.getKlc(), dto.getCurrNodeId() });
		super.writeResult("sucess");
		dto = null;
		return super.globalAjax();
	}

	public View setCaseCount(BusiRequestEvent req) {
		OutLineManagerDto dto = super.getDto(OutLineManagerDto.class, req);
		String hql = "update OutlineInfo set caseCount=? where moduleId=?";
		outLineService.executeUpdateByHql(hql, new Object[] {
				dto.getKlc().intValue(), dto.getCurrNodeId() });
		super.writeResult("sucess");
		dto = null;
		return super.globalAjax();
	}

	public View setScrpCount(BusiRequestEvent req) {
		OutLineManagerDto dto = super.getDto(OutLineManagerDto.class, req);
		String hql = "update OutlineInfo set scrpCount=? where moduleId=?";
		outLineService.executeUpdateByHql(hql, new Object[] {
				dto.getKlc().intValue(), dto.getCurrNodeId() });
		super.writeResult("sucess");
		dto = null;
		return super.globalAjax();
	}

	public View chgRepType(BusiRequestEvent req) {
		OutLineManagerDto dto = super.getDto(OutLineManagerDto.class, req);
		String hql = "update OutlineInfo set scrpCount=? where moduleId=?";
		outLineService.executeUpdateByHql(hql, new Object[] {
				dto.getKlc().intValue(), dto.getCurrNodeId() });
		super.writeResult("sucess");
		dto = null;
		return super.globalAjax();
	}

	public View setSceneCount(BusiRequestEvent req) {
		OutLineManagerDto dto = super.getDto(OutLineManagerDto.class, req);
		String hql = "update OutlineInfo set sceneCount=? where moduleId=?";
		outLineService.executeUpdateByHql(hql, new Object[] {
				dto.getKlc().intValue(), dto.getCurrNodeId() });
		super.writeResult("sucess");
		dto = null;
		return super.globalAjax();
	}

	public View setQuotiety(BusiRequestEvent req) {
		OutLineManagerDto dto = super.getDto(OutLineManagerDto.class, req);
		String hql = "update OutlineInfo set quotiety=? where moduleId=?";
		outLineService.executeUpdateByHql(hql, new Object[] {
				dto.getKlc().doubleValue(), dto.getCurrNodeId() });
		super.writeResult("sucess");
		dto = null;
		return super.globalAjax();
	}

	private String getNextNum(String parentId, String parentNum) {
		PropertiesBean conf = (PropertiesBean) Context.getInstance().getBean(
				"ContextProperties");
		String maxLevelSql = null;
		if (conf.getProperty("config.db.driver") != null
				&& conf.getProperty("config.db.driver").indexOf("mysql") > 0) {
			maxLevelSql = "select IfNull(max(t.modulenum),0) as count  from t_outlineinfo t  where t.superModuleId = ?";
		} else {
			maxLevelSql = "select nvl(max(t.modulenum),0) as count  from t_outlineinfo t  where t.superModuleId = ?";
		}
		List<Object> numList = outLineService.findBySql(maxLevelSql, null,
				parentId);
		String moduleNum = numList.get(0).toString();
		if ("0".equals(moduleNum)) {
			if (parentNum == null) {
				return "001";
			}
			return parentNum + "001";
		}
		String countStr = moduleNum.substring(moduleNum.length() - 3);
		int count = Integer.parseInt(countStr);
		count++;
		String prefix = moduleNum.substring(0, moduleNum.lastIndexOf(countStr));
		String nextNum = prefix + String.valueOf((count + 1000)).substring(1);
		return nextNum;
	}

	private String getNexnum(String moduleNum) {
		String countStr = moduleNum.substring(moduleNum.length() - 3);
		int count = Integer.parseInt(countStr);
		count++;
		String prefix = moduleNum.substring(0, moduleNum.lastIndexOf(countStr));
		String nextNum = prefix + String.valueOf((count + 1000)).substring(1);
		return nextNum;
	}

	private List<ListObject> getTemMemberListObjs(String taskId, Integer actor) {

		StringBuffer sb = new StringBuffer();
		sb.append("select new cn.com.codes.framework.common.ListObject(");
		sb.append(" u.id as keyObj,(u.loginName||'('||u.name||')') as valueObj )");
		sb.append(" from TaskUseActor ta  join ta.user u where  ta.isEnable = 1 and ta.taskId=? and ta.actor=? ");
		return outLineService.findByHql(sb.toString(), taskId, actor);
	}

	private String moveCheck(OutLineManagerDto dto) {
		Long targetId = dto.getTargetId();
		Long sourceId = dto.getCurrNodeId();
		StringBuffer sb = new StringBuffer();
		sb.append("select count(p.modulename) as count");
		sb.append("  from t_outlineinfo c, ");
		sb.append(" (select s.modulename from t_outlineinfo s where s.SUPERMODULEID =? ) p");
		sb.append(" where c.modulename = p.modulename and c.moduleid =? and c.TASKID=?");
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		int repCount = outLineService
				.getHibernateGenericController()
				.getResultCountBySql(sb.toString(), "p.modulename", targetId,
						sourceId, taskId).intValue();
		if (repCount == 0) {
			return "ok";
		}
		return "cancel";
	}

	private boolean isCanDelete(Long moduleId) {

		return true;
	}

	private String toTreeStr(List<OutlineInfo> list) {
		if (list == null || list.isEmpty()) {
			return "0,1,功能分解树尚未建立,0.1";
		}
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
					Integer moduleState = outLine.getModuleState();
					if (moduleState == 1) {
						treeJsonVo.setText("<span style='color:red'>" + outLine.getModuleName() + "</span>");
					} else {
						treeJsonVo.setText(outLine.getModuleName());
					}
					treeJsonVo.setCurrLevel(outLine.getModuleLevel());
					treeJsonVo.setModuleState(outLine.getModuleState());
//					treeJsonVo.setState(this.formatterStr(outLine.getModuleState()));
//					treeJsonVo.setState(this.formatterStr(outLine.getIsleafNode()));
					treeJsonVo.setState("open");
					treeJsonVo.setLeaf(this.formatterInt(outLine.getIsleafNode()));
				}else{
					TreeJsonVo treeJsonVoC = new TreeJsonVo();
					treeJsonVoC.setId(outLine.getModuleId().toString());
					Integer moduleState = outLine.getModuleState();
					if (moduleState == 1) {
						treeJsonVoC.setText("<span style='color:red'>" + outLine.getModuleName() + "</span>");
					} else {
						treeJsonVoC.setText(outLine.getModuleName());
					}
					treeJsonVoC.setCurrLevel(outLine.getModuleLevel());
					treeJsonVoC.setModuleState(outLine.getModuleState());
					treeJsonVoC.setState(this.formatterStr(outLine.getIsleafNode()));
					treeJsonVoC.setLeaf(this.formatterInt(outLine.getIsleafNode()));
					treeJsonVo.getChildren().add(treeJsonVoC);
				}
			}
			treeJsonVos.add(treeJsonVo);
		}else{
			for (OutlineInfo outLine : list) {
				TreeJsonVo treeJsonVoC = new TreeJsonVo();
				treeJsonVoC.setId(outLine.getModuleId().toString());
				Integer moduleState = outLine.getModuleState();
				if (moduleState == 1) {
					treeJsonVoC.setText("<span style='color:red'>" + outLine.getModuleName() + "</span>");
				} else {
					treeJsonVoC.setText(outLine.getModuleName());
				}
				treeJsonVoC.setCurrLevel(outLine.getModuleLevel());
				treeJsonVoC.setModuleState(outLine.getModuleState());
				treeJsonVoC.setState(this.formatterStr(outLine.getIsleafNode()));
				treeJsonVoC.setLeaf(this.formatterInt(outLine.getIsleafNode()));
				treeJsonVos.add(treeJsonVoC);
			}
		}
		
		return treeJsonVos;
	}

	public synchronized void  addImportNodes(
			Map<String, Map<String, OutlineInfo>> allNodemap,
			OutlineInfo rootNode, Map<String ,CasePri> casePriMap,Map<String ,CaseType> caseTypeMap,List<TestCaseInfo> caseList){
		Map<String, OutlineInfo> parentInMap = null;
		 Map<String, OutlineInfo> allSaveNodemap = new HashMap<String, OutlineInfo>();
		for(int i =2; i<10; i++){
			Map<String, OutlineInfo> outMap = allNodemap.get(String.valueOf(i));
			if(i==2){
				parentInMap = new HashMap<String, OutlineInfo>(1);
				parentInMap.put("root", rootNode);
			}else{
				parentInMap = allNodemap.get(String.valueOf((i-1)));
			}
			this.addNodes(outMap, parentInMap);
			allSaveNodemap.putAll(outMap);
		}
		
		Iterator<Entry<String, CasePri>>  it = casePriMap.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, CasePri> me = it.next();
			if(me.getValue().getTypeId()==null){
				outLineService.add(me.getValue());
			}
			
		}

		Iterator<Entry<String, CaseType>>  it2 = caseTypeMap.entrySet().iterator();
		while(it2.hasNext()){
			Entry<String, CaseType> me = it2.next();
			if(me.getValue().getTypeId()==null){
				outLineService.add(me.getValue());
			}
			
		}
		
		for(TestCaseInfo tc :caseList ){
			String taskId = SecurityContextHolderHelp.getCurrTaksId();
			tc.setTaskId(taskId);
			tc.setCaseTypeId(caseTypeMap.get(tc.getTypeName()).getTypeId());
			tc.setPriId(casePriMap.get(tc.getPriName()).getTypeId());
			tc.setModuleId(allSaveNodemap.get(tc.getModuleName()).getModuleId());
			tc.setModuleNum(allSaveNodemap.get(tc.getModuleName()).getModuleNum());
		}
		caseService.saveImpotTestCase(caseList);
	}

	private OutlineInfo getParentNodeByName(Map<String, OutlineInfo> parentInMap ,String currNodeParentName){
		
		Iterator<Entry<String, OutlineInfo>>  it = parentInMap.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, OutlineInfo> me = it.next();
			if(me.getValue().getModuleName().equals(currNodeParentName)){
				return me.getValue();
			}
		}
		return null;
	}
	
	public  void  addNodes(Map<String, OutlineInfo> outMap,Map<String, OutlineInfo> parentInMap) {
		
		String comapnyId = SecurityContextHolderHelp.getCompanyId();
		Iterator<Entry<String, OutlineInfo>>  it = outMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, OutlineInfo> me = it.next();
			OutlineInfo outline = me.getValue() ;
			if(outline.getModuleId()==null){
				OutlineInfo parent = this.getParentNodeByName(parentInMap,outline.getSuperModuleName());
				outline.setIsleafNode(1);
				outline.setModuleState(0);
				outline.setTaskId(SecurityContextHolderHelp.getCurrTaksId());
				outline.setSuperModuleId(parent.getModuleId());
				outline.setCompanyId(comapnyId);
				outline.setReqType(0);
				String moduleNum = this.getNextNum(parent.getModuleId().toString(),
				parent.getModuleNum());

				
				String seqStr = moduleNum.substring(moduleNum.length() - 2);
				int count = Integer.parseInt(seqStr);
				outline.setModuleNum(moduleNum);
				outline.setSeq(count);
				outLineService.add(outline);
				if(parent.getIsleafNode()==1||parent.getIsleafNode()==null){
					parent.setIsleafNode(0);
					if(parent.getModuleId()!=null){
						outLineService.update(parent);
					}
				}

				
			}
		}
	}
	
	public synchronized View addNodes(BusiRequestEvent req) {
		OutLineManagerDto dto = super.getDto(OutLineManagerDto.class, req);
		String command = dto.getCommand();
		Long parentId = dto.getParentNodeId();
		List<OutlineInfo> list = new ArrayList<OutlineInfo>();
		String comapnyId = SecurityContextHolderHelp.getCompanyId();
		if ("addchild".equals(command)) {
			parentId = dto.getCurrNodeId();
			dto.setParentNodeId(parentId);
		}
		if (parentId == 0) {
			throw new DataBaseException("不能增加根节点");
		}
		// 这样查询是为了防止前台篡改ID提交 所以加上任务Id
		String hql = "from OutlineInfo where moduleId=? and taskId=?";
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		OutlineInfo parent = (OutlineInfo) outLineService.findByNoCache(hql,
				parentId, taskId).get(0);
		Integer currLevel = parent.getModuleLevel() + 1;
		if (this.reNameCheck(dto) != null) {
			return super.globalAjax();
		}
		String moduleNum = this.getNextNum(parentId.toString(),
				parent.getModuleNum());
		int i = 0;
		String currVer = null;
		String seqStr = moduleNum.substring(moduleNum.length() - 2);
		int count = Integer.parseInt(seqStr);
		// 集成测试时，二级模块要设置当前片本
//		if (currLevel == 2) {
//			hql = " select new TestTaskDetail(outlineState,testPhase,currentVersion) from TestTaskDetail where taskId=?";
//			TestTaskDetail taskDetal = (TestTaskDetail) outLineService
//					.findByNoCache(hql,
//							SecurityContextHolderHelp.getCurrTaksId()).get(0);
//			// 集成测试，一级模块不建版本了，所以下面的己无用
//			// if(taskDetal.getTestPhase()==1){
//			// currVer =
//			// testTaskService.getCurrVer(SecurityContextHolderHelp.getCurrTaksId(),
//			// parentId);
//			// }
//		}
		int index = 0;
		for (String name : dto.getModuleData()) {
			if (name != null && !"".equals(name.trim())) {
				OutlineInfo outline = new OutlineInfo();
				outline.setIsleafNode(1);
				if (dto.getCaseCount()[index] != 0) {
					outline.setCaseCount(dto.getCaseCount()[index]);
				}
				if (dto.getQuotiety()[index] != 0) {
					outline.setQuotiety(dto.getQuotiety()[index]);
				}
				if (dto.getScrpCount()[index] != null
						&& dto.getScrpCount()[index] != 0) {
					outline.setScrpCount(dto.getScrpCount()[index]);
				}
				if (dto.getSceneCount()[index] != null
						&& dto.getSceneCount()[index] != 0) {
					outline.setSceneCount(dto.getSceneCount()[index]);
				}
				outline.setModuleState(0);
				outline.setTaskId(SecurityContextHolderHelp.getCurrTaksId());
				outline.setModuleName(name);
				outline.setSuperModuleId(parentId);
				outline.setModuleLevel(currLevel);
				outline.setCompanyId(comapnyId);
				outline.setCurrVer(currVer);
				outline.setReqType(dto.getReqType());
				if (i == 0) {
					outline.setModuleNum(moduleNum);
					outline.setSeq(count);
				} else {
					moduleNum = this.getNexnum(moduleNum);
					outline.setModuleNum(moduleNum);
					outline.setSeq(++count);
				}
				list.add(outline);
				i++;
			}
			index++;
		}
		List rest = outLineService.addNodes(list);
		super.writeResult(this.toTreeStr(rest));
		dto = null;
		return super.globalAjax();
	}

	private String reNameCheck(OutLineManagerDto dto) {
		StringBuffer sb = new StringBuffer("select new OutlineInfo(");
		sb.append(" moduleId,superModuleId,isleafNode, moduleName,moduleState,moduleLevel,moduleNum )");
		sb.append(" from OutlineInfo where  superModuleId=? order by moduleId");
		List<OutlineInfo> list = outLineService.findByHql(sb.toString(),
				dto.getParentNodeId());
		for (String name : dto.getModuleData()) {
			if (name != null && !"".equals(name.trim())) {
				for (OutlineInfo outline : list) {
					if (outline.getModuleName().equals(name)) {
						super.writeResult("reName_" + name + "$"
								+ this.toTreeStr(list));
						return "reName";
					}
				}
			}
		}
		return null;
	}

	public View submitModule(BusiRequestEvent req) {
		OutLineManagerDto dto = super.getDto(OutLineManagerDto.class, req);
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		dto.setTaskId(taskId);
		String taskHql = "select new TestTaskDetail(outlineState,testPhase,currentVersion) from TestTaskDetail where taskId=?";
		TestTaskDetail task = (TestTaskDetail) outLineService.findByHql(
				taskHql, dto.getTaskId()).get(0);
		task.setTaskId(taskId);
		if (task.getOutlineState() == 1) {
			super.writeResult("sucess");
			return super.globalAjax();
		}
		String rootNodeIdHql = "select new OutlineInfo(moduleId) from OutlineInfo where taskId=? and superModuleId =0 ";
		OutlineInfo rootOutLine = (OutlineInfo) outLineService.findByHql(
				rootNodeIdHql, taskId).get(0);
		int i = 1;
		if (moduleNumControl.get(taskId) != null) {
			while (moduleNumControl.get(taskId) != null) {
				if (i < 4) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						logger.error(e);
					}
				} else {
					setModuleMumExe(taskId, rootOutLine, task);
					super.writeResult("sucess");
					return super.globalAjax();
				}
				i++;
			}
		}
		setModuleMumExe(taskId, rootOutLine, task);
		super.writeResult("sucess");
		dto = null;
		return super.globalAjax();
	}

	private void setModuleMumExe(String taskId, OutlineInfo rootOutLine,
			TestTaskDetail task) {
		try {
			moduleNumControl.put(task.getTaskId(), "");
			String hql = this.buildHqlForSetMum();
			List<OutlineInfo> modList = outLineService.findByHql(hql, taskId,
					rootOutLine.getModuleId());
			String maxLevelSql = "select max(t.modulelevel) from t_outlineinfo t  where t.taskid = ?";
			List<Object> level = outLineService.findBySql(maxLevelSql, null,
					taskId);
			int maxLevel = Integer.parseInt(level.get(0).toString());
			List<Map<String, Object>> NumInfoList = this.getModNumInMemory(
					modList, maxLevel, task.getCurrentVersion());
			outLineService.submitMoudle(NumInfoList, taskId);
		} catch (NumberFormatException e) {
			logger.error(e);
		} catch (RuntimeException e) {
			logger.error(e);
			throw e;
		} finally {
			moduleNumControl.remove(taskId);
		}
	}

	private List<Map<String, Object>> getModNumInMemory(
			List<OutlineInfo> modList, int maxLevel, String currVer) {
		List<Map<String, Object>> NumInfoList = new ArrayList<Map<String, Object>>();
		Map<String, String> parentHolder = new HashMap();
		for (int i = 2; i < maxLevel + 1; i++) {
			int count = 1;
			String currentParentId = null;
			for (OutlineInfo obj : modList) {
				if (obj.getModuleLevel() == i) {
					if (i == 2) {
						int num = 1000 + count;
						String moduleNum = String.valueOf(num).substring(1);
						Map map = new HashMap(2);
						map.put("moduleId", obj.getModuleId());
						map.put("moduleNum", moduleNum);
						map.put("currVer", currVer);
						NumInfoList.add(map);
						if (obj.getIsleafNode() == 0) {
							parentHolder.put(obj.getModuleId().toString(),
									moduleNum);
						}
						count++;
					} else {
						if (currentParentId == null) {
							currentParentId = obj.getSuperModuleId().toString();
							count = 1;
						} else if (!currentParentId.equals(obj
								.getSuperModuleId().toString())) {
							currentParentId = obj.getSuperModuleId().toString();
							count = 1;
						} else {
							count++;
						}
						int num = 1000 + count;
						String moduleNum = parentHolder.get(currentParentId)
								+ String.valueOf(num).substring(1);
						Map map = new HashMap(2);
						map.put("moduleId", obj.getModuleId());
						map.put("moduleNum", moduleNum);
						map.put("currVer", currVer);
						NumInfoList.add(map);
						if (obj.getIsleafNode() == 0) {
							parentHolder.put(obj.getModuleId().toString(),
									moduleNum);
						}
					}
				}
			}
		}
		return NumInfoList;
	}
	
	
	private String buildHqlForSetMum() {
		StringBuffer sb = new StringBuffer();
		sb.append("select new OutlineInfo");
		sb.append("  (moduleId,superModuleId, moduleLevel,isleafNode)");
		sb.append("  from OutlineInfo");
		sb.append("  where taskId=? and moduleId<>?");
		sb.append("  order by moduleLevel, superModuleId, moduleId");
		return sb.toString();
	}

	private List<Map<String, Object>> getChildAdjustInfo(OutLineManagerDto dto,
			int levelOffSet, String parentNum, String oldParentNum) {

		Long currentId = dto.getCurrNodeId();
		String hql = this.buildLoadAllChild();
		List<OutlineInfo> list = outLineService.findByHql(hql, oldParentNum
				+ "%", dto.getTaskId(), currentId);
		List<Map<String, Object>> adjustInfoList = new ArrayList<Map<String, Object>>(
				list.size());
		// 这是后来加的，主要是更新用例的块编号，这么做是为了用例查询时，以后不用视图了
		List<Map<String, Object>> adjustCaseInfoList = new ArrayList<Map<String, Object>>(
				list.size());
		for (OutlineInfo obj : list) {
			Map map = new HashMap(3);
			Map caseMap = new HashMap(3);
			map.put("moduleId", obj.getModuleId());
			map.put("moduleLevel", obj.getModuleLevel() - levelOffSet + 1);
			map.put("moduleNum",
					obj.getModuleNum().replaceFirst(oldParentNum, parentNum));

			caseMap.put("moduleId", obj.getModuleId());
			caseMap.put("moduleNum",
					obj.getModuleNum().replaceFirst(oldParentNum, parentNum));
			caseMap.put("taskId", dto.getTaskId());
			if ((obj.getModuleLevel() - levelOffSet + 1) > 100) {
				adjustInfoList.clear();
				adjustInfoList = null;
				adjustCaseInfoList.clear();
				adjustCaseInfoList = null;
				throw new DataBaseException("最多只能99级");

			}
			adjustInfoList.add(map);
			adjustCaseInfoList.add(caseMap);
		}
		Map caseMap = new HashMap(3);
		caseMap.put("moduleId", dto.getCurrNodeId());
		caseMap.put("moduleNum", dto.getModuleNum());
		caseMap.put("taskId", dto.getTaskId());
		adjustCaseInfoList.add(caseMap);
		dto.setHqlParamLists(adjustCaseInfoList);
		return adjustInfoList;
	}
	
	public Boolean formatterInt(Integer value) {
		if(value==0){
			return false;
		}else{
			return true;
		}
	}
	
	public String formatterStr(Integer value) {
		if(value==0){
			return "closed";
		}else{
			return "open";
		}
	}

	private String buildLoadAllChild() {
		StringBuffer sb = new StringBuffer();
		sb.append(" select  new OutlineInfo(moduleId, moduleLevel, moduleNum) from OutlineInfo where  moduleNum like ? and taskId=? and moduleId<>? order by moduleId");
		return sb.toString();
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

	public CaseManagerService getCaseService() {
		return caseService;
	}

	public void setCaseService(CaseManagerService caseService) {
		this.caseService = caseService;
	}

	
	public View getTaskNodeCount(BusiRequestEvent req) {
		OutLineManagerDto dto = super.getDto(OutLineManagerDto.class, req);
		String taskId = dto.getTaskId();
		if(!StringUtils.isNullOrEmpty(taskId)){
			taskId= SecurityContextHolderHelp.getCurrTaksId();
		}
		Long count = outLineService.getTaskNodeCount(taskId);
		super.writeResult(count.toString());
		return super.globalAjax();
	}
	
}
