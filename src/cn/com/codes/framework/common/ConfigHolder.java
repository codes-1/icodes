package cn.com.codes.framework.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import cn.com.codes.framework.common.ConfigHolder;
import cn.com.codes.framework.common.ResourceUtils;

public class ConfigHolder {
	private static Logger logger = Logger.getLogger(ConfigHolder.class);
	private Properties props = null;
	private static Map<String,ConfigHolder> holder = new HashMap<String,ConfigHolder>(1);

	private ConfigHolder(String fileName) {

		props = new Properties();
		URL url = ResourceUtils.getFileURL(fileName);
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
					logger.error(e);
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 如用缺省Configure.properties里的配置，fileName为null既可
	 * @param fileName
	 * @return
	 */
	public static  ConfigHolder getInstance(String fileName) {
		if(fileName==null||"".equals(fileName.trim())){
			fileName = "Configure.properties" ;
		}
		ConfigHolder config = holder.get(fileName);
		if (config == null) {
			config = new ConfigHolder(fileName);
			holder.put(fileName, config);
		}
		return config;
	}
	
	public String getProperty(String proName){
		return (String)props.getProperty(proName);
	}
	
}
