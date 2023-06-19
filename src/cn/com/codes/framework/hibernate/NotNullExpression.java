package cn.com.codes.framework.hibernate;

import java.io.Serializable;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import cn.com.codes.framework.hibernate.HibernateExpression;


@SuppressWarnings("serial")
public class NotNullExpression implements Serializable, HibernateExpression {

	String propertyName;

	public NotNullExpression(String propertyName) {
		this.propertyName = propertyName;
	}

	public Criterion createCriteria() {
		return Restrictions.isNotNull(propertyName);
	}

}
