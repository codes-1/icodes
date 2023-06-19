package cn.com.codes.framework.app.services;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.springframework.orm.hibernate3.HibernateCallback;

import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.common.ConvertObjArrayToVo;
import cn.com.codes.framework.common.HtmlListComponent;
import cn.com.codes.framework.common.HtmlListQueryObj;
import cn.com.codes.framework.common.ListObject;
import cn.com.codes.framework.exception.DataBaseException;
import cn.com.codes.framework.hibernate.HibernateGenericController;
import cn.com.codes.framework.jdbc.JdbcTemplateWrapper;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.transmission.dto.BaseDto;
import cn.com.codes.object.SoftwareVersion;
import cn.com.codes.object.TypeDefine;
import cn.com.codes.object.User;
import cn.com.codes.framework.app.services.BaseService;
import cn.com.codes.framework.app.services.BaseServiceImpl;
//import cn.com.codes.framework.app.services.idComparator;


public  class BaseServiceImpl implements BaseService{
	private static Logger logger = Logger.getLogger(BaseServiceImpl.class);
	
	
	public HibernateGenericController hibernateGenericController;
	private JdbcTemplateWrapper jdbcTemplateWrapper;

	public BaseServiceImpl() {

	}

	public HibernateGenericController getHibernateGenericController() {
		return hibernateGenericController;
	}

	public void setHibernateGenericController(
			HibernateGenericController hibernateGenericController) {
		this.hibernateGenericController = hibernateGenericController;
	}

	public List<?> query(String hql, Object[] values) {
		return hibernateGenericController.findByHql(hql, values);
	}

