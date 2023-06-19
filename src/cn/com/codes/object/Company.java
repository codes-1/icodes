package cn.com.codes.object;

import java.util.Date;

import cn.com.codes.framework.common.Global;
import cn.com.codes.framework.transmission.JsonInterface;


public class Company implements  JsonInterface{
	
	private String id;
	private Integer type;
	private Integer compSize;
	private Integer status;
	private String owner;
	private String ownereMail;
	private String ownerTel;
	private String telephone;
	private String fax;
	private String address;
	private String url;
	private Date startTime;
	private Date endTime;
	private Date extendTime;
	private String name;
	private String remark;
	private Date insertDate;
	private Date updateDate;
	private String loginName;
	private Integer delFlag = 0;
	private Integer knowWay;
	private Integer accountType;
	private String regisIp;
	private Long docRootDir;
	private Long docProRootDir;
	
	public Company() {
	}
	public Company(String loginName) {
		this.loginName = loginName;
	}
	public Company(String id,Long docRootDir,Long docProRootDir) {
		this.id = id;
		this.docRootDir = docRootDir;
		this.docProRootDir = docProRootDir;
	}
	public String getRegisIp() {
		return regisIp;
	}
	public void setRegisIp(String regisIp) {
		this.regisIp = regisIp;
	}
	
	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	
	public Integer getAccountType() {
		return accountType;
	}
	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}
	
	public Integer getKnowWay() {
		return knowWay;
	}
	public void setKnowWay(Integer knowWay) {
		this.knowWay = knowWay;
	}
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public Integer getCompSize() {
		return compSize;
	}
	
	public void setCompSize(Integer compSize) {
		this.compSize = compSize;
	}

	public String getOwnereMail() {
		return ownereMail;
	}

	public void setOwnereMail(String ownereMail) {
		this.ownereMail = ownereMail;
	}

	public String getOwnerTel() {
		return ownerTel;
	}

	public void setOwnerTel(String ownerTel) {
		this.ownerTel = ownerTel;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getExtendTime() {
		return extendTime;
	}

	public void setExtendTime(Date extendTime) {
		this.extendTime = extendTime;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	public void toString(StringBuffer bf){
	}
	
	public String toStrUpdateInit() {
		StringBuffer sbf = new StringBuffer();
		sbf.append("companyId="+id);
		sbf.append("^");
		sbf.append("name=" +name);
		sbf.append("^");
		sbf.append("companyType=" +type);
		sbf.append("^");
		sbf.append("companySize="+compSize);
		sbf.append("^");
		sbf.append("owner="+owner);
		sbf.append("^");
		sbf.append("loginName="+loginName);
		sbf.append("^");
		sbf.append("ownereMail=" +ownereMail);
		sbf.append("^");
		sbf.append("ownerTel="+ownerTel);
		sbf.append("^");
		sbf.append("telephone="+telephone);
		sbf.append("^");
		sbf.append("fax="+fax);
		sbf.append("^");
		sbf.append("address="+address);
		sbf.append("^");
		sbf.append("url="+url);
		sbf.append("^");
		sbf.append("remark="+remark);
		sbf.append("^");
		sbf.append("status="+status);
		return sbf.toString();
	}
	
	public String toStrList(){
		StringBuffer sbf = new StringBuffer();
		sbf.append("{");
		sbf.append("id:'");
		sbf.append(id);
		//append data start 
		sbf.append("',data: [0,'','");
		sbf.append((name == null) ? "" : name);
		sbf.append("','");
		sbf.append((type == null) ? "" : Global.getCompTypeByValue(type.toString()).trim());
		sbf.append("','");
		sbf.append((compSize == null) ? "" : Global.getCompSizeByValue(compSize.toString()));
		sbf.append("','");
		sbf.append((owner));
		sbf.append("','");
		sbf.append(loginName);
		sbf.append("','");
		sbf.append((ownereMail == null ? "" : ownereMail));
		sbf.append("','");
		sbf.append((ownerTel == null ? "" : ownerTel));
		sbf.append("','");
		sbf.append((telephone == null ? "" : telephone));
		sbf.append("','");
		sbf.append((fax == null ? "" : fax));
		sbf.append("','");
		sbf.append((address == null ? "" : address));
		sbf.append("','");
		sbf.append((url == null ? "" : url));
		sbf.append("','");
		sbf.append((remark == null ? "" : remark));
		sbf.append("','");
		sbf.append((status == null ? "" : Global.getCompStatusByValue(status.toString())));
		sbf.append("'");
		sbf.append("]");
		//append data end  
		sbf.append("}");
		return sbf.toString();
	}
	
	public String toStrUpdateRest(){
		StringBuffer sbf = new StringBuffer();
		sbf.append(id);
		sbf.append("^");
		sbf.append("0,,");
		sbf.append(name);
		sbf.append(",");
		sbf.append((type == null) ? "null" : cn.com.codes.framework.common.Global.getCompTypeByValue(type.toString()));
		sbf.append(",");
		sbf.append((compSize == null) ? "null" : cn.com.codes.framework.common.Global.getCompSizeByValue(compSize.toString()));
		sbf.append(",");
		sbf.append((owner));
		sbf.append(",");
		sbf.append(loginName);
		sbf.append(",");
		sbf.append(ownereMail);
		sbf.append(",");
		sbf.append( ownerTel);
		sbf.append(",");
		sbf.append(telephone);
		sbf.append(",");
		sbf.append(fax);
		sbf.append(",");
		sbf.append(address);
		sbf.append(",");
		sbf.append(url);
		sbf.append(",");
		sbf.append(remark);
		sbf.append(",");
		sbf.append((status == null ? "null" : Global.getCompStatusByValue(status.toString())));
		return sbf.toString();
	}
	public Long getDocRootDir() {
		return docRootDir;
	}
	public void setDocRootDir(Long docRootDir) {
		this.docRootDir = docRootDir;
	}
	public Long getDocProRootDir() {
		return docProRootDir;
	}
	public void setDocProRootDir(Long docProRootDir) {
		this.docProRootDir = docProRootDir;
	}



}