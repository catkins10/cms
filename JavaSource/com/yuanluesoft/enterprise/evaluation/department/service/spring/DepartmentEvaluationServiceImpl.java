package com.yuanluesoft.enterprise.evaluation.department.service.spring;

import java.sql.Date;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.enterprise.evaluation.department.pojo.DepartmentEvaluation;
import com.yuanluesoft.enterprise.evaluation.department.pojo.DepartmentEvaluationParameter;
import com.yuanluesoft.enterprise.evaluation.department.pojo.DepartmentEvaluationResult;
import com.yuanluesoft.enterprise.evaluation.department.service.DepartmentEvaluationService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class DepartmentEvaluationServiceImpl extends BusinessServiceImpl implements DepartmentEvaluationService {
	private boolean evaluateCurrentMonth = false; //是否互评本月
	private OrgService orgService; //组织机构服务
	private AccessControlService accessControlService; //访问控制服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.evaluation.department.service.DepartmentEvaluationService#loadDepartmentEvaluationParameter()
	 */
	public DepartmentEvaluationParameter loadDepartmentEvaluationParameter() throws ServiceException {
		return (DepartmentEvaluationParameter)getDatabaseService().findRecordByHql("from DepartmentEvaluationParameter DepartmentEvaluationParameter");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.evaluation.department.service.DepartmentEvaluationService#createDepartmentEvaluation(com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public DepartmentEvaluation createDepartmentEvaluation(SessionInfo sessionInfo) throws ServiceException {
		//获取互评配置
		DepartmentEvaluationParameter evaluationParameter = loadDepartmentEvaluationParameter();
		if(evaluationParameter==null) {
			return null;
		}
		List toEvaluatePersons = listToEvaluateDepartments(sessionInfo, evaluationParameter);
		if(toEvaluatePersons==null || toEvaluatePersons.isEmpty()) {
			return null;
		}
		Date date = getEvaluateMonth();
		DepartmentEvaluation departmentEvaluation = new DepartmentEvaluation();
		departmentEvaluation.setId(UUIDLongGenerator.generateId()); //ID
		departmentEvaluation.setEvaluationYear(DateTimeUtils.getYear(date)); //年度
		departmentEvaluation.setEvaluationMonth(DateTimeUtils.getMonth(date)+1); //月份
		departmentEvaluation.setCreatorId(sessionInfo.getUserId()); //互评人ID
		departmentEvaluation.setCreator(sessionInfo.getUserName()); //互评人
		departmentEvaluation.setCreated(DateTimeUtils.now()); //互评时间
		departmentEvaluation.setResults(new LinkedHashSet()); //结果列表
		for(Iterator iterator = toEvaluatePersons.iterator(); iterator.hasNext();) {
			Org department = (Org)iterator.next();
			DepartmentEvaluationResult departmentEvaluationResult = new DepartmentEvaluationResult();
			departmentEvaluationResult.setId(UUIDLongGenerator.generateId()); //ID
			departmentEvaluationResult.setEvaluationId(departmentEvaluation.getId()); //互评ID
			departmentEvaluationResult.setDepartmentId(department.getId()); //被考核部门ID
			departmentEvaluationResult.setDepartmentName(department.getDirectoryName()); //被考核部门
			departmentEvaluationResult.setResult(0d); //权重
			departmentEvaluation.getResults().add(departmentEvaluationResult);
		}
		return departmentEvaluation;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.evaluation.department.service.DepartmentEvaluationService#listToEvaluateDepartments(com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listToEvaluateDepartments(SessionInfo sessionInfo) throws ServiceException {
		if(sessionInfo==null || SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) {
			return null;
		}
		//获取部门考核配置
		DepartmentEvaluationParameter evaluationParameter = loadDepartmentEvaluationParameter();
		return listToEvaluateDepartments(sessionInfo, evaluationParameter);
	}
	
	/**
	 * 获取需要考核的部门
	 * @param sessionInfo
	 * @param evaluationParameter
	 * @return
	 * @throws ServiceException
	 */
	public List listToEvaluateDepartments(SessionInfo sessionInfo, DepartmentEvaluationParameter evaluationParameter) throws ServiceException {
		//检查用户的考核权限
		List acl = accessControlService.getAcl("enterprise/evaluation/department", sessionInfo);
		if(acl==null || !acl.contains("manageUnit_evaluate")) {
			return null;
		}
		//获取部门列表
		List departments = orgService.listChildDirectories(sessionInfo.getUnitId(), null, null, null, false, false, sessionInfo, 0, 0);
		if(departments==null || departments.isEmpty()) {
			return null;
		}
		//剔除不需要考核的部门
		if(evaluationParameter!=null && evaluationParameter.getDropoutDepartmentIds()!=null && !evaluationParameter.getDropoutDepartmentIds().isEmpty()) {
			String[] dropoutDepartmentIds = evaluationParameter.getDropoutDepartmentIds().split(",");
			for(int i=0; i<dropoutDepartmentIds.length; i++) {
				ListUtils.removeObjectByProperty(departments, "id", new Long(dropoutDepartmentIds[i]));
			}
			if(departments.isEmpty()) {
				return null;
			}
		}
		//获取已考核的部门
		Date date = getEvaluateMonth();
		String hql = "select DepartmentEvaluationResult.departmentId" +
					 " from DepartmentEvaluation DepartmentEvaluation left join DepartmentEvaluation.results DepartmentEvaluationResult" +
					 " where DepartmentEvaluation.evaluationYear=" + DateTimeUtils.getYear(date) +
					 " and DepartmentEvaluation.evaluationMonth=" + (DateTimeUtils.getMonth(date)+1) +
					 " and DepartmentEvaluation.creatorId=" + sessionInfo.getUserId() + 
					 " and DepartmentEvaluationResult.departmentId in (" + ListUtils.join(departments, "id", ",", false) + ")";
		List evaluationDepartmentIds = getDatabaseService().findRecordsByHql(hql);
		if(evaluationDepartmentIds==null || evaluationDepartmentIds.isEmpty()) {
			return departments;
		}
		for(Iterator iterator = departments.iterator(); iterator.hasNext();) {
			Org department = (Org)iterator.next();
			if(evaluationDepartmentIds.indexOf(new Long(department.getId()))!=-1) {
				iterator.remove();
			}
		}
		return departments.isEmpty() ? null : departments;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.evaluation.department.service.DepartmentEvaluationService#submitDepartmentEvaluation(long, boolean, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public DepartmentEvaluation submitDepartmentEvaluation(long evaluationId, boolean isNew, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		DepartmentEvaluation departmentEvaluation;
		if(isNew) {
			departmentEvaluation = createDepartmentEvaluation(sessionInfo);
			if(departmentEvaluation==null) {
				return null;
			}
			getDatabaseService().saveRecord(departmentEvaluation);
		}
		else {
			departmentEvaluation = (DepartmentEvaluation)load(DepartmentEvaluation.class, evaluationId);
			departmentEvaluation.setCreated(DateTimeUtils.now()); //互评时间
			getDatabaseService().updateRecord(departmentEvaluation);
		}
		String[] departmentIds = request.getParameterValues("departmentId");
		String[] results = request.getParameterValues("result");
		String[] remarks = request.getParameterValues("remark");
		if(results==null || results.length==0) {
			return departmentEvaluation;
		}
		for(Iterator iterator = departmentEvaluation.getResults()==null ? null : departmentEvaluation.getResults().iterator(); iterator!=null && iterator.hasNext();) {
			DepartmentEvaluationResult result = (DepartmentEvaluationResult)iterator.next();
			for(int i=0; i<departmentIds.length; i++) {
				if(Long.parseLong(departmentIds[i])!=result.getDepartmentId()) {
					continue;
				}
				result.setResult(Double.parseDouble(results[i]));
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
		return departmentEvaluation;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.evaluation.department.service.DepartmentEvaluationService#getDepartmentEvaluationResult(long, int, int)
	 */
	public double getDepartmentEvaluationResult(long orgId, int year, int month) throws ServiceException {
		//获取当前机构或者上级机构的评价记录
		String hql = "select DepartmentEvaluationResult" +
					 " from DepartmentEvaluationResult DepartmentEvaluationResult" +
					 " left join DepartmentEvaluationResult.evaluation DepartmentEvaluation, OrgSubjection OrgSubjection" +
					 " where DepartmentEvaluation.evaluationYear=" + year +
					 " and DepartmentEvaluation.evaluationMonth=" + month +
					 " and OrgSubjection.directoryId=" + orgId +
					 " and DepartmentEvaluationResult.departmentId=OrgSubjection.parentDirectoryId" +
					 " order by OrgSubjection.id";
		DepartmentEvaluationResult departmentEvaluationResult = (DepartmentEvaluationResult)getDatabaseService().findRecordByHql(hql);
		if(departmentEvaluationResult==null) {
			return -1;
		}
		//计算平均分
		hql = "select avg(DepartmentEvaluationResult.result)" +
		 	  " from DepartmentEvaluationResult DepartmentEvaluationResult" +
		 	  " left join DepartmentEvaluationResult.evaluation DepartmentEvaluation" +
		 	  " where DepartmentEvaluation.evaluationYear=" + year +
		 	  " and DepartmentEvaluation.evaluationMonth=" + month +
		 	  " and DepartmentEvaluationResult.departmentId=" + departmentEvaluationResult.getDepartmentId();
		Number result = (Number)getDatabaseService().findRecordByHql(hql);
		return result==null ? -1 : result.doubleValue();
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
	 * @return the accessControlService
	 */
	public AccessControlService getAccessControlService() {
		return accessControlService;
	}

	/**
	 * @param accessControlService the accessControlService to set
	 */
	public void setAccessControlService(AccessControlService accessControlService) {
		this.accessControlService = accessControlService;
	}
}