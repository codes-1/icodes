package cn.com.codes.testLibrary.blh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.com.codes.common.dto.PageModel;
import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.common.JsonUtil;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.object.TestCaseLibrary;
import cn.com.codes.object.TestCaseLibraryDetail;
import cn.com.codes.testLibrary.dto.TestLibraryDto;
import cn.com.codes.testLibrary.service.TestLibraryService;

public class TestLibraryBlh extends BusinessBlh {

	private TestLibraryService testLibraryService;
	

	public View loadTree(BusiRequestEvent req){
		return super.getView();
	}

	public View caseLook(BusiRequestEvent req){
		return super.getView();
	}
	

	public View caseExamine(BusiRequestEvent req){
		return super.getView();
	}
	

	public View loadTreeList(BusiRequestEvent req){
		TestLibraryDto dto = super.getDto(TestLibraryDto.class, req);
		//获得用例库列表
		List<TestCaseLibrary> testCaseLibraries  = testLibraryService.getAllTestLibrary(dto);
		//形成树结构
		List<TestCaseLibrary> libraryTreeVos = this.toTreeJson(testCaseLibraries);
		super.writeResult(JsonUtil.toJson(libraryTreeVos));
		return super.globalAjax();
	}
	

	public View loadTreeList1(BusiRequestEvent req){
		TestLibraryDto dto = super.getDto(TestLibraryDto.class, req);
		//获得用例库列表
		List<TestCaseLibrary> testCaseLibraries  = testLibraryService.getAllTestLibrary(dto);
		//形成树结构
		List<TestCaseLibrary> libraryTreeVos = this.toTreeJson(testCaseLibraries);
		//构造一个总的根节点
		List<TestCaseLibrary> testCaseLibraries2 = new ArrayList<TestCaseLibrary>();
		TestCaseLibrary testCaseLibrary = new TestCaseLibrary();
		testCaseLibrary.setLibraryId("");
		testCaseLibrary.setTestcaseType("所有类别");
		testCaseLibrary.setLibraryCode("");
		testCaseLibrary.setChildren(libraryTreeVos);
		testCaseLibraries2.add(testCaseLibrary);
		super.writeResult(JsonUtil.toJson(testCaseLibraries2));
		return super.globalAjax();
	}
	
	

	public View add(BusiRequestEvent req){
		TestLibraryDto dto = super.getDto(TestLibraryDto.class, req);
		String flag = testLibraryService.addTestLibrary(dto);
		if(flag.equals("existed")){
			super.writeResult("existed");
		}else{
			super.writeResult("success");
		}
		return super.globalAjax();
	}
	

	public View update(BusiRequestEvent req){
		TestLibraryDto dto = super.getDto(TestLibraryDto.class, req);
		String flag = testLibraryService.updateTestLibrary(dto);
		if(flag.equals("existed")){
			super.writeResult("existed");
		}else{
			super.writeResult("success");
		}
		return super.globalAjax();
	}
	

	public View delete(BusiRequestEvent req){
		TestLibraryDto dto = super.getDto(TestLibraryDto.class, req);
		testLibraryService.deleteTestLibrary(dto);
		super.writeResult("success");
		return super.globalAjax();
	}
	

	public View saveTestLibraryDetails(BusiRequestEvent req){
		TestLibraryDto dto = super.getDto(TestLibraryDto.class, req);
		testLibraryService.saveTestLibraryDetails(dto);
		super.writeResult("success");
		return super.globalAjax();
	}
	

	public View deleteTestLibraryDetail(BusiRequestEvent req){
		TestLibraryDto dto = super.getDto(TestLibraryDto.class, req);
		testLibraryService.deleteTestLibraryDetail(dto);
		super.writeResult("success");
		return super.globalAjax();
	}
	

	public View updateTestLibraryDetailStatus(BusiRequestEvent req){
		TestLibraryDto dto = super.getDto(TestLibraryDto.class, req);
		testLibraryService.updateTestLibraryDetailStatus(dto);
		super.writeResult("success");
		return super.globalAjax();
	}
	

