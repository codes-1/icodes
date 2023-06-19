package cn.com.codes.object;

public class BugType extends TypeDefine {

	public BugType(){
		
	}
	public BugType(Long typeId, String typeName){
		super(typeId, typeName);
	}
	public BugType(Long typeId, String typeName,Integer isDefault,String remark,String status){
		super(typeId, typeName,isDefault,remark,status);
	}
}
