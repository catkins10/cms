package com.yuanluesoft.jeaf.tools.databasebrowse.forms;

import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.tree.model.Tree;

/**
 * 
 * @author linchuan
 *
 */
public class DatabaseBrowse extends ActionForm {
	private String jdbcURL;
	private String jdbcUserName;
	private String jdbcPassword;
	private Tree tableTree; //表列表
	private String sql; //需要运行的SQL语句
	private int limit = 50; //返回记录数上限
	
	/**
	 * @return the jdbcPassword
	 */
	public String getJdbcPassword() {
		return jdbcPassword;
	}
	/**
	 * @param jdbcPassword the jdbcPassword to set
	 */
	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}
	/**
	 * @return the jdbcURL
	 */
	public String getJdbcURL() {
		return jdbcURL;
	}
	/**
	 * @param jdbcURL the jdbcURL to set
	 */
	public void setJdbcURL(String jdbcURL) {
		this.jdbcURL = jdbcURL;
	}
	/**
	 * @return the jdbcUserName
	 */
	public String getJdbcUserName() {
		return jdbcUserName;
	}
	/**
	 * @param jdbcUserName the jdbcUserName to set
	 */
	public void setJdbcUserName(String jdbcUserName) {
		this.jdbcUserName = jdbcUserName;
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
	 * @return the tableTree
	 */
	public Tree getTableTree() {
		return tableTree;
	}
	/**
	 * @param tableTree the tableTree to set
	 */
	public void setTableTree(Tree tableTree) {
		this.tableTree = tableTree;
	}
}