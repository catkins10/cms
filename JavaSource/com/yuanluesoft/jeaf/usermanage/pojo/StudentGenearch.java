/*
 * Created on 2007-4-11
 *
 */
package com.yuanluesoft.jeaf.usermanage.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * @author Administrator
 *
 *
 */
public class StudentGenearch extends Record {
	private long studentId;
	private long genearchId;
	private String relation;
	
	private Person genearch; //家长
	
	/**
	 * @return Returns the genearchId.
	 */
	public long getGenearchId() {
		return genearchId;
	}
	/**
	 * @param genearchId The genearchId to set.
	 */
	public void setGenearchId(long genearchId) {
		this.genearchId = genearchId;
	}
	/**
	 * @return Returns the relation.
	 */
	public String getRelation() {
		return relation;
	}
	/**
	 * @param relation The relation to set.
	 */
	public void setRelation(String relation) {
		this.relation = relation;
	}
	/**
	 * @return Returns the studentId.
	 */
	public long getStudentId() {
		return studentId;
	}
	/**
	 * @param studentId The studentId to set.
	 */
	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}
	/**
	 * @return the genearch
	 */
	public Person getGenearch() {
		return genearch;
	}
	/**
	 * @param genearch the genearch to set
	 */
	public void setGenearch(Person genearch) {
		this.genearch = genearch;
	}
}
