package com.lanbao.service;

import java.util.List;

public interface BaseService<T> {

	void h_update(T entity);

	void h_deleteById(Long id);

	T h_getById(Long id);

    List<T> h_getByIds(Long[] ids);

	List h_getList();

   void h_delete(T entity);

	T r_get(String key, Class<T> cls);

	void r_put(String key, T t, Class<T> cls);

}
