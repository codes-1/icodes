package cn.com.codes.framework.jdbc;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import cn.com.codes.common.util.CalendaUtilities;


@SuppressWarnings("rawtypes")
public class ObjectRowMapper implements RowMapper {

	private String dbType;
	private Class<?> objectClass;
	private String[] columnNames = null;
	private Field[] fields ;
	private Map<String ,Field> currQueryFieldMap ;
	private Map<String ,String> fieldClassMap ;
	private Boolean isConvertSwitch  = null;
	private String mysqlLowerCaseTableNames = null;
	
	private static Log logger = LogFactory.getLog(ObjectRowMapper.class);
	//private static Map<String, Map<String, String>> classFieldDbColumnMap = new ConcurrentHashMap<String, Map<String, String>>();
	private static Map<String, Map<String, String>> dbColumnClassFieldMap = new ConcurrentHashMap<String, Map<String, String>>();
	
	public ObjectRowMapper(Class<?> objectClass) {
		this.objectClass = objectClass;
		fields = objectClass.getDeclaredFields();
	}

	public void clean(){
		if(currQueryFieldMap!=null){
			currQueryFieldMap.clear();
			currQueryFieldMap = null;
		}
		if(fieldClassMap!=null){
			fieldClassMap.clear();
			fieldClassMap = null;
		}
		if(fields!=null){
			fields = null;
		}
		if(columnNames!=null){
			columnNames = null;
		}
	}
	/**
	 * 该方法自动将数据库字段对应到Object中相应字段 要求：字段名严格为驼峰形式 == 数据库字段名去掉下划线转为驼峰形式
	 * 
	 */
	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

