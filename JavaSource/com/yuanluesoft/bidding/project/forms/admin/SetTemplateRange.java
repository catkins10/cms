package com.yuanluesoft.bidding.project.forms.admin;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class SetTemplateRange extends ActionForm {
	private String[] categoryArray; //适用的工程分类列表,系统预设，如建安、市政
	private String[] procedureArray; //适用的招标内容列表,系统预设，如施工、监理、设计
	private String[] cityArray; //适用的地区列表
	private String[] biddingModeArray; //适用的招标方式
	
	/**
	 * @return the biddingModeArray
	 */
	public String[] getBiddingModeArray() {
		return biddingModeArray;
	}
	/**
	 * @param biddingModeArray the biddingModeArray to set
	 */
	public void setBiddingModeArray(String[] biddingModeArray) {
		this.biddingModeArray = biddingModeArray;
	}
	/**
	 * @return the categoryArray
	 */
	public String[] getCategoryArray() {
		return categoryArray;
	}
	/**
	 * @param categoryArray the categoryArray to set
	 */
	public void setCategoryArray(String[] categoryArray) {
		this.categoryArray = categoryArray;
	}
	/**
	 * @return the cityArray
	 */
	public String[] getCityArray() {
		return cityArray;
	}
	/**
	 * @param cityArray the cityArray to set
	 */
	public void setCityArray(String[] cityArray) {
		this.cityArray = cityArray;
	}
	/**
	 * @return the procedureArray
	 */
	public String[] getProcedureArray() {
		return procedureArray;
	}
	/**
	 * @param procedureArray the procedureArray to set
	 */
	public void setProcedureArray(String[] procedureArray) {
		this.procedureArray = procedureArray;
	}
}