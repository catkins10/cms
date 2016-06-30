package com.yuanluesoft.jeaf.database.dialect.mysql;

import com.yuanluesoft.jeaf.database.dialect.DatabaseDialect;

/**
 * 
 * @author linchuan
 *
 */
public class MysqlDatabaseDialect extends DatabaseDialect {
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.dialect.DatabaseDialect#getColumnSqlType(java.lang.String, java.lang.String)
	 */
	public String getColumnSqlType(String columnType, String columnLength) {
		if("text".equals(columnType)) {
			return "mediumtext";
		}
		return super.getColumnSqlType(columnType, columnLength);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.dialect.DatabaseDialect#generateDropIndexSql(java.lang.String, java.lang.String)
	 */
	public String generateDropIndexSql(String tableName, String indexName) {
		return "drop index " + indexName + " on " + tableName + ";";
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.dialect.DatabaseDialect#generateRenameColumnSql(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String generateRenameColumnSql(String tableName, String columnName, String newColumnName, String columnSqlType) {
		return "alter table " + tableName + " change " + columnName + " " + newColumnName + " " + columnSqlType + ";";
	}
}