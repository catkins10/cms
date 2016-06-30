package com.yuanluesoft.jeaf.database.dialect.access;

import com.yuanluesoft.jeaf.database.dialect.DatabaseDialect;

/**
 * 
 * @author linchuan
 *
 */
public class AccessDatabaseDialect extends DatabaseDialect {
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.dialect.DatabaseDialect#replaceFunction(java.lang.String)
	 */
	public String replaceFunction(String sql) {
		//替换DATE和TIMESTAMP
		return sql.replaceAll("DATE\\((.{10})\\)", "#$1#").replaceAll("TIMESTAMP\\((.{19})\\)", "#$1#");
	}
}