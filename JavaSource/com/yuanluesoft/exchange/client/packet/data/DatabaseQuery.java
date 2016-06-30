package com.yuanluesoft.exchange.client.packet.data;

import java.util.List;

import com.yuanluesoft.exchange.client.packet.ExchangePacket;

/**
 * 按HQL查询数据库
 * @author linchuan
 *
 */
public class DatabaseQuery extends ExchangePacket {
	private String databaseServiceName; //数据库服务名称
	private String hql; //HQL
	private String sql; //SQL
	private List lazyLoadProperties; //需要加载的延迟加载属性
	private int offset; //起始行
	private int limit; //行数
	
	public DatabaseQuery(String databaseServiceName, String hql, String sql, List lazyLoadProperties, int offset, int limit) {
		super();
		this.databaseServiceName = databaseServiceName;
		this.hql = hql;
		this.sql = sql;
		this.lazyLoadProperties = lazyLoadProperties;
		this.offset = offset;
		this.limit = limit;
	}
	/**
	 * @return the hql
	 */
	public String getHql() {
		return hql;
	}
	/**
	 * @param hql the hql to set
	 */
	public void setHql(String hql) {
		this.hql = hql;
	}
	/**
	 * @return the lazyLoadProperties
	 */
	public List getLazyLoadProperties() {
		return lazyLoadProperties;
	}
	/**
	 * @param lazyLoadProperties the lazyLoadProperties to set
	 */
	public void setLazyLoadProperties(List lazyLoadProperties) {
		this.lazyLoadProperties = lazyLoadProperties;
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
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}
	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
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
}