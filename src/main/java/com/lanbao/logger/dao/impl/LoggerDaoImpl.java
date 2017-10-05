package com.lanbao.logger.dao.impl;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
 
import com.mongodb.WriteResult;

import com.lanbao.base.Page;
import com.lanbao.hibernate.impl.HibernateBaseDaoImpl;
import com.lanbao.logger.dao.LoggerDao;
import com.lanbao.mongodb.impl.MongoBaseDaoImpl;
import com.lanbao.utils.Logger;

@Component
public class LoggerDaoImpl extends MongoBaseDaoImpl<Logger> implements LoggerDao<Logger> {
	
  
}
