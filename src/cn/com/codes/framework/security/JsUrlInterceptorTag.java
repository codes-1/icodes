package cn.com.codes.framework.security;

import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.security.JsUrlInterceptorTag;
import cn.com.codes.framework.security.UrlSecurityCheckInterceptor;

public class JsUrlInterceptorTag extends TagSupport {

	private Log logger = LogFactory.getLog(JsUrlInterceptorTag.class);
	private String aduitExe ; //审核该URL，无权限时在标签调用处插入安全提示拦截代码，,否则生成执行URL的js代码;
	private String aduitRwrt ; //被审核的RUL，如没权限返回“”，否则该返回URL本身
	private String aduitIntcept ;//审核该URL，无权限时在标签调用处插入安全提示拦截代码,否则该返回URL本身;
	private String definedUrlVar;//定义URL变量
    private String formId = "";
    private String urlVarName ="currUrl" ;//插入JS代码时 URL变量名
    private String restVarName ="ajaxRest";//生成执行URL时的AJAX 返回值付给的变量,缺省为ajaxRest
    private String gloableVar ="true";
	
    /**
     * 优先级从高到低为aduitExe,aduitIntcept,aduitRwrt
     */
	public int doStartTag() throws JspException { 
		
		try {

			if(aduitRwrt!=null){
				jsAduitRwrt();
			}else if(definedUrlVar!=null){
				jsUrlVarDefined();
			}else if(aduitIntcept!=null){
				jsAduitIntcept();
			}else if(aduitExe!=null){
				jsAduitExe();
			}
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
	
	private void jsAduitExe() throws IOException{
		Set<String> privilege = SecurityContextHolder.getContext().getVisit().getUserInfo().getPrivilege();
		Set<String> needCheckUrls = UrlSecurityCheckInterceptor.needCheckUrls;
		String interceptorUrl = getIntercepterUrl(aduitExe) ;
		if(needCheckUrls.size()>0 && needCheckUrls.contains(interceptorUrl)){
			if(!privilege.contains(interceptorUrl)){
				aduitExe = null;
			}
		}
		StringBuffer sb = new StringBuffer() ;
		if(aduitExe == null ||aduitExe.lastIndexOf("\"")<0){
			if(!"true".equalsIgnoreCase(gloableVar)){
				sb.append("        var ").append(urlVarName).append("=\"" +aduitExe +"\";\n");
			}else{
				sb.append(urlVarName).append("=\"" +aduitExe +"\";\n");
			}
		}else{
			if(!"true".equalsIgnoreCase(gloableVar)){
				sb.append("        var ").append(urlVarName).append("=\"" +aduitExe +";\n");
			}else{
				sb.append(urlVarName).append("=\"" +aduitExe +";\n");
			}
		}
		sb.append("        if(").append(urlVarName).append(" == \"null\"){\n");
		//用户管理因下面的函数面了，要调整
		sb.append("            hintMsg('您没有当前操作权限');\n");
		sb.append("            return;\n");
		sb.append("         }\n");
		if(aduitExe == null){
			sb.append("        var ").append(restVarName).append("= \"\";\n");
		}else{
			if(aduitExe.lastIndexOf("\"")<0){
				sb.append("        var ").append(restVarName).append(" = dhtmlxAjax.postSync(\""+aduitExe +"\",\""+formId +"\").xmlDoc.responseText;\n");
			}else{
				sb.append("        var ").append(restVarName).append(" = dhtmlxAjax.postSync(\""+aduitExe +",\""+formId +"\").xmlDoc.responseText;\n");
			}
			
		}
		JspWriter out=pageContext.getOut(); 
		out.println(sb.toString());
	}

	private void jsAduitIntcept() throws IOException{
		Set<String> privilege = SecurityContextHolder.getContext().getVisit().getUserInfo().getPrivilege();
		Set<String> needCheckUrls = UrlSecurityCheckInterceptor.needCheckUrls;
		String interceptorUrl = getIntercepterUrl(aduitIntcept) ;
		if(needCheckUrls.size()>0 && needCheckUrls.contains(interceptorUrl)){
			if(!privilege.contains(interceptorUrl)){
				aduitIntcept = null;
			}
		}
		StringBuffer sb = new StringBuffer() ;
		if(aduitIntcept == null ||aduitIntcept.lastIndexOf("\"")<0){
			if(!"true".equalsIgnoreCase(gloableVar)){
				sb.append("        var ").append(urlVarName).append("=\"" +aduitIntcept +"\";\n");
			}else{
				sb.append(urlVarName).append("=\"" +aduitIntcept +"\";\n");
			}
		}else{
			if(!"true".equalsIgnoreCase(gloableVar)){
				sb.append("        var ").append(urlVarName).append("=\"" +aduitIntcept+";\n");
			}else{
				sb.append(urlVarName).append("=\"" +aduitIntcept +";\n");
			}
		}
		sb.append("        if(").append(urlVarName).append(" == \"null\"){\n");
		sb.append("            hintMsg('您没有当前操作权限');\n");
		sb.append("            return;\n");
		sb.append("         }\n");
		JspWriter out=pageContext.getOut(); 
		out.println(sb.toString());
	}

	private void jsAduitRwrt() throws IOException{
		Set<String> privilege = SecurityContextHolder.getContext().getVisit().getUserInfo().getPrivilege();
		Set<String> needCheckUrls = UrlSecurityCheckInterceptor.needCheckUrls;
		String interceptorUrl = getIntercepterUrl(aduitRwrt) ;
		if(needCheckUrls.size()>0 && needCheckUrls.contains(interceptorUrl)){
			if(!privilege.contains(interceptorUrl)){
				aduitRwrt = "";
			}
		}
		JspWriter out=pageContext.getOut(); 
		out.print(aduitRwrt);
	}
	
	private void jsUrlVarDefined() throws IOException{
		Set<String> privilege = SecurityContextHolder.getContext().getVisit().getUserInfo().getPrivilege();
		Set<String> needCheckUrls = UrlSecurityCheckInterceptor.needCheckUrls;
		String url = getIntercepterUrl(definedUrlVar) ;
		if(needCheckUrls.size()>0 && needCheckUrls.contains(url)){
			if(!privilege.contains(url)){
				definedUrlVar = "";
			}
		}
		JspWriter out=pageContext.getOut(); 
		StringBuffer sb  = new StringBuffer();
		sb.append("var ").append(urlVarName).append("=\"").append(definedUrlVar).append("\";");
		out.println(sb.toString());
	}
	
	private String adjustUrl(String url){
		if(url==null){
			return null;
		}
		String webContextPath = ((HttpServletRequest)pageContext.getRequest()).getContextPath();
		
		if(url.startsWith("../")){
			return aduitRwrt.replace("../",webContextPath);
		}else if(url.startsWith("/")){
			return webContextPath+url;
		}
		return url;
	}
	private String getIntercepterUrl(String url){
		
		if(url == null ){
			return null;
		}
		return url.substring(url.lastIndexOf("/")+1,url.indexOf(".", url.lastIndexOf("/")));
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getAduitExe() {
		return aduitExe;
	}

	public void setAduitExe(String aduitExe) {
		this.aduitExe = aduitExe;
	}

	public String getAduitRwrt() {
		return aduitRwrt;
	}

	public void setAduitRwrt(String aduitRwrt) {
		this.aduitRwrt = aduitRwrt;
	}

	public String getUrlVarName() {
		return urlVarName;
	}

	public void setUrlVarName(String urlVarName) {
		this.urlVarName = urlVarName;
	}

	public String getRestVarName() {
		return restVarName;
	}

	public void setRestVarName(String restVarName) {
		this.restVarName = restVarName;
	}

	public String getAduitIntcept() {
		return aduitIntcept;
	}

	public void setAduitIntcept(String aduitIntcept) {
		this.aduitIntcept = aduitIntcept;
	}

	public String getGloableVar() {
		return gloableVar;
	}

	public void setGloableVar(String gloableVar) {
		this.gloableVar = gloableVar;
	}

	public String getDefinedUrlVar() {
		return definedUrlVar;
	}

	public void setDefinedUrlVar(String definedUrlVar) {
		this.definedUrlVar = definedUrlVar;
	}



}
