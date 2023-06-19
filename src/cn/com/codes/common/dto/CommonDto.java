package cn.com.codes.common.dto;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Locale;

import cn.com.codes.framework.transmission.dto.BaseDto;
import cn.com.codes.licenseMgr.dto.LicesneMgrDto;
import cn.com.codes.common.dto.FeedBack;

public class CommonDto extends BaseDto {

	private String objName; // 重名检查时的pojo类名
	private String nameVal;// 重名检查时的值
	private String idPropName;// 修改操作重名检查时，主键属性名
	private String namePropName;
	private String idPropVal;
	private String projectId;
	private String myHomeUrl;
	private FeedBack feedBack;
	private LicesneMgrDto dto ;
	
	private String operCmd ;
	private String repId;

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public String getIdPropName() {
		return idPropName;
	}

	public void setIdPropName(String idPropName) {
		this.idPropName = idPropName;
	}

	public String getNameVal() {
		return nameVal;
	}

	public void setNameVal(String nameVal) {
		this.nameVal = nameVal;
	}

	public String getIdPropVal() {
		return idPropVal;
	}

	public void setIdPropVal(String idPropVal) {
		this.idPropVal = idPropVal;
	}

	public String getNamePropName() {
		return namePropName;
	}

	public void setNamePropName(String namePropName) {
		this.namePropName = namePropName;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getMyHomeUrl() {
		return myHomeUrl;
	}

	public void setMyHomeUrl(String myHomeUrl) {
		this.myHomeUrl = myHomeUrl;
	}



	public char charAt(int index) {
		return operCmd.charAt(index);
	}

	public int codePointAt(int index) {
		return operCmd.codePointAt(index);
	}

	public int codePointBefore(int index) {
		return operCmd.codePointBefore(index);
	}

	public int codePointCount(int beginIndex, int endIndex) {
		return operCmd.codePointCount(beginIndex, endIndex);
	}

	public int compareTo(String anotherString) {
		return operCmd.compareTo(anotherString);
	}

	public int compareToIgnoreCase(String str) {
		return operCmd.compareToIgnoreCase(str);
	}

	public String concat(String str) {
		return operCmd.concat(str);
	}

	public boolean contains(CharSequence s) {
		return operCmd.contains(s);
	}

	public boolean contentEquals(CharSequence cs) {
		return operCmd.contentEquals(cs);
	}

	public boolean contentEquals(StringBuffer sb) {
		return operCmd.contentEquals(sb);
	}

	public boolean endsWith(String suffix) {
		return operCmd.endsWith(suffix);
	}

	public boolean equals(Object anObject) {
		return operCmd.equals(anObject);
	}

	public boolean equalsIgnoreCase(String anotherString) {
		return operCmd.equalsIgnoreCase(anotherString);
	}

	public byte[] getBytes() {
		return operCmd.getBytes();
	}

	public byte[] getBytes(Charset charset) {
		return operCmd.getBytes(charset);
	}

	public void getBytes(int srcBegin, int srcEnd, byte[] dst, int dstBegin) {
		operCmd.getBytes(srcBegin, srcEnd, dst, dstBegin);
	}

	public byte[] getBytes(String charsetName)
			throws UnsupportedEncodingException {
		return operCmd.getBytes(charsetName);
	}

	public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
		operCmd.getChars(srcBegin, srcEnd, dst, dstBegin);
	}

	public int hashCode() {
		return operCmd.hashCode();
	}

	public int indexOf(int ch, int fromIndex) {
		return operCmd.indexOf(ch, fromIndex);
	}

	public int indexOf(int ch) {
		return operCmd.indexOf(ch);
	}

	public int indexOf(String str, int fromIndex) {
		return operCmd.indexOf(str, fromIndex);
	}

	public int indexOf(String str) {
		return operCmd.indexOf(str);
	}

	public String intern() {
		return operCmd.intern();
	}

	public boolean isEmpty() {
		return operCmd.isEmpty();
	}

	public int lastIndexOf(int ch, int fromIndex) {
		return operCmd.lastIndexOf(ch, fromIndex);
	}

	public int lastIndexOf(int ch) {
		return operCmd.lastIndexOf(ch);
	}

	public int lastIndexOf(String str, int fromIndex) {
		return operCmd.lastIndexOf(str, fromIndex);
	}

	public int lastIndexOf(String str) {
		return operCmd.lastIndexOf(str);
	}

	public int length() {
		return operCmd.length();
	}

	public boolean matches(String regex) {
		return operCmd.matches(regex);
	}

	public int offsetByCodePoints(int index, int codePointOffset) {
		return operCmd.offsetByCodePoints(index, codePointOffset);
	}

	public boolean regionMatches(boolean ignoreCase, int toffset, String other,
			int ooffset, int len) {
		return operCmd.regionMatches(ignoreCase, toffset, other, ooffset, len);
	}

	public boolean regionMatches(int toffset, String other, int ooffset, int len) {
		return operCmd.regionMatches(toffset, other, ooffset, len);
	}

	public String replace(char oldChar, char newChar) {
		return operCmd.replace(oldChar, newChar);
	}

	public String replace(CharSequence target, CharSequence replacement) {
		return operCmd.replace(target, replacement);
	}

	public String replaceAll(String regex, String replacement) {
		return operCmd.replaceAll(regex, replacement);
	}

	public String replaceFirst(String regex, String replacement) {
		return operCmd.replaceFirst(regex, replacement);
	}

	public String[] split(String regex, int limit) {
		return operCmd.split(regex, limit);
	}

	public String[] split(String regex) {
		return operCmd.split(regex);
	}

	public boolean startsWith(String prefix, int toffset) {
		return operCmd.startsWith(prefix, toffset);
	}

	public boolean startsWith(String prefix) {
		return operCmd.startsWith(prefix);
	}

	public CharSequence subSequence(int beginIndex, int endIndex) {
		return operCmd.subSequence(beginIndex, endIndex);
	}

	public String substring(int beginIndex, int endIndex) {
		return operCmd.substring(beginIndex, endIndex);
	}

	public String substring(int beginIndex) {
		return operCmd.substring(beginIndex);
	}

	public char[] toCharArray() {
		return operCmd.toCharArray();
	}

	public String toLowerCase() {
		return operCmd.toLowerCase();
	}

	public String toLowerCase(Locale locale) {
		return operCmd.toLowerCase(locale);
	}

	public String toString() {
		return operCmd.toString();
	}

	public String toUpperCase() {
		return operCmd.toUpperCase();
	}

	public String toUpperCase(Locale locale) {
		return operCmd.toUpperCase(locale);
	}

	public String trim() {
		return operCmd.trim();
	}

	public String getOperCmd() {
		return operCmd;
	}

	public void setOperCmd(String operCmd) {
		this.operCmd = operCmd;
	}

	public String getRepId() {
		return repId;
	}

	public void setRepId(String repId) {
		this.repId = repId;
	}

	public FeedBack getFeedBack() {
		return feedBack;
	}

	public void setFeedBack(FeedBack feedBack) {
		this.feedBack = feedBack;
	}

	public LicesneMgrDto getDto() {
		return dto;
	}

	public void setDto(LicesneMgrDto dto) {
		this.dto = dto;
	}

}
