package com.yuanluesoft.jeaf.formula.service;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 条件检查回调
 * @author linchuan
 *
 */
public interface ConditionCheckCallback {

	/**
	 * 检查条件是否满足
	 * @param formulaName
	 * @param parameters
	 * @return
	 * @throws ServiceException
	 */
	public boolean check(String formulaName, String[] parameters) throws ServiceException;
}