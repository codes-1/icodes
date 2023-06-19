package cn.com.codes.framework.common;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;

import cn.com.codes.framework.common.PropertyUtil;


public class PropertyUtil {

	private static Logger logger = Logger.getLogger(PropertyUtil.class);
	public static void setProperty(Object obj,String propertyName,Object value)
	{
		if(BeanUtilsBean.getInstance().getPropertyUtils().isWriteable(obj, propertyName))
		{
			 try{
				 BeanUtilsBean.getInstance().copyProperty(obj,propertyName,value);
			 }catch(Exception e)
			 {
				 String err = "Conn't set "+obj.getClass().getName()+"' "+propertyName+" value";
				 logger.error(err,e);
			 }
		}
	}
	public static boolean isWriteable(Object obj,String propertyName)
	{
		if(BeanUtilsBean.getInstance().getPropertyUtils().isWriteable(obj, propertyName))
		{
			return true;
		}
		return false;
	}
	public static Object getProperty(Object obj,String propertyName)
	{
		Object value = null;
		try{
			value = BeanUtilsBean.getInstance().getPropertyUtils().getProperty(obj,propertyName);
		}catch(Exception e)
		{
			//logger.error(obj,e);
		}
		return value;
		
	}
	

}
