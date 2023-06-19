package cn.com.codes.otherMission.web;

import cn.com.codes.framework.app.blh.BaseBizLogicHandler;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;
import cn.com.codes.otherMission.blh.OtherMissionBlh;
import cn.com.codes.otherMission.dto.OtherMissionDto;

public class OtherMissionAction extends BaseAction<OtherMissionBlh> {

	/**  
	* 字段:      字段名称
	* @Fields serialVersionUID : TODO 
	*/
	private static final long serialVersionUID = 1L;
	private OtherMissionDto dto = new OtherMissionDto();
	private OtherMissionBlh otherMissionBlh;
	protected void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException {
		reqEvent.setDto(dto);

	}
	public OtherMissionDto getDto() {
		return dto;
	}
	public void setDto(OtherMissionDto dto) {
		this.dto = dto;
	}
	public  BaseBizLogicHandler getBlh(){
		return otherMissionBlh;
	}
	public OtherMissionBlh getOtherMissionBlh() {
		return otherMissionBlh;
	}
	public void setOtherMissionBlh(OtherMissionBlh otherMissionBlh) {
		this.otherMissionBlh = otherMissionBlh;
	}
}
