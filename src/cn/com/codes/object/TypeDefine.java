package cn.com.codes.object;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.com.codes.framework.transmission.JsonInterface;

public abstract  class TypeDefine implements JsonInterface {

	private Long typeId;
	private String compId;
	private String typeName;
	private String remark;
	private Integer isDefault;
	private String subName;
	private String status;
	private Date updDate;
	private Integer preference;

	public TypeDefine() {

	}

	public TypeDefine(Long typeId, String typeName) {
		this.typeId = typeId;
		this.typeName = typeName;
	}
	public TypeDefine(Long typeId, String typeName,Integer isDefault,String remark,String status) {
		this.typeId = typeId;
		this.typeName = typeName;
		this.isDefault = isDefault;
		this.remark = remark;
		this.status = status;
		this.setSubName();
	}
	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;	
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public String getSubName() {
		return subName;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getUpdDate() {
		return updDate;
	}

	public void setUpdDate(Date upDate) {
		this.updDate = upDate;
	}
	
	private static Map<String,String> typeNameMap = new HashMap<String,String>(16);
	
	static {
		typeNameMap.put("BugFreq", "BUG频率");
		typeNameMap.put("BugType", "BUG类型");
		typeNameMap.put("BugGrade", "BUG等级");
		typeNameMap.put("BugOpotunity", "BUG发现时机");
		typeNameMap.put("BugPri", "BUG优先级");
		typeNameMap.put("BugSource", "BUG来源");
		typeNameMap.put("CasePri", "用例优先级");
		typeNameMap.put("CaseType", "用例类型");
		typeNameMap.put("GeneCause", "BUG测试时机");
		typeNameMap.put("OccurPlant", "BUG发生平台");
		typeNameMap.put("ImportPhase", "BUG引入原因");
		typeNameMap.put("MissionType", "任务类别");
		typeNameMap.put("EmergencyDegree", "任务紧急程度");
		typeNameMap.put("DifficultyDegree", "任务难易程度");
		//typeNameMap.put("BugFreq", "BUG频率");
		//typeNameMap.put("BugFreq", "BUG频率");
	}
	public void setSubName(){
		
		if(getClass().getSimpleName().equals("BugFreq")){
			this.subName = "BUG频率";
		}else if(this.getClass().getSimpleName().equals("BugType")){
			this.subName = "BUG类型";
 		}else if(this.getClass().getSimpleName().equals("BugGrade")){
 			this.subName = "BUG等级";
		}else if(this.getClass().getSimpleName().equals("BugOpotunity")){
			this.subName = "BUG发现时机";
		}else if(this.getClass().getSimpleName().equals("BugPri")){
			this.subName = "BUG优先级";
		}else if(this.getClass().getSimpleName().equals("BugSource")){
			this.subName = "BUG来源";
		}else if(this.getClass().getSimpleName().equals("CasePri")){
			this.subName = "用例优先级";
		}else if(this.getClass().getSimpleName().equals("CaseType")){
			this.subName = "用例类型";
		}else if(this.getClass().getSimpleName().equals("GeneCause")){
			this.subName = "BUG测试时机";
		}else if(this.getClass().getSimpleName().equals("OccurPlant")){
			this.subName = "BUG发生平台";
		}else if(this.getClass().getSimpleName().equals("ImportPhase")){
			this.subName = "BUG引入原因";
		}else if(this.getClass().getSimpleName().equals("MissionType")){
			this.subName = "任务类别";
		}else if(this.getClass().getSimpleName().equals("EmergencyDegree")){
			this.subName = "任务紧急程度";
		}else if(this.getClass().getSimpleName().equals("DifficultyDegree")){
			this.subName = "任务难易程度";
		}
	}
	public void setSubName(String subName) {
		this.subName = subName;
	}

	public String toStrList() {
		StringBuffer sbf = new StringBuffer();
		sbf.append("{");
		sbf.append("id:'");
		sbf.append(getTypeId().toString());
		sbf.append("',data: [0,'','");
		sbf.append(subName);
		sbf.append("','");
		sbf.append(typeName);
		sbf.append("','");
		sbf.append("1".equals(status) ? "启用" : "停用");
		sbf.append("','");
		sbf.append(remark == null ? "" : remark);
		sbf.append("','");
		sbf.append(isDefault);
		sbf.append("'");
		sbf.append("]");
		sbf.append("}");
		return sbf.toString();
	}

	public String toStrUpdateInit() {
		StringBuffer sbf = new StringBuffer();
		setSubName();
		sbf.append("typeId=");
		sbf.append(getTypeId().toString());
		sbf.append("^");
		sbf.append("subName=").append(subName);
		sbf.append("^");
		sbf.append("typeName=").append(typeName);
		sbf.append("^");
		sbf.append("remark=").append(remark == null ? "" : remark);
		sbf.append("^");
		sbf.append("status=").append(status);
		sbf.append("^");
		sbf.append("isDefault=").append(isDefault);
		sbf.append("^");
		sbf.append("initSubName=").append(subName);
		return sbf.toString();
	}

	public String toStrUpdateRest() {
		StringBuffer sbf = new StringBuffer();
		sbf.append(getTypeId().toString());
		sbf.append("^");
		sbf.append("0,,");
		sbf.append(subName);
		sbf.append(",");
		sbf.append(typeName);
		sbf.append(",");
		sbf.append("1".equals(status) ? "启用" : "停用");
		sbf.append(",");
		sbf.append(remark == null ? "" : remark);
		sbf.append(",");
		sbf.append(isDefault);
		return sbf.toString();
	}

	public void toString(StringBuffer sbf) {
		sbf.append("{");
		sbf.append("id:'");
		sbf.append(getTypeId().toString());
		sbf.append("',data: [0,'','");
		sbf.append(subName);
		sbf.append("','");
		sbf.append(typeName);
		sbf.append("','");
		sbf.append("1".equals(status) ? "启用" : "停用");
		sbf.append("','");
		sbf.append(remark == null ? "" : remark);
		sbf.append("','");
		sbf.append(isDefault);
		sbf.append("'");
		sbf.append("]");
		sbf.append("}");
	}

	public static Map<String, String> getTypeNameMap() {
		return typeNameMap;
	}

	/**
	 * @return the preference
	 */
	public Integer getPreference() {
		return preference;
	}

	/**
	 * @param preference the preference to set
	 */
	public void setPreference(Integer preference) {
		this.preference = preference;
	}

	

}
