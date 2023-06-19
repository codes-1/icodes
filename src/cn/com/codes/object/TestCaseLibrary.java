package cn.com.codes.object;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.com.codes.framework.transmission.JsonInterface;


public class TestCaseLibrary implements JsonInterface {

	/**  
	* 字段:      字段名称
	* @Fields serialVersionUID : TODO 
	*/
	private static final long serialVersionUID = 1L;
	private String libraryId;
	private String testcaseType;
	private String libraryCode;
	private String parentId;
	private String createUserId;
	private Date createTime;
	private Date updateTime;
	private List<TestCaseLibrary> children = new ArrayList<TestCaseLibrary>();
   
	public TestCaseLibrary() {
	}
	
	
	


	

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







	public String getLibraryCode() {
		return libraryCode;
	}







	public void setLibraryCode(String libraryCode) {
		this.libraryCode = libraryCode;
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







	public List<TestCaseLibrary> getChildren() {
		return children;
	}







	public void setChildren(List<TestCaseLibrary> children) {
		this.children = children;
	}







	@Override
	public String toStrUpdateInit() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String toStrList() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String toStrUpdateRest() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void toString(StringBuffer bf) {
		// TODO Auto-generated method stub
		
	}
}