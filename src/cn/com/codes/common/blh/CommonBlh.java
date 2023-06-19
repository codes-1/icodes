package cn.com.codes.common.blh;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.dto.CommonDto;
import cn.com.codes.common.dto.FeedBack;
import cn.com.codes.common.util.InitDatabaseUtil;
import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.services.BaseService;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.common.config.PropertiesBean;
import cn.com.codes.framework.common.util.Context;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.jms.log.LogProducer;
import cn.com.codes.framework.jms.mail.MailProducer;
import cn.com.codes.framework.security.SecurityPrivilege;
import cn.com.codes.framework.security.SysLog;
import cn.com.codes.framework.security.filter.SecurityContext;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.licenseMgr.blh.LicesneMgrBlh;
import cn.com.codes.msgManager.dto.MailBean;
import cn.com.codes.object.BroadcastMsg;
import cn.com.codes.object.BugBaseInfo;
import cn.com.codes.object.OperaLog;
import cn.com.codes.object.SoftwareVersion;
import cn.com.codes.object.User;

public class CommonBlh extends BusinessBlh {

	private BaseService myPmbaseService;
	private SecurityPrivilege securityPrivilege ;
	private LogProducer logMessageProducer;
	MailProducer mailProducer;
	
	public View activeConn(BusiRequestEvent req){
		//System.out.println("==============activeConn");
		return super.globalAjax();
	}
	public View sendFdBack(BusiRequestEvent req){
		CommonDto dto = super.getDto(CommonDto.class, req);
		FeedBack fdBack = dto.getFeedBack();
		MailBean mb = new MailBean();
		mb.setMimeMail(false);
		PropertiesBean conf = (PropertiesBean) Context.getInstance().getBean("ContextProperties");
		mb.setRecip(conf.getProperty("contactMail"));
		mb.setSubject(fdBack.getCustomName() +" MYPM反馈 "+fdBack.getFdDesc());
		mb.setMsg(fdBack.getFdReProStep());
		mailProducer.sendMail(mb);
		super.writeResult("success");
		return super.globalAjax();
	}

