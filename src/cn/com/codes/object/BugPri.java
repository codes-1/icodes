package cn.com.codes.object;



public class BugPri extends TypeDefine {

	public BugPri(){
		
	}
	public BugPri(Long typeId, String typeName){
		super(typeId, typeName);
	}
	public BugPri(Long typeId, String typeName,Integer isDefault,String remark,String status){
		super(typeId, typeName,isDefault,remark,status);
	}
}
