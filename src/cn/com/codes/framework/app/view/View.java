package cn.com.codes.framework.app.view;

import java.util.HashMap;
import java.util.Map;

import cn.com.codes.framework.transmission.events.BusiResponseEvent;

/**
 * 
 * <p>
 * Title: 返回显示数据的容器
 * </p>
 * <p>
 * Description: 允许方便往前台传的数据
 * </p>
 */
public abstract class View {
	
	protected Map displayData = null;

	/**
	 * 构造函数
	 * 
	 * @return BusiResponseEvent
	 */
	public BusiResponseEvent toResponse() {
		BusiResponseEvent res = _genResponseEvent();
		res.setDisplayData(displayData);
		return res;
	}

	/**
	 * 生成 BusiResponseEvent
	 * 
	 * @return 响应对象
	 */
	protected abstract BusiResponseEvent _genResponseEvent();

	/**
	 * 
	 * @param fieldName
	 *            想要显示的数据字段名
	 * @param obj
	 *            想要显示的数据对象
	 */
	protected void _addDisplayData(String fieldName, Object obj) {
		if(displayData==null){
			displayData = new HashMap();
		}
		displayData.put(fieldName, obj);

	}

	/**
	 * 设置需要显示的数据
	 * 
	 * @param fieldName
	 *            想要显示的数据字段名
	 * @param data
	 *            想要显示的数据内容
	 */
	public void displayData(String fieldName, Object data) {
		_addDisplayData(fieldName, data);
	}

	public Map getDisplayData() {
		return displayData;
	}

}
