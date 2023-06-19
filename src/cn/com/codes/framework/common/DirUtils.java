package cn.com.codes.framework.common;

import static cn.com.codes.framework.common.LogWrap.info;

import java.io.File;

import org.apache.log4j.Logger;

import cn.com.codes.framework.common.DirUtils;

public final class DirUtils {
	private static final Logger log = Logger.getLogger(DirUtils.class);

	public static boolean delFolder(String folderPath) {
		try {
			if (!delAllFile(folderPath))
				return false;
			return true;
		} catch (Exception e) {
			log.error("delete dir fail:" + e.getMessage());
			return false;
		}
	}

	private static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);
				delFolder(path + "/" + tempList[i]);
				flag = true;
			}
		}
		file.delete();
		return flag;
	}

	public static boolean buildDir(String fileRealPath) {
		File dirFile = null;
		try {
			dirFile = new File(fileRealPath);
			if (!(dirFile.exists()) && !(dirFile.isDirectory())) {
				boolean creadok = dirFile.mkdirs();
				if (creadok) {
					info(log, "create dir :"+fileRealPath +" success!");
					return true;
				} else {
					info(log, "create dir :"+fileRealPath +"fail!");
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			log.error("create dir fail ：" + e.getMessage());
			return false;
		}
	}
}
