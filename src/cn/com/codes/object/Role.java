package cn.com.codes.object;

import java.util.Date;
import java.util.Set;

import cn.com.codes.framework.transmission.JsonInterface;
import cn.com.codes.userManager.dto.UserVo;
import cn.com.codes.object.Function;
import cn.com.codes.object.Role;
import cn.com.codes.object.SimpleFunction;
import cn.com.codes.object.SimpleUser;

public class Role implements JsonInterface {
	
	private String roleId;
	private String roleName;
	private Date insertDate;
	private Date updateDate;
	private String remark;
	private Set<SimpleFunction> function;
	private Set<Function> privilege;
	private Set<SimpleUser> user;
	private Set<UserVo> userVoSet;
	private String accessIp;
	private String companyId;
	private String userIds;
	private String funIds;
	private Long docRoleId;

	public Role() {
	}

	public Role(String roleId,String roleName,String remark,String accessIp) {
		this.roleId = roleId;
		this.roleName = roleName;
		this.remark = remark;
		this.accessIp = accessIp;
	}	
	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Set<SimpleUser> getUser() {
		return user;
	}

	public void setUser(Set<SimpleUser> user) {
		this.user = user;
	}

	public String getAccessIp() {
		return accessIp;
	}

	public void setAccessIp(String accessIp) {
		this.accessIp = accessIp;
	}

	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Set<SimpleFunction> getFunction() {
		return function;
	}

	public void setFunction(Set<SimpleFunction> function) {
		this.function = function;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getFunIds() {
		return funIds;
	}

	public void setFunIds(String funIds) {
		this.funIds = funIds;
	}

	public Set<UserVo> getUserVoSet() {
		return userVoSet;
	}

	public void setUserVoSet(Set<UserVo> userVoSet) {
		this.userVoSet = userVoSet;
	}
	public Set<Function> getPrivilege() {
		return privilege;
	}

	public void setPrivilege(Set<Function> privilege) {
		this.privilege = privilege;
	}
	
	public Long getDocRoleId() {
		return docRoleId;
	}

	public void setDocRoleId(Long docRoleId) {
		this.docRoleId = docRoleId;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof Role))
			return false;
		Role castOther = (Role) other;

		return ((this.roleId == castOther.roleId) || (this.roleId != null
				&& castOther.roleId != null && this.roleId
				.equals(castOther.roleId)));
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + (roleId == null ? 0 : this.roleId.hashCode());
		return result;
	}

	public String toString() {
		StringBuffer sbf = new StringBuffer();
		sbf.append("{");
		sbf.append("id:'");
		sbf.append(roleId);
		// append data start
		sbf.append("',data: [0,'','");
		sbf.append((roleName == null) ? "" : roleName);
		sbf.append("','");
		sbf.append((accessIp == null) ? "" : accessIp);
		sbf.append("','");
		sbf.append((remark == null ? "" : remark));
		sbf.append("'");
		sbf.append("]");
		// append data end
		sbf.append("}");
		return sbf.toString();
	}

	public void toString(StringBuffer sbf) {
		sbf.append("{");
		sbf.append("id:'");
		sbf.append(roleId);
		sbf.append("',data: [0,'','");
		sbf.append("superRole".equals(roleName)?"超级管理员":roleName);
		sbf.append("','");
		if(userVoSet==null||userVoSet.isEmpty()){
			sbf.append("");
		}else {
			userVo2Str(sbf);
		}
		sbf.append("','");
		sbf.append((remark == null ? "" : "superRole".equals(roleName)?"超级管理员":remark));
		sbf.append("','");
		sbf.append("");//访问Ip 占位符
		sbf.append("'");
		sbf.append("]");
		sbf.append("}");	
	}
	private void userVo2Str(StringBuffer sbf){
		int i=1;
		for(UserVo usr :userVoSet){
			if(i>1){
				sbf.append(" ").append(usr.getName());
			}else{
				sbf.append(usr.getName());
			}
			i++;
		}
		//userVoSet = null;
	}
	public String toStrList() {
		StringBuffer sbf = new StringBuffer();
		sbf.append("{");
		sbf.append("id:'");
		sbf.append(roleId);
		// append data start
		sbf.append("',data: [0,'','");
		sbf.append((roleName == null) ? "" : roleName);
		sbf.append("','");
		sbf.append((accessIp == null) ? "" : accessIp);
		sbf.append("','");
		sbf.append((remark == null ? "" : remark));
		sbf.append("'");
		sbf.append("]");
		// append data end
		sbf.append("}");
		return sbf.toString();
	}

    public String toStrUpdateInit(){
        StringBuffer sbf = new StringBuffer();
        sbf.append("roleId=");
        sbf.append(getRoleId().toString());
        sbf.append("^");
        sbf.append("roleName=").append(roleName == null ? "" : "superRole".equals(roleName)?"超级管理员":roleName);
        sbf.append("^");
        sbf.append("remark=").append(remark == null ? "" : "superRole".equals(roleName)?"超级管理员":remark);
        sbf.append("^");
        sbf.append("accessIp=").append(accessIp == null ? "" : accessIp);
        return sbf.toString();
    }

	public String toStrUpdateRest() {

		return null;
	}

}