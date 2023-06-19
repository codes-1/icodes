package cn.com.codes.msgManager.blh;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.msgManager.dto.CommonMsgDto;
import cn.com.codes.msgManager.service.CommonMsgService;
import cn.com.codes.object.BroadcastMsg;
import cn.com.codes.object.User;

public class CommonMsgBlh extends BusinessBlh {

	private CommonMsgService mypmCommonMsgService;
	
	public View loadMsg(BusiRequestEvent reqEvent){
		CommonMsgDto dto = super.getDto(CommonMsgDto.class, reqEvent);
		List list = mypmCommonMsgService.loadBroMsg(dto);
		Map<String,User> userMap = mypmCommonMsgService.getRelaUserWithName(list, "senderId");
		for(BroadcastMsg broMsg :(List<BroadcastMsg>)list){
			//这里用senderId 来存json形式用户名
			if(userMap.get(broMsg.getSenderId())==null){
				continue;
			}
			User user = userMap.get(broMsg.getSenderId());
			broMsg.setSenderId(user.getUniqueName());
		}
		userMap = null;
		if(dto.getIsAjax()!=null){
			StringBuffer sb = new StringBuffer();
			dto.toJson(list, sb);
			super.writeResult(sb.toString());
			dto = null;
			return super.globalAjax();
		}
		dto.setJsonData(list);
		return super.getView();
	}
	
	public View loadSendMsg(BusiRequestEvent reqEvent){
		CommonMsgDto dto = super.getDto(CommonMsgDto.class, reqEvent);
		List list = mypmCommonMsgService.loadSendBroMsg(dto);
		String sendName = SecurityContextHolderHelp.getMyRealDisplayName();
		for(BroadcastMsg broMsg :(List<BroadcastMsg>)list){
			//这里用senderId 来存json形式用户名
			broMsg.setSenderId(sendName);
		}
		if(dto.getIsAjax()!=null){
			StringBuffer sb = new StringBuffer();
			dto.toJson(list, sb);
			super.writeResult(sb.toString());
			dto = null;
			return super.globalAjax();
		}
		dto.setJsonData(list);
		return super.getView();
	}
	
	
	public View loadRecevMsg(BusiRequestEvent reqEvent){
		CommonMsgDto dto = super.getDto(CommonMsgDto.class, reqEvent);
		List list = mypmCommonMsgService.loadReceivedBroMsg(dto);
		Map<String,User> userMap = mypmCommonMsgService.getRelaUserWithName(list, "senderId");
		for(BroadcastMsg broMsg :(List<BroadcastMsg>)list){
			//这里用senderId 来存json形式用户名
			if(userMap.get(broMsg.getSenderId())==null){
				continue;
			}
			User user = userMap.get(broMsg.getSenderId());
			broMsg.setSenderId(user.getUniqueName());
		}
		userMap = null;
		if(dto.getIsAjax()!=null){
			StringBuffer sb = new StringBuffer();
			dto.toJson(list, sb);
			super.writeResult(sb.toString());
			dto = null;
			return super.globalAjax();
		}
		dto.setJsonData(list);
		return super.getView();
	}
	
	public View delBroMsg(BusiRequestEvent reqEvent){
		CommonMsgDto dto = super.getDto(CommonMsgDto.class, reqEvent);
		mypmCommonMsgService.delBroMsg(dto.getBroMsg().getLogicId());
		super.writeResult("success");
		dto=null;
		return super.globalAjax();
	}
	
	public View sendBroMsgDel(BusiRequestEvent reqEvent){
		CommonMsgDto dto = super.getDto(CommonMsgDto.class, reqEvent);
		String delHql = "delete from BroadcastMsg where logicId=? and senderId=?";
		mypmCommonMsgService.executeUpdateByHql(delHql,new Object[]{dto.getBroMsg().getLogicId(),SecurityContextHolderHelp.getUserId()});
		super.writeResult("success");
		dto=null;
		return super.globalAjax();
	}
	public View sendMsgUpInit(BusiRequestEvent reqEvent){
		CommonMsgDto dto = super.getDto(CommonMsgDto.class, reqEvent);
		mypmCommonMsgService.upinit(dto);
		dto.getBroMsg().setRecpiUser(dto.getRecpiUser());
		Date now = new Date();
		now.setHours(0);
		now.setMinutes(0);
		now.setSeconds(0);
		if(dto.getBroMsg()==null){
			super.writeResult("failed^记录己被其他人删除");
		}else if(dto.getIsView()==0&&dto.getBroMsg().getStatus(now).equals("己生效")){
			super.writeResult("failed^己生效不许修改");
			return super.globalAjax();
		}else{
			super.writeResult(dto.getBroMsg().toStrUpdateInit());
		}
		return super.globalAjax();
	}
	public View upinit(BusiRequestEvent reqEvent){
		CommonMsgDto dto = super.getDto(CommonMsgDto.class, reqEvent);
		mypmCommonMsgService.upinit(dto);
		dto.getBroMsg().setRecpiUser(dto.getRecpiUser());
		Date now = new Date();
		now.setHours(0);
		now.setMinutes(0);
		now.setSeconds(0);
		if(dto.getBroMsg()==null){
			super.writeResult("failed^记录己被其他人删除");
		}else if(dto.getIsView()==0&&dto.getBroMsg().getStatus(now).equals("己生效")){
			super.writeResult("failed^己生效不许修改");
			return super.globalAjax();
		}else{
			super.writeResult(dto.getBroMsg().toStrUpdateInit());
		}
		return super.globalAjax();
	}

	public View viewDetal(BusiRequestEvent reqEvent){
		CommonMsgDto dto = super.getDto(CommonMsgDto.class, reqEvent);
		mypmCommonMsgService.upinit(dto);
		dto.getBroMsg().setRecpiUser(dto.getRecpiUser());
		Date now = new Date();
		now.setHours(0);
		now.setMinutes(0);
		now.setSeconds(0);
		if(dto.getBroMsg()==null){
			super.writeResult("failed^记录己被其他人删除");
		}else if(dto.getIsView()==0&&dto.getBroMsg().getStatus(now).equals("己生效")){
			super.writeResult("failed^己生效不许修改");
			return super.globalAjax();
		}else{
			super.writeResult(dto.getBroMsg().toStrUpdateInit());
		}
		return super.globalAjax();
	}
	public View publisgMsg(BusiRequestEvent reqEvent){
		CommonMsgDto dto = super.getDto(CommonMsgDto.class, reqEvent);
		BroadcastMsg broMsg = dto.getBroMsg();
		broMsg.setSenderId(SecurityContextHolderHelp.getUserId());
		broMsg.setCompId(SecurityContextHolderHelp.getCompanyId());
		broMsg.setSendDate(new Date());
		if(broMsg.getRecpiUserId()!=null&&!broMsg.getRecpiUserId().equals("")){
			String[] recpiIds = broMsg.getRecpiUserId().split(" ");
			Set<User> recpiSet = new HashSet<User>();
			for(String id :recpiIds){
				User recpiUser = new User();
				recpiUser.setId(id);
				recpiSet.add(recpiUser);
			}
			broMsg.setRecpiUser(recpiSet);
			if(dto.getBroMsg().getMailFlg()!=null&&dto.getBroMsg().getMailFlg()==1){
				dto.setRecipMailAddress(mypmCommonMsgService.getMailAddrByUserIds(broMsg.getRecpiUserId()," "));
			}
		}

		mypmCommonMsgService.publishBroMsg(dto);
		super.writeResult("success^"+dto.getBroMsg().getLogicId().toString()+"^"+dto.getBroMsg().getAttachUrl());
		return super.globalAjax();
	}

	public View sendBroMsg(BusiRequestEvent reqEvent){
		CommonMsgDto dto = super.getDto(CommonMsgDto.class, reqEvent);
		BroadcastMsg broMsg = dto.getBroMsg();
		broMsg.setSenderId(SecurityContextHolderHelp.getUserId());
		broMsg.setCompId(SecurityContextHolderHelp.getCompanyId());
		broMsg.setSendDate(new Date());
		if(broMsg.getRecpiUserId()!=null&&!broMsg.getRecpiUserId().equals("")){
			String[] recpiIds = broMsg.getRecpiUserId().split(" ");
			Set<User> recpiSet = new HashSet<User>();
			for(String id :recpiIds){
				User recpiUser = new User();
				recpiUser.setId(id);
				recpiSet.add(recpiUser);
			}
			broMsg.setRecpiUser(recpiSet);
			if(dto.getBroMsg().getMailFlg()!=null&&dto.getBroMsg().getMailFlg()==1){
				dto.setRecipMailAddress(mypmCommonMsgService.getMailAddrByUserIds(broMsg.getRecpiUserId()," "));
			}
		}

		mypmCommonMsgService.publishBroMsg(dto);
		super.writeResult("success^"+dto.getBroMsg().getLogicId().toString()+"^"+dto.getBroMsg().getAttachUrl());
		return super.globalAjax();
	}
	public CommonMsgService getMypmCommonMsgService() {
		return mypmCommonMsgService;
	}

	public void setMypmCommonMsgService(CommonMsgService mypmCommonMsgService) {
		this.mypmCommonMsgService = mypmCommonMsgService;
	}

}
