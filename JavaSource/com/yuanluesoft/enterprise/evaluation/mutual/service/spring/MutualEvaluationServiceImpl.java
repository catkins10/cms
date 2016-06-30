package com.yuanluesoft.enterprise.evaluation.mutual.service.spring;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.enterprise.evaluation.mutual.model.MutualEvaluationTotal;
import com.yuanluesoft.enterprise.evaluation.mutual.pojo.MutualEvaluation;
import com.yuanluesoft.enterprise.evaluation.mutual.pojo.MutualEvaluationParameter;
import com.yuanluesoft.enterprise.evaluation.mutual.pojo.MutualEvaluationResult;
import com.yuanluesoft.enterprise.evaluation.mutual.service.MutualEvaluationService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.usermanage.service.RoleService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class MutualEvaluationServiceImpl extends BusinessServiceImpl implements MutualEvaluationService {
	private boolean evaluateCurrentMonth = false; //是否互评本月
	private PersonService personService; //用户服务
	private OrgService orgService; //组织机构服务
	private RoleService roleService; //角色服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.evaluation.mutual.service.MutualEvaluationService#loadMutualEvaluationParameter()
	 */
	public MutualEvaluationParameter loadMutualEvaluationParameter() throws ServiceException {
		return (MutualEvaluationParameter)getDatabaseService().findRecordByHql("from MutualEvaluationParameter MutualEvaluationParameter");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.evaluation.mutual.service.MutualEvaluationService#createMutualEvaluation(com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public MutualEvaluation createMutualEvaluation(SessionInfo sessionInfo) throws ServiceException {
		//获取互评配置
		MutualEvaluationParameter evaluationParameter = loadMutualEvaluationParameter();
		if(evaluationParameter==null) {
			return null;
		}
		List toEvaluatePersons = listToEvaluatePersons(sessionInfo, evaluationParameter);
		if(toEvaluatePersons==null || toEvaluatePersons.isEmpty()) {
			return null;
		}
		Date date = getEvaluateMonth();
		MutualEvaluation mutualEvaluation = new MutualEvaluation();
		mutualEvaluation.setId(UUIDLongGenerator.generateId()); //ID
		mutualEvaluation.setEvaluationYear(DateTimeUtils.getYear(date)); //年度
		mutualEvaluation.setEvaluationMonth(DateTimeUtils.getMonth(date)+1); //月份
		mutualEvaluation.setOrgId(sessionInfo.getDepartmentId()); //部门ID
		mutualEvaluation.setOrgName(sessionInfo.getDepartmentName()); //部门名称
		mutualEvaluation.setVoteNumber(Math.min(toEvaluatePersons.size()/2, Math.max(1, (int)Math.round(evaluationParameter.getRatio()/100*toEvaluatePersons.size())))); //投票数
		mutualEvaluation.setCreatorId(sessionInfo.getUserId()); //互评人ID
		mutualEvaluation.setCreator(sessionInfo.getUserName()); //互评人
		mutualEvaluation.setCreated(DateTimeUtils.now()); //互评时间
		mutualEvaluation.setResults(new LinkedHashSet()); //结果列表
		for(Iterator iterator = toEvaluatePersons.iterator(); iterator.hasNext();) {
			Person person = (Person)iterator.next();
			MutualEvaluationResult mutualEvaluationResult = new MutualEvaluationResult();
			mutualEvaluationResult.setId(UUIDLongGenerator.generateId()); //ID
			mutualEvaluationResult.setEvaluationId(mutualEvaluation.getId()); //互评ID
			mutualEvaluationResult.setPersonId(person.getId()); //被考核人ID
			mutualEvaluationResult.setPersonName(person.getName()); //被考核人
			mutualEvaluationResult.setEvaluateLevel(1); //评价等级,0/靠后,1/居中,2/靠前
			mutualEvaluation.getResults().add(mutualEvaluationResult);
		}
		return mutualEvaluation;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.evaluation.mutual.service.MutualEvaluationService#listToEvaluatePersons(com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listToEvaluatePersons(SessionInfo sessionInfo) throws ServiceException {
		if(sessionInfo==null || SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) {
			return null;
		}
		//获取互评配置
		MutualEvaluationParameter evaluationParameter = loadMutualEvaluationParameter();
		if(evaluationParameter==null) {
			return null;
		}
		return listToEvaluatePersons(sessionInfo, evaluationParameter);
	}
	
	/**
	 * 获取需要互评的用户
	 * @param sessionInfo
	 * @param evaluationParameter
	 * @return
	 * @throws ServiceException
	 */
	public List listToEvaluatePersons(SessionInfo sessionInfo, MutualEvaluationParameter evaluationParameter) throws ServiceException {
		//获取部门用户
		List persons = orgService.listOrgPersons("" + sessionInfo.getDepartmentId(), null, false, false, 0, 0);
		int minPersonNumber = Math.max(1, evaluationParameter.getMinPersonNumber());
		if(persons==null || persons.size()<minPersonNumber) { //少于最低人数
			return null;
		}
		//部门领导不参与互评时,剔除部门领导
		List rejectPersons = new ArrayList();
		if(evaluationParameter.getLeaderEnabled()!=1) {
			List leaders = personService.listOrgLeaders(sessionInfo.getDepartmentId());
			if(leaders!=null && !leaders.isEmpty()) {
				rejectPersons.addAll(leaders);
			}
		}
		//剔除排除掉的角色
		if(evaluationParameter.getRejectPostIds()!=null && !evaluationParameter.getRejectPostIds().isEmpty()) {
			List roleMembers = roleService.listRoleMembersInOrgs(evaluationParameter.getRejectPostIds(), "" + sessionInfo.getDepartmentId(), false, false);
			if(roleMembers!=null && !roleMembers.isEmpty()) {
				rejectPersons.addAll(roleMembers);
			}
		}
		for(Iterator iterator = rejectPersons.iterator(); iterator.hasNext();) {
			Person person = (Person)iterator.next();
			ListUtils.removeObjectByProperty(persons, "id", new Long(person.getId()));
		}
		if(persons.size()<minPersonNumber) { //少于最低人数
			return null;
		}
		//获取已经互评过的用户
		Date date = getEvaluateMonth();
		String hql = "select MutualEvaluationResult.personId" +
					 " from MutualEvaluation MutualEvaluation left join MutualEvaluation.results MutualEvaluationResult" +
					 " where MutualEvaluation.evaluationYear=" + DateTimeUtils.getYear(date) +
					 " and MutualEvaluation.evaluationMonth=" + (DateTimeUtils.getMonth(date)+1) +
					 " and MutualEvaluation.creatorId=" + sessionInfo.getUserId() +
					 " and MutualEvaluation.orgId=" + sessionInfo.getDepartmentId();
		List evaluationPersonIds = getDatabaseService().findRecordsByHql(hql);
		if(evaluationPersonIds==null || evaluationPersonIds.isEmpty()) {
			return persons;
		}
		for(Iterator iterator = persons.iterator(); iterator.hasNext();) {
			Person person = (Person)iterator.next();
			if(evaluationPersonIds.indexOf(new Long(person.getId()))!=-1) {
				iterator.remove();
			}
		}
		return persons.isEmpty() ? null : persons;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.evaluation.mutual.service.MutualEvaluationService#submitMutualEvaluation(long, boolean, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public MutualEvaluation submitMutualEvaluation(long evaluationId, boolean isNew, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		MutualEvaluation mutualEvaluation;
		if(isNew) {
			mutualEvaluation = createMutualEvaluation(sessionInfo);
			if(mutualEvaluation==null) {
				return null;
			}
			getDatabaseService().saveRecord(mutualEvaluation);
		}
		else {
			mutualEvaluation = (MutualEvaluation)load(MutualEvaluation.class, evaluationId);
			mutualEvaluation.setCreated(DateTimeUtils.now()); //互评时间
			getDatabaseService().updateRecord(mutualEvaluation);
		}
		for(Iterator iterator = mutualEvaluation.getResults()==null ? null : mutualEvaluation.getResults().iterator(); iterator!=null && iterator.hasNext();) {
			MutualEvaluationResult result = (MutualEvaluationResult)iterator.next();
			String evaluateLevel = request.getParameter("evaluateLevel_" + result.getPersonId());
			result.setEvaluateLevel(evaluateLevel==null || evaluateLevel.isEmpty() ? 1 : Integer.parseInt(evaluateLevel));
			if(isNew) {
				getDatabaseService().saveRecord(result);
			}
			else {
				getDatabaseService().updateRecord(result);
			}
		}
		return mutualEvaluation;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.evaluation.mutual.service.MutualEvaluationService#getMutualEvaluationTotal(long, int, int)
	 */
	public MutualEvaluationTotal getMutualEvaluationTotal(long personId, int year, int month) throws ServiceException {
		String hql = "select MutualEvaluationResult" +
					 " from MutualEvaluationResult MutualEvaluationResult left join MutualEvaluationResult.evaluation MutualEvaluation" +
					 " where MutualEvaluation.evaluationYear=" + year +
					 " and MutualEvaluation.evaluationMonth=" + month +
					 " and MutualEvaluationResult.personId=" + personId;
		List results = getDatabaseService().findRecordsByHql(hql);
		MutualEvaluationTotal total = new MutualEvaluationTotal();
		if(results==null || results.isEmpty()) {
			return total;
		}
		total.setVoteNumber(results.size());
		for(Iterator iterator = results.iterator(); iterator.hasNext();) {
			MutualEvaluationResult result = (MutualEvaluationResult)iterator.next();
			if(result.getEvaluateLevel()==2) {
				total.setHighNumber(total.getHighNumber()+1);
			}
			else if(result.getEvaluateLevel()==0) {
				total.setLowNumber(total.getLowNumber()+1);
			}
		}
		return total;
	}

	/**
	 * 获取互评月份
	 * @return
	 */
	private Date getEvaluateMonth() {
		Date date = DateTimeUtils.date();
		if(!evaluateCurrentMonth) { //不是考核当月
			date = DateTimeUtils.add(date, Calendar.MONTH, -1); //考核前一个月
		}
		return date;
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
}