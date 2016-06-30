package com.yuanluesoft.jeaf.formula.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 公式服务
 * @author linchuan
 *
 */
public interface FormulaService extends FormulaSupport {
	
	/**
	 * 条件检查
	 * @param condition
	 * @param callback
	 * @param applicationName
	 * @param request
	 * @param acl
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public boolean checkCondition(String condition, ConditionCheckCallback callback, String applicationName, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws ServiceException;
}