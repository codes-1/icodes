package cn.com.codes.framework.security.config;

import cn.com.codes.framework.app.services.BaseServiceImpl;
import cn.com.codes.framework.security.SysLogConfigure;

public class LogConfigService extends BaseServiceImpl {



	public void queryList(String title) {

		String sql = " T_LOG_CONFIG ";
		if (title!=null) {

			sql += " where title like'%" + title + "%'";
		}

	}

	public void save(SysLogConfigure logConfigure) {
		this.getHibernateGenericController().saveOrUpdate(logConfigure);

	}

	public SysLogConfigure queryById(String id) {
		return this.getHibernateGenericController().get(SysLogConfigure.class, id);

	}

	public void delete(SysLogConfigure logConfigure){
		this.getHibernateGenericController().delete(logConfigure);
	}
}
