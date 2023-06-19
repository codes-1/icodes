package cn.com.codes.testBaseSet.service;

import cn.com.codes.framework.app.services.BaseService;
import cn.com.codes.object.TypeDefine;
import cn.com.codes.testBaseSet.dto.TestBaseSetDto;

public interface TestBaseSetService extends BaseService{
	
	public TypeDefine chgTypeDefine(TestBaseSetDto dto);

}
