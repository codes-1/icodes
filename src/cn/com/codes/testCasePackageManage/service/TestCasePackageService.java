/**
 * 
 */
package cn.com.codes.testCasePackageManage.service;

import java.util.List;

import cn.com.codes.framework.app.services.BaseService;
import cn.com.codes.object.TestCasePackage;
import cn.com.codes.object.UserTestCasePkg;
import cn.com.codes.overview.dto.DataVo;
import cn.com.codes.testCasePackageManage.dto.TestCasePackageDto;


public interface TestCasePackageService extends BaseService{
	
//	public List<TestCasePackage> loadTestCasePackageList(TestCasePackageDto testCasePackageDto);
	
	public void saveTestCasePackage(TestCasePackageDto testCasePackageDto);
	
	public void updateTestCasePackage(TestCasePackageDto testCasePackageDto);
	
	public void deleteTestCasePkgById(String packageId);
	
	public String[] getUserIdsByPackageId(TestCasePackageDto testCasePackageDto);
	
	public String[] getTestCaseIdsByPackageId(TestCasePackageDto testCasePackageDto);
	
	public List<TestCasePackageDto> getSelTestCasesByPkgId(TestCasePackageDto testCasePackageDto);
	
	public void saveTestCase_CasePkg(TestCasePackageDto testCasePackageDto);
	
	public boolean reNameChkInTask(String objName,final String nameVal,final String namePropName,String idPropName,final String idPropVal,final String taskId);	

	public DataVo getBugStaticsByPkgId(TestCasePackageDto testCasePackageDto);
}
