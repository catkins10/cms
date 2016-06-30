/*
 * Created on 2004-12-18
 *
 */
package com.yuanluesoft.jeaf.database;

import java.util.List;

import com.yuanluesoft.jeaf.database.exception.DataAccessException;
import com.yuanluesoft.jeaf.database.model.Table;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public interface DatabaseService {
	
	/**
	 * 获取数据库服务器名称,如mysql,oracle等
	 * @return
	 * @throws DataAccessException
	 */
	public abstract String getDbServerName() throws DataAccessException;
	
	/**
	 * 获取JDBC URL
	 * @return
	 * @throws DataAccessException
	 */
	public abstract String getJdbcURL() throws DataAccessException; 
	
	/**
	 * Saves an object
	 * @param record
	 */
	public abstract Record saveRecord(Record record) throws DataAccessException;
	
	/**
	 * update an object
	 * @param record
	 * @return
	 */
	public abstract Record updateRecord(Record record) throws DataAccessException;
	
	/**
	 * deleet an object
	 * @param record
	 */
	public abstract void deleteRecord(Record record) throws DataAccessException;
	
	/**
	 * deleet an object by id
	 * @param recordClassName
	 * @param id
	 * @throws DataAccessException
	 */
	public void deleteRecordById(String recordClassName, long id)  throws DataAccessException;
	
	/**
	 * Looks up an object by ID.
	 * @param id the ID for the object
	 * @return Object
	 */
	public abstract Record findRecordById(String recordClassName, long id) throws DataAccessException;

	/**
	 * Looks up an object by ID, then auto load lazyLoadProperties
	 * @param recordClassName
	 * @param id
	 * @param lazyLoadProperties
	 * @return
	 * @throws DataAccessException
	 */
	public abstract Record findRecordById(String recordClassName, long id, List lazyLoadProperties) throws DataAccessException;

	/**
	 * Looks up an object by hql.
	 * @param hql
	 * @return
	 */
	public abstract Object findRecordByHql(String hql) throws DataAccessException;
	
	/**
	 * Looks up an object by hql, then auto load lazyLoadProperties
	 * @param hql
	 * @param lazyLoadProperties
	 * @return
	 * @throws DataAccessException
	 */
	public abstract Object findRecordByHql(String hql, List lazyLoadProperties) throws DataAccessException;
	
	/**
	 * Looks up objects by hql.
	 * @param hql
	 * @return
	 */
	public abstract List findRecordsByHql(String hql) throws DataAccessException;
	
	/**
	 * Looks up objects by hql.
	 * @param hql
	 * @param lazyLoadProperties
	 * @return
	 */
	public abstract List findRecordsByHql(String hql, List lazyLoadProperties) throws DataAccessException;

	/**
	 * Looks up objects by hql.
	 * @param hql
	 * @param first
	 * @param max
	 * @return
	 */
	public abstract List findRecordsByHql(String hql, int first, int max) throws DataAccessException;

	/**
	 * Looks up objects by hql.
	 * @param hql
	 * @param lazyLoadProperties
	 * @param first
	 * @param max
	 * @return
	 */
	public abstract List findRecordsByHql(String hql, List lazyLoadProperties, int first, int max) throws DataAccessException;

	/**
	 * Looks up an object by key.
	 * @param recordClassName
	 * @param keyName
	 * @param keyValue
	 * @return
	 */
	public abstract Record findRecordByKey(String recordClassName, String keyName, Object keyValue) throws DataAccessException;
	/**
	 * 
	 * @param recordClassName
	 * @param keyName
	 * @param keyValue
	 * @return
	 * @throws DataAccessException
	 */
	public abstract Record findRecordByKey(String recordClassName, String keyName, Object keyValue, List lazyLoadProperties) throws DataAccessException;

	/**
	 * Looks up objects by key.
	 * @param recordClassName
	 * @param keyName
	 * @param keyValue
	 * @return
	 */
	public abstract List findRecordsByKey(String recordClassName, String keyName, Object keyValue) throws DataAccessException;

	/**
	 * Looks up objects by key.
	 * @param recordClassName
	 * @param keyName
	 * @param keyValue
	 * @param first
	 * @param max
	 * @return
	 * @throws Exception
	 */
	public abstract List findRecordsByKey(String recordClassName, String keyName, Object keyValue, int first, int max) throws DataAccessException;

	/**
	 * delete objects by hql.
	 * @param hql
	 */
	public abstract void deleteRecordsByHql(String hql) throws DataAccessException;

	/**
	 * flush
	 * @return
	 * @throws DataAccessException
	 */
	public abstract void flush() throws DataAccessException;

	/**
	 * 获取sequence的值
	 * @param sequenceName
	 * @return
	 * @throws DataAccessException
	 */
	public long getSequenceValue(String sequenceName) throws DataAccessException;
		
	/**
	 * 按sql获取记录
	 * @param sql
	 * @param returnAliases
	 * @param returnClasses
	 * @return
	 * @throws DataAccessException
	 */
	public abstract List findRecordsBySql(String sql, String[] returnAliases, Class[] returnClasses) throws DataAccessException;
	
	/**
	 * 执行SQL查询
	 * @param sql
	 * @param offset 其实记录位置
	 * @param limit 获取的记录数, limit==0,表示获取全部
	 * @return 记录列表,记录采用HashMap存储
	 * @throws DataAccessException
	 */
	public SqlResultList executeQueryBySql(String sql, int offset, int limit) throws DataAccessException;
	
	/**
	 * 执行SQL
	 * @param sql
	 * @throws DataAccessException
	 */
	public void executeSql(String sql) throws DataAccessException;
	
	/**
	 * 获取用户有访问权限权限的记录列表
	 * @param recordClassName
	 * @param hqlSelect
	 * @param hqlJoin
	 * @param hqlWhere
	 * @param hqlOrderBy
	 * @param hqlGroupBy
	 * @param privilegeLevel
	 * @param highLevelDisbaled
	 * @param lazyLoadProperties
	 * @param offset
	 * @param max
	 * @param sessionInfo
	 * @return
	 * @throws DataAccessException
	 */
	public List findPrivilegedRecords(String recordClassName, String hqlSelect, String hqlJoin, String hqlWhere, String hqlOrderBy, String hqlGroupBy, char privilegeLevel, boolean highLevelDisbaled, List lazyLoadProperties, int offset, int max, SessionInfo sessionInfo) throws DataAccessException;
	
	/**
	 * 获取用户有访问权限权限的记录数量
	 * @param recordClassName
	 * @param hqlJoin
	 * @param hqlWhere
	 * @param hqlGroupBy
	 * @param privilegeLevel
	 * @param highLevelDisbaled
	 * @param sessionInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int countPrivilegedRecords(String recordClassName, String hqlJoin, String hqlWhere, String hqlGroupBy, char privilegeLevel, boolean highLevelDisbaled, SessionInfo sessionInfo) throws DataAccessException;
	
	/**
	 * 按指定的过滤方式获取记录列表
	 * @param pojoClassName
	 * @param hqlSelect
	 * @param hqlJoin
	 * @param hqlWhere
	 * @param hqlGroupBy
	 * @param hqlOrderBy
	 * @param filter
	 * @param lazyLoadProperties
	 * @param offset
	 * @param limit
	 * @param sessionInfo
	 * @return
	 * @throws DataAccessException
	 */
	public List findRecordsByFilter(String pojoClassName, String hqlSelect, String hqlJoin, String hqlWhere, String hqlGroupBy, String hqlOrderBy, String filter, List lazyLoadProperties, int offset, int limit, SessionInfo sessionInfo) throws DataAccessException;

	/**
	 * 按指定的过滤方式获取记录数
	 * @param pojoClassName
	 * @param hqlJoin
	 * @param hqlWhere
	 * @param hqlGroupBy
	 * @param filter
	 * @param sessionInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int countRecordsByFilter(String pojoClassName, String hqlJoin, String hqlWhere, String hqlGroupBy, String filter, SessionInfo sessionInfo) throws DataAccessException;
	
	/**
	 * 获取用户待办的记录列表
	 * @param recordClassName
	 * @param hqlSelect
	 * @param hqlJoin
	 * @param hqlWhere
	 * @param hqlOrderBy
	 * @param hqlGroupBy
	 * @param lazyLoadProperties
	 * @param offset
	 * @param max
	 * @param sessionInfo
	 * @return
	 * @throws DataAccessException
	 */
	public List findTodoRecords(String recordClassName, String hqlSelect, String hqlJoin, String hqlWhere, String hqlOrderBy, String hqlGroupBy, List lazyLoadProperties, int offset, int max, SessionInfo sessionInfo) throws DataAccessException;
	
	/**
	 * 获取用户待办的记录数量
	 * @param recordClassName
	 * @param hqlJoin
	 * @param hqlWhere
	 * @param hqlGroupBy
	 * @param sessionInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int countTodoRecords(String recordClassName, String hqlJoin, String hqlWhere, String hqlGroupBy, SessionInfo sessionInfo) throws DataAccessException;
	
	/**
	 * 获取处理中的记录列表
	 * @param recordClassName
	 * @param hqlSelect
	 * @param hqlJoin
	 * @param hqlWhere
	 * @param hqlOrderBy
	 * @param hqlGroupBy
	 * @param lazyLoadProperties
	 * @param offset
	 * @param max
	 * @param sessionInfo
	 * @return
	 * @throws DataAccessException
	 */
	public List findProcessingRecords(String recordClassName, String hqlSelect, String hqlJoin, String hqlWhere, String hqlOrderBy, String hqlGroupBy, List lazyLoadProperties, int offset, int max, SessionInfo sessionInfo) throws DataAccessException;
	
	/**
	 * 获取处理中的记录数量
	 * @param recordClassName
	 * @param hqlJoin
	 * @param hqlWhere
	 * @param hqlGroupBy
	 * @param sessionInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int countProcessingRecords(String recordClassName, String hqlJoin, String hqlWhere, String hqlGroupBy, SessionInfo sessionInfo) throws DataAccessException;
	
	/**
	 * 获取办结的记录列表
	 * @param recordClassName
	 * @param hqlSelect
	 * @param hqlJoin
	 * @param hqlWhere
	 * @param hqlOrderBy
	 * @param hqlGroupBy
	 * @param lazyLoadProperties
	 * @param offset
	 * @param max
	 * @param sessionInfo
	 * @return
	 * @throws DataAccessException
	 */
	public List findCompletedRecords(String recordClassName, String hqlSelect, String hqlJoin, String hqlWhere, String hqlOrderBy, String hqlGroupBy, List lazyLoadProperties, int offset, int max, SessionInfo sessionInfo) throws DataAccessException;
	
	/**
	 * 获取办结的记录数量
	 * @param recordClassName
	 * @param hqlJoin
	 * @param hqlWhere
	 * @param hqlGroupBy
	 * @param sessionInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int countCompletedRecords(String recordClassName, String hqlJoin, String hqlWhere, String hqlGroupBy, SessionInfo sessionInfo) throws DataAccessException;
	
	/**
	 * 根据hql子句获取记录集,注:有分组时,排序字段必须在分组字段中
	 * @param recordClassName
	 * @param hqlSelect
	 * @param hqlFrom
	 * @param hqlWhere
	 * @param hqlOrderBy
	 * @param hqlGroupBy
	 * @param lazyLoadProperties
	 * @param offset
	 * @param max
	 * @return
	 * @throws DataAccessException
	 */
	public List findRecordsByHqlClause(String recordClassName, String hqlSelect, String hqlFrom, String hqlWhere, String hqlOrderBy, String hqlGroupBy, List lazyLoadProperties, int offset, int max) throws DataAccessException;
	
	/**
	 * 根据hql子句统计记录数
	 * @param recordClassName
	 * @param hqlFrom
	 * @param hqlWhere
	 * @param hqlGroupBy
	 * @return
	 * @throws DataAccessException
	 */
	public int countRecordsByHqlClause(String recordClassName, String hqlFrom, String hqlWhere, String hqlGroupBy) throws DataAccessException;
	
	/**
	 * 创建表
	 * @param table
	 * @throws DataAccessException
	 */
	public void createTable(Table table) throws DataAccessException;
	
	/**
	 * 删除表
	 * @param tableName
	 * @return
	 * @throws DataAccessException
	 */
	public void dropTable(String tableName) throws DataAccessException;
	
	/**
	 * 创建索引
	 * @param tableName
	 * @param indexName
	 * @param indexColumns
	 * @return
	 * @throws DataAccessException
	 */
	public void createIndex(String tableName, String indexName, String indexColumns) throws DataAccessException;
	
	/**
	 * 删除索引
	 * @param tableName
	 * @param indexName
	 * @return
	 * @throws DataAccessException
	 */
	public void dropIndex(String tableName, String indexName) throws DataAccessException;
	
	/**
	 * 添加列
	 * @param tableName
	 * @param columnName
	 * @param columnType
	 * @param columnLength
	 * @return
	 * @throws DataAccessException
	 */
	public void addTableColumn(String tableName, String columnName, String columnType, String columnLength) throws DataAccessException;
	
	/**
	 * 修改列
	 * @param tableName
	 * @param columnName
	 * @param columnType
	 * @param columnLength
	 * @return
	 * @throws DataAccessException
	 */
	public void modifyTableColumn(String tableName, String columnName, String columnType, String columnLength) throws DataAccessException;
	
	/**
	 * 添加或者修改列
	 * @param tableName
	 * @param columnName
	 * @param columnType
	 * @param columnLength
	 * @param renameColumnIfNeed 是否在需要时修改列名称
	 * @return
	 * @throws DataAccessException
	 */
	public void addOrModifyTableColumn(String tableName, String columnName, String columnType, String columnLength, boolean renameColumnIfNeed) throws DataAccessException;
	
	/**
	 * 删除列
	 * @param tableName
	 * @param columnName
	 * @return
	 * @throws DataAccessException
	 */
	public void dropTableColumn(String tableName, String columnName) throws DataAccessException;
	
	/**
	 * 修改列名称
	 * @param tableName
	 * @param columnName
	 * @param newColumnName
	 * @param columnType
	 * @param columnLength
	 * @return
	 * @throws DataAccessException
	 */
	public void renameTableColumnName(String tableName, String columnName, String newColumnName, String columnType, String columnLength) throws DataAccessException;
}