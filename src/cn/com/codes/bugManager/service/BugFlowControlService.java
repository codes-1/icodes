package cn.com.codes.bugManager.service;

import cn.com.codes.framework.app.services.BaseService;
import cn.com.codes.object.BugBaseInfo;
import cn.com.codes.testTaskManager.dto.CurrTaskInfo;

public interface BugFlowControlService extends BaseService {

	public void upInitContl(CurrTaskInfo currTaskInfo,BugBaseInfo bug);
}
