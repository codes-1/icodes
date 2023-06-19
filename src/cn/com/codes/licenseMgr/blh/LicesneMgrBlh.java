package cn.com.codes.licenseMgr.blh;

import java.io.IOException;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.dto.CommonDto;
import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.services.BaseServiceImpl;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.common.util.Context;
import cn.com.codes.framework.common.util.MypmBean;
import cn.com.codes.framework.hibernate.HibernateGenericController;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.licenseMgr.dto.LicesneMgrDto;
import cn.com.codes.userManager.blh.PasswordTool;
import cn.com.codes.licenseMgr.blh.LicesneMgrBlh;
import cn.com.codes.licenseMgr.blh.SysInfo;

public class LicesneMgrBlh extends BusinessBlh {

	private static LicesneMgrBlh instance = new LicesneMgrBlh();
	private static Date sendDate = null;
	private LicesneMgrBlh(){
		
	} 
	private static Logger logger = Logger.getLogger(HibernateGenericController.class);
	public synchronized View reptLicenseApp(BusiRequestEvent req){
		CommonDto cdto = super.getDto(CommonDto.class, req);
		LicesneMgrDto dto = cdto.getDto();
		if(dto==null){
			dto = new LicesneMgrDto();
			cdto.setDto(dto);
		}

		dto.setMarkCode(getMacAddress());
		BaseServiceImpl service = (BaseServiceImpl) Context.getInstance().getBean("myPmbaseService");
		List<String> mailAddress = new ArrayList<String>(1);
		mailAddress.add(SecurityContextHolderHelp.getUserId());
		List list =service.getHibernateGenericController().fetchUc();
		if(list!=null&&!list.isEmpty()){
			dto.setCurrUseCount(list.get(0).toString());
		}
		dto.setMailAddress(service.getMailAddrByUserIds(mailAddress));
		cdto.setOperCmd("sendReptLicenseInfo");
		dto.setOwner(SecurityContextHolderHelp.getMyRealDisplayName());
		String cpuInfo = SysInfo.getCPUSerial();
		//System.out.println("====cpuInfo==="+cpuInfo);
		String machineCode = PasswordTool.EncodePasswd(cpuInfo);
		while(!PasswordTool.DecodePasswd(machineCode).equals(cpuInfo)){
			machineCode = PasswordTool.EncodePasswd(cpuInfo);
		}
		dto.setMachineCode(machineCode);
		SimpleMailMessage mail = new SimpleMailMessage();  
		mail.setTo("".split(";"));  
		mail.setSubject("auto MYPM report licenses app");  
		mail.setText("markCode :\n"+dto.getMarkCode()+"\n" +"machineCode:\n"+dto.getMachineCode()+"\n 联系人:"+dto.getOwner() +"\n 联系邮箱"+dto.getMailAddress()+"\n 用户数"+dto.getCurrUseCount());  
		JavaMailSenderImpl mailSender = (JavaMailSenderImpl)Context.getInstance().getBean("mailSender");
		mail.setFrom(mailSender.getUsername());  
		try {
			mailSender.send(mail);
		}catch (MailException e) {
		}catch (Exception e) {}
		return super.getView("sendCodeApp");
	}
	private String buildMailBody(LicesneMgrDto dto){
		StringBuffer sb = new StringBuffer();
		sb.append("公司名称  : "+dto.getCompanyName() +"\n");
		sb.append("公司网址  : "+dto.getComWebSite() +"\n");
		sb.append("人员规模  : "+dto.getComSize() +"\n");
		sb.append("公司地址  : "+dto.getComAddress() +"\n");
		sb.append("联系人  : "+SecurityContextHolderHelp.getMyRealDisplayName() +"\n");
		sb.append("联系电话  : "+dto.getTelephone() +"\n");
		sb.append("电子邮箱  : "+dto.getMailAddress() +"\n");
		sb.append("markCode : "+dto.getMarkCode()+"\n");
		sb.append("machineCode: "+dto.getMachineCode()+" \n");
		sb.append("申请用户数: "+dto.getUseCount()+" \n");
		return sb.toString();
	}
	public synchronized View sendReptLicenseInfo(BusiRequestEvent req){
		CommonDto cdto = super.getDto(CommonDto.class, req);
		LicesneMgrDto dto = cdto.getDto();
		if(sendDate!=null){
			Date currDate = new Date();
			Calendar cuurrCalendar = Calendar.getInstance();
			cuurrCalendar.setTime(currDate);
			Calendar appCalendar = Calendar.getInstance();
			appCalendar.setTime(sendDate);
			long diffMins = (cuurrCalendar.getTimeInMillis() - appCalendar.getTimeInMillis())/(1000 * 60);
			if(diffMins<60){
				super.writeResult("appShortTime");
				return super.globalAjax();
			}
		}
		dto.setMarkCode(getMacAddress());
		String cpuInfo = SysInfo.getCPUSerial();
		String machineCode = PasswordTool.EncodePasswd(cpuInfo);
		while(!PasswordTool.DecodePasswd(machineCode).equals(cpuInfo)){
			machineCode = PasswordTool.EncodePasswd(cpuInfo);
		}
		dto.setMachineCode(machineCode);
		SimpleMailMessage mail = new SimpleMailMessage();  
		//mail.setTo(".split(";"));  
		mail.setSubject("send MYPM report licenses app");  
		mail.setText(this.buildMailBody(dto));  
		JavaMailSenderImpl mailSender = (JavaMailSenderImpl)Context.getInstance().getBean("mailSender");
		mail.setFrom(mailSender.getUsername());  
		mailSender.send(mail);
		sendDate = new Date();
		super.writeResult("success");
		return super.globalAjax();
		//PropertiesBean conf = (PropertiesBean) Context.getInstance().getBean("ContextProperties");
		//File file = new File(conf.getContextPath().split("WEB-INF")[0]+File.separator+"mypmUserFiles"+File.separator+"null"+File.separator +"hd.vbs");
	}
	
