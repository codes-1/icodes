package cn.com.codes.framework.security;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

import cn.com.codes.framework.hibernate.HibernateGenericController;
import cn.com.codes.framework.jms.log.LogProducer;
import cn.com.codes.framework.security.BuildLog;
import cn.com.codes.framework.security.SecurityLog;
import cn.com.codes.framework.security.SysLog;
import cn.com.codes.framework.security.SysLogConfigure;
import cn.com.codes.framework.security.VisitUser;

public class SecurityLogImpl implements SecurityLog {

	HibernateGenericController hibernateGenericController = null;
	private LogProducer logProducer;
	private Map<String, SysLogConfigure> confMap = new HashMap<String, SysLogConfigure>();
	
	private BuildLog buildLog;

	public void addLog(Method method,Object[] args,VisitUser user){
		if(user==null){
			return;
		}
		String className = this.getDeclaringClass(method
				.getDeclaringClass().getName());
		String methodName = className + "." + method.getName();

		SysLogConfigure configure = getConfigure(methodName);
		if (configure != null) {
			process(configure, args, user);
		}
	}
	public void addLog(MethodInvocation invoke, VisitUser user) {
		
		if(user==null){
			return;
		}
		String className = this.getDeclaringClass(invoke.getMethod()
				.getDeclaringClass().getName());
		String method = className + "." + invoke.getMethod().getName();

		SysLogConfigure configure = getConfigure(method);
		if (configure != null) {
			process(configure, invoke.getArguments(), user);
		}

	}

	private String getDeclaringClass(String str) {

		return str.substring(str.lastIndexOf(".") + 1);
	}

	private void process(SysLogConfigure configure, Object[] objs, VisitUser user) {

		String args = configure.getArgs();
		String[] rules = args.split(";");
		SysLog log = buildLog.buildLog(configure, objs, user.getLoginName());
		log.setLogType(0);
		logProducer.log(log);
	}

	private SysLogConfigure getConfigure(String method) {

		if (confMap.isEmpty()) {
			confMap.clear();
			DetachedCriteria query = DetachedCriteria
					.forClass(SysLogConfigure.class);
			query.addOrder(Order.desc("id"));

			List<SysLogConfigure> configures = hibernateGenericController.getHibernateTemplate()
					.findByCriteria(query);
			for (int i = 0; i < configures.size(); i++) {
				SysLogConfigure configure = configures.get(i);
				confMap.put(configure.getMethod(), configure);
			}
		}
		return confMap.get(method);
	}

	public HibernateGenericController getHc() {
		return hibernateGenericController;
	}

	public void setHc(HibernateGenericController hc) {
		this.hibernateGenericController = hc;
	}


	public void setLogProducer(LogProducer logProducer) {
		this.logProducer = logProducer;
	}

	public BuildLog getBuildLog() {
		return buildLog;
	}

	public void setBuildLog(BuildLog buildLog) {
		this.buildLog = buildLog;
	}

}
