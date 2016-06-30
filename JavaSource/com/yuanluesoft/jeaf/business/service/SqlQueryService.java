package com.yuanluesoft.jeaf.business.service;

import java.util.List;

import com.yuanluesoft.jeaf.database.SqlResult;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * SQL查询服务
 * @author linchuan
 *
 */
public interface SqlQueryService extends BusinessService {
	
	/**
	 * 查询记录列表查询记录列表
	 * @param recordName
	 * @param sqlSelect
	 * @param sqlWhere
	 * @param sqlGroupBy
	 * @param sqlOrderBy
	 * @param offset
	 * @param limit
	 * @return
	 * @throws ServiceException
	 */
	public List listResords(String recordName, String sqlSelect, String sqlWhere, String sqlGroupBy, String sqlOrderBy, int offset, int limit) throws ServiceException;
	
	/**
	 * 统计记录数
	 * @param recordName
	 * @param sqlWhere
	 * @return
	 * @throws ServiceException
	 */
	public int countResords(String recordName, String sqlWhere) throws ServiceException;
	
	/**
	 * 获取记录
	 * @param recordName
	 * @param recordId
	 * @return
	 * @throws ServiceException
	 */
	public SqlResult getRecord(String recordName, String recordId) throws ServiceException;
}