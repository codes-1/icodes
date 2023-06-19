package cn.com.codes.framework.upload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.com.codes.framework.app.services.BaseService;
import cn.com.codes.framework.common.Global;
import cn.com.codes.framework.upload.HelpDownloadServlet;

public class HelpDownloadServlet extends HttpServlet {

	private static BaseService baseService ;
	private ServletConfig config;
	private static Logger loger = Logger.getLogger(HelpDownloadServlet.class);
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		//下面这三行一下不要删了，要不上传成功后，在iframe里的脚本无法执行
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		this.doDownLoad(request, response);
	}
	
	public  void init(ServletConfig config) throws ServletException {
		this.config = config;
	}
	  public void doDownLoad(HttpServletRequest request, HttpServletResponse response)  {    
	      java.io.BufferedInputStream bis = null;    
	      java.io.BufferedOutputStream bos = null;    
	      String downFlg = request.getParameter("doc"); 
	      String initFileName = "";
	      String filePath = "";
	      //MYPM_Solution.pdf
	      if("soluDoc".equals(downFlg)){
	    	  filePath = config.getServletContext().getRealPath(Global.upPath)+File.separator +"MYPM_Solution.pdf";
	    	  initFileName = "MYPM项目管理解决方案.pdf";
	      }else if("proDoc".equals(downFlg)){
	    	  filePath = config.getServletContext().getRealPath(Global.upPath)+File.separator +"MYPM_ProDoc.pdf";
	    	  initFileName = "MYPM产品说明书.pdf";
	      }else if("testBaseDoc".equals(downFlg)){
	    	  filePath = config.getServletContext().getRealPath(Global.upPath)+File.separator +"testBaseDoc.pdf";
	    	  initFileName = "MYPM测试基础内训双英（中英）资料.pdf";
	      }else{
	    	  filePath = config.getServletContext().getRealPath(Global.upPath)+File.separator +downFlg;
	    	  initFileName = downFlg;
	      }
	      try {    
	         long fileLength = new File(filePath).length();    
	          response.setContentType("application/x-msdownload;");    
	          response.setHeader("Content-disposition", "attachment; filename=" + new String(initFileName.getBytes("GBK"),"ISO8859-1")); 
	          response.setHeader("Content-Length", String.valueOf(fileLength));    
	          bis = new BufferedInputStream(new FileInputStream(filePath));    
	          bos = new BufferedOutputStream(response.getOutputStream());    
	          byte[] buff = new byte[2048];    
	          int bytesRead;    
	          while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {    
	              bos.write(buff, 0, bytesRead);    
	          }    
	      } catch(java.io.FileNotFoundException e){
	    	  //有可能文件被删除了，这里不做任何处理，也不写日志
	      }catch (Exception e) {   
	    	//loger.error(e);   
	      } finally {    
	          try {
				if (bis != null)    
				      bis.close();    
				  if (bos != null)    
				      bos.close();
			} catch (IOException e) {
				//loger.error(e); 
			}    
	      }    
	  } 


}
