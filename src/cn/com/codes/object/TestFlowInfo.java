package cn.com.codes.object;

import java.io.Serializable;
import java.util.Date;

import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.transmission.JsonInterface;
import cn.com.codes.object.TestFlowInfo;



public class TestFlowInfo implements JsonInterface,Serializable {



	private String testFlowId;
	private String testFlowName;
	private Date insdate;
	private Date upddate;
	private String taskId;
	private Integer testFlowCode;


	public TestFlowInfo() {
	}
	public TestFlowInfo(String testFlowId) {
		this.testFlowId = testFlowId;
	}

	//投影查询用
	public TestFlowInfo(Integer testFlowCode){
		this.testFlowCode = testFlowCode;
	}

	public TestFlowInfo(String taskId,Integer testFlowCode) {
		this.taskId = taskId;
		this.testFlowCode = testFlowCode;
	}

	public String getTestFlowName() {
		return this.testFlowName;
	}

	public void setTestFlowName(String testFlowName) {
		this.testFlowName = testFlowName;
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




	public String getTestFlowId() {
		return testFlowId;
	}


	public void setTestFlowId(String testFlowId) {
		this.testFlowId = testFlowId;
	}


	public String getTaskId() {
		return taskId;
	}


	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}


	public Integer getTestFlowCode() {
		return testFlowCode;
	}


	public void setTestFlowCode(Integer testFlowCode) {
		this.testFlowCode = testFlowCode;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TestFlowInfo))
			return false;
		TestFlowInfo castOther = (TestFlowInfo) other;

		return ((this.getTaskId() == castOther.getTaskId()) || (this
				.getTaskId() != null
				&& castOther.getTaskId() != null && this.getTaskId().equals(
				castOther.getTaskId())))
				&& ((this.getTestFlowCode() == castOther.getTestFlowCode()) || (this
						.getTestFlowCode() != null
						&& castOther.getTestFlowCode() != null && this
						.getTestFlowCode().equals(castOther.getTestFlowCode())));
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result
				+ (getTaskId() == null ? 0 : this.getTaskId().hashCode());
		result = 37
				* result
				+ (getTestFlowCode() == null ? 0 : this.getTestFlowCode()
						.hashCode());
		return result;
	}
	public String toStrList(){
	        StringBuffer sbf = new StringBuffer();
	        sbf.append("{");
	        sbf.append("id:'");
	        sbf.append(getTestFlowId().toString());
	        sbf.append("',data: [0,'','");
	        sbf.append(testFlowName == null ? "" : testFlowName );
	        sbf.append("'");
	        sbf.append(insdate == null ? "" : StringUtils.formatShortDate(insdate ));
	        sbf.append("'");
	        sbf.append(upddate == null ? "" : StringUtils.formatShortDate(upddate ));
	        sbf.append("'");
	        sbf.append("]");
	        sbf.append("}");
	        return sbf.toString();
	  }

	 public String toStrUpdateInit(){
        StringBuffer sbf = new StringBuffer();
        sbf.append(getTestFlowId().toString());
        sbf.append("^");
        sbf.append("testFlowName=").append(testFlowName == null ? "" : testFlowName);
        sbf.append("^");
        sbf.append("insdate=").append(insdate == null ? "" : StringUtils.formatShortDate(insdate));
        sbf.append("^");
        sbf.append("upddate=").append(upddate == null ? "" : StringUtils.formatShortDate(upddate));
        return sbf.toString();
	    }

	 public String toStrUpdateRest(){
        StringBuffer sbf = new StringBuffer();
        sbf.append(getTestFlowId().toString());
        sbf.append("0,,");
        sbf.append(testFlowName == null ? "" : testFlowName );
        sbf.append("'");
        sbf.append(insdate == null ? "" : StringUtils.formatShortDate(insdate ));
        sbf.append("'");
        sbf.append(upddate == null ? "" : StringUtils.formatShortDate(upddate ));
        return sbf.toString();
	 }
	 public void toString(StringBuffer bf){
		   
	 }
}