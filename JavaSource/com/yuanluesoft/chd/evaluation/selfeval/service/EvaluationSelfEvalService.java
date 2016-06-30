/*
 * Created on 2007-7-3
 *
 */
package com.yuanluesoft.chd.evaluation.selfeval.service;

import com.yuanluesoft.chd.evaluation.selfeval.pojo.ChdEvaluationSelf;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 * 
 */
public interface EvaluationSelfEvalService extends BusinessService {
	
	/**
	 * 更新隶属目录
	 * @param selfEval
	 * @param isNew
	 * @param subjectionDirectoryIds
	 * @throws ServiceException
	 */
	public void updateSelfEvalSubjections(ChdEvaluationSelf selfEval, boolean isNew, String subjectionDirectoryIds) throws ServiceException;
	
	/**
	 * 是否自查当月
	 * @return
	 */
	public boolean isSelfEvalCurrentMonth();
}