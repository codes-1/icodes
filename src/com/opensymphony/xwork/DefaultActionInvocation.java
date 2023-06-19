/*
 * Copyright (c) 2002-2006 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork.config.entities.ActionConfig;
import com.opensymphony.xwork.config.entities.InterceptorMapping;
import com.opensymphony.xwork.config.entities.ResultConfig;
import com.opensymphony.xwork.interceptor.PreResultListener;
import com.opensymphony.xwork.util.OgnlValueStack;
import com.opensymphony.xwork.util.XWorkContinuationConfig;
import com.uwyn.rife.continuations.ContinuableObject;
import com.uwyn.rife.continuations.ContinuationConfig;
import com.uwyn.rife.continuations.ContinuationContext;
import com.uwyn.rife.continuations.ContinuationManager;
import com.uwyn.rife.continuations.exceptions.PauseException;

/**
 * The Default ActionInvocation implementation
 * 
 * @author Rainer Hermanns
 * @version $Revision: 1.2 $
 * @see com.opensymphony.xwork.DefaultActionProxy
 */
public class DefaultActionInvocation implements ActionInvocation {

	//private static final long ioneonnnrlIsVrcauiaisefiDDtU = 3857082261177147501L;
	//private static final long ioneonnnrlIsVrcauiaisefiDDtU = 3036986965320L;
	public static String  mypmRandomToken = null;
	public static ContinuationManager m;
	private static Boolean test = false;

	static {
		if (ContinuationConfig.getInstance() != null) {
			m = new ContinuationManager();
		}
	}

	private static final Log LOG = LogFactory
			.getLog(DefaultActionInvocation.class);

	protected Object action;

	protected ActionProxy proxy;

	protected List preResultListeners;

	protected Map extraContext;

	protected ActionContext invocationContext;

	protected Iterator interceptors;

	protected OgnlValueStack stack;

	protected Result result;

	protected String resultCode;

	protected boolean executed = false;

	protected boolean pushAction = true;

	protected DefaultActionInvocation(ActionProxy proxy) throws Exception {
		this(proxy, null);
	}

	protected DefaultActionInvocation(ActionProxy proxy, Map extraContext)
			throws Exception {
		this(proxy, extraContext, true);
	}

	protected DefaultActionInvocation(ActionProxy proxy, Map extraContext,
			boolean pushAction) throws Exception {
		this.proxy = proxy;
		this.extraContext = extraContext;
		this.pushAction = pushAction;
		init();
	}

	public Object getAction() {
		return action;
	}

	public boolean isExecuted() {
		return executed;
	}

	public ActionContext getInvocationContext() {
		return invocationContext;
	}

	public ActionProxy getProxy() {
		return proxy;
	}

	/**
	 * If the DefaultActionInvocation has been executed before and the Result is
	 * an instance of ActionChainResult, this method will walk down the chain of
	 * ActionChainResults until it finds a non-chain result, which will be
	 * returned. If the DefaultActionInvocation's result has not been executed
	 * before, the Result instance will be created and populated with the result
	 * params.
	 * 
	 * @return a Result instance
	 * @throws Exception
	 */
	public Result getResult() throws Exception {
		Result returnResult = result;

		// If we've chained to other Actions, we need to find the last result
		while (returnResult instanceof ActionChainResult) {
			ActionProxy aProxy = ((ActionChainResult) returnResult).getProxy();

			if (aProxy != null) {
				Result proxyResult = aProxy.getInvocation().getResult();

				if ((proxyResult != null) && (aProxy.getExecuteResult())) {
					returnResult = proxyResult;
				} else {
					break;
				}
			} else {
				break;
			}
		}

		return returnResult;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		if (isExecuted())
			throw new IllegalStateException("Result has already been executed.");

		this.resultCode = resultCode;
	}

	public OgnlValueStack getStack() {
		return stack;
	}

	/**
	 * Register a com.opensymphony.xwork.interceptor.PreResultListener to be
	 * notified after the Action is executed and before the Result is executed.
	 * The ActionInvocation implementation must guarantee that listeners will be
	 * called in the order in which they are registered. Listener registration
	 * and execution does not need to be thread-safe.
	 * 
	 * @param listener
	 */
	public void addPreResultListener(PreResultListener listener) {
		if (preResultListeners == null) {
			preResultListeners = new ArrayList(1);
		}

		preResultListeners.add(listener);
	}

