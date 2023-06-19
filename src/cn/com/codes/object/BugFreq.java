package cn.com.codes.object;

public class BugFreq extends TypeDefine {

	public BugFreq(){
	}
	public BugFreq(Long typeId, String typeName){
		super(typeId, typeName);
	}
	public BugFreq(Long typeId, String typeName,Integer isDefault,String remark,String status){
		super(typeId, typeName,isDefault,remark,status);
	}
}
