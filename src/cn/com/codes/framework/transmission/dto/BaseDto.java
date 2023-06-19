package cn.com.codes.framework.transmission.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.common.BeanUtils;
import cn.com.codes.framework.common.HtmlListQueryObj;
import cn.com.codes.framework.common.ListObject;
import cn.com.codes.framework.common.config.PropertiesBean;
import cn.com.codes.framework.common.util.Context;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.transmission.JsonInterface;
import cn.com.codes.framework.transmission.dto.BaseDto;

public class BaseDto implements Serializable {

	private static Logger logger = Logger.getLogger(BaseDto.class);
	private String busiMethod;
	protected HashMap map;
	private String operationId;

	private List<JsonInterface> jsonData;

	private int pageNumber;
	//private int pageSize;
	private Integer pageNo;
	private int total;
	
	private int page;
	private int rows;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	private Integer pageCount;

	private Integer pageSize;

	private String isAjax;// 由于转向的问题 增加此变量 用于区分分页时是否重新刷新页面

	private String listStr;

	private String updateHql;

	private String isNull;// 在HQL区分is null 和 =

	private String noSelect;// 查询中不要更新toolbar中的数据

	private Object[] hqlParamsArrays;// 查询参数:占位符

	private List<Map<String, Object>> hqlParamLists;// 查询参数:占位符

	private Map<String, Object> hqlParamMaps;// 查询参数Map:占位符

	private String hql;// 通用

	private List<HtmlListQueryObj> listObjects;// 下拉列表

	private List<List<ListObject>> ListListObjects;// 下拉列表

	private String selectStr;// 下拉列表字符串

	private String successFailed;// 返回成功与否

	private String customMessage;

	private HtmlListQueryObj htmlListQueryObj;

	private List objectList;
	
	private String filterStr;//模糊查询条件

	public BaseDto() {
	}

	public String getBusiMethod() {
		return busiMethod;
	}

	public void setBusiMethod(String busiMethod) {
		this.busiMethod = busiMethod;
	}

	public String getFilterStr() {
		return filterStr;
	}

	public void setFilterStr(String filterStr) {
		this.filterStr = filterStr;
	}

	public void setAttr(String valueKey, Object o) {
		if (map == null) {
			map = new HashMap(1);
		}
		map.put(valueKey, o);
	}

	public Object getAttr(String valueKey) {
		if (map == null) {
			return null;
		}
		return map.get(valueKey);
	}

	public void removeAttr(String key) {
		if (map == null) {
			return;
		}
		map.remove(key);
		if (map.isEmpty()) {
			map = null;
		}
	}

	public void clearDataContainer() {
		map = null;
	}

