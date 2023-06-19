package cn.com.codes.licenseMgr.service;

import cn.com.codes.framework.app.services.BaseService;
import cn.com.codes.licenseMgr.dto.LicesneMgrDto;

public interface LicesneMgrService extends BaseService {

	public void regReptLicense(LicesneMgrDto dto);

}