	public List getListDataByQBE(Class clasz, Object qbeObject, int pageNo, int pageSize) {
		if (qbeObject == null) {
			try {
				qbeObject = clasz.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		if (qbeObject == null) {
			return new ArrayList();
		}
		DetachedCriteria criteria = this.createDetachedCriteria(clasz, qbeObject);
		return this.findByDetachedCriteria(criteria, pageNo, pageSize);
	}

	public void update(Object object) {
		this.hibernateGenericController.update(object);
	}

	public void add(Object entity) {
		this.getHibernateGenericController().save(entity);
	}
	
	public void saveOrUpdate(Object entity) {
		this.getHibernateGenericController().saveOrUpdate(entity);
	}
	
	public void batchSaveOrUpdate(final List objects){
		this.getHibernateGenericController().batchSaveOrUpdate(objects);
	}

	public void delete(Class clasz, String id) {
		this.getHibernateGenericController().deleteById(clasz, id);
	}
	public void delete(Class clasz, Long id) {
		this.getHibernateGenericController().deleteById(clasz, id);
	}
	public void delete(Object entity){
		hibernateGenericController.delete(entity);
	}
	public <T> T get(Class<T> clasz, String id) {
		return hibernateGenericController.get(clasz, id);
	}

	public <T> DetachedCriteria createDetachedCriteria(Class<T> clasz, Criterion... criterions) {
		return hibernateGenericController.createDetachedCriteria(clasz, criterions);
	}

	/**
	 * 匹配模式,使用模糊查询必填项,空的不做查询条件,字串忽略大小写
	 */
	public <T> DetachedCriteria createDetachedCriteria(Class<T> clasz, Object qbeObject, Criterion... criterions) {
		Example example = Example.create(qbeObject);
		example.enableLike(MatchMode.ANYWHERE);
		//example.excludeNone();
		example.ignoreCase();
		DetachedCriteria criteria = hibernateGenericController.createDetachedCriteria(clasz, criterions);
		criteria.add(example);
		return criteria;
	}

	public List findByDetachedCriteria(DetachedCriteria criteria, int pageNo, int pageSize) {
		int totalRows = hibernateGenericController.getResultCount(criteria).intValue();
		pageNo = this.getValidPage(pageNo, totalRows, pageSize);
		if(totalRows==0){
			return new ArrayList();
		}
		return this.getHibernateGenericController().findByDetachedCriteria(criteria, pageNo, pageSize);
	}

	public List findBySql(final String sql, final Class clasz ,final Object... values) {
		return hibernateGenericController.findBySql(sql, clasz,values);
	}
	
	public List findBySql(final String sql) {
		return hibernateGenericController.findBySql(sql);
	}

	public List findBySqlWithValuesMap(final String sql, final Class clasz ,final Map<String, Object> praValuesMap){
		return hibernateGenericController.findBySqlWithValuesMap(sql, clasz, praValuesMap);
	}
	
	public List findByHqlWithReshFlg(final String hql, final Boolean isRefresh,final Object... values) {
		return hibernateGenericController.findByHqlWithReshFlg(hql, isRefresh,values);
	}	
	
	public List findBySqlConn(final String sql, final List<String> praValuesList, final int colCount){
		return hibernateGenericController.findBySqlConn(sql, praValuesList, colCount);
	}
	
	
	public List<Map<String,Object>> findBySqlByJDBC(final String sql){
		return hibernateGenericController.findBySqlByJDBC(sql);
	}
	
	public List<Map<String,Object>> commonfindBySqlByJDBC(final String sql,final boolean proConvert,final HashMap praValuesMap){
		return hibernateGenericController.commonfindBySqlByJDBC(sql, proConvert, praValuesMap);
	}
	public List findByCriteria(Criteria criteria, int pageNo, int pageSize) {
		int totalRows = hibernateGenericController.getResultCount(criteria).intValue();
		pageNo = this.getValidPage(pageNo, totalRows, pageSize);
		//dto.setTotal(totalRows);
		if(totalRows==0){
			return new ArrayList();
		}
		return hibernateGenericController.findByCriteria(criteria, pageNo, pageSize);
	}

	public List findByHql(final String hql, final Object... values) {
		return hibernateGenericController.findByHql(hql, values);
	}
	
	public List findByHqlPage(final String hql, int pageNo, int pageSize, String columnNameForCount, final Object... values) {
		int totalRows = hibernateGenericController.getResultCount(hql, values, columnNameForCount).intValue();
		pageNo = this.getValidPage(pageNo, totalRows, pageSize);
		//dto.setTotal(totalRows);
		if(totalRows==0){
			return new ArrayList();
		}
		return hibernateGenericController.findByHql(hql, pageNo, pageSize, values);
	}

	public List findMaxMinByHql(final String hql, final Object... values) {
		return hibernateGenericController.findByHql(hql, values);
	}

	public List findByDetachedCriteria(DetachedCriteria criteria) {
		return hibernateGenericController.findByDetachedCriteria(criteria);
	}

	public List findByCriteria(Criteria criteria) {
		return hibernateGenericController.findByCriteria(criteria);
	}
	
	public List findByHqlNoCache(final String hql, final Object... values) {
		return hibernateGenericController.findByHqlNoCache(hql, values);
	}

	public List findByHqlWithPraValues(final String hql,  int pageNo, int pageSize, String columnNameForCount, final Object[] values) {
		int totalRows = hibernateGenericController.getResultCount(hql, values, columnNameForCount).intValue();
		pageNo = this.getValidPage(pageNo, totalRows, pageSize);
		//dto.setTotal(totalRows);
		if(totalRows==0){
			return new ArrayList();
		}
		return hibernateGenericController.findByHqlWithPraValues(hql, pageNo, pageSize, values);
	}
	
	public List findByHqlWithValuesMap(final String hql, final Map<String, Object> praValuesMap, final boolean isRefresh) {
		return hibernateGenericController.findByHqlWithValuesMap(hql, praValuesMap, isRefresh);
	}
	
	public Object findMaxMinByHqlWithValuesMap(final String hql, final Map<String, Object> praValuesMap, final boolean isRefresh){
		return hibernateGenericController.findMaxMinByHqlWithValuesMap(hql, praValuesMap, isRefresh);
	}

	public List findByHqlWithValuesMap(final String hql,  int pageNo, int pageSize, String columnNameForCount, final Map<String, Object> praValuesMap, final boolean isRefresh) {
		int totalRows = hibernateGenericController.getResultCountWithValuesMap(hql, praValuesMap, columnNameForCount, isRefresh).intValue();
		pageNo = this.getValidPage(pageNo, totalRows, pageSize);
//		dto.setTotal(totalRows);
		if(totalRows==0){
			return new ArrayList();
		}
		return hibernateGenericController.findByHqlWithValuesMap(hql, pageNo, pageSize, praValuesMap, isRefresh);
	}

	public List findByHqlWithPraValues(final String hql, final Object[] values){
		return hibernateGenericController.findByHqlWithPraValues(hql, values);
	}
	
	public Integer executeUpdateByHql(final String hql, final Object[] objects){
		return hibernateGenericController.executeUpdateByHql(hql, objects);
	}
	
	public Integer executeUpdateByHqlWithValuesMap(final String hql, final Map<String, Object> praValuesMap){
		return hibernateGenericController.executeUpdateByHqlWithValuesMap(hql, praValuesMap);
	}

	public <T> List<T> getAll(Class<T> clasz, String orderBy, boolean isAsc) {
		return hibernateGenericController.getAll(clasz, orderBy, isAsc);
	}

	public <T> T get(Class<T> clasz, Serializable id) {
		return hibernateGenericController.get(clasz, id);
	}

	public <T> List<T> findByProperties(Class<T> clasz, String[] propertyNames, Object[] values) {
		return hibernateGenericController.findByProperties(clasz, propertyNames, values);
	}

	public List findByNoCache(final String hql, final Object... values) {

		return hibernateGenericController.findByNoCache(hql, values);
	}
	
	private Integer  getValidPage(Integer pageNo, int totalRows, Integer pageSize){
        if (!isValidPage(pageNo, totalRows, pageSize)) {
            return getValidPage(--pageNo, totalRows, pageSize);
        }
        int pageCount = (totalRows+(pageSize-(totalRows%pageSize==0?pageSize:totalRows%pageSize)))/pageSize ;
        StringBuffer pagesb = new StringBuffer(pageNo.toString());
    	pagesb.append("/");
    	pagesb.append(pageSize.toString());
    	pagesb.append("/");
    	pagesb.append(pageCount);
    	pagesb.append("$");
        SecurityContextHolder.getContext().setAttr("pageInfo", pagesb.toString());
        SecurityContextHolder.getContext().setAttr("pageInfoTotalRows", totalRows);
        return pageNo;	
	}
	
    private boolean isValidPage(Integer pageNo, Integer totalRows, Integer pageSize) {
        if (pageNo == 1) {
            return true;
        }
        int rowStart = (pageNo - 1) * pageSize;
        int rowEnd = rowStart + pageSize;
        if (rowEnd > totalRows) {
            rowEnd = totalRows;
        }
        return rowEnd > rowStart;
    }
    
	private void appendListStr(List<List<ListObject>> list){
		SecurityContextHolder.getContext().appendToResponse(HtmlListComponent.toSelectStrWithBreak(list));
	}

	public void setAjaxListData(List<HtmlListQueryObj> QueryObjList, final boolean isRefresh) {
		this.appendListStr(this.getListData(QueryObjList, isRefresh));
	}

	public void setListData(List<HtmlListQueryObj> QueryObjList, final boolean isRefresh){
		HtmlListComponent.setListDate(getListDataMap(QueryObjList, isRefresh));
	}

	public List<List<ListObject>> getListData(List<HtmlListQueryObj> QueryObjList, final boolean isRefresh){
		List<List<ListObject>> list = new ArrayList<List<ListObject>>();
		for (HtmlListQueryObj queryObj : QueryObjList) {
			StringBuffer hql = new StringBuffer();
			hql.append("select new cn.com.codes.framework.common.ListObject(")
					.append(queryObj.getKeyPropertyName())
					.append(" as keyObj , ")
					.append(queryObj.getValuePropertyName())
					.append(" as valueObj ) from ")
					.append(queryObj.getHqlObjName())
					.append(" ")
					.append((queryObj.getConditions() != null ? queryObj.getConditions(): ""));
			List<ListObject> listDates = this.findByHqlWithValuesMap(hql.toString(), queryObj.getParaValues(), isRefresh);
			list.add(listDates);
		}
		return list;
	}
	public List<ListObject> getListData(HtmlListQueryObj queryObj, final boolean isRefresh){
			StringBuffer hql = new StringBuffer();
			hql.append("select new cn.com.codes.framework.common.ListObject(")
					.append(queryObj.getKeyPropertyName())
					.append(" as keyObj , ")
					.append(queryObj.getValuePropertyName())
					.append(" as valueObj ) from ")
					.append(queryObj.getHqlObjName())
					.append(" ")
					.append((queryObj.getConditions() != null ? queryObj.getConditions(): ""));
			List<ListObject> listDates = this.findByHqlWithValuesMap(hql.toString(), queryObj.getParaValues(), isRefresh);
		return listDates;
	}

	public HashMap<String, List<ListObject>> getListDataMap(List<HtmlListQueryObj> QueryObjList, final boolean isRefresh) {
		HashMap<String, List<ListObject>> map = new HashMap<String, List<ListObject>>();
		for (HtmlListQueryObj queryObj : QueryObjList){
			StringBuffer hql = new StringBuffer();
			hql.append("select new cn.com.codes.framework.common.ListObject(")
					.append(queryObj.getKeyPropertyName())
					.append(" as keyObj , ")
					.append(queryObj.getValuePropertyName())
					.append(" as valueObj ")
					.append(queryObj.getHqlObjName())
					.append(" ").append((queryObj.getConditions() != null ? queryObj.getConditions(): ""));
			List<ListObject> listDates = this.findByHqlWithValuesMap(hql.toString(), queryObj.getParaValues(), isRefresh);
			map.put(queryObj.getListName(), listDates);
		}
		return map;
	}
    
    public String getUuId(){
    	return hibernateGenericController.getUuId();
    }
    
    public void evict(Class clasz){
    	hibernateGenericController.evict(clasz);
    }
    
    public void evict(Class clasz, final Serializable id){
    	hibernateGenericController.evict(clasz, id);
    }
    
    public void evictQuery(String cacheRegion){
    	hibernateGenericController.evictQueries(cacheRegion);
    }
    
    public void evictQuery(){
    	hibernateGenericController.evictQueries();
    }
    
	public List findByHqlRefresh(final String hql, int pageNo, int pageSize, String columnNameForCount, final Object... values){
		int totalRows = hibernateGenericController.getResultCountByRefresh(hql, values, columnNameForCount).intValue();
		pageNo = this.getValidPage(pageNo, totalRows, pageSize);
		//dto.setTotal(totalRows);
		if(totalRows==0){
			return new ArrayList();
		}
		return hibernateGenericController.findByHqlRefresh(hql, values);
	}
	
	public List findByHqlRefresh(final String hql, final Object... values){
		 return hibernateGenericController.findByHqlRefresh(hql, values);
	}

	public boolean excuteBatchHql(final String hql,final Map<String, Object> praValuesList){
		return hibernateGenericController.excuteBatchHql(hql, praValuesList);
	}
	
	public boolean excuteBatchHql(final String hql,final List<Map<String, Object>> praValuesList){
		return hibernateGenericController.excuteBatchHql(hql, praValuesList);
	}
	
	public Integer callProcedure(final String callProcedStr, final Object... obj) {
		return hibernateGenericController.callProcedure(callProcedStr, obj);
	}
	
	public List<String> callProcedureReturnList(final String callProcedStr, final Object... obj){
		return hibernateGenericController.callProcedureReturnList(callProcedStr, obj);
	}
	
	public String listSelectStr(List<HtmlListQueryObj> listObjects, final boolean isRefresh){
		return HtmlListComponent.toSelectStrWithBreak(this.getListData(listObjects, isRefresh));
	}
	
    /**
     * 保存一对多关联对像一一方，且多方不由一方来维护，多方的维护方法:
     * 比较两个set 如newSet中没有，oldSet有的对像，将执行删除，所有要删的删除后，会合并两个
     * set 然后循环合并的set 并调用idPorperty的get方法返回null或是""，将做增加操作 否则什么也不做
     * @param oldSet 保存一方前，原有的多方的集合
     * @param oldSet 当前新的多方集合
     * @param idPorperty 对像的唯一标识属性名 
     * @param cacheRegion 要清的query缓存区域 如不为空，两个set不一样时，就执行清query缓存 不需要清传空既可
     */
	public  <T> Set<T> saveRealSet(Set<T> oldSet,Set<T> newSet,String idPorperty,String cacheRegion){
		boolean havcChage = false ;
		if (!newSet.containsAll(oldSet)){
			for (Iterator<T> it = oldSet.iterator(); it.hasNext();) {
				T obj = (T) it.next();
				if (!newSet.contains(obj)) {
					it.remove();
					this.delete(obj);
				}
			}
			if(cacheRegion!=null&&!"".equals(cacheRegion.trim())){
				havcChage = true ;
			}
		}
		oldSet.addAll(newSet);
		Method idPorGetMethod = null;
		String idPorGetMethodName = "get"+upperFirstChar(idPorperty);
		for(T obj :oldSet){
			if(idPorGetMethod == null){
				try {
					idPorGetMethod = obj.getClass().getMethod(idPorGetMethodName);
				} catch (SecurityException e) {
					logger.error(e);
					throw new DataBaseException("occur error when invoke getMethod  " +idPorGetMethodName,e);
				} catch (NoSuchMethodException e) {
					logger.error(e);
					throw new DataBaseException(obj.getClass().getSimpleName() +"no"+ idPorGetMethodName+"method ",e);
				}
			}
			Object idValue = null;
			try {
				idValue = idPorGetMethod.invoke(obj);
			} catch (IllegalArgumentException e) {
				logger.error(e);
				String msg = "occur error when invoke " +obj.getClass().getSimpleName() +"Object's " +idPorGetMethodName;
				throw new DataBaseException(msg,e);
			} catch (IllegalAccessException e) {
				logger.error(e);
				String msg = "occur error when invoke " +obj.getClass().getSimpleName() +"Object's " +idPorGetMethodName;
				throw new DataBaseException(msg,e);
			} catch (InvocationTargetException e) {
				logger.error(e);
				String msg = "occur error when invoke " +obj.getClass().getSimpleName() +"Object's " +idPorGetMethodName;
				throw new DataBaseException(msg,e);
			}
			
			if(idValue==null||"".equals(idValue.toString().trim())){
				this.add(obj);
				havcChage = true ;
			}
		}
		if(havcChage&&(cacheRegion!=null&&!"".equals(cacheRegion.trim()))){
			this.evictQuery(cacheRegion);
		}
		newSet = null;
		return oldSet;
	}
	
	public  <T>Set<T> listToSet(List<T> list){
		Set<T> set = new HashSet<T>() ;
		set.addAll(list);
		return set ;
	}
	private  static String upperFirstChar(String string) {
		if (string == null){
			return null;
		}
		if (string.length() <= 1){
			return string.toLowerCase();
		}
		StringBuffer sb = new StringBuffer(string);
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		return sb.toString();
	}

	
	public String ObjArrList2Json(List<Object[]> list,StringBuffer sb,int idIndex){
		if(sb==null){
			sb = new StringBuffer();
		}
		int i =0 ;
		if(list != null && list.size()>0){
			sb.append("{rows: [");
			for(Object[] values:list){
				i++ ;
				if(i != list.size()){
					this.ObjArr2Json(values,sb, idIndex, true);
				}else{
					this.ObjArr2Json(values,sb, idIndex, false);
				}
			}	
			sb.append("]}");
		}		
	    Object pageInfo = SecurityContextHolder.getContext().getAttr("pageInfo");
	    if(pageInfo != null){
	    	return  sb.insert(0, pageInfo.toString()).toString();
	    }else{
	    	return sb.toString();
	    }
	}
	
	private void ObjArr2Json(Object[] values,StringBuffer sb,int idIndex,boolean appendComma){
		sb.append("{");
		sb.append("id:'");
        sb.append(values[idIndex].toString());
        sb.append("',data: [0,'','");
        int i=0;
        for(Object obj :values){
        	if(i!=idIndex){
        		if(i<values.length-1){
        			if(obj!=null&&obj instanceof  Date){
        				sb.append(StringUtils.formatShortDate((Date)obj)).append("','");
        			}else{
        				sb.append(obj==null?"":obj.toString()).append("','");
        			}
        		}else{
        			if(obj!=null&&obj instanceof  Date){
        				sb.append(StringUtils.formatMiddleDate((Date)obj)).append("'");
        			}else{
        				sb.append(obj==null?"":obj.toString()).append("'");
        			}
        		}
        		i++;
        	}else if(idIndex==0&&i==0){
            	i++;
            }
        }
        sb.append("]");
        sb.append("}");
        if(appendComma){
        	sb.append(",");
        }
	}
	/**
	 * @deprecated
	 */
	public String isProjectLock(String projectId){
		
		return "true";
	}
	
	public Map<String,User> getRelaUserWithName(List list,String... usrIdProNames){
		if(list==null||list.size()==0||usrIdProNames==null||list.isEmpty()){
			return null;
		}
		List<String> getMethdNameList = new ArrayList<String>(usrIdProNames.length);
		List<Method> getMethdList = new ArrayList<Method>(usrIdProNames.length);
		for(String getMth :usrIdProNames){
			getMethdNameList.add("get"+upperFirstChar(getMth));
		}
		List<String> userIds = new ArrayList<String>();
		for(Object obj :list){
			if(getMethdList.size()==0){
				int i=0;
				try {
					for(String methName :getMethdNameList){
						getMethdList.add(obj.getClass().getMethod(methName));
						i++;
					}
				} catch (SecurityException e) {
					logger.error(e);
					throw new DataBaseException("occur error when invoke getMethod  " +getMethdNameList.get(i) ,e);
				} catch (NoSuchMethodException e) {
					logger.error(e);
					throw new DataBaseException(obj.getClass().getSimpleName() +"no " +getMethdNameList.get(i) +"method ",e);
				}
			}
			Object idValue = null;
			int i=0;
			try {
				for(Method idPorGetMethod:getMethdList){
					idValue = idPorGetMethod.invoke(obj);
					if(idValue!=null&&!"".equals(idValue)&&!userIds.contains(idValue.toString())){
						userIds.add(idValue.toString());
					}	
					i++;
				}
			} catch (IllegalArgumentException e) {
				logger.error(e);
				String msg = "occur error when invoke " +obj.getClass().getSimpleName() +"Object's " +getMethdNameList.get(i);
				throw new DataBaseException(msg,e);
			} catch (IllegalAccessException e) {
				logger.error(e);
				String msg = "occur error when invoke " +obj.getClass().getSimpleName() +"Object's " +getMethdNameList.get(i);
				throw new DataBaseException(msg,e);
			} catch (InvocationTargetException e) {
				logger.error(e);
				String msg = "occur error when invoke " +obj.getClass().getSimpleName() +"Object's " +getMethdNameList.get(i);
				throw new DataBaseException(msg,e);
			}
		}
		String hql = "select new  User(id,name ,loginName) from User where id in(:ids) " ;
		Map praValuesMap = new HashMap(1);
		//排序是为了同样的用户ID但顺序不一样时，统一排序以尽可能我多的用缓存
		this.sortStringList(userIds);
		praValuesMap.put("ids", userIds);
		if(userIds.size()==0){
			return  new HashMap<String,User>(1);
		}
		List<User> userList = this.findByHqlWithValuesMap(hql, praValuesMap, false);
		if(userList==null||userList.isEmpty()){
			return  new HashMap<String,User>(1);
		}
		Map<String,User> userMap = new HashMap<String,User>(userList.size());
		for(User user :userList){
			userMap.put(user.getId(), user);
		}
		praValuesMap = null;
		userIds = null;
		getMethdNameList = null;
		getMethdList = null;
		return userMap;
	}
	
	public Map<String,TypeDefine> getRelaTypeDefine(List list,String... idProNames){
		if(list==null||list.size()==0||idProNames==null){
			return null;
		}
		List<String> getMethdNameList = new ArrayList<String>(idProNames.length);
		List<Method> getMethdList = new ArrayList<Method>(idProNames.length);
		for(String getMth :idProNames){
			getMethdNameList.add("get"+upperFirstChar(getMth));
		}
		List<Long> typeDefineId = new ArrayList<Long>();
		for(Object obj :list){
			if(getMethdList.size()==0){
				int i=0;
				try {
					for(String methName :getMethdNameList){
						getMethdList.add(obj.getClass().getMethod(methName));
						i++;
					}
				} catch (SecurityException e) {
					logger.error(e);
					throw new DataBaseException("occur error when invoke getMethod  " +getMethdNameList.get(i) ,e);
				} catch (NoSuchMethodException e) {
					logger.error(e);
					throw new DataBaseException(obj.getClass().getSimpleName() +"no " +getMethdNameList.get(i) +"method ",e);
				}
			}
			Object idValue = null;
			int i=0;
			try {
				for(Method idPorGetMethod:getMethdList){
					idValue = idPorGetMethod.invoke(obj);
					if(idValue!=null&&!"".equals(idValue)&&!typeDefineId.contains(idValue)){
						typeDefineId.add((Long)idValue);
					}	
					i++;
				}
			} catch (IllegalArgumentException e) {
				logger.error(e);
				String msg = "occur error when invoke " +obj.getClass().getSimpleName() +"Object's " +getMethdNameList.get(i);
				throw new DataBaseException(msg,e);
			} catch (IllegalAccessException e) {
				logger.error(e);
				String msg = "occur error when invoke " +obj.getClass().getSimpleName() +"Object's " +getMethdNameList.get(i);
				throw new DataBaseException(msg,e);
			} catch (InvocationTargetException e) {
				logger.error(e);
				String msg = "occur error when invoke " +obj.getClass().getSimpleName() +"Object's " +getMethdNameList.get(i);
				throw new DataBaseException(msg,e);
			}
		}
		String hql = " select new cn.com.codes.object.DefaultTypeDefine(typeId,typeName) from TypeDefine where typeId in(:ids) " ;
		Map praValuesMap = new HashMap(1);
		//排序是为了同样的用户ID但顺序不一样时，统一排序以尽可能我多的用缓存
		this.sortLongList(typeDefineId);
		praValuesMap.put("ids", typeDefineId);
		if(typeDefineId.size()==0){
			return new HashMap<String,TypeDefine>(1);
		}
		List<TypeDefine> typeList = this.findByHqlWithValuesMap(hql, praValuesMap, false);
		if(typeList==null||typeList.isEmpty()){
			return new HashMap<String,TypeDefine>(1);
		}
		Map<String,TypeDefine> typeMap = new HashMap<String,TypeDefine>(typeList.size());
		for(TypeDefine tp :typeList){
			typeMap.put(tp.getTypeId().toString(), tp);
		}
		praValuesMap = null;
		typeDefineId = null;
		getMethdNameList = null;
		getMethdList = null;
		return typeMap;
	}
	
	public Map<String, SoftwareVersion> getRelaVers(List list, String... idProNames){
		
		if(list==null||list.size()==0||idProNames==null){
			return null;
		}
		List<String> getMethdNameList = new ArrayList<String>(idProNames.length);
		List<Method> getMethdList = new ArrayList<Method>(idProNames.length);
		for(String getMth :idProNames){
			getMethdNameList.add("get"+upperFirstChar(getMth));
		}
		List<Long> versionIds = new ArrayList<Long>();
		for(Object obj :list){
			if(getMethdList.size()==0){
				int i=0;
				try {
					for(String methName :getMethdNameList){
						getMethdList.add(obj.getClass().getMethod(methName));
						i++;
					}
				} catch (SecurityException e) {
					logger.error(e);
					throw new DataBaseException("occur error when invoke getMethod  " +getMethdNameList.get(i) ,e);
				} catch (NoSuchMethodException e) {
					logger.error(e);
					throw new DataBaseException(obj.getClass().getSimpleName() +"no " +getMethdNameList.get(i) +"method ",e);
				}
			}
			Object idValue = null;
			int i=0;
			try {
				for(Method idPorGetMethod:getMethdList){
					idValue = idPorGetMethod.invoke(obj);
					if(idValue!=null&&!"".equals(idValue)&&!versionIds.contains(idValue)){
						versionIds.add((Long)idValue);
					}	
					i++;
				}
			} catch (IllegalArgumentException e) {
				logger.error(e);
				String msg = "occur error when invoke " +obj.getClass().getSimpleName() +"Object's " +getMethdNameList.get(i);
				throw new DataBaseException(msg,e);
			} catch (IllegalAccessException e) {
				logger.error(e);
				String msg = "occur error when invoke " +obj.getClass().getSimpleName() +"Object's " +getMethdNameList.get(i);
				throw new DataBaseException(msg,e);
			} catch (InvocationTargetException e) {
				logger.error(e);
				String msg = "occur error when invoke " +obj.getClass().getSimpleName() +"Object's " +getMethdNameList.get(i);
				throw new DataBaseException(msg,e);
			}
		}
		String hql = "select new SoftwareVersion(versionId, versionNum) from SoftwareVersion where versionId in(:ids) " ;
		Map praValuesMap = new HashMap(1);
		//排序是为了同样的用户ID但顺序不一样时，统一排序以尽可能我多的用缓存
		this.sortLongList(versionIds);
		praValuesMap.put("ids", versionIds);
		if(versionIds.size()==0){
			return new HashMap<String,SoftwareVersion>(1);
		}
		List<SoftwareVersion> verList = this.findByHqlWithValuesMap(hql, praValuesMap, false);
		if(verList==null||verList.isEmpty()){
			return new HashMap<String,SoftwareVersion>(1);
		}
		Map<String,SoftwareVersion> verMap = new HashMap<String,SoftwareVersion>(verList.size());
		
		for(SoftwareVersion ver :verList){
			if(ver==null){
				continue;
			}
			verMap.put(ver.getVersionId().toString(), ver);
		}
		praValuesMap = null;
		versionIds = null;
		getMethdNameList = null;
		getMethdList = null;
		return verMap;
	}
	public Map<String, ListObject> getRelaTestTasks(List list, String... idProNames){
		
		if(list==null||list.size()==0||idProNames==null){
			return null;
		}
		List<String> getMethdNameList = new ArrayList<String>(idProNames.length);
		List<Method> getMethdList = new ArrayList<Method>(idProNames.length);
		for(String getMth :idProNames){
			getMethdNameList.add("get"+upperFirstChar(getMth));
		}
		List<String> taskIds = new ArrayList<String>();
		for(Object obj :list){
			if(getMethdList.size()==0){
				int i=0;
				try {
					for(String methName :getMethdNameList){
						getMethdList.add(obj.getClass().getMethod(methName));
						i++;
					}
				} catch (SecurityException e) {
					logger.error(e);
					throw new DataBaseException("occur error when invoke getMethod  " +getMethdNameList.get(i) ,e);
				} catch (NoSuchMethodException e) {
					logger.error(e);
					throw new DataBaseException(obj.getClass().getSimpleName() +"no " +getMethdNameList.get(i) +"method ",e);
				}
			}
			Object idValue = null;
			int i=0;
			try {
				for(Method idPorGetMethod:getMethdList){
					idValue = idPorGetMethod.invoke(obj);
					if(idValue!=null&&!"".equals(idValue)&&!taskIds.contains(idValue)){
						taskIds.add(idValue.toString());
					}	
					i++;
				}
			} catch (IllegalArgumentException e) {
				logger.error(e);
				String msg = "occur error when invoke " +obj.getClass().getSimpleName() +"Object's " +getMethdNameList.get(i);
				throw new DataBaseException(msg,e);
			} catch (IllegalAccessException e) {
				logger.error(e);
				String msg = "occur error when invoke " +obj.getClass().getSimpleName() +"Object's " +getMethdNameList.get(i);
				throw new DataBaseException(msg,e);
			} catch (InvocationTargetException e) {
				logger.error(e);
				String msg = "occur error when invoke " +obj.getClass().getSimpleName() +"Object's " +getMethdNameList.get(i);
				throw new DataBaseException(msg,e);
			}
		}
		//String taskHql = "select  new cn.com.codes.framework.common.ListObject (t.id as key,t.name as value) from Task t where t.id in(:ids) and  t.companyId= :companyId";
		String testtaskHql = "select  new cn.com.codes.framework.common.ListObject(t.taskId as key,t.proName as value ) from SingleTestTask t where t.taskId in(:ids) and t.companyId= :companyId";
		
		Map praValuesMap = new HashMap(1);
		//排序是为了同样的用户ID但顺序不一样时，统一排序以尽可能我多的用缓存
		this.sortStringList(taskIds);
		praValuesMap.put("ids", taskIds);
		praValuesMap.put("companyId", SecurityContextHolderHelp.getCompanyId());
		if(taskIds.size()==0){
			return new HashMap<String,ListObject>(1);
		}
		List<ListObject> testTaskList = this.findByHqlWithValuesMap(testtaskHql, praValuesMap, false);
		if(testTaskList==null||testTaskList.isEmpty()){
			return new HashMap<String,ListObject>(1);
		}
		Map<String,ListObject> listObjectMap = new HashMap<String,ListObject>(testTaskList.size());
		
		if(testTaskList!=null&&!testTaskList.isEmpty()){
			for(ListObject obj :testTaskList){
				if(obj==null){
					continue;
				}
				listObjectMap.put(obj.getKeyObj(), obj);
			}			
		}
		praValuesMap = null;
		taskIds = null;
		getMethdNameList = null;
		getMethdList = null;
		return listObjectMap;
	}
	
	public Map<String,User> getRelaUserWithNameByDocUserId(List list,String... DocUserIdProNames){
		if(list==null||list.size()==0||DocUserIdProNames==null||list.isEmpty()){
			return null;
		}
		List<String> getMethdNameList = new ArrayList<String>(DocUserIdProNames.length);
		List<Method> getMethdList = new ArrayList<Method>(DocUserIdProNames.length);
		for(String getMth :DocUserIdProNames){
			getMethdNameList.add("get"+upperFirstChar(getMth));
		}
		List<Long> userIds = new ArrayList<Long>();
		for(Object obj :list){
			if(getMethdList.size()==0){
				int i=0;
				try {
					for(String methName :getMethdNameList){
						getMethdList.add(obj.getClass().getMethod(methName));
						i++;
					}
				} catch (SecurityException e) {
					logger.error(e);
					throw new DataBaseException("occur error when invoke getMethod  " +getMethdNameList.get(i) ,e);
				} catch (NoSuchMethodException e) {
					logger.error(e);
					throw new DataBaseException(obj.getClass().getSimpleName() +"no " +getMethdNameList.get(i) +"method ",e);
				}
			}
			Object idValue = null;
			int i=0;
			try {
				for(Method idPorGetMethod:getMethdList){
					idValue = idPorGetMethod.invoke(obj);
					if(idValue!=null&&!userIds.contains(idValue)){
						userIds.add((Long)idValue);
					}	
					i++;
				}
			} catch (IllegalArgumentException e) {
				logger.error(e);
				String msg = "occur error when invoke " +obj.getClass().getSimpleName() +"Object's " +getMethdNameList.get(i);
				throw new DataBaseException(msg,e);
			} catch (IllegalAccessException e) {
				logger.error(e);
				String msg = "occur error when invoke " +obj.getClass().getSimpleName() +"Object's " +getMethdNameList.get(i);
				throw new DataBaseException(msg,e);
			} catch (InvocationTargetException e) {
				logger.error(e);
				String msg = "occur error when invoke " +obj.getClass().getSimpleName() +"Object's " +getMethdNameList.get(i);
				throw new DataBaseException(msg,e);
			}
		}
		String hql = "select new  User(docUserId,name ,loginName) from User where  docUserId in(:docUserIds) " ;
		Map praValuesMap = new HashMap(1);
		//排序是为了同样的用户ID但顺序不一样时，统一排序以尽可能我多的用缓存
		this.sortLongList(userIds);
		praValuesMap.put("docUserIds", userIds);
		if(userIds.size()==0){
			return  new HashMap<String,User>(1);
		}
		List<User> userList = this.findByHqlWithValuesMap(hql, praValuesMap, false);
		if(userList==null||userList.isEmpty()){
			return  new HashMap<String,User>(1);
		}
		Map<String,User> userMap = new HashMap<String,User>(userList.size());
		for(User user :userList){
			userMap.put(user.getDocUserId().toString(), user);
		}
		praValuesMap = null;
		userIds = null;
		getMethdNameList = null;
		getMethdList = null;
		return userMap;		
		
	}
	
	public void sortStringList(List<String> list){
		if(list==null||list.size()==0){
			return;
		}
		String[] ids = new String[list.size()];
		java.util.Arrays.sort(list.toArray(ids), new idComparator());
		list.clear();
		for(String id :ids){
			list.add(id);
		}
	}
	public void sortLongList(List<Long> list){
		if(list==null||list.size()==0){
			return;
		}
		Long[] ids = new Long[list.size()];
		java.util.Arrays.sort(list.toArray(ids), new idComparator());
		list.clear();
		for(Long id :ids){
			list.add(id);
		}
	}
	class idComparator implements Comparator{
	    public int compare(Object o1, Object o2) {
	        Integer key1 = o1.hashCode();
	        Integer key2 =  o2.hashCode();
	        return key1.compareTo(key2);
	    }
	}
	class TypeDefineComparator implements Comparator{
	    public int compare(Object o1, Object o2) {
	        Integer key1 = o1.hashCode();
	        Integer key2 =  o2.hashCode();
	        return key1.compareTo(key2);
	    }
	}	
	public void flush(){
		this.flush();
	}
	
	public boolean reNameChk(String objName,final String nameVal,final String namePropName,String idPropName,final String idPropVal){
		StringBuffer hql = new StringBuffer();
		hql.append("select count(").append(idPropName==null?"*":idPropName).append(")");
		hql.append(" from ").append(objName);
		hql.append(" where ").append(namePropName).append("=?");
		if(idPropName!=null&&!"".equals(idPropName.trim())&&idPropVal!=null&&!"".equals(idPropVal.trim())){
			hql.append(" and  ").append(idPropName).append("!=?");
		}
		final String countHql= hql.toString();
		List countlist = (List)this.hibernateGenericController.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)throws HibernateException {
				Query queryObject = session.createQuery(countHql).setCacheable(true);
				queryObject.setParameter(0, nameVal);
					if(idPropVal!=null&&!"".equals(idPropVal.trim())) {
						queryObject.setParameter(1, idPropVal);
					}
				return queryObject.list();
			}
		}, true);
		int count = HibernateGenericController.toLong(countlist.get(0)).intValue();
		return count>0?true:false;
	}

	public boolean reNameChkInComp(String objName,final String nameVal,final String namePropName,String idPropName,final String idPropVal){
		
		StringBuffer hql = new StringBuffer();
		hql.append("select count(").append(idPropName==null?"*":idPropName).append(")");
		hql.append(" from ").append(objName);
		hql.append(" where ").append(namePropName).append("=? and companyId=?");
		if(idPropName!=null&&!"".equals(idPropName.trim())&&idPropVal!=null&&!"".equals(idPropVal.trim())){
			hql.append(" and  ").append(idPropName).append("!=?");
		}
		final String countHql= hql.toString();
		List countlist = (List)this.hibernateGenericController.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)throws HibernateException {
				Query queryObject = session.createQuery(countHql).setCacheable(true);
				queryObject.setParameter(0, nameVal);
				queryObject.setParameter(1, SecurityContextHolderHelp.getCompanyId());
					if(idPropVal!=null&&!"".equals(idPropVal.trim())) {
						queryObject.setParameter(2, idPropVal);
					}
				return queryObject.list();
			}
		}, true);
		int count = HibernateGenericController.toLong(countlist.get(0)).intValue();
		return count>0?true:false;
	}	
	
	public boolean reNameChkInComp(String objName,final String nameVal,final String namePropName,String idPropName,final Object idPropVal,final String companyrpoName){
		
		StringBuffer hql = new StringBuffer();
		hql.append("select count(").append(idPropName==null?"*":idPropName).append(")");
		hql.append(" from ").append(objName);
		if(companyrpoName==null||"".equals(companyrpoName.trim())){
			hql.append(" where ").append(namePropName).append("=? and companyId=?");
		}else{
			hql.append(" where ").append(namePropName).append("=? and ").append(companyrpoName).append("=?");
		}
		
		if(idPropName!=null&&!"".equals(idPropName.trim())&&idPropVal!=null){
			hql.append(" and  ").append(idPropName).append("!=?");
		}
		final String countHql= hql.toString();
		List countlist = (List)this.hibernateGenericController.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)throws HibernateException {
				Query queryObject = session.createQuery(countHql).setCacheable(true);
				queryObject.setParameter(0, nameVal);
				queryObject.setParameter(1, SecurityContextHolderHelp.getCompanyId());
					if(idPropVal!=null) {
						queryObject.setParameter(2, idPropVal);
					}
				return queryObject.list();
			}
		}, true);
		int count = HibernateGenericController.toLong(countlist.get(0)).intValue();
		return count>0?true:false;
	}
	
	public List findByHqlWithValuesMap(BaseDto dto){
		int totalRows = hibernateGenericController.getResultCountWithValuesMap(dto.getHql(), dto.getHqlParamMaps(), "*", false).intValue();
		int pageNo = this.getValidPage( dto.getPageNo(), totalRows, dto.getPageSize());
		dto.setTotal(totalRows);
		if(totalRows==0){
			return new ArrayList();
		}
		return hibernateGenericController.findByHqlWithValuesMap(dto.getHql(), pageNo, dto.getPageSize(), dto.getHqlParamMaps(),false);
	}
	
	public List findBySqlWithValuesMap(BaseDto dto,Class...classz){
		int totalRows = hibernateGenericController.getResultCountWithValuesMap(dto.getHql(), dto.getHqlParamMaps(), "*", false).intValue();
		int pageNo = this.getValidPage( dto.getPageNo(), totalRows, dto.getPageSize());
		dto.setTotal(totalRows);
		if(totalRows==0){
			return new ArrayList();
		}
		if(classz!=null&&classz[0]!=null){
			return hibernateGenericController.findBySqlWithValuesMap(dto.getHql(), pageNo, dto.getPageSize(),classz[0], dto.getHqlParamMaps());
		}else{
			return hibernateGenericController.findBySqlWithValuesMap(dto.getHql(), pageNo, dto.getPageSize(),null, dto.getHqlParamMaps());
		}
		
	}
	public List findBySqlWithValuesMap(BaseDto dto,ConvertObjArrayToVo build){
		int totalRows = hibernateGenericController.getResultCountBySqlWithValuesMap(dto.getHql(), dto.getHqlParamMaps()).intValue();
		int pageNo = this.getValidPage( dto.getPageNo(), totalRows, dto.getPageSize());
		dto.setTotal(totalRows);
		if(totalRows==0){
			return new ArrayList();
		}
		List list =  hibernateGenericController.findBySqlWithValuesMap(dto.getHql(), pageNo, dto.getPageSize(),null, dto.getHqlParamMaps());
		return build.convert(list);
	}
	
	public String getMailAddrByUserIds(String userIds,String splitStr){
		if(userIds==null||"".equals(userIds.trim())){
			return null;
		}
		String hql  ="select new User(email,status) from User u where u.id in(:ids) and u.status=1 ";
		String[] idsAarray = userIds.trim().split(splitStr);
		List<String> list = null; 
		if(idsAarray.length>1){
			list = new ArrayList<String>(idsAarray.length);
			for(String id:idsAarray){
				if(!list.contains(id)){
					list.add(id);
				}
			}
			this.sortStringList(list);
		}else{
			list = new ArrayList<String>(1);
			list.add(userIds);
		}
		Map paraValMap = new HashMap(idsAarray.length);
		paraValMap.put("ids", list);
		List<User> userList = hibernateGenericController.findByHqlWithValuesMap(hql, paraValMap, false);
		StringBuffer sb = new StringBuffer("");
		if(userList==null||userList.isEmpty()){
			return "";
		}
			
			
		for(User user :userList){
			sb.append(";").append(user.getEmail());
		}
		return sb.toString().substring(1);
	}
	
	public String getMailAddrByUserIds(List<String> recpiIds){
		
		if(recpiIds==null||recpiIds.isEmpty()){
			return null;
		}
		String hql  ="select new User(email,status) from User u where u.id in(:ids) and u.status=1 ";
		this.sortStringList(recpiIds);
		Map paraValMap = new HashMap(1);
		paraValMap.put("ids", recpiIds);
		List<User> userList = hibernateGenericController.findByHqlWithValuesMap(hql, paraValMap, false);
		StringBuffer sb = new StringBuffer("");
		for(User user :userList){
			sb.append(";").append(user.getEmail());
		}
		if(sb.length()<2){
			return "";
		}
		return sb.toString().substring(1);
	}

	public JdbcTemplateWrapper getJdbcTemplateWrapper() {
		return jdbcTemplateWrapper;
	}

	public void setJdbcTemplateWrapper(JdbcTemplateWrapper jdbcTemplateWrapper) {
		this.jdbcTemplateWrapper = jdbcTemplateWrapper;
	}

}