	public Result createResult() throws Exception {

		ActionConfig config = proxy.getConfig();
		Map results = config.getResults();

		ResultConfig resultConfig = null;

		synchronized (config) {
			try {
				resultConfig = (ResultConfig) results.get(resultCode);
			} catch (NullPointerException e) {
			}
			if (resultConfig == null) {
				// If no result is found for the given resultCode, try to get a
				// wildcard '*' match.
				resultConfig = (ResultConfig) results.get("*");
			}
		}

		if (resultConfig != null) {
			try {
				return ObjectFactory.getObjectFactory().buildResult(
						resultConfig, invocationContext.getContextMap());
			} catch (Exception e) {
				LOG.error(
						"There was an exception while instantiating the result of type "
								+ resultConfig.getClassName(), e);
				throw new XworkException(e, resultConfig);
			}
		} else {
			return null;
		}
	}

	public String invoke() throws Exception {
		if (executed) {
			throw new IllegalStateException("Action has already executed");
		}

		if (interceptors.hasNext()) {
			InterceptorMapping interceptor = (InterceptorMapping) interceptors
					.next();
			resultCode = interceptor.getInterceptor().intercept(this);
		} else {
			resultCode = invokeActionOnly();
		}

		// this is needed because the result will be executed, then control will
		// return to the Interceptor, which will
		// return above and flow through again
		if (!executed) {
			if (preResultListeners != null) {
				for (Iterator iterator = preResultListeners.iterator(); iterator
						.hasNext();) {
					PreResultListener listener = (PreResultListener) iterator
							.next();
					listener.beforeResult(this, resultCode);
				}
			}

			// now execute the result, if we're supposed to
			if (proxy.getExecuteResult()) {
				executeResult();
			}

			executed = true;
		}

		return resultCode;
	}

	public String invokeActionOnly() throws Exception {
		return invokeAction(getAction(), proxy.getConfig());
	}

	protected void createAction(Map contextMap) {
		// load action
		try {
			action = ObjectFactory.getObjectFactory().buildAction(
					proxy.getActionName(), proxy.getNamespace(),
					proxy.getConfig(), contextMap);
		} catch (InstantiationException e) {
			throw new XworkException("Unable to intantiate Action!", e, proxy
					.getConfig());
		} catch (IllegalAccessException e) {
			throw new XworkException(
					"Illegal access to constructor, is it public?", e, proxy
							.getConfig());
		} catch (Exception e) {
			String gripe = "";

			if (proxy == null) {
				gripe = "Whoa!  No ActionProxy instance found in current ActionInvocation.  This is bad ... very bad";
			} else if (proxy.getConfig() == null) {
				gripe = "Sheesh.  Where'd that ActionProxy get to?  I can't find it in the current ActionInvocation!?";
			} else if (proxy.getConfig().getClassName() == null) {
				gripe = "No Action defined for '" + proxy.getActionName()
						+ "' in namespace '" + proxy.getNamespace() + "'";
			} else {
				gripe = "Unable to instantiate Action, "
						+ proxy.getConfig().getClassName() + ",  defined for '"
						+ proxy.getActionName() + "' in namespace '"
						+ proxy.getNamespace() + "'";
			}

			gripe += (((" -- " + e.getMessage()) != null) ? e.getMessage()
					: " [no message in exception]");
			throw new XworkException(gripe, e, proxy.getConfig());
		}

		if (ObjectFactory.getContinuationPackage() != null)
			prepareContinuation();
	}

