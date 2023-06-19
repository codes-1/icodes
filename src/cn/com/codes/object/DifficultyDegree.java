package cn.com.codes.object;



public class DifficultyDegree extends TypeDefine {

	public DifficultyDegree(){
		
	}
	public DifficultyDegree(Long typeId, String typeName){
		super(typeId, typeName);
	}
	public DifficultyDegree(Long typeId, String typeName,Integer isDefault,String remark,String status){
		super(typeId, typeName,isDefault,remark,status);
	}
}
