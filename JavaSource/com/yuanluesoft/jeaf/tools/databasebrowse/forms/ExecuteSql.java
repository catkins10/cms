package com.yuanluesoft.jeaf.tools.databasebrowse.forms;

import com.yuanluesoft.jeaf.database.SqlResultList;
import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class ExecuteSql extends ActionForm {
	private String sql;
	private int limit; //返回记录数上限
	private SqlResultList resultList;
	private String executeResult;
	
	/**
	 * @return the executeResult
	 */
	public String getExecuteResult() {
		return executeResult;
	}
	/**
	 * @param executeResult the executeResult to set
	 */
	public void setExecuteResult(String executeResult) {
		this.executeResult = executeResult;
	}
	/**
	 * @return the sql
	 */
	public String getSql() {
		return sql;
	}
	/**
	 * @param sql the sql to set
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}
	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}
	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
	/**
	 * @return the resultList
	 */
	public SqlResultList getResultList() {
		return resultList;
	}
	/**
	 * @param resultList the resultList to set
	 */
	public void setResultList(SqlResultList resultList) {
		this.resultList = resultList;
	}
}