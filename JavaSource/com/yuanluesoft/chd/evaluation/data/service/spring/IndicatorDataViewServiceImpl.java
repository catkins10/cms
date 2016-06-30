package com.yuanluesoft.chd.evaluation.data.service.spring;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.chd.evaluation.data.service.EvaluationDataService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;

/**
 * 
 * @author linchuan
 *
 */
public class IndicatorDataViewServiceImpl extends ViewServiceImpl {
	private EvaluationDataService evaluationDataService;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveRecords(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.util.List, int, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List retrieveRecords(View view, String currentCategories, List searchConditionList, int beginRow, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		return evaluationDataService.loadIndicatorDataList(RequestUtils.getParameterLongValue(request, "directoryId"), RequestUtils.getParameterIntValue(request, "year"), RequestUtils.getParameterIntValue(request, "month"), false, sessionInfo);
	}

	/**
	 * @return the evaluationDataService
	 */
	public EvaluationDataService getEvaluationDataService() {
		return evaluationDataService;
	}

	/**
	 * @param evaluationDataService the evaluationDataService to set
	 */
	public void setEvaluationDataService(EvaluationDataService evaluationDataService) {
		this.evaluationDataService = evaluationDataService;
	}
}