package com.yuanluesoft.enterprise.workload.service.spring;

import java.sql.Date;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.enterprise.workload.pojo.WorkloadAssess;
import com.yuanluesoft.enterprise.workload.pojo.WorkloadAssessResult;
import com.yuanluesoft.enterprise.workload.service.WorkloadService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class WorkloadServiceImpl extends BusinessServiceImpl implements WorkloadService {
	private boolean asessCurrentMonth; //是否考核本月工作量
	private PersonService personService; //用户服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.workload.service.WorkloadService#createAssess(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public WorkloadAssess createAssess(SessionInfo sessionInfo) throws ServiceException {
		List toAssessPersons = listToAssessPersons(sessionInfo);
		if(toAssessPersons==null || toAssessPersons.isEmpty()) {
			return null;
		}
		Date date = getAssessMonth();
		WorkloadAssess workloadAssess = new WorkloadAssess();
		workloadAssess.setId(UUIDLongGenerator.generateId()); //ID
		workloadAssess.setAssessYear(DateTimeUtils.getYear(date)); //年度
		workloadAssess.setAssessMonth(DateTimeUtils.getMonth(date)+1); //月份
		workloadAssess.setCreatorId(sessionInfo.getUserId()); //考核人ID
		workloadAssess.setCreator(sessionInfo.getUserName()); //考核人
		workloadAssess.setCreated(DateTimeUtils.now()); //考核时间
		workloadAssess.setResults(new LinkedHashSet()); //成绩列表
		for(Iterator iterator = toAssessPersons.iterator(); iterator.hasNext();) {
			Person person = (Person)iterator.next();
			WorkloadAssessResult workloadAssessResult = new WorkloadAssessResult();
			workloadAssessResult.setId(UUIDLongGenerator.generateId()); //ID
			workloadAssessResult.setAssessId(workloadAssess.getId()); //考核ID
			workloadAssessResult.setPersonId(person.getId()); //被考核人ID
			workloadAssessResult.setPersonName(person.getName()); //被考核人
			workloadAssessResult.setWorkload(0); //工作量
			workloadAssessResult.setRemark(null); //考核说明
			workloadAssess.getResults().add(workloadAssessResult);
		}
		return workloadAssess;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.workload.service.WorkloadService#listToAssessPersons(com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listToAssessPersons(SessionInfo sessionInfo) throws ServiceException {
		if(sessionInfo==null || SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) {
			return null;
		}
		//获取被分管的用户
		List persons = personService.listSupervisePersons(sessionInfo.getUserId());
		if(persons==null || persons.isEmpty()) {
			return null;
		}
		//获取已经考核过的用户
		Date date = getAssessMonth();
		String hql = "select WorkloadAssessResult.personId" +
					 " from WorkloadAssess WorkloadAssess left join WorkloadAssess.results WorkloadAssessResult" +
					 " where WorkloadAssess.assessYear=" + DateTimeUtils.getYear(date) +
					 " and WorkloadAssess.assessMonth=" + (DateTimeUtils.getMonth(date)+1) +
					 " and WorkloadAssessResult.personId in (" + ListUtils.join(persons, "id", ",", false)  + ")";
		List assessPersonIds = getDatabaseService().findRecordsByHql(hql);
		if(assessPersonIds==null || assessPersonIds.isEmpty()) {
			return persons;
		}
		for(Iterator iterator = persons.iterator(); iterator.hasNext();) {
			Person person = (Person)iterator.next();
			if(assessPersonIds.indexOf(new Long(person.getId()))!=-1) {
				iterator.remove();
			}
		}
		return persons.isEmpty() ? null : persons;
	}
	
	/**
	 * 获取考核月份
	 * @return
	 */
	private Date getAssessMonth() {
		Date date = DateTimeUtils.date();
		if(!asessCurrentMonth) { //不是考核当月
			date = DateTimeUtils.add(date, Calendar.MONTH, -1); //考核前一个月
		}
		return date;
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.workload.service.WorkloadService#submitAssess(long, boolean, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public WorkloadAssess submitAssess(long assessId, boolean isNew, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		WorkloadAssess workloadAssess;
		if(isNew) {
			workloadAssess = createAssess(sessionInfo);
			if(workloadAssess==null) {
				return null;
			}
			getDatabaseService().saveRecord(workloadAssess);
		}
		else {
			workloadAssess = (WorkloadAssess)load(WorkloadAssess.class, assessId);
			workloadAssess.setCreated(DateTimeUtils.now()); //考核时间
			getDatabaseService().updateRecord(workloadAssess);
		}
		String[] personIds = request.getParameterValues("personId");
		String[] workloads = request.getParameterValues("workload");
		String[] remarks = request.getParameterValues("remark");
		for(Iterator iterator = workloadAssess.getResults()==null ? null : workloadAssess.getResults().iterator(); iterator!=null && iterator.hasNext();) {
			WorkloadAssessResult result = (WorkloadAssessResult)iterator.next();
			for(int i=0; i<personIds.length; i++) {
				if(Long.parseLong(personIds[i])!=result.getPersonId()) {
					continue;
				}
				result.setWorkload(Double.parseDouble(workloads[i]));
				result.setRemark(remarks[i]);
				if(isNew) {
					getDatabaseService().saveRecord(result);
				}
				else {
					getDatabaseService().updateRecord(result);
				}
				break;
			}
		}
		return workloadAssess;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.workload.service.WorkloadService#getWorkloadAssessResult(long, int, int)
	 */
	public WorkloadAssessResult getWorkloadAssessResult(long personId, int year, int month) throws ServiceException {
		String hql = "select WorkloadAssessResult" +
					 " from WorkloadAssessResult WorkloadAssessResult left join WorkloadAssessResult.assess WorkloadAssess" +
					 " where WorkloadAssessResult.personId=" + personId +
					 " and WorkloadAssess.assessYear=" + year +
					 " and WorkloadAssess.assessMonth=" + month;
		return (WorkloadAssessResult)getDatabaseService().findRecordByHql(hql);
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
	 * @return the asessCurrentMonth
	 */
	public boolean isAsessCurrentMonth() {
		return asessCurrentMonth;
	}

	/**
	 * @param asessCurrentMonth the asessCurrentMonth to set
	 */
	public void setAsessCurrentMonth(boolean asessCurrentMonth) {
		this.asessCurrentMonth = asessCurrentMonth;
	}
}