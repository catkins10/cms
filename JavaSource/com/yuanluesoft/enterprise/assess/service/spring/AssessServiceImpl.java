package com.yuanluesoft.enterprise.assess.service.spring;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.enterprise.assess.pojo.Assess;
import com.yuanluesoft.enterprise.assess.pojo.AssessActivity;
import com.yuanluesoft.enterprise.assess.pojo.AssessClassify;
import com.yuanluesoft.enterprise.assess.pojo.AssessIndividualResult;
import com.yuanluesoft.enterprise.assess.pojo.AssessResult;
import com.yuanluesoft.enterprise.assess.pojo.AssessStandard;
import com.yuanluesoft.enterprise.assess.service.AssessService;
import com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectTeam;
import com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectTeamMember;
import com.yuanluesoft.enterprise.project.service.EnterpriseProjectService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class AssessServiceImpl extends BusinessServiceImpl implements AssessService {
	private PersonService personService; //用户服务
	private SessionService sessionService; //会话服务
	private EnterpriseProjectService enterpriseProjectService; //项目服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		return record;
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.assess.service.AssessService#createAssess(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Assess createAssess(long teamId, SessionInfo sessionInfo) throws ServiceException {
		Assess assess = new Assess();
		assess.setId(UUIDLongGenerator.generateId());
		assess.setTeamId(teamId); //项目组ID,非项目组考核时为0
		assess.setResults(new HashSet());
		assess.setAssessClassifies(new ArrayList());
		//检查是否需要自评
		AssessClassify assessClassify = getAssessClassify((teamId>0), sessionInfo);
		if(assessClassify!=null && assessClassify.getSelfAssess()=='1') {
			//创建自评
			assess.getResults().add(createPersonalAssess(assess.getId(), assessClassify, sessionInfo));
			assess.getAssessClassifies().add(assessClassify);
			return assess;
		}
		
		//获取被分管的用户列表
		List persons;
		if(teamId==0) {
			persons = personService.listSupervisePersons(sessionInfo.getUserId());
			if(persons==null || persons.isEmpty()) {
				return assess;
			}
		}
		else {
			EnterpriseProjectTeam team = enterpriseProjectService.getProjectTeam(teamId);
			if(team==null) {
				return assess;
			}
			persons = new ArrayList(team.getMembers());
		}
		for(Iterator iterator = persons.iterator(); iterator.hasNext();) {
			Object person = (Object)iterator.next();
			String loginName;
			if(person instanceof Person) {
				loginName = ((Person)person).getLoginName();
			}
			else {
				EnterpriseProjectTeamMember member = (EnterpriseProjectTeamMember)person;
				if(member.getIsManager()=='1') { //项目经理
					continue;
				}
				loginName = personService.getPersonLoginName(member.getMemberId());
			}
			try {
				sessionInfo = sessionService.getSessionInfo(loginName);
			} 
			catch (SessionException e) {
				continue;
			}
			assessClassify = getAssessClassify((teamId>0), sessionInfo);
			if(assessClassify==null || assessClassify.getSelfAssess()=='1') { //没有获取到考核类别,或者需要自评
				continue;
			}
			//加入到引用的考核类型列表
			if(ListUtils.findObjectByProperty(assess.getAssessClassifies(), "id", new Long(assessClassify.getId()))==null) {
				assess.getAssessClassifies().add(assessClassify);
			}
			assess.getResults().add(createPersonalAssess(assess.getId(), assessClassify, sessionInfo));
		}
		return assess;
	}
	
	/**
	 * 获取用户所属的考核类别
	 * @param isProjectTeam
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	private AssessClassify getAssessClassify(boolean isProjectTeam, SessionInfo sessionInfo) throws ServiceException {
		String hql = "select AssessClassify" +
					 " from AssessClassify AssessClassify, AssessClassifyPrivilege AssessClassifyPrivilege" +
					 " where AssessClassifyPrivilege.recordId=AssessClassify.id" +
					 (isProjectTeam ? "" : " and AssessClassify.projectTeamAccess!='1'") +
					 " and AssessClassifyPrivilege.visitorId in (" + sessionInfo.getUserIds() + ")" +
					 " order by " + (isProjectTeam ? "AssessClassify.projectTeamAccess DESC," : "") + "AssessClassify.priority DESC";
		return (AssessClassify)getDatabaseService().findRecordByHql(hql, ListUtils.generateList("standards,activities", ","));
	}
	
	/**
	 * 创建个人考核记录
	 * @param assessId
	 * @param assessClassify
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	private AssessResult createPersonalAssess(long assessId, AssessClassify assessClassify, SessionInfo sessionInfo) throws ServiceException {
		AssessResult assessResult = new AssessResult();
		assessResult.setId(UUIDLongGenerator.generateId()); //ID
		assessResult.setAssessId(assessId); //考核ID
		assessResult.setPersonId(sessionInfo.getUserId()); //被考核人ID
		assessResult.setPersonName(sessionInfo.getUserName()); //被考核人姓名
		assessResult.setClassifyId(assessClassify.getId()); //考核类型ID
		return assessResult;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.assess.service.AssessService#loadAssess(long)
	 */
	public Assess loadAssess(long assessId) throws ServiceException {
		//获取考核记录
		Assess assess = (Assess)load(Assess.class, assessId);
		if(assess==null) {
			return null;
		}
		//获取个人考核记录列表
		String hql = "from AssessResult AssessResult" +
					 " where AssessResult.assessId=" + assessId +
					 " order by AssessResult.id";
		List assessResults = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("individualResults", ","));
		assess.setResults(new LinkedHashSet(assessResults));
		//获取考核类别
		hql = "select distinct AssessClassify" +
			  " from AssessClassify AssessClassify" +
			  " where AssessClassify.id in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(assessResults, "classifyId", ",", false)) + ")";
		assess.setAssessClassifies(getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("standards,activities", ",")));
		return assess;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.assess.service.AssessService#saveAssess(long, long, java.lang.String, boolean, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void saveAssess(long assessId, long teamId, String workflowInstanceId, boolean isNewAssess, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		//创建或加载考核
		Assess assessModel = isNewAssess ? createAssess(teamId, sessionInfo) : loadAssess(assessId);
		//保存考核
		if(isNewAssess) {
			Assess assess = new Assess();
			assess.setId(assessId); //ID
			assess.setTeamId(teamId); //项目组ID,非项目组考核时为0
			if(teamId>0) {
				EnterpriseProjectTeam team = enterpriseProjectService.getProjectTeam(teamId);
				assess.setProjectName(team.getProjectName()); //项目名称
				assess.setProjectStage(team.getStage()); //项目阶段
			}
			assess.setCreatorId(sessionInfo.getUserId()); //创建人ID
			assess.setCreator(sessionInfo.getUserName()); //创建人
			assess.setCreated(DateTimeUtils.now()); //创建时间
			assess.setWorkflowInstanceId(workflowInstanceId); //工作流实例ID
			getDatabaseService().saveRecord(assess);
		}
		for(Iterator iterator = assessModel.getResults().iterator(); iterator.hasNext();) {
			AssessResult assessResult = (AssessResult)iterator.next();
			//获取考核类型
			AssessClassify assessClassify = (AssessClassify)ListUtils.findObjectByProperty(assessModel.getAssessClassifies(), "id", new Long(assessResult.getClassifyId()));
			//保存或更新单项分数
			float total = 0;
			boolean totalEnabled = true;
			for(Iterator iteratorStandard = assessClassify.getStandards().iterator(); iteratorStandard.hasNext();) {
				AssessStandard assessStandard = (AssessStandard)iteratorStandard.next();
				for(Iterator iteratorActivity = assessClassify.getActivities().iterator(); iteratorActivity.hasNext();) {
					AssessActivity assessActivity = (AssessActivity)iteratorActivity.next();
					//获取原来保存的数据
					AssessIndividualResult assessIndividualResult = (AssessIndividualResult)ListUtils.findObjectByProperty(ListUtils.getSubListByProperty(assessResult.getIndividualResults(), "contentId", new Long(assessStandard.getId())), "activityId", new Long(assessActivity.getId()));
					//获取考核分数
					String result = request.getParameter("result_" + assessResult.getPersonId() + "_" + assessStandard.getId() + "_" + assessActivity.getId());
					if(result!=null) { //有提交
						boolean isNew = (assessIndividualResult==null);
						if(isNew) {
							assessIndividualResult = new AssessIndividualResult();
							assessIndividualResult.setId(UUIDLongGenerator.generateId()); //ID
							assessIndividualResult.setResultId(assessResult.getId()); //个人考核ID
							assessIndividualResult.setContentId(assessStandard.getId()); //考核内容ID
							assessIndividualResult.setActivityId(assessActivity.getId()); //考核步骤ID
							assessIndividualResult.setResult(Float.parseFloat(result)); //考核成绩
							getDatabaseService().saveRecord(assessIndividualResult);
						}
						else {
							assessIndividualResult.setResult(Float.parseFloat(result)); //考核成绩
							getDatabaseService().updateRecord(assessIndividualResult);
						}
					}
					else if(assessIndividualResult==null) { //没有提交,且有还有考核步骤没有完成
						totalEnabled = false;
						continue;
					}
					//累计考核分数
					total += assessIndividualResult.getResult() * assessActivity.getWeight();
				}
			}
			//设置考核分数
			if(totalEnabled) {
				assessResult.setResult(Math.round(total * 100.0f)/100.0f);
				if(!isNewAssess) {
					getDatabaseService().updateRecord(assessResult);
				}
			}
			if(isNewAssess) { //新记录
				assessResult.setAssessId(assessId);
				getDatabaseService().saveRecord(assessResult);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.assess.service.AssessService#listProjectTeamAssesses(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, int, int)
	 */
	public List listProjectTeamAssesses(long teamId, SessionInfo sessionInfo, int offset, int limit) throws ServiceException {
		//获取考核记录
		return getDatabaseService().findPrivilegedRecords(Assess.class.getName(), null, null, "Assess.teamId=" + teamId, "Assess.created DESC", null, RecordControlService.ACCESS_LEVEL_READONLY, false, ListUtils.generateList("results", ","), offset, limit, sessionInfo);
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
	 * @return the sessionService
	 */
	public SessionService getSessionService() {
		return sessionService;
	}

	/**
	 * @param sessionService the sessionService to set
	 */
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	/**
	 * @return the enterpriseProjectService
	 */
	public EnterpriseProjectService getEnterpriseProjectService() {
		return enterpriseProjectService;
	}

	/**
	 * @param enterpriseProjectService the enterpriseProjectService to set
	 */
	public void setEnterpriseProjectService(
			EnterpriseProjectService enterpriseProjectService) {
		this.enterpriseProjectService = enterpriseProjectService;
	}
}