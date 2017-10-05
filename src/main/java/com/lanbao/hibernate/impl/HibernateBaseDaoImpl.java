
package com.lanbao.hibernate.impl;


import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.lanbao.hibernate.HibernateBaseDao;

@Repository("hibernateBaseDao") 
public class HibernateBaseDaoImpl implements HibernateBaseDao{

	@Resource
    private SessionFactory sessionFactory;
	
	public Session getSession(){
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public Serializable save(Object o) {
		return getSession().save(o);
	}


	public void update(Object o) {
		getSession().update(o);
	}
	
	public int update(String hql, Object... values) {
		int i = createQuery(hql, values).executeUpdate();
		return i;
	}

	
	public void saveOrUpdate(Object o) {
		getSession().saveOrUpdate(o);
	}
 
	@Override
	public <T> void saveOrUpdateAll(Collection<T> c) {
		for(T t : c)
			getSession().saveOrUpdate(t);
	}




	@Override
	public <T> T get(Class<T> clazz, Serializable id) {
		return (T)getSession().get(clazz, id);
	}

	public <T> T load(Class<T> clazz, Serializable id) {
		return (T)getSession().load(clazz, id);
	}

	public Long total(String hql, Map<String, ?> values) {
		Long total = findUnique(hql, values);
		return total;
	}

	public Long total(String hql, Object... values) {
		Long total = findUnique(hql, values);
		return total;
	}
	
	public long total(Criteria c) {
		Long totalCountObject = (Long) c.setProjection(Projections.rowCount()).uniqueResult();
		long total = (totalCountObject != null) ? totalCountObject : 0;
		return total;
	}

	public <T> List<T> getAll(Class<T> entityClass, String orderByProperty, boolean isAsc) {
		Criteria c = createCriteria(entityClass);
		if (isAsc) {
			c.addOrder(Order.asc(orderByProperty));
		} else {
			c.addOrder(Order.desc(orderByProperty));
		}
		return c.list();
	}


	public <T> List<T> findBy(Class<T> entityClass, String propertyName, Object value) {
		Criterion criterion = Restrictions.eq(propertyName, value);
		return find(entityClass, criterion);
	}

	public <T> T findUniqueBy(Class<T> entityClass, String propertyName, Object value) {
		Criterion criterion = Restrictions.eq(propertyName, value);
		return (T)createCriteria(entityClass, criterion).uniqueResult();
	}

	public <T> List<T> find(String hql, Object... values) {
		return createQuery(hql, values).list();
	}
	
	public <T> List<T> findSplit(String hql, int currentPage, int pageSize, Object... values) {
		return setPageParameterToQuery(createQuery(hql, values), currentPage, pageSize).list();
	}
	
	
	public <T> List<T> find(String hql, Map<String, ?> values) {
		return createQuery(hql, values).list();
	}

	
	public <T> List<T> findBySql(Class<T> clazz, String sql, Object... values){ 
		List<T> list = createSQLQuery(sql, values)
		.setResultTransformer(Transformers.aliasToBean(clazz))
		.list();
		return list;
	}
	
	@Override
	public <T> List<T> findBySqlReturnMap(String sql, Object... values){ 
		List<T> list = createSQLQuery(sql, values)
		.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	
	

	public <T> List<T> findBySqlSplit(Class<T> clazz, String sql, int currentPage, int pageSize, Object... values){
		List<T> list = createSQLQuery(sql, values)
		.setFirstResult((currentPage - 1) * pageSize)
		.setMaxResults(pageSize)
		.setResultTransformer(Transformers.aliasToBean(clazz))
		.list();
		return list;
	}
	
	/*public <T> List<T> findBySql(Class<T> clazz, String sql, Map<String, ?> values){
		List<T> list = createSQLQuery(sql, values)
		.setResultTransformer(Transformers.aliasToBean(ReflectionUtils.getSuperClassGenricType(clazz)))
		.list();
		return list;
	}*/
	
	public <T> T findUnique(String hql, Object... values) {
		return (T)createQuery(hql, values).uniqueResult();
	}

	public <T> T findUnique(String hql, Map<String, ?> values) {
		return (T)createQuery(hql, values).uniqueResult();
	}

	public int batchExecute(String hql, Object... values) {
		return createQuery(hql, values).executeUpdate();
	}

	public int batchExecute(String hql, Map<String, ?> values) {
		return createQuery(hql, values).executeUpdate();
	}

	public Query createQuery(String queryString, Object... values) {
		Query query = getSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}

	public SQLQuery createSQLQuery(String sqlQueryString, Object... values) {
		SQLQuery query = getSession().createSQLQuery(sqlQueryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}

	public Query createQuery(String queryString, Map<String, ?> values) {
		Query query = getSession().createQuery(queryString);
		if (values != null) {
			query.setProperties(values);
		}
		return query;
	}
	

	public SQLQuery createSQLQuery(String sqlQueryString, Map<String, ?> values) {
		SQLQuery query = getSession().createSQLQuery(sqlQueryString);
		if (values != null) {
			query.setProperties(values);
		}
		return query;
	}

	public <T> List<T> find(Class<T> entiryClass, Criterion... criterions) {
		return createCriteria(entiryClass, criterions).list();
	}

	public <T> T findUnique(Class<T> entiryClass, Criterion... criterions) {
		return (T)createCriteria(entiryClass, criterions).uniqueResult();
	}

	public <T> Criteria createCriteria(Class<T> entityClass, Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}
	
	/**
	 * 为Query添加distinct transformer.
	 * 预加载关联对象的HQL会引起主对象重复, �??要进行distinct处理.
	 */
	public Query distinct(Query query) {
		query.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return query;
	}

	/**
	 * 为Criteria添加distinct transformer.
	 * 预加载关联对象的HQL会引起主对象重复, �??要进行distinct处理.
	 */
	public Criteria distinct(Criteria criteria) {
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria;
	}

	/**
	 * 判断对象的属性�?�在数据库内是否唯一.
	 * 
	 * 在修改对象的情景�??,如果属�?�新修改的�??(value)等于属�?�原来的�??(orgValue)则不作比�??.
	 */
	public boolean isPropertyUnique(final String propertyName, final Object newValue, final Object oldValue) {
	/*	if (newValue == null || newValue.equals(oldValue)) {
			return true;
		}
		Object object = findUniqueBy(propertyName, newValue);*/
		return false;//(object == null);
	}
	
	/**
	 * 设置分页参数到Query对象,辅助函数.
	 */
	protected <T> Query setPageParameterToQuery(final Query q, int currentPage, int pageSize) {
		q.setFirstResult((currentPage - 1) * pageSize);
		q.setMaxResults(pageSize);
		return q;
	}
 

	@Override
	public void delete(Object o) {
		getSession().delete(o);
	}

	@Override
	public <T> boolean delete(Class<T> clazz, Serializable id) {
		T t = (T)get(clazz, id);
		if(null != t){
			getSession().delete(t);
			return true;
		}
		return false;
	}

	public Integer delete(String hql, Object... values) {
		int i = createQuery(hql, values).executeUpdate();
		return i;
	}

	public <T> void deleteAll(Collection<T> c) {
		for(T t : c)
			getSession().delete(t);
	}
 
}
