package cn.com.codes.framework.common;

import java.util.Map;

import cn.com.codes.framework.common.HtmlListQueryObj;

public class HtmlListQueryObj {
	private String keyPropertyName;
	private String valuePropertyName;
	private String hqlObjName;
	private String listName;// �??jax方法时，把列表数据存入request时的Attribute 名�??	
	private String conditions;// 含where 的参数绑定ＨＱＬ字�??�??here companyID:companyID
	public Map<String, Object> paraValues; // key 为属性名，value为属性�?

	public String getKeyPropertyName() {
		return keyPropertyName;
	}

	public void setKeyPropertyName(String keyPropertyName) {
		this.keyPropertyName = keyPropertyName;
	}

	public String getValuePropertyName() {
		return valuePropertyName;
	}

	public void setValuePropertyName(String valuePropertyName) {
		this.valuePropertyName = valuePropertyName;
	}

	public String getHqlObjName() {
		return hqlObjName;
	}

	public void setHqlObjName(String hqlObjName) {
		this.hqlObjName = hqlObjName;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public Map<String, Object> getParaValues() {
		return paraValues;
	}

	public void setParaValues(Map<String, Object> paraValues) {
		this.paraValues = paraValues;
	}

	public String getConditions() {
		return conditions;
	}

	public void setConditions(String conditions) {
		this.conditions = conditions;
	}

	public HtmlListQueryObj copy() {
		HtmlListQueryObj obj = new HtmlListQueryObj();
		obj.conditions = this.conditions;
		obj.keyPropertyName = this.keyPropertyName;
		obj.valuePropertyName = this.valuePropertyName;
		obj.hqlObjName = this.hqlObjName;
		obj.listName = this.listName;
		obj.conditions = this.conditions;
		obj.paraValues = this.paraValues;
		return obj;
	}


}
