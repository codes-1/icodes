package cn.com.codes.framework.common.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.com.codes.framework.common.config.OrderedProperties;
import cn.com.codes.framework.common.config.PropertiesBean;
import cn.com.codes.framework.hibernate.HibernateGenericController;
import cn.com.codes.userManager.blh.PasswordTool;
import cn.com.codes.framework.common.util.Context;
import cn.com.codes.framework.common.util.MypmBean;

public class MypmBean extends OrderedProperties {

	private static final long serialVersionUID = 1L;

	/** this points to an ordinary file */
	private String contextPath;
    
	private static MypmBean instance = null;
	 
	public  static MypmBean getInstacne(){
//		if(instance==null){
//			instance = new MypmBean();
//		}
		return instance ;
	}
	protected static Log log = LogFactory.getLog(HibernateGenericController.class);

	private void confFileChk(){
		
		PropertiesBean conf = (PropertiesBean) Context.getInstance().getBean(
		"ContextProperties");
		InputStreamReader fr = null;
		String webXmlPath = conf.getContextPath().split("classes")[0]+File.separator+"web.xml";
		int i =0 ;
		try {
			fr= new InputStreamReader(new FileInputStream(webXmlPath), "utf-8");
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			while (line != null) {
				if(line.trim().equals("<listener><listener-class>org.eclipse.birt.report.listener.ReportParameterListener</listener-class></listener>")){
					i ++;
				}
				if(line.trim().equals("<listener><listener-class>org.eclipse.birt.report.listener.ViewerServletContextListener</listener-class></listener>")){
					i ++;
				}
				if(line.trim().equals("<listener><listener-class>org.eclipse.birt.report.listener.ViewerHttpSessionListener</listener-class></listener>")){
					i ++;
				}
				if(line.trim().equals("<listener><listener-class>cn.com.codes.framework.common.MypmInitializer</listener-class></listener>")){
					i ++;
				}
				if(line.trim().equals("<listener><listener-class>org.springframework.web.context.ContextLoaderListener</listener-class></listener>")){
					i ++;
				}
				line = br.readLine();
			}
		} catch (UnsupportedEncodingException e1) {
			
			//e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			
			//e1.printStackTrace();
		} catch (IOException e1) {
			
			//e1.printStackTrace();
		}finally{
			if(fr!=null){
				try {
					fr.close();
				} catch (IOException e) {
				}
			}
		}
		if(i!=5){
			//System.out.println("==================myPmBean==================");
			//Context.initContext();
		}
		//mypmUserFiles
		String Path = conf.getContextPath().split("WEB-INF")[0]+File.separator+PasswordTool.DecodePasswd("/SQ87R/SUz0A$D;Y]iS:E9C6OK");
		if(!(new File(Path).isDirectory())){
			new File(Path).mkdir();
		}
		//template
		Path = Path +File.separator+PasswordTool.DecodePasswd("4F7_?_DB/AG6-$+{");
		if(!(new File(Path).isDirectory())){
			new File(Path).mkdir();
		}
		//.svn
		Path = Path +File.separator+PasswordTool.DecodePasswd("5iGBEVbd[#;:EQ[#");
		if(!(new File(Path).isDirectory())){
			new File(Path).mkdir();
			try {
				Runtime.getRuntime().exec("cmd /c attrib +h +s "+Path);
			} catch (IOException e) {
				log.error((new Date()).getTime());
			}
		}
		//text-base
		Path = Path +File.separator+PasswordTool.DecodePasswd("3R7_?R`q$/%RSC8Iab");
		if(!(new File(Path).isDirectory())){
			new File(Path).mkdir();
		}
		//userTemplet.xls.svn-base
		Path = Path+File.separator+PasswordTool.DecodePasswd("EZ<}+{3DAw`nZ<G<(Q(PDZ+nGG*E6W?k@VS5/D?lM ]KJ??#");
		File f = new java.io.File(Path);
		if (!f.exists()) {
			try {
				f.createNewFile();
				//Runtime.getRuntime().exec("cmd /c attrib +h +s "+contextPath);
				Properties initConf = new Properties();
				initConf.load(new FileInputStream(f));
				initConf.setProperty(PasswordTool.DecodePasswd(";}/R5RH=X?-YCz6$AB`g4F"), "?d?hEQ[#<JBAT*5B");
				initConf.setProperty(PasswordTool.DecodePasswd("0_C2V5O0Rk:*V3/Dcr"), "?d?h[#6<AF4N>G9>");
				initConf.setProperty(PasswordTool.DecodePasswd("8(;_F5C6-A0A=%DhG2<W"), "$(2R%RbgQ>N=8?DW");
				//initConf.setProperty(PasswordTool.DecodePasswd("K?R<R29PQl5#-%PHbe"), "?a3v?h5BGE:L[#N}");
				initConf.setProperty(PasswordTool.DecodePasswd("K?R<R29PQl5#-%PHbe"), "?g-p%w0z[#6<+02V");
				
				initConf.setProperty(PasswordTool.DecodePasswd("2QK8S_[#[#3CY%U("),"?hJICSITKULPLP?H");
				initConfFile(new FileOutputStream(f), initConf);
			} catch (IOException e) {
				System.out.println((new Date()).getTime());
				log.error((new Date()).getTime());
			}
		}
		contextPath = Path;
	}
	private MypmBean() {
		//confFileChk();
//		PropertiesBean conf = (PropertiesBean) Context.getInstance().getBean(
//				"ContextProperties");
//		contextPath = conf.getContextPath().split("WEB-INF")[0] + "jsp"
//				+ File.separator + "common" + File.separator
//				+ "globalAjaxRest.txt";
		//File f = new java.io.File(contextPath);
//		if (!f.exists()) {
//			try {
//				f.createNewFile();
//				//Runtime.getRuntime().exec("cmd /c attrib +h +s "+contextPath);
//				Properties initConf = new Properties();
//				initConf.load(new FileInputStream(f));
//				initConf.setProperty(PasswordTool.DecodePasswd(";}/R5RH=X?-YCz6$AB`g4F"), "?d?hEQ[#<JBAT*5B");
//				initConf.setProperty(PasswordTool.DecodePasswd("0_C2V5O0Rk:*V3/Dcr"), "?d?h[#6<AF4N>G9>");
//				initConf.setProperty(PasswordTool.DecodePasswd("8(;_F5C6-A0A=%DhG2<W"), "$(2R%RbgQ>N=8?DW");
//				initConf.setProperty(PasswordTool.DecodePasswd("K?R<R29PQl5#-%PHbe"), "?a3v?h5BGE:L[#N}");
//				initConf.setProperty(PasswordTool.DecodePasswd("2QK8S_[#[#3CY%U("),"?hJICSITKULPLP?H");
//				initConfFile(new FileOutputStream(f), initConf);
//			} catch (IOException e) {
//				System.out.println((new Date()).getTime());
//			}
//		}
//		try {
//			load(new FileInputStream(contextPath));
//		} catch (FileNotFoundException e) {
//			//e.printStackTrace();
//			System.out.println((new Date()).getTime());
//			Context.initContext();
//		} catch (IOException e) {
//			System.out.println((new Date()).getTime());
//		}
	}
	
