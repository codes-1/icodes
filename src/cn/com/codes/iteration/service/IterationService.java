package cn.com.codes.iteration.service;

import java.util.ArrayList;
import java.util.List;

import cn.com.codes.framework.app.services.BaseService;
import cn.com.codes.iteration.dto.IterationDto;
import cn.com.codes.iteration.dto.IterationVo;
import cn.com.codes.object.BugBaseInfo;
import cn.com.codes.object.IterationBugReal;
import cn.com.codes.object.IterationList;
import cn.com.codes.object.IterationTaskReal;
import cn.com.codes.object.IterationTestcasepackageReal;
import cn.com.codes.object.OtherMission;
import cn.com.codes.object.TestCasePackage;
import cn.com.codes.object.UserTestCasePkg;


public interface IterationService extends BaseService {

	public void addIterationData(IterationDto dto);
	
	public void updateIterationData(IterationDto dto);

	public void getBugTaskTestcaseInfo(IterationDto dto);

	public void deleteIterationAboutInfo(IterationDto dto);

	public void bugDetail(IterationDto dto);

	public void batchSaveTestCasePackage(
			List<IterationTestcasepackageReal> iterationTestcaseList);
	
	public void TestCaseDetail(IterationDto dto);

	public void batchSaveIteraTask(
			List<IterationTaskReal> iterationTaskRealList);

	public void TaskDetail(IterationDto dto);

	public void batchSaveIteraBug(List<IterationBugReal> iterationBugReals);

	public List<IterationVo> differentPersonWatchIterationDataInfo(
			IterationDto dto);
	
	public List<IterationVo> differentPersonWatchIterationDataInfoWithParams(
			IterationDto dto);

	public List<UserTestCasePkg> searchTestCasePackageInfo(String packageId);

}
