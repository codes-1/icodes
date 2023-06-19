package cn.com.codes.object;

public class BugGrade extends TypeDefine {

	public BugGrade(){
		
	}
	public BugGrade(Long typeId, String typeName){
		super(typeId, typeName);
	}
	public BugGrade(Long typeId, String typeName,Integer isDefault,String remark,String status){
		super(typeId, typeName,isDefault,remark,status);
	}
}
