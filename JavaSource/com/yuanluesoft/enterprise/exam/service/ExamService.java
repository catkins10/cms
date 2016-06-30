package com.yuanluesoft.enterprise.exam.service;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.enterprise.exam.model.ExamRankingList;
import com.yuanluesoft.enterprise.exam.pojo.Exam;
import com.yuanluesoft.enterprise.exam.pojo.ExamPaper;
import com.yuanluesoft.enterprise.exam.pojo.ExamTest;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public interface ExamService extends BusinessService {
	//考试状态,0/在考,1/待批改,2/完成,3/放弃
	public static final int EXAM_TEST_STATUS_TESTING = 0;
	public static final int EXAM_TEST_STATUS_TOCONRRECT = 1;
	public static final int EXAM_TEST_STATUS_COMPLETE = 2;
	public static final int EXAM_TEST_STATUS_CANCEL = 3;
	
	/**
	 * 更新考试的组成部分
	 * @param exam
	 * @param isNewExam
	 * @param examPostIds
	 * @param examPostNames
	 * @param examCorrectorIds
	 * @param examCorrectorNames
	 * @param request
	 * @throws ServiceException
	 */
	public void updateExamComponents(Exam exam, boolean isNewExam, String examPostIds, String examPostNames, String examCorrectorIds, String examCorrectorNames, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 生成试卷
	 * @param examPaperId
	 * @param examId
	 * @param examPaperName
	 * @param resit
	 * @param beginTime
	 * @param endTime
	 * @param onComputer
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public ExamPaper generateExamPaper(long examId, String examPaperName, boolean resit, Timestamp beginTime, Timestamp endTime, boolean onComputer, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 生成试卷(自助方式)
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void generateSelfTestExamPaper(SessionInfo sessionInfo) throws ServiceException;

	/**
	 * 发布试卷
	 * @param examPaper
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void releaseExamPaper(ExamPaper examPaper, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 创建一个答卷
	 * @param examPaperId
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public ExamTest createExamTest(long examPaperId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 提交试卷
	 * @param examTest
	 * @param request
	 * @throws ServiceException
	 */
	public void submitExamTest(ExamTest examTest, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 批改试卷
	 * @param examTest
	 * @param request
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void correctExamTest(ExamTest examTest, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 放弃考试
	 * @param examTest
	 * @param request
	 * @throws ServiceException
	 */
	public void cancelExamTest(ExamTest examTest, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 检查用户是否批改人
	 * @param examId
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public boolean isCorrector(long examId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 按考卷ID获取考试列表
	 * @param examPaperId
	 * @return
	 * @throws ServiceException
	 */
	public List listExamTests(long examPaperId) throws ServiceException;
	
	/**
	 * 按岗位获取成绩名次表
	 * @param postId
	 * @param year
	 * @param month
	 * @return
	 * @throws ServiceException
	 */
	public ExamRankingList getRankingList(long postId, int year, int month) throws ServiceException;
	
	/**
	 * 考试统计,返回ExamStat列表
	 * @param postIds
	 * @param orgIds
	 * @param year
	 * @param month
	 * @return
	 * @throws ServiceException
	 */
	public List examStat(String postIds, String orgIds, int year, int month) throws ServiceException;
}