package cn.com.codes.framework.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.Assert;

import cn.com.codes.common.dto.PageModel;
import cn.com.codes.framework.exception.DataBaseException;


public class JdbcTemplateWrapper  {

	private static Log logger = LogFactory.getLog(JdbcTemplateWrapper.class);
	
	private JdbcTemplate jdbcTemplate;
	
	private String dbType = "mysql";

	private String showSql = "false";

	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	private String mysqlLowerCaseTableNames = null;


	public JdbcTemplateWrapper() {
		super();
	}

	public JdbcTemplateWrapper(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
	}

	public JdbcTemplateWrapper(JdbcTemplate jdbcTemplate, String dbType, String showSql) {
		super();
		this.jdbcTemplate = jdbcTemplate;
		this.dbType = dbType;
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		this.showSql = showSql;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	/**
	 * 查询所有匹配的列表
	 * 
	 * @param sql
	 *            查询sql
	 * @param className
	 *            对象类型
	 * @param args
	 *            查询参数
	 * @return
	 * @author liuyg
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List queryAllMatchList(String sql, Class clasz, Object[] args) {
		Assert.notNull(clasz, "clasz must not be null");
		List dataList = null;
		RowMapper rowMapper = new ObjectRowMapper(clasz);
		dataList = getJdbcTemplate().query(sql, args, rowMapper);
		((ObjectRowMapper) rowMapper).clean();
		rowMapper = null;
		return dataList;
	}

	/**
	 * 查询所有匹配的列表
	 * 
	 * @param sql
	 *            查询sql
	 * @param className
	 *            对象类型
	 * @param args
	 *            查询参数
	 * @return
	 * @author liuyg
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List queryAllMatchListWithFreePra(String sql, Class clasz, Object... args) {
		Assert.notNull(clasz, "clasz must not be null");
		List dataList = null;
		RowMapper rowMapper = new ObjectRowMapper(clasz);
		dataList = getJdbcTemplate().query(sql, args, rowMapper);
		((ObjectRowMapper) rowMapper).clean();
		rowMapper = null;
		return dataList;
	}

	/**
	 * 把list map 原生JDBC结果集中，字段名，也就是MAP中的KEY，转换为驼峰规则的JAVA对属性名
	 * 
	 * @param resultList ：JDBC 结果集
	 * @return    把MAP中的KEY转换为转换为驼峰规则的JAVA对属性名的LIST<map<驼峰规则的JAVA对属性名形式的KEY,Object>>
	 * @author liuyg
	 */
	public void converDbColumnName2ObjectPropName(List<Map<String,Object>> resultList) {
		
		if(resultList!=null&&!resultList.isEmpty()) {
			List<Map<String,Object>> convertList=  new ArrayList<Map<String,Object>>(resultList.size());
			 //用于缓存字段名到属性名的映射，第二条记录时就不再处理字段名到属性名的转换，提升性能
			Map<String,String> ColumnNamePropNameMap = null;
			if(resultList.size()>1) {
				ColumnNamePropNameMap = new HashMap<String,String>();
			}
			for(Map<String,Object> currMap :resultList) {
				 if(currMap!=null&&!currMap.isEmpty()) {
					 Iterator<Entry<String, Object>>   currentIt =  currMap.entrySet().iterator();
					 Map tempMap = new HashMap<String,Object>();
					 convertList.add(tempMap);
					 while(currentIt.hasNext()) {
						 Map.Entry<String,Object>  me=  currentIt.next();
						 String dbColumnName = me.getKey();
						 Object value = me.getValue();
						 if(resultList.size()>1) {
							 if(ColumnNamePropNameMap.get(dbColumnName)==null) {
								 String currProName = convertColumnName2OFieldName(dbColumnName);
								 tempMap.put(currProName, value);
								 //缓存起来，第二条记录时就不再处理字段名到属性名的转换，提升性能
								 ColumnNamePropNameMap.put(dbColumnName, currProName);
							 }else {
								 tempMap.put(ColumnNamePropNameMap.get(dbColumnName), value);
							 }
						}else {
							 tempMap.put(convertColumnName2OFieldName(dbColumnName), value);
						}

						 
					 }
				 }
			}
			resultList.clear();
			for(Map<String,Object> currMap:convertList) {
				resultList.add(currMap);
			}
			convertList.clear();
			convertList = null;

		}
	}
	
	 public String convertColumnName2OFieldName(String columnName ) {
		 
//		 if(mysqlLowerCaseTableNames == null){
//			String lowerCaseNames = PropertiesBean.getInstance().getProperty("conf.mysql.lowerCaseNames");
//			if(lowerCaseNames==null){
//				mysqlLowerCaseTableNames = "yes";
//			}else{
//				mysqlLowerCaseTableNames = "no";
//			}
//		}
//		if ("oracle".equals(dbType)||("mysql".equals(dbType)&&"no".equals(mysqlLowerCaseTableNames))) {
//			columnName = columnName.toLowerCase();
//		}
		 if("true".equals(mysqlLowerCaseTableNames)) {
			 columnName = columnName.toLowerCase();
		 }
		
		StringBuffer buf = new StringBuffer();
		int i = 0;
		while ((i = columnName.indexOf('_')) > 0) {
			buf.append(columnName.substring(0, i));
			columnName = StringUtils.capitalize(columnName.substring(i + 1));
		}
		buf.append(columnName);
		return  buf.toString();
	 }
	/**
	 * 查询所有匹配的列表
	 * 
	 * @param sql
	 *            查询sql
	 * @param className
	 *            对象类型
	 * @param paramMap
	 *            查询参数
	 * @return
	 * @author liuyg
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List queryAllMatchListWithParaMap(String sql, Class clasz, Map paramMap) {
		if (paramMap != null && paramMap.isEmpty()) {
			paramMap = null;
		}
		if ("true".equals(showSql)) {
			try {
				logger.info(getSqlFromQueryData(sql, paramMap));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		List resultList = null;
		if (clasz == null) {
			resultList = namedParameterJdbcTemplate.queryForList(sql, paramMap);
		} else {
			RowMapper rowMapper = new ObjectRowMapper(clasz);
			resultList = namedParameterJdbcTemplate.query(sql, paramMap, rowMapper);
			((ObjectRowMapper) rowMapper).clean();
			rowMapper = null;
		}

		return resultList;
	}

	/**
	 * 
	 * @param pageModel:
	 * @param className
	 *            : 从查询结果集中构建出的类，如为null则pageModel的PageData为List<Map>，
	 *            不为null则pageModel的PageData为List<className>
	 * @param columnNameForCount:查询记录数时的字段名，一般用主键
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void fillPageModelData(PageModel pageModel, Class className, String columnNameForCount) {
		if (pageModel.getHqlParamMap() != null && pageModel.getHqlParamMap().isEmpty()) {
			pageModel.setHqlParamMap(null);
		}
		if (pageModel.getTotal() == 0) {
			int totalRows = this.getResultCountWithValuesMap(pageModel.getQueryHql(), columnNameForCount,
					pageModel.getHqlParamMap());
			pageModel.setTotal(totalRows);
		}
		if (pageModel.getTotal() == 0) {
			pageModel.setRows(new ArrayList());
			return;
		}
		if (pageModel.getPageNo() > 1) {
			int pageCount = this.getValidPage(pageModel.getPageNo(), pageModel.getTotal(), pageModel.getPageSize());
			if (pageCount < pageModel.getPageNo()) {
				pageModel.setPageNo(pageCount);
			}
		}

		int startRow = getStartOfPage(pageModel.getPageNo(), pageModel.getPageSize());

		String sql = this.buildPageSql(pageModel.getQueryHql(), startRow, pageModel.getPageSize());
		if ("true".equals(showSql)) {
			try {
				logger.info(getSqlFromQueryData(sql, pageModel.getHqlParamMap()));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		List dataList = null;
		if (className == null) {
			dataList = namedParameterJdbcTemplate.queryForList(sql, pageModel.getHqlParamMap());
		} else {
			RowMapper rowMapper = new ObjectRowMapper(className);
			dataList = namedParameterJdbcTemplate.query(sql, pageModel.getHqlParamMap(), rowMapper);
			((ObjectRowMapper) rowMapper).clean();
			rowMapper = null;
		}
		pageModel.setRows(dataList);
	}

	/**
	 * 多表sql 分页查询,多表连查时，才用这个方法，其他请用commonDao的 SQL分页查询
	 * 
	 * @param sql
	 * @param className
	 * @param paramMap
	 * @param pageNo
	 * @param PageSize
	 * @param columnNameForCount
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List queryAllMatchListWithParaMap(String sql, Class clasz, Map paramMap, int pageNo, int pageSize,
			String columnNameForCount) {
		if (paramMap != null && paramMap.isEmpty()) {
			paramMap = null;
		}
		if ("true".equals(showSql)) {
			try {
				logger.info(getSqlFromQueryData(sql, paramMap));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
//		 int totalRows = this.getResultCountWithValuesMap(sql,
//		 columnNameForCount, paramMap);
//		 if(pageNo>1){
//			 int pageCount = this.getValidPage(pageNo, totalRows, pageSize);
//		 }
		List resultList = null;

		int startRow = getStartOfPage(pageNo, pageSize);
		if (clasz == null) {
			resultList = namedParameterJdbcTemplate.queryForList(this.buildPageSql(sql, startRow, pageSize), paramMap);
		} else {
			RowMapper rowMapper = new ObjectRowMapper(clasz);
			resultList = namedParameterJdbcTemplate.query(this.buildPageSql(sql, startRow, pageSize), paramMap,
					rowMapper);
			rowMapper = null;
		}
		return resultList;
	}

	private String buildPageSql(String sql, int startRow, int pageSize) {
		if ("oracle".equals(this.getDbType())) {
			return this.buildOraclePageSql(sql, startRow, pageSize);
		} else if ("mysql".equals(this.getDbType())) {
			return this.buildMysqlPageSql(sql, startRow, pageSize);
		} else if ("informix".equals(this.getDbType())) {
			return this.buildInformixPageSql(sql, startRow, pageSize);
		}
		throw new DataBaseException("don't support db type,please confirm db is oracle or mysql or informix");
	}

	private String buildOraclePageSql(String sql, int startRow, int pageSize) {
		StringBuilder pageSql = new StringBuilder("SELECT * FROM  ");
		pageSql.append(" ( ");
		pageSql.append(" SELECT pageDataTable.*, ROWNUM RNV ");
		pageSql.append(" FROM (" + sql + " ) pageDataTable ");
		pageSql.append(" WHERE ROWNUM <= " + (startRow + pageSize));
		pageSql.append(" )  WHERE RNV >= " + (startRow + 1));
		return pageSql.toString();
	}

	private String buildMysqlPageSql(String sql, int startRow, int pageSize) {
		sql = sql + " limit " + startRow + ", " + pageSize;
		return sql;
	}

	private String buildInformixPageSql(String sql, int startRow, int pageSize) {
		sql = sql.trim();
		if (sql.startsWith("select")) {
			sql = sql.replaceFirst("select", " select skip " + startRow + " first " + pageSize + " ");
		} else {
			sql = sql.replaceFirst("SELECT", " select skip " + startRow + " first " + pageSize + " ");
		}

		return sql;
	}

	private Integer getValidPage(Integer pageNo, int totalRows, Integer pageSize) {
		if (!isValidPage(pageNo, totalRows, pageSize)) {
			return getValidPage(--pageNo, totalRows, pageSize);
		}
		int pageCount = (totalRows + (pageSize - (totalRows % pageSize == 0 ? pageSize : totalRows % pageSize)))
				/ pageSize;
		return pageCount;
	}

	private static int getStartOfPage(int pageNo, int pageSize) {
		if(pageNo==0){
			pageNo=1;
		}
		return (pageNo - 1) * pageSize;
	}

	private boolean isValidPage(Integer pageNo, Integer totalRows, Integer pageSize) {
		if (pageNo == 1) {
			return true;
		}
		int rowStart = (pageNo - 1) * pageSize;
		int rowEnd = rowStart + pageSize;
		if (rowEnd > totalRows) {
			rowEnd = totalRows;
		}
		return rowEnd > rowStart;
	}

	/**
	 * 查询记录数
	 * 
	 * @param sql
	 * @param columnNameForCount
	 * @param praValuesMap
	 * @return
	 */
	// @SuppressWarnings("deprecation")
	public int getResultCountWithValuesMap(String sql, String columnNameForCount, Map<String, Object> praValuesMap) {
		if (praValuesMap != null && praValuesMap.isEmpty()) {
			praValuesMap = null;
		}
		String countQuerySql = null;
		countQuerySql = " select count(*) from ( " + sql + " ) V_TABLE";
//		if (sql.indexOf("GROUP BY") > 0) {
//			if (columnNameForCount == null) {
//				countQuerySql = " select count(*) from ( " + sql + " ) V_TABLE";
//			} else {
//				countQuerySql = " select count(" + columnNameForCount + ") from ( " + sql + " ) V_TABLE";
//			}
//		} else {
//			countQuerySql = " select count(" + columnNameForCount + ") " + removeSelect(removeOrders(sql));
//		}

		// return namedParameterJdbcTemplate.queryForInt(countQuerySql,
		// praValuesMap);

		return namedParameterJdbcTemplate.queryForInt(countQuerySql, praValuesMap);
	}

	public int queryForIntWithpraValuesMap(String countQuerySql, Map<String, Object> praValuesMap) {
		if (praValuesMap != null && praValuesMap.isEmpty()) {
			praValuesMap = null;
		}
		return namedParameterJdbcTemplate.queryForInt(countQuerySql, praValuesMap);
	}

	public int queryForInt(String countQuerySql, Object... args) {
		return getJdbcTemplate().queryForInt(countQuerySql, args);
	}

	public static String getSqlFromQueryData(String sql, Map<String, Object> paramMap) {
		if (StringUtils.isEmpty(sql)) {
			return null;
		}
		if (paramMap == null) {
			return sql;
		}
		StringBuffer sqlExp = new StringBuffer(sql);
		Set<Entry<String, Object>> set = paramMap.entrySet();
		for (Entry<String, Object> entry : set) {
			int start = sqlExp.indexOf(":" + entry.getKey() + " ");
			if (start < 0) {
				continue;
			}
			int last = sqlExp.lastIndexOf(":" + entry.getKey() + " ");

			if (start >= 0 && start == last) {
				if (entry.getValue() != null) {
					sqlExp.replace(start, start + entry.getKey().length(), "'" + entry.getValue().toString() + "'");
					// sqlExp.replace(start-1, start+entry.getKey().length(),
					// "'"+entry.getValue().toString()+"'");
				}
			} else {
				// 处理同一参数多处出现
				sqlExp.replace(start, start + entry.getKey().length(), "'" + entry.getValue().toString() + "'");
				start = sqlExp.indexOf(":" + entry.getKey());
				while (start > 0) {
					sqlExp.replace(start, start + entry.getKey().length(), "'" + entry.getValue().toString() + "'");
					start = sqlExp.indexOf(":" + entry.getKey());
				}

			}

		}
		return sqlExp.toString();
	}

	private static String removeSelect(String sql) {
		int beginPos = sql.indexOf(" from ");
		if (beginPos < 0) {
			beginPos = sql.indexOf("from ");
		}
		if (beginPos < 0) {
			beginPos = sql.toLowerCase().indexOf(" from ");
		}
		if (beginPos < 0) {
			beginPos = sql.toLowerCase().indexOf("from ");
		}
		return sql.substring(beginPos);
	}

	/**
	 * 去除sql的orderby 用于页查果询
	 * 
	 * @param sql
	 * @return
	 */
	private static String removeOrders(String sql) {
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	public String getDbType() {
		return dbType;
	}

	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
	}

	public String getMysqlLowerCaseTableNames() {
		return mysqlLowerCaseTableNames;
	}

	public void setMysqlLowerCaseTableNames(String mysqlLowerCaseTableNames) {
		this.mysqlLowerCaseTableNames = mysqlLowerCaseTableNames;
	}

	public String isShowSql() {
		return showSql;
	}

	public void setShowSql(String showSql) {
		this.showSql = showSql;
	}



}