	protected String getKeyValuesInMap() {
		return BeanUtils.getKeyValuesInMap(map);
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public List<JsonInterface> getJsonData() {
		return jsonData;
	}

	public void setJsonData(List<JsonInterface> jsonData) {
		this.jsonData = jsonData;
	}

	public Integer getPageNo() {
		return (pageNo == null || pageNo == 0 ? 1 : pageNo);
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	
	public int getPageNumber() {
		return ( pageNumber == 0 ? 1 : pageNumber);
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
		this.pageNo = pageNumber;
	}
	
	
	
//	public int getPageSize() {
//		return ( pageSize == 0 ? 20 : pageSize);
//	}
//
//	public void setPageSize(int pageSize) {
//		this.pageSize = pageSize;
//	}
	
	
	public Integer getPageCount() {
		return (pageCount == null ? 1 : pageCount);
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public Integer getPageSize() {

		if (pageSize == null || pageSize == 0) {
			PropertiesBean conf = (PropertiesBean) Context.getInstance().getBean("ContextProperties");
			String pageSizeStr = conf.getProperty("itest.page.size");
			if (pageSizeStr != null && !"".equals(pageSizeStr)) {
				int currPageSize = 20;
				try {
					currPageSize = Integer.parseInt(pageSizeStr);
				} catch (NumberFormatException e) {
				}
				pageSize = currPageSize;
			} else {
				pageSize = 20;
			}
		}
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getIsAjax() {
		return isAjax;
	}

	public void setIsAjax(String isAjax) {
		this.isAjax = isAjax;
	}

	public String toListStrJson() {
		List<JsonInterface> jsonData = this.getJsonData();
		StringBuffer sb = new StringBuffer();
		return this.toJson(jsonData, sb);
	}

	public String toJson(List<JsonInterface> list, StringBuffer sb) {
		if (sb == null) {
			sb = new StringBuffer();
		}
		int i = 0;
		if (list != null && !list.isEmpty()) {
			sb.append("{rows: [");
			for (JsonInterface obj : list) {
				i++;
				if (i != list.size()) {
					sb.append(obj.toStrList()).append(",");
				} else {
					sb.append(obj.toStrList());
				}
			}
			sb.append("]}");
		}
		Object pageInfo = SecurityContextHolder.getContext().getAttr("pageInfo");
		if (pageInfo != null) {
			return sb.insert(0, pageInfo.toString()).toString();
		} else {
			return sb.toString();
		}
	}

	public void toJson2(List<JsonInterface> list, StringBuffer sb) {
		int i = 0;
		if (list != null && !list.isEmpty()) {
			sb.append("{rows: [");
			for (JsonInterface obj : list) {
				i++;
				if (i != list.size()) {
					obj.toString(sb);
					sb.append(",");
				} else {
					obj.toString(sb);
				}
			}
			sb.append("]}");
		}
		Object pageInfo = SecurityContextHolder.getContext().getAttr("pageInfo");
		if (pageInfo != null) {
			sb.insert(0, pageInfo.toString()).toString();
		}
	}

	public String ObjArrList2Json(List<Object[]> list, StringBuffer sb, int idIndex) {
		if (sb == null) {
			sb = new StringBuffer();
		}
		int i = 0;
		if (list != null && !list.isEmpty()) {
			sb.append("{rows: [");
			for (Object[] values : list) {
				i++;
				if (i != list.size()) {
					this.ObjArr2Json(values, sb, idIndex, true);
				} else {
					this.ObjArr2Json(values, sb, idIndex, false);
				}
			}
			sb.append("]}");
		}
		Object pageInfo = SecurityContextHolder.getContext().getAttr("pageInfo");
		if (pageInfo != null) {
			return sb.insert(0, pageInfo.toString()).toString();
		} else {
			return sb.toString();
		}
	}

	private void ObjArr2Json(Object[] values, StringBuffer sb, int idIndex, boolean appendComma) {
		sb.append("{");
		sb.append("id:'");
		sb.append(values[idIndex].toString());
		sb.append("',data: [0,'','");
		int i = 0;
		for (Object obj : values) {
			if (i != idIndex) {
				if (i < values.length - 1) {
					if (obj != null && obj instanceof Date) {
						sb.append(StringUtils.formatShortDate((Date) obj)).append("','");
					} else {
						sb.append(obj == null ? "" : obj.toString()).append("','");
					}
				} else {
					if (obj != null && obj instanceof Date) {
						sb.append(StringUtils.formatMiddleDate((Date) obj)).append("'");
					} else {
						sb.append(obj == null ? "" : obj.toString()).append("'");
					}
				}
				i++;
			} else if (idIndex == 0 && i == 0) {
				i++;
			}
		}
		sb.append("]");
		sb.append("}");
		if (appendComma) {
			sb.append(",");
		}
	}

	public String getListStr() {
		return (listStr == null ? toListStrJson() : listStr);
	}

	public void setListStr(String listStr) {
		this.listStr = listStr;
	}

	public String getUpdateHql() {
		return updateHql;
	}

	public void setUpdateHql(String updateHql) {
		this.updateHql = updateHql;
	}

	public String getIsNull() {
		return isNull;
	}

	public void setIsNull(String isNull) {
		this.isNull = isNull;
	}

	public String getNoSelect() {
		return noSelect;
	}

	public void setNoSelect(String noSelect) {
		this.noSelect = noSelect;
	}

	public Object[] getHqlParamsArrays() {
		return hqlParamsArrays;
	}

	public void setHqlParamsArrays(Object[] hqlParamsArrays) {
		this.hqlParamsArrays = hqlParamsArrays;
	}

	public List<Map<String, Object>> getHqlParamLists() {
		return hqlParamLists;
	}

	public void setHqlParamLists(List<Map<String, Object>> hqlParamLists) {
		this.hqlParamLists = hqlParamLists;
	}

	public Map<String, Object> getHqlParamMaps() {
		return hqlParamMaps;
	}

	public void setHqlParamMaps(Map<String, Object> hqlParamMaps) {
		this.hqlParamMaps = hqlParamMaps;
	}

	public List<HtmlListQueryObj> getListObjects() {
		return listObjects;
	}

	public void setListObjects(List<HtmlListQueryObj> listObjects) {
		this.listObjects = listObjects;
	}

	public List<List<ListObject>> getListListObjects() {
		return ListListObjects;
	}

	public void setListListObjects(List<List<ListObject>> listListObjects) {
		ListListObjects = listListObjects;
	}

	public String getSelectStr() {
		return selectStr;
	}

	public void setSelectStr(String selectStr) {
		this.selectStr = selectStr;
	}

	public String getSuccessFailed() {
		return successFailed;
	}

	public void setSuccessFailed(String successFailed) {
		this.successFailed = successFailed;
	}

	public String getHql() {
		return hql;
	}

	public void setHql(String hql) {
		this.hql = hql;
	}

	public String getCustomMessage() {
		return customMessage;
	}

	public void setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
	}

	public HtmlListQueryObj getHtmlListQueryObj() {
		return htmlListQueryObj;
	}

	public void setHtmlListQueryObj(HtmlListQueryObj htmlListQueryObj) {
		this.htmlListQueryObj = htmlListQueryObj;
	}

	public List getObjectList() {
		return objectList;
	}

	public void setObjectList(List objectList) {
		this.objectList = objectList;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
		this.pageNo = page;
		this.pageNumber = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
		this.pageSize = rows;
	}
}
