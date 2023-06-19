package cn.com.codes.object;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.transmission.JsonInterface;
import cn.com.codes.object.User;

public class BroadcastMsg implements  JsonInterface {

	private Long logicId;
	private String compId;
	private Date overdueDate;
	private String content;
	private String title;
	private Integer state;
	private Date sendDate;
	private String senderId;
	private Integer msgType;
	private Integer mailFlg;
	private String attachUrl;
	private Date startDate;
	private String sendName;
	Set<User> recpiUser;
    private String recpiUserId;
   
	public BroadcastMsg() {
	}
	public BroadcastMsg(String title) {
		this.title = title;
	}
	public BroadcastMsg(Long logicId,String title,Date sendDate,
			Integer msgType,String attachUrl) {
		this.logicId = logicId;
		this.title = title;
		this.sendDate = sendDate;
		this.msgType = msgType;
		this.attachUrl = attachUrl;
	}
	
	public BroadcastMsg(Long logicId,String title,Date sendDate,
			Integer msgType,String attachUrl,String senderId) {
		this.logicId = logicId;
		this.title = title;
		this.sendDate = sendDate;
		this.msgType = msgType;
		this.attachUrl = attachUrl;
		this.senderId = senderId;
	}
	public BroadcastMsg(Long logicId,String title,String senderId,Date sendDate,
			Integer msgType,Date startDate,Date overdueDate,String attachUrl) {
		this.logicId = logicId;
		this.title = title;
		this.senderId= senderId;
		this.sendDate = sendDate;
		this.msgType = msgType;
		this.startDate = startDate;
		this.overdueDate = overdueDate;
		this.attachUrl = attachUrl;
	}
	public Long getLogicId() {
		return this.logicId;
	}

	public String getSendName() {
		return sendName;
	}

	public void setSendName(String sendName) {
		this.sendName = sendName;
	}
	public void setLogicId(Long logicId) {
		this.logicId = logicId;
	}

