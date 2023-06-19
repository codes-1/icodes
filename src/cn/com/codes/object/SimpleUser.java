package cn.com.codes.object;

import java.io.Serializable;

import cn.com.codes.object.SimpleUser;

public class SimpleUser implements Serializable {

	private String name;
	private String id;
	private String loginName;

	public SimpleUser() {
	}

	public SimpleUser(String id) {
		this.id = id;
	}

	public SimpleUser(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public SimpleUser(String id, String name, String loginName) {
		this.id = id;
		this.name = name;
		this.loginName = loginName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SimpleUser))
			return false;
		SimpleUser castOther = (SimpleUser) other;

		return ((this.getId() == castOther.getId()) || (this.getId() != null
				&& castOther.getId() != null && this.getId().equals(
				castOther.getId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());

		return result;
	}

}