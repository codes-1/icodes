package cn.com.codes.object;

public class BugSource extends TypeDefine {

	public BugSource(){
		
	}
	public BugSource(Long typeId, String typeName){
		super(typeId, typeName);
	}
	public BugSource(Long typeId, String typeName,Integer isDefault,String remark,String status){
		super(typeId, typeName,isDefault,remark,status);
	}
}
