package cn.com.codes.framework.security.config;

import org.apache.commons.lang.StringUtils;

import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.view.UniversalView;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.security.Function;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.security.config.SecurityConfigService;

public class SecurityConfigBlh extends BusinessBlh {

	private SecurityConfigService securitService = null;
	public View _do(BusiRequestEvent req) throws BaseException {
		UniversalView view = new UniversalView();
		String method = req.getDealMethod();
		Function founction = (Function) req.getDto().getAttr("founction");
		if("setMethodsInit".equals(method)){
			founction = securitService.getfouctionByID(founction.getFunctionId());
			view.displayData("dto", req.getDto());
			req.getDto().setAttr("founction", founction);
			return view;
		}
		
		if("setMethodsSave".equals(method)){
			securitService.saveFounction(founction);
		}

		
		String fouctionName = null;
		if (founction != null) {
			if (!StringUtils.isEmpty(founction.getFunctionName())) {
				fouctionName =  founction.getFunctionName();
			}
		}

		view.displayData("dto", req.getDto());
		return view;
	}

	public SecurityConfigService getSecuritService() {
		return securitService;
	}

	public void setSecuritService(SecurityConfigService securitService) {
		this.securitService = securitService;
	}

}
