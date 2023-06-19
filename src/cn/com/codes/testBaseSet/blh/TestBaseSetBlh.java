package cn.com.codes.testBaseSet.blh;

import java.util.Date;
import java.util.List;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.dto.PageModel;
import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.common.JsonUtil;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.object.TypeDefine;
import cn.com.codes.testBaseSet.dto.TestBaseSetDto;
import cn.com.codes.testBaseSet.dto.TestBaseSetVo;
import cn.com.codes.testBaseSet.service.TestBaseSetService;

public class TestBaseSetBlh extends BusinessBlh {

	private TestBaseSetService testBaseSetService;
	
	public View testBaseSetList(BusiRequestEvent req){
		return super.getView();
	}
	
	public View loadTestBaseSetList(BusiRequestEvent req){
		
		TestBaseSetDto dto = super.getDto(TestBaseSetDto.class, req);
		String hql = "";
		if(!cn.com.codes.common.util.StringUtils.isNullOrEmpty(dto.getFlag()) && dto.getFlag().equals("1")){
			hql = "from TypeDefine where (compId=? or compId=1) and status=1  order by updDate,isDefault desc ";
		}else{
			hql = "from TypeDefine where (compId=? or compId=1) and status!=3  order by updDate,isDefault desc ";
		}
		
		if(dto.getSubName()!=null&&!"all".equals(dto.getSubName())&&!"".equals(dto.getSubName())){
			if(dto.getTestBaseSet()==null){
				dto.setTestBaseSet(new TestBaseSetVo());
			}
			
			hql = hql.replaceAll("TypeDefine", dto.getTestBaseSet().getSubClass(dto.getSubName()).getSimpleName());
		}
		List<TypeDefine> typeList = testBaseSetService.findByHqlPage(hql, dto.getPageNo(), dto.getPageSize(), "typeId", SecurityContextHolderHelp.getCompanyId()); 
		SecurityContextHolderHelp.getCompanyId(); 
		//System.out.println(JsonUtil.toJson(typeList));
		if(typeList!=null&&!typeList.isEmpty()){
			for(TypeDefine td :typeList){
				td.setSubName(TypeDefine.getTypeNameMap().get(td.getClass().getSimpleName()));
			}
		}
		
		List list = typeList;
		PageModel pageModel = new PageModel(); 
		pageModel.setRows(list);
		Integer total =  (Integer) SecurityContextHolder.getContext().getAttr("pageInfoTotalRows");
		pageModel.setTotal(total);

		super.writeResult(JsonUtil.toJson(pageModel));
		return super.globalAjax();
	}

	
	public View add(BusiRequestEvent req){
		TestBaseSetDto dto = super.getDto(TestBaseSetDto.class, req);
		String reNameHql = "from " +dto.getTestBaseSet().getSubClass(dto.getTestBaseSet().getSubName()).getSimpleName();
		reNameHql = reNameHql + " where typeName=? and status!=3" ;
		Long count = testBaseSetService.getHibernateGenericController().getResultCount(reNameHql, new Object[]{dto.getTestBaseSet().getTypeName()}, "typeId");
		if(count>0){
			super.writeResult("reName");
			return super.globalAjax();			
		}
		TypeDefine td = dto.getTestBaseSet().copy2TypeDefine();
		td.setCompId(SecurityContextHolderHelp.getCompanyId());
		td.setUpdDate(new Date());
		testBaseSetService.add(td);
		super.writeResult("success$"+td.toStrUpdateRest());
		return super.globalAjax();
	}
	
