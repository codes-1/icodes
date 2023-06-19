package cn.com.codes.framework.web.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.context.ServletContextAware;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionSupport;

import cn.com.codes.framework.app.blh.BaseBizLogicHandler;
import cn.com.codes.framework.app.blh.BlhDelegate;
import cn.com.codes.framework.app.blh.BlhFactory;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.exception.DataBaseException;
import cn.com.codes.framework.hibernate.HibernateGenericController;
import cn.com.codes.framework.jms.log.LogProducer;
import cn.com.codes.framework.security.filter.SecurityContext;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.transmission.events.BusiResponseEvent;
import cn.com.codes.framework.transmission.events.ResponseEvent;


public abstract class BaseAction<T> extends ActionSupport implements BlhDelegate<T>,ServletContextAware {

	private static Logger logger = Logger.getLogger(HibernateGenericController.class);
	private static LogProducer logProducer;
	public BaseException exc = null;
	protected Map<?, ?> displayData = null;
	protected String BlhControlFlow;
	private String operationId;
	private ServletContext sc;
	
	public BaseAction() {
	}

	public String execute() throws Exception {
		return doExecute();
	}

	private void _setDisplayData(Map<?, ?> displayData) {
		this.displayData = displayData;
	}

	final protected Map<?, ?> _getDisplayData() {

		return displayData;
	}

	public final String doExecute() {
		
		BusiResponseEvent Res = null;
		BusiRequestEvent reqEvent = new BusiRequestEvent();
		reqEvent.setUserID(operationId);
		try {
			_prepareRequest(reqEvent);
//			if(ServletActionContext.getRequest().getParameter("ajax")!=null&&reqEvent.getDto()!=null){
//				reqEvent.getDto().setIsAjax("true");
//			}
			if(isAjaxRequest()) {
				reqEvent.getDto().setIsAjax("true");
			}
			String page = ServletActionContext.getRequest().getParameter("page");
			if(page!=null) {
				reqEvent.getDto().setPage(Integer.valueOf(page));
				String rows = ServletActionContext.getRequest().getParameter("rows");
				reqEvent.getDto().setRows(Integer.valueOf(rows == null ? "20" : rows));
			}
			String BlhControlFlow = this.getBlhControlFlow();
			reqEvent.setDealMethod(BlhControlFlow);
			BlhControlFlow = _getCustomBlhControlFlow(reqEvent);
			if (BlhControlFlow != null) {
				reqEvent.setDealMethod(BlhControlFlow);
			}
			BaseBizLogicHandler blh = this.getBlh();
			if(blh == null){
				 blh = BlhFactory.getBlh(sc,getClass());
			}
			ResponseEvent resEvent = blh.performTask(reqEvent);
			Res = (BusiResponseEvent) resEvent;
			_setDisplayData(Res.getDisplayData());
			if(reqEvent.getDto()!=null&&"true".equals(reqEvent.getDto().getIsAjax())){
				reqEvent.getDto().setJsonData(null);
				reqEvent.getDto().setListListObjects(null);
				reqEvent.getDto().setListListObjects(null);
				reqEvent.getDto().setHqlParamLists(null);
				reqEvent.getDto().setHqlParamMaps(null);
				reqEvent.getDto().setHqlParamsArrays(null);
				reqEvent.getDto().clearDataContainer();
				reqEvent.setDto(null);
			}
			reqEvent = null;
			Res = null;
			return  _processResponse();
		} catch (BaseException e) {
			exc = e;
			if(!"overdue".equals(e.getMessage())){
				if (e.getLinkedException() != null) {
					if(e.getLinkedException().getMessage()!=null){
						e.setMessage(e.getMessage() +e.getLinkedException().getMessage());
					}
					logger.error(e.getLinkedException());
					//printStack(e.getLinkedException());
				} else {
					logger.error(e);
					printStack(e);
				}
			}
			SecurityContext sec = SecurityContextHolder.getContext();
			//this.writeOperaLog(e.getMessage(),sec);
			if(reqEvent.getDto() != null && "true".equals(reqEvent.getDto().getIsAjax())){
				sec.writeResponse("failed");
				return "ajaxException";
			}
			ServletActionContext.getRequest().setAttribute("EXP_INFO", e);
			return "globalException";
		} catch (DataBaseException e) {
			if (e.getLinkedException() != null) {
				printStack(e.getLinkedException());
			} else {
				printStack(e);
			}
			logger.error(e.getMessage());
			SecurityContext sec = SecurityContextHolder.getContext();
			//this.writeOperaLog(e.getMessage(),sec);
			if(reqEvent.getDto() != null && "true".equals(reqEvent.getDto().getIsAjax())){
				sec.writeResponse("failed");
				return "ajaxException";
			}
			exc = new BaseException(e.getMessage(), e.getLinkedException(),
					true);
			ServletActionContext.getRequest().setAttribute("EXP_INFO", exc);
			return "globalException";
		} catch (Exception e) {
			logger.error(e);
			printStack(e);
			SecurityContext sec = SecurityContextHolder.getContext();
			//this.writeOperaLog(e.getMessage(),sec);
			if(reqEvent.getDto() != null && "true".equals(reqEvent.getDto().getIsAjax())){
				sec.writeResponse("failed");
				return "ajaxException";
			}
			exc = new BaseException("未知异常" + e.getMessage() + "请联系管理员", e, true);
			ServletActionContext.getRequest().setAttribute("EXP_INFO", exc);
			return "globalException";
		}
	}
	private void printStack(Throwable e){
	//	if(isPrintStack()){
	//		e.printStackTrace();
	//	}
	}
	private boolean isPrintStack(){
		if(sc.getInitParameter("printStackTrace")!=null){
			return sc.getInitParameter("printStackTrace").equalsIgnoreCase("true")?true:false;
		}
		return false;
	}
	protected  String _processResponse() throws BaseException{
		HashMap<?, ?> displayData = (HashMap<?, ?>) _getDisplayData();
		return forwardPage(displayData);
	}

