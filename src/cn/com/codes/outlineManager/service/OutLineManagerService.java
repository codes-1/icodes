package cn.com.codes.outlineManager.service;

import java.util.List;
import java.util.Map;

import cn.com.codes.framework.app.services.BaseService;
import cn.com.codes.object.OutlineInfo;
import cn.com.codes.object.OutlineTeamMember;
import cn.com.codes.object.User;
import cn.com.codes.outlineManager.dto.OutLineManagerDto;

public interface OutLineManagerService extends BaseService {
	
	public List<OutlineInfo> loadTree(OutLineManagerDto dto);
	
	public List<OutlineInfo> loadNormalNode(String taskId,Long currNodeId);
	
	public List addNodes(List<OutlineInfo> list);
	
	//public void addNodes(OutlineInfo out ,OutlineInfo parentOut);
	
	public void updateNode(OutLineManagerDto dto);
	
	public void switchState(OutLineManagerDto dto);
	
	public void delete(OutlineInfo outline);
	
	public List<OutlineTeamMember> getInitAsignPeople(OutLineManagerDto dto,List<Long> assignIdList);
	
	public void move(OutLineManagerDto dto,List<Map<String, Object>> adjustInfo);
	
	public List<OutlineInfo> loadTreePeople(OutLineManagerDto dto);
	
	public void submitMoudle(List<Map<String, Object>> NumInfoList, String taskId);
	
	public List<Long> getAllAssignNode(OutLineManagerDto dto);
	
	public List<User> getModuleMemb(Long moduleId,Integer userRole);
	
	public void moveUpItem(OutLineManagerDto dto);
	
	public void moveDownItem(OutLineManagerDto dto);
	
	public void initSeq(OutlineInfo moveNodeInfo);
	
	public Long getTaskNodeCount(String taskId);
}
