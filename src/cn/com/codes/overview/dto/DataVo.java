package cn.com.codes.overview.dto;

public class DataVo {
	/*用例数量统计*/
	private int allCount = 0;
	private int passCount = 0;
	private int failedCount = 0;
	private int blockCount = 0;
	private int noTestCount = 0;
	private int invalidCount = 0;
	private int waitModifyCount = 0;
	private int waitAuditCount = 0;
	
	//已改未确认
	private int fixNoConfirmCount = 0;
	//非错 未确认
	private int noBugNoConfirmCount = 0;
	/*bug数量统计*/
	private int bugAllCount = 0;
	private int bugValidCout = 0;
	private int bugResolCount = 0;
	
	/*任务统计*/
	private int meCharge = 0;
	private int meJoin = 0;
	private int meCreate = 0;
	private int meAll = 0;
	
	public int getAllCount() {
		return allCount;
	}
	public void setAllCount(int allCount) {
		this.allCount = allCount;
	}
	public int getPassCount() {
		return passCount;
	}
	public void setPassCount(int passCount) {
		this.passCount = passCount;
	}
	public int getFailedCount() {
		return failedCount;
	}
	public void setFailedCount(int failedCount) {
		this.failedCount = failedCount;
	}
	public int getBlockCount() {
		return blockCount;
	}
	public void setBlockCount(int blockCount) {
		this.blockCount = blockCount;
	}
	public int getNoTestCount() {
		return noTestCount;
	}
	public void setNoTestCount(int noTestCount) {
		this.noTestCount = noTestCount;
	}
	public int getInvalidCount() {
		return invalidCount;
	}
	public void setInvalidCount(int invalidCount) {
		this.invalidCount = invalidCount;
	}
	public int getWaitModifyCount() {
		return waitModifyCount;
	}
	public void setWaitModifyCount(int waitModifyCount) {
		this.waitModifyCount = waitModifyCount;
	}
	public int getWaitAuditCount() {
		return waitAuditCount;
	}
	public void setWaitAuditCount(int waitAuditCount) {
		this.waitAuditCount = waitAuditCount;
	}
	public int getBugAllCount() {
		return bugAllCount;
	}
	public void setBugAllCount(int bugAllCount) {
		this.bugAllCount = bugAllCount;
	}
	public int getBugValidCout() {
		return bugValidCout;
	}
	public void setBugValidCout(int bugValidCout) {
		this.bugValidCout = bugValidCout;
	}
	public int getBugResolCount() {
		return bugResolCount;
	}
	public void setBugResolCount(int bugResolCount) {
		this.bugResolCount = bugResolCount;
	}
	public int getMeCharge() {
		return meCharge;
	}
	public void setMeCharge(int meCharge) {
		this.meCharge = meCharge;
	}
	public int getMeJoin() {
		return meJoin;
	}
	public void setMeJoin(int meJoin) {
		this.meJoin = meJoin;
	}
	public int getMeCreate() {
		return meCreate;
	}
	public void setMeCreate(int meCreate) {
		this.meCreate = meCreate;
	}
	public int getMeAll() {
		return meAll;
	}
	public void setMeAll(int meAll) {
		this.meAll = meAll;
	}
	public int getFixNoConfirmCount() {
		return fixNoConfirmCount;
	}
	public void setFixNoConfirmCount(int fixNoConfirmCount) {
		this.fixNoConfirmCount = fixNoConfirmCount;
	}
	public int getNoBugNoConfirmCount() {
		return noBugNoConfirmCount;
	}
	public void setNoBugNoConfirmCount(int noBugNoConfirmCount) {
		this.noBugNoConfirmCount = noBugNoConfirmCount;
	}
}