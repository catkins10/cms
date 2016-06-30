package com.yuanluesoft.chd.evaluation.forms.admin;

import java.util.Set;

import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationDirectory;

/**
 * 
 * @author linchuan
 *
 */
public class Rule extends DirectoryForm {
	private int score; //标准分
	private String provision; //评分规定
	private char isIndicator = '0'; //是否指标评价
	private Set scores; //各等级对应的分数列表
	
	//表单属性
	private ChdEvaluationDirectory parentDirectory; //父目录
	
	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}
	/**
	 * @return the scores
	 */
	public Set getScores() {
		return scores;
	}
	/**
	 * @param scores the scores to set
	 */
	public void setScores(Set scores) {
		this.scores = scores;
	}
	/**
	 * @return the provision
	 */
	public String getProvision() {
		return provision;
	}
	/**
	 * @param provision the provision to set
	 */
	public void setProvision(String provision) {
		this.provision = provision;
	}
	/**
	 * @return the isIndicator
	 */
	public char getIsIndicator() {
		return isIndicator;
	}
	/**
	 * @param isIndicator the isIndicator to set
	 */
	public void setIsIndicator(char isIndicator) {
		this.isIndicator = isIndicator;
	}
	/**
	 * @return the parentDirectory
	 */
	public ChdEvaluationDirectory getParentDirectory() {
		return parentDirectory;
	}
	/**
	 * @param parentDirectory the parentDirectory to set
	 */
	public void setParentDirectory(ChdEvaluationDirectory parentDirectory) {
		this.parentDirectory = parentDirectory;
	}
}