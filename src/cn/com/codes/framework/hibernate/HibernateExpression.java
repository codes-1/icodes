
package cn.com.codes.framework.hibernate;

import org.hibernate.criterion.Criterion;


public interface HibernateExpression {
	public Criterion createCriteria();
}
