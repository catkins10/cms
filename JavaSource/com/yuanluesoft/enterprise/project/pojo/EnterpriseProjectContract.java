package com.yuanluesoft.enterprise.project.pojo;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 项目管理:合同(enterprise_project_contract)
 * @author linchuan
 *
 */
public class EnterpriseProjectContract extends Record {
	private long projectId; //项目ID
	private String contractNo; //合同编号,允许自动和手工编号
	private String contractUnit; //合同单位
	private String contractName; //合同名称
	private double contractValue; //合同金额（元）
	private Date signDate; //签订时间
	private Date archiveDate; //存档时间,指纸质合同是否已经在公司存档
	private Date sealDate; //盖章时间,该子项主要提醒经营部管理人员对在客户单位的合同进行盖章
	private String body; //HTML正文,从word导出
	private String creator; //起草人
	private Timestamp created; //创建时间
	private String remark; //备注
	
	private EnterpriseProject project; //项目
	private Set collects; //收款记录
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
	/**
	 * @return the contractName
	 */
	public String getContractName() {
		return contractName;
	}
	/**
	 * @param contractName the contractName to set
	 */
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	/**
	 * @return the contractNo
	 */
	public String getContractNo() {
		return contractNo;
	}
	/**
	 * @param contractNo the contractNo to set
	 */
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	/**
	 * @return the contractValue
	 */
	public double getContractValue() {
		return contractValue;
	}
	/**
	 * @param contractValue the contractValue to set
	 */
	public void setContractValue(double contractValue) {
		this.contractValue = contractValue;
	}
	/**
	 * @return the projectId
	 */
	public long getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(long projectId) {
		this.projectId = projectId;
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
	 * @return the archiveDate
	 */
	public Date getArchiveDate() {
		return archiveDate;
	}
	/**
	 * @param archiveDate the archiveDate to set
	 */
	public void setArchiveDate(Date archiveDate) {
		this.archiveDate = archiveDate;
	}
	/**
	 * @return the sealDate
	 */
	public Date getSealDate() {
		return sealDate;
	}
	/**
	 * @param sealDate the sealDate to set
	 */
	public void setSealDate(Date sealDate) {
		this.sealDate = sealDate;
	}
	/**
	 * @return the signDate
	 */
	public Date getSignDate() {
		return signDate;
	}
	/**
	 * @param signDate the signDate to set
	 */
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return the contractUnit
	 */
	public String getContractUnit() {
		return contractUnit;
	}
	/**
	 * @param contractUnit the contractUnit to set
	 */
	public void setContractUnit(String contractUnit) {
		this.contractUnit = contractUnit;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the project
	 */
	public EnterpriseProject getProject() {
		return project;
	}
	/**
	 * @param project the project to set
	 */
	public void setProject(EnterpriseProject project) {
		this.project = project;
	}
	/**
	 * @return the collects
	 */
	public Set getCollects() {
		return collects;
	}
	/**
	 * @param collects the collects to set
	 */
	public void setCollects(Set collects) {
		this.collects = collects;
	}
}