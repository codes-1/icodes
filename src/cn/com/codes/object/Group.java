package cn.com.codes.object;

import java.util.Date;
import java.util.Set;

import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.transmission.JsonInterface;
import cn.com.codes.object.Group;
import cn.com.codes.object.User;

public class Group implements JsonInterface {

	private String id;
	private String name;
	private Integer adminFlag;
	private String remark;
	private String companyId;
	private Date insertDate;
	private Date updateDate;
	private Set<User> user;
	private String userIds;

	public Group() {
	}

	public Group(String id, String name, String remark) {
		this.id = id;
		this.name = name;
		this.remark = remark;

	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Set getUser() {
		return user;
	}

	public void setUser(Set user) {
		this.user = user;
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

	public Integer getAdminFlag() {
		return adminFlag;
	}

	public void setAdminFlag(Integer adminFlag) {
		this.adminFlag = adminFlag;
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
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof Group))
			return false;
		Group castOther = (Group) other;

		return ((this.getId() == castOther.getId()) || (this.getId() != null
				&& castOther.getId() != null && this.getId().equals(
				castOther.getId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());

		return result;
	}

	public String toString() {
		StringBuffer sbf = new StringBuffer();
		sbf.append("{");
		sbf.append("id:'");
		sbf.append(id);
		sbf.append("',data: [0,'','");
		sbf.append((name == null) ? "" : name);
		sbf.append("','");
		sbf.append((remark == null) ? "" : remark);
		sbf.append("'");
		sbf.append("]");
		sbf.append("}");
		return sbf.toString();
	}

	public String toStrList() {
		StringBuffer sbf = new StringBuffer();
		sbf.append("{");
		sbf.append("id:'");
		sbf.append(getId().toString());
		sbf.append("',data: [0,'','");
		sbf.append(name == null ? "" : name);
		sbf.append("','");
		sbf.append(adminFlag == null ? "" : adminFlag);
		sbf.append("','");
		sbf.append(remark == null ? "" : remark);
		sbf.append("','");
		sbf.append(companyId == null ? "" : companyId);
		sbf.append("','");
		sbf.append(insertDate == null ? "" : StringUtils.formatShortDate(insertDate));
		sbf.append("','");
		sbf.append(updateDate == null ? "" : StringUtils.formatShortDate(updateDate));
		sbf.append("','");
		sbf.append(user == null ? "" : user);
		sbf.append("'");
		sbf.append("]");
		sbf.append("}");
		return sbf.toString();
	}

	public String toStrUpdateInit() {
		StringBuffer sbf = new StringBuffer();
		sbf.append("groupId=");
		sbf.append(getId().toString());
		sbf.append("^");
		sbf.append("name=").append(name == null ? "" : name);
		sbf.append("^");
		sbf.append("adminFlag=").append(adminFlag == null ? "" : adminFlag);
		sbf.append("^");
		sbf.append("remark=").append(remark == null ? "" : remark);
		sbf.append("^");
		sbf.append("insertDate=").append(insertDate == null ? "" : StringUtils.formatLongDate(insertDate));
		sbf.append("^");
		if(user==null||user.isEmpty()){
			sbf.append("userName=").append("");
		}else{
			sbf.append("userName=");
			this.userJsonConvert2(sbf);
		}
		sbf.append("^");
		sbf.append("userIds=");
		sbf.append(userIds == null ? "" : userIds);
		return sbf.toString();
	}

	public String toStrUpdateRest() {
		StringBuffer sbf = new StringBuffer();
		sbf.append(getId().toString());
		sbf.append("^");
		sbf.append("0,,");
		sbf.append(name == null ? "" : name);
		sbf.append(",");
		sbf.append(adminFlag == null ? "" : adminFlag);
		sbf.append(",");
		sbf.append(remark == null ? "" : remark);
		sbf.append(",");
		sbf.append(companyId == null ? "" : companyId);
		sbf.append(",");
		sbf.append(insertDate == null ? "" : StringUtils.formatShortDate(insertDate));
		sbf.append(",");
		sbf.append(updateDate == null ? "" : StringUtils.formatShortDate(updateDate));
		sbf.append(",");
		sbf.append(user == null ? "" : user);
		return sbf.toString();
	}

	public void toString(StringBuffer sbf) {
		sbf.append("{");
		sbf.append("id:'");
		sbf.append(getId().toString());
		sbf.append("',data: [0,'','");
		sbf.append(name);
		sbf.append("','");
		if(user==null||user.isEmpty()){
			sbf.append( "");
		}else{
			this.userJsonConvert(sbf);
		}
		sbf.append("','");
		sbf.append(remark == null ? "" : remark);
		sbf.append("','");
		sbf.append(userIds == null ? "" : userIds);
		sbf.append("'");
		sbf.append("]");
		sbf.append("}");
	}
	private void userJsonConvert(StringBuffer sbf){
		StringBuffer usrIds = new StringBuffer();
		int i=1;
		for(User usr :user){
			usrIds.append(" ").append(usr.getId());
			if(i>1){
				sbf.append(",").append(usr.getLoginName()).append("(").append(usr.getName()).append(")");
			}else{
				sbf.append(usr.getLoginName()).append("(").append(usr.getName()).append(")");
			}
			i++;
		}
		this.userIds=usrIds.toString().substring(1);
	}

	private void userJsonConvert2(StringBuffer sbf){
		StringBuffer usrIds = new StringBuffer();
		int i=1;
		for(User usr :user){
			usrIds.append(" ").append(usr.getId());
			if(i>1){
				sbf.append(",").append(usr.getLoginName()).append("(").append(usr.getName()).append(")");
			}else{
				sbf.append(usr.getLoginName()).append("(").append(usr.getName()).append(")");
			}
			i++;
		}
		this.userIds=usrIds.toString().substring(1);
		user = null;
	}
}