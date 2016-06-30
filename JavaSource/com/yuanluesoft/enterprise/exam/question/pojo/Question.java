package com.yuanluesoft.enterprise.exam.question.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 试题(exam_question)
 * @author linchuan
 *
 */
public class Question extends Record {
	private String no; //编号
	private int difficulty; //难度系数
	private String questionType; //题型
	private int eachScore; //题目单项分值
	private int score; //题目总分值
	private String source; //试题来源
	private int responseTime; //作答时间,以秒为单位
	private int blankNumber; //答案个数,填空题有效
	private int optionNumber; //选项个数,选择题时有效,默认为4个
	private int answerOnComputer; //计算机上可作答
	private int computerMarking; //计算机可判卷
	private int wrong; //题目有错误,收到举报并确认后置1,处理完毕后恢复
	private int useTimes; //使用次数
	private int failedTimes; //答错次数
	private int failedRate; //答错率,如100,表示100%
	private Timestamp created;
	private long creatorId; //创建人ID
	private String creator; //创建人
	private long importLogId; //导入日志ID
	private String remark; //备注
	private Set questionContents; //正文
	private Set questionAnswers; //答案
	private Set questionHints; //提示
	private Set questionAnalysises; //分析
	private Set questionDetails; //明细
	private Set questionUseds; //用户使用记录
	private Set questionPosts; //适用的岗位
	private Set questionKnowledges; //知识分类
	private Set questionItems; //项目分类
	private Set wrongQuestions; //错题本
	
