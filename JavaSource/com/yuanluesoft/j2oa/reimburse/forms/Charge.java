package com.yuanluesoft.j2oa.reimburse.forms;

import com.yuanluesoft.j2oa.reimburse.pojo.ReimburseCharge;

/**
 * 
 * @author linchuan
 *
 */
public class Charge extends ReimburseForm {
	private ReimburseCharge charge = new ReimburseCharge();

	/**
	 * @return the charge
	 */
	public ReimburseCharge getCharge() {
		return charge;
	}

	/**
	 * @param charge the charge to set
	 */
	public void setCharge(ReimburseCharge charge) {
		this.charge = charge;
	}
}