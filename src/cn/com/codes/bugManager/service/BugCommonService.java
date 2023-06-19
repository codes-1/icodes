package cn.com.codes.bugManager.service;

import java.util.List;
import java.util.Map;

import cn.com.codes.bugManager.dto.BugManagerDto;
import cn.com.codes.framework.app.services.BaseService;
import cn.com.codes.object.BugBaseInfo;
import cn.com.codes.object.BugHandHistory;
import cn.com.codes.object.BugQueryInfo;
import cn.com.codes.object.TypeDefine;
import cn.com.codes.testTaskManager.dto.CurrTaskInfo;

public interface BugCommonService extends BaseService {

	public List<BugBaseInfo> findBug(BugManagerDto dto);

	public Map<String, TypeDefine> convertTdMap(BugManagerDto dto);

	public void setBugsRelaTypeDefine(Map<String, TypeDefine> convertTdMap,
			List<BugBaseInfo> bugs);

	public void initBugListDate(BugManagerDto dto);

	public List<TypeDefine> getBugListData();

	public void setRelaType(BugBaseInfo bug);

	public void setRelaUser(BugBaseInfo bug);

	public void findInit(BugManagerDto dto);
	
	public BugQueryInfo saveQueryInfo(BugManagerDto dto);
	
	public List<BugBaseInfo> findByQuery(BugManagerDto dto);
	
	public String getQueryJsonStr(Long queryId);
	
	public void setActorListData(CurrTaskInfo currTaskInfo,BugBaseInfo bug);
	
	public String getMdPathName(Long moduleId,String pathName);
	
	public List<BugHandHistory> getBugHistory(Long bugId,int pageNo);
	
	public void updatHandSub(BugManagerDto dto,BugHandHistory bugHistory);
	
	public int getAllMyBugCount();
	
	public List<BugBaseInfo> findAllMyBug(BugManagerDto dto);
	
	public BugBaseInfo qucikQuery(Long bugId);
	
	public List getMyTestTask();
	
	public void sendBugNotice(BugHandHistory history,BugBaseInfo bug,String mailLinkUrl);
	
	public List<BugBaseInfo> findAssignBug(BugManagerDto dto);
	
	public int executeAssignBug(BugManagerDto dto);
	
	public void sendBatchBugAssignNotice(BugManagerDto dto);
	
	public List<Object[]> loadBugBoard(String taskId);
	public List<Map<String,Object>>  loadBugBoardCurrNotInPro(String taskId);
	
	public List loadOwnerBug(String taskId,String loginName);
}
