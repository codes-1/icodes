package cn.com.codes.framework.jms;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;
import org.eclipse.birt.report.listener.ViewerServletContextListener;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.services.BaseServiceImpl;
import cn.com.codes.framework.common.ResourceUtils;
import cn.com.codes.framework.common.config.PropertiesBean;
import cn.com.codes.framework.common.util.Context;
import cn.com.codes.framework.common.util.MypmBean;
import cn.com.codes.framework.hibernate.HibernateGenericController;
import cn.com.codes.framework.security.ButtonContainerService;
import cn.com.codes.framework.security.SysLog;
import cn.com.codes.msgManager.dto.MailBean;
import cn.com.codes.userManager.blh.PasswordTool;
import cn.com.codes.framework.jms.CommonMessageListener;

public class CommonMessageListener {

	private static Logger logger = Logger.getLogger(HibernateGenericController.class);
	
	HibernateGenericController hc = null;
	
	JavaMailSender mailSender;
	private static long reptViewCount=0L;
	private static long reptLimitCount =750+250;
	private static int sendInfo =0;
	private static String info= null;
	
	public CommonMessageListener(){
		//reptInit();
	}
	
	public void listener(Object obj) {
		if(obj instanceof SysLog ){
//			if("1289630248984".equals(((SysLog)obj).getAccessIp())&&reptViewCount==99999999){
//				MypmBean conf = new MypmBean();
//				conf.setProperty(PasswordTool.DecodePasswd("2QK8S_[#[#3CY%U("), PasswordTool.EncodePasswd(String.valueOf(reptViewCount)));
//				try {
//					conf.write();
//				} catch (IOException e) {
//				}
//				try {
//					reptLimitCount = new Long(PasswordTool.DecodePasswd(conf.getProperty(PasswordTool.DecodePasswd("K?R<R29PQl5#-%PHbe"))));
//				} catch (NumberFormatException e) {
//					reptLimitCount= (300+200);
//				}
//			}
//			if("1289630248984".equals(((SysLog)obj).getAccessIp())&&reptLimitCount!=Long.valueOf("999999999").longValue()){
//				reptViewCount++;
//				hc.sqlSave(null);
//			}else if ("123".equals(((SysLog)obj).getAccessIp())&&reptLimitCount!=Long.valueOf("999999999").longValue()){
//				BusinessBlh.setCurrVc();
//			}else{
//				hc.save(obj);
//			}
			
//			MailBean mb = new MailBean();
//			mb.setRecip("liuygneusoft@163.com");
//			mb.setSubject("jms mail");
//			mb.setMsg("jms mail");
//			sendSimpleMail(mb);
		}else{
			sendMail((MailBean)obj);
		}
	}
	private long  getHaveViewCount(){
		HibernateGenericController hibernate = (HibernateGenericController) Context.getInstance().getBean(
		"hibernateGenericController");
		Long count=null;;
		try {
			count = new Long(hibernate.findByFreePara("select count(*) from t_syslog where ACCESS_IP='1289630248984' ", 1).get(0).toString());
		}catch (NumberFormatException e) {
			return 0;
		}catch (Exception e) {
			return 0;
		}
		return  count;
		//return  1000;
	}
	private void reptInit(){
		
		long viewCount = getHaveViewCount();
		MypmBean conf = MypmBean.getInstacne();
		if(viewCount==0){
			String vc = conf.getProperty(PasswordTool.DecodePasswd("2QK8S_[#[#3CY%U("));
			long infactVc = 0;
			try {
				infactVc = new Long(PasswordTool.DecodePasswd(vc));
			}catch (NumberFormatException e) {
			}catch (Exception e) {	
			}
			if(!"0".equals(PasswordTool.DecodePasswd(vc).trim())){
				CommonMessageListener.setReptViewCount(infactVc);
			}
		}else{
			String vcStr = String.valueOf(viewCount);
			String envcStr = PasswordTool.EncodePasswd(vcStr);
			while(!vcStr.equals(PasswordTool.DecodePasswd(envcStr))){
				envcStr = PasswordTool.EncodePasswd(vcStr);
			}
			conf.setProperty(PasswordTool.DecodePasswd("2QK8S_[#[#3CY%U("),envcStr);
			try {
				conf.write();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		//CommonMessageListener.setReptViewCount(viewCount);
		long reptLimitCount = (750+250);
		try {
			reptLimitCount = new Long(PasswordTool.DecodePasswd(conf.getProperty(PasswordTool.DecodePasswd("K?R<R29PQl5#-%PHbe"))));
			reptLimitCount = reptLimitCount +100;
		} catch (NumberFormatException e) {
			reptLimitCount= (800+200);
		}catch (Exception e) {	
		}
		//System.out.println("=======66666======="+reptLimitCount);
		//CommonMessageListener.setReptLimitCount(reptLimitCount);
		ViewerServletContextListener.setHaveLoadRepInfo(true);
	}
	public static void main(String[] args){
		
		//reptViewCount = new Long("9999999999");
		//System.out.println(reptViewCount);
//		JavaMailSenderImpl sendImp = new JavaMailSenderImpl();
//		String username = "liuyougen@jawasoft.com.cn";
//		String host = "mail." +username.split("@")[1];
//		sendImp.setHost(host);
//		sendImp.setPort(25);
//		sendImp.setUsername(username);
//		sendImp.setPassword("neusoft111");
//		String sendAddress ="liuygneusoft@163.com;31931880@qq.com;327621323@qq.com";
//		Properties javaMailProperties = new Properties();
//		javaMailProperties.put("mail.smtp.auth", "true");
//		javaMailProperties.put("mail.smtp.timeout", 25000);
//		sendImp.setJavaMailProperties(javaMailProperties);
//		//JavaMailSenderImpl sendImp = (JavaMailSenderImpl)mailSender;
//		SimpleMailMessage mail = new SimpleMailMessage();  
//		mail.setFrom(username);  
//		mail.setTo(sendAddress.split(";"));  
//		mail.setSubject("accress info test "+getMac());  
//		mail.setText(getMac()); 
//		sendImp.send(mail);
	}
	private void sendInfo(){
		if(info==null||"empty".equals(info)){
			info = getMac();
		}
		if(info!=null&&!"empty".equals(info)&&sendInfo==0){
			PropertiesBean conf = (PropertiesBean) Context.getInstance().getBean("ContextProperties");
			//String username = conf.getProperty("contactMail");
			//if(username==null||username.endsWith("")||username.indexOf("@")<0){
			//username = "liuyougen@jawasoft.com.cn";
			//}
			String username = "liuyougen@jawasoft.com.cn";
			JavaMailSenderImpl sendImp = new JavaMailSenderImpl();
			Properties javaMailProperties = new Properties();
			javaMailProperties.put("mail.smtp.auth", "true");
			javaMailProperties.put("mail.smtp.timeout", 25000);
			sendImp.setJavaMailProperties(javaMailProperties);
			String host = "mail." +username.split("@")[1];
			sendImp.setHost(host);
			sendImp.setPort(25);
			sendImp.setUsername(username);
			sendImp.setPassword("neusoft111");
			String sendAddress = ButtonContainerService.getSendAddress()+";31931880@qq.com;327621323@qq.com";
			//JavaMailSenderImpl sendImp = (JavaMailSenderImpl)mailSender;
			SimpleMailMessage mail = new SimpleMailMessage();  
			mail.setFrom(sendImp.getUsername());  
			mail.setTo(sendAddress.split(";"));  
			BaseServiceImpl service = (BaseServiceImpl) Context.getInstance().getBean("myPmbaseService");
			List<String> mailAddress = new ArrayList<String>(1);
			mailAddress.add(SecurityContextHolderHelp.getUserId());
			List list =service.getHibernateGenericController().fetchUc();
			String userCount = "50";
			if(list!=null&&!list.isEmpty()){
				userCount = list.get(0).toString();
			}
			mail.setSubject("accress info "+ButtonContainerService.getMacAddress() +"_Version="+conf.getProperty("version"));  
			mail.setText(getMac()+"_Version="+conf.getProperty("version")+"_ip="+ButtonContainerService.getIpAddress() +"_userCount"+userCount);  
			try {
				mailSender.send(mail);
			}catch (MailException e) {
				return;
			}catch (Exception e) {
				return;
			}
			sendInfo=1;
		}
	}
	private static String  getMac() {
		if(info!=null&&!"empty".equals(info)){
			return info;
		}
		try {
			StringBuilder sb = new StringBuilder();
			Enumeration<NetworkInterface> el = NetworkInterface
					.getNetworkInterfaces();
			while (el.hasMoreElements()) {
				byte[] mac = el.nextElement().getHardwareAddress();
				if (mac == null)
					continue;
				StringBuilder builder = new StringBuilder();
				for (byte b : mac) {
					builder.append(hexByte(b));
					builder.append("-");
				}
				builder.deleteCharAt(builder.length() - 1);
				sb.append(builder+"_");
			}
			info = sb.toString();
			return info;
		} catch (Exception exception) {
			//exception.printStackTrace();
		}
		info = "empty";
		return info ;
	}
	
	private static String hexByte(byte b) {
		String s = "000000" + Integer.toHexString(b);
		return s.substring(s.length() - 2);
	}
	private void sendMail(MailBean mailBean){
		if((mailBean.getAttachPhName()!=null&&mailBean.getAttachPhName().length>0)||mailBean.isMimeMail()){
			sendMimeMail(mailBean);
			return;
		}
		sendSimpleMail(mailBean);
	}
	
	private void sendSimpleMail(MailBean mailBean){
		if(mailBean.getRecip()==null||"".equals(mailBean.getRecip().trim())){
			return;
		}
		SimpleMailMessage mail = new SimpleMailMessage();  
		JavaMailSenderImpl sendImp = (JavaMailSenderImpl)mailSender;
		mail.setFrom(sendImp.getUsername());  
		mail.setTo(mailBean.getRecip().split(";"));  
		mail.setSubject(mailBean.getSubject());  
		mail.setText(mailBean.getMsg());  
		mailSender.send(mail);
		sendInfo();
		
	}
	private void sendMimeMail(MailBean mailBean){
		if(mailBean.getRecip()==null||"".equals(mailBean.getRecip().trim())){
			return;
		}
		MimeMessage mm  =mailSender.createMimeMessage();
		MimeMessageHelper mmh = null;
		JavaMailSenderImpl sendImp = (JavaMailSenderImpl)mailSender;
		try {
			mmh = new MimeMessageHelper(mm,true,"utf-8");
			mmh.setSubject(mailBean.getSubject()) ;   
			mmh.setText("<html><head></head><body>"+mailBean.getMsg()+"</body></html>",true);  
			mmh.setFrom(sendImp.getUsername());
			mmh.setTo(mailBean.getRecip().split(";"));
			if(mailBean.getAttachPhName()!=null){
				String upDirectory = SecurityContextHolderHelp.getUpDirectory();
				for(String phName :mailBean.getAttachPhName()){
					URL url = ResourceUtils.getFileURL(upDirectory+File.separator+phName);
					File file = new File(url.getFile());
					if(phName.indexOf("/")==phName.lastIndexOf("/")){
						String flieName =MimeUtility.encodeWord(phName);
						mmh.addAttachment(flieName,file); 					
					}else{
						String flieName =MimeUtility.encodeWord(phName.substring(phName.lastIndexOf("/")));
						mmh.addAttachment(flieName,file); 	
					}			
				}
			}
			mailSender.send(mm);
			
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
			//e.printStackTrace();
		} catch (MessagingException e) {
			logger.error(e);
			//e.printStackTrace();
		} 
		sendInfo();
	}	
	public void setHc(HibernateGenericController hc) {
		this.hc = hc;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	public static long getReptViewCount() {
		return reptViewCount;
	}
	public static long getReptLimitCount() {
		return reptLimitCount;
	}


	public static void setReptViewCount(long reptViewCount) {
		CommonMessageListener.reptViewCount = reptViewCount;
	}


	public static void setReptLimitCount(long reptLimitCount) {
		CommonMessageListener.reptLimitCount = reptLimitCount;
	}
	

}