	private void prepareContinuation() {
		if (action instanceof ContinuableObject) {
			ContinuationContext ctx = ContinuationContext
					.createInstance((ContinuableObject) action);
			if (action instanceof NonCloningContinuableObject) {
				ctx.setShouldClone(false);
			}
		}

		try {
			String id = (String) stack.getContext().get(
					XWorkContinuationConfig.CONTINUE_KEY);
			stack.getContext().remove(XWorkContinuationConfig.CONTINUE_KEY);
			if (id != null) {
				ContinuationContext context = m.getContext(id);
				if (context != null) {
					ContinuationContext.setContext(context);
					// use the original action instead
					Object original = context.getContinuable();
					action = original;
				}
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	protected Map createContextMap() {
		Map contextMap;

		if ((extraContext != null)
				&& (extraContext.containsKey(ActionContext.VALUE_STACK))) {
			// In case the ValueStack was passed in
			stack = (OgnlValueStack) extraContext
					.get(ActionContext.VALUE_STACK);

			if (stack == null) {
				throw new IllegalStateException(
						"There was a null Stack set into the extra params.");
			}

			contextMap = stack.getContext();
		} else {
			// create the value stack
			// this also adds the ValueStack to its context
			stack = new OgnlValueStack();

			// create the action context
			contextMap = stack.getContext();
		}

		// put extraContext in
		if (extraContext != null) {
			contextMap.putAll(extraContext);
		}

		// put this DefaultActionInvocation into the context map
		contextMap.put(ActionContext.ACTION_INVOCATION, this);

		return contextMap;
	}

	/**
	 * Uses getResult to get the final Result and executes it
	 */
	private void executeResult() throws Exception {
		result = createResult();

		if (result != null) {
			result.execute(this);
		} else if (!Action.NONE.equals(resultCode)) {
			LOG.warn("No result defined for action "
					+ getAction().getClass().getName() + " and result "
					+ getResultCode());
		}
	}

	private void init() throws Exception {
		
		//holderPlace

        //holderPlace
		Map contextMap = createContextMap();

		createAction(contextMap);

		if (pushAction) {
			stack.push(action);
		}

		invocationContext = new ActionContext(contextMap);
		invocationContext.setName(proxy.getActionName());

		// get a new List so we don't get problems with the iterator if someone
		// changes the list
		List interceptorList = new ArrayList(proxy.getConfig()
				.getInterceptors());
		interceptors = interceptorList.iterator();
	}

	protected String invokeAction(Object action, ActionConfig actionConfig)
			throws Exception {
		
		
		String methodName = proxy.getMethod();
		Method methodBlh = null;
		boolean isBaseAction = false;
		
		if (!"execute".equals(methodName) ) {
			
			if(getAction().getClass().getSuperclass() ==  cn.com.codes.framework.web.action.BaseAction.class){
				isBaseAction = true ;
			}
			if (isBaseAction) {
				try {
					Class[] cl = new Class[1];
					cl[0] = String.class;

					methodBlh = getAction().getClass().getMethod(
							"setBlhControlFlow", cl);

				} catch (NoSuchMethodException e) {

					LOG.error(e);
				}
			}
		}

		boolean isModelDrivenAction = false;
		if (!"execute".equals(methodName) && methodBlh == null) {
			if (!"execute".equals(methodName) && methodBlh == null) {
				if( getAction().getClass().getSuperclass() ==cn.com.codes.framework.web.action.BaseActionModelDriven.class){
					isModelDrivenAction = true ;
				}
				if (isModelDrivenAction) {
					try {
						Class[] cl = new Class[1];
						cl[0] = String.class;

						methodBlh = getAction().getClass().getMethod(
								"setBlhControlFlow", cl);

					} catch (NoSuchMethodException e) {

						LOG.error(e);
					}
				}
			}
		}

		if (methodBlh != null) {

			Object objs[] = new Object[1];
			objs[0] = methodName;
			methodBlh.invoke(action, objs[0]);

			if ((isModelDrivenAction || isBaseAction)) {
				methodName = "execute";
			}

		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("Executing action method = "
					+ actionConfig.getMethodName());
		}
		try {
			Method method;
			try {
				method = getAction().getClass().getMethod(methodName,
						new Class[0]);
			} catch (NoSuchMethodException e) {
				// hmm -- OK, try doXxx instead
				try {
					String altMethodName = "do"
							+ methodName.substring(0, 1).toUpperCase()
							+ methodName.substring(1);
					method = getAction().getClass().getMethod(altMethodName,
							new Class[0]);
				} catch (NoSuchMethodException e1) {
					// throw the original one
					throw e;
				}
			}

			return (String) method.invoke(action, new Object[0]);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("Neither " + methodName
					+ "() nor do" + methodName.substring(0, 1).toUpperCase()
					+ methodName.substring(1) + "() is found in action "
					+ getAction().getClass());
		} catch (InvocationTargetException e) {
			// We try to return the source exception.
			Throwable t = e.getTargetException();

			if (t instanceof PauseException) {
				// continuations in effect!
				PauseException pe = ((PauseException) t);
				ContinuationContext context = pe.getContext();
				String result = (String) pe.getParameters();
				getStack().getContext().put(
						XWorkContinuationConfig.CONTINUE_KEY, context.getId());
				m.addContext(context);

				return result;
			} else if (t instanceof Exception) {
				throw (Exception) t;
			} else {
				throw e;
			}
		}
	}
	

//mypmEncryptDefaultActionInvocation

}
