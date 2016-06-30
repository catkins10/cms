package com.yuanluesoft.enterprise.iso.forms;

import com.yuanluesoft.jeaf.directorymanage.forms.DirectoryForm;

/**
 * 
 * @author linchuan
 *
 */
public class Directory extends DirectoryForm {
	private String category; //文件类别
	private String version; //版本号
	private String control; //受控状态
	private String urgency; //紧急程度
	private String security; //文件密级
	private String storage; //保存期限
	private String storageDepartment; //管理部门
	private String numberingRule; //编号规则
	private String createWorkflowId; //登记流程ID
	private String createWorkflowName; //登记流程名称
	private String modifyWorkflowId; //修改流程ID
	private String modifyWorkflowName; //修改流程名称
	private String loanWorkflowId; //借阅流程ID
	private String loanWorkflowName; //借阅流程名称
	private String destroyWorkflowId; //销毁流程ID
	private String destroyWorkflowName; //销毁流程名称
	
	private String workflowType; //流程配置时有效
	
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the control
	 */
	public String getControl() {
		return control;
	}
	/**
	 * @param control the control to set
	 */
	public void setControl(String control) {
		this.control = control;
	}
	/**
	 * @return the createWorkflowId
	 */
	public String getCreateWorkflowId() {
		return createWorkflowId;
	}
	/**
	 * @param createWorkflowId the createWorkflowId to set
	 */
	public void setCreateWorkflowId(String createWorkflowId) {
		this.createWorkflowId = createWorkflowId;
	}
	/**
	 * @return the createWorkflowName
	 */
	public String getCreateWorkflowName() {
		return createWorkflowName;
	}
	/**
	 * @param createWorkflowName the createWorkflowName to set
	 */
	public void setCreateWorkflowName(String createWorkflowName) {
		this.createWorkflowName = createWorkflowName;
	}
	/**
	 * @return the destroyWorkflowId
	 */
	public String getDestroyWorkflowId() {
		return destroyWorkflowId;
	}
	/**
	 * @param destroyWorkflowId the destroyWorkflowId to set
	 */
	public void setDestroyWorkflowId(String destroyWorkflowId) {
		this.destroyWorkflowId = destroyWorkflowId;
	}
	/**
	 * @return the destroyWorkflowName
	 */
	public String getDestroyWorkflowName() {
		return destroyWorkflowName;
	}
	/**
	 * @param destroyWorkflowName the destroyWorkflowName to set
	 */
	public void setDestroyWorkflowName(String destroyWorkflowName) {
		this.destroyWorkflowName = destroyWorkflowName;
	}
	/**
	 * @return the loanWorkflowId
	 */
	public String getLoanWorkflowId() {
		return loanWorkflowId;
	}
	/**
	 * @param loanWorkflowId the loanWorkflowId to set
	 */
	public void setLoanWorkflowId(String loanWorkflowId) {
		this.loanWorkflowId = loanWorkflowId;
	}
	/**
	 * @return the loanWorkflowName
	 */
	public String getLoanWorkflowName() {
		return loanWorkflowName;
	}
	/**
	 * @param loanWorkflowName the loanWorkflowName to set
	 */
	public void setLoanWorkflowName(String loanWorkflowName) {
		this.loanWorkflowName = loanWorkflowName;
	}
	/**
	 * @return the modifyWorkflowId
	 */
	public String getModifyWorkflowId() {
		return modifyWorkflowId;
	}
	/**
	 * @param modifyWorkflowId the modifyWorkflowId to set
	 */
	public void setModifyWorkflowId(String modifyWorkflowId) {
		this.modifyWorkflowId = modifyWorkflowId;
	}
	/**
	 * @return the modifyWorkflowName
	 */
	public String getModifyWorkflowName() {
		return modifyWorkflowName;
	}
	/**
	 * @param modifyWorkflowName the modifyWorkflowName to set
	 */
	public void setModifyWorkflowName(String modifyWorkflowName) {
		this.modifyWorkflowName = modifyWorkflowName;
	}
	/**
	 * @return the numberingRule
	 */
	public String getNumberingRule() {
		return numberingRule;
	}
	/**
	 * @param numberingRule the numberingRule to set
	 */
	public void setNumberingRule(String numberingRule) {
		this.numberingRule = numberingRule;
	}
	/**
	 * @return the security
	 */
	public String getSecurity() {
		return security;
	}
	/**
	 * @param security the security to set
	 */
	public void setSecurity(String security) {
		this.security = security;
	}
	/**
	 * @return the storage
	 */
	public String getStorage() {
		return storage;
	}
	/**
	 * @param storage the storage to set
	 */
	public void setStorage(String storage) {
		this.storage = storage;
	}
	/**
	 * @return the storageDepartment
	 */
	public String getStorageDepartment() {
		return storageDepartment;
	}
	/**
	 * @param storageDepartment the storageDepartment to set
	 */
	public void setStorageDepartment(String storageDepartment) {
		this.storageDepartment = storageDepartment;
	}
	/**
	 * @return the urgency
	 */
	public String getUrgency() {
		return urgency;
	}
	/**
	 * @param urgency the urgency to set
	 */
	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @return the workflowType
	 */
	public String getWorkflowType() {
		return workflowType;
	}
	/**
	 * @param workflowType the workflowType to set
	 */
	public void setWorkflowType(String workflowType) {
		this.workflowType = workflowType;
	}
}