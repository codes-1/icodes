package cn.com.codes.framework.upload;

import java.io.File;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

public class MonitoredDiskFileItemFactory extends DiskFileItemFactory {
	private OutputStreamListener listener = null;
	public MonitoredDiskFileItemFactory(OutputStreamListener listener) {
		super();
		this.listener = listener;
	}
	public MonitoredDiskFileItemFactory(int sizeThreshold, File repository,
			OutputStreamListener listener) {
		super(sizeThreshold, repository);
		this.listener = listener;
	}
	public FileItem createItem(String fieldName, String contentType,
			boolean isFormField, String fileName) {
		
		return new MonitoredDiskFileItem(fieldName, contentType, isFormField,
				fileName, getSizeThreshold(), getRepository(), listener);
	}
}