	public View reptLicenseMgr(BusiRequestEvent req){
		LicesneMgrBlh licenseMgr =LicesneMgrBlh.getInstance();
		CommonDto dto = super.getDto(CommonDto.class, req);
		if(dto==null){
			return super.getView("handFdBack");
		}
		String operCmd = dto.getOperCmd();
		if(dto.getDto()!=null&&!"reptLicenseApp".equals(operCmd)){
			if("sendReptLicenseInfo".equals(operCmd)){
				return licenseMgr.sendReptLicenseInfo(req);
			}else if("regReptLicense".equals(operCmd)){
				return licenseMgr.regLicense(req);
			}	
		}
		if("reptLicenseApp".equals(operCmd)){
			return licenseMgr.reptLicenseApp(req);
		}

		//随便返回一个迷糊人的VIEW
		return super.getView("handFdBack");
		
	}
	public View userLicenseMgr(BusiRequestEvent req){
		LicesneMgrBlh licenseMgr =LicesneMgrBlh.getInstance();
		CommonDto dto = super.getDto(CommonDto.class, req);
		if(dto==null){
			return super.getView("handFdBack");
		}
		String operCmd = dto.getOperCmd();
		if(!"userLicenseApp".equals(operCmd)){
			if("sendUserLicenseInfo".equals(operCmd)){
				return licenseMgr.sendUserLicenseInfo(req);
			}else if("regUserLicense".equals(operCmd)){
				return licenseMgr.regLicense(req);
			}
		}
		if("userLicenseApp".equals(operCmd)){
			return licenseMgr.userLicenseApp(req);
		}

		//随便返回一个迷糊人的VIEW
		return super.getView("handFdBack");
		
	}
	public View getMyHome(BusiRequestEvent req){
		String hql = "select myHome from User where id=?";
		List list = myPmbaseService.findByHql(hql, SecurityContextHolderHelp.getUserId());
		String homeUrl = "/project/projectAction!listProjects.action";
		if(list!=null&&!list.isEmpty()){
			try {
				homeUrl = list.get(0).toString();
			} catch (NullPointerException e) {
			}
		}
		if(homeUrl.length()<=3){
			SecurityContext sec = SecurityContextHolder.getContext();
			sec.setSessionAttr("myHome",homeUrl);
		}
		super.writeResult("success^"+homeUrl);
		return super.globalAjax();
		
	}
	public View regisInfo(BusiRequestEvent req){
		CommonDto dto = super.getDto(CommonDto.class, req);
		String mypmUserInfo= dto.getOperCmd();
		String[] infoArr = mypmUserInfo.split("_");
		if(infoArr[infoArr.length-1].length()>=13){
			//System.out.println(mypmUserInfo);
			SysLog log = new OperaLog();
			log.setLogType(0);
			log.setOperDesc(mypmUserInfo);
			log.setOperSummary("访问控制");
			log.setOperDate(new Date());
			log.setAccessIp(SecurityContextHolder.getContext().getRequest().getRemoteAddr());
			logMessageProducer.log(log);
			super.writeResult("success");
		}
		return super.globalAjax();
	}
	public View initDatabases(BusiRequestEvent req)throws BaseException{
		InitDatabaseUtil mypmIt = new InitDatabaseUtil();
		CommonDto dto = super.getDto(CommonDto.class, req);
		PropertiesBean conf = (PropertiesBean) Context.getInstance()
		.getBean("ContextProperties");
		//System.out.println("haveUpgrade=="+conf.getProperty("haveUpgrade"));
		if (conf.getProperty("haveUpgrade") != null) {
			super.writeResult("duplicateUpgrade");
		}else{
			Integer exeRest = 0;
			//if("".equals(dto.getOperCmd()!=null)){
				exeRest = mypmIt.exeUpgrade(true);
			//}else{
			//	exeRest = mypmIt.exeUpgrade(false);
			//}
			super.writeResult(exeRest.toString());
		}

		return super.globalAjax();	
	}
	public View loadMyHomeMsg(BusiRequestEvent req){
		CommonDto dto = super.getDto(CommonDto.class, req);
		this.buildMsgHql(dto);
		List list = myPmbaseService.findByHqlWithValuesMap(dto.getHql(), 1, dto.getPageSize(), "m.logicId", dto.getHqlParamMaps(),false);
		Map<String,User> userMap = myPmbaseService.getRelaUserWithName(list, "senderId");
		for(BroadcastMsg broMsg :(List<BroadcastMsg>)list){
			//这里用senderId 来存json形式用户名
			if(userMap.get(broMsg.getSenderId())==null){
				continue;
			}
			User user = userMap.get(broMsg.getSenderId());
			broMsg.setSenderId(user.getUniqueName());
		}		
		StringBuffer sb = new StringBuffer();
		dto.toJson2(list, sb);
		super.writeResult(sb.toString());
		dto.setHqlParamMaps(null);
		dto = null;
		return super.globalAjax();	
	}
	
	public View loadMyHomeBug(BusiRequestEvent req){
		CommonDto dto = super.getDto(CommonDto.class, req);
		this.buildBugHql(dto);
		List<BugBaseInfo> list = myPmbaseService.findByHqlWithValuesMap(dto.getHql(), 1,dto.getPageSize(), "bugId", dto.getHqlParamMaps(),false);
		Map<String, User> testerMap = myPmbaseService.getRelaUserWithName(list, "testOwnerId");
		Map<String, SoftwareVersion> verMap = myPmbaseService.getRelaVers(list, "bugReptVer");
		if((testerMap!=null&&!testerMap.isEmpty())||(verMap!=null&&!verMap.isEmpty())){
			 for(BugBaseInfo bug :list){
				 if(testerMap!=null&&!testerMap.isEmpty()){
					 User tester = testerMap.get(bug.getTestOwnerId());
					 if(tester!=null){
						 bug.settestName(tester.getName());
					 }					 
				 }
				 if(verMap!=null&&!verMap.isEmpty()){
					 SoftwareVersion sv = verMap.get(bug.getBugReptVer().toString());
					 if(sv!=null){
						 bug.setReptVersion(sv);
					 }
				 }

			 }
		}
		testerMap = null;
		verMap = null;
		List bugList = list;
		StringBuffer sbf = new StringBuffer();
		this.myHomeBug2Json(list, sbf);
		super.writeResult(sbf.toString());
		dto.setHqlParamMaps(null);
		dto = null;
		return super.globalAjax();	
	}
	public void myHomeBug2Json(List<BugBaseInfo> list,StringBuffer sbf){
		int i =0 ;
		if(list != null && list.size()>0){
			sbf.append("{rows: [");
			for(BugBaseInfo bug:list){
				i++ ;
				if(i != list.size()){
					bug.toMyHomeString(sbf);
					sbf.append(",");
				}else{
					bug.toMyHomeString(sbf);
				}
			}
			sbf.append("]}");				
		}		
	    Object pageInfo = SecurityContextHolder.getContext().getAttr("pageInfo");
	    if(pageInfo != null){
	    	 sbf.insert(0, pageInfo.toString()).toString();
	    }
	}

