package com.yuanluesoft.enterprise.exam.forms.admin;

import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class ExamTestReport extends ActionForm {
	private long examPaperId; //考卷ID
	private List tested; //已考人员(ExamTest)
	private List notTestPersons; //未考人员(Peron)
	
	/**
	 * @return the examPaperId
	 */
	public long getExamPaperId() {
		return examPaperId;
	}
	/**
	 * @param examPaperId the examPaperId to set
	 */
	public void setExamPaperId(long examPaperId) {
		this.examPaperId = examPaperId;
	}
	/**
	 * @return the notTestPersons
	 */
	public List getNotTestPersons() {
		return notTestPersons;
	}
	/**
	 * @param notTestPersons the notTestPersons to set
	 */
	public void setNotTestPersons(List notTestPersons) {
		this.notTestPersons = notTestPersons;
	}
	/**
	 * @return the tested
	 */
	public List getTested() {
		return tested;
	}
	/**
	 * @param tested the tested to set
	 */
	public void setTested(List tested) {
		this.tested = tested;
	}
}