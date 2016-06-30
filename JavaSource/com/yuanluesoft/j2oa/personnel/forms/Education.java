package com.yuanluesoft.j2oa.personnel.forms;

import com.yuanluesoft.j2oa.personnel.pojo.PersonnelEducation;


/**
 * 
 * @author linchuan
 *
 */
public class Education extends Employee {
	private PersonnelEducation personnelEducation = new PersonnelEducation();

	/**
	 * @return the personnelEducation
	 */
	public PersonnelEducation getPersonnelEducation() {
		return personnelEducation;
	}

	/**
	 * @param personnelEducation the personnelEducation to set
	 */
	public void setPersonnelEducation(PersonnelEducation personnelEducation) {
		this.personnelEducation = personnelEducation;
	}
}