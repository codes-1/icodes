package cn.com.codes.framework.security;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.codes.framework.security.Button;



public class UserInfo implements java.io.Serializable {

	private Set<String> privilege;
	private Map<String, List<Button>> visiableButton;
	private Integer isAdmin;
	private String id;
	private String loginName;
	private String myHome;
	
	public String getId() {
		return id;
	}

	public  void setId(String id) {
		this.id = id;
	}

	public  Integer getIsAdmin() {
		if(isAdmin==null){
			return 0;
		}
		return isAdmin;
	}

	public  void setIsAdmin(Integer isAdmin) {
		this.isAdmin = isAdmin;
	}

	public void setPrivilege(Set<String> privilege) {
		this.privilege = privilege;
	}

	public Set<String> getPrivilege() {
		return privilege;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Map<String, List<Button>> getVisiableButton() {
		return visiableButton;
	}

	public void setVisiableButton(Map<String, List<Button>> visiableButton) {
		this.visiableButton = visiableButton;
	}

	public String getMyHome() {
		///bugManager/bugManagerAction!loadAllMyBug.action
		return myHome;
	}

	public void setMyHome(String myHome) {
		this.myHome = myHome;
	}

}