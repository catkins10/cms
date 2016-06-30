package com.yuanluesoft.enterprise.exam.model;

/**
 * 考试统计
 * @author linchuan
 *
 */
public class ExamStat {
	private String name; //姓名
	private String unit; //单位
	private String department; //部门
	private String post; //岗位
	private String examName; //考试名称
	private int times; //考试次数
	private double totalScore; //总分
	private String status; //考试状态:考试批改完成/考试待批改
	
	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}
	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}
	/**
	 * @return the examName
	 */
	public String getExamName() {
		return examName;
	}
	/**
	 * @param examName the examName to set
	 */
	public void setExamName(String examName) {
		this.examName = examName;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the post
	 */
	public String getPost() {
		return post;
	}
	/**
	 * @param post the post to set
	 */
	public void setPost(String post) {
		this.post = post;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the times
	 */
	public int getTimes() {
		return times;
	}
	/**
	 * @param times the times to set
	 */
	public void setTimes(int times) {
		this.times = times;
	}
	/**
	 * @return the totalScore
	 */
	public double getTotalScore() {
		return totalScore;
	}
	/**
	 * @param totalScore the totalScore to set
	 */
	public void setTotalScore(double totalScore) {
		this.totalScore = totalScore;
	}
	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
}