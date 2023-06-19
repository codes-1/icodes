package cn.com.codes.msgManager.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.framework.app.services.BaseServiceImpl;
import cn.com.codes.framework.jms.mail.MailProducer;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.security.filter.SecurityFilter;
import cn.com.codes.msgManager.dto.CommonMsgDto;
import cn.com.codes.msgManager.dto.MailBean;
import cn.com.codes.msgManager.service.CommonMsgService;
import cn.com.codes.object.BroadcastMsg;
import cn.com.codes.object.User;
import cn.com.codes.msgManager.service.impl.CommonMsgServiceImpl;

public class CommonMsgServiceImpl extends BaseServiceImpl implements
		CommonMsgService {
	
	private static Logger logger = Logger.getLogger(CommonMsgServiceImpl.class);
	MailProducer mailProducer;
	
	public List loadBroMsg(CommonMsgDto dto) {
		this.buildAllBroMsgHql(dto);
		List list = this.findByHqlWithValuesMap(dto.getHql(), dto.getPageNo(), dto.getPageSize(), "logicId", dto.getHqlParamMaps(),false);
		dto.setHql(null);
		dto.setHqlParamMaps(null);
		return list;
	}
	public List loadReceivedBroMsg(CommonMsgDto dto){
		this.buildReceivedHql(dto);
		List list = this.findByHqlWithValuesMap(dto.getHql(), dto.getPageNo(), dto.getPageSize(), "m.logicId", dto.getHqlParamMaps(),false);
		dto.setHql(null);
		dto.setHqlParamMaps(null);
		return list;		
	}
	public List loadSendBroMsg(CommonMsgDto dto){
		this.buildSendBroHql(dto);
		List list = this.findByHqlWithValuesMap(dto.getHql(), dto.getPageNo(), dto.getPageSize(), "logicId", dto.getHqlParamMaps(),false);
		dto.setHql(null);
		dto.setHqlParamMaps(null);
		return list;
	}
	
	private void buildSendBroHql(CommonMsgDto dto){
		StringBuffer hql = new StringBuffer("select new BroadcastMsg(");
		hql.append("logicId,title,senderId,sendDate,msgType,startDate,overdueDate,attachUrl");
		hql.append(") from BroadcastMsg");
		hql.append(" where compId=:compId ");
		Map praValuesMap = new HashMap();
		hql.append(" and senderId in (:senderId)");
		List<String> idList = new ArrayList<String>();
		idList.add(SecurityContextHolderHelp.getUserId());
		praValuesMap.put("senderId", idList);
		BroadcastMsg broMsg = dto.getBroMsg();
		String compId = SecurityContextHolderHelp.getCompanyId();
		praValuesMap.put("compId", compId);
		if(broMsg!=null){
			if(broMsg.getTitle()!=null&&!broMsg.getTitle().trim().equals("")){
				hql.append(" and( title like:title or content like :title )");
				praValuesMap.put("title", "%"+broMsg.getTitle()+"%");
			}
			if(broMsg.getMsgType()!=-1){
				hql.append(" and msgType=:msgType");
				praValuesMap.put("msgType", broMsg.getMsgType());
			}
			if(broMsg.getState()!=-1){
				//1:'未生效',2:'己生效',3:'己过期'
				Date now = new Date();
				now.setHours(0);
				now.setMinutes(0);
				now.setSeconds(0);
				if(broMsg.getState()==1){
					hql.append(" and startDate >:now");				
				}else if(broMsg.getState()==2){
					hql.append(" and overdueDate >=:now and startDate<=:now");					
				}else if(broMsg.getState()==3){
					hql.append(" and overdueDate <:now");				
				}
				praValuesMap.put("now", now);	
			}

		}
		hql.append(" order by sendDate desc ");
		dto.setHql(hql.toString());
		dto.setHqlParamMaps(praValuesMap);
	}	
	private void buildReceivedHql(CommonMsgDto dto){
		StringBuffer hql = new StringBuffer("select distinct new BroadcastMsg(");
		hql.append("m.logicId,m.title,m.senderId,m.sendDate,m.msgType,m.startDate,m.overdueDate,m.attachUrl");
		hql.append(") from BroadcastMsg m left join  m.recpiUser  o where (o.id=:uId or m.msgType=0 ) ");
		hql.append("  and m.compId=:compId ");
		BroadcastMsg broMsg = dto.getBroMsg();
		Map praValuesMap = new HashMap();
		String compId = SecurityContextHolderHelp.getCompanyId();
		praValuesMap.put("compId", compId);
		praValuesMap.put("uId", SecurityContextHolderHelp.getUserId());
		if(broMsg!=null){
			if(broMsg.getTitle()!=null&&!broMsg.getTitle().trim().equals("")){
				hql.append(" and( m.title like:title or m.content like :title )");
				praValuesMap.put("title", "%"+broMsg.getTitle()+"%");
			}
			if(!"".equals(broMsg.getSenderId())){
				hql.append(" and m.senderId in (:senderId)");
				List<String> idList = new ArrayList<String>();
				for(String id:broMsg.getSenderId().split(" ")){
					idList.add(id);
				}
				praValuesMap.put("senderId", idList);
			}
		}else{
			hql.append(" and m.overdueDate >=:now and m.startDate<=:now");	
			Date now = new Date();
			now.setHours(0);
			now.setMinutes(0);
			now.setSeconds(0);
			praValuesMap.put("now", now);	
		}
		hql.append(" order by m.sendDate desc ");
		dto.setHql(hql.toString());
		dto.setHqlParamMaps(praValuesMap);
	}

	private void buildAllBroMsgHql(CommonMsgDto dto){
		StringBuffer hql = new StringBuffer("select new BroadcastMsg(");
		hql.append("logicId,title,senderId,sendDate,msgType,startDate,overdueDate,attachUrl");
		hql.append(") from BroadcastMsg");
		hql.append(" where compId=:compId ");
		BroadcastMsg broMsg = dto.getBroMsg();
		Map praValuesMap = new HashMap();
		String compId = SecurityContextHolderHelp.getCompanyId();
		praValuesMap.put("compId", compId);
		if(broMsg!=null){
			if(broMsg.getTitle()!=null&&!broMsg.getTitle().trim().equals("")){
				hql.append(" and( title like:title or content like :title )");
				praValuesMap.put("title", "%"+broMsg.getTitle()+"%");
			}
			if(broMsg.getMsgType()!=-1){
				hql.append(" and msgType=:msgType");
				praValuesMap.put("msgType", broMsg.getMsgType());
			}
			if(broMsg.getState()!=-1){
				//1:'未生效',2:'己生效',3:'己过期'
				Date now = new Date();
				now.setHours(0);
				now.setMinutes(0);
				now.setSeconds(0);
				if(broMsg.getState()==1){
					hql.append(" and startDate >:now");				
				}else if(broMsg.getState()==2){
					hql.append(" and overdueDate >=:now and startDate<=:now");					
				}else if(broMsg.getState()==3){
					hql.append(" and overdueDate <:now");				
				}
				praValuesMap.put("now", now);	
			}
			if(!"".equals(broMsg.getSenderId())){
				hql.append(" and senderId in (:senderId)");
				List<String> idList = new ArrayList<String>();
				for(String id:broMsg.getSenderId().split(" ")){
					idList.add(id);
				}
				praValuesMap.put("senderId", idList);
			}
		}
		hql.append(" order by sendDate desc ");
		dto.setHql(hql.toString());
		dto.setHqlParamMaps(praValuesMap);
	}
	public void  upinit(CommonMsgDto dto){
		String hql = "from BroadcastMsg where logicId=? and compId=?";
		String compId = SecurityContextHolderHelp.getCompanyId();
		List list = this.findByHql(hql, dto.getBroMsg().getLogicId(),compId);
		if(list==null||list.size()==0){
			dto.setBroMsg(null);
		}else{
			Date now = new Date();
			now.setHours(0);
			now.setMinutes(0);
			now.setSeconds(0);
			BroadcastMsg broMsg = (BroadcastMsg)list.get(0);
			dto.setBroMsg(broMsg);
			if(broMsg.getMsgType()==1){
				StringBuffer recpHql = new StringBuffer("select new  User(o.id,o.name ,o.loginName)");
				recpHql.append(" from BroadcastMsg m  join  m.recpiUser  o where  ");
				recpHql.append("   m.compId=? and m.logicId=? ");
				List recpList = this.findByHql(recpHql.toString(), compId,dto.getBroMsg().getLogicId());
				Set<User> recpiUser = new HashSet<User>(recpList.size());
				recpiUser.addAll(recpList);
				//先放DTO中 出了service再放进去 
				dto.setRecpiUser(recpiUser);
			}
			if(broMsg.getSenderId().equals(SecurityContextHolderHelp.getUserId())){
				broMsg.setSendName(SecurityContextHolderHelp.getMyRealDisplayName());
			}else{
				hql = "select new  User(id,name ,loginName) from User where id=?";
				User sender = (User)this.findByHql(hql, broMsg.getSenderId()).get(0);
				broMsg.setSendName(sender.getUniqueName());
			}
		}
	}
	
	public void delBroMsg(Long broMsgId){
		BroadcastMsg broMsg = new BroadcastMsg();
		broMsg.setLogicId(broMsgId);
		this.delete(broMsg);
	}
	
	public void  publishBroMsg(CommonMsgDto dto){
		dto.getBroMsg().setSendDate(new Date());
		this.saveOrUpdate(dto.getBroMsg());
		if(dto.getRecipMailAddress()!=null){
			MailBean mb = new MailBean();
			mb.setMimeMail(true);
			mb.setRecip(dto.getRecipMailAddress());
			mb.setSubject(dto.getBroMsg().getTitle());
			String text = "<html><body>"+dto.getBroMsg().getContent()+"</body></html>";
			text = text.replaceAll("＜","<");
			text = text.replaceAll("＞",">");
			text = text.replaceAll("”","\"");
			text = text.replaceAll("＇","'");
			text = text.replaceAll("，",",");
			mb.setMsg(text);
			mailProducer.sendMail(mb);
		}
	}
	
	public Long sendMsg(List<String> recpiIds,String title,String content,boolean andSendMail){
		BroadcastMsg broMsg = new BroadcastMsg();
		broMsg.setSenderId(SecurityContextHolderHelp.getUserId());
		broMsg.setCompId(SecurityContextHolderHelp.getCompanyId());
		Set<User> recpiSet = new HashSet<User>();
		for(String id :recpiIds){
			User recpiUser = new User();
			recpiUser.setId(id);
			if(id!=null&&!"".equals(id)&&!recpiSet.contains(recpiUser)){
				recpiSet.add(recpiUser);
			}
		}
		if(recpiSet.isEmpty()){
			logger.error("recpiIds is null no msg to be send ");
			return null;
		}
		broMsg.setRecpiUser(recpiSet);
		Calendar calendar = Calendar.getInstance();
		Date tempDate = new Date();
		calendar.setTime(tempDate);
		calendar.add(Calendar.DATE,-1);
		broMsg.setSendDate(tempDate);
		broMsg.setStartDate(calendar.getTime());
		broMsg.setMsgType(1);
		broMsg.setTitle(title);
		broMsg.setContent(content);
		if(andSendMail){
			broMsg.setMailFlg(1);
		}
		calendar = Calendar.getInstance();
		calendar.setTime(tempDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.DATE,6);
		broMsg.setOverdueDate(calendar.getTime());
		this.add(broMsg);
		if(andSendMail){
			MailBean mb = new MailBean();
			mb.setMimeMail(true);
			mb.setRecip(this.getMailAddrByUserIds(recpiIds));
			System.out.println(mb.getRecip());
			mb.setSubject(title);
			String text = content;	
			text = text.replaceAll("＜","<");
			text = text.replaceAll("＞",">");
			text = text.replaceAll("”","\"");
			text = text.replaceAll("＇","'");
			text = text.replaceAll("，",",");

			mb.setMsg(text);
			mailProducer.sendMail(mb);
		}
		return broMsg.getLogicId();
	}
	
	public Long sendMsgCommon(List<String> recpiIds,String title,String content,boolean andSendMail,String linkUrl,String linkTip){
		BroadcastMsg broMsg = new BroadcastMsg();
		broMsg.setSenderId(SecurityContextHolderHelp.getUserId());
		broMsg.setCompId(SecurityContextHolderHelp.getCompanyId());
		Set<User> recpiSet = new HashSet<User>();
		for(String id :recpiIds){
			User recpiUser = new User();
			recpiUser.setId(id);
			if(id!=null&&!"".equals(id)&&!recpiSet.contains(recpiUser)){
				recpiSet.add(recpiUser);
			}
		}
		if(recpiSet.isEmpty()){
			logger.error("recpiIds is null no msg to be send ");
			return null;
		}
		broMsg.setRecpiUser(recpiSet);
		Calendar calendar = Calendar.getInstance();
		Date tempDate = new Date();
		calendar.setTime(tempDate);
		calendar.add(Calendar.DATE,-1);
		broMsg.setSendDate(tempDate);
		broMsg.setStartDate(calendar.getTime());
		broMsg.setMsgType(1);
		broMsg.setTitle(title);
		broMsg.setContent(content);
		if(andSendMail){
			broMsg.setMailFlg(1);
		}
		calendar = Calendar.getInstance();
		calendar.setTime(tempDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.DATE,6);
		broMsg.setOverdueDate(calendar.getTime());
		this.add(broMsg);
		if(andSendMail){
			MailBean mb = new MailBean();
			mb.setMimeMail(true);
			mb.setRecip(this.getMailAddrByUserIds(recpiIds));
			mb.setSubject(title);
			String text = content;
			if (linkUrl!= null&&!"".endsWith(linkUrl)){
				String url = ((HttpServletRequest)SecurityContextHolder.getContext().getRequest()).getRequestURL().toString();
				url = url.split(SecurityFilter.getAppName())[0] +SecurityFilter.getAppName()+linkUrl;
				if(linkTip!= null&&!"".endsWith(linkTip)){
					text= content +" <br> <a href='"+url+"'>"+linkTip+"</a>";
				}else{
					text= content +" <br> <a href='"+url+"'>在MYPM中查看</a>";
				}
			}else{
				text= content;
			}
			text = text.replaceAll("＜","<");
			text = text.replaceAll("＞",">");
			text = text.replaceAll("”","\"");
			text = text.replaceAll("＇","'");
			text = text.replaceAll("，",",");
			mb.setMsg(text);
			mailProducer.sendMail(mb);
		}
		return broMsg.getLogicId();
	}
	public void sendMsg(List<String> recpiIds,String title,String content,boolean andSendMail,String senderId,String companyId){
		BroadcastMsg broMsg = new BroadcastMsg();
		broMsg.setSenderId(senderId);
		broMsg.setCompId(companyId);
		Set<User> recpiSet = new HashSet<User>();
		for(String id :recpiIds){
			User recpiUser = new User();
			recpiUser.setId(id);
			recpiSet.add(recpiUser);
		}
		broMsg.setRecpiUser(recpiSet);
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE,-1);
		broMsg.setSendDate(new Date());
		broMsg.setStartDate(calendar.getTime());
		broMsg.setMsgType(1);
		broMsg.setTitle(title);
		broMsg.setContent(content);
		if(andSendMail){
			broMsg.setMailFlg(1);
		}
		calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.DATE,6);
		broMsg.setOverdueDate(calendar.getTime());
		this.add(broMsg);
		if(andSendMail){
			MailBean mb = new MailBean();
			mb.setMimeMail(true);
			mb.setRecip(this.getMailAddrByUserIds(recpiIds));
			mb.setSubject(title);
			String text = content;
			text = text.replaceAll("＜","<");
			text = text.replaceAll("＞",">");
			text = text.replaceAll("”","\"");
			text = text.replaceAll("＇","'");
			text = text.replaceAll("，",",");
			mb.setMsg(text);
			mailProducer.sendMail(mb);
		}
	}
	public MailProducer getMailProducer() {
		return mailProducer;
	}
	public void setMailProducer(MailProducer mailProducer) {
		this.mailProducer = mailProducer;
	}
	

}
