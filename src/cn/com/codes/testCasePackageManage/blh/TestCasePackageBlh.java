package cn.com.codes.testCasePackageManage.blh;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.dto.PageModel;
import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.common.JsonUtil;
import cn.com.codes.framework.common.ListObject;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.security.Visit;
import cn.com.codes.framework.security.VisitUser;
import cn.com.codes.framework.security.filter.SecurityContext;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.object.TestCaseInfo;
import cn.com.codes.object.TestCasePackage;
import cn.com.codes.object.TypeDefine;
import cn.com.codes.object.User;
import cn.com.codes.object.UserTestCasePkg;
import cn.com.codes.overview.dto.DataVo;
import cn.com.codes.testCasePackageManage.dto.TestCasePackageDto;
import cn.com.codes.testCasePackageManage.service.TestCasePackageService;


public class TestCasePackageBlh extends BusinessBlh {
	
	private static Logger log = Logger.getLogger(TestCasePackageBlh.class);
	private TestCasePackageService testCasePackageService;
	
	/**
	 * @return the testCasePackageService
	 */
	public TestCasePackageService getTestCasePackageService() {
		return testCasePackageService;
	}

	/**
	 * @param testCasePackageService the testCasePackageService to set
	 */
	public void setTestCasePackageService(
			TestCasePackageService testCasePackageService) {
		this.testCasePackageService = testCasePackageService;
	}
	
	public View goTestCasePkgMain(BusiRequestEvent req) throws BaseException{
		return super.getView();
	}
	
	@SuppressWarnings("unchecked")
	public View loadTestCasePackageList(BusiRequestEvent req) throws BaseException {
		TestCasePackageDto testCasePackageDto = (TestCasePackageDto) req.getDto();
		this.buildTestCasesPackageHql(testCasePackageDto);//拼接Hql
		List<TestCasePackage> casePackages = testCasePackageService.findByHqlWithValuesMap(testCasePackageDto);
		PageModel pageModel = new PageModel();
		pageModel.setRows(casePackages);
		pageModel.setTotal(testCasePackageDto.getTotal());
		super.writeResult(JsonUtil.toJson(pageModel));
		return super.globalAjax();
	}

	private void buildTestCasesPackageHql(TestCasePackageDto testCasePackageDto) {
		StringBuilder hqlStr = new StringBuilder();
//		String queryParam = new String();
		
		Map<String,Object> hashMap = new HashMap<String,Object>();
		hqlStr.append(" from TestCasePackage tcp where 1=1 ");
		if(null != testCasePackageDto.getTestCasePackage()){
			if(null != testCasePackageDto.getTestCasePackage().getTaskId()){
				hqlStr.append(" and tcp.taskId = :taskId");
				hashMap.put("taskId",  testCasePackageDto.getTestCasePackage().getTaskId());
			}
		}
	
		
	    if(null != testCasePackageDto.getQueryParam()){
    	  hqlStr.append(" and tcp.packageName like :packageName");
    	  hashMap.put("packageName",  "%" + testCasePackageDto.getQueryParam() +"%");
        }
	    
	    if(!StringUtils.isNullOrEmpty(testCasePackageDto.getPackageIdJoin())){
			List<String> packageIdJs = new ArrayList<String>();
			String[] packageId = testCasePackageDto.getPackageIdJoin().split(" ");
			for(int i=0;i<packageId.length;i++){
				packageIdJs.add(packageId[i]);
			}
			
			hqlStr.append(" and tcp.packageId not in (:packageIds) ");
			hashMap.put("packageIds", packageIdJs);
		}
	    
		hqlStr.append(" order by  tcp.updateTime desc ");
	    testCasePackageDto.setHql(hqlStr.toString());
		if(log.isInfoEnabled()){
			log.info(hqlStr.toString());
		}
	
		testCasePackageDto.setHqlParamMaps(hashMap);
	}

	public View selTestCase(BusiRequestEvent req) throws BaseException{
		return super.getView();
	}
	
	public View viewTestCase(BusiRequestEvent req) throws BaseException{
		return super.getView();
	}
	
	public View executeTestCase(BusiRequestEvent req) throws BaseException{
		return super.getView();
	}
	
	public View viewTestCaseResult(BusiRequestEvent req) throws BaseException{
		return super.getView();
	}
	
	public View saveTestCasePackage(BusiRequestEvent req) throws BaseException{
		TestCasePackageDto testCasePackageDto = (TestCasePackageDto) req.getDto();
		Date nowDate = new Date();
		testCasePackageDto.getTestCasePackage().setCreateTime(nowDate);
		testCasePackageDto.getTestCasePackage().setUpdateTime(nowDate);
		SecurityContext sc = SecurityContextHolder.getContext();
		Visit visit = sc.getVisit();
		VisitUser user =  null;
		if(visit!=null){
			user = visit.getUserInfo(VisitUser.class);
			testCasePackageDto.getTestCasePackage().setCreaterId(user.getId());
		}
		
		String taskId = testCasePackageDto.getTestCasePackage().getTaskId();
		if(taskId==null){
			taskId = SecurityContextHolderHelp.getCurrTaksId();
			 testCasePackageDto.getTestCasePackage().setTaskId(taskId);
		}
		boolean chkRest = testCasePackageService.reNameChkInTask("TestCasePackage", testCasePackageDto.getTestCasePackage().getPackageName(), "packageName",null,null, testCasePackageDto.getTestCasePackage().getTaskId());
		if(chkRest){
			super.writeResult("reName");
			return super.globalAjax();
		}
		testCasePackageService.saveTestCasePackage(testCasePackageDto); 
		
		super.writeResult("success");
		return super.globalAjax();
	}
	
