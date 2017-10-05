package com.lanbao.plugin;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.PropertyException;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import com.lanbao.base.JqGridPage;
import com.lanbao.base.Page;
import com.lanbao.utils.ReflectHelper;
import com.lanbao.utils.Tools;

/**
 * 
* 缁鎮曠粔甯窗PagePlugin.java
* 缁粯寮挎潻甯窗 
* @author lanbao
* 娴ｆ粏锟藉懎宕熸担宥忕窗 
* 閼辨梻閮撮弬鐟扮础閿涙q313596790
* 閸掓稑缂撻弮鍫曟？閿涳拷2014楠烇拷7閺堬拷1閺冿拷
* @version 1.0
 */
@Intercepts({@Signature(type=StatementHandler.class,method="prepare",args={Connection.class})})
public class PagePlugin implements Interceptor {

	private static String dialect = "";	//閺佺増宓佹惔鎾存煙鐟凤拷
	private static String pageSqlId = ""; //mapper.xml娑擃參娓剁憰浣瑰閹搭亞娈慖D(濮濓絽鍨崠褰掑帳)
	
	public Object intercept(Invocation ivk) throws Throwable {
		// TODO Auto-generated method stub
		if(ivk.getTarget() instanceof RoutingStatementHandler){
			RoutingStatementHandler statementHandler = (RoutingStatementHandler)ivk.getTarget();
			BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper.getValueByFieldName(statementHandler, "delegate");
			MappedStatement mappedStatement = (MappedStatement) ReflectHelper.getValueByFieldName(delegate, "mappedStatement");
			
			if(mappedStatement.getId().matches(pageSqlId)){ //閹凤附鍩呴棁锟界憰浣稿瀻妞ょ數娈慡QL
				BoundSql boundSql = delegate.getBoundSql();
				Object parameterObject = boundSql.getParameterObject();//閸掑棝銆塖QL<select>娑撶捀arameterType鐏炵偞锟窖冾嚠鎼存梻娈戠�圭偘缍嬮崣鍌涙殶閿涘苯宓哅apper閹恒儱褰涙稉顓熷⒔鐞涘苯鍨庢い鍨煙濞夋洜娈戦崣鍌涙殶,鐠囥儱寮弫棰佺瑝瀵版ぞ璐熺粚锟�
				if(parameterObject==null){
					throw new NullPointerException("parameterObject鐏忔碍婀�圭偘绶ラ崠鏍电磼");
				}else{
					Connection connection = (Connection) ivk.getArgs()[0];
					String sql = boundSql.getSql();
					//String countSql = "select count(0) from (" + sql+ ") as tmp_count"; //鐠佹澘缍嶇紒鐔活吀
					String countSql = "select count(0) from (" + sql+ ")  tmp_count"; //鐠佹澘缍嶇紒鐔活吀 == oracle 閸旓拷 as 閹躲儵鏁�(SQL command not properly ended)
					PreparedStatement countStmt = connection.prepareStatement(countSql);
					BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(),countSql,boundSql.getParameterMappings(),parameterObject);
					setParameters(countStmt,mappedStatement,countBS,parameterObject);
					ResultSet rs = countStmt.executeQuery();
					int count = 0;
					if (rs.next()) {
						count = rs.getInt(1);
					}
					rs.close();
					countStmt.close();
					//System.out.println(count);
					Page page = null;
					JqGridPage jqGridPage = null;
					if(parameterObject instanceof Page){	//閸欏倹鏆熺亸杈ㄦЦPage鐎圭偘缍�
						page = (Page) parameterObject;
						page.setEntityOrField(true);	 
						page.setTotalResult(count);
						page.setRecords(count); 
					}else if(parameterObject instanceof JqGridPage){
						jqGridPage = (JqGridPage)parameterObject;
						jqGridPage.setRecords(count);
					}else{	//閸欏倹鏆熸稉鐑樼厙娑擃亜鐤勬担鎿勭礉鐠囥儱鐤勬担鎾村閺堝age鐏炵偞锟斤拷
						Field pageField = ReflectHelper.getFieldByFieldName(parameterObject,"page");
						if(pageField!=null){
							page = (Page) ReflectHelper.getValueByFieldName(parameterObject,"page");
							if(page==null)
								page = new Page();
							page.setEntityOrField(false); 
							page.setTotalResult(count);
							page.setRecords(count);
							ReflectHelper.setValueByFieldName(parameterObject,"page", page); //闁俺绻冮崣宥呯殸閿涘苯顕�圭偘缍嬬�电钖勭拋鍓х枂閸掑棝銆夌�电钖�
						}else{
							throw new NoSuchFieldException(parameterObject.getClass().getName()+"娑撳秴鐡ㄩ崷锟� page 鐏炵偞锟窖嶇磼");
						}
					}
				   String pageSql = null;
				  if(parameterObject instanceof JqGridPage){
					  pageSql = generatejqGridPageSql(sql,jqGridPage); 
				  }else{
					 pageSql = generatePageSql(sql,page);  
				  }
					 ReflectHelper.setValueByFieldName(boundSql, "sql", pageSql); //鐏忓棗鍨庢い绁峲l鐠囶厼褰為崣宥呯殸閸ユ卡oundSql.
				}
			}
		}
		return ivk.proceed();
	}

	
	/**
	 * 鐎电瓖QL閸欏倹鏆�(?)鐠佹儳锟斤拷,閸欏倽锟藉兌rg.apache.ibatis.executor.parameter.DefaultParameterHandler
	 * @param ps
	 * @param mappedStatement
	 * @param boundSql
	 * @param parameterObject
	 * @throws SQLException
	 */
	private void setParameters(PreparedStatement ps,MappedStatement mappedStatement,BoundSql boundSql,Object parameterObject) throws SQLException {
		ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		if (parameterMappings != null) {
			Configuration configuration = mappedStatement.getConfiguration();
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			MetaObject metaObject = parameterObject == null ? null: configuration.newMetaObject(parameterObject);
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					PropertyTokenizer prop = new PropertyTokenizer(propertyName);
					if (parameterObject == null) {
						value = null;
					} else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)&& boundSql.hasAdditionalParameter(prop.getName())) {
						value = boundSql.getAdditionalParameter(prop.getName());
						if (value != null) {
							value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
						}
					} else {
						value = metaObject == null ? null : metaObject.getValue(propertyName);
					}
					TypeHandler typeHandler = parameterMapping.getTypeHandler();
					if (typeHandler == null) {
						throw new ExecutorException("There was no TypeHandler found for parameter "+ propertyName + " of statement "+ mappedStatement.getId());
					}
					typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
				}
			}
		}
	}
	
	/**
	 * 閺嶈宓侀弫鐗堝祦鎼存挻鏌熺懛锟介敍宀�鏁撻幋鎰鐎规氨娈戦崚鍡涖�塻ql
	 * @param sql
	 * @param page
	 * @return
	 */
	private String generatejqGridPageSql(String sql,JqGridPage page){
		if(page!=null && Tools.notEmpty(dialect)){
			StringBuffer pageSql = new StringBuffer();
			if("mysql".equals(dialect)){
				pageSql.append(sql);
				pageSql.append(" limit "+page.getCurrentResult()+","+page.getRows());
			}else if("oracle".equals(dialect)){
				pageSql.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
				pageSql.append(sql);
				//pageSql.append(") as tmp_tb where ROWNUM<=");
				pageSql.append(") tmp_tb where ROWNUM<=");
				pageSql.append(page.getCurrentResult()+page.getRows());
				pageSql.append(") where row_id>");
				pageSql.append(page.getCurrentResult());
			}
			return pageSql.toString();
		}else{
			return sql;
		}
	}
	
	private String generatePageSql(String sql,Page page){
		if(page!=null && Tools.notEmpty(dialect)){
			StringBuffer pageSql = new StringBuffer();
			if("mysql".equals(dialect)){
				pageSql.append(sql);
				pageSql.append(" limit "+page.getCurrentResult()+","+page.getShowCount());
			}else if("oracle".equals(dialect)){
				pageSql.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
				pageSql.append(sql);
				//pageSql.append(") as tmp_tb where ROWNUM<=");
				pageSql.append(") tmp_tb where ROWNUM<=");
				pageSql.append(page.getCurrentResult()+page.getShowCount());
				pageSql.append(") where row_id>");
				pageSql.append(page.getCurrentResult());
			}
			return pageSql.toString();
		}else{
			return sql;
		}
	}
	
	
	public Object plugin(Object arg0) {
		// TODO Auto-generated method stub
		return Plugin.wrap(arg0, this);
	}

	public void setProperties(Properties p) {
		dialect = p.getProperty("dialect");
		if (Tools.isEmpty(dialect)) {
			try {
				throw new PropertyException("dialect property is not found!");
			} catch (PropertyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		pageSqlId = p.getProperty("pageSqlId");
		if (Tools.isEmpty(pageSqlId)) {
			try {
				throw new PropertyException("pageSqlId property is not found!");
			} catch (PropertyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
