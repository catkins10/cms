package com.yuanluesoft.jeaf.usermanage.model;

import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class StudyStage {
	private String stageName;
	private List curriculums; //对应的科目列表
	private List grages; //对应的年级列表
	
	/**
	 * @return the curriculums
	 */
	public List getCurriculums() {
		return curriculums;
	}
	/**
	 * @param curriculums the curriculums to set
	 */
	public void setCurriculums(List curriculums) {
		this.curriculums = curriculums;
	}
	/**
	 * @return the grages
	 */
	public List getGrages() {
		return grages;
	}
	/**
	 * @param grages the grages to set
	 */
	public void setGrages(List grages) {
		this.grages = grages;
	}
	/**
	 * @return the stageName
	 */
	public String getStageName() {
		return stageName;
	}
	/**
	 * @param stageName the stageName to set
	 */
	public void setStageName(String stageName) {
		this.stageName = stageName;
	}
	
}
