package cn.com.codes.bugManager.service;

import java.util.List;
import java.util.Map;

import cn.com.codes.bugManager.dto.BugManagerDto;
import cn.com.codes.bugManager.dto.BugShortMsgDto;
import cn.com.codes.bugManager.dto.RelaCaseDto;
import cn.com.codes.framework.app.services.BaseService;
import cn.com.codes.object.BugBaseInfo;
import cn.com.codes.object.BugShortMsg;
import cn.com.codes.object.DefaultTypeDefine;

public interface BugManagerService extends BaseService{

	public List<BugBaseInfo> loadBug(BugManagerDto dto);
	
	public void addBug(BugManagerDto dto);
	
	public void updateBug(BugManagerDto dto);
	
	public void handlBug(BugManagerDto dto);
	
	public void upInitPrepare(BugManagerDto dto);
	
	public void upInit(BugManagerDto dto);
	
	public void update(BugManagerDto dto);
	
	public void delete(Long bugId);
	
	public void sendMsg(BugShortMsg shortMsg);
	
	public List<Object[]> findShortMsg(BugShortMsgDto dto);
	
	public List<BugBaseInfo> getRelaBug(RelaCaseDto dto);
	
	public List getRelaCase(RelaCaseDto dto);
	
	public void bugRelaCase(RelaCaseDto dto);
	
	public void caseRelaBug(RelaCaseDto dto);
	
	public String getMdPath(Long moduleId);
	
	public Map<String,DefaultTypeDefine> getRelaTypeDefine(List<BugBaseInfo> list);
	
	//public void reSetMsgFlag(BugShortMsgDto dto);
	
	
	
}
 