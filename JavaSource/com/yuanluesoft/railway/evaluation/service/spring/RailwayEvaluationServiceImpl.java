package com.yuanluesoft.railway.evaluation.service.spring;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yuanluesoft.enterprise.evaluation.department.service.DepartmentEvaluationService;
import com.yuanluesoft.enterprise.evaluation.mutual.model.MutualEvaluationTotal;
import com.yuanluesoft.enterprise.evaluation.mutual.service.MutualEvaluationService;
import com.yuanluesoft.enterprise.exam.model.ExamRankingList;
import com.yuanluesoft.enterprise.exam.model.ExamTranscript;
import com.yuanluesoft.enterprise.exam.service.ExamService;
import com.yuanluesoft.enterprise.workload.pojo.WorkloadAssessResult;
import com.yuanluesoft.enterprise.workload.service.WorkloadService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.pojo.Role;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.usermanage.service.RoleService;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.railway.evaluation.model.RailwayEvaluation;
import com.yuanluesoft.railway.evaluation.pojo.RailwayEvaluationParameter;
import com.yuanluesoft.railway.evaluation.service.RailwayEvaluationService;
import com.yuanluesoft.railway.event.service.RailwayEventService;

/**
 * 
 * @author linchuan
 *
 */
public class RailwayEvaluationServiceImpl extends BusinessServiceImpl implements RailwayEvaluationService {
	private boolean evaluateCurrentMonth = false; //是否考评本月
	private WorkloadService workloadService; //工作量服务
	private DepartmentEvaluationService departmentEvaluationService; //部门考核服务
	private MutualEvaluationService mutualEvaluationService; //互评服务
	private ExamService examService; //考试服务
	private RailwayEventService railwayEventService; //铁路局问题服务
	private OrgService orgService; //组织机构服务
	private RoleService roleService; //角色服务
	private PersonService personService; //用户服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.railway.evaluation.service.RailwayEvaluationService#loadRailwayEvaluationParameter()
	 */
	public RailwayEvaluationParameter loadRailwayEvaluationParameter() throws ServiceException {
		return (RailwayEvaluationParameter)getDatabaseService().findRecordByHql("from RailwayEvaluationParameter RailwayEvaluationParameter");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.railway.evaluation.service.RailwayEvaluationService#listRailwayEvaluations(java.lang.String, java.lang.String, int, int)
	 */
	public List listRailwayEvaluations(String postIds, String orgIds, int year, int month) throws ServiceException {
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
		RailwayEvaluationParameter railwayEvaluationParameter = loadRailwayEvaluationParameter();
		Map departmentWeights = new HashMap();
		Map examRankingLists = new HashMap();
		List railwayEvaluations = new ArrayList();
		for(Iterator iterator = persons.iterator(); iterator.hasNext();) {
			Person person = (Person)iterator.next();
			railwayEvaluations.add(retrieveRailwayEvaluation(railwayEvaluationParameter, person.getId(), person.getName(), year, month, departmentWeights, examRankingLists));
		}
		//排序
		Collections.sort(railwayEvaluations, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				RailwayEvaluation evaluation0 = (RailwayEvaluation)arg0;
				RailwayEvaluation evaluation1 = (RailwayEvaluation)arg1;
				double score0 = (evaluation0.getScore()==null ? -1 : Double.parseDouble(evaluation0.getScore()));
				double score1 = (evaluation1.getScore()==null ? -1 : Double.parseDouble(evaluation1.getScore()));
				return (score0==score1 ? 0 : (score0>score1 ? -1 : 1));
			}
		});
		return railwayEvaluations;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.railway.evaluation.service.RailwayEvaluationService#getPersonalRailwayEvaluation(long, int, int)
	 */
	public RailwayEvaluation getPersonalRailwayEvaluation(long personId, int year, int month) throws ServiceException {
		RailwayEvaluationParameter railwayEvaluationParameter = loadRailwayEvaluationParameter();
		Map departmentWeights = new HashMap();
		Map examRankingLists = new HashMap();
		return retrieveRailwayEvaluation(railwayEvaluationParameter, personId, personService.getPersonName(personId), year, month, departmentWeights, examRankingLists);
	}
	