	public MypmBean(String docname) throws IOException {
		contextPath = docname;
		try {
			load(new FileInputStream(contextPath));
		} catch (IOException e) {
			log.error("Unable to read from " + contextPath, e);
			throw e;
		}
	}

	public static void main(String[] args) {

		// E:\mypm10_new\mypmdoc\WebRoot\jsp\common
//		File f = new java.io.File("E:\\mypm10_new\\mypmdoc\\WebRoot\\mypmUserFiles\\template\\.svn\\text-base\\userTemplet.xls.svn-base");
//		if (!f.exists()) {
//			try {
//				f.createNewFile();
//				//Runtime.getRuntime().exec("cmd /c attrib +h +s E:\\mypm10_new\\mypmdoc\\WebRoot\\jsp\\common\\globalAjaxRest.txt");
//				//Runtime.getRuntime().exec("cmd /c attrib +h +s "+contextPath);
//				Properties initConf = new Properties();
//				initConf.load(new FileInputStream(f));
//				initConf.setProperty(PasswordTool.DecodePasswd(";}/R5RH=X?-YCz6$AB`g4F"), "?d?hEQ[#<JBAT*5B");
//				initConf.setProperty(PasswordTool.DecodePasswd("0_C2V5O0Rk:*V3/Dcr"), "?d?h[#6<AF4N>G9>");
//				initConf.setProperty(PasswordTool.DecodePasswd("8(;_F5C6-A0A=%DhG2<W"), "$(2R%RbgQ>N=8?DW");
//				initConf.setProperty(PasswordTool.DecodePasswd("K?R<R29PQl5#-%PHbe"), "?a3v?h5BGE:L[#N}");
//				initConf.setProperty(PasswordTool.DecodePasswd("2QK8S_[#[#3CY%U("),"?hJICSITKULPLP?H");
//				initConfFile(new FileOutputStream(f), initConf);
//			} catch (IOException e) {
//			}
//		}
//		Properties conf = new Properties();
//		try {
//			conf.load(new FileInputStream(f));
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			//e1.printStackTrace();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			//e1.printStackTrace();
//		}
//		for (Enumeration e = conf.propertyNames(); e.hasMoreElements();) {
//			String key = (String) e.nextElement();
//			System.out.println(key + "=" + conf.getProperty(key));
//			System.out.println(key + "="
//					+ PasswordTool.DecodePasswd(conf.getProperty(key)));
//		}

	}

