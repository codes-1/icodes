package cn.com.codes.framework.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.com.codes.framework.common.ResourceUtils;

public class ResourceUtils {

	private static ClassLoader defaultClassLoader;

	public ResourceUtils() {

	}

	/**
	 * Returns the default classloader (may be null).
	 * 
	 * @return The default classloader
	 */
	public static ClassLoader getDefaultClassLoader() {
		return defaultClassLoader;
	}

	/**
	 * Sets the default classloader
	 * 
	 * @param defaultClassLoader -
	 *            the new default ClassLoader
	 */
	public static void setDefaultClassLoader(ClassLoader ClassLoader) {
		defaultClassLoader = ClassLoader;
	}

	public static URL getFileURL(String path) {
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		ClassLoader loader = ResourceUtils.class.getClass().getClassLoader();
		if (loader == null) {
			loader = Thread.currentThread().getContextClassLoader();
		}
		URL fileUrl = loader.getResource(path);
		if (fileUrl == null) {
			fileUrl = loader.getResource(path);
		}
		return fileUrl;
	}

	public static InputStream getInputStream(String path) throws IOException {
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		InputStream is = null;
		is = getClassLoader().getResourceAsStream(path);
		if (is == null) {
			throw new FileNotFoundException(
					" cannot be opened because it does not exist");
		}
		return is;
	}

	public static URL getFileURL(String fileName, ClassLoader loader) {

		if (loader == null) {
			loader = Thread.currentThread().getContextClassLoader();
		}
		URL fileUrl = loader.getResource(fileName);
		if (fileUrl == null) {
			fileUrl = loader.getResource(fileName);
		}
		return fileUrl;
	}

	public static URL getResourceURL(String resource) throws IOException {
		return getResourceURL(getClassLoader(), resource);
	}

	private static ClassLoader getClassLoader() {
		if (defaultClassLoader != null) {
			return defaultClassLoader;
		} else {
			return Thread.currentThread().getContextClassLoader();
		}
	}

	public static URL getResourceURL(ClassLoader loader, String resource)
			throws IOException {
		URL url = null;
		if (loader != null)
			url = loader.getResource(resource);
		if (url == null)
			url = loader.getResource(resource);
		if (url == null)
			throw new IOException("Could not find resource " + resource);
		return url;
	}

	public static File[] getSpeciDirFiles(String dir, final String fileType) {

		File f = new File(getFileURL(dir).getFile());
		if (fileType == null) {
			return f.listFiles();
		} else {
			return f.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if (name.endsWith("." + fileType)
							|| name.endsWith("." + fileType))
						return true;
					return false;
				}
			});
		}
	}

	public static String[] getSpeciDirFileNames(String dir,
			final String fileType) {

		File[] files = null;
		File f = new File(getFileURL(dir).getFile());
		if (fileType == null) {
			files = f.listFiles();
		} else {
			files = f.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if (name.endsWith("." + fileType))
						return true;
					return false;
				}
			});
		}
		int fileCount = files.length;
		String[] fileNames = new String[fileCount];
		for (int i = 0; i < fileCount; i++) {
			fileNames[i] = dir + "/" + files[i].getName();
		}
		return fileNames;
	}

	public static String[] getSpeciDirSonFileNames(String dir,
			final String fileType) {

		File[] files = null;
		File f = new File(getFileURL(dir).getFile());
		if (fileType == null) {
			files = f.listFiles();
		} else {
			files = f.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if (name.endsWith("." + fileType))
						return true;
					return false;
				}
			});
		}
		int fileCount = files.length;
		String[] fileNames = new String[fileCount];
		for (int i = 0; i < fileCount; i++) {
			fileNames[i] = files[i].getName();
		}
		return fileNames;
	}

	public static List getSpeciDirFileNamesByFiles(String dir,
			final String fileType) {
		List fileNames = new ArrayList();
		File dirFile = new File(dir);
		File[] tempFiles = dirFile.listFiles();
		if(tempFiles == null || tempFiles.length == 0)
			return null;

		for (int i = 0; i < tempFiles.length; i++) {
			if (tempFiles[i].getName().endsWith("." + fileType)) {
				fileNames.add(tempFiles[i].getName());
			}
		}

		return fileNames;
	}
	

}