package cn.com.codes.framework.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.log4j.Logger;

import cn.com.codes.framework.common.PropertyReader;

public class PropertyReader {
	private Properties prop = new Properties();

	private static PropertyReader validateReader;

	private static final Logger logger = Logger.getLogger(PropertyReader.class);

	private PropertyReader() {

		try {
			prop.load(this.getClass().getResourceAsStream("/ambow.properties"));
		} catch (FileNotFoundException e) {

			logger.error(e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		}

	}

	public static PropertyReader getInstance() {
		if (validateReader == null) {
			validateReader = new PropertyReader();
		}
		return validateReader;
	}

	public String getProperty(String propertyName) {

		String propertyValue = this.prop.getProperty(propertyName);
		if (propertyValue == null) {
			logger.error("====Property:" + propertyName
					+ " don't be defined in ======ambow.properties");
			return null;
		}
		try {
			propertyValue = new String(propertyValue.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return propertyValue;
	}

	public static void main(String[] args) {
		PropertyReader aa = PropertyReader.getInstance();
		System.out.println("size:" + aa.prop.size());

		System.out.println("value:" + aa.getProperty("test2"));
	}

}
