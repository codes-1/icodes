package cn.com.codes.core.security;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import cn.com.codes.framework.common.PropertyUtil;
import cn.com.codes.framework.security.BuildLog;
import cn.com.codes.framework.security.SysLog;
import cn.com.codes.framework.security.SysLogConfigure;
import cn.com.codes.object.OperaLog;

public class buildLogImp implements BuildLog {

	public SysLog buildLog(SysLogConfigure configure, Object[] objs,
			String loginName) {
		
		String args = configure.getArgs();
		String[] rules = args.split(";");
		SysLog log = new OperaLog();
		log.setLogType(0);
		log.setOperSummary(configure.getTitle());
		log.setOperDate(new Date());
		log.setOperId(loginName);
		String detail = "";
		for (int i = 0; i < rules.length; i++) {
			String rule = rules[i];
			String[] pvs = rule.split(":");
			int index = Integer.parseInt(pvs[0]);
			Object obj = objs[index - 1];
			if(obj instanceof String){
				detail += obj;
			}else if (obj instanceof Collection) {
				Iterator iter = ((Collection) obj).iterator();
				while (iter.hasNext()) {
					detail += getDetail(iter.next(), pvs);
				}
			} else {
				detail += getDetail(obj, pvs);
			}
		}
		log.setOperDesc(detail);
		return log;
	}

	private String getDetail(Object obj, String[] pvs) {
		String detail = "{";
		for (int j = 1; j < pvs.length; j++) {
			String[] pv = pvs[j].split("=");
			String[] propertys = pv[0].split("\\.");
			detail += pv[1] + ":";

			try {
				Object value = obj;

				for (int i = 0; i < propertys.length; i++) {
					value = PropertyUtil.getProperty(value, propertys[i]);
				}
				detail += value;
			} catch (Exception e) {
				detail += "空";
			}
			detail += "; ";
		}
		detail += "}\n";
		return detail;
	}
	
}
