package com.yuanluesoft.railway.evaluation.forms.admin;

import com.yuanluesoft.jeaf.application.forms.ApplicationView;

/**
 * 
 * @author linchuan
 *
 */
public class EvaluationView extends ApplicationView {
	private String orgIds; //部门ID
	private String orgNames; //部门名称
	private String postIds; //岗位ID
	private String postNames; //岗位名称
	private int year; //年度
	private int month; //月份
	
	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}
	/**
	 * @param month the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
	}
	/**
	 * @return the orgIds
	 */
	public String getOrgIds() {
		return orgIds;
	}
	/**
	 * @param orgIds the orgIds to set
	 */
	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}
	/**
	 * @return the orgNames
	 */
	public String getOrgNames() {
		return orgNames;
	}
	/**
	 * @param orgNames the orgNames to set
	 */
	public void setOrgNames(String orgNames) {
		this.orgNames = orgNames;
	}
	/**
	 * @return the postIds
	 */
	public String getPostIds() {
		return postIds;
	}
	/**
	 * @param postIds the postIds to set
	 */
	public void setPostIds(String postIds) {
		this.postIds = postIds;
	}
	/**
	 * @return the postNames
	 */
	public String getPostNames() {
		return postNames;
	}
	/**
	 * @param postNames the postNames to set
	 */
	public void setPostNames(String postNames) {
		this.postNames = postNames;
	}
	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
}