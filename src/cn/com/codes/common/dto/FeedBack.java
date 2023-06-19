package cn.com.codes.common.dto;

import java.util.Date;

public class FeedBack {

	private Long feedBackId;
	private String fdDesc;
	private String fdReProStep;
	private String remark;
	private String reptId;
	private String fdNum;
	private String repName;
	private String attachUrl;
	private Date insertDate;
	private Date updateDate;
	private String handlerId;
    private String customName;
    private Long customId;
    private String compId;
	public FeedBack(){
		
	}
	public FeedBack(Long feedBackId,String fdNum,String reptId,Date insertDate,
			String remark,Date updateDate,String attachUrl,String fdDesc,String handlerId){
		this.feedBackId = feedBackId;
		this.fdNum = fdNum;
		this.reptId = reptId;
		this.insertDate = insertDate;
		this.remark = remark;
		this.updateDate = updateDate;
		this.attachUrl = attachUrl;
		this.fdDesc = fdDesc;
		this.handlerId = handlerId;
	}
	public FeedBack(Long feedBackId,String fdNum,String reptId,Date insertDate,
			String remark,Date updateDate,String attachUrl,String fdDesc,String handlerId,String customName){
		this.feedBackId = feedBackId;
		this.fdNum = fdNum;
		this.reptId = reptId;
		this.insertDate = insertDate;
		this.remark = remark;
		this.updateDate = updateDate;
		this.attachUrl = attachUrl;
		this.fdDesc = fdDesc;
		this.handlerId = handlerId;
		this.customName = customName;
	}
	public FeedBack(Long feedBackId,String fdNum,String reptId,Date insertDate,
			String remark,Date updateDate,String attachUrl,String fdDesc,String handlerId,Long customId){
		this.feedBackId = feedBackId;
		this.fdNum = fdNum;
		this.reptId = reptId;
		this.insertDate = insertDate;
		this.remark = remark;
		this.updateDate = updateDate;
		this.attachUrl = attachUrl;
		this.fdDesc = fdDesc;
		this.handlerId = handlerId;
		this.customId = customId;
	}
	public Long getFeedBackId() {
		return feedBackId;
	}

	public void setFeedBackId(Long feedBackId) {
		this.feedBackId = feedBackId;
	}

	public String getFdDesc() {
		return fdDesc;
	}

	public void setFdDesc(String fdDesc) {
		this.fdDesc = fdDesc;
	}

	public String getFdReProStep() {
		return fdReProStep;
	}

	public void setFdReProStep(String fdReProStep) {
		this.fdReProStep = fdReProStep;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReptId() {
		return reptId;
	}

	public void setReptId(String reptId) {
		this.reptId = reptId;
	}

	public String getAttachUrl() {
		return attachUrl;
	}

	public void setAttachUrl(String attachUrl) {
		this.attachUrl = attachUrl;
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

	public String getFdNum() {
		return fdNum;
	}

	public void setFdNum(String fdNum) {
		this.fdNum = fdNum;
	}

	public String getRepName() {
		return repName;
	}

	public void setRepName(String repName) {
		this.repName = repName;
	}

	public String getHandlerId() {
		return handlerId;
	}
	public void setHandlerId(String handlerId) {
		this.handlerId = handlerId;
	}
	public String getCustomName() {
		return customName;
	}
	public void setCustomName(String customName) {
		this.customName = customName;
	}
	public Long getCustomId() {
		return customId;
	}
	public void setCustomId(Long customId) {
		this.customId = customId;
	}
	public String getCompId() {
		return compId;
	}
	public void setCompId(String compId) {
		this.compId = compId;
	}

}
