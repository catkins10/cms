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
 * 类型为"执行过程"的流程出口
 *
 */
public class ProcedureExit extends BaseExit {
	private Application procedureApplication; //过程
	private List applicationReturnList; //过程执行结果
	
	/**
	 * @return Returns the procedureApplication.
	 */
	public Application getProcedureApplication() {
		return procedureApplication;
	}
	/**
	 * @param procedureApplication The procedureApplication to set.
	 */
	public void setProcedureApplication(Application procedureApplication) {
		this.procedureApplication = procedureApplication;
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
