package com.yuanluesoft.jeaf.view.sqlview.service.spring;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.business.service.SqlQueryService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;

/**
 * 
 * @author yuanluesoft
 *
 */
public class SqlViewServiceImpl extends ViewServiceImpl {
	private SqlQueryService sqlQueryService; //SQL查询服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveRecordCount(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.util.List, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected int retrieveRecordCount(View view, String currentCategories, List searchConditionList, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		return sqlQueryService.countResords(view.getPojoClassName(), view.getWhere());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveRecords(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.util.List, int, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List retrieveRecords(View view, String currentCategories, List searchConditionList, int beginRow, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		return sqlQueryService.listResords(view.getPojoClassName(), null, view.getWhere(), null, view.getOrderBy(), beginRow, view.getPageRows());
	}

	/**
	 * @return the sqlQueryService
	 */
	public SqlQueryService getSqlQueryService() {
		return sqlQueryService;
	}

	/**
	 * @param sqlQueryService the sqlQueryService to set
	 */
	public void setSqlQueryService(SqlQueryService sqlQueryService) {
		this.sqlQueryService = sqlQueryService;
	}

}