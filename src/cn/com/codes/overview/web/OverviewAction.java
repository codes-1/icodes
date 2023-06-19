package cn.com.codes.overview.web;

import cn.com.codes.framework.app.blh.BaseBizLogicHandler;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;
import cn.com.codes.overview.blh.OverviewBlh;
import cn.com.codes.overview.dto.OverviewDto;

public class OverviewAction extends BaseAction<OverviewBlh> {

	/**  
	* 字段:      字段名称
	* @Fields serialVersionUID : TODO 
	*/
	private static final long serialVersionUID = 1L;
	private OverviewDto dto = new OverviewDto();
	private OverviewBlh overviewBlh;
	protected void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException {
		reqEvent.setDto(dto);

	}
	public OverviewDto getDto() {
		return dto;
	}
	public void setDto(OverviewDto dto) {
		this.dto = dto;
	}
	public  BaseBizLogicHandler getBlh(){
		return overviewBlh;
	}
	public OverviewBlh getOverviewBlh() {
		return overviewBlh;
	}
	public void setOverviewBlh(OverviewBlh overviewBlh) {
		this.overviewBlh = overviewBlh;
	}
	
}
