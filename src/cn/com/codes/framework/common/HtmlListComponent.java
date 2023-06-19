package cn.com.codes.framework.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.common.ListObject;

public class HtmlListComponent {

	public static void setListDate(HashMap<String, List<ListObject>> listMap) {
		Iterator it = listMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry me = (Map.Entry) it.next();
			SecurityContextHolder.getContext().setAttr((String) me.getKey(), me.getValue());
		}
	}
	
	public static String toSelectStrWithBreak(List<List<ListObject>> list ){
		if(list==null||list.isEmpty()){
			return "";
		}
		StringBuffer sb = new StringBuffer("");
		for(List<ListObject> ListObjects :list){
			sb.append("$");
			int i = 0;
			for(ListObject obj :ListObjects){
				obj.toString(sb);
				if(i!=ListObjects.size()-1){
					sb.append("^");
				}
				i++;
			}
		}
		return sb.toString();
	}

	public static String toSelectStr(List<ListObject> list ){	
		if(list==null||list.isEmpty()){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		int i = 1; 
		for(ListObject obj :list){
			obj.toString(sb);
			if(i != list.size()){
				sb.append("^");
			}
			i++;
		}
		return sb.toString();
	}
	public static String toSelectStr(List<ListObject> list ,String separator){
		if(list==null||list.isEmpty()){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		int i = 1; 
		for(ListObject obj :list){
			obj.toString(sb);
			if(i != list.size()){
				sb.append(separator);
			}
			i++;
		}
		return sb.toString();
	}	
	 
	public static String toJsonWithBreak(List<List<ListObject>> list ){
		if(list==null||list.isEmpty()){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for(List<ListObject> listObjects :list){
			if(i != 0){
				sb.append("$");
			}
			//if(listObjects != null && listObjects.size() > 0){
				sb.append("{rows: [");
				int j = 0;
				for(ListObject obj :listObjects){
					if(j != 0)
						sb.append(",");
					obj.toJson(sb);
					j ++;
				}
				sb.append("]}");
			//}
			i++;
		}
		if(sb.toString().equals("{rows: []}"))
			return "";
		else
			return sb.toString();
	}
	
	public static String toJsonWithBreakSimple(List<List<ListObject>> list ){
		if(list==null||list.isEmpty()){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for(List<ListObject> listObjects :list){
			if(i != 0){
				sb.append("$");
			}
			//if(listObjects != null && listObjects.size() > 0){
				sb.append("{rows: [");
				int j = 0;
				for(ListObject obj :listObjects){
					if(j != 0)
						sb.append(",");
					obj.toJsonSimple(sb);
					j ++;
				}
				sb.append("]}");
			//}
			i++;
		}
		if(sb.toString().equals("{rows: []}"))
			return "";
		else
			return sb.toString();
	}
	
	public static String toJsonList(List<ListObject> pageList ){
		if(pageList==null||pageList.isEmpty()){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		int i =0 ;
		if(pageList != null && pageList.size()>0){
			sb.append("{rows: [");
			for(ListObject listObject : pageList){
				i++ ;
				if(i != pageList.size()){
					listObject.toJson(sb);
					sb.append(",");
				}else{
					listObject.toJson(sb);
				}
			}
			sb.append("]}");				
		}
		return sb.toString();
	}
}
