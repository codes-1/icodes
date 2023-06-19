package cn.com.codes.object;



public class MissionType extends TypeDefine {

	public MissionType(){
		
	}
	public MissionType(Long typeId, String typeName){
		super(typeId, typeName);
	}
	public MissionType(Long typeId, String typeName,Integer isDefault,String remark,String status){
		super(typeId, typeName,isDefault,remark,status);
	}
}
