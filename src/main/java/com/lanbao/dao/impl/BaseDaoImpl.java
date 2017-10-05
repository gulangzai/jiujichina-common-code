package com.lanbao.dao.impl;


import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.Transaction;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import com.lanbao.dao.BaseDao;
import com.lanbao.hibernate.HibernateBaseDao;
import com.lanbao.mongodb.MongoBaseDao;
import com.lanbao.mybatis.MyBatisBaseDao; 
import com.lanbao.redis.dao.RedisBaseDao; 

import com.lanbao.base.PageData;
@Component
public class BaseDaoImpl<T> implements BaseDao<T>{

	@Resource(name="myBatisBaseDao")   
	MyBatisBaseDao myBatisBaseDao;
	
	@Autowired
	HibernateBaseDao hibernateBaseDao;
	
	@Autowired
	RedisBaseDao redisBaseDao;
	
	/*@Autowired*/
	MongoBaseDao mongoBaseDao;
	
	
	
	 
	
	Session session = null;
	Class<T> clazz;
	
	public BaseDaoImpl(){ 
		System.out.println("clazz---"+clazz);
	}
	
	
	@Override
	public Object mb_save(String str,Object pd) throws Exception{
		myBatisBaseDao.save(str, pd); 
	  //int id = (int) ((PageData) pd).get("ID");
	    return pd;
	}
 
	@Override
	public Object mb_update(String str,Object pd) throws Exception{ 
	    return myBatisBaseDao.update(str, pd);
	}
	
	@Override
	public Object mb_delete(String str,Object pd) throws Exception{ 
	    return myBatisBaseDao.delete(str, pd); 
	}
	
	@Override
	public Object mb_findForObject(String str,Object pd) throws Exception{ 
	    return myBatisBaseDao.findForObject(str,pd);
	}
	
	@Override
	public Object mb_findForList(String str,Object pd) throws Exception{ 
	    return myBatisBaseDao.findForList(str, pd); 
	}
	 

	@Override
	public Object h_save(T entity) { 
		return hibernateBaseDao.save(entity);
	}

	@Override
	public void h_update(T entity) {
		// TODO Auto-generated method stub
		hibernateBaseDao.update(entity);
	}
 
	public void h_saveOrUpdate(Object o) {
	    hibernateBaseDao.saveOrUpdate(o);
	}
	 
	public <T> void h_saveOrUpdateAll(Collection<T> c) {
		hibernateBaseDao.saveOrUpdateAll(c);
	}
	
	
	@Override
	public void h_delete(Object entity) {
		// TODO Auto-generated method stub 
		hibernateBaseDao.delete(entity);
		System.out.println("......删除操作");
	}
	
	@Override
	public <T> T get(Class<T> clazz, Serializable id) {
		return (T)hibernateBaseDao.get(clazz, id);
	}
	
	@Override
	public <T> List<T> h_findBy(Class<T> entityClass, String propertyName, Object value) {
		// TODO Auto-generated method stub
		return hibernateBaseDao.findBy(entityClass, propertyName, value);
	} 
	
	@Override
	public <T> List<T> h_find(String hql, Map<String, ?> values){
		return hibernateBaseDao.find(hql, values);
	}
	
	@Override
	public SQLQuery h_createSQLQuery(String sqlQueryString, Map<String, ?> values) {
		return hibernateBaseDao.createSQLQuery(sqlQueryString, values);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
 
	@Override
	public T r_get(String key, Class<T> cls) {
		// TODO Auto-generated method stub
		return (T) redisBaseDao.get(key, cls);
	}
	
	@Override
	public void r_put(String key, T t, Class<T> cls) {
		redisBaseDao.put(key, t, cls);
	}
  
	@Override
	public void r_delete(String key) {
		// TODO Auto-generated method stub
		redisBaseDao.delete(key);
	}


 
 
 
}
