package cn.com.codes.framework.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import cn.com.codes.framework.common.Resources;


public class Resources {

	private static Resources instance = null;

	private static HashMap resMessage = null;


	private Resources() {
		resMessage = ReadResource();
	}

	public static Resources getInstance() {
		if (instance == null) {
			instance = new Resources();
		}
		return instance;
	}

	
	public String getString(String key) {
		return (String) resMessage.get(key);
	}

	private HashMap ReadResource() {

		HashMap map = new HashMap();
		try {
			if (getClass().getResource("/ambow.properties") == null) {
				return null;
			}
			String fileName = getClass().getResource("/ambow.properties")
					.toString();
			fileName = fileName.substring(6, fileName.length());
			InputStreamReader read = new InputStreamReader(new FileInputStream(
					fileName), "UTF-8");
			BufferedReader reader = new BufferedReader(read);
			String line;
			while ((line = reader.readLine()) != null) {
				if (!(line.trim()).startsWith("#") && !"".equals(line)) {
					String[] mes = (line.trim()).split("=");
					map.put(mes[0], mes[1]);
				}

			}
			read.close();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	public static void main(String[] args) {
		

		System.out.println("value:" + Resources.getInstance().getString("test2"));
	}

}
