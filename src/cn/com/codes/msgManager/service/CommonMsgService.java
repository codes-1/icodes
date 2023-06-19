package cn.com.codes.msgManager.service;

import java.util.List;

import cn.com.codes.framework.app.services.BaseService;
import cn.com.codes.msgManager.dto.CommonMsgDto;

public interface CommonMsgService extends BaseService {

	public List loadBroMsg(CommonMsgDto dto);
	
	public List loadReceivedBroMsg(CommonMsgDto dto);
	
	public void delBroMsg(Long broMsgId);
	
	public void  publishBroMsg(CommonMsgDto dto);
	
	public void  upinit(CommonMsgDto dto);
	
	public List loadSendBroMsg(CommonMsgDto dto);
	
	public Long sendMsg(List<String> recpiIds,String title,String content,boolean andSendMail);
	
	public void sendMsg(List<String> recpiIds,String title,String content,boolean andSendMail,String senderId,String companyId);
	
	public String getMailAddrByUserIds(List<String> recpiIds);
	
	public Long sendMsgCommon(List<String> recpiIds,String title,String content,boolean andSendMail,String linkUrl,String linkTip);
	
}
