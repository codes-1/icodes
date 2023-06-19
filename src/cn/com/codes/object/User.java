package cn.com.codes.object;

import java.util.Date;
import java.util.List;
import java.util.Set;

import cn.com.codes.framework.security.UserInfo;
import cn.com.codes.framework.security.VisitUser;
import cn.com.codes.framework.transmission.JsonInterface;
import cn.com.codes.object.Group;
import cn.com.codes.object.OutlineTeamMember;
import cn.com.codes.object.Role;
import cn.com.codes.object.TaskUseActor;
import cn.com.codes.object.User;

public class User extends UserInfo implements JsonInterface {

	private String companyId;
	private String name;
	private String password;
	private String employeeId;
	private Integer status;
	private String email;
	private String tel;
	private String question;
	private String answer;
	private Date insertDate;
	private Date updateDate;
	private List<Role> roleList ;
	private List<Group> groupList ;
	private Integer delFlag;
	private String officeTel ;
	private String groupNames; //所在组组名，各个组名用_分开
	private String groupIds; //所在组组ID，各个ＩＤ用_分开
	private String headShip ;
	private Date  joinCompDate ;//入职日期
	private String oldPwd;
	private String uniqueName;
	private Long docUserId;
	private Set<OutlineTeamMember> devMemb;
	private Set<TaskUseActor> taskUseActors;
	private String svn;
	private String chgPwdFlg;
	public User() {
	}
	
	public User(String id) {
		super.setId(id);
	}


	public User(String id,String name ) {
		super.setId(id);
		this.name = name;
	}
	public User(String email,Integer status) {
		this.email = email;
		this.status = status;
	}
	public User(String id,String name ,String loginName) {
		super.setId(id);
		this.name = name;
		super.setLoginName(loginName);
	}
	public User(String id,String name ,String loginName,Integer delFlag) {
		super.setId(id);
		this.name = name;
		super.setLoginName(loginName);
		this.delFlag = delFlag;
	}
	public User(Long docUserId,String name,String loginName ) {
		this.docUserId = docUserId;
		this.name = name;
		super.setLoginName(loginName);
	}
	public User(String id,String name ,String loginName,String groupIds) {
		super.setId(id);
		this.name = name;
		super.setLoginName(loginName);
		this.groupIds = groupIds;
	}
	public User(String id,String name ,String loginName,String tel,
			String officeTel,String email,String headShip,Integer status,String employeeId) {
		super.setId(id);
		this.name = name;
		super.setLoginName(loginName);
		this.tel = tel;
		this.officeTel = officeTel;
		this.email = email;
		this.headShip = headShip;
		this.status = status;
		this.employeeId = employeeId;
	}
	
	public User(String id,String name ,String loginName,String tel,
			String officeTel,String email,String headShip,Integer status,String employeeId,Integer isAdmin) {
		super.setId(id);
		this.name = name;
		super.setLoginName(loginName);
		this.tel = tel;
		this.officeTel = officeTel;
		this.email = email;
		this.headShip = headShip;
		this.status = status;
		this.employeeId = employeeId;
		super.setIsAdmin(isAdmin);
	}
	public User(String id,String name ,String loginName,String companyId,String password, Integer isAdmin,String myHome,Long docUserId) {
		super.setId(id);
		this.name = name;
		super.setLoginName(loginName);
		this.companyId = companyId;
		this.password = password;
		super.setIsAdmin(isAdmin);
		super.setMyHome(myHome);
		this.docUserId =docUserId;
	}
	

