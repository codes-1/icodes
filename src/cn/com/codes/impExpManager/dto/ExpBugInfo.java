package cn.com.codes.impExpManager.dto;

import java.util.Date;

public class ExpBugInfo {

	private String bugcardId;
	private String bugDesc;
	private String reproductTxt;
	private int currState;
	private String levelName;
	private String typeName;
	private String occaName;
	private String reptName;
	private Date reptDate;
	private String disVer;
	private String fixName;
	private Date fixDate;
	private String clsName;
	private Date clsDate;
	private String reslVer;
	private  String modulename;
	private  String superModuleName;
	private String currName;
	public String getBugcardId() {
		return bugcardId;
	}

	public void setBugcardId(String bugcardId) {
		this.bugcardId = bugcardId;
	}

	public String getBugDesc() {
		return bugDesc;
	}

	public void setBugDesc(String bugDesc) {
		this.bugDesc = bugDesc;
	}

	public String getReproductTxt() {
		return reproductTxt;
	}

	public void setReproductTxt(String reproductTxt) {
		this.reproductTxt = reproductTxt;
	}

	public int getCurrState() {
		return currState;
	}

	public void setCurrState(int currState) {
		this.currState = currState;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getOccaName() {
		return occaName;
	}

	public void setOccaName(String occaName) {
		this.occaName = occaName;
	}

	public String getReptName() {
		return reptName;
	}

	public void setReptName(String reptName) {
		this.reptName = reptName;
	}

	public Date getReptDate() {
		return reptDate;
	}

	public void setReptDate(Date reptDate) {
		this.reptDate = reptDate;
	}

	public String getDisVer() {
		return disVer;
	}

	public void setDisVer(String disVer) {
		this.disVer = disVer;
	}

	public String getFixName() {
		return fixName;
	}

	public void setFixName(String fixName) {
		this.fixName = fixName;
	}

	public Date getFixDate() {
		return fixDate;
	}

	public void setFixDate(Date fixDate) {
		this.fixDate = fixDate;
	}

	public String getClsName() {
		return clsName;
	}

	public void setClsName(String clsName) {
		this.clsName = clsName;
	}

	public Date getClsDate() {
		return clsDate;
	}

	public void setClsDate(Date clsDate) {
		this.clsDate = clsDate;
	}

	public String getReslVer() {
		return reslVer;
	}

	public void setReslVer(String reslVer) {
		this.reslVer = reslVer;
	}

	public String getModulename() {
		return modulename;
	}

	public void setModulename(String modulename) {
		//System.out.println(modulename);
		this.modulename = modulename;
	}

	public String getSuperModuleName() {
		return superModuleName;
	}

	public void setSuperModuleName(String superModuleName) {
		//System.out.println(superModuleName);
		this.superModuleName = superModuleName;
	}

	public String getCurrName() {
		return currName;
	}

	public void setCurrName(String currName) {
		this.currName = currName;
	}
}
