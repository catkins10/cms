package com.yuanluesoft.enterprise.exam.question.service.spring;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.yuanluesoft.enterprise.exam.question.model.DifficultyLevel;
import com.yuanluesoft.enterprise.exam.question.pojo.Question;
import com.yuanluesoft.enterprise.exam.question.pojo.QuestionAnalysis;
import com.yuanluesoft.enterprise.exam.question.pojo.QuestionAnswer;
import com.yuanluesoft.enterprise.exam.question.pojo.QuestionContent;
import com.yuanluesoft.enterprise.exam.question.pojo.QuestionDetail;
import com.yuanluesoft.enterprise.exam.question.pojo.QuestionHint;
import com.yuanluesoft.enterprise.exam.question.pojo.QuestionItem;
import com.yuanluesoft.enterprise.exam.question.pojo.QuestionKnowledge;
import com.yuanluesoft.enterprise.exam.question.pojo.QuestionPost;
import com.yuanluesoft.enterprise.exam.question.pojo.QuestionUsed;
import com.yuanluesoft.enterprise.exam.question.service.QuestionService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class QuestionServiceImpl extends BusinessServiceImpl implements QuestionService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		Question question = (Question)record;
		if("单选题,多选题,判断题".indexOf(question.getQuestionType())!=-1) {
			question.setAnswerOnComputer(1); //计算机上可作答
			question.setComputerMarking(1); //计算机可判卷
		}
		return super.save(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		Question question = (Question)record;
		if("单选题,多选题,判断题".indexOf(question.getQuestionType())!=-1) {
			question.setAnswerOnComputer(1); //计算机上可作答
			question.setComputerMarking(1); //计算机可判卷
		}
		return super.update(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.exam.question.service.QuestionService#updateQuestionComponents(com.yuanluesoft.enterprise.exam.question.pojo.Question, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public void updateQuestionComponents(Question question, String content, String hint, String analysis, String detail, String postIds, String posts, String knowledgeIds, String knowledges, String itemIds, String items, String answers, String essayAnswers, boolean answerCaseSensitive, boolean isNewQuestion) throws ServiceException {
		//题目内容
		if(content!=null) {
			if(!isNewQuestion) {
				getDatabaseService().deleteRecordsByHql("from QuestionContent QuestionContent where QuestionContent.questionId=" + question.getId());
			}
			if(!content.isEmpty()) {
				QuestionContent questionContent = new QuestionContent();
				questionContent.setId(UUIDLongGenerator.generateId()); //ID
				questionContent.setQuestionId(question.getId()); //题目ID
				questionContent.setQuestionContent(content); //内容
				getDatabaseService().saveRecord(questionContent);
			}
		}
		
		//做题提示
		if(hint!=null) {
			if(!isNewQuestion) {
				getDatabaseService().deleteRecordsByHql("from QuestionHint QuestionHint where QuestionHint.questionId=" + question.getId());
			}
			if(!hint.isEmpty()) {
				QuestionHint questionHint = new QuestionHint();
				questionHint.setId(UUIDLongGenerator.generateId()); //ID
				questionHint.setQuestionId(question.getId()); //题目ID
				questionHint.setQuestionHint(hint); //内容
				getDatabaseService().saveRecord(questionHint);
			}
		}
		
		//试题分析
		if(analysis!=null) {
			if(!isNewQuestion) {
				getDatabaseService().deleteRecordsByHql("from QuestionAnalysis QuestionAnalysis where QuestionAnalysis.questionId=" + question.getId());
			}
			if(!analysis.isEmpty()) {
				QuestionAnalysis questionAnalysis = new QuestionAnalysis();
				questionAnalysis.setId(UUIDLongGenerator.generateId()); //ID
				questionAnalysis.setQuestionId(question.getId()); //题目ID
				questionAnalysis.setQuestionAnalysis(analysis); //内容
				getDatabaseService().saveRecord(questionAnalysis);
			}
		}
		
		//详解
		if(detail!=null) {
			if(!isNewQuestion) {
				getDatabaseService().deleteRecordsByHql("from QuestionDetail QuestionDetail where QuestionDetail.questionId=" + question.getId());
			}
			if(!detail.isEmpty()) {
				QuestionDetail questionDetail = new QuestionDetail();
				questionDetail.setId(UUIDLongGenerator.generateId()); //ID
				questionDetail.setQuestionId(question.getId()); //题目ID
				questionDetail.setQuestionDetail(detail); //内容
				getDatabaseService().saveRecord(questionDetail);
			}
		}
		
		//适用岗位
		if(postIds!=null) {
			if(!isNewQuestion) {
				getDatabaseService().deleteRecordsByHql("from QuestionPost QuestionPost where QuestionPost.questionId=" + question.getId());
			}
			if(!postIds.isEmpty()) {
				String[] ids = postIds.split(",");
				String[] values = posts.split(",");
				for(int i=0; i<ids.length; i++) {
					QuestionPost questionPost = new QuestionPost();
					questionPost.setId(UUIDLongGenerator.generateId()); //ID
					questionPost.setQuestionId(question.getId()); //题目ID
					questionPost.setPostId(Long.parseLong(ids[i])); //岗位ID
					questionPost.setPost(values[i]); //岗位
					getDatabaseService().saveRecord(questionPost);
				}
			}
		}
		
		//知识类别
		if(knowledgeIds!=null) {
			if(!isNewQuestion) {
				getDatabaseService().deleteRecordsByHql("from QuestionKnowledge QuestionKnowledge where QuestionKnowledge.questionId=" + question.getId());
			}
			if(!knowledgeIds.isEmpty()) {
				String[] ids = knowledgeIds.split(",");
				String[] values = knowledges.split(",");
				for(int i=0; i<ids.length; i++) {
					QuestionKnowledge questionKnowledge = new QuestionKnowledge();
					questionKnowledge.setId(UUIDLongGenerator.generateId()); //ID
					questionKnowledge.setQuestionId(question.getId()); //题目ID
					questionKnowledge.setKnowledgeId(Long.parseLong(ids[i])); //类别ID
					questionKnowledge.setKnowledge(values[i]); //类别
					getDatabaseService().saveRecord(questionKnowledge);
				}
			}
		}
		
		//项目分类
		if(itemIds!=null) {
			if(!isNewQuestion) {
				getDatabaseService().deleteRecordsByHql("from QuestionItem QuestionItem where QuestionItem.questionId=" + question.getId());
			}
			if(!itemIds.isEmpty()) {
				String[] ids = itemIds.split(",");
				String[] values = items.split(",");
				for(int i=0; i<ids.length; i++) {
					QuestionItem questionItem = new QuestionItem();
					questionItem.setId(UUIDLongGenerator.generateId()); //ID
					questionItem.setQuestionId(question.getId()); //题目ID
					questionItem.setItemId(Long.parseLong(ids[i])); //分类ID
					questionItem.setItem(values[i]); //分类
					getDatabaseService().saveRecord(questionItem);
				}
			}
		}
		
		//答案
		if("单选题,多选题,判断题,填空题".indexOf(question.getQuestionType())!=-1) {
			if(answers!=null) {
				if(!isNewQuestion) {
					getDatabaseService().deleteRecordsByHql("from QuestionAnswer QuestionAnswer where QuestionAnswer.questionId=" + question.getId());
				}
				if(!answers.isEmpty()) {
					String[] values = answers.split("\\r\\n");
					for(int i=0; i<values.length; i++) {
						QuestionAnswer questionAnswer = new QuestionAnswer();
						questionAnswer.setId(UUIDLongGenerator.generateId()); //ID
						questionAnswer.setQuestionId(question.getId()); //题目ID
						questionAnswer.setQuestionAnswer(values[i]); //分类
						questionAnswer.setCaseSensitive(answerCaseSensitive ? '1' : '0'); //是否区分大小写
						getDatabaseService().saveRecord(questionAnswer);
					}
				}
			}
		}
		else if(essayAnswers!=null) { //问答类
			if(!isNewQuestion) {
				getDatabaseService().deleteRecordsByHql("from QuestionAnswer QuestionAnswer where QuestionAnswer.questionId=" + question.getId());
			}
			if(!essayAnswers.isEmpty()) {
				QuestionAnswer questionAnswer = new QuestionAnswer();
				questionAnswer.setId(UUIDLongGenerator.generateId()); //ID
				questionAnswer.setQuestionId(question.getId()); //题目ID
				questionAnswer.setQuestionAnswer(essayAnswers); //答案
				questionAnswer.setCaseSensitive(answerCaseSensitive ? '1' : '0'); //是否区分大小写
				getDatabaseService().saveRecord(questionAnswer);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.exam.question.service.QuestionService#deleteImportedData(long)
	 */
	public void deleteImportedData(long importLogId) throws ServiceException {
		getDatabaseService().deleteRecordsByHql("from Question Question where Question.importLogId=" + importLogId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.exam.question.service.QuestionService#listQuestionTypes()
	 */
	public List listQuestionTypes() throws ServiceException {
		List items = FieldUtils.listSelectItems(FieldUtils.getRecordField(Question.class.getName(), "questionType", null), null, null);
		List questionTypes = new ArrayList();
		for(Iterator iterator = items.iterator(); iterator.hasNext();) {
			String[] values = (String[])iterator.next();
			questionTypes.add(values[0]);
		}
		return questionTypes;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.exam.question.service.QuestionService#listDifficultyLevels()
	 */
	public List listDifficultyLevels() throws ServiceException {
		List items = FieldUtils.listSelectItems(FieldUtils.getRecordField(Question.class.getName(), "difficulty", null), null, null);
		List difficultyLevels = new ArrayList();
		for(Iterator iterator = items.iterator(); iterator.hasNext();) {
			String[] values = (String[])iterator.next();
			DifficultyLevel difficultyLevel = new DifficultyLevel();
			difficultyLevel.setLevel(values[0]);
			difficultyLevel.setDifficulty(Integer.parseInt(values[1]));
			difficultyLevels.add(difficultyLevel);
		}
		return difficultyLevels;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.exam.question.service.QuestionService#makeOutQuestions(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, int, int, boolean, long)
	 */
	public List makeOutQuestions(String questionType, int questionNumber, int makeOutAfterUsedDays, String postIds, String knowledgeIds, String itemIds, int minDifficulty, int maxDifficulty, boolean answerOnComputerOnly, long personId) throws ServiceException {
		if(questionNumber<=0) {
			return null;
		}
		Timestamp useTime = null;
		if(makeOutAfterUsedDays>0) {
			useTime = DateTimeUtils.add(DateTimeUtils.now(), Calendar.DAY_OF_MONTH, -makeOutAfterUsedDays);
		}
		//获取课本目录对应的知识点
		String hql = "select distinct Question" +
					 " from Question Question" + 
					 (knowledgeIds==null || knowledgeIds.isEmpty() ? "" : " left join Question.questionKnowledges QuestionKnowledge") + //知识类别
					 (itemIds==null || itemIds.isEmpty() ? "" : " left join Question.questionItems QuestionItem") + //项目分类
					 (postIds==null || postIds.isEmpty() ? "" : " left join Question.questionPosts QuestionPost") + //岗位
					 " where " + (answerOnComputerOnly ? " Question.answerOnComputer=1" : "") + //计算机作答
					 (answerOnComputerOnly ? " and " : "") + "Question.questionType='" + JdbcUtils.resetQuot(questionType) + "'" + //题型
					 " and Question.difficulty>=" + minDifficulty + //最低难度
					 " and Question.difficulty<=" + maxDifficulty + //最高难度
					 (knowledgeIds==null || knowledgeIds.isEmpty() ? "" : " and QuestionKnowledge.knowledgeId in (" + JdbcUtils.validateInClauseNumbers(knowledgeIds) + ")") + //知识类别
					 (itemIds==null || itemIds.isEmpty() ? "" : " and QuestionItem.itemId in (" + JdbcUtils.validateInClauseNumbers(itemIds) + ")") + //项目分类
					 (postIds==null || postIds.isEmpty() ? "" : " and QuestionPost.postId in (" + JdbcUtils.validateInClauseNumbers(postIds) + ")") + //岗位
					 " and " + (useTime==null ? "not" : "") + " Question.id in (" + //题目最近没有使用过
					 "  select QuestionUsed.questionId" +
					 "   from QuestionUsed QuestionUsed" +
					 "   where QuestionUsed.personId=" + personId +
					 "   and QuestionUsed.questionId=Question.id" +
					 (useTime==null ? "" : "   and QuestionUsed.useTime<TIMESTAMP(" + DateTimeUtils.formatTimestamp(useTime, null) + ")") +
					 " )";
		List questions = getDatabaseService().findRecordsByHql(hql, listLazyLoadProperties(Question.class), 0, questionNumber * (questionNumber<=5 ? 5 : (questionNumber<=10 ? 3 : 2))); //最多获取5倍的题目,最少2倍
		if(questions==null || questions.isEmpty()) {
			return null;
		}
		if(questions.size()<=questionNumber) {
			return questions;
		}
		//随机获取题目
		List makeOutQuestions = new ArrayList();
		Random random = new Random();
		for(int i=0; i<questionNumber; i++) {
			int index = random.nextInt(questions.size());
			makeOutQuestions.add(questions.get(index));
			questions.remove(index);
		}
		return makeOutQuestions;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.exam.question.service.QuestionService#questionUsed(long, long)
	 */
	public void questionUsed(long questionId, long personId) throws ServiceException {
		questionUsed((Question)getDatabaseService().findRecordById(Question.class.getName(), questionId, null), personId);
	}

	/**
	 * 把题目标志为已使用
	 * @param question
	 * @param personId
	 * @throws ServiceException
	 */
	private void questionUsed(Question question, long personId) throws ServiceException {
		//获取原来的使用记录
		String hql = "from QuestionUsed QuestionUsed" +
					 " where QuestionUsed.personId=" + personId +
					 " and QuestionUsed.questionId=" + question.getId();
		QuestionUsed questionUsed = (QuestionUsed)getDatabaseService().findRecordByHql(hql);
		if(questionUsed!=null) { //更新使用记录
			questionUsed.setUseTime(DateTimeUtils.now()); //使用时间
			getDatabaseService().updateRecord(questionUsed);
		}
		else { //创建新的使用记录
			questionUsed = new QuestionUsed();
			questionUsed.setId(UUIDLongGenerator.generateId()); //ID
			questionUsed.setPersonId(personId); //用户ID
			questionUsed.setQuestionId(question.getId()); //题目ID
			questionUsed.setUseTime(DateTimeUtils.now()); //使用时间
			getDatabaseService().saveRecord(questionUsed);
		}
		//增加题目的使用次数
		question.setUseTimes(question.getUseTimes()+1);
		question.setFailedRate((question.getFailedTimes()*100)/question.getUseTimes());
		getDatabaseService().updateRecord(question);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.exam.question.service.QuestionService#listQuestions(java.lang.String)
	 */
	public List listQuestions(String questionIds) throws ServiceException {
		List questions = getDatabaseService().findRecordsByHql("from Question Question where Question.id in (" + JdbcUtils.validateInClauseNumbers(questionIds) + ")", listLazyLoadProperties(Question.class));
		return ListUtils.sortByProperty(questions, "id", questionIds);
	}
}