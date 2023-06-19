package cn.com.codes.framework.security.config;

import org.apache.commons.lang.StringUtils;

import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.view.UniversalView;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.security.SysLogConfigure;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.security.config.LogConfigService;

public class LogConfigBlh extends BusinessBlh {

	private LogConfigService logConfigService = null;

	public View _do(BusiRequestEvent req) throws BaseException {
		UniversalView view = new UniversalView();
		String method = req.getDealMethod();
		SysLogConfigure logConfigure = (SysLogConfigure) req.getDto().getAttr("logConfigure");
	    if("modifyInit".equals(method)){
	    	logConfigure = logConfigService.queryById(logConfigure.getId());
	    	req.getDto().setAttr("logConfigure", logConfigure);
			view.displayData("dto", req.getDto());
			return view;
	    }
	    
	    if("addInit".equals(method)){
	    	view.displayData("dto", req.getDto());
	    	return view;
	    }
	    if("save".equals(method)){
	    	if("".equals(logConfigure.getId())){
	    		logConfigure.setId(null);
	    	}
	    	logConfigService.save(logConfigure);
	    }
	    
	    if("delete".equals(method)){
	    	logConfigService.delete(logConfigure);
	    }

	
		String title = null;
		if (!StringUtils.isEmpty(logConfigure.getTitle())) {
			title =logConfigure.getTitle();
		}
		logConfigService.queryList( title);
		
		
		view.displayData("dto", req.getDto());
		return view;
	}

	public LogConfigService getLogConfigService() {
		return logConfigService;
	}

	public void setLogConfigService(LogConfigService logConfigService) {
		this.logConfigService = logConfigService;
	}


}
