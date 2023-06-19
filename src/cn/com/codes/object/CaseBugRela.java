package cn.com.codes.object;

import cn.com.codes.object.CaseBugRela;

public class CaseBugRela implements java.io.Serializable {

	private Long bugCaseRelaId;
	private String createrId;
	private Long testCaseId;
	private Long bugId;
	public CaseBugRela(Long bugId) {
		this.bugId = bugId;
	}
	public CaseBugRela() {
	}
	public Long getBugCaseRelaId() {
		return bugCaseRelaId;
	}

	public void setBugCaseRelaId(Long bugCaseRelaId) {
		this.bugCaseRelaId = bugCaseRelaId;
	}

	public Long getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(Long testCaseId) {
		this.testCaseId = testCaseId;
	}

	public Long getBugId() {
		return bugId;
	}

	public void setBugId(Long bugId) {
		this.bugId = bugId;
	}

	public String getCreaterId() {
		return createrId;
	}

	public void setCreaterId(String createrId) {
		this.createrId = createrId;
	}
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CaseBugRela))
			return false;
		CaseBugRela castOther = (CaseBugRela) other;
		return ( this.bugId != null
				&& castOther.bugId != null && this.bugId.toString().equals(
						this.bugId.toString())
				&&this.testCaseId != null&&castOther.testCaseId != null && this.testCaseId.toString().equals(
						this.testCaseId.toString())
						
		);
	}
	
	public int hashCode() {
		int result = 17;
		result = 37 * result + (testCaseId == null ? 0 :testCaseId.hashCode());
		result = 37 * result + (bugId == null ? 0 :bugId.hashCode());
		return result;
	}

}