	private void buildBugHql(CommonDto dto){
		StringBuffer hql = new StringBuffer("select new BugBaseInfo");
		hql.append("(bugId,bugDesc,testOwnerId,currStateId,taskId,reptDate, bugReptVer,currHandlDate,attachUrl) ");
		hql.append("from BugBaseInfo b where  ");
		hql.append("(b.currHandlerId=:ownerId and b.currStateId not in(4,5,14,15,22,23)) or b.nextOwnerId=:ownerId");
		hql.append("   order by b.currHandlDate desc ") ;
		dto.setHql(hql.toString());
		Map praValuesMap = new HashMap();
		praValuesMap.put("ownerId", SecurityContextHolderHelp.getUserId());
		dto.setHqlParamMaps(praValuesMap);
	}
	private void buildMsgHql(CommonDto dto){
		StringBuffer hql = new StringBuffer("select distinct new BroadcastMsg(");
		hql.append("m.logicId,m.title,m.sendDate,m.msgType,m.attachUrl,m.senderId");
		hql.append(") from BroadcastMsg m left join  m.recpiUser  o where (o.id=:uId or m.msgType=0 ) ");
		hql.append("  and m.compId=:compId ");
		Map praValuesMap = new HashMap();
		String compId = SecurityContextHolderHelp.getCompanyId();
		praValuesMap.put("compId", compId);
		praValuesMap.put("uId", SecurityContextHolderHelp.getUserId());
		hql.append(" and m.overdueDate >=:now and m.startDate<=:now");	
		Date now = new Date();
		now.setHours(0);
		now.setMinutes(0);
		now.setSeconds(0);
		praValuesMap.put("now", now);	
		hql.append(" order by m.sendDate desc ");
		dto.setHql(hql.toString());
		dto.setHqlParamMaps(praValuesMap);
	}
	public View setTaskAsHome(BusiRequestEvent req){
		CommonDto dto = super.getDto(CommonDto.class, req);
		String projectId = dto.getProjectId();
		String hql = "update User set myHome=? where id=?";
		String myHomeUrl = "/task/taskAction!taskLists.action?taskDto.project.id="+projectId;
		myPmbaseService.executeUpdateByHql(hql, new Object[]{myHomeUrl,SecurityContextHolderHelp.getUserId()});
		super.writeResult("success");
		return super.globalAjax();
	}
	public View customMyHome(BusiRequestEvent req){
		CommonDto dto = super.getDto(CommonDto.class, req);
		String hql = "update User set myHome=? where id=?";
		String homeUrl = dto.getMyHomeUrl()==null||"".equals(dto.getMyHomeUrl())?"/bugManager/bugManagerAction!loadAllMyBug.action":dto.getMyHomeUrl();
		myPmbaseService.executeUpdateByHql(hql, new Object[]{homeUrl,SecurityContextHolderHelp.getUserId()});
		super.writeResult("success");
		return super.globalAjax();
	}	
	
	public View setProAsHome(BusiRequestEvent req){
		String hql = "update User set myHome=? where id=?";
		String myHomeUrl = "/project/projectAction!listProjects.action";
		myPmbaseService.executeUpdateByHql(hql, new Object[]{myHomeUrl,SecurityContextHolderHelp.getUserId()});
		super.writeResult("success");
		return super.globalAjax();
	}
	
	public View setCaseAsHome(BusiRequestEvent req){
		
		String hql = "update User set myHome=? where id=?";
		String myHomeUrl = "/caseManager/caseManagerAction!loadCase.action?dto.taskId="+SecurityContextHolderHelp.getCurrTaksId();
		myPmbaseService.executeUpdateByHql(hql, new Object[]{myHomeUrl,SecurityContextHolderHelp.getUserId()});
		super.writeResult("success^");
		return super.globalAjax();
	}
	
