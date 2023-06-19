package cn.com.codes.framework.upload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.framework.app.services.BaseService;
import cn.com.codes.framework.common.Global;
import cn.com.codes.framework.common.config.PropertiesBean;
import cn.com.codes.framework.common.util.Context;
import cn.com.codes.framework.security.filter.SecurityContext;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.object.Company;
import cn.com.codes.framework.upload.MonitoredDiskFileItemFactory;
import cn.com.codes.framework.upload.SmartUploadServlet;
import cn.com.codes.framework.upload.UploadListener;

public class SmartUploadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static BaseService baseService ;
	private ServletConfig config;
	private static Logger loger = Logger.getLogger(SmartUploadServlet.class);
	
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
		if ("chkUpLoad".equalsIgnoreCase(request.getParameter("cmd"))) {
			chkUpStatus(request, response);
		}else if("upload".equalsIgnoreCase(request.getParameter("cmd"))) {
			doFileUpload(request, response);
		}else if("imp".equalsIgnoreCase(request.getParameter("cmd"))) {
			doFileUpload(request, response);
		}
		else if("download".equalsIgnoreCase(request.getParameter("cmd"))) {
			this.doDownLoad(request, response);
		}else if("erase".equalsIgnoreCase(request.getParameter("cmd"))){
			 request.getSession().removeAttribute("FILE_UPLOAD_STATS");
			 request.getSession().removeAttribute("FILE_UPLOAD_STATS"+request.getParameter("upTime").toString());
			 request.getSession().removeAttribute("ManyUpOccur"+request.getParameter("upTime").toString());
		}else if("delete".equalsIgnoreCase(request.getParameter("cmd"))){
			this.eraseAttach(request, response);
		}else if("getTemplate".equalsIgnoreCase(request.getParameter("cmd"))) {
			this.doDownTempplate(request, response);
		}
	}
	public void eraseAttach(HttpServletRequest request, HttpServletResponse response){
		if(request.getParameter("eraseFiles")==null){
			return;
		}
		String[] initFiles = request.getParameter("eraseFiles").split(" ");
		for(String initFile:initFiles){
			(new  File(this.getUpDirectory(), initFile)).delete();
		}
	}
	  public void doDownLoad(HttpServletRequest request, HttpServletResponse response)  {    
	      java.io.BufferedInputStream bis = null;    
	      java.io.BufferedOutputStream bos = null;    
	      String fileName = request.getParameter("downloadFileName"); 
	      String initFileName = "";
	      String filePath = "";
	      if("MYPM_Solution.pdf".equals(fileName)){
	    	  filePath = config.getServletContext().getRealPath(Global.upPath)+File.separator +"MYPM_Solution.pdf";
	    	  initFileName = "MYPM项目管理解决方案.pdf";
	      }else{
		      initFileName = fileName.substring(fileName.indexOf("_")+1) ;
		      filePath = getUpDirectory() + File.separator+fileName  ; 
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
	    	 // loger.error(e);   
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
	  public void doDownTempplate(HttpServletRequest request, HttpServletResponse response)  {    
	      java.io.BufferedInputStream bis = null;    
	      java.io.BufferedOutputStream bos = null;    
	      String fileName = request.getParameter("downloadFileName"); 
	      String filePath = config.getServletContext().getRealPath(Global.upPath)+File.separator+"template"+File.separator +fileName;
	      String initFileName = fileName.substring(fileName.indexOf("_")+1) ;
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
	    	 // loger.error(e);   
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
	private void doFileUpload(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		UploadListener listener = new UploadListener();
		//下面处理是防止同时上传并发
		if(request.getSession().getAttribute("FILE_UPLOAD_STATS")!=null){//表明当前正在上传别的附件，用它来控制并发
			request.getSession().setAttribute("ManyUpOccur"+request.getParameter("upTime").toString(),"");
			//发生并发后，用ManyUpOccur+当前上传时间来，表明，当前这具上传碰到了并发，必须带上上传时间，因为正在上传的那个线程
			//还要正常上传,当这个碰到并发的上传的，检查上传进度被调用时，通过这标记来，停止再次的进度轮循
			//结束这次上传任务
			sendCompleteResponse(response,"ManyUpOccur",null);		
			return;
		}
		// 创建UploadListener对象
		listener.setTotalSize(request.getContentLength());
		HttpSession session = request.getSession();
		listener.start();// 启动监听状态
		// 将监听器对象的状态保存在Session中
		session.setAttribute("FILE_UPLOAD_STATS"+request.getParameter("upTime").toString(), listener
				.getFileUploadStats());
		session.setAttribute("FILE_UPLOAD_STATS","");
		// 创建MonitoredDiskFileItemFactory对象
		//原为做的读取时也要监听进度，现在不用了
		FileItemFactory factory = new MonitoredDiskFileItemFactory(listener);
		// 通过该工厂对象创建ServletFileUpload对象
		ServletFileUpload upload = new ServletFileUpload(factory);
		PropertiesBean conf = (PropertiesBean) Context.getInstance().getBean("ContextProperties");
		String allSize = conf.getProperty("mypm.upload.sizeMax");
		String singleSize = conf.getProperty("mypm.upload.fileSizeMax");
		upload.setSizeMax(Integer.valueOf(allSize)*1024*1024);
		upload.setFileSizeMax(Integer.valueOf(singleSize)*1024*1024);
		upload.setFileItemFactory(factory);
		// 将转化请求保存到list对象中
		List<FileItem> items = null;
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e) {
			listener.error("error");//记录下发生错误，让进度检查程序不再轮循
			request.getSession().removeAttribute("FILE_UPLOAD_STATS");//删除当前用户正上传标记
			loger.error(e);
			if(e.getMessage()!=null&&e.getMessage().indexOf("exceeds its maximum permitted")>0){
				sendCompleteResponse(response,"fileExceeds",null);
			}else if(e.getMessage()!=null&&e.getMessage().indexOf("exceeds the configured maximum")>0){
				sendCompleteResponse(response,"Over5M",null);
			}else{
				sendCompleteResponse(response,"error",null);
			}
			return; 
		}catch(Exception e){
			listener.error("error");//记录下发生错误，让进度检查程序不再轮循
			request.getSession().removeAttribute("FILE_UPLOAD_STATS");//删除当前用户正上传标记
			sendCompleteResponse(response,"error",null);
			return;
		}
		if(!this.allowFileType(items)){
			listener.error("error");
			listener = null;
			request.getSession().removeAttribute("FILE_UPLOAD_STATS");
			sendCompleteResponse(response, "typeDeny",null);// 调用sendCompleteResponse方法
			return;
		}
		// 循环list中的对象
		StringBuffer fileNameSb = new StringBuffer();
		for (Iterator<FileItem> i = items.iterator(); i.hasNext();) {
			FileItem fileItem = i.next();
			String saveFileName = null;
			if (!fileItem.isFormField()) {// 如果该FileItem不是表单域
				saveFileName = processUploadedFile(fileItem, request,response);
				if(!fileItem.getName().equals("")){
					//用*分开是因为文件名不许含它
					fileNameSb.append(" ").append(saveFileName);
				}else{
					return;
				}
			}
		}
		// 停止使用监听器
		listener.done();
		listener = null;
		session.removeAttribute("FILE_UPLOAD_STATS");
		sendCompleteResponse(response, null,SecurityContextHolderHelp.getUpDirectory()+fileNameSb.toString());
		
	}

	/**
	 * 
	 * @param item
	 * @param request
	 * @param response
	 * @return 在服务器了保存的文件名
	 * @throws IOException
	 */
	private String processUploadedFile(FileItem item,HttpServletRequest request
			,HttpServletResponse response)throws IOException {
		// 获得上传文件的文件名
		String fileName = item.getName().substring(
				item.getName().lastIndexOf(File.separator) + 1);
		if(fileName==null||"".equals(fileName)){
			UploadListener.FileUploadStats fileUploadStats = (UploadListener.FileUploadStats)request.getSession()
			.getAttribute("FILE_UPLOAD_STATS"+request.getParameter("upTime").toString());
			fileUploadStats.setCurrentStatus("error");//记录下发生错误，让进度检查程序不再轮循
			request.getSession().removeAttribute("FILE_UPLOAD_STATS");//删除当前用户正上传标记
			item.delete();
			return "";
		}
		fileName = (new Date()).getTime()+"_"+fileName;
		String rewriteFileName = (new Date()).getTime()+fileName.substring(fileName.lastIndexOf("."));
		File file = new File(this.getUpDirectory(), rewriteFileName);
		try {
			item.write(file);
		} catch (Exception e) {			
			UploadListener.FileUploadStats fileUploadStats = (UploadListener.FileUploadStats)request.getSession()
			.getAttribute("FILE_UPLOAD_STATS"+request.getParameter("upTime").toString());
			fileUploadStats.setCurrentStatus("error");//记录下发生错误，让进度检查程序不再轮循
			request.getSession().removeAttribute("FILE_UPLOAD_STATS");//删除当前用户正上传标记
			loger.error(e);
			sendCompleteResponse(response,"error",null);
			fileName="";
		}finally{
			item.delete();
		}
		//return fileName;
		return rewriteFileName;
	}

	private boolean allowFileType(List<FileItem> items){
		String fileName = null;
		for(FileItem fileItem :items){
			fileName = fileItem.getName();
			if(fileName==null||"".equals(fileName)){
				continue;
			}
			fileName = fileName.substring(
					fileItem.getName().lastIndexOf(File.separator) + 1);
			if(fileName.endsWith(".exe")||fileName.endsWith(".EXE")
					||fileName.endsWith(".BAT")||fileName.endsWith(".bat")
					||fileName.endsWith(".sh")||fileName.endsWith(".sh")
					||fileName.endsWith(".jsp")||fileName.endsWith(".js")
					||fileName.endsWith(".htm")||fileName.endsWith(".html")||fileName.endsWith(".sql")){
				return false;
			}else if(fileName.indexOf(".")<0){
				return false;
			}
		}
		return true;
	}
	private void chkUpStatus(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// 设置该响应不在缓存中读取HttpServletRequest request
		response.addHeader("Expires", "0");
		response.addHeader("Cache-Control",
				"no-store, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.addHeader("Pragma", "no-cache");
		// 获得保存在Session中的状态信息 
		if(request.getSession().getAttribute("ManyUpOccur"+request.getParameter("upTime").toString())!=null){
			response.getWriter().print("ManyUpOccur");
			return;
		}
		UploadListener.FileUploadStats fileUploadStats = (UploadListener.FileUploadStats)request.getSession()
				.getAttribute("FILE_UPLOAD_STATS"+request.getParameter("upTime").toString());
		if(request.getParameter("upPercent").toString().equals("error")){
			response.getWriter().print("error");
			return;
		}
		long upPercent =Long.valueOf(request.getParameter("upPercent"));
		long percentComplete=0;
		if (fileUploadStats != null&&upPercent!=fileUploadStats.getBytesRead()) {
			if(fileUploadStats.getCurrentStatus().equals("error")){
				response.getWriter().println("error");
				return;
			}
			long bytesProcessed = fileUploadStats.getBytesRead();// 获得已经上传的数据大小
			long sizeTotal = fileUploadStats.getTotalSize();// 获得上传文件的总大小
			// 计算上传完成的百分比
			percentComplete = (long) Math
					.floor(((double) bytesProcessed / (double) sizeTotal) * 100.0);
			if (bytesProcessed != sizeTotal) {
				StringBuffer sb = new StringBuffer();
				sb.append(percentComplete).append("^");
				sb.append("<div class=\"prog-border\"><div class=\"prog-bar\" style=\"width: ");
				sb.append(percentComplete).append("%;\"></div></div>");
				response.getWriter().println(sb.toString());
			} else {
				// 如果文件已经上传完毕
				response.getWriter().print(
						"100^<div class=\"prog-border\"><div class=\"prog-bar\" style=\"width:100%;\"></div></div>");
			}
		}
		if(percentComplete==upPercent){
			try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			loger.error(e);
		}
	}

	}

	private void sendCompleteResponse(HttpServletResponse response,
			String message,String saveFileName) throws IOException {
		//response.setCharacterEncoding("UTF-8");
		//response.setContentType("text/html; charset=UTF-8");
		//String fileName = new String(saveFileName.getBytes("utf-8"),"ISO8859-1");
		if (message == null) {
			response.getOutputStream().print(
							"<html><head> <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" /><script type='text/javascript'>function loadResult() { window.parent.stopStatusChk('finish','"+saveFileName+"'); }</script></head><body onload='loadResult()'></body></html>");
		} else {
			response.getOutputStream().print(
							"<html><head><script type='text/javascript'>function loadResult() { window.parent.stopStatusChk('"
									+ message
									+ "'); }</script></head><body onload='loadResult()'></body></html>");
		}
	}
	public  void init(ServletConfig config) throws ServletException {
		this.config = config;
	}
	

	
	private String getUpDirectory(){
		String upDirectory = SecurityContextHolderHelp.getUpDirectory();
		if(upDirectory!=null){
			String realPath = config.getServletContext().getRealPath(Global.upPath);
			upDirectory = realPath+File.separator+upDirectory;
			return upDirectory;
		}
		if(baseService == null){
			baseService = (BaseService)WebApplicationContextUtils
			.getWebApplicationContext(config.getServletContext()).getBean("myPmbaseService");
		}
		SecurityContext sc = SecurityContextHolder.getContext();
		StringBuffer hql = new StringBuffer();
		hql.append("select new  Company(loginName) from Company where id=?");
		String compId = SecurityContextHolderHelp.getCompanyId();
		Company comp = (Company)baseService.findByHql(hql.toString(), compId).get(0);
		//ConfigHolder holder = ConfigHolder.getInstance("fckeditor.properties"); 
		//String realPath = config.getServletContext().getRealPath(holder.getProperty("fckeditor.basePath"));
		String realPath = config.getServletContext().getRealPath(Global.upPath);
		upDirectory = realPath+File.separator+comp.getLoginName();
		sc.getVisit().setUpDirectory(comp.getLoginName());
		comp = null;
		if(!new File(upDirectory).isDirectory()) { 
			 new File(upDirectory).mkdirs(); 
		}
		return upDirectory;
	}
	
 
}
