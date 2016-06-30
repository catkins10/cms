package com.yuanluesoft.telex.base.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class TelexConfigure extends ActionForm {
	private String securityLevels; //电报密级列表
	private String levels; //电报级别列表
	private String categories; //电报分类列表
	private int currentSendSn; //当前电报发送顺序号
	private int currentCrypticSendSn; //当前电报加密发送顺序号
	private int currentReceiveSn; //当前电报接收顺序号
	private int currentCrypticReceiveSn; //当前电报加密接收顺序号
	
	/**
	 * @return the categories
	 */
	public String getCategories() {
		return categories;
	}
	/**
	 * @param categories the categories to set
	 */
	public void setCategories(String categories) {
		this.categories = categories;
	}
	/**
	 * @return the levels
	 */
	public String getLevels() {
		return levels;
	}
	/**
	 * @param levels the levels to set
	 */
	public void setLevels(String levels) {
		this.levels = levels;
	}
	/**
	 * @return the currentCrypticReceiveSn
	 */
	public int getCurrentCrypticReceiveSn() {
		return currentCrypticReceiveSn;
	}
	/**
	 * @param currentCrypticReceiveSn the currentCrypticReceiveSn to set
	 */
	public void setCurrentCrypticReceiveSn(int currentCrypticReceiveSn) {
		this.currentCrypticReceiveSn = currentCrypticReceiveSn;
	}
	/**
	 * @return the currentCrypticSendSn
	 */
	public int getCurrentCrypticSendSn() {
		return currentCrypticSendSn;
	}
	/**
	 * @param currentCrypticSendSn the currentCrypticSendSn to set
	 */
	public void setCurrentCrypticSendSn(int currentCrypticSendSn) {
		this.currentCrypticSendSn = currentCrypticSendSn;
	}
	/**
	 * @return the currentReceiveSn
	 */
	public int getCurrentReceiveSn() {
		return currentReceiveSn;
	}
	/**
	 * @param currentReceiveSn the currentReceiveSn to set
	 */
	public void setCurrentReceiveSn(int currentReceiveSn) {
		this.currentReceiveSn = currentReceiveSn;
	}
	/**
	 * @return the currentSendSn
	 */
	public int getCurrentSendSn() {
		return currentSendSn;
	}
	/**
	 * @param currentSendSn the currentSendSn to set
	 */
	public void setCurrentSendSn(int currentSendSn) {
		this.currentSendSn = currentSendSn;
	}
	/**
	 * @return the securityLevels
	 */
	public String getSecurityLevels() {
		return securityLevels;
	}
	/**
	 * @param securityLevels the securityLevels to set
	 */
	public void setSecurityLevels(String securityLevels) {
		this.securityLevels = securityLevels;
	}
}