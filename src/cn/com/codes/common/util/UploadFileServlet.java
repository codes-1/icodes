package cn.com.codes.common.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.opensymphony.webwork.ServletActionContext;

/**
 * Servlet implementation class UploadFileServlet
 */
@WebServlet("/UploadFileServlet")
public class UploadFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadFileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

//    
//    String tempPath = "D:\\test";
//    String filePath = "D:\\filePath";
    String tempPath = ServletActionContext.getServletContext().getRealPath("/")+"/itest/uploadTest";
    String filePath = ServletActionContext.getServletContext().getRealPath("/")+"/itest/upload";
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/plain;charset=utf-8");
        PrintWriter pw = res.getWriter();
        try {
            DiskFileItemFactory diskFactory = new DiskFileItemFactory();
            // threshold 极限、临界值，即硬盘缓存 1M
            diskFactory.setSizeThreshold(4 * 1024);
            // repository 贮藏室，即临时文件目录
            diskFactory.setRepository(new File(tempPath));
            ServletFileUpload upload = new ServletFileUpload(diskFactory);
            // 设置允许上传的最大文件大小 4M
            upload.setSizeMax(4 * 1024 * 1024);
            // 解析HTTP请求消息头
            List<FileItem> fileItems = upload.parseRequest(req);
            Iterator<FileItem> iter = fileItems.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                if (item.isFormField()) {
                   // System.out.println("处理表单内容 ...");
                    processFormField(item, pw);
                } else {
                   // System.out.println("处理上传的文件 ...");
                    processUploadFile(item, pw);
                }
            }
            pw.close();
        } catch (Exception e) {
           // System.out.println("使用 fileupload 包时发生异常 ...");
            e.printStackTrace();
        }
    }

    // 处理表单内容
    private void processFormField(FileItem item, PrintWriter pw) throws Exception {
        String name = item.getFieldName();
        String value = item.getString();
        pw.println(name + " : " + value + "\r\n");
    }

    // 处理上传的文件
    private void processUploadFile(FileItem item, PrintWriter pw) throws Exception {
        // 此时的文件名包含了完整的路径，得注意加工一下
        String filename = item.getName();
        //System.out.println("完整的文件名：" + filename);
        int index = filename.lastIndexOf("\\");
        filename = filename.substring(index + 1, filename.length());
        long fileSize = item.getSize();
        if ("".equals(filename) && fileSize == 0) {
           // System.out.println("文件名为空 ...");
            return;
        }
        File uploadFile = new File(filePath + "/" + filename);
        File uploadFile2 = new File(filePath);
        if(!uploadFile2.exists()){
            uploadFile2.mkdirs();
        }
        item.write(uploadFile);
        pw.println(filename + " 文件保存完毕 ...");
        pw.println("文件大小为 ：" + fileSize + "\r\n");
    }

}
