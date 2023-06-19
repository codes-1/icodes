package cn.com.codes.common.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import cn.com.codes.framework.common.JsonUtil;


@WebServlet("/Uploader")
public class UploaderServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(UploaderServlet.class);
	private static final long serialVersionUID = 1L;
	String repositoryPath;
	String uploadPath;
	String relativeUrl;

	/**
	 * 上传处理
	 * @方法名:doPost 
	 * @参数:@param request
	 * @参数:@param response
	 * @参数:@throws ServletException
	 * @参数:@throws IOException 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8"); 
		String type = request.getParameter("type");
		if(logger.isInfoEnabled()) {
			logger.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++"+type);
		}
		//System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++"+type);
		Integer schunk = null;//分割块数
		Integer schunks = null;//总分割数
		String name = null;//文件名
		List<FileInfoVo> fileInfos = new ArrayList<FileInfoVo>();
		BufferedOutputStream outputStream=null; 
		if (ServletFileUpload.isMultipartContent(request)) {
			try {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold(1024);
				factory.setRepository(new File(repositoryPath));//设置临时目录
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setHeaderEncoding("UTF-8");
				upload.setSizeMax(1024 * 1024 * 1024);//设置文件最大值
				List<FileItem> items = upload.parseRequest(request);
				String fileUrl="";
				//生成新文件名
				String newFileName = null;
				for (FileItem item : items) {
					if (!item.isFormField()) {// 如果是文件类型
						FileInfoVo fileInfoVo = new FileInfoVo();
						fileInfoVo.setFileOriginalName(item.getName());
						name = item.getName();// 获得文件名
						newFileName = UUID.randomUUID().toString().replace("-","").concat(".").concat(FilenameUtils.getExtension(name));
						if (name != null) {
							String nFname = newFileName;
							if (schunk != null) {
								nFname = schunk + "_" + name;
							}
							String filePath = uploadPath+"\\"+type;
							File f = new File(filePath);
							if (!f.exists()) {
								f.mkdirs();
							}
							File savedFile = new File(filePath, nFname);
							item.write(savedFile);
							fileInfoVo.setFileUrl(relativeUrl+"/"+type+"/"+nFname);
						}
						fileInfos.add(fileInfoVo);
					} else {
						//判断是否带分割信息
						if (item.getFieldName().equals("chunk")) {
							schunk = Integer.parseInt(item.getString());
						}
						if (item.getFieldName().equals("chunks")) {
							schunks = Integer.parseInt(item.getString());
						}
						if (item.getFieldName().equals("fileName")) {
							newFileName = new String(item.getString().getBytes("8859_1"),"utf-8");  
						}
					}
				}
				
				if (schunk != null && schunk + 1 == schunks) {
					outputStream = new BufferedOutputStream(new FileOutputStream(new File(uploadPath, newFileName)));
					//遍历文件合并
					for (int i = 0; i < schunks; i++) {
						File tempFile=new File(uploadPath,i+"_"+name);
						byte[] bytes=FileUtils.readFileToByteArray(tempFile);  
						outputStream.write(bytes);
						outputStream.flush();
						tempFile.delete();
					}
					outputStream.flush();
				}
//				response.getWriter().write("{\"path\":\"1111111111111111111\"}");
//				response.getWriter().write("{\"status\":true,\"newName\":\""+uploadPath+"\\"+newFileName+"\"}");
				response.getWriter().write(JsonUtil.toJson(fileInfos).toString());
			} catch (FileUploadException e) {
				e.printStackTrace();
				response.getWriter().write(JsonUtil.toJson("false"));
			} catch (Exception e) {
				e.printStackTrace();
				response.getWriter().write(JsonUtil.toJson("false"));
			}finally{  
	            try {  
	            	if(outputStream!=null)
	            		outputStream.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }   
		}
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		//repositoryPath = FileUtils.getTempDirectoryPath();
		relativeUrl = config.getInitParameter("uploadPath");
		uploadPath = config.getServletContext().getRealPath(config.getInitParameter("uploadPath"));
		repositoryPath = uploadPath.replace("uploadFile", "temp");
		File up = new File(uploadPath);
		if(!up.exists()){
			up.mkdir();
		}
		File upTemp = new File(repositoryPath);
		if(logger.isInfoEnabled()) {
			logger.info("upTemp=="+repositoryPath);
		}
		if(!upTemp.exists()){
			upTemp.mkdir();
		}
	}
}
