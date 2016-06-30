package com.yuanluesoft.enterprise.project.forms;

import com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectContract;

/**
 * 
 * @author linchuan
 *
 */
public class ProjectContract extends Project {
	private EnterpriseProjectContract contract = new EnterpriseProjectContract(); //合同
	private String selectedContractTemplateName; //选中的合同模板名称
	private long selectedContractTemplateId; //选中的合同模板ID
	private boolean contractCreated; //合同是否已经创建
	private String openerTabPage;
	
	/**
	 * @return the contract
	 */
	public EnterpriseProjectContract getContract() {
		return contract;
	}
	/**
	 * @param contract the contract to set
	 */
	public void setContract(EnterpriseProjectContract contract) {
		this.contract = contract;
	}
	/**
	 * @return the selectedContractTemplateId
	 */
	public long getSelectedContractTemplateId() {
		return selectedContractTemplateId;
	}
	/**
	 * @param selectedContractTemplateId the selectedContractTemplateId to set
	 */
	public void setSelectedContractTemplateId(long selectedContractTemplateId) {
		this.selectedContractTemplateId = selectedContractTemplateId;
	}
	/**
	 * @return the selectedContractTemplateName
	 */
	public String getSelectedContractTemplateName() {
		return selectedContractTemplateName;
	}
	/**
	 * @param selectedContractTemplateName the selectedContractTemplateName to set
	 */
	public void setSelectedContractTemplateName(String selectedContractTemplateName) {
		this.selectedContractTemplateName = selectedContractTemplateName;
	}
	/**
	 * @return the openerTabPage
	 */
	public String getOpenerTabPage() {
		return openerTabPage;
	}
	/**
	 * @param openerTabPage the openerTabPage to set
	 */
	public void setOpenerTabPage(String openerTabPage) {
		this.openerTabPage = openerTabPage;
	}
	/**
	 * @return the contractCreated
	 */
	public boolean isContractCreated() {
		return contractCreated;
	}
	/**
	 * @param contractCreated the contractCreated to set
	 */
	public void setContractCreated(boolean contractCreated) {
		this.contractCreated = contractCreated;
	}
}