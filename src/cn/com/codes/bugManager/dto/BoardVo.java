package cn.com.codes.bugManager.dto;

import cn.com.codes.framework.transmission.JsonInterface;

public class BoardVo implements JsonInterface {

	private String userName;
	//处理用例数
	private Integer whCount;
	
	//今日处理用例数
	private Integer hCount;
    //待处理bug数
	private Integer bwhCount;
	
	//今日处理bug次数
	private Integer bhCount;
	
	private String loginName;
	
	private String teamActor;
	//whCount',title:'待处理bug数',
	//'hcount',title:'今日处理bug次数
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getWhCount() {
		return whCount;
	}

	public void setWhCount(Integer whCount) {
		this.whCount = whCount;
	}

	public Integer getHCount() {
		return hCount;
	}

	public void setHCount(Integer count) {
		hCount = count;
	}

	public Integer getBwhCount() {
		return bwhCount;
	}

	public void setBwhCount(Integer bwhCount) {
		this.bwhCount = bwhCount;
	}

	public Integer getBhCount() {
		return bhCount;
	}

	public void setBhCount(Integer bhCount) {
		this.bhCount = bhCount;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	public String toStrList() {
		return null;
	}

	public String toStrUpdateInit() {
		return null;
	}

	public String toStrUpdateRest() {
		return null;
	}

	public void toString(StringBuffer sbf) {
		sbf.append("{");
		sbf.append("id:'");
		sbf.append(getUserName());
		sbf.append("',data: [0,'','");
		sbf.append(getUserName());
		sbf.append("','");
		sbf.append(whCount == null ? "0" : whCount);
		sbf.append("','");
		sbf.append(hCount == null ? "0" : hCount);
		sbf.append("','");
		sbf.append(this.loginName);
		sbf.append("'");
		sbf.append("]");
		sbf.append("}");
	}

	public void toString2(StringBuffer sbf) {
		sbf.append("{");
		sbf.append("id:'");
		sbf.append(getUserName());
		sbf.append("',data: [0,'','");
		sbf.append(getUserName());
		sbf.append("','");
		sbf.append(whCount == null ? "0" : whCount);
		sbf.append("','");
		sbf.append(hCount == null ? "0" : hCount);
		sbf.append("','");
		sbf.append(bwhCount == null ? "0" : bwhCount);
		sbf.append("','");
		sbf.append(bhCount == null ? "0" : bhCount);
		sbf.append("','");
		sbf.append(loginName);
		sbf.append("'");
		sbf.append("]");
		sbf.append("}");
	}

	public String getTeamActor() {
		return teamActor;
	}

	public void setTeamActor(String teamActor) {
		this.teamActor = teamActor;
	}



}