	protected  String _getCustomBlhControlFlow(BusiRequestEvent reqEvent){
		
		return null;
	}

	protected abstract void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException;



	public String getBlhControlFlow() {
		return BlhControlFlow;
	}

	public void setBlhControlFlow(String blhControlFlow) {
		BlhControlFlow = blhControlFlow;
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	/*
	 * 得到返回( webwork result)标记
	 * 
	 * @return
	 */
	protected String forwardPage(HashMap<?, ?> displayData) {

		if (displayData!=null&&displayData.get("forward") != null) {
			String viewName = displayData.get("forward").toString();
			displayData = null;
			
			return viewName;
		}
		String page = this.getBlhControlFlow();
		return page;
	}

	public  BaseBizLogicHandler getBlh(){
		  
		return null;
	}

	public void setServletContext(ServletContext sc) {
		this.sc = sc;
	}
	
//	private void writeOperaLog(String msg ,SecurityContext sec){
//		SysLog log = new ErrorLog();
//		log.setLogType(1);
//		log.setOperDesc(msg);
//		log.setOperSummary("Exception");
//		log.setOperDate(new Date());
//		try {
//			log.setOperId(sec.getUserInfo().getLoginName());
//		} catch (RuntimeException e) {
//			logger.error(e);
//			goLoginPage();
//			return;
//		}
//		log.setAccessIp(sec.getIpAddr());
//		if(logProducer==null){
//			WebApplicationContext wac = WebApplicationContextUtils
//			.getWebApplicationContext(sc);
//			logProducer = (LogProducer)wac.getBean("logMessageProducer");			
//		}
//		logProducer.log(log);
//	}

	public ServletContext getSc() {
		return sc;
	}

	private void goLoginPage() { 
		try {
			HttpServletRequest httpRequest = ServletActionContext.getRequest();
			HttpServletResponse httpResponse = ServletActionContext.getResponse();
			httpResponse.setContentType("text/html; charset=UTF-8");
			PrintWriter out = httpResponse.getWriter();
			StringBuffer sb = new StringBuffer();
			sb.append("<html><head><script type='text/javascript'>function toLgin() { top.location='");
			sb.append(httpRequest.getContextPath())
			.append("/itest/jsp/login.jsp")
			.append("'}</script>");
			sb.append("</head><body onload='toLgin()'></body></html>");
			out.print(sb.toString());
		} catch (IOException iox) {
			logger.error(iox);
		}
	}
	
	protected static boolean isAjaxRequest() {
		String requestType = ServletActionContext.getRequest().getHeader(
				"X-Requested-With");
		if (requestType != null && "XMLHttpRequest".equals(requestType)) {
			return true;
		}
		return false;
	}
}
