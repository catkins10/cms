package com.yuanluesoft.jeaf.formula.service.spring;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.formula.service.ConditionCheckCallback;
import com.yuanluesoft.jeaf.formula.service.FormulaService;
import com.yuanluesoft.jeaf.formula.service.FormulaSupport;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author linchuan
 *
 */
public class FormulaServiceImpl implements FormulaService {
	private Map formulaMap; //公式和服务的映射表

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.formula.service.FormulaSupport#executeFormula(java.lang.String, java.lang.String[], java.lang.String, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Object executeFormula(String formulaName, String[] parameters, String applicationName, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws ServiceException {
		String serviceName = (String)formulaMap.get(formulaName); //按公式名称获取公式服务
		if(serviceName==null) {
			return null;
		}
		FormulaSupport formulaSupport = (FormulaSupport)Environment.getService(serviceName);
		if(formulaSupport==null) {
			return null;
		}
		return formulaSupport.executeFormula(formulaName, parameters, applicationName, request, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.formula.service.FormulaService#checkCondition(java.lang.String, com.yuanluesoft.jeaf.formula.service.ConditionCheckCallback, java.lang.String, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public boolean checkCondition(String condition, ConditionCheckCallback callback, String applicationName, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws ServiceException {
		if(condition==null || condition.equals("")) {
			return false;
		}
		String[] orConditions = condition.split(" or "); //and 的优先级高于 or
		for(int i=0; i<orConditions.length; i++) {
			String[] andConditions=orConditions[i].split(" and ");
			boolean result = true;
			for(int j=0; j<andConditions.length; j++) {
				andConditions[j] = andConditions[j].trim();
				int index = andConditions[j].indexOf('(');
				String formulaName = andConditions[j].substring(0, index); //公式名称
				String parameter = andConditions[j].substring(index + 1, andConditions[j].length() - 1);
				String[] parameters = parameter.equals("") ? null : parameter.split(","); //参数
				Object formulaResult = executeFormula(formulaName, parameters, applicationName, request, acl, sessionInfo); //运行公式
				if(formulaResult!=null) {
					result = ((Boolean)formulaResult).booleanValue();
				}
				else if(callback!=null) {
					result = callback.check(formulaName, parameters);
				}
				if(!result) {
					break;
				}
			}
			if(result) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the formulaMap
	 */
	public Map getFormulaMap() {
		return formulaMap;
	}

	/**
	 * @param formulaMap the formulaMap to set
	 */
	public void setFormulaMap(Map formulaMap) {
		this.formulaMap = formulaMap;
	}
}