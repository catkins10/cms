package com.yuanluesoft.jeaf.database.dialect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author linchuan
 *
 */
public class DatabaseDialect {
	
	/**
	 * 初始化
	 *
	 */
	public void init() {
		Environment.setDatabaseDialect(this);
	}
	
	/**
	 * 读入CLOB字段中的文本
	 * @param rs
	 * @param fieldName
	 * @return
	 * @throws SQLException
	 */
	public String readClobText(ResultSet rs, String fieldName) throws SQLException {
		Reader clobReader = rs.getCharacterStream(fieldName);
		if(clobReader == null) {
			return null;
		}
		BufferedReader bufferedClobReader = new BufferedReader(clobReader);
		StringBuffer buffer = new StringBuffer();
		char[] readBuffer = new char[4096];
		int readLen;
		try {
			while((readLen=bufferedClobReader.read(readBuffer, 0, 4096))>0 ) {
				buffer.append(readBuffer, 0, readLen);
			}
		}
		catch (IOException e) {
			throw new SQLException( e.toString() );
		}
		finally {
			try {
				bufferedClobReader.close();
			}
			catch(Exception e) {
				
			}
			readBuffer = null;
		}
		return buffer.toString();
	}
	
	/**
	 * 保存CLOB文本
	 * @param st
	 * @param value
	 * @param index
	 * @throws SQLException
	 */
	public void writeClobText(PreparedStatement st, String value, int index) throws SQLException {
		if(value==null) {
			value = "";
		}
		st.setString(index, (String)value);
	}
	
	/**
	 * 清理临时的CLOB,针对ORACLE
	 * @throws SQLException
	 */
	public void freeTemporaryClob() throws SQLException {
		
	}
	
	/**
	 * 替换sql中的日期、时间字段
	 * @param sql
	 * @return
	 */
	public String replaceFunction(String sql) {
		//替换DATE和TIMESTAMP
		return sql.replaceAll("DATE\\((.{10})\\)", "'$1'").replaceAll("TIMESTAMP\\((.{19})\\)", "'$1'");
	}
	
	/**
	 * 给SQL加入分页代码
	 * @param sql
	 * @param offset
	 * @param limit
	 * @param connection
	 * @return
	 */
	public String generatePagingSql(String sql, int offset, int limit) {
		if(limit<=0 && offset<=0) {
			return sql;
		}
		return sql + " limit " + offset + (limit>0 ? "," + limit : "");
	}
	
	/**
	 * 获取列对应的SQL类型
	 * @param columnType 列类型,包括:varchar/text/number/date/timestamp
	 * @param columnLength
	 * @return
	 */
	public String getColumnSqlType(String columnType, String columnLength) {
		if("varchar".equals(columnType)) { //文本
			return "varchar(" + columnLength + ")";
		}
		else if("text".equals(columnType)) { //长文本
			return "text";
		}
		else if("number".equals(columnType)) { //数字
			return "numeric(" + columnLength + ")";
		}
		else if("char".equals(columnType)) { //文本
			return "char(" + columnLength + ")";
		}
		else if("date".equals(columnType) || "timestamp".equals(columnType)) { //日期、时间
			return columnType;
		}
		return null;
	}
	
	/**
	 * 生成创建主键的SQL
	 * @param tableName
	 * @param primaryKeyColumnName
	 * @return
	 */
	public String generatePrimaryKeySql(String tableName, String primaryKeyColumnName) {
		return "primary key (" + primaryKeyColumnName + ")";
	}
	
	/**
	 * 对DDL SQL做大小写转换
	 * @return
	 */
	public String ddlSqlCase(String ddlSql) {
		return ddlSql.toLowerCase();
	}
	
	/**
	 * 生成删除索引的SQL
	 * @param tableName
	 * @param indexName
	 * @return
	 */
	public String generateDropIndexSql(String tableName, String indexName) {
		return "drop index " + indexName + ";";
	}
	
	/**
	 * 生成修改列的SQL
	 * @param tableName
	 * @param columnName
	 * @param columnType
	 * @return
	 */
	public String generateAlterColumnSql(String tableName, String columnName, String columnSqlType) {
		return "alter table " + tableName + " modify " + columnName + " " + columnSqlType + ";";
	}
	
	/**
	 * 生成修改列名称的SQL
	 * @param tableName
	 * @param columnName
	 * @param columnType
	 * @return
	 */
	public String generateRenameColumnSql(String tableName, String columnName, String newColumnName, String columnSqlType) {
		return "alter table " + tableName + " rename column " + columnName + " to " + newColumnName + ";";
	}
}