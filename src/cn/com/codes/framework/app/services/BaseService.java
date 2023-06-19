package cn.com.codes.framework.app.services;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;

import cn.com.codes.framework.common.ConvertObjArrayToVo;
import cn.com.codes.framework.common.HtmlListQueryObj;
import cn.com.codes.framework.common.ListObject;
import cn.com.codes.framework.hibernate.HibernateGenericController;
import cn.com.codes.framework.jdbc.JdbcTemplateWrapper;
import cn.com.codes.framework.transmission.dto.BaseDto;
import cn.com.codes.object.SoftwareVersion;
import cn.com.codes.object.TypeDefine;
import cn.com.codes.object.User;

public interface BaseService {

	public void add(Object entity);

	public void update(Object entity);

	public void delete(Class clasz, String id);

	public void delete(Object entity);

	public List<?> query(String hql, Object[] values);

	public <T> T get(Class<T> clasz, String id);

	public HibernateGenericController getHibernateGenericController();

	public <T> DetachedCriteria createDetachedCriteria(Class<T> clasz,
			Criterion... criterions);

	public <T> DetachedCriteria createDetachedCriteria(Class<T> clasz,
			Object qbeObject, Criterion... criterions);

	public Integer executeUpdateByHql(final String hql, final Object[] objects);

	public List findByDetachedCriteria(DetachedCriteria criteria, int pageNo,
			int pageSize);

	public List findByCriteria(Criteria criteria, int pageNo, int pageSize);

	public List findByDetachedCriteria(DetachedCriteria criteria);

	public List findByCriteria(Criteria criteria);

	public List findByHql(final String hql, final Object... values);

	public List findByHqlPage(final String hql, int pageNo, int pageSize,
			String columnNameForCount, final Object... values);

	public List findByHqlWithReshFlg(final String hql, final Boolean isRefresh,final Object... values);
	
	public List findByHqlWithPraValues(final String hql, final Object[] values);

	public List findByHqlWithValuesMap(final String hql,
			final Map<String, Object> praValuesMap, final boolean isRefresh);

	public List findByHqlWithValuesMap(final String hql, int pageNo,
			int pageSize, String columnNameForCount,
			final Map<String, Object> praValuesMap, final boolean isRefresh);

	public List findByHqlWithValuesMap(BaseDto dto);
	public List findBySqlWithValuesMap(BaseDto dto,Class...classz);
	
	public List findBySqlWithValuesMap(BaseDto dto,ConvertObjArrayToVo build);
	
	public Object findMaxMinByHqlWithValuesMap(final String hql,
			final Map<String, Object> praValuesMap, final boolean isRefresh);

	public List findBySql(final String sql, final Class clasz,
			final Object... values);

	public List findBySqlWithValuesMap(final String sql, final Class clasz,
			final Map<String, Object> praValuesMap);

	public List findBySqlConn(final String sql, final List<String> praValuesList, final int colCount);

	public List findByHqlWithPraValues(final String hql, int pageNo,
			final int pageSize, String columnNameForCount, final Object[] values);

	public <T> List<T> getAll(Class<T> clasz, String orderBy, boolean isAsc);

	public <T> T get(Class<T> clasz, Serializable id);

	public <T> List<T> findByProperties(Class<T> clasz, String[] propertyNames,
			Object[] values);

	public List findByNoCache(final String hql, final Object... values);

	public List getListDataByQBE(Class clasz, Object qbeObject, int pageNo,
			int pageSize);

	public void setAjaxListData(List<HtmlListQueryObj> QueryObjList,
			final boolean isRefresh);

	public List<List<ListObject>> getListData(List<HtmlListQueryObj> list,
			final boolean isRefresh);

	public void setListData(List<HtmlListQueryObj> QueryObjList,
			final boolean isRefresh);

	public HashMap<String, List<ListObject>> getListDataMap(
			List<HtmlListQueryObj> QueryObjList, final boolean isRefresh);

	public void evict(Class clasz);

	public void evictQuery(String cacheRegion);

	public List findByHqlRefresh(final String hql, final Object... values);

	public boolean excuteBatchHql(final String hql,
			final List<Map<String, Object>> praValuesList);

	public Integer callProcedure(final String callProcedStr,
			final Object... obj);

	public List<String> callProcedureReturnList(final String callProcedStr,
			final Object... obj);

	public String listSelectStr(List<HtmlListQueryObj> listObjects,
			final boolean isRefresh);

	public <T> Set<T> saveRealSet(Set<T> oldSet, Set<T> newSet,
			String idPorperty, String cacheRegion);

	public <T> Set<T> listToSet(List<T> list);

	public String ObjArrList2Json(List<Object[]> list, StringBuffer sb,
			int idIndex);

	public String isProjectLock(String projectId);

	public Map<String, User> getRelaUserWithName(List list,
			String... usrIdProNames);

	public Map<String, TypeDefine> getRelaTypeDefine(List list,
			String... idProNames);

	public Map<String, SoftwareVersion> getRelaVers(List list, String... idProNames);

	public Map<String,User> getRelaUserWithNameByDocUserId(List list,String... DocUserIdProNames);
	
	public void sortStringList(List<String> list);
	
	public Map<String, ListObject> getRelaTestTasks(List list, String... idProNames);

	public boolean reNameChk(String objName, String nameVal,
			String namePropName, String idPropName, String idPropVal);

	public boolean reNameChkInComp(String objName, String nameVal,
			String namePropName, String idPropName, String idPropVal);
	
	public boolean reNameChkInComp(String objName, final String nameVal,
			final String namePropName, String idPropName, final Object idPropVal,String companyrpoName);
	

	public void sortLongList(List<Long> list);
	
	public String getMailAddrByUserIds(String userIds,String splitStr);
	
	public List<Map<String,Object>> findBySqlByJDBC(final String sql);
	public List<Map<String,Object>> commonfindBySqlByJDBC(final String sql,final boolean proConvert,final HashMap praValuesMap);
	
	public JdbcTemplateWrapper getJdbcTemplateWrapper() ;


}
