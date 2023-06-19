package cn.com.codes.impExpManager.service;

import java.util.List;

import cn.com.codes.bugManager.dto.BugManagerDto;
import cn.com.codes.caseManager.dto.CaseManagerDto;
import cn.com.codes.framework.app.services.BaseService;
import cn.com.codes.object.OutlineInfo;
import cn.com.codes.overview.dto.DataVo;

public interface ImpExpManagerService extends BaseService {

	public void buildBugWhereSql(BugManagerDto dto);
	
	public  String getExpBugSql();
    
	public  String getExpCaseSql();
	
	public void buildCaseWhereSql(CaseManagerDto dto);
	
	public String getBugCountStr(String taskId);
	public String getBugCountStr();
	
	public String getCaseCountStr(String taskId);
	
	public String getCaseCountStr(String taskId,String moduleNum);
	
	public List getOutLineDetailInfo(String taskId,List<OutlineInfo>  loadNodes,boolean onlyNormal);
	
	public List<OutlineInfo> getOutLineInfo(String taskId,List<Long> listId ,boolean onlyNormal);
	
	public String getBugCountStr(String taskId,String moduleNum);
	
	public DataVo getCaseCount(String taskId);
	
	public void getBugCount(String taskId,DataVo dataVo);
}
