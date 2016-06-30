package com.yuanluesoft.dpc.keyproject.pojo;

import java.sql.Date;

/**
 * 问题反映及措施建议(keyproject_problem)
 * @author linchuan
 *
 */
public class KeyProjectProblem extends KeyProjectComponent {
	private int problemYear; //年份
	private int problemMonth; //月份
	private String problem; //问题描述
	private String responsible; //责任单位或责任人
	private Date timeLimit; //解决时限
	private char completed = '0'; //是否已提交解决情况
	private String result; //解决情况
	
	/**
	 * @return the problem
	 */
	public String getProblem() {
		return problem;
	}
	/**
	 * @param problem the problem to set
	 */
	public void setProblem(String problem) {
		this.problem = problem;
	}
	/**
	 * @return the problemMonth
	 */
	public int getProblemMonth() {
		return problemMonth;
	}
	/**
	 * @param problemMonth the problemMonth to set
	 */
	public void setProblemMonth(int problemMonth) {
		this.problemMonth = problemMonth;
	}
	/**
	 * @return the problemYear
	 */
	public int getProblemYear() {
		return problemYear;
	}
	/**
	 * @param problemYear the problemYear to set
	 */
	public void setProblemYear(int problemYear) {
		this.problemYear = problemYear;
	}
	/**
	 * @return the responsible
	 */
	public String getResponsible() {
		return responsible;
	}
	/**
	 * @param responsible the responsible to set
	 */
	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}
	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * @return the timeLimit
	 */
	public Date getTimeLimit() {
		return timeLimit;
	}
	/**
	 * @param timeLimit the timeLimit to set
	 */
	public void setTimeLimit(Date timeLimit) {
		this.timeLimit = timeLimit;
	}
	/**
	 * @return the completed
	 */
	public char getCompleted() {
		return completed;
	}
	/**
	 * @param completed the completed to set
	 */
	public void setCompleted(char completed) {
		this.completed = completed;
	}
}