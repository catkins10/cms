package com.yuanluesoft.j2oa.personnel.forms;

import com.yuanluesoft.j2oa.personnel.pojo.PersonnelDutyChange;


/**
 * 
 * @author linchuan
 *
 */
public class DutyChange extends Employee {
	private PersonnelDutyChange dutyChange = new PersonnelDutyChange();

	/**
	 * @return the dutyChange
	 */
	public PersonnelDutyChange getDutyChange() {
		return dutyChange;
	}

	/**
	 * @param dutyChange the dutyChange to set
	 */
	public void setDutyChange(PersonnelDutyChange dutyChange) {
		this.dutyChange = dutyChange;
	}
}