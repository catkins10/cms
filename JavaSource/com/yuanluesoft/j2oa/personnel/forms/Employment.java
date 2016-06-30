package com.yuanluesoft.j2oa.personnel.forms;

import com.yuanluesoft.j2oa.personnel.pojo.PersonnelEmployment;


/**
 * 
 * @author linchuan
 *
 */
public class Employment extends Employee {
	private PersonnelEmployment employment = new PersonnelEmployment();

	/**
	 * @return the employment
	 */
	public PersonnelEmployment getEmployment() {
		return employment;
	}

	/**
	 * @param employment the employment to set
	 */
	public void setEmployment(PersonnelEmployment employment) {
		this.employment = employment;
	}
}