	/**
	 * 获取单个用户的综合评价
	 * @param railwayEvaluationParameter
	 * @param personId
	 * @param personName
	 * @param year
	 * @param month
	 * @param departmentWeights
	 * @param examRankingLists
	 * @return
	 * @throws ServiceException
	 */
	private RailwayEvaluation retrieveRailwayEvaluation(RailwayEvaluationParameter railwayEvaluationParameter, long personId, String personName, int year, int month, Map departmentWeights, Map examRankingLists) throws ServiceException {
		RailwayEvaluation railwayEvaluation = new RailwayEvaluation();
		railwayEvaluation.setPersonId(personId); //用户ID
		railwayEvaluation.setPersonName(personName); //用户名
		railwayEvaluation.setYear(year); //年度
		railwayEvaluation.setMonth(month); //月份
		
		//获取部门
		Org department = (Org)orgService.listOrgsOfPerson("" + personId, false).get(0);
		railwayEvaluation.setDepartmentId(department.getId()); //部门ID
		railwayEvaluation.setDepartmentName(department.getDirectoryName()); //部门名称
		
		//获取岗位
		List posts = roleService.listRolesOfPerson("" + personId, true);
		if(posts!=null && !posts.isEmpty()) {
			Role post = (Role)posts.get(0);
			railwayEvaluation.setPostId(post.getId()); //岗位ID
			railwayEvaluation.setPostName(post.getRoleName()); //岗位名称
		}
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		//获取工作量
		WorkloadAssessResult workloadAssessResult = workloadService.getWorkloadAssessResult(personId, year, month);
		if(workloadAssessResult!=null) { //有考核
			railwayEvaluation.setWorkload(decimalFormat.format(workloadAssessResult.getWorkload()));
		}
		
		//获取部门权重
		Double departmentWeight = (Double)departmentWeights.get(railwayEvaluation.getDepartmentId() + "/" + year + "/" + month);
		if(departmentWeight==null) {
			double departmentEvaluationResult = departmentEvaluationService.getDepartmentEvaluationResult(railwayEvaluation.getDepartmentId(), year, month);
			departmentWeight = new Double(departmentEvaluationResult);
			departmentWeights.put(railwayEvaluation.getDepartmentId() + "/" + year + "/" + month, departmentWeight);
		}
		railwayEvaluation.setDepartmentWeight(departmentWeight==null || departmentWeight.doubleValue()<0 ? null : decimalFormat.format(departmentWeight.doubleValue()));
		
		//获取互评成绩
		MutualEvaluationTotal mutualEvaluationTotal = mutualEvaluationService.getMutualEvaluationTotal(personId, year, month);
		railwayEvaluation.setMutualEvaluationVoteNumber(mutualEvaluationTotal.getVoteNumber()); //互评投票数
		railwayEvaluation.setMutualEvaluationHighNumber(mutualEvaluationTotal.getHighNumber()); //互评靠前数
		railwayEvaluation.setMutualEvaluationLowNumber(mutualEvaluationTotal.getLowNumber()); //互评靠后数
		//计算互评得分
		if(railwayEvaluation.getMutualEvaluationVoteNumber()>0) {
			double score = (railwayEvaluation.getMutualEvaluationHighNumber() - railwayEvaluation.getMutualEvaluationLowNumber() + 0.0d)/railwayEvaluation.getMutualEvaluationVoteNumber();
			double reward = 0;
			if(score>0) { //靠前
				reward = railwayEvaluationParameter.getMutualEvaluationRaise() * score;
			}
			else if(score<0) { //靠后
				reward = railwayEvaluationParameter.getMutualEvaluationDeduct() * score;
			}
			railwayEvaluation.setMutualEvaluationReward(Math.round(reward*100)/100.0);
		}
		
		//获取考试成绩
		if(railwayEvaluation.getPostId()>0) {
			ExamRankingList examRankingList = (ExamRankingList)examRankingLists.get(railwayEvaluation.getPostId() + "/" + year + "/" + month);
			if(examRankingList==null) {
				examRankingList = examService.getRankingList(railwayEvaluation.getPostId(), year, month);
				examRankingLists.put(railwayEvaluation.getPostId() + "/" + year + "/" + month, examRankingList);
			}
			railwayEvaluation.setMonthTestQuestionNumber(examRankingList.getMonthTestQuestionNumber()); //每月需要完成的题量
			railwayEvaluation.setTestMaxScore(examRankingList.getTestMaxScore()); //考试最高分
			railwayEvaluation.setTestMinScore(examRankingList.getTestMinScore()); //考试最低分
			railwayEvaluation.setTestAverageScore(examRankingList.getTestAverageScore()); //考试平均分
			ExamTranscript examTranscript = (ExamTranscript)ListUtils.findObjectByProperty(examRankingList.getTranscripts(), "personId", new Long(personId));
			if(examTranscript!=null) {
				railwayEvaluation.setTestedQuestionNumber(examTranscript.getTestedQuestionNumber()); //实际完成的题量
				railwayEvaluation.setTestScore(examTranscript.getTestScore()); //考试总分
				railwayEvaluation.setTestPosition(examTranscript.getTestPosition()); //考试名次
			}
			//计算答题量扣分
			double testReward = 0;
			if(railwayEvaluation.getTestedQuestionNumber()<railwayEvaluation.getMonthTestQuestionNumber()) { //答题量少于每月需要完成的题量
				testReward = railwayEvaluationParameter.getTestLackDeduct() * (railwayEvaluation.getTestedQuestionNumber() - railwayEvaluation.getMonthTestQuestionNumber()) / railwayEvaluation.getMonthTestQuestionNumber();
			}
			//计算考试得分,以平均成绩为基准
			if(railwayEvaluation.getTestScore()>railwayEvaluation.getTestAverageScore()) { //靠前
				testReward += railwayEvaluationParameter.getTestRaise() * (railwayEvaluation.getTestScore()-railwayEvaluation.getTestAverageScore()) / (railwayEvaluation.getTestMaxScore() - railwayEvaluation.getTestAverageScore());
			}
			else if(railwayEvaluation.getTestScore()<railwayEvaluation.getTestAverageScore()) { //靠后
				testReward += railwayEvaluationParameter.getTestDeduct() * (railwayEvaluation.getTestScore()-railwayEvaluation.getTestAverageScore()) / (railwayEvaluation.getTestAverageScore() - railwayEvaluation.getTestMinScore());
			}
			railwayEvaluation.setTestReward(Math.round(testReward*100)/100.0);
		}
		
		//获取铁路局问题记录
		int[] eventCounts = railwayEventService.getEventCounts(personId, year, month);
		railwayEvaluation.setEventLevelANumber(eventCounts[0]); //铁路局问题A个数
		railwayEvaluation.setEventLevelBNumber(eventCounts[1]); //铁路局问题B个数
		railwayEvaluation.setEventLevelCNumber(eventCounts[2]); //铁路局问题C个数
		railwayEvaluation.setEventLevelDNumber(eventCounts[3]); //铁路局问题D个数
		//计算问题扣分
		double eventPunish = 0;
		if(eventCounts[0]>0) {
			eventPunish -= eventCounts[0] * railwayEvaluationParameter.getEventLevelADeduct();
		}
		if(eventCounts[1]>0) {
			eventPunish -= eventCounts[1] * railwayEvaluationParameter.getEventLevelBDeduct();
		}
		if(eventCounts[2]>0) {
			eventPunish -= eventCounts[2] * railwayEvaluationParameter.getEventLevelCDeduct();
		}
		if(eventCounts[3]>0) {
			eventPunish -= eventCounts[3] * railwayEvaluationParameter.getEventLevelDDeduct();
		}
		railwayEvaluation.setEventPunish(Math.round(Math.min(railwayEvaluationParameter.getEventDeductLimit(), eventPunish) *100) / 100.0); //铁路局问题处罚
		
		//计算综合评价成绩
		if(railwayEvaluation.getWorkload()!=null) {
			double score = Double.parseDouble(railwayEvaluation.getWorkload()) * //工作量
						   (railwayEvaluation.getDepartmentWeight()==null ? 1 : Double.parseDouble(railwayEvaluation.getDepartmentWeight())) + //权重
						   railwayEvaluation.getMutualEvaluationReward() + //互评得分
						   railwayEvaluation.getTestReward() + //考试得分
						   railwayEvaluation.getEventPunish(); //问题扣分
			railwayEvaluation.setScore(decimalFormat.format(Math.round(score*100)/100.0));
		}
		return railwayEvaluation;
	}

