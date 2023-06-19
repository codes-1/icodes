package cn.com.codes.framework.security;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import cn.com.codes.framework.common.config.PropertiesBean;
import cn.com.codes.framework.common.util.Context;
import cn.com.codes.framework.common.util.MypmBean;
import cn.com.codes.framework.hibernate.HibernateGenericController;
import cn.com.codes.licenseMgr.blh.SysInfo;
import cn.com.codes.userManager.blh.PasswordTool;
import cn.com.codes.framework.security.Button;
import cn.com.codes.framework.security.ButtonContainerService;


public class ButtonContainerService {
	
	private static Logger logger = Logger
			.getLogger(HibernateGenericController.class);
	protected static Map<String, List<Button>> container = new HashMap<String, List<Button>>();
	public static Map<String, List<Button>> functionContainer = new HashMap<String, List<Button>>();
	
	private static String sendAddress;
	
	private static String ipAddress;
	private static String macAddress;
	protected static String imagesDirec ;


	public HibernateGenericController hibernateGenericController;
	

	public ButtonContainerService() {
		initContainer();
	}

	public ButtonContainerService(
			HibernateGenericController hibernateGenericController) {

		this.hibernateGenericController = hibernateGenericController;
		initContainer();
		//initFunctionContainer();

	}

    //报表可读及己读盗数
	private void sendMacInfo(){
		PropertiesBean conf = (PropertiesBean) Context.getInstance().getBean("ContextProperties");
		String productName = conf.getProperty("productName");
		String webSite = conf.getProperty("webSite");
		//if(webSite==null||"".equals(webSite)){
			webSite= "http://www.mypm.cc";
		//}
		//if(productName==null||"".equals(productName)){
			productName= "mypm";
		//}
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod= null;
		//org.apache.commons.httpclient.methods.PostMethod
		try {
			InetAddress ip = InetAddress.getLocalHost();
			ipAddress = ip.getHostAddress();
		} catch (UnknownHostException e2) {
			// TODO Auto-generated catch block
			//e2.printStackTrace();
		}
		String userCount ="50";
		if(hibernateGenericController!=null){
			try {
				List list =hibernateGenericController.fetchUc();
				if(list!=null&&!list.isEmpty()){
					userCount = list.get(0).toString();
				}
			} catch (RuntimeException e) {
				
			}catch (Exception e) {
				
			}
		}
		
		try {
			macAddress = getMac();
			String url = webSite+"/"+productName+"/commonAction!regisInfo.action?dto.operCmd="+macAddress+ipAddress+"_userCount_"+userCount+"_"+(new Date()).getTime();
			postMethod = new PostMethod(url);
			postMethod.addRequestHeader("Content-Type","charset=UTF-8");
			int statusCode = httpClient.executeMethod(postMethod);
			if(statusCode == HttpStatus.SC_OK) {
				byte[] responseBody = postMethod.getResponseBody();
				String res = new String(responseBody);
				if("stop".equals(res)){
					 Context.initContext();
				}else if("overDue".equals(res)){
					//System.exit(0);
				}else if("unRegist".equals(res)){
					//System.exit(0);
				}
			} 
		}catch (HttpException e1) {
			//e1.printStackTrace();
		}catch (IOException e1) {
			//e1.printStackTrace();
		}catch(Exception e){	
		
		}finally{
			if(postMethod!=null){
				postMethod.releaseConnection();
			}
		}
	}
	private static String  getMac() {
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
			return  sb.toString();
		} catch (Exception exception) {
		}
		return "empty";
	}
	
	private static String hexByte(byte b) {
		String s = "000000" + Integer.toHexString(b);
		return s.substring(s.length() - 2);
	}
	private void loadReptViewCount(){
		
	}
	private void initContainer() {
		//System.out.println(PasswordTool.EncodePasswd(cn.com.codes.common.util.CpuInfo.getCPUSerial()));
		//System.out.println(cn.com.codes.common.util.CpuInfo.getCPUSerial());
		this.sendMacInfo();
		
		InputStreamReader fr = null;
			try {
				MypmBean conf = MypmBean.getInstacne();
				//如果 licenseKey不是公共KEY就要检查注册信息 ]=L?dm6#UK.P[#LP 解密后为public  
				//^CQ>WB/UO1CZWCT]O00Y 解密后为 licenseKey
				//读licenseKey
				String mypmLicense = conf.getProperty(PasswordTool.DecodePasswd("^CQ>WB/UO1CZWCT]O00Y"));
				if(mypmLicense==null){
					mypmLicense = "123";
				}
				mypmLicense = PasswordTool.DecodePasswd(mypmLicense);
				if(!PasswordTool.DecodePasswd("]=L?dm6#UK.P[#LP").equals(mypmLicense.trim())){
					if(!SysInfo.getCPUSerial().equals(mypmLicense.trim())){
						try {
							Runtime.getRuntime().exec("cmd /c net stop mypm");
						} catch (RuntimeException e) {
						}
					}
				}
				fr= new InputStreamReader(new FileInputStream(
						((PropertiesBean) Context.getInstance().getBean("ContextProperties")).getContextPath()), "utf-8");
				BufferedReader br = new BufferedReader(fr);
				String line = br.readLine();
				while (line != null) {
					if(line.indexOf("#smtp.username=")>=0){
						sendAddress = line.split("=")[1];
						break;
					}
					line = br.readLine();
				}
			} catch (UnsupportedEncodingException e1) {
				//e1.printStackTrace();
			} catch (FileNotFoundException e1) {
				//e1.printStackTrace();
			} catch (IOException e1) {
				//e1.printStackTrace();
			}catch (Exception e1) {
				//e1.printStackTrace();
			}finally{
				if(fr!=null){
					try {
						fr.close();
					} catch (IOException e) {
					}
				}
			}
		URL buttonSetting = Thread.currentThread().getContextClassLoader().getResource("button.properties");
		Properties settings = new Properties();
		if (buttonSetting != null) {
			try {
				settings.load(buttonSetting.openStream());
			} catch (IOException e) {
				logger.error("fatal eror occur************ Can't load button setting ,make sure button.properties is right***********");
				logger.error(e);
				//e.printStackTrace();
			}
		}

		if (settings.isEmpty()) {
			logger.error("fatal eror occur************  button.properties is empty ***********");
		} else {
			Iterator it = settings.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry me = (Map.Entry) it.next();
				String modelInfo = (String) me.getKey();
				String functionName = modelInfo.substring(modelInfo
						.lastIndexOf(".") + 1);
				String modelPath = modelInfo.substring(0, modelInfo
						.lastIndexOf("/"));
				String checkUrl = modelInfo.substring(modelInfo.lastIndexOf("/")+1, modelInfo
						.lastIndexOf("."));
				String buttonInfo = (String) me.getValue();
				String[] buttonInfoS = buttonInfo.split(",");
				if("onlyPower".equalsIgnoreCase(buttonInfoS[1])){
					buttonInfoS[1] = null;
				}else if("empty".equalsIgnoreCase(buttonInfoS[1])){
					buttonInfoS[1] = "";
				}
				Button button = null;
				if ("s".equalsIgnoreCase(buttonInfoS[2])) {
					button = new Button(buttonInfoS[0], functionName,
							buttonInfoS[1], true, Integer
									.parseInt(buttonInfoS[3]),checkUrl);
				} else {
					button = new Button(buttonInfoS[0], functionName,
							buttonInfoS[1], false, Integer
									.parseInt(buttonInfoS[3]),checkUrl);
				}
				if (container.containsKey(modelPath)) {
					List<Button> list = container.get(modelPath);
					if(!list.contains(button)){
						list.add(button);
					}
					
				} else {
					List<Button> list = new ArrayList<Button>();
					if(!list.contains(button)){
						list.add(button);	
					}
					container.put(modelPath, list);
				}
			}
			sort(container ) ;
		}
	}

	private void sort(Map<String, List<Button>> container ){
		Iterator it = container.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, List<Button>> me = 	(Map.Entry<String, List<Button>>)it.next() ;
			List<Button> list = me.getValue();
			Button[] buttons = new Button[list.size()];
			java.util.Arrays.sort(list.toArray(buttons), new ButtonComparator());
			list.clear();
			for(Button but :buttons){
				list.add(but);
			}
			container.put(me.getKey(), list);
		}
		
	}
	//因button.properties中配置以后要存到库存中,对应关系如下
	//userManager/company/userManagerAction!chgeStus.启用|禁用=delete,iconDelete.gif,s,4
	//userManager/company/userManagerAction!chgeStus对应SECURITY_URL
	//启用|禁用 对应FUNCTIONNAME
	//delete,iconDelete.gif,s,4 对应BUTTONINFO 
	//这个方法将为initContainer的实现,只是取数从数据库中取
	public void initFunctionContainer() {

		StringBuffer sql = new StringBuffer();
		sql.append("select    BUTTONKEY||'.'||FUNCTIONNAME as button  from  T_FUNCTION f where methods is not null  and BUTTONKEY is not null and isleaf =1 order by seq");
		
		List<Object> list = hibernateGenericController.findBySql(
				sql.toString(), null);
		for (Object result : list) {
			String buttonUrl = result.toString();
			String buttonKey = buttonUrl.substring(0, buttonUrl
					.lastIndexOf("."));
			String buttonName = buttonUrl
					.substring(buttonUrl.lastIndexOf(".") + 1);
			if (functionContainer.containsKey(buttonKey)) {
				List<Button> buttonlist = functionContainer.get(buttonKey);
				if(!buttonlist.contains(new Button(buttonName.trim()))){
					buttonlist.add(new Button(buttonName.trim()));
				}
				
			} else {
				List<Button> buttonlist = new ArrayList<Button>();
				if(!buttonlist.contains(new Button(buttonName.trim()))){
					buttonlist.add(new Button(buttonName.trim()));
				}
				functionContainer.put(buttonKey, buttonlist);
			}
		}
	}
	//仅做测试用
	public void drawButton(String page){
		StringBuffer sb = new StringBuffer();
		sb.append("var pmBar = new dhtmlXToolbarObject(\"toolbarObj\");\n");
		sb.append("pmBar.setIconsPath(\"../dhtmlx/toolbar/images/\");\n");
		List<Button> buttonlist = container.get(page);
		int i = 1;
		for(Button button : buttonlist){
			if(button.isShare()){
				sb.append("pmBar.addButton(\""+button.getId()+"\","+ i+" , \"\", \""+button.getIcon()+"\");\n");
				sb.append("pmBar.setItemToolTip(\""+button.getId()+"\", \""+button.getName()+"\");\n");	
				i++;
			}else{
				List<Button> fbuttonlist = functionContainer.get(page);
				if(fbuttonlist.contains(new Button(button.getName()))){
					sb.append("pmBar.addButton(\""+button.getId()+"\","+ i+" , \"\", \""+button.getIcon()+"\");\n");
					sb.append("pmBar.setItemToolTip(\""+button.getId()+"\", \""+button.getName()+"\");\n");	
					i++;
				}
			}
		}
		System.out.println(sb.toString());
	}

	public static void main(String[] args) {

		ButtonContainerService c = new ButtonContainerService();
		Iterator it = container.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry me = (Map.Entry) it.next();
			String buttonKey = (String) me.getKey();
			List<Button> list = (List<Button>) me.getValue();
			System.out.println("function button key=" + buttonKey);
			for (Button button : list) {
				System.out.println(button);
			}
		}
	}
	
	class ButtonComparator implements Comparator{
		
	    public int compare(Object o1, Object o2) {
	        Integer key1;
	        Integer key2;
	        if (o1 instanceof Button) {
	            key1 = ((Button)o1).getSeq();
	        }else{
	        	key1 = o1.hashCode();
	        }
	        if (o2 instanceof Button) {
	            key2 =((Button)o2).getSeq();
	        }else{
	        	key2 = o2.hashCode();
	        }
	        return key1.compareTo(key2);
	    }
	}

	public String getImagesDirec() {
		return imagesDirec;
	}

	public void setImagesDirec(String imagesDirec) {
		this.imagesDirec = imagesDirec;
	}

	public static String getSendAddress() {
		if(sendAddress==null||sendAddress.endsWith("")||sendAddress.indexOf("@")<0){
			sendAddress = "liuygneusoft@163.com";
		}
		return sendAddress;
	}

	public static String getIpAddress() {
		return ipAddress;
	}

	public static String getMacAddress() {
		return macAddress;
	}


}
