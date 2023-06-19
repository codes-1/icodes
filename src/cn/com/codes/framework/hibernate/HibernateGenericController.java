package cn.com.codes.framework.hibernate;

import static cn.com.codes.framework.common.LogWrap.info;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.UUIDHexGenerator;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.ReflectionUtils;

import cn.com.codes.common.util.Utilities;
import cn.com.codes.framework.common.BeanUtils;
import cn.com.codes.framework.common.ListObject;
import cn.com.codes.framework.hibernate.HibernateExpression;

@SuppressWarnings("unchecked")
public class HibernateGenericController extends HibernateDaoSupport {

	private boolean cacheQueries = false;
	
	private static Logger log = Logger.getLogger(HibernateGenericController.class);


	public void setCacheQueries(boolean cacheQueries) {
		this.cacheQueries = cacheQueries;
	}

	public boolean isCacheQueries() {
		return this.cacheQueries;
	}

	protected Criteria createCriteria(Class clazz) {

		Criteria criteria = getSession().createCriteria(clazz);
		if (isCacheQueries()) {
			criteria.setCacheable(true);
		}
		return criteria;
	}

	protected DetachedCriteria createDetachedCriteria(Class clazz) {
		return DetachedCriteria.forClass(clazz);
	}

	protected Query createQuery(String hql) throws HibernateException {
		Query query = getSession().createQuery(hql);
		if (isCacheQueries()) {
			query.setCacheable(true);
		}
		return query;
	}

	protected Query prepareQuery(Query query) {
		if (isCacheQueries()) {
			query.setCacheable(true);
		}
		return query;
	}

	public <T> T load(Class<T> clasz, Serializable id)throws HibernateException {
		return (T) getHibernateTemplate().load(clasz, id);
	}

	public <T> T load(Class<T> clasz, Serializable id, LockMode lockMode)throws HibernateException {
		return (T) getHibernateTemplate().load(clasz, id, lockMode);
	}

	public <T> T get(Class<T> clasz, Serializable id) {
		return (T) getHibernateTemplate().get(clasz, id);
	}

	public Object get(String entityName, Serializable id) {
		return getHibernateTemplate().get(entityName, id);
	}

	public String getEntityName(Object obj) {
		return getSessionFactory().getClassMetadata(obj.getClass()).getEntityName();
		// return getSession().getEntityName(obj);
	}

	public <T> T get(Class<T> clasz, Serializable id, LockMode lockMode) {
		return (T) getHibernateTemplate().get(clasz, id, lockMode);
	}

	/**
	 * 依据可变的查询条件查询数??可以接受模糊查询�????	 * @author tangfeng
	 * @param <T>
	 * @param clasz查询表对??
	 * @param propertyNames查询�??????
	 * @param object[]查询值对??
	 * @param orderStr排序??�段
	 * @param asc升序�????序布�??
	 */
	public <T> List<T> getListByPorpertyParamater(Class<T> clasz, String[] propertyNames, Object[] values, final String orderStr, final Boolean asc) {
		final DetachedCriteria deCriteria = createDetachedCriteria(clasz);
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < propertyNames.length; i++) {
			// 如果条件值为空则不加入where子句中，�??��值为""和null都默认为??
			if (values[i] != null && !values[i].toString().equalsIgnoreCase("")) {
				String str = values[i].toString();
				if (str.indexOf("%") == 0)
					deCriteria.add(Expression.like(propertyNames[i], values[i]));
				else
					deCriteria.add(Expression.eq(propertyNames[i], values[i]));
			}
		}
		
