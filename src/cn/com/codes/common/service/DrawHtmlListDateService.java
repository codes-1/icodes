package cn.com.codes.common.service;

import java.util.List;

import cn.com.codes.framework.app.services.BaseService;
import cn.com.codes.framework.common.ListObject;
import cn.com.codes.object.User;

public interface DrawHtmlListDateService extends BaseService{

	public List<ListObject> getCompamyType();
	public List<ListObject> getCompamySize();
	public void setListForCompany();
	public List<ListObject> getTypeDefine(String classSimpleName);
	public List<ListObject> getTestRest();
	public List<ListObject> convertUser2ListObj(List<User> userList);

}
