package cn.com.codes.testCasePackageManage.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.aspectj.weaver.ast.Var;
import org.springframework.orm.hibernate3.HibernateCallback;

import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.app.services.BaseServiceImpl;
import cn.com.codes.framework.hibernate.HibernateGenericController;
import cn.com.codes.object.TestCasePackage;
import cn.com.codes.object.TestCase_CasePkg;
import cn.com.codes.object.UserTestCasePkg;
import cn.com.codes.overview.dto.DataVo;
import cn.com.codes.testCasePackageManage.dto.TestCasePackageDto;
import cn.com.codes.testCasePackageManage.service.TestCasePackageService;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.HibernateException;

public class TestCasePackageServiceImpl extends BaseServiceImpl implements TestCasePackageService {
	
	private static Logger loger = Logger.getLogger(TestCasePackageServiceImpl.class);
	private TestCasePackageService testCasePackageService;

	public void saveTestCasePackage(TestCasePackageDto testCasePackageDto){
		//保存t_testcasepackage表
		this.add(testCasePackageDto.getTestCasePackage());
		
		//保存t_user_testcasepkg表
		this.saveUserTestCasePkg(testCasePackageDto);
		
	}
	
	public void updateTestCasePackage(TestCasePackageDto testCasePackageDto){
		//更新t_testcasepackage表
		this.update(testCasePackageDto.getTestCasePackage());
		
		if(!StringUtils.isNullOrEmpty(testCasePackageDto.getTestCasePackage().getPackageId())){
			//更新t_user_testcasepkg表
			//先删除t_user_testcasepkg表原有数据
			this.deleteUserTestCasePkg(testCasePackageDto.getTestCasePackage().getPackageId());
			
		}
	
		this.saveUserTestCasePkg(testCasePackageDto);
	}
	
	public void deleteTestCasePkgById(String packageId){
	   this.delete(TestCasePackage.class, packageId);
	   //删除t_user_testcasepkg表
	   this.deleteUserTestCasePkg(packageId);
	   //删除t_testcase_casepkg表
	   this.deleteTestCase_CasePkg(packageId);
	}

	public String[] getUserIdsByPackageId(TestCasePackageDto testCasePackageDto){
		String hql = "from UserTestCasePkg  where packageId=?";
		@SuppressWarnings("unchecked")
		List<UserTestCasePkg> userTestCasePkgs = this.findByHql(hql, testCasePackageDto.getTestCasePackage().getPackageId());
		if(userTestCasePkgs != null && userTestCasePkgs.size() > 0){
			String[] userIds = new String[userTestCasePkgs.size()];
			for(int i=0;i<userTestCasePkgs.size();i++){
				userIds[i] = userTestCasePkgs.get(i).getUserId();
			}
			return userIds;
		}else{
			return null;
		}
	}
	
	public String[] getTestCaseIdsByPackageId(TestCasePackageDto testCasePackageDto){
		String hql = "from TestCase_CasePkg  where packageId=?";
		@SuppressWarnings("unchecked")
		List<TestCase_CasePkg> testCase_CasePkg = this.findByHql(hql, testCasePackageDto.getTestCasePackage().getPackageId());
		if(testCase_CasePkg != null && testCase_CasePkg.size() > 0){
			String[] testCaseIds = new String[testCase_CasePkg.size()];
			for(int i=0;i<testCase_CasePkg.size();i++){
				testCaseIds[i] = testCase_CasePkg.get(i).getTestCaseId();
			}
			return testCaseIds;
		}else{
			return null;
		}
	}
	
	private void saveUserTestCasePkg(TestCasePackageDto testCasePackageDto) {
		List<UserTestCasePkg> userTestCasePkgs = new ArrayList<UserTestCasePkg>();

		if(null !=testCasePackageDto.getSelectedUserIds()){
			String[] selectedIdList = testCasePackageDto.getSelectedUserIds().split(",");
			for(String selId:selectedIdList){
				UserTestCasePkg userTestCasePkg = new UserTestCasePkg();
				userTestCasePkg.setPackageId(testCasePackageDto.getTestCasePackage().getPackageId());
				userTestCasePkg.setUserId(selId);
				userTestCasePkgs.add(userTestCasePkg);
			}
		this.batchSaveOrUpdate(userTestCasePkgs);
	  }
	}

	  //删除t_user_testcasepkg表
	private void deleteUserTestCasePkg(String packageId) {
		this.executeUpdateByHql("delete from UserTestCasePkg where packageId =? ", new Object[]{packageId});
	}
	
	
	public void saveTestCase_CasePkg(TestCasePackageDto testCasePackageDto) {
		List<TestCase_CasePkg> testCase_CasePkgs = new ArrayList<TestCase_CasePkg>();
		//更新t_testcase_casepkg表
		//先删除t_testcase_casepkg表原有数据
		this.deleteTestCase_CasePkg(testCasePackageDto.getTestCasePackage().getPackageId());
		
		if(null !=testCasePackageDto.getSelectedTestCaseIds()){
			String[] selectedCaseIdList = testCasePackageDto.getSelectedTestCaseIds().split(",");
			for(String selCaseId:selectedCaseIdList){
				TestCase_CasePkg testCase_CasePkg = new TestCase_CasePkg();
				testCase_CasePkg.setPackageId(testCasePackageDto.getTestCasePackage().getPackageId());
				testCase_CasePkg.setTestCaseId(selCaseId);;
				testCase_CasePkgs.add(testCase_CasePkg);
			}
		this.batchSaveOrUpdate(testCase_CasePkgs);
	  }
	}
	