		list = (List<T>) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)throws HibernateException {
						Criteria criteria = deCriteria.getExecutableCriteria(session);
						if (!"".equals(orderStr) && orderStr != null) {
							if (asc) {
								criteria.addOrder(Order.asc(orderStr));
							} else {
								criteria.addOrder(Order.desc(orderStr));
							}
						}
						return criteria.list();
					}
				}, true);
		return list;
	}

	public void delete(Object entity) {
		getHibernateTemplate().delete(entity);
	}

	public void delete(Collection collection) {
		getHibernateTemplate().deleteAll(collection);
	}

	public <T> T merge(T entity) {
		return (T) getHibernateTemplate().merge(entity);
	}

	public void update(Object entity) {
		getHibernateTemplate().update(entity);
	}

	public void persist(Object entity) {
		getHibernateTemplate().persist(entity);
	}

	public void save(Object entity) {
		getHibernateTemplate().save(entity);
	}

	public java.io.Serializable saveEntity(Object entity) {
		return getHibernateTemplate().save(entity);
	}

	public void saveOrUpdate(Object entity) {
		getHibernateTemplate().saveOrUpdate(entity);
	}

	public <T> void deleteById(Class<T> clasz, Serializable id) {
		delete(get(clasz, id));
	}

	protected void flush() {
		getHibernateTemplate().flush();
	}

	protected void clear() {
		getHibernateTemplate().clear();
	}

	protected void refresh(Object entity, LockMode lockMode) {
		getHibernateTemplate().refresh(entity, lockMode);
	}

	protected void refresh(Object entity) {
		getHibernateTemplate().refresh(entity);
	}

	protected Query createQuery(String hql, Object... values) {
		Query query = createQuery(hql);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		return query;
	}

	protected <T> Criteria createCriteria(Class<T> clasz, Criterion... criterions) {
		Criteria criteria = createCriteria(clasz);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	public <T> DetachedCriteria createDetachedCriteria(Class<T> clasz, Criterion... criterions) {
		DetachedCriteria criteria = createDetachedCriteria(clasz);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	protected <T> DetachedCriteria getEQDetachedCriteria(Class<T> clasz, String[] propertyNames, Object[] values) {
		DetachedCriteria criteria = createDetachedCriteria(clasz);
		int i = 0;
		for (String propertyName : propertyNames) {
			criteria.add(Restrictions.eq(propertyName, values[i]));
			i++;
		}
		return criteria;
	}

	protected <T> DetachedCriteria createDetachedCriteria(Class<T> clasz, String orderBy, boolean isAsc, Criterion... criterions) {
		DetachedCriteria criteria = createDetachedCriteria(clasz, criterions);
		if (isAsc)
			criteria.addOrder(Order.asc(orderBy));
		else
			criteria.addOrder(Order.desc(orderBy));
		return criteria;
	}

	protected <T> DetachedCriteria createDetachedCriteria(Class<T> clasz, Collection<HibernateExpression> expressions) {
		List<Criterion> criterions = new ArrayList<Criterion>();
		for (HibernateExpression expression : expressions) {
			Criterion criterion = expression.createCriteria();
			if (criterion != null) {
				criterions.add(criterion);
			}
		}
		return createDetachedCriteria(clasz, criterions.toArray(new Criterion[0]));
	}

	protected <T> DetachedCriteria createDetachedCriteria(Class<T> clasz, String orderBy, boolean isAsc, Collection<HibernateExpression> expressions) {
		List<Criterion> criterions = new ArrayList<Criterion>();
		for (HibernateExpression expression : expressions) {
			Criterion criterion = expression.createCriteria();
			if (criterion != null) {
				criterions.add(criterion);
			}
		}
		return createDetachedCriteria(clasz, orderBy, isAsc, criterions.toArray(new Criterion[0]));
	}

	protected <T> Criteria createCriteria(Class<T> clasz, String orderBy, boolean isAsc, Criterion... criterions) {
		Criteria criteria = createCriteria(clasz, criterions);
		if (isAsc)
			criteria.addOrder(Order.asc(orderBy));
		else
			criteria.addOrder(Order.desc(orderBy));
		return criteria;
	}

	protected <T> Criteria createCriteria(Class<T> clasz,
			Collection<HibernateExpression> expressions) {
		List<Criterion> criterions = new ArrayList<Criterion>();
		for (HibernateExpression expression : expressions) {
			Criterion criterion = expression.createCriteria();
			if (criterion != null) {
				criterions.add(criterion);
			}
		}
		return createCriteria(clasz, criterions.toArray(new Criterion[0]));
	}

	/** 创建Criteria对�??带有HibernateExpression */
	protected <T> Criteria createCriteria(Class<T> clasz, String orderBy, boolean isAsc, Collection<HibernateExpression> expressions) {
		List<Criterion> criterions = new ArrayList<Criterion>();
		for (HibernateExpression expression : expressions) {
			Criterion criterion = expression.createCriteria();
			if (criterion != null) {
				criterions.add(criterion);
			}
		}
		return createCriteria(clasz, orderBy, isAsc, criterions.toArray(new Criterion[0]));
	}

	private Integer getUniqueNum(final DetachedCriteria criteria) {
		return (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)throws HibernateException {
				Criteria cri = criteria.getExecutableCriteria(session);
				return cri.uniqueResult();
			}
		}, true);
	}

	/** 取得??�象的主�??,辅助函数 */
	public Serializable getId(Class clasz, Object entity)throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return (Serializable) PropertyUtils.getProperty(entity, getIdName(clasz));
	}

	/** 取得??�象的主�??,辅助函数 */
	public Serializable getId(Object entity) {
		return getSession().getIdentifier(entity);
	}

	/** 取得??�象的主键名,辅助函数 */
	public String getIdName(Class clazz) {
		ClassMetadata meta = getSessionFactory().getClassMetadata(clazz);
		String idName = meta.getIdentifierPropertyName();
		return idName;
	}

	/** 去除??�象缓�??*/
	public void evict(final Object object) {
		getHibernateTemplate().evict(object);
	}

	/** 清除指定关键??�的对象缓存 */
	public void evict(final Class clasz, final Serializable id) {
		getHibernateTemplate().getSessionFactory().evict(clasz, id);
	}

	/** 清除该类的所有缓??*/
	public void evict(final Class clasz) {
		getHibernateTemplate().getSessionFactory().evict(clasz);
	}
	
	/** 移除查询缓�??*/
	public void evictQueries(String cacheRegion){
		getHibernateTemplate().getSessionFactory().evictQueries(cacheRegion);
	}
	
	public void evictQueries(){
		getHibernateTemplate().getSessionFactory().evictQueries();
	}

	/** 清除指定集合缓存 */
	public void evictCollection(final String collectionName, final Serializable id) {
		getHibernateTemplate().getSessionFactory().evictCollection(collectionName, id);
	}

	/** 清除集合缓�??*/
	public void evictCollection(final String collectionName) {
		getHibernateTemplate().getSessionFactory().evictCollection(collectionName);
	}

	/** 去除hql的select 子句，未考虑union的情�?用于页查??*/
	private static String removeSelect(String hql) {
		int beginPos = hql.toLowerCase().indexOf("from");
		return hql.substring(beginPos);
	}

	/** 去除hql的orderby 子�??用于页查?? */
	private static String removeOrders(String hql) {
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/** 获取任一页第�?��数据在数�????的位�?????��的�????每页记录条�??return 该页第一条数�?*/
	private static int getStartOfPage(int pageNo, int pageSize) {
		return (pageNo - 1) * pageSize;
	}

	/** 为Hibernate3.1之前版本做兼???*/
	public static Long toLong(Object obj) {
		if (obj instanceof Long) {
			return (Long) obj;
		} else if (obj instanceof Integer) {
			Integer i = (Integer) obj;
			return i.longValue();
		} else {
			return 0L;
		}
	}
	
	/** 执行HQL数据库语??*/
	public Integer executeUpdate(final String hql, final Object... objects) {
		return (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)throws HibernateException {
				Query query = session.createQuery(hql);
				if (objects != null) {
					for (int i = 0; i < objects.length; i++) {
						query.setParameter(i, objects[i]);
					}
				}
				return query.executeUpdate();
			}
		}, true);
	}
	
	/** 执行HQL数据库语??*/
	public Integer executeUpdateByHql(final String hql, final Object[] objects) {
		return (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				info(log, hql);
				Query query = session.createQuery(hql);
				if (objects != null) {
					for (int i = 0; i < objects.length; i++) {
						query.setParameter(i, objects[i]);
					}
				}
				return query.executeUpdate();
			}
		}, true);
	}
	
	/** 执行HQL数据库语??praValuesMap */
	public Integer executeUpdateByHqlWithValuesMap(final String hql, final Map<String, Object> praValuesMap) {
		return (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.createQuery(hql);
				if (!praValuesMap.isEmpty()) {
					Iterator it = praValuesMap.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<String, Object> me = (Map.Entry<String, Object>) it.next();
						if(me.getValue() instanceof List)
							queryObject.setParameterList(me.getKey(), (List)me.getValue());
						else
							queryObject.setParameter(me.getKey(), me.getValue());	
					}
				}
				return queryObject.executeUpdate();
			}
		}, true);
	}

	/** 批量运行的ＨＱＬ语�??praValuesList 包含参数及其值的list 批量处理时就用这个list循环的方式来??�现批�??*/
	public boolean excuteBatchHql(final String hql, final List<Map<String, Object>> praValuesList){
		getHibernateTemplate().executeWithNativeSession(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException {
				if(praValuesList != null &&  praValuesList.size() > 0){
					for(Map<String, Object> praValuesMap : praValuesList){
						Query queryObject = session.createQuery(hql);
						prepareQuery(queryObject);
						if (!praValuesMap.isEmpty()) {
							Iterator it = praValuesMap.entrySet().iterator();
							while (it.hasNext()) {
								Map.Entry<String, Object> me = (Map.Entry<String, Object>) it.next();
								if(me.getValue() instanceof List)
									queryObject.setParameterList(me.getKey(), (List)me.getValue());
								else
									queryObject.setParameter(me.getKey(), me.getValue());				
							}
						}
						queryObject.executeUpdate();
					}
				}
				return null;
			}
		});
		return true;
	}
	
	/** 批量运行的ＨＱＬ语�??praValuesList 包含参数及其值的list 批量处理时就用这个list循环的方式来??�现批�??*/
	public boolean excuteBatchHql(final String hql, final Map<String, Object> praValuesMap){
		getHibernateTemplate().executeWithNativeSession(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException {
						Query queryObject = session.createQuery(hql);
						prepareQuery(queryObject);
						if (!praValuesMap.isEmpty()) {
							Iterator it = praValuesMap.entrySet().iterator();
							while (it.hasNext()) {
								Map.Entry<String, Object> me = (Map.Entry<String, Object>) it.next();
								if(me.getValue() instanceof List)
									queryObject.setParameterList(me.getKey(), (List)me.getValue());
								else
									queryObject.setParameter(me.getKey(), me.getValue());				
							}
						}
						queryObject.executeUpdate();
				return null;
			}
		});
		return true;
	}
	
	/** 批量保存对�??*/
	public void batchSaveOrUpdate(final List<Object> objects) {
		if(Utilities.isNullOrEmpty(objects))
			return;
		getHibernateTemplate().executeWithNativeSession(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException {
				Iterator it = objects.iterator();
				while(it.hasNext()){
					Object object = it.next();
					if(object != null)
						session.saveOrUpdate(object);
				}
				return null;
			}
		});
	}

	public HibernateTemplate getTemplate() {
		return this.getHibernateTemplate();
	}

	protected void prepareCriteria(Criteria ct) {
		if (isCacheQueries()) {
			ct.setCacheable(true);
		}
	}

	public Integer callProcedure(final String callProcedStr, final Object... obj) {
		return (Integer) getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Connection con = session.connection();
					try {
						CallableStatement cstmt = con.prepareCall("{CALL " + callProcedStr + "}");
						if(obj != null && obj.length > 0){
							for(int i = 0 ; i < obj.length ; i++){
								cstmt.setObject(i+1, obj[i]);
							}
						}
						cstmt.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
						return new Integer(0);
					}
					return new Integer(1);
				}
			}
		);
	}
	
	public List<String> callProcedureReturnList(final String callProcedStr, final Object... obj) {
		return (List<String>) getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				List idList = new ArrayList();
				ListObject listObject = null;
				Connection con = session.connection();
					try {
						CallableStatement cstmt = con.prepareCall("{CALL " + callProcedStr + "}");
						cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR); 
						if(obj != null && obj.length > 0){
							for(int i = 0 ; i < obj.length ; i++){
								cstmt.setObject(i+2, obj[i]);
							}
						}
						cstmt.execute();
						ResultSet rs = (ResultSet)cstmt.getObject(1); 
						while (rs.next()) {
							idList.add(rs.getString(1));
						}
					} catch (SQLException e) {
						e.printStackTrace();
						return idList;
					}
					return idList;
				}
			}
		);
	}
	
	public String getUuId(){
		Properties props = new Properties();
		props.setProperty("separator", "");
		IdentifierGenerator gen = new UUIDHexGenerator();
		((Configurable)gen).configure(Hibernate.STRING, props, null);
		return (String)gen.generate(null, null);
	}
	
	
	public <T> List<T> getAll(Class<T> clasz) {
		return getHibernateTemplate().loadAll(clasz);
	}

	public <T> List<T> getAll(Class<T> clasz, String orderBy, boolean isAsc) {
		if (isAsc)
			return getHibernateTemplate().findByCriteria(DetachedCriteria.forClass(clasz).addOrder(Order.asc(orderBy)));
		else
			return getHibernateTemplate().findByCriteria(DetachedCriteria.forClass(clasz).addOrder(Order.desc(orderBy)));
	}
	
	/**  根据hql查�??可变参数 */
	public List findByFreePara(String hql, Object... values) {
		return getHibernateTemplate().find(hql, values);
	}
	
	/** executeWithNativeSession */
	public List findByHqlNoCache(final String hql, final Object... values) {
		return (List) getHibernateTemplate().executeWithNewSession(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Query query = session.createQuery(hql);
				query.setCacheable(true);
				query.setCacheMode(org.hibernate.CacheMode.REFRESH);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				return query.list();
			}

		});
	}
	
	/** 根据�??名和�??值查询对??*/
	public <T> List<T> findBy(Class<T> clasz, String propertyName, Object value) {
		return getHibernateTemplate().findByCriteria(createDetachedCriteria(clasz, Restrictions.eq(propertyName, value)));
	}

	public <T> List<T> findByProperties(Class<T> clasz, String[] propertyNames, Object[] values) {
		DetachedCriteria criteria = this.getEQDetachedCriteria(clasz, propertyNames, values);
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/** 根据�??名和�??值查询对??带排序参??*/
	public <T> List<T> findBy(Class<T> clasz, String propertyName, Object value, String orderBy, boolean isAsc) {
		return getHibernateTemplate().findByCriteria(createDetachedCriteria(clasz, orderBy, isAsc, Restrictions.eq(propertyName, value)));
	}

	/**  根据�??名和�??值查询唯??���?*/
	public <T> T findUniqueBy(final Class<T> clasz, final String propertyName, final Object value) {

		return (T) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
			
				Criteria criteria = session.createCriteria(clasz);
				criteria.add(Restrictions.eq(propertyName, value));
				return criteria.uniqueResult();
			}
		}, true);
	}

	/** 获得记录总数，如果没配二级缓存，不要调用??�，不配�????缓存时调?? getResultCountNocache */
	public Long getResultCount(String hql, final Object[] values, String columnNameForCount) {
		final String countQueryString = " select count(" + columnNameForCount + ") " + removeSelect(removeOrders(hql));
		List countlist = (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)throws HibernateException {
				Query queryObject = session.createQuery(countQueryString).setCacheable(true);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i, values[i]);
					}
				}
				return queryObject.list();
			}
		}, true);
		return toLong(countlist.get(0));
	}

	public Long getRefshResultCount(String hql, final Object[] values, String columnNameForCount) {
		final String countQueryString = " select count(" + columnNameForCount + ") " + removeSelect(removeOrders(hql));
		List countlist = (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.createQuery(countQueryString);
				queryObject.setCacheable(true);
				queryObject.setCacheMode(org.hibernate.CacheMode.REFRESH);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i, values[i]);
					}
				}
				return queryObject.list();
			}
		}, true);
		return toLong(countlist.get(0));
	}

	
	public Long getResultCountBySql(String sql, String columnNameForCount,final Object ...values) {
		if (columnNameForCount != null && !"".equals(columnNameForCount)) {
			final String countQueryString = " select count(" + columnNameForCount + ") " + removeSelect(removeOrders(sql));
			List countlist = (List) getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException {
					Query queryObject = session.createSQLQuery(countQueryString);
					if (values != null) {
						for (int i = 0; i < values.length; i++) {
							queryObject.setParameter(i, values[i]);
						}
					}
					return queryObject.list();
				}
			}, true);
			return new Long(countlist.size() == 0 ? "0" : countlist.get(0).toString());
		} else {
			final String countQueryString = " select count(*) " + removeSelect(removeOrders(sql));
			List countlist = (List) getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException {
					Query queryObject = session.createSQLQuery(countQueryString);
					if (values != null) {
						for (int i = 0; i < values.length; i++) {
							queryObject.setParameter(i, values[i]);
						}
					}
					return queryObject.list();
				}
			}, true);
			return new Long(countlist.size() == 0 ? "0" : countlist.get(0).toString());
		}
	}
	
	public Long getResultCountBySqlWithValuesMap(final String sql, final Map<String, Object> praValuesMap) {
		final String countQueryString = " select count(*) " + removeSelect(removeOrders(sql));
		List countlist = (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.createSQLQuery(countQueryString);
				if (!praValuesMap.isEmpty()) {
					Iterator it = praValuesMap.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<String, Object> me = (Map.Entry<String, Object>) it.next();
						if(me.getValue() instanceof List)
							queryObject.setParameterList(me.getKey(), (List)me.getValue());
						else
							queryObject.setParameter(me.getKey(), me.getValue());				
					}
				}
				return queryObject.list();
			}
		}, true);
		return new Long(countlist.size() == 0 ? "0" : countlist.get(0).toString());
	}

	public Long getResultCount(DetachedCriteria dcriteria) {
		Criteria criteria = dcriteria.getExecutableCriteria(this.getSession());
		this.prepareCriteria(criteria);
		return getResultCount(criteria);
	}

	/** 获得记录总�??*/
	public Long getResultCount(Criteria criteria) {
		CriteriaImpl impl = (CriteriaImpl) criteria;
		Long totalCount = 0L;
		try {
			// 先把Projection和OrderBy�??��取出??清空�??来执行Count操�??			
			Projection projection = impl.getProjection();
			List<CriteriaImpl.OrderEntry> orderEntries;
			try {
				orderEntries = (List) BeanUtils.forceGetProperty(impl, "orderEntries");
				BeanUtils.forceSetProperty(impl, "orderEntries", new ArrayList());
			} catch (Exception e) {
				throw new InternalError(" Runtime Exception impossibility throw ");
			}
			// 执行查�??			
			totalCount = toLong(criteria.setProjection(Projections.rowCount()).uniqueResult());
			// 将之前的Projection和OrderBy�??��重新设回�?
			criteria.setProjection(projection);
			if (projection == null) {
				criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
			}

			try {
				BeanUtils.forceSetProperty(impl, "orderEntries", orderEntries);
			} catch (Exception e) {
				throw new InternalError(" Runtime Exception impossibility throw ");
			}
		} finally {
			SessionFactoryUtils.closeSession((Session) impl.getSession());
		}
		return totalCount;
	}

	/** 获得记录总�??*/
	public Long getResultCount(Class clasz, Criterion... criterions) {
		Criteria criteria = createCriteria(clasz, criterions);
		return getResultCount(criteria);
	}

	/** 获得记录总�??*/
	public Long getResultCount(Class clasz, Collection<HibernateExpression> expressions) {
		Criteria criteria = createCriteria(clasz);
		for (HibernateExpression expression : expressions) {
			Criterion criterion = expression.createCriteria();
			if (criterion != null) {
				criteria.add(criterion);
			}
		}
		return getResultCount(criteria);
	}


	public List findBySql(final String sql, final Class clasz ,final Object ...values) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)throws HibernateException {
				Query queryObject = null;
				if (clasz == null) {
					queryObject = session.createSQLQuery(sql);
				} else {
					queryObject = session.createSQLQuery(sql).addEntity(clasz);
				}
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i, values[i]);
					}
				}
				return queryObject.list();
			}
		}, true);
	}
	
	public List findBySql(final String sql) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)throws HibernateException {
				Query queryObject = session.createSQLQuery(sql);
				return queryObject.list();
			}
		}, true);
	}
	
	public List<Map<String,Object>> findBySqlByJDBC(final String sql) {
		return (List<Map<String,Object>>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)throws HibernateException {
				Connection con = session.connection();
				List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
				try {
					PreparedStatement pstmt = con.prepareStatement(sql);
					Query queryObject = session.createSQLQuery(sql);
					 ResultSet resultSet = null;
					 resultSet = pstmt.executeQuery();
					 ResultSetMetaData rsmd =  resultSet.getMetaData();
					 int columnCount = rsmd.getColumnCount();
					 String[] columnNames = null;
					 columnNames = new String[columnCount];
					 for (int i = 0; i < columnCount; i++) {
							columnNames[i] = rsmd.getColumnLabel(i + 1);
					 }

					  while(resultSet.next()) {
						 HashMap<String, Object> map  = new HashMap<String, Object>();
						 for (String columnName : columnNames) {
							 map.put(columnName, resultSet.getObject(columnName));
						 }
						 list.add(map);
					 }
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
				    
				return list;
			}
		}, true);
	}
	

	
	 String convertColumnName2OFieldName(String columnName ) {
		 
		columnName = columnName.toLowerCase();
		StringBuffer buf = new StringBuffer();
		int i = 0;
		while ((i = columnName.indexOf('_')) > 0) {
			buf.append(columnName.substring(0, i));
			columnName = StringUtils.capitalize(columnName.substring(i + 1));
			
		}
		buf.append(columnName);
		return  buf.toString();
	 }
	public List<Map<String,Object>> commonfindBySqlByJDBC(final String sql,final boolean proConvert,final HashMap praValuesMap) {
		return (List<Map<String,Object>>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)throws HibernateException {
				Connection con = session.connection();
				List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
				try {
					PreparedStatement pstmt = con.prepareStatement(sql);
					Query queryObject = session.createSQLQuery(sql);
					if (!praValuesMap.isEmpty()) {
						Iterator it = praValuesMap.entrySet().iterator();
						while (it.hasNext()) {
							Map.Entry<String, Object> me = (Map.Entry<String, Object>) it.next();
							if(me.getValue() instanceof List)
								queryObject.setParameterList(me.getKey(), (List)me.getValue());
							else
								queryObject.setParameter(me.getKey(), me.getValue());				
						}
					}
					 ResultSet resultSet = null;
					 resultSet = pstmt.executeQuery();
					 ResultSetMetaData rsmd =  resultSet.getMetaData();
					 int columnCount = rsmd.getColumnCount();
					 String[] columnNames = null;
					 columnNames = new String[columnCount];
					 for (int i = 0; i < columnCount; i++) {
							columnNames[i] = rsmd.getColumnLabel(i + 1);
					 } 
					  while(resultSet.next()) {
						  
						 HashMap<String, Object> map  = new HashMap<String, Object>();
						 for (String columnName : columnNames) {
							map.put(columnName, resultSet.getObject(columnName));
						 }
						 list.add(map);
					 }
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
				if(proConvert) {
					converDbColumnName2ObjectPropName(list);
				}
				return list;
			}
		}, true);
	}
	
	/**
	 * 把list map 原生JDBC结果集中，字段名，也就是MAP中的KEY，转换为驼峰规则的JAVA对属性名
	 * 
	 * @param resultList ：JDBC 结果集
	 * @return    把MAP中的KEY转换为转换为驼峰规则的JAVA对属性名的LIST<map<驼峰规则的JAVA对属性名形式的KEY,Object>>
	 * @author liuyg
	 */
	public void converDbColumnName2ObjectPropName(List<Map<String,Object>> resultList) {
		
		if(resultList!=null&&!resultList.isEmpty()) {
			List<Map<String,Object>> convertList=  new ArrayList<Map<String,Object>>(resultList.size());
			 //用于缓存字段名到属性名的映射，第二条记录时就不再处理字段名到属性名的转换，提升性能
			Map<String,String> ColumnNamePropNameMap = null;
			if(resultList.size()>1) {
				ColumnNamePropNameMap = new HashMap<String,String>();
			}
			for(Map<String,Object> currMap :resultList) {
				 if(currMap!=null&&!currMap.isEmpty()) {
					 Iterator<Entry<String, Object>>   currentIt =  currMap.entrySet().iterator();
					 Map tempMap = new HashMap<String,Object>();
					 convertList.add(tempMap);
					 while(currentIt.hasNext()) {
						 Map.Entry<String,Object>  me=  currentIt.next();
						 String dbColumnName = me.getKey();
						 Object value = me.getValue();
						 if(resultList.size()>1) {
							 if(ColumnNamePropNameMap.get(dbColumnName)==null) {
								 String currProName = convertColumnName2OFieldName(dbColumnName);
								 tempMap.put(currProName, value);
								 //缓存起来，第二条记录时就不再处理字段名到属性名的转换，提升性能
								 ColumnNamePropNameMap.put(dbColumnName, currProName);
							 }else {
								 tempMap.put(ColumnNamePropNameMap.get(dbColumnName), value);
							 }
						}else {
							 tempMap.put(convertColumnName2OFieldName(dbColumnName), value);
						}

						 
					 }
				 }
			}
			resultList.clear();
			for(Map<String,Object> currMap:convertList) {
				resultList.add(currMap);
			}
			convertList.clear();
			convertList = null;

		}
	}
	public List fetchUc() {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)throws HibernateException {
				List<Object> coll = new ArrayList<Object>();
				Connection con = session.connection();
				try {
					PreparedStatement stmt = con.prepareStatement("SELECT count(*) FROM t_user t where t.status=1");
					ResultSet rs = stmt.executeQuery(); 
					while (rs.next()) {
						coll.add(rs.getObject(1));
					}
				} catch (SQLException e) {
				}
				return coll;
			}
		}, true);
	}
	public List findBy(final String query, final int returnedColumns, final Object ...values) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)throws HibernateException {
				List<Object> coll = new ArrayList<Object>();
				Connection con = session.connection();
				try {
					PreparedStatement stmt = con.prepareStatement(query);
					if (values != null && values.length != 0) {
						for (int i = 0; i < values.length; i++) {
							stmt.setObject(i + 1, values[i]);
						}
					}
					ResultSet rs = stmt.executeQuery(); 
					while (rs.next()) {
						if (returnedColumns == 1) {
							coll.add(rs.getObject(1));
						} else {
							Object[] entry = new Object[returnedColumns];
							for (int i = 1; i <= returnedColumns; i++) {
								entry[i - 1] = rs.getObject(i);
							}
							coll.add(entry);
						}
					}
				} catch (SQLException e) {
				}
				return coll;
			}
		}, true);
	}

	/**
	 * 
	 * @param query
	 * @param returnedColumns
	 * @param values
	 * @return
	 * @deprecated
	 */
