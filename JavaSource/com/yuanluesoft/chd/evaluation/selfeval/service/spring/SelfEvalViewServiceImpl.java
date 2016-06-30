package com.yuanluesoft.chd.evaluation.selfeval.service.spring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.chd.evaluation.selfeval.model.SelfEvalByDepartment;
import com.yuanluesoft.chd.evaluation.selfeval.model.SelfEvalByMonth;
import com.yuanluesoft.chd.evaluation.selfeval.pojo.ChdEvaluationSelf;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;

/**
 * 
 * @author linchuan
 *
 */
public class SelfEvalViewServiceImpl extends ViewServiceImpl {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveRecords(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.util.List, int, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List retrieveRecords(View view, String currentCategories, List searchConditionList, int beginRow, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		//获取目录ID
		long directoryId = RequestUtils.getParameterLongValue(request, "directoryId");
		if(directoryId==0) {
			return null;
		}
		//获取年度
		int year = RequestUtils.getParameterIntValue(request, "evalYear");
		if(year==0) {
			year = DateTimeUtils.getYear(DateTimeUtils.date());
		}
		String hqlFrom = "from ChdEvaluationSelf ChdEvaluationSelf left join ChdEvaluationSelf.subjections subjections, ChdEvaluationDirectorySubjection ChdEvaluationDirectorySubjection";
		String hqlWhere = "ChdEvaluationSelf.evalYear=" + year +
						  " and subjections.directoryId=ChdEvaluationDirectorySubjection.directoryId" +
						  " and ChdEvaluationDirectorySubjection.parentDirectoryId=" + directoryId;
		String hqlOrderBy = "ChdEvaluationSelf.created, ChdEvaluationSelf.department";
		List evals = getDatabaseService().findRecordsByHqlClause(ChdEvaluationSelf.class.getName(), null, hqlFrom, hqlWhere, hqlOrderBy, null, null, 0, 3000);
		List months = new ArrayList();
		for(int i=1; i<=12; i++) {
			SelfEvalByMonth selfEvalByMonth = new SelfEvalByMonth();
			selfEvalByMonth.setMonth(i);
			//获取该月的自查记录
			List monthEvals = ListUtils.getSubListByProperty(evals, "evalMonth", new Integer(i));
			if(monthEvals!=null && !monthEvals.isEmpty()) {
				selfEvalByMonth.setDepartments(new ArrayList());
				SelfEvalByDepartment selfEvalByDepartment = new SelfEvalByDepartment();
				for(Iterator iterator = monthEvals.iterator(); iterator.hasNext();) {
					ChdEvaluationSelf selfEval = (ChdEvaluationSelf)iterator.next();
					if(!selfEval.getDepartment().equals(selfEvalByDepartment.getDepartment())) {
						selfEvalByDepartment = new SelfEvalByDepartment();
						selfEvalByDepartment.setDepartment(selfEval.getDepartment());
						selfEvalByDepartment.setSelfEvalList(new ArrayList());
						selfEvalByMonth.getDepartments().add(selfEvalByDepartment);
					}
					selfEvalByDepartment.getSelfEvalList().add(selfEval);
				}
			}
			months.add(selfEvalByMonth);
		}
		return months;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveRecordCount(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.util.List, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected int retrieveRecordCount(View view, String currentCategories, List searchConditionList, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		return 12;
	}
}