package cn.com.codes.common.util.uploadExcel.parsexml;

import java.util.List;

public class XmlEntity {
	private String className;//object类,全名包含包路径
	private int length;//excel文件列长度
	private List<XmlEntity.CellConfig> cellConfigList;
	public class CellConfig{
		private int index;//列索引号
		private String[] match;// 校验器数组
		private String method;//校验方法
		private String propetyName;//Object类属性名
		private boolean noNull;//是否可为空
		private String group;//同一组内数据必有一个不为空
		private String dataType;// 数据类型,标准的java数据类型
		private String errorInformation;//错误提示信息
		public CellConfig(){
		}
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

		public String[] getMatch() {
			return match;
		}

		public void setMatch(String[] match) {
			this.match = match;
		}
		public String getPropetyName() {
			return propetyName;
		}
		public void setPropetyName(String propetyName) {
			this.propetyName = propetyName;
		}
		public boolean isNoNull() {
			return noNull;
		}
		public void setNoNull(boolean noNull) {
			this.noNull = noNull;
		}
		public String getErrorInformation() {
			return errorInformation;
		}
		public void setErrorInformation(String errorInformation) {
			this.errorInformation = errorInformation;
		}
		public String getGroup() {
			return group;
		}
		public void setGroup(String group) {
			this.group = group;
		}
		public String getMethod() {
			return method;
		}
		public void setMethod(String method) {
			this.method = method;
		}
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<CellConfig> getCellConfigList() {
		return cellConfigList;
	}
	public void setCellConfigList(List<CellConfig> cellConfigList) {
		this.cellConfigList = cellConfigList;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
}
