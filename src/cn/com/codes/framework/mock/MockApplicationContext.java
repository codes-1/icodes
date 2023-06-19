package cn.com.codes.framework.mock;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.services.BaseServiceImpl;
import cn.com.codes.framework.common.ResourceUtils;
import cn.com.codes.framework.mock.MockApplicationContext;
import cn.com.codes.framework.mock.ServiceLocator;

public class MockApplicationContext {

	// 下面是全局设置的缺省配置，可根据需要增加
	public static final String BOOTSTRAP = "mockBootStrap.properties";

	public static final String SpringConfig_Dir = "spring.cfg.file.dir";
	private static String SpringSupport = null;

	private static boolean initialized = false;

	private static Map contextPool = null;

	private ServiceLocator sl = null;

	private static MockApplicationContext applicationContext = null;

	private MockApplicationContext() {
		init();
		applicationContext = this;
		sl = ServiceLocator.singleton();

	}

	private synchronized void init() {
		if (initialized) {
			return;
		}
		contextPool = new HashMap();
		loadProperties();
		initSpringBeanFactory();
		setInitialized(true);
	}

	private void initSpringBeanFactory() {

		String spring_cfg_file = (String) contextPool.get(SpringConfig_Dir);
		if (spring_cfg_file == null) {
			spring_cfg_file = "";
		}
		for (StringTokenizer stConfig = new StringTokenizer(spring_cfg_file,
				","); stConfig.hasMoreElements();) {
			String config = ((String) stConfig.nextElement()).trim();
			String configDirectoryKey = (String) contextPool.get(config);
			if (null == configDirectoryKey) {
				// 作异常处理，暂不实现
			} else {
				StringTokenizer stConfigDirectory = new StringTokenizer(
						configDirectoryKey, ",");

				for (; stConfigDirectory.hasMoreElements(); ServiceLocator
						.initBeanFactory(((String) stConfigDirectory
								.nextElement()).trim()))
					;
			}
		}
	}

	private void loadProperties() {
		Properties props = getProperties();
		SpringSupport = (String) props.getProperty("SpringSupport");
		for (Iterator iter = props.keySet().iterator(); iter != null
				&& iter.hasNext();) {
			String key = (String) iter.next();
			String value = props.getProperty(key);
			synchronized (contextPool) {
				contextPool.put(key, value);
			}
		}

	}

	private Properties getProperties() {
		Properties props;
		InputStream is;
		props = new Properties();
		is = null;
		URL url = ResourceUtils.getFileURL("mockBootStrap.properties");
		try {
			is = url.openStream();
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return props;
	}

	public static synchronized MockApplicationContext singleton() {
		if (applicationContext == null) {
			applicationContext = new MockApplicationContext();
		}
		return applicationContext;
	}

	private boolean containsKey(Object key) {
		return contextPool.containsKey(key);
	}

	private boolean containsValue(Object value) {
		return contextPool.containsValue(value);
	}

	public void clear() {
		contextPool.clear();
		initialized = false;
	}

	public Object getValue(String key) {
		return contextPool.get(key);
	}

	public String getValueAsString(String key) {
		return (String) getValue(key);
	}

	public boolean isInitialized() {
		return initialized;
	}

	private void setInitialized(boolean flag) {
		initialized = flag;
	}

	public void reLoadConfig() {
		clear();
		init();
	}



	public BaseServiceImpl getService(String serviceName) {

		return sl.getService(serviceName);
	}
	public BusinessBlh getBusinessBlh(String blhName) {
		return sl.getBusinessBlh(blhName);
	}
	public Object getBean(String BeanName) {
		return sl.getBean(BeanName);
	}

	public static void main(String[] args){
		
		MockApplicationContext ma = MockApplicationContext.singleton();
		//ApplicationContext context = new ClassPathXmlApplicationContext("/resource/spring/applicationContext-mail.xml");
		
	}

}