	public String getCompId() {
		return this.compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	public Date getOverdueDate() {
		return this.overdueDate;
	}

	public void setOverdueDate(Date overdueDate) {
		this.overdueDate = overdueDate;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getSendDate() {
		return this.sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getSenderId() {
		return this.senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}


	public String getAttachUrl() {
		return this.attachUrl;
	}

	public void setAttachUrl(String attachUrl) {
		this.attachUrl = attachUrl;
	}

	public Integer getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}



	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public Set<User> getRecpiUser() {
		return recpiUser;
	}

	public void setRecpiUser(Set<User> recpiUser) {
		this.recpiUser = recpiUser;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public String getRecpiUserId() {
		return recpiUserId;
	}

	public void setRecpiUserId(String recpiUserId) {
		this.recpiUserId = recpiUserId;
	}		
	public String toStrList() {
		Date now = new Date();
		now.setHours(0);
		now.setMinutes(0);
		now.setSeconds(0);
		StringBuffer sbf = new StringBuffer();
		sbf.append("{");
		sbf.append("id:'");
		sbf.append(getLogicId().toString());
		sbf.append("',data: [0,'','");
		sbf.append(title);
		sbf.append("','");
		sbf.append(msgType==0?"广播":"限定接收人");
		sbf.append("','");
		sbf.append(getStatus(now));
		sbf.append("','");
		sbf.append( senderId);
		sbf.append("','");
		sbf.append(StringUtils.formatShortDate(sendDate));
		sbf.append("','");
		sbf.append(StringUtils.formatShortDate(startDate));
		sbf.append("','");
		sbf.append(StringUtils.formatShortDate(overdueDate));
		sbf.append("','");
		sbf.append(attachUrl==null?"":attachUrl);
		sbf.append("'");
		sbf.append("]");
		sbf.append("}");
		now = null;
		return sbf.toString();
	}
	public String getStatus(Date now){
		if(now.compareTo(this.startDate)<0){
			return "未生效";
		}else if(now.compareTo(this.overdueDate)>0){
			return "己失效";	
		}else if(this.overdueDate.compareTo(now)>=0&&now.compareTo(this.startDate)>=0){
			return "己生效";
		}
		return "";
	}
	public String toStrUpdateInit() {
		StringBuffer sbf = new StringBuffer();
		sbf.append("logicId=").append(getLogicId().toString());
		sbf.append("^");
		sbf.append("overdueDate=").append(StringUtils.formatShortDate(overdueDate));
		sbf.append("^");
		sbf.append("content=").append(content == null ? "" : content);
		sbf.append("^");
		sbf.append("recpiUserName=").append(getecpiName());
		sbf.append("^");
		sbf.append("recpiUserId=").append(recpiUserId == null ? "" : recpiUserId);
		sbf.append("^");
		sbf.append("sendDate=").append(StringUtils.formatShortDate(sendDate));
		sbf.append("^");
		sbf.append("senderId=").append(senderId == null ? "" : senderId);
		sbf.append("^");
		sbf.append("msgType=").append(msgType == null ? "" : msgType);
		sbf.append("^");
		sbf.append("mailFlg=").append(mailFlg == null ? false : true);
		sbf.append("^");
		sbf.append("attachUrl=").append(attachUrl == null ? "" : attachUrl);
		sbf.append("^");
		sbf.append("startDate=").append(StringUtils.formatShortDate(startDate));
		sbf.append("^");
		sbf.append("msgTitle=").append(title);
		sbf.append("^");
		sbf.append("sendName=").append(sendName);		
		
		return sbf.toString();
	}
	
	private String getecpiName(){
		if(recpiUser==null||recpiUser.size()==0){
			return "";
		}
		Iterator it =recpiUser.iterator();
		StringBuffer sb = new StringBuffer();
		StringBuffer idSb = new StringBuffer();
		while(it.hasNext()){
			User usr = (User)it.next();
			sb.append(",").append(usr.getUniqueName());
			idSb.append(usr.getId()).append(" ");
		}
		recpiUserId = idSb.toString().trim();
		return sb.toString().substring(1).trim();
	}
	public String toStrUpdateRest(){
        StringBuffer sbf = new StringBuffer();
        sbf.append(getLogicId().toString());
        sbf.append("^");
        sbf.append("0,,");
        sbf.append(title == null ? "" : title );
        sbf.append(",");
        sbf.append(msgType == null ? "" : msgType );
        sbf.append(",");
        sbf.append(state == null ? "" : state );
        sbf.append(",");
        sbf.append(senderId == null ? "" : senderId );
        sbf.append(",");
        sbf.append(sendDate == null ? "" : StringUtils.formatShortDate(sendDate ));
        sbf.append(",");
        sbf.append(startDate == null ? "" : StringUtils.formatShortDate(startDate ));
        sbf.append(",");
        sbf.append(overdueDate == null ? "" : StringUtils.formatShortDate(overdueDate ));
        return sbf.toString();
	}

	/**
	 * 欢迎页面json化用这方法
	 */
	public void toString(StringBuffer sbf) {

		sbf.append("{");
		sbf.append("id:'");
		sbf.append(getLogicId().toString());
		sbf.append("',data: [0,'','");
		sbf.append(title);
		sbf.append("','");
		sbf.append(msgType==0?"广播":"限定接收人");
		sbf.append("','");
		sbf.append(this.senderId);
		sbf.append("','");
		sbf.append(StringUtils.formatShortDate(sendDate));
		sbf.append("','");
		sbf.append(attachUrl==null?"":attachUrl);
		sbf.append("'");
		sbf.append("]");
		sbf.append("}");
	}

	public Integer getMailFlg() {
		return mailFlg;
	}

	public void setMailFlg(Integer mailFlg) {
		this.mailFlg = mailFlg;
	}


}