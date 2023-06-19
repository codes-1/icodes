package cn.com.codes.testCycleMgr.blh;

import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;

public class TestCycleBlh extends BusinessBlh {

	public View loadCycle(BusiRequestEvent reqEvent){
		
		
		return null;
	}
	public View addCycle(BusiRequestEvent req){
		
		return super.globalAjax();
	}
}
