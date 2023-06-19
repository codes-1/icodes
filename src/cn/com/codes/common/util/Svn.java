package cn.com.codes.common.util;

import java.io.File;

public class Svn {
	public static void main(String[] args) {
		File path = new File("E:\\服务器\\mypm");
		deleteSVN(path);
	}

	private static void deleteSVN(File path) {
		if (path.exists() && path.isDirectory()) {
			File[] files = path.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					if (file.getName().equals(".svn")) {
						//System.out.println("delete file: " + file.getAbsolutePath());
						deleteFile(file);
					} else {
						deleteSVN(file);
					}
				}
			}
		}
	}

	private static void deleteFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]);
				}
			}
			file.delete();
		} else {
			System.out.println("not exist" + '\n');
		}
	}
}
