package com.yuanluesoft.appraise.model;

/**
 * 评议结果
 * @author linchuan
 *
 */
public class AppraiseResult {
	private long unitId; //单位ID
	private String unitName; //单位名称
	private String category; //单位分类
	private double recipientScore; //服务对象评议得分
	private double expertScore; //专家评议得分
	private double secondaryScore; //二级单位得分
	private double subordinateScore; //下级单位得分
	private double deduct; //扣分
	private double score; //得分
	
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the deduct
	 */
	public double getDeduct() {
		return deduct;
	}
	/**
	 * @param deduct the deduct to set
	 */
	public void setDeduct(double deduct) {
		this.deduct = deduct;
	}
	/**
	 * @return the expertScore
	 */
	public double getExpertScore() {
		return expertScore;
	}
	/**
	 * @param expertScore the expertScore to set
	 */
	public void setExpertScore(double expertScore) {
		this.expertScore = expertScore;
	}
	/**
	 * @return the recipientScore
	 */
	public double getRecipientScore() {
		return recipientScore;
	}
	/**
	 * @param recipientScore the recipientScore to set
	 */
	public void setRecipientScore(double recipientScore) {
		this.recipientScore = recipientScore;
	}
	/**
	 * @return the score
	 */
	public double getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(double score) {
		this.score = score;
	}
	/**
	 * @return the secondaryScore
	 */
	public double getSecondaryScore() {
		return secondaryScore;
	}
	/**
	 * @param secondaryScore the secondaryScore to set
	 */
	public void setSecondaryScore(double secondaryScore) {
		this.secondaryScore = secondaryScore;
	}
	/**
	 * @return the subordinateScore
	 */
	public double getSubordinateScore() {
		return subordinateScore;
	}
	/**
	 * @param subordinateScore the subordinateScore to set
	 */
	public void setSubordinateScore(double subordinateScore) {
		this.subordinateScore = subordinateScore;
	}
	/**
	 * @return the unitId
	 */
	public long getUnitId() {
		return unitId;
	}
	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}
	/**
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}
	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
}