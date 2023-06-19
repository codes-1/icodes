package cn.com.codes.object;

public class ImportPhase extends TypeDefine {
	
	public ImportPhase(){
		
	}
	public ImportPhase(Long typeId, String typeName){
		super(typeId, typeName);
	}
	public ImportPhase(Long typeId, String typeName,Integer isDefault,String remark,String status){
		super(typeId, typeName,isDefault,remark,status);
	}

}
