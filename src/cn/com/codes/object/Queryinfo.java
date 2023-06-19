package cn.com.codes.object;

import java.util.Map;

public abstract class Queryinfo implements java.io.Serializable {

	private Long queryId;
	private String queryName;
	private String ownerId;
	private String paraValueStr;
	private Integer onlyMe;
	private String hqlCondiStr;
	private Map praValues;
	private String taskId;
	public Queryinfo() {
	}

	public Queryinfo(Long queryId, Integer onlyMe, String paraValueStr,String taskId,String queryName) {
		this.queryId = queryId;
		this.onlyMe = onlyMe;
		this.paraValueStr = paraValueStr;
		this.queryName = queryName;
		this.taskId = taskId;
	}
	public Queryinfo(String paraValueStr,String queryName,Long queryId, Integer onlyMe) {
		this.queryId = queryId;
		this.onlyMe = onlyMe;
		this.paraValueStr = paraValueStr;
		this.queryName = queryName;
	}
	public Queryinfo(Long queryId, Integer onlyMe,String taskId,String queryName) {
		this.queryId = queryId;
		this.onlyMe = onlyMe;
		this.taskId = taskId;
		this.queryName = queryName;
	}
	public Queryinfo(Long queryId, String queryName, String ownerId) {
		this.queryId = queryId;
		this.queryName = queryName;
		this.ownerId = ownerId;
		;
	}

	public Queryinfo(Long queryId, String queryName, String ownerId,
			 String paraValueStr) {
		this.queryId = queryId;
		this.queryName = queryName;
		this.ownerId = ownerId;
		this.paraValueStr = paraValueStr;
	}

	public Long getQueryId() {
		return this.queryId;
	}

	public void setQueryId(Long queryId) {
		this.queryId = queryId;
	}

	public String getQueryName() {
		return this.queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public String getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}


	public String getParaValueStr() {
		return this.paraValueStr;
	}

	public void setParaValueStr(String paraValueStr) {
		this.paraValueStr = paraValueStr;
	}

	public Integer getOnlyMe() {
		return onlyMe;
	}

	public void setOnlyMe(Integer onlyMe) {
		this.onlyMe = onlyMe;
	}

	public String getHqlCondiStr() {
		return hqlCondiStr;
	}

	public void setHqlCondiStr(String hqlStr) {
		this.hqlCondiStr = hqlStr;
	}

	public Map getPraValues() {
		return praValues;
	}

	public void setPraValues(Map praValues) {
		this.praValues = praValues;
	}
	public void setPraValAnd2Str(Map praValues) {
		this.praValues = praValues;
		this.paraValueStr = this.praValues2Str();
	}
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public abstract Map praValueStr2Map();
	
	public abstract String praValues2Str(); 
	


}