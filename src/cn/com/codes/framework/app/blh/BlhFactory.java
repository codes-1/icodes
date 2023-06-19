package cn.com.codes.framework.app.blh;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.com.codes.framework.common.ClassUtil;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.app.blh.BaseBizLogicHandler;
import cn.com.codes.framework.app.blh.BlhFactory;

public class BlhFactory {

	private static Logger logger = Logger.getLogger(BlhFactory.class);
	private static Map<String, BaseBizLogicHandler> blhRegisterManager = new HashMap<String, BaseBizLogicHandler>();
	private static WebApplicationContext wac ;
	
	public static BaseBizLogicHandler getBlh(ServletContext sc ,String blhBeanId){
		return (BaseBizLogicHandler)getWac(sc).getBean(blhBeanId);
	}
	public static BaseBizLogicHandler getBlh(ServletContext sc,
			Class actionClass) throws BaseException { 

		//StopWatch clock = new StopWatch();
		//clock.start(); // 计时开始
		BaseBizLogicHandler blh = blhRegisterManager.get(actionClass.getName());
		if (blh == null) {
			Class blhClass = ClassUtil.getSuperClassGenricType(actionClass);
			if (blhClass == actionClass) {
				String blhBeanId = actionClass.getSimpleName();
				try {
					blhBeanId = getDefaultBlhIdByAction(actionClass.getSimpleName());
					blh = (BaseBizLogicHandler)getWac(sc).getBean(blhBeanId);
				} catch (StringIndexOutOfBoundsException e) {
					logger.warn("action name isn't a standard name");
				}catch(NoSuchBeanDefinitionException e){
					logger.error(e);
				}
				if(blh == null){
					throw new BaseException(
							"程序配置错误:Please inject BLH for "
									+ actionClass.getName()
									+ " via autowire=\"byName\" and implements getHlh() method to get the injected blh or blh Bean's id naming by default",
							true);
				}

			}else{
				String[] blhName = getWac(sc).getBeanNamesForType(blhClass, false, true);
				blh = (BaseBizLogicHandler) getWac(sc).getBean(blhName[0], blhClass);			
			}
			blhRegisterManager.put(actionClass.getName(), blh);	
		}
		//clock.stop(); // 计时结束
		//logger.info(" Get blh for ["+ actionClass.getName() +"] Takes:" + clock.getTime() + " ms");	
		return blh;
	}
	
	private  static String LowerFirstChar(String string) {
		if (string == null){
			return null;
		}
		if (string.length() <= 1){
			return string.toLowerCase();
		}
		StringBuffer sb = new StringBuffer(string);
		sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
		return sb.toString();
	}
	
	public static String getDefaultBlhIdByAction(String actionClassSimpleName) throws StringIndexOutOfBoundsException{
		
		return LowerFirstChar(actionClassSimpleName.substring(0,actionClassSimpleName.lastIndexOf("Action")))+"Blh";
	}
	
	public static WebApplicationContext getWac(ServletContext sc){
		if(wac == null){
			wac = WebApplicationContextUtils
			.getWebApplicationContext(sc);
		}
		return wac ;
	}
}