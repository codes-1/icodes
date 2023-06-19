package cn.com.codes.roleManager.web;

import java.util.Date;
import java.util.HashMap;

import cn.com.codes.framework.app.blh.BaseBizLogicHandler;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;
import cn.com.codes.roleManager.blh.RoleBlh;
import cn.com.codes.roleManager.dto.RoleDto;


@SuppressWarnings("unchecked")
public class RoleAction extends BaseAction<RoleBlh> {

	private RoleDto dto = new RoleDto();

	// 请求前的准备方法
	protected void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException {

		reqEvent.setDto(dto);
	}

	// 响应方法，返回结果
	protected String _processResponse() throws BaseException {
		
		HashMap<?, ?> displayData = (HashMap<?, ?>) _getDisplayData();
		return super.forwardPage(displayData);
	}
	
	// 如覆写此方法使返回值不为null delegate将以按返回值作为方法去调用BLH，无必要，不要覆盖
	protected String _getCustomBlhControlFlow(BusiRequestEvent reqEvent) {

		return null;
	}

	/**
	 * 实现直接跳转，不走ＢＬＨ
	 */
	public String directlyJump(){
		if("addRoleInit".equals(BlhControlFlow)){
			return BlhControlFlow;
		}
		return null ;
	}

	public RoleDto getDto() {
		return dto;
	}

	public void setDto(RoleDto dto) {
		this.dto = dto;
	}
	
	private RoleBlh roleBlh;
	public RoleBlh getRoleBlh() {
		return roleBlh;
	}

	public void setRoleBlh(RoleBlh roleBlh) {
		this.roleBlh = roleBlh;
	}
	
	public BaseBizLogicHandler getBlh(){
		return roleBlh;
	}
	
	public static final long serialVersionUID = (new Date()).getTime()
			+ Long.valueOf(Double.doubleToLongBits(Math.random()));

}
