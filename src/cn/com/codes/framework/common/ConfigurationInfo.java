package cn.com.codes.framework.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import cn.com.codes.framework.common.ConfigurationInfo;
import cn.com.codes.framework.common.ResourceUtils;

public class ConfigurationInfo {
	private static ConfigurationInfo conInfo = null;
	private static Logger logger = Logger.getLogger(ConfigurationInfo.class);
	private Properties props = null;

	private ConfigurationInfo() {
		props = new Properties();
		URL url = ResourceUtils.getFileURL("resource/spring/configure.properties");
		InputStream is = null;
		try {
			is = url.openStream();
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e);
		}finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static synchronized ConfigurationInfo getInstance() {
		if (conInfo == null) {
			conInfo = new ConfigurationInfo();
		}
		return conInfo;
	}
	
	
	public String getProperty(String proName){
		return (String)props.getProperty(proName);
	}
	
	public Map  getProperties(){
		HashMap mappingSyneFiles = new HashMap();
		String key = "";
		Enumeration en = props.propertyNames();
        while (en.hasMoreElements()) {
            key = (String) en.nextElement();
            mappingSyneFiles.put(key, props.getProperty (key));
        }
		return mappingSyneFiles;
	}
}
