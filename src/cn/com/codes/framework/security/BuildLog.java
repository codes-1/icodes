package cn.com.codes.framework.security;

import cn.com.codes.framework.security.SysLog;
import cn.com.codes.framework.security.SysLogConfigure;

public interface BuildLog {

	public SysLog buildLog(SysLogConfigure configure, Object[] objs, String accountName);
}
