package cn.com.codes.framework.app.view;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cn.com.codes.framework.transmission.events.BusiResponseEvent;
import cn.com.codes.framework.app.view.View;

public class UniversalView extends View {

	private String viewName = "UniversalView";

	public UniversalView(String viewName) {
		this.viewName = viewName;
	}
	public UniversalView(){
		
	}
	protected BusiResponseEvent _genResponseEvent() {
		BusiResponseEvent res = new BusiResponseEvent();
		res.setViewName(viewName);
		return res;
	}


	
	public String toString() {
		if (this.displayData == null) {
			return "null";
		} else {
			StringBuffer sb = new StringBuffer("\n below are values in view");
			Set set = displayData.entrySet();
			Iterator it = set.iterator();
			Object value = null;
			Object key = null;
			while (it.hasNext()) {
				Map.Entry en = (Map.Entry) it.next();
				value = en.getValue();
				key = en.getKey();
				sb.append("\n" +key+"==" +value);
				
			}
			sb.append("\n");
			sb.append("=================================================================");
			return sb.toString();
		}

	}
}
