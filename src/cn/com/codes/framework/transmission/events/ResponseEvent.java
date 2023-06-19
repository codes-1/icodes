package cn.com.codes.framework.transmission.events;

import java.util.ArrayList;
import java.util.List;

import cn.com.codes.framework.transmission.events.IEvent;

public class ResponseEvent implements IEvent {
	
	
	/**
	 * 暂时不用，将来用来保存异常
	 */
	protected ArrayList exceptionParam;

	/**
	 * 暂时不用
	 * 返回码 当有一些业务异常时，用返回码来标识
	 */
	protected String reponseCode;

	/**
	 * 用户帐号
	 */
	protected String userID;

	public ResponseEvent() {
		//exceptionParam = new ArrayList();
		//reponseCode = "0";
	}

	public ResponseEvent(String userID) {
		this.userID = userID;
		
	}

	public String getRepCode() {
		return reponseCode;
	}

	public void setRepCode(String reponseCode) {
		this.reponseCode = reponseCode;
	}



	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("ResponseEvent = {\r\n").append("\tsessionID = ");
		result.append(userID).append("\r\n");
		result.append("\treponseCode = ").append(reponseCode).append("\r\n");
		result.append("\r\n").append("}\r\n");
		return result.toString();
	}

	public ArrayList getExceptionParam() {
		return exceptionParam;
	}

	public String[] getExceptionParamsAsString() {
		if (exceptionParam.size() <= 0)
			return null;
		String result[] = new String[exceptionParam.size()];
		for (int i = 0; i < exceptionParam.size(); i++)
			result[i] = (String) exceptionParam.get(i);

		return result;
	}

	public void setExceptionParam(List exceptionParam) {
		this.exceptionParam.addAll(exceptionParam);
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

}
