package com.yuanluesoft.jeaf.util;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.jeaf.database.SqlResult;
import com.yuanluesoft.jeaf.database.SqlResultList;
import com.yuanluesoft.jeaf.database.dialect.DatabaseDialect;
import com.yuanluesoft.jeaf.database.dialect.access.AccessDatabaseDialect;
import com.yuanluesoft.jeaf.database.dialect.mysql.MysqlDatabaseDialect;
import com.yuanluesoft.jeaf.database.dialect.oracle.OracleDatabaseDialect;
import com.yuanluesoft.jeaf.database.dialect.postgres.PostgresDatabaseDialect;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.logger.Logger;

/**
 * 
 * @author linchuan
 *
 */
public class JdbcUtils {
	
	/**
	 * 重设单引号
	 * @param value
	 * @return
	 */
	public static String resetQuot(String value) {
		return value==null ? null : value.replaceAll("'", "''");
	}
	
	/**
	 * 校验IN子句中的数字,避免SQL注入
	 * @param values
	 * @throws ValidateException
	 */
	public static String validateInClauseNumbers(String values) {
		try {
			if(values==null || values.isEmpty()) {
				return values;
			}
			String[] numbers = values.split(",");
			for(int i=0; i<numbers.length; i++) {
				Double.parseDouble(numbers[i]);
			}
			return values;
		}
		catch(Exception e) {
			throw new Error(e);
		}
	}

