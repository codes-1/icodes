package cn.com.codes.object;

import java.util.Date;
import java.util.Set;

import cn.com.codes.framework.transmission.JsonInterface;
import cn.com.codes.object.BugBaseInfo;

public class SoftwareVersion implements JsonInterface {

	private Long versionId;
	private String taskid;
	private String versionNum;
	private Date insdate;
	private Date upddate;
	private String remark;
	private Integer seq;
	private Integer verStatus;
	private Set<BugBaseInfo> bugs;

	public SoftwareVersion() {
	}

	public SoftwareVersion(String versionNum) {
		this.versionNum = versionNum;
	}
	public SoftwareVersion(Long versionId,String versionNum) {
		this.versionId = versionId;
		this.versionNum = versionNum;
	}
	
	public SoftwareVersion(Long versionId, String versionNum, String remark,
			Integer seq,Integer verStatus) {
		this.versionId = versionId;
		this.versionNum = versionNum;
		this.remark = remark;
		this.seq = seq;
		this.verStatus = verStatus;
	}
	
	public SoftwareVersion(String taskid,Long versionId,String versionNum,String remark,
			Integer seq,Integer verStatus,Date insdate,Date upddate) {
		this.taskid = taskid;
		this.versionNum = versionNum;
		this.insdate = insdate;
		this.upddate = upddate;
		this.remark = remark;
		this.seq = seq;
		this.verStatus = verStatus;
		this.versionId = versionId;
	}

	public Long getVersionId() {
		return this.versionId;
	}

	public void setVersionId(Long versionId) {
		this.versionId = versionId;
	}

	public String getTaskid() {
		return this.taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getVersionNum() {
		return this.versionNum;
	}

	public void setVersionNum(String versionNum) {
		this.versionNum = versionNum;
	}

	public Date getInsdate() {
		return this.insdate;
	}

	public void setInsdate(Date insdate) {
		this.insdate = insdate;
	}

	public Date getUpddate() {
		return this.upddate;
	}

	public void setUpddate(Date upddate) {
		this.upddate = upddate;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSeq() {
		return this.seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Integer getVerStatus() {
		return verStatus;
	}

	public void setVerStatus(Integer verStatus) {
		this.verStatus = verStatus;
	}

	public Set<BugBaseInfo> getBugs() {
		return bugs;
	}

	public void setBugs(Set<BugBaseInfo> bugs) {
		this.bugs = bugs;
	}
	
	public String toStrList() {
		StringBuffer sbf = new StringBuffer();
		sbf.append("{");
		sbf.append("id:'");
		sbf.append(getVersionId().toString());
		sbf.append("',data: [0,'','");
		sbf.append(versionNum == null ? "" : versionNum);
		sbf.append("','");
		sbf.append(seq == null ? "" : seq);
		sbf.append("','");
		sbf.append(remark == null ? "" : remark);
		sbf.append("','");
		sbf.append(verStatus == 0 ? "启用" : "停用");
		sbf.append("'");
		sbf.append("]");
		sbf.append("}");
		return sbf.toString();
	}

	public String toStrUpdateInit() {
		return "";
	}

	public String toStrUpdateRest() {
		return "";
	}

	public void toString(StringBuffer sbf) {
		sbf.append("{");
		sbf.append("id:'");
		sbf.append(getVersionId().toString());
		sbf.append("',data: [0,'','");
		sbf.append(versionNum == null ? "" : versionNum);
		sbf.append("','");
		sbf.append(seq == null ? "" : seq);
		sbf.append("','");
		sbf.append(remark == null ? "" : remark);
		sbf.append("','");
		sbf.append(verStatus == 0 ? "启用" : "停用");
		sbf.append("'");
		sbf.append("]");
		sbf.append("}");
	}



}