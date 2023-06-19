package cn.com.codes.userManager.dto;

public class UserVo  {

	private String name;
	private String roleId;
	private String id;
	private String loginName;
	
	
	public UserVo() {
	}
	
	public UserVo(String id) {
		this.id = id;
	}
	
	public UserVo(String id,String name ) {
		this.id = id;
		this.name = name;
	}
	public UserVo(String id,String name ,String loginName,String roleId) {
		this.id = id;
		this.name = name;
		this.roleId = roleId;
		this.loginName = loginName;
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
	
	public String getName() {
		return this.name;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());

		return result;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}	

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UserVo))
			return false;
		UserVo castOther = (UserVo) other;

		return ((this.getId() == castOther.getId()) || (this.getId() != null
				&& castOther.getId() != null && this.getId().equals(
				castOther.getId())));
	}


}