/**
 * 
 */
package cn.com.codes.testCasePackageManage.web;

import java.util.HashMap;

import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;
import cn.com.codes.testCasePackageManage.blh.TestCasePackageBlh;
import cn.com.codes.testCasePackageManage.dto.TestCasePackageDto;


public class TestCasePackageAction extends BaseAction<TestCasePackageBlh>{
	public static final long serialVersionUID = 1l;
	private TestCasePackageBlh testCasePackageBlh;
	private TestCasePackageDto dto = new TestCasePackageDto();
	
	protected void _prepareRequest(BusiRequestEvent reqEvent)throws BaseException {
		reqEvent.setDto(dto);
	}

	protected String _processResponse() throws BaseException {
		HashMap<?, ?> displayData = (HashMap<?, ?>) _getDisplayData();
		return super.forwardPage(displayData);
	}


	/**
	 * @return the testCasePackageBlh
	 */
	public TestCasePackageBlh getTestCasePackageBlh() {
		return testCasePackageBlh;
	}

	/**
	 * @param testCasePackageBlh the testCasePackageBlh to set
	 */
	public void setTestCasePackageBlh(TestCasePackageBlh testCasePackageBlh) {
		this.testCasePackageBlh = testCasePackageBlh;
	}

	/**
	 * @return the dto
	 */
	public TestCasePackageDto getDto() {
		return dto;
	}

	/**
	 * @param dto the dto to set
	 */
	public void setDto(TestCasePackageDto dto) {
		this.dto = dto;
	}

	
	
}