	/**
	 * 给SQL加入分页代码
	 * @param sql
	 * @param offset
	 * @param limit
	 * @param connection
	 * @return
	 */
	public static String generatePagingSql(String sql, int offset, int limit, Connection connection) {
		try {
			if(!sql.toLowerCase().trim().startsWith("select")) { //不是select语句
				return sql;
			}
			if(limit<=0 && offset<=0) {
				return sql;
			}
			sql = sql.trim();
			if(sql.endsWith(";")) {
				sql = sql.substring(0, sql.length()-1);
			}
			String jdbcURL = connection.getMetaData().getURL();
			if(jdbcURL.indexOf("mysql")!=-1) { //mysql
				if(sql.toLowerCase().indexOf(" limit ")==-1) {
					sql += " limit " + offset + "," + limit;
				}
			}
			else if(jdbcURL.indexOf("oracle")!=-1) { //oracle
				if(sql.toLowerCase().indexOf("rownum")==-1) {
					sql = "select result_.*, rownum rownum_ from (" + sql + ") result_" +
					" where rownum<=" + (offset + limit);
					if(offset>0) {
						sql = "select * from (" + sql + ") where rownum_>" + offset;
					}
				}
			}
			else if(jdbcURL.indexOf("postgres")!=-1) { //postgres
				if(sql.toLowerCase().indexOf(" limit ")==-1) {
					sql += " offset " + offset + (limit>0 ? " limit " + limit : "");
				}
			}
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		return sql;
	}
	
	/**
	 * 按JDBC URL获取JDBC驱动名称
	 * @param jdbcUrl
	 * @return
	 */
	public static String getJdbcDriver(String jdbcUrl) {
		jdbcUrl = jdbcUrl.trim().toLowerCase();
		String jdbcDriver = null;
		if(jdbcUrl.indexOf("jdbc:odbc:driver")!=-1) { //sun.jdbc.odbc.JdbcOdbcDriver|jdbc:odbc:Driver={MicroSoft Access Driver (*.mdb)};DBQ=[文件路径]
			jdbcDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
		}
		else if(jdbcUrl.indexOf("jdbc:sybase:tds")!=-1) { //com.sybase.jdbc2.jdbc.SybDriver|jdbc:sybase:Tds:[IP]:5000/[数据库名称]?CharSet=cp936
			jdbcDriver = "com.sybase.jdbc2.jdbc.SybDriver";
		}
		else if(jdbcUrl.indexOf("jdbc:jtds:sqlserver")!=-1) { //net.sourceforge.jtds.jdbc.Driver|jdbc:jtds:sqlserver://[IP]:1433;DatabaseName=[数据库名称]
			jdbcDriver = "net.sourceforge.jtds.jdbc.Driver";
		}
		else if(jdbcUrl.indexOf("jdbc:mysql")!=-1) { //org.gjt.mm.mysql.Driver|jdbc:mysql://[服务器IP]/[数据库名称]?useUnicode=true&characterEncoding=gbk
			jdbcDriver = "org.gjt.mm.mysql.Driver";
		}
		else if(jdbcUrl.indexOf("jdbc:postgresql")!=-1) { //org.postgresql.Driver|jdbc:postgresql://[服务器IP]:5432/[数据库名称]
			jdbcDriver = "org.postgresql.Driver";
		}
		else if(jdbcUrl.indexOf("jdbc:oracle:thin")!=-1) { //oracle.jdbc.driver.OracleDriver|jdbc:oracle:thin:@[服务器IP]:1521:[数据库名称]
			jdbcDriver = "oracle.jdbc.driver.OracleDriver";
		}
		return jdbcDriver;
	}
	
	/**
	 * 获取表列表
	 * @param jdbcDriver
	 * @param jdbcUrl
	 * @param jdbcUserName
	 * @param jdbcPassword
	 * @return
	 */
	public static List listTableNames(String jdbcUrl, String jdbcUserName, String jdbcPassword) throws Exception {
		String sql = null;
		if(jdbcUrl.toLowerCase().indexOf("jdbc:sybase:tds")!=-1) { //com.sybase.jdbc2.jdbc.SybDriver|jdbc:sybase:Tds:[IP]:5000/[数据库名称]?CharSet=cp936
			sql = "select name from sysobjects where type='U' order by name";
		}
		else if(jdbcUrl.toLowerCase().indexOf("jdbc:jtds:sqlserver")!=-1) { //net.sourceforge.jtds.jdbc.Driver|jdbc:jtds:sqlserver://[IP]:1433;DatabaseName=[数据库名称]
			sql = "select name from SysObjects where XType='U' order by name";
		}
		else if(jdbcUrl.toLowerCase().indexOf("jdbc:mysql")!=-1) { //org.gjt.mm.mysql.Driver|jdbc:mysql://[服务器IP]/[数据库名称]?useUnicode=true&characterEncoding=gbk
			sql = "show tables";
		}
		else if(jdbcUrl.toLowerCase().indexOf("jdbc:postgresql")!=-1) { //org.postgresql.Driver|jdbc:postgresql://[服务器IP]:5432/[数据库名称]
			sql = "select tablename from pg_tables where schemaname='public' order by tablename";
		}
		else if(jdbcUrl.toLowerCase().indexOf("jdbc:oracle:thin")!=-1) { //oracle.jdbc.driver.OracleDriver|jdbc:oracle:thin:@[服务器IP]:1521:[数据库名称]
			sql = "select table_name from user_tables order by table_name";
		}
		else {
			return null;
		}
		SqlResultList resultList = (SqlResultList)executeSql(jdbcUrl, jdbcUserName, jdbcPassword, sql, 3000, true);
		for(int i=0; i<resultList.size(); i++) {
			SqlResult sqlResult = (SqlResult)resultList.get(i);
			String fieldName = (String)resultList.getFieldNames().get(0);
			resultList.set(i, sqlResult.get(fieldName));
		}
		return resultList;
	}
	
	/**
	 * 获取字段列表
	 * @param jdbcUrl
	 * @param jdbcUserName
	 * @param jdbcPassword
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public static SqlResultList listTableFields(String jdbcUrl, String jdbcUserName, String jdbcPassword, String tableName) throws Exception {
		String sql = null;
		if(jdbcUrl.toLowerCase().indexOf("jdbc:sybase:tds")!=-1) { //com.sybase.jdbc2.jdbc.SybDriver|jdbc:sybase:Tds:[IP]:5000/[数据库名称]?CharSet=cp936
			sql = "sp_columns " + tableName;
		}
		else if(jdbcUrl.toLowerCase().indexOf("jdbc:jtds:sqlserver")!=-1) { //net.sourceforge.jtds.jdbc.Driver|jdbc:jtds:sqlserver://[IP]:1433;DatabaseName=[数据库名称]
			sql = "sp_columns " + tableName;
		}
		else if(jdbcUrl.toLowerCase().indexOf("jdbc:mysql")!=-1) { //org.gjt.mm.mysql.Driver|jdbc:mysql://[服务器IP]/[数据库名称]?useUnicode=true&characterEncoding=gbk
			sql = "desc " + tableName;
		}
		else if(jdbcUrl.toLowerCase().indexOf("jdbc:postgresql")!=-1) { //org.postgresql.Driver|jdbc:postgresql://[服务器IP]:5432/[数据库名称]
			sql = "select" +
				  " a.attname as name," +
				  " format_type(a.atttypid,a.atttypmod) as type," +
				  " a.attnotnull as notnull," +
				  " col_description(a.attrelid,a.attnum) as comment" +
				  " from pg_class as c,pg_attribute as a" +
				  " where c.relname='" + tableName + "'" +
				  " and a.attrelid=c.oid" +
				  " and a.attnum>0" +
				  " and a.attisdropped='f'";
		}
		else if(jdbcUrl.toLowerCase().indexOf("jdbc:oracle:thin")!=-1) { //oracle.jdbc.driver.OracleDriver|jdbc:oracle:thin:@[服务器IP]:1521:[数据库名称]
			sql = "desc " + tableName;
		}
		else {
			return null;
		}
		return (SqlResultList)executeSql(jdbcUrl, jdbcUserName, jdbcPassword, sql, 1000, true);
	}
	
	/**
	 * 运行SQL,返回记录集列表获取执行结果
	 * @param jdbcDriver
	 * @param jdbcUrl
	 * @param jdbcUserName
	 * @param jdbcPassword
	 * @param sql
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public static Object executeSql(String jdbcUrl, String jdbcUserName, String jdbcPassword, String sql, int limit, boolean readOnly) throws Exception {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Class.forName(getJdbcDriver(jdbcUrl));
			connection = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
			if(readOnly) {
				connection.setReadOnly(true);
			}
			statement = connection.createStatement();
			sql = generatePagingSql(sql, 0, limit, connection);
			if(Logger.isDebugEnabled()) {
				Logger.debug("execute sql " + sql);
			}
			statement.execute(sql);
			resultSet = statement.getResultSet();
			if(resultSet!=null) {
				return convertToSqlResultList(resultSet, (limit<=0 ? 50 : limit));
			}
			else {
				return "Processed " + statement.getUpdateCount() + " rows.";
			}
		}
		finally {
			try {
				resultSet.close();
			}
			catch (Exception e) {
				
			}
			try {
				statement.close();
			}
			catch (Exception e) {
				
			}
			try {
				connection.close();
			}
			catch (Exception e) {
				
			}
		}
	}
	
	/**
	 * 按JDBC URL获取数据库方言
	 * @param jdbcUrl
	 * @return
	 */
	public static DatabaseDialect getDatabaseDialect(String jdbcUrl) {
		jdbcUrl = jdbcUrl.trim();
		DatabaseDialect databaseDialect = null;
		if(jdbcUrl.indexOf("jdbc:mysql")!=-1) { //org.gjt.mm.mysql.Driver|jdbc:mysql://[服务器IP]/[数据库名称]?useUnicode=true&characterEncoding=gbk
			databaseDialect = new MysqlDatabaseDialect();
		}
		else if(jdbcUrl.indexOf("jdbc:postgresql")!=-1) { //org.postgresql.Driver|jdbc:postgresql://[服务器IP]:5432/[数据库名称]
			databaseDialect = new PostgresDatabaseDialect();
		}
		else if(jdbcUrl.indexOf("jdbc:oracle:thin")!=-1) { //oracle.jdbc.driver.OracleDriver|jdbc:oracle:thin:@[服务器IP]:1521:[数据库名称]
			databaseDialect = new OracleDatabaseDialect();
		}
		else if(jdbcUrl.indexOf("mdb")!=-1) { //jdbc:odbc:Driver={MicroSoft Access Driver (*.mdb)};DBQ=[文件路径]
			databaseDialect = new AccessDatabaseDialect();
		}
		return databaseDialect==null ? new DatabaseDialect() : databaseDialect;
	}
	
	/**
	 * 转换为SqlResult
	 * @param resultSet
	 * @param limit
	 * @return
	 */
	public static SqlResultList convertToSqlResultList(ResultSet resultSet, int limit) {
		try {
			ResultSetMetaData metaData = resultSet.getMetaData();
			String charset = null;
			String jdbcURL = resultSet.getStatement().getConnection().getMetaData().getURL();
			if(jdbcURL.indexOf("mysql")!=-1) { //myslq,通过url获取当前数据库的字符集
				int index = jdbcURL.indexOf("characterEncoding=");
				if(index!=-1) {
					index += "characterEncoding=".length();
					int endIndex = jdbcURL.indexOf('&', index);
					charset = (endIndex==-1 ? jdbcURL.substring(index) : jdbcURL.substring(index, endIndex));
					if(charset.compareToIgnoreCase("utf-8")==0) {
						charset = null;
					}
				}
			}
			String[] columnNames = new String[metaData.getColumnCount()];
			for(int i=1; i<=metaData.getColumnCount(); i++) {
				String columnName;
				if(charset==null) {
					columnName = metaData.getColumnName(i);
				}
				else {
					try {
						columnName = new String(metaData.getColumnName(i).getBytes(charset), "utf-8");
					}
					catch (UnsupportedEncodingException e) {
						columnName = metaData.getColumnName(i);
					}
				}
				columnNames[i-1] =  columnName;
			}
			SqlResultList resultList = new SqlResultList();
			resultList.setFieldNames(ListUtils.generateListFromArray(columnNames));
			for(int count=0; (limit==0 || count<limit) && resultSet.next(); count++) {
				SqlResult sqlResult = new SqlResult();
				for(int i=0; i<columnNames.length; i++) {
					Object columnValue = resultSet.getObject(i + 1);
					if(columnValue instanceof Clob) { //Clob,转换为字符串
						columnValue = resultSet.getString(i + 1);
					}
					else if(columnValue instanceof Blob) { //Blob,转换为二进制数组
						columnValue = resultSet.getBytes(i + 1);
					}
					sqlResult.put(columnNames[i], columnValue);
				}
				resultList.add(sqlResult);
			}
			return resultList;
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new Error(e.getMessage());
		}
	}
	
	/**
	 * 获取文本字段值
	 * @param rs
	 * @param fieldName
	 * @return
	 */
	public static String getString(ResultSet resultSet, String fieldName) {
		try {
			return resultSet.getString(fieldName);
		}
		catch (SQLException e) {
			return null;
		}
	}
	
	/**
	 * 获取字符字段值
	 * @param resultSet
	 * @param fieldName
	 * @return
	 */
	public static char getChar(ResultSet resultSet, String fieldName) {
		try {
			return resultSet.getString(fieldName).charAt(0);
		}
		catch (SQLException e) {
			return ' ';
		}
	}
	
	/**
	 * 获取整型字段值
	 * @param resultSet
	 * @param fieldName
	 * @return
	 */
	public static int getInt(ResultSet resultSet, String fieldName) {
		try {
			return resultSet.getInt(fieldName);
		}
		catch (SQLException e) {
			return 0;
		}
	}
	
	/**
	 * 获取长整型字段值
	 * @param resultSet
	 * @param fieldName
	 * @return
	 */
	public static long getLong(ResultSet resultSet, String fieldName) {
		try {
			return resultSet.getLong(fieldName);
		}
		catch (SQLException e) {
			return 0;
		}
	}
	
	/**
	 * 获取浮点数字段值
	 * @param resultSet
	 * @param fieldName
	 * @return
	 */
	public static float getFloat(ResultSet resultSet, String fieldName) {
		try {
			return resultSet.getFloat(fieldName);
		}
		catch (SQLException e) {
			return 0;
		}
	}
	
	/**
	 * 获取双精度浮点数字段值
	 * @param resultSet
	 * @param fieldName
	 * @return
	 */
	public static double getDouble(ResultSet resultSet, String fieldName) {
		try {
			return resultSet.getDouble(fieldName);
		}
		catch (SQLException e) {
			return 0;
		}
	}
	
	/**
	 * 获取时间字段值
	 * @param resultSet
	 * @param fieldName
	 * @return
	 */
	public static Timestamp getTimestamp(ResultSet resultSet, String fieldName) {
		try {
			return resultSet.getTimestamp(fieldName);
		}
		catch (SQLException e) {
			return null;
		}
	}
	
	/**
	 * 获取日期字段值
	 * @param resultSet
	 * @param fieldName
	 * @return
	 */
	public static Date getDate(ResultSet resultSet, String fieldName) {
		try {
			return resultSet.getDate(fieldName);
		}
		catch (SQLException e) {
			return null;
		}
	}
	
	/**
	 * 拷贝resultSet字段值到bean
	 * @param bean
	 * @param resultSet
	 */
	public static void copyFields(Object bean, ResultSet resultSet) {
		PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(bean);
		for(int i=0; i<properties.length; i++) {
			try {
				String propertyName = properties[i].getName();
				Object propertyValue = null;
				if(properties[i].getPropertyType().equals(String.class)) {
					propertyValue = resultSet.getString(propertyName);
				}
				else if(properties[i].getPropertyType().equals(long.class)) {
					propertyValue = new Long(resultSet.getLong(propertyName));
				}
				else if(properties[i].getPropertyType().equals(int.class)) {
					propertyValue = new Integer(resultSet.getInt(propertyName));
				}
				else if(properties[i].getPropertyType().equals(float.class)) {
					propertyValue = new Float(resultSet.getFloat(propertyName));
				}
				else if(properties[i].getPropertyType().equals(double.class)) {
					propertyValue = new Double(resultSet.getDouble(propertyName));
				}
				else if(properties[i].getPropertyType().equals(char.class)) {
					String str = resultSet.getString(propertyName);
					propertyValue = str.charAt(0)==0 ? null : new Character(str.charAt(0));
				}
				else if(properties[i].getPropertyType().equals(Date.class)) {
					propertyValue = resultSet.getDate(propertyName);
				}
				else if(properties[i].getPropertyType().equals(Timestamp.class)) {
					propertyValue = resultSet.getTimestamp(propertyName);
				}
				if(propertyValue!=null) {
					PropertyUtils.setProperty(bean, propertyName, propertyValue);
				}
			}
			catch(Exception e) {
				
			}
		}
	}
	
	/**
	 * 拷贝sqlResult字段值到bean
	 * @param bean
	 * @param resultSet
	 */
	public static void copyFields(Object bean, SqlResult sqlResult) {
		PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(bean);
		for(int i=0; i<properties.length; i++) {
			try {
				String propertyName = properties[i].getName();
				Object propertyValue = null;
				if(properties[i].getPropertyType().equals(String.class)) {
					propertyValue = sqlResult.get(propertyName).toString();
				}
				else if(properties[i].getPropertyType().equals(long.class)) {
					propertyValue = new Long(sqlResult.get(propertyName).toString());
				}
				else if(properties[i].getPropertyType().equals(int.class)) {
					propertyValue = new Integer(sqlResult.get(propertyName).toString());
				}
				else if(properties[i].getPropertyType().equals(float.class)) {
					propertyValue = new Float(sqlResult.get(propertyName).toString());
				}
				else if(properties[i].getPropertyType().equals(double.class)) {
					propertyValue = new Double(sqlResult.get(propertyName).toString());
				}
				else if(properties[i].getPropertyType().equals(char.class)) {
					String str = sqlResult.get(propertyName).toString();
					propertyValue = str.charAt(0)==0 ? null : new Character(str.charAt(0));
				}
				else if(properties[i].getPropertyType().equals(Date.class)) {
					Object value = sqlResult.get(propertyName);
					if(value instanceof Date) {
						propertyValue = value;
					}
					else if(value instanceof Timestamp) {
						propertyValue = new Date(((Timestamp)value).getTime());
					}
					else if(value instanceof String) {
						propertyValue = DateTimeUtils.parseDate((String)value, null);
					}
					else if(value instanceof Number) {
						propertyValue = new Date(((Number)value).longValue());
					}
				}
				else if(properties[i].getPropertyType().equals(Timestamp.class)) {
					Object value = sqlResult.get(propertyName);
					if(value instanceof Timestamp) {
						propertyValue = value;
					}
					else if(value instanceof Date) {
						propertyValue = new Timestamp(((Date)value).getTime());
					}
					else if(value instanceof String) {
						try {
							propertyValue = DateTimeUtils.parseTimestamp((String)value, null);
						}
						catch(Exception e) {
							propertyValue = new Timestamp(DateTimeUtils.parseDate((String)value, null).getTime());
						}
					}
					else if(value instanceof Number) {
						propertyValue = new Timestamp(((Number)value).longValue());
					}
				}
				if(propertyValue!=null) {
					PropertyUtils.setProperty(bean, propertyName, propertyValue);
				}
			}
			catch(Exception e) {
				
			}
		}
	}
}