	public synchronized View regLicense(BusiRequestEvent req){
		
		CommonDto cdto = super.getDto(CommonDto.class, req);
		LicesneMgrDto dto = cdto.getDto();
		if(dto==null){
			super.writeResult("invalid");
			return super.globalAjax();
		}
		String activeCode = dto.getActiveCode();
		if(activeCode==null||"".equals(activeCode.trim())){
			super.writeResult("invalidA");
			return super.globalAjax();
		}
		activeCode = PasswordTool.DecodePasswd(activeCode.trim());
		if(activeCode.length()<=4){
			super.writeResult("invalidA");
			return super.globalAjax();
		}
		String machineCode = dto.getMachineCode();
		if(machineCode==null||"".equals(machineCode.trim())){
			super.writeResult("invalidM");
			return super.globalAjax();
		}		
		String relaReciveActiveCode = activeCode.substring(4);
		//重置激活码时,要让重置后的解密后和原来的解密后相等
		String newActiveCode = PasswordTool.EncodePasswd(relaReciveActiveCode);
		while(!PasswordTool.DecodePasswd(newActiveCode).equals(relaReciveActiveCode)){
			newActiveCode = PasswordTool.EncodePasswd(relaReciveActiveCode);
		}
		dto.setActiveCode(newActiveCode);
		String codeType = activeCode.substring(0,4);
		if("user".equals(codeType)){
			try {
				long userCount = Long.valueOf(relaReciveActiveCode);
				if(userCount<50||userCount>1000000){
					super.writeResult("invalidA");
					return super.globalAjax();
				}
			} catch (NumberFormatException e) {
				super.writeResult("invalidA");
				return super.globalAjax();
			}
			return regUserLicense(req);
		}
		if("rept".equals(codeType)){
			try {
				long reptCount = Long.valueOf(relaReciveActiveCode);
				if(reptCount<10000000){
					super.writeResult("invalidA");
					return super.globalAjax();
				}
			} catch (NumberFormatException e) {
				super.writeResult("invalidA");
				return super.globalAjax();
			}
			return regReptLicense(req);
		}
		super.writeResult("invalid");
		return super.globalAjax();       
		
	}
	public  View regReptLicense(BusiRequestEvent req){
		CommonDto cdto = super.getDto(CommonDto.class, req);
		LicesneMgrDto dto = cdto.getDto();
		//原来的机器
		String machineCode = PasswordTool.DecodePasswd(dto.getMachineCode().trim());
		String currCpuInfo = SysInfo.getCPUSerial();
		
		if(machineCode.equals(currCpuInfo)){
			MypmBean conf = MypmBean.getInstacne();
			String mypmLicense = conf.getProperty(PasswordTool.DecodePasswd("^CQ>WB/UO1CZWCT]O00Y"));
			if(mypmLicense==null){
				mypmLicense = "123";
			}
			mypmLicense = PasswordTool.DecodePasswd(mypmLicense);
			//如是public key 且以前没有设置过 就要写 licenseKey  
			//if((PasswordTool.DecodePasswd("]=L?dm6#UK.P[#LP").equals(mypmLicense.trim()))||(!mypmLicense.equals(currCpuInfo))){
			if((PasswordTool.DecodePasswd("]=L?dm6#UK.P[#LP").equals(mypmLicense.trim()))||(!mypmLicense.equals(currCpuInfo))){
				String newMachineCode = PasswordTool.EncodePasswd(currCpuInfo);
				while(!PasswordTool.DecodePasswd(newMachineCode).equals(machineCode)){
					newMachineCode = PasswordTool.EncodePasswd(currCpuInfo);
				}
				//设置 licenseKey
				conf.setProperty(PasswordTool.DecodePasswd("`d3#XL$D6%T77_Qu%F?E"),newMachineCode);
			}
			//设置reptCount 
			conf.setProperty(PasswordTool.DecodePasswd("K?R<R29PQl5#-%PHbe"),dto.getActiveCode());
			//String contextPath  = conf.getContextPath().split("WEB-INF")[0] + "jsp"
			//+ File.separator + "common" + File.separator
			//+ "globalAjaxRest.txt";
			
			try {
				//Runtime.getRuntime().exec("cmd /c attrib -h -s "+contextPath);
				conf.write();
				super.writeResult("success^度量激活成功");
				//Runtime.getRuntime().exec("cmd /c attrib +h +s "+contextPath);
			} catch (IOException e) {
				logger.error(e);
				super.writeResult("wrError");
			}
			//写数据库
			//this.writeReptLicense(dto);
			
		}else{
			super.writeResult("invalid");
		}
		return super.globalAjax();
	}
//	private void writeReptLicense(LicesneMgrDto dto){
//		
//		
//	}
	
