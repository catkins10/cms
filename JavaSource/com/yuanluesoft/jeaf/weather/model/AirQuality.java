package com.yuanluesoft.jeaf.weather.model;

/**
 * 空气质量
 * @author chuan
 *
 */
public class AirQuality {
	private double qualityValue; //空气质量数值
	private String level; //空气质量等级
	private String describe; //空气质量说明
	private String outdoor; //户外活动说明
	private String colorText; //颜色
	private String color; //颜色值
	
	public AirQuality(double qualityValue, String level, String describe, String outdoor, String colorText, String color) {
		super();
		this.qualityValue = qualityValue;
		this.level = level;
		this.describe = describe;
		this.outdoor = outdoor;
		this.colorText = colorText;
		this.color = color;
	}
	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}
	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}
	/**
	 * @return the colorText
	 */
	public String getColorText() {
		return colorText;
	}
	/**
	 * @param colorText the colorText to set
	 */
	public void setColorText(String colorText) {
		this.colorText = colorText;
	}
	/**
	 * @return the describe
	 */
	public String getDescribe() {
		return describe;
	}
	/**
	 * @param describe the describe to set
	 */
	public void setDescribe(String describe) {
		this.describe = describe;
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
	/**
	 * @return the outdoor
	 */
	public String getOutdoor() {
		return outdoor;
	}
	/**
	 * @param outdoor the outdoor to set
	 */
	public void setOutdoor(String outdoor) {
		this.outdoor = outdoor;
	}
	/**
	 * @return the qualityValue
	 */
	public double getQualityValue() {
		return qualityValue;
	}
	/**
	 * @param qualityValue the qualityValue to set
	 */
	public void setQualityValue(double qualityValue) {
		this.qualityValue = qualityValue;
	}
}