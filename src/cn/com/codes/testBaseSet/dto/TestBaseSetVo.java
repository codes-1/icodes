package cn.com.codes.testBaseSet.dto;

import cn.com.codes.object.TypeDefine;

public class TestBaseSetVo {

	private Long typeId;
	private String compId;
	private String typeName;
	private String remark;
	private Integer isDefault;
	private String subName;
	private String status;
	private String initSubName;

	public TestBaseSetVo() {

	}

	public TestBaseSetVo(Long typeId, String typeName) {
		this.typeId = typeId;
		this.typeName = typeName;
	}

	public TestBaseSetVo(Long typeId, String typeName, Integer isDefault,
			String remark, String status) {
		this.typeId = typeId;
		this.typeName = typeName;
		this.isDefault = isDefault;
		this.remark = remark;
		this.status = status;
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
	public void setSubName(String subName) {
		this.subName = subName;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public String getInitSubName() {
		return initSubName;
	}

	public void setInitSubName(String initSubName) {
		this.initSubName = initSubName;
	}
	public Class getSubClass() {

		if (subName.equals("BUG频率")) {
			return cn.com.codes.object.BugFreq.class;
		} else if (subName.equals("BUG类型")) {
			return cn.com.codes.object.BugType.class;
		} else if (subName.equals("BUG等级")) {
			return cn.com.codes.object.BugGrade.class;
		} else if (subName.equals("BUG发现时机")) {
			return cn.com.codes.object.BugOpotunity.class;
		} else if (subName.equals("BUG优先级")) {
			return cn.com.codes.object.BugPri.class;
		} else if (subName.equals("BUG来源")) {
			return cn.com.codes.object.BugSource.class;
		} else if (subName.equals("用例优先级")) {
			return cn.com.codes.object.CasePri.class;
		} else if (subName.equals("用例类型")) {
			return cn.com.codes.object.CaseType.class;
		} else if (subName.equals("BUG测试时机")) {
			return cn.com.codes.object.GeneCause.class;
		} else if (subName.equals("BUG发生平台")) {
			return cn.com.codes.object.OccurPlant.class;
		} else if (subName.equals("BUG引入原因")) {
			return cn.com.codes.object.ImportPhase.class;
		} else if (subName.equals("任务类别")) {
			return cn.com.codes.object.MissionType.class;
		} else if (subName.equals("任务紧急程度")) {
			return cn.com.codes.object.EmergencyDegree.class;
		} else if (subName.equals("任务难易程度")) {
			return cn.com.codes.object.DifficultyDegree.class;
		}
		return null;
	}
	public Class getSubClass(String subName) {

		if (subName.equals("BUG频率")) {
			return cn.com.codes.object.BugFreq.class;
		} else if (subName.equals("BUG类型")) {
			return cn.com.codes.object.BugType.class;
		} else if (subName.equals("BUG等级")) {
			return cn.com.codes.object.BugGrade.class;
		} else if (subName.equals("BUG发现时机")) {
			return cn.com.codes.object.BugOpotunity.class;
		} else if (subName.equals("BUG优先级")) {
			return cn.com.codes.object.BugPri.class;
		} else if (subName.equals("BUG来源")) {
			return cn.com.codes.object.BugSource.class;
		} else if (subName.equals("用例优先级")) {
			return cn.com.codes.object.CasePri.class;
		} else if (subName.equals("用例类型")) {
			return cn.com.codes.object.CaseType.class;
		} else if (subName.equals("BUG测试时机")) {
			return cn.com.codes.object.GeneCause.class;
		} else if (subName.equals("BUG发生平台")) {
			return cn.com.codes.object.OccurPlant.class;
		} else if (subName.equals("BUG引入原因")) {
			return cn.com.codes.object.ImportPhase.class;
		} else if (subName.equals("任务类别")) {
			return cn.com.codes.object.MissionType.class;
		} else if (subName.equals("任务紧急程度")) {
			return cn.com.codes.object.EmergencyDegree.class;
		} else if (subName.equals("任务难易程度")) {
			return cn.com.codes.object.DifficultyDegree.class;
		}
		return null;
	}
	public TypeDefine copy2TypeDefine(){
		Class cls = this.getSubClass();
		TypeDefine td = null;
		try {
			td = (TypeDefine)cls.newInstance();
			td.setCompId(this.getCompId());
			td.setTypeId(this.getTypeId());
			td.setTypeName(this.getTypeName().trim());
			td.setRemark(this.getRemark().trim());
			td.setStatus(this.getStatus());
			td.setIsDefault(this.getIsDefault());
			td.setSubName(this.getSubName());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return td;
	}

	
}
