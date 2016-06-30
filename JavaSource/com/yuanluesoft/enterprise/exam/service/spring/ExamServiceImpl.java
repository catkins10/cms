package com.yuanluesoft.enterprise.exam.service.spring;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.Collator;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.enterprise.exam.model.ExamRankingList;
import com.yuanluesoft.enterprise.exam.model.ExamStat;
import com.yuanluesoft.enterprise.exam.model.ExamTestQuestionType;
import com.yuanluesoft.enterprise.exam.model.ExamTranscript;
import com.yuanluesoft.enterprise.exam.pojo.Exam;
import com.yuanluesoft.enterprise.exam.pojo.ExamCorrector;
import com.yuanluesoft.enterprise.exam.pojo.ExamDifficultyLevel;
import com.yuanluesoft.enterprise.exam.pojo.ExamPaper;
import com.yuanluesoft.enterprise.exam.pojo.ExamPaperPrivilege;
import com.yuanluesoft.enterprise.exam.pojo.ExamPaperQuestion;
import com.yuanluesoft.enterprise.exam.pojo.ExamPost;
import com.yuanluesoft.enterprise.exam.pojo.ExamQuestionType;
import com.yuanluesoft.enterprise.exam.pojo.ExamTest;
import com.yuanluesoft.enterprise.exam.pojo.ExamTestAnswer;
import com.yuanluesoft.enterprise.exam.pojo.ExamWrongQuestion;
import com.yuanluesoft.enterprise.exam.question.model.DifficultyLevel;
import com.yuanluesoft.enterprise.exam.question.pojo.Question;
import com.yuanluesoft.enterprise.exam.question.pojo.QuestionAnswer;
import com.yuanluesoft.enterprise.exam.question.service.QuestionService;
import com.yuanluesoft.enterprise.exam.service.ExamService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.pojo.Role;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.RoleService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class ExamServiceImpl extends BusinessServiceImpl implements ExamService {
	private QuestionService questionService; //题库服务
	private int makeOutAfterUsedDays = 5; //允许出makeOutAfterUsedDays天前使用过的题
	private RoleService roleService; //角色管理
	private OrgService orgService; //组织机构服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#load(java.lang.Class, long)
	 */
	public Record load(Class recordClass, long id) throws ServiceException {
		Record record = super.load(recordClass, id);
		if(record instanceof ExamPaper) {
			ExamPaper examPaper = (ExamPaper)record;
			if(examPaper.getPaperQuestions()!=null && !examPaper.getPaperQuestions().isEmpty()) {
				//获取题目
				List questions = questionService.listQuestions(ListUtils.join(examPaper.getPaperQuestions(), "questionId", ",", false));
				for(Iterator iterator = examPaper.getPaperQuestions().iterator(); iterator.hasNext();) {
					ExamPaperQuestion examPaperQuestion = (ExamPaperQuestion)iterator.next();
					examPaperQuestion.setQuestion((Question)ListUtils.findObjectByProperty(questions, "id", new Long(examPaperQuestion.getQuestionId())));
				}
			}
		}
		else if(record instanceof ExamTest) {
			ExamTest examTest = (ExamTest)record;
			//获取考卷
			ExamPaper examPaper = (ExamPaper)load(ExamPaper.class, examTest.getPaperId());
			examTest.setExamPaper(examPaper); //考卷
			examTest.setExam(examPaper.getExam()); //考试
			examTest.setExamTestQuestionTypes(generateExamTestQuestionTypes(examPaper.getPaperQuestions(), examTest.getTestAnswers())); //考试题型(ExamTestQuestionType)列表
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		if(record instanceof Exam) { //考试
			Exam exam = (Exam)record;
			//检查是否有考卷存在
			if(getDatabaseService().findRecordByHql("select ExamPaper.id from ExamPaper ExamPaper where ExamPaper.examId=" + exam.getId())!=null) {
				//考卷存在,将考试设置为已删除
				exam.setIsDeleted(1);
				update(exam);
				return;
			}
		}
		super.delete(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.exam.service.ExamService#updateExamComponents(com.yuanluesoft.enterprise.exam.pojo.Exam, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	public void updateExamComponents(Exam exam, boolean isNewExam, String examPostIds, String examPostNames, String examCorrectorIds, String examCorrectorNames, HttpServletRequest request) throws ServiceException {
		//题型
		String[] examQuestionTypes = request.getParameterValues("examQuestionType");
		if(examQuestionTypes!=null) {
			if(!isNewExam) {
				getDatabaseService().deleteRecordsByHql("from ExamQuestionType ExamQuestionType where ExamQuestionType.examId=" + exam.getId());
			}
			for(int i=0; i<examQuestionTypes.length; i++) {
				ExamQuestionType examQuestionType = new ExamQuestionType();
				examQuestionType.setId(UUIDLongGenerator.generateId()); //ID
				examQuestionType.setExamId(exam.getId()); //考试ID
				examQuestionType.setQuestionType(examQuestionTypes[i]); //题型
				int questionNumber = RequestUtils.getParameterIntValue(request, "examQuestionNumber_" + examQuestionTypes[i]);
				examQuestionType.setQuestionNumber(questionNumber%1000); //题目数
				double questionScore = RequestUtils.getParameterDoubleValue(request, "examQuestionScore_" + examQuestionTypes[i]);
				examQuestionType.setQuestionScore(((int)(questionScore * 10)%10000/10.0)); //分数
				getDatabaseService().saveRecord(examQuestionType);
			}
		}
		
		//难度
		String[] examDifficultyLevels = request.getParameterValues("examDifficultyLevel");
		if(examDifficultyLevels!=null) {
			if(!isNewExam) {
				getDatabaseService().deleteRecordsByHql("from ExamDifficultyLevel ExamDifficultyLevel where ExamDifficultyLevel.examId=" + exam.getId());
			}
			for(int i=0; i<examDifficultyLevels.length; i++) {
				if(examDifficultyLevels[i].isEmpty()) {
					continue;
				}
				ExamDifficultyLevel examDifficultyLevel = new ExamDifficultyLevel();
				examDifficultyLevel.setId(UUIDLongGenerator.generateId()); //ID
				examDifficultyLevel.setExamId(exam.getId()); //考试ID
				examDifficultyLevel.setDifficulty(examDifficultyLevels[i]); //难度
				double difficultyRatio = RequestUtils.getParameterDoubleValue(request, "examDifficultyRatio_" + examDifficultyLevels[i]);
				examDifficultyLevel.setRatio(((int)(difficultyRatio * 10)%10000/10.0)); //比例
				getDatabaseService().saveRecord(examDifficultyLevel);
			}
		}
		
		//适用岗位
		if(examPostIds!=null) {
			if(!isNewExam) {
				getDatabaseService().deleteRecordsByHql("from ExamPost ExamPost where ExamPost.examId=" + exam.getId());
			}
			if(!examPostIds.isEmpty()) {
				String[] ids = examPostIds.split(",");
				String[] values = examPostNames.split(",");
				for(int i=0; i<ids.length; i++) {
					ExamPost examPost = new ExamPost();
					examPost.setId(UUIDLongGenerator.generateId()); //ID
					examPost.setExamId(exam.getId()); //考试ID
					examPost.setPostId(Long.parseLong(ids[i])); //岗位ID
					examPost.setPost(values[i]); //岗位
					getDatabaseService().saveRecord(examPost);
				}
			}
		}
		
		//批改人
		if(examCorrectorIds!=null) {
			if(!isNewExam) {
				getDatabaseService().deleteRecordsByHql("from ExamCorrector ExamCorrector where ExamCorrector.examId=" + exam.getId());
			}
			if(!examCorrectorIds.isEmpty()) {
				String[] ids = examCorrectorIds.split(",");
				String[] values = examCorrectorNames.split(",");
				for(int i=0; i<ids.length; i++) {
					ExamCorrector examCorrector = new ExamCorrector();
					examCorrector.setId(UUIDLongGenerator.generateId()); //ID
					examCorrector.setExamId(exam.getId()); //考试ID
					examCorrector.setCorrectorId(Long.parseLong(ids[i])); //批改人ID
					examCorrector.setCorrector(values[i]); //批改人
					getDatabaseService().saveRecord(examCorrector);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.exam.service.ExamService#generateSelfTestExamPaper(com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void generateSelfTestExamPaper(SessionInfo sessionInfo) throws ServiceException {
		if(sessionInfo==null || sessionInfo.getRoleIds()==null || sessionInfo.getRoleIds().isEmpty()) {
			return;
		}
		//获取没有在考卷子的考试
		String hql = "select distinct Exam" +
					 " from Exam Exam left join Exam.examPosts ExamPost" +
					 " where Exam.isDeleted!=1" +
					 " and Exam.selfTest=1" + //自助方式
					 " and ExamPost.postId in (" + sessionInfo.getRoleIds() + ")" + //岗位匹配
					 " and (" +
					 "  select min(ExamPaper.id)" +
					 "   from ExamPaper ExamPaper" +
					 "   where ExamPaper.examId=Exam.id" +
					 "   and ExamPaper.creatorId=" + sessionInfo.getUserId() +
					 "   and (select min(ExamTest.id) from ExamTest ExamTest where ExamTest.paperId=ExamPaper.id) is null" + //没有回答
					 " ) is null"; //没有在考卷子
		List exams = getDatabaseService().findRecordsByHql(hql);
		if(exams==null || exams.isEmpty()) {
			return;
		}
		for(Iterator iterator = exams.iterator(); iterator.hasNext();) {
			Exam exam = (Exam)iterator.next();
			Date date = DateTimeUtils.date();
			//检查是否已经超出日考试次数上限
			if(exam.getExamDayLimit()>0) {
				hql = "select count(ExamPaper.id)" +
					  " from ExamPaper ExamPaper" +
					  " where ExamPaper.examId=" + exam.getId() +
					  " and ExamPaper.creatorId=" + sessionInfo.getUserId() +
					  " and ExamPaper.created>=DATE(" + DateTimeUtils.formatDate(date, null) + ")" +
					  " and ExamPaper.created<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(date, Calendar.DAY_OF_MONTH, 1), null) + ")";
				Number count = (Number)getDatabaseService().findRecordByHql(hql);
				if(count!=null && count.intValue()>=exam.getExamDayLimit()) {
					continue;
				}
			}
			//检查是否已经超过月考试上限
			date = DateTimeUtils.set(date, Calendar.DAY_OF_MONTH, 1);
			if(exam.getMonthQuestionNumber()>0) {
				if(exam.getMonthQuestionNumber()<=getTestedQuestionNumber(exam.getId(), date, DateTimeUtils.add(date, Calendar.MONTH, 1), sessionInfo)) {
					continue;
				}
			}
			//检查是否已经超过年考试上限
			if(exam.getYearQuestionNumber()>0) {
				date = DateTimeUtils.set(date, Calendar.MONTH, 0);
				if(exam.getYearQuestionNumber()<=getTestedQuestionNumber(exam.getId(), date, DateTimeUtils.add(date, Calendar.YEAR, 1), sessionInfo)) {
					continue;
				}
			}
			//出卷
			generateExamPaper(exam.getId(), exam.getName(), false, null, null, true, sessionInfo);
		}
	}
	
	/**
	 * 获取指定时间段内的出题量
	 * @param examId
	 * @param beginDate
	 * @param endDate
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	private int getTestedQuestionNumber(long examId, Date beginDate, Date endDate, SessionInfo sessionInfo) throws ServiceException {
		String hql = "select count(ExamPaperQuestion.id)" +
					 " from ExamPaper ExamPaper left join ExamPaper.paperQuestions ExamPaperQuestion" +
					 " where ExamPaper.examId=" + examId +
					 " and ExamPaper.creatorId=" + sessionInfo.getUserId() +
					 " and ExamPaper.created>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" +
					 " and ExamPaper.created<DATE(" + DateTimeUtils.formatDate(endDate, null) + ")";
		Number count = (Number)getDatabaseService().findRecordByHql(hql);
		return count==null ? 0 : count.intValue();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.exam.service.ExamService#generateExamPaper(long, long, java.lang.String, int, java.sql.Timestamp, java.sql.Timestamp, int, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ExamPaper generateExamPaper(long examId, String examPaperName, boolean resit, Timestamp beginTime, Timestamp endTime, boolean onComputer, SessionInfo sessionInfo) throws ServiceException {
		ExamPaper examPaper = new ExamPaper();
		examPaper.setId(UUIDLongGenerator.generateId()); //ID
		examPaper.setExamId(examId); //考试ID
		examPaper.setExamPaperName(examPaperName); //考卷名称
		examPaper.setResit(resit ? 1 : 0); //是否补考,只出原来做错的题目
		examPaper.setBeginTime(beginTime); //开始时间
		examPaper.setEndTime(endTime); //截止时间,如果有指定截止时间,则要求在截止时间前完成,否则,时间以用户开始答卷为准
		examPaper.setOnComputer(onComputer ? 1 : 0); //计算机作答
		examPaper.setCreated(DateTimeUtils.now()); //出卷时间
		examPaper.setCreatorId(sessionInfo.getUserId()); //出卷人ID
		examPaper.setCreator(sessionInfo.getUserName()); //出卷人
		
		//获取考试
		Exam exam = (Exam)load(Exam.class, examId);
		List difficultyLevels = questionService.listDifficultyLevels(); //难度等级列表
		List examQuestionTypes = new ArrayList(exam.getExamQuestionTypes());
		List examPaperQuestions = new ArrayList();
		double examPaperScore = 0; //试卷分数
		int previousTypeGap = 0; //前一个题型题目数缺口
		for(int i=examQuestionTypes.size()-1; i>=0; i--) { //从最后一个题型开始出题,如果题量不够,则增加前一个题型的出题量
			ExamQuestionType examQuestionType = (ExamQuestionType)examQuestionTypes.get(i);
			//出题数:题型题目数+前一个题型题目数缺口
			int questionNumber = examQuestionType.getQuestionNumber() + previousTypeGap;
			System.out.println("*************examPaperQuestionType1:" + examQuestionType.getQuestionType() + ", " + previousTypeGap + "," + examQuestionType.getQuestionNumber());
			List questions = makeOutQuestions(examQuestionType.getQuestionType(), questionNumber, exam, difficultyLevels, 0, onComputer, sessionInfo);
			if(questions.size()<questionNumber) {
				questions.addAll(makeOutQuestions(examQuestionType.getQuestionType(), questionNumber - questions.size(), exam, difficultyLevels, makeOutAfterUsedDays, onComputer, sessionInfo));
			}
			for(Iterator iterator = questions.iterator(); iterator.hasNext();) {
				Question question = (Question)iterator.next();
				ExamPaperQuestion examPaperQuestion = new ExamPaperQuestion();
				examPaperQuestion.setPaperId(examPaper.getId()); //考卷ID
				examPaperQuestion.setQuestionId(question.getId()); //题目ID
				examPaperQuestion.setQuestion(question); //题目
				examPaperQuestion.setQuestionScore(examQuestionType.getQuestionScore()<=0.5 ? 0.5 : Math.round(examQuestionType.getQuestionScore())); //分数
				examPaperScore += examPaperQuestion.getQuestionScore();
				examPaperQuestions.add(examPaperQuestion);
			}
			//计算题目缺口
			previousTypeGap = questionNumber - questions.size();
			System.out.println("*************examPaperQuestionType2:" + examQuestionType.getQuestionType() + ", " + previousTypeGap + "," + examQuestionType.getQuestionNumber());
		}
		
		//按比例调整分数
		int examScore = exam.getScore()<1 ? 100 : exam.getScore();
		double ratio = examPaperScore/examScore; //分数缩放比例
		examPaperScore = 0;
		for(Iterator iterator = examPaperQuestions.iterator(); iterator.hasNext();) {
			ExamPaperQuestion examPaperQuestion = (ExamPaperQuestion)iterator.next();
			double questionScore = examPaperQuestion.getQuestionScore()/ratio;
			examPaperQuestion.setQuestionScore(questionScore<=0.5 ? 0.5 : Math.round(questionScore)); //题目分值
			examPaperScore += examPaperQuestion.getQuestionScore();
			System.out.println("********score1:" + examPaperQuestion.getQuestionScore() + "/" + examPaperQuestion.getQuestion().getQuestionType() + "/" + examPaperScore);
		}
		
		List questionTypes = ListUtils.generatePropertyList(exam.getExamQuestionTypes(), "questionType");
		//调整题目分数
		if(examPaperQuestions.size()*0.5>examScore) { //以每题0.5分计算,卷面分数都超出总分
			sortQuestions(examPaperQuestions, questionTypes, true); //排序
			examPaperScore = 0;
			//把所有题目分值都设置为0.5,并删除后面的题目
			for(Iterator iterator = examPaperQuestions.iterator(); iterator.hasNext();) {
				ExamPaperQuestion examPaperQuestion = (ExamPaperQuestion)iterator.next();
				if(examPaperScore>=examScore) {
					iterator.remove();
				}
				else {
					examPaperQuestion.setQuestionScore(0.5);
					examPaperScore += examPaperQuestion.getQuestionScore();
					System.out.println("********score2:" + examPaperQuestion.getQuestionScore() + "/" + examPaperQuestion.getQuestion().getQuestionType() + "/" + examPaperScore);
				}
			}
		}
		else if(!examPaperQuestions.isEmpty() && examPaperScore<examScore) { //卷面分数低于考试总分
			sortQuestions(examPaperQuestions, questionTypes, true); //排序
			//从后向前加分,每次加1分
			while(examPaperScore<examScore) {
				for(int i=examPaperQuestions.size()-1; i>=0 && examPaperScore<examScore; i--) {
					ExamPaperQuestion examPaperQuestion = (ExamPaperQuestion)examPaperQuestions.get(i);
					double inc = Math.min(examScore-examPaperScore, 1);
					examPaperScore += inc;
					examPaperQuestion.setQuestionScore(examPaperQuestion.getQuestionScore()+inc);
					System.out.println("********score3:" + examPaperQuestion.getQuestionScore() + "/" + examPaperQuestion.getQuestion().getQuestionType() + "/" + examPaperScore);
				}
			}
		}
		else if(!examPaperQuestions.isEmpty() && examPaperScore>examScore) { //卷面分数超过考试总分
			sortQuestions(examPaperQuestions, questionTypes, false); //排序
			//从后向前减分,每次减1分,不够1分时减0.5
			while(examPaperScore>examScore) {
				for(int i=examPaperQuestions.size()-1; i>=0 && examPaperScore>examScore; i--) {
					ExamPaperQuestion examPaperQuestion = (ExamPaperQuestion)examPaperQuestions.get(i);
					if(examPaperQuestion.getQuestionScore()<1) {
						continue;
					}
					double dec = Math.min(examPaperScore-examScore, (examPaperQuestion.getQuestionScore()>1 ? 1 : 0.5));
					examPaperScore -= dec;
					examPaperQuestion.setQuestionScore(examPaperQuestion.getQuestionScore()-dec);
					System.out.println("********score4:" + examPaperQuestion.getQuestionScore() + "/" + examPaperQuestion.getQuestion().getQuestionType() + "/" + examPaperScore);
				}
			}
		}
		//保存出题记录,并将题目设置为已使用
		sortQuestions(examPaperQuestions, questionTypes, true); //排序
		for(Iterator iterator = examPaperQuestions.iterator(); iterator.hasNext();) {
			ExamPaperQuestion examPaperQuestion = (ExamPaperQuestion)iterator.next();
			examPaperQuestion.setId(UUIDLongGenerator.generateId());
			getDatabaseService().saveRecord(examPaperQuestion);
			questionService.questionUsed(examPaperQuestion.getQuestionId(), sessionInfo.getUserId());
		}
		examPaper.setScore(examPaperScore); //卷面总分
		examPaper.setPaperQuestions(new LinkedHashSet(examPaperQuestions));
		if(exam.getSelfTest()==1) { //自助方式,总是自动发布
			examPaper.setStatus(1); //已发布
			examPaper.setReleasePerson(sessionInfo.getUserName()); //发布人
			examPaper.setReleasePersonId(sessionInfo.getUserId()); //发布人ID
			examPaper.setReleaseTime(DateTimeUtils.now()); //发布时间
			//添加权限记录
			ExamPaperPrivilege privilege = new ExamPaperPrivilege();
			privilege.setId(UUIDLongGenerator.generateId());
			privilege.setAccessLevel(RecordControlService.ACCESS_LEVEL_READONLY);
			privilege.setRecordId(examPaper.getId());
			privilege.setVisitorId(sessionInfo.getUserId());
			getDatabaseService().saveRecord(privilege);
		}
		getDatabaseService().saveRecord(examPaper); //保存记录
		return examPaper;
	}
	
	/**
	 * 按题型和难度对题目排序
	 * @param questions
	 * @param difficultyLevels
	 * @param lowDifficultyFirst
	 */
	private void sortQuestions(List examPaperQuestions, final List questionTypes, final boolean lowDifficultyFirst) {
		Collections.sort(examPaperQuestions, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				ExamPaperQuestion examPaperQuestion0 = (ExamPaperQuestion)arg0;
				ExamPaperQuestion examPaperQuestion1 = (ExamPaperQuestion)arg1;
				int index0 = questionTypes.indexOf(examPaperQuestion0.getQuestion().getQuestionType());
				int index1 = questionTypes.indexOf(examPaperQuestion1.getQuestion().getQuestionType());
				if(index0>index1) {
					return 1;
				}
				else if(index0<index1) {
					return -1;
				}
				else if(examPaperQuestion0.getQuestion().getDifficulty()>examPaperQuestion1.getQuestion().getDifficulty()) {
					return lowDifficultyFirst ? 1 : -1;
				}
				else if(examPaperQuestion0.getQuestion().getDifficulty()<examPaperQuestion1.getQuestion().getDifficulty()) {
					return lowDifficultyFirst ? -1 : 1;
				}
				else if(examPaperQuestion0.getQuestionScore()>examPaperQuestion1.getQuestionScore()) {
					return 1;
				}
				else if(examPaperQuestion0.getQuestionScore()<examPaperQuestion1.getQuestionScore()) {
					return -1;
				}
				return 0;
			}
		});
	}
	
	/**
	 * 出题
	 * @param questionType
	 * @param questionNumber
	 * @param exam
	 * @param makeOutAfterUsedDays
	 * @param answerOnComputerOnly
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	private List makeOutQuestions(String questionType, int questionNumber, Exam exam, List difficultyLevels, int makeOutAfterUsedDays, boolean answerOnComputerOnly, SessionInfo sessionInfo) throws ServiceException {
		int questionTotal = questionNumber;
		int previousDifficultyGap = 0; //前一个难度题目数缺口
		List makeOutQuestions = new ArrayList();
		for(Iterator iteratorDifficulty = exam.getExamDifficultyLevels()==null ? null : exam.getExamDifficultyLevels().iterator(); questionNumber>0 && iteratorDifficulty!=null && iteratorDifficulty.hasNext();) {
			ExamDifficultyLevel examDifficultyLevel = (ExamDifficultyLevel)iteratorDifficulty.next();
			DifficultyLevel difficultyLevel = (DifficultyLevel)ListUtils.findObjectByProperty(difficultyLevels, "level", examDifficultyLevel.getDifficulty());
			if(examDifficultyLevel==null) { //难度等级已经不存在
				continue;
			}
			int difficultyQuestionNumber = Math.min(questionNumber, (int)Math.round(questionTotal * examDifficultyLevel.getRatio() / 100.0) + previousDifficultyGap);
			System.out.println("*************difficultyLevel1:" + examDifficultyLevel.getDifficulty() + ", difficultyQuestionNumber:" + difficultyQuestionNumber + ", " + examDifficultyLevel.getRatio() + ", " + previousDifficultyGap + ", " + questionNumber);
			int index = difficultyLevels.indexOf(difficultyLevel);
			int minDifficulty = index==0 ? 1 : ((DifficultyLevel)difficultyLevels.get(index-1)).getDifficulty()+1; //最低难度,上一个等级的难度+1
			int maxDifficulty = difficultyLevel.getDifficulty(); //最大难度
			List questions = questionService.makeOutQuestions(questionType, difficultyQuestionNumber, makeOutAfterUsedDays, ListUtils.join(exam.getExamPosts(), "postId", ",", false), exam.getKnowledgeIds(), exam.getItemIds(), minDifficulty, maxDifficulty, answerOnComputerOnly, sessionInfo.getUserId());
			if(questions!=null) {
				makeOutQuestions.addAll(questions);
			}
			previousDifficultyGap = difficultyQuestionNumber - (questions==null ? 0 : questions.size());
			questionNumber -= questions==null ? 0 : questions.size();
			System.out.println("*************difficultyLevel2:" + examDifficultyLevel.getDifficulty() + ", difficultyQuestionNumber:" + difficultyQuestionNumber + ", questions:" + (questions==null ? 0 : questions.size()) + ", getRatio:" + examDifficultyLevel.getRatio() + ", " + previousDifficultyGap + ", " + questionNumber);
		}
		if(questionNumber>0) { //没有足够的题目
			//在全部难度等级中补充题目
			List questions = questionService.makeOutQuestions(questionType, questionNumber, makeOutAfterUsedDays, ListUtils.join(exam.getExamPosts(), "postId", ",", false), exam.getKnowledgeIds(), exam.getItemIds(), 0, 100, answerOnComputerOnly, sessionInfo.getUserId());
			if(questions!=null) {
				makeOutQuestions.addAll(questions);
			}
		}
		return makeOutQuestions;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.exam.service.ExamService#releaseExamPaper(com.yuanluesoft.enterprise.exam.pojo.ExamPaper, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void releaseExamPaper(ExamPaper examPaper, SessionInfo sessionInfo) throws ServiceException {
		examPaper.setStatus(1); //已发布
		examPaper.setReleasePerson(sessionInfo.getUserName()); //发布人
		examPaper.setReleasePersonId(sessionInfo.getUserId()); //发布人ID
		examPaper.setReleaseTime(DateTimeUtils.now()); //发布时间
		getDatabaseService().updateRecord(examPaper);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.exam.service.ExamService#createExamTest(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ExamTest createExamTest(long examPaperId, SessionInfo sessionInfo) throws ServiceException {
		if(sessionInfo!=null) {
			String hql = "from ExamTest ExamTest where ExamTest.personId=" + sessionInfo.getUserId() + " and ExamTest.paperId=" + examPaperId;
			if(getDatabaseService().findRecordByHql(hql)!=null) {
				return null;
			}
		}
		//获取考卷
		ExamPaper examPaper = (ExamPaper)load(ExamPaper.class, examPaperId);
		//创建答卷
		ExamTest examTest = new ExamTest();
		examTest.setId(UUIDLongGenerator.generateId());
		examTest.setPaperId(examPaper.getId()); //考卷ID
		if(sessionInfo!=null) {
			examTest.setPersonId(sessionInfo.getUserId()); //用户ID
			examTest.setPersonName(sessionInfo.getUserName()); //用户名
		}
		examTest.setScore(0); //成绩
		examTest.setBeginTime(DateTimeUtils.now()); //开始时间
		examTest.setEndTime(null); //完成时间
		examTest.setStatus(EXAM_TEST_STATUS_TESTING); //状态,0/在考,1/待批改,2/完成,3/放弃
		examTest.setExamPaper(examPaper); //考卷
		examTest.setExam(examPaper.getExam()); //考试
		examTest.setExamTestQuestionTypes(generateExamTestQuestionTypes(examPaper.getPaperQuestions(), examTest.getTestAnswers())); //考试题型(ExamTestQuestionType)列表
		return examTest;
	}
	
	/**
	 * 生成答卷题型列表
	 * @param examPaperQuestions
	 * @param examTestAnswers
	 * @return
	 */
	private List generateExamTestQuestionTypes(Set examPaperQuestions, Set examTestAnswers) {
		if(examPaperQuestions==null || examPaperQuestions.isEmpty()) {
			return null;
		}
		List examTestQuestionTypes = new ArrayList();
		ExamTestQuestionType examTestQuestionType = null;
		for(Iterator iterator = examPaperQuestions.iterator(); iterator.hasNext();) {
			ExamPaperQuestion examPaperQuestion = (ExamPaperQuestion)iterator.next();
			if(examPaperQuestion.getQuestion()==null) {
				continue;
			}
			if(examTestQuestionType==null || !examPaperQuestion.getQuestion().getQuestionType().equals(examTestQuestionType.getQuestionType())) {
				examTestQuestionType = new ExamTestQuestionType();
				examTestQuestionType.setQuestionType(examPaperQuestion.getQuestion().getQuestionType());
				examTestQuestionType.setExamTestQuestions(new ArrayList());
				examTestQuestionTypes.add(examTestQuestionType);
			}
			examPaperQuestion.setExamTestAnswer((ExamTestAnswer)ListUtils.findObjectByProperty(examTestAnswers, "paperQuestionId", new Long(examPaperQuestion.getId())));
			examTestQuestionType.getExamTestQuestions().add(examPaperQuestion);
		}
		return examTestQuestionTypes;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.exam.service.ExamService#submitExamTest(com.yuanluesoft.enterprise.exam.pojo.ExamTest, javax.servlet.http.HttpServletRequest)
	 */
	public void submitExamTest(ExamTest examTest, HttpServletRequest request) throws ServiceException {
		//检查用户原来是否提交过答卷
		if(examTest.getPersonId()>0) {
			String hql = "from ExamTest ExamTest where ExamTest.personId=" + examTest.getPersonId() + " and ExamTest.paperId=" + examTest.getPaperId();
			if(getDatabaseService().findRecordByHql(hql)!=null) {
				throw new ValidateException("试卷已经回答过，不允许重复作答。");
			}
		}
		
		boolean toCorrect = false; //是否需要批改
		double testScore = 0;
		//保存答题记录
		for(Iterator iterator = examTest.getExamPaper().getPaperQuestions()==null ? null : examTest.getExamPaper().getPaperQuestions().iterator(); iterator!=null && iterator.hasNext();) {
			ExamPaperQuestion examPaperQuestion = (ExamPaperQuestion)iterator.next();
			String[] answers = request.getParameterValues("answer_" + examPaperQuestion.getId());
			if(answers==null || answers.length==0) { //没有作答
				continue;
			}
			ExamTestAnswer examTestAnswer = new ExamTestAnswer();
			examTestAnswer.setId(UUIDLongGenerator.generateId()); //ID
			examTestAnswer.setTestId(examTest.getId()); //答卷ID
			examTestAnswer.setPaperQuestionId(examPaperQuestion.getId()); //出题记录ID
			examTestAnswer.setAnswer(ListUtils.join(answers, "\r\n", false)); //答案,多选题、填空题答案用\r\n分隔
			examTestAnswer.setScore(0); //得分
			boolean corrected = false;
			if(examPaperQuestion.getQuestion().getComputerMarking()!=1 || //计算机不可批改
			   examPaperQuestion.getQuestion().getQuestionAnswers()==null || examPaperQuestion.getQuestion().getQuestionAnswers().isEmpty()) { //答案为空
				corrected = false;
			}
			else if(!"填空题".equals(examPaperQuestion.getQuestion().getQuestionType())) { //不是填空题
				examTestAnswer.setScore(StringUtils.isEquals(examTestAnswer.getAnswer(), ListUtils.join(examPaperQuestion.getQuestion().getQuestionAnswers(), "questionAnswer", "\r\n", false)) ? examPaperQuestion.getQuestionScore() : 0); //得分
				corrected = true;
			}
			else if(examPaperQuestion.getQuestion().getQuestionAnswers().size()==answers.length) { //填空题,答案个数匹配
				corrected = true;
				double correct = 0; //回答正确的空格
				int index = 0;
				for(Iterator iteratorAnswer = examPaperQuestion.getQuestion().getQuestionAnswers().iterator(); corrected && iteratorAnswer.hasNext();) {
					QuestionAnswer questionAnswer = (QuestionAnswer)iteratorAnswer.next();
					if(questionAnswer.getQuestionAnswer()==null || questionAnswer.getQuestionAnswer().isEmpty()) {
						corrected = false;
						break;
					}
					String[] questionAnswers = questionAnswer.getQuestionAnswer().split("###"); //有多个正确答案时,用###分隔
					int i = questionAnswers.length-1;
					for(; i>=0; i--) {
						if(questionAnswer.getCaseSensitive()=='1') { //区分大小写
							if(questionAnswers[i].equals(answers[i].trim())) {
								break;
							}
						}
						else { //不区分大小写
							if(questionAnswers[i].equalsIgnoreCase(answers[i].trim())) {
								break;
							}
						}
					}
					if(i>=0) { //答案正确
						correct++;
					}
					index++;
				}
				if(corrected) {
					double score = Math.round(examPaperQuestion.getQuestionScore() * (correct/examPaperQuestion.getQuestion().getQuestionAnswers().size()) * 2.0) / 2.0;
					if(correct>0 && correct<examPaperQuestion.getQuestion().getQuestionAnswers().size()) {
						score = Math.min(examPaperQuestion.getQuestionScore()-0.5, Math.max(0.5, score));
					}
					examTestAnswer.setScore(score);
				}
			}
			if(!corrected) { //没有批改过
				toCorrect = true;
			}
			else { //已批改
				testScore += examTestAnswer.getScore();
				if(examTestAnswer.getScore()<examPaperQuestion.getQuestionScore() && examTestAnswer.getCorrectorId()==0) {
					addWrongQuestion(examTest.getPersonId(), examPaperQuestion.getQuestionId(), examTestAnswer.getId()); //加入错题本
				}
				examTestAnswer.setCorrectorId(10); //批改人ID
				examTestAnswer.setCorrector("系统"); //批改人姓名
			}
			getDatabaseService().saveRecord(examTestAnswer);
		}
		//保存答卷
		examTest.setStatus(toCorrect ? EXAM_TEST_STATUS_TOCONRRECT : EXAM_TEST_STATUS_COMPLETE); //状态
		examTest.setScore(toCorrect ? 0 : testScore); //考试分数
		examTest.setEndTime(DateTimeUtils.now()); //考试结束时间
		getDatabaseService().saveRecord(examTest);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.exam.service.ExamService#correctExamTest(com.yuanluesoft.enterprise.exam.pojo.ExamTest, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void correctExamTest(ExamTest examTest, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		boolean toCorrect = false; //是否需要批改
		double testScore = 0;
		//保存答题记录
		for(Iterator iterator = examTest.getExamPaper().getPaperQuestions()==null ? null : examTest.getExamPaper().getPaperQuestions().iterator(); iterator!=null && iterator.hasNext();) {
			ExamPaperQuestion examPaperQuestion = (ExamPaperQuestion)iterator.next();
			if(examPaperQuestion.getExamTestAnswer()==null) { //没有作答
				continue;
			}
			if(examPaperQuestion.getExamTestAnswer().getCorrectorId()==10) { //计算机批改
				testScore += examPaperQuestion.getExamTestAnswer().getScore();
				continue;
			}
			String score = request.getParameter("score_" + examPaperQuestion.getId());
			if(score==null || score.isEmpty()) { //没有批改
				if(examPaperQuestion.getExamTestAnswer().getCorrectorId()>0) { //已经批改过
					testScore += examPaperQuestion.getExamTestAnswer().getScore();
				}
				else if(examTest.getStatus()!=EXAM_TEST_STATUS_COMPLETE) {
					toCorrect = true;
				}
				continue;
			}
			if(examPaperQuestion.getExamTestAnswer().getCorrectorId()>0 && examPaperQuestion.getExamTestAnswer().getScore()==Double.parseDouble(score)) { //已经批改过,分数没有变化
				testScore += examPaperQuestion.getExamTestAnswer().getScore();
				continue;
			}
			examPaperQuestion.getExamTestAnswer().setScore(Double.parseDouble(score));
			if(examPaperQuestion.getExamTestAnswer().getScore()<examPaperQuestion.getQuestionScore() && examPaperQuestion.getExamTestAnswer().getCorrectorId()==0) {
				addWrongQuestion(examTest.getPersonId(), examPaperQuestion.getQuestionId(), examPaperQuestion.getExamTestAnswer().getId()); //加入错题本
			}
			testScore += examPaperQuestion.getExamTestAnswer().getScore();
			examPaperQuestion.getExamTestAnswer().setRemark(request.getParameter("remark_" + examPaperQuestion.getId()));
			examPaperQuestion.getExamTestAnswer().setCorrector(sessionInfo.getUserName());
			examPaperQuestion.getExamTestAnswer().setCorrectorId(sessionInfo.getUserId());
			getDatabaseService().updateRecord(examPaperQuestion.getExamTestAnswer());
		}
		//更新答卷
		examTest.setStatus(toCorrect ? EXAM_TEST_STATUS_TOCONRRECT : EXAM_TEST_STATUS_COMPLETE); //状态
		examTest.setScore(toCorrect ? 0 : testScore); //考试分数
		getDatabaseService().updateRecord(examTest);
	}
	
	/**
	 * 加入错题本
	 * @param personId
	 * @param questionId
	 * @param answerId
	 * @throws ServiceException
	 */
	private void addWrongQuestion(long personId, long questionId, long answerId) throws ServiceException {
		ExamWrongQuestion wrongQuestion = new ExamWrongQuestion();
		wrongQuestion.setId(UUIDLongGenerator.generateId()); //ID
		wrongQuestion.setPersonId(personId); //用户ID
		wrongQuestion.setAnswerId(answerId); //答题记录ID
		wrongQuestion.setQuestionId(questionId); //题目ID
		wrongQuestion.setCreated(DateTimeUtils.now()); //加入时间
		//wrongQuestion.setreason; //答错原因
		getDatabaseService().saveRecord(wrongQuestion);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.exam.service.ExamService#cancelExamTest(com.yuanluesoft.enterprise.exam.pojo.ExamTest, javax.servlet.http.HttpServletRequest)
	 */
	public void cancelExamTest(ExamTest examTest, HttpServletRequest request) throws ServiceException {
		//保存答卷
		examTest.setStatus(EXAM_TEST_STATUS_CANCEL); //状态
		examTest.setEndTime(DateTimeUtils.now()); //考试结束时间
		getDatabaseService().saveRecord(examTest);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.exam.service.ExamService#isCorrector(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public boolean isCorrector(long examId, SessionInfo sessionInfo) throws ServiceException {
		String hql = "select ExamCorrector.id" +
					 " from ExamCorrector ExamCorrector" +
					 " where ExamCorrector.examId=" + examId +
					 " and ExamCorrector.correctorId=" + sessionInfo.getUserId();
		return getDatabaseService().findRecordByHql(hql)!=null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.exam.service.ExamService#listExamTests(long)
	 */
	public List listExamTests(long examPaperId) throws ServiceException {
		String hql = "from ExamTest ExamTest" +
					 " where ExamTest.paperId=" + examPaperId +
					 " order by ExamTest.endTime";
		return getDatabaseService().findRecordsByHql(hql, 0, 2000);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.exam.service.ExamService#getRankingList(long, int, int)
	 */
	public ExamRankingList getRankingList(long postId, int year, int month) throws ServiceException {
		ExamRankingList rankingList = new ExamRankingList();
		//获取岗位用户列表
		List persons = roleService.listRoleMembers("" + postId);

		//从自助式考试统计每月需要完成的题量
		String hql = "select sum(Exam.monthQuestionNumber)" +
					 " from Exam Exam left join Exam.examPosts ExamPost" +
					 " where Exam.isDeleted!=1" +
					 " and Exam.selfTest=1" + //自助方式
					 " and ExamPost.postId=" + postId;
		Number monthQuestionNumber = (Number)getDatabaseService().findRecordByHql(hql);
		rankingList.setMonthTestQuestionNumber(monthQuestionNumber==null ? 0 : monthQuestionNumber.intValue()); //每月需要完成的题量
		
		//统计答题数
		Date beginDate;
		try {
			beginDate = DateTimeUtils.parseDate(year + "-" + month + "-1", "yyyy-M-d");
		}
		catch (ParseException e) {
			throw new ServiceException();
		}
		Date endDate = DateTimeUtils.add(beginDate, Calendar.MONTH, 1);
		hql = "select ExamTest.personId, count(ExamTestAnswer.id)" +
			  " from ExamTest ExamTest left join ExamTest.testAnswers ExamTestAnswer" +
			  " where ExamTest.personId in (" + ListUtils.join(persons, "id", ",", false) + ")" +
			  " and ExamTest.beginTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" +
			  " and ExamTest.beginTime<DATE(" + DateTimeUtils.formatDate(endDate, null) + ")" +
			  " group by ExamTest.personId";
		List testedQuestionNumbers = getDatabaseService().findRecordsByHql(hql);
		
		//统计考分
		hql = "select ExamTest.personId, sum(ExamTest.score)" +
		  	  " from ExamTest ExamTest" +
		  	  " where ExamTest.status=" + EXAM_TEST_STATUS_COMPLETE +
		  	  " and ExamTest.personId in (" + ListUtils.join(persons, "id", ",", false) + ")" +
		  	  " and ExamTest.beginTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" +
			  " and ExamTest.beginTime<DATE(" + DateTimeUtils.formatDate(endDate, null) + ")" +
		  	  " group by ExamTest.personId";
		List scores = getDatabaseService().findRecordsByHql(hql);
		
		//生成名次表
		List transcripts = new ArrayList();
		for(Iterator iterator = persons.iterator(); iterator.hasNext();) {
			Person person = (Person)iterator.next();
			ExamTranscript examTranscript = new ExamTranscript();
			examTranscript.setPersonId(person.getId()); //用户ID
			examTranscript.setPersonName(person.getName()); //用户名
			//获取实际完成的题量
			for(Iterator iteratorQuestionNumber = testedQuestionNumbers==null ? null : testedQuestionNumbers.iterator(); iteratorQuestionNumber!=null && iteratorQuestionNumber.hasNext();) {
				Object[] values = (Object[])iteratorQuestionNumber.next();
				if(((Number)values[0]).longValue()==person.getId()) {
					examTranscript.setTestedQuestionNumber(((Number)values[1]).intValue()); //实际完成的题量
					break;
				}
			}
			//获取成绩
			for(Iterator iteratorScore = scores==null ? null : scores.iterator(); iteratorScore!=null && iteratorScore.hasNext();) {
				Object[] values = (Object[])iteratorScore.next();
				if(((Number)values[0]).longValue()==person.getId()) {
					examTranscript.setTestScore(((Number)values[1]).doubleValue()); //考试总分
					break;
				}
			}
			//最高得分
			if(rankingList.getTestMaxScore()<examTranscript.getTestScore()) {
				rankingList.setTestMaxScore(examTranscript.getTestScore());
			}
			//最低得分
			if(rankingList.getTestMinScore()>examTranscript.getTestScore()) {
				rankingList.setTestMinScore(examTranscript.getTestScore());
			}
			//平均分
			rankingList.setTestAverageScore(rankingList.getTestAverageScore() + examTranscript.getTestScore());
			transcripts.add(examTranscript);
		}
		rankingList.setTestAverageScore(Math.round(rankingList.getTestAverageScore()/transcripts.size() * 100) / 100.0); //平均分
		//排名
		Collections.sort(transcripts, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				ExamTranscript examTranscript0 = (ExamTranscript)arg0;
				ExamTranscript examTranscript1 = (ExamTranscript)arg1;
				if(examTranscript0.getTestScore()>examTranscript1.getTestScore()) {
					return -1;
				}
				else if(examTranscript0.getTestScore()<examTranscript1.getTestScore()) {
					return 1;
				}
				return 0;
			}
		});
		//设置名次
		for(int i=0; i<transcripts.size(); i++) {
			ExamTranscript examTranscript = (ExamTranscript)transcripts.get(i);
			if(i==0) {
				examTranscript.setTestPosition(1);
			}
			else if(examTranscript.getTestScore()==((ExamTranscript)transcripts.get(i-1)).getTestScore()) {
				examTranscript.setTestPosition(((ExamTranscript)transcripts.get(i-1)).getTestPosition());
			}
			else {
				examTranscript.setTestPosition(i+1);
			}
		}
		rankingList.setTranscripts(transcripts); //成绩单列表
		return rankingList;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.exam.service.ExamService#examStat(java.lang.String, java.lang.String, int, int)
	 */
	public List examStat(String postIds, String orgIds, int year, int month) throws ServiceException {
		List persons;
		if(postIds!=null && !postIds.isEmpty()) {
			if(orgIds!=null && !orgIds.isEmpty() && ("," + orgIds + ",").indexOf(",0,")==-1) {
				persons = roleService.listRoleMembersInOrgs(postIds, orgIds, true, false);
			}
			else {
				persons = roleService.listRoleMembers(postIds);
			}
		}
		else {
			persons = orgService.listOrgPersons(orgIds!=null && !orgIds.isEmpty() && ("," + orgIds + ",").indexOf(",0,")==-1 ? orgIds : "0", null, true, false, 0, 0);
		}
		if(persons==null || persons.isEmpty()) {
			return null;
		}
		//统计答题数
		Date beginDate;
		try {
			beginDate = DateTimeUtils.parseDate(year + "-" + month + "-1", "yyyy-M-d");
		}
		catch (ParseException e) {
			throw new ServiceException();
		}
		Date endDate = DateTimeUtils.add(beginDate, Calendar.MONTH, 1);
		//统计考分、考试次数,并检查考试状态
		String hql = "select ExamTest.personId, sum(ExamTest.score), count(ExamTest.id), min(ExamTest.status)" +
					 " from ExamTest ExamTest" +
					 " where (ExamTest.status=" + EXAM_TEST_STATUS_COMPLETE + " or ExamTest.status=" + EXAM_TEST_STATUS_TOCONRRECT + ")" +
					 " and ExamTest.personId in (" + ListUtils.join(persons, "id", ",", false) + ")" +
					 " and ExamTest.beginTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" +
					 " and ExamTest.beginTime<DATE(" + DateTimeUtils.formatDate(endDate, null) + ")" +
					 " group by ExamTest.personId";
		List results = getDatabaseService().findRecordsByHql(hql);
		
		List examStats = new ArrayList();
		for(Iterator iterator = persons.iterator(); iterator.hasNext();) {
			Person person = (Person)iterator.next();
			ExamStat examStat = new ExamStat();
			
			//姓名
			examStat.setName(person.getName());
			
			//获取部门
			String departmentIds = orgService.listOrgIdsOfPerson("" + person.getId(), false);
			String[] departmentNames = orgService.getDirectoryFullName(Long.parseLong(departmentIds.split(",")[0]), "/", "unit").split("/");
			examStat.setUnit(departmentNames[departmentNames.length==1 ? 0 : departmentNames.length-2]); //单位
			examStat.setDepartment(departmentNames.length==1 ? null : departmentNames[departmentNames.length-1]); //部门
			
			//获取岗位
			List posts = roleService.listRolesOfPerson("" + person.getId(), true);
			if(posts!=null && !posts.isEmpty()) {
				Role post = (Role)posts.get(0);
				examStat.setPost(post.getRoleName()); //岗位名称
			}
			
			//获取成绩
			for(Iterator iteratorResult = results==null ? null : results.iterator(); iteratorResult!=null && iteratorResult.hasNext();) {
				Object[] values = (Object[])iteratorResult.next();
				if(((Number)values[0]).longValue()==person.getId()) {
					examStat.setTotalScore(((Number)values[1]).doubleValue()); //总分
					examStat.setTimes(((Number)values[2]).intValue()); //考试次数
					examStat.setStatus(((Number)values[3]).intValue()==EXAM_TEST_STATUS_COMPLETE ? "考试批改完成" : "考试待批改"); //状态
					break;
				}
			}
			
			if(examStat.getTimes()>0) {
				//获取考卷名称
				hql = "select distinct Exam.name" +
					  " from ExamTest ExamTest, ExamPaper ExamPaper, Exam Exam" +
					  " where (ExamTest.status=" + EXAM_TEST_STATUS_COMPLETE + " or ExamTest.status=" + EXAM_TEST_STATUS_TOCONRRECT + ")" +
					  " and ExamTest.personId=" + person.getId() +
					  " and ExamTest.beginTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" +
					  " and ExamTest.beginTime<DATE(" + DateTimeUtils.formatDate(endDate, null) + ")" +
					  " and ExamPaper.id=ExamTest.paperId" +
					  " and Exam.id=ExamPaper.examId";
				List examNames = getDatabaseService().findRecordsByHql(hql);
				for(Iterator iteratorExam = results==null ? null : examNames.iterator(); iteratorExam!=null && iteratorExam.hasNext();) {
					String examName = (String)iteratorExam.next();
					examStat.setExamName((examStat.getExamName()==null ? "" : examStat.getExamName() + ",") + year + "年" + month + "月" + examName);
				}
			}
			examStats.add(examStat);
		}
		
		//排序,岗位->分数
		Collections.sort(examStats, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				ExamStat examStat0 = (ExamStat)arg0;
				ExamStat examStat1 = (ExamStat)arg1;
				int cmp = Collator.getInstance(Locale.CHINA).compare(examStat0.getPost()==null ? "" : examStat0.getPost(), examStat1.getPost()==null ? "" : examStat1.getPost());
				if(cmp!=0) {
					return cmp;
				}
				return (examStat0.getTotalScore()==examStat1.getTotalScore() ? 0 : (examStat0.getTotalScore()>examStat1.getTotalScore() ? -1 : 1));
			}
		});
		return examStats;
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

	/**
	 * @return the makeOutAfterUsedDays
	 */
	public int getMakeOutAfterUsedDays() {
		return makeOutAfterUsedDays;
	}

	/**
	 * @param makeOutAfterUsedDays the makeOutAfterUsedDays to set
	 */
	public void setMakeOutAfterUsedDays(int makeOutAfterUsedDays) {
		this.makeOutAfterUsedDays = makeOutAfterUsedDays;
	}

	/**
	 * @return the roleService
	 */
	public RoleService getRoleService() {
		return roleService;
	}

	/**
	 * @param roleService the roleService to set
	 */
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	/**
	 * @return the orgService
	 */
	public OrgService getOrgService() {
		return orgService;
	}

	/**
	 * @param orgService the orgService to set
	 */
	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}
}