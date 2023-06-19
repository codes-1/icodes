package cn.com.codes.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WelcomeServlet extends HttpServlet {

	String webclomeUrl;
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// 下面这三行一下不要删了，要不上传成功后，在iframe里的脚本无法执行
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		((HttpServletRequest) request).getSession().invalidate();
		request.getRequestDispatcher(webclomeUrl).forward(request, response);

	}
	

	public void init(ServletConfig config) throws ServletException {
		
		webclomeUrl = config.getInitParameter("webclomeUrl");
	}
}
