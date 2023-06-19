package cn.com.codes.object;



public class CaseType extends TypeDefine {

	public CaseType() {
		super();
	}

	public CaseType(Long typeId, String typeName) {
		super(typeId, typeName);
	}
	
	public CaseType(Long typeId, String typeName,Integer isDefault,String remark,String status){
		super(typeId, typeName,isDefault,remark,status);
	}

}