	public View setBugAsHome(BusiRequestEvent req){
		
		String hql = "update User set myHome=? where id=?";
		String myHomeUrl = "/bugManager/bugManagerAction!loadMyBug.action?dto.taskId="+SecurityContextHolderHelp.getCurrTaksId();
		myPmbaseService.executeUpdateByHql(hql, new Object[]{myHomeUrl,SecurityContextHolderHelp.getUserId()});
		super.writeResult("success");
		return super.globalAjax();
	}
	public View setAllMyBugAsHome(BusiRequestEvent req){
		
		String hql = "update User set myHome=? where id=?";
		String myHomeUrl = "/bugManager/bugManagerAction!loadAllMyBug.action?dto.allTestTask=true";
		myPmbaseService.executeUpdateByHql(hql, new Object[]{myHomeUrl,SecurityContextHolderHelp.getUserId()});
		super.writeResult("success");
		return super.globalAjax();
	}
	public View reNameChk(BusiRequestEvent req){
		CommonDto dto = super.getDto(CommonDto.class, req);
		boolean chkResult = false;
		if("User".equals(dto.getObjName())){
			chkResult = this.userReNameChk(dto.getObjName(), dto.getNameVal(),dto.getNamePropName() ,dto.getIdPropName(), dto.getIdPropVal());
		}else{
			chkResult = myPmbaseService.reNameChk(dto.getObjName(), dto.getNameVal(),dto.getNamePropName() ,dto.getIdPropName(), dto.getIdPropVal());
		}
		if(chkResult)
			super.writeResult("true");
		else
			super.writeResult("false");
		return super.getView("ajaxRest");
	}
	/**
	 * 因为用户检查时,要加用户不被删的标记,所以这里单独来实现
	 * @param objName
	 * @param nameVal
	 * @param namePropName
	 * @param idPropName
	 * @param idPropVal
	 * @return
	 */
	public boolean userReNameChk(String objName,final String nameVal,final String namePropName,String idPropName,final String idPropVal){
		StringBuffer hql = new StringBuffer();
		hql.append("select count(").append(idPropName==null?"*":idPropName).append(")");
		hql.append(" from ").append(objName);
		Map praValuesMap = new HashMap(2);
		hql.append(" where  delFlag=0 and ").append(namePropName).append("=:namePropName");
		praValuesMap.put("namePropName", nameVal);
		if(idPropName!=null&&!"".equals(idPropName.trim())&&idPropVal!=null&&!"".equals(idPropVal.trim())){
			hql.append(" and  ").append(idPropName).append("!=:idPropName");
			praValuesMap.put("idPropName", idPropVal);
		}
		List countlist = myPmbaseService.findByHqlWithValuesMap(hql.toString(), praValuesMap, false);
		int count = ((Long)countlist.get(0)).intValue();
		return count>0?true:false;
	}
	public View loginPass(BusiRequestEvent req){
		return super.getView("loginPass");
		
	}
	//注册重名检查，不直接调reNameChk是为了权限控制
	public View regisChk(BusiRequestEvent req){
		CommonDto dto = super.getDto(CommonDto.class, req);
		dto.setIdPropName("id");
		dto.setNamePropName("loginName");
		return this.reNameChk(req);
	}
	//注册Mail重名检查，不直接调reNameChk是为了权限控制
	public View reMailChk(BusiRequestEvent req){
		return this.reNameChk(req);
	}
	public BaseService getMyPmbaseService() {
		return myPmbaseService;
	}

	public void setMyPmbaseService(BaseService myPmbaseService) {
		this.myPmbaseService = myPmbaseService;
	}

	public SecurityPrivilege getSecurityPrivilege() {
		return securityPrivilege;
	}

	public void setSecurityPrivilege(SecurityPrivilege securityPrivilege) {
		this.securityPrivilege = securityPrivilege;
	}
	public LogProducer getLogMessageProducer() {
		return logMessageProducer;
	}
	public void setLogMessageProducer(LogProducer logMessageProducer) {
		this.logMessageProducer = logMessageProducer;
	}

	public MailProducer getMailProducer() {
		return mailProducer;
	}

	public void setMailProducer(MailProducer mailProducer) {
		this.mailProducer = mailProducer;
	}
	
}
