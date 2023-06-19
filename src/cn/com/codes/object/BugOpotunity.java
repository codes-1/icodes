package cn.com.codes.object;

public class BugOpotunity extends TypeDefine {

	public BugOpotunity(){
		
	}
	public BugOpotunity(Long typeId, String typeName){
		super(typeId, typeName);
	}
	public BugOpotunity(Long typeId, String typeName,Integer isDefault,String remark,String status){
		super(typeId, typeName,isDefault,remark,status);
	}
}