	@SuppressWarnings("unchecked")
	public View testCaseDetailLoad(BusiRequestEvent req){
		TestLibraryDto dto = super.getDto(TestLibraryDto.class, req);
		//构造hql查询语句
		this.buildTestCaseDetailListHql(dto);
		List<TestCaseLibraryDetail> testCaseLibraryDetails  = testLibraryService.findByHqlWithValuesMap(dto);
		PageModel pg = new PageModel();
		pg.setRows(testCaseLibraryDetails);
		pg.setTotal(dto.getTotal());
		super.writeResult(JsonUtil.toJson(pg));
		return super.globalAjax();
	}
	

	public View getLibraryTypeName(BusiRequestEvent req){
		TestLibraryDto dto = super.getDto(TestLibraryDto.class, req);
		String hql = "from TestCaseLibrary tcl where tcl.libraryId=?";
		@SuppressWarnings("unchecked")
		List<TestCaseLibrary> testCaseLibraries = testLibraryService.findByHql(hql, dto.getTestCaseLibrary().getLibraryId());
		if(testCaseLibraries != null && testCaseLibraries.size() == 1){
			super.writeResult(testCaseLibraries.get(0).getTestcaseType());
		}else{
			super.writeResult("failed");
		}
		return super.globalAjax();
	}

	private List<TestCaseLibrary> toTreeJson(List<TestCaseLibrary> list) {
		// 存储树结构权限
		List<TestCaseLibrary> roots = new ArrayList<TestCaseLibrary>();
		
		Map<String, TestCaseLibrary> systemMap = new HashMap<String, TestCaseLibrary>();
		
		if (null != list&&list.size() > 0) {
			// 获取根节点权限数据
			for (TestCaseLibrary testCaseLibrary : list) {
				if (testCaseLibrary.getParentId() == null || testCaseLibrary.getParentId().equals("")) {
					roots.add(testCaseLibrary);
				} 
				systemMap.put(testCaseLibrary.getLibraryId(), testCaseLibrary);
			}
		}
		
		
		// 构造树结构菜单数据
		Iterator<String> keys = systemMap.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			TestCaseLibrary testCaseLibrary = systemMap.get(key);
			if (testCaseLibrary.getParentId() != null && !testCaseLibrary.getParentId().equals("")) {
				TestCaseLibrary parent = systemMap.get(testCaseLibrary.getParentId());
				if (null != parent) {
					parent.getChildren().add(testCaseLibrary);
				}
			}
		}
		return roots;
	}


	public View addTestLibraryDetailsToCaseInfo(BusiRequestEvent req){
		TestLibraryDto dto = super.getDto(TestLibraryDto.class, req);
		testLibraryService.addTestLibraryDetailsToCaseInfo(dto);
		super.writeResult("success");
		return super.globalAjax();
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void buildTestCaseDetailListHql(TestLibraryDto dto){
		StringBuffer hql = new StringBuffer();
		Map praValMap = new HashMap();
		hql.append("from TestCaseLibraryDetail tcl where 1=1 ");
		if(dto.getTestCaseLibraryDetail() != null){
			if(!StringUtils.isNullOrEmpty(dto.getTestCaseLibraryDetail().getLibraryId())){
				hql.append(" and tcl.libraryId = :libraryId ");
				praValMap.put("libraryId", dto.getTestCaseLibraryDetail().getLibraryId());
			}
			if(!StringUtils.isNullOrEmpty(dto.getTestCaseLibraryDetail().getExamineStatus())){
				hql.append(" and tcl.examineStatus = :examineStatus");
				praValMap.put("examineStatus", dto.getTestCaseLibraryDetail().getExamineStatus());
			}
			if(!StringUtils.isNullOrEmpty(dto.getTestCaseLibraryDetail().getLibraryCode())){
				hql.append(" and tcl.libraryCode like :libraryCode");
				praValMap.put("libraryCode", "%"+dto.getTestCaseLibraryDetail().getLibraryCode()+"%");
			}
		}
		hql.append(" order by tcl.creatdate desc ");
		dto.setHql(hql.toString());
		dto.setHqlParamMaps(praValMap);
	}
	
	public TestLibraryService getTestLibraryService() {
		return testLibraryService;
	}

	public void setTestLibraryService(TestLibraryService testLibraryService) {
		this.testLibraryService = testLibraryService;
	}
	
	
}
