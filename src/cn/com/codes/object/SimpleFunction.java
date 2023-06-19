package cn.com.codes.object;

import cn.com.codes.object.SimpleFunction;

public class SimpleFunction {

	private String functionId;
	private String functionName;
	private Integer seq;
	private String parentId;
	private Integer isleaf;
	private String pageType;

	public SimpleFunction() {

	}

	public SimpleFunction(String functionId, String functionName,
			String parentId) {
		this.functionId = functionId;
		this.functionName = functionName;
		this.parentId = parentId;
	}

	public Integer getSeq() {
		return this.seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SimpleFunction))
			return false;
		SimpleFunction castOther = (SimpleFunction) other;

		return ((getFunctionId() == castOther.getFunctionId()) || (getFunctionId() != null
				&& castOther.getFunctionId() != null &&getFunctionId()
				.equals(castOther.getFunctionId())));
	}

	public int hashCode() {
		int result = 17;
		result = 37
				* result
				+ (getFunctionId() == null ? 0 :getFunctionId()
						.hashCode());
		return result;
	}

	public Integer getIsleaf() {
		return isleaf;
	}

	public void setIsleaf(Integer isleaf) {
		this.isleaf = isleaf;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

}