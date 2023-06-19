package cn.com.codes.framework.common;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.com.codes.framework.common.ClassUtil;


public class ClassUtil {

	private static final Log log = LogFactory.getLog(ClassUtil.class); 
	
	private ClassUtil(){
		
	}
	
	public static boolean isInterface(Class clasz, Class interfacz) {
		boolean bResult = false;

		Class[] itfaces = clasz.getInterfaces();
		for (int i = 0; i < itfaces.length; i++) {

			if (itfaces[i].equals(interfacz)) {
				bResult = true;
				break;
			}
		}
		return bResult;
	}

	
	public static Object newInstance(String className) throws Exception {

		Class clasz = Class.forName(className);
		return clasz.newInstance();

	}

	public static Object invoke(Object clasz, String methodName, Object[] params)
			throws Exception {
		Class[] claszParam = null;
		if (params != null && params.length > 0) {
			claszParam = new Class[params.length];
			for (int i = 0; i < params.length; i++) {

				claszParam[i] = params[i].getClass();

			}
		}
		Method method = clasz.getClass().getDeclaredMethod(methodName,
				claszParam);

		return method.invoke(clasz, params);
	}

	public static void clone(Object obj, Object targetObj) {
		try {
			BeanUtilsBean.getInstance().getPropertyUtils().copyProperties(
					targetObj, obj);
		} catch (Exception e) {
		}
	}

	/**
	 * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends GenricManager<Book>
	 *
	 * @param clazz The class to introspect
	 * @return the first generic declaration, or <code>Object.class</code> if cannot be determined
	 */
	public static Class getSuperClassGenricType(Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends GenricManager<Book>
	 *
	 * @param clazz clazz The class to introspect
	 * @param index the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or <code>Object.class</code> if cannot be determined
	 * 如父没有参数化，则返回类本身的class
	 */
	public static Class getSuperClassGenricType(Class clazz, int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			log.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return clazz;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			log.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return clazz;
		}
		if (!(params[index] instanceof Class)) {
			log.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return clazz;
		}
		return (Class) params[index];
	}

}
