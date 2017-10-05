package com.lanbao.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired; 

public interface HibernateBaseDao{
 

    Serializable save(Object o); 
	
	void update(Object o);

	void saveOrUpdate(Object o); 
	
	<T> void saveOrUpdateAll(Collection<T> c);

	public <T> T get(Class<T> clazz, Serializable id);
 
	<T> List<T> findBy(Class<T> entityClass, String propertyName, Object value);

	public <T> List<T> find(String hql, Map<String, ?> values);

	SQLQuery createSQLQuery(String sqlQueryString, Map<String, ?> values);

	public <T> List<T> findBySql(Class<T> clazz, String sql, Object[] values);

	public <T> List<T> findBySqlReturnMap(String sql, Object[] values);

	

	void delete(Object o);

	<T> boolean delete(Class<T> clazz, Serializable id);
 
}
