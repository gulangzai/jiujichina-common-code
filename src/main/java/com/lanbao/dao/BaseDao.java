package com.lanbao.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;

public interface BaseDao<T> {
	
	/**
	 * hibernate funciton
	 * @param t
	 */
	public  Object h_save(T t);
	 
	public  void h_update(T t);
	
	public void h_saveOrUpdate(Object o); 
	
	<T> void h_saveOrUpdateAll(Collection<T> c);
	
	public  void h_delete(Object obj);
	  
	public <T> T get(Class<T> clazz, Serializable id);
	  
	<T> List<T> h_findBy(Class<T> entityClass, String propertyName, Object value);
	
	<T> List<T> h_find(String hql, Map<String, ?> values);
	
	public SQLQuery h_createSQLQuery(String sqlQueryString, Map<String, ?> values);
	
	
	
	
	
	
	
	
	
	
	
	
	//redis function
	public T r_get(String key, Class<T> cls);

	void r_put(String key, T t, Class<T> cls);
  
	void r_delete(String key);

	
	
	/**
	 * myBatis
	 * @param pd
	 * @return
	 * @throws Exception 
	 */
	Object mb_save(String str,Object pd) throws Exception;

	Object mb_update(String str,Object pd) throws Exception;

	Object mb_delete(String string, Object pd) throws Exception;

	Object mb_findForObject(String str,Object pd) throws Exception;
	
	Object mb_findForList(String str,Object pd) throws Exception;


	
	
	
}
