package cn.com.codes.framework.mock;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.services.BaseServiceImpl;
import cn.com.codes.framework.common.ResourceUtils;
import cn.com.codes.framework.transmission.dto.BaseDto;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.mock.ServiceLocator;

public class ServiceLocator {

	private static final Map MapBeanFactory = new HashMap();

	private static final Map BeanMap = new HashMap();

	private static ServiceLocator SerLor = null;

	private ServiceLocator() {
		SerLor = this;
	}

	public static synchronized ServiceLocator singleton() {
		if (SerLor == null) {
			SerLor = new ServiceLocator();
		}
		return SerLor;
	}

	public static BaseServiceImpl getService(String serviceName) {

		return (BaseServiceImpl) getBean(serviceName);
	}

	public static BusinessBlh getBusinessBlh(String blhName) {
		return (BusinessBlh) getBean(blhName);
	}

	public static <T> T getBean(Class<T> clasz, BusiRequestEvent req) {
		return (T) req.getDto();
	}
	public  static Object getBean(String BeanName) {

		if (BeanMap.containsKey(BeanName)) {
			 Object obj = BeanMap.get(BeanName);
			 return obj;
		} else {
			Set BeanFactorySet = MapBeanFactory.entrySet();
			Iterator BeanFactoryIt = BeanFactorySet.iterator();
			BeanFactory BF = null;
			while (BeanFactoryIt.hasNext()) {
				Map.Entry ME = (Map.Entry) BeanFactoryIt.next();
				BF = (BeanFactory) ME.getValue();
				Object Bean = null;
				try {
					Bean = BF.getBean(BeanName);
				} catch (NoSuchBeanDefinitionException e) {
					continue;
				}
				if (Bean != null) {
					BeanMap.put(BeanName, Bean);
					return Bean;
				}
			}
			return null;
		}
	}

	public synchronized static void initBeanFactory(String directory) {

		if (MapBeanFactory.get(directory) != null) {
			return;
		}
		String[] contextFiles = ResourceUtils.getSpeciDirFileNames(directory,"xml");
		BeanFactory tmpCtx = new ClassPathXmlApplicationContext(contextFiles);
		MapBeanFactory.put(directory, tmpCtx);
		cacheBean((ClassPathXmlApplicationContext)tmpCtx);

	}
	
	public static void cacheBean(ClassPathXmlApplicationContext ctx){
		String[] names = ctx.getBeanDefinitionNames();
		for(int i =0 ; i<names.length; i++){
			BeanMap.put(names[i],ctx.getBean(names[i]));
		}
	}

}
