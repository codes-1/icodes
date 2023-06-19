package cn.com.codes.licenseMgr.web;

import cn.com.codes.framework.app.blh.BaseBizLogicHandler;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;
import cn.com.codes.licenseMgr.blh.LicesneMgrBlh;
import cn.com.codes.licenseMgr.dto.LicesneMgrDto;

public class LicesneMgrAction extends BaseAction<LicesneMgrBlh> {

	private LicesneMgrDto dto = new LicesneMgrDto();
	private LicesneMgrBlh licesneMgrBlh;
	protected void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException {

	}

	public LicesneMgrDto getDto() {
		return dto;
	}

	public void setDto(LicesneMgrDto dto) {
		this.dto = dto;
	}

	public LicesneMgrBlh getLicesneMgrBlh() {
		return licesneMgrBlh;
	}

	public void setLicesneMgrBlh(LicesneMgrBlh licesneMgrBlh) {
		this.licesneMgrBlh = licesneMgrBlh;
	}
	public  BaseBizLogicHandler getBlh(){
		  
		return licesneMgrBlh;
	}
}
