package com.yuanluesoft.bidding.enterprise.forms.admin;

import com.yuanluesoft.bidding.enterprise.pojo.BiddingEmployee;

/**
 * 
 * @author linchuan
 *
 */
public class Employee extends Enterprise {
	private BiddingEmployee employee = new BiddingEmployee();

	/**
	 * @return the employee
	 */
	public BiddingEmployee getEmployee() {
		return employee;
	}

	/**
	 * @param employee the employee to set
	 */
	public void setEmployee(BiddingEmployee employee) {
		this.employee = employee;
	}
}