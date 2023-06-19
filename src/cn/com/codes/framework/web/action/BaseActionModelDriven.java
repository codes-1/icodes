package cn.com.codes.framework.web.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionSupport;
import com.opensymphony.xwork.ModelDriven;

import cn.com.codes.framework.app.blh.BaseBizLogicHandler;
import cn.com.codes.framework.common.Global;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.security.Visit;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.transmission.events.BusiResponseEvent;
import cn.com.codes.framework.transmission.events.ResponseEvent;
import cn.com.codes.framework.web.action.BaseActionModelDriven;

public abstract class BaseActionModelDriven extends ActionSupport implements
		ModelDriven {

	private static Logger logger = Logger.getLogger(BaseActionModelDriven.class);
	//private BaseBizLogicHandler blh;

	private BaseException exc = null;

	protected Map displayData = null;

	protected String BlhControlFlow;

	private String message;
	
	private String pageNum;
	
	private String displayCount ;
	
	private String operationId ; 
	
	public BaseActionModelDriven() {

	}

	public String execute() throws Exception {

		return doExecute();
	}

	private void _setDisplayData(Map displayData) {
		this.displayData = displayData;
	}

	final protected Map _getDisplayData() {

		return displayData;
	}

	public final String doExecute() {
		String page = null;
		BusiResponseEvent Res = null;
		BusiRequestEvent reqEvent = new BusiRequestEvent();
		//reqEvent.setBlhName(getBlh().getClass().getName());
		try {
			_prepareRequest(reqEvent);
			HttpServletRequest request =ServletActionContext.getRequest();
			request.getSession().setAttribute("logined","");
			if(getPageNum()!=null&&getDisplayCount()!=null){
				request.setAttribute("pageNum", getPageNum());
				request.setAttribute("displayCount", getDisplayCount());
			}
			
			HttpSession session = ServletActionContext.getRequest().getSession(
					true);
			Object user = session.getAttribute(Global.VISITOR);
			if (user != null) {
				operationId = ((Visit) user).getUserInfo().getId();
				reqEvent.setUserID(operationId);
				
			}
			if(reqEvent.getDto()!=null){
				reqEvent.getDto().setOperationId(operationId);
			}
			String BlhControlFlow = this.getBlhControlFlow();
			reqEvent.setDealMethod(BlhControlFlow);
			BlhControlFlow = _getCustomBlhControlFlow(reqEvent);
			if (BlhControlFlow != null) {

				reqEvent.setDealMethod(BlhControlFlow);
			}
			

			ResponseEvent resEvent = getBlh().performTask(reqEvent);;
			Res = (BusiResponseEvent) resEvent;
			_setDisplayData(Res.getDisplayData());
			Object msg = displayData.get("message");
			if(null!=msg&&!StringUtils.isEmpty((String)msg))
			{
				message = (String)displayData.get("message");
				
			}
			if (forwardToDeniedPage()) {
				return "globalException";
			}
			page = _processResponse();
			return page;
		} catch (BaseException e) {
			exc = e;
			logger.error(e);
			if(e.getLinkedException()!=null){
				ServletActionContext.getRequest().setAttribute("EXP_INFO",
						e.getLinkedException());
			}else{
				ServletActionContext.getRequest().setAttribute("EXP_INFO",
						e);
			}
			e.printStackTrace();
			return "globalException";
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			if (forwardToDeniedPage()) {
				return "globalException";
			}
			exc = new BaseException("未知异常" + e.getMessage() + "请联系管理员", true);
			ServletActionContext.getRequest().setAttribute("EXP_INFO", e);
			return "globalException";
		}
	}

	protected abstract String _processResponse() throws BaseException;

	protected abstract String _getCustomBlhControlFlow(BusiRequestEvent reqEvent);

	protected abstract void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException;

//	public void setBlh(BaseBizLogicHandler blh) {
//		this.blh = blh;
//	}

	public String getBlhControlFlow() {
		return BlhControlFlow;
	}

	public void setBlhControlFlow(String blhControlFlow) {
		BlhControlFlow = blhControlFlow;
	}

	private boolean forwardToDeniedPage() {

		HttpServletRequest request = ServletActionContext.getRequest();
		Object obj = request.getSession().getAttribute(Global.VISITOR);
		Visit vist = null;
		if(obj!=null){
			
			vist = (Visit)obj;
		}
		if(vist==null){
			return false;
		}
		Set<?> mesSet = vist.getErrors();
		if (mesSet == null) {

			return false;
		}
		String ms = "";

		Object[] os = mesSet.toArray();
		if (os.length != 0) {
			ms = (String) os[mesSet.size() - 1];
			exc = new BaseException(ms, true);
			request.setAttribute("EXP_INFO",
					exc);
			return true;
		}
		return false;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	/***
	 * 得到返回( webwork result)标记
	 * @return
	 */
	protected String forwardPage(HashMap<?,?> displayData ){
		
		if(displayData.get("forward")!=null){
			
			return displayData.get("forward").toString();
		}
		String page = this.getBlhControlFlow() ;
		
		return page;
	}




	public String getPageNum() {
		return (pageNum==null||"".equals(pageNum))?"1":pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public String getDisplayCount() {
		return (displayCount==null||"".equals(displayCount))?"10":displayCount;
	}

	public void setDisplayCount(String displayCount) {
		this.displayCount = displayCount;
	}
	public abstract BaseBizLogicHandler getBlh();
}
