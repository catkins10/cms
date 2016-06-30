/*
 * Created on 2004-12-29
 *
 */
package com.yuanluesoft.jeaf.taglib;

import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.yuanluesoft.jeaf.database.DatabaseService;

/**
 * 
 * @author linchuan
 *
 */
public class IterateSqlTag extends org.apache.struts.taglib.logic.IterateTag {
	private WebApplicationContext webApplicationContext = null;
	
	private String sql = null; //查询语句
	private int rowOffset = 0; //位移
	private int rowLimit = 20; //记录数
	private String databaseServiceName = null; //数据库服务名称
	//动态dao名称
	protected String scopeDaoName = null;
	protected String nameDaoName = null;
	protected String propertyDaoName = null;
	//动态SQL
	protected String scopeSQL = null;
	protected String nameSQL = null;
	protected String propertySQL = null;
	//动态位移
	protected String scopeRowOffset = null;
	protected String nameRowOffset = null;
	protected String propertyRowOffset = null;
	//动态记录数
	protected String scopeRowLimit = null;
	protected String nameRowLimit = null;
	protected String propertyRowLimit = null;
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		try {
			if(webApplicationContext==null) {
				webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(pageContext.getServletContext());
			}
			if(nameDaoName!=null || propertyDaoName!=null) {
				if(nameDaoName==null) {
					nameDaoName = Constants.BEAN_KEY;
				}
				databaseServiceName = (String)TagUtils.getInstance().lookup(pageContext, nameDaoName, propertyDaoName, scopeDaoName);
			}
			DatabaseService databaseService = (DatabaseService)webApplicationContext.getBean(databaseServiceName==null ? "databaseService" : databaseServiceName);
			if(nameSQL!=null || propertySQL!=null) {
				if(nameSQL==null) {
					nameSQL = Constants.BEAN_KEY;
				}
				sql = (String)TagUtils.getInstance().lookup(pageContext, nameSQL, propertySQL, scopeSQL);
			}
			if(nameRowOffset!=null || propertyRowOffset!=null) {
				if(nameRowOffset==null) {
					nameRowOffset = Constants.BEAN_KEY;
				}
				rowOffset = ((Number)TagUtils.getInstance().lookup(pageContext, nameRowOffset, propertyRowOffset, scopeRowOffset)).intValue();
			}
			if(nameRowLimit!=null || propertyRowLimit!=null) {
				if(nameRowLimit==null) {
					nameRowLimit = Constants.BEAN_KEY;
				}
				rowLimit = ((Number)TagUtils.getInstance().lookup(pageContext, nameRowLimit, propertyRowLimit, scopeRowLimit)).intValue();
			}
			collection = databaseService.executeQueryBySql(sql, rowOffset, rowLimit);
			return super.doStartTag();
		}
		finally {
			collection = null;
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#release()
	 */
	public void release() {
		sql = null; //查询语句
		rowOffset = 0; //位移
		rowLimit = 20; //记录数
		databaseServiceName = null; //DAO名称
		//动态dao名称
		scopeDaoName = null;
		nameDaoName = null;
		propertyDaoName = null;
		//动态SQL
		scopeSQL = null;
		nameSQL = null;
		propertySQL = null;
		//动态位移
		scopeRowOffset = null;
		nameRowOffset = null;
		propertyRowOffset = null;
		//动态记录数
		scopeRowLimit = null;
		nameRowLimit = null;
		propertyRowLimit = null;
		super.release();
	}

	/**
	 * @return the nameRowLimit
	 */
	public String getNameRowLimit() {
		return nameRowLimit;
	}

	/**
	 * @param nameRowLimit the nameRowLimit to set
	 */
	public void setNameRowLimit(String nameRowLimit) {
		this.nameRowLimit = nameRowLimit;
	}

	/**
	 * @return the nameRowOffset
	 */
	public String getNameRowOffset() {
		return nameRowOffset;
	}

	/**
	 * @param nameRowOffset the nameRowOffset to set
	 */
	public void setNameRowOffset(String nameRowOffset) {
		this.nameRowOffset = nameRowOffset;
	}

	/**
	 * @return the nameSQL
	 */
	public String getNameSQL() {
		return nameSQL;
	}

	/**
	 * @param nameSQL the nameSQL to set
	 */
	public void setNameSQL(String nameSQL) {
		this.nameSQL = nameSQL;
	}

	/**
	 * @return the propertyRowLimit
	 */
	public String getPropertyRowLimit() {
		return propertyRowLimit;
	}

	/**
	 * @param propertyRowLimit the propertyRowLimit to set
	 */
	public void setPropertyRowLimit(String propertyRowLimit) {
		this.propertyRowLimit = propertyRowLimit;
	}

	/**
	 * @return the propertyRowOffset
	 */
	public String getPropertyRowOffset() {
		return propertyRowOffset;
	}

	/**
	 * @param propertyRowOffset the propertyRowOffset to set
	 */
	public void setPropertyRowOffset(String propertyRowOffset) {
		this.propertyRowOffset = propertyRowOffset;
	}

	/**
	 * @return the propertySQL
	 */
	public String getPropertySQL() {
		return propertySQL;
	}

	/**
	 * @param propertySQL the propertySQL to set
	 */
	public void setPropertySQL(String propertySQL) {
		this.propertySQL = propertySQL;
	}

	/**
	 * @return the rowLimit
	 */
	public int getRowLimit() {
		return rowLimit;
	}

	/**
	 * @param rowLimit the rowLimit to set
	 */
	public void setRowLimit(int rowLimit) {
		this.rowLimit = rowLimit;
	}

	/**
	 * @return the rowOffset
	 */
	public int getRowOffset() {
		return rowOffset;
	}

	/**
	 * @param rowOffset the rowOffset to set
	 */
	public void setRowOffset(int rowOffset) {
		this.rowOffset = rowOffset;
	}

	/**
	 * @return the scopeRowLimit
	 */
	public String getScopeRowLimit() {
		return scopeRowLimit;
	}

	/**
	 * @param scopeRowLimit the scopeRowLimit to set
	 */
	public void setScopeRowLimit(String scopeRowLimit) {
		this.scopeRowLimit = scopeRowLimit;
	}

	/**
	 * @return the scopeRowOffset
	 */
	public String getScopeRowOffset() {
		return scopeRowOffset;
	}

	/**
	 * @param scopeRowOffset the scopeRowOffset to set
	 */
	public void setScopeRowOffset(String scopeRowOffset) {
		this.scopeRowOffset = scopeRowOffset;
	}

	/**
	 * @return the scopeSQL
	 */
	public String getScopeSQL() {
		return scopeSQL;
	}

	/**
	 * @param scopeSQL the scopeSQL to set
	 */
	public void setScopeSQL(String scopeSQL) {
		this.scopeSQL = scopeSQL;
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
	 * @return the nameDaoName
	 */
	public String getNameDaoName() {
		return nameDaoName;
	}

	/**
	 * @param nameDaoName the nameDaoName to set
	 */
	public void setNameDaoName(String nameDaoName) {
		this.nameDaoName = nameDaoName;
	}

	/**
	 * @return the webApplicationContext
	 */
	public WebApplicationContext getWebApplicationContext() {
		return webApplicationContext;
	}

	/**
	 * @param webApplicationContext the webApplicationContext to set
	 */
	public void setWebApplicationContext(WebApplicationContext webApplicationContext) {
		this.webApplicationContext = webApplicationContext;
	}

	/**
	 * @return the propertyDaoName
	 */
	public String getPropertyDaoName() {
		return propertyDaoName;
	}

	/**
	 * @param propertyDaoName the propertyDaoName to set
	 */
	public void setPropertyDaoName(String propertyDaoName) {
		this.propertyDaoName = propertyDaoName;
	}

	/**
	 * @return the scopeDaoName
	 */
	public String getScopeDaoName() {
		return scopeDaoName;
	}

	/**
	 * @param scopeDaoName the scopeDaoName to set
	 */
	public void setScopeDaoName(String scopeDaoName) {
		this.scopeDaoName = scopeDaoName;
	}

	/**
	 * @return the databaseServiceName
	 */
	public String getDatabaseServiceName() {
		return databaseServiceName;
	}

	/**
	 * @param databaseServiceName the databaseServiceName to set
	 */
	public void setDatabaseServiceName(String databaseServiceName) {
		this.databaseServiceName = databaseServiceName;
	}

}
