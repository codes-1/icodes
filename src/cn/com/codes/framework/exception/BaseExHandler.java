package cn.com.codes.framework.exception;

import com.opensymphony.xwork.ActionSupport;

public class BaseExHandler extends ActionSupport{

	public String execute() {

		return "SYSErrorPage";

	}
}
