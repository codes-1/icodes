package cn.com.codes.caseManager.service;

import java.util.List;

import cn.com.codes.caseManager.dto.CaseManagerDto;
import cn.com.codes.caseManager.dto.TestCaseVo;
import cn.com.codes.framework.app.services.BaseService;
import cn.com.codes.object.CaseExeHistory;
import cn.com.codes.object.TestCaseInfo;


public interface CaseManagerService extends BaseService {

	public List loadCase(CaseManagerDto dto,String moduleNum);
	
	public void exeCase(CaseManagerDto dto);
	
	public void addOrUpCase(CaseManagerDto dto);
	
	public boolean isCanDel(Long caseId);
	
	public void delCase(Long caseId);
	
	public List<TestCaseInfo> loadCaseRest(CaseManagerDto dto);
	
	public List<TestCaseVo> loadExeRecord(CaseManagerDto dto);
	
	public int getLastExeCaseCount();
	
	public List<TestCaseVo> loadLastExeCase(CaseManagerDto dto);
	
	public TestCaseVo quickQueryLastExeCase(Long CaseId);
	
	public TestCaseInfo quickQueryCase(Long CaseId,Long moduleId,String taskId);
	
	public List loadAuditCase(CaseManagerDto dto,String moduleNum);
	
	public void batchAudit(CaseManagerDto dto);
	
	public List<CaseExeHistory> loadHistory(CaseManagerDto dto);
	
	public List loadCaseBoard(String taskId);
	
	public void copyCase(CaseManagerDto dto);
	
	public void pasteCase(CaseManagerDto dto);
	
	public void saveImpotTestCase(List<TestCaseInfo> casrList);
}
