package cn.com.codes.framework.security;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.security.UserPrivilegeTag;
import cn.com.codes.framework.security.Visit;

public class UserPrivilegeTag extends TagSupport {
	private Log logger = LogFactory.getLog(UserPrivilegeTag.class);
	
	private String  urls;
	private String  varNames;
	private String  jsContext = "true";
	public int doStartTag() throws JspException { 
		
		try {
			writeVariable();
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
	
	private void writeVariable() throws IOException{
		JspWriter out=pageContext.getOut();
		if("".equals(urls.trim())||"".equals(varNames.trim())){
			if(jsContext.equals("true")){
				out.println("var urlChekError='受保护URL为空';");
			}else{
				out.println("<script type=\"text/javascript\">");
				out.println("var urlChekError='受保护URL为空';");
				out.println("</script>");
			}
			return;
		}
		String[] urlArr = urls.split(",");
		String[] varNameArr = varNames.split(",");
		if(urlArr.length!=varNameArr.length){
			if(jsContext.equals("true")){
				out.println("var urlChekError='url数不等于变量数';");
			}else{
				out.println("<script type=\"text/javascript\">");
				out.println("var urlChekError='url数不等于变量数';");
				out.println("</script>");
			}
			return;
		}
		StringBuffer sb = new StringBuffer();
		if(!jsContext.equals("true")){
			sb.append("<script type=\"text/javascript\">\n");
		}
		for(int i=0; i<urlArr.length; i++){
			if(this.securityCheck(urlArr[i])){
				sb.append("var ").append(varNameArr[i]).append("=true;\n");
			}else{
				sb.append("var ").append(varNameArr[i]).append("=false;\n");
			}
		}
		if(!jsContext.equals("true")){
			sb.append("</script>\n");
		}
		out.println(sb.toString());		
	}
	public boolean securityCheck(String url) {
		if (getVisit() == null) {
			return false;
		}
		Visit vist = getVisit();
		if(("taskAction!taskLists".equals(url)||"testTaskManagerAction!flwSetInit".equals(url)||
				"bugManagerAction!loadMyBug".equals(url)||
				"bugManagerAction!loadAllMyBug".equals(url))&&vist.getUserInfo().getIsAdmin()>=1){
			return true;
		}		
		if (!vist.getUserInfo().getPrivilege().contains(url.trim())&&!"taskAction!taskLists".equals(url)) {
			return false;
		}

		return true;
	}
	private Visit getVisit() {

		return SecurityContextHolder.getContext().getVisit();
	}


	public String getVarNames() {
		return varNames;
	}

	public void setVarNames(String varNames) {
		this.varNames = varNames;
	}

	public String getJsContext() {
		return jsContext;
	}

	public void setJsContext(String jsContext) {
		this.jsContext = jsContext;
	}

	public String getUrls() {
		return urls;
	}

	public void setUrls(String urls) {
		this.urls = urls;
	}

}

