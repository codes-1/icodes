package cn.com.codes.framework.upload;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.fileupload.disk.DiskFileItem;

import cn.com.codes.framework.upload.MonitoredOutputStream;
import cn.com.codes.framework.upload.OutputStreamListener;

public class MonitoredDiskFileItem extends DiskFileItem {
	private MonitoredOutputStream mos = null;
	private OutputStreamListener listener;
	
	public MonitoredDiskFileItem(String fieldName, String contentType,
			boolean isFormField, String fileName, int sizeThreshold,
			File repository, OutputStreamListener listener) {
		super(fieldName, contentType, isFormField, fileName, sizeThreshold,
				repository);
		this.listener = listener;
	}
	public OutputStream getOutputStream() throws IOException {
		if (mos == null) {
			mos = new MonitoredOutputStream(super.getOutputStream(), listener);
		}
		return mos;
	}
}
