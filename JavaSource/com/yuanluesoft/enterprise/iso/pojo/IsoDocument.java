package com.yuanluesoft.enterprise.iso.pojo;

import java.sql.Date;
import java.util.Set;

import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * ISO:文档(iso_document)
 * @author linchuan
 *
 */
public class IsoDocument extends WorkflowData {
	private String category; //文件类别
	private String documentNumber; //文件编号
	private String name; //文件名称
	private String keywords; //关键词
	private double version; //版本号
	private String control; //受控状态
	private String urgency; //紧急程度
	private String security; //文件密级
	private String storage; //保存期限
	private String writer; //编制人
	private Date writeDate; //编制日期
	private String status; //文件状态
	private String storageDepartment; //管理部门
	private String manager; //管理员
	private String summary; //内容概述
	private char isValid = '0'; //文件是否生效,登记完成后置1
	private char isDestroy = '0'; //是否文件销毁记录
	private char isModify = '0'; //是否文件修改记录
	private long sourceDocumentId; //修改或销毁源文件ID
	private String modifyDescription; //修改说明,根据用户操作,自动做记录
	private String workflowInstanceId; //工作流实例ID
	private String remark; //备注
	
	private Set subjections; //隶属目录

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
	 * @return the documentNumber
	 */
	public String getDocumentNumber() {
		return documentNumber;
	}

	/**
	 * @param documentNumber the documentNumber to set
	 */
	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	/**
	 * @return the isValid
	 */
	public char getIsValid() {
		return isValid;
	}

	/**
	 * @param isValid the isValid to set
	 */
	public void setIsValid(char isValid) {
		this.isValid = isValid;
	}

	/**
	 * @return the keywords
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 * @return the manager
	 */
	public String getManager() {
		return manager;
	}

	/**
	 * @param manager the manager to set
	 */
	public void setManager(String manager) {
		this.manager = manager;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
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
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
	 * @return the subjections
	 */
	public Set getSubjections() {
		return subjections;
	}

	/**
	 * @param subjections the subjections to set
	 */
	public void setSubjections(Set subjections) {
		this.subjections = subjections;
	}

	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
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
	public double getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(double version) {
		this.version = version;
	}

	/**
	 * @return the workflowInstanceId
	 */
	public String getWorkflowInstanceId() {
		return workflowInstanceId;
	}

	/**
	 * @param workflowInstanceId the workflowInstanceId to set
	 */
	public void setWorkflowInstanceId(String workflowInstanceId) {
		this.workflowInstanceId = workflowInstanceId;
	}

	/**
	 * @return the writeDate
	 */
	public Date getWriteDate() {
		return writeDate;
	}

	/**
	 * @param writeDate the writeDate to set
	 */
	public void setWriteDate(Date writeDate) {
		this.writeDate = writeDate;
	}

	/**
	 * @return the writer
	 */
	public String getWriter() {
		return writer;
	}

	/**
	 * @param writer the writer to set
	 */
	public void setWriter(String writer) {
		this.writer = writer;
	}

	/**
	 * @return the isDestroy
	 */
	public char getIsDestroy() {
		return isDestroy;
	}

	/**
	 * @param isDestroy the isDestroy to set
	 */
	public void setIsDestroy(char isDestroy) {
		this.isDestroy = isDestroy;
	}

	/**
	 * @return the isModify
	 */
	public char getIsModify() {
		return isModify;
	}

	/**
	 * @param isModify the isModify to set
	 */
	public void setIsModify(char isModify) {
		this.isModify = isModify;
	}

	/**
	 * @return the modifyDescription
	 */
	public String getModifyDescription() {
		return modifyDescription;
	}

	/**
	 * @param modifyDescription the modifyDescription to set
	 */
	public void setModifyDescription(String modifyDescription) {
		this.modifyDescription = modifyDescription;
	}

	/**
	 * @return the sourceDocumentId
	 */
	public long getSourceDocumentId() {
		return sourceDocumentId;
	}

	/**
	 * @param sourceDocumentId the sourceDocumentId to set
	 */
	public void setSourceDocumentId(long sourceDocumentId) {
		this.sourceDocumentId = sourceDocumentId;
	}
}