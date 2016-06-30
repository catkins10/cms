/*
 * Created on 2006-4-17
 *
 */
package com.yuanluesoft.workflow.client.model.runtime;

import java.util.List;

import com.yuanluesoft.workflow.client.model.resource.Application;


/**
 *
 * @author LinChuan
 * 类型为"决策"的流程出口
 *
 */
public class DecisionExit extends BaseExit {
	private Application decisionApplication;
	private List applicationReturnList; //过程执行结果
	
	/**
	 * @return Returns the decisionApplication.
	 */
	public Application getDecisionApplication() {
		return decisionApplication;
	}
	/**
	 * @param decisionApplication The decisionApplication to set.
	 */
	public void setDecisionApplication(Application decisionApplication) {
		this.decisionApplication = decisionApplication;
	}
	/**
	 * @return Returns the applicationReturnList.
	 */
	public List getApplicationReturnList() {
		return applicationReturnList;
	}
	/**
	 * @param applicationReturnList The applicationReturnList to set.
	 */
	public void setApplicationReturnList(List applicationReturnList) {
		this.applicationReturnList = applicationReturnList;
	}
}
