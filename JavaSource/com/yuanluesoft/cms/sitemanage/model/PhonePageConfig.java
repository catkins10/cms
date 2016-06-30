package com.yuanluesoft.cms.sitemanage.model;

import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class PhonePageConfig {
	private String systemName; //系统名称
	private int screenWidth; //屏幕宽度
	private int screenHeight; //屏幕高度
	private List recommendedThemes; //推荐的主题
	private List otherThemes; //其他备选主题
	
	/**
	 * @return the otherThemes
	 */
	public List getOtherThemes() {
		return otherThemes;
	}
	/**
	 * @param otherThemes the otherThemes to set
	 */
	public void setOtherThemes(List otherThemes) {
		this.otherThemes = otherThemes;
	}
	/**
	 * @return the screenWidth
	 */
	public int getScreenWidth() {
		return screenWidth;
	}
	/**
	 * @param screenWidth the screenWidth to set
	 */
	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}
	/**
	 * @return the systemName
	 */
	public String getSystemName() {
		return systemName;
	}
	/**
	 * @param systemName the systemName to set
	 */
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	/**
	 * @return the recommendedThemes
	 */
	public List getRecommendedThemes() {
		return recommendedThemes;
	}
	/**
	 * @param recommendedThemes the recommendedThemes to set
	 */
	public void setRecommendedThemes(List recommendedThemes) {
		this.recommendedThemes = recommendedThemes;
	}
	/**
	 * @return the screenHeight
	 */
	public int getScreenHeight() {
		return screenHeight;
	}
	/**
	 * @param screenHeight the screenHeight to set
	 */
	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}
}