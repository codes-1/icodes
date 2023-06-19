package cn.com.codes.testRequirement.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.framework.app.services.BaseServiceImpl;
import cn.com.codes.object.ModuleVerRec;
import cn.com.codes.object.OutlineInfo;
import cn.com.codes.object.OutlineTeamMember;
import cn.com.codes.object.TestTaskDetail;
import cn.com.codes.object.User;
import cn.com.codes.testRequirement.dto.TestRequirementDto;
import cn.com.codes.testRequirement.service.TestRequirementService;

public class TestRequirementServiceImpl extends BaseServiceImpl implements
		TestRequirementService {

	public List<User> getModuleDevMemb(Long moduleId) {
		String hql = "select new User(u.id,u.name ,u.loginName) from  OutlineTeamMember o join o.user u  where o.moduleId=? ";
		List<User> list = this.findByHql(hql, moduleId);
		return list;
	}

	public List<OutlineInfo> loadTree(TestRequirementDto dto) {

		String taskId = dto.getTaskId();
		Long currNodeId = dto.getCurrNodeId();
		OutlineInfo rootOutLine = null;
		if (currNodeId == null) {
			String taskHql = "select new TestTaskDetail(outlineState,testPhase) from TestTaskDetail where taskId=?";
			TestTaskDetail task = (TestTaskDetail) this.findByHql(taskHql,
					dto.getTaskId()).get(0);
			dto.setIsCommit(task.getOutlineState());
			String rootNodeIdHql = "select new OutlineInfo(moduleId,superModuleId,isleafNode, moduleName,moduleState,moduleNum) from OutlineInfo where taskId=? and superModuleId =0 ";
			rootOutLine = (OutlineInfo) this.findByHql(rootNodeIdHql, taskId)
					.get(0);
			currNodeId = rootOutLine.getModuleId();
			dto.setCurrNodeId(currNodeId);
		}
		String loadTreeHql = this.buildLoadChildHql(false);
		List<OutlineInfo> list = this.findByHql(loadTreeHql, currNodeId);
		if (rootOutLine != null && list.size() > 0) {
			list.add(0, rootOutLine);
		} else if (rootOutLine != null) {
			list.add(rootOutLine);
		}
		return list;
	}

	public List<OutlineInfo> loadNormalNode(String taskId, Long currNodeId) {
		OutlineInfo rootOutLine = null;
		if (currNodeId == null) {
			String rootNodeIdHql = "select new OutlineInfo(moduleId,superModuleId,isleafNode, moduleName,moduleState,moduleNum) from OutlineInfo where taskId=? and superModuleId =0 ";
			rootOutLine = (OutlineInfo) this.findByHql(rootNodeIdHql, taskId)
					.get(0);
			currNodeId = rootOutLine.getModuleId();
		}
		String loadTreeHql = this.buildLoadChildHql(true);
		List<OutlineInfo> list = this.findByHql(loadTreeHql, currNodeId);
		if (rootOutLine != null && list.size() > 0) {
			list.add(0, rootOutLine);
		} else if (rootOutLine != null) {
			list.add(rootOutLine);
		}
		return list;
	}

	public List<OutlineInfo> loadTreePeople(TestRequirementDto dto) {
		String hql = this.buildLoadChildHql(false);
		String taskId = dto.getTaskId();
		Long currNodeId = dto.getCurrNodeId();
		List list = this.findByHql(hql, currNodeId);
		// 子模块中能有重复的用户，所以这里不查询模块ID
		String memberHql = "select distinct new OutlineTeamMember(userId, userRole) from OutlineTeamMember where moduleId in(:moduleIds)";
		String userHql = "select new User(id, (loginName||'('||name||')') as name) from User where id=? and companyId=?";
		List<Long> ids = new ArrayList<Long>();
		for (int i = 0; i < list.size(); i++) {
			OutlineInfo outline = (OutlineInfo) list.get(i);
			Set<OutlineTeamMember> teamMeb = new HashSet<OutlineTeamMember>();
			ids.clear();
			if (outline.getIsleafNode() == 0) {
				String childHql = this.buildLoadAllChild(false);
				List<OutlineInfo> childList = this.findByHql(childHql, outline
						.getModuleNum()
						+ "%", taskId, outline.getModuleId());

				for (OutlineInfo child : childList) {
					ids.add(child.getModuleId());
				}
				Map<String, Object> praValuesMap = new HashMap<String, Object>();
				this.sortLongList(ids);
				praValuesMap.put("moduleIds", ids);
				if (ids.size() == 0) {
					continue;
				}
				List<OutlineTeamMember> memberList = this
						.findByHqlWithValuesMap(memberHql, praValuesMap, false);
				teamMeb.addAll(memberList);
			} else {
				ids.add(outline.getModuleId());
				Map<String, Object> praValuesMap = new HashMap<String, Object>();
				this.sortLongList(ids);
				praValuesMap.put("moduleIds", ids);
				List<OutlineTeamMember> memberList = this
						.findByHqlWithValuesMap(memberHql, praValuesMap, false);
				teamMeb.addAll(memberList);
			}
			if (teamMeb != null && teamMeb.size() > 0) {
				String compId = SecurityContextHolderHelp.getCompanyId();
				for (Iterator<OutlineTeamMember> it = teamMeb.iterator(); it
						.hasNext();) {
					OutlineTeamMember member = it.next();
					User user = (User) this.findByHql(userHql,
							member.getUserId(), compId).get(0);
					member.setUser(user);
				}
			}
			outline.setTeamMember(teamMeb);
		}
		ids = null;
		return list;
	}

	public List addNodes(List<OutlineInfo> list) {
		int isleafNode = 0;
		Long parentId = null;
		String taskId = "";
		if (list.size() > 0) {
			parentId = list.get(0).getSuperModuleId();
			OutlineInfo parent = super.getHibernateGenericController().load(
					OutlineInfo.class, parentId);
			taskId = parent.getTaskId();
			isleafNode = parent.getIsleafNode();
			if (isleafNode == 1) {
				parent.setIsleafNode(0);
				this.update(parent);
			}
		}
		for (OutlineInfo outLine : list) {
			this.add(outLine);
		}
		// 如增加前就是非叶子节点，要查所有节点
		if (parentId != null && isleafNode == 0) {
			String loadTreeHql = this.buildLoadChildHql(false);
			return this.findByHql(loadTreeHql, parentId);

		}
		return list;
	}

	public void updateNode(TestRequirementDto dto) {
		Long nodeId = dto.getCurrNodeId();
		String nodeName = dto.getNodeName();
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		String hql = "update OutlineInfo set moduleName=? where moduleId=? and taskId=?";
		super.getHibernateGenericController().executeUpdate(hql, nodeName,
				nodeId, taskId);
	}

	public void delete(OutlineInfo outline) {
		String hql = "delete from OutlineInfo where moduleId=? and taskId=? and superModuleId !=0";
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		this.executeUpdateByHql(hql, new Object[] { outline.getModuleId(),
				taskId });
		this.setParentIsLeaf(outline.getSuperModuleId());

	}

	public void switchState(TestRequirementDto dto) {
		Long nodeId = dto.getCurrNodeId();
		Integer moduleState = dto.getModuleState();
		if (moduleState == 0) {
			moduleState = 1;
		} else {
			moduleState = 0;
		}
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		String getNumHql = "select new OutlineInfo(moduleNum) from OutlineInfo where moduleId =? and taskId=?";
		List<OutlineInfo> numList = this.findByHql(getNumHql, nodeId, taskId);
		String moduleNum = numList.get(0).getModuleNum() + "%";
		String hql = "update OutlineInfo set moduleState=? where taskId=? and moduleNum like ?  ";
		this.getHibernateGenericController().executeUpdate(hql, moduleState,
				taskId, moduleNum);
	}

	public List<OutlineTeamMember> getInitAsignPeople(TestRequirementDto dto,
			List<Long> assignIdList) {
		StringBuffer hql = new StringBuffer();
		hql
				.append(" select new OutlineTeamMember(moduleMemberId,moduleId,userId,userRole) ");
		hql.append(" from OutlineTeamMember where moduleId in(:moduleIds)");
		Map<String, Object> praValuesMap = new HashMap<String, Object>();
		praValuesMap.put("moduleIds", assignIdList);
		return this.findByHqlWithValuesMap(hql.toString(), praValuesMap, false);
	}

	public List<Long> getAllAssignNode(TestRequirementDto dto) {
		String assignNIds = dto.getAssignNIds();
		String[] idsArr = assignNIds.split("，");
		Map<String, Object> paraValues = new HashMap<String, Object>();
		List<Long> ids = new ArrayList<Long>();
		for (String id : idsArr) {
			ids.add(new Long(id));
		}
		String getNumHql = "select new OutlineInfo(moduleNum) from OutlineInfo where moduleId in(:moduleIds)";
		paraValues.put("moduleIds", ids);
		List<OutlineInfo> numList = this.findByHqlWithValuesMap(getNumHql,
				paraValues, false);
		List<Long> idsList = new ArrayList<Long>();
		String hql = this.buildAllNumHql(numList.size());
		Object[] paraVavues = new Object[numList.size() + 1];
		paraVavues[0] = dto.getTaskId();
		for (int i = 0; i < numList.size(); i++) {
			paraVavues[i + 1] = numList.get(i).getModuleNum() + "%";
		}
		List<OutlineInfo> chdList = this.getHibernateGenericController()
				.findByHql(hql, paraVavues);
		for (OutlineInfo obj : chdList) {
			idsList.add(obj.getModuleId());
		}
		return idsList;
	}

	public void move(TestRequirementDto dto, List<Map<String, Object>> adjustInfo) {
		Long targetId = dto.getTargetId();
		Long sourceId = dto.getCurrNodeId();
		String hql = "update OutlineInfo set superModuleId=?,moduleLevel=? ,moduleNum=?,currVer=? where moduleId=?";
		this.getHibernateGenericController().executeUpdate(hql, targetId,
				dto.getCurrLevel(), dto.getModuleNum(), dto.getCurrVer(),
				sourceId);
		this.setParentIsLeaf(dto.getParentNodeId());
		hql = "update OutlineInfo set moduleLevel=:moduleLevel,moduleNum=:moduleNum where moduleId=:moduleId";
		this.excuteBatchHql(hql, adjustInfo);
		hql = "update OutlineInfo set isleafNode=? where moduleId=?";
		this.getHibernateGenericController().executeUpdate(hql, 0, targetId);
		hql = "update TestCaseInfo set moduleNum=:moduleNum where moduleId=:moduleId and taskId=:taskId";
		this.excuteBatchHql(hql, dto.getHqlParamLists());
		adjustInfo = null;
		dto.setHqlParamLists(null);
	}

	public void submitMoudle(List<Map<String, Object>> NumInfoList,
			String taskId) {
		String taskHql = "select new TestTaskDetail(outlineState,testPhase,currentVersion) from TestTaskDetail where taskId=?";
		TestTaskDetail task = (TestTaskDetail) this.findByHql(taskHql, taskId)
				.get(0);
		task.setTaskId(taskId);
		// 可能会并发所以这里再确认一次
		if (task.getOutlineState() == 1) {
			return;
		}
		String hql = "update OutlineInfo set moduleNum=:moduleNum ,currVer=:currVer where moduleId=:moduleId";
		this.excuteBatchHql(hql, NumInfoList);
		this
				.getHibernateGenericController()
				.executeUpdate(
						"update TestTaskDetail set outlineState=1 ,testSeq=1 where taskId=?",
						taskId);
	}

	/**
	 * @deprecated 提交模块时无需再建模块板本 所以不再使用
	 * @param task
	 */
	private void addVerRec(TestTaskDetail task) {
		// 系统测试
		if (task.getTestPhase() == 2) {
			ModuleVerRec mv = new ModuleVerRec();
			mv.setSeq(1);
			mv.setModuleId(new Long(0));
			mv.setCreatdate(new Date());
			mv.setTaskId(task.getTaskId());
			mv.setModuleVersion(task.getCurrentVersion());
			this.add(mv);
			return;
		}
		String hql = "select new OutlineInfo(moduleId) from OutlineInfo where taskId=? and moduleLevel=2";
		List<OutlineInfo> list = this.findByHql(hql, task.getTaskId());
		for (OutlineInfo outLine : list) {
			ModuleVerRec mv = new ModuleVerRec();
			mv.setSeq(1);
			mv.setModuleId(outLine.getModuleId());
			mv.setCreatdate(new Date());
			mv.setTaskId(task.getTaskId());
			mv.setModuleVersion(task.getCurrentVersion());
			this.add(mv);
		}
	}

	private void setParentIsLeaf(Long superModuleId) {
		String hql = "from OutlineInfo where superModuleId=?";
		int childCount = this.getHibernateGenericController().getResultCount(
				hql, new Object[] { superModuleId }, "moduleId").intValue();
		if (childCount == 0) {
			hql = "update OutlineInfo set isleafNode=? where moduleId=?";
			super.getHibernateGenericController().executeUpdate(hql, 1,
					superModuleId);
		}
	}

	private String buildLoadChildHql(boolean onlyNormal) {
		StringBuffer sb = new StringBuffer("select new OutlineInfo(");
		sb.append(" moduleId,superModuleId,isleafNode, moduleName,moduleState,moduleNum ,klc,moduleLevel)");
		sb.append(" from OutlineInfo where  superModuleId=? ");
		if (onlyNormal) {
			sb.append(" and moduleState= 0 ");
		}
		sb.append(" order by moduleId");
		return sb.toString();
	}

	private String buildLoadAllChild(boolean onlyNormal) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select distinct new OutlineInfo(moduleId) from OutlineInfo where  moduleNum like ? and taskId=? and moduleId<>? ");
		if (onlyNormal) {
			sb.append(" and moduleState= 0 ");
		}
		sb.append(" order by moduleId");
		return sb.toString();
	}

	private String buildAllNumHql(int numCount) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select  new OutlineInfo(moduleId)");
		sb.append("   from OutlineInfo where  taskId=? and (");
		for (int i = 0; i < numCount; i++) {
			if (i > 0) {
				sb.append("  or  moduleNum like  ? ");
			} else {
				sb.append("  moduleNum like ? ");
			}
		}
		sb.append("  ) ");
		return sb.toString();
	}

	public void toString(StringBuffer bf) {

	}



}
