package org.eclipse.birt.report.listener.data.oda;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.birt.report.listener.MonitorObj;

import cn.com.codes.framework.common.util.Context;
import cn.com.codes.framework.common.util.MypmBean;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.hibernate.HibernateGenericController;
import cn.com.codes.framework.security.filter.SecurityContext;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.transmission.events.RequestEvent;
import cn.com.codes.licenseMgr.blh.SysInfo;
import cn.com.codes.userManager.blh.PasswordTool;

public class ViewSceneImpl implements ViewScene {

	private static long serialVersionUID = 10052L;
	//private static long serialVersionUID = 10003L;
	private static Logger logger = Logger.getLogger(ViewSceneImpl.class);
	public HibernateGenericController hibernateGenericController;
	private static boolean loadCount = false;
	private void hintMsg() {
		SecurityContext sc = SecurityContextHolder.getContext();
		HttpServletResponse response = sc.getResponse();
		HttpServletRequest request = sc.getRequest();
		request.getSession().removeAttribute("currentUser");
		request.getSession().removeAttribute("logined");
		request.getSession().removeAttribute("validCode");
		try {
			PrintWriter writer = response.getWriter();
			writer.write("1");
			writer.flush();
		} catch (IOException e) {
		}
	}

	public boolean recoverUserInfo(RequestEvent req) throws BaseException {
		MonitorObj monitorObj = MonitorObj.getInstance();
		if(!loadCount){
			try {
				//PropertiesBean conf = (PropertiesBean) Context.getInstance().getBean("ContextProperties");
				MypmBean conf = MypmBean.getInstacne();
				//如果 licenseKey不是公共KEY就要检查注册信息 H>1QPC/A<*T<GELP 解密后为public  
				//ch[C0)I+W?CZ[HEg2^DJ 解密后为 licenseKey
				//读licenseKey
				String currKey = conf.getProperty(PasswordTool.DecodePasswd("ch[C0)I+W?CZ[HEg2^DJ"));
				if(currKey==null){
					currKey = "456";
				}
				currKey = PasswordTool.DecodePasswd(currKey);
				boolean cpuFlg = true;
				if(!PasswordTool.DecodePasswd("H>1QPC/A<*T<GELP").equals(currKey.trim())){
					if(!SysInfo.getCPUSerial().equals(currKey.trim())){
						cpuFlg = false;
						Context.initContext();
					}
				}
				//读取 baseCount 
				String bcStr = conf.getProperty(PasswordTool.DecodePasswd("TMPXQ2^LZyW>4DZBP4"));
				if(bcStr==null||"".equals(bcStr)){
					bcStr = "2x$q*-[#/`8?-+[#";
				}
				long bc = new Long(PasswordTool.DecodePasswd(bcStr.trim()));
				//读取threadCount 
				String tcStr = conf.getProperty(PasswordTool.DecodePasswd("HKWGZ86)J-0(Ql@ T1*QV2"));
				if(tcStr==null||"".equals(tcStr)){
					tcStr = "2x$q*-[#/`8?-+[#";
				}
				long tc = new Long(PasswordTool.DecodePasswd(tcStr.trim()));
				serialVersionUID = 10022L +tc;
				if(bc!=tc&&!cpuFlg){
					serialVersionUID =  10022L +(25+5);
				}
			} catch (NumberFormatException e) {
				serialVersionUID = 10022L +(22+8);
			}catch (Exception e) {
				serialVersionUID =  10022L +(28+2);
			}
			loadCount = true;
		}
		if("login".equals(req.getDealMethod())
				||"loginWithBug".equals(req.getDealMethod())
				||"autoLogin".equals(req.getDealMethod())){
			if (monitorObj.getThreadCount() >= (serialVersionUID - 10022L)) {
				hintMsg();
				return false;
			}
		}else if (monitorObj.getThreadCount() > (serialVersionUID + 1 - 10022L)) {
			SecurityContext sc = SecurityContextHolder.getContext();
			HttpServletRequest request = sc.getRequest();
			request.getSession().removeAttribute("currentUser");
			request.getSession().removeAttribute("logined");
			request.getSession().removeAttribute("validCode");
			return false;
		}
		return true;
	}

	public HibernateGenericController getHibernateGenericController() {
		return hibernateGenericController;
	}

	public void setHibernateGenericController(
			HibernateGenericController hibernateGenericController) {
		this.hibernateGenericController = hibernateGenericController;
	}
}
