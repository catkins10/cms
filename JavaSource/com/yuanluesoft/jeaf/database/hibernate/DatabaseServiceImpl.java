/*
 * Created on 2004-12-18
 *
 */
package com.yuanluesoft.jeaf.database.hibernate;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.FlushMode;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.LockMode;
import net.sf.hibernate.ObjectNotFoundException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Expression;
import net.sf.hibernate.impl.SessionFactoryImpl;
import net.sf.hibernate.impl.SessionImpl;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.HibernateTemplate;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.database.SqlResultList;
import com.yuanluesoft.jeaf.database.dialect.DatabaseDialect;
import com.yuanluesoft.jeaf.database.exception.DataAccessException;
import com.yuanluesoft.jeaf.database.model.Table;
import com.yuanluesoft.jeaf.database.model.TableColumn;
import com.yuanluesoft.jeaf.database.model.TableIndex;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 
 * @author linchuan
 *
 */
public class DatabaseServiceImpl extends HibernateDaoSupport implements DatabaseService {
	private String dbServerName = null;
	private String jdbcURL = null;
	private DatabaseDialect databaseDialect; //数据库方言
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#getDbServerName()
	 */
	public String getDbServerName() throws DataAccessException {
		if(dbServerName==null) {
			String dialect = ((SessionImpl)getSession()).getFactory().getDialect().getClass().getName();
			dialect = dialect.substring(dialect.lastIndexOf('.') + 1);
			dbServerName = dialect.replaceAll("Dialect", "").toLowerCase();
		}
		return dbServerName;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.DatabaseService#getJdbcURL()
	 */
	public String getJdbcURL() throws DataAccessException {
		try {
			if(jdbcURL==null) {
				jdbcURL = ((SessionImpl)getSession()).getFactory().getConnectionProvider().getConnection().getMetaData().getURL();
			}
			return jdbcURL;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#saveObject(java.lang.Object)
	 */
	public Record saveRecord(final Record record) throws DataAccessException {
		final HibernateTemplate hibernateTemplate = getHibernateTemplate();
		try {
			resetPropertyValue(record);
			hibernateTemplate.execute(new HibernateCallback() {
				/* (non-Javadoc)
				 * @see org.springframework.orm.hibernate.HibernateCallback#doInHibernate(net.sf.hibernate.Session)
				 */
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					if((hibernateTemplate.isCheckWriteOperations()) && (hibernateTemplate.getFlushMode() != 2) && (FlushMode.NEVER.equals(session.getFlushMode()))) {
				      throw new InvalidDataAccessApiUsageException("Write operations are not allowed in read-only mode (FlushMode.NEVER) - turn your Session into FlushMode.AUTO respectively remove 'readOnly' marker from transaction definition");
				    }
					session.save(record);
				    session.flush();
				    databaseDialect.freeTemporaryClob();
					return record;
				}
			});
			return record;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw(new DataAccessException(e.getMessage()));
		}
		finally {
			hibernateTemplate.clear();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#updateObject(java.lang.Object)
	 */
	public Record updateRecord(final Record record) throws DataAccessException  {
		final HibernateTemplate hibernateTemplate = getHibernateTemplate();
		try {
			resetPropertyValue(record);
			hibernateTemplate.execute(new HibernateCallback() {
				/* (non-Javadoc)
				 * @see org.springframework.orm.hibernate.HibernateCallback#doInHibernate(net.sf.hibernate.Session)
				 */
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					if((hibernateTemplate.isCheckWriteOperations()) && (hibernateTemplate.getFlushMode() != 2) && (FlushMode.NEVER.equals(session.getFlushMode()))) {
						throw new InvalidDataAccessApiUsageException("Write operations are not allowed in read-only mode (FlushMode.NEVER) - turn your Session into FlushMode.AUTO respectively remove 'readOnly' marker from transaction definition");
				    }
				    session.update(record);
				    session.flush();
				    databaseDialect.freeTemporaryClob();
				    return record;
				}
			});
			return record;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw(new DataAccessException(e.getMessage()));
		}
		finally {
			hibernateTemplate.clear();
		}
	}
	
	/**
	 * 重置参数值
	 * @param record
	 * @throws DataAccessException
	 */
	private void resetPropertyValue(Record record) throws DataAccessException  {
		try {
			PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(record);
			for(int i=0; i<propertyDescriptors.length; i++) {
				String propertyName = propertyDescriptors[i].getName();
				if(PropertyUtils.isWriteable(record, propertyName) && propertyDescriptors[i].getPropertyType().equals(String.class)) { //文本
					//如果值为"",转换为null,用于
					try {
						Object propertyValue = PropertyUtils.getProperty(record, propertyName);
						if(propertyValue!=null && "".equals(propertyValue)) {
							PropertyUtils.setProperty(record, propertyName, null);
						}
					}
					catch(Exception e) {
						
					}
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#deleteObject(java.lang.Object)
	 */
	public void deleteRecord(Record record) throws DataAccessException  {
		HibernateTemplate hibernateTemplate = getHibernateTemplate();
		hibernateTemplate.refresh(record);
		hibernateTemplate.delete(record);
		hibernateTemplate.flush();
		hibernateTemplate.clear();
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#deleteObjectById(java.lang.String, long)
	 */
	public void deleteRecordById(String recordClassName, long id)  throws DataAccessException  {
		try {
		    Object obj = findRecordById(recordClassName, id);
		    if(obj!=null) {
		        getHibernateTemplate().delete(obj);
		    }
		}
		catch (org.springframework.dao.DataAccessException e) {
			throw(e);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw(new DataAccessException(e.getMessage()));
		}
		finally {
			HibernateTemplate hibernateTemplate = getHibernateTemplate();
			hibernateTemplate.flush();
			hibernateTemplate.clear();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#findObjectById(java.lang.String, long)
	 */
	public Record findRecordById(String recordClassName, long id) throws DataAccessException  {
		return findRecordById(recordClassName, id, null);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#findObjectById(java.lang.String, long, java.util.List)
	 */
	public Record findRecordById(String recordClassName, long id, List lazyLoadProperties) throws DataAccessException  {
		if(lazyLoadProperties==null) {
			lazyLoadProperties = new ArrayList();
		}
		Record record = null;
		LoadPojoCallback loadPojoCallback;
		try {
			Class pojoClass = Class.forName(recordClassName);
			loadPojoCallback = new LoadPojoCallback(pojoClass, id, lazyLoadProperties);
			record = (Record)getHibernateTemplate().execute(loadPojoCallback);
		}
		catch (ClassNotFoundException e) {
			throw(new DataAccessException("pojo class not found."));
		}
		catch (Exception e) {
			e.printStackTrace();
			throw(new DataAccessException(e.getMessage()));
		}
		finally {
			getHibernateTemplate().clear();
			loadPojoCallback = null;
		}
		return record;
	}
	
	/**
	 * 加载POJO类
	 * @author linchuan
	 *
	 */
	private class LoadPojoCallback implements HibernateCallback {
		private Class pojoClass;
		private long id;
		private List lazyLoadProperties;

		public LoadPojoCallback(Class pojoClass, long id, List lazyLoadProperties) {
			super();
			this.pojoClass = pojoClass;
			this.id = id;
			this.lazyLoadProperties = lazyLoadProperties;
		}

		public Object doInHibernate(Session session) throws HibernateException {
			Object entry;
			try {
				entry = session.load(pojoClass, new Long(id));
			}
			catch(ObjectNotFoundException e) {
				return null;
			}
			if(entry==null) {
				return null;
			}
			if(lazyLoadProperties!=null) {
				for(Iterator iterator = lazyLoadProperties.iterator(); iterator.hasNext();) {
					String propertyName = (String)iterator.next();
					try {
						Object property = PropertyUtils.getProperty(entry, propertyName);
						if(property!=null) {
							property.getClass().getMethod("isEmpty", new Class[] {}).invoke(property, new Object[] {});
						}
					}
					catch (Exception e) {

					}
				}
			}
			return entry;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#findObjectByHql(java.lang.String)
	 */
	public Object findRecordByHql(String hql) throws DataAccessException {
		List objects = findRecordsByHql(hql, 0, 1);
		return objects==null || objects.isEmpty() ? null : objects.get(0);
	}

    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.database.BaseDAO#findObjectByHql(java.lang.String, java.util.List)
     */
    public Object findRecordByHql(String hql, List lazyLoadProperties) throws DataAccessException {
    	List objects = findRecordsByHql(hql, lazyLoadProperties, 0, 1);
		return objects==null || objects.isEmpty() ? null : objects.get(0);
    }
    
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#findObjectsByHql(java.lang.String)
	 */
	public List findRecordsByHql(String hql) throws DataAccessException  {
		return findRecordsByHql(hql, null, 0, 0);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#findObjectsByHql(java.lang.String, java.util.List)
	 */
	public List findRecordsByHql(String hql, List lazyLoadProperties) throws DataAccessException {
		return findRecordsByHql(hql, lazyLoadProperties, 0, 0);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#findObjectsByHql(java.lang.String, int, int)
	 */
	public List findRecordsByHql(String hql, int first, int max) throws DataAccessException  {
		return findRecordsByHql(hql, null, first, max);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#findObjectsByHql(java.lang.String, java.util.List, int, int)
	 */
	public List findRecordsByHql(String hql, List lazyLoadProperties, int first, int max) throws DataAccessException {
		HQLQueryCallback callback;
		try {
			callback = new HQLQueryCallback(hql, lazyLoadProperties, first, max);
			return getHibernateTemplate().executeFind(callback);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw(new DataAccessException(e.getMessage()));
		}
		finally {
			callback = null;
			getHibernateTemplate().clear();
		}
	}
	
	/**
	 * 按hql查询类
	 * @author linchuan
	 *
	 */
	private class HQLQueryCallback implements HibernateCallback {
		private String hql;
		private int first;
		private int max;
		private List lazyLoadProperties;
		
		public HQLQueryCallback(String hql, List lazyLoadProperties, int first, int max) {
			this.hql = hql;
			this.lazyLoadProperties = lazyLoadProperties;
			this.first = first;
			this.max = max;
		}
		
		public Object doInHibernate(Session session) throws HibernateException, SQLException {
			Query query = session.createQuery(databaseDialect.replaceFunction(hql));
			try {
				query.setFirstResult(first);
				if(max>0) {
					query.setMaxResults(max);
				}
				List list = query.list();
				if(list==null) {
					return null;
				}
				if(list.isEmpty()) {
					list = null;
					return null;
				}
				if(lazyLoadProperties!=null) {
					for(Iterator entry = list.iterator(); entry.hasNext();) {
						Object obj = entry.next();
						for(Iterator iterator = lazyLoadProperties.iterator(); iterator.hasNext();) {
							String propertyName = (String)iterator.next();
							try {
								Object property = PropertyUtils.getProperty(obj, propertyName);
								if(property!=null) {
									property.getClass().getMethod("isEmpty", new Class[] {}).invoke(property, new Object[] {});
								}
							} 
							catch (Exception e) {
								
							}
						}
					}
				}
				return list;
			}
			catch (Exception e) {
				e.printStackTrace();
				throw(new DataAccessException(e.getMessage()));
			}
			finally {
				query = null;
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#findObjectByKey(java.lang.String, java.lang.String, java.lang.Object)
	 */
	public Record findRecordByKey(String recordClassName, String keyName, Object keyValue) throws DataAccessException  {
		List objects = findRecordsByKey(recordClassName, keyName, keyValue, 0, 1);
		return objects==null || objects.isEmpty() ? null : (Record)objects.get(0);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#findObjectsByKey(java.lang.String, java.lang.String, java.lang.Object)
	 */
	public List findRecordsByKey(String recordClassName, String keyName, Object keyValue) throws DataAccessException  {
		return findRecordsByKey(recordClassName, keyName, keyValue, 0, 0);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.core.dao.BaseDAO#findObjectByKey(java.lang.String, java.lang.String, java.lang.Object, java.util.List)
	 */
	public Record findRecordByKey(String recordClassName, String keyName, Object keyValue, List lazyLoadProperties) throws DataAccessException {
		Record record = null;
		KeyQueryCallback keyQueryCallback;
		try {
			Class.forName(recordClassName);
			keyQueryCallback = new KeyQueryCallback(recordClassName, keyName, keyValue, lazyLoadProperties);
			record = (Record)getHibernateTemplate().execute(keyQueryCallback);
		}
		catch (ClassNotFoundException e) {
			throw(new DataAccessException("pojo class not found."));
		}
		catch (Exception e) {
			e.printStackTrace();
			throw(new DataAccessException(e.getMessage()));
		}
		finally {
			keyQueryCallback = null;
			getHibernateTemplate().clear();
		}
		return record;
	}
	
	/**
	 * 按关键字查询类
	 * @author linchuan
	 *
	 */
	private class KeyQueryCallback implements HibernateCallback {
		private String pojoName;
		private String keyName;
		private Object keyValue;
		private List lazyLoadProperties; 

		public KeyQueryCallback(String pojoName, String keyName, Object keyValue, List lazyLoadProperties) {
			super();
			this.pojoName = pojoName;
			this.keyName = keyName;
			this.keyValue = keyValue;
			this.lazyLoadProperties = lazyLoadProperties;
		}

		public Object doInHibernate(Session session) throws HibernateException {
			Criteria criteria = null;
			try {
				try {
					criteria = session.createCriteria(Class.forName(pojoName));
				}
				catch(ClassNotFoundException e) {
					throw(new HibernateException("class " + pojoName + " can not found"));
				}
				criteria.setFirstResult(0);
				criteria.setMaxResults(1);
				if(keyName!=null && keyValue!=null) {
					criteria.add(Expression.eq(keyName, keyValue));
				}
				List list = criteria.list();
				if(list==null) {
					return null;
				}
				if(list.isEmpty()) {
					list = null;
					return null;
				}
				Object entry = list.get(0);
				if(lazyLoadProperties!=null) {
					for(Iterator iterator = lazyLoadProperties.iterator(); iterator.hasNext();) {
						String propertyName = (String)iterator.next();
						try {
							Object property = PropertyUtils.getProperty(entry, propertyName);
							if(property!=null) {
								property.getClass().getMethod("isEmpty", new Class[] {}).invoke(property, new Object[] {});
							}
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				return entry;
			}
			catch (Exception e) {
				e.printStackTrace();
				throw(new DataAccessException(e.getMessage()));
			}
			finally {
				criteria = null;
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#findObjectsByKey(java.lang.String, java.lang.String, java.lang.Object, int, int)
	 */
	public List findRecordsByKey(String recordClassName, String keyName, Object keyValue, int first, int max) throws DataAccessException  {
		ListObjectsByKeyCallback listObjectsByKeyCallback;
		try {
			listObjectsByKeyCallback = new ListObjectsByKeyCallback(recordClassName, keyName, keyValue, first, max);
			return getHibernateTemplate().executeFind(listObjectsByKeyCallback);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw(new DataAccessException(e.getMessage()));
		}
		finally {
			listObjectsByKeyCallback = null;
			getHibernateTemplate().clear();
		}
	}
	
	/**
	 * 按关键字查询批量数据类
	 * @author linchuan
	 *
	 */
	private class ListObjectsByKeyCallback implements HibernateCallback {
		private String pojoName;
		private String keyName;
		private Object keyValue;
		private int first;
		private int max;
		
		public ListObjectsByKeyCallback(String pojoName, String keyName, Object keyValue, int first, int max) {
			super();
			this.pojoName = pojoName;
			this.keyName = keyName;
			this.keyValue = keyValue;
			this.first = first;
			this.max = max;
		}

		public Object doInHibernate(Session session) throws HibernateException {
			Criteria criteria = null;
			try {
				try {
					criteria = session.createCriteria(Class.forName(pojoName));
				}
				catch(ClassNotFoundException e) {
					throw(new HibernateException("class " + pojoName + " can not found"));
				}
				criteria.setFirstResult(first);
				if(max>0) {
					criteria.setMaxResults(max);
				}
				if(keyName!=null && keyValue!=null) {
					criteria.add(Expression.eq(keyName, keyValue));
				}
				return criteria.list();
			}
			catch (Exception e) {
				e.printStackTrace();
				throw(new DataAccessException(e.getMessage()));
			}
			finally {
				criteria = null;
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#deleteObjectsByHql(java.lang.String)
	 */
	public void deleteRecordsByHql(String hql) throws DataAccessException {
		HibernateTemplate hibernateTemplate = getHibernateTemplate();
		for(int i=0; i<1000000 ; i++) { //每次处理200条记录,最多2亿条记录
			List records = findRecordsByHql(hql, 0, 200);
			if(records==null || records.isEmpty()) {
				break;
			}
			for(Iterator iterator = records.iterator(); iterator.hasNext();) {
				Record record = (Record)iterator.next();
				hibernateTemplate.refresh(record);
				hibernateTemplate.delete(record);
			}
			hibernateTemplate.flush();
			hibernateTemplate.clear();
			if(records.size()<200) {
				break;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.core.dao.BaseDAO#flush()
	 */
	public void flush() throws DataAccessException {
		getHibernateTemplate().flush();
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#clear()
	 */
	public void clear() throws DataAccessException {
		getHibernateTemplate().clear();
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#refresh(java.lang.Object)
	 */
	public void refresh(Object object) throws DataAccessException {
		getHibernateTemplate().refresh(object, LockMode.UPGRADE_NOWAIT);
	}

	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.DatabaseService#executeQueryBySql(java.lang.String, int, int)
	 */
	public SqlResultList executeQueryBySql(String sql, int offset, int limit) throws DataAccessException {
		ExecuteQueryBySqlCallback callback;
		try {
			callback = new ExecuteQueryBySqlCallback(databaseDialect.replaceFunction(sql), offset, limit);
			return (SqlResultList)getHibernateTemplate().execute(callback);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw(new DataAccessException(e.getMessage()));
		}
		finally {
			callback = null;
		}
	}
	
	/**
	 * 执行sql类
	 * @author linchuan
	 *
	 */
	private class ExecuteQueryBySqlCallback implements HibernateCallback {
		private String sql;
		private int offset;
		private int limit;
		
		public ExecuteQueryBySqlCallback(String sql, int offset, int limit) {
			super();
			this.sql = sql;
			this.offset = offset;
			this.limit = limit;
		}

		public Object doInHibernate(Session session) throws HibernateException, SQLException {
			//加入分页代码
			sql = databaseDialect.generatePagingSql(sql, offset, limit);
			if(Logger.isDebugEnabled()) {
				Logger.debug("execute sql: " + sql);
			}
			Statement statement = null;
			ResultSet resultSet = null;
			try {
				statement = session.connection().createStatement();
				resultSet = statement.executeQuery(sql);
				return JdbcUtils.convertToSqlResultList(resultSet, 5000);
			}
			finally {
				try {
					resultSet.close();
				}
				catch(Exception e) {
					
				}
				resultSet = null;
				try {
					statement.close();
				}
				catch(Exception e) {
					
				}
				statement = null;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.DatabaseService#executeSql(java.lang.String)
	 */
	public void executeSql(String sql) throws DataAccessException {
		if(Logger.isDebugEnabled()) {
			Logger.debug("execute sql: " + sql);
		}
		Session session = null;
		Statement statement = null;
		try {
			session = getHibernateTemplate().getSessionFactory().openSession();
			statement = session.connection().createStatement();
			statement.execute(sql);
		}
		catch(Exception e) {
			throw new DataAccessException(StringUtils.exceptionToString(e));
		}
		finally {
			try {
				statement.close();
			}
			catch(Exception e) {
				
			}
			try {
				session.close();
			}
			catch(Exception e) {
				
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#getSequenceValue(java.lang.String)
	 */
	public long getSequenceValue(String sequenceName) throws DataAccessException {
		GetSequenceValueCallback getSequenceValueCallback;
		try {
			getSequenceValueCallback = new GetSequenceValueCallback(sequenceName);
			return ((Number)getHibernateTemplate().execute(getSequenceValueCallback)).longValue();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw(new DataAccessException(e.getMessage()));
		}
		finally {
			getSequenceValueCallback = null;
		}
	}
	
	/**
	 * 获取sequence类
	 * @author linchuan
	 *
	 */
	private class GetSequenceValueCallback implements HibernateCallback {
		private String sequenceName;

		public GetSequenceValueCallback(String sequenceName) {
			super();
			this.sequenceName = sequenceName;
		}

		public Object doInHibernate(Session session) throws HibernateException, SQLException {
			Statement statement = null;
			ResultSet resultSet = null;
			try {
				statement = session.connection().createStatement();
				resultSet = statement.executeQuery("select " + sequenceName + ".nextval from dual");
				resultSet.next();
				Number seq = (Number)resultSet.getObject(1); 
				return seq;
			}
			catch (Exception e) {
				Logger.exception(e);
				throw(new DataAccessException(e.getMessage()));
			}
			finally {
				try {
					resultSet.close();
				}
				catch(Exception e) {
					
				}
				resultSet = null;
				try {
					statement.close();
				}
				catch(Exception e) {
					
				}
				statement = null;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#findObjectsBySql(java.lang.String, java.lang.String[], java.lang.Class[], int, int)
	 */
	public List findRecordsBySql(String sql, String[] returnAliases, Class[] returnClasses) throws DataAccessException {
		SqlQueryCallback sqlQueryCallback;
		try {
			sqlQueryCallback = new SqlQueryCallback(sql, returnAliases, returnClasses);
			return getHibernateTemplate().executeFind(sqlQueryCallback);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw(new DataAccessException(e.getMessage()));
		}
		finally {
			sqlQueryCallback = null;
			getHibernateTemplate().clear();
		}
	}
	
	/**
	 * 按SQL查询类
	 * @author linchuan
	 *
	 */
	private class SqlQueryCallback implements HibernateCallback {
		private String sql;
		private String[] returnAliases;
		private Class[] returnClasses;

		public SqlQueryCallback(String sql, String[] returnAliases, Class[] returnClasses) {
			super();
			this.sql = sql;
			this.returnAliases = returnAliases;
			this.returnClasses = returnClasses;
		}

		public Object doInHibernate(Session session) throws HibernateException, SQLException {
			Query query = ((SessionImpl)session).createSQLQuery(sql,  returnAliases, returnClasses);
			try {
				return query.list();
			}
			catch (Exception e) {
				e.printStackTrace();
				throw(new DataAccessException(e.getMessage()));
			}
			finally {
				query = null;
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.DatabaseService#findPrivilegedRecords(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, char, boolean, java.util.List, int, int, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List findPrivilegedRecords(String recordClassName, String hqlSelect, String hqlJoin, String hqlWhere, String hqlOrderBy, String hqlGroupBy, char privilegeLevel, boolean highLevelDisbaled, List lazyLoadProperties, int offset, int max, SessionInfo sessionInfo) throws DataAccessException {
		String[] hql = generatePrivilegedObjectsHql(recordClassName, hqlJoin, hqlWhere, privilegeLevel, highLevelDisbaled, sessionInfo);
		return findRecordsByHqlClause(recordClassName, hqlSelect, hql[0], hql[1], hqlOrderBy, hqlGroupBy, lazyLoadProperties, offset, max);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.DatabaseService#countPrivilegedRecords(java.lang.String, java.lang.String, java.lang.String, java.lang.String, char, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public int countPrivilegedRecords(String recordClassName, String hqlJoin, String hqlWhere, String hqlGroupBy, char privilegeLevel, boolean highLevelDisbaled, SessionInfo sessionInfo) throws DataAccessException {
		String[] hql = generatePrivilegedObjectsHql(recordClassName, hqlJoin, hqlWhere, privilegeLevel, highLevelDisbaled, sessionInfo);
		return countRecordsByHqlClause(recordClassName, hql[0], hql[1], hqlGroupBy);
	}
	
	/**
	 * 生成获取用户有访问权限的记录列表的hql
	 * @param pojoClassName
	 * @param hqlJoin
	 * @param hqlWhere
	 * @param privilegeLevel
	 * @param highLevelDisbaled
	 * @param sessionInfo
	 * @return
	 */
	private String[] generatePrivilegedObjectsHql(String pojoClassName, String hqlJoin, String hqlWhere, char privilegeLevel, boolean highLevelDisbaled, SessionInfo sessionInfo) {
		String privilegePojoClassName = getPrivilegePojoClassName(pojoClassName);
		pojoClassName = pojoClassName.substring(pojoClassName.lastIndexOf(".") + 1);
		String hqlFrom = pojoClassName + " " + pojoClassName +
						 (hqlJoin==null ? "" : " " + hqlJoin) + "," +
						 privilegePojoClassName + " " + privilegePojoClassName;
		hqlWhere = pojoClassName + ".id=" + privilegePojoClassName + ".recordId" +
				   " and " + privilegePojoClassName + ".visitorId in (" + sessionInfo.getUserIds() + ")" +
				   " and " + privilegePojoClassName + ".accessLevel" + (highLevelDisbaled ?  "=" : ">=") + "'" + privilegeLevel + "'" +
				   (hqlWhere==null ? "" : " and (" + hqlWhere + ")");
		return new String[]{hqlFrom, hqlWhere};
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.DatabaseService#findRecordsByFilter(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List, int, int, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List findRecordsByFilter(String pojoClassName, String hqlSelect, String hqlJoin, String hqlWhere, String hqlGroupBy, String hqlOrderBy, String filter, List lazyLoadProperties, int offset, int limit, SessionInfo sessionInfo) throws DataAccessException {
		if(filter==null) {
			String pojoShortName = pojoClassName.substring(pojoClassName.lastIndexOf(".") + 1);
			String hqlFrom = pojoShortName + " " + pojoShortName + (hqlJoin==null ? "" : " " + hqlJoin);
			return findRecordsByHqlClause(pojoClassName, hqlSelect, hqlFrom, hqlWhere, hqlOrderBy, hqlGroupBy, lazyLoadProperties, offset, limit);
		}
		if(filter.equals("TODO")) { //待办
			return findTodoRecords(pojoClassName, hqlSelect, hqlJoin, hqlWhere, hqlOrderBy, hqlGroupBy, lazyLoadProperties, offset, limit, sessionInfo);
		}
		if(filter.equals("INPROCESS")) { //在办
		    return findProcessingRecords(pojoClassName, hqlSelect, hqlJoin, hqlWhere, hqlOrderBy, hqlGroupBy, lazyLoadProperties, offset, limit, sessionInfo);
		}
		if(filter.equals("COMPLETED")) { //已办结
		    return findCompletedRecords(pojoClassName, hqlSelect, hqlJoin, hqlWhere, hqlOrderBy, hqlGroupBy, lazyLoadProperties, offset, limit, sessionInfo);
		}
		if(filter.equals("READABLE") || filter.equals("EDITABLE")) { //可读,可编辑
			return findPrivilegedRecords(pojoClassName, hqlSelect, hqlJoin, hqlWhere, hqlOrderBy, hqlGroupBy, (filter.equals("READABLE") ? RecordControlService.ACCESS_LEVEL_READONLY :  RecordControlService.ACCESS_LEVEL_EDITABLE), false, lazyLoadProperties, offset, limit, sessionInfo);
		}
		throw new DataAccessException("Filter mode is not support");
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.DatabaseService#countRecordsByFilter(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public int countRecordsByFilter(String pojoClassName, String hqlJoin, String hqlWhere, String hqlGroupBy, String filter, SessionInfo sessionInfo) throws DataAccessException {
		if(filter==null) {
			pojoClassName = pojoClassName.substring(pojoClassName.lastIndexOf(".") + 1);
			String hqlForm = pojoClassName + " " + pojoClassName + (hqlJoin==null ? "" : " " + hqlJoin);
			return countRecordsByHqlClause(pojoClassName, hqlForm, hqlWhere, hqlGroupBy);
		}
		if(filter.equals("TODO")) { //待办
			return countTodoRecords(pojoClassName, hqlJoin, hqlWhere, hqlGroupBy, sessionInfo);
		}
		if(filter.equals("INPROCESS")) { //在办
		    return countProcessingRecords(pojoClassName, hqlJoin, hqlWhere, hqlGroupBy, sessionInfo);
		}
		if(filter.equals("COMPLETED")) { //已办结
		    return countCompletedRecords(pojoClassName, hqlJoin, hqlWhere, hqlGroupBy, sessionInfo);
		}
		if(filter.equals("READABLE") || filter.equals("EDITABLE")) { //可读,可编辑
			return countPrivilegedRecords(pojoClassName, hqlJoin, hqlWhere, hqlGroupBy, (filter.equals("READABLE") ? RecordControlService.ACCESS_LEVEL_READONLY :  RecordControlService.ACCESS_LEVEL_EDITABLE), false, sessionInfo);
		}
		throw new DataAccessException("Filter mode is not support");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#listTodoObjects(java.lang.Class, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, char, java.util.List, int, int, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List findTodoRecords(String recordClassName, String hqlSelect, String hqlJoin, String hqlWhere, String hqlOrderBy, String hqlGroupBy, List lazyLoadProperties, int offset, int max, SessionInfo sessionInfo) throws DataAccessException {
		String[] hql = generateTodoObjectsHql(recordClassName, hqlJoin, hqlWhere, sessionInfo);
		if(hqlOrderBy==null && hqlSelect==null) {
			hqlOrderBy = "workItems.created DESC";
		}
		if(lazyLoadProperties==null) {
			lazyLoadProperties = ListUtils.generateList("workItems", ",");
		}
		return findRecordsByHqlClause(recordClassName, hqlSelect, hql[0], hql[1], hqlOrderBy, hqlGroupBy, lazyLoadProperties, offset, max);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#countTodoObjects(java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public int countTodoRecords(String recordClassName, String hqlJoin, String hqlWhere, String hqlGroupBy, SessionInfo sessionInfo) throws DataAccessException {
		String[] hql = generateTodoObjectsHql(recordClassName, hqlJoin, hqlWhere, sessionInfo);
		return countRecordsByHqlClause(recordClassName, hql[0], hql[1], hqlGroupBy);
	}
	
	/**
	 * 生成获取待办记录的HQL
	 * @param pojoClassName
	 * @param hqlJoin
	 * @param hqlWhere
	 * @param sessionInfo
	 * @return
	 */
	private String[] generateTodoObjectsHql(String pojoClassName, String hqlJoin, String hqlWhere, SessionInfo sessionInfo) {
		pojoClassName = pojoClassName.substring(pojoClassName.lastIndexOf(".") + 1);
		if(hqlJoin==null) {
	        hqlJoin = " left join " + pojoClassName + ".workItems workItems";
	    }
	    else if(hqlJoin.indexOf(pojoClassName + ".workItems")==-1) {
	        hqlJoin += " left join " + pojoClassName + ".workItems workItems";
	    }
		String hqlFrom = pojoClassName + " " + pojoClassName + " " + hqlJoin;
		hqlWhere = "workItems.participantId in (" + sessionInfo.getUserIds() + ")" +
				   (hqlWhere==null ? "" : " and (" + hqlWhere + ")");
		return new String[]{hqlFrom, hqlWhere};
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#listProcessingObjects(java.lang.Class, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.util.List, int, int, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List findProcessingRecords(String recordClassName, String hqlSelect, String hqlJoin, String hqlWhere, String hqlOrderBy, String hqlGroupBy, List lazyLoadProperties, int offset, int max, SessionInfo sessionInfo) throws DataAccessException {
		String[] hql = generateProcessingObjectsHql(recordClassName, hqlJoin, hqlWhere, sessionInfo);
		if(lazyLoadProperties==null) {
			lazyLoadProperties = ListUtils.generateList("workItems", ",");
		}
		return findRecordsByHqlClause(recordClassName, hqlSelect, hql[0], hql[1], hqlOrderBy, hqlGroupBy, lazyLoadProperties, offset, max);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#countProcessingObjects(java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public int countProcessingRecords(String recordClassName, String hqlJoin, String hqlWhere, String hqlGroupBy, SessionInfo sessionInfo) throws DataAccessException {
		String[] hql = generateProcessingObjectsHql(recordClassName, hqlJoin, hqlWhere, sessionInfo);
		return countRecordsByHqlClause(recordClassName, hql[0], hql[1], hqlGroupBy);
	}
	
	/**
	 * 生成获取在办记录的HQL
	 * @param pojoClassName
	 * @param hqlJoin
	 * @param hqlWhere
	 * @param sessionInfo
	 * @return
	 * @throws DataAccessException
	 */
	private String[] generateProcessingObjectsHql(String pojoClassName, String hqlJoin, String hqlWhere, SessionInfo sessionInfo) throws DataAccessException {
		String privilegePojoClassName = getPrivilegePojoClassName(pojoClassName);
		pojoClassName = pojoClassName.substring(pojoClassName.lastIndexOf(".") + 1);
		if(hqlJoin==null) {
	        hqlJoin = " left join " + pojoClassName + ".workItems workItems";
	    }
	    else if(hqlJoin.indexOf(pojoClassName + ".workItems")==-1) {
	        hqlJoin += " left join " + pojoClassName + ".workItems workItems";
	    }
		String hqlFrom = pojoClassName + " " + pojoClassName + " " +
						 hqlJoin  + "," +
						 privilegePojoClassName + " " + privilegePojoClassName;
		
		hqlWhere = "workItems.id>0" + //必须有工作项
				   " and " + pojoClassName + ".id not in (" + //当前用户不是办理人
				   "   select WorkItem.recordId from WorkItem WorkItem" +
				   "    where WorkItem.recordId=" + pojoClassName + ".id" +
				   "    and WorkItem.participantId in (" + sessionInfo.getUserIds() + ")" +
				   ")" +
				   " and " + pojoClassName + ".id=" + privilegePojoClassName + ".recordId" +
				   " and " + privilegePojoClassName + ".accessLevel>'" + RecordControlService.ACCESS_LEVEL_PREREAD + "'" +
				   " and " + privilegePojoClassName + ".visitorId in (" + sessionInfo.getUserIds() + ")" +
				   (hqlWhere==null ? "" : " and (" + hqlWhere + ")");
		return new String[]{hqlFrom, hqlWhere};
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#listCompletedObjects(java.lang.Class, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.util.List, int, int, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List findCompletedRecords(String recordClassName, String hqlSelect, String hqlJoin, String hqlWhere, String hqlOrderBy, String hqlGroupBy, List lazyLoadProperties, int offset, int max, SessionInfo sessionInfo) throws DataAccessException {
		String[] hql = generateCompletedObjectsHql(recordClassName, hqlJoin, hqlWhere, sessionInfo);
		return findRecordsByHqlClause(recordClassName, hqlSelect, hql[0], hql[1], hqlOrderBy, hqlGroupBy, lazyLoadProperties, offset, max);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#countCompletedObjects(java.lang.Class, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public int countCompletedRecords(String recordClassName, String hqlJoin, String hqlWhere, String hqlGroupBy, SessionInfo sessionInfo) throws DataAccessException {
		String[] hql = generateCompletedObjectsHql(recordClassName, hqlJoin, hqlWhere, sessionInfo);
		return countRecordsByHqlClause(recordClassName, hql[0], hql[1], hqlGroupBy);
	}
	
	/**
	 * 生成获取办结记录的HQL
	 * @param pojoClassName
	 * @param hqlJoin
	 * @param hqlWhere
	 * @param sessionInfo
	 * @return
	 * @throws DataAccessException
	 */
	private String[] generateCompletedObjectsHql(String pojoClassName, String hqlJoin, String hqlWhere, SessionInfo sessionInfo) throws DataAccessException {
		String privilegePojoClassName = getPrivilegePojoClassName(pojoClassName);
		pojoClassName = pojoClassName.substring(pojoClassName.lastIndexOf(".") + 1);
		if(hqlJoin==null) {
	        hqlJoin = " left join " + pojoClassName + ".workItems workItems";
	    }
	    else if(hqlJoin.indexOf(pojoClassName + ".workItems")==-1) {
	        hqlJoin += " left join " + pojoClassName + ".workItems workItems";
	    }
		String hqlFrom = pojoClassName + " " + pojoClassName + " " +
						 hqlJoin + "," +
						 privilegePojoClassName + " " + privilegePojoClassName; 
		
		hqlWhere = "workItems is null" + // 没有工作项
				   " and " + pojoClassName + ".id=" + privilegePojoClassName + ".recordId" +
				   " and " + privilegePojoClassName + ".accessLevel>'" + RecordControlService.ACCESS_LEVEL_PREREAD + "'" +
				   " and " + privilegePojoClassName + ".visitorId in (" + sessionInfo.getUserIds() + ")" +
				   (hqlWhere==null ? "" : " and (" + hqlWhere + ")");
		return new String[]{hqlFrom, hqlWhere};
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#retrieveObjects(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List, int, int)
	 */
	public List findRecordsByHqlClause(String recordClassName, String hqlSelect, String hqlFrom, String hqlWhere, String hqlOrderBy, String hqlGroupBy, List lazyLoadProperties, int offset, int max)  throws DataAccessException {
		if(hqlGroupBy!=null && !hqlGroupBy.isEmpty()) { //有分组
			//把分组的字段全部加入到hqlOrderBy中
			String[] groupFields = hqlGroupBy.split(",");
			for(int i=0; i<groupFields.length; i++) {
				if(hqlOrderBy==null || hqlOrderBy.indexOf(groupFields[i].trim())==-1) {
					hqlOrderBy = (hqlOrderBy==null ? "" : hqlOrderBy + ",") + groupFields[i];
				}
			}
		}
		recordClassName = recordClassName.substring(recordClassName.lastIndexOf(".") + 1);
		if(hqlSelect!=null) { //有指定输出的字段
			if(hqlOrderBy!=null) {
				hqlSelect += "," + (hqlOrderBy + " ").replaceAll("(?i) DESC([,\\s])", " $1").replaceAll("(?i) ASC([,\\s])", " $1");
			}
			String hql = "select " + (isMultipleTablesQuery(hqlFrom) || hqlSelect.indexOf(".id")==-1 ?  "distinct " : "") + hqlSelect +
			 	  		 " from " + hqlFrom +
			 	  		 (hqlWhere==null ? "" : " where " + hqlWhere) +
			 	  		 (hqlGroupBy==null ? "" : " group by " + hqlGroupBy) +
			 	  		 (hqlOrderBy==null ? "" : " order by " + hqlOrderBy);
			return findRecordsByHql(hql, lazyLoadProperties, offset, max);
		}
		
		//没有指定输出的字段,先获取记录ID,再按记录ID获取列表
		if(hqlGroupBy!=null) { //有分组时,返回id查询hql,详细信息根据id逐一获取
			hqlSelect = "select max(" + recordClassName + ".id)";
		}
		else {
			hqlSelect = "select " + (isMultipleTablesQuery(hqlFrom)  ? "distinct " : "") + recordClassName + ".id";
			if(hqlOrderBy!=null) {
				hqlSelect += "," + (hqlOrderBy + " ").replaceAll("(?i) DESC([,\\s])", " $1").replaceAll("(?i) ASC([,\\s])", " $1");
			}
		}
		String hql = hqlSelect +
		 	  		 " from " + hqlFrom +
		 	  		 (hqlWhere==null ? "" : " where " + hqlWhere) +
		 	  		 (hqlGroupBy==null ? "" : " group by " + hqlGroupBy) +
		 	  		 (hqlOrderBy==null ? "" : " order by " + hqlOrderBy);
		
		//获取ID列表
		List idList = findRecordsByHql(hql, offset, max);
		if(idList==null || idList.isEmpty()) {
		    return null;
		}
		
		//连接ID
		String ids = null;
		for(int i=0; i<idList.size(); i++) {
			Object obj = idList.get(i);
			if(obj instanceof Object[]) {
				obj = ((Object[])obj)[0];
			}
			ids = (ids==null ? "" : ids + ",") + obj;
		}
		hql = "from " + recordClassName + " " + recordClassName + " where " + recordClassName + ".id in (" + JdbcUtils.validateInClauseNumbers(ids) + ")";
		return ListUtils.sortByProperty(findRecordsByHql(hql, lazyLoadProperties, 0, 0), "id", ids);
	}
	
	/**
	 * 判断是否多表查询
	 * @param hqlFrom
	 * @return
	 */
	private boolean isMultipleTablesQuery(String hqlFrom) {
		return hqlFrom.indexOf(",")!=-1 || hqlFrom.indexOf("join")!=-1;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.BaseDAO#countObjects(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public int countRecordsByHqlClause(String recordClassName, String hqlFrom, String hqlWhere, String hqlGroupBy) throws DataAccessException {
		recordClassName = recordClassName.substring(recordClassName.lastIndexOf(".") + 1);
		if(hqlGroupBy==null) {
			String hql = "select count(" + (isMultipleTablesQuery(hqlFrom) ? "distinct " : "") + recordClassName + ".id" + ") " +
						 " from " + hqlFrom +
				  		 (hqlWhere==null ? "" : " where " + hqlWhere);
			Number count = (Number)findRecordByHql(hql);
			return count==null ? 0 : ((Number)count).intValue();
		}
		else { //有分组时,返回子查询
			String hql = "select " + hqlGroupBy +
				  		 " from " + hqlFrom +
				  		 (hqlWhere==null ? "" : " where " + hqlWhere) +
				  		 " group by " + hqlGroupBy;
			return retrieveSubQueryRecordCount(hql); //获取记录数
		}
	}
	
	/**
	 * 获取权限POJO名称
	 * @param pojoClassName
	 * @return
	 */
	private String getPrivilegePojoClassName(String pojoClassName) {
		for(int i=0; i<5; i++) {
			try {
				String className = pojoClassName + "Privilege";
				Class.forName(className); //检查类是否存在
				return className.substring(className.lastIndexOf('.') + 1);
			}
			catch (ClassNotFoundException e) {
				
			}
			try {
				pojoClassName = Class.forName(pojoClassName).getSuperclass().getName(); //查找父类对应的权限POJO
			}
			catch (ClassNotFoundException e) {
				
			}
		}
		return null;
	}
	
	/**
	 * 获取子查询返回的记录数
	 * @param hql
	 * @return
	 * @throws DataAccessException
	 */
	private int retrieveSubQueryRecordCount(String hql) throws DataAccessException {
		CountSubQueryRecordCallback countSubQueryRecordCallback;
		try {
			countSubQueryRecordCallback = new CountSubQueryRecordCallback(hql);
			Object count = getHibernateTemplate().execute(countSubQueryRecordCallback);
			return ((Integer)count).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw(new DataAccessException(e.getMessage()));
		}
		finally {
			countSubQueryRecordCallback = null;
			getHibernateTemplate().clear();
		}
	}
	
	/**
	 * 统计子查询记录数类
	 * @author linchuan
	 *
	 */
	private class CountSubQueryRecordCallback implements HibernateCallback {
		private String hql;
		
		public CountSubQueryRecordCallback(String hql) {
			super();
			this.hql = hql;
		}

		public Object doInHibernate(Session session) throws HibernateException, SQLException {
			Statement statement = null;
			ResultSet resultSet = null;
			try {
				String sql = ((SessionFactoryImpl)session.getSessionFactory()).getQuery(databaseDialect.replaceFunction(hql), false)[0].getSQLString();
				sql = "select count(*) from (" + sql + ")";
				if(!getDbServerName().startsWith("oracle")) {
					sql += "as subQuery";
				}
				statement = session.connection().createStatement();
				resultSet = statement.executeQuery(sql);
				resultSet.next();
				return new Integer(resultSet.getInt(1));
			}
			catch(Exception e) {
				e.printStackTrace();
				return new Integer(0);
			}
			finally {
				try {
					resultSet.close();
				}
				catch(Exception e) {
					
				}
				resultSet = null;
				try {
					statement.close();
				}
				catch(Exception e) {
					
				}
				statement = null;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.DatabaseService#createTable(com.yuanluesoft.jeaf.database.model.Table)
	 */
	public void createTable(Table table) throws DataAccessException {
		//创建表
		String sql = "create table " + table.getTableName() + " (\n";
		for(int i=0; i<table.getColumns().size(); i++) {
			TableColumn tableColumn = (TableColumn)table.getColumns().get(i);
			String sqlType = databaseDialect.getColumnSqlType(tableColumn.getType(), tableColumn.getLength());
			if(sqlType==null) {
				continue;
			}
			sql += tableColumn.getName() + " " + sqlType + (i==0 ? " not" : "") + " null,\n";
		}
		sql += databaseDialect.generatePrimaryKeySql(table.getTableName(), table.getPrimaryKey()) + ");";
		executeSql(databaseDialect.ddlSqlCase(sql));
		
		//创建索引
		for(Iterator iterator = table.getIndexes()==null ? null : table.getIndexes().iterator(); iterator.hasNext();) {
			TableIndex index = (TableIndex)iterator.next();
			createIndex(table.getTableName(), index.getIndexName(), index.getIndexColumns());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.DatabaseService#dropTable(java.lang.String)
	 */
	public void dropTable(String tableName) throws DataAccessException {
		String sql = "create table " + tableName + ";";
		executeSql(databaseDialect.ddlSqlCase(sql));
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.DatabaseService#createIndex(java.lang.String, java.lang.String, java.util.List)
	 */
	public void createIndex(String tableName, String indexName, String indexColumns) throws DataAccessException {
		String sql = "create index " + indexName + " on " + tableName + " (" + indexColumns + ");";
		executeSql(databaseDialect.ddlSqlCase(sql));
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.DatabaseService#dropIndex(java.lang.String, java.lang.String)
	 */
	public void dropIndex(String tableName, String indexName) throws DataAccessException {
		String sql = databaseDialect.generateDropIndexSql(tableName, indexName);
		executeSql(databaseDialect.ddlSqlCase(sql));
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.DatabaseService#addTableColumn(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addTableColumn(String tableName, String columnName, String columnType, String columnLength) throws DataAccessException {
		String sqlType = databaseDialect.getColumnSqlType(columnType, columnLength);
		if(sqlType==null) {
			return;
		}
		String sql = "alter table " + tableName + " add " + columnName + " " + sqlType + ";";
		executeSql(databaseDialect.ddlSqlCase(sql));
		if("number".equals(columnType) || "char".equals(columnType)) {
			sql = "update " + tableName + " set " + columnName + "=" + ("number".equals(columnType) ? "0" : "'0'") + " where " + columnName + " is null;";
			executeSql(databaseDialect.ddlSqlCase(sql));
		}
	}
	
	/**
	 * 修改列
	 * @param tableName
	 * @param columnName
	 * @param columnType
	 * @param columnLength
	 * @return
	 * @throws DataAccessException
	 */
	public void modifyTableColumn(String tableName, String columnName, String columnType, String columnLength) throws DataAccessException {
		String sqlType = databaseDialect.getColumnSqlType(columnType, columnLength);
		if(sqlType==null) {
			return;
		}
		String sql = databaseDialect.generateAlterColumnSql(tableName, columnName, sqlType);
		executeSql(databaseDialect.ddlSqlCase(sql));
		if("number".equals(columnType) || "char".equals(columnType)) {
			sql = "update " + tableName + " set " + columnName + "=" + ("number".equals(columnType) ? "0" : "'0'") + " where " + columnName + " is null;";
			executeSql(databaseDialect.ddlSqlCase(sql));
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.DatabaseService#addOrModifyTableColumn(java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public void addOrModifyTableColumn(String tableName, String columnName, String columnType, String columnLength, boolean renameColumnIfNeed) throws DataAccessException {
		//添加字段
		try {
			addTableColumn(tableName, columnName, columnType, columnLength);
			return;
		}
		catch(Exception e) {
			
		}
		//修改字段
		try {
			modifyTableColumn(tableName, columnName, columnType, columnLength);
			return;
		}
		catch(Exception e) {
			
		}
		if(!renameColumnIfNeed) {
			throw new DataAccessException("add or modify table column failed");
		}
		//重命名原来的字段
		String newColumnName = (columnName.length()<24 ? columnName : columnName.substring(0, 24)) + "_" + new Random().nextInt(100000);
		renameTableColumnName(tableName, columnName, newColumnName, columnType, columnLength);
		//添加新字段
		addTableColumn(tableName, columnName, columnType, columnLength);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.DatabaseService#dropTableColumn(java.lang.String, java.lang.String)
	 */
	public void dropTableColumn(String tableName, String columnName) throws DataAccessException {
		String sql = "alter table " + tableName + " drop " + columnName + ";";
		executeSql(databaseDialect.ddlSqlCase(sql));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.DatabaseService#changeTableColumnName(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void renameTableColumnName(String tableName, String columnName, String newColumnName, String columnType, String columnLength) throws DataAccessException {
		String sqlType = databaseDialect.getColumnSqlType(columnType, columnLength);
		if(sqlType==null) {
			return;
		}
		String sql = databaseDialect.generateRenameColumnSql(tableName, columnName, newColumnName, sqlType);
		executeSql(databaseDialect.ddlSqlCase(sql));
	}

	/**
	 * @return the databaseDialect
	 */
	public DatabaseDialect getDatabaseDialect() {
		return databaseDialect;
	}

	/**
	 * @param databaseDialect the databaseDialect to set
	 */
	public void setDatabaseDialect(DatabaseDialect databaseDialect) {
		this.databaseDialect = databaseDialect;
	}
}