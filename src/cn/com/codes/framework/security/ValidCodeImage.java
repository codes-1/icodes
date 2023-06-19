package cn.com.codes.framework.security;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import cn.com.codes.framework.security.ValidCodeImage;


public class ValidCodeImage {

	private static Logger logger = Logger
	.getLogger(ValidCodeImage.class);
	String validCode = ""; // 返回的验证码内容

	/**
	 * 创建验证码图像文件 格式为jpg类型
	 * 
	 * @param fileName
	 *            String 生成文件名
	 * @param content
	 *            String 图片内容
	 * @param width
	 *            int 图片宽度
	 * @param height
	 *            int 图片高度
	 */
	public void creatImage(String fileName, String content, int width,
			int height) {
		// 在内存中创建图象
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		// 获取图形上下文
		Graphics2D g = image.createGraphics();
		// 产生随机的认证码
		char[] rands = this.getChar();
		// 产生图像
		this.drawBackground(g, width, height);
		drawRands(g, rands);

		g.dispose();
		try {
			File f = new File(fileName);
			if (!f.exists()) {
				f.createNewFile();
			} else {
				Thread.sleep(200);
				f.delete();
				f.createNewFile();
			}
			ImageIO.write(image, "jpg", f);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}

	/**
	 * 创建图像 格式为jpg类型
	 * 
	 * @param content -
	 *            String 图片输出内容
	 * @param width -
	 *            int 图片宽度
	 * @param height -
	 *            int 图片高度
	 * @return java.awt.image.BufferedImage
	 */
	public BufferedImage getBufferedImage(int width, int height) {
		// 在内存中创建图象
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		// 获取图形上下文
		Graphics2D g = image.createGraphics();
		// 产生随机的认证码
		char[] rands = this.getChar();
		// 产生图像
		this.drawBackground(g, width, height);
		drawRands(g, rands);

		g.dispose();
		return image;
	}

	/**
	 * 返回一个4位的验证码
	 */

	public String getValidCode() throws InterruptedException {
		return this.validCode;
	}

	/**
	 * 获取随机字符
	 */
	public char[] getChar() {
		// 定义验证码的字符表
		String chars = "0123456789";
		char[] rands = new char[4];
		for (int i = 0; i < 4; i++) {
			int rand = (int) (Math.random() * 10);
			rands[i] = chars.charAt(rand);
			// 对验证码变量赋值
			this.validCode = this.validCode + chars.charAt(rand);
		}
		return rands;
	}

	/**
	 * 在不同的高度上输出验证码的每个字符
	 * 
	 * @param g
	 *            Graphics
	 * @param rands
	 *            char[]
	 */
	private void drawRands(Graphics g, char[] rands) {
		g.setColor(Color.BLACK);
		g.setFont(new Font(null, Font.ITALIC | Font.BOLD, 18));
		// 在不同的高度上输出验证码的每个字符
		g.drawString("" + rands[0], 1, 17);
		g.drawString("" + rands[1], 16, 15);
		g.drawString("" + rands[2], 31, 18);
		g.drawString("" + rands[3], 46, 16);
	}

	/**
	 * 画背景
	 * 
	 * @param g
	 *            Graphics
	 * @param width
	 *            int
	 * @param height
	 *            int
	 */
	private void drawBackground(Graphics g, int width, int height) {
		// 画背景
		g.setColor(new Color(0xDCDCDC));
		g.fillRect(0, 0, width, height);
		// 随机产生120个干扰点
		for (int i = 0; i < 120; i++) {
			int x = (int) (Math.random() * width);
			int y = (int) (Math.random() * height);
			int red = (int) (Math.random() * 255);
			int green = (int) (Math.random() * 255);
			int blue = (int) (Math.random() * 255);
			g.setColor(new Color(red, green, blue));
			g.drawOval(x, y, 1, 0);
		}
	}

	/**
	 * 测试
	 */
	public static void main(String[] args) throws Exception {
		ValidCodeImage c = new ValidCodeImage();
		c.creatImage("c:/me.jpg", c.getValidCode(), 60, 20);
		System.out.println("验证码内容为：" + c.getValidCode());
	}
}
