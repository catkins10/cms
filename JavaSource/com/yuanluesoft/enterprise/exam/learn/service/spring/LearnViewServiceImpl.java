package com.yuanluesoft.enterprise.exam.learn.service.spring;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.enterprise.exam.learn.pojo.LearnQuestion;
import com.yuanluesoft.enterprise.exam.question.pojo.Question;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;

/**
 * 
 * @author linchuan
 *
 */
public class LearnViewServiceImpl extends ViewServiceImpl {


	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveRecords(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.util.List, int, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List retrieveRecords(View view, String currentCategories, List searchConditionList, int beginRow, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		List questions = super.retrieveRecords(view, currentCategories, searchConditionList, beginRow, request, sessionInfo);
		for(Iterator iterator = questions==null ? null : questions.iterator(); iterator!=null && iterator.hasNext();) {
			Question question = (Question)iterator.next();
			//设为“已学习”
			String hql = "from LearnQuestion LearnQuestion" +
						 " where LearnQuestion.questionId=" + question.getId() +
						 " and LearnQuestion.personId=" + sessionInfo.getUserId();
			LearnQuestion learnQuestion = (LearnQuestion)getDatabaseService().findRecordByHql(hql);
			if(learnQuestion!=null) {
				learnQuestion.setLearnTime(DateTimeUtils.now());
				getDatabaseService().updateRecord(learnQuestion);
			}
			else {
				learnQuestion = new LearnQuestion();
				learnQuestion.setId(UUIDLongGenerator.generateId()); //ID
				learnQuestion.setQuestionId(question.getId()); //试题ID
				learnQuestion.setPersonId(sessionInfo.getUserId()); //用户ID
				learnQuestion.setLearnTime(DateTimeUtils.now());
				getDatabaseService().saveRecord(learnQuestion);
			}
		}
		return questions;
	}
}