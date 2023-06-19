package cn.com.codes.common.dto;

import java.util.List;
import java.util.Map;

public class PageModel {

	private int total;
	private List<?> rows;
	private int pageNo = 1;
	private transient String queryHql;
	private transient Map<String, Object> hqlParamMap;
	private transient String countProName;
	private int pageSize = 10;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

	public String getQueryHql() {
		return queryHql;
	}

	public void setQueryHql(String queryHql) {
		this.queryHql = queryHql;
	}

	public Map<String, Object> getHqlParamMap() {
		return hqlParamMap;
	}

	public void setHqlParamMap(Map<String, Object> hqlParamMap) {
		this.hqlParamMap = hqlParamMap;
	}

	public String getCountProName() {
		return countProName;
	}

	public void setCountProName(String countProName) {
		this.countProName = countProName;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
