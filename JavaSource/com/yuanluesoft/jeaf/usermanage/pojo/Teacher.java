/*
 * Created on 2007-4-14
 *
 */
package com.yuanluesoft.jeaf.usermanage.pojo;

import java.util.Set;

/**
 * 
 * @author linchuan
 *
 */
public class Teacher extends Person {
	private Set classes; //所带的班级

	/**
	 * @return the classes
	 */
	public Set getClasses() {
		return classes;
	}

	/**
	 * @param classes the classes to set
	 */
	public void setClasses(Set classes) {
		this.classes = classes;
	}
	
}