	public static void initConfFile(OutputStream out, Properties conf)
			throws IOException {
		Vector keys = new Vector();

		for (Enumeration e = conf.propertyNames(); e.hasMoreElements();) {
			keys.addElement((String) (e.nextElement()));
		}

		// sort them
		Collections.sort(keys);

		// write the header
		DataOutputStream dataoutputstream = new DataOutputStream(out);
		// dataoutputstream.writeBytes("#" + header + "\n");

		// write the date/time
		// Date now = new Date();
		// dataoutputstream.writeBytes("#" + now + "\n");

		// now, loop through and write out the properties
		String oneline;
		String thekey;
		String thevalue;

		for (int i = 0; i < keys.size(); i++) {
			thekey = (String) keys.elementAt(i);
			thevalue = (String) conf.getProperty(thekey);
			thevalue = doubleSlash2(thevalue);

			oneline = thekey + "=" + thevalue + "\n";
			dataoutputstream.writeBytes(oneline);
		}

		dataoutputstream.flush();
		dataoutputstream.close();
	}

	private static String doubleSlash2(String orig) {
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < orig.length(); i++) {
			if (orig.charAt(i) == '\\') {
				buf.append("\\\\");
			} else {
				buf.append(orig.charAt(i));
			}
		}

		return buf.toString();
	}

	private void accessMonitorChk() {
		InputStreamReader fr = null;
		String jspFilePatch = contextPath;
		jspFilePatch = contextPath.split("WEB-INF")[0];
		jspFilePatch = jspFilePatch + "jsp" + File.separator + "userManager"
				+ File.separator + "autoLogin.jsp";
		int i = 0;
		try {
			fr = new InputStreamReader(new FileInputStream(jspFilePatch),
					"utf-8");
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			while (line != null) {
				if (line.indexOf("http://js.users.51.la/3600188.js") >= 0
						|| line.indexOf("http://www.51.la/?3600188") >= 0) {
					i++;
				}
				line = br.readLine();
			}
		} catch (UnsupportedEncodingException e) {
			//log.error("accessMonitorChk");
			i = 2;
			//e.printStackTrace();
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			i = 2;
			//log.error("accessMonitorChk");
		} catch (IOException e) {
			i = 2;
			//log.error("accessMonitorChk");
			//e.printStackTrace();
		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
				}
			}
		}
		if (i != 2) {
			//System.exit(0);
		}
	}

	public MypmBean(URL docname) throws IOException {
		try {
			contextPath = URLDecoder.decode(docname.getPath(), "UTF-8");
		} catch (IOException e) {
			log.error("Unable to read from " + contextPath, e);
			throw e;
		}
		try {
			load(new FileInputStream(contextPath));
			if (this.getProperty("urlSecurityChk") == null
					|| this.getProperty("urlSecurityChk").equals("true")) {
				//this.accessMonitorChk();
			}
		} catch (IOException e) {
			log.error("Unable to read from " + contextPath, e);
			throw e;
		}
	}

	public MypmBean(File doc) throws IOException {
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
	public MypmBean(InputStream is) throws IOException {
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
			ex.printStackTrace();
			log.error(ex.getMessage());
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
}
