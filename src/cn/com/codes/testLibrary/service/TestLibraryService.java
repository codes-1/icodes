package cn.com.codes.testLibrary.service;

import java.util.List;

import cn.com.codes.framework.app.services.BaseService;
import cn.com.codes.object.TestCaseLibrary;
import cn.com.codes.testLibrary.dto.TestLibraryDto;

public interface TestLibraryService extends BaseService {
	
	public List<TestCaseLibrary> getAllTestLibrary(TestLibraryDto testLibraryDto);

	public String addTestLibrary(TestLibraryDto testLibraryDto);
	
	public String updateTestLibrary(TestLibraryDto testLibraryDto);
	
	public void deleteTestLibrary(TestLibraryDto testLibraryDto);
	
	public void saveTestLibraryDetails(TestLibraryDto testLibraryDto);
	
	public void deleteTestLibraryDetail(TestLibraryDto testLibraryDto);
	
	public void updateTestLibraryDetailStatus(TestLibraryDto testLibraryDto);
	
	public void addTestLibraryDetailsToCaseInfo(TestLibraryDto testLibraryDto);
}
