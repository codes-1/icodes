package cn.com.codes.licenseMgr.dto;

import cn.com.codes.framework.transmission.dto.BaseDto;

public class LicesneMgrDto extends BaseDto {

	private String machineCode;//机器码 既CPU号
	private String markCode;//标识号 既网卡号
	private String activeCode;
	private String companyName;
	private String telephone;
	private String comAddress;
	private String comWebSite;
	private String mailAddress;
	private String owner;
    private String comSize;
    private String useCount;
    private String currUseCount;
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getComAddress() {
		return comAddress;
	}

	public void setComAddress(String comAddress) {
		this.comAddress = comAddress;
	}

	public String getComWebSite() {
		return comWebSite;
	}

	public void setComWebSite(String comWebSite) {
		this.comWebSite = comWebSite;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getActiveCode() {
		return activeCode==null?"":activeCode;
	}

	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}

	public String getMachineCode() {
		return machineCode==null?"":machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public String getMarkCode() {
		return markCode;
	}

	public void setMarkCode(String markCode) {
		this.markCode = markCode;
	}

	public String getComSize() {
		return comSize;
	}

	public void setComSize(String comSize) {
		this.comSize = comSize;
	}

	public String getUseCount() {
		return useCount;
	}

	public void setUseCount(String useCount) {
		this.useCount = useCount;
	}

	public String getCurrUseCount() {
		return currUseCount;
	}

	public void setCurrUseCount(String currUseCount) {
		this.currUseCount = currUseCount;
	}

}
