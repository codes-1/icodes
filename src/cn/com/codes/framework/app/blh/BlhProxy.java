package cn.com.codes.framework.app.blh;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

import org.apache.log4j.Logger;

import cn.com.codes.framework.app.view.UniversalView;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.hibernate.HibernateGenericController;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.app.blh.BlhProxy;

public class BlhProxy {

	private static Logger loger = Logger.getLogger(BlhProxy.class);

	public static View invokeBlh(BusiRequestEvent req, Object refBlh,
			Class<?> refClass) throws BaseException {


		Class<?>[] paramClass = new Class[1];
		paramClass[0] = BusiRequestEvent.class;
		Method methodBlh = null;
		Object[] paramObjs = new Object[1];
		paramObjs[0] = req;
		View result = null;
		try {
			if (req.getDealMethod().indexOf("add") >= 0
					|| req.getDealMethod().indexOf("update") >= 0
					|| req.getDealMethod().indexOf("delete") >= 0) {
				if (req.getDto().getIsAjax() == null) {
					throw new BaseException("非法操作", true);
				}

			}
			methodBlh = refClass.getMethod(req.getDealMethod(), paramClass);
			if (!"void".equals(methodBlh.getReturnType().getSimpleName())) {
				Object returnObj = methodBlh.invoke(refBlh, paramObjs);
				if (returnObj.getClass() == UniversalView.class) {
					result = (View) returnObj;
				} else {
					if (req.getDto().getIsAjax() == null) {
						result = new UniversalView();
						result.displayData(req.getDealMethod(), returnObj);
					}
				}
			} else {
				methodBlh.invoke(refBlh, paramObjs);
				result = new UniversalView();
				result.displayData("forward", req.getDealMethod());
			}
		} catch (SecurityException e) {
			throw new BaseException(e.getMessage(), e, true);
		} catch (NoSuchMethodException e) {
			loger.error(refClass.getSimpleName() + "不含" + req.getDealMethod()
					+ "方法");
			throw new BaseException(refClass.getSimpleName() + "不含"
					+ req.getDealMethod() + "方法", e, true);
			//throw new BaseException("严重警告 不要试图攻击 后果自负", true);
		} catch (IllegalArgumentException e) {
			loger.error("调用" 
					+ methodBlh.getName() + "时所传参数不是BusiRequestEvent类型");
			loger.error(e);
			throw new BaseException("调用" 
					+ methodBlh.getName() + "时所传参数不是BusiRequestEvent类型", e,
					true);
		} catch (IllegalAccessException e) {
			loger.error("调用" 
					+ methodBlh.getName() + "发生IllegalAccessException 异常");
			loger.error(e);
			throw new BaseException("调用" 
					+ methodBlh.getName() + "发生IllegalAccessException 导常", e,
					true);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			Throwable t = e.getTargetException();
			loger.error("调用" +refClass.getSimpleName()+"."
					+ methodBlh.getName() + "发生InvocationTargetException 异常"
					+ t);
			if (t instanceof Error) {
				t.printStackTrace();
				throw new BaseException("调用" 
						+ methodBlh.getName() + "发生异常:" + t.getMessage(), true);
			} else {
				t.printStackTrace();
				throw new BaseException("调用" 
						+ methodBlh.getName() + "发生异常", (Exception) t, true);
			}
		} catch (ClassCastException e) {
			loger.error("返回类型不是View");
			loger.error(e);
			throw new BaseException( "返回类型不是View", e, true);
		} catch (UndeclaredThrowableException e) {
			loger.error(e);
			e.printStackTrace();
//			loger.error("调用" + refClass.getSimpleName() + "."
//					+ methodBlh.getName() + " occur error");
//			loger.error("调用" + refClass.getSimpleName() + "."
//					+ methodBlh.getName() + " occur error");
//			throw new BaseException(refClass.getSimpleName() + "."
//					+ methodBlh.getName() + "返回类型不是View", (Exception) e
//					.getUndeclaredThrowable(), true);
			throw new BaseException( "返回类型不是View", true);
		}
		return result;

	}

}
