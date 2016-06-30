/*
 * Created on 2007-4-11
 *
 */
package com.yuanluesoft.jeaf.usermanage.forms.admin;


/**
 * @author Administrator
 *
 *
 */
public class Unit extends Org {
	private String fullName; //单位全称
	private String unitCode; //单位编码
	private String responsibility; //主要职责
	private String address; //单位地址
	private String postcode; //邮政编码
	private String linkman; //联系人
	private String tel; //联系电话
	private String way; //交通线路
	private String webSite; //网站
	private String email; //EMAIL
	private String subordinateUnitIds; //下级单位ID
	private String subordinateUnitNames; //下级单位名称
	private String secondaryUnitIds; //二级单位ID,如：公路局是交通局的二级单位
	private String secondaryUnitNames; //二级单位名称
	
	/**
	 * @return Returns the fullName.
	 */
	public String getFullName() {
		return fullName;
	}
	/**
	 * @param fullName The fullName to set.
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the linkman
	 */
	public String getLinkman() {
		return linkman;
	}
	/**
	 * @param linkman the linkman to set
	 */
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	/**
	 * @return the postcode
	 */
	public String getPostcode() {
		return postcode;
	}
	/**
	 * @param postcode the postcode to set
	 */
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	/**
	 * @return the responsibility
	 */
	public String getResponsibility() {
		return responsibility;
	}
	/**
	 * @param responsibility the responsibility to set
	 */
	public void setResponsibility(String responsibility) {
		this.responsibility = responsibility;
	}
	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}
	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}
	/**
	 * @return the unitCode
	 */
	public String getUnitCode() {
		return unitCode;
	}
	/**
	 * @param unitCode the unitCode to set
	 */
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	/**
	 * @return the way
	 */
	public String getWay() {
		return way;
	}
	/**
	 * @param way the way to set
	 */
	public void setWay(String way) {
		this.way = way;
	}
	/**
	 * @return the webSite
	 */
	public String getWebSite() {
		return webSite;
	}
	/**
	 * @param webSite the webSite to set
	 */
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}
	/**
	 * @return the secondaryUnitIds
	 */
	public String getSecondaryUnitIds() {
		return secondaryUnitIds;
	}
	/**
	 * @param secondaryUnitIds the secondaryUnitIds to set
	 */
	public void setSecondaryUnitIds(String secondaryUnitIds) {
		this.secondaryUnitIds = secondaryUnitIds;
	}
	/**
	 * @return the secondaryUnitNames
	 */
	public String getSecondaryUnitNames() {
		return secondaryUnitNames;
	}
	/**
	 * @param secondaryUnitNames the secondaryUnitNames to set
	 */
	public void setSecondaryUnitNames(String secondaryUnitNames) {
		this.secondaryUnitNames = secondaryUnitNames;
	}
	/**
	 * @return the subordinateUnitIds
	 */
	public String getSubordinateUnitIds() {
		return subordinateUnitIds;
	}
	/**
	 * @param subordinateUnitIds the subordinateUnitIds to set
	 */
	public void setSubordinateUnitIds(String subordinateUnitIds) {
		this.subordinateUnitIds = subordinateUnitIds;
	}
	/**
	 * @return the subordinateUnitNames
	 */
	public String getSubordinateUnitNames() {
		return subordinateUnitNames;
	}
	/**
	 * @param subordinateUnitNames the subordinateUnitNames to set
	 */
	public void setSubordinateUnitNames(String subordinateUnitNames) {
		this.subordinateUnitNames = subordinateUnitNames;
	}
}
