package cn.com.codes.framework.common;

import java.util.HashMap;
import java.util.Map;

import cn.com.codes.framework.common.ListObject;

public class Global {

	/**
	 * session中存放用户的变量
	 */
	public final static String USER_IN_SESSION = "user";

	/**
	 * 存验证码变量
	 */
	public final static String VALID_CODE = "validCode";

	/**
	 * 存放session中visitor对像的变量
	 */
	public final static String VISITOR = "currentUser";

	public final static String isAdmin = "1";

	public final static String AUTHENTICATION = "currentAuthenticated";

	/**
	 *  公司类型
	 */
	private static Map<String, ListObject> CompamyTypeMap = new HashMap<String, ListObject>(4);
	
	/**
	 * 公司规模
	 */
	private static Map<String, ListObject> CompamySizeMap = new HashMap<String, ListObject>(8);
	
	//公司使用系统装态
	private static Map<String, ListObject> CompamyStatusMap = new HashMap<String, ListObject>(5);
	
	// -1 禁用 0 试用中 1 正常使用中 2 到期未续费 可用 3 到期未续费 禁用
	static {
		CompamyTypeMap.put("1", new ListObject("1", "互联网"));
		CompamyTypeMap.put("2", new ListObject("2", "计算机软件"));
		CompamyTypeMap.put("3", new ListObject("3", "系统集成"));
		CompamyTypeMap.put("4", new ListObject("4", "其它"));

		CompamySizeMap.put("1", new ListObject("1", "1-20人"));
		CompamySizeMap.put("2", new ListObject("2", "21～50人"));
		CompamySizeMap.put("3", new ListObject("3", "51～100人"));
		CompamySizeMap.put("4", new ListObject("4", "101～200人"));
		CompamySizeMap.put("5", new ListObject("5", "200～300人"));
		CompamySizeMap.put("6", new ListObject("6", "301～500人"));
		CompamySizeMap.put("7", new ListObject("7", "500～1000人"));
		CompamySizeMap.put("8", new ListObject("8", "1000人以上"));
		
		
		CompamyStatusMap.put("-1", new ListObject("-1", "禁用"));
		CompamyStatusMap.put("0", new ListObject("0", "试用中"));
		CompamyStatusMap.put("1", new ListObject("1", "正常使用中"));
		CompamyStatusMap.put("2", new ListObject("2", "到期未续费 可用"));
		CompamyStatusMap.put("3", new ListObject("3", "到期未续费 禁用"));
	}
	public static String upPath = "/mypmUserFiles";
	public static String getCompTypeByValue(String value) {

		return CompamyTypeMap.get(value).getValueObj();
	}

	public static String getCompSizeByValue(String value) {

		return CompamySizeMap.get(value).getValueObj();
	}

	public static String getCompStatusByValue(String value) {

		return CompamyStatusMap.get(value).getValueObj();
	}
	public static Map<String, ListObject> getCompamyTypeMap() {
		return CompamyTypeMap;
	}

	public static void setCompamyTypeMap(Map<String, ListObject> compamyTypeMap) {
		CompamyTypeMap = compamyTypeMap;
	}

	public static Map<String, ListObject> getCompamySizeMap() {
		return CompamySizeMap;
	}

	public static void setCompamySizeMap(Map<String, ListObject> compamySizeMap) {
		CompamySizeMap = compamySizeMap;
	}

	public static Map<String, ListObject> getCompamyStatusMap() {
		return CompamyStatusMap;
	}

	public static void setCompamyStatusMap(Map<String, ListObject> compamyStatusMap) {
		CompamyStatusMap = compamyStatusMap;
	}
}
