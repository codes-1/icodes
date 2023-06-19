package cn.com.codes.userManager.web;

import static cn.com.codes.framework.common.LogWrap.info;

import java.util.HashMap;

import org.apache.log4j.Logger;

import cn.com.codes.framework.app.blh.BaseBizLogicHandler;
import cn.com.codes.framework.common.ConfigHolder;
import cn.com.codes.framework.common.Global;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;
import cn.com.codes.userManager.blh.UserManagerBlh;
import cn.com.codes.userManager.dto.UserManagerDto;


public class UserManagerAction extends BaseAction<UserManagerBlh> {

	private static Logger logger = Logger.getLogger(UserManagerAction.class);
	private UserManagerDto dto = new UserManagerDto();
	private UserManagerBlh userManagerBlh;
	private String mailBugId ;
	private String viewCode ;

	protected void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException {
		if(dto.getCompany()!=null){
			String companyId = dto.getCompany().getId();
			if(super.getBlhControlFlow().equals("companyMaintence")&&(companyId==null||"".equals(companyId))){
				String realPath = super.getSc().getRealPath(Global.upPath);
				dto.setUserFilesRealPath(realPath);
				info(logger,"ckeditor.basePath=" +realPath);
			}
		}
		if("regisAct".equals(super.getBlhControlFlow())){
			dto.getCompany().setRegisIp(SecurityContextHolder.getContext().getIpAddr());
				ConfigHolder holder = ConfigHolder.getInstance("fckeditor.properties");
				String realPath = super.getSc().getRealPath(holder.getProperty("fckeditor.basePath"));
				dto.setUserFilesRealPath(realPath);
				info(logger,"ckeditor.basePath=" +realPath);
		}
		if(mailBugId!=null){
			
			dto.setAttr("mailBugId", mailBugId);
		}
		reqEvent.setDto(dto);
	}

	protected String _processResponse() throws BaseException {
//		if("login".equals(super.getBlhControlFlow())){
//			HttpServletRequest request = SecurityContextHolder.getContext().getRequest();
//			HttpServletResponse response = SecurityContextHolder.getContext().getResponse();
//			String saveLoginName = request.getParameter("saveLoginName");
//			if("1".equals(saveLoginName)){
//				String host = request.getServerName();   
//				Cookie cookie = new Cookie("myAttachId", dto.getUser().getLoginName());
//				cookie.setMaxAge(365 * 24 * 3600);
//				cookie.setPath("/");
//				cookie.setDomain(host); 
//				response.addCookie(cookie);
//				Cookie pwdCookie = new Cookie("myAttachInfo", Encipher.EncodePasswd(dto.getUser().getPassword()));
//				pwdCookie.setMaxAge(365 * 24 * 3600);
//				pwdCookie.setPath("/");
//				pwdCookie.setDomain(host); 
//				response.addCookie(pwdCookie);
//			}
//		}
		HashMap<?, ?> displayData = (HashMap<?, ?>) _getDisplayData();
		return forwardPage(displayData);
	}

	public UserManagerDto getDto() {
		return dto;
	}

	public void setDto(UserManagerDto dto) {
		this.dto = dto;
	}

	/**
	 * 实现直接跳转，不走ＢＬＨ
	 */
	public String directlyJump() {
		if ("initLogin".equals(BlhControlFlow)) {
			return BlhControlFlow;
		}
		return null;
	}

	public String getViewCode() {
		return viewCode;
	}

	public void setViewCode(String viewCode) {
		this.viewCode = viewCode;
	}

	public  BaseBizLogicHandler getBlh(){
		  
		return userManagerBlh;
	}

	public UserManagerBlh getUserManagerBlh() {
		return userManagerBlh;
	}

	public void setUserManagerBlh(UserManagerBlh userManagerBlh) {
		this.userManagerBlh = userManagerBlh;
	}

	public String getMailBugId() {
		return mailBugId;
	}

	public void setMailBugId(String mailBugId) {
		this.mailBugId = mailBugId;
	}
	

}