	/**
	 * 试题内容
	 * @return
	 */
	public String getQuestionContent() {
		try {
			return ListUtils.join(questionContents, "questionContent", ",", false);
		}
		catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * 提示
	 * @return
	 */
	public String getQuestionHint() {
		try {
			return ListUtils.join(questionHints, "questionHint", ",", false);
		}
		catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * 分析
	 * @return
	 */
	public String getQuestionAnalysis() {
		try {
			return ListUtils.join(questionAnalysises, "questionAnalysis", ",", false);
		}
		catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * 明细
	 * @return
	 */
	public String getQuestionDetail() {
		try {
			return ListUtils.join(questionDetails, "questionDetail", ",", false);
		}
		catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * 适用的岗位
	 * @return
	 */
	public String getQuestionPost() {
		try {
			return ListUtils.join(questionPosts, "post", ",", false);
		}
		catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * 知识分类
	 * @return
	 */
	public String getQuestionKnowledge() {
		try {
			return ListUtils.join(questionKnowledges, "knowledge", ",", false);
		}
		catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * 项目分类
	 * @return
	 */
	public String getQuestionItem() {
		try {
			return ListUtils.join(questionItems, "item", ",", false);
		}
		catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * 答案
	 * @return
	 */
	public String getQuestionAnswer() {
		try {
			return ListUtils.join(questionAnswers, "questionAnswer", ",", false);
		}
		catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * @return the answerOnComputer
	 */
	public int getAnswerOnComputer() {
		return answerOnComputer;
	}
	/**
	 * @param answerOnComputer the answerOnComputer to set
	 */
	public void setAnswerOnComputer(int answerOnComputer) {
		this.answerOnComputer = answerOnComputer;
	}
	/**
	 * @return the blankNumber
	 */
	public int getBlankNumber() {
		return blankNumber;
	}
	/**
	 * @param blankNumber the blankNumber to set
	 */
	public void setBlankNumber(int blankNumber) {
		this.blankNumber = blankNumber;
	}
	/**
	 * @return the computerMarking
	 */
	public int getComputerMarking() {
		return computerMarking;
	}
	/**
	 * @param computerMarking the computerMarking to set
	 */
	public void setComputerMarking(int computerMarking) {
		this.computerMarking = computerMarking;
	}
	/**
	 * @return the difficulty
	 */
	public int getDifficulty() {
		return difficulty;
	}
	/**
	 * @param difficulty the difficulty to set
	 */
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	/**
	 * @return the eachScore
	 */
	public int getEachScore() {
		return eachScore;
	}
	/**
	 * @param eachScore the eachScore to set
	 */
	public void setEachScore(int eachScore) {
		this.eachScore = eachScore;
	}
	/**
	 * @return the failedRate
	 */
	public int getFailedRate() {
		return failedRate;
	}
	/**
	 * @param failedRate the failedRate to set
	 */
	public void setFailedRate(int failedRate) {
		this.failedRate = failedRate;
	}
	/**
	 * @return the failedTimes
	 */
	public int getFailedTimes() {
		return failedTimes;
	}
	/**
	 * @param failedTimes the failedTimes to set
	 */
	public void setFailedTimes(int failedTimes) {
		this.failedTimes = failedTimes;
	}
	/**
	 * @return the no
	 */
	public String getNo() {
		return no;
	}
	/**
	 * @param no the no to set
	 */
	public void setNo(String no) {
		this.no = no;
	}
	/**
	 * @return the optionNumber
	 */
	public int getOptionNumber() {
		return optionNumber;
	}
	/**
	 * @param optionNumber the optionNumber to set
	 */
	public void setOptionNumber(int optionNumber) {
		this.optionNumber = optionNumber;
	}
	/**
	 * @return the questionType
	 */
	public String getQuestionType() {
		return questionType;
	}
	/**
	 * @param questionType the questionType to set
	 */
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the responseTime
	 */
	public int getResponseTime() {
		return responseTime;
	}
	/**
	 * @param responseTime the responseTime to set
	 */
	public void setResponseTime(int responseTime) {
		this.responseTime = responseTime;
	}
	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the useTimes
	 */
	public int getUseTimes() {
		return useTimes;
	}
	/**
	 * @param useTimes the useTimes to set
	 */
	public void setUseTimes(int useTimes) {
		this.useTimes = useTimes;
	}
	/**
	 * @return the wrong
	 */
	public int getWrong() {
		return wrong;
	}
	/**
	 * @param wrong the wrong to set
	 */
	public void setWrong(int wrong) {
		this.wrong = wrong;
	}
	/**
	 * @return the questionAnalysises
	 */
	public Set getQuestionAnalysises() {
		return questionAnalysises;
	}
	/**
	 * @param questionAnalysises the questionAnalysises to set
	 */
	public void setQuestionAnalysises(Set questionAnalysises) {
		this.questionAnalysises = questionAnalysises;
	}
	/**
	 * @return the questionAnswers
	 */
	public Set getQuestionAnswers() {
		return questionAnswers;
	}
	/**
	 * @param questionAnswers the questionAnswers to set
	 */
	public void setQuestionAnswers(Set questionAnswers) {
		this.questionAnswers = questionAnswers;
	}
	/**
	 * @return the questionContents
	 */
	public Set getQuestionContents() {
		return questionContents;
	}
	/**
	 * @param questionContents the questionContents to set
	 */
	public void setQuestionContents(Set questionContents) {
		this.questionContents = questionContents;
	}
	/**
	 * @return the questionDetails
	 */
	public Set getQuestionDetails() {
		return questionDetails;
	}
	/**
	 * @param questionDetails the questionDetails to set
	 */
	public void setQuestionDetails(Set questionDetails) {
		this.questionDetails = questionDetails;
	}
	/**
	 * @return the questionHints
	 */
	public Set getQuestionHints() {
		return questionHints;
	}
	/**
	 * @param questionHints the questionHints to set
	 */
	public void setQuestionHints(Set questionHints) {
		this.questionHints = questionHints;
	}
	/**
	 * @return the questionItems
	 */
	public Set getQuestionItems() {
		return questionItems;
	}
	/**
	 * @param questionItems the questionItems to set
	 */
	public void setQuestionItems(Set questionItems) {
		this.questionItems = questionItems;
	}
	/**
	 * @return the questionKnowledges
	 */
	public Set getQuestionKnowledges() {
		return questionKnowledges;
	}
	/**
	 * @param questionKnowledges the questionKnowledges to set
	 */
	public void setQuestionKnowledges(Set questionKnowledges) {
		this.questionKnowledges = questionKnowledges;
	}
	/**
	 * @return the questionPosts
	 */
	public Set getQuestionPosts() {
		return questionPosts;
	}
	/**
	 * @param questionPosts the questionPosts to set
	 */
	public void setQuestionPosts(Set questionPosts) {
		this.questionPosts = questionPosts;
	}
	/**
	 * @return the questionUseds
	 */
	public Set getQuestionUseds() {
		return questionUseds;
	}
	/**
	 * @param questionUseds the questionUseds to set
	 */
	public void setQuestionUseds(Set questionUseds) {
		this.questionUseds = questionUseds;
	}
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return the importLogId
	 */
	public long getImportLogId() {
		return importLogId;
	}
	/**
	 * @param importLogId the importLogId to set
	 */
	public void setImportLogId(long importLogId) {
		this.importLogId = importLogId;
	}

	/**
	 * @return the wrongQuestions
	 */
	public Set getWrongQuestions() {
		return wrongQuestions;
	}

	/**
	 * @param wrongQuestions the wrongQuestions to set
	 */
	public void setWrongQuestions(Set wrongQuestions) {
		this.wrongQuestions = wrongQuestions;
	}
}