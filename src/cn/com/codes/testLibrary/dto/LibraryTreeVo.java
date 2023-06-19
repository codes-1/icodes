package cn.com.codes.testLibrary.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LibraryTreeVo {
	private String libraryId;
	private String testcaseType;
	private String parentId;
	private String createUserId;
	private Date createTime;
	private Date updateTime;
	private List<LibraryTreeVo> children = new ArrayList<LibraryTreeVo>();
   
	public String getLibraryId() {
		return libraryId;
	}







	public void setLibraryId(String libraryId) {
		this.libraryId = libraryId;
	}







	public String getTestcaseType() {
		return testcaseType;
	}







	public void setTestcaseType(String testcaseType) {
		this.testcaseType = testcaseType;
	}







	public String getParentId() {
		return parentId;
	}







	public void setParentId(String parentId) {
		this.parentId = parentId;
	}







	public String getCreateUserId() {
		return createUserId;
	}







	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}







	public Date getCreateTime() {
		return createTime;
	}







	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}







	public Date getUpdateTime() {
		return updateTime;
	}







	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}







	public List<LibraryTreeVo> getChildren() {
		return children;
	}







	public void setChildren(List<LibraryTreeVo> children) {
		this.children = children;
	}

}