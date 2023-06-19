package cn.com.codes.framework.common;

import cn.com.codes.framework.transmission.JsonInterface;
import cn.com.codes.framework.common.ListObject;

public class ListObject implements JsonInterface{
	private String keyObj;
	private String valueObj;
	private String remark;//

	public ListObject(){		
	}

	public ListObject(String key) {
		this.keyObj = key;
	}
	public ListObject(String key, String value) {
		this.keyObj = key;
		this.valueObj = value;
	}
	public ListObject(String key, String value,String remark) {
		this.keyObj = key;
		this.valueObj = value;
		this.remark = remark;
	}
	public ListObject(Long key, String value) {
		this.keyObj = key.toString();
		this.valueObj = value;
	}	
	public String getKeyObj() {
		return keyObj;
	}
	
	public void setKeyObj(String keyObj) {
		this.keyObj = keyObj;
	}
	
	public String getValueObj() {
		return valueObj;
	}
	
	public void setValueObj(String valueObj) {
		this.valueObj = valueObj;
	}

	/** 转成下拉列表ajax形�??*/
	public void toString(StringBuffer sb){
		sb.append(keyObj);
		sb.append(";");
		sb.append(valueObj);
	}
	
	/** 转成分�??列表ajax json形�??*/
	public void toJson(StringBuffer sb){
		sb.append("{");
		sb.append("id:'");
		sb.append(keyObj);
		//append data start 
		sb.append("',data: [0,'','");
		sb.append((valueObj == null) ? "" : valueObj);
		sb.append("'");
		sb.append("]");
		//append data end  
		sb.append("}");
	}
	
	/** 转成分�??列表ajax json 形�??没有checkbox */
	public void toJsonSimple(StringBuffer sb){
		sb.append("{");
		sb.append("id:'");
		sb.append(keyObj);
		//append data start 
		sb.append("',data: [0,'");
		sb.append((valueObj == null) ? "" : valueObj);
		sb.append("'");
		sb.append("]");
		//append data end  
		sb.append("}");
	}
	
	public String toStrUpdateInit(){
		return null;
	}
	
	public String toStrList(){
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("id:'");
		sb.append(keyObj);
		//append data start 
		sb.append("',data: [0,'','");
		sb.append((valueObj == null) ? "" : valueObj);
		sb.append("'");
		sb.append("]");
		//append data end  
		sb.append("}");
		return sb.toString();
	}
	
	public String toStrUpdateRest(){
		return null;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ListObject))
			return false;
		ListObject castOther = (ListObject) other;
		return ( this.keyObj != null
				&& castOther.getKeyObj() != null && this.keyObj.equals(
				castOther.getKeyObj()));
	}
	
	public int hashCode() {
		int result = 17;
		result = 37 * result + (keyObj== null ? 0 : keyObj.hashCode());
		return result;
	}


	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


}