	public View updateTestCasePackage(BusiRequestEvent req) throws BaseException{
		TestCasePackageDto testCasePackageDto = (TestCasePackageDto) req.getDto();
		Date nowDate = new Date();
		testCasePackageDto.getTestCasePackage().setUpdateTime(nowDate);;
		String taskId = testCasePackageDto.getTestCasePackage().getTaskId();
		if(taskId==null){
			taskId = SecurityContextHolderHelp.getCurrTaksId();
			 testCasePackageDto.getTestCasePackage().setTaskId(taskId);
		}
		boolean chkRest = testCasePackageService.reNameChkInTask("TestCasePackage",  testCasePackageDto.getTestCasePackage().getPackageName(), "packageName", "packageId", testCasePackageDto.getTestCasePackage().getPackageId(),testCasePackageDto.getTestCasePackage().getTaskId());
		if(chkRest){
			super.writeResult("reName");
			return super.globalAjax();
		}
		testCasePackageService.updateTestCasePackage(testCasePackageDto); 
		super.writeResult("success");
		return super.globalAjax();
	}
	
	public View deleteTestCasePkgById(BusiRequestEvent req) throws BaseException{
		TestCasePackageDto testCasePackageDto = (TestCasePackageDto) req.getDto();
		  if(null != testCasePackageDto.getTestCasePackage()){
	    	   if(null != testCasePackageDto.getTestCasePackage().getPackageId()){
	    		   testCasePackageService.deleteTestCasePkgById(testCasePackageDto.getTestCasePackage().getPackageId()); 
	    	   }
		  }
		super.writeResult("success");
		return super.globalAjax();
	}
	
	
	public View getUserIdsByPackageId(BusiRequestEvent req) throws BaseException{
		TestCasePackageDto testCasePackageDto = (TestCasePackageDto) req.getDto();
		String[] userIds = testCasePackageService.getUserIdsByPackageId(testCasePackageDto); 
		super.writeResult(JsonUtil.toJson(userIds));
		return super.globalAjax();
	}
	
	public View getSelTestCasesByPkgId(BusiRequestEvent req) throws BaseException{
		TestCasePackageDto testCasePackageDto = (TestCasePackageDto) req.getDto();
		List<TestCasePackageDto> lists = testCasePackageService.getSelTestCasesByPkgId(testCasePackageDto); 
		if(lists.size()!=0){
			this.setRelaUser(lists);
			this.setRelaTaskName(lists);
			this.setRelaType(lists);
		}
		super.writeResult(JsonUtil.toJson(lists));
		return super.globalAjax();
	}
	
	private void setRelaType(List<TestCasePackageDto> lists){
		 Map<String,TypeDefine> typeMap= testCasePackageService.getRelaTypeDefine(lists, "priId","caseTypeId");
		 if(typeMap.isEmpty()){
			 return;
		 }
		 for(TestCasePackageDto obj:lists ){
			 if(typeMap.get(obj.getCaseTypeId().toString())!=null){
				 obj.setTypeName(typeMap.get(obj.getCaseTypeId().toString()).getTypeName());
			 }
			 if(typeMap.get(obj.getPriId().toString())!=null){
				 obj.setPriName(typeMap.get(obj.getPriId().toString()).getTypeName());
			 }
		 }
		 typeMap = null;
	}
	private void setRelaUser(List<TestCasePackageDto> lists){
		Map<String,User> userMap= testCasePackageService.getRelaUserWithName(lists, "createrId","auditId");
		User own = null;
		for(TestCasePackageDto obj :lists){
			own = userMap.get(obj.getCreaterId());
			if(own!=null)
				obj.setAuthorName(own.getUniqueName());
			if(obj.getAuditId()!=null){
				own = userMap.get(obj.getAuditId());
				if(own==null){
					continue;
				}
				obj.setAuditerNmae(own.getUniqueName());					
			}
			own = null;
		}
		own= null;
		userMap = null;
	}
	
	private void setRelaTaskName(List<TestCasePackageDto> lists){
		if(lists==null||lists.isEmpty()){
			return;
		}
		Map<String,ListObject> taskMap= testCasePackageService.getRelaTestTasks(lists, "taskId");
		ListObject lstObj = null;
		for(TestCasePackageDto obj :lists){
			lstObj = taskMap.get(obj.getTaskId());
			if(lstObj==null){
				continue;
			}
			obj.setTaskName(lstObj.getValueObj());
		}
		lstObj= null;
		taskMap = null;
	}
	
	public View saveTestCase_CasePkg(BusiRequestEvent req) throws BaseException{
		TestCasePackageDto testCasePackageDto = (TestCasePackageDto) req.getDto();
		testCasePackageService.saveTestCase_CasePkg(testCasePackageDto); 
		super.writeResult("success");
		return super.globalAjax();
	}
	
	public View getBugStaticsByPkgId(BusiRequestEvent req) throws BaseException{
		TestCasePackageDto testCasePackageDto = (TestCasePackageDto) req.getDto();
		if(testCasePackageDto !=null && testCasePackageDto.getTestCasePackage() !=null && !StringUtils.isNullOrEmpty(testCasePackageDto.getTestCasePackage().getPackageId())){
			DataVo dataVo = testCasePackageService.getBugStaticsByPkgId(testCasePackageDto); 
			super.writeResult(JsonUtil.toJson(dataVo));
		}else{
			super.writeResult("操作失败，请稍后再试");
		}
	
		return super.globalAjax();
	}
}
