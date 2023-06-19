package org.eclipse.birt.report.listener.data.oda;

import java.lang.reflect.Method;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

public class ViewListener extends StaticMethodMatcherPointcut {

	public boolean matches(Method method, Class targetClass) {
		return "performTask".equals(method.getName());

	}

	public ClassFilter getClassFilter() {
		return new ClassFilter() {
			public boolean matches(Class clasz) {
				return (clasz.getSuperclass().getName()
						.equals("cn.com.codes.framework.app.blh.BusinessBlh"));
			}
		};
	}

}