		Object targetObject = null;
		try {
			targetObject = objectClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		}
		if (columnNames == null) {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			columnNames = new String[columnCount];
			for (int i = 0; i < columnCount; i++) {
				columnNames[i] = rsmd.getColumnLabel(i + 1);
			}
		}
		isConvertSwitch = true;
//		if(isConvertSwitch==null){
//			String cusRowMapper = PropertiesBean.getInstance().getProperty(
//					"conf.jdbc.rowMapper");
//			if(cusRowMapper!=null&&"custom".equals(cusRowMapper)){
//				isConvertSwitch = true;
//			}
//		}
//		if(mysqlLowerCaseTableNames == null){
//			String lowerCaseNames = PropertiesBean.getInstance().getProperty("conf.mysql.lowerCaseNames");
//			if(lowerCaseNames==null){
//				mysqlLowerCaseTableNames = "yes";
//			}else{
//				mysqlLowerCaseTableNames = "no";
//			}
//		}
		if(currQueryFieldMap==null){
			currQueryFieldMap = new HashMap<String,Field>(columnNames.length);
			for (String columnName : columnNames) {
				for (Field field : fields) {
					if(isConvertSwitch==null){
						if (field.getName().equals(
								convertColumnNameToFieldName(columnName))) {
							currQueryFieldMap.put(columnName, field);
							break;
						}
					}else{
						if(isConvertSwitch){
							if(targetObject instanceof  CustomRowMapper&&(!((CustomRowMapper)targetObject).isConvert())){
								if (field.getName().equals(columnName)) {
									currQueryFieldMap.put(columnName, field);
									break;
								}
							}else{
								if (field.getName().equals(
										convertColumnNameToFieldName(columnName))) {
									currQueryFieldMap.put(columnName, field);
									break;
								}
							}
						}

					}

				}
			}
		}
		for (String columnName : columnNames) {
			Field field = currQueryFieldMap.get(columnName);
			if(field==null){
				if(logger.isDebugEnabled()){
					logger.debug(objectClass.getName() +"is  not property match  db columnName:"+columnName );
				}
				continue;
			}
			Object value = rs.getObject(columnName);
			if (value == null) {
				continue;
			}
			boolean accessFlag = field.isAccessible();
			if (!accessFlag) {
				field.setAccessible(true);
			}
			if(fieldClassMap==null){
				fieldClassMap = new HashMap<String,String>(columnNames.length);
			}
			if(fieldClassMap.get(columnName)==null){
				fieldClassMap.put(columnName, getFieldClaszName(field));
			}
			setFieldValue(targetObject, field, rs, columnName,fieldClassMap.get(columnName));
			// 恢复相应field的权限
			if (!accessFlag) {
				field.setAccessible(accessFlag);
			}
		}
		return targetObject;
	}


	public String convertColumnNameToFieldName(String columnName) {

		Map<String, String> fieldMap = dbColumnClassFieldMap.get(objectClass
				.getName());
		boolean emptyFlg = false;
		if (fieldMap == null) {
			fieldMap = new HashMap<String, String>();
			emptyFlg = true;
		}

		String classFieldName = fieldMap.get(columnName);
		if (classFieldName != null) {
			return classFieldName;
		}
		String columnNameKey = columnName;

		//if ("oracle".equals(dbType)||("mysql".equals(dbType)&&"no".equals(mysqlLowerCaseTableNames))) {
			columnName = columnName.toLowerCase();
		//}

		StringBuffer buf = new StringBuffer();
		int i = 0;
		while ((i = columnName.indexOf('_')) > 0) {
			buf.append(columnName.substring(0, i));
			columnName = StringUtils.capitalize(columnName.substring(i + 1));
		}
		buf.append(columnName);
		fieldMap.put(columnNameKey, buf.toString());
		if (emptyFlg) {
			dbColumnClassFieldMap.put(objectClass.getName(), fieldMap);
		}
		return fieldMap.get(columnNameKey);
	}

	/**
	 * 根据类型对具体对象属性赋值,先调用这个方法再调用把VALUE to String后的方法是为了提高性能
	 */
	public static void setFieldValue(Object targetObj, Field field,
			ResultSet rs, String columnLabel,String fieldClass) {

		try {
			if ("String".equals(fieldClass)) {
				field.set(targetObj, rs.getString(columnLabel));
			} else if ("Double".equals(fieldClass)) {
				field.set(targetObj, rs.getDouble(columnLabel));
			} else if ("Float".equals(fieldClass)) {
				field.set(targetObj, rs.getFloat(columnLabel));
			} else if ("Integer".equals(fieldClass)) {
				field.set(targetObj, rs.getInt(columnLabel));
			} else if ("Long".equals(fieldClass)) {
				field.set(targetObj, rs.getLong(columnLabel));
			} else if ("BigDecimal".equals(fieldClass)) {
				field.set(targetObj, rs.getBigDecimal(columnLabel));
			} else if ("Date".equals(fieldClass)) {
				field.set(targetObj, rs.getDate(columnLabel));
			} else if ("Short".equals(fieldClass)) {
				field.set(targetObj, rs.getShort(columnLabel));
			} else if ("Boolean".equals(fieldClass)) {
				field.set(targetObj, rs.getBoolean(columnLabel));
			} else if ("Byte".equals(fieldClass)) {
				field.set(targetObj, rs.getByte(columnLabel));
			} else if ("Timestamp".equals(fieldClass)) {
				field.set(targetObj, rs.getTimestamp(columnLabel));
			} else if("BigDecimal".equals(fieldClass)) {
				field.set(targetObj, rs.getBigDecimal(columnLabel));
			}else {
				setFieldValue(targetObj, field, rs.getString(columnLabel));
			}
			
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private static String getFieldClaszName(Field field) {

		String elemType = field.getType().toString();
		if ("class java.lang.String".equals(elemType)
				|| elemType.indexOf("char") != -1
				|| elemType.indexOf("Character") != -1) {
			return "String";
		} else if (elemType.indexOf("double") != -1
				|| elemType.indexOf("Double") != -1) {
			return "Double"; 
		} else if (elemType.indexOf("float") != -1
				|| elemType.indexOf("Float") != -1) {
			return "Float"; 
		} else if (elemType.indexOf("int") != -1
				|| elemType.indexOf("Integer") != -1||elemType.indexOf("BigInteger") != -1) {
			return "Integer"; 
		} else if (elemType.indexOf("long") != -1
				|| elemType.indexOf("Long") != -1) {
			return "Long"; 
		} else if (elemType.indexOf("BigDecimal") != -1) {
			return "BigDecimal"; 
		} else if (elemType.indexOf("Date") != -1) {
			return "Date"; 
		} else if (elemType.indexOf("short") != -1
				|| elemType.indexOf("Short") != -1) {
			return "Short"; 
		} else if (elemType.indexOf("boolean") != -1
				|| elemType.indexOf("Boolean") != -1) {
			return "Boolean"; 
		} else if (elemType.indexOf("byte") != -1
				|| elemType.indexOf("Byte") != -1) {
			return "Byte"; 
		}  else if (elemType.indexOf("Timestamp") != -1) {
			return "Timestamp"; 
		}
		
		return "String";

	}

	/**
	 * 根据类型对具体对象属性赋值
	 */
	public static void setFieldValue(Object targetObj, Field field, String value) {

		String elemType = field.getType().toString();
		if (elemType.indexOf("char") != -1
				|| elemType.indexOf("Character") != -1) {
			try {
				field.set(targetObj, Character.valueOf(value.charAt(0)));
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			}
		} else if (elemType.indexOf("double") != -1
				|| elemType.indexOf("Double") != -1) {
			try {
				field.set(targetObj, Double.valueOf(value));
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			}
		} else if (elemType.indexOf("float") != -1
				|| elemType.indexOf("Float") != -1) {
			try {
				field.set(targetObj, Float.valueOf(value));
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			}
		} else if (elemType.indexOf("int") != -1
				|| elemType.indexOf("Integer") != -1) {
			try {
				field.set(targetObj, Integer.valueOf(value));
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			}
		} else if (elemType.indexOf("long") != -1
				|| elemType.indexOf("Long") != -1) {
			try {
				field.set(targetObj, Long.valueOf(value));
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			}
		} else if (elemType.indexOf("Date") != -1) {
			try {
				if (null != value) {
					if (value.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
						field.set(targetObj, CalendaUtilities.strToDate(value,
								CalendaUtilities.shortDate));
					} else if (value
							.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")) {
						field.set(targetObj, CalendaUtilities.strToDate(value,
								CalendaUtilities.longDate));
					} else if (value
							.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d+$")) {
						field.set(targetObj, CalendaUtilities.strToDate(value,
								CalendaUtilities.longDate));
					} else {
						field.set(targetObj, CalendaUtilities.strToDate(value,
								CalendaUtilities.longDate));
					}
				}
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			}
		} else if (elemType.indexOf("short") != -1
				|| elemType.indexOf("Short") != -1) {
			try {
				field.set(targetObj, Short.valueOf(value));
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			}
		} else if (elemType.indexOf("boolean") != -1
				|| elemType.indexOf("Boolean") != -1) {
			try {
				field.set(targetObj, Boolean.valueOf(value));
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			}
		} else if (elemType.indexOf("byte") != -1
				|| elemType.indexOf("Byte") != -1) {
			try {
				field.set(targetObj, Byte.valueOf(value));
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			}
		} else {
			try {
				field.set(targetObj, (Object) value);
			} catch (IllegalAccessException | IllegalArgumentException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

}