//	public Object sqlSave(final String insertSql) {
//		  return getHibernateTemplate().execute(new HibernateCallback() {
//			public Object doInHibernate(Session session)throws HibernateException {
//				List<Object> coll = new ArrayList<Object>();
//				Connection con = session.connection();
//				try {
//					Statement st = con.createStatement();
//					if(insertSql!=null){
//						st.execute(insertSql);
//						con.commit();
//					}else{
//						st.execute("insert into T_SYSLOG(LOG_ID,OPER_DESC,LOG_TYPE,OPER_SUMMARY,ACCESS_IP,OPER_ID)values('"+(new Date()).getTime()+"','rept',1,'rept','1289630248984','1289630248984')");
//						con.commit();
//					}
//				} catch (SQLException e) {
//					Context.reloadCacheData();
//				}
//				return null;
//			}
//		}, true);
//	}
	public List findBySqlWithValuesMap(final String sql, final Class clasz ,final Map<String, Object> praValuesMap) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)throws HibernateException {
				Query queryObject = null;
				if (clasz == null) {
					queryObject = session.createSQLQuery(sql);
				} else {
					queryObject = session.createSQLQuery(sql).addEntity(clasz);
				}
				if (!praValuesMap.isEmpty()) {
					Iterator it = praValuesMap.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<String, Object> me = (Map.Entry<String, Object>) it.next();
						if(me.getValue() instanceof List){
							queryObject.setParameterList(me.getKey(), (List)me.getValue());
						}else{
							queryObject.setParameter(me.getKey(), me.getValue());
						}
					}
				}
				return queryObject.list();
			}
		}, true);
	}

    //该方法主要是为了加强第三方依赖,别删除也不要调用
	public List findBySqlWithValuesMap(final String sql, final int pageNo, final int pageSize ,final Map<String, Object> praValuesMap) {

		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			
			public Object doInHibernate(Session session)throws HibernateException {
				Query	queryObject = session.createSQLQuery(sql);
				if (!praValuesMap.isEmpty()) {
					Iterator it = praValuesMap.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<String, Object> me = (Map.Entry<String, Object>) it.next();
						if(me.getValue() instanceof List){
							queryObject.setParameterList(me.getKey(), (List)me.getValue());
						}else{
							queryObject.setParameter(me.getKey(), me.getValue());
						}
					}
				}
				int startIndex = getStartOfPage(pageNo, pageSize);
				return queryObject.setFirstResult(startIndex).setMaxResults(pageSize).list();
			}
		}, true);
	}
	
	public List findBySqlWithValuesMap(final String sql, final int pageNo, final int pageSize,final Class clasz ,final Map<String, Object> praValuesMap) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)throws HibernateException {
				Query queryObject = null;
				if (clasz == null) {
					queryObject = session.createSQLQuery(sql);
				} else {
					queryObject = session.createSQLQuery(sql).addEntity(clasz);
				}
				if (!praValuesMap.isEmpty()) {
					Iterator it = praValuesMap.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<String, Object> me = (Map.Entry<String, Object>) it.next();
						if(me.getValue() instanceof List){
							queryObject.setParameterList(me.getKey(), (List)me.getValue());
						}else{
							queryObject.setParameter(me.getKey(), me.getValue());
						}
					}
				}
				int startIndex = getStartOfPage(pageNo, pageSize);
				return queryObject.setFirstResult(startIndex).setMaxResults(pageSize).list();
			}
		}, true);
	}	
	public List findBySqlConn(final String sql, final List<String> praValuesList, final int colCount) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)throws HibernateException, SQLException {
				List lists = new ArrayList();
				Connection conn = session.connection();  
				PreparedStatement ps = conn.prepareStatement(sql);  
				int j = 1;
				if (praValuesList != null && praValuesList.size() >0) {
					Iterator it = praValuesList.iterator();
					while (it.hasNext()) {
						ps.setString(j, it.next().toString());
						j++;
					}
				}
				java.sql.ResultSet rs = ps.executeQuery();
				while(rs.next()){
					List rowLists = new ArrayList();
					for(int i = 1; i <= colCount; i++){
						if(i == 4 || i == 5 || i == 12 || i == 13){
							rowLists.add(rs.getDate(i));
						}else{
							rowLists.add(rs.getObject(i));
						}
					}
					lists.add(rowLists);
				}
				ps.close(); 
				return lists;
			}
		}, true);
	}
	
	/** 分页查询 */
	public List findBy(DetachedCriteria criteria, int pageNo, int pageSize) {
		int startIndex = getStartOfPage(pageNo, pageSize);
		return getHibernateTemplate().findByCriteria(criteria, startIndex, pageSize);
		// return
		// criteria.setFirstResult(startIndex).setMaxResults(pageSize).list();
	}

	/** 按起始记录查�?起始记录??返回记录??*/
	public List findByWithStart(DetachedCriteria criteria, int startIndex, int endIndex) {
		return getHibernateTemplate().findByCriteria(criteria, startIndex, endIndex);
	}

	/** 分页查询 */
	public List findBy(Class clasz, int pageNo, int pageSize, Criterion... criterions) {
		DetachedCriteria criteria = createDetachedCriteria(clasz, criterions);
		int startIndex = getStartOfPage(pageNo, pageSize);
		return getHibernateTemplate().findByCriteria(criteria, startIndex, pageSize);
	}

	/** 分页查询,带有HibernateExpression */
	public List findBy(Class clasz, int pageNo, int pageSize, Collection<HibernateExpression> expressions) {
		DetachedCriteria criteria = createDetachedCriteria(clasz, expressions);
		int startIndex = getStartOfPage(pageNo, pageSize);
		return getHibernateTemplate().findByCriteria(criteria, startIndex, pageSize);
	}

	/** 按起始记录查�?带有HibernateExpression */
	public List findByWithStart(Class clasz, int startIndex, int rowSize, Collection<HibernateExpression> expressions) {
		DetachedCriteria criteria = createDetachedCriteria(clasz, expressions);
		return findByWithStart(criteria, startIndex, rowSize);
	}

	/** 带排序功??分页查询,带有HibernateExpression */
	public List findBy(Class clasz, int pageNo, int pageSize, String orderBy, boolean isAsc, Collection<HibernateExpression> expressions) {
		DetachedCriteria criteria = createDetachedCriteria(clasz, orderBy, isAsc, expressions);
		return findBy(criteria, pageNo, pageSize);
	}

	/** 分页查询 */
	public List findBy(Class clasz, int pageNo, int pageSize, String orderBy, boolean isAsc, Criterion... criterions) {
		DetachedCriteria criteria = createDetachedCriteria(clasz, orderBy, isAsc, criterions);
		return findBy(criteria, pageNo, pageSize);
	}

	/** 判断??�象某些�??的�?在数据库中是�??????在POJO里不能重复的�??列�??以�?号分�???name,loginid,password" */
	public <T> boolean isUnique(Class<T> clasz, Object entity, String uniquePropertyNames) {
		DetachedCriteria criteria = createDetachedCriteria(clasz).setProjection(Projections.rowCount());
		String[] nameList = uniquePropertyNames.split(",");
		try {
			// 循环加入唯一�?
			for (String name : nameList) {
				criteria.add(Restrictions.eq(name, PropertyUtils.getProperty(entity, name)));
			}
			// 以下代码为了如果是update的情�?排除entity自�??
			// 取得entity的主�??
			Serializable id = getId(clasz, entity);
			// 如果id!=null,说明??�象已存�?该操�????update,加入排除自身的判??
			if (id != null) {
				String idName = getIdName(clasz);
				criteria.add(Restrictions.not(Restrictions.eq(idName, id)));
			}
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
		}
		Integer resultCount = getUniqueNum(criteria);
		// Integer resultCount = (Integer)criteria.uniqueResult();
		return resultCount.intValue() == 0;
	}
	
	
	public List findByDetachedCriteria(DetachedCriteria criteria) {
		Criteria ct = criteria.getExecutableCriteria(getSession());
		this.prepareCriteria(ct);
		return ct.list();
	}

	public List findByCriteria(Criteria criteria) {
		this.prepareCriteria(criteria);
		return criteria.list();
	}
	
	public List findByDetachedCriteria(DetachedCriteria criteria, int pageNo, int pageSize) {
		int startIndex = getStartOfPage(pageNo, pageSize);
		Criteria ct = criteria.getExecutableCriteria(getSession());
		this.prepareCriteria(ct);
		return ct.setFirstResult(startIndex).setMaxResults(pageSize).list();
	}

	public List findByCriteria(Criteria criteria, int pageNo, int pageSize) {
		int startIndex = getStartOfPage(pageNo, pageSize);
		this.prepareCriteria(criteria);
		return criteria.setFirstResult(startIndex).setMaxResults(pageSize).list();
	}
	

	
	/** REFRESH�????*/
	public List findByHqlRefresh(final String hql, final Object... values) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)throws HibernateException {
				Query query = session.createQuery(hql);
				query.setCacheable(true);
				query.setCacheMode(org.hibernate.CacheMode.REFRESH);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				return query.list();
			}
		}, true);
	}
	
	public List findByHqlWithReshFlg(final String hql, final Boolean isRefresh,final Object[] values) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);
				query.setCacheable(true);
				if(isRefresh!=null&&isRefresh){
					query.setCacheMode(org.hibernate.CacheMode.REFRESH);
				}
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				return query.list();
			}
		}, true);
	}
	/** hql分页查询 */
	public List findByHql(final String hql, final int pageNo, final int pageSize, final Object... values) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);
				query.setCacheable(true);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				int startIndex = getStartOfPage(pageNo, pageSize);
				return query.setFirstResult(startIndex).setMaxResults(pageSize).list();
			}
		}, true);
	}
	/** hql分页查询 */
	public List findByHqlWhitRowIndex(final String hql, final int rowIndex, final int pageSize, final Object... values) {
		//System.out.println("==============findByHqlWhitRowIndex===");
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);
				query.setCacheable(true);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				return query.setFirstResult(rowIndex).setMaxResults(pageSize).list();
			}
		}, true);
	}
	public List findByNoCache(final String hql, final Object... values) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);
				query.setCacheable(false);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				return query.list();
			}
		}, true);
	}
	
	
	public List findByHql(final String hql, final Object[] values) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);
				query.setCacheable(true);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				return query.list();
			}
		}, true);
	}
	
	/** 查记录数用REFRESH�????*/
	public Long getResultCountByRefresh(String hql, final Object[] values, String columnNameForCount) {
		final String countQueryString = " select count(" + columnNameForCount + ") " + removeSelect(removeOrders(hql));
		List countlist = (List) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException {
						Query queryObject = session.createQuery(countQueryString);
						queryObject.setCacheable(true);
						queryObject.setCacheMode(org.hibernate.CacheMode.REFRESH);
						if (values != null) {
							for (int i = 0; i < values.length; i++) {
								queryObject.setParameter(i, values[i]);
							}
						}
						return queryObject.list();
					}
				}, true);

		return toLong(countlist.get(0));
	}
	
	public List findByHqlWithPraValues(final String hql, final Object[] values) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)throws HibernateException {
				Query queryObject = session.createQuery(hql);
				prepareQuery(queryObject);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i, values[i]);
					}
				}
				return queryObject.list();
			}
		}, true);
	}

	public List findByHqlWithPraValues(final String hql, final int pageNo, final int pageSize, final Object[] values) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.createQuery(hql);
				prepareQuery(queryObject);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i,values[i]);
					}
				}
				int startIndex = getStartOfPage(pageNo, pageSize);
				return queryObject.setFirstResult(startIndex).setMaxResults(pageSize).list();
			}
		}, true);
	}
	
	public Long getResultCountWithValuesMap(String hql, final Map<String, Object> praValuesMap, String columnNameForCount, final boolean isRefresh) {
		final String countQueryString = " select count(" + columnNameForCount + ") " + removeSelect(removeOrders(hql));
		List countlist = (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)throws HibernateException {
				Query query = session.createQuery(countQueryString);
				query.setCacheable(true);
				if(isRefresh)
					query.setCacheMode(org.hibernate.CacheMode.REFRESH);
				if (praValuesMap!=null&&!praValuesMap.isEmpty()) {
					Iterator it = praValuesMap.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<String, Object> me = (Map.Entry<String, Object>) it.next();
						if(me.getValue() instanceof List){
							query.setParameterList(me.getKey(), (List)me.getValue());
						}else{
							query.setParameter(me.getKey(), me.getValue());
						}
						
					}
				}
				return query.list();
			}
		}, true);
		return toLong(countlist.get(0));
	}
	
	public List findByHqlWithValuesMap(final String hql, final Map<String, Object> praValuesMap, final boolean isRefresh) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);
				query.setCacheable(true);
				if(isRefresh)
					query.setCacheMode(org.hibernate.CacheMode.REFRESH);
				if (praValuesMap!=null&&!praValuesMap.isEmpty()) {
					Iterator it = praValuesMap.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<String, Object> me = (Map.Entry<String, Object>) it.next();
						if(me.getValue() instanceof List){
							query.setParameterList(me.getKey(), (List)me.getValue());
						}else{
							query.setParameter(me.getKey(), me.getValue());
						}
					}
				}
				return query.list();
			}
		}, true);
	}
	static public class  cacheSwitch{
		public static void enableCache(){
			cacheAble = true;
			//System.out.println("===============cacheSwitch=================");
		}
		public static void disableCache(){
			cacheAble = false;
		}
	}
	public List findByHqlWithValuesMap(final String hql, final int pageNo, final int pageSize, final Map<String, Object> praValuesMap, final boolean isRefresh) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);
				query.setCacheable(true);
				if(isRefresh)
					query.setCacheMode(org.hibernate.CacheMode.REFRESH);
				if (praValuesMap!=null&&!praValuesMap.isEmpty()) {
					Iterator it = praValuesMap.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<String, Object> me = (Map.Entry<String, Object>) it.next();
						if(me.getValue() instanceof List){
							query.setParameterList(me.getKey(), (List)me.getValue());
						}else{
							query.setParameter(me.getKey(), me.getValue());
						}
					}
				}
				int startIndex = getStartOfPage(pageNo, pageSize);
				return query.setFirstResult(startIndex).setMaxResults(pageSize).list();
			}
		}, true);
	}
	private static Boolean cacheAble = false;
	public Object findMaxMinByHqlWithValuesMap(final String hql, final Map<String, Object> praValuesMap, final boolean isRefresh) {
		return (Object)getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);
				query.setCacheable(true);
				if(isRefresh)
					query.setCacheMode(org.hibernate.CacheMode.REFRESH);
				if (praValuesMap!=null&&!praValuesMap.isEmpty()) {
					Iterator it = praValuesMap.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<String, Object> me = (Map.Entry<String, Object>) it.next();
						if(me.getValue() instanceof List){
							query.setParameterList(me.getKey(), (List)me.getValue());
						}else{
							query.setParameter(me.getKey(), me.getValue());
						}
					}
				}
				List list = query.setFirstResult(0).setMaxResults(1).list();
				if(list != null && list.size() > 0)
					return list.get(0);
				else
					return null;
			} 
		}, true);
	}

	private static String removeSqlSelect(String sql) {
		int beginPos = sql.indexOf(" from ");
		if (beginPos < 0) {
			beginPos = sql.indexOf("from ");
		}
		if (beginPos < 0) {
			beginPos = sql.toLowerCase().indexOf(" from ");
		}
		if (beginPos < 0) {
			beginPos = sql.toLowerCase().indexOf("from ");
		}
		return sql.substring(beginPos);
	}


	public static boolean reloadCache() {
		//NamedParameterJdbcTemplate dd;
		//RowMapper d;
		return cacheAble;
		
	}
//	static {
//		org.apache.commons.logging.impl.Log4JLogger.cacheSwitch.enableCache();
//	}
}
