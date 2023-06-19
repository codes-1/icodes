package cn.com.codes.singleTestTaskManager.service;

import java.util.List;
import java.util.Map;

import cn.com.codes.framework.app.services.BaseService;
import cn.com.codes.object.SingleTestTask;
import cn.com.codes.singleTestTaskManager.dto.SingleTestTaskDto;

public interface SingleTestTaskService extends BaseService {

	public String addSingleTest(SingleTestTask singleTest);
	
	public String updateSingleTest(SingleTestTask singleTest,boolean updateOutLint);
	
	public void deleteSingleTest(SingleTestTask singleTest);
	
	public SingleTestTask updInit(SingleTestTaskDto dto);
	
	public String[] getTaskBugDateLimit(String taskId);
	
	public String[] getTaskeExeCaseDateLimit(String taskId);
	
	public String[] getTaskeWriteCaseDateLimit(String taskId);

	public List<Map<String, Object>> getAllProName(SingleTestTaskDto dto);
}
