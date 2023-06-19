package cn.com.codes.common.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.service.DrawHtmlListDateService;
import cn.com.codes.framework.app.services.BaseServiceImpl;
import cn.com.codes.framework.common.HtmlListComponent;
import cn.com.codes.framework.common.HtmlListQueryObj;
import cn.com.codes.framework.common.ListObject;
import cn.com.codes.object.User;

public class DrawHtmlListDateServiceImpl extends BaseServiceImpl implements
		DrawHtmlListDateService {
	private static List<ListObject> compamyTypeList = new ArrayList<ListObject>(5);
	
	private static List<ListObject> compamySizeList = new ArrayList<ListObject>(9);
	
	private static List<ListObject> testResultList = new ArrayList<ListObject>(8);
	
	static{
		compamyTypeList.add(new ListObject("0", "未知"));
		compamyTypeList.add(new ListObject("1", "互联网"));
		compamyTypeList.add(new ListObject("2", "计算机软件"));
		compamyTypeList.add(new ListObject("3", "系统集成"));
		compamyTypeList.add(new ListObject("4", "其它"));
		
		compamySizeList.add(new ListObject("0", "未知"));
		compamySizeList.add(new ListObject("1", "1-20人以下"));
		compamySizeList.add(new ListObject("2", "21～50"));
		compamySizeList.add(new ListObject("3", "51～100"));
		compamySizeList.add(new ListObject("4", "101～200"));
		compamySizeList.add(new ListObject("5", "200～300"));
		compamySizeList.add(new ListObject("6", "301～500"));
		compamySizeList.add(new ListObject("7", "500～1000"));
		compamySizeList.add(new ListObject("8", "1000以上"));
		
		testResultList.add(new ListObject("-1", "请选择"));
		testResultList.add(new ListObject("0", "ReView"));
		testResultList.add(new ListObject("1", "未测试"));
		testResultList.add(new ListObject("2", "通过"));
		testResultList.add(new ListObject("3", "未通过"));
		testResultList.add(new ListObject("4", "不适用"));
		testResultList.add(new ListObject("5", "阻塞"));
		testResultList.add(new ListObject("6", "修正"));
		
	}
	public List<ListObject> getCompamyType() {
		return compamyTypeList;
	}

	public List<ListObject> getCompamySize() {
		return compamySizeList;
	}

	public List<ListObject> getTestRest(){
		return testResultList;		
	}
	public void setListForCompany() {

		HashMap<String, List<ListObject>> map = new HashMap<String, List<ListObject>>();
		map.put("companyTypeList", this.getCompamyType());
		map.put("companySizeList", this.getCompamySize());
		HtmlListComponent.setListDate(map);
	}

	public List<ListObject> getTypeDefine(String classSimpleName){
		HtmlListQueryObj queryObj = new HtmlListQueryObj();
		queryObj.setKeyPropertyName("typeId");
		queryObj.setValuePropertyName("typeName");
		Map calParaValues = new HashMap();
		calParaValues.put("compId", SecurityContextHolderHelp.getCompanyId());
		queryObj.setParaValues(calParaValues);
		queryObj.setConditions(" WHERE (compId=:compId or compId=1) and status=1");
		queryObj.setHqlObjName(classSimpleName);
		return this.getListData(queryObj, false);
	} 
	
	/**
	 * 用户如果 登录名不为空，则转成ListObject后name值形式为 name(loginName)
	 */
	public List<ListObject> convertUser2ListObj(List<User> userList){
		
		List<ListObject> list = new ArrayList<ListObject>();
		if(userList==null||userList.size()==0){
			return list;
		}
		for(User user :userList){
			ListObject obj = new ListObject();
			obj.setKeyObj(user.getId());
			obj.setValueObj(user.getUniqueName());
			list.add(obj);
		}
		return list;
	}
	

}
