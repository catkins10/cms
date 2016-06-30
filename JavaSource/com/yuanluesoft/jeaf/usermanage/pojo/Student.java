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
public class Student extends Person {
	private int seatNumber;
	private Set genearches; //家长列表
	
	/**
	 * @return Returns the seatNumber.
	 */
	public int getSeatNumber() {
		return seatNumber;
	}
	/**
	 * @param seatNumber The seatNumber to set.
	 */
	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}
	/**
	 * @return the genearchs
	 */
	public Set getGenearches() {
		return genearches;
	}
	/**
	 * @param genearchs the genearchs to set
	 */
	public void setGenearches(Set genearches) {
		this.genearches = genearches;
	}
}
