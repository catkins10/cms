package com.yuanluesoft.jeaf.database.dialect.oracle;

import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import oracle.sql.CLOB;

import com.yuanluesoft.jeaf.database.dialect.DatabaseDialect;
import com.yuanluesoft.jeaf.logger.Logger;

/**
 * 
 * @author linchuan
 *
 */
public class OracleDatabaseDialect extends DatabaseDialect {
	private ThreadLocal threadLocal = new ThreadLocal();
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.dialect.DatabaseDialect#writeClobText(java.sql.PreparedStatement, java.lang.String, int)
	 */
	public void writeClobText(PreparedStatement st, String value, int index) throws SQLException {
		Connection connection = st.getConnection().getMetaData().getConnection();
		/*if(connectionClassName.equals("weblogic.jdbc.extensions.WLConnection")) { //for weblogic
			//connection = ((WLConnection)conn).getVendorConnection();
			temporaryClob = CLOB.createTemporary(connection, true, CLOB.DURATION_SESSION);
		}*/
		CLOB clob = CLOB.createTemporary(connection, true, CLOB.DURATION_SESSION);
		if(Logger.isTraceEnabled()) {
			Logger.trace("OracleDatabaseDialect: create temporary clob");
		}
		List temporaryClobs = (List)threadLocal.get();
		if(temporaryClobs==null) {
			temporaryClobs = new ArrayList();
			threadLocal.set(temporaryClobs);
		}
		temporaryClobs.add(clob);

		Writer clobWriter = null;
		try {
			clob.open(CLOB.MODE_READWRITE);
			clobWriter = clob.setCharacterStream(0);
			clobWriter.write((String)value);
			clobWriter.flush();
		}
		catch (IOException e) {
			throw new SQLException(e);
		}
		finally {
			try { clobWriter.close(); } catch(Exception e) { }
			try { clob.close(); } catch(Exception e) { e.printStackTrace(); }
		}
		st.setClob(index, clob);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.dialect.DatabaseDialect#freeTemporaryClob()
	 */
	public void freeTemporaryClob() throws SQLException {
		List temporaryClobs = (List)threadLocal.get();
		for(Iterator iterator = temporaryClobs==null ? null : temporaryClobs.iterator(); iterator!=null && iterator.hasNext();) {
			CLOB clob = (CLOB)iterator.next();
			if(Logger.isTraceEnabled()) {
				Logger.trace("OracleDatabaseDialect: free temporary clob");
			}
			try {
				clob.freeTemporary();
			}
			catch(Exception e) {
				Logger.exception(e);
			}
			iterator.remove();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.dialect.DatabaseDialect#replaceDateTimeField(java.lang.String)
	 */
	public String replaceFunction(String sql) {
		return sql.replaceAll("DATE\\((.{10})\\)", "to_date('$1', 'YYYY-MM-DD')") //DATE
				  .replaceAll("TIMESTAMP\\((.{19})\\)", "to_date('$1', 'YYYY-MM-DD HH24:MI:SS')") //TIMESTAMP
				  .replaceAll("year\\(([^\\)]*)\\)", "to_char($1, 'YYYY')") //year
				  .replaceAll("month\\(([^\\)]*)\\)", "to_char($1, 'MM')") //month
				  .replaceAll("day\\(([^\\)]*)\\)", "to_char($1, 'DD')") //day
				  .replaceAll("hour\\(([^\\)]*)\\)", "to_char($1, 'HH24')") //hour
				  .replaceAll("minute\\(([^\\)]*)\\)", "to_char($1, 'MI')") //minute
				  .replaceAll("second\\(([^\\)]*)\\)", "to_char($1, 'SS')") //second
				  //.replaceAll("([\\( ])([^ \\(]*) like[ \r\n]*'%(.*)%'([ \r\n\\)]?)", "$1instr($2, '$3')>0$4") //like 替换为 instr
				  .replaceAll(" DESC", " DESC NULLS LAST"); //降序排序,空值放在后面
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.dialect.DatabaseDialect#generatePagingSql(java.lang.String, int, int)
	 */
	public String generatePagingSql(String sql, int offset, int limit) {
		if(limit<=0 && offset<=0) {
			return sql;
		}
		sql = "select result_.*, rownum rownum_ from (" + sql + ") result_ where rownum<=" + (offset + limit);
		if(offset>0) {
			sql = "select * from (" + sql + ") where rownum_>" + offset;
		}
		return sql;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.dialect.DatabaseDialect#generatePrimaryKeySql(java.lang.String, java.lang.String)
	 */
	public String generatePrimaryKeySql(String tableName, String primaryKeyColumnName) {
		return "constraint pk_" + (tableName.length()<=27 ? tableName : tableName.substring(0, 27)) + " primary key (" + primaryKeyColumnName + ")";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.dialect.DatabaseDialect#getColumnSqlType(java.lang.String, java.lang.String)
	 */
	public String getColumnSqlType(String columnType, String columnLength) {
		if("varchar".equals(columnType)) { //文本
			return "varchar2(" + columnLength + ")";
		}
		else if("text".equals(columnType)) { //长文本
			return "clob";
		}
		else if("number".equals(columnType)) { //数字
			return "number(" + columnLength + ")";
		}
		return super.getColumnSqlType(columnType, columnLength);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.dialect.DatabaseDialect#ddlSqlCase(java.lang.String)
	 */
	public String ddlSqlCase(String ddlSql) {
		return ddlSql.toUpperCase();
	}
}