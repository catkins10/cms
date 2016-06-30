package com.yuanluesoft.cms.supervision.forms.admin;

import com.yuanluesoft.cms.publicservice.forms.PublicServiceAdminForm;

/**
 * 
 * @author linchuan
 *
 */
public class Supervision extends PublicServiceAdminForm {
	private String unit; //被监督机构
	//漳州检察院
	private String source; //来源
	private String otherSupervision; //其他举报人
	private String unitCategory; //被监督单位主体类别
	private String mainSuspected; //主要涉嫌性质
	private String secondarySuspected; //次要涉嫌性质

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * @return the mainSuspected
	 */
	public String getMainSuspected() {
		return mainSuspected;
	}

	/**
	 * @param mainSuspected the mainSuspected to set
	 */
	public void setMainSuspected(String mainSuspected) {
		this.mainSuspected = mainSuspected;
	}

	/**
	 * @return the otherSupervision
	 */
	public String getOtherSupervision() {
		return otherSupervision;
	}

	/**
	 * @param otherSupervision the otherSupervision to set
	 */
	public void setOtherSupervision(String otherSupervision) {
		this.otherSupervision = otherSupervision;
	}

	/**
	 * @return the secondarySuspected
	 */
	public String getSecondarySuspected() {
		return secondarySuspected;
	}

	/**
	 * @param secondarySuspected the secondarySuspected to set
	 */
	public void setSecondarySuspected(String secondarySuspected) {
		this.secondarySuspected = secondarySuspected;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the unitCategory
	 */
	public String getUnitCategory() {
		return unitCategory;
	}

	/**
	 * @param unitCategory the unitCategory to set
	 */
	public void setUnitCategory(String unitCategory) {
		this.unitCategory = unitCategory;
	}

}