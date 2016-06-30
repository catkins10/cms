package com.yuanluesoft.jeaf.database.hibernate.dialect;

/**
 * Sybase方言,改写分页
 * supportsLimit 返回true之后,hibernate不会再进行游标起始位置定位,只会从第一条记录开始取指定的记录条数
 * 效率上看,长度小于350时,用java游标效率更快
 * @author lmiky
 *
 */
public class Sybase12_5_2Dialect extends net.sf.hibernate.dialect.SybaseDialect {
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.hibernate.dialect.Dialect#getLimitString(java.lang.String, boolean, int)
	 */
	public String getLimitString(String querySelect, boolean hasOffset, int limit) {
		if(!hasOffset && limit < 350) {
			return new StringBuffer(querySelect.length() + 10).append(
					querySelect).insert(getAfterSelectInsertPoint(querySelect),
					" ? as tempField, ").toString();  //需要添加？来占位,否则会报参数报错
		}
		
		StringBuffer pagingSelect = new StringBuffer(querySelect.length()+200);
		
		//select(distinct)所在位置
		int starIndex = getAfterSelectInsertPoint(querySelect);
		int endIndex = querySelect.toLowerCase().indexOf("from");
		String fieldSql = querySelect.substring(starIndex, endIndex);
			
		if(hasOffset) {
			//hibernate分页从0开始
			pagingSelect.append("select temptableid=identity(12), (? + 1) as beginTempId, "); 
		} else {
			pagingSelect.append("select temptableid=identity(12), "); 
		}
		pagingSelect.append(fieldSql);
		pagingSelect.append(" into #HBTempTab ");
		//条件
		pagingSelect.append(querySelect.substring(endIndex));
		if(hasOffset) {
			pagingSelect.append(" select * from  #HBTempTab where temptableid >= beginTempId and temptableid < (beginTempId + ?) drop table #HBTempTab");
		} else {
			pagingSelect.append(" select * from  #HBTempTab where  temptableid <= ? drop table #HBTempTab");
		}
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
