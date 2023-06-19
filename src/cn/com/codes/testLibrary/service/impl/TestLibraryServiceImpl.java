package cn.com.codes.testLibrary.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.app.services.BaseServiceImpl;
import cn.com.codes.object.TestCaseInfo;
import cn.com.codes.object.TestCaseLibrary;
import cn.com.codes.object.TestCaseLibraryDetail;
import cn.com.codes.testLibrary.dto.TestLibraryDto;
import cn.com.codes.testLibrary.service.TestLibraryService;

public class TestLibraryServiceImpl extends BaseServiceImpl implements TestLibraryService {
	
	@SuppressWarnings("unchecked")
	public List<TestCaseLibrary> getAllTestLibrary(TestLibraryDto testLibraryDto){
		String hql = "from TestCaseLibrary tc where 1=1 ";
		testLibraryDto.setHql(hql.toString());
		testLibraryDto.setHqlParamMaps(null);
		testLibraryDto.setPageNo(1);
		testLibraryDto.setPageSize(1000);
		List<TestCaseLibrary> testCaseLibraries = this.findByHqlWithValuesMap(testLibraryDto);
		if(testCaseLibraries != null && testCaseLibraries.size() > 0){
			return testCaseLibraries;
		}
		return null;
	}

	public String addTestLibrary(TestLibraryDto testLibraryDto){
		//判断该类型是否已存在
		String hql = "from TestCaseLibrary tcl where tcl.testcaseType=?";
		@SuppressWarnings("unchecked")
		List<TestCaseLibrary> testCaseLibraries = this.findByHql(hql, testLibraryDto.getTestCaseLibrary().getTestcaseType());
		if(testCaseLibraries != null && testCaseLibraries.size() > 0){
			return "existed";
		}
		//保存t_testcase_library表
		if(StringUtils.isNullOrEmpty(testLibraryDto.getTestCaseLibrary().getLibraryCode())){
			//随机生成一个2位的code
			String libraryCode = getRandomString(2);
			testLibraryDto.getTestCaseLibrary().setLibraryCode(libraryCode);
		}
		testLibraryDto.getTestCaseLibrary().setCreateTime(new Date());
		this.add(testLibraryDto.getTestCaseLibrary());
		return "success";
	}
	
	public String updateTestLibrary(TestLibraryDto testLibraryDto){
		//判断该类型是否已存在
		String hql = "from TestCaseLibrary tcl where tcl.testcaseType=?";
		@SuppressWarnings("unchecked")
		List<TestCaseLibrary> testCaseLibraries = this.findByHql(hql, testLibraryDto.getTestCaseLibrary().getTestcaseType());
		if(testCaseLibraries != null && testCaseLibraries.size() == 1 && !testCaseLibraries.get(0).getLibraryId().equals(testLibraryDto.getTestCaseLibrary().getLibraryId())){
			return "existed";
		}else if(testCaseLibraries != null && testCaseLibraries.size() == 1 && testCaseLibraries.get(0).getLibraryId().equals(testLibraryDto.getTestCaseLibrary().getLibraryId())){
			testCaseLibraries.get(0).setUpdateTime(new Date());
			this.update(testCaseLibraries.get(0));
			return "success";
		}
		//更新t_testcase_library表
		testLibraryDto.getTestCaseLibrary().setUpdateTime(new Date());
		this.update(testLibraryDto.getTestCaseLibrary());
		return "success";
	}
	
