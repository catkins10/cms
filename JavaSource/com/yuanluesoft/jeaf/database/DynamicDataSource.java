package com.yuanluesoft.jeaf.database;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * 
 * @author linchuan
 *
 */
public class DynamicDataSource implements DataSource {
	protected ThreadLocal threadLocal = new ThreadLocal();
	private DataSource defaultDataSource;
	
	/**
	 * 设置数据源
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
		threadLocal.set(dataSource);
	}
	
	/**
	 * 获取数据源
	 * @param dataSourceName
	 * @return
	 */
	private DataSource getDataSource() {
		DataSource dataSource = (DataSource)threadLocal.get();
		return (dataSource==null ? defaultDataSource : dataSource);
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sql.DataSource#getConnection()
	 */
	public Connection getConnection() throws SQLException {     
		return getDataSource().getConnection();     
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
	 */
	public Connection getConnection(String arg0, String arg1) throws SQLException {     
		return getDataSource().getConnection(arg0, arg1);     
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sql.CommonDataSource#getLogWriter()
	 */
	public PrintWriter getLogWriter() throws SQLException {     
		return getDataSource().getLogWriter();     
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sql.CommonDataSource#getLoginTimeout()
	 */
	public int getLoginTimeout() throws SQLException {     
		return getDataSource().getLoginTimeout();     
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sql.CommonDataSource#setLogWriter(java.io.PrintWriter)
	 */
	public void setLogWriter(PrintWriter arg0) throws SQLException {     
		getDataSource().setLogWriter(arg0);     
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sql.CommonDataSource#setLoginTimeout(int)
	 */
	public void setLoginTimeout(int arg0) throws SQLException {     
		getDataSource().setLoginTimeout(arg0);     
	}

	/* (non-Javadoc)
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	public boolean isWrapperFor(Class arg0) throws SQLException {
		return getDataSource().isWrapperFor(arg0);
	}

	/* (non-Javadoc)
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	public Object unwrap(Class arg0) throws SQLException {
		return getDataSource().unwrap(arg0);
	}

	/**
	 * @return the defaultDataSource
	 */
	public DataSource getDefaultDataSource() {
		return defaultDataSource;
	}

	/**
	 * @param defaultDataSource the defaultDataSource to set
	 */
	public void setDefaultDataSource(DataSource defaultDataSource) {
		this.defaultDataSource = defaultDataSource;
	}
}