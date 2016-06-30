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
public interface FormulaSupport {
	
	/**
	 * 执行公式
	 * @param formulaName 公式名称
	 * @param parameters 参数列表
	 * @param applicationName 应用名称
	 * @param request
	 * @param acl
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public Object executeFormula(String formulaName, String[] parameters, String applicationName, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws ServiceException;
}