package com.yuanluesoft.enterprise.exam.service.spring;

import java.sql.Date;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.enterprise.exam.pojo.ExamWrongQuestion;
import com.yuanluesoft.enterprise.exam.question.pojo.Question;
import com.yuanluesoft.enterprise.exam.question.service.QuestionService;
import com.yuanluesoft.enterprise.exam.service.ExamService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;

/**
 * 
 * @author linchuan
 *
 */
public class ExamViewServiceImpl extends ViewServiceImpl {
	private ExamService examService; //考试服务
	private QuestionService questionService; //试题服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveViewPackage(com.yuanluesoft.jeaf.view.model.ViewPackage, com.yuanluesoft.jeaf.view.model.View, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void retrieveViewPackage(ViewPackage viewPackage, View view, int beginRow, boolean retrieveDataOnly, boolean readRecordsOnly, boolean countRecordsOnly, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException, PrivilegeException {
		if("admin/examStat".equals(viewPackage.getView().getName())) { //考试统计
			int year = RequestUtils.getParameterIntValue(request, "year");
			int month = RequestUtils.getParameterIntValue(request, "month");
			if(year==0) {
				Date date = DateTimeUtils.add(DateTimeUtils.date(), Calendar.MONTH, -1);
				year = DateTimeUtils.getYear(date);
				month = DateTimeUtils.getMonth(date) + 1;
			}
			List records = examService.examStat(request.getParameter("postIds"), request.getParameter("orgIds"), year, month);
			viewPackage.setRecords(records);
			viewPackage.setRecordCount(records==null ? 0 : records.size());
			viewPackage.setRowNum(1);
			viewPackage.setPageCount(1);
			return;
		}
		super.retrieveViewPackage(viewPackage, view, beginRow, retrieveDataOnly, readRecordsOnly, countRecordsOnly, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveRecordCount(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.util.List, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected int retrieveRecordCount(View view, String currentCategories, List searchConditionList, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		generateSelfTestExamPaper(view, request, sessionInfo);
		return super.retrieveRecordCount(view, currentCategories, searchConditionList, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveRecords(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.util.List, int, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List retrieveRecords(View view, String currentCategories, List searchConditionList, int beginRow, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		generateSelfTestExamPaper(view, request, sessionInfo);
		List records = super.retrieveRecords(view, currentCategories, searchConditionList, beginRow, request, sessionInfo);
		if("wrongQuestion".equals(view.getName())) { //错题本
			for(Iterator iterator = records==null ? null : records.iterator(); iterator!=null && iterator.hasNext();) {
				ExamWrongQuestion wrongQuestion = (ExamWrongQuestion)iterator.next();
				wrongQuestion.setQuestion((Question)questionService.load(Question.class, wrongQuestion.getQuestionId()));
			}
		}
		return records;
	}

	/**
	 * 生成自助考试试卷
	 * @param view
	 * @param request
	 * @param sessionInfo
	 * @throws ServiceException
	 * @throws PrivilegeException
	 */
	private void generateSelfTestExamPaper(View view, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("toTestExamPapers".equals(view.getName()) && request.getAttribute("generateSelfTestExamPaper")==null) { //待答试卷
			examService.generateSelfTestExamPaper(sessionInfo); //自助考试出卷
			request.setAttribute("generateSelfTestExamPaper", "1");
		}
	}

	/**
	 * @return the examService
	 */
	public ExamService getExamService() {
		return examService;
	}

	/**
	 * @param examService the examService to set
	 */
	public void setExamService(ExamService examService) {
		this.examService = examService;
	}

	/**
	 * @return the questionService
	 */
	public QuestionService getQuestionService() {
		return questionService;
	}

	/**
	 * @param questionService the questionService to set
	 */
	public void setQuestionService(QuestionService questionService) {
		this.questionService = questionService;
	}
}