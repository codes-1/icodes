package cn.com.codes.testLibrary.web;

import cn.com.codes.framework.app.blh.BaseBizLogicHandler;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;
import cn.com.codes.testLibrary.blh.TestLibraryBlh;
import cn.com.codes.testLibrary.dto.TestLibraryDto;

public class TestLibraryAction extends BaseAction<TestLibraryBlh> {

	/**  
	* 字段:      字段名称
	* @Fields serialVersionUID : TODO 
	*/
	private static final long serialVersionUID = 1L;
	private TestLibraryDto dto = new TestLibraryDto();
	private TestLibraryBlh testLibraryBlh;
	protected void _prepareRequest(BusiRequestEvent reqEvent)
			throws BaseException {
		reqEvent.setDto(dto);

	}
	public TestLibraryDto getDto() {
		return dto;
	}
	public void setDto(TestLibraryDto dto) {
		this.dto = dto;
	}
	public  BaseBizLogicHandler getBlh(){
		return testLibraryBlh;
	}
	public TestLibraryBlh getTestLibraryBlh() {
		return testLibraryBlh;
	}
	public void setTestLibraryBlh(TestLibraryBlh testLibraryBlh) {
		this.testLibraryBlh = testLibraryBlh;
	}
	
}
