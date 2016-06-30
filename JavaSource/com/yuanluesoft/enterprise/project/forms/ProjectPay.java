package com.yuanluesoft.enterprise.project.forms;

import com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectPay;

/**
 * 
 * @author linchuan
 *
 */
public class ProjectPay extends Project {
	private EnterpriseProjectPay pay = new EnterpriseProjectPay();
	private String openerTabPage;

	/**
	 * @return the pay
	 */
	public EnterpriseProjectPay getPay() {
		return pay;
	}

	/**
	 * @param pay the pay to set
	 */
	public void setPay(EnterpriseProjectPay pay) {
		this.pay = pay;
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
}