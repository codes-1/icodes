package cn.com.codes.object;

import java.util.Date;

import cn.com.codes.framework.transmission.JsonInterface;
import cn.com.codes.object.ModuleVerRec;



public class ModuleVerRec implements  JsonInterface {



	private Long verRecId ;
	private Date creatdate;
	private Date insdate;
	private Date upddate;
	private String taskId;
	private Long moduleId;
	private Integer seq ;
	private String moduleVersion;
	

	public ModuleVerRec() {
	}

	public ModuleVerRec(Long  verRecId) {
		this.verRecId = verRecId;
		
	}
	public ModuleVerRec(Integer seq,String moduleVersion) {
		this.moduleVersion = moduleVersion;
		this.seq = seq;
	}
	public ModuleVerRec(Long  verRecId,Integer seq ,String moduleVersion) {
		this.verRecId = verRecId;
		this.seq = seq;
		this.moduleVersion = moduleVersion;
	}
	public Long getVerRecId() {
		return this.verRecId;
	}

	public void setVerRecId(Long  verRecId) {
		this.verRecId = verRecId;
	}

	public Date getCreatdate() {
		return this.creatdate;
	}

	public void setCreatdate(Date creatdate) {
		this.creatdate = creatdate;
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


	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getModuleVersion() {
		return moduleVersion;
	}

	public void setModuleVersion(String moduleVersion) {
		this.moduleVersion = moduleVersion;
	}
	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public ModuleVerRec copyToNew(Long moudleId){
		ModuleVerRec mv = new ModuleVerRec();
		mv.setCreatdate(this.getCreatdate());
		mv.setTaskId(this.getTaskId());
		mv.setModuleVersion(this.getModuleVersion());
		mv.setSeq(this.getSeq());
		mv.setModuleId(moudleId);
		return mv;
	}
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ModuleVerRec))
			return false;
		ModuleVerRec castOther = (ModuleVerRec) other;

		return ((this.getTaskId() == castOther.getTaskId()) || (this
				.getTaskId() != null
				&& castOther.getTaskId() != null && this.getTaskId().equals(
				castOther.getTaskId())))&& ((this.getModuleId() == castOther.getModuleId()) || (this
						.getModuleId() != null
						&& castOther.getModuleId() != null && this
						.getModuleId().equals(castOther.getModuleId())))
				&& ((this.getModuleVersion() == castOther.getModuleVersion()) || (this
						.getModuleVersion() != null
						&& castOther.getModuleVersion() != null && this
						.getModuleVersion()
						.equals(castOther.getModuleVersion())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getTaskId() == null ? 0 : this.getTaskId().hashCode());
		result = 37 * result
				+ (getModuleId() == null ? 0 : this.getModuleId().hashCode());
		result = 37
				* result
				+ (getModuleVersion() == null ? 0 : this.getModuleVersion()
						.hashCode());
		return result;
	}

	   public String toStrList(){
	        StringBuffer sbf = new StringBuffer();
	        sbf.append("{");
	        sbf.append("id:'");
	        sbf.append(getVerRecId()==null?"":getVerRecId().toString());
	        sbf.append("',data: [0,'','");
	        sbf.append(seq == null ? "" : seq );
	        sbf.append("','");
	        sbf.append(moduleVersion == null ? "" : moduleVersion );
	        sbf.append("'");
	        sbf.append("]");
	        sbf.append("}");
	        return sbf.toString();
	    }

	   public String toStrUpdateInit(){
		   return "";
	    }

	   public String toStrUpdateRest(){
	        return "";
	     }

	   public void toString(StringBuffer sbf){

	    }
}
