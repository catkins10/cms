package com.yuanluesoft.enterprise.project.forms;

import com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectCollect;

/**
 * 
 * @author linchuan
 *
 */
public class ProjectCollect extends Project {
	private EnterpriseProjectCollect collect = new EnterpriseProjectCollect();
	private String openerTabPage;
	private String contractName; //合同名称
	
	/**
	 * @return the collect
	 */
	public EnterpriseProjectCollect getCollect() {
		return collect;
	}

	/**
	 * @param collect the collect to set
	 */
	public void setCollect(EnterpriseProjectCollect collect) {
		this.collect = collect;
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
}