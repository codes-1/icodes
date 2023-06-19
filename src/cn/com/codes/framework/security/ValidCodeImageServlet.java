package cn.com.codes.framework.security;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import cn.com.codes.framework.common.Global;
import cn.com.codes.framework.security.ValidCodeImage;
import cn.com.codes.framework.security.ValidCodeImageServlet;

public class ValidCodeImageServlet extends HttpServlet {
	
	private static final String CONTENT_TYPE = "image/jpg; charset=UTF-8";
	
	private static Logger logger = Logger
	.getLogger(ValidCodeImageServlet.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE); // 设定输出的类型
		// 设置浏览器不要缓存此图片
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response
				.getOutputStream());
		ValidCodeImage validCodeImage = new ValidCodeImage();
		int width = 60, height = 20;
		BufferedImage image = validCodeImage.getBufferedImage(width, height);
		// 将图像输出到客户端
		ServletOutputStream sos = response.getOutputStream();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(image, "JPEG", bos);
		byte[] buf = bos.toByteArray();
		response.setContentLength(buf.length);
		// 输出图象
		sos.write(buf);
		bos.close();
		sos.close();

		// 将当前验证码存入到Session中
		HttpSession session = request.getSession();
		try {
			session.setAttribute(Global.VALID_CODE, validCodeImage.getValidCode());
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
