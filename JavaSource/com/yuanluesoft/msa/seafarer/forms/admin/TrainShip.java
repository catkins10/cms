package com.yuanluesoft.msa.seafarer.forms.admin;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class TrainShip extends ActionForm {
	private String name; //名称
	private String port; //船籍港
	private String category; //船舶种类
	private String tonnage; //总吨位
	private String power; //功率
	private String shipBelong; //船舶所有人
	private String practiceOrg; //船上培训/见习单位
	private String orgBelong; //单位所属海事机构
	private String remark; //备注
	private long importId; //导入记录ID
	
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
	 * @return the importId
	 */
	public long getImportId() {
		return importId;
	}
	/**
	 * @param importId the importId to set
	 */
	public void setImportId(long importId) {
		this.importId = importId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the orgBelong
	 */
	public String getOrgBelong() {
		return orgBelong;
	}
	/**
	 * @param orgBelong the orgBelong to set
	 */
	public void setOrgBelong(String orgBelong) {
		this.orgBelong = orgBelong;
	}
	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}
	/**
	 * @return the power
	 */
	public String getPower() {
		return power;
	}
	/**
	 * @param power the power to set
	 */
	public void setPower(String power) {
		this.power = power;
	}
	/**
	 * @return the practiceOrg
	 */
	public String getPracticeOrg() {
		return practiceOrg;
	}
	/**
	 * @param practiceOrg the practiceOrg to set
	 */
	public void setPracticeOrg(String practiceOrg) {
		this.practiceOrg = practiceOrg;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the shipBelong
	 */
	public String getShipBelong() {
		return shipBelong;
	}
	/**
	 * @param shipBelong the shipBelong to set
	 */
	public void setShipBelong(String shipBelong) {
		this.shipBelong = shipBelong;
	}
	/**
	 * @return the tonnage
	 */
	public String getTonnage() {
		return tonnage;
	}
	/**
	 * @param tonnage the tonnage to set
	 */
	public void setTonnage(String tonnage) {
		this.tonnage = tonnage;
	}
}