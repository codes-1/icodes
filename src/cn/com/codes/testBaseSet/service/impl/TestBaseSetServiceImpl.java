
package cn.com.codes.testBaseSet.service.impl;

import java.util.Date;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.framework.app.services.BaseServiceImpl;
import cn.com.codes.object.TypeDefine;
import cn.com.codes.testBaseSet.dto.TestBaseSetDto;
import cn.com.codes.testBaseSet.service.TestBaseSetService;

public class TestBaseSetServiceImpl extends BaseServiceImpl implements TestBaseSetService {

	public TypeDefine chgTypeDefine(TestBaseSetDto dto){
		String hql = "update TypeDefine set status = ? ,updDate = ? where typeId=?";
		this.executeUpdateByHql(hql, new Object[]{"3",new Date(),dto.getTestBaseSet().getTypeId()});
		TypeDefine td = dto.getTestBaseSet().copy2TypeDefine();
		td.setCompId(SecurityContextHolderHelp.getCompanyId());
		td.setUpdDate(new Date());
		synchronized (this){
			this.add(td);
		}
		return td;
	}
	

}
