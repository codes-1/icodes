
package cn.com.codes.framework.hibernate;

import java.io.Serializable;

import org.hibernate.criterion.Criterion;

import cn.com.codes.framework.hibernate.HibernateExpression;
import cn.com.codes.framework.hibernate.LogicalType;


@SuppressWarnings("serial")
public class LogicalExpression extends  org.hibernate.criterion.LogicalExpression implements HibernateExpression, Serializable {

	
	
	public LogicalExpression(HibernateExpression lhs,HibernateExpression rhs,LogicalType type)
	{
		super(lhs.createCriteria(),rhs.createCriteria(),type.getValue());
	}
	
	public Criterion createCriteria() {
		return  this;
	}

}