	public View update(BusiRequestEvent req){
		TestBaseSetDto dto = super.getDto(TestBaseSetDto.class, req);
		Long id = dto.getTestBaseSet().getTypeId();
		String hql = "from TypeDefine where typeId=? and compId=? and isDefault=0 ";
		List<TypeDefine> list = testBaseSetService.findByHql(hql, id,SecurityContextHolderHelp.getCompanyId());
		TypeDefine td = list.get(0);
		td.setSubName();
		if(!dto.getTestBaseSet().getInitSubName().equals(dto.getTestBaseSet().getSubName())||!dto.getTestBaseSet().getInitSubName().equals(td.getSubName())){
			String reNameHql = "from " +dto.getTestBaseSet().getSubClass(dto.getTestBaseSet().getSubName()).getSimpleName();
			reNameHql = reNameHql + " where typeName=? and status!=3" ;
			Long count = testBaseSetService.getHibernateGenericController().getResultCount(reNameHql, new Object[]{dto.getTestBaseSet().getTypeName()}, "typeId");
			if(count>0){
				super.writeResult("reName");
				return super.globalAjax();			
			}
			TypeDefine newTd = testBaseSetService.chgTypeDefine(dto);
			super.writeResult("success$"+newTd.toStrUpdateRest());
			return super.globalAjax();		
		}else{
			String reNameHql = "from " +dto.getTestBaseSet().getSubClass(dto.getTestBaseSet().getSubName()).getSimpleName();
			reNameHql = reNameHql + " where typeName=? and typeId!=? and status!=3" ;
			Long count = testBaseSetService.getHibernateGenericController().getResultCount(reNameHql, new Object[]{dto.getTestBaseSet().getTypeName(),dto.getTestBaseSet().getTypeId()}, "typeId");
			if(count>0){
				super.writeResult("reName");
				return super.globalAjax();			
			}
			td.setUpdDate(new Date());
			td.setIsDefault(0);
			td.setRemark(dto.getTestBaseSet().getRemark().trim());
			td.setTypeName(dto.getTestBaseSet().getTypeName().trim());
			super.writeResult("success$"+td.toStrUpdateRest());
			testBaseSetService.update(td);
		}
		return super.globalAjax();
	}

	public View updInit(BusiRequestEvent req){
		TestBaseSetDto dto = super.getDto(TestBaseSetDto.class, req);
		Long id = dto.getTestBaseSet().getTypeId();
		String hql = "from TypeDefine where typeId=? and compId=? and isDefault=0";
		List<TypeDefine> list = testBaseSetService.findByHql(hql, id,SecurityContextHolderHelp.getCompanyId());
		if(list==null||list.isEmpty()){
			super.writeResult("NotFind");
		}else{
			list.get(0).setSubName();
			super.writeResult(super.addJsonPre("dto.testBaseSet", list.get(0)));
		}
		return super.globalAjax();
	}
	
	public View delete(BusiRequestEvent req){
		TestBaseSetDto dto = super.getDto(TestBaseSetDto.class, req);
		String hql = "update TypeDefine set status = ? ,updDate = ? where typeId=?";
		testBaseSetService.executeUpdateByHql(hql, new Object[]{"3",new Date(),dto.getTestBaseSet().getTypeId()});
		return super.globalAjax();
	}
	public View swStatus(BusiRequestEvent req){
		TestBaseSetDto dto = super.getDto(TestBaseSetDto.class, req);
		String hql = "update TypeDefine set status = ?,updDate=? where typeId=?";
		testBaseSetService.executeUpdateByHql(hql, new Object[]{dto.getTestBaseSet().getStatus(),new Date(),dto.getTestBaseSet().getTypeId()});
		return super.globalAjax();
	}

	
	public TestBaseSetService getTestBaseSetService() {
		return testBaseSetService;
	}

	public void setTestBaseSetService(TestBaseSetService testBaseSetService) {
		this.testBaseSetService = testBaseSetService;
	}
	
	public View getSubList(BusiRequestEvent req){
		TestBaseSetDto dto = new TestBaseSetDto();
		super.writeResult(JsonUtil.toJson(dto.getSubList()));
		return super.globalAjax();
	}
	
	//设置首选项
	public View setPreference(BusiRequestEvent req){
		
		TestBaseSetDto dto = super.getDto(TestBaseSetDto.class, req);
		Long id = dto.getTestBaseSet().getTypeId();
		//查询该类型的首先选项
		String  hql = "from TypeDefine where (compId=? or compId=1) and preference=1 and status!=3  order by updDate,isDefault desc ";
		
		if(dto.getSubName()!=null&&!"all".equals(dto.getSubName())&&!"".equals(dto.getSubName())){
			if(dto.getTestBaseSet()==null){
				dto.setTestBaseSet(new TestBaseSetVo());
			}
			
			hql = hql.replaceAll("TypeDefine", dto.getTestBaseSet().getSubClass(dto.getSubName()).getSimpleName());
		}
		
		List<TypeDefine> list = testBaseSetService.findByHql(hql, SecurityContextHolderHelp.getCompanyId());
		String updateStr = "update TypeDefine set preference = ? where typeId=?";
		if(list.size()>0){
			//更新原来的默认项的preference设置为0
			testBaseSetService.executeUpdateByHql(updateStr, new Object[]{new Integer(0),list.get(0).getTypeId()});
		}
		//更新新的默认项
		testBaseSetService.executeUpdateByHql(updateStr, new Object[]{new Integer(1),dto.getTestBaseSet().getTypeId()});
		super.writeResult("success");
		return super.globalAjax();
		
	}

}