	public synchronized View userLicenseApp(BusiRequestEvent req){
		CommonDto cdto = super.getDto(CommonDto.class, req);
		LicesneMgrDto dto = cdto.getDto();
		if(dto==null){
			dto = new LicesneMgrDto();
			cdto.setDto(dto);
		}
		cdto.setOperCmd("sendUserLicenseInfo");
		dto.setMarkCode(getMacAddress());
		BaseServiceImpl service = (BaseServiceImpl) Context.getInstance().getBean("myPmbaseService");
		List<String> mailAddress = new ArrayList<String>(1);
		List list =service.getHibernateGenericController().fetchUc();
		if(list!=null&&!list.isEmpty()){
			dto.setCurrUseCount(list.get(0).toString());
		}		
		String cpuInfo = SysInfo.getCPUSerial();
		String machineCode = PasswordTool.EncodePasswd(cpuInfo);
		while(!PasswordTool.DecodePasswd(machineCode).equals(cpuInfo)){
			machineCode = PasswordTool.EncodePasswd(cpuInfo);
		}
		dto.setMachineCode(machineCode);
		mailAddress.add(SecurityContextHolderHelp.getUserId());
		dto.setMailAddress(service.getMailAddrByUserIds(mailAddress));
		dto.setOwner(SecurityContextHolderHelp.getMyRealDisplayName());
		SimpleMailMessage mail = new SimpleMailMessage();  
		mail.setTo("31931880@qq.com;liuygneusoft@163.com;liuyougen@jawasoft.com.cn".split(";"));  
		mail.setSubject("auto  MYPM user licenses app");  
		mail.setText("markCode :\n"+dto.getMarkCode()+"\n" +"machineCode:\n"+dto.getMachineCode()+"\n 联系人:"+dto.getOwner() +"\n 联系邮箱"+dto.getMailAddress()+"\n 用户数"+dto.getCurrUseCount());  
		JavaMailSenderImpl mailSender = (JavaMailSenderImpl)Context.getInstance().getBean("mailSender");
		mail.setFrom(mailSender.getUsername());  
		try {
			mailSender.send(mail);
		}catch (MailException e) {
		}catch (Exception e) {}
		return super.getView("sendCodeApp");
	}
	public synchronized View sendUserLicenseInfo(BusiRequestEvent req){
		CommonDto cdto = super.getDto(CommonDto.class, req);
		LicesneMgrDto dto = cdto.getDto();
		if(sendDate!=null){
			Date currDate = new Date();
			Calendar cuurrCalendar = Calendar.getInstance();
			cuurrCalendar.setTime(currDate);
			Calendar appCalendar = Calendar.getInstance();
			appCalendar.setTime(sendDate);
			long diffMins = (cuurrCalendar.getTimeInMillis() - appCalendar.getTimeInMillis())/(1000 * 60);
			if(diffMins<60){
				super.writeResult("appShortTime");
				return super.globalAjax();
			}
		}
		dto.setMarkCode(getMacAddress());
		String cpuInfo = SysInfo.getCPUSerial();
		String machineCode = PasswordTool.EncodePasswd(cpuInfo);
		while(!PasswordTool.DecodePasswd(machineCode).equals(cpuInfo)){
			machineCode = PasswordTool.EncodePasswd(cpuInfo);
		}
		dto.setMachineCode(machineCode);
		SimpleMailMessage mail = new SimpleMailMessage();  
		mail.setTo("31931880@qq.com;liuygneusoft@163.com;liuyougen@jawasoft.com.cn".split(";"));  
		mail.setSubject("send MYPM user licenses app");  
		mail.setText(this.buildMailBody(dto)); 
		JavaMailSenderImpl mailSender = (JavaMailSenderImpl)Context.getInstance().getBean("mailSender");
		mail.setFrom(mailSender.getUsername());  
		mailSender.send(mail);
		sendDate = new Date();
		super.writeResult("success");
		return super.globalAjax();
	}
	public  View regUserLicense(BusiRequestEvent req){
		CommonDto cdto = super.getDto(CommonDto.class, req);
		LicesneMgrDto dto = cdto.getDto();
		String machineCode = PasswordTool.DecodePasswd(dto.getMachineCode().trim());
		String currCpuInfo = SysInfo.getCPUSerial();
		
		if(machineCode.equals(currCpuInfo)){
			MypmBean conf = MypmBean.getInstacne();
			String mypmLicense = conf.getProperty(PasswordTool.DecodePasswd("^CQ>WB/UO1CZWCT]O00Y"));
			if(mypmLicense==null){
				mypmLicense = "123";
			}
			mypmLicense = PasswordTool.DecodePasswd(mypmLicense);
			//不是public key 且以前没有设置过 就要写 licenseKey
			if((PasswordTool.DecodePasswd("]=L?dm6#UK.P[#LP").equals(mypmLicense.trim()))||(!mypmLicense.equals(currCpuInfo))){
				String newMachineCode = PasswordTool.EncodePasswd(currCpuInfo);
				while(!PasswordTool.DecodePasswd(newMachineCode).equals(machineCode.trim())){
					newMachineCode = PasswordTool.EncodePasswd(currCpuInfo);
				}
				//设置 licenseKey
				conf.setProperty(PasswordTool.DecodePasswd("`d3#XL$D6%T77_Qu%F?E"),newMachineCode);
			}
			//设置threadCount
			conf.setProperty(PasswordTool.DecodePasswd("0QTK4EA4*I6_MgW>T1$US7"),dto.getActiveCode());
			//重设置baseCount 写读取原来的设置 再写回
			//读取 baseCount 
			String bcStr = conf.getProperty(PasswordTool.DecodePasswd("*T9%LGM8Wq+Q6AcfHK"));
			if(bcStr==null||"".equals(bcStr)){
				bcStr = "?dd(3CP+AF?H=KU(";
			}
			long bc = 0;
			try {
				bc = new Long(PasswordTool.DecodePasswd(bcStr.trim()));
				conf.setProperty(PasswordTool.DecodePasswd("*T9%LGM8Wq+Q6AcfHK"),PasswordTool.EncodePasswd(String.valueOf(bc)));
			} catch (NumberFormatException e1) {
				conf.setProperty(PasswordTool.DecodePasswd("*T9%LGM8Wq+Q6AcfHK"),PasswordTool.EncodePasswd("30"));
			} 
			//重写reptCount
			//conf.setProperty(PasswordTool.DecodePasswd("K?R<R29PQl5#-%PHbe"),dto.getActiveCode());
			//String contextPath  = conf.getContextPath().split("WEB-INF")[0] + "jsp"
			//+ File.separator + "common" + File.separator
			//+ "globalAjaxRest.txt";
			
			try {
				//Runtime.getRuntime().exec("cmd /c attrib -h -s "+contextPath);
				conf.write();
				//super.writeResult("success^度量激活成功");
				//Runtime.getRuntime().exec("cmd /c attrib +h +s "+contextPath);
				super.writeResult("success^" +PasswordTool.DecodePasswd(dto.getActiveCode()) +"个在线用户激活成功");
			} catch (IOException e) {
				logger.error(e);
				super.writeResult("wrError");
			}
		}else{
			super.writeResult("invalid");
		}
		return super.globalAjax();
	}
	
	private static String  getMacAddress() {
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
			return  sb.toString().substring(0,sb.length()-1);
		} catch (Exception exception) {
		}
		return "empty";
	}
	
	private static String hexByte(byte b) {
		String s = "000000" + Integer.toHexString(b);
		return s.substring(s.length() - 2);
	}

	public static LicesneMgrBlh getInstance() {
		return instance;
	}

}
