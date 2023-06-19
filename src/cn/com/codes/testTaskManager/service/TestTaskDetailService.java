package cn.com.codes.testTaskManager.service;

import java.util.List;
import java.util.Map;

import cn.com.codes.framework.app.services.BaseService;
import cn.com.codes.framework.common.ListObject;
import cn.com.codes.object.SingleTestTask;
import cn.com.codes.object.SoftwareVersion;
import cn.com.codes.object.TestFlowInfo;
import cn.com.codes.object.User;
import cn.com.codes.testTaskManager.dto.CurrTaskInfo;
import cn.com.codes.testTaskManager.dto.TestTaskManagerDto;

public interface TestTaskDetailService extends BaseService{
	
	public CurrTaskInfo getCurrTaskInfo();
	
	public int getNextFlowCd(int currFlowCd);
	
	public List<TestFlowInfo> getCurrTestFlow();
	
	public CurrTaskInfo getCurrTaskDetalInfo();
	
	public void update(TestTaskManagerDto dto);
	
	public void updateInit(TestTaskManagerDto dto);
	
	public String createVerCheck(TestTaskManagerDto dto);
	
	public String getCurrVer(String taskId,Long moduleId);
	
	public List<User> getTeamMembByActCd(Integer actorCd);
	
	public Map getCurrTestSeqAndVer(String taskId,Long moduleId);
	
	public void saveTestTaskDet(String taskId,Integer testPhase,String projectId);
	
	public List<SoftwareVersion> getSoftVerList(TestTaskManagerDto dto);
	
	public String softVerRepChk(SoftwareVersion softVer);
	
	public List<ListObject> getVerSelList();
	
	public SingleTestTask getProNameAndPmName(String taskId);

	public List<SoftwareVersion> getSoftVerListLoad(TestTaskManagerDto dto);
}