	public void deleteTestLibrary(TestLibraryDto testLibraryDto){
		//删除t_testcase_library表，如果是父节点，还要删除下面的子节点
		this.executeUpdateByHql("delete from TestCaseLibrary where libraryCode like ? ", new Object[]{"%"+testLibraryDto.getTestCaseLibrary().getLibraryCode()+"%"});
		//删除t_testcase_library_detail表
		this.executeUpdateByHql("delete from TestCaseLibraryDetail where libraryCode like ? ", new Object[]{"%"+testLibraryDto.getTestCaseLibrary().getLibraryCode()+"%"});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void saveTestLibraryDetails(TestLibraryDto testLibraryDto){
			List<Long> testCaseIds = new ArrayList<Long>();
			for(int i=0;i<testLibraryDto.getCaseIdS().split(",").length;i++){
				testCaseIds.add(Long.parseLong(testLibraryDto.getCaseIdS().split(",")[i]));
			}
			Map praValMap = new HashMap();
			String hql1 = "from TestCaseInfo tci where tci.testCaseId in (:ids) ";
			praValMap.put("ids",testCaseIds);
			testLibraryDto.setHql(hql1.toString());
			testLibraryDto.setHqlParamMaps(praValMap);
			testLibraryDto.setPageNo(1);
			testLibraryDto.setPageSize(300);
			List<TestCaseInfo> testCaseInfos = this.findByHqlWithValuesMap(testLibraryDto);
			if(testCaseInfos != null && testCaseInfos.size() > 0){
				//将查出的测试用例的数据保存到另一张表
				List<TestCaseLibraryDetail> testCaseLibraryDetails = new ArrayList<TestCaseLibraryDetail>();
				for(int t=0;t<testCaseInfos.size();t++){
					//判断该用例是否已推荐到用例库，已推荐的话，跳过此次循环
					String hql = "from TestCaseLibraryDetail tcl where tcl.number=?";
					List<TestCaseLibraryDetail> testCaseLibraryDetails2 = this.findByHql(hql, testCaseInfos.get(t).getTestCaseId());
					if(testCaseLibraryDetails2 != null && testCaseLibraryDetails2.size() > 0){
						continue;
					}
					TestCaseLibraryDetail testCaseLibraryDetail = new TestCaseLibraryDetail();
					testCaseLibraryDetail.setTaskId(null);
					testCaseLibraryDetail.setModuleId(testCaseInfos.get(t).getModuleId());
					testCaseLibraryDetail.setCreaterId(testCaseInfos.get(t).getCreaterId());
					testCaseLibraryDetail.setPrefixCondition(testCaseInfos.get(t).getPrefixCondition());
					testCaseLibraryDetail.setTestCaseDes(testCaseInfos.get(t).getTestCaseDes());
					testCaseLibraryDetail.setTestData(testCaseInfos.get(t).getTestData());
					testCaseLibraryDetail.setOperDataRichText(testCaseInfos.get(t).getOperDataRichText());
					testCaseLibraryDetail.setExpResult(testCaseInfos.get(t).getExpResult());
					testCaseLibraryDetail.setIsReleased(testCaseInfos.get(t).getIsReleased());
					testCaseLibraryDetail.setCreatdate(testCaseInfos.get(t).getCreatdate());
					testCaseLibraryDetail.setAttachUrl(testCaseInfos.get(t).getAttachUrl());
					testCaseLibraryDetail.setRemark(testCaseInfos.get(t).getRemark());
					testCaseLibraryDetail.setLibraryId(testLibraryDto.getLibraryId());
					testCaseLibraryDetail.setLibraryCode(testLibraryDto.getLibraryCode());
					testCaseLibraryDetail.setRecommendUserId(testLibraryDto.getRecommendUserId());
					testCaseLibraryDetail.setRecommendReason(testLibraryDto.getRecommendReason());
					testCaseLibraryDetail.setExamineStatus("0");
					testCaseLibraryDetail.setNumber(testCaseInfos.get(t).getTestCaseId());
					testCaseLibraryDetails.add(testCaseLibraryDetail);
				}
				this.batchSaveOrUpdate(testCaseLibraryDetails);
			}
	}
	
	public void deleteTestLibraryDetail(TestLibraryDto testLibraryDto){
		List<String> testCaseIds = new ArrayList<String>();
		for(int i=0;i<testLibraryDto.getTestCaseIds().split(",").length;i++){
			testCaseIds.add(testLibraryDto.getTestCaseIds().split(",")[i]);
		}
		//删除t_testcase_library_detail表
		for(int t=0;t<testCaseIds.size();t++){
			this.executeUpdateByHql("delete from TestCaseLibraryDetail where testCaseId = ? ", new Object[]{testCaseIds.get(t)});
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void updateTestLibraryDetailStatus(TestLibraryDto testLibraryDto){
		List<String> testCaseIds = new ArrayList<String>();
		for(int i=0;i<testLibraryDto.getTestCaseIds().split(",").length;i++){
			testCaseIds.add(testLibraryDto.getTestCaseIds().split(",")[i]);
		}
		Map praValMap = new HashMap();
		String hql1 = "from TestCaseLibraryDetail tcl where tcl.testCaseId in (:testCaseIds) ";
		praValMap.put("testCaseIds",testCaseIds);
		testLibraryDto.setHql(hql1.toString());
		testLibraryDto.setHqlParamMaps(praValMap);
		testLibraryDto.setPageNo(1);
		testLibraryDto.setPageSize(800);
		List<TestCaseLibraryDetail> testCaseLibraryDetails = this.findByHqlWithValuesMap(testLibraryDto);
		if(testCaseLibraryDetails != null && testCaseLibraryDetails.size() > 0){
			for(int t=0;t<testCaseLibraryDetails.size();t++){
				testCaseLibraryDetails.get(t).setExamineStatus("1");
				testCaseLibraryDetails.get(t).setExamineUserId(testLibraryDto.getTestCaseLibraryDetail().getExamineUserId());
			}
			this.batchSaveOrUpdate(testCaseLibraryDetails);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addTestLibraryDetailsToCaseInfo(TestLibraryDto testLibraryDto){
		List<Long> testCaseIds = new ArrayList<Long>();
		for(int i=0;i<testLibraryDto.getNumbers().split(",").length;i++){
			testCaseIds.add(Long.parseLong(testLibraryDto.getNumbers().split(",")[i]));
		}
		Map praValMap = new HashMap();
		String hql1 = "from TestCaseInfo tci where tci.testCaseId in (:ids) ";
		praValMap.put("ids",testCaseIds);
		testLibraryDto.setHql(hql1.toString());
		testLibraryDto.setHqlParamMaps(praValMap);
		testLibraryDto.setPageNo(1);
		testLibraryDto.setPageSize(300);
		List<TestCaseInfo> testCaseInfos = this.findByHqlWithValuesMap(testLibraryDto);
		if(testCaseInfos != null && testCaseInfos.size() > 0){
			//将查出的测试用例的数据保存同一张表，只是不同项目，节点，导入者
			List<TestCaseInfo> testCaseInfos2 = new ArrayList<TestCaseInfo>();
			for(int t=0;t<testCaseInfos.size();t++){
				TestCaseInfo testCaseInfo = new TestCaseInfo();
				testCaseInfo.setTestCaseId(null);
				testCaseInfo.setTaskId(testLibraryDto.getTaskId());
				testCaseInfo.setModuleId(Long.parseLong(testLibraryDto.getModuleId()));
				testCaseInfo.setModuleNum(testLibraryDto.getModuleNum());
				testCaseInfo.setCreaterId(testLibraryDto.getCreaterId());
				testCaseInfo.setTestStatus(testCaseInfos.get(t).getTestStatus());
				testCaseInfo.setCaseTypeId(testCaseInfos.get(t).getCaseTypeId());
				testCaseInfo.setWeight(testCaseInfos.get(t).getWeight());
				testCaseInfo.setCasePri(testCaseInfos.get(t).getCasePri());
				testCaseInfo.setPriId(testCaseInfos.get(t).getPriId());
				testCaseInfo.setRemark(testCaseInfos.get(t).getRemark());
				testCaseInfo.setAuditId(testCaseInfos.get(t).getAuditId());
				testCaseInfo.setPrefixCondition(testCaseInfos.get(t).getPrefixCondition());
				testCaseInfo.setTestCaseDes(testCaseInfos.get(t).getTestCaseDes());
				testCaseInfo.setTestData(testCaseInfos.get(t).getTestData());
				testCaseInfo.setOperDataRichText(testCaseInfos.get(t).getOperDataRichText());
				testCaseInfo.setExpResult(testCaseInfos.get(t).getExpResult());
				testCaseInfo.setIsReleased(testCaseInfos.get(t).getIsReleased());
				testCaseInfo.setCreatdate(new Date());
				testCaseInfo.setAttachUrl(testCaseInfos.get(t).getAttachUrl());
				
				testCaseInfos2.add(testCaseInfo);
			}
			this.batchSaveOrUpdate(testCaseInfos2);
		}
	}
	

	private static String getRandomString(int length){
	    //定义一个字符串（A-Z，a-z）即52位；
	    String str="zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM";
	    //由Random生成随机数
        Random random=new Random();  
        StringBuffer sb=new StringBuffer();
        //长度为几就循环几次
        for(int i=0; i<length; ++i){
          //产生0-51的数字
          int number=random.nextInt(52);
          //将产生的数字通过length次承载到sb中
          sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
	  }
}