	/**
	 * @return the departmentEvaluationService
	 */
	public DepartmentEvaluationService getDepartmentEvaluationService() {
		return departmentEvaluationService;
	}

	/**
	 * @param departmentEvaluationService the departmentEvaluationService to set
	 */
	public void setDepartmentEvaluationService(
			DepartmentEvaluationService departmentEvaluationService) {
		this.departmentEvaluationService = departmentEvaluationService;
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
	 * @return the mutualEvaluationService
	 */
	public MutualEvaluationService getMutualEvaluationService() {
		return mutualEvaluationService;
	}

	/**
	 * @param mutualEvaluationService the mutualEvaluationService to set
	 */
	public void setMutualEvaluationService(
			MutualEvaluationService mutualEvaluationService) {
		this.mutualEvaluationService = mutualEvaluationService;
	}

	/**
	 * @return the railwayEventService
	 */
	public RailwayEventService getRailwayEventService() {
		return railwayEventService;
	}

	/**
	 * @param railwayEventService the railwayEventService to set
	 */
	public void setRailwayEventService(RailwayEventService railwayEventService) {
		this.railwayEventService = railwayEventService;
	}

	/**
	 * @return the workloadService
	 */
	public WorkloadService getWorkloadService() {
		return workloadService;
	}

	/**
	 * @param workloadService the workloadService to set
	 */
	public void setWorkloadService(WorkloadService workloadService) {
		this.workloadService = workloadService;
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
	 * @return the personService
	 */
	public PersonService getPersonService() {
		return personService;
	}

	/**
	 * @param personService the personService to set
	 */
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	/**
	 * @return the evaluateCurrentMonth
	 */
	public boolean isEvaluateCurrentMonth() {
		return evaluateCurrentMonth;
	}

	/**
	 * @param evaluateCurrentMonth the evaluateCurrentMonth to set
	 */
	public void setEvaluateCurrentMonth(boolean evaluateCurrentMonth) {
		this.evaluateCurrentMonth = evaluateCurrentMonth;
	}
}