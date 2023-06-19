package cn.com.codes.object;

import org.apache.log4j.Logger;

import cn.com.codes.object.DefaultTypeDefine;
import cn.com.codes.object.TypeDefine;

public class DefaultTypeDefine extends TypeDefine {

	private static Logger logger = Logger.getLogger(DefaultTypeDefine.class); 
	public DefaultTypeDefine() {

	}

	public DefaultTypeDefine(Long typeId, String typeName) {
		super(typeId, typeName);
	}

	public DefaultTypeDefine(Long typeId, String typeName, Integer isDefault,
			String remark, String status) {
		super(typeId, typeName, isDefault, remark, status);
	}
	public  <T extends TypeDefine> T   newTypeDefine(Class<T>  clasz ){
		TypeDefine td = null;
		try {
			td = (TypeDefine)clasz.newInstance();
			td.setTypeId(this.getTypeId());
			td.setTypeName(this.getTypeName());
		} catch (InstantiationException e) {
			logger.error(e);
		} catch (IllegalAccessException e) {
			logger.error(e);
		}
		return (T)td ;
	}
}
