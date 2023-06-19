package cn.com.codes.testRequirement.service;

import java.util.List;
import java.util.Map;

import cn.com.codes.framework.app.services.BaseService;
import cn.com.codes.object.OutlineInfo;
import cn.com.codes.object.OutlineTeamMember;
import cn.com.codes.object.User;
import cn.com.codes.testRequirement.dto.TestRequirementDto;

public interface TestRequirementService extends BaseService {
	
	public List<OutlineInfo> loadTree(TestRequirementDto dto);
	
	public List<OutlineInfo> loadNormalNode(String taskId,Long currNodeId);
	
	public List addNodes(List<OutlineInfo> list);
	
	public void updateNode(TestRequirementDto dto);
	
	public void switchState(TestRequirementDto dto);
	
	public void delete(OutlineInfo outline);
	
	public List<OutlineTeamMember> getInitAsignPeople(TestRequirementDto dto,List<Long> assignIdList);
	
	public void move(TestRequirementDto dto,List<Map<String, Object>> adjustInfo);
	
	public List<OutlineInfo> loadTreePeople(TestRequirementDto dto);
	
	public void submitMoudle(List<Map<String, Object>> NumInfoList, String taskId);
	
	public List<Long> getAllAssignNode(TestRequirementDto dto);
	
	public List<User> getModuleDevMemb(Long moduleId);

}
