package cn.com.codes.object;

import cn.com.codes.object.Function;

public class Function extends cn.com.codes.framework.security.Function {
	
	private String pageType;
	
	public Function(){
		super();
	}
	public Function(String secutiryUrl){
		super(secutiryUrl);
	}
	
	public Function(String functionId,String functionName,String parentId) {
		super(functionId,functionName,parentId);
	}
	public Function (String functionId,String parentId,String functionName,Integer level,String url ,Integer seq ){
		super(functionId,parentId,functionName,level,url ,seq);
	}
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof Function))
			return false;
		Function castOther = (Function) other;

		return ((super.getFunctionId() == castOther.getFunctionId()) || (super.getFunctionId() != null
				&&castOther.getFunctionId() != null && super.getFunctionId()
				.equals(castOther.getFunctionId())));
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + (super.getFunctionId() == null ? 0 : super.getFunctionId().hashCode());
		return result;
	}
	
	public String getPageType() {
		return pageType;
	}
	
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

}