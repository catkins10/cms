package com.yuanluesoft.jeaf.database.dialect.postgres;

import com.yuanluesoft.jeaf.database.dialect.DatabaseDialect;

/**
 * 
 * @author linchuan
 *
 */
public class PostgresDatabaseDialect extends DatabaseDialect {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.dialect.DatabaseDialect#replaceDateTimeField(java.lang.String)
	 */
	public String replaceFunction(String sql) {
		sql = sql.replaceAll("DATE\\((.{10})\\)", "date '$1'") //DATE
		  		 .replaceAll("TIMESTAMP\\((.{19})\\)", "timestamp '$1'") //TIMESTAMP
		  		 .replaceAll("year\\(([^\\)]*)\\)", "date_part('year', $1)") //year
		  		 .replaceAll("month\\(([^\\)]*)\\)", "date_part('month', $1)") //month
		  		 .replaceAll("day\\(([^\\)]*)\\)", "date_part('day', $1)") //day
		  		 .replaceAll("hour\\(([^\\)]*)\\)", "date_part('hour', $1)") //hour
		  		 .replaceAll("minute\\(([^\\)]*)\\)", "date_part('minute', $1)") //minute
		  		 .replaceAll("second\\(([^\\)]*)\\)", "date_part('second', $1)") //second
		  		 .replaceAll(" DESC", " DESC NULLS LAST"); //降序排序,空值放在后面
		for(int i=0; i<20 && sql.indexOf("concat(")!=-1; i++) {
			sql = sql.replaceAll("concat\\(([^,]*),([^\\)]*)\\)", "($1 || $2)"); //concat
		}
		return sql;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.dialect.DatabaseDialect#generatePagingSql(java.lang.String, int, int)
	 */
	public String generatePagingSql(String sql, int offset, int limit) {
		if(limit<=0 && offset<=0) {
			return sql;
		}
		return sql + " offset " + offset + (limit>0 ? " limit " + limit : "");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.dialect.DatabaseDialect#generatePrimaryKeySql(java.lang.String, java.lang.String)
	 */
	public String generatePrimaryKeySql(String tableName, String primaryKeyColumnName) {
		return "constraint pk_" + (tableName.length()<=27 ? tableName : tableName.substring(0, 27)) + " primary key (" + primaryKeyColumnName + ")";
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.dialect.DatabaseDialect#generateAlterColumnSql(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String generateAlterColumnSql(String tableName, String columnName, String columnType) {
		return "alter table " + tableName + " alter " + columnName + " type " + columnType + ";";
	}
}