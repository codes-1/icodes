package cn.com.codes.object;



public class CasePri extends TypeDefine {

	public CasePri() {
		super();
	}

	public CasePri(Long typeId, String typeName) {
		super(typeId, typeName);
	}
	
	public CasePri(Long typeId, String typeName,Integer isDefault,String remark,String status){
		super(typeId, typeName,isDefault,remark,status);
	}
}
