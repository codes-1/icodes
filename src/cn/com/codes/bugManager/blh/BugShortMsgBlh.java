package cn.com.codes.bugManager.blh;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.codes.bugManager.dto.BugShortMsgDto;
import cn.com.codes.bugManager.service.BugManagerService;
import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.common.JsonUtil;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.object.BugShortMsg;

public class BugShortMsgBlh extends BusinessBlh {


	private BugManagerService bugManagerService ;
	
	public View loadMsgList(BusiRequestEvent req){
		BugShortMsgDto dto = super.getDto(BugShortMsgDto.class, req);
		if(dto.getTaskId()!=null&&!"".equals(dto.getTaskId().trim())){
			SecurityContextHolder.getContext().getVisit().setTaskId(dto.getTaskId());	
		}
		List<Object[]> msgList = bugManagerService.findShortMsg(dto);
		if(msgList.size()>0){
			for(Object[] objs :msgList){
				if(objs[3].toString().equals("1")){
					objs[3]="测试人员";
				}else if(objs[3].toString().equals("3")){
					objs[3]="分析人员";
				}else if(objs[3].toString().equals("4")){
					objs[3]="分配人员";
				}else if(objs[3].toString().equals("5")){
					objs[3]="开发人员";
				}else if(objs[3].toString().equals("7")){
					objs[3]="开发负责人";
				}else if(objs[3].toString().equals("10")){
					objs[3]="项目所有成员";
				}
			}			
		}
		if(dto.getReSetMsgLink()==1){
			String upHql = "update BugBaseInfo set msgFlag=9 where bugId=? and msgFlag in(1,3,4,5,7) ";
			bugManagerService.getHibernateGenericController().executeUpdate(upHql, dto.getShortMsg().getBugId());
		}
		Map<String, Object> map = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if("true".equals(dto.getIsAjax())){
			for (int i = 0; i < msgList.size(); i++) {
				Object[] objects = msgList.get(i);
				map = new HashMap<String, Object>();
//				for (int j = 0; j < objects.length; j++) {
					map.put("msgId", objects[0]);
					map.put("pName", objects[1]);
					map.put("message", objects[2]);
					map.put("recipCd", objects[3]);
					map.put("insDate", objects[4]);
//				}
				list.add(map);
			}
			super.writeResult(JsonUtil.toJson(list));
//			super.writeResult(JsonUtil.toJson(dto.ObjArrList2Json(msgList, null, 0)));
			dto = null;
			msgList = null;
			return super.globalAjax();
		}
		dto.setListStr(dto.ObjArrList2Json(msgList, null, 0));
		return super.getView();
	}

	public View sendMsg(BusiRequestEvent req){
		BugShortMsgDto dto = super.getDto(BugShortMsgDto.class, req);
		BugShortMsg shortMsg = dto.getShortMsg();
		shortMsg.setInsDate(new Date());
		shortMsg.setSenderId(SecurityContextHolderHelp.getUserId());
		shortMsg.setTaskId(SecurityContextHolderHelp.getCurrTaksId());
		bugManagerService.sendMsg(shortMsg);
		super.writeResult(shortMsg.getMsgId().toString()+"^"+StringUtils.formatMiddleDate(new Date()));
		dto = null;
		super.writeResult("success");
		return super.globalAjax();
	}


	public BugManagerService getBugManagerService() {
		return bugManagerService;
	}

	public void setBugManagerService(BugManagerService bugManagerService) {
		this.bugManagerService = bugManagerService;
	}


	
}
