package cn.com.codes.object;



public class EmergencyDegree extends TypeDefine {

	public EmergencyDegree(){
		
	}
	public EmergencyDegree(Long typeId, String typeName){
		super(typeId, typeName);
	}
	public EmergencyDegree(Long typeId, String typeName,Integer isDefault,String remark,String status){
		super(typeId, typeName,isDefault,remark,status);
	}
}
