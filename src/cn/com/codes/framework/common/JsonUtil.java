package cn.com.codes.framework.common;

import java.text.SimpleDateFormat;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import cn.com.codes.framework.exception.DataBaseException;

public class JsonUtil {
	
	
	
	private static ObjectMapper mapper = new ObjectMapper();
	static {
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}
	
	
	/**
	 * 对象转化为Json串
	 * @param obj 要转化成Json串的对象
	 * @return obj 的json 表式的字符串
	 */
	public static String toJson(Object obj) {
		String json = null;
		try {
			json = mapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new DataBaseException(e.getMessage(), e);
		}
		return json;
	}

	
	/**
	 * json串转化为指定类型的对象
	 * @param str          json串
	 * @param objClass       要转化为的对象的类型
	 * @return 返回转化的对象
	 */
	public static <T> T toObject(String str, Class<T> objClass) {

		T obj = null;
		try {
			obj = mapper.readValue(str, objClass);
		} catch (Exception e) {
			throw new DataBaseException(e.getMessage(), e);
		}
		return obj;
	}
	
	/**
	 * json串转化为指定类型且带有泛型的对象
	 * @param str json串
	 * @param objClass  要转化为的泛型对象的类型 ,如new TypeReference<PagerModel<ListObject>>() { }一个这样的参数
	 * @return 返回转化的对象
	 */
	@SuppressWarnings({"rawtypes" })
	public static <T> T toObject(String str, TypeReference valueTypeRef) {

		T obj = null;
		try {
			obj = mapper.readValue(str, valueTypeRef);
		} catch (Exception e) {
			throw new DataBaseException(e.getMessage(), e);
		}
		return obj;
	}
}
