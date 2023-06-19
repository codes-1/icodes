package cn.com.codes.framework.security;

import java.util.Set;

public class VisitUser {

	private String id;
	private Integer isAdmin;
	private String myHome;
	private String name;
	private String companyId;
	private String loginName;
	private Set<String> privilege;
	private Set<String> repPrivilege;
	private Long docUserId;
	
	
//	private String timezone;
//	private Locale locale;
//	private String language;
//	
	//private boolean haveLoginDoc ;
	
	public Set<String> getPrivilege() {
		return privilege;
	}
	
	public String getId2() {
		StringBuilder sbd = new StringBuilder();
		for(int i=id.length()-1;i>=0;i--){
			sbd.append(id.charAt(i));
	 }  
		return sbd.toString();
	}
	public void setPrivilege(Set<String> privilege) {
		this.privilege = privilege;
	}

	public Integer getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(Integer isAdmin) {
		this.isAdmin = isAdmin;
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
	public String getMyHome() {
		return myHome;
	}
	public void setMyHome(String myHome) {
		this.myHome = myHome;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUniqueName(){
		StringBuffer sb = new StringBuffer();
		sb.append(loginName);
		sb.append("(").append(name).append(")");
		return sb.toString();
	}
	public Long getDocUserId() {
		return docUserId;
	}
	public void setDocUserId(Long docUserId) {
		this.docUserId = docUserId;
	}

	public Set<String> getRepPrivilege() {
		return repPrivilege;
	}

	public void setRepPrivilege(Set<String> repPrivilege) {
		this.repPrivilege = repPrivilege;
	}

//	public String getTimezone() {
//		return timezone;
//	}
//
//
//	public void setTimezone(String timezone) {
//		this.timezone = timezone;
//	}
//
//
//	public Locale getLocale() {
//		return locale;
//	}
//
//
//	public void setLocale(Locale locale) {
//		this.locale = locale;
//	}
//
//
//	public String getLanguage() {
//		return language;
//	}
//
//
//	public void setLanguage(String language) {
//		this.language = language;
//	}


//	public boolean isHaveLoginDoc() {
//		return haveLoginDoc;
//	}
//
//
//	public void setHaveLoginDoc(boolean haveLoginDoc) {
//		this.haveLoginDoc = haveLoginDoc;
//	}

}
