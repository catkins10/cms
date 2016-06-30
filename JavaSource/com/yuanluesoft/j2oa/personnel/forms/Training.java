package com.yuanluesoft.j2oa.personnel.forms;

import com.yuanluesoft.j2oa.personnel.pojo.PersonnelTraining;


/**
 * 
 * @author linchuan
 *
 */
public class Training extends Employee {
	private PersonnelTraining training = new PersonnelTraining();

	/**
	 * @return the training
	 */
	public PersonnelTraining getTraining() {
		return training;
	}

	/**
	 * @param training the training to set
	 */
	public void setTraining(PersonnelTraining training) {
		this.training = training;
	}
}