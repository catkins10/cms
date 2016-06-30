package com.yuanluesoft.sportsevents.contestschedule.forms;

import com.yuanluesoft.jeaf.form.ActionForm;
/**
 * 体育赛事：成绩分数
 * @author kangshiwei
 *
 */
public class Score extends ActionForm {

	private	String player;//参赛代表团
	private	String gameName;//比赛项目名称
	private	long gameId;//比赛项目ID
	private	double score;//分数
	private int goldMedalCount;//金牌数
	private int silverMedalCount;//银牌数
	private int bronzeMedalCount;//铜牌数
	
	/**
	 * @return bronzeMedalCount
	 */
	public int getBronzeMedalCount() {
		return bronzeMedalCount;
	}
	/**
	 * @param bronzeMedalCount 要设置的 bronzeMedalCount
	 */
	public void setBronzeMedalCount(int bronzeMedalCount) {
		this.bronzeMedalCount = bronzeMedalCount;
	}
	/**
	 * @return gameId
	 */
	public long getGameId() {
		return gameId;
	}
	/**
	 * @param gameId 要设置的 gameId
	 */
	public void setGameId(long gameId) {
		this.gameId = gameId;
	}
	/**
	 * @return gameName
	 */
	public String getGameName() {
		return gameName;
	}
	/**
	 * @param gameName 要设置的 gameName
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	/**
	 * @return goldMedalCount
	 */
	public int getGoldMedalCount() {
		return goldMedalCount;
	}
	/**
	 * @param goldMedalCount 要设置的 goldMedalCount
	 */
	public void setGoldMedalCount(int goldMedalCount) {
		this.goldMedalCount = goldMedalCount;
	}
	/**
	 * @return player
	 */
	public String getPlayer() {
		return player;
	}
	/**
	 * @param player 要设置的 player
	 */
	public void setPlayer(String player) {
		this.player = player;
	}
	/**
	 * @return score
	 */
	public double getScore() {
		return score;
	}
	/**
	 * @param score 要设置的 score
	 */
	public void setScore(double score) {
		this.score = score;
	}
	/**
	 * @return silverMedalCount
	 */
	public int getSilverMedalCount() {
		return silverMedalCount;
	}
	/**
	 * @param silverMedalCount 要设置的 silverMedalCount
	 */
	public void setSilverMedalCount(int silverMedalCount) {
		this.silverMedalCount = silverMedalCount;
	}
	

}