	public VisitUser copy2VisitUser(){
		VisitUser vu = new VisitUser();
		vu.setId(super.getId());
		vu.setIsAdmin(super.getIsAdmin());
		vu.setLoginName(super.getLoginName());
		vu.setCompanyId(this.getCompanyId());
		vu.setPrivilege(super.getPrivilege());
		vu.setName(name);
		vu.setDocUserId(this.getDocUserId());
		return vu;
	}
	public String getOldPwd() {
		return this.oldPwd;
	}
	public String getUniqueName(){
		StringBuffer sb = new StringBuffer();
		sb.append(super.getLoginName());
		if(uniqueName!=null){
			return uniqueName;
		}
		if(super.getLoginName()!=null){
			sb.append("(").append(this.name).append(")");
		}
		return sb.toString();
	}
	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}	
	public void setoldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}
	
	public String getName() {
		return this.name;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getQuestion() {
		return this.question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return this.answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}


	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}


	public void setGroupList(List<Group> groupList) {
		this.groupList = groupList;
	}

	public String getOfficeTel() {
		return officeTel;
	}

	public void setOfficeTel(String officeTel) {
		this.officeTel = officeTel;
	}
	
	public String getGroupNames() {
		return groupNames;
	}

	public void setGroupNames(String groupNames) {
		this.groupNames = groupNames;
	}

	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}

	public String getHeadShip() {
		return headShip;
	}

	public void setHeadShip(String headShip) {
		this.headShip = headShip;
	}
	

	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getJoinCompDate() {
		return joinCompDate;
	}

	public void setJoinCompDate(Date joinCompDate) {
		this.joinCompDate = joinCompDate;
	}
	public Set<OutlineTeamMember> getDevMemb() {
		return devMemb;
	}

	public void setDevMemb(Set<OutlineTeamMember> devMemb) {
		this.devMemb = devMemb;
	}
	
	public Set<TaskUseActor> getTaskUseActors() {
		return taskUseActors;
	}

	public void setTaskUseActors(Set<TaskUseActor> taskUseActors) {
		this.taskUseActors = taskUseActors;
	}
	public Long getDocUserId() {
		return docUserId;
	}

	public void setDocUserId(Long docUserId) {
		this.docUserId = docUserId;
	}
	
	public String getSvn() {
		return svn;
	}

	public void setSvn(String svn) {
		this.svn = svn;
	}

	public String getChgPwdFlg() {
		return chgPwdFlg;
	}

	public void setChgPwdFlg(String chgPwdFlg) {
		this.chgPwdFlg = chgPwdFlg;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof User))
			return false;
		User castOther = (User) other;

		return ((this.getId() == castOther.getId()) || (this.getId() != null
				&& castOther.getId() != null && this.getId().equals(
				castOther.getId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());

		return result;
	}

	public List<Group> getGroupList() {
		return groupList;
	}
	public void toString(StringBuffer sbf){
		sbf.append("{");
		sbf.append("id:'");
		sbf.append(getId());
		//append data start 
		sbf.append("',data: [0,'','");
		sbf.append(name);
		sbf.append("','");
		sbf.append(employeeId==null?"":employeeId);
		sbf.append("','");
		sbf.append(getLoginName());
		sbf.append("','");
		sbf.append((tel == null ? "" : tel));
		sbf.append("','");
		sbf.append((officeTel == null ? "" : officeTel));
		sbf.append("','");
		sbf.append((headShip == null ? "" : headShip));
		sbf.append("','");
		sbf.append(status==0?"禁用":"启用	");
		sbf.append("'");
		sbf.append("]");
		//append data end  
		sbf.append("}");
	}
	public String toString(){
		StringBuffer sbf = new StringBuffer();
		sbf.append("{");
		sbf.append("id:'");
		sbf.append(getId());
		//append data start 
		sbf.append("',data: [0,'','");
		sbf.append((name == null) ? "" : name);
		sbf.append("','");
		sbf.append((employeeId == null) ? "" : employeeId);
		sbf.append("','");
		sbf.append((getLoginName() == null ? "" : getLoginName() ));
		sbf.append("','");
		sbf.append((tel == null ? "" : tel));
		sbf.append("','");
		sbf.append((officeTel == null ? "" : officeTel));
		sbf.append("','");
		sbf.append((email == null ? "" : email));
		sbf.append("'");
		sbf.append("]");
		//append data end  
		sbf.append("}");
		return sbf.toString();
	}

	public String toStrList(){
		StringBuffer sbf = new StringBuffer();
		sbf.append("{");
		sbf.append("id:'");
		sbf.append(getId());
		//append data start 
		sbf.append("',data: [0,'','");
		sbf.append( name);
		sbf.append("','");
		sbf.append(employeeId==null?"":employeeId);
		sbf.append("','");
		sbf.append(getLoginName());
		sbf.append("','");
		sbf.append((tel == null ? "" : tel));
		sbf.append("','");
		sbf.append((officeTel == null ? "" : officeTel));
		sbf.append("','");
		sbf.append( email);
		sbf.append("','");
		sbf.append((headShip == null ? "" : headShip));
		sbf.append("','");
		sbf.append(status==0?"禁用":"启用	");
		sbf.append("','");
		sbf.append(super.getIsAdmin()==null?"0":super.getIsAdmin()==0?"0":"2");
		sbf.append("'");
		sbf.append("]");
		//append data end  
		sbf.append("}");
		return sbf.toString();
	}
	
	public String toStrUpdateInit(){
		StringBuffer sbf = new StringBuffer();
		sbf.append("userId=").append(getId());
		sbf.append("^");
		sbf.append("loginName=").append(getLoginName());
		sbf.append("^");
		sbf.append("oldPwd=").append("4567KK6slygjk");
		sbf.append("^");
		sbf.append("password=").append("4567KK6slygjk");
		sbf.append("^");
		sbf.append("vpassword=").append("4567KK6slygjk");
		sbf.append("^");
		sbf.append("voldPwd=").append("4567KK6slygjk");
		sbf.append("^");
		sbf.append("headShip=").append((headShip == null) ? "" : headShip);
		sbf.append("^");
		sbf.append("name=").append(name);
		sbf.append("^");
		sbf.append("employeeId=").append(employeeId == null ? "" : employeeId);
		sbf.append("^");
		sbf.append("tel=").append(tel == null ? "" : tel);
		sbf.append("^");
		sbf.append("officeTel=").append(officeTel == null ? "" : officeTel);
		sbf.append("^");
		sbf.append("email=").append((email == null) ? "" : email);
		sbf.append("^");
		sbf.append("groupNames=").append((groupNames == null) ? "" : groupNames);
		sbf.append("^");
		sbf.append("groupIds=").append((groupIds == null) ? "" : groupIds);
		sbf.append("^");
		sbf.append("isAdmin=").append(getIsAdmin() == null ? 0 : getIsAdmin().toString());
		sbf.append("^");
		sbf.append("status=").append(status == null ? 0 : status.toString());
		sbf.append("^");
		sbf.append("delFlag=").append(delFlag == null ? 0 : delFlag.toString());
		sbf.append("^");
		sbf.append("question=").append(question == null ? "" : question);		
		sbf.append("^");
		sbf.append("answer=").append(answer == null ? "" : answer);	
		return sbf.toString();
	}
	
	public String toStrUpdateRest(){
		StringBuffer sbf = new StringBuffer();
		sbf.append(super.getId());
		sbf.append("^");
		sbf.append("0,,");
		sbf.append( name);
		sbf.append(",");
		sbf.append((employeeId == null) ? "" : employeeId);
		sbf.append(",");
		sbf.append(getLoginName());
		sbf.append(",");
		sbf.append((tel == null ? "" : tel));
		sbf.append(",");
		sbf.append((officeTel == null ? "" : officeTel));
		sbf.append(",");
		sbf.append(email);
		sbf.append(",");
		sbf.append(headShip==null?"":headShip);	
		sbf.append(",");
		sbf.append(status==0?"禁用":"启用");
		sbf.append(",");
		sbf.append(super.getIsAdmin()==null?"0":super.getIsAdmin()==0?"0":"2");
		return sbf.toString();
	}

}