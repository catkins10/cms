/*
 * Created on 2005-1-17
 *
 */
package com.yuanluesoft.workflow.client.model.definition;

import java.util.Date;
import java.util.List;

import com.yuanluesoft.jeaf.base.model.Element;

/**
 * 
 * @author linchuan
 *
 */
public class WorkflowPackage extends Element {
	private String XPDLVersion; //XPDL版本
	private String vendor; //公司名称
	private Date createDate; //创建时间
	private String graphConformance;
	private String scriptType; //脚本类型
	
	private List enumerationList; //枚举定义列表
	private List applicationList; //过程、判断、外部程序定义列表
	private List dataFieldList; //字段列表
	private List workflowProcessList; //流程列表
	private List subFormList; //子表单列表
	private List actionList; //操作列表
	private List programmingParticipantList; //编程的办理人列表
	
	private WorkflowPackageExtend workflowPackageExtend;
	
	/**
	 * @return Returns the applicationList.
	 */
	public List getApplicationList() {
		return applicationList;
	}
	/**
	 * @param applicationList The applicationList to set.
	 */
	public void setApplicationList(List applicationList) {
		this.applicationList = applicationList;
	}
	/**
	 * @return Returns the createDate.
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate The createDate to set.
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return Returns the dataFieldList.
	 */
	public List getDataFieldList() {
		return dataFieldList;
	}
	/**
	 * @param dataFieldList The dataFieldList to set.
	 */
	public void setDataFieldList(List dataFieldList) {
		this.dataFieldList = dataFieldList;
	}
	/**
	 * @return Returns the enumerationList.
	 */
	public List getEnumerationList() {
		return enumerationList;
	}
	/**
	 * @param enumerationList The enumerationList to set.
	 */
	public void setEnumerationList(List enumerationList) {
		this.enumerationList = enumerationList;
	}
	/**
	 * @return Returns the scriptType.
	 */
	public String getScriptType() {
		return scriptType;
	}
	/**
	 * @param scriptType The scriptType to set.
	 */
	public void setScriptType(String scriptType) {
		this.scriptType = scriptType;
	}
	/**
	 * @return Returns the vendor.
	 */
	public String getVendor() {
		return vendor;
	}
	/**
	 * @param vendor The vendor to set.
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	/**
	 * @return Returns the workflowProcessList.
	 */
	public List getWorkflowProcessList() {
		return workflowProcessList;
	}
	/**
	 * @param workflowProcessList The workflowProcessList to set.
	 */
	public void setWorkflowProcessList(List workflowProcessList) {
		this.workflowProcessList = workflowProcessList;
	}
	/**
	 * @return Returns the xPDLVersion.
	 */
	public String getXPDLVersion() {
		return XPDLVersion;
	}
	/**
	 * @param version The xPDLVersion to set.
	 */
	public void setXPDLVersion(String version) {
		XPDLVersion = version;
	}
	
	/**
	 * @return Returns the graphConformance.
	 */
	public String getGraphConformance() {
		return graphConformance;
	}
	/**
	 * @param graphConformance The graphConformance to set.
	 */
	public void setGraphConformance(String graphConformance) {
		this.graphConformance = graphConformance;
	}
	
	/**
	 * @return Returns the actionList.
	 */
	public List getActionList() {
		return actionList;
	}
	/**
	 * @param actionList The actionList to set.
	 */
	public void setActionList(List actionList) {
		this.actionList = actionList;
	}
	/**
	 * @return Returns the subFormList.
	 */
	public List getSubFormList() {
		return subFormList;
	}
	/**
	 * @param subFormList The subFormList to set.
	 */
	public void setSubFormList(List subFormList) {
		this.subFormList = subFormList;
	}
    /**
     * @return Returns the workflowPackageExtend.
     */
    public WorkflowPackageExtend getWorkflowPackageExtend() {
        return workflowPackageExtend;
    }
    /**
     * @param workflowPackageExtend The workflowPackageExtend to set.
     */
    public void setWorkflowPackageExtend(
            WorkflowPackageExtend workflowPackageExtend) {
        this.workflowPackageExtend = workflowPackageExtend;
    }
	/**
	 * @return the programmingParticipantList
	 */
	public List getProgrammingParticipantList() {
		return programmingParticipantList;
	}
	/**
	 * @param programmingParticipantList the programmingParticipantList to set
	 */
	public void setProgrammingParticipantList(List programmingParticipantList) {
		this.programmingParticipantList = programmingParticipantList;
	}
}
