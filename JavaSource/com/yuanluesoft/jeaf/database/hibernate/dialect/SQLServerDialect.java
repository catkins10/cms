package com.yuanluesoft.jeaf.database.hibernate.dialect;

/**
 * SQLServer自定义方言
 * supportsLimit 返回true之后,hibernate不会再进行游标起始位置定位,只会从第一条记录开始取指定的记录条数
 * @author lmiky
 * 
 */
public class SQLServerDialect extends net.sf.hibernate.dialect.SybaseDialect {
	/*
	 * (non-Javadoc)
	 * @see net.sf.hibernate.dialect.SQLServerDialect#getLimitString(java.lang.String, boolean, int)
	 */
	public String getLimitString(String querySelect, boolean hasOffset, int limit) {
		//没有指定起始位置时,用top
		if (!hasOffset)
			/*
			 * sql不支持为select top ? 预编译
			 * 如果不加入?,会出现错误
			 */
			return new StringBuffer(querySelect.length() + 10).append(
					querySelect).insert(getAfterSelectInsertPoint(querySelect),
					" top " + limit + " ? as tempField, ").toString(); //需要添加？来占位,否则会报参数报错
		
		StringBuffer pagingSelect = new StringBuffer(querySelect.length() + 200);
		
		//获取select和from之间要查询的字段
		int starIndex = getAfterSelectInsertPoint(querySelect);
		int endIndex = querySelect.toLowerCase().indexOf("from");
		String fieldSql = querySelect.substring(starIndex, endIndex);
			
		//hibernate分页从0开始
		pagingSelect.append("select temptableid=identity(int,1,1), (? + 1) as beginTempId, "); 
		pagingSelect.append(fieldSql);
		pagingSelect.append(" into #HBTempTab ");
		//条件
		pagingSelect.append(querySelect.substring(endIndex));
		pagingSelect.append(" select * from  #HBTempTab where temptableid >= beginTempId and temptableid < (beginTempId + ?) drop table #HBTempTab");
		return pagingSelect.toString();
	}

	/**
	 * 获取sql中select子句位置。
	 * @param sql
	 * @return
	 */
	protected static int getAfterSelectInsertPoint(String sql) {
		int selectIndex = sql.toLowerCase().indexOf("select");
		int selectDistinctIndex = sql.toLowerCase().indexOf("select distinct");
		return selectIndex + ((selectDistinctIndex == selectIndex) ? 15 : 6);
	}
	/*
	 * (non-Javadoc)
	 * @see net.sf.hibernate.dialect.Dialect#supportsLimit()
	 */
	public boolean supportsLimit() {
		return true;
	}
}