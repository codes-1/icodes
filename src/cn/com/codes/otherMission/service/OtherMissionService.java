package cn.com.codes.otherMission.service;

import java.util.List;

import cn.com.codes.framework.app.services.BaseService;
import cn.com.codes.object.Project;
import cn.com.codes.object.User;
import cn.com.codes.otherMission.dto.OtherMissionDto;

public interface OtherMissionService extends BaseService {

	public String addOtherMission(OtherMissionDto otherMissionDto);
	
	public String updateOtherMission(OtherMissionDto otherMissionDto);
	
	public void deleteOtherMission(OtherMissionDto otherMissionDto);
	
	public String[] getUsers(OtherMissionDto otherMissionDto);
	
	public String[] getConcerns(OtherMissionDto otherMissionDto);
	
	public Boolean addProject(OtherMissionDto otherMissionDto);
	
	public List<Project> getProjectLists(OtherMissionDto otherMissionDto);
	
	public List<Project> getProjectLists1(OtherMissionDto otherMissionDto);
	
	public List<Project> getProjectLists2(OtherMissionDto otherMissionDto);
	
	public List<User> getPeopleLists(OtherMissionDto otherMissionDto);
	
	public void updateStatus(OtherMissionDto otherMissionDto);
	
	public String[] getUserNames(OtherMissionDto otherMissionDto);
	
	public String[] getConcernNames(OtherMissionDto otherMissionDto);
	
	public void concernMissions(OtherMissionDto otherMissionDto);
}