	 //删除t_testcase_casepkg表
	private void deleteTestCase_CasePkg(String packageId){
		this.executeUpdateByHql("delete from TestCase_CasePkg where packageId =? ", new Object[]{packageId});
	}
	
	public  List<TestCasePackageDto> getSelTestCasesByPkgId(TestCasePackageDto testCasePackageDto){
		StringBuilder hqlStr = new StringBuilder();
		hqlStr.append("SELECT new cn.com.codes.testCasePackageManage.dto.TestCasePackageDto( tc.packageId,  tt.taskId,   tt.moduleId, " + 
			 " tt.testCaseId,   tt.createrId,   tt.prefixCondition, tt.testCaseDes,   tt.isReleased,   tt.creatdate, " +
			 " tt.attachUrl,   tt.caseTypeId,   tt.priId,   tt.weight, tc.execStatus as testStatus, " +
			 " tt.moduleNum,  tc.executorId as auditId) FROM TestCase_CasePkg tc " + 
             ",TestCaseInfo tt where tc.testCaseId = tt.testCaseId ");
                      
	      if(null != testCasePackageDto.getTestCasePackage()){
			if(null != testCasePackageDto.getTestCasePackage().getPackageId()){
				hqlStr.append(" and tc.packageId = '" + testCasePackageDto.getTestCasePackage().getPackageId() + "'");
			}
		  }
	      
	     List<TestCasePackageDto> lists = this.findByHql(hqlStr.toString(), null);
	     return lists;
	}
	
	public boolean reNameChkInTask(String objName,final String nameVal,final String namePropName,String idPropName,final String idPropVal,final String taskId){
		
		StringBuffer hql = new StringBuffer();
		hql.append("select count(").append(idPropName==null?"*":idPropName).append(")");
		hql.append(" from ").append(objName);
		hql.append(" where ").append(namePropName).append("=? and taskId=?");
		if(idPropName!=null&&!"".equals(idPropName.trim())&&idPropVal!=null&&!"".equals(idPropVal.trim())){
			hql.append(" and  ").append(idPropName).append("!=?");
		}
		final String countHql= hql.toString();
		List countlist = (List)this.hibernateGenericController.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)throws HibernateException {
				Query queryObject = session.createQuery(countHql).setCacheable(true);
				queryObject.setParameter(0, nameVal);
				queryObject.setParameter(1, taskId);
					if(idPropVal!=null&&!"".equals(idPropVal.trim())) {
						queryObject.setParameter(2, idPropVal);
					}
				return queryObject.list();
			}
		}, true);
		int count = HibernateGenericController.toLong(countlist.get(0)).intValue();
		return count>0?true:false;
	}	
	
	public DataVo getBugStaticsByPkgId(TestCasePackageDto testCasePackageDto){
		DataVo dataVo = new DataVo();
		String sql = "SELECT * FROM ( ( SELECT count(*) AS allcount FROM t_testcase_casepkg WHERE packageId = ? ) AS aa, " +
			  "( SELECT count(*) AS waitAuditCount FROM t_testcase_casepkg WHERE execStatus =0 AND packageId =? ) bb, " + 
			  "( SELECT count(*) AS passCount FROM t_testcase_casepkg WHERE ( execStatus = 2 ) AND packageId = ? ) dd, " + 
			  "(SELECT count(*) AS failedCount FROM t_testcase_casepkg WHERE execStatus = 3 AND packageId = ? ) ee, " + 
			  "( SELECT count(*) AS invalidCount FROM t_testcase_casepkg WHERE execStatus = 4 AND packageId = ?  ) ff, " + 
			  "( SELECT count(*) AS blockCount FROM t_testcase_casepkg WHERE execStatus = 5 AND packageId = ? ) gg," + 
			  "( SELECT count(*) AS waitModifyCount FROM t_testcase_casepkg WHERE execStatus = 6 AND packageId = ? ) hh ) " ;
		String pkgId = testCasePackageDto.getTestCasePackage().getPackageId();
		List<Object[]> countlist = this.findBySql(sql,null,pkgId,pkgId,pkgId,pkgId,pkgId,pkgId,pkgId);
		
		dataVo.setAllCount(Integer.parseInt(countlist.get(0)[0].toString()));
		dataVo.setWaitAuditCount(Integer.parseInt(countlist.get(0)[1].toString()));
		dataVo.setPassCount(Integer.parseInt(countlist.get(0)[2].toString()));
		dataVo.setFailedCount(Integer.parseInt(countlist.get(0)[3].toString()));
		dataVo.setInvalidCount(Integer.parseInt(countlist.get(0)[4].toString()));
		dataVo.setBlockCount(Integer.parseInt(countlist.get(0)[5].toString()));
		dataVo.setWaitModifyCount(Integer.parseInt(countlist.get(0)[6].toString()));
		
		return dataVo;
	}
}