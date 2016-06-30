package com.yuanluesoft.enterprise.exam.question.service;

import java.util.List;

import com.yuanluesoft.enterprise.exam.question.pojo.Question;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface QuestionService extends BusinessService {

	/**
	 * 更新题目组成部分
	 * @param question
	 * @param content
	 * @param hint
	 * @param analysis
	 * @param detail
	 * @param postIds
	 * @param posts
	 * @param knowledgeIds
	 * @param knowledges
	 * @param itemIds
	 * @param items
	 * @param answers
	 * @param essayAnswers
	 * @param answerCaseSensitive
	 * @param isNewQuestion
	 * @throws ServiceException
	 */
	public void updateQuestionComponents(Question question, String content, String hint, String analysis, String detail, String postIds, String posts, String knowledgeIds, String knowledges, String itemIds, String items, String answers, String essayAnswers, boolean answerCaseSensitive, boolean isNewQuestion) throws ServiceException;

	/**
	 * 删除导入的数据
	 * @param importLogId
	 * @throws ServiceException
	 */
	public void deleteImportedData(long importLogId) throws ServiceException;
	
	/**
	 * 获取题型列表
	 * @return
	 * @throws ServiceException
	 */
	public List listQuestionTypes() throws ServiceException;
	
	/**
	 * 获取难度等级(DifficultyLevel)列表
	 * @return
	 * @throws ServiceException
	 */
	public List listDifficultyLevels() throws ServiceException;
	
	/**
	 * 出题
	 * @param questionType 题型
	 * @param questionNumber 题目数
	 * @param makeOutAfterUsedDays 出makeOutAfterUsedDays天内未用过的题目,如果是0,则出没有使用过的题目
	 * @param postIds 适用的岗位
	 * @param knowledgeIds 知识类别ID
	 * @param itemIds 项目分类ID
	 * @param minDifficulty 最低难度,题目难度>=minDifficulty
	 * @param maxDifficulty 最高难度,题目难度<=maxDifficulty
	 * @param answerOnComputerOnly 计算机作答
	 * @param personId 用户ID
	 * @return
	 * @throws ServiceException
	 */
	public List makeOutQuestions(String questionType, int questionNumber, int makeOutAfterUsedDays, String postIds, String knowledgeIds, String itemIds, int minDifficulty, int maxDifficulty, boolean answerOnComputerOnly, long personId) throws ServiceException;

	/**
	 * 把题目标记为使用过
	 * @param questionId
	 * @param personId
	 * @throws ServiceException
	 */
	public void questionUsed(long questionId, long personId) throws ServiceException;
	
	/**
	 * 获取题目列表
	 * @param questionIds
	 * @return
	 * @throws ServiceException
	 */
	public List listQuestions(String questionIds) throws ServiceException;
}