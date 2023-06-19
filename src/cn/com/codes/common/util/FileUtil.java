
  
package cn.com.codes.common.util;  

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.UUID;

import com.opensymphony.webwork.ServletActionContext;

import cn.com.codes.framework.exception.DataBaseException;
import cn.com.codes.framework.security.filter.SecurityContextHolder;


public class FileUtil {
	//最大缓存空间 
	private static final int BUFFER_SIZE = 512;
	
	/**
	 * 完成文件上传的同时，返回路径path(相对路径)
	 * @param file：上传的文件
	 * @param string：上传的文件名
	 * @param model：模块名称(用户，医生或其他)
	 * @return：文件路径
	 * 
	 * 1：完成文件上传的要求
		  1：将上传的文件统一放置到upload的文件夹下
		  2：将每天上传的文件，使用日期格式的文件夹分开，将每个业务的模块放置统一文件夹下
		  3：上传的文件名要指定唯一，可以使用UUID的方式，也可以使用日期作为文件名
		  4：封装一个文件上传的方法，该方法可以支持多文件的上传，即支持各种格式文件的上传
		  5：保存路径path的时候，使用相对路径进行保存，这样便于项目的可移植性
	 */
	public static String fileUploadReturnPath(File file, String fileName,String classify,
			String model) {
		//1:获取需要上传文件统一的路径path（即upload）
		String basepath = ServletActionContext.getServletContext().getRealPath("/upload");
		//2:获取日期文件夹(格式：/yyyy/MM/dd/)
//		String datepath = DateUtils.dateToStringByFile(new Date());
		//格式（upload\2014\12\01\用户管理）
		String filePath = basepath+"/"+classify+"/"+model;
		//3：判断该文件夹是否存在，如果不存在，创建
		File dateFile = new File(filePath);
		if(!dateFile.exists()){
			dateFile.mkdirs();//创建
		}
		//4：指定对应的文件名
		//文件的后缀
		String prifix = fileName.substring(fileName.lastIndexOf("."));
		//uuid的文件名
		String uuidFileName = UUID.randomUUID().toString()+prifix;
		//最终上传的文件（目标文件）
		File destFile = new File(dateFile,uuidFileName);
		//上传文件
		try {
			org.apache.commons.io.FileUtils.copyFile(file, destFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "/upload/"+classify+"/"+model+"/"+uuidFileName;
	}
	
	// 复制文件
	public static void copyFile(File sourceFile, File targetFile)
			throws IOException {
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			// 新建文件输入流并对它进行缓冲
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

			// 新建文件输出流并对它进行缓冲
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

			// 缓冲数组
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			outBuff.flush();
		} finally {
			// 关闭流
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}

	public static void fileChannelCopy(File sourceFile, File targetFile) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(sourceFile);
			fo = new FileOutputStream(targetFile);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fi) {
					fi.close();
				}
				if (null != in) {
					in.close();
				}
				if (null != fo) {
					fo.close();
				}
				if (null != out) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 复制文件夹
	public static void copyDirectiory(String sourceDir, String targetDir)
			throws IOException {
		File sourceDirFile = new File(sourceDir);
		if(!sourceDirFile.exists())return;
		// 新建目标目录
		File targetDirFile = new File(targetDir);
		if(!targetDirFile.exists() || !targetDirFile.isDirectory()){
			targetDirFile.mkdirs();
		}
		// 获取源文件夹当前下的文件或目录
		File[] file = sourceDirFile.listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile = file[i];
				// 目标文件
				File targetFile = new File(
						new File(targetDir).getAbsolutePath() + File.separator
								+ file[i].getName());
				fileChannelCopy(sourceFile, targetFile);
			}
			if (file[i].isDirectory()) {
				// 准备复制的源文件夹
				String dir1 = sourceDir + "/" + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + "/" + file[i].getName();
				copyDirectiory(dir1, dir2);
			}
		}
	}

	
	/** 
	* 方法功能描述:获取文件后缀名
	* @author 田育林
	*/
	public static String getFileExp(String fileName) {
		try {
			int index = fileName.lastIndexOf(".");
			return fileName.substring(index);
		} catch (Exception e) {
			throw new DataBaseException(e.getMessage(), e);
		}
	}
	
	/** 
	* 方法功能描述:通过数据流将图片从源文件拷贝到目标文件
	* @author 田育林
	*/
	public static void copyToFile(File srcFile, File destFile) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			try {
				inputStream = new BufferedInputStream(new FileInputStream(srcFile), BUFFER_SIZE);
				outputStream = new BufferedOutputStream(new FileOutputStream(destFile), 64*BUFFER_SIZE);
				byte[] buffer = new byte[16*BUFFER_SIZE];
				
				while(inputStream.read(buffer) > 0) {
					outputStream.write(buffer);
				}
			} finally {
				if (null != inputStream) {
					inputStream.close();
				}
				if (null != outputStream) {
					outputStream.close();
				}
			}
		} catch (Exception e) {
			throw new DataBaseException(e.getMessage(), e);
		}
	}
	
    /**
     * 删除目录（文件夹）以及目录下的文件
     * @param   sPath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    public static void deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return;
        }
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                deleteFile(files[i].getAbsolutePath());
            } //删除子目录
            else {
                deleteDirectory(files[i].getAbsolutePath());
            }
        }
        //删除当前目录
       dirFile.delete();
    }
    
    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static void deleteFile(String sPath) {
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }
    
    /** 
	* 方法名:          delFile
	* 方法功能描述:    删除磁盘指定路径下的文件夹及其文件
	* @param:         
	* @return:        
	* @Author:        李飞
	* @Create Date:   2015年3月31日 下午5:40:38
	*/
	public static boolean delFolder(String imgPath) {
		boolean flag = false;
		
		try {
			String folder = "/" + imgPath.split("/")[1] + "/" + imgPath.split("/")[2];
			folder = SecurityContextHolder.getContext().getRequest().getServletContext().getRealPath(folder);
			
			// 删除文件及空文件夹
			File temp = new File(folder);
			File[] files = null;
			if (temp.isDirectory() && temp.exists()) {
				files = temp.listFiles();
				
				// 删除文件
				if (null != files) {
					for (int i = 0; i < files.length; i++) {
						if(files[i].isDirectory()){
							File[] fs = files[i].listFiles();
							for(File f : fs){
								f.delete();
							}
							if (files[i].list().length == 0) {
								files[i].delete();
							}
						}else{
							files[i].delete();
						}
					}
				}
				
				// 删除空文件夹
				if (temp.list().length == 0) {
					temp.delete();
				}
			} else if (temp.isFile() && temp.exists()) {
				temp.delete();
			}
		} catch (Exception e) {
			throw new DataBaseException(e.getMessage(), e);
		}
		
		return flag;
	}
	
	/** 
	* 方法名:          delFile
	* 方法功能描述:    删除文件，如果文件所在文件夹为空，同时删除文件夹
	* @param:         
	* @return:        
	* @Author:        李飞
	* @Create Date:   2015年11月2日 下午5:41:22
	*/
	public static boolean delFile(String imgPath) {
		boolean flag = false;
		
		try {
			// 先删除文件
			File file = new File(SecurityContextHolder.getContext().getRequest().getServletContext().getRealPath(imgPath));
			if (file.exists() && file.isFile()) {
				file.delete();
				
				// 删除文件后，文件所在的文件夹为空，则删除文件夹
				File folder = new File(SecurityContextHolder.getContext().getRequest().getServletContext().getRealPath(
					imgPath.substring(0, imgPath.lastIndexOf("/"))));
				
				if (folder.exists() && folder.isDirectory()) {
					File[] files = folder.listFiles();
					
					if (files.length == 0) {
						folder.delete();
					}
				}
				flag = true;
			} else {
				flag = false;
			}
		} catch (Exception e) {
			throw new DataBaseException(e.getMessage(), e);
		}
		
		return flag;
	}
}
  
