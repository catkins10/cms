package com.yuanluesoft.enterprise.exam.question.model;

/**
 * 难度等级
 * @author linchuan
 *
 */
public class DifficultyLevel {
	private String level; //难度等级
	private int difficulty; //难度
	
	/**
	 * @return the difficulty
	 */
	public int getDifficulty() {
		return difficulty;
	}
	/**
	 * @param difficulty the difficulty to set
	 */
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}
}