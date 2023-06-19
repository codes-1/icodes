package cn.com.codes.object;



public class GeneCause extends TypeDefine {

	public GeneCause(){
		
	}
	public GeneCause(Long typeId, String typeName){
		super(typeId, typeName);
	}
	public GeneCause(Long typeId, String typeName,Integer isDefault,String remark,String status){
		super(typeId, typeName,isDefault,remark,status);
	}
}
