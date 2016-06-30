package com.yuanluesoft.appraise.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 评分标准(appraise_mark_standard)
 * @author linchuan
 *
 */
public class AppraiseMarkStandard extends Record {
	private String areaIds; //适用的地区ID
	private String areaNames; //适用的地区名称
	private String categories; //适用的单位类别
	private double recipientRatio; //服务对象评议所占比例
	private double expertRatio; //专家评议所占比例
	private double secondaryRatio; //二级单位所占比例
	private double subordinateRatio; //下级单位所占比例
	private double deductLimit; //扣分上限
	
	/**
	 * @return the areaIds
	 */
	public String getAreaIds() {
		return areaIds;
	}
	/**
	 * @param areaIds the areaIds to set
	 */
	public void setAreaIds(String areaIds) {
		this.areaIds = areaIds;
	}
	/**
	 * @return the areaNames
	 */
	public String getAreaNames() {
		return areaNames;
	}
	/**
	 * @param areaNames the areaNames to set
	 */
	public void setAreaNames(String areaNames) {
		this.areaNames = areaNames;
	}
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
	 * @return the deductLimit
	 */
	public double getDeductLimit() {
		return deductLimit;
	}
	/**
	 * @param deductLimit the deductLimit to set
	 */
	public void setDeductLimit(double deductLimit) {
		this.deductLimit = deductLimit;
	}
	/**
	 * @return the expertRatio
	 */
	public double getExpertRatio() {
		return expertRatio;
	}
	/**
	 * @param expertRatio the expertRatio to set
	 */
	public void setExpertRatio(double expertRatio) {
		this.expertRatio = expertRatio;
	}
	/**
	 * @return the recipientRatio
	 */
	public double getRecipientRatio() {
		return recipientRatio;
	}
	/**
	 * @param recipientRatio the recipientRatio to set
	 */
	public void setRecipientRatio(double recipientRatio) {
		this.recipientRatio = recipientRatio;
	}
	/**
	 * @return the secondaryRatio
	 */
	public double getSecondaryRatio() {
		return secondaryRatio;
	}
	/**
	 * @param secondaryRatio the secondaryRatio to set
	 */
	public void setSecondaryRatio(double secondaryRatio) {
		this.secondaryRatio = secondaryRatio;
	}
	/**
	 * @return the subordinateRatio
	 */
	public double getSubordinateRatio() {
		return subordinateRatio;
	}
	/**
	 * @param subordinateRatio the subordinateRatio to set
	 */
	public void setSubordinateRatio(double subordinateRatio) {
		this.subordinateRatio = subordinateRatio;